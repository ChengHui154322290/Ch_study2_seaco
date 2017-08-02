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
import java.util.function.BiConsumer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.util.OrderUtils;
import com.tp.common.vo.Constant;
import com.tp.common.vo.mmp.TopicConstant;
import com.tp.dto.common.ResultInfo;
import com.tp.dto.ord.OrderInitDto;
import com.tp.dto.ord.PreOrderDto;
import com.tp.dto.ord.SimpleFullDiscountDTO;
import com.tp.model.dss.ChannelInfo;
import com.tp.model.mmp.PointMember;
import com.tp.model.mmp.Topic;
import com.tp.model.ord.OrderItem;
import com.tp.model.ord.OrderPoint;
import com.tp.proxy.dss.ChannelInfoProxy;
import com.tp.proxy.mmp.TopicProxy;
import com.tp.service.mem.IMemberInfoService;
import com.tp.service.mmp.IPointMemberService;

/**
 * 订单积分计算
 * @author szy
 *
 */
@Service
public class PointAmountProxy implements IAmountProxy<OrderInitDto> {

	@Autowired
	private IPointMemberService pointMemberService;
	@Autowired
	private ChannelInfoProxy channelInfoProxy;
	@Autowired
	IMemberInfoService memberInfoService;
	@Autowired
	TopicProxy  topicProxy;
	
	@Override
	public OrderInitDto computeAmount(final OrderInitDto orderInitDto) {
		initPoint(orderInitDto);
		if(orderInitDto.getUsedPointSign()){
			Integer points = orderInitDto.getUsedPoint()!=null?orderInitDto.getUsedPoint():0;
			Map<OrderItem,Double> pointOrderItemMap = new HashMap<OrderItem,Double>();
			Integer totalUsedPoint = 0;
			for(PreOrderDto subOrder:orderInitDto.getPreSubOrderList()){
				if(subOrder.getUsedPointSign()){
					if(OrderUtils.isSeaOrder(subOrder.getType()) && subOrder.getTotal().doubleValue()>MIN_AMOUNT){
						subOrder.setSubUsedPoint(toPrice(multiply(subtract(subOrder.getTotal(),MIN_AMOUNT),100)).intValue());
						totalUsedPoint+=subOrder.getSubUsedPoint();
					}else {
						subOrder.setSubUsedPoint(toPrice(multiply(subOrder.getTotal(),100)).intValue());
						totalUsedPoint+=subOrder.getSubUsedPoint();
					}
				}
			}
			Integer preTotalPoints=0;
			PreOrderDto minSubOrder = orderInitDto.getPreSubOrderList().get(0);
			for(PreOrderDto subOrder:orderInitDto.getPreSubOrderList()){
				if(subOrder.getSubUsedPoint()>0){
					Integer subPoints = toPrice(multiply(divide(subOrder.getSubUsedPoint(),totalUsedPoint,6),points)).intValue();
					subOrder.setPoints(subPoints);
					preTotalPoints+=subPoints;
					if(minSubOrder.getSubUsedPoint()>subOrder.getSubUsedPoint()){
						minSubOrder = subOrder;
					}
				}
			}
			if(preTotalPoints-points!=0){
				minSubOrder.setPoints(minSubOrder.getPoints()+(points-preTotalPoints));
			}
			for(PreOrderDto subOrder:orderInitDto.getPreSubOrderList()){
				if(subOrder.getPoints()!=null && subOrder.getPoints()>0){
					Integer subPoints =subOrder.getPoints();
					OrderItem maxOrderItem = subOrder.getOrderItemList().get(0);
					Double sum_PointAmount = ZERO;
					Double pointAmount =toPrice(divide(subPoints,100,6));
					for(OrderItem orderItem:subOrder.getOrderItemList()){
						Double rate = divide(orderItem.getSubTotal(), subOrder.getTotal(), 6).doubleValue();
						Double subPointAmount = toPrice(multiply(pointAmount,rate));
						sum_PointAmount = toPrice(add(sum_PointAmount,subPointAmount));
						orderItem.setSubTotal(toPrice(subtract(orderItem.getSubTotal(),subPointAmount)));
						orderItem.setPoints(multiply(subPointAmount,100).intValue());
						subPoints = subPoints+orderItem.getPoints();
						if(maxOrderItem.getSubTotal().compareTo(orderItem.getSubTotal())<0){
							maxOrderItem = orderItem;
						}
						pointOrderItemMap.put(orderItem, subPointAmount);
					}
					if(!sum_PointAmount.equals(pointAmount)){
						Double remain = toPrice(subtract(pointAmount,sum_PointAmount));
						maxOrderItem.setPoints(toPrice(multiply(add(pointOrderItemMap.get(maxOrderItem),remain),100)).intValue());
						maxOrderItem.setSubTotal(toPrice(subtract(maxOrderItem.getSubTotal(),remain)));
						pointOrderItemMap.put(maxOrderItem,toPrice(add(pointOrderItemMap.get(maxOrderItem),remain)));
					}
					for(OrderItem orderItem:subOrder.getOrderItemList()){
						operateOrderItemByPoint(orderItem);
					}
				}
			}
			List<OrderPoint> orderPointList = new ArrayList<OrderPoint>();
			pointOrderItemMap.forEach(new BiConsumer<OrderItem,Double>(){
				public void accept(OrderItem t, Double u) {
					OrderPoint orderPoint = new OrderPoint();
					orderPoint.setOrderItemId(t.getId());
					orderPoint.setOrderId(t.getOrderId());
					orderPoint.setOrderCode(t.getOrderCode());
					orderPoint.setParentOrderId(t.getParentOrderId());
					orderPoint.setParentOrderCode(t.getParentOrderCode());
					orderPoint.setCreateUser(Constant.AUTHOR_TYPE.MEMBER+orderInitDto.getMemberAccount());
					orderPoint.setPoint(multiply(u,100).intValue());
					orderPoint.setRefundedPoint(0);
					orderPoint.setChannelCode(orderInitDto.getChannelCode());
					orderPoint.setPointType(orderInitDto.getPointType());
					orderPoint.setPointPackageId(null);
					t.getOrderPointList().add(orderPoint);
					orderPointList.add(orderPoint);
				}
			});
			orderInitDto.setOrderPointList(orderPointList);
		}
		return orderInitDto;
	}

