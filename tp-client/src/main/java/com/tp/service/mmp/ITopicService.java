package com.tp.service.mmp;

import java.util.Date;
import java.util.List;

import com.tp.common.vo.PageInfo;
import com.tp.dto.common.ResultInfo;
import com.tp.dto.mem.BrandCollection;
import com.tp.dto.mem.ItemCollection;
import com.tp.dto.mmp.SkuTopicDTO;
import com.tp.dto.mmp.TopicDetailDTO;
import com.tp.dto.mmp.TopicItemBrandCategoryDTO;
import com.tp.dto.mmp.TopicQueryDTO;
import com.tp.dto.ord.CartDTO;
import com.tp.dto.ord.ItemInventoryDTO;
import com.tp.dto.ord.remote.TopicPolicyDTO;
import com.tp.model.mmp.PolicyInfo;
import com.tp.model.mmp.Topic;
import com.tp.model.mmp.TopicItem;
import com.tp.query.mmp.CmsTopicQuery;
import com.tp.query.mmp.CmsTopicSimpleQuery;
import com.tp.query.mmp.ItemQuery;
import com.tp.query.mmp.TopicItemCartQuery;
import com.tp.query.mmp.TopicItemInfoQuery;
import com.tp.query.mmp.TopicItemPageQuery;
import com.tp.result.mmp.TopicInfo;
import com.tp.result.mmp.TopicItemInfoResult;
import com.tp.service.IBaseService;

