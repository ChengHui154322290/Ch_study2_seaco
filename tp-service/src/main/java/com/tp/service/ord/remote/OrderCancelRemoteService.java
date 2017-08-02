package com.tp.service.ord.remote;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tp.common.util.ExceptionUtils;
import com.tp.common.util.OrderUtils;
import com.tp.common.vo.OrderConstant;
import com.tp.common.vo.PaymentConstant;
import com.tp.common.vo.Constant.SPLIT_SIGN;
import com.tp.common.vo.DAOConstant.MYBATIS_SPECIAL_STRING;
import com.tp.common.vo.OrderConstant.ClearanceStatus;
import com.tp.common.vo.OrderConstant.ORDER_STATUS;
import com.tp.common.vo.OrderConstant.OrderType;
import com.tp.common.vo.mmp.PointConstant;
import com.tp.common.vo.ord.OrderErrorCodes;
import com.tp.common.vo.ord.RefundConstant;
import com.tp.common.vo.ord.LogTypeConstant.LOG_TYPE;
import com.tp.common.vo.ord.TopicLimitItemConstant.TopicLimitType;
import com.tp.dao.ord.OrderConsigneeDao;
import com.tp.dao.ord.OrderInfoDao;
import com.tp.dao.ord.OrderPointDao;
import com.tp.dao.ord.SubOrderDao;
import com.tp.dto.common.FailInfo;
import com.tp.dto.common.ResultInfo;
import com.tp.dto.mmp.enums.CouponType;
import com.tp.dto.mmp.enums.CouponUserStatus;
import com.tp.dto.ord.HhbShopOrderInfoDTO;
import com.tp.model.dss.ChannelInfo;
import com.tp.model.mmp.PointDetail;
import com.tp.model.ord.OrderInfo;
import com.tp.model.ord.OrderItem;
import com.tp.model.ord.OrderPoint;
import com.tp.model.ord.OrderPromotion;
import com.tp.model.ord.OrderStatusLog;
import com.tp.model.ord.RefundInfo;
import com.tp.model.ord.SubOrder;
import com.tp.model.ord.TopicLimitItem;
import com.tp.model.pay.PaymentInfo;
import com.tp.redis.util.JedisCacheUtil;
import com.tp.service.dss.IChannelInfoService;
import com.tp.service.mem.IMemberInfoService;
import com.tp.service.mmp.ICouponUserService;
import com.tp.service.mmp.IPointDetailService;
import com.tp.service.ord.ICancelInfoService;
import com.tp.service.ord.IOffsetInfoService;
import com.tp.service.ord.IOrderItemService;
import com.tp.service.ord.IOrderPromotionService;
import com.tp.service.ord.IOrderRedeemItemService;
import com.tp.service.ord.IOrderStatusLogService;
import com.tp.service.ord.IRefundInfoService;
import com.tp.service.ord.ITopicLimitItemService;
import com.tp.service.ord.remote.IOrderCancelRemoteService;
import com.tp.service.pay.IPaymentInfoService;
import com.tp.service.pay.IPaymentService;
import com.tp.service.stg.IInventoryOperService;
import com.tp.util.StringUtil;

/**
 * 订单取消服务
 * 
 * @author szy
 * @version 0.0.1
 */
@Service
public class OrderCancelRemoteService implements IOrderCancelRemoteService {

	private static final Logger log = LoggerFactory.getLogger(OrderCancelRemoteService.class);

	private static final String CACHE_KEY_ORDER_CANCEL_PREFIX = "order:cancel:";
	private static final int SEA_CANCEL_LIMIT = 30;	// 海淘单付款30分钟后不允许取消
	
