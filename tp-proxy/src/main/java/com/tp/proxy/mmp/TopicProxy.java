package com.tp.proxy.mmp;

import com.tp.common.util.ImageUtil;
import com.tp.common.vo.Constant;
import com.tp.common.vo.Constant.DEFAULTED;
import com.tp.common.vo.PageInfo;
import com.tp.common.vo.StorageConstant;
import com.tp.common.vo.StorageConstant.App;
import com.tp.common.vo.mmp.ProcessingErrorMessage;
import com.tp.common.vo.mmp.TopicConstant;
import com.tp.dto.common.ResultInfo;
import com.tp.dto.mmp.TopicCouponDTO;
import com.tp.dto.mmp.TopicDetailDTO;
import com.tp.dto.mmp.TopicItemInfoDTO;
import com.tp.dto.mmp.TopicQueryDTO;
import com.tp.dto.mmp.enums.DeletionStatus;
import com.tp.dto.mmp.enums.TopicStatus;
import com.tp.dto.mmp.enums.TopicType;
import com.tp.dto.stg.InventoryDto;
import com.tp.dto.stg.query.InventoryDtoQuery;
import com.tp.dto.stg.query.InventoryQuery;
import com.tp.dto.stg.query.SkuInventoryQuery;
import com.tp.exception.ServiceException;
import com.tp.model.bse.Brand;
import com.tp.model.bse.ForbiddenWords;
import com.tp.model.mmp.PolicyInfo;
import com.tp.model.mmp.Topic;
import com.tp.model.mmp.TopicCoupon;
import com.tp.model.mmp.TopicItem;
import com.tp.model.prd.ItemInfo;
import com.tp.proxy.BaseProxy;
import com.tp.proxy.mmp.callBack.Callback;
import com.tp.query.mmp.TopicItemInfoQuery;
import com.tp.result.mmp.TopicItemInfoResult;
import com.tp.service.IBaseService;
import com.tp.service.bse.IBrandService;
import com.tp.service.bse.IForbiddenWordsService;
import com.tp.service.mmp.*;
import com.tp.service.prd.IItemInfoService;
import com.tp.service.stg.IInventoryOperService;
import com.tp.service.stg.IInventoryQueryService;

import net.sf.json.JSONArray;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;

/**
 * 促销活动代理层
 *
 * @author szy
 */
@Service
public class TopicProxy extends BaseProxy<Topic> {

    Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private IItemInfoService infoService;

    @Autowired
    private ITopicItemService topicItemService;

    @Autowired
    private IInventoryOperService inventoryOperService;

    @Autowired
    private IInventoryQueryService inventoryQueryService;

    @Autowired
    private IPolicyInfoService policyService;

    @Autowired
    private IRelateService relateService;

    @Autowired
    private ITopicManagementService topicManageService;

    @Autowired
    private IBrandService brandService;

    @Autowired
    private IForbiddenWordsService forbiddenWordsService;

    @Autowired
    private ITopicService topicService;

    @Override
    public IBaseService<Topic> getService() {
        return topicService;
    }

    /**
     * test method
     *
     * @param id
     * @return
     */
    public Topic getTopicById(Long id) {
        return topicService.queryById(id);
    }

    public TopicItem getPromotionItemById(Long id) {
        return topicItemService.queryById(id);
    }

    /**
     * @param id
     * @return
     */
    public ItemInfo getItemInfoById(Long id) {
        ItemInfo ItemInfo = infoService.queryById(id);
        return ItemInfo;
    }

    /**
     * 创建专题 -添加商品
     *
     * @param topic
     * @return
     */
    public ResultInfo createTopic(final Topic topic) {
        final ResultInfo result = new ResultInfo();
        this.execute(result, new Callback() {
            @Override
            public void process() {
                if (checkForbiddenWordsForCreateTopic(topic)) {
                    throw new ServiceException(ProcessingErrorMessage.HAS_FORBIDDEN_WORDS);
                }
                ResultInfo resultInfo = topicManageService.createTopic(topic);
                result.setSuccess(resultInfo.isSuccess());
                result.setData(resultInfo.getData());
            }
        });
        return result;
    }

