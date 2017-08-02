package com.tp.service.usr;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.dao.BaseDao;
import com.tp.common.vo.Constant;
import com.tp.dao.usr.UserDetailDao;
import com.tp.model.usr.UserDetail;
import com.tp.service.BaseService;
import com.tp.service.usr.IUserDetailService;

@Service
public class UserDetailService extends BaseService<UserDetail> implements IUserDetailService {

	@Autowired
	private UserDetailDao userDetailDao;
	
	@Override
	public BaseDao<UserDetail> getDao() {
		return userDetailDao;
	}
	@Override
	public UserDetail save(UserDetail userDetail){
		userDetail.setUpdateTime(new Date());
		if(null != userDetail.getId()){//修改
			userDetailDao.updateNotNullById(userDetail);
			return userDetail;
		}else{//新增
			userDetail.setStatus(Constant.DEFAULTED.YES);
			userDetail.setCreateTime(new Date());
			userDetailDao.insert(userDetail);
			return userDetail;
		}
	}
	
	@Override
	public UserDetail findByUserId(Long userId){
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("userId", userId);
		return super.queryUniqueByParams(params);
	}
}
