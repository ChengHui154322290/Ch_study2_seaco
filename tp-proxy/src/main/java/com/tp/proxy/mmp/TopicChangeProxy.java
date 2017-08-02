package com.tp.proxy.mmp;

import com.tp.common.util.ImageUtil;
import com.tp.common.vo.Constant;
import com.tp.common.vo.PageInfo;
import com.tp.common.vo.mmp.ProcessingErrorMessage;
import com.tp.common.vo.mmp.TopicConstant;
import com.tp.dfsutils.util.DfsDomainUtil;
import com.tp.dto.common.ResultInfo;
import com.tp.dto.mmp.*;
import com.tp.dto.mmp.enums.DeletionStatus;
import com.tp.dto.mmp.enums.OperStatus;
import com.tp.dto.mmp.enums.TopicStatus;
import com.tp.dto.mmp.enums.TopicType;
import com.tp.dto.stg.InventoryDto;
import com.tp.dto.stg.query.InventoryDtoQuery;
import com.tp.dto.stg.query.InventoryQuery;
import com.tp.exception.ServiceException;
import com.tp.model.bse.ForbiddenWords;
import com.tp.model.mmp.*;
import com.tp.proxy.BaseProxy;
import com.tp.proxy.mmp.callBack.Callback;
import com.tp.service.IBaseService;
import com.tp.service.bse.IForbiddenWordsService;
import com.tp.service.mmp.ITopicChangeService;
import com.tp.service.mmp.ITopicManagementService;
import com.tp.service.mmp.ITopicService;
import com.tp.service.stg.IInventoryQueryService;
import com.tp.util.BeanUtil;

import net.sf.json.JSONArray;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.lang.reflect.InvocationTargetException;
import java.util.*;

/**
 * 促销活动代理层
 *
 * @author szy
 */
@Service
public class TopicChangeProxy extends BaseProxy<TopicChange> {

    @Autowired
    private ITopicChangeService topicChangeService;

    @Autowired
    private ITopicManagementService topicManagementService;

    @Autowired
    private ITopicService topicService;

   /* @Autowired
    private DfsDomainUtil dfsDomainUtil;*/

    @Autowired
    private IInventoryQueryService inventoryQueryService;

    @Autowired
    private IForbiddenWordsService forbiddenWordsService;

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public IBaseService<TopicChange> getService() {
        return topicChangeService;
    }

    /**
     * 查询活动信息
     *
     * @param tid
     * @return
     * @throws Exception
     */
    private ResultInfo<TopicDetailDTO> getTopicInfoById(final Long tid) throws Exception {
        final ResultInfo<TopicDetailDTO> result = new ResultInfo<>();
        this.execute(result, new Callback() {
            @Override
            public void process() throws Exception {
                TopicDetailDTO topicDetailDTO = topicService.getTopicDetailByIdWithLockItem(tid);
                result.setData(topicDetailDTO);
            }
        });
        return result;
    }

    public ResultInfo<Long> generateTopicChange(final Long tid, final Long userId, final String userName) {
        final ResultInfo<Long> result = new ResultInfo<>();
        this.execute(result, new Callback() {
            @Override
            public void process() throws Exception {
                ResultInfo<TopicDetailDTO> resultInfo = getTopicInfoById(tid);
                if (!resultInfo.isSuccess()
                        || resultInfo.getData().getTopic() == null
                        || resultInfo.getData().getTopic().getStatus() != TopicStatus.PASSED.ordinal()) {
                    logger.error("import topic not exist, or status is not audited................");
                    throw new ServiceException(ProcessingErrorMessage.TOPIC_CHANGE_IS_NOT_AUDITED);
                }
                Long topicChangeId = createTopicChangeInfo(resultInfo.getData(), userId, userName);
                result.setData(topicChangeId);
            }
        });
        return result;
    }

    /**
     * 生成新的活动变更单
     *
     * @param tid
     * @param userId
     * @param userName
     * @return
     * @throws Exception
     */
    public ResultInfo<Long> getNewTopicChange(Long tid, Long userId, String userName) {
        ResultInfo<Long> resultInfo = generateTopicChange(tid, userId, userName);
        return resultInfo;
    }

