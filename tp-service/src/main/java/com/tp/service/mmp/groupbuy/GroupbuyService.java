package com.tp.service.mmp.groupbuy;

import com.alibaba.fastjson.JSON;
import com.tp.common.util.ImageUtil;
import com.tp.common.util.mmp.AssertUtil;
import com.tp.common.util.mmp.DataUtil;
import com.tp.common.vo.Constant;
import com.tp.common.vo.Constant.DEFAULTED;
import com.tp.common.vo.DAOConstant;
import com.tp.common.vo.PageInfo;
import com.tp.common.vo.StorageConstant;
import com.tp.common.vo.StorageConstant.App;
import com.tp.common.vo.mmp.AreaConstant;
import com.tp.common.vo.mmp.ProcessingErrorMessage;
import com.tp.dao.mmp.*;
import com.tp.dto.common.ResultInfo;
import com.tp.dto.mmp.TopicInventoryExchangeDTO;
import com.tp.dto.mmp.enums.*;
import com.tp.dto.mmp.enums.groupbuy.*;
import com.tp.dto.mmp.groupbuy.Groupbuy;
import com.tp.dto.mmp.groupbuy.GroupbuyDetail;
import com.tp.dto.mmp.groupbuy.GroupbuyGroupDTO;
import com.tp.dto.prd.InfoDetailDto;
import com.tp.dto.stg.InventoryDto;
import com.tp.dto.stg.query.SkuInventoryQuery;
import com.tp.enums.common.PlatformEnum;
import com.tp.exception.ServiceException;
import com.tp.model.mem.MemberInfo;
import com.tp.model.mmp.*;
import com.tp.model.prd.ItemPictures;
import com.tp.model.usr.UserInfo;
import com.tp.result.mem.app.ResultMessage;
import com.tp.service.mem.IMemberInfoService;
import com.tp.service.mmp.ITopicManagementService;
import com.tp.service.mmp.groupbuy.IGroupbuyService;
import com.tp.service.ord.remote.ICheckTopicRemoteService;
import com.tp.service.prd.IItemPicturesService;
import com.tp.service.prd.IItemService;
import com.tp.service.stg.IInventoryOperService;
import com.tp.service.stg.IInventoryQueryService;
import com.tp.util.BeanUtil;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.*;

/**
 * Created by ldr on 2016/3/11.
 */
@Service
public class GroupbuyService implements IGroupbuyService {

    @Autowired
    private TopicDao topicDao;

    @Autowired
    private TopicItemDao topicItemDao;

    @Autowired
    private PolicyInfoDao policyInfoDao;

    @Autowired
    private GroupbuyInfoDao groupbuyInfoDao;

    @Autowired
    private ITopicManagementService topicManagementService;

    @Autowired
    private IItemService itemService;

    @Autowired
    private GroupbuyGroupDao groupbuyGroupDao;

    @Autowired
    private GroupbuyJoinDao groupbuyJoinDao;

    @Autowired
    private IMemberInfoService memberInfoService;

    @Autowired
    private IInventoryOperService inventoryOperService;

    @Autowired
    private IInventoryQueryService inventoryQueryService;

    @Autowired
    private ICheckTopicRemoteService checkTopicRemoteService;

    @Autowired
    private IItemPicturesService itemPicturesService;

    private Logger logger = Logger.getLogger(this.getClass());

    @Override
    @Transactional
    public void save(Groupbuy groupbuy, UserInfo user) throws Exception {

        check(groupbuy);

        Date cur = new Date();

        PolicyInfo policyInfo = getPolicyInfo(cur);

        Topic topic = getTopic(groupbuy, user, cur, policyInfo);

        TopicItem topicItem = getTopicItem(groupbuy, user, cur, topic);
        TopicItem itemForInventory = (TopicItem)BeanUtils.cloneBean(topicItem);

        GroupbuyInfo groupbuyInfo = getGroupbuyInfo(groupbuy, user, cur, topic, topicItem);

        TopicItem topicItemBase = null;

        if (groupbuy.getTopicId() == null) {
            policyInfoDao.insert(policyInfo);

            topic.setLimitPolicyId(policyInfo.getId());
            topicDao.insert(topic);

            topicItem.setTopicId(topic.getId());
            topicItemDao.insert(topicItem);

            groupbuyInfo.setTopicId(topic.getId());
            groupbuyInfo.setTopicItemId(topicItem.getId());
            groupbuyInfoDao.insert(groupbuyInfo);

        } else {
            Topic topicBase = topicDao.queryById(groupbuy.getTopicId());
            AssertUtil.notNull(topicBase, "团购信息错误");

            policyInfo.setId(topicBase.getLimitPolicyId());
            policyInfo.setCreateTime(null);
            policyInfoDao.updateNotNullById(policyInfo);

            topic.setCreateTime(null);
            topic.setUpdateTime(null);
            //对审批通过的处理
            if (topicBase.getStatus() != null && topicBase.getStatus().equals(TopicStatus.PASSED.ordinal())) {
                topic.setProgress(topicBase.getProgress() == null ? ProgressStatus.NotStarted.ordinal() : topicBase.getProgress().equals(ProgressStatus.FINISHED.ordinal()) ? ProgressStatus.NotStarted.ordinal() : topicBase.getProgress());
                topic.setStatus(TopicStatus.PASSED.ordinal());
            }
            topicDao.updateNotNullById(topic);

            TopicItem queryItem = new TopicItem();

            queryItem.setTopicId(groupbuy.getTopicId());
            queryItem.setDeletion(DeletionStatus.NORMAL.ordinal());
            List<TopicItem> topicItemList = topicItemDao.queryByParam(BeanUtil.beanMap(queryItem));
            if (!CollectionUtils.isEmpty(topicItemList)) {
                topicItemBase = topicItemList.get(0);
                if (topicItem.getId() == null) {
                    topicItem.setId(topicItemBase.getId());
                }
                topicItem.setCreateTime(null);
                topicItem.setCreateUser(null);
                //审批通过的专场不可以修改商品信息
                if (topicBase.getStatus() != null && topicBase.getStatus().equals(TopicStatus.PASSED.ordinal())) {
                    topicItem.setSku(null);
                    topicItem.setBarCode(null);
                    topicItem.setSalePrice(null);
                    topicItem.setName(null);
                }
                topicItemDao.updateNotNullById(topicItem);
            } else {
                topicItem.setId(null);
                topicItemDao.insert(topicItem);
            }

            groupbuyInfo.setTopicId(topic.getId());
            groupbuyInfo.setTopicItemId(topicItem.getId());
            GroupbuyInfo groupbuyInfoQuery = new GroupbuyInfo();
            groupbuyInfoQuery.setTopicId(topic.getId());
            List<GroupbuyInfo> groupbuyInfoList = groupbuyInfoDao.queryByParam(BeanUtil.beanMap(groupbuyInfoQuery));
            if (CollectionUtils.isEmpty(groupbuyInfoList)) {
                groupbuyInfoDao.insert(groupbuyInfo);
            } else {
                groupbuyInfo.setId(groupbuyInfoList.get(0).getId());
                groupbuyInfo.setCreateUser(null);
                groupbuyInfo.setCreateTime(null);
                groupbuyInfoDao.updateNotNullById(groupbuyInfo);
            }

        }
        List<TopicInventoryExchangeDTO> inventoryExchangeDTOList = new ArrayList<>();
        List<TopicInventoryExchangeDTO> inventoryExchangeDTOListForDel = new ArrayList<>();

        processInventory(groupbuy, user, topic, itemForInventory, topicItemBase, inventoryExchangeDTOList, inventoryExchangeDTOListForDel);

        ResultMessage resultMessage = topicManagementService.checkAvailableStock(inventoryExchangeDTOList);
        if (null == resultMessage || ResultMessage.FAIL == resultMessage.getResult()) {
            logger.error(resultMessage.getMessage());
            if (StringUtils.isBlank(resultMessage.getMessage())) {
                throw new ServiceException(ProcessingErrorMessage.CHECK_STORAGE_INVENTORY_FAILD);
            } else {
                throw new ServiceException(resultMessage.getMessage());
            }
        }

        inventoryExchangeDTOListForDel.addAll(inventoryExchangeDTOList);
        ResultInfo resultInfo = topicManagementService.requestStorageAmount(inventoryExchangeDTOListForDel);
        if (!resultInfo.isSuccess()) {
            throw new ServiceException(resultInfo.getMsg().getMessage(), resultInfo.getMsg().getCode());
        }

    }


