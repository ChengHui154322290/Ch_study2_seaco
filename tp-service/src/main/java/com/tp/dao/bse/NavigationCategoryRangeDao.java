package com.tp.dao.bse;

import com.tp.common.dao.BaseDao;
import com.tp.model.bse.NavigationCategoryRange;

import java.util.List;

public interface NavigationCategoryRangeDao extends BaseDao<NavigationCategoryRange> {

    List<NavigationCategoryRange> queryByCategoryIds(List<Long> ids);

    Integer delByCategoryIds(List<Long> ids);

}
