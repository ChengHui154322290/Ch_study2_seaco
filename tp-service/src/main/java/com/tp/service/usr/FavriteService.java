package com.tp.service.usr;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.dao.BaseDao;
import com.tp.dao.usr.FavriteDao;
import com.tp.dto.common.FailInfo;
import com.tp.dto.common.ResultInfo;
import com.tp.model.usr.Favrite;
import com.tp.service.BaseService;
import com.tp.service.usr.IFavriteService;

@Service
public class FavriteService extends BaseService<Favrite> implements IFavriteService {

	@Autowired
	private FavriteDao favriteDao;
	
	@Override
	public BaseDao<Favrite> getDao() {
		return favriteDao;
	}
	
	@Override
	public ResultInfo<Boolean> addFavrite(Long userId, Long menuId) {
		if(null==userId){
			return new ResultInfo<Boolean>(new FailInfo(1,"请指定用户"));
		}
		if(null==menuId){
			return new ResultInfo<Boolean>(new FailInfo(2,"请指定要添加的功能"));
		}
		
		Favrite favriteDO = new Favrite();
		favriteDO.setMenuId(menuId);
		favriteDO.setUserId(userId);
		List<Favrite> favriteDOs = favriteDao.queryByObject(favriteDO);
		if(CollectionUtils.isNotEmpty(favriteDOs)){
			return new ResultInfo<Boolean>(new FailInfo(3,"该功能已添加"));
		}
		favriteDO.setCreateTime(new Date());
		favriteDao.insert(favriteDO);
		return new ResultInfo<Boolean>(Boolean.TRUE);
	}

	@Override
	public void removeFavrite(Long userId, Long menuId) {
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("userId", userId);
		params.put("menuId", menuId);
		favriteDao.deleteByParam(params);
	}

	@Override
	public List<Favrite> selectByUserId(Long userId) {
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("userId", userId);
		return favriteDao.queryByParam(params);
	}

	@Override
	public Favrite selectByUserIdAndMenuId(Long userId, Long menuId) {
		Favrite favriteDO = new Favrite();
		favriteDO.setMenuId(menuId);
		favriteDO.setUserId(userId);
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("menuId", menuId);
		params.put("userId", userId);
		return super.queryUniqueByParams(params);
	}
}
