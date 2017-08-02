package com.tp.proxy.ord;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.util.ExceptionUtils;
import com.tp.common.vo.Constant;
import com.tp.common.vo.mmp.PointConstant;
import com.tp.dto.common.FailInfo;
import com.tp.dto.mmp.enums.CouponUserStatus;
import com.tp.dto.ord.OrderDto;
import com.tp.dto.ord.OrderInitDto;
import com.tp.dto.ord.PreOrderDto;
import com.tp.dto.ord.ShoppingCartDto;
import com.tp.model.mmp.PointDetail;
import com.tp.model.ord.OrderItem;
import com.tp.proxy.ord.split.IOrderAmountSplitProxy;
import com.tp.service.mmp.ICouponUserService;
import com.tp.service.mmp.IPointDetailService;
import com.tp.service.ord.IOrderChannelTrackService;
import com.tp.service.ord.IOrderInfoService;
import com.tp.util.ThreadUtil;

/**
 * 下单成功后后续处理
 * @author szy
 *
 */
@Service
public class CreateOrderAfterProxy {

	private final static Logger logger = LoggerFactory.getLogger(CreateOrderAfterProxy.class);
	
	@Autowired
	private ICouponUserService couponUserService;
	@Autowired
	private IPointDetailService pointDetailService;
	@Autowired
	private IOrderInfoService orderInfoService;
	@Autowired
	private CartProxy cartProxy;
	@Autowired
	private CartItemInfoProxy cartItemInfoProxy;
	@Autowired
	private IOrderAmountSplitProxy orderAmountSplitProxy;
	@Autowired
	private IOrderAmountSplitProxy orderCouponSplitProxy;
	@Autowired
	private IOrderAmountSplitProxy orderFreightSplitProxy;
	@Autowired
	private IOrderChannelTrackService orderChannelTrackService;
	
	
	public void excute(final OrderDto orderDto,final OrderInitDto orderInitDto){
		List<Long> couponIds = orderInitDto.getCouponIds();
		excuteCouponUsed(couponIds,2);
		excutePointUsed(orderDto,orderInitDto,2);
		excuteCartClean(orderInitDto);
		pushYiQiFaOrder(orderDto);
	}
	
	/**
	 * 更新优惠券的使用状态
	 * @param couponIds
	 * @param subOrderCodeList
	 */
	public void excuteCouponUsed(final List<Long> couponIds,Integer interval){
		Runnable runnable = new Runnable(){
			@Override
			public void run() {
				try {
					couponUserService.updateCouponUserStatus(couponIds, CouponUserStatus.USED.ordinal());
				} catch (Throwable throwable) {
					ExceptionUtils.print(new FailInfo(throwable), logger,couponIds,CouponUserStatus.USED.ordinal());
					if(interval<=128){
						try {
							TimeUnit.MINUTES.sleep(interval);
						} catch (InterruptedException e) {
						}
						excuteCouponUsed(couponIds,interval*2);
					}
				}
			}
			
		};
		ThreadUtil.excAsync(runnable,false);
	}
	
	/**
	 * 更新积分的使用状态
	 * @param couponIds
	 * @param subOrderCodeList
	 */
	public void excutePointUsed(final OrderDto orderDto,final OrderInitDto orderInitDto,Integer interval){
		if(orderInitDto.getUsedPointSign()){
			PointDetail pointDetail = new PointDetail();
			pointDetail.setBizId(""+orderDto.getOrderInfo().getParentOrderCode());
			pointDetail.setBizType(PointConstant.BIZ_TYPE.ORDER.code);
			pointDetail.setCreateUser(orderInitDto.getMemberAccount());
			pointDetail.setPoint(orderInitDto.getUsedPoint());
			pointDetail.setMemberId(orderInitDto.getMemberId());
			pointDetail.setTitle(PointConstant.BIZ_TYPE.ORDER.title);
			pointDetail.setPointType(PointConstant.OPERATE_TYPE.MINUS.type);
			pointDetail.setChannelCode(orderInitDto.getChannelCode());
			Runnable runnable = new Runnable(){
				@Override
				public void run() {
					try {
						pointDetailService.updatePointByMemberUsed(pointDetail);
					} catch (Throwable throwable) {
						ExceptionUtils.print(new FailInfo(throwable), logger,pointDetail);
						if(interval<=128){
							try {
								TimeUnit.MINUTES.sleep(interval);
							} catch (InterruptedException e) {
							}
							excutePointUsed(orderDto,orderInitDto,interval*2);
						}
					}
				}
				
			};
			ThreadUtil.excAsync(runnable,false);
		}
	}
	