    /**
     * 生成活动变更单
     *
     * @param tid
     * @return
     * @throws Exception
     */
    public TopicChangeDetailDTO getTopicChange(Long tid) throws Exception {
        TopicChangeDetailDTO changeDetailDto = topicChangeService.getTopicChangeDetailById(tid);
        // 针对查询结果 补全展示信息
        if (null != changeDetailDto && null != changeDetailDto.getTopic()) {
            // 获取DFS完整图片路径
            String pcImage = changeDetailDto.getTopic().getImage();
            String newImage = changeDetailDto.getTopic().getImageNew();
            String mobileImage = changeDetailDto.getTopic().getImageMobile();
            String interestedImage = changeDetailDto.getTopic().getImageInterested();
            String hitaoImage = changeDetailDto.getTopic().getImageHitao();

            String pcImageN = changeDetailDto.getTopic().getPcImage();
            String interestImageN = changeDetailDto.getTopic().getPcInterestImage();
            String mobileImageN = changeDetailDto.getTopic().getMobileImage();
            String mallImageN = changeDetailDto.getTopic().getMallImage();
            String haitaoImageN = changeDetailDto.getTopic().getHaitaoImage();
            // PC端图片
            if (!StringUtils.isBlank(pcImage)) {
                changeDetailDto.setTopicPcImageFull(ImageUtil.getCMSImgFullUrl(pcImage));
            }
            // 明日预告图片
            if (!StringUtils.isBlank(newImage)) {
                changeDetailDto.setTopicNewImageFull(ImageUtil.getCMSImgFullUrl(newImage));
            }
            // 移动图片
            if (!StringUtils.isBlank(mobileImage)) {
                changeDetailDto.setTopicMobileImageFull(ImageUtil.getCMSImgFullUrl(mobileImage));
            }
            // 可能感兴趣图片
            if (!StringUtils.isBlank(interestedImage)) {
                changeDetailDto.setTopicInterestedImageFull(ImageUtil.getCMSImgFullUrl(interestedImage));
            }
            // 海淘图片
            if (!StringUtils.isBlank(hitaoImage)) {
                changeDetailDto.setTopicHitaoImageFull(ImageUtil.getCMSImgFullUrl(hitaoImage));
            }

            // PC端图片(新)
            if (!StringUtils.isBlank(pcImageN)) {
                changeDetailDto.setPcImageFull(ImageUtil.getCMSImgFullUrl(pcImageN));
            }
            // 商城图片(新)
            if (!StringUtils.isBlank(mallImageN)) {
                changeDetailDto.setMallImageFull(ImageUtil.getCMSImgFullUrl(mallImageN));
            }
            // 移动图片(新)
            if (!StringUtils.isBlank(mobileImageN)) {
                changeDetailDto.setMobileImageFull(ImageUtil.getCMSImgFullUrl(mobileImageN));
            }
            // 可能感兴趣图片(新)
            if (!StringUtils.isBlank(interestImageN)) {
                changeDetailDto.setPcInterestImageFull(ImageUtil.getCMSImgFullUrl(interestImageN));
            }
            // 海淘图片(新)
            if (!StringUtils.isBlank(haitaoImageN)) {
                changeDetailDto.setHaitaoImageFull(ImageUtil.getCMSImgFullUrl(haitaoImageN));
            }
            this.SyncCurrentItemStockAndPic(changeDetailDto);
            this.getCouponPic(changeDetailDto);
        }
        return changeDetailDto;
    }

    /**
     * 查询商品当前库存
     *
     * @param changeDetailDto
     */
    private void SyncCurrentItemStockAndPic(TopicChangeDetailDTO changeDetailDto) {
        if (null != changeDetailDto.getTopicItemDtoList()) {
            // 生成查询对象
            List<InventoryQuery> queries = new ArrayList<InventoryQuery>();
            for (TopicItemInfoDTO itemDto : changeDetailDto.getTopicItemDtoList()) {
                if (null != itemDto.getSku()
                        && null != itemDto.getStockLocationId()) {
                	InventoryQuery inventory = new InventoryQuery();
                    inventory.setSku(itemDto.getSku());
                    inventory.setWarehouseId(itemDto.getStockLocationId());
                    queries.add(inventory);
                }
                if (null != itemDto
                        && !StringUtils.isBlank(itemDto.getTopicImage())) {
                    String filePath = itemDto.getTopicImage();
                    itemDto.setImageFullPath(ImageUtil.getImgFullUrl(Constant.IMAGE_URL_TYPE.item, filePath));
                }
            }
            // 循环填充最新库存
//            List<InventoryDto> inventoryDtos = inventoryQueryService
//                    .selectAvailableForSaleBySkuAndWhIdList(queries);
            List<InventoryDto> inventoryDtos = inventoryQueryService.queryAvailableInventory(queries);
            if (inventoryDtos != null) {
                Map<String, InventoryDto> inventoryDtoMap = new HashMap<String, InventoryDto>();
                for (InventoryDto inventoryDto : inventoryDtos) {
                    if (null == inventoryDto.getWarehouseId()
                            || null == inventoryDto.getSku()) {
                        continue;
                    }
                    String inventoryKey = String.valueOf(inventoryDto
                            .getWarehouseId()) + "_" + inventoryDto.getSku();
                    inventoryDtoMap.put(inventoryKey, inventoryDto);
                }
                for (TopicItemInfoDTO itemDto : changeDetailDto
                        .getTopicItemDtoList()) {
                    if (null != itemDto.getSku()
                            && null != itemDto.getStockLocationId()) {
                        String inventoryKey = String.valueOf(itemDto
                                .getStockLocationId()) + "_" + itemDto.getSku();
                        if (inventoryDtoMap.containsKey(inventoryKey)) {
                            InventoryDto inventoryDto = inventoryDtoMap
                                    .get(inventoryKey);
                            itemDto.setStockAmout(inventoryDto.getInventory());
                        } else {
                            itemDto.setStockAmout(-1);
                        }
                    }
                }
            }
        }
    }

