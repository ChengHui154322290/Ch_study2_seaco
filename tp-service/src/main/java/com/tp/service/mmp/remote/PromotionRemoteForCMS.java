/**
 *
 */
package com.tp.service.mmp.remote;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.vo.Constant.DEFAULTED;
import com.tp.common.vo.PageInfo;
import com.tp.common.vo.StorageConstant;
import com.tp.common.vo.StorageConstant.App;
import com.tp.common.vo.mmp.AreaConstant;
import com.tp.common.vo.mmp.TopicRedisKeyConstant;
import com.tp.dao.mmp.TopicDao;
import com.tp.dao.mmp.TopicItemDao;
import com.tp.dto.mmp.TopicDetailDTO;
import com.tp.dto.mmp.TopicItemBrandCategoryDTO;
import com.tp.dto.mmp.enums.LockStatus;
import com.tp.dto.mmp.enums.SalesPartten;
import com.tp.enums.common.PlatformEnum;
import com.tp.exception.ServiceException;
import com.tp.model.mmp.Topic;
import com.tp.model.mmp.TopicItem;
import com.tp.query.mmp.CmsTopicQuery;
import com.tp.redis.util.JedisCacheUtil;
import com.tp.result.mmp.TopicItemInfoResult;
import com.tp.service.bse.ICategoryService;
import com.tp.service.mmp.remote.IPromotionRemoteForCMS;
import com.tp.service.stg.IInventoryQueryService;

/**
 */
@Service(value = "promotionRemoteForCMS")
public class PromotionRemoteForCMS implements IPromotionRemoteForCMS {

    private Log logger = LogFactory.getLog(this.getClass());


    @Autowired
    private TopicDao topicDao;

    @Autowired
    private TopicItemDao topicItemDao;

    @Autowired
    private ICategoryService categoryService;

    @Autowired
    private IInventoryQueryService inventoryQueryService;

    @Autowired
    private JedisCacheUtil jedisCacheUtil;


    @SuppressWarnings("unchecked")
    @Override
    public PageInfo<TopicDetailDTO> getXGMallTopicList(CmsTopicQuery query)
            throws ServiceException {
        PageInfo<TopicDetailDTO> page = new PageInfo<TopicDetailDTO>();
        Integer platformType = query.getPlatformType();
        Integer areaId = query.getAreaId();
        Integer topicType = query.getTopicType();
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("salesPartten", SalesPartten.XG_STORE.getValue());
        paramMap.put("topicType", topicType);
        int pageId = query.getPageId();
        int pageSize = query.getPageSize();
        paramMap.put("start", (pageId > 1 ? (pageId - 1) * pageSize : 0));
        paramMap.put("pageSize", pageSize);
        String key = TopicRedisKeyConstant.PR_PROMOTION_XG_TOPIC_KEYS;
        if (platformType != null && platformType != PlatformEnum.ALL.getCode()) {// 全部平台为1取枚举
            paramMap.put("platformType", platformType);
            key += TopicRedisKeyConstant.TOPIC_INFO_SPLIT
                    + String.valueOf(platformType);
        }
        if (areaId != null && areaId != AreaConstant.AREA_ALL) {// 全部地区为9999999取枚举
            paramMap.put("areaId", areaId);
            key += TopicRedisKeyConstant.TOPIC_INFO_SPLIT
                    + String.valueOf(areaId);
        }
        key += TopicRedisKeyConstant.TOPIC_INFO_SPLIT
                + String.valueOf(topicType);
        key += TopicRedisKeyConstant.TOPIC_INFO_SPLIT + String.valueOf(pageId);
        key += TopicRedisKeyConstant.TOPIC_INFO_SPLIT
                + String.valueOf(pageSize);
        List<TopicDetailDTO> resList = new ArrayList<TopicDetailDTO>();
        if (jedisCacheUtil.keyExists(key)) {
            page = (PageInfo<TopicDetailDTO>) jedisCacheUtil.getCache(key);
            if (page != null) {
                if (logger.isDebugEnabled()) {
                    logger.info("[getXGMallTopicList]read cache");
                    logger.info("[getXGMallTopicList]processing end time:"
                            + new Date());
                }
                return page;
            }
        }
        List<Topic> doList = topicDao.queryTopicList(paramMap);
        Long totalCount = topicDao.countTopicList(paramMap);
        if (doList != null && doList.size() > 0) {
            if (logger.isDebugEnabled()) {
                logger.info("[getXGMallTopicList]getTodayTopic list size:"
                        + doList.size());
            }
            for (Topic Topic : doList) {
                TopicDetailDTO topicDetail = new TopicDetailDTO();
                topicDetail.setTopic(Topic);
                resList.add(topicDetail);
            }
        }
        page.setRows(resList);
        page.setPage(pageId);
        page.setSize(pageSize);
        page.setRecords(totalCount.intValue());
        boolean result = jedisCacheUtil.setCache(key, page,
                TopicRedisKeyConstant.COMMON_EXPIRE);
        if (logger.isDebugEnabled()) {
            if (!result) {
                logger.info("[getXGMallTopicList]save cache result:" + result);
            }
            logger.info("[getXGMallTopicList]read db");
            logger.info("[getXGMallTopicList]processing end time:" + new Date());
        }
        return page;
    }

