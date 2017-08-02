package com.tp.proxy.ord;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.util.ExceptionUtils;
import com.tp.dto.common.FailInfo;
import com.tp.dto.common.ResultInfo;
import com.tp.dto.ord.CartDTO;
import com.tp.dto.ord.CartLineDTO;
import com.tp.dto.ord.CartLineSimpleDTO;
import com.tp.exception.OrderServiceException;
import com.tp.model.ord.CartItem;
import com.tp.result.mmp.TopicItemInfoResult;
import com.tp.service.ord.ICartItemService;
import com.tp.service.ord.ICartService;
import com.tp.service.ord.local.ICartLocalService;

@Service
public class CartInfoProxy{

	private final static Logger logger = LoggerFactory.getLogger(CartInfoProxy.class);
	@Autowired
	private ICartLocalService cartLocalService;
	@Autowired
	private ICartService cartService;
	@Autowired
	private ICartItemService cartItemService;

	public ResultInfo<CartDTO> findMemberCart(Long memberId, int cartType) {
		try {
			return new ResultInfo<CartDTO>(cartLocalService.findMemberCart(memberId,cartType));
		} catch(OrderServiceException ex){
			return new ResultInfo<>(new FailInfo(ex.getMessage()));
		}catch (Exception exception) {
			//FailInfo failInfo = ExceptionUtils.print(new FailInfo(exception),logger, memberId,cartType);
			return new ResultInfo<>(new FailInfo("加载购物车失败"));
		}
	}

	public ResultInfo<Integer> getCartQuantity(Long memberId) {
		try {
			return new ResultInfo<Integer>(cartLocalService.getCartQuantity(memberId));
		} catch(OrderServiceException ex){
			return new ResultInfo<>(new FailInfo(ex.getMessage()));
		}catch (Exception exception) {
			ExceptionUtils.print(new FailInfo(exception),logger, memberId);
			return new ResultInfo<>(new FailInfo("获取购物车数量失败"));
		}
	}
	
	public ResultInfo<Integer> getCartQuantityByCartType(Long memberId, int cartType) {
		try {
			return new ResultInfo<Integer>(cartLocalService.getCartQuantityByCartType(memberId, cartType));
		} catch(OrderServiceException ex){
			return new ResultInfo<>(new FailInfo(ex.getMessage()));
		}catch (Exception exception) {
			ExceptionUtils.print(new FailInfo(exception),logger, memberId, cartType);
			return new ResultInfo<>(new FailInfo("获取购物车数量失败"));
		}
	}

//	public ResultInfo<CartLineSimpleDTO> addToCart(CartLineDTO cartline, Long memberId,
//			String ip, int platformType) {
//		try {
//			return cartLocalService.addToCart(cartline, memberId, ip, platformType);
//		} catch(OrderServiceException ex){
//			return new ResultInfo<>(new FailInfo(ex.getMessage()));
//		}catch (Exception exception) {
//			ExceptionUtils.print(new FailInfo(exception),logger, cartline, memberId, ip, platformType);
//			return new ResultInfo<>(new FailInfo("添加购物车失败"));
//		}
//	}
//
//	public ResultInfo<CartDTO> loadCart(Long memberId, int cartType) {
//		try {
//			return new ResultInfo<CartDTO>(cartLocalService.loadCart(memberId, cartType));
//		}catch(OrderServiceException ex){
//			return new ResultInfo<>(new FailInfo(ex.getMessage()));
//		} catch (Exception exception) {
//			ExceptionUtils.print(new FailInfo(exception),logger, memberId, cartType);
//			return new ResultInfo<>(new FailInfo("加载购物车失败"));
//		}
//	}

	public ResultInfo<List<TopicItemInfoResult>> loadLastestSingleTopic() {
		try {
			return new ResultInfo<List<TopicItemInfoResult>>(cartLocalService.loadLastestSingleTopic());
		} catch(OrderServiceException ex){
			return new ResultInfo<>(new FailInfo(ex.getMessage()));
		}catch (Exception exception) {
			ExceptionUtils.print(new FailInfo(exception),logger);
			return new ResultInfo<>(new FailInfo("加载最新单品团失败"));
		}
	}

	public ResultInfo<Boolean> selectCart(String skuCode, Long topicId, Boolean selected,
			Long memberId) {
		try {
			cartLocalService.selectCart(skuCode, topicId, selected, memberId);
			return new ResultInfo<>(Boolean.TRUE);
		} catch(OrderServiceException ex){
			return new ResultInfo<>(new FailInfo(ex.getMessage()));
		}catch (Exception exception) {
			ExceptionUtils.print(new FailInfo(exception),logger, skuCode, topicId, selected, memberId);
			return new ResultInfo<>(new FailInfo("单选失败"));
		}
		
	}

	public ResultInfo<Boolean> selectAllCart(Boolean selectedAll, int cartType, Long memberId) {		
		try {
			cartLocalService.selectAllCart(selectedAll, cartType, memberId);
			return new ResultInfo<>(Boolean.TRUE);
		}catch(OrderServiceException ex){
			return new ResultInfo<>(new FailInfo(ex.getMessage()));
		} catch (Exception exception) {
			ExceptionUtils.print(new FailInfo(exception),logger, selectedAll, cartType, memberId);
			return new ResultInfo<>(new FailInfo("全选失败"));
		}
	}