	@Autowired
	private IPaymentService paymentService;
	@Autowired
	private IPaymentInfoService paymentInfoService;
	@Autowired
	private IOrderItemService orderItemService;
	@Autowired
	private ICouponUserService couponUserService;
	@Autowired
	private IPointDetailService pointDetailService;
	@Autowired
	private IOrderPromotionService orderPromotionService;
	@Autowired
	private IInventoryOperService inventoryOperService;
	@Autowired
	private ICancelInfoService cancelInfoService;
	@Autowired
	private IRefundInfoService refundInfoService;
	@Autowired
	private SubOrderDao  subOrderDao;
	@Autowired
	private OrderInfoDao  orderInfoDao;
	@Autowired
	private OrderPointDao orderPointDao;
	@Autowired
	private IOrderStatusLogService orderStatusLogService;
	@Autowired
	private JedisCacheUtil jedisCacheUtil;
	@Autowired
	private ITopicLimitItemService  topicLimitItemService;
	@Autowired
	private OrderConsigneeDao orderConsigneeDao;
	@Autowired
	private IOffsetInfoService  offsetInfoService;
	@Autowired
	IOrderRedeemItemService  orderRedeemItemService;
	@Autowired
	private IChannelInfoService channelInfoService;
	@Autowired
	private IMemberInfoService memberInfoService;
	
	
	@Override
	@Transactional
	public void cancelOrder(Long orderCode, Long userId, String userName) {
		cancelOrder(orderCode, userId, userName, LOG_TYPE.MEMBER, CANCEL_TYPE.MEMBER_NOPAY,null);
	}

	@Override
	@Transactional
	public void cancelOrderByJob(Long orderCode, Long userId, String userName) {
		cancelOrder(orderCode, userId, userName, LOG_TYPE.SYSTEM, CANCEL_TYPE.OUT_TIME,"超时系统自动取消订单");
	}
	
	
	/**
	 * 兑换码失效自动取消订单
	 * @param orderCode
	 * @param userId
	 * @param userName
	 */
	public void cancelOrderRedeemByJob(Long orderCode, Long userId, String userName){
		cancelOrder(orderCode, userId, userName, LOG_TYPE.SYSTEM, CANCEL_TYPE.OUT_TIME,"兑换码超过有效期，系统自动取消订单");
		orderRedeemItemService.cancleOverDueRedeemInfo(orderCode);//设置兑换码状态
	}
	@Override
	@Transactional
	public void cancelOrderByPaymented(Long subOrderCode, Long userId, String userName) {
		cancelOrder(subOrderCode, userId,userName, LOG_TYPE.MEMBER, CANCEL_TYPE.MEMBER_PAYED,null);
	}
	
