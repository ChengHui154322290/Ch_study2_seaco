package com.tp.service.usr;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.dao.BaseDao;
import com.tp.dao.usr.RoleInfoDao;
import com.tp.model.usr.RoleInfo;
import com.tp.service.BaseService;
import com.tp.service.usr.IRoleInfoService;

@Service
public class RoleInfoService extends BaseService<RoleInfo> implements IRoleInfoService {

	@Autowired
	private RoleInfoDao roleInfoDao;
	
	@Override
	public BaseDao<RoleInfo> getDao() {
		return roleInfoDao;
	}
	@Override
	public Long save(RoleInfo roleDO){
		roleDO.setUpdateTime(new Date());
		if(null != roleDO.getId()){//修改
			roleInfoDao.updateNotNullById(roleDO);
			return roleDO.getId();
		}else{//新增
			roleDO.setCreateTime(new Date());
			roleDO.setUpdateTime(roleDO.getCreateTime());
			roleInfoDao.insert(roleDO);
			return roleDO.getId();
		}
	}
}
