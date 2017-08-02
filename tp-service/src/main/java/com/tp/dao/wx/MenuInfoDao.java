package com.tp.dao.wx;

import java.util.Map;

import com.tp.common.dao.BaseDao;
import com.tp.model.wx.MenuInfo;

public interface MenuInfoDao extends BaseDao<MenuInfo>{

	public Integer queryCountByPid(Integer pid);
	
	public Integer delMenu(Map<String,Object> params);
}
