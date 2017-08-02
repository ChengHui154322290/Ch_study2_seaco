package com.tp.dao.sch;

import com.tp.common.dao.BaseDao;
import com.tp.dto.sch.SearchKeyCensusQuery;
import com.tp.model.sch.SearchKeyCensus;

import java.util.List;
import java.util.Map;

public interface SearchKeyCensusDao extends BaseDao<SearchKeyCensus> {

    List<SearchKeyCensus> multiQuery(SearchKeyCensusQuery param);

    Integer multiQueryCount(SearchKeyCensusQuery query);

}
