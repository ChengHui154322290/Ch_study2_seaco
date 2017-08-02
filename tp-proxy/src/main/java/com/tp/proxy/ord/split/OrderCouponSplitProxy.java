package com.tp.proxy.ord.split;

import static com.tp.util.BigDecimalUtil.add;
import static com.tp.util.BigDecimalUtil.divide;
import static com.tp.util.BigDecimalUtil.multiply;
import static com.tp.util.BigDecimalUtil.subtract;
import static com.tp.util.BigDecimalUtil.toPrice;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.vo.Constant;
import com.tp.dto.mmp.enums.CouponType;
import com.tp.dto.ord.OrderDto;
import com.tp.dto.ord.OrderInitDto;
import com.tp.model.mmp.Coupon;
import com.tp.model.mmp.CouponUser;
import com.tp.model.ord.CartItemInfo;
import com.tp.model.ord.OrderItem;
import com.tp.model.ord.OrderPromotion;
import com.tp.model.ord.SubOrder;
import com.tp.proxy.mmp.CouponUserProxy;

/**
 * 优惠券、促销优惠分拆到订单、商品项
 * @author szy
 *
 */
@Service
public class OrderCouponSplitProxy  implements IOrderAmountSplitProxy{
	@Autowired
	private CouponUserProxy couponUserProxy;
	@Autowired
	private CouponFilterProxy couponFilterProxy;
	

	@Override
	public OrderDto splitAmount(OrderDto orderDto, OrderInitDto orderInitDto) {
		List<SubOrder> subOrderList = orderDto.getSubOrderList();
		for(SubOrder subOrder:subOrderList){
			if(subOrder.getDiscount()>0){
				List<OrderItem> orderItemList = subOrder.getOrderItemList();
				Double subTotal = Constant.ZERO;
				OrderItem maxOrderItem = orderItemList.get(0);
				for(OrderItem orderItem:orderItemList){
					Double freight = toPrice(multiply(divide(maxOrderItem.getItemAmount(),subOrder.getTotal(),DIVIDE_SCALE),subOrder.getFreight()));
					orderItem.setFreight(freight);
					subTotal = add(subTotal,freight).doubleValue();
				}
				maxOrderItem.setFreight(toPrice(add(maxOrderItem.getFreight(),subtract(subOrder.getFreight(),subTotal))));
			}
		}
		return orderDto;
	}
	
	/**
	 * 拆分商品到对应的券
	 * @param orderInitDto
	 * @return
	 */
	public OrderInitDto splitCouponList(OrderInitDto orderInitDto){
		List<CouponUser> couponUserList = couponUserProxy.queryCouponListbyCouponUserIdList(orderInitDto.getCouponIds()).getData();
		if(CollectionUtils.isNotEmpty(couponUserList)){
			Map<Coupon,List<CartItemInfo>> couponCartItemMap = new HashMap<Coupon,List<CartItemInfo>>();
			Map<CouponUser,List<CartItemInfo>> couponUserCartItemMap = new HashMap<CouponUser,List<CartItemInfo>>();
			List<OrderItem> orderItemList = orderInitDto.getOrderItemList();
			if(CollectionUtils.isNotEmpty(orderItemList)){
				List<CartItemInfo> cartItemInfoList = orderInitDto.getCartItemInfoList();
				if(CollectionUtils.isNotEmpty(cartItemInfoList)){
					cartItemInfoList.forEach(new Consumer<CartItemInfo>(){
						public void accept(CartItemInfo t) {
							t.setPlatformId(orderInitDto.getOrderSource());
						}
					});
				}
				for(CouponUser couponUser:couponUserList){
					couponUser.getCoupon().receiveDate = couponUser.getCreateTime();
					couponCartItemMap.put(couponUser.getCoupon(), new ArrayList<CartItemInfo>(cartItemInfoList));
					couponUserCartItemMap.put(couponUser, new ArrayList<CartItemInfo>(cartItemInfoList));
				}
				//过滤掉不对应的商品
				couponCartItemMap = couponFilterProxy.operateAmountfilter(couponCartItemMap);
				for(Entry<CouponUser,List<CartItemInfo>> entry:couponUserCartItemMap.entrySet()){
					Map<OrderItem,Double> itemAmountMap = new HashMap<OrderItem,Double>();//每个商品对应的要拆分金额
					Double orgItemAmount = Constant.ZERO;
					List<OrderItem> subOrderItemList = queryOrderItemByCartItem(couponCartItemMap.get(entry.getKey().getCoupon()),orderItemList);
					Coupon coupon = new Coupon();
					BeanUtils.copyProperties(entry.getKey().getCoupon(), coupon);//不同券对应同一批次
					if(CollectionUtils.isNotEmpty(subOrderItemList)){
						for(OrderItem orderItem:subOrderItemList){
							Double couponOrgTotalAmount = orderItem.getItemAmount();
							if(!coupon.getCouponType().equals(CouponType.HAS_CONDITION.ordinal())){
								couponOrgTotalAmount = add(couponOrgTotalAmount,orderItem.getTaxFee()).doubleValue();
								couponOrgTotalAmount = add(couponOrgTotalAmount,orderItem.getFreight()).doubleValue();
							}
							if(couponOrgTotalAmount>0){
								orgItemAmount = toPrice(add(orgItemAmount,couponOrgTotalAmount));
								itemAmountMap.put(orderItem, couponOrgTotalAmount);
							}else{
								itemAmountMap.put(orderItem, Constant.ZERO);
							}
						}
					}
					//计算券对应每商品优惠金额
					if(CollectionUtils.isNotEmpty(subOrderItemList)){
						Map<OrderItem,Double> cartItemCouponMap = new HashMap<OrderItem,Double>();//每个商品对应的要拆分优惠金额
						operatorItemCouponAmount(coupon,orgItemAmount,subOrderItemList,cartItemCouponMap,itemAmountMap);
									
						//拆分到每个商品上
						splitCouponAmountToItem(itemAmountMap,cartItemCouponMap,coupon);
						Double discountTotal = initOrderPromotionList(subOrderItemList,coupon,entry.getKey(),cartItemCouponMap);
						orderInitDto.setDiscountTotal(toPrice(add(orderInitDto.getDiscountTotal(),discountTotal)));
					}
				}
			}
		}
		return orderInitDto;
	}
	