    @Override
    public PageInfo<Groupbuy> queryPageByObj(Groupbuy groupbuy) {

        if (groupbuy.getStartPage() == null) groupbuy.setStartPage(1);
        if (groupbuy.getPageSize() == null) groupbuy.setPageSize(10);
        Map<String, Object> param = BeanUtil.beanMap(groupbuy);
        param.put("start", groupbuy.getStart());
        param.put("pageSize", groupbuy.getPageSize());
        List<Topic> topicList = topicDao.queryTopicForGroupbuy(param);
        Integer count = topicDao.queryTopicForGroupbuyCount(param);
        if (topicList.isEmpty()) {
            return new PageInfo<>();
        }
        List<Long> topicIds = new ArrayList<>();
        for (Topic topic : topicList) {
            topicIds.add(topic.getId());
        }
        List<TopicItem> topicItemList = topicItemDao.getTopicItemByTopicIds(topicIds);

        List<GroupbuyInfo> groupbuyInfoList = groupbuyInfoDao.queryByTopicIds(topicIds);

        List<Groupbuy> groupbuyList = new ArrayList<>();
        for (Topic topic : topicList) {
            Groupbuy group = new Groupbuy();
            group.setTopicId(topic.getId());
            group.setName(topic.getName());
            group.setStartTime(topic.getStartTime());
            group.setEndTime(topic.getEndTime());
            group.setStatus(topic.getStatus());
            group.setProgress(topic.getProgress());
            getTopicItemInfo(topicItemList, topic, group);
            getGroupbuyInfo(groupbuyInfoList, topic, group);
            groupbuyList.add(group);
        }
        PageInfo<Groupbuy> page = new PageInfo<>(groupbuy.getStartPage(), groupbuy.getPageSize());
        page.setRows(groupbuyList);
        page.setRecords(count);
        page.setTotal((count % groupbuy.getPageSize()) > 0 ? (count / groupbuy.getPageSize() + 1) : (count / groupbuy.getPageSize()));

        return page;
    }

    @Override
    public Groupbuy getGroupbuyInfo(Long id) {
        if (id == null) return null;
        Topic topic = topicDao.queryById(id);
        if (topic == null) {
            throw new ServiceException("团购信息不存在");
        }
        TopicItem topicItemQuery = new TopicItem();
        topicItemQuery.setTopicId(id);
        topicItemQuery.setDeletion(DeletionStatus.NORMAL.ordinal());
        List<TopicItem> topicItemList = topicItemDao.queryByParam(BeanUtil.beanMap(topicItemQuery));
        TopicItem topicItem = null;
        if (!CollectionUtils.isEmpty(topicItemList)) {
            topicItem = topicItemList.get(0);
        }

        PolicyInfo policyInfo = policyInfoDao.queryById(topic.getLimitPolicyId());

        GroupbuyInfo groupbuyInfoQuery = new GroupbuyInfo();
        groupbuyInfoQuery.setTopicId(id);
        GroupbuyInfo groupbuyInfo = null;
        List<GroupbuyInfo> groupbuyInfoList = groupbuyInfoDao.queryByParam(BeanUtil.beanMap(groupbuyInfoQuery));
        if (!CollectionUtils.isEmpty(groupbuyInfoList)) {
            groupbuyInfo = groupbuyInfoList.get(0);
        }

        Groupbuy groupbuy = getGroupbuy(topic, topicItem, groupbuyInfo);
        return groupbuy;
    }

    @Override
    @Transactional
    public void updateGroupbuyStatus(Groupbuy groupbuy, UserInfo user) {
    	 AssertUtil.notNull(groupbuy, "参数为空");
         AssertUtil.notNull(groupbuy.getTopicId(), "TopicId为空");
         AssertUtil.notNull(groupbuy.getStatus(), "状态为空");

         Topic topic = new Topic();
         topic.setId(groupbuy.getTopicId());
         topic.setStatus(groupbuy.getStatus());
         topic.setUpdateTime(new Date());
         topic.setUpdateUser(user.getUserName());
         if(groupbuy.getStatus().equals(TopicStatus.STOP.ordinal())){ //终止后团购变为已结束
         	topic.setProgress(TopicProcess.ENDING.ordinal());
         }
         int count = topicDao.updateNotNullById(topic);
         if (count < 1) {
             throw new ServiceException("操作失败");
         }
         if (groupbuy.getStatus().equals(TopicStatus.CANCELED.ordinal()) || groupbuy.getStatus().equals(TopicStatus.STOP.ordinal())) {
             ResultInfo<Boolean> inventoryResult = inventoryOperService.backRequestInventory(StorageConstant.App.PROMOTION, String.valueOf(groupbuy.getTopicId()));
             System.out.println("BACKINVENTORY" + JSON.toJSONString(inventoryResult));
             if (!inventoryResult.isSuccess()) {
                 throw new ServiceException(inventoryResult.getMsg() == null ? "还库存错误" : inventoryResult.getMsg().getMessage() == null ? "还库存错误" : inventoryResult.getMsg().getMessage());
             }
         }
//         
//        AssertUtil.notNull(groupbuy, "参数为空");
//        AssertUtil.notNull(groupbuy.getTopicId(), "TopicId为空");
//        AssertUtil.notNull(groupbuy.getStatus(), "状态为空");
//
//        Topic topic = new Topic();
//        topic.setId(groupbuy.getTopicId());
//        topic.setStatus(groupbuy.getStatus());
//        topic.setUpdateTime(new Date());
//        topic.setUpdateUser(user.getUserName());
//        int count = topicDao.updateNotNullById(topic);
//        if (count < 1) {
//            throw new ServiceException("操作失败");
//        }
//        if (groupbuy.getStatus().equals(TopicStatus.CANCELED.ordinal()) || groupbuy.getStatus().equals(TopicStatus.STOP.ordinal())) {
//            ResultInfo<String> inventoryResult = inventoryOperService.backRequestInventory(StorageConstant.App.PROMOTION, String.valueOf(groupbuy.getTopicId()));
//            System.out.println("BACKINVENTORY" + JSON.toJSONString(inventoryResult));
//            if (!inventoryResult.isSuccess()) {
//                throw new ServiceException(inventoryResult.getMsg() == null ? "还库存错误" : inventoryResult.getMsg().getMessage() == null ? "还库存错误" : inventoryResult.getMsg().getMessage());
//            }
//        }
    }


