package com.tp.service.ord;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.dao.ord.CartItemDao;
import com.tp.dto.ord.CartLineDTO;
import com.tp.dto.ord.CartLineSimpleDTO;
import com.tp.model.ord.CartItem;
import com.tp.redis.util.DBJedisList;
import com.tp.service.ord.ICartService;

@Service
public class CartService implements ICartService {
	
	@Autowired
	private CartItemDao cartItemDao;
	@SuppressWarnings("unused")
	private Log logger = LogFactory.getLog(this.getClass());
	@Override
	public List<CartLineSimpleDTO> selectCartLineDTOListByMemberId(Long memberId) {
		DBJedisList<CartLineSimpleDTO> redis = getRedis(memberId);
		return redis.getList();
	}
	@Override
	public List<CartItem> queryListByMemberId(Long memberId) {
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("memberId", memberId);
		
		
		return cartItemDao.queryByParam(params);

	}
	
	@Override
	public void insertCartLineSimpleDTOList(Long memberId, List<CartLineSimpleDTO> lineList) {
		DBJedisList<CartLineSimpleDTO> redis = getRedis(memberId);
		redis.watchAddAll(lineList);
	}

	@Override
	public void updateCartLineSimpleDTOBySelected(CartLineSimpleDTO cartLineSimpleDTO, Boolean selected, Long memberId) {
		DBJedisList<CartLineSimpleDTO> redis = getRedis(memberId);
		List<CartLineSimpleDTO> redisList = redis.getList();
		for(CartLineSimpleDTO cartLineSimple : redisList){
			if(cartLineSimple.equals(cartLineSimpleDTO)){
				if (selected) {
					cartLineSimple.setSelected(true);
				} else {
					cartLineSimple.setSelected(false);
				}
			}
		}		
		redis.watchAddAll(redisList);
	}

	@Override
	public void updateCartLineSimpleDTOByQuantity(CartLineSimpleDTO cartLineSimpleDTO, Integer quantity, Long memberId) {
		DBJedisList<CartLineSimpleDTO> redis = getRedis(memberId);
		List<CartLineSimpleDTO> redisList = redis.getList();
		for(CartLineSimpleDTO cartLineSimple : redisList){
			if(cartLineSimple.equals(cartLineSimpleDTO)){
				cartLineSimple.setQuantity(quantity);
			}
		}		
		redis.watchAddAll(redisList);
	}

	@Override
	public void deleteCartLineSimpleDTO(CartLineSimpleDTO cartLineSimpleDTO, Long memberId) {
		DBJedisList<CartLineSimpleDTO> redis = getRedis(memberId);
		if (redis.remove(cartLineSimpleDTO)) {

		} else {
			// TO 异常处理待定
		}
	}

	@Override
	public void deleteCartLineSimpleDTOBySettlement(Long memberId, List<CartLineDTO> settlementcartLineList) {
		DBJedisList<CartLineSimpleDTO> redis = getRedis(memberId);

		List<CartLineSimpleDTO> redisCartLineSimpleList = new ArrayList<CartLineSimpleDTO>();
		redisCartLineSimpleList = redis.getList();

		if (CollectionUtils.isNotEmpty(settlementcartLineList)) {
			for (CartLineDTO settlementCartLine : settlementcartLineList) {
				CartLineSimpleDTO cartLineSimple =  new CartLineSimpleDTO();
				cartLineSimple.setSkuCode(settlementCartLine.getSkuCode());
				cartLineSimple.setTopicId(settlementCartLine.getTopicId());
				if (CollectionUtils.isNotEmpty(redisCartLineSimpleList)) {
					for (CartLineSimpleDTO redisCartLineSimple : redisCartLineSimpleList) {
						if (redisCartLineSimple.equals(cartLineSimple)) {
							redis.remove(redisCartLineSimple);
							break;
						}
					}
				}
			}
		}
	}

	private DBJedisList<CartLineSimpleDTO> getRedis(Long memberId) {
		DBJedisList<CartLineSimpleDTO> redis = new DBJedisList<CartLineSimpleDTO>(memberId.toString(), memberId.intValue());
		return redis;
	}
}
