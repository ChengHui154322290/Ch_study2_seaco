package com.tp.dao.usr;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.tp.common.dao.BaseDao;
import com.tp.model.usr.RoleResource;

public interface RoleResourceDao extends BaseDao<RoleResource> {

	/**
	 * 批量插入
	 * @param roleResourceList
	 */
	void insertBatchRoleResourceList(@Param("roleResourceList") List<RoleResource> roleResourceList);

}