    @Override
    public GroupbuyDetail getGroupbuyDetail(Long groupbuyId, Long groupId, Long memberId) {
        AssertUtil.notNull(groupbuyId, "参数错误");
        AssertUtil.notNull(memberId, "参数错误");
        GroupbuyInfo groupbuyInfo = groupbuyInfoDao.queryById(groupbuyId);
        AssertUtil.notNull(groupbuyInfo, "团购不存在");
        Topic topic = topicDao.queryById(groupbuyInfo.getTopicId());
        AssertUtil.notNull(topic, "团购不存在");
        TopicItem topicItemQuery = new TopicItem();
        topicItemQuery.setTopicId(topic.getId());
        topicItemQuery.setDeletion(DeletionStatus.NORMAL.ordinal());
        List<TopicItem> topicItemList = topicItemDao.queryByParam(BeanUtil.beanMap(topicItemQuery));
        AssertUtil.notEmpty(topicItemList, "团购不存在");
        String sku = topicItemList.get(0).getSku();
        TopicItem topicItem = topicItemList.get(0);
        InfoDetailDto item = itemService.queryItemSkuTopicInfoForAPPHaiTao(sku, topic.getId().toString());
        GroupbuyDetail groupbuyDTO = new GroupbuyDetail();
        if(item!=null && StringUtils.isNotBlank(groupbuyInfo.getIntroduce())){
            item.setSubTitle(groupbuyInfo.getIntroduce());
        }
        groupbuyDTO.setItem(item);
        groupbuyDTO.setDetail(topic.getIntroMobile());
        groupbuyDTO.setTopicId(topic.getId());

        groupbuyDTO.setPlanAmount(groupbuyInfo.getMemberLimit());
        groupbuyDTO.setGroupbuyId(groupbuyInfo.getId());
        groupbuyDTO.setGroupType(groupbuyInfo.getType());


        MemberInfo memberInfo = memberInfoService.queryById(memberId);
        AssertUtil.notNull(memberInfo, "用户信息错误");
        Date cur = new Date();

        if (groupId != null) {
            GroupbuyGroup groupbuyGroup = groupbuyGroupDao.queryById(groupId);
            AssertUtil.notNull(groupbuyGroup, "团购不存在");
            if (!topic.getId().equals(groupbuyGroup.getTopicId())) {
                throw new ServiceException("团购不存在");
            }
            AssertUtil.notNull(groupbuyGroup, "团购不存在");

            Date endTime = groupbuyGroup.getEndTime().after(topic.getEndTime()) ? topic.getEndTime() : groupbuyGroup.getEndTime();

            long leftsec = endTime.getTime() - cur.getTime();
            if (leftsec < 0) leftsec = 0;

            groupbuyDTO.setLeftSecond(leftsec / 1000);
            groupbuyDTO.setGroupId(groupbuyGroup.getId());
            groupbuyDTO.setFactAmount(groupbuyGroup.getFactAmount());

            GroupbuyJoin groupbuyJoinQuery = new GroupbuyJoin();
            groupbuyJoinQuery.setGroupId(groupId);
            groupbuyJoinQuery.setMemberId(memberId);
            Integer myCount = groupbuyJoinDao.queryByParamCount(BeanUtil.beanMap(groupbuyJoinQuery));
            if (groupbuyGroup.getStatus().equals(GroupbuyGroupStatus.SUCCESS.getCode())) {
                groupbuyDTO.setGroupStatus(GroupbuyGroupStatus.SUCCESS.getCode());
                if (myCount > 0) {

                    int boughtCountWithGroupId = checkTopicRemoteService.getBoughtCountWithGroupId(groupId, memberId);
                    if (boughtCountWithGroupId > 0) {
                        groupbuyDTO.setPayStatus(GroupbuyGroupBuyStatus.BOUGHT.getCode());
                    } else {
                        int boughtTotalCount = checkTopicRemoteService.getBoughtQuantityForGroup(topic.getId(), topicItem.getSku(), memberId);
                        if (boughtTotalCount >= topicItem.getLimitAmount()) {
                            groupbuyDTO.setPayStatus(GroupbuyGroupBuyStatus.ACHIEVE_LIMIT.getCode());
                        } else if (cur.after(topic.getEndTime())) {
                            groupbuyDTO.setPayStatus(GroupbuyGroupBuyStatus.TIMEOUT.getCode());
                        } else {
                            groupbuyDTO.setPayStatus(GroupbuyGroupBuyStatus.CAN_BUY.getCode());
                        }
                    }
                } else {
                    groupbuyDTO.setPayStatus(GroupbuyGroupBuyStatus.NO_BUY.getCode());
                }

            } else if (groupbuyGroup.getStatus().equals(GroupbuyGroupStatus.PROCESSING.getCode())) {
                if (cur.after(endTime)) {
                    groupbuyDTO.setGroupStatus(GroupbuyGroupStatus.FAILED.getCode());
                } else {
                    groupbuyDTO.setGroupStatus(GroupbuyGroupStatus.PROCESSING.getCode());
                }
                if (myCount > 0) {
                    groupbuyDTO.setJoinStatus(GroupbuyGroupJoinStatus.JOINED.getCode());
                } else {
                    if (groupbuyInfo.getType().equals(GroupbuyType.NEW_ONLY.getCode())) {
                        if (memberInfo.getCreateTime().before(groupbuyInfo.getCreateTime())) {
                            groupbuyDTO.setJoinStatus(GroupbuyGroupJoinStatus.NEW_ONLY_JOIN.getCode());
                        } else {
                            groupbuyDTO.setJoinStatus(GroupbuyGroupJoinStatus.CAN_JOIN.getCode());
                        }
                    } else {
                        groupbuyDTO.setJoinStatus(GroupbuyGroupJoinStatus.CAN_JOIN.getCode());
                    }
                }
            } else {
                groupbuyDTO.setGroupStatus(GroupbuyGroupStatus.FAILED.getCode());
            }
        }
        Integer count = groupbuyGroupDao.queryProcessingGroupCount(topic.getId(), memberId);
        if (count == null || count.intValue() < 10) {
            groupbuyDTO.setLaunchStatus(GroupbuyGroupLaunchStatus.CAN_LAUNCH.getCode());
        } else {
            groupbuyDTO.setLaunchStatus(GroupbuyGroupLaunchStatus.LAUNCHED.getCode());
        }
        if (cur.after(topic.getEndTime()) || !topic.getStatus().equals(TopicStatus.PASSED.ordinal())) {
            groupbuyDTO.setTopicStats(TopicStatus.STOP.ordinal());
        } else if (cur.before(topic.getStartTime())) {
            groupbuyDTO.setTopicStats(TopicStatus.AUDITING.ordinal());
        } else {
            groupbuyDTO.setTopicStats(TopicStatus.PASSED.ordinal());
        }
        return groupbuyDTO;
    }

    @Override
    @Transactional
    public GroupbuyDetail launch(Long groupbuyId, Long memberId) {

        GroupbuyInfo groupbuyInfo = groupbuyInfoDao.queryById(groupbuyId);

        checkParam(groupbuyId, memberId);
        Date cur = new Date();
        Topic topic = topicDao.queryById(groupbuyInfo.getTopicId());
        checkTopic(cur, topic);

        TopicItem topicItemQuery = new TopicItem();
        topicItemQuery.setTopicId(topic.getId());
        topicItemQuery.setDeletion(DeletionStatus.NORMAL.ordinal());
        List<TopicItem> topicItemList = topicItemDao.queryByParam(BeanUtil.beanMap(topicItemQuery));
        if (CollectionUtils.isEmpty(topicItemList)) {
            throw new ServiceException("团购不存在");
        }

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(cur);
        calendar.add(Calendar.HOUR_OF_DAY, groupbuyInfo.getDuration());
        Date endDate = calendar.getTime();

        MemberInfo memberInfo = memberInfoService.queryById(memberId);
        AssertUtil.notNull(memberInfo, "用户信息错误");

        GroupbuyGroup groupbuyGroupQuery = new GroupbuyGroup();
        groupbuyGroupQuery.setTopicId(topic.getId());
        groupbuyGroupQuery.setMemberId(memberId);
        Integer count = groupbuyGroupDao.queryProcessingGroupCount(topic.getId(), memberId);
        if (count != null && count.intValue() > 10) {
            throw new ServiceException("每个用户最多可发起10个进行中的团购活动");
        }

        GroupbuyGroup groupbuyGroup = addGroupbuyGroup(groupbuyId, memberId, groupbuyInfo, cur, topic, endDate, memberInfo);

        addGroupbuyJoin(memberId, cur, topic, memberInfo, groupbuyGroup);

        return this.getGroupbuyDetail(groupbuyId, groupbuyGroup.getId(), memberId);
    }