    /**
     * 商品加到专题
     *
     * @param itemDO
     * @param topicId
     * @return
     */
    public ResultInfo<TopicItem> addItem(final TopicItem itemDO, final long topicId) {
        final ResultInfo<TopicItem> result = new ResultInfo<>();
        this.execute(result, new Callback() {
            @Override
            public void process() {
                Date now = new Date();
                itemDO.setCreateTime(now);
                itemDO.setUpdateTime(now);
                itemDO.setDeletion(DeletionStatus.NORMAL.ordinal());
                itemDO.setTopicId(topicId);
                topicItemService.insert(itemDO);
                result.setData(itemDO);
            }
        });
        return result;
    }

    /**
     * 获得专题详细
     *
     * @param tid
     * @return
     */
    public ResultInfo<TopicDetailDTO> getTopicDetailById(final long tid) {
        final ResultInfo<TopicDetailDTO> result = new ResultInfo<>();
        this.execute(result, new Callback() {
            @Override
            public void process() throws Exception {
                TopicDetailDTO dto = topicService.getTopicDetailWithItemDTOById(tid);
                if (null != dto && null != dto.getTopic()) {
                    String pcImage = dto.getTopic().getImage();
                    String newImage = dto.getTopic().getImageNew();
                    String mobileImage = dto.getTopic().getImageMobile();
                    String interestedImage = dto.getTopic().getImageInterested();
                    String hitaoImage = dto.getTopic().getImageHitao();

                    String pcImageN = dto.getTopic().getPcImage();
                    String interestImageN = dto.getTopic()
                            .getPcInterestImage();
                    String mobileImageN = dto.getTopic().getMobileImage();
                    String mallImageN = dto.getTopic().getMallImage();
                    String haitaoImageN = dto.getTopic().getHaitaoImage();
                    // PC端图片
                    if (!StringUtils.isBlank(pcImage)) {
                        dto.setTopicPcImageFull(ImageUtil.getCMSImgFullUrl(pcImage));
                    }
                    // 明日预告图片
                    if (!StringUtils.isBlank(newImage)) {
                        dto.setTopicNewImageFull(ImageUtil.getCMSImgFullUrl(newImage));
                    }
                    // 移动端图片
                    if (!StringUtils.isBlank(mobileImage)) {
                        dto.setTopicMobileImageFull(ImageUtil.getCMSImgFullUrl(mobileImage));
                    }
                    // 感兴趣图片
                    if (!StringUtils.isBlank(interestedImage)) {
                        dto.setTopicInterestedImageFull(ImageUtil.getCMSImgFullUrl(interestedImage));
                    }
                    // 海淘图片
                    if (!StringUtils.isBlank(hitaoImage)) {
                        dto.setTopicHitaoImageFull(ImageUtil.getCMSImgFullUrl(hitaoImage));
                    }
                    // PC端图片(新)
                    if (!StringUtils.isBlank(pcImageN)) {
                        dto.setPcImageFull(ImageUtil.getCMSImgFullUrl(pcImageN));
                    }
                    // 商城图片(新)
                    if (!StringUtils.isBlank(mallImageN)) {
                        dto.setMallImageFull(ImageUtil.getCMSImgFullUrl(mallImageN));
                    }
                    // 移动图片(新)
                    if (!StringUtils.isBlank(mobileImageN)) {
                        dto.setMobileImageFull(ImageUtil.getCMSImgFullUrl(mobileImageN));
                    }
                    // 可能感兴趣图片(新)
                    if (!StringUtils.isBlank(interestImageN)) {
                        dto.setPcInterestImageFull(ImageUtil.getCMSImgFullUrl(interestImageN));
                    }
                    // 海淘图片(新)
                    if (!StringUtils.isBlank(haitaoImageN)) {
                        dto.setHaitaoImageFull(ImageUtil.getCMSImgFullUrl(haitaoImageN));
                    }
                    if (null != dto.getTopicItemDtoList()) {
                        List<InventoryQuery> queries = new ArrayList<InventoryQuery>();
                        List<SkuInventoryQuery> storageQueryList = new ArrayList<SkuInventoryQuery>();
                        for (TopicItemInfoDTO itemDto : dto.getTopicItemDtoList()) {
                            if(itemDto.getName()!=null){
                                itemDto.setName(itemDto.getName().replaceAll("\"","&quot;"));
                            }

                            if (null != itemDto.getSku()
                                    && null != itemDto.getStockLocationId()) {
                                InventoryQuery inventoryQuery = new InventoryQuery();
                                inventoryQuery.setSku(itemDto.getSku());
                                inventoryQuery.setWarehouseId(itemDto.getStockLocationId());
                                queries.add(inventoryQuery);
                                //用于查询专场内商品剩余库存
                                SkuInventoryQuery skuInventoryQuery = new SkuInventoryQuery();
                                skuInventoryQuery.setApp(App.PROMOTION);
                        		skuInventoryQuery.setBizId(dto.getTopic().getId().toString());
                        		skuInventoryQuery.setSku(itemDto.getSku());
                        		skuInventoryQuery.setWarehouseId(itemDto.getStockLocationId());
                        		skuInventoryQuery.setBizPreOccupy(DEFAULTED.YES.equals(dto.getTopic().getReserveInventoryFlag()));
                        		storageQueryList.add(skuInventoryQuery);
                            }
                            if (null != itemDto
                                    && !StringUtils.isBlank(itemDto.getTopicImage())) {
                                String filePath = itemDto.getTopicImage();
                                itemDto.setImageFullPath(ImageUtil.getImgFullUrl(Constant.IMAGE_URL_TYPE.item,filePath));
                            }
                        }
                        
                        // 循环填充最新库存
//                        List<InventoryDto> inventoryDtos = inventoryQueryService.selectAvailableForSaleBySkuAndWhIdList(queries);
                        List<InventoryDto> inventoryDtos = inventoryQueryService.queryAvailableInventory(queries);
                        if (inventoryDtos != null) {
                            Map<String, InventoryDto> inventoryDtoMap = new HashMap<String, InventoryDto>();
                            for (InventoryDto inventoryDto : inventoryDtos) {
                                if (null == inventoryDto.getWarehouseId() || null == inventoryDto.getSku()) {
                                    continue;
                                }
                                String inventoryKey = String.valueOf(inventoryDto
                                        .getWarehouseId())
                                        + "_"
                                        + inventoryDto.getSku();
                                inventoryDtoMap.put(inventoryKey, inventoryDto);
                            }
                            for (TopicItemInfoDTO itemDto : dto.getTopicItemDtoList()) {
                                if (null != itemDto.getSku()
                                        && null != itemDto.getStockLocationId()) {
                                    String inventoryKey = String.valueOf(itemDto
                                            .getStockLocationId())
                                            + "_"
                                            + itemDto.getSku();
                                    if (inventoryDtoMap.containsKey(inventoryKey)) {
                                        InventoryDto inventoryDto = inventoryDtoMap
                                                .get(inventoryKey);
                                        itemDto.setStockAmout(inventoryDto
                                                .getInventory());
                                    } else {
                                        itemDto.setStockAmout(-1);
                                    }
                                }
                            }
                        }
                        
                        //已审核通过的专场显示专场内商品剩余库存(已分配给专场的库存)
                        if(dto.getTopic().getStatus() != null && 
                        		TopicConstant.TOPIC_STATUS_AUDITED == dto.getTopic().getStatus() &&
                        		!CollectionUtils.isEmpty(storageQueryList)){
//                        	Map<String, Integer> inventoryQueryResult = inventoryQueryService.batchSelectInventory(storageQueryList);
                        	Map<String, Integer> inventoryQueryResult = inventoryQueryService.querySalableInventory(storageQueryList);
                        	for (TopicItemInfoDTO itemDto : dto.getTopicItemDtoList()) {
                        		if(itemDto.getSku() == null) continue;
                        		String key = dto.getTopic().getId() + StorageConstant.straight + itemDto.getSku();
                        		Integer inventory = inventoryQueryResult.get(key);
                        		if(inventory != null){
                        			itemDto.setRemainStock(inventory);
                        		}
                        	}
                        }
                    }
                    // 生成优惠券图片全路径
                    if (null != dto.getCouponList()
                            && dto.getCouponList().size() > 0) {
                        for (TopicCouponDTO couponDto : dto.getCouponList()) {
                            if (null != couponDto && !StringUtils.isBlank(couponDto.getCouponImage())) {
                                String filePath = couponDto.getCouponImage();
                                couponDto.setCouponFullImage(ImageUtil.getCMSImgFullUrl(filePath));
                            }
                        }
                    }
                }
                result.setData(dto);
            }
        });
        return result;
    }

