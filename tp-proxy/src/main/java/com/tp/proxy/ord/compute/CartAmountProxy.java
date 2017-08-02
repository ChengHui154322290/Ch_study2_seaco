package com.tp.proxy.ord.compute;

import static com.tp.util.BigDecimalUtil.add;
import static com.tp.util.BigDecimalUtil.divide;
import static com.tp.util.BigDecimalUtil.multiply;
import static com.tp.util.BigDecimalUtil.subtract;
import static com.tp.util.BigDecimalUtil.toPrice;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.util.OrderUtils;
import com.tp.common.vo.Constant;
import com.tp.common.vo.OrderConstant;
import com.tp.common.vo.Constant.SPLIT_SIGN;
import com.tp.common.vo.bse.ClearanceChannelsEnum;
import com.tp.dto.ord.PreOrderDto;
import com.tp.dto.ord.ShoppingCartDto;
import com.tp.model.mmp.FreightTemplate;
import com.tp.model.ord.OrderItem;
import com.tp.model.ord.OrderPromotion;
import com.tp.model.ord.SubOrder;
import com.tp.proxy.ord.split.OrderSplitProxy;
import com.tp.service.mmp.IFreightTemplateService;
import com.tp.util.BigDecimalUtil;

/**
 * 计算购物车中选中商品价格
 * @author szy
 *
 */
@Service
public class CartAmountProxy implements IAmountProxy<ShoppingCartDto> {
	public static final Double TAX_FREE_AMOUNT_LIMIT= 2000.00D;
	@Autowired
	private OrderSplitProxy orderSplitProxy;
	@Override
	public ShoppingCartDto computeAmount(final ShoppingCartDto shoppingCartDto) {
		computeItemSubTotalBase(shoppingCartDto);
		computeItemTaxFee(shoppingCartDto);
		computeTotalAmount(shoppingCartDto);
		return shoppingCartDto;
	}

	/**
	 * 计算商品基本价格
	 * @param shoppingCartDto
	 * @return
	 */
	public ShoppingCartDto computeItemSubTotalBase(final ShoppingCartDto shoppingCartDto){
		List<PreOrderDto> subOrderList = shoppingCartDto.getPreSubOrderList();
		Double payableAmount = ZERO;
		for(SubOrder subOrder:subOrderList){
			List<OrderItem> orderItemList = subOrder.getOrderItemList();
			Double total = ZERO;
			for(OrderItem orderItem:orderItemList){
				orderItem.setItemAmount(toPrice(multiply(orderItem.getQuantity(),orderItem.getPrice())));
				orderItem.setSubTotal(orderItem.getItemAmount());
				orderItem.setOriginalSubTotal(orderItem.getItemAmount());
				if(orderItem.getSelectedBoolean()){
					total = toPrice(add(total,orderItem.getSubTotal()));
				}
			}
			subOrder.setItemTotal(total);
			subOrder.setOriginalTotal(total);
			payableAmount = BigDecimalUtil.add(payableAmount, total).doubleValue();
		}
		shoppingCartDto.setActuallyAmount(payableAmount);
		shoppingCartDto.setPayableAmount(payableAmount);
		shoppingCartDto.setSummation(payableAmount);
		shoppingCartDto.setOrginItemAmount(payableAmount);
		return shoppingCartDto;
	}
	
