package com.tp.m.ao.search;


import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.vo.PageInfo;
import com.tp.dto.common.ResultInfo;
import com.tp.dto.mmp.enums.DeletionStatus;
import com.tp.dto.mmp.enums.SalesPartten;
import com.tp.dto.mmp.enums.TopicStatus;
import com.tp.dto.mmp.enums.TopicType;
import com.tp.dto.sch.Nav;
import com.tp.m.base.MResultVO;
import com.tp.m.base.Page;
import com.tp.m.base.PageForSearch;
import com.tp.m.convert.SearchConvert;
import com.tp.m.enums.MResultInfo;
import com.tp.m.query.search.QuerySearch;
import com.tp.m.vo.search.NavigationVO;
import com.tp.m.vo.search.SearchConditionVO;
import com.tp.m.vo.search.SearchItemVO;
import com.tp.m.vo.search.SearchShopVO;
import com.tp.model.mmp.Topic;
import com.tp.model.sch.result.Aggregate;
import com.tp.model.sch.result.ItemResult;
import com.tp.model.sch.result.ShopResult;
import com.tp.proxy.bse.NavigationCategoryFacadeProxy;
import com.tp.proxy.mmp.TopicProxy;
import com.tp.proxy.sch.SearchProxy;
import com.tp.query.sch.SearchQuery;
import com.tp.util.JsonUtil;

import org.springframework.util.CollectionUtils;

/**
 * 搜索业务层
 *
 * @author zhuss
 * @2016年3月2日 上午11:42:27
 */
@Service
public class SearchAO {

    private final Logger log = LoggerFactory.getLogger(SearchAO.class);

    @Autowired
    private NavigationCategoryFacadeProxy navigationCategoryFacadeProxy;

    @Autowired
    private SearchProxy searchProxy;

    @Autowired
    private TopicProxy topicProxy;

    /**
     * 搜索-导航(分类和品牌)
     *
     * @param
     * @return
     */
    public MResultVO<NavigationVO> navigation() {
        try {
            ResultInfo<Nav> result = navigationCategoryFacadeProxy.getNavigation();
            if (result.isSuccess())
                return new MResultVO<>(MResultInfo.SUCCESS, SearchConvert.convertNavigation(result.getData()));
            return new MResultVO<>(result.getMsg().getMessage());
        } catch (Exception e) {
            log.error("[API接口 - 搜索导航 Exception] = {}", e);
            return new MResultVO<>(MResultInfo.CONN_ERROR);
        }
    }

    /**
     * 执行搜索
     *
     * @param
     * @return
     */
    public MResultVO<PageForSearch<SearchItemVO, List<SearchShopVO>>> search(QuerySearch search) {
        try {
            SearchQuery query = SearchConvert.convertSearchParam(search);
            ResultInfo<PageInfo<ItemResult>> result = searchProxy.search(query);
            ResultInfo<ShopResult> shopResult = new ResultInfo<>();
            if(query.getSalesPattern() == null || !query.getSalesPattern().equals(SalesPartten.OFF_LINE_GROUP_BUY.getValue())){
             // shopResult =searchProxy.searchShop(query);
            }
            //Long topicId = getCorrespondingTopicIdBySupplierId(shopResult);
            if (result.isSuccess())
                return new MResultVO<>(MResultInfo.SUCCESS, SearchConvert.convertSearchItem(result.getData(), shopResult.getData(), null));

            return new MResultVO<>(result.getMsg().getMessage());
        } catch (Exception e) {
            log.error("[API接口 - 执行搜索 Exception] = {}", e);
            return new MResultVO<>(MResultInfo.CONN_ERROR);
        }
    }

    private Long getCorrespondingTopicIdBySupplierId(ResultInfo<ShopResult> shopResult) {
        try {
            if (!shopResult.isSuccess() || shopResult.getData() == null ) {
                log.error("GET_CORRESPONDING_TOPIC_ID_ERROR:SHOP_RESULT_FAILED:" + JsonUtil.convertObjToStr(shopResult));
                return null;
            }
            Long supplierId = Long.parseLong(shopResult.getData().getSupplier_id());
            Topic topic = new Topic();
            topic.setStatus(TopicStatus.PASSED.ordinal());
            topic.setType(TopicType.SUPPLIER_SHOP.ordinal());
            topic.setDeletion(DeletionStatus.NORMAL.ordinal());
            topic.setSalesPartten(SalesPartten.FLAGSHIP_STORE.getValue());
            topic.setSupplierId(supplierId);
            ResultInfo<List<Topic>> topicListResult = topicProxy.queryByObject(topic);
            if (!topicListResult.isSuccess() ||  CollectionUtils.isEmpty(topicListResult.getData())) {
                log.error("GET_CORRESPONDING_TOPIC_ID_ERROR:GET_TOPIC_LIST_FAILED:" + JsonUtil.convertObjToStr(topicListResult));
                return null;
            }
            Long topicId = topicListResult.getData().get(0).getId();
            return topicId;
        } catch (Exception e) {
            log.error("GET_CORRESPONDING_TOPIC_ID_ERROR:PARAM" + JsonUtil.convertObjToStr(shopResult));
            log.error("GET_CORRESPONDING_TOPIC_ID_ERROR", e);
            return null;
        }
    }

    /**
     * 搜索结果 - 筛选
     *
     * @param
     * @return
     */
    public MResultVO<List<SearchConditionVO>> condition(QuerySearch search) {
        try {
            ResultInfo<List<Aggregate>> result = searchProxy.aggregate(SearchConvert.convertSearchParam(search));
            if (result.isSuccess())
                return new MResultVO<>(MResultInfo.SUCCESS, SearchConvert.convertSearchCondition(result.getData()));
            return new MResultVO<>(result.getMsg().getMessage());
        } catch (Exception e) {
            log.error("[API接口 - 搜索结果 - 筛选 Exception] = {}", e);
            return new MResultVO<>(MResultInfo.CONN_ERROR);
        }
    }
}