    public List<TopicItemInfoResult> getTopicItemInfo(TopicItemInfoQuery query) {
        return topicService.getTopicItemInfo(query);
    }

    /**
     * 分页获取专题信息
     *
     * @param topic
     * @param currentPage
     * @param pageSize
     * @return
     */
    public ResultInfo<PageInfo<Topic>> getTopicInfosByPaged(final Topic topic, final int currentPage, final int pageSize) {
        final ResultInfo<PageInfo<Topic>> result = new ResultInfo<>();
        this.execute(result, new Callback() {
            @Override
            public void process() {
                PageInfo<Topic> page = topicService.queryPageByObject(topic, new PageInfo<Topic>(currentPage, pageSize));
                result.setData(page);
            }
        });
        return result;
    }

    /**
     * 分页获取专题信息，按Like
     *
     * @param topic
     * @param currentPage
     * @param pageSize
     * @return
     */
    public ResultInfo<PageInfo<Topic>> getTopicInfosByPagedWithLike(final TopicQueryDTO topic, final int currentPage, final int pageSize) {
        final ResultInfo<PageInfo<Topic>> result = new ResultInfo<>();
        this.execute(result, new Callback() {
            @Override
            public void process() {
                PageInfo<Topic> page = topicService.queryPageListByTopicDOAndStartPageSizeWithLike(topic, currentPage, pageSize);
                result.setData(page);
            }
        });
        return result;
    }

