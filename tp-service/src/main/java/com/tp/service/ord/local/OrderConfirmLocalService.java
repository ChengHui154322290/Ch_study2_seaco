/**
 * NewHeight.com Inc.
 * Copyright (c) 2008-2010 All Rights Reserved.
 */
package com.tp.service.ord.local;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.tp.common.util.ImageDownUtil;
import com.tp.common.vo.Constant;
import com.tp.common.vo.OrderConstant;
import com.tp.common.vo.Constant.SPLIT_SIGN;
import com.tp.common.vo.DAOConstant.MYBATIS_SPECIAL_STRING;
import com.tp.common.vo.ord.CartConstant;
import com.tp.common.vo.ord.OrderErrorCodes;
import com.tp.dto.mmp.CouponOrderDTO;
import com.tp.dto.mmp.OrderCouponDTO;
import com.tp.dto.mmp.TopicItemInfoDTO;
import com.tp.dto.ord.CartDTO;
import com.tp.dto.ord.CartLineDTO;
import com.tp.dto.ord.SeaOrderItemDTO;
import com.tp.dto.ord.SeaOrderItemWithSupplierDTO;
import com.tp.dto.ord.SeaOrderItemWithWarehouseDTO;
import com.tp.enums.common.PlatformEnum;
import com.tp.exception.OrderServiceException;
import com.tp.query.mmp.CouponOrderQuery;
import com.tp.redis.util.JedisCacheUtil;
import com.tp.redis.util.JedisDBUtil;
import com.tp.service.mmp.ICouponUserService;
import com.tp.service.mmp.IPriceService;
import com.tp.service.ord.ISubOrderService;
import com.tp.service.ord.local.ICartLocalService;
import com.tp.service.ord.local.IOrderConfirmLocalService;
import com.tp.service.ord.local.IOrderSubmitLocalService;

/**
 * 
 * <pre>
 * 订单确认服务实现类
 * </pre>
 * 
 * @author szy
 * @version 0.0.1
 */
@Service("orderConfirmLocalService")
public class OrderConfirmLocalService implements IOrderConfirmLocalService {
	private static final Logger logger = LoggerFactory.getLogger(OrderConfirmLocalService.class);
	
	@Autowired
	private ICartLocalService cartLocalService;
	@Autowired
	private ICouponUserService couponUserService;
	@Autowired
	private IPriceService priceService;
	@Autowired
	private IOrderSubmitLocalService orderSubmitLocalService;
	@Autowired
	private JedisCacheUtil jedisCacheUtil;
    @Autowired
    private JedisDBUtil jedisDBUtil;
    @Autowired
    private ISubOrderService subOrderService;
    
	@Override
	public CartDTO calcItemPrice(Long memberId, List<Long> couponIdList) {
		CartDTO calcCartDTO = null;
		try {
			CartDTO cartDTO = getChoosedItemInfo(memberId, CartConstant.TYPE_COMMON);
			if (cartDTO != null) {
				calcCartDTO = priceService.orderTotalPrice(cartDTO, couponIdList, memberId);
			}
		} catch (Exception e) {
			logger.error("计算订单总价异常", e);
			throw new OrderServiceException("计算订单总价异常", e);
		}
		return calcCartDTO;
	}
	
	@Override
	public SeaOrderItemDTO calcItemPrice4Sea(Long memberId, List<Long> couponIdList,Integer sourceType) {
		logger.debug("---------->进入查询海淘商品信息接口");
		SeaOrderItemDTO seaOrderItem = null;
		try {
			CartDTO cartDTO = getChoosedItemInfo(memberId, CartConstant.TYPE_SEA);// 获取海淘需要购买的商品
			if (cartDTO != null) {
				seaOrderItem = orderSubmitLocalService.splitSeaOrder(cartDTO);
				if (seaOrderItem != null) {
					seaOrderItem.setMemberId(memberId);
					seaOrderItem = setFirstMinus(seaOrderItem,sourceType);
					// 海淘商品价格计算，需要算好税费、运费、总价
					seaOrderItem = priceService.hitaoOrderTotalPrice(seaOrderItem, couponIdList);
					logger.debug("用户{}订购的海淘商品数据为：{}", memberId, JSONObject.toJSONString(seaOrderItem));
					wrapItemImage(seaOrderItem);
				}
			}
		} catch (Exception e) {
			logger.error("计算订单总价异常", e);
			throw new OrderServiceException("计算订单总价异常", e);
		}
		return seaOrderItem;
	}
	