	/**
	 * 清除购物车下单数据
	 * @param token
	 * @param orderItemList
	 */
	public void excuteCartClean(final OrderInitDto orderInitDto){
		Runnable runnable = new Runnable(){
			@Override
			public void run() {
				try{
					ShoppingCartDto shoppingCartDto = cartProxy.getCartDto(orderInitDto.getMemberId(), orderInitDto.getToken(),orderInitDto.getShopId());
					cartProxy.deleteCacheCart(orderInitDto.getMemberId(), orderInitDto.getToken(),orderInitDto.getShopId());
					List<PreOrderDto> subOrderList = shoppingCartDto.getPreSubOrderList();
					if(CollectionUtils.isNotEmpty(subOrderList) && Constant.TF.NO.equals(shoppingCartDto.getBuyNow())){
						Map<String,Object> params = new HashMap<String,Object>();
						for(PreOrderDto subOrder:subOrderList){
							for(OrderItem orderItem:subOrder.getOrderItemList()){
								params.put("memberId", shoppingCartDto.getMemberId());
								params.put("skuCode",orderItem.getSkuCode());
								params.put("topicId",orderItem.getTopicId());
								cartItemInfoProxy.deleteByParam(params);
							}
						}
					}
				}catch(Throwable throwable){
					ExceptionUtils.print(new FailInfo(throwable), logger,orderInitDto.getMemberId(), orderInitDto.getToken(),orderInitDto);
				}
			}
		};
		ThreadUtil.excAsync(runnable,false);
	}

	/**
	 * 拆分订单金额(分摊)
	 */
	public OrderDto splitAmount(final OrderDto orderDto,final OrderInitDto orderInitDto){
		return orderAmountSplitProxy.splitAmount(orderDto, orderInitDto);
	}
	
	/**
	 * 拆分优惠券金额(分摊)
	 */
	public OrderDto splitCouponAmount(final OrderDto orderDto,final OrderInitDto orderInitDto){
		return orderCouponSplitProxy.splitAmount(orderDto, orderInitDto);
	}
	
	public OrderDto splitFreightAmount(final OrderDto orderDto,final OrderInitDto orderInitDto){
		return orderFreightSplitProxy.splitAmount(orderDto, orderInitDto);
	}
	
	public void updateOrderDto(OrderDto orderDto,Integer interval){
		Runnable runnable = new Runnable(){
			@Override
			public void run() {
				try{
					orderInfoService.updateOrderSplitAmount(orderDto.getSubOrderList());
				}catch(Throwable throwable){
					ExceptionUtils.print(new FailInfo(throwable), logger,orderDto);
					if(interval<=128){
						try {
							TimeUnit.MINUTES.sleep(interval);
						} catch (InterruptedException e) {
						}
						updateOrderDto(orderDto,interval*2);
					}
				}
			}
		};
		ThreadUtil.excAsync(runnable,false);
	}
	
	private void pushYiQiFaOrder(OrderDto orderDto){
		Runnable runnable = new Runnable(){
			@Override
			public void run() {
				orderChannelTrackService.pushOrderByChannelYiQiFaParentOrderCode(orderDto.getOrderInfo().getParentOrderCode());
			}
		};
		ThreadUtil.excAsync(runnable,false);
	}
}