    /**
     * 复制指定专题
     *
     * @param topicId
     * @return
     */
    public ResultInfo copyTopic(final Long topicId, final Long userId, final String userName) {
        final ResultInfo result = new ResultInfo();
        this.execute(result, new Callback() {
            @Override
            public void process() throws Exception {
                ResultInfo resultInfo = topicManageService.copyTopic(topicId, userId, userName);
                result.setSuccess(resultInfo.isSuccess());
            }
        });
        return result;
    }

    /**
     * 保存专题相关信息
     *
     * @param topicDetail
     * @param itemInfos
     * @param removeItemIds
     * @param relateIds
     * @param removeRelateIds
     * @param topicStatus
     * @throws Exception
     */
    public ResultInfo saveTopicInfo(TopicDetailDTO topicDetail,
                                    TopicItemInfoDTO[] itemInfos, String removeItemIds,
                                    String relateIds, String removeRelateIds,
                                    TopicCoupon[] topicCoupons, String removeCouponIds, Long userId,
                                    String userName,Integer reserveInventoryFlag) {
        return saveOrSubmit(topicDetail, itemInfos, removeItemIds, relateIds,
                removeRelateIds, topicCoupons, removeCouponIds, userId,
                userName, true,reserveInventoryFlag);
    }

    /**
     * 保存专题相关信息
     *
     * @param topicDetail
     * @param itemInfos
     * @param removeItemIds
     * @param relateIds
     * @param removeRelateIds
     * @throws Exception
     */
    public ResultInfo submitTopicInfo(TopicDetailDTO topicDetail,
                                      TopicItemInfoDTO[] itemInfos, String removeItemIds,
                                      String relateIds, String removeRelateIds,
                                      TopicCoupon[] topicCoupons, String removeCouponIds, Long userId,
                                      String userName,Integer reserveInventoryFlag) {
        return saveOrSubmit(topicDetail, itemInfos, removeItemIds, relateIds,
                removeRelateIds, topicCoupons, removeCouponIds, userId,
                userName, false,reserveInventoryFlag);
    }