    @Override
    @Transactional
    public GroupbuyDetail join(Long groupbuyId, Long groupId, Long memberId) {

        checkParam(groupbuyId, memberId);
        AssertUtil.notNull(groupId, "参数错误");

        GroupbuyInfo groupbuyInfo = groupbuyInfoDao.queryById(groupbuyId);
        AssertUtil.notNull(groupbuyInfo, "团购不存在");

        Date cur = new Date();
        Topic topic = topicDao.queryById(groupbuyInfo.getTopicId());
        checkTopic(cur, topic);

        GroupbuyGroup groupbuyGroup = groupbuyGroupDao.queryById(groupId);
        AssertUtil.notNull(groupbuyGroup, "团购信息错误");
        Date endTime = groupbuyGroup.getEndTime().after(topic.getEndTime()) ? topic.getEndTime() : groupbuyGroup.getEndTime();
        if (cur.after(endTime)) {
            throw new ServiceException("该团已失效");
        }

        MemberInfo memberInfo = memberInfoService.queryById(memberId);
        AssertUtil.notNull(memberInfo, "用户信息错误");

        GroupbuyJoin groupbuyJoinQuery = new GroupbuyJoin();
        groupbuyJoinQuery.setGroupId(groupId);
        groupbuyJoinQuery.setMemberId(memberId);
        Integer myJoinCount = groupbuyJoinDao.queryByParamCount(BeanUtil.beanMap(groupbuyJoinQuery));
        if (myJoinCount > 0) {
            throw new ServiceException("您已参加该团");
        }
        if (groupbuyGroup.getFactAmount().intValue() >= groupbuyInfo.getMemberLimit().intValue()) {
            throw new ServiceException("该团已满,请参与其他的团,或开团");
        }
        if (groupbuyInfo.getType().equals(GroupbuyType.NEW_ONLY.getCode())) {
            if (memberInfo.getCreateTime().before(groupbuyInfo.getCreateTime())) {
                throw new ServiceException("该团购仅限新人参加");
            }
        }
        // 更新团购人数
        updateFactAmount(groupbuyGroup, groupbuyInfo.getMemberLimit().intValue(), 2);

        addGroupbuyJoin(groupId, memberId, cur, topic, groupbuyGroup, memberInfo);

        // groupbuySendMsgService.sendMsg(groupbuyGroup.getId());
        return this.getGroupbuyDetail(groupbuyId, groupId, memberId);
    }


    @Override
    public List<GroupbuyGroupDTO> myLaunch(Long memberId) {
        if (memberId == null) return Collections.emptyList();
        GroupbuyGroup groupbuyGroupQuery = new GroupbuyGroup();
        groupbuyGroupQuery.setMemberId(memberId);
        Map<String, Object> param = BeanUtil.beanMap(groupbuyGroupQuery);
        param.put(DAOConstant.MYBATIS_SPECIAL_STRING.ORDER_BY.name(), " create_time desc");
        List<GroupbuyGroup> groupbuyGroupList = groupbuyGroupDao.queryByParam(param);

        List<GroupbuyGroupDTO> groupbuyGroupDTOList = getGroupbuyGroupDTOs(groupbuyGroupList);
        processItemPic(groupbuyGroupDTOList);
        return groupbuyGroupDTOList;
    }

    @Override
    public List<GroupbuyGroupDTO> myJoin(Long memberId) {
        if (memberId == null) return Collections.emptyList();
        List<Long> groupIds = groupbuyJoinDao.myJoin(memberId);
        if (CollectionUtils.isEmpty(groupIds)) return Collections.emptyList();
        List<GroupbuyGroup> groupbuyGroupList = groupbuyGroupDao.queryByIds(groupIds);

        List<GroupbuyGroupDTO> groupbuyGroupDTOList = getGroupbuyGroupDTOs(groupbuyGroupList);
        processItemPic(groupbuyGroupDTOList);
        return groupbuyGroupDTOList;


    }


    @Override
    public Map<String, Object> getInventoryInfo(Long topicId, String sku, Long wareHouseId) {
        SkuInventoryQuery query = new SkuInventoryQuery();
        query.setApp(App.PROMOTION);
        query.setBizId(topicId.toString());
        query.setSku(sku);
        query.setWarehouseId(wareHouseId);
        query.setBizPreOccupy(false);
        Integer salableInventory = inventoryQueryService.querySalableInventory(query);
        Map<String, Object> info = new HashMap<>();
        info.put("left", salableInventory);
        List<InventoryDto> inventoryDtoList = inventoryQueryService.queryAvailableInventory(sku, wareHouseId);
        if (!CollectionUtils.isEmpty(inventoryDtoList)) {
            info.put("inventory", inventoryDtoList.get(0).getInventory());
        }
        return info;
//        
//        int left = inventoryQueryService.selectInvetory(StorageConstant.App.PROMOTION, String.valueOf(topicId), sku);
//        Map<String, Object> info = new HashMap<>();
//        info.put("left", left);
//        List<InventoryDto> inventoryDtoList = inventoryQueryService.selectAvailableForSaleBySkuAndWhId(sku, wareHouseId);
//        if (!CollectionUtils.isEmpty(inventoryDtoList)) {
//            info.put("inventory", inventoryDtoList.get(0).getInventory());
//        }
//        return info;
    }

    @Override
    public void addInventory(Long topicId, String sku, Long wareHouseId, Long supplierId, Integer inventory, Long userId) {
        AssertUtil.notNull(topicId, "topicId为空");
        AssertUtil.notEmpty(sku, "sku为空");
        AssertUtil.notNull(wareHouseId, "wareHouseId为空");
        AssertUtil.notNull(inventory, "inventory为空");
        AssertUtil.notNull(supplierId, "supplierId为空");
        if (inventory.intValue() < 0) {
            throw new ServiceException("库存不能小于0");
        } else if (inventory == 0) {
            return;
        }

        Topic topic = topicDao.queryById(topicId);
        TopicItem topicItemQuery = new TopicItem();
        topicItemQuery.setTopicId(topicId);
        topicItemQuery.setSku(sku);
        topicItemQuery.setDeletion(DeletionStatus.NORMAL.ordinal());
        List<TopicItem> topicItemList = topicItemDao.queryByParam(BeanUtil.beanMap(topicItemQuery));
        AssertUtil.notEmpty(topicItemList, "团购信息错误");
        TopicItem topicItem = topicItemList.get(0);
        AssertUtil.notNull(topic, "团购信息为空");
        TopicItem forUpdate = new TopicItem();
        forUpdate.setId(topicItem.getId());
        forUpdate.setUpdateTime(new Date());
        forUpdate.setUpdateUser(String.valueOf(userId));
        forUpdate.setLimitTotal(topicItem.getLimitTotal() + inventory);
        topicItemDao.updateNotNullById(forUpdate);

        //ResultInfo<String> resultInfo = inventoryOperService.requestInventory(StorageConstant.App.PROMOTION, topicId.toString(), sku, inventory, supplierId, wareHouseId);
        ResultInfo<Boolean> resultInfo = inventoryOperService.requestInventory(StorageConstant.App.PROMOTION, topicId.toString(), sku, inventory, supplierId, wareHouseId, true);
        if (!resultInfo.isSuccess()) {
            logger.error("GROUPBUY_MODIFY_INVENTORY_ERROR,RESULT=" + JSON.toJSONString(resultInfo));
            throw new ServiceException(resultInfo.getMsg() == null ? "申请库存失败" : resultInfo.getMsg().getMessage() == null ? "申请库存失败" : resultInfo.getMsg().getMessage());
        }

    }


