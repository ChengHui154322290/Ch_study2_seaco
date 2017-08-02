package com.tp.proxy.usr;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.dto.common.ResultInfo;
import com.tp.model.usr.Favrite;
import com.tp.proxy.BaseProxy;
import com.tp.service.IBaseService;
import com.tp.service.usr.IFavriteService;
/**
 * 会员的常用功能信息表代理层
 * @author szy
 *
 */
@Service
public class FavriteProxy extends BaseProxy<Favrite>{

	@Autowired
	private IFavriteService favriteService;

	@Override
	public IBaseService<Favrite> getService() {
		return favriteService;
	}
	public List<Favrite> selectFavriteByUserId(Long userId){
		 if(null==userId){
			 return null;
		 }
		 List<Favrite> favriteDOs = favriteService.selectByUserId(userId);
		 return favriteDOs;
	}

	public ResultInfo<Boolean> saveFavrite(Long userId, Long menuId) {
		return favriteService.addFavrite(userId, menuId);
	}

	
	public void removeFavrite(Long userId, Long menuId) {
		favriteService.removeFavrite(userId, menuId);;
	}

	public Favrite selectByUserIdAndMeunId(Long userId, Long menuId) {
		Favrite favriteDO = favriteService.selectByUserIdAndMenuId(userId,menuId);
		return favriteDO;
	}
}