	@Override
	@Transactional
	public void cancelOrderByBackend(Long orderCode, Long userId, String userName,String cancelReason) {
		cancelOrder(orderCode, userId, userName, LOG_TYPE.USER,CANCEL_TYPE.USER,cancelReason);
	}

	
	public ResultInfo<Boolean> cancelOrder(Long orderCode,Long userId,String userName,LOG_TYPE userType,CANCEL_TYPE operateType,String cancelReason){
		final String lockKey = CACHE_KEY_ORDER_CANCEL_PREFIX + orderCode;
		OrderInfo orderInfo = orderInfoDao.selectOneByCode(orderCode);
		List<SubOrder> subOrderList = new ArrayList<SubOrder>();
		SubOrder subOrder = subOrderDao.selectOneByCode(orderCode);
		Long parentOrderCode = null;
		String operateIp = null,consigneeMobile=null,channelCode=null;
		final List<Long> orderCodeList = new ArrayList<Long>();
		if(orderInfo==null){
			if(subOrder!=null){
				Integer orderStatus = subOrder.getOrderStatus();
				if(OrderConstant.ORDER_STATUS.DELIVERY.code.equals(orderStatus)){
					operateType = CANCEL_TYPE.MEMBER_PAYED;
				}
				parentOrderCode = subOrder.getParentOrderCode();
				if(OrderConstant.OrderType.BUY_COUPONS.code==subOrder.getType()){//
					consigneeMobile = orderInfoDao.selectOneByCode(parentOrderCode).getReceiveTel();
				}else{
					consigneeMobile = orderConsigneeDao.selectOneByParentOrderCode(parentOrderCode).getMobile();
					
				}
					orderInfo =  orderInfoDao.selectOneByCode(parentOrderCode);
				subOrderList.add(subOrder);
				channelCode = subOrder.getChannelCode();
			}
		}else{
			operateIp = orderInfo.getIp();
			parentOrderCode = orderInfo.getParentOrderCode();
			consigneeMobile = orderConsigneeDao.selectOneByParentOrderCode(parentOrderCode).getMobile();
			Map<String,Object> params = new HashMap<String,Object>();
			params.put("parentOrderCode", orderCode);
			params.put("orderStatus", OrderConstant.ORDER_STATUS.PAYMENT.code);
			subOrderList = subOrderDao.queryByParam(params);
			channelCode = subOrderList.get(0).getChannelCode();
		}
		FailInfo failInfo = validate(orderCode,userId,userName,userType,orderInfo,subOrder,operateType);
		if(failInfo!=null){
			return new ResultInfo<Boolean>(failInfo);
		}
		subOrderList.forEach(new Consumer<SubOrder>(){
			public void accept(SubOrder subOrder){
				orderCodeList.add(subOrder.getOrderCode());
			}
		});
		
		//佣金累计
		Double  totalReturnMoney=0D;
		for( SubOrder  subOrderInfo:subOrderList){
            List<OrderItem> orderItemList = orderItemService.selectListBySubId(subOrderInfo.getId());
            Double subReturnMoney=0d;
            for( OrderItem  orderItem:orderItemList){
            	Double  subreturnMoney=orderItem.getSalesPrice()*orderItem.getCommisionRate();
				subReturnMoney=subReturnMoney+subreturnMoney;
            }
            subOrder.setReturnMoney(subReturnMoney);
            totalReturnMoney=totalReturnMoney+subOrder.getReturnMoney();
		}
		orderInfo.setReturnMoney(totalReturnMoney);
		
		
		if(jedisCacheUtil.lock(lockKey)){
			//1.返回库存
			failInfo = rollbackInventory(orderCodeList);
			//2.修改状态
			//3.记录日志
			//4.取消父订单
			cancelSubOrder(subOrderList,userId,userName,userType,cancelReason);
			//5.取消支付单
			cancelPaymentInfo(orderCode,operateIp,userId,subOrder!=null && subOrder.getPayTime()!=null);
			//6.返回优惠券
			rollbackCoupon(parentOrderCode,CANCEL_TYPE.MEMBER_PAYED.equals(operateType)||CANCEL_TYPE.USER.equals(operateType) );
			//第三方商城积分接入--start
			Map<String, Object> channeParams = new HashMap<>();
			channeParams.put("channelCode", channelCode);
			ChannelInfo channelInfo = channelInfoService.queryUniqueByParams(channeParams);
			if(channelInfo!=null && "1".equals(channelInfo.getIsUsedPoint())){//是否使用自己商城的积分
				HhbShopOrderInfoDTO  hhbOrderInfo=new HhbShopOrderInfoDTO();
				String userMobile=memberInfoService.getLoginInfoByMemId(orderInfo.getMemberId()).getMobile();
				String openId=memberInfoService.getByMobile(userMobile).getTpin();
				hhbOrderInfo.setOpenId(openId);
				hhbOrderInfo.setCode(String.valueOf(orderCode));//订单编号
				if(subOrder!=null){
						hhbOrderInfo.setCash(subOrder.getTotal());//应付金额
						hhbOrderInfo.setReturnMoney(subOrder.getReturnMoney());//返回佣金
						hhbOrderInfo.setTotalMoney(subOrder.getOriginalTotal());
						hhbOrderInfo.setBalance(Double.valueOf(subOrder.getPoints()));//退还积分数
				}else{
					hhbOrderInfo.setCash(orderInfo.getTotal());//应付金额
					hhbOrderInfo.setReturnMoney(orderInfo.getReturnMoney());//返回佣金
					hhbOrderInfo.setTotalMoney(orderInfo.getOriginalTotal());
					hhbOrderInfo.setBalance(Double.valueOf(orderInfo.getTotalPoint()));//退还积分数
				}
				
				hhbOrderInfo.setIntegral(0D);//使用积分数
				hhbOrderInfo.setType("0");//退单
				memberInfoService.sendOrderToThirdShop(orderInfo.getChannelCode(), hhbOrderInfo);//发送订单到第三方商城
			}else{
				//返还积分
				rollbackPoint(subOrderList,parentOrderCode,subOrder.getMemberId(),userName);
			}
			
			//7.生成取消单，生成退款单
			createRufendInfo(subOrder,userName);
			//8.返回限购
			topicLimitReleaseBySubOrder(orderCode,consigneeMobile);
		}else{
			return new ResultInfo<Boolean>(new FailInfo("订单取消中，请稍后查看取消结果。"));
		}
		jedisCacheUtil.unLock(lockKey);
		return new ResultInfo<Boolean>(Boolean.TRUE);
	}
	
	
	/**
	 * 回滚库存
	 * @param orderCodeList
	 * @return
	 */
	private FailInfo rollbackInventory(List<Long> orderCodeList){
		/* 回滚库存 */
		ResultInfo<String> invenResult = inventoryOperService.batchUnoccupyInventory(orderCodeList);
		return invenResult.msg;
	}
	
