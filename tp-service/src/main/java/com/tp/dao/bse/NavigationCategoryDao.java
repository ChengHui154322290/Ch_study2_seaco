package com.tp.dao.bse;

import com.tp.common.dao.BaseDao;
import com.tp.model.bse.NavigationCategory;

import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface NavigationCategoryDao extends BaseDao<NavigationCategory> {

    Integer getMaxSort(@Param("parentId") Long parentId, @Param("type") Integer type);

    List<Long> getIdsByParentId(Long parentId);

    Integer delByParentId(Long parentId);
}