    private void getCouponPic(TopicChangeDetailDTO changeDetailDto) {
        for (TopicCouponChangeDTO couponDto : changeDetailDto.getCouponList()) {
            if (null != couponDto
                    && !StringUtils.isBlank(couponDto.getCouponImage())) {
                String filePath = couponDto.getCouponImage();
                couponDto.setCouponFullImage(ImageUtil.getCMSImgFullUrl(filePath));
            }
        }
    }

    /**
     * 创建活动变更单
     *
     * @param detailDTO
     * @param userId
     * @param userName
     * @return
     */
    private Long createTopicChangeInfo(TopicDetailDTO detailDTO, Long userId,
                                       String userName) {
        if (detailDTO == null) {
            logger.error("save topic not exist");
            throw new ServiceException(
                    ProcessingErrorMessage.VALID_TOPIC_INFO);
        }
        TopicChange topicChange = getTopicChangeDO(detailDTO.getTopic(), userId, userName);
        PolicyChange policyChange = this.getPolicyChangeDO(detailDTO.getPolicyForChangeOrder(), userId, userName);
        List<TopicItemChange> itemChangeDOs = this.getTopicItemChangeDO(detailDTO.getPromotionItemList(), userId, userName);
        List<TopicCouponChange> couponDtos = getCouponChangeList(detailDTO.getCouponList(), userId, userName);
        List<TopicPromoterChange> topicPromoterChangeList = getTopicPromoterChangeList(detailDTO.getTopicPromoterList(),userName);
        List<Long> relateIdsList = detailDTO.getRelateTidList();
        Long topicChangeId = topicManagementService.generateTopicChange(topicChange,
                itemChangeDOs, couponDtos, relateIdsList, policyChange,topicPromoterChangeList, userId,
                userName);
        if (topicChangeId == null) {
            logger.error("save topic error");
            throw new ServiceException(
                    ProcessingErrorMessage.SAVE_TOPIC_CHANGE_FAILD);
        }
        return topicChangeId;
    }

    /**
     * 生成活动变更单活动商品
     *
     * @param topicItems
     * @param userId
     * @param userName
     * @return
     */
    private List<TopicItemChange> getTopicItemChangeDO(
            List<TopicItem> topicItems, Long userId, String userName) {
        List<TopicItemChange> itemChanges = new ArrayList<TopicItemChange>();
        for (TopicItem topicItem : topicItems) {
            TopicItemChange itemChange = new TopicItemChange();
            itemChange.setBarCode(topicItem.getBarCode());
            itemChange.setBrandId(topicItem.getBrandId());
            itemChange.setCategoryId(topicItem.getCategoryId());
            itemChange.setCreateTime(new Date());
            itemChange.setCreateUser(userName);
            itemChange.setDeletion(DeletionStatus.NORMAL.ordinal());
            itemChange.setInputSource(topicItem.getInputSource());
            itemChange.setItemId(topicItem.getItemId());
            itemChange.setItemSpec(topicItem.getItemSpec());
            itemChange.setLimitAmount(topicItem.getLimitAmount());
            itemChange.setLimitTotal(topicItem.getLimitTotal());
            itemChange.setSourceLimitTotal(topicItem.getLimitTotal());
            itemChange.setName(topicItem.getName());
            itemChange.setOperStatus(OperStatus.MODIFY.ordinal());
            itemChange.setPictureSize(topicItem.getPictureSize());
            itemChange.setRemark(topicItem.getRemark());
            itemChange.setSaledAmount(topicItem.getSaledAmount());
            itemChange.setSalePrice(topicItem.getSalePrice());
            itemChange.setSku(topicItem.getSku());
            itemChange.setSortIndex(topicItem.getSortIndex());
            itemChange.setSpu(topicItem.getSpu());
            itemChange.setStock(topicItem.getStock());
            itemChange.setStockAmount(topicItem.getStockAmount());
            itemChange.setStockLocation(topicItem.getStockLocation());
            itemChange.setStockLocationId(topicItem.getStockLocationId());
            itemChange.setPutSign(topicItem.getPutSign());
            itemChange.setSupplierId(topicItem.getSupplierId());
            itemChange.setSupplierName(topicItem.getSupplierName());
            itemChange.setTopicImage(topicItem.getTopicImage());
            itemChange.setTopicPrice(topicItem.getTopicPrice());
            itemChange.setChangeTopicItemId(topicItem.getId());
            itemChange.setWhType(topicItem.getWhType());
            itemChange.setBondedArea(topicItem.getBondedArea());
            itemChange.setCountryId(topicItem.getCountryId());
            itemChange.setCountryName(topicItem.getCountryName());
            itemChange.setPurchaseMethod(topicItem.getPurchaseMethod());

            itemChange.setItemColor(topicItem.getItemColor());
            itemChange.setItemSize(topicItem.getItemSize());
            itemChange.setDetailId(topicItem.getDetailId());
            itemChange.setLargeCateoryId(topicItem.getLargeCateoryId());
            itemChange.setMiddleCategoryId(topicItem.getMiddleCategoryId());
            itemChange.setPrdid(topicItem.getPrdid());
            itemChange.setIsHot(topicItem.getIsHot());
            itemChange.setListingTime(topicItem.getListingTime());
            itemChange.setItemStatus(topicItem.getItemStatus());
            itemChange.setUpdateUser(userName);
            itemChange.setApplyAgeId(topicItem.getApplyAgeId());
            itemChanges.add(itemChange);
        }
        return itemChanges;
    }