	/**
	 * 计算商品项对应卡券上的优惠金额
	 * @param coupon
	 * @param orgItemAmount
	 * @param subOrderItemList
	 * @param cartItemCouponMap
	 * @param itemCouponAmount
	 */
	public void operatorItemCouponAmount(final Coupon coupon,final Double orgItemAmount,final List<OrderItem> subOrderItemList,
			final Map<OrderItem,Double> cartItemCouponMap,
			final Map<OrderItem,Double> itemAmountMap){
		coupon.factValue = coupon.getFaceValue().doubleValue();//券的实际值
		Double sumCouponAmount = Constant.ZERO;
		if(orgItemAmount>Constant.ZERO){
			if(coupon.getFaceValue()>=orgItemAmount){//如果商品总额小于面值，则实际值=商品总额
				coupon.factValue = orgItemAmount;
				for(int i=0;i<subOrderItemList.size();i++){
					OrderItem orderItem = subOrderItemList.get(i);
					cartItemCouponMap.put(orderItem, itemAmountMap.get(orderItem));
				}
			}else{
				OrderItem maxItem = subOrderItemList.get(0);
				for(int i=0;i<subOrderItemList.size();i++){
					OrderItem orderItem = subOrderItemList.get(i);
					Double couponAmount = toPrice(multiply(coupon.factValue,divide(itemAmountMap.get(orderItem),orgItemAmount,DIVIDE_SCALE)));
					sumCouponAmount = toPrice(add(sumCouponAmount,couponAmount));
					cartItemCouponMap.put(orderItem, couponAmount);
					if(cartItemCouponMap.get(maxItem)<cartItemCouponMap.get(orderItem)){
						maxItem = orderItem;
					}
				}
				if(coupon.getFaceValue().doubleValue()!=sumCouponAmount){
					cartItemCouponMap.put(maxItem, toPrice(add(cartItemCouponMap.get(maxItem),subtract(coupon.factValue,sumCouponAmount))));
				}
			}
		}else{
			for(int i=0;i<subOrderItemList.size();i++){
				cartItemCouponMap.put(subOrderItemList.get(i), Constant.ZERO);
			}
		}
	}
	
