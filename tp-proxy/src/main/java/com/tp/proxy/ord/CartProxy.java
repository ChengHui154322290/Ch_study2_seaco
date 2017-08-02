package com.tp.proxy.ord;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Predicate;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.tp.common.util.ExceptionUtils;
import com.tp.common.vo.Constant;
import com.tp.common.vo.RedisConstant;
import com.tp.common.vo.Constant.SPLIT_SIGN;
import com.tp.common.vo.RedisConstant.REDIS_KEY;
import com.tp.dto.common.FailInfo;
import com.tp.dto.common.ResultInfo;
import com.tp.dto.ord.PreOrderDto;
import com.tp.dto.ord.ShoppingCartDto;
import com.tp.enums.common.OrderErrorCodeEnum;
import com.tp.model.ord.CartItem;
import com.tp.model.ord.CartItemInfo;
import com.tp.model.ord.OrderItem;
import com.tp.proxy.ord.compute.IAmountProxy;
import com.tp.proxy.ord.validate.CartValidateProxy;
import com.tp.redis.util.JedisCacheUtil;
import com.tp.service.ord.ICartItemService;

/**
 * 购物车代理层
 * @author szy
 *
 */
@Service
public class CartProxy {

	private static final Logger logger = LoggerFactory.getLogger(CartProxy.class);

	@Autowired
	private ICartItemService cartItemService;
	@Autowired
	private JedisCacheUtil jedisCacheUtil;
	@Autowired
	private CartItemInfoProxy cartItemInfoProxy;
	@Autowired
	private CartValidateProxy cartValidateProxy;
	@Autowired
	private IAmountProxy<ShoppingCartDto> cartAmountProxy;
	
	/**
	 * 根据会员ID查询购物车
	 * @param memberId 会员ID
	 * @param ip 下单IP
	 * @param areaId 选中地区
	 * @return
	 */
	public ResultInfo<ShoppingCartDto> queryCartByMemberId(Long memberId,String ip,Long areaId,Integer platfromId,String token,Long shopId) {
		String tempToken = token;
		if(StringUtils.isBlank(token)){
			tempToken = UUID.randomUUID().toString();
		}
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("memberId", memberId);
		try {
			//初始化购物车数据
			ShoppingCartDto shoppingCartDto = cartItemInfoProxy.initCart(memberId,ip,areaId,platfromId,shopId);
			shoppingCartDto.setToken(tempToken);
			shoppingCartDto.setPlatformType(platfromId);
			shoppingCartDto.setShopId(shopId);
			if(CollectionUtils.isNotEmpty(shoppingCartDto.getPreSubOrderList())){
				setCacheCart(shoppingCartDto);
			}
			return new ResultInfo<ShoppingCartDto>(shoppingCartDto);
		} catch (Throwable e) {
			FailInfo failInfo = ExceptionUtils.println(new FailInfo(e), logger,memberId);
			return new ResultInfo<ShoppingCartDto>(failInfo);
		}
	}
	
	public ResultInfo<ShoppingCartDto> queryCartByTocken(CartItemInfo cartItemInfo) {
		String tempToken = cartItemInfo.getToken();
		if(StringUtils.isBlank(tempToken)){
			tempToken = UUID.randomUUID().toString();
			cartItemInfo.setToken(tempToken);
		}
		try {
			//初始化购物车数据
			ShoppingCartDto shoppingCartDto = cartItemInfoProxy.initCart(cartItemInfo);
			shoppingCartDto.setToken(tempToken);
			shoppingCartDto.setBuyNow(Constant.TF.YES);
			shoppingCartDto.setPlatformType(cartItemInfo.getPlatformId());
			shoppingCartDto.setGroupId(cartItemInfo.getGroupId());
			shoppingCartDto.setShopPromoterId(cartItemInfo.getShopPromoterId());
			shoppingCartDto.setShopId(cartItemInfo.getShopId());
			if(CollectionUtils.isNotEmpty(shoppingCartDto.getPreSubOrderList())){
				setCacheCart(shoppingCartDto);
			}
			return new ResultInfo<ShoppingCartDto>(shoppingCartDto);
		} catch (Throwable e) {
			FailInfo failInfo = ExceptionUtils.print(new FailInfo(e), logger,cartItemInfo);
			return new ResultInfo<ShoppingCartDto>(failInfo);
		}
	}
	