    private void addGroupbuyJoin(Long groupId, Long memberId, Date cur, Topic topic, GroupbuyGroup groupbuyGroup, MemberInfo memberInfo) {
        GroupbuyJoin groupbuyJoin = new GroupbuyJoin();
        groupbuyJoin.setTopicId(topic.getId());
        groupbuyJoin.setTopicName(topic.getName());
        groupbuyJoin.setGroupId(groupId);
        groupbuyJoin.setGroupCode(groupbuyGroup.getCode());
        groupbuyJoin.setMemberId(memberId);
        groupbuyJoin.setMemberName(memberInfo.getMobile());
        groupbuyJoin.setLeader(0);
        groupbuyJoin.setCreateTime(cur);
        groupbuyJoin.setCreateUser(memberInfo.getNickName());
        groupbuyJoin.setUpdateTime(cur);
        groupbuyJoin.setUpdateUser(memberInfo.getNickName());
        groupbuyJoinDao.insert(groupbuyJoin);
    }

    private void addGroupbuyJoin(Long memberId, Date cur, Topic topic, MemberInfo memberInfo, GroupbuyGroup groupbuyGroup) {
        GroupbuyJoin groupbuyJoin = new GroupbuyJoin();
        groupbuyJoin.setTopicId(topic.getId());
        groupbuyJoin.setTopicName(topic.getName());
        groupbuyJoin.setCreateTime(cur);
        groupbuyJoin.setCreateUser(StringUtils.isBlank(memberInfo.getMobile()) ? memberInfo.getNickName() : memberInfo.getMobile());
        groupbuyJoin.setUpdateTime(cur);
        groupbuyJoin.setUpdateUser(StringUtils.isBlank(memberInfo.getMobile()) ? memberInfo.getNickName() : memberInfo.getMobile());
        groupbuyJoin.setGroupId(groupbuyGroup.getId());
        groupbuyJoin.setGroupCode(groupbuyGroup.getCode());
        groupbuyJoin.setMemberId(memberId);
        groupbuyJoin.setMemberName(StringUtils.isBlank(memberInfo.getMobile()) ? memberInfo.getNickName() : memberInfo.getMobile());
        groupbuyJoin.setLeader(1);
        groupbuyJoinDao.insert(groupbuyJoin);
    }

    private GroupbuyGroup addGroupbuyGroup(Long groupbuyId, Long memberId, GroupbuyInfo groupbuyInfo, Date cur, Topic topic, Date endDate, MemberInfo memberInfo) {
        GroupbuyGroup groupbuyGroup = new GroupbuyGroup();
        groupbuyGroup.setGroupbuyId(groupbuyId);
        groupbuyGroup.setTopicId(topic.getId());
        groupbuyGroup.setTopicName(topic.getName());
        groupbuyGroup.setCreateTime(cur);
        groupbuyGroup.setCreateUser(memberInfo.getNickName());
        groupbuyGroup.setUpdateTime(cur);
        groupbuyGroup.setUpdateUser(memberInfo.getNickName());
        groupbuyGroup.setCode(DataUtil.radomCode());
        groupbuyGroup.setStatus(GroupbuyGroupStatus.PROCESSING.getCode());
        groupbuyGroup.setCreateUser(memberInfo.getMobile());
        groupbuyGroup.setStartTime(cur);
        groupbuyGroup.setEndTime(endDate);
        groupbuyGroup.setMemberId(memberId);
        groupbuyGroup.setMemberName(StringUtils.isBlank(memberInfo.getMobile()) ? memberInfo.getNickName() : memberInfo.getMobile());
        groupbuyGroup.setPlanAmount(groupbuyInfo.getMemberLimit());
        groupbuyGroup.setFactAmount(1);
        groupbuyGroup.setTopicEndTime(topic.getEndTime());
        groupbuyGroupDao.insert(groupbuyGroup);
        return groupbuyGroup;
    }


    private void check(Groupbuy groupbuy) {
        AssertUtil.notNull(groupbuy, "参数错误");
        AssertUtil.notEmpty(groupbuy.getName(), "活动名称为空");
        AssertUtil.notNull(groupbuy.getType(), "活动类型为空");
        AssertUtil.notNull(groupbuy.getMemberLimit(), "成团人数为空");
        AssertUtil.notNull(groupbuy.getDuration(), "持续时间为空");
        AssertUtil.notNull(groupbuy.getStartTime(), "活动开始时间为空");
        AssertUtil.notNull(groupbuy.getEndTime(), "活动能够结束时间为空");
        AssertUtil.notNull(groupbuy.getSku(), "商品sku为空");
        AssertUtil.notNull(groupbuy.getItemId(), "商品Id为空");
        AssertUtil.notNull(groupbuy.getItemName(), "商品名称为空");
        AssertUtil.notNull(groupbuy.getBarcode(), "商品条码为空");
        AssertUtil.notNull(groupbuy.getWarehouseId(), "仓库为空");
        AssertUtil.notNull(groupbuy.getApplyInventory(), "申请库存为空");
        AssertUtil.notNull(groupbuy.getGroupPrice(), "团购价为空");
        AssertUtil.notNull(groupbuy.getDetail(), "团购详情为空");
        if (groupbuy.getEndTime().before(groupbuy.getStartTime())) {
            throw new ServiceException("结束时间必须在开始时间之前");
        }
        if (groupbuy.getDuration() < 1) {
            throw new ServiceException("持续时间不能小于1");
        }
        if (groupbuy.getMemberLimit() < 2) {
            throw new ServiceException("成团人数不能小于2");
        }
        if (groupbuy.getApplyInventory() < 1) {
            throw new ServiceException("申请库存数量不能小于1");
        }
        if (groupbuy.getGroupPrice() <= 0) {
            throw new ServiceException("团购价必须大于0");
        }

        if (groupbuy.getLimitAmount() == null || groupbuy.getLimitAmount() < 1) groupbuy.setLimitAmount(1);
    }