	/**
	 * 把计算出来的商品项优惠金额拆分到商品项上
	 * @param itemAmountMap
	 * @param cartItemCouponMap
	 * @param coupon
	 */
	public void splitCouponAmountToItem(final Map<OrderItem,Double> itemAmountMap,final Map<OrderItem,Double> cartItemCouponMap,final Coupon coupon){
		itemAmountMap.forEach(new BiConsumer<OrderItem,Double>(){
			public void accept(OrderItem orderItem, Double amount) {
				Double couponAmount = cartItemCouponMap.get(orderItem);
				Double couponOrgTotalAmount =itemAmountMap.get(orderItem);
				if(couponAmount.equals(Constant.ZERO) || couponOrgTotalAmount.equals(Constant.ZERO)){
					return;
				}
				Double sumCouponAmount = Constant.ZERO;
				Double itemAmount= toPrice(multiply(couponAmount,divide(orderItem.getItemAmount(),couponOrgTotalAmount,DIVIDE_SCALE)));
				Double taxfFee = Constant.ZERO; 
				Double freight =Constant.ZERO; 
				if(coupon.getCouponType()==CouponType.NO_CONDITION.ordinal()){
					taxfFee = toPrice(multiply(couponAmount,divide(orderItem.getTaxFee(),couponOrgTotalAmount,DIVIDE_SCALE)));
					freight = toPrice(multiply(couponAmount,divide(orderItem.getFreight(),couponOrgTotalAmount,DIVIDE_SCALE)));
					
					orderItem.setTaxFee(toPrice(subtract(orderItem.getTaxFee(),taxfFee)));
					orderItem.setFreight(toPrice(subtract(orderItem.getFreight(),freight)));
					orderItem.setCouponAmount(toPrice(add(orderItem.getCouponAmount(),couponAmount)));
				}else if(coupon.getCouponType()==CouponType.HAS_CONDITION.ordinal()){
					orderItem.setOrderCouponAmount(toPrice(add(orderItem.getOrderCouponAmount(),couponAmount)));
				}
				sumCouponAmount = toPrice(add(add(itemAmount,taxfFee),freight));
				if(couponAmount!=sumCouponAmount){
					itemAmount = toPrice(add(itemAmount,subtract(couponAmount,sumCouponAmount)));
					if(coupon.getCouponType()==CouponType.NO_CONDITION.ordinal()){
						orderItem.setCouponAmount(toPrice(add(orderItem.getCouponAmount(),subtract(couponAmount,sumCouponAmount))));
					}else if(coupon.getCouponType()==CouponType.HAS_CONDITION.ordinal()){
						orderItem.setOrderCouponAmount(toPrice(add(orderItem.getOrderCouponAmount(),subtract(couponAmount,sumCouponAmount))));
					}
				}
				orderItem.setItemAmount(toPrice(subtract(orderItem.getItemAmount(),itemAmount)));
				orderItem.setSubTotal(toPrice(subtract(orderItem.getSubTotal(),couponAmount)));
			}
		});
	}
	
	/**
	 * 初始化订单优惠拆分信息
	 * @param subOrderItemList
	 * @param coupon
	 * @param couponUser
	 * @param cartItemCouponMap
	 * @return
	 */
	private Double initOrderPromotionList(List<OrderItem> subOrderItemList,final Coupon coupon,final CouponUser couponUser,final Map<OrderItem,Double> cartItemCouponMap){
		BigDecimal discountTotal = BigDecimal.ZERO;
		for(OrderItem orderItem:subOrderItemList){
			OrderPromotion orderPromotion = new OrderPromotion();
			orderPromotion.setCouponCode(String.valueOf(couponUser.getId()));
			orderPromotion.setCouponFaceAmount(coupon.getFaceValue().doubleValue());
			orderPromotion.setCouponType(coupon.getCouponType());
			orderPromotion.setCouponUserId(couponUser.getId());
			orderPromotion.setPromotionName(coupon.getCouponName());
			orderPromotion.setSourceType(coupon.getSourceType());
			orderPromotion.setPromoterId(couponUser.getPromoterId());
			orderPromotion.setDiscount(cartItemCouponMap.get(orderItem));
			orderPromotion.setPromotionId(coupon.getId());
			orderPromotion.setType(coupon.getCouponType());
			orderPromotion.setSupplierId(coupon.getSourceId());
			orderPromotion.setOrderCode(orderItem.getOrderCode());
			orderPromotion.setParentOrderCode(orderItem.getParentOrderCode());
			orderItem.getOrderPromotionList().add(orderPromotion);
			discountTotal = add(discountTotal,orderPromotion.getDiscount());
		}
		return toPrice(discountTotal);
	}
	
	private List<OrderItem> queryOrderItemByCartItem(List<CartItemInfo> cartItemInfoList,List<OrderItem> orderItemList){
		List<OrderItem> subOrderItemList = new ArrayList<OrderItem>();
		if(CollectionUtils.isNotEmpty(cartItemInfoList)){
			if(CollectionUtils.isNotEmpty(orderItemList)){
				cartItemInfoList.forEach(new Consumer<CartItemInfo>(){
					public void accept(CartItemInfo cartItemInfo) {
						orderItemList.forEach(new Consumer<OrderItem>(){
							public void accept(OrderItem orderItem) {
								if(cartItemInfo.getSkuCode().equals(orderItem.getSkuCode()) && cartItemInfo.getTopicId().equals(orderItem.getTopicId())){
									subOrderItemList.add(orderItem);
								}
							}
						});
					}
				});
			}
		}
		return subOrderItemList;
	}
	
}
