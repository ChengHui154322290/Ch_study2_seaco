package com.tp.dao.usr;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.tp.common.dao.BaseDao;
import com.tp.model.usr.RoleMenuLimit;

public interface RoleMenuLimitDao extends BaseDao<RoleMenuLimit> {

	List<RoleMenuLimit> selectByIds(@Param("ids")List<Long> ids);

	List<Long> selectSysMenuLimitIds(RoleMenuLimit roleMenuLimit);

}