	/**
	 * 初始化积分
	 * @param shoppingCartDto
	 * @return
	 */
	public OrderInitDto initPoint(OrderInitDto orderInitDto){
		String canUseXgMomney="";
		PointMember pointMember = pointMemberService.queryPointMemberByMemberId(orderInitDto.getMemberId());
		
		//第三方商城积分接入--start
		String chanelCode=orderInitDto.getChannelCode();
		ResultInfo<ChannelInfo> chanelInfo=channelInfoProxy.getChannelInfoByCode(chanelCode);
		if(chanelInfo.getData()!=null && "1".equals(chanelInfo.getData().getIsUsedPoint())){//是否使用自己商城的积分
			String mobile=memberInfoService.getLoginInfoByMemId(orderInitDto.getMemberId()).getMobile();
			String openId=memberInfoService.getByMobile(mobile).getTpin();
			Double  totalPoint=memberInfoService.getThirdShopPoint(openId, chanelCode);
			orderInitDto.setTotalPoint((int) Math.round(totalPoint==null?0:totalPoint));
			orderInitDto.setPointType(chanelCode);
			orderInitDto.setUsedPoint(0);
		}else{//默认使用西客币
			if(null!=pointMember && pointMember.getTotalPoint()>0){
				orderInitDto.setTotalPoint(pointMember.getTotalPoint());
				orderInitDto.setUsedPoint(0);
			}
		}
		
		//第三方商城积分接入--end
		
		 
		Double surplusPoint = toPrice(divide(orderInitDto.getTotalPoint(),100,6));
		for(PreOrderDto subOrder:orderInitDto.getPreSubOrderList()){
			Boolean usedPointSign = !(subOrder.getPutSign()!=null && subOrder.getPutSign()>0 && subOrder.getTotal()<=MIN_AMOUNT);
			//usedPointSign = usedPointSign && !OrderConstant.OrderType.COMMON_SEA.code.equals(subOrder.getType());
			subOrder.setUsedPointSign(usedPointSign);
			
			if(usedPointSign){
				if(OrderUtils.isSeaOrder(subOrder.getType()) 
				  && subOrder.getTotal()>MIN_AMOUNT){
					Double tempTotal = subtract(subOrder.getTotal(),MIN_AMOUNT).doubleValue();
					if(surplusPoint<=tempTotal){
						surplusPoint=Constant.ZERO;
					}else{
						surplusPoint = subtract(surplusPoint,tempTotal).doubleValue();
					}
				}else{
					if(surplusPoint>subOrder.getTotal()){
						surplusPoint=subtract(surplusPoint,subOrder.getTotal()).doubleValue();
					}else{
						surplusPoint = Constant.ZERO;
					}
				}
			}
			for(SimpleFullDiscountDTO dataKey : subOrder.getFullDiscountMap().keySet())    
			{    
				List<OrderItem>   orderItems=subOrder.getFullDiscountMap().get(dataKey);
				for(OrderItem OrderItem:orderItems){
					Long topicId  =OrderItem.getTopicId();
					Topic topic= topicProxy.queryById(topicId).getData();
					if(TopicConstant.CAN_NOT_USE_XG_MONEY.equals(topic.getCanUseXgMoney())){
						canUseXgMomney=TopicConstant.CAN_NOT_USE_XG_MONEY;
					}
				}
			} 

		}
		Double points = subtract(divide(orderInitDto.getTotalPoint(),100,6),surplusPoint).doubleValue();
		if(orderInitDto.getTotalPoint()>=multiply(points,100).intValue()){
			orderInitDto.setUsedPoint(multiply(points,100).intValue());
		}
 		if((orderInitDto.getTotalPoint()==null || (orderInitDto.getTotalPoint()!=null && orderInitDto.getTotalPoint()<=0))
		|| (orderInitDto.getUsedPoint()==null || (orderInitDto.getUsedPoint()!=null && orderInitDto.getUsedPoint()<=0))){
			orderInitDto.setUsedPointSign(Boolean.FALSE);
			orderInitDto.setUsedPoint(0);
		}
		if(TopicConstant.CAN_NOT_USE_XG_MONEY.equals(canUseXgMomney)){//如果有一个专场不允许使用西客币，则可以使用的西客币为0
			orderInitDto.setUsedPoint(0);
		}
		return orderInitDto;
	}
	
	private void operateOrderItemByPoint(OrderItem orderItem){
		Double subTotal = orderItem.getSubTotal();
		Double itemAmount = orderItem.getItemAmount();
		Double freight = orderItem.getFreight();
		Double taxFee = orderItem.getTaxFee();
		Double oldSubTotal = toPrice(add(add(itemAmount,freight),taxFee));
		Double rate = divide(itemAmount,oldSubTotal,6).doubleValue();
		orderItem.setItemAmount(toPrice(multiply(subTotal,rate)));
		rate = divide(freight,oldSubTotal,6).doubleValue();
		orderItem.setFreight(toPrice(multiply(subTotal,rate)));
		rate = divide(taxFee,oldSubTotal,6).doubleValue();
		orderItem.setTaxFee(toPrice(multiply(subTotal,rate)));
		oldSubTotal = toPrice(add(add(orderItem.getItemAmount(),orderItem.getFreight()),orderItem.getTaxFee()));
		if(!subTotal.equals(oldSubTotal)){
			Double remain = toPrice(subtract(subTotal,oldSubTotal));
			orderItem.setSubTotal(toPrice(subtract(orderItem.getSubTotal(),remain)));
		}
	}
}
