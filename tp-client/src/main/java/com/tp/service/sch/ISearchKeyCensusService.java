package com.tp.service.sch;

import com.tp.common.vo.PageInfo;
import com.tp.dto.sch.SearchKeyCensusQuery;
import com.tp.model.sch.SearchKeyCensus;
import com.tp.query.sch.SearchQuery;
import com.tp.service.IBaseService;

import org.springframework.scheduling.annotation.Async;

import java.util.List;
import java.util.Map;

/**
  * @author szy
  * 接口
  */
public interface ISearchKeyCensusService extends IBaseService<SearchKeyCensus>{

    void addSearchKeyCensus(SearchQuery query) ;

    PageInfo<SearchKeyCensus> multiQuery(SearchKeyCensusQuery query);


}