    /*
     * (non-Javadoc)
     *
     * @see
     */
    @Override
    public PageInfo<TopicItemInfoResult> getXGMallItemList(CmsTopicQuery query)
            throws ServiceException {
        Long categoryId = query.getCategoryId();
        List<Long> categoryIds = getEndingCategoryId(categoryId);
        if (categoryIds == null || categoryIds.size() == 0) {
            logger.error("[getXGMallItemList]category id is empty");
            return null;
        }
        List<Long> topicIds = getXGTopicIds(query);
        if (topicIds == null || topicIds.size() == 0) {
            logger.error("[getXGMallItemList]mt store topic id is empty");
            return null;
        }
        Map<String, Object> paramMap = new HashMap<String, Object>();
        Integer pageId = query.getPageId();
        Integer pageSize = query.getPageSize();
        List<Long> brandIds = query.getBrandIds();
        Boolean stock = query.isStock();
        List<String> sortColumns = query.getOrderSortColumns();
        paramMap.put("topicIds", topicIds);
        paramMap.put("categoryIds", categoryIds);
        paramMap.put("start", (pageId > 1 ? (pageId - 1) * pageSize : 0));
        paramMap.put("pageSize", pageSize);
        if (brandIds != null && brandIds.size() > 0) {
            paramMap.put("brandIds", brandIds);
        }
        if (sortColumns != null && sortColumns.size() > 0) {
            paramMap.put("orderSortColumns", sortColumns);
            if (query.isDesc()) {
                paramMap.put("sortDirection", "desc");
            } else {
                paramMap.put("sortDirection", "asc");
            }
        }
        if (stock != null && stock) {
            paramMap.put("stock", stock);
        }

        PageInfo<TopicItemInfoResult> page = new PageInfo<>();

        List<TopicItem> list = topicItemDao.queryTopicPageItemByDynamicCondition(paramMap);
        List<TopicItemInfoResult> resultList = new ArrayList<>();
        if (list != null && list.size() > 0) {
            for (TopicItem pitemDO : list) {
                Topic topic = topicDao.queryById(pitemDO.getTopicId());
                if (null != topic) {
                    TopicItemInfoResult result = new TopicItemInfoResult();
                    result.setHasStock(true);
                    result.setSku(pitemDO.getSku());
                    result.setItemId(pitemDO.getItemId());
                    result.setStockAmount(pitemDO.getStockAmount());
                    result.setLimitAmount(pitemDO.getLimitAmount());
                    result.setTopicPrice(pitemDO.getTopicPrice());
                    result.setTopicImage(pitemDO.getTopicImage());
                    result.setItemName(pitemDO.getName());
                    result.setSalePrice(pitemDO.getSalePrice());
                    result.setSaledAmount(pitemDO.getSaledAmount());
                    if (pitemDO.getLockStatus() != null) {
                        result.setLockStatus(pitemDO.getLockStatus());
                        result.setHasStock(pitemDO.getLockStatus() != LockStatus.LOCK
                                .ordinal());
                    } else {
                        result.setLockStatus(LockStatus.UNLOCK.ordinal());
                    }
                    result.setTopicId(pitemDO.getTopicId());
                    result.setTopicName(topic.getName());
                    result.setTopicStatus(topic.getStatus());
                    result.setLastingType(topic.getLastingType());
                    result.setStartTime(topic.getStartTime());
                    result.setEndTime(topic.getEndTime());
                    result.setTopicType(topic.getType());
                    result.setStockLocationId(pitemDO.getStockLocationId());
                    result.setStockLocationName(pitemDO.getStockLocation());
                    result.setPurchaseMethod(pitemDO.getPurchaseMethod());
                    //活动是否预占库存：1是0否（5.5）
                    result.setTopicInventoryFlag(topic.getReserveInventoryFlag());
                    
                    // 提供首页库存是否已卖完
                    if (logger.isDebugEnabled()) {
                        logger.info("[TopicService.queryTopicPageItemByCondition]queryTopicPageItem query inventory remain stock:"
                                + result.isHasStock());
                    }
                    if (result.isHasStock()) {
                        if (logger.isDebugEnabled()) {
                            logger.info("[TopicService.queryTopicPageItemByCondition]queryTopicPageItem query inventory topicId:"
                                    + result.getTopicId());
                            logger.info("[TopicService.queryTopicPageItemByCondition]queryTopicPageItem query inventory sku:"
                                    + result.getSku());
                        }
                        boolean hasStock = false;
                        int remainStock = 0;
                        if (!StringUtils.isBlank(result.getSku())) {
                        	remainStock = inventoryQueryService.querySalableInventory(App.PROMOTION, String.valueOf(result.getTopicId()), result.getSku(),
                        			result.getStockLocationId(), DEFAULTED.YES.equals(result.getTopicInventoryFlag()));
//                            remainStock = inventoryQueryService
//                                    .selectInvetory(StorageConstant.App.PROMOTION, String
//                                                    .valueOf(result.getTopicId()),
//                                            result.getSku());
                            hasStock = remainStock > 0;
                        } else {
                            logger.error("[TopicService.queryTopicPageItemByCondition]queryTopicPageItem query sku is empty!");
                        }
                        if (logger.isDebugEnabled()) {
                            logger.info("[TopicService.queryTopicPageItemByCondition]queryTopicPageItem query inventory remain stock amount:"
                                    + remainStock);
                            logger.info("[TopicService.queryTopicPageItemByCondition]queryTopicPageItem query inventory remain stock:"
                                    + hasStock);
                        }
                        result.setHasStock(hasStock);
                    }

                    resultList.add(result);
                }
            }
        }
        page.setRows(resultList);
        page.setPage(pageId);
        page.setSize(pageSize);
        Long totalCount = topicItemDao.countTopicPageItemByDynamicCondition(paramMap);
        page.setRecords(totalCount.intValue());

        return page;
    }