	/**
	 * 获取会员购物车中商品总数
	 * @param memberId
	 * @return
	 */
	public ResultInfo<Integer> queryQuantityCountByMemberId(Long memberId,Long shopId){
		try {
			return new ResultInfo<>(cartItemService.queryQuantityCountByMemberId(memberId,shopId));
		} catch (Throwable exception) {
			FailInfo failInfo = ExceptionUtils.print(new FailInfo(exception), logger, memberId);
			return new ResultInfo<>(failInfo);
		}
	}
	/**
	 * 添加商品到购物车
	 * @param cartItemInfo
	 * @return
	 */
	public ResultInfo<Integer> insertCartItemInfo(CartItemInfo cartItemInfo){
		setDefaultQuantity(cartItemInfo);
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("skuCode", cartItemInfo.getSkuCode());
		params.put("topicId", cartItemInfo.getTopicId());
		params.put("memberId", cartItemInfo.getMemberId());
		params.put("shopId", cartItemInfo.getShopId());
		CartItem oldCartItemInfo = cartItemService.queryUniqueByParams(params);
		if (null != oldCartItemInfo) {
			cartItemInfo.setCartItemId(oldCartItemInfo.getCartItemId());
			cartItemInfo.setQuantity(oldCartItemInfo.getQuantity()+ cartItemInfo.getQuantity());
		} 
		cartItemInfo.setSelected(Constant.SELECTED.YES);
		FailInfo failInfo = cartValidateProxy.validate(cartItemInfo);
		if(null!=failInfo){
			return new ResultInfo<Integer>(failInfo);
		}
		String lock = getCartItemKey(cartItemInfo);
		// 加分布锁
		CartItem cartItem = new CartItem();
		BeanUtils.copyProperties(cartItemInfo, cartItem);
		if (jedisCacheUtil.lock(lock)) {
			try {
				if (null != oldCartItemInfo) {
					cartItemService.updateNotNullById(cartItem);
				} else {
					cartItemService.insert(cartItem);
				}
				return new ResultInfo<Integer>(cartItemService.queryQuantityCountByMemberId(cartItemInfo.getMemberId(),cartItemInfo.getShopId()));
			} catch (Throwable exception) {
				failInfo = ExceptionUtils.print(new FailInfo(exception), logger, cartItemInfo);
				return new ResultInfo<>(failInfo);
			} finally {
				jedisCacheUtil.unLock(lock);
			}
		}
		return new ResultInfo<Integer>(new FailInfo(OrderErrorCodeEnum.CART_ERROR.OTHER_PLATFORM_OPERATION));
	}
	
	/**
	 * 选择商品项
	 * @param cartItemInfo
	 * @return
	 */
	public ResultInfo<ShoppingCartDto> selectCartItem(final CartItemInfo cartItemInfo){
		ShoppingCartDto shoppingCartDto = getCartDto(cartItemInfo.getMemberId(),cartItemInfo.getToken(),cartItemInfo.getShopId());
		List<PreOrderDto> subOrderList = shoppingCartDto.getPreSubOrderList();
		for(PreOrderDto subOrder:subOrderList){
			subOrder.getOrderItemList().forEach(new Consumer<OrderItem>(){
				@Override
				public void accept(OrderItem orderItem) {
					if(orderItem.getTopicId().equals(cartItemInfo.getTopicId()) 
					&& orderItem.getSkuCode().equals(cartItemInfo.getSkuCode())){
						orderItem.setSelected(cartItemInfo.getSelected());
						CartItem cartItem = new CartItem();
						cartItem.setSkuCode(orderItem.getSkuCode());
						cartItem.setTopicId(orderItem.getTopicId());
						cartItem.setSelected(orderItem.getSelected());
						cartItem.setMemberId(cartItemInfo.getMemberId());
						cartItem.setShopId(cartItemInfo.getShopId());
						cartItemService.updateSelectedByList(null, cartItem);
					}
				}
			});
		}
		return operateTotal(shoppingCartDto);
	}
	