	/**
	 * 修改订单状态，记录日志
	 * @param subOrderList
	 * @param userId
	 * @param userName
	 * @param userType
	 */
	private void cancelSubOrder(List<SubOrder> subOrderList,Long userId,String userName,LOG_TYPE userType,String cancelReason){
		if(CollectionUtils.isNotEmpty(subOrderList)){
			List<OrderStatusLog> orderStatusLogList = new ArrayList<OrderStatusLog>();
			List<Long> orderCodeList = new ArrayList<Long>();
			for(SubOrder subOrder:subOrderList){
				OrderStatusLog orderStatusLog = new OrderStatusLog();
				orderStatusLog.setParentOrderCode(subOrder.getParentOrderCode());
				orderStatusLog.setOrderCode(subOrder.getOrderCode());
				orderStatusLog.setPreStatus(subOrder.getOrderStatus());
				orderStatusLog.setCurrStatus(OrderConstant.ORDER_STATUS.CANCEL.code);
				orderStatusLog.setContent(userType+"取消订单 原因："+cancelReason);
				orderStatusLog.setCreateTime(new Date());
				orderStatusLog.setCreateUserId(userId);
				orderStatusLog.setCreateUserName(userName);
				orderStatusLog.setCreateUserType(userType.code);
				orderStatusLogList.add(orderStatusLog);
				orderCodeList.add(subOrder.getOrderCode());
			}
			Long parentOrderCode = subOrderList.get(0).getParentOrderCode();
			Map<String,Object> params = new HashMap<String,Object>();
			params.put("parentOrderCode", parentOrderCode);
			params.put(MYBATIS_SPECIAL_STRING.COLUMNS.name(), " order_status !=0");
			Integer noCancelCount = subOrderDao.queryByParamCount(params);
			Boolean isCancelParentOrder = subOrderList.size()==noCancelCount;
			if(isCancelParentOrder){
				OrderStatusLog orderStatusLog = new OrderStatusLog();
				orderStatusLog.setParentOrderCode(parentOrderCode);
				orderStatusLog.setOrderCode(null);
				orderStatusLog.setPreStatus(null);
				orderStatusLog.setCurrStatus(OrderConstant.ORDER_STATUS.CANCEL.code);
				orderStatusLog.setCreateTime(new Date());
				orderStatusLog.setCreateUserId(userId);
				orderStatusLog.setCreateUserName(userName);
				orderStatusLog.setCreateUserType(userType.code);
				orderStatusLogList.add(orderStatusLog);
			}
			
			subOrderDao.updateSubOrderListByCancel(orderCodeList, OrderConstant.ORDER_STATUS.CANCEL.code, OrderConstant.ORDER_STATUS.RECEIPT.code,cancelReason);
			orderStatusLogService.batchInsert(orderStatusLogList);
			if(isCancelParentOrder){
				OrderInfo salesOrderForUpdate = new OrderInfo();
				salesOrderForUpdate.setParentOrderCode(parentOrderCode);
				salesOrderForUpdate.setOrderStatus(OrderConstant.ORDER_STATUS.CANCEL.code);
				salesOrderForUpdate.setUpdateTime(new Date());
				orderInfoDao.updateSalesOrderStatusAfterCancel(salesOrderForUpdate);
			}
		}
	}
	/**
	 * 取消支付单
	 * @param orderCode
	 * @param ip
	 * @param userId
	 */
	private void cancelPaymentInfo(Long orderCode,String ip,Long userId,Boolean isPayed){
		if(!isPayed){
			int payResult = paymentService.updateCancelPaymentInfo(orderCode, null, ip, userId.toString());
			if(payResult==0){
				log.error("根据订单编号 {} 取消支付单信息失败",orderCode);
			}
		}
	}

