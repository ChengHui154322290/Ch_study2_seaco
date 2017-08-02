package com.tp.service.usr;

import java.util.List;

import com.tp.dto.common.ResultInfo;
import com.tp.model.usr.Favrite;
import com.tp.service.IBaseService;
/**
  * @author szy
  * 会员的常用功能信息表接口
  */
public interface IFavriteService extends IBaseService<Favrite>{

	ResultInfo<Boolean> addFavrite(Long userId, Long menuId);

	void removeFavrite(Long userId, Long menuId);

	List<Favrite> selectByUserId(Long userId);

	Favrite selectByUserIdAndMenuId(Long userId, Long menuId);

}
