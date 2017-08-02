package com.tp.service.ord;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.dao.BaseDao;
import com.tp.dao.ord.CartItemDao;
import com.tp.model.ord.CartItem;
import com.tp.service.BaseService;
import com.tp.service.ord.ICartItemService;

@Service
public class CartItemService extends BaseService<CartItem> implements ICartItemService {

	@Autowired
	private CartItemDao cartItemDao;
	
	@Override
	public BaseDao<CartItem> getDao() {
		return cartItemDao;
	}

	@Override
	public List<CartItem> queryListByMemberId(Long memberId,Long shopId) {
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("memberId", memberId);
		params.put("shopId", shopId);
		return super.queryByParam(params);
	}

	@Override
	public Integer queryQuantityCountByMemberId(Long memberId,Long shopId) {
		return cartItemDao.queryQuantityCountByMemberId(memberId,shopId);
	}

	public Integer updateSelectedByList(List<CartItem> cartItemList,CartItem cartItem){
		return cartItemDao.updateSelectedByList(cartItemList, cartItem.getSelected(), cartItem.getMemberId(), cartItem.getSkuCode(), cartItem.getTopicId(),cartItem.getShopId());
	}
}