	/**
	 * 返回优惠券
	 */
	private void rollbackCoupon(Long parentOrderCode,Boolean isPayed){
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("parentOrderCode", parentOrderCode);
		params.put(MYBATIS_SPECIAL_STRING.COLUMNS.name(), " order_status=0");
		List<SubOrder> subOrderList = subOrderDao.queryByParam(params);
		List<Long> orderCodeList = new ArrayList<Long>();
		subOrderList.forEach(new Consumer<SubOrder>(){
			public void accept(SubOrder t) {
				if(isPayed){
					orderCodeList.add(t.getOrderCode());
				}else if(!isPayed && t.getPayTime()==null){
					orderCodeList.add(t.getOrderCode());
				}
			}
		});
		params.put(MYBATIS_SPECIAL_STRING.COLUMNS.name(), " coupon_type !="+CouponType.FIRST_MINUS.ordinal());
		List<OrderPromotion> orderPromotionList = orderPromotionService.queryByParam(params);
		List<Long> offsetOrderCodeList = new ArrayList<Long>();
		if(CollectionUtils.isNotEmpty(orderPromotionList)){
			Map<String,List<OrderPromotion>> couponCountMap = new HashMap<String,List<OrderPromotion>>();
			Map<String,List<OrderPromotion>> orderCouponCountMap = new HashMap<String,List<OrderPromotion>>();
			for (OrderPromotion orderPromotion : orderPromotionList) {
				String couponCode = orderPromotion.getCouponCode();
				offsetOrderCodeList.add(orderPromotion.getOrderCode());
				List<OrderPromotion> opList = couponCountMap.get(couponCode);
				if(opList==null){
					opList = new ArrayList<OrderPromotion>();
				}
				opList.add(orderPromotion);
				couponCountMap.put(couponCode, opList);
				List<OrderPromotion> oopList = orderCouponCountMap.get(couponCode);
				if(oopList==null){
					oopList = new ArrayList<OrderPromotion>();
				}
				for(Long orderCode:orderCodeList){
					if(orderPromotion.getOrderCode().equals(orderCode)){
						oopList.add(orderPromotion);
					}
				}
				orderCouponCountMap.put(couponCode, oopList);
			}
			params.clear();
			params.put(MYBATIS_SPECIAL_STRING.COLUMNS.name(), " order_code in ("+StringUtil.join(offsetOrderCodeList, SPLIT_SIGN.COMMA)+")");
			Integer count = offsetInfoService.queryByParamCount(params);
			if(count>0){
				log.info("订单 {} 是否支付 {}  中优惠券已有补偿，不能判断是否已走补偿，不能自动返回卡券，",parentOrderCode,isPayed);
				return;
			}
			final Set<Long> couponCodeList = new HashSet<Long>();
			orderCouponCountMap.forEach(new BiConsumer<String,List<OrderPromotion>>(){
				public void accept(String t, List<OrderPromotion> u) {
					List<OrderPromotion> opList = couponCountMap.get(t);
					if(u.size()==opList.size() && u.size()>0 && (!isPayed || (isPayed && CouponType.NO_CONDITION.ordinal()==u.get(0).getCouponType()))){
						couponCodeList.add(Long.valueOf(t));
					}
				}
			});
			if(CollectionUtils.isNotEmpty(couponCodeList)){
				try{
					boolean couponResult = couponUserService.updateCouponUserStatus(new ArrayList<Long>(couponCodeList), CouponUserStatus.NORMAL.ordinal());	// 退优惠券
					if(!couponResult){
						ExceptionUtils.print(new FailInfo("返还优惠券不成功"), log, couponCodeList,orderCodeList);
					}
				}catch(Throwable throwable){
					ExceptionUtils.print(new FailInfo(throwable), log, couponCodeList,orderCodeList);
				}
			}
		}
	}