	/**
	 * 选择全部商品、或全部不选
	 * @param cartItemInfo
	 * @return
	 */
	public ResultInfo<ShoppingCartDto> selectAllCartItem(Long memberId,String token,Boolean allSelected,Long shopId){
		ShoppingCartDto shoppingCartDto = getCartDto(memberId,token,shopId);
		List<PreOrderDto> subOrderList = shoppingCartDto.getPreSubOrderList();
		List<CartItem> cartItemList = new ArrayList<CartItem>();
		for(PreOrderDto subOrder:subOrderList){
			subOrder.getOrderItemList().forEach(new Consumer<OrderItem>(){
				@Override
				public void accept(OrderItem orderItem) {
					orderItem.setSelected(allSelected?Constant.SELECTED.YES:Constant.SELECTED.NO);
					CartItem cartItem = new CartItem();
					cartItem.setSkuCode(orderItem.getSkuCode());
					cartItem.setTopicId(orderItem.getTopicId());
					cartItem.setShopId(shopId);
					cartItemList.add(cartItem);
				}
			});
		}
		if(CollectionUtils.isNotEmpty(cartItemList)){
			CartItem cartItem = new CartItem();
			cartItem.setSelected(allSelected?Constant.SELECTED.YES:Constant.SELECTED.NO);
			cartItem.setMemberId(memberId);
			cartItem.setShopId(shopId);
			cartItemService.updateSelectedByList(cartItemList, cartItem);
		}
		return operateTotal(shoppingCartDto);
	} 
	/**
	 * 修改商品购买数量
	 * 
	 * @param CartItemInfo
	 * @return
	 */
	public ResultInfo<ShoppingCartDto> refreshItem(CartItemInfo cartItemInfo) {
		setDefaultQuantity(cartItemInfo);
		FailInfo failInfo = cartValidateProxy.validate(cartItemInfo);
		if(null!=failInfo){
			return new ResultInfo<ShoppingCartDto>(failInfo);
		}
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("memberId", cartItemInfo.getMemberId());
		params.put("topicId", cartItemInfo.getTopicId());
		params.put("skuCode", cartItemInfo.getSkuCode());
		params.put("shopId", cartItemInfo.getShopId());
		CartItem cartItem = cartItemService.queryUniqueByParams(params);
		ShoppingCartDto shoppingCartDto = getCartDto(cartItemInfo.getMemberId(),cartItemInfo.getToken(),cartItemInfo.getShopId());
		List<PreOrderDto> subOrderList = shoppingCartDto.getPreSubOrderList(); 
		subOrderList.forEach(new Consumer<PreOrderDto>(){
			@Override
			public void accept(PreOrderDto subOrder) {
				subOrder.getOrderItemList().forEach(new Consumer<OrderItem>(){
					@Override
					public void accept(OrderItem orderItem) {
						if(orderItem.getSkuCode().equals(cartItemInfo.getSkuCode())
						&& orderItem.getTopicId().equals(cartItemInfo.getTopicId())){
							orderItem.setQuantity(cartItemInfo.getQuantity());
						}
					}
				});
			}
		});
		String lock = getCartItemKey(cartItemInfo);
		if (jedisCacheUtil.lock(lock)) {
			try {
				if(cartItem!=null){
					cartItem.setQuantity(cartItemInfo.getQuantity());
					cartItemService.updateNotNullById(cartItem);
				}
			} catch (Throwable exception) {
				failInfo = ExceptionUtils.print(new FailInfo(exception),logger, cartItemInfo);
				return new ResultInfo<ShoppingCartDto>(failInfo);
			}finally{
				jedisCacheUtil.unLock(lock);
			}
		}
		return operateTotal(shoppingCartDto);
	}
	