    private TopicChange getTopicChangeDO(Topic topic, Long userId,
                                         String userName) {
        if (topic == null) {
            throw new ServiceException( ProcessingErrorMessage.VALID_TOPIC_INFO);
        }
        TopicChange topicChange = new TopicChange();
        topicChange.setAreaStr(topic.getAreaStr());
        topicChange.setBrandId(topic.getBrandId());
        topicChange.setBrandName(topic.getBrandName());
        topicChange.setChangeTopicId(topic.getId());
        topicChange.setCreateTime(new Date());
        topicChange.setCreateUser(userName);
        topicChange.setUpdateTime(new Date());
        topicChange.setUpdateUser(userName);
        topicChange.setDeletion(DeletionStatus.NORMAL.ordinal());
        topicChange.setDiscount(topic.getDiscount());
        topicChange.setEndTime(topic.getEndTime());
        topicChange.setFreightTemplet(topic.getFreightTemplet() == null ? -1 : topic.getFreightTemplet());
        topicChange.setImage(topic.getImage());
        topicChange.setImageMobile(topic.getImageMobile());
        topicChange.setImageNew(topic.getImageNew());
        topicChange.setImageInterested(topic.getImageInterested());
        topicChange.setImageHitao(topic.getImageHitao());
        topicChange.setPcImage(topic.getPcImage());
        topicChange.setPcInterestImage(topic.getPcInterestImage());
        topicChange.setMobileImage(topic.getMobileImage());
        topicChange.setMallImage(topic.getMallImage());
        topicChange.setHaitaoImage(topic.getHaitaoImage());
        topicChange.setIntro(topic.getIntro());
        topicChange.setIntroMobile(topic.getIntroMobile());
        topicChange.setIsSupportSupplier(topic.getIsSupportSupplier());
        topicChange.setIsSupportSupplierInfo(topic.getIsSupportSupplierInfo() == null ? -1 : topic.getIsSupportSupplierInfo());
        topicChange.setLastingType(topic.getLastingType());
        topicChange.setName(topic.getName());
        topicChange.setNumber(topic.getNumber());
        topicChange.setPlatformStr(topic.getPlatformStr());
        topicChange.setProgress(topic.getProgress());
        topicChange.setRemark(topic.getRemark());
        topicChange.setSortIndex(topic.getSortIndex());
        topicChange.setStartTime(topic.getStartTime());
        topicChange.setStatus(TopicStatus.EDITING.ordinal());
        topicChange.setType(topic.getType());
        topicChange.setTopicPoint(topic.getTopicPoint());
        topicChange.setSalesPartten(topic.getSalesPartten());
        topicChange.setSupplierId(topic.getSupplierId());
        topicChange.setSupplierName(topic.getSupplierName());
        topicChange.setCanUseXgMoney(topic.getCanUseXgMoney());
        topicChange.setReserveInventoryFlag(topic.getReserveInventoryFlag());
        return topicChange;
    }