	/**
	 * 返还积分
	 * @param parentOrderCode
	 */
	private void rollbackPoint(List<SubOrder> subOrderList,Long parentOrderCode,Long memberId,String userName){
		List<Long> orderIdList = new ArrayList<Long>();
		Integer usedPoints = 0;
		for(SubOrder subOrder:subOrderList){
			if(subOrder.getPoints()!=null && subOrder.getPoints()>0){
				double   rate=orderRedeemItemService.getUnusedRateByOrderCode(subOrder.getOrderCode());
				orderIdList.add(subOrder.getId());
				if(OrderConstant.OrderType.BUY_COUPONS.code.equals(subOrder.getType())){//退还未使用的
					usedPoints=(int) (usedPoints+Double.valueOf(subOrder.getPoints())*rate);//未使用的兑换码比例
				}else{
					usedPoints+=subOrder.getPoints();
				}
				
			}
			
		}
		if(CollectionUtils.isEmpty(orderIdList)){
			return;
		}
		Map<String,Object> params = new HashMap<String,Object>();
		params.put(MYBATIS_SPECIAL_STRING.COLUMNS.name(), "order_id in ("+StringUtil.join(orderIdList, SPLIT_SIGN.COMMA)+")");
		List<OrderPoint> orderPointList = orderPointDao.queryByParam(params);
		if(CollectionUtils.isEmpty(orderPointList)){
			return;
		}
		Integer totalPoint = 0;
		Integer refundPoint =0;
		for(OrderPoint orderPoint:orderPointList){
			totalPoint+=orderPoint.getPoint();
			refundPoint+=orderPoint.getRefundedPoint();
		}
		if(totalPoint<=refundPoint){
			return;
		}
		params.clear();
		params.put("bizType", PointConstant.BIZ_TYPE.ORDER.code);
		params.put("memberId", memberId);
		params.put("bizId", parentOrderCode);
		Integer count = pointDetailService.queryByParamCount(params);
		if(count>0){
			PointDetail pointDetail = new PointDetail();
			pointDetail.setTitle(PointConstant.BIZ_TYPE.CANCEL.title);
			
			pointDetail.setPoint(usedPoints);
			pointDetail.setBizId(Long.toString(parentOrderCode));
			pointDetail.setRelateBizType(PointConstant.BIZ_TYPE.ORDER.code);
			pointDetail.setBizType(PointConstant.BIZ_TYPE.CANCEL.code);
			pointDetail.setMemberId(memberId);
			pointDetail.setPointType(PointConstant.OPERATE_TYPE.ADD.type);
			pointDetail.setCreateUser(userName);
			pointDetailService.updatePointByRefund(pointDetail);
		}
		
		for(OrderPoint orderPoint:orderPointList){
			orderPoint.setRefundedPoint(orderPoint.getPoint());
			orderPointDao.updateNotNullById(orderPoint);
		}
	}
	/**
	 * 生成取消单
	 * @param subOrderList
	 * @param userName
	 */
	private void createRufendInfo(SubOrder subOrder,String userName){
		if(subOrder!=null && subOrder.getPayTime()!=null){
			Long refundCode = insertRefundInfoDO(subOrder, userName);	// 退款单
			List<OrderItem> orderItemList = orderItemService.selectListBySubId(subOrder.getId());
			cancelInfoService.addCancelInfo(subOrder, orderItemList, refundCode);	// 取消单
		}
	}
	/**
	 *  限购商品释放
	 * @param orderCode
	 * @param mobile
	 */
	private void topicLimitReleaseBySubOrder(Long orderCode,String mobile) {
		Map<String,Object> params = new HashMap<String,Object>();
		params.put(MYBATIS_SPECIAL_STRING.COLUMNS.name()," order_code="+orderCode+" or parent_order_code="+orderCode);
		List<OrderItem> orderItemList =  orderItemService.queryByParam(params);
		if(CollectionUtils.isNotEmpty(orderItemList)){
			for(OrderItem orderItem:orderItemList){
				//用户手机号限购
				TopicLimitItem  topicLimitItem = new TopicLimitItem();
				topicLimitItem.setTopicId(orderItem.getTopicId());
				topicLimitItem.setSkuCode(orderItem.getSkuCode());
				topicLimitItem.setBuyedQuantity(orderItem.getQuantity());
				topicLimitItem.setLimitType(TopicLimitType.MOBILE.code);
				topicLimitItem.setLimitValue(mobile);	
				try{
					topicLimitItemService.updateTopicLimitItemQuantity(topicLimitItem);//更新促销限购统计数量
				}catch(Throwable throwable){
					ExceptionUtils.print(new FailInfo(throwable), log, topicLimitItem);
				}
			}
		}
	}
	