/**
  * @author szy
  * 促销活动接口
  */
 public  interface ITopicService extends IBaseService<Topic>{


    /**
     * 根据专题id，获得专题的详细信息
     *
     * @param tid
     * @return
     */
    TopicDetailDTO getTopicDetailById(Long promoterid, long tid) throws Exception;

    /**
     * 根据专题id，获得专题的详细信息(CMS)
     * 返回活动状态 是审批通过，进度是进行中的活动
     * @param tids
     * @return 如果
     */
    TopicDetailDTO getSingleTopicDetailByIdsForCms(List<Long> tids) throws Exception;

    /**
     * 根据专题id，获得专题的详细信息(CMS)
     * 返回活动状态 是审批通过，进度是进行中的活动
     * @param tids
     * @return 如果
     */
    List<TopicDetailDTO> getTopicDetailByIdsForCms(Long promoterid, List<Long> tids) throws Exception;

    
    /**
     * 根据专题id，获得专题的详细信息(清单返回TopicItemInfoDTO)
     *
     * @param tid
     * @return
     */
    TopicDetailDTO getTopicDetailWithItemDTOById(long tid) throws Exception;

    /**
     * 根据topicIDList 获得 活动集合
     *
     * @param topicIdList
     * @return
     */
     List<TopicDetailDTO> queryTopicDetailList(List<Long> topicIdList) throws Exception;

    /**
     * cms模块，根据查询条件查询活动信息
     *
     * @param query
     * @return
     * @throws Exception
     */
    PageInfo<TopicDetailDTO> getCmsTopicList(CmsTopicQuery query) throws Exception;

    /**
     * 明日预告
     */
    PageInfo<TopicDetailDTO> getTomorrowForecast(CmsTopicSimpleQuery query) throws Exception;

    /**
     * 最后疯抢 24小时结束的专题
     */
    PageInfo<TopicDetailDTO> getTodayLastRash(CmsTopicSimpleQuery query) throws Exception;

    /**
     * 今日特卖
    
     * @return
     * @throws Exception
     */
     PageInfo<TopicDetailDTO> getTodayTopic(CmsTopicSimpleQuery query) throws Exception;
    /***
     * 今日必海淘（单品团）
     * @param pageId
     * @param pageSize
     * @return
     * @throws Exception
     */
     PageInfo<TopicDetailDTO> getTodaySingleTopic(CmsTopicSimpleQuery query) throws Exception;
    /**
     * 今日上线 （移动端） 单品团 + 非单品团
     * @param pageId
     * @param pageSize
     * @return
     * @throws Exception
     */
     PageInfo<TopicDetailDTO> getTodayAllTopic(CmsTopicSimpleQuery query) throws Exception;
    /**
     * 商品详细页 根据专题id、 商品sku List、 地区 ，获得商品在你本地区的本专题的信息
     */
     List<TopicItemInfoResult> getTopicItemInfo(TopicItemInfoQuery query);

    /**
     * 商品详细页面查询商品的关联（联想）商品
     *
     * @param topicId
     * @param sku
     * @return
     */
     List<TopicItemInfoResult> queryItemRelateTopicItem(long topicId, String sku);

    /**
     * 减库存
     *

     * @return
     */
    ResultInfo reduceStock(List<ItemQuery> itemQueries);

    /**
     * 增加库存
     *
  
     * @return
     */
    ResultInfo addStock(List<ItemQuery> itemQueries);

    /**
     * 检查库存
     *
    
     * @return
     */
    ResultInfo checkStock(List<ItemQuery> itemQueries);

    /**
     * 购物车专题优惠信息
     *
     */
    CartDTO cartValidate(CartDTO cartDTO);

    /**
     * 验证单个商品信息
     */
    ResultInfo<Boolean> validateSingleTopicItem(TopicItemCartQuery query) ;

    /**
     * 验证多个商品信息
     */
    ResultInfo validateTopicItemList(List<TopicItemCartQuery> query) ;

    /**
     * 我关注的专题
     */
    List<TopicInfo> queryTopicList(List<Long> topicIdList);

    /**
     * 分页查询，部分String字段支持like查询
     *
     * @param Topic
     * @return
     */
    List<Topic> selectDynamicPageQueryWithLike(TopicQueryDTO Topic);

    /**
     * 查询除传入Id外，其他专题信息 (专题状态为已审核)
     *
     * @param paramMap
     * @return
     */
    PageInfo<Topic> getTopicsWithoutSpecialId(Long specialTopicId, String number, String name, Integer startPage,
                                            Integer pageSize);

    /**
     * 获取除指定id之外的专题信息数量
     *
     * @param specialTopicId
     * @param number
     * @param name
     * @return
     */
    Long getTopicsWithoutSpecialIdCount(Long specialTopicId, String number, String name);

     Long selectCountDynamicWithLike(TopicQueryDTO topicDO);

    /**
     * 支持name和number字段Like查询
     *
     * @param Topic
     * @return
     */
    PageInfo<Topic> queryPageListByTopicDOWithLike(TopicQueryDTO Topic);

    /**
     * 支持name和number字段Like查询的分页查询
     *
     * @param Topic
     * @param startPage
     * @param pageSize
     * @return
     */
    PageInfo<Topic> queryPageListByTopicDOAndStartPageSizeWithLike(TopicQueryDTO Topic, int startPage, int pageSize);

    /**
     * 保存Topic信息及其明细信息
     *
     * @param topic
     * @param policy
     * @param topicItems
     * @param removeTopicItems
     * @param relateTopicIds
     * @param removeRelateTopicIds
     */
    ResultInfo saveTopicInfo(Topic topic, PolicyInfo policy, List<TopicItem> topicItems,
                                List<TopicItem> removeTopicItems, Long[] relateTopicIds, Long[] removeRelateTopicIds) throws Exception;

    /**
     * 获取最大排序序号
     *
     * @return
     */
    Integer getMaxTopicInfoSortIndex();

    /**
     * 查询专题下根据品牌 & 分类 & 价格排序 & 库存的查询
     */
     PageInfo<TopicItemInfoResult> queryTopicPageItemByCondition(TopicItemPageQuery query) throws Exception;

    /**
     * 查询某个专题下的 品牌列表 & 商品分类Id 列表
     *
     */
     TopicItemBrandCategoryDTO queryTopicItemBrandAndCategoryList(long topicId) throws Exception;

    /**
     * cms 查询关联活动
     */
     List<TopicInfo> queryTopicRelate(long topicId, int size);

    /**
     * 查询最新的几个单品团
     */
     List<TopicItemInfoResult> queryLastestSingleTopic(int size) throws Exception;

    /**
     * 某个sku 有多少即将开始的专题 有多少在卖的专题
     **/
     SkuTopicDTO queryTopicListBySku(String sku, int days) throws Exception;

    /** 自动扫描更新活动状态 */
     ResultInfo scanTopicStatus(Date rangeTime);

    /**检查哪些sku在活动中*/
     List<String> checkTopicSkuList(Long topickId, List<String> skuList) throws Exception;

    /***
     * 查询海淘的基本信息
     */
     Topic queryTunInfo() throws Exception;

    /**
     * 获得海淘下面的商品信息
     * 查询tun下根据品牌 & 分类 & 价格排序 & 库存的查询
     */
     PageInfo<TopicItemInfoResult> queryTunTopicItem(TopicItemPageQuery query) throws Exception;

    /**
     * 查询某个专题下 在品牌列表范围内 的 品牌列表 & 商品分类Id 列表
     *
     */
     TopicItemBrandCategoryDTO queryTopicItemBrandList(long topicId, List<Long> brandIds) throws Exception;

    /**
     * 查询海淘下的 品牌列表 & 商品分类Id 列表
     *
     */
     TopicItemBrandCategoryDTO queryTunBrandAndCategoryList(long topicId) throws Exception;

    /***
     * 根据 id list 查询 topic list
     * @param topicIdList
     * @return
     */
     List<Topic> queryTopicInList(List<Long> topicIdList);

    /**
     * 关注活动验证活动是否状态正常
     * @param topicId
     * @return
     * @throws Exception
     */
     Boolean checkFavouriteTopicStatus(Long topicId) throws Exception;

    /**
     *  验证收藏活动的活动状态
     *
     * @param topicId
     * @return
     * @throws Exception
     */
     Boolean checkCollectTopicStatus(Long topicId) throws Exception;

    /**
     * 根据收藏的商品，返回可用的专题id
     *
     * @param topicId
     * @param sku
     * @return
     * @throws Exception
     */
     List<ItemCollection> queryCollectItemTopicId(List<ItemCollection> itemList) throws Exception;

    /**
     * 根据收藏的品牌，返回可用的专题
     *
    
     * @return
     * @throws Exception
     */
     List<BrandCollection> queryCollectBrandTopicId(List<BrandCollection> brandList) throws Exception;

    /**
     * 获取商品限购信息
     *
     * @param queryPolicyInfos
     * @return
     * @throws Exception
     */
     List<TopicPolicyDTO> queryTopicPolicyInfo(List<TopicItemCartQuery> queryPolicyInfos) throws Exception;

    /**
     * 根据专题id，获得专题的详细信息(商品列表之获取锁定商品)
     *
     * @param tid
     * @return
     */
    TopicDetailDTO getTopicDetailByIdWithLockItem(Long tid) throws Exception;

     List<ItemInventoryDTO> queryItemInventory(List<ItemInventoryDTO> itemInventoryList);
     
     List<TopicItem> queryTopicItemInfoByTopicIdAndSku(List<TopicItemCartQuery> queryPolicyInfos);
}
