package com.tp.dao.bse;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.tp.common.dao.BaseDao;
import com.tp.model.bse.Category;

public interface CategoryDao extends BaseDao<Category> {
	/**
	 * 根据 id 集合和状态 返回  所有匹配CategoryDO;
	 * @param ids
	 * @param status 0:仅无效  , 1: 仅有效, 2:查全部 . 如果状态 为其它值, 返回 null;
	 * @return
	 */
	List<Category> queryCategoryByParams(@Param("ids") List<Long> ids, @Param("status") int status);
	
	String getAutoCode(Category category);
}