    @Override
    public TopicItemBrandCategoryDTO getXGMallBrandList(CmsTopicQuery query)
            throws ServiceException {
        Long categoryId = query.getCategoryId();
        String key = TopicRedisKeyConstant.PR_PROMOTION_XG_BRAND_KEYS;
        key += TopicRedisKeyConstant.TOPIC_INFO_SPLIT
                + String.valueOf(categoryId);
        // JSONObject categoryObject = JSONObject.fromObject(categoryIds);
        // if (categoryObject != null) {
        // key += TopicRedisKeyConstant.TOPIC_INFO_SPLIT
        // + categoryObject.toString();
        // }
        TopicItemBrandCategoryDTO categoryDto = new TopicItemBrandCategoryDTO();
        if (jedisCacheUtil.keyExists(key)) {
            categoryDto = (TopicItemBrandCategoryDTO) jedisCacheUtil
                    .getCache(key);
            if (categoryDto != null && categoryDto.getBrandIdList() != null
                    && categoryDto.getBrandIdList().size() > 0) {
                if (logger.isDebugEnabled()) {
                    logger.info("[getXGMallBrandList]read cache");
                    logger.info("[getXGMallBrandList]read size:"
                            + categoryDto.getBrandIdList().size());
                    logger.info("[getXGMallBrandList]processing end time:"
                            + new Date());
                }
                return categoryDto;
            }
        }
        List<Long> categoryIds = getEndingCategoryId(categoryId);
        if (categoryIds == null || categoryIds.size() == 0) {
            logger.error("[getXGMallItemList]mt store category id is empty");
            return null;
        }
        List<Long> topicIds = getXGTopicIds(query);
        if (topicIds == null || topicIds.size() == 0) {
            logger.error("[getXGMallItemList]mt store topic id is empty");
            return null;
        }
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("categoryIds", categoryIds);
        paramMap.put("topicIds", topicIds);
        List<Long> brandIds = topicItemDao.getTopicItemBrandsByCategoryIds(
                paramMap);
        categoryDto.setBrandIdList(brandIds);
        boolean result = jedisCacheUtil.setCache(key, categoryDto,
                TopicRedisKeyConstant.COMMON_EXPIRE);
        if (logger.isDebugEnabled()) {
            if (!result) {
                logger.info("[getXGMallBrandList]save cache result:" + result);
            }
            logger.info("[getXGMallBrandList]read db");
            logger.info("[getXGMallBrandList]read size:" + brandIds.size());
            logger.info("[getXGMallBrandList]processing end time:" + new Date());
        }
        return categoryDto;
    }