	/**
	 * 计算商品项税金
	 * 国内直发/海外直邮 免税金
	 */
	public ShoppingCartDto computeItemTaxFee(final ShoppingCartDto shoppingCartDto){
		List<PreOrderDto> subOrderList = shoppingCartDto.getPreSubOrderList();
		Double taxFeeSum = Constant.ZERO;
		for(SubOrder subOrder:subOrderList){
			subOrder = orderSplitProxy.initItemTaxRate(subOrder);
			List<OrderItem> orderItemList = subOrder.getOrderItemList();
			for(OrderItem orderItem:orderItemList){
				if(orderItem.getIsSea()){
					Double taxFee = toPrice(divide(multiply(orderItem.getItemAmount(),orderItem.getTaxRate()),100,6));
					orderItem.setTaxFee(taxFee);
					if (OrderConstant.OrderType.DOMESTIC.getCode().equals(orderItem.getStorageType())
					   || ClearanceChannelsEnum.HWZY.id.equals(orderItem.getSeaChannel())
					   || OrderConstant.OrderType.FAST.getCode().equals(orderItem.getStorageType())){
						orderItem.setTaxFee(ZERO);
					}
					orderItem.setOrigTaxFee(orderItem.getTaxFee());
					orderItem.setOriginalSubTotal(toPrice(add(orderItem.getOriginalSubTotal(),orderItem.getTaxFee())));
					orderItem.setSubTotal(toPrice(add(orderItem.getSubTotal(),orderItem.getTaxFee())));
					taxFeeSum = BigDecimalUtil.add(taxFeeSum, orderItem.getTaxFee()).doubleValue();
				}
			}
		}
		shoppingCartDto.setOrginTaxFee(taxFeeSum);
		return shoppingCartDto;
	}
	/**
	 * 计算订单，子订单合计金额
	 * @param orderInitDto
	 * @return
	 */
	public ShoppingCartDto computeTotalAmount(ShoppingCartDto orderInitDto){
		Double totalReturnMoney=0d;
		orderInitDto.setActuallyAmount(Constant.ZERO);
		orderInitDto.setTaxes(Constant.ZERO);
		orderInitDto.setFreight(Constant.ZERO);
		orderInitDto.setPayableAmount(Constant.ZERO);
		orderInitDto.setDiscountTotal(Constant.ZERO);
		for(PreOrderDto subOrder:orderInitDto.getPreSubOrderList()){
			subOrder.setItemTotal(Constant.ZERO);
			subOrder.setTaxFee(Constant.ZERO);
			subOrder.setFreight(Constant.ZERO);
			subOrder.setOriginalTotal(Constant.ZERO);
			subOrder.setDiscount(Constant.ZERO);
			subOrder.getOrderItemList().forEach(new Consumer<OrderItem>(){
				Double subReturnMoney=0d;
				public void accept(OrderItem orderItem) {
					if(orderItem.getSelectedBoolean()){
						subOrder.setItemTotal(toPrice(add(subOrder.getItemTotal(),orderItem.getItemAmount())));
						subOrder.setTaxFee(toPrice(add(subOrder.getTaxFee(),orderItem.getTaxFee())));
						subOrder.setFreight(toPrice(add(subOrder.getFreight(),orderItem.getFreight())));
						subOrder.setOriginalTotal(toPrice(add(subOrder.getOriginalTotal(),orderItem.getOriginalSubTotal())));
						subOrder.setDiscount(toPrice(add(subOrder.getDiscount(),add(orderItem.getOrderCouponAmount(),orderItem.getCouponAmount()))));
						Double  subreturnMoney=orderItem.getSalesPrice()*orderItem.getCommisionRate();
    					subReturnMoney=subReturnMoney+subreturnMoney;
					}
					subOrder.setReturnMoney(subReturnMoney);
				}
			});
			totalReturnMoney=totalReturnMoney+subOrder.getReturnMoney();
			subOrder.setTotal(toPrice(add(add(subOrder.getItemTotal(),subOrder.getTaxFee()),subOrder.getFreight())));
			orderInitDto.setPayableAmount(toPrice(add(orderInitDto.getPayableAmount(),subOrder.getOriginalTotal())));
			orderInitDto.setActuallyAmount(toPrice(add(orderInitDto.getActuallyAmount(),subOrder.getItemTotal())));
			orderInitDto.setTaxes(toPrice(add(orderInitDto.getTaxes(),subOrder.getTaxFee())));
			orderInitDto.setFreight(toPrice(add(orderInitDto.getFreight(),subOrder.getFreight())));
			orderInitDto.setDiscountTotal(toPrice(add(orderInitDto.getDiscountTotal(),subOrder.getDiscount())));
		}
		orderInitDto.setReturnMoney(totalReturnMoney);
		orderInitDto.setSummation(toPrice(add(add(orderInitDto.getActuallyAmount(),orderInitDto.getTaxes()),orderInitDto.getFreight())));
		return orderInitDto;
	}
	
	/**
	 * 如果子订单小于0.1元则订单金额为0.1元
	 * @param orderInitDto
	 * @return
	 */
	public ShoppingCartDto addAmountIfZero(ShoppingCartDto orderInitDto){
		for(PreOrderDto subOrder:orderInitDto.getPreSubOrderList()){
			if(OrderUtils.isSeaOrder(subOrder.getType()) && subOrder.getTotal().doubleValue()<MIN_AMOUNT
				&& subOrder.getPutSign()!=null && subOrder.getPutSign()>0){
				Double minus = toPrice(subtract(MIN_AMOUNT,subOrder.getTotal()));
				OrderItem orderItem = subOrder.getOrderItemList().get(0);
				orderItem.setItemAmount(toPrice(add(orderItem.getItemAmount(),minus)));
				if(orderItem.getOrderCouponAmount()!=null && orderItem.getOrderCouponAmount()>0){
					orderItem.setOrderCouponAmount(toPrice(subtract(orderItem.getOrderCouponAmount(),minus)));
					if(orderItem.getOrderCouponAmount()<0){
						orderItem.setOrderCouponAmount(ZERO);
					}
				}
				if(orderItem.getCouponAmount()!=null && orderItem.getCouponAmount()>0){
					orderItem.setCouponAmount(toPrice(subtract(orderItem.getCouponAmount(),minus)));
					if(orderItem.getCouponAmount()<0){
						orderItem.setCouponAmount(ZERO);
					}
				}
				List<OrderPromotion> orderPromotionList = orderItem.getOrderPromotionList();
				if(CollectionUtils.isNotEmpty(orderPromotionList)){
					OrderPromotion orderPromotion = orderPromotionList.get(0);
					orderPromotion.setDiscount(toPrice(subtract(orderItem.getDiscount(),minus)));
					if(orderPromotion.getDiscount()<0){
						orderPromotion.setDiscount(ZERO);
					}
				}
				
				subOrder.setItemTotal(MIN_AMOUNT);
				subOrder.setDiscount(toPrice(subtract(subOrder.getDiscount(),minus)));
				if(subOrder.getDiscount()<0){
					subOrder.setDiscount(ZERO);
				}
				orderInitDto.setActuallyAmount(toPrice(add(orderInitDto.getActuallyAmount(),minus)));
				orderInitDto.setDiscountTotal(toPrice(subtract(orderInitDto.getDiscountTotal(),minus)));
			}
			orderInitDto.setSummation(toPrice(add(add(orderInitDto.getActuallyAmount(),orderInitDto.getTaxes()),orderInitDto.getFreight())));
		}
		return orderInitDto;
	}
}