    private PolicyChange getPolicyChangeDO(PolicyInfo policy, Long userId, String userName) {
        PolicyChange PolicyChange = new PolicyChange();
        if (policy == null)
            return PolicyChange;
        PolicyChange.setByIp(policy.getByIp());
        PolicyChange.setByMobile(policy.getByMobile());
        PolicyChange.setByRegisterTime(policy.getByRegisterTime());
        PolicyChange.setByTopic(policy.getByTopic());
        PolicyChange.setByUid(policy.getByUid());
        PolicyChange.setCreateTime(new Date());
        PolicyChange.setEarlyThanTime(policy.getEarlyThanTime());
        PolicyChange.setLateThanTime(policy.getLateThanTime());
        PolicyChange.setPolicyChangeId(policy.getId());
        return PolicyChange;
    }

    private List<TopicCouponChange> getCouponChangeList(
            List<TopicCouponDTO> couponList, Long userId, String userName) {
        List<TopicCouponChange> couponChangeDOs = new ArrayList<TopicCouponChange>();
        if (couponList == null)
            return Collections.emptyList();
        for (TopicCouponDTO couponDTO : couponList) {
            TopicCouponChange couponChange = new TopicCouponChange();
            couponChange.setCouponId(couponDTO.getCouponId());
            couponChange.setCouponImage(couponDTO.getCouponImage());
            couponChange.setCouponSize(couponDTO.getCouponSize());
            couponChange.setSortIndex(couponDTO.getSortIndex());
            couponChangeDOs.add(couponChange);
        }
        return couponChangeDOs;
    }

    private List<TopicPromoterChange> getTopicPromoterChangeList(List<TopicPromoter> topicPromoterList, String userName) {
        List<TopicPromoterChange> topicPromoterChangeList = new ArrayList<TopicPromoterChange>();
        if (!CollectionUtils.isEmpty(topicPromoterList)){
        	for(TopicPromoter topicPromoter:topicPromoterList){
        		TopicPromoterChange topicPromoterChange = new TopicPromoterChange();
        		try {
					BeanUtil.copyProperties(topicPromoterChange, topicPromoter);
					topicPromoterChange.setCreateUser(userName);
					topicPromoterChangeList.add(topicPromoterChange);
				} catch (IllegalAccessException e) {
				} catch (InvocationTargetException e) {
				}
        	}
        }
        return topicPromoterChangeList;
    }
    
    /**
     * 分页获取专题信息，按Like
     *
     * @param currentPage
     * @param pageSize
     * @return
     */
    public PageInfo<TopicChange> getTopicInfosByPagedWithLike(TopicChange topicChange, int currentPage, int pageSize) {

        return topicChangeService.queryPageListByTopicChangeDOAndStartPageSizeWithLike(topicChange, currentPage, pageSize);
    }

    /**
     * 批准指定专题
     *
     * @param topicChangeId
     * @param userId
     * @param userName
     * @return
     */
    public ResultInfo approveTopicChange(final Long topicChangeId, final Long userId, final String userName) {
        final ResultInfo result = new ResultInfo();
        this.execute(result, new Callback() {
            @Override
            public void process() throws Exception {
                if (null == topicChangeId || 0 == topicChangeId) {
                    throw new ServiceException(ProcessingErrorMessage.VALID_TOPIC_ID);
                }
                ResultInfo rm = topicManagementService.approveTopicChange(topicChangeId, userId, userName);
                rm.setSuccess(rm.isSuccess());
            }
        });
        return result;
    }

    /**
     * 取消指定专题活动
     *
     * @param topicId
     * @throws Exception
     */
    public ResultInfo cancelTopicChange(final Long topicChangeId, final Long userId,
                                        final String userName) {
        final ResultInfo result = new ResultInfo();
        this.execute(result, new Callback() {
            @Override
            public void process() throws Exception {
                if (null == topicChangeId || 0 == topicChangeId) {
                    throw new ServiceException(ProcessingErrorMessage.VALID_TOPIC_ID);
                }
                ResultInfo rm = topicManagementService.cancelTopicChange(topicChangeId, userId, userName);
                result.setSuccess(rm.isSuccess());
            }
        });
        return result;
    }

    /**
     * 驳回指定专题
     */
    public ResultInfo refuseTopicChange(final Long topicChangeId, final Long userId,
                                        final String userName) {
        final ResultInfo result = new ResultInfo();
        this.execute(result, new Callback() {
            @Override
            public void process() throws Exception {
                if (null == topicChangeId || 0 == topicChangeId) {
                    throw new ServiceException(ProcessingErrorMessage.VALID_TOPIC_ID);
                }

                ResultInfo rm = topicManagementService.refuseTopicChange(
                        topicChangeId, userId, userName);

                if (!rm.isSuccess()) {
                    result.setMsg(rm.getMsg());
                }
                result.setSuccess(result.isSuccess());
            }
        });

        return result;
    }