    private List<Long> getEndingCategoryId(Long lv2CateId) {
        if (lv2CateId == null) {
            logger.error("[getXGMallItemList]category lv2 is null");
            // 无法取到二级分类时报错
            return null;
        }
        return categoryService.findSmallCateIdListById(lv2CateId);
    }

    @SuppressWarnings("unchecked")
    private List<Long> getXGTopicIds(CmsTopicQuery query) {
        Integer platformType = query.getPlatformType();
        Integer areaId = query.getAreaId();
        Map<String, Object> paramMap = new HashMap<String, Object>();
        String key = TopicRedisKeyConstant.PR_PROMOTION_XG_TOPIC_KEY_LIST_KEYS;
        paramMap.put("salesPartten", SalesPartten.XG_STORE.getValue());
        if (platformType != null && platformType != PlatformEnum.ALL.getCode()) {
            paramMap.put("platformType", platformType);
            key += TopicRedisKeyConstant.TOPIC_INFO_SPLIT
                    + String.valueOf(platformType);
        }
        if (areaId != null && areaId != AreaConstant.AREA_ALL) {
            paramMap.put("areaId", areaId);
            key += TopicRedisKeyConstant.TOPIC_INFO_SPLIT
                    + String.valueOf(areaId);
        }
        List<Long> topicIds = new ArrayList<Long>();
        if (jedisCacheUtil.keyExists(key)) {
            topicIds = (List<Long>) jedisCacheUtil.getCache(key);
            if (topicIds != null && topicIds.size() > 0) {
                if (logger.isDebugEnabled()) {
                    logger.info("[getXGTopicIds]read cache");
                    logger.info("[getXGTopicIds]read size" + topicIds.size());
                    logger.info("[getXGTopicIds]processing end time:"
                            + new Date());
                }
                return topicIds;
            }
        }
        topicIds = topicDao.getTopicIdList(paramMap);
        if (topicIds == null || topicIds.size() == 0) {
            logger.error("[getXGTopicIds]hasn't avaliable topic id");
        }
        boolean result = jedisCacheUtil.setCache(key, topicIds,
                TopicRedisKeyConstant.COMMON_EXPIRE);
        if (logger.isDebugEnabled()) {
            if (!result) {
                logger.info("[getXGTopicIds]save cache result:" + result);
            }
            logger.info("[getXGTopicIds]read db");
            logger.info("[getXGTopicIds]read size" + topicIds.size());
            logger.info("[getXGTopicIds]processing end time:" + new Date());
        }
        return topicIds;
    }
}