    private void processInventory(Groupbuy groupbuy, UserInfo user, Topic topic, TopicItem topicItem, TopicItem topicItemBase, List<TopicInventoryExchangeDTO> inventoryExchangeDTOList, List<TopicInventoryExchangeDTO> inventoryExchangeDTOListForDel) {

        if (topicItemBase == null) {
            TopicInventoryExchangeDTO topicInventoryExchange = new TopicInventoryExchangeDTO();
            topicInventoryExchange.setAmount(topicItem.getLimitTotal());
            topicInventoryExchange.setSku(topicItem.getSku());
            topicInventoryExchange.setStatus(OperStatus.NEW.ordinal());
            topicInventoryExchange.setSupplierId(topicItem.getSupplierId());
            topicInventoryExchange.setWarehouseId(topicItem.getStockLocationId());
            topicInventoryExchange.setTopicId(topic.getId());
            topicInventoryExchange.setTopicItemId(topicItem.getId());
            topicInventoryExchange.setOperatorId(user.getId());
            topicInventoryExchange.setOperatorName(user.getUserName());
            topicInventoryExchange.setOperType(InventoryOperType.NEW);
            topicInventoryExchange.setBizType(InnerBizType.TOPIC);
            //活动是否预占库存：0否1是（5.5）
            topicInventoryExchange.setTopicInventoryFlag(topicItem.getReserveInventoryFlag());
            inventoryExchangeDTOList.add(topicInventoryExchange);
            return;
        }

        if (topicItemBase.getSku().equals(topicItem.getSku()) && topicItemBase.getSupplierId().equals(topicItem.getSupplierId()) && topicItemBase.getStockLocationId().equals(topicItem.getStockLocationId())) {
            int balance = topicItem.getLimitTotal() - topicItemBase.getLimitTotal();
            if (balance != 0) {
                TopicInventoryExchangeDTO topicInventoryExchange = new TopicInventoryExchangeDTO();
                topicInventoryExchange.setAmount(balance);
                topicInventoryExchange.setSku(groupbuy.getSku());
                topicInventoryExchange.setStatus(OperStatus.MODIFY.ordinal());
                topicInventoryExchange.setSupplierId(groupbuy.getSupplierId());
                topicInventoryExchange.setWarehouseId(groupbuy.getWarehouseId());
                topicInventoryExchange.setTopicId(topic.getId());
                topicInventoryExchange.setTopicItemId(topicItem.getId());
                topicInventoryExchange.setOperatorId(user.getId());
                topicInventoryExchange.setOperatorName(user.getUserName());
                topicInventoryExchange.setOperType(InventoryOperType.EDIT);
                topicInventoryExchange.setBizType(InnerBizType.TOPIC);
                //活动是否预占库存：0否1是（5.5）
                topicInventoryExchange.setTopicInventoryFlag(topicItem.getReserveInventoryFlag());
                inventoryExchangeDTOList.add(topicInventoryExchange);
            }
        } else {
            TopicInventoryExchangeDTO topicInventoryExchangeForDel = new TopicInventoryExchangeDTO();
            topicInventoryExchangeForDel.setAmount(topicItemBase.getLimitTotal());
            topicInventoryExchangeForDel.setSku(topicItemBase.getSku());
            topicInventoryExchangeForDel.setStatus(OperStatus.DELETE.ordinal());
            topicInventoryExchangeForDel.setSupplierId(topicItemBase.getSupplierId());
            topicInventoryExchangeForDel.setWarehouseId(topicItemBase.getStockLocationId());
            topicInventoryExchangeForDel.setTopicId(topic.getId());
            topicInventoryExchangeForDel.setTopicItemId(topicItemBase.getId());
            topicInventoryExchangeForDel.setOperatorId(user.getId());
            topicInventoryExchangeForDel.setOperatorName(user.getUserName());
            topicInventoryExchangeForDel.setOperType(InventoryOperType.DELETE);
            topicInventoryExchangeForDel.setBizType(InnerBizType.TOPIC);
          //活动是否预占库存：0否1是（5.5）
            topicInventoryExchangeForDel.setTopicInventoryFlag(topicItem.getReserveInventoryFlag());
            inventoryExchangeDTOListForDel.add(topicInventoryExchangeForDel);

            TopicInventoryExchangeDTO topicInventoryExchangeForAdd = new TopicInventoryExchangeDTO();
            topicInventoryExchangeForAdd.setAmount(topicItem.getLimitTotal());
            topicInventoryExchangeForAdd.setSku(topicItem.getSku() == null ? topicItemBase.getSku() : topicItem.getSku());
            topicInventoryExchangeForAdd.setStatus(OperStatus.NEW.ordinal());
            topicInventoryExchangeForAdd.setSupplierId(topicItem.getSupplierId());
            topicInventoryExchangeForAdd.setWarehouseId(topicItem.getStockLocationId());
            topicInventoryExchangeForAdd.setTopicId(topic.getId());
            topicInventoryExchangeForAdd.setTopicItemId(topicItem.getId());
            topicInventoryExchangeForAdd.setOperatorId(user.getId());
            topicInventoryExchangeForAdd.setOperatorName(user.getUserName());
            topicInventoryExchangeForAdd.setOperType(InventoryOperType.NEW);
            topicInventoryExchangeForAdd.setBizType(InnerBizType.TOPIC);
            //活动是否预占库存：0否1是（5.5）
            topicInventoryExchangeForDel.setTopicInventoryFlag(topicItem.getReserveInventoryFlag());
            inventoryExchangeDTOList.add(topicInventoryExchangeForAdd);

        }
    }

    private GroupbuyInfo getGroupbuyInfo(Groupbuy groupbuy, UserInfo user, Date cur, Topic topic, TopicItem topicItem) {
        GroupbuyInfo groupbuyInfo = new GroupbuyInfo();
        groupbuyInfo.setCreateTime(cur);
        groupbuyInfo.setUpdateTime(cur);
        groupbuyInfo.setCreateUser(user.getUserName());
        groupbuyInfo.setUpdateUser(user.getUserName());
        groupbuyInfo.setGroupPrice(groupbuy.getGroupPrice());
        groupbuyInfo.setMemberLimit(groupbuy.getMemberLimit());
        groupbuyInfo.setTopicId(topic.getId());
        groupbuyInfo.setTopicItemId(topicItem.getId());
        groupbuyInfo.setDuration(groupbuy.getDuration());
        groupbuyInfo.setType(groupbuy.getType());
        groupbuyInfo.setSort(groupbuy.getSort());
        groupbuyInfo.setIntroduce(groupbuy.getIntroduce());
        return groupbuyInfo;
    }

    private TopicItem getTopicItem(Groupbuy groupbuy, UserInfo user, Date cur, Topic topic) {
        TopicItem topicItem = new TopicItem();
        topicItem.setId(groupbuy.getTopicItemId());
        topicItem.setIsTest(0);
        topicItem.setSalePrice(groupbuy.getSalePrice());
        topicItem.setBarCode(groupbuy.getBarcode());
        topicItem.setName(groupbuy.getItemName());
        topicItem.setItemId(groupbuy.getItemId());
        topicItem.setTopicImage(groupbuy.getItemPic());
        topicItem.setSku(groupbuy.getSku());
        topicItem.setSpu(groupbuy.getSpu());
        topicItem.setBrandId(groupbuy.getBrandId());
        topicItem.setCategoryId(groupbuy.getCategoryId());
        topicItem.setTopicPrice(groupbuy.getGroupPrice());

        topicItem.setCountryId(groupbuy.getCountryId());
        topicItem.setCountryName(groupbuy.getCountryName());

        topicItem.setSupplierId(groupbuy.getSupplierId());
        topicItem.setSupplierName(groupbuy.getSupplierName());

        topicItem.setStockLocationId(groupbuy.getWarehouseId());
        topicItem.setPutSign(groupbuy.getPutSign());
        topicItem.setStockLocation(groupbuy.getWarehouseName());

        topicItem.setBondedArea(groupbuy.getBondedArea());
        topicItem.setWhType(groupbuy.getWhtype());

        topicItem.setLimitTotal(groupbuy.getApplyInventory());
        topicItem.setLimitAmount(groupbuy.getLimitAmount());

        topicItem.setStockAmount(groupbuy.getApplyInventory());

        topicItem.setTopicId(topic.getId());

        topicItem.setIsHot(0);
        topicItem.setItemColor("");
        topicItem.setItemStatus(0);
        topicItem.setItemSize("");
        topicItem.setItemSpec("");
        topicItem.setItemTags("");
        topicItem.setDeletion(DeletionStatus.NORMAL.ordinal());
        topicItem.setLargeCateoryId(0L);
        topicItem.setMiddleCategoryId(0L);
        topicItem.setPrdid("");
        topicItem.setStock(1);
        topicItem.setSaledAmount(0);
        topicItem.setRemark("");
        topicItem.setPictureSize(0);
        topicItem.setInputSource(InputSource.MANUAL.ordinal());
        topicItem.setLockStatus(LockStatus.UNLOCK.ordinal());
        topicItem.setApplyAgeId(0L);
        topicItem.setListingTime(cur);
        topicItem.setDetailId(0L);
        topicItem.setHotTitle("");

        topicItem.setSortIndex(0);
        topicItem.setQuotationId(0L);

        topicItem.setCreateUser(user.getUserName());
        topicItem.setUpdateUser(user.getUserName());
        topicItem.setCreateTime(cur);
        topicItem.setUpdateTime(cur);
        //预占库存：0否1是（团购默认预占库存）
        topicItem.setReserveInventoryFlag(DEFAULTED.YES);
        return topicItem;
    }

