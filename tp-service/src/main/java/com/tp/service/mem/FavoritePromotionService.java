package com.tp.service.mem;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.dao.BaseDao;
import com.tp.common.vo.DAOConstant.MYBATIS_SPECIAL_STRING;
import com.tp.dao.mem.FavoritePromotionDao;
import com.tp.exception.UserServiceException;
import com.tp.model.mem.FavoritePromotion;
import com.tp.service.BaseService;
import com.tp.service.mem.IFavoritePromotionService;

@Service
public class FavoritePromotionService extends BaseService<FavoritePromotion> implements IFavoritePromotionService {

	@Autowired
	private FavoritePromotionDao favoritePromotionDao;
	
	@Override
	public BaseDao<FavoritePromotion> getDao() {
		return favoritePromotionDao;
	}

	@Override
	public List<FavoritePromotion> getOnSalePromotionsByUid(Long uid) throws UserServiceException {
		if(null == uid || uid.longValue() == 0L) {
			logger.error(">>>>>[ERROR!]: Invalid uid.");
			throw new IllegalArgumentException(">>>>>[ERROR!]: Invalid uid.");
		}
		logger.debug(">>>>>[begin]: getOnSalePromotionsByUid, uid: " + uid);
		FavoritePromotion query = new FavoritePromotion();
		query.setUid(uid);
		query.setIsDelete(false);
		List<FavoritePromotion> list = new ArrayList<FavoritePromotion>();
		try {
			list = favoritePromotionDao.queryByObject(query);
		} catch (Exception ex) {
			logger.error(">>>>>[ERROR!]: " + ex.getMessage(), ex);
			throw new UserServiceException(">>>>>[ERROR!]: Fail to get on sale promotions");
		}
		list =  filterOnSalePromotions(list);
		logger.debug(">>>>>[end]: getOnSalePromotionsByUid");
		return list;
	}
	private List<FavoritePromotion> filterOnSalePromotions(List<FavoritePromotion> all) {
		List<FavoritePromotion> result = new ArrayList<FavoritePromotion>();
		if(null == all || all.size() == 0)
			return result;
		Date now = new Date();
		for(FavoritePromotion favo : all) {
			if(null != favo.getOnSaleTime() && now.before(favo.getOnSaleTime())) 
				result.add(favo);
		}
		
		return result;
	}

	@Override
	public List<Long> selectPromotionIdsByUid(Long uid) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put(MYBATIS_SPECIAL_STRING.COLUMNS.name(), " uid=" + uid);
		List<FavoritePromotion> list = queryByParam(params);
		if(CollectionUtils.isNotEmpty(list)){
			List<Long> ids = new ArrayList<Long>();
			for(FavoritePromotion favoritePromotion : list){
				ids.add(favoritePromotion.getPromotionId());
			}
			return ids;
		}
		return null;
	}
}
