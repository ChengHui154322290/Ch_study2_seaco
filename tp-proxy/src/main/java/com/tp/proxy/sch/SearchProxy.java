package com.tp.proxy.sch;

import com.tp.common.vo.PageInfo;
import com.tp.dto.common.ResultInfo;
import com.tp.model.sch.result.Aggregate;
import com.tp.model.sch.result.ItemResult;
import com.tp.model.sch.result.ShopResult;
import com.tp.proxy.mmp.callBack.AbstractProxy;
import com.tp.proxy.mmp.callBack.Callback;
import com.tp.query.sch.SearchQuery;
import com.tp.service.sch.ISearchService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by ldr on 2016/2/26.
 */
@Service
public class SearchProxy extends AbstractProxy {

    @Autowired
    private ISearchService searchService;

    /**
     * 搜索及分类导航数据查询接口
     * @param query
     * @return 商品集合
     */
    public ResultInfo<PageInfo<ItemResult>> search(final SearchQuery query){
        final ResultInfo<PageInfo<ItemResult>> result = new ResultInfo<>();
        this.execute(result, new Callback() {
            @Override
            public void process() throws Exception {
                ResultInfo<PageInfo<ItemResult>> resultInfo = searchService.search(query);
                result.setData(resultInfo.getData());
                result.setMsg(resultInfo.getMsg());
                result.setSuccess(resultInfo.isSuccess());
            }
        });
        return result;
    }
    /**
     * 搜索店铺
     * @param query
     * @return 商品集合
     */
    public ResultInfo<ShopResult> searchShop(final SearchQuery query){
        final ResultInfo<ShopResult> result = new ResultInfo<>();
        this.execute(result, new Callback() {
            @Override
            public void process() throws Exception {
                ResultInfo<ShopResult> resultInfo = searchService.searchShop(query);
                result.setData(resultInfo.getData());
                result.setMsg(resultInfo.getMsg());
                result.setSuccess(resultInfo.isSuccess());
            }
        });
        return result;
    }




    /**
     * 搜索及分类导航统计信息接口
     * @param query
     * @return 统计信息
     */
    public ResultInfo<List<Aggregate>>  aggregate(final SearchQuery query){
        final ResultInfo<List<Aggregate>> result = new ResultInfo<>();
        this.execute(result, new Callback() {
            @Override
            public void process() throws Exception {
                ResultInfo<List<Aggregate>>  resultInfo = searchService.aggregate(query);
                result.setData(resultInfo.getData());
                result.setMsg(resultInfo.getMsg());
                result.setSuccess(resultInfo.isSuccess());
            }
        });
        return result;
    }
}