	public FailInfo validate(Long orderCode, Long userId, String userName,LOG_TYPE userType,OrderInfo orderInfo,SubOrder subOrder,CANCEL_TYPE operateType){
		FailInfo failInfo = null;
		switch(operateType){
		 	case OUT_TIME:
			case MEMBER_NOPAY:
				new NotNullCodeValidate(orderCode, failInfo);
				new NotNullOrderInfoValidate(orderInfo, failInfo);
				new OrderStatusValidate(orderInfo.getOrderStatus(), failInfo);
				new PermissionValidate(new Long[]{orderInfo.getMemberId(),userId}, failInfo);
				break;
			case MEMBER_PAYED:
				new NotNullCodeValidate(orderCode, failInfo);
				new NotNullSubOrderValidate(subOrder, failInfo);
				new OrderStatusValidate(subOrder.getOrderStatus(), failInfo);
				new PermissionValidate(new Long[]{subOrder.getMemberId(),userId}, failInfo);
				new OverTimeValidate(subOrder, failInfo);
				break;
			case USER:
				new NotNullCodeValidate(orderCode, failInfo);
				new NotNullSubOrderValidate(subOrder, failInfo);
				new OrderStatusValidate(subOrder.getOrderStatus(), failInfo);
				new CommonSeaOrderValidate(subOrder, failInfo);
			default:;
		}
		return failInfo;
	}
	/**
	 * 验证
	 */
	abstract class CancelValidate<T>{
		protected CancelValidate(T t, FailInfo failInfo){
			validate(t,failInfo);
		}
		private CancelValidate<T> validate(T t, FailInfo failInfo) {
			if(failInfo==null){
				failInfo = validate(t);
			}
			return this;
		}
		public <F> F add(F validate){
			return validate;
		}
		abstract FailInfo validate(T t);
	}
	
	/**订单编号是否为空*/
	class NotNullCodeValidate extends CancelValidate<Long>{
		protected NotNullCodeValidate(Long t, FailInfo failInfo) {
			super(t, failInfo);
		}

		public FailInfo validate(Long t) {
			if(t==null){
				return new FailInfo(String.format("无效的订单号[%s]", t),OrderErrorCodes.INVALID_ORDER_CODE);
			}
			return null;
		}
	}
	
	/**订单是否为空*/
	class NotNullSubOrderValidate extends CancelValidate<SubOrder>{
		protected NotNullSubOrderValidate(SubOrder t, FailInfo failInfo) {
			super(t, failInfo);
		}

		public FailInfo validate(SubOrder subOrder) {
			if(subOrder==null){
				return new FailInfo(String.format("无效的订单[%s]"),OrderErrorCodes.INVALID_ORDER_CODE);
			}
			return null;
		}
	}
	
	/**订单是否为空*/
	class NotNullOrderInfoValidate extends CancelValidate<OrderInfo>{
		protected NotNullOrderInfoValidate(OrderInfo t, FailInfo failInfo) {
			super(t, failInfo);
		}

		public FailInfo validate(OrderInfo orderInfo) {
			if(orderInfo==null){
				return new FailInfo(String.format("无效的订单"),OrderErrorCodes.INVALID_ORDER_CODE);
			}
			return null;
		}
	}
	
	/**验证订单状态*/
	class OrderStatusValidate extends CancelValidate<Integer>{
		protected OrderStatusValidate(Integer t, FailInfo failInfo) {
			super(t, failInfo);
		}

		public FailInfo validate(Integer orderStatus) {
			if(!ORDER_STATUS.DELIVERY.code.equals(orderStatus) && !ORDER_STATUS.PAYMENT.code.equals(orderStatus)){
				return new FailInfo(String.format("只能取消未支付、已支付的订单，订单状态是",ORDER_STATUS.getCnName(orderStatus)),OrderErrorCodes.NOT_ALLOW_CANCELE);
			}
			return null;
		}
	}
	
	/**权限验证*/
	class PermissionValidate extends CancelValidate<Long[]>{
		protected PermissionValidate(Long[] t, FailInfo failInfo) {
			super(t, failInfo);
		}