    private PolicyInfo getPolicyInfo(Date cur) {
        PolicyInfo policyInfo = new PolicyInfo();
        policyInfo.setByIp(0);
        policyInfo.setByMobile(0);
        policyInfo.setByRegisterTime(0);
        policyInfo.setByUid(1);
        policyInfo.setByTopic(0);
        policyInfo.setUpdateTime(cur);
        policyInfo.setCreateTime(cur);
        return policyInfo;
    }

    private Topic getTopic(Groupbuy groupbuy, UserInfo user, Date cur, PolicyInfo policyInfo) {
        Topic topic = new Topic();
        topic.setId(groupbuy.getTopicId());
        topic.setName(groupbuy.getName());
        topic.setStartTime(groupbuy.getStartTime());
        topic.setEndTime(groupbuy.getEndTime());
        topic.setType(TopicType.SINGLE.ordinal());
        topic.setAreaStr(String.valueOf(AreaConstant.AREA_ALL));
        topic.setPlatformStr(String.valueOf(PlatformEnum.ALL.getCode()));
        topic.setLimitPolicyId(policyInfo.getId());
        topic.setIntroMobile(groupbuy.getDetail());

        topic.setCreateUser(user.getUserName());
        topic.setUpdateUser(user.getUserName());
        topic.setCreateTime(cur);
        topic.setUpdateTime(cur);
        topic.setLastingType(LastingType.SOMETIME.ordinal());
        topic.setSalesPartten(SalesPartten.GROUP_BUY.getValue());
        topic.setProgress(ProgressStatus.NotStarted.ordinal());
        topic.setIsSupportSupplier(0);
        topic.setDeletion(DeletionStatus.NORMAL.ordinal());
        topic.setSortIndex(0);
        topic.setAgeEndKey(0);
        topic.setAgeStartKey(0);
        topic.setBrandId(null);
        topic.setDiscount("");
        topic.setBrandName("");
        topic.setImage("");
        topic.setImageMobile("");
        topic.setImageHitao("");
        topic.setImageInterested("");
        topic.setImageNew("");
        topic.setHaitaoImage("");
        topic.setMallImage("");
        topic.setMobileImage("");
        topic.setMallImage("");
        topic.setPcImage("");
        topic.setPcInterestImage("");
        topic.setFreightTemplet(0);
        topic.setIntro(groupbuy.getDetail());
        topic.setIsTest(0);
        topic.setNumber("");
        topic.setStatus(groupbuy.getStatus());
        topic.setPcIndex(0L);
        topic.setAndroidIndex(0L);
        topic.setIosIndex(0L);
        topic.setWapIndex(0L);
        topic.setWxIndex(0L);
        //预占库存:0否1是（团购默认预占库存）
        topic.setReserveInventoryFlag(com.tp.common.vo.Constant.DEFAULTED.YES);
        return topic;
    }


    private void getGroupbuyInfo(List<GroupbuyInfo> groupbuyInfoList, Topic topic, Groupbuy group) {
        for (GroupbuyInfo groupbuyInfo : groupbuyInfoList) {
            if (groupbuyInfo.getTopicId().equals(topic.getId())) {
                group.setGroupbuyId(groupbuyInfo.getId());
                group.setSort(groupbuyInfo.getSort());
                return;
            }
        }
    }


    private void getTopicItemInfo(List<TopicItem> topicItemList, Topic topic, Groupbuy group) {
        for (TopicItem topicItem : topicItemList) {
            if (topicItem.getTopicId().equals(topic.getId())) {
                group.setSku(topicItem.getSku());
                group.setBarcode(topicItem.getBarCode());
                group.setItemName(topicItem.getName());
                group.setTopicItemId(topicItem.getId());
                return;
            }
        }
    }


    private void checkTopic(Date cur, Topic topic) {
        AssertUtil.notNull(topic, "团购不存在");
        if (topic.getSalesPartten() == null || !topic.getSalesPartten().equals(SalesPartten.GROUP_BUY.getValue())) {
            throw new ServiceException("该团购不存在");
        }
        if (topic.getStatus() == null || !topic.getStatus().equals(TopicStatus.PASSED.ordinal())) {
            throw new ServiceException("团购不存在");
        }

        if (cur.before(topic.getStartTime())) {
            throw new ServiceException("该团购还未开始,请稍后再来");
        }
        if (cur.after(topic.getEndTime())) {
            throw new ServiceException("该团购已经结束");
        }
    }

    private void checkParam(Long topicId, Long memberId) {
        AssertUtil.notNull(topicId, "参数错误");
        AssertUtil.notNull(memberId, "参数错误");
    }


    private void updateFactAmount(GroupbuyGroup groupbuyGroup, int memberLimit, int times) {
        if (times < 0) {
            throw new ServiceException("系统繁忙,请稍后再试");
        }
        int oldFact = groupbuyGroup.getFactAmount();
        int curFact = oldFact + 1;

        Map<String, Object> param = new HashMap();
        param.put("oldFact", oldFact);
        param.put("curFact", curFact);
        param.put("id", groupbuyGroup.getId());

        int effect = groupbuyGroupDao.updateFactAmountById(param);
        while (effect < 1) {
            groupbuyGroup = groupbuyGroupDao.queryById(groupbuyGroup.getId());
            AssertUtil.notNull(groupbuyGroup, "参团信息错误");
            if (groupbuyGroup.getFactAmount().intValue() >= memberLimit) {
                throw new ServiceException("该团已满,请参与其他的团,或开团");
            }
            updateFactAmount(groupbuyGroup, memberLimit, --times);
        }
        if ((groupbuyGroup.getFactAmount() + 1) >= memberLimit) {
            GroupbuyGroup groupbuyGroupQuery = new GroupbuyGroup();
            groupbuyGroupQuery.setId(groupbuyGroup.getId());
            groupbuyGroupQuery.setStatus(GroupbuyGroupStatus.SUCCESS.getCode());
            groupbuyGroupDao.updateNotNullById(groupbuyGroupQuery);

        }
    }

