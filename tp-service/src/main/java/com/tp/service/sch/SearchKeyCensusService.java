package com.tp.service.sch;

import com.alibaba.fastjson.JSON;
import com.tp.common.dao.BaseDao;
import com.tp.common.vo.PageInfo;
import com.tp.dao.sch.BrandSearchDao;
import com.tp.dao.sch.SearchKeyCensusDao;
import com.tp.dto.sch.SearchKeyCensusQuery;
import com.tp.model.bse.NavigationCategory;
import com.tp.model.sch.BrandSearch;
import com.tp.model.sch.SearchKeyCensus;
import com.tp.query.sch.SearchQuery;
import com.tp.service.BaseService;
import com.tp.service.bse.INavigationCategoryService;
import com.tp.service.sch.ISearchKeyCensusService;
import com.tp.util.BeanUtil;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class SearchKeyCensusService extends BaseService<SearchKeyCensus> implements ISearchKeyCensusService {

    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private SearchKeyCensusDao searchKeyCensusDao;

    @Autowired
    private BrandSearchDao brandSearchDao;

    @Autowired
    private INavigationCategoryService navigationCategoryService;

    @Override
    public BaseDao<SearchKeyCensus> getDao() {
        return searchKeyCensusDao;
    }


    @Async
    @Override
    public void addSearchKeyCensus(SearchQuery query) {
        if (query == null || (StringUtils.isBlank(query.getKey())) && query.getNavCategoryId() == null && query.getNavBrandId() == null) {
            return;
        }

        try {
            int type;
            String key;
            if (StringUtils.isNotBlank(query.getKey())) {
                type = 1;
                key = query.getKey().trim();
            } else if (query.getNavCategoryId() != null) {
                type = 2;
                NavigationCategory category = navigationCategoryService.queryById(query.getNavCategoryId());
                if (category == null) {
                    return;
                }
                key = category.getName();
            } else if (query.getNavBrandId() != null) {
                type = 3;
                BrandSearch brandSearch = brandSearchDao.queryById(query.getNavBrandId());
                if (brandSearch == null) {
                    return;
                }
                key = brandSearch.getBrandName();

            }else {
                LOGGER.error("SEARCH_KEY_CENSUS_ERROR;PARAM_ERROR.PARAM="+ JSON.toJSONString(query));
                return;
            }

            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.HOUR_OF_DAY, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND, 0);
            Date cur = calendar.getTime();

            SearchKeyCensus census = new SearchKeyCensus();
            census.setSearchDate(cur);
            census.setSearchKey(key);
            census.setSearchType(type);


            List<SearchKeyCensus> searchKeyCensusList = searchKeyCensusDao.queryByParam(BeanUtil.beanMap(census));
            if (searchKeyCensusList == null || searchKeyCensusList.isEmpty()) {
                census.setSearchCount(1);
                searchKeyCensusDao.insert(census);
            } else {
                census.setSearchCount(searchKeyCensusList.get(0).getSearchCount() + 1);
                census.setId(searchKeyCensusList.get(0).getId());
                searchKeyCensusDao.updateNotNullById(census);
            }
        } catch (Exception e) {
            LOGGER.error("SEARCH_KEY_CENSUS_ERROR;PARAM="+ JSON.toJSONString(query));
            LOGGER.error("SEARCH_KEY_CENSUS_ERROR;EXCEPTION=",e);

        }

    }

    public PageInfo<SearchKeyCensus> multiQuery(SearchKeyCensusQuery query){
        Integer count = searchKeyCensusDao.multiQueryCount(query);
        List<SearchKeyCensus> list = searchKeyCensusDao.multiQuery(query);
        PageInfo<SearchKeyCensus> page = new PageInfo<>();
        page.setRows(list);
        page.setRecords(count);
        page.setSize(query.getPageSize());
        page.setPage(query.getStartPage());
        page.setTotal(count%query.getPageSize()>0? count/query.getPageSize()+1: count/query.getPageSize() );

        return page;
    }

}