		public FailInfo validate(Long[] userIdList) {
			if(!userIdList[0].equals(userIdList[1])){
				return new FailInfo(String.format("只能取消自己的订单"),OrderErrorCodes.ORDER_CODE_UNMATCH_MEMBER);
			}
			return null;
		}
	}
	
	/**超时不能取消*/
	class OverTimeValidate extends  CancelValidate<SubOrder>{
		protected OverTimeValidate(SubOrder t, FailInfo failInfo) {
			super(t, failInfo);
		}

		public FailInfo validate(SubOrder subOrder) {
			Calendar calendar = Calendar.getInstance();
			calendar.add(Calendar.MINUTE, -SEA_CANCEL_LIMIT);;
			if(OrderUtils.isSeaOrder(subOrder.getType()) && subOrder.getPayTime().before(calendar.getTime())){
				return new FailInfo("订单已推送海关，等待审核不能取消",OrderErrorCodes.SEA_ORDER_CANCEL_ERROR);
			}
			return null;
		}
	}
	
	/** 需要海关申报的订单，已经申报通关的或者等待通关的单子不允许取消 */
	class CommonSeaOrderValidate extends  CancelValidate<SubOrder>{
		protected CommonSeaOrderValidate(SubOrder t, FailInfo failInfo) {
			super(t, failInfo);
		}
		public FailInfo validate(SubOrder subOrder) {
			Integer clearanceStatus = subOrder.getClearanceStatus();
			if(OrderType.COMMON_SEA.code.equals(subOrder.getType())){
				if (!ClearanceStatus.NEW.code.equals(clearanceStatus)) {
					return new FailInfo("已推送海关的订单不允许取消",OrderErrorCodes.ORDER_CANCEL_ERROR);
				}
			}
			return null;
		}
	}
	
	/**
	 * 取消操作类型
	 * @author szy
	 *
	 */
	enum CANCEL_TYPE{
		OUT_TIME,
		MEMBER_NOPAY,
		MEMBER_PAYED,
		USER
	}
	// 添加退款单，返回退款单号，若是0元单，则不生成退款单，并返回null
	private Long insertRefundInfoDO(SubOrder sub, String operatorName) {
		if (sub.getTotal()<=0) {	// 0元单
			log.debug("订单[{}]为0元单，不生成退款单", sub.getOrderCode());
			return null;
		}
		//子单实付总价
		Double refundBizType=sub.getTotal();
		//兑换码退单逻辑  start  
		if(OrderConstant.OrderType.BUY_COUPONS.code.equals(sub.getType())){//退还未使用的
			refundBizType=sub.getTotal()*orderRedeemItemService.getUnusedRateByOrderCode(sub.getOrderCode());//未使用的兑换码比例
		}else{
			refundBizType=sub.getTotal();
		}
		
		//兑换码退单逻辑  end  
		RefundInfo refundInfo = new RefundInfo();
		refundInfo.setOrderCode(sub.getOrderCode());
		refundInfo.setRefundBizType(RefundConstant.BIZ_TYPE.ORDER.code);
		refundInfo.setRefundAmount(refundBizType);
		refundInfo.setRefundStatus(RefundConstant.REFUND_STATUS.APPLY.code);
		refundInfo.setRefundType(RefundConstant.REFUND_TYPE.CANCEL.code);
		
		Long gatewayId=null;
		PaymentInfo payinfo = paymentInfoService.queryPaymentInfoByBizCodeAndBizType(sub.getOrderCode(), PaymentConstant.BIZ_TYPE.SUBORDER.code);
		if(payinfo == null){
			log.error("paymentinfo 为空, 子订单号为[{}]", sub.getOrderCode());
			payinfo = paymentInfoService.queryPaymentInfoByBizCodeAndBizType(sub.getParentOrderCode(), PaymentConstant.BIZ_TYPE.ORDER.code);
		}
		if(payinfo == null){
			log.error("paymentinfo 为空, 父订单号为[{}]", sub.getParentOrderCode());
		}else{
			gatewayId = payinfo.getGatewayId();
		}
		refundInfo.setGatewayId(gatewayId);
		refundInfo.setCreateUser(operatorName);
		RefundInfo result = refundInfoService.insert(refundInfo);
		return result==null?null:result.getRefundCode();
	}
}