    /**
     * 保存专题相关信息
     *
     * @param topicChangeDetail
     * @param itemInfos
     * @param removeItemIds
     * @param relateIds
     * @param removeRelateIds
     * @param topicStatus
     * @throws Exception
     */
    public ResultInfo submitTopicInfo(final TopicChangeDetailDTO topicChangeDetail,
                                      final TopicItemInfoDTO[] itemInfos, final String removeItemIds,
                                      final String relateIds, final String removeRelateIds,
                                      final TopicCouponChange[] topicCoupons, final String removeCouponIds,
                                      final Long userId, final String userName) throws ServiceException {

        final ResultInfo result = new ResultInfo();
        this.execute(result, new Callback() {
            @Override
            public void process() throws Exception {
                ResultInfo resultInfo = saveOrSubmit(topicChangeDetail, itemInfos, removeItemIds,
                        relateIds, removeRelateIds, topicCoupons, removeCouponIds,
                        userId, userName, false);
                if (!resultInfo.isSuccess()) {
                    result.setMsg(resultInfo.getMsg());
                }
                result.setSuccess(resultInfo.isSuccess());
            }

        });
        return result;
    }

    /**
     * 保存活动变更单
     *
     * @param topicChangeDetail
     * @param itemInfos
     * @param removeItemIds
     * @param relateIds
     * @param removeRelateIds
     * @param userId
     * @param userName
     * @return
     * @throws ServiceException
     */
    public ResultInfo saveTopicChangeInfo(
            final TopicChangeDetailDTO topicChangeDetail,
            final TopicItemInfoDTO[] itemInfos, final String removeItemIds,
            final String relateIds, final String removeRelateIds,
            final TopicCouponChange[] topicCoupons, final String removeCouponIds,
            final Long userId, final String userName) {

        final ResultInfo result = new ResultInfo();
        this.execute(result, new Callback() {
            @Override
            public void process() throws Exception {
                ResultInfo resultInfo = saveOrSubmit(topicChangeDetail, itemInfos, removeItemIds,
                        relateIds, removeRelateIds, topicCoupons, removeCouponIds,
                        userId, userName, true);
                if (!resultInfo.isSuccess()) {
                    result.setMsg(resultInfo.getMsg());
                }
                result.setSuccess(resultInfo.isSuccess());
            }

        });

        return result;
    }

    /**
     * 保存或提交活动变更单
     *
     * @param topicChangeDetail
     * @param itemInfos
     * @param removeItemIds
     * @param relateIds
     * @param removeRelateIds
     * @param userId
     * @param userName
     * @param save
     * @return
     */
    private ResultInfo saveOrSubmit(TopicChangeDetailDTO topicChangeDetail,
                                    TopicItemInfoDTO[] itemInfos, String removeItemIds,
                                    String relateIds, String removeRelateIds,
                                    TopicCouponChange[] topicCoupons, String removeCouponIds,
                                    Long userId, String userName, boolean save) throws Exception {
        final ResultInfo result = new ResultInfo();
        this.execute(result, new Callback() {
            @Override
            public void process() throws Exception {
		        Long[] relateTopicChangeIds = null;
		        Long[] removeRelateTopicChangeIds = null;
		        Long[] removeTopicItemChangeIds = null;
		        Long[] removeTopicCouponIds = null;
		        if (null != relateIds && !StringUtils.isBlank(relateIds)) {
		            // 转换关联专题的Id信息
		            relateTopicChangeIds = (Long[]) JSONArray.toArray(
		                    JSONArray.fromObject(relateIds), Long.class);
		        }
		        if (null != relateIds && !StringUtils.isBlank(removeRelateIds)) {
		            // 转换待删除的关联信息Id
		            removeRelateTopicChangeIds = (Long[]) JSONArray.toArray(
		                    JSONArray.fromObject(removeRelateIds), Long.class);
		        }
		        if (null != removeItemIds && !StringUtils.isBlank(removeItemIds)) {
		            removeTopicItemChangeIds = (Long[]) JSONArray.toArray(
		                    JSONArray.fromObject(removeItemIds), Long.class);
		        }
		        if (null != removeCouponIds && !StringUtils.isBlank(removeCouponIds)) {
		            removeTopicCouponIds = (Long[]) JSONArray.toArray(
		                    JSONArray.fromObject(removeCouponIds), Long.class);
		        }
		        boolean validateHasForbidden = checkForbiddenWords(topicChangeDetail,
		                itemInfos);
		        if (validateHasForbidden) {
		            throw new ServiceException(ProcessingErrorMessage.HAS_FORBIDDEN_WORDS);
		        }
		        Long tcid = 0L;
		        if (topicChangeDetail.getTopic() != null) {
		            tcid = topicChangeDetail.getTopic().getId();
		        }
		        // 新增/保存 专题活动商品
		        Integer reserveInventoryFlag = topicChangeDetail.getTopic().getReserveInventoryFlag();
		        List<TopicItemChange> topicItemChanges = getTopicItemList(itemInfos,reserveInventoryFlag);
		        // 变更专题的信息
		        TopicChange topicChange = getTopicChange(topicChangeDetail);
		        // 获取优惠券列表
		        List<TopicCouponChange> coupons = getTopicCouponChangeList(tcid,
		                topicCoupons);
		        // 生成策略
		        PolicyChange policy = topicChangeDetail.getPolicy();
		        ResultInfo resultInfo = null;
		        if (save) {
		        	resultInfo = topicManagementService.saveTopicChange(topicChange, topicItemChanges,
		                    removeTopicItemChangeIds, relateTopicChangeIds,
		                    removeRelateTopicChangeIds, coupons,
		                    removeTopicCouponIds, policy, userId, userName);
		        } else {
		        	resultInfo = topicManagementService.submitTopicChange(topicChange, topicItemChanges,
		                    removeTopicItemChangeIds, relateTopicChangeIds,
		                    removeRelateTopicChangeIds, coupons,
		                    removeTopicCouponIds, policy, userId, userName);
		        }
		        if(resultInfo.isSuccess()){
                	result.setSuccess(Boolean.TRUE);
                }else{
                	result.setMsg(resultInfo.getMsg());
                }
            }
        });

        return result;
    }