    private ResultInfo<Object> saveOrSubmit(final TopicDetailDTO topicDetail,
                                    final TopicItemInfoDTO[] itemInfos, final String removeItemIds,
                                    final String relateIds, final String removeRelateIds,
                                    final TopicCoupon[] topicCoupons, final String removeCouponIds, final Long userId,
                                    final String userName, final boolean save,Integer reserveInventoryFlag) {

        final ResultInfo result = new ResultInfo();
        this.execute(result, new Callback() {
            @Override
            public void process() throws Exception {
                Long[] relateTopicIds = null;
                Long[] removeRelateTopicIds = null;
                Long[] removeTopicItemIds = null;
                Long[] removeTopicCouponIds = null;
                if (null != relateIds && !StringUtils.isBlank(relateIds)) {
                    // 转换关联专题的Id信息
                    relateTopicIds = (Long[]) JSONArray.toArray(
                            JSONArray.fromObject(relateIds), Long.class);
                }
                if (null != relateIds && !StringUtils.isBlank(removeRelateIds)) {
                    // 转换待删除的关联信息Id
                    removeRelateTopicIds = (Long[]) JSONArray.toArray(
                            JSONArray.fromObject(removeRelateIds), Long.class);
                }
                if (null != removeItemIds && !StringUtils.isBlank(removeItemIds)) {
                    removeTopicItemIds = (Long[]) JSONArray.toArray(
                            JSONArray.fromObject(removeItemIds), Long.class);
                }
                if (null != removeCouponIds && !StringUtils.isBlank(removeCouponIds)) {
                    removeTopicCouponIds = (Long[]) JSONArray.toArray(
                            JSONArray.fromObject(removeCouponIds), Long.class);
                }
                boolean validateHasForbidden = checkForbiddenWords(topicDetail,
                        itemInfos);
                if (validateHasForbidden) {
                    throw new ServiceException(ProcessingErrorMessage.HAS_FORBIDDEN_WORDS);
                }
                Long tid = 0L;
                if (topicDetail.getTopic() != null) {
                    tid = topicDetail.getTopic().getId();
                }
                // 新增/保存 专题活动商品
                List<TopicItem> topicItems = getTopicItemList(itemInfos);
                // 新增/保存 专题活动商品
                List<TopicCoupon> coupons = getTopicCouponList(tid, topicCoupons);
                // 变更专题的信息
                ResultInfo<Topic> topicResult = getTopic(topicDetail);
                if (!topicResult.isSuccess() || topicResult.getData() == null) {
                    throw new ServiceException("获取专场信息失败");
                }
                // 生成策略
                PolicyInfo policy = topicDetail.getPolicy();

                ResultInfo resultInfo;
                if (save) {
                    resultInfo = topicManageService.saveTopic(topicResult.getData(), topicItems,
                            removeTopicItemIds, relateTopicIds,
                            removeRelateTopicIds, coupons, removeTopicCouponIds,
                            policy, userId, userName,reserveInventoryFlag);
                } else {
                    resultInfo = topicManageService.submitTopic(topicResult.getData(), topicItems,
                            removeTopicItemIds, relateTopicIds,
                            removeRelateTopicIds, coupons, removeTopicCouponIds,
                            policy, userId, userName,reserveInventoryFlag);
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
     * 取消指定专题活动
     *
     * @param topicId
     * @throws Exception
     */
    public ResultInfo cancelTopic(final Long topicId, final Long userId, final String userName) {
        final ResultInfo result = new ResultInfo();
        this.execute(result, new Callback() {
            @Override
            public void process() throws Exception {
                if (null == topicId || 0 == topicId) {
                    throw new ServiceException(ProcessingErrorMessage.VALID_TOPIC_ID);
                }
                ResultInfo resultInfo = topicManageService.cancelTopic(topicId, userId, userName);
                result.setSuccess(resultInfo.isSuccess());
            }
        });
        return result;
    }

    /**
     * 终止指定专题活动
     *
     * @param topicId
     * @throws Exception
     */
    public ResultInfo terminateTopic(final Long topicId, final Long userId, final String userName) {
        final ResultInfo result = new ResultInfo();
        this.execute(result, new Callback() {
            @Override
            public void process() throws Exception {
                if (null == topicId || 0 == topicId) {
                    throw new ServiceException(ProcessingErrorMessage.VALID_TOPIC_ID);
                }
                ResultInfo resultInfo = topicManageService.terminateTopic(topicId, userId, userName);
                result.setSuccess(resultInfo.isSuccess());
            }
        });
        return result;
    }

    /**
     * 批准指定专题
     *
     * @param topicId
     * @throws Exception
     */
    public ResultInfo approveTopic(final Long topicId, final Long userId, final String userName) {
        final ResultInfo result = new ResultInfo();
        this.execute(result, new Callback() {
            @Override
            public void process() throws Exception {
                if (null == topicId || 0 == topicId) {
                    throw new ServiceException(ProcessingErrorMessage.VALID_TOPIC_ID);
                }
                // TODO: 考虑是否需要下沉
                ResultInfo<TopicDetailDTO> topicDetailRes = getTopicDetailById(topicId);
                if (!topicDetailRes.isSuccess() || topicDetailRes.getData() == null) {
                    throw new ServiceException("获取专场详情失败,Id:" + topicId);
                }
                TopicDetailDTO detailDto = topicDetailRes.getData();
                boolean validateHasForbidden = checkForbiddenWords(detailDto);
                if (validateHasForbidden) {
                    throw new ServiceException(ProcessingErrorMessage.HAS_FORBIDDEN_WORDS);
                }
                ResultInfo resultInfo = topicManageService.approveTopic(topicId, userId, userName);
                result.setSuccess(resultInfo.isSuccess());
            }
        });
        return result;
    }

    /**
     * 驳回指定专题
     *
     * @param topicId
     */
    public ResultInfo refuseTopic(final Long topicId, final Long userId, final String userName) {
        final ResultInfo result = new ResultInfo();
        this.execute(result, new Callback() {
            @Override
            public void process() throws Exception {
                if (null == topicId || 0 == topicId) {
                    throw new ServiceException(ProcessingErrorMessage.VALID_TOPIC_ID);
                }
                ResultInfo resultInfo = topicManageService.refuseTopic(topicId, userId, userName);
                result.setSuccess(resultInfo.isSuccess());
            }
        });
        return result;
    }

    /**
     * 获取最大排序序号
     *
     * @return
     */
    public ResultInfo<Integer> getMaxTopicInfoSortIndex() {
        final ResultInfo<Integer> result = new ResultInfo<>();
        this.execute(result, new Callback() {
            @Override
            public void process() throws Exception {
                Integer max = topicManageService.getMaxTopicInfoSortIndex();
                result.setData(max);
            }
        });
        return result;
    }

    /**
     * 自动更新扫描活动状态
     *
     * @return
     */
    public ResultInfo scanTopicStatus() {
        final ResultInfo result = new ResultInfo();
        this.execute(result, new Callback() {
            @Override
            public void process() throws Exception {
                ResultInfo resultInfo = topicService.scanTopicStatus(new Date());
                result.setSuccess(resultInfo.isSuccess());
            }
        });
        return result;
    }

    /**
     * 查询获取专场活动品牌列表
     *
     * @return
     */
    public ResultInfo<PageInfo<Brand>> searchTopicBrand(final Brand brand) {
        final ResultInfo<PageInfo<Brand>> result = new ResultInfo<>();
        this.execute(result, new Callback() {
            @Override
            public void process() throws Exception {
                brand.setStatus(1);
                PageInfo<Brand> page = brandService.queryPageByObject(brand, new PageInfo<Brand>(brand.getStartPage(), brand.getPageSize()));
                result.setData(page);
            }
        });
        return result;
    }

    /**
     * 获取品牌信息
     *
     * @return
     */
    public ResultInfo<Brand> searchTopicBrandInfoById(final Long bid) {
        final ResultInfo<Brand> result = new ResultInfo<>();
        this.execute(result, new Callback() {
            @Override
            public void process() throws Exception {
                Brand brand = brandService.queryById(bid);
                result.setData(brand);
            }
        });
        return result;
    }

    /**
     * 获取活动前台的跳转链接
     *
     * @param topicInfos
     * @return
     */
    public ResultInfo<List<TopicDetailDTO>> getSingleProductSkuInfo(final List<Topic> topicInfos) {
        final ResultInfo<List<TopicDetailDTO>> result = new ResultInfo<>();
        this.execute(result, new Callback() {
            @Override
            public void process() throws Exception {
                // TopicDetailDTO topicDto = new TopicDetailDTO();
                List<Long> topicIds = new ArrayList<Long>();
                for (Topic topic : topicInfos) {
                    if (topic != null
                            && TopicConstant.TOPIC_TYPE_SINGLE == topic.getType()
                            && TopicStatus.PASSED.ordinal() == topic.getStatus()) {
                        topicIds.add(topic.getId());
                    }
                }
                Map<Long, String> singleTopicInfo = new HashMap<Long, String>();
                if (topicIds != null && topicIds.size() > 0) {
                    List<TopicItem> items = topicItemService.getTopicItemByTopicIds(topicIds);
                    if (items != null && items.size() > 0) {
                        for (TopicItem TopicItem : items) {
                            if (!singleTopicInfo.containsKey(TopicItem.getTopicId())) {
                                singleTopicInfo.put(TopicItem.getTopicId(),
                                        TopicItem.getSku());
                            }
                        }
                    }
                }
                List<TopicDetailDTO> detailDtos = new ArrayList<TopicDetailDTO>();
                for (Topic topic : topicInfos) {
                    TopicDetailDTO dto = new TopicDetailDTO();
                    dto.setTopic(topic);
                    if (singleTopicInfo.containsKey(topic.getId())) {
                        dto.setSkipLink("hd.htm?tid=" + topic.getId() + ".htm&sku="
                                + singleTopicInfo.get(topic.getId()));
                    } else {
                        if (topic.getType() == TopicType.SINGLE.ordinal()) {
                            dto.setSkipLink(null);
                        } else {
                            dto.setSkipLink("hd.htm?tid=" + topic.getId());
                        }
                    }
                    detailDtos.add(dto);
                }
                result.setData(detailDtos);
            }
        });
        return result;
    }

    /**
     * 拼装活动信息针对界面冗余字段
     *
     * @return
     */
    private ResultInfo<Topic> getTopic(final TopicDetailDTO topicDetail) {
        final ResultInfo<Topic> result = new ResultInfo<>();
        this.execute(result, new Callback() {
            @Override
            public void process() throws Exception {
                Topic topic = topicDetail.getTopic();
                topic.setAreaStr(topicDetail.getArea());
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
                topic.setPlatformStr(p.toString());
                topic.setStatus(TopicConstant.TOPIC_STATUS_EDITING);
                if (topic.getType() != TopicType.THEME.ordinal()) {
                    topic.setMobileImage(topic.getPcImage());
                }
                if (topic.getType() != TopicType.SINGLE.ordinal()) {
                    topic.setPcInterestImage(topic.getPcImage());
                }
                topic.setPromoterIdList(topicDetail.getPromoterIdList());
                result.setData(topic);
            }
        });
        return result;
    }

    /**
     * @param itemInfos
     * @return
     */
    private List<TopicItem> getTopicItemList(TopicItemInfoDTO[] itemInfos) {
        List<TopicItem> topicItems = new ArrayList<TopicItem>();
        if (null != itemInfos && itemInfos.length > 0) {
            for (TopicItemInfoDTO topicItemDTO : itemInfos) {
                TopicItem topicItem = topicItemDTO.getTopicItem();
                topicItem.setDeletion(DeletionStatus.NORMAL.ordinal());

                topicItems.add(topicItem);

            }
        }
        return topicItems;
    }

    /**
     * 生成优惠券列表
     *
     * @param tid
     * @param couponInfos
     * @return
     */
    private List<TopicCoupon> getTopicCouponList(Long tid,
                                                 TopicCoupon[] couponInfos) {
        List<TopicCoupon> coupons = new ArrayList<TopicCoupon>();
        if (null != couponInfos && couponInfos.length > 0) {
            for (TopicCoupon coupon : couponInfos) {
                coupon.setTopicId(tid);
                coupons.add(coupon);
            }
        }
        return coupons;
    }

    /**
     * 检查违禁词
     *
     * @param topic 活动信息
     * @return
     */
    private boolean checkForbiddenWordsForCreateTopic(Topic topic) {
        if (null == topic) {
            throw new ServiceException(
                    ProcessingErrorMessage.VALID_TOPIC_INFO);
        }
        List<String> forbiddenWords = getForbiddenWords();
        boolean hasForbidden = false;
        for (String word : forbiddenWords) {
            if (!hasForbidden && null != topic.getName()) {
                hasForbidden = topic.getName().contains(word);
            }
        }
        return hasForbidden;
    }

    /**
     * 检查违禁词
     *
     * @param topic 活动信息
     * @return
     */
    private boolean checkForbiddenWords(TopicDetailDTO topic) {
        if (null == topic || null == topic.getTopic()) {
            throw new ServiceException(
                    ProcessingErrorMessage.VALID_TOPIC_ID);
        }
        List<String> forbiddenWords = getForbiddenWords();
        return validateHasForbiddenWordsInTopic(topic, forbiddenWords);
    }

    /**
     * 检查活动信息和商品信息
     *
     * @param topic     活动信息
     * @param itemInfos 活动商品信息
     * @return
     */
    private boolean checkForbiddenWords(TopicDetailDTO topic,
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
    private boolean validateHasForbiddenWordsInTopic(TopicDetailDTO topic,
                                                     List<String> words) {
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
     * @param topic
     * @param hasForbidden
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
        List<ForbiddenWords> wordsDO = forbiddenWordsService
                .queryByObject(forbiddenDO);
        List<String> forbiddenWords = new ArrayList<String>();
        for (ForbiddenWords ForbiddenWords : wordsDO) {
            forbiddenWords.add(ForbiddenWords.getWords());
        }
        return forbiddenWords;
    }


}