	public ResultInfo<ShoppingCartDto> deleteCartItem(CartItemInfo cartItemInfo){
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("memberId", cartItemInfo.getMemberId());
		params.put("topicId", cartItemInfo.getTopicId());
		params.put("skuCode", cartItemInfo.getSkuCode());
		params.put("shopId", cartItemInfo.getShopId());
		try{
			//删除缓存中的商品项
			ShoppingCartDto cartDto =getCartDto(cartItemInfo.getMemberId(),cartItemInfo.getToken(),cartItemInfo.getShopId());
			List<PreOrderDto> preSubOrderList = cartDto.getPreSubOrderList();
			cartDto.setShopId(cartItemInfo.getShopId());
			preSubOrderList.forEach(new Consumer<PreOrderDto>(){
				public void accept(PreOrderDto subOrder) {
					subOrder.getOrderItemList().removeIf(new Predicate<OrderItem>(){
						public boolean test(OrderItem orderItem) {
							return orderItem.getTopicId().equals(cartItemInfo.getTopicId()) && 
								   orderItem.getSkuCode().equals(cartItemInfo.getSkuCode());
						}
					});
				}
			});
			preSubOrderList.removeIf(new Predicate<PreOrderDto>(){
				public boolean test(PreOrderDto subOrder) {
					return CollectionUtils.isEmpty(subOrder.getOrderItemList());
				}
			});
			cartDto.getDeleteCartItemList().removeIf(new Predicate<CartItemInfo>(){
				public boolean test(CartItemInfo cartItem) {
					return cartItem.getTopicId().equals(cartItemInfo.getTopicId()) && 
						   cartItem.getSkuCode().equals(cartItemInfo.getSkuCode());
				}
			});
			//删除数据库中的商品项
			cartItemService.deleteByParam(params);
			//获取用户购物车，计算选中商品金额
			return operateTotal(cartDto);
		}catch(Throwable exception){
			FailInfo failInfo = ExceptionUtils.print(new FailInfo(exception),logger, cartItemInfo);
			return new ResultInfo<>(failInfo);
		}
	}
	
	/**
	 * 计算选中商品的总价
	 * 
	 * @param CartItemInfo
	 * @return
	 */
	public ResultInfo<ShoppingCartDto> operateTotal(Long memberId,String token,Long shopId) {
		Assert.notNull(token);// 对PC来讲则是每一次的选择UUID
		Assert.notNull(memberId);
		ShoppingCartDto cartDto =getCartDto(memberId,token,shopId);
		if(null!=cartDto){
			cartDto.setShopId(shopId);
			return operateTotal(cartDto);
		}
		cartDto= new ShoppingCartDto();
		return new ResultInfo<ShoppingCartDto>(cartDto);
	}
	
	public ResultInfo<ShoppingCartDto> operateTotal(ShoppingCartDto shoppingCartDto){
		shoppingCartDto = cartAmountProxy.computeAmount(shoppingCartDto);
		FailInfo failInfo = cartValidateProxy.validatePassLimit(shoppingCartDto, null);
		if(failInfo!=null){
			return new ResultInfo<ShoppingCartDto>(failInfo);
		}
		setCacheCart(shoppingCartDto);
		return new ResultInfo<ShoppingCartDto>(shoppingCartDto);
	}
	
	/**
	 * 获取缓存中的购物车
	 * @param memberId
	 * @param token
	 * @return
	 */
	public ShoppingCartDto getCartDto(Long memberId,String token,Long shopId){
		ShoppingCartDto shoppingCartDto = (ShoppingCartDto)jedisCacheUtil.getCache(getKey(memberId,token,shopId));
		if(null==shoppingCartDto){
			shoppingCartDto = new ShoppingCartDto();
			shoppingCartDto.setMemberId(memberId);
			shoppingCartDto.setToken(token);
		}
		shoppingCartDto.setShopId(shopId);
		return shoppingCartDto;
	}
	
	/**
	 * 设置选中商品的购物车到缓存中
	 * @param shoppingCartDto
	 */
	public void setCacheCart(final ShoppingCartDto shoppingCartDto){
		jedisCacheUtil.setCache(getKey(shoppingCartDto.getMemberId(),shoppingCartDto.getToken(),shoppingCartDto.getShopId()), shoppingCartDto, 40*60);
	}
	
	/**
	 * 清除缓存
	 * @param memberId
	 * @param token
	 */
	public void deleteCacheCart(final Long memberId,final String token,Long shopId){
		jedisCacheUtil.deleteCacheKey(getKey(memberId,token,shopId));
	}
	private String getKey(Long memberId, String token,Long shopId) {
		return RedisConstant.REDIS_KEY.order+"cartinfo"+SPLIT_SIGN.COLON+memberId
				+SPLIT_SIGN.COLON+shopId+SPLIT_SIGN.COLON+token;
	}
	
	private String getCartItemKey(CartItemInfo cartItemInfo){
		return REDIS_KEY.order+"cartItemInfo" + SPLIT_SIGN.COLON + cartItemInfo.getSkuCode()+SPLIT_SIGN.COLON +cartItemInfo.getTopicId();
	}
	
	private void setDefaultQuantity(CartItemInfo cartItemInfo){
		if(null==cartItemInfo.getQuantity() || cartItemInfo.getQuantity()<1){
			cartItemInfo.setQuantity(1);
		}
	}
}
