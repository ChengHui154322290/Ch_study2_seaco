package com.tp.service.ord;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.dao.BaseDao;
import com.tp.dao.ord.OrderPromotionDao;
import com.tp.model.ord.OrderPromotion;
import com.tp.service.BaseService;
import com.tp.service.ord.IOrderPromotionService;

@Service
public class OrderPromotionService extends BaseService<OrderPromotion> implements IOrderPromotionService {

	@Autowired
	private OrderPromotionDao orderPromotionDao;
	
	@Override
	public BaseDao<OrderPromotion> getDao() {
		return orderPromotionDao;
	}
	@Override
	public List<OrderPromotion> selectByOrderId(Long orderId) {
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("parentOrderId", orderId);
		return orderPromotionDao.queryByParam(params);
	}
	
	@Override
	public List<OrderPromotion> selectListByCouponCode(String couponCode) {
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("couponCode", couponCode);
		return orderPromotionDao.queryByParam(params);
	}

	@Override
	public List<OrderPromotion> selectListBySubCode(Long orderCode) {
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("orderCode", orderCode);
		return orderPromotionDao.queryByParam(params);
	}

	@Override
	public List<OrderPromotion> selectListByOrderCode(Long orderCode) {
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("parentOrderCode", orderCode);
		return orderPromotionDao.queryByParam(params);
	}
}