	public ResultInfo<Boolean> updateCart(CartLineDTO cartLine, Long memberId, int platformType) {		
		try {
			cartLocalService.updateCart(cartLine, memberId, platformType);
			return new ResultInfo<>(Boolean.TRUE);
		}catch(OrderServiceException ex){
			return new ResultInfo<>(new FailInfo(ex.getMessage()));
		} catch (Exception exception) {
			ExceptionUtils.print(new FailInfo(exception),logger, cartLine, memberId, platformType);
			return new ResultInfo<>(new FailInfo("更新数量失败"));
		}
	}

	public ResultInfo<Boolean> deleteCart(String skuCode, Long topicId, Long memberId) {	
		try {
			cartLocalService.deleteCart(skuCode, topicId, memberId);
			return new ResultInfo<>(Boolean.TRUE);
		} catch(OrderServiceException ex){
			return new ResultInfo<>(new FailInfo(ex.getMessage()));
		}catch (Exception exception) {
			ExceptionUtils.print(new FailInfo(exception),logger, skuCode, topicId, memberId);
			return new ResultInfo<>(new FailInfo("删除失败"));
		}
	}
	
	public ResultInfo<Boolean> deleteAllCart(int cartType, Long memberId) {		
		try {
			cartLocalService.deleteAllCart(cartType, memberId);
			return new ResultInfo<>(Boolean.TRUE);
		} catch(OrderServiceException ex){
			return new ResultInfo<>(new FailInfo(ex.getMessage()));
		}catch (Exception exception) {
			ExceptionUtils.print(new FailInfo(exception),logger, cartType, memberId);
			return new ResultInfo<>(new FailInfo("全部删除失败"));
		}
	}

//	public ResultInfo<Boolean> checkCart(Long memberId, String ip,
//			Integer platform, List<CartLineDTO> cartLineList) {
//		try {
//			cartLocalService.checkCart(memberId, ip, platform, cartLineList);
//			return new ResultInfo<>(Boolean.TRUE);
//		}catch(OrderServiceException ex){
//			return new ResultInfo<>(new FailInfo(ex.getMessage()));
//		} catch (Exception exception) {
//			ExceptionUtils.print(new FailInfo(exception),logger, memberId, ip, platform, cartLineList);
//			return new ResultInfo<>(new FailInfo("校验失败"));
//		}
//	}

	public ResultInfo<CartDTO> getValidateCart(Long memberId, int cartType) {
		try {	
			return new ResultInfo<>(cartLocalService.getValidateCart(memberId, cartType));
		}catch(OrderServiceException ex){
			return new ResultInfo<>(new FailInfo(ex.getMessage()));
		} catch (Exception exception) {
			ExceptionUtils.print(new FailInfo(exception),logger, memberId, cartType);
			return new ResultInfo<>(new FailInfo("加载有效数据失败"));
		}
	}

	public ResultInfo<String> showCartTab(Long memberId) {
		try {
			return new ResultInfo<>(cartLocalService.showCartTab(memberId));
		} catch(OrderServiceException ex){
			return new ResultInfo<>(new FailInfo(ex.getMessage()));
		}catch (Exception exception) {
			ExceptionUtils.print(new FailInfo(exception),logger, memberId);
			return new ResultInfo<>(new FailInfo("购物车显示控制失败"));
		}
	}
	
	public ResultInfo<List<CartLineSimpleDTO>> selectCartLineDTOListByMemberId(Long memberId){
		try {	
			return new ResultInfo<>(cartService.selectCartLineDTOListByMemberId(memberId));
		}catch(Exception exception){
			ExceptionUtils.print(new FailInfo(exception),logger, memberId);
			return new ResultInfo<>(new FailInfo("购物车显示失败"));
		} 

	}
	
	public ResultInfo<List<CartItem>> selectCartLineDTOListByMemberIdFromDB(Long memberId){
		try {	
			List<CartItem> cartItemInfoList = cartService.queryListByMemberId(memberId);

			return new ResultInfo<List<CartItem>>(cartItemInfoList);
		}catch(Exception exception){
			ExceptionUtils.print(new FailInfo(exception),logger, memberId);
			return new ResultInfo<>(new FailInfo("购物车显示失败"));
		} 
	}
	

	
	public ResultInfo<Boolean> deleteCartLineSimpleDTO(String skuCode, Long topicId, Long memberId){
		try {	
			cartLocalService.deleteCart(skuCode,topicId, memberId);
			return new ResultInfo<>(Boolean.TRUE);
		}catch(Exception exception){
			ExceptionUtils.print(new FailInfo(exception),logger, memberId);
			return new ResultInfo<>(new FailInfo("购物车显示失败"));
		} 
	}
	
	public ResultInfo<Boolean> deleteCartLineSimpleDTOFromDB(String skuCode, Long topicId, Long memberId){
		try {
			Map<String, Object> params = new HashMap<>();
			params.put("memberId", memberId);
			params.put("topicId", topicId);
			params.put("skuCode", skuCode);
			int count = cartItemService.deleteByParam(params);
			if(count != 1) return new ResultInfo<>(new FailInfo("删除失败"));
			return new ResultInfo<>(Boolean.TRUE);
		}catch(Exception exception){
			ExceptionUtils.print(new FailInfo(exception),logger, memberId);
			return new ResultInfo<>(new FailInfo("删除购物车失败"));
		} 
	}
	
}