    private Groupbuy getGroupbuy(Topic topic, TopicItem topicItem, GroupbuyInfo groupbuyInfo) {
        Groupbuy groupbuy = new Groupbuy();
        groupbuy.setTopicId(topic.getId());
        groupbuy.setName(topic.getName());
        groupbuy.setStatus(topic.getStatus());
        groupbuy.setDetail(topic.getIntroMobile());
        groupbuy.setEndTime(topic.getEndTime());
        groupbuy.setStartTime(topic.getStartTime());
        if (topicItem != null) {
            groupbuy.setSku(topicItem.getSku());
            groupbuy.setSpu(topicItem.getSpu());
            groupbuy.setBarcode(topicItem.getBarCode());
            groupbuy.setItemId(topicItem.getItemId());
            groupbuy.setItemName(topicItem.getName());
            groupbuy.setItemPic(topicItem.getTopicImage());
            groupbuy.setTopicItemId(topicItem.getId());
            groupbuy.setApplyInventory(topicItem.getLimitTotal());
            groupbuy.setBondedArea(topicItem.getBondedArea());
            groupbuy.setBrandId(topicItem.getBrandId());
            groupbuy.setCategoryId(topicItem.getCategoryId());
            groupbuy.setLimitAmount(topicItem.getLimitAmount());
            groupbuy.setSalePrice(topicItem.getSalePrice());
            groupbuy.setCountryId(topicItem.getCountryId());
            groupbuy.setCountryName(topicItem.getCountryName());
            groupbuy.setSupplierId(topicItem.getSupplierId());
            groupbuy.setSupplierName(topicItem.getSupplierName());
            groupbuy.setWhtype(topicItem.getWhType());
            groupbuy.setWarehouseId(topicItem.getStockLocationId());
            groupbuy.setWarehouseName(topicItem.getStockLocation());
            groupbuy.setPutSign(topicItem.getPutSign());
        }
        if (groupbuyInfo != null) {
            groupbuy.setType(groupbuyInfo.getType());
            groupbuy.setGroupPrice(groupbuyInfo.getGroupPrice());
            groupbuy.setMemberLimit(groupbuyInfo.getMemberLimit());
            groupbuy.setDuration(groupbuyInfo.getDuration());
            groupbuy.setSort(groupbuyInfo.getSort());
            groupbuy.setIntroduce(groupbuyInfo.getIntroduce());
        }
        return groupbuy;
    }

    private List<GroupbuyGroupDTO> getGroupbuyGroupDTOs(List<GroupbuyGroup> groupbuyGroupList) {
        List<GroupbuyGroupDTO> groupbuyGroupDTOList = new ArrayList<>();
        for (GroupbuyGroup groupbuyGroup : groupbuyGroupList) {
            GroupbuyGroupDTO groupbuyGroupDTO = new GroupbuyGroupDTO();
            groupbuyGroupDTO.setCode(groupbuyGroup.getCode());
            groupbuyGroupDTO.setId(groupbuyGroup.getId());
            groupbuyGroupDTO.setGroupbuyId(groupbuyGroup.getGroupbuyId());
            groupbuyGroupDTO.setTopicName(groupbuyGroup.getTopicName());
            groupbuyGroupDTO.setTopicId(groupbuyGroup.getTopicId());
            groupbuyGroupDTO.setStartTime(groupbuyGroup.getStartTime());
            groupbuyGroupDTO.setEndTime(groupbuyGroup.getEndTime());
            groupbuyGroupDTO.setTopicEndTime(groupbuyGroup.getTopicEndTime());
            groupbuyGroupDTO.setMemberName(groupbuyGroup.getMemberName());
            groupbuyGroupDTO.setMemberId(groupbuyGroup.getMemberId());
            groupbuyGroupDTO.setPlanAmount(groupbuyGroup.getPlanAmount());
            groupbuyGroupDTO.setFactAmount(groupbuyGroup.getFactAmount());
            groupbuyGroupDTO.setStatus(groupbuyGroup.getStatus());
            groupbuyGroupDTOList.add(groupbuyGroupDTO);
        }
        return groupbuyGroupDTOList;
    }

    private void processItemPic(List<GroupbuyGroupDTO> groupbuyGroupDTOs) {
        if (CollectionUtils.isEmpty(groupbuyGroupDTOs)) return;
        Set<Long> groupbuyIds = new HashSet<>();
        for (GroupbuyGroupDTO groupbuyGroupDTO : groupbuyGroupDTOs) {
            groupbuyIds.add(groupbuyGroupDTO.getGroupbuyId());
        }
        List<GroupbuyInfo> groupbuyInfoList = groupbuyInfoDao.queryByIds(new ArrayList<>(groupbuyIds));
        if (CollectionUtils.isEmpty(groupbuyInfoList)) return;
        List<Long> topicItemIds = new ArrayList<>();
        for (GroupbuyInfo groupbuyInfo : groupbuyInfoList) {
            topicItemIds.add(groupbuyInfo.getTopicItemId());
        }
        List<TopicItem> topicItemList = topicItemDao.getTopicItemByIds(topicItemIds);

        List<Long> itemIds = new ArrayList<>();
        for(TopicItem topicItem: topicItemList){
            itemIds.add(topicItem.getItemId());
        }
        List<ItemPictures> pictures = null;
        if(!CollectionUtils.isEmpty(itemIds)){
            Map<String,Object> params = new HashMap<>();
            List<DAOConstant.WHERE_ENTRY> whereList = new ArrayList<DAOConstant.WHERE_ENTRY>();
            whereList.add(new DAOConstant.WHERE_ENTRY("item_id", DAOConstant.MYBATIS_SPECIAL_STRING.INLIST,itemIds));
            params.put(DAOConstant.MYBATIS_SPECIAL_STRING.WHERE.name(), whereList);
            params.put("main",1);
            pictures = itemPicturesService.queryByParam(params);
        }


        for (GroupbuyGroupDTO groupbuyGroupDTO : groupbuyGroupDTOs) {
            setPicAndSalePrice(getTopicItemId(groupbuyGroupDTO, groupbuyInfoList), topicItemList, groupbuyGroupDTO,pictures);
            setGroupPrice(groupbuyInfoList, groupbuyGroupDTO);
        }
    }

    private void setGroupPrice(List<GroupbuyInfo> groupbuyInfoList, GroupbuyGroupDTO groupbuyGroupDTO) {
        for (GroupbuyInfo groupbuyInfo : groupbuyInfoList) {
            if (groupbuyGroupDTO.getGroupbuyId().equals(groupbuyInfo.getId())) {
                groupbuyGroupDTO.setGroupPrice(groupbuyInfo.getGroupPrice());
            }
        }
    }

    private Long getTopicItemId(GroupbuyGroupDTO groupbuyGroup, List<GroupbuyInfo> groupbuyInfoList) {
        if (CollectionUtils.isEmpty(groupbuyInfoList)) return null;
        for (GroupbuyInfo groupbuyInfo : groupbuyInfoList) {
            if (groupbuyGroup.getGroupbuyId().equals(groupbuyInfo.getId())) {
                return groupbuyInfo.getTopicItemId();
            }
        }
        return null;
    }

    private void setPicAndSalePrice(Long topicItemId, List<TopicItem> topicItemList, GroupbuyGroupDTO groupbuyGroupDTO,List<ItemPictures> pictures) {
        if (CollectionUtils.isEmpty(topicItemList)) return;
        for (TopicItem topicItem : topicItemList) {
            if (topicItem.getId().equals(topicItemId)) {
                groupbuyGroupDTO.setPic(ImageUtil.getImgFullUrl(Constant.IMAGE_URL_TYPE.item, topicItem.getTopicImage()));
                groupbuyGroupDTO.setSalePrice(topicItem.getSalePrice());
                Long itemId = topicItem.getItemId();
                getItemPic(groupbuyGroupDTO, pictures, itemId);
            }
        }
    }

    private void getItemPic(GroupbuyGroupDTO groupbuyGroupDTO, List<ItemPictures> pictures, Long itemId) {
        if(CollectionUtils.isEmpty(pictures)) return;
        for(ItemPictures p: pictures){
            if(p.getItemId() !=null && p.getItemId().equals(itemId)){
                groupbuyGroupDTO.setPic(ImageUtil.getImgFullUrl(Constant.IMAGE_URL_TYPE.item,p.getPicture()));
                break;
            }
        }
    }

}
