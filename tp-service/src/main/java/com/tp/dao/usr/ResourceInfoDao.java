package com.tp.dao.usr;

import java.util.List;
import java.util.Map;

import com.tp.common.dao.BaseDao;
import com.tp.model.usr.ResourceInfo;

public interface ResourceInfoDao extends BaseDao<ResourceInfo> {

	/**
	 * 参数查询权限
	 * @param ids
	 * @return
	 */
	public List<ResourceInfo> queryResourceInfoListByParams(Map<String,Object> params);
}