    /**
     * 拼装活动信息针对界面冗余字段
     *
     * @param topicDetail
     * @return
     */
    private TopicChange getTopicChange(TopicChangeDetailDTO topicDetail) {
        TopicChange topicChange = topicDetail.getTopic();
        topicChange.setAreaStr(topicDetail.getArea());
        // topicChange.setPlatformStr(topicDetail.getPlatform());
        topicChange.setStatus(TopicConstant.TOPIC_STATUS_EDITING);

        List<Integer> platforms = topicDetail.getPlatformCodes();
        StringBuilder p = new StringBuilder();
        if (!CollectionUtils.isEmpty(platforms)) {
            for (Integer i : platforms) {
                if (i == null) continue;
                p.append(i);
                p.append(",");
            }
            p.deleteCharAt(p.lastIndexOf(","));
        }
        topicChange.setPlatformStr(p.toString());

        if (topicChange.getType() != TopicType.THEME.ordinal()) {
            //注掉
           // topicChange.setMobileImage(topicChange.getPcImage());
        }
        if (topicChange.getType() != TopicType.SINGLE.ordinal()) {
            topicChange.setPcInterestImage(topicChange.getPcImage());
        }
        topicChange.setPromoterIdList(topicDetail.getPromoterIdList());
        return topicChange;
    }

    /**
     * 检查活动信息和商品信息
     *
     * @param topic     活动信息
     * @param itemInfos 活动商品信息
     * @return
     */
    private boolean checkForbiddenWords(TopicChangeDetailDTO topic,
                                        TopicItemInfoDTO[] itemInfos) {
        if (null == topic) {
            throw new ServiceException(
                    ProcessingErrorMessage.VALID_TOPIC_ID);
        }
        // TODO:性能优化
        List<String> forbiddenWords = getForbiddenWords();
        boolean topicHasForbidden = validateHasForbiddenWordsInTopic(topic,
                forbiddenWords);
        if (topicHasForbidden) {
            return topicHasForbidden;
        }
        for (String word : forbiddenWords) {
            topicHasForbidden = validateTopicItemForbiddenWords(
                    Arrays.asList(itemInfos), word);
            if (topicHasForbidden) {
                return topicHasForbidden;
            }
        }
        return topicHasForbidden;
    }

