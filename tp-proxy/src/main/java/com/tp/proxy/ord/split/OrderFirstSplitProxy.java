package com.tp.proxy.ord.split;

import static com.tp.util.BigDecimalUtil.add;
import static com.tp.util.BigDecimalUtil.divide;
import static com.tp.util.BigDecimalUtil.multiply;
import static com.tp.util.BigDecimalUtil.subtract;
import static com.tp.util.BigDecimalUtil.toPrice;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.tp.common.vo.Constant;
import com.tp.dto.mmp.enums.CouponType;
import com.tp.dto.ord.OrderDto;
import com.tp.dto.ord.OrderInitDto;
import com.tp.model.ord.OrderItem;
import com.tp.model.ord.OrderPromotion;

/**
 * 首单立减
 * @author szy
 *
 */
@Service
public class OrderFirstSplitProxy  implements IOrderAmountSplitProxy{

	private static final Double FIRST_MINUS_AMOUNT = 5.00D;
	@Override
	public OrderDto splitAmount(OrderDto orderDto, OrderInitDto orderInitDto) {
		return orderDto;
	}
	
	/**
	 * 拆分商品到对应的券
	 * @param orderInitDto
	 * @return
	 */
	public OrderInitDto splitFirstMinus(OrderInitDto orderInitDto){
		Map<OrderItem,Double> itemMinusAmountMap = new HashMap<OrderItem,Double>();
		if(orderInitDto.getFirstMinus()){
			List<OrderItem> orderItemList = orderInitDto.getOrderItemList();

			Double total = Constant.ZERO;
			for(OrderItem orderItem:orderItemList){
				total = new BigDecimal(total).add(new BigDecimal(orderItem.getSubTotal())).doubleValue();
			}
			if(total<=0){
				return orderInitDto;
			}
			Double firstMinusAmount = FIRST_MINUS_AMOUNT;
			if(total < FIRST_MINUS_AMOUNT){
				firstMinusAmount = total;
			}
			Double sumAmount = Constant.ZERO;
			OrderItem maxOrderItem = orderItemList.get(0);
			for(OrderItem orderItem:orderItemList){
				Double subFirstMinus = toPrice(multiply(firstMinusAmount,divide(orderItem.getSubTotal(),total,DIVIDE_SCALE)));
				if(subFirstMinus>0){
					Double subItemFristMinus = toPrice(multiply(subFirstMinus,divide(orderItem.getItemAmount(),orderItem.getSubTotal(),DIVIDE_SCALE)));
					Double subTaxFristMinus = toPrice(multiply(subFirstMinus,divide(orderItem.getTaxFee(),orderItem.getSubTotal(),DIVIDE_SCALE)));
					Double subFreightFristMinus = toPrice(multiply(subFirstMinus,divide(orderItem.getFreight(),orderItem.getSubTotal(),DIVIDE_SCALE)));
					Double sumSubFirstMinus = toPrice(add(add(subItemFristMinus,subTaxFristMinus),subFreightFristMinus));
					if(sumSubFirstMinus!=subFirstMinus){
						subItemFristMinus = toPrice(add(subItemFristMinus,subtract(subFirstMinus,sumSubFirstMinus)));
					}
					orderItem.setItemAmount(toPrice(subtract(orderItem.getItemAmount(),subItemFristMinus)));
					orderItem.setTaxFee(toPrice(subtract(orderItem.getTaxFee(),subTaxFristMinus)));
					orderItem.setFreight(toPrice(subtract(orderItem.getFreight(),subFreightFristMinus)));
				}
				orderItem.setSubTotal(toPrice(subtract(orderItem.getSubTotal(),subFirstMinus)));
				sumAmount = add(sumAmount,subFirstMinus).doubleValue();
				if(maxOrderItem.getSubTotal()<orderItem.getSubTotal()){
					maxOrderItem = orderItem;
				}
				itemMinusAmountMap.put(orderItem, subFirstMinus);
			}
			Double residue = subtract(sumAmount,firstMinusAmount).doubleValue();
			Double maxSubTotal = toPrice(add(maxOrderItem.getSubTotal(),residue));
			if(residue!=Constant.ZERO && maxSubTotal>0){
				maxOrderItem.setSubTotal(toPrice(add(maxOrderItem.getSubTotal(),residue)));
				maxOrderItem.setItemAmount(toPrice(add(maxOrderItem.getItemAmount(),residue)));
				itemMinusAmountMap.put(maxOrderItem, toPrice(add(itemMinusAmountMap.get(maxOrderItem),residue)));
				sumAmount = firstMinusAmount;
			}
			orderInitDto.setDiscountTotal(toPrice(add(orderInitDto.getDiscountTotal(),sumAmount)));
			for(OrderItem orderItem:orderItemList){
				OrderPromotion orderPromotion = new OrderPromotion();
				orderPromotion.setCouponCode("-1000");
				orderPromotion.setCouponFaceAmount(FIRST_MINUS_AMOUNT);
				orderPromotion.setCouponType(CouponType.FIRST_MINUS.ordinal());
				orderPromotion.setCouponUserId(-1L);
				orderPromotion.setPromotionId(-1L);
				orderPromotion.setPromotionName("首单立减"+FIRST_MINUS_AMOUNT+"元");
				orderPromotion.setSourceType(orderInitDto.getOrderSource());
				orderPromotion.setPromoterId(null);
				orderPromotion.setDiscount(itemMinusAmountMap.get(orderItem));
				orderPromotion.setType(-1);
				orderPromotion.setSupplierId(0L);
				orderPromotion.setOrderCode(orderItem.getOrderCode());
				orderPromotion.setParentOrderCode(orderItem.getParentOrderCode());
				orderItem.getOrderPromotionList().add(orderPromotion);
			}
		}
		
		return orderInitDto;
	}
	
}
