package com.tp.service.sch;

import com.tp.common.vo.PageInfo;
import com.tp.dto.common.ResultInfo;
import com.tp.model.sch.result.Aggregate;
import com.tp.model.sch.result.ItemResult;
import com.tp.model.sch.result.ShopResult;
import com.tp.query.sch.SearchQuery;

import java.io.IOException;
import java.util.List;

/**
 * 搜索服务
  * @author szy 
  * 接口
  */
public interface ISearchService   {

    /**
     * 搜索和分类导航 获取商品信息
     * @param query
     * @return 商品信息
     * @throws IOException
     */
    ResultInfo<PageInfo<ItemResult>> search(SearchQuery query) throws  Exception;

    /**
     * 搜索和分类导航 获取统计信息
     * @param query
     * @return 统计信息
     * @throws IOException
     */
    ResultInfo<List<Aggregate>>  aggregate(SearchQuery query) throws IOException;

    /**
     * 搜索店铺，只返回一个最精确的店铺信息
     * @param query
     * @return
     */
    ResultInfo<ShopResult> searchShop(SearchQuery query) throws IOException;


}