    /**
     * 检查违禁词
     *
     * @param topic 活动信息
     * @param words 违禁词列表
     * @return
     */
    private boolean validateHasForbiddenWordsInTopic(
            TopicChangeDetailDTO topic, List<String> words) {
        boolean hasForbidden = false;
        // TODO:性能优化
        for (String word : words) {
            // Topic信息校验
            if (!hasForbidden && null != topic.getTopic().getAreaStr()) {
                hasForbidden = topic.getTopic().getAreaStr().contains(word);
            }
            if (!hasForbidden && null != topic.getArea()) {
                hasForbidden = topic.getArea().contains(word);
            }
            if (!hasForbidden && null != topic.getTopic().getPlatformStr()) {
                hasForbidden = topic.getTopic().getPlatformStr().contains(word);
            }
            if (!hasForbidden && null != topic.getPlatform()) {
                hasForbidden = topic.getPlatform().contains(word);
            }
            if (!hasForbidden && null != topic.getTopic().getName()) {
                hasForbidden = topic.getTopic().getName().contains(word);
            }
//            if (!hasForbidden && null != topic.getTopic().getNumber()) {
//                hasForbidden = topic.getTopic().getNumber().contains(word);
//            }
            if (!hasForbidden && null != topic.getTopic().getBrandName()) {
                hasForbidden = topic.getTopic().getBrandName().contains(word);
            }
            if (!hasForbidden && null != topic.getTopic().getDiscount()) {
                hasForbidden = topic.getTopic().getDiscount().contains(word);
            }
            if (!hasForbidden && null != topic.getTopic().getIntro()) {
                hasForbidden = topic.getTopic().getIntro().contains(word);
            }
            if (!hasForbidden && null != topic.getTopic().getIntroMobile()) {
                hasForbidden = topic.getTopic().getIntroMobile().contains(word);
            }
            if (!hasForbidden && null != topic.getTopic().getRemark()) {
                hasForbidden = topic.getTopic().getRemark().contains(word);
            }
            if (hasForbidden) {
                break;
            }
            if (null == topic.getTopicItemDtoList()
                    || topic.getTopicItemDtoList().size() == 0) {
                continue;
            }
            hasForbidden = validateTopicItemForbiddenWords(
                    topic.getTopicItemDtoList(), word);
            if (hasForbidden) {
                break;
            }
        }
        return hasForbidden;
    }

    /**
     * @param word
     * @return
     */
    private boolean validateTopicItemForbiddenWords(
            List<TopicItemInfoDTO> itemInfos, String word) {
        boolean hasForbidden = false;
        // Topic商品信息校验
        for (TopicItemInfoDTO topicItem : itemInfos) {
            if (!hasForbidden && null != topicItem.getRemark()) {
                hasForbidden = topicItem.getRemark().contains(word);
            }
            if (!hasForbidden && null != topicItem.getSku()) {
                hasForbidden = topicItem.getSku().contains(word);
            }
            if (!hasForbidden && null != topicItem.getSpu()) {
                hasForbidden = topicItem.getSpu().contains(word);
            }
            if (!hasForbidden && null != topicItem.getBarCode()) {
                hasForbidden = topicItem.getBarCode().contains(word);
            }
            if (!hasForbidden && null != topicItem.getItemSpec()) {
                hasForbidden = topicItem.getItemSpec().contains(word);
            }
            if (!hasForbidden && null != topicItem.getSupplierName()) {
                hasForbidden = topicItem.getSupplierName().contains(word);
            }
            if (!hasForbidden && null != topicItem.getStockLocation()) {
                hasForbidden = topicItem.getStockLocation().contains(word);
            }
            if (!hasForbidden && null != topicItem.getName()) {
                hasForbidden = topicItem.getName().contains(word);
            }
            if (!hasForbidden && null != topicItem.getTopicName()) {
                hasForbidden = topicItem.getTopicName().contains(word);
            }
            if (hasForbidden) {
                break;
            }
        }
        return hasForbidden;
    }

    /**
     * 获得违禁词列表
     *
     * @return
     */
    private List<String> getForbiddenWords() {
        ForbiddenWords forbiddenDO = new ForbiddenWords();
        forbiddenDO.setStatus(1);
        List<ForbiddenWords> wordsDO = forbiddenWordsService.queryByObject(forbiddenDO);
        List<String> forbiddenWords = new ArrayList<String>();
        for (ForbiddenWords ForbiddenWords : wordsDO) {
            forbiddenWords.add(ForbiddenWords.getWords());
        }
        return forbiddenWords;
    }

    /**
     * @param itemInfos
     * @return
     */
    private List<TopicItemChange> getTopicItemList(
            TopicItemInfoDTO[] itemInfos,Integer reserveInventoryFlag) {
        List<TopicItemChange> topicItems = new ArrayList<TopicItemChange>();
        if (null != itemInfos && itemInfos.length > 0) {
            for (TopicItemInfoDTO topicItemDTO : itemInfos) {
                TopicItemChange topicItem = topicItemDTO.getTopicItemChange();
                topicItem.setDeletion(DeletionStatus.NORMAL.ordinal());
                topicItems.add(topicItem);
                // 活动是否预占库存：0否1是
                if(reserveInventoryFlag==0){
                	topicItem.setLimitTotal(0);
                }
            }
        }
        return topicItems;
    }

    private List<TopicCouponChange> getTopicCouponChangeList(Long tid,
                                                             TopicCouponChange[] couponDOS) {
        List<TopicCouponChange> couponChanges = new ArrayList<TopicCouponChange>();
        if (null != couponDOS && couponDOS.length > 0) {
            for (TopicCouponChange coupon : couponDOS) {
                coupon.setTopicChangeId(tid);
                couponChanges.add(coupon);
            }
        }
        return couponChanges;
    }


}
