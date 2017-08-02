package com.tp.dao.mmp;

import java.util.List;
import java.util.Map;

import com.tp.common.dao.BaseDao;
import com.tp.model.mmp.TopicItem;

import org.apache.ibatis.annotations.Param;

public interface TopicItemDao extends BaseDao<TopicItem> {

    List<TopicItem>   getTopicItemByIds(List<Long> ids);

    Integer getMaxTopicItemSortIndex(Long id);

    List<TopicItem> getTopicItemInfoBySku(TopicItem topicItem);

    List<TopicItem> queryTopicPageItemByDynamicCondition(Map<String,Object> param);

     <T> Long countTopicPageItemByDynamicCondition(Map<String,T> js);

     List<Long> getTopicItemBrandsByCategoryIds(Map<String,Object> param);

    /**
     * 查询专题下根据品牌 & 分类 & 价格排序 & 库存的查询
     */
    List<TopicItem> queryTopicPageItemByCondition(Map<String, Object> paramMap);


    /**
     * 統計专题下根据品牌 & 分类 & 价格排序 & 库存的查询
     */
    Long countTopicPageItemByCondition(Map<String, Object> paramMap);


    /**
     * 根据Id列表，获取sku集合
     *
     * @param ids
     * @return
     */
    List<String> getSkuListByIdList(List<Long> ids);

    
    

    /***
     * 验证哪些在专题中
     *
     * @param topicId
     * @param skuList
     * @return
     */
    List<String> checkTopicSkuList(@Param("topicId") Long topicId,@Param("skuList") List<String> skuList);


    /**
     * 根据TopicId和品牌Id查询活动商品信息
     *
     * @param topicId
     * @param brandIds
     * @return
     */
    List<TopicItem> getTopicItemByTopicIdAndBrands(Long topicId, List<Long> brandIds);


    /**
     * 根据Topic Id获取活动商品信息
     * @param topicIds
     * @param isReadSlave
     * @return
     */
    public List<TopicItem> getTopicItemByTopicIds(List<Long> topicIds);

    /**
     * 根据获取活动商品信息
     *
     * @param topicItems
     * @param isReadSlave
     * @return
     */
    public List<TopicItem> getTopicItemByTopicIdAndSku(List<TopicItem> list);

    /**
     * 根据SKU信息获取活动商品列表
     * @param skus
     * @param isReadSlave
     * @return
     */
    public List<TopicItem> getTopicItemBySkuList(List<String> skus);


    List<TopicItem> getTopicItemByIdsWithPage(Map<String,Object> param);

    Integer getTopicItemByIdsWithPageCount(Map<String,Object> param);


    /**
     * 获取活动商品列表
     * @param param
     * @return
     */
    List<TopicItem> getTopicItemFileterByDSS(Map<String, Object> param);

    
    /**
     * 经过dss过滤，获取活动商品列表
     * @param param
     * @return
     */
    List<TopicItem> getTopicItemFileterByDSS_2(Map<String, Object> param);
    
    

    /**
     * 根据sku找到 状态有效的活动商品
     * @param param
     * @return
     */
    List<TopicItem> getValidTopicItemBySku(String sku);
    /**
     * 取品牌团10个商品信息
     * @param topicIds
     * @return
     */
    List<TopicItem> getTopicItemByTopicId_Top10(List<Long> topicIds);


    /**
     * 取品牌团商品信息
     * @param topicIds
     * @return
     */
    List<TopicItem> getTopicItemByTopicId(List<Long> topicIds);
}