	@Override
	public Map<String, List<OrderCouponDTO>> getOrderCouponInfo(Long memberId, Integer sourceType, Integer itemType) {
		CartDTO cartDTO = getChoosedItemInfo(memberId, itemType);
		if (cartDTO == null) {
			return null;
		}
		CouponOrderQuery query = buildParam4QueryCoupon(cartDTO, memberId, sourceType, itemType);
		try {
			List<OrderCouponDTO> orderCouponList = couponUserService.queryOrderCouponList(query);
			Map<String, List<OrderCouponDTO>> map = new HashMap<String, List<OrderCouponDTO>>();
			if (orderCouponList != null && orderCouponList.size() > 0) {
				List<OrderCouponDTO> orderCouponTmpList = null;
				for (OrderCouponDTO orderCouponDTO : orderCouponList) {
					String key = orderCouponDTO.getCouponType().toString();
					if (map.get(key) != null) {
						orderCouponTmpList = map.get(key);
					} else {
						orderCouponTmpList = new ArrayList<OrderCouponDTO>();
					}
					orderCouponTmpList.add(orderCouponDTO);
					map.put(key, orderCouponTmpList);
				}
			}
			return map;
	
		} catch (Exception e) {
			logger.error("从促销平台获取用户本次订购可用的优惠券信息异常", e);
			throw new OrderServiceException(OrderErrorCodes.PROMOTION_USEFUL_CONPON_ERROR);
		}
	}
	
	/**
	 * 
	 * <pre>
	 * 构建查询优惠券信息请求参数
	 * </pre>
	 * 
	 * @param cartDTO
	 * @param memberId
	 * @return
	 */
	private CouponOrderQuery buildParam4QueryCoupon(CartDTO cartDTO, Long memberId, Integer sourceType, int itemType) {
		CouponOrderQuery query = new CouponOrderQuery();
		query.setUserId(memberId);
		query.setPrice(cartDTO.getRealSum());
		int platformType = sourceType == null ? PlatformEnum.PC.ordinal() : sourceType.intValue();
		query.setPlatformType(platformType);
		query.setItemType(itemType);
		List<CouponOrderDTO> couponOrderDTOList = new ArrayList<CouponOrderDTO>();
		List<CartLineDTO> cartLineDTOs = null;
		if (itemType == CartConstant.TYPE_COMMON) {
			cartLineDTOs = cartDTO.getLineList();
		} else {
			cartLineDTOs = new ArrayList<CartLineDTO>();
			Map<String, List<CartLineDTO>> map = cartDTO.getSeaMap();
			if (MapUtils.isNotEmpty(map)) {
				for (List<CartLineDTO> cartLineDTOList : map.values()) {
					cartLineDTOs.addAll(cartLineDTOList);
				}
			}
		}
		if (cartLineDTOs != null) {
			CouponOrderDTO couponOrderDTO = null;
			for (CartLineDTO cartLineDTO : cartLineDTOs) {
				couponOrderDTO = new CouponOrderDTO();
				couponOrderDTO.setFirstCategoryId(cartLineDTO.getLargeId());
				couponOrderDTO.setSecondCategoryId(cartLineDTO.getMediumId());
				couponOrderDTO.setThordCategoryId(cartLineDTO.getSmallId());
				couponOrderDTO.setSku(cartLineDTO.getSkuCode());
				couponOrderDTO.setSupplierId(cartLineDTO.getSupplierId());
				couponOrderDTO.setBrandId(cartLineDTO.getBrandId());
				couponOrderDTO.setItemType(cartLineDTO.getSaleType());
				if (cartLineDTO.getTopicItemInfo() != null) {
					couponOrderDTO.setPrice(cartLineDTO.getTopicItemInfo().getTopicPrice());
				}
				couponOrderDTO.setQuantity(cartLineDTO.getQuantity());
				couponOrderDTOList.add(couponOrderDTO);
			}
		}
		query.setCouponOrderDTOList(couponOrderDTOList);
		return query;
	}

	/**
	 * 
	 * <pre>
	 * 获取被勾选的购物车行
	 * </pre>
	 * 
	 * @param memberId
	 * @return
	 */
	private CartDTO getChoosedItemInfo(Long memberId, Integer cartType) {
		// 获取购物车信息
		CartDTO cartDTO = null;
		try {
			cartDTO = cartLocalService.getValidateCart(memberId, cartType);
		} catch (Exception e) {
			logger.error("加载购物车数据异常", e);
			return null;
		}
		return cartDTO;
	}
	
	@Override
	public CartDTO calcItemPriceBuyNow(Long memberId, String buyNowId, List<Long> couponIdList) {
		logger.debug("---------->进入计算购买商品的价格接口");
		CartDTO calcCartDTO = null;
		try {
			Object object = jedisCacheUtil.getCache(buyNowId);
			CartDTO cartDTO = (CartDTO) object;
			if (null != cartDTO) {
				calcCartDTO = priceService.orderTotalPrice(cartDTO, couponIdList, memberId);
			}
	
		} catch (Exception e) {
			logger.error("计算订单总价异常", e);
			throw new OrderServiceException("计算订单总价异常", e);
		}
		return calcCartDTO;
	}

