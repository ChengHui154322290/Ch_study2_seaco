package com.tp.service.mmp.groupbuy;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.dao.BaseDao;
import com.tp.common.util.ImageUtil;
import com.tp.common.util.mmp.AssertUtil;
import com.tp.common.vo.Constant;
import com.tp.common.vo.PageInfo;
import com.tp.dao.mmp.*;
import com.tp.dto.common.FailInfo;
import com.tp.dto.common.ResultInfo;
import com.tp.dto.mmp.enums.DeletionStatus;
import com.tp.dto.mmp.enums.SalesPartten;
import com.tp.dto.mmp.enums.TopicStatus;
import com.tp.dto.mmp.enums.groupbuy.GroupbuyGroupStatus;
import com.tp.dto.mmp.groupbuy.GroupbuyListDTO;
import com.tp.model.bse.ClearanceChannels;
import com.tp.model.bse.NationalIcon;
import com.tp.model.mmp.*;
import com.tp.model.prd.ItemDetail;
import com.tp.model.prd.ItemPictures;
import com.tp.query.mmp.TopicItemCartQuery;
import com.tp.service.BaseService;
import com.tp.service.bse.IClearanceChannelsService;
import com.tp.service.bse.IDistrictInfoService;
import com.tp.service.bse.INationalIconService;
import com.tp.service.mmp.groupbuy.IGroupbuyInfoService;
import com.tp.service.prd.IItemDetailService;
import com.tp.service.prd.IItemPicturesService;
import com.tp.util.BeanUtil;

import org.springframework.util.CollectionUtils;

import java.util.*;

@Service
public class GroupbuyInfoService extends BaseService<GroupbuyInfo> implements IGroupbuyInfoService {

    @Autowired
    private GroupbuyInfoDao groupbuyInfoDao;

    @Autowired
    private GroupbuyGroupDao groupbuyGroupDao;

    @Autowired
    private GroupbuyJoinDao groupbuyJoinDao;

    @Autowired
    private TopicDao topicDao;

    @Autowired
    private TopicItemDao topicItemDao;

    @Autowired
    private IItemDetailService itemDetailService;

    @Autowired
    private INationalIconService nationalIconService;
    @Autowired
    private IClearanceChannelsService clearanceChannelsService;
    @Autowired
    private IDistrictInfoService districtInfoService;

    @Autowired
    private IItemPicturesService iItemPicturesService;

    @Override
    public BaseDao<GroupbuyInfo> getDao() {
        return groupbuyInfoDao;
    }

    @Override
    public ResultInfo checkForOrder(Long topicId, String sku, Long groupId, Long memberId) {

        if (topicId == null || groupId == null) {
            return new ResultInfo(new FailInfo("团购信息错误", -1));
        }
        if (StringUtils.isBlank(sku)) {
            return new ResultInfo(new FailInfo("商品信息错误", -1));
        }
        if (memberId == null) {
            return new ResultInfo(new FailInfo("用户信息错误", -1));
        }

        Topic topic = topicDao.queryById(topicId);
        if (topic == null) {
            return new ResultInfo(new FailInfo("团购信息错误", -1));
        }
        AssertUtil.notNull(topic, "团购信息错误");
        if (!SalesPartten.GROUP_BUY.getValue().equals(topic.getSalesPartten())) {
            return new ResultInfo(new FailInfo("团购信息错误", -1));
        }
        TopicItem topicItemQuery = new TopicItem();
        topicItemQuery.setTopicId(topicId);
        topicItemQuery.setSku(sku);
        topicItemQuery.setDeletion(DeletionStatus.NORMAL.ordinal());
        Integer count = topicItemDao.queryByParamCount(BeanUtil.beanMap(topicItemQuery));
        if (count == null || count == 0) {
            return new ResultInfo(new FailInfo("团购信息错误", -1));
        }

        GroupbuyGroup groupbuyGroup = groupbuyGroupDao.queryById(groupId);
        if (groupbuyGroup == null) return new ResultInfo(new FailInfo("团购信息错误", -1));
        if (groupbuyGroup.getStatus() == null || !groupbuyGroup.getStatus().equals(GroupbuyGroupStatus.SUCCESS.getCode())) {
            return new ResultInfo(new FailInfo("参团成功才可以购买", -1));
        }
        if (!topic.getId().equals(groupbuyGroup.getTopicId())) {
            return new ResultInfo(new FailInfo("团购信息错误", -1));
        }

        GroupbuyJoin groupbuyJoinQuery = new GroupbuyJoin();
        groupbuyJoinQuery.setGroupId(groupId);
        groupbuyJoinQuery.setMemberId(memberId);
        Integer joinCount = groupbuyJoinDao.queryByParamCount(BeanUtil.beanMap(groupbuyJoinQuery));
        if (joinCount == null || joinCount < 1) {
            return new ResultInfo(new FailInfo("参团成功才可以购买", -1));
        }
        return new ResultInfo();
    }

