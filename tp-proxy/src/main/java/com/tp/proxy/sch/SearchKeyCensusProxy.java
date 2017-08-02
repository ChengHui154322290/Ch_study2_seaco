package com.tp.proxy.sch;

import com.tp.common.vo.PageInfo;
import com.tp.dto.common.ResultInfo;
import com.tp.dto.sch.SearchKeyCensusQuery;
import com.tp.model.sch.Search;
import com.tp.model.sch.SearchKeyCensus;
import com.tp.proxy.BaseProxy;
import com.tp.proxy.mmp.callBack.Callback;
import com.tp.service.IBaseService;
import com.tp.service.sch.ISearchKeyCensusService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 代理层
 *
 * @author szy
 */
@Service
public class SearchKeyCensusProxy extends BaseProxy<SearchKeyCensus> {

    @Autowired
    private ISearchKeyCensusService searchKeyCensusService;

    @Override
    public IBaseService<SearchKeyCensus> getService() {
        return searchKeyCensusService;
    }

    public ResultInfo<PageInfo<SearchKeyCensus>> multiQuery(final SearchKeyCensusQuery query) {
        final ResultInfo<PageInfo<SearchKeyCensus>> result = new ResultInfo<>();
        this.execute(result, new Callback() {
            @Override
            public void process() throws Exception {
                PageInfo<SearchKeyCensus> page = searchKeyCensusService.multiQuery(query);
                result.setData(page);
            }
        });
        return result;
    }
}