	@Override
	public SeaOrderItemDTO calcItemPrice4SeaBuyNow(Long memberId, String buyNowId, List<Long> couponIdList,Integer sourceType) {
		logger.debug("---------->进入查询海淘商品信息接口");
		SeaOrderItemDTO seaOrderItem = null;
	
		try {
			Object object = jedisCacheUtil.getCache(buyNowId);
			CartDTO cartDTO = (CartDTO) object;
			if (cartDTO != null) {
				seaOrderItem = orderSubmitLocalService.splitSeaOrder(cartDTO);
				if (seaOrderItem != null) {
					seaOrderItem.setMemberId(memberId);
					seaOrderItem = setFirstMinus(seaOrderItem,sourceType);
					// 海淘商品价格计算，需要算好税费、运费、总价
					seaOrderItem = priceService.hitaoOrderTotalPrice(seaOrderItem, couponIdList);
				}
				wrapItemImage(seaOrderItem);
			}
		} catch (Exception e) {
			logger.error("计算订单总价异常", e);
			throw new OrderServiceException("计算订单总价异常", e);
		}
		return seaOrderItem;
	}
	
	public void wrapItemImage(final SeaOrderItemDTO seaOrderItem){
		if(seaOrderItem!=null){
			List<SeaOrderItemWithSupplierDTO> seaOrderItemWithSupplierList = seaOrderItem.getSeaOrderItemWithSupplierList();
			if(CollectionUtils.isNotEmpty(seaOrderItemWithSupplierList)){
				for(SeaOrderItemWithSupplierDTO orderSupplier:seaOrderItemWithSupplierList){
					List<SeaOrderItemWithWarehouseDTO> seaOrderItemWithWarehouseList = orderSupplier.getSeaOrderItemWithWarehouseList();
					if(CollectionUtils.isNotEmpty(seaOrderItemWithWarehouseList)){
						for(SeaOrderItemWithWarehouseDTO orderWarehouse:seaOrderItemWithWarehouseList){
							List<CartLineDTO> cartLineList = orderWarehouse.getCartLineList();
							if(CollectionUtils.isNotEmpty(cartLineList)){
								for(CartLineDTO orderItem:cartLineList){
									orderItem.setItemPic(ImageDownUtil.getOriginalImage(Constant.IMAGE_URL_TYPE.item.url, orderItem.getItemPic()));
									TopicItemInfoDTO topicItemInfo = orderItem.getTopicItemInfo();
									if(topicItemInfo!=null){
										topicItemInfo.setTopicImage(ImageDownUtil.getOriginalImage(Constant.IMAGE_URL_TYPE.item.url, topicItemInfo.getTopicImage()));
										topicItemInfo.setImageFullPath(ImageDownUtil.getOriginalImage(Constant.IMAGE_URL_TYPE.item.url, topicItemInfo.getImageFullPath()));
									}
								}
							}
						}
					}
				}
			}
		}
	}
	
    @Override
    public Map<String, List<OrderCouponDTO>> getOrderCouponInfoBuyNow(Long memberId, Integer sourceType, Integer itemType, String uuid){
        Object object = jedisCacheUtil.getCache(uuid);
        CartDTO cartDTO = (CartDTO) object;
        if (cartDTO == null) {
            logger.error("get buynow item from redisDB error");
            return null;
        }
        return queryAvailableCoupons(buildParam4QueryCoupon(cartDTO, memberId, sourceType, itemType));
    }
    
    public Map<String, List<OrderCouponDTO>> queryAvailableCoupons(CouponOrderQuery query) {
        try {
            List<OrderCouponDTO> orderCouponList = couponUserService.queryOrderCouponList(query);
            Map<String, List<OrderCouponDTO>> map = new HashMap<String, List<OrderCouponDTO>>();
            if (orderCouponList != null && orderCouponList.size() > 0) {
                List<OrderCouponDTO> orderCouponTmpList = null;
                for (OrderCouponDTO orderCouponDTO : orderCouponList) {
                    String key = orderCouponDTO.getCouponType().toString();
                    if (map.get(key) != null) {
                        orderCouponTmpList = map.get(key);
                    } else {
                        orderCouponTmpList = new ArrayList<OrderCouponDTO>();
                    }
                    orderCouponTmpList.add(orderCouponDTO);
                    map.put(key, orderCouponTmpList);
                }
            }
            return map;

        } catch (Exception e) {
            logger.error("query the current order's available coupons exception", e);
            return null;
        }
    }
    
    /**
     * 首次下单减
     * @param seaOrderItemDTO
     * @return
     */
    public SeaOrderItemDTO setFirstMinus(final SeaOrderItemDTO seaOrderItemDTO,Integer sourceType){
    	if(sourceType!=null && (PlatformEnum.IOS.code == sourceType || PlatformEnum.ANDROID.code==sourceType)){
    		Map<String,Object> params = new HashMap<String,Object>();
        	params.put("memberId", seaOrderItemDTO.getMemberId());
        	params.put(MYBATIS_SPECIAL_STRING.COLUMNS.name(), " source in ("+PlatformEnum.IOS.code+SPLIT_SIGN.COMMA+PlatformEnum.ANDROID.code+") and order_status !="+OrderConstant.ORDER_STATUS.CANCEL.getCode());
        	Integer count = subOrderService.queryByParamCount(params);
        	if(count==0){
        		seaOrderItemDTO.setFirstMinus(Boolean.TRUE);
        	}
    	}
    	return seaOrderItemDTO;
    }
}
