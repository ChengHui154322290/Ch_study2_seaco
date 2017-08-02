package com.tp.service.mem;

import java.util.List;

import com.tp.exception.UserServiceException;
import com.tp.model.mem.FavoritePromotion;
import com.tp.service.IBaseService;
/**
  * @author szy
  * 接口
  */
public interface IFavoritePromotionService extends IBaseService<FavoritePromotion>{

	List<FavoritePromotion> getOnSalePromotionsByUid(Long uid) throws UserServiceException;

	List<Long> selectPromotionIdsByUid(Long uid);

}