    public PageInfo<GroupbuyListDTO> list(GroupbuyListDTO query) {
        AssertUtil.notNull(query, "参数错误");
        Map<String, Object> param = new HashMap<>();
        Integer page = query.getStartPage() == null ? 1 : query.getStartPage() < 1 ? 1 : query.getStartPage();
        Integer size = query.getPageSize() == null ? 10 : query.getPageSize() < 1 ? 10 : query.getPageSize();
        Integer start = (page - 1) * size;
        param.put("start", start);
        param.put("pageSize", size);
        Date cur = new Date();
        param.put("startTime", cur);
        param.put("endTime", cur);
        param.put("status", TopicStatus.PASSED.ordinal());

        List<Long> topicIds = topicDao.queryTopicIdsForGroupbuyAPP(param);
        if (CollectionUtils.isEmpty(topicIds)) return new PageInfo<>();
        Integer count = topicDao.queryTopicForGroupbuyAPPCount(param);
        // 为支持排序
        param.put("topicIds", topicIds);
        List<GroupbuyInfo> groupbuyInfoList = groupbuyInfoDao.queryByTopicIdsForApp(param);

        List<Long> target = new ArrayList<>();
        for (GroupbuyInfo groupbuyInfo : groupbuyInfoList) {
            target.add(groupbuyInfo.getTopicId());
        }
        if (CollectionUtils.isEmpty(target)) return new PageInfo<>();
        List<Topic> topicList = topicDao.queryTopicInfoList(target);
        List<TopicItem> topicItemList = topicItemDao.getTopicItemByTopicIds(target);

        List<GroupbuyListDTO> groupbuyListDTOList = new ArrayList<>();

        for (GroupbuyInfo groupbuyInfo : groupbuyInfoList) {
            GroupbuyListDTO groupbuyListDTO = new GroupbuyListDTO();
            groupbuyListDTO.setTopicId(groupbuyInfo.getTopicId());
            groupbuyListDTO.setGroupbuyId(groupbuyInfo.getId());
            groupbuyListDTO.setGroupPrice(groupbuyInfo.getGroupPrice());
            groupbuyListDTO.setPlanAmount(groupbuyInfo.getMemberLimit());
            setTopicInfo(topicList, groupbuyInfo, groupbuyListDTO);
            setItemInfo(topicItemList, groupbuyListDTO, query.isForIndex());
            groupbuyListDTOList.add(groupbuyListDTO);
        }

        PageInfo<GroupbuyListDTO> pageInfo = new PageInfo<>(page, size);
        pageInfo.setRows(groupbuyListDTOList);
        pageInfo.setRecords(count);
        pageInfo.setTotal(count % size > 0 ? (count / size + 1) : count / size);
        return pageInfo;
    }

    private void setTopicInfo(List<Topic> topicList, GroupbuyInfo groupbuyInfo, GroupbuyListDTO groupbuyListDTO) {
        for (Topic topic : topicList) {
            if (topic.getId().equals(groupbuyInfo.getTopicId())) {
                groupbuyListDTO.setStartTime(topic.getStartTime());
                groupbuyListDTO.setEndTime(topic.getEndTime());
                groupbuyListDTO.setTopicName(topic.getName());
                return;
            }
        }
    }

    private void setItemInfo(List<TopicItem> topicItemList, GroupbuyListDTO groupbuyListDTO, boolean forIndex) {
        for (TopicItem topicItem : topicItemList) {
            if (topicItem.getDeletion().equals(DeletionStatus.DELETED.ordinal())) continue;
            if (topicItem.getTopicId().equals(groupbuyListDTO.getTopicId())) {

                groupbuyListDTO.setChannelId(topicItem.getBondedArea());
                ClearanceChannels clearanceChannels = clearanceChannelsService.queryById(topicItem.getBondedArea());
                groupbuyListDTO.setChannel(clearanceChannels == null ? "" : clearanceChannels.getName());

                groupbuyListDTO.setSku(topicItem.getSku());
                groupbuyListDTO.setSalePrice(topicItem.getSalePrice());
                groupbuyListDTO.setTopicItemId(topicItem.getId());
                groupbuyListDTO.setItemImg(ImageUtil.getImgFullUrl(Constant.IMAGE_URL_TYPE.item, topicItem.getTopicImage()));

                NationalIcon iconQuery = new NationalIcon();
                iconQuery.setCountryId(topicItem.getCountryId().intValue());
                NationalIcon nationalIcon = nationalIconService.queryUniqueByObject(iconQuery);
                groupbuyListDTO.setCountryImg(ImageUtil.getImgFullUrl(Constant.IMAGE_URL_TYPE.basedata, nationalIcon == null ? "" : nationalIcon.getPicPath()));
                groupbuyListDTO.setCountryName(topicItem.getCountryName());
                groupbuyListDTO.setCountryId(topicItem.getCountryId());

                ItemDetail itemQuery = new ItemDetail();
                itemQuery.setItemId(topicItem.getItemId());
                ItemDetail itemDetail = itemDetailService.queryUniqueByObject(itemQuery);

                groupbuyListDTO.setFeature(itemDetail == null ? "" : itemDetail.getSubTitle());

                if (forIndex) {
                    if (itemDetail != null) {
                        Long itemId = itemDetail.getItemId();
                        ItemPictures query = new ItemPictures();
                        query.setItemId(itemId);
                        query.setMain(1);
                        ItemPictures itemPictures = iItemPicturesService.queryUniqueByObject(query);
                        if (itemPictures != null) {
                            groupbuyListDTO.setItemImg(ImageUtil.getImgFullUrl(Constant.IMAGE_URL_TYPE.item, itemPictures.getPicture()));
                        }
                    }

                }

                return;
            }

        }
    }

}
