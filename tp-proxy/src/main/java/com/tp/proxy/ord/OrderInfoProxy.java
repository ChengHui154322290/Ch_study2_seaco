package com.tp.proxy.ord;

import static com.tp.util.BigDecimalUtil.add;
import static com.tp.util.BigDecimalUtil.divide;
import static com.tp.util.BigDecimalUtil.formatToPrice;
import static com.tp.util.BigDecimalUtil.multiply;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.function.Consumer;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.util.ExceptionUtils;
import com.tp.common.vo.DssConstant;
import com.tp.common.vo.PageInfo;
import com.tp.common.vo.Constant.SPLIT_SIGN;
import com.tp.common.vo.DAOConstant.MYBATIS_SPECIAL_STRING;
import com.tp.common.vo.DssConstant.ACCOUNT_TYPE;
import com.tp.common.vo.DssConstant.BUSSINESS_TYPE;
import com.tp.common.vo.DssConstant.INDIRECT_TYPE;
import com.tp.dto.common.FailInfo;
import com.tp.dto.common.ResultInfo;
import com.tp.dto.mmp.OrderCouponDTO;
import com.tp.dto.mmp.enums.CouponType;
import com.tp.dto.ord.CartLineDTO;
import com.tp.dto.ord.OrderInfoDTO;
import com.tp.dto.ord.OrderReceiveGoodsDTO;
import com.tp.dto.ord.SeaOrderItemDTO;
import com.tp.dto.ord.directOrderNB.RetMessageNBDto;
import com.tp.dto.ord.remote.OrderCountDTO;
import com.tp.dto.ord.remote.OrderDetails4UserDTO;
import com.tp.dto.ord.remote.OrderList4UserDTO;
import com.tp.dto.ord.remote.SubOrder4BackendDTO;
import com.tp.exception.OrderServiceException;
import com.tp.model.dss.CommisionDetail;
import com.tp.model.dss.GlobalCommision;
import com.tp.model.dss.PromoterInfo;
import com.tp.model.mem.MemberInfo;
import com.tp.model.ord.OrderInfo;
import com.tp.model.ord.OrderItem;
import com.tp.model.ord.OrderPromotion;
import com.tp.model.ord.SubOrder;
import com.tp.model.pay.PaymentInfo;
import com.tp.model.usr.UserInfo;
import com.tp.proxy.BaseProxy;
import com.tp.proxy.dss.GlobalCommisionProxy;
import com.tp.proxy.dss.PromoterInfoProxy;
import com.tp.query.ord.SubOrderQO;
import com.tp.result.ord.SubOrderExpressInfoDTO;
import com.tp.service.IBaseService;
import com.tp.service.dss.ICommisionDetailService;
import com.tp.service.mem.IMemberInfoService;
import com.tp.service.ord.IOrderInfoService;
import com.tp.service.ord.IOrderPromotionService;
import com.tp.service.ord.ISubOrderService;
import com.tp.service.ord.directOrder.IDirectOrderService;
import com.tp.service.ord.directOrder.NB.IDirectOrderNBService;
import com.tp.service.ord.local.IBuyNowLocalService;
import com.tp.service.ord.local.IOrderConfirmLocalService;
import com.tp.service.ord.local.IOrderSubmitLocalService;
import com.tp.service.ord.remote.IOrderCancelRemoteService;
import com.tp.service.ord.remote.ISalesOrderRemoteService;
import com.tp.util.BigDecimalUtil;
import com.tp.util.StringUtil;
/**
 * 订单表代理层
 * @author szy
 *
 */
@Service
public class OrderInfoProxy extends BaseProxy<OrderInfo>{

	@Autowired
	private IOrderInfoService orderInfoService;
	@Autowired
	private ISalesOrderRemoteService salesOrderRemoteService;
	@Autowired
	private IOrderConfirmLocalService orderConfirmLocalService;
	@Autowired
	private IOrderCancelRemoteService orderCancelRemoteService;
	@Autowired
	private IOrderSubmitLocalService orderSubmitLocalService;
	@Autowired
	private IBuyNowLocalService buyNowLocalService;
	
	@Autowired
	private IMemberInfoService memberInfoService;
	@Autowired
	private IOrderPromotionService orderPromotionService;
	@Autowired
	private PromoterInfoProxy promoterInfoProxy;
	@Autowired
	private GlobalCommisionProxy globalCommisionProxy;
	@Autowired	
	private ICommisionDetailService commisionDetailService;
	
	@Autowired
	private ISubOrderService subOrderService;
	
	@Autowired
	private IDirectOrderNBService directOrderNBService;
	
	@Autowired
	Properties settings;
	
	@Override
	public IBaseService<OrderInfo> getService() {
		return orderInfoService;
	}

	public SubOrder4BackendDTO queryOrder(Long orderCode) {
		SubOrder4BackendDTO subOrder4BackendDTO = salesOrderRemoteService.findSubOrder4BackendByCode(orderCode);
		MemberInfo user = memberInfoService.queryById(subOrder4BackendDTO.getOrder().getMemberId());
		subOrder4BackendDTO.setLoginName(user.getMobile());
		return subOrder4BackendDTO;
	}

	public ResultInfo<Boolean> cancelOrder(Long orderCode,UserInfo userInfo,String reason) {
		try{
			SubOrder subOrder = subOrderService.selectOneByCode(orderCode);
			if(null!=settings.getProperty("NB_HWZY_WAREHOUSE_ID")&&settings.getProperty("NB_HWZY_WAREHOUSE_ID").equals(String.valueOf(subOrder.getWarehouseId()))){//特定的海外直邮订单
				if(1!=subOrder.getDirectOrderStatus()){//判断是否已推送
					return new ResultInfo<>(new FailInfo("订单未推送或推送失败，不能取消"));
				}
				RetMessageNBDto retMessage = directOrderNBService.cancelOrderNB(String.valueOf(orderCode)); //调取消订单接口
				if("F".equals(retMessage.getResult())){
					return new ResultInfo<>(new FailInfo("取消订单失败："+retMessage.getResultMsg()));
				}
				subOrder.setDirectOrderStatus(0);
				subOrderService.updateNotNullById(subOrder);
			}
			orderCancelRemoteService.cancelOrderByBackend(orderCode, userInfo.getId(), userInfo.getUserName(),reason);
			return new ResultInfo<>(Boolean.TRUE);
		} catch(OrderServiceException ex){
			return new ResultInfo<>(new FailInfo(ex.getMessage()));
		}catch(Throwable exception){
			ExceptionUtils.print(new FailInfo(exception), logger,orderCode,userInfo);
			return new ResultInfo<>(new FailInfo("取消失败"));
		}
	}
	public ResultInfo<Boolean> cancelOrderByMember(Long orderCode,MemberInfo memberInfo) {
		try{
			orderCancelRemoteService.cancelOrder(orderCode, memberInfo.getId(), memberInfo.getNickName());
			return new ResultInfo<>(Boolean.TRUE);
		}catch(OrderServiceException ex){
			return new ResultInfo<>(new FailInfo(ex.getMessage()));
		}catch(Throwable exception){
			ExceptionUtils.print(new FailInfo(exception), logger,orderCode,memberInfo);
			return new ResultInfo<>(new FailInfo("取消失败"));
		}
	}
	public ResultInfo<OrderCountDTO> findOrderCountDTOByMemberId(Long memberId,List<Integer> orderTypeList){
		try{
			OrderCountDTO order = salesOrderRemoteService.findOrderCountDTOByMemberId(memberId,orderTypeList,null);
			return new ResultInfo<>(order);
		}catch(OrderServiceException ex){
			return new ResultInfo<>(new FailInfo(ex.getMessage()));
		}catch(Throwable exception){
			ExceptionUtils.print(new FailInfo(exception), logger,memberId);
			return new ResultInfo<>(new FailInfo("查询订单数失败"));
		}
	}
	public ResultInfo<OrderCountDTO> findOrderCountDTOByMemberId(Long memberId,List<Integer> orderTypeList,String channelCode){
		try{
			OrderCountDTO order = salesOrderRemoteService.findOrderCountDTOByMemberId(memberId,orderTypeList,channelCode);
			return new ResultInfo<>(order);
		}catch(OrderServiceException ex){
			return new ResultInfo<>(new FailInfo(ex.getMessage()));
		}catch(Throwable exception){
			ExceptionUtils.print(new FailInfo(exception), logger,memberId);
			return new ResultInfo<>(new FailInfo("查询订单数失败"));
		}
	}
	/**
     * 会员中心 - 订单详情查询
     * 
     * @param memberId
     * @param code
     * @param memberId
     * @param code
     * @return
     */
    public ResultInfo<OrderDetails4UserDTO> findOrderDetails4User(Long memberId, Long code){
    	try{
			return new ResultInfo<OrderDetails4UserDTO>(salesOrderRemoteService.findOrderDetails4User(memberId,code));
		}catch(OrderServiceException ex){
			return new ResultInfo<>(new FailInfo(ex.getMessage()));
		}catch(Throwable exception){
			ExceptionUtils.print(new FailInfo(exception), logger,memberId,code);
			return new ResultInfo<>(new FailInfo("查询订单列表失败"));
		}
    }
    
    /**
     * 促销中心订单 - 订单列表查询
     * 
     * @param memberId
     * @param code
     * @param memberId
     * @param code
     * @return
     */
    public ResultInfo<PageInfo<OrderList4UserDTO>> queryOrderPageByPromoter(SubOrderQO query){
    	try{
//    		 PageInfo<OrderList4UserDTO> orderPage = salesOrderRemoteService.findOrderList4UserPageByQO(query);
//    		 initPromoterCommision(orderPage);
   		 	PageInfo<OrderList4UserDTO> orderPage = salesOrderRemoteService.findOrderList4UserPageByQO_coupons_shop_scan(query);
    		 initPromoterCommision_coupons_shop_scan(orderPage);
			return new ResultInfo<PageInfo<OrderList4UserDTO>>(orderPage);
		}catch(OrderServiceException ex){
			return new ResultInfo<>(new FailInfo(ex.getMessage()));
		}catch(Throwable exception){
			ExceptionUtils.print(new FailInfo(exception), logger,query);
			return new ResultInfo<>(new FailInfo("查询物流信息失败"));
		}
    }
    
    
    /**
     * 促销中心订单 - 客户总数
     * 
     * @param query
     * @return
     */
    public ResultInfo<Integer> getTotalCustomersByPromoter(SubOrderQO query){
    	try{
    		return salesOrderRemoteService.getTotalCustomersForPromoterByQO(query);
		}catch(OrderServiceException ex){
			return new ResultInfo<>(new FailInfo(ex.getMessage()));
		}catch(Throwable exception){
			ExceptionUtils.print(new FailInfo(exception), logger,query);
			return new ResultInfo<>(new FailInfo("查询物流信息失败"));
		}
    }

    
    /**
     * 会员中心 - 订单列表查询
     * 
     * @param memberId
     * @param code
     * @param memberId
     * @param code
     * @return
     */
    public ResultInfo<PageInfo<OrderList4UserDTO>> findOrderList4UserPage(SubOrderQO query){
    	try{
			return new ResultInfo<PageInfo<OrderList4UserDTO>>(salesOrderRemoteService.findOrderList4UserPage(query));
		}catch(OrderServiceException ex){
			return new ResultInfo<>(new FailInfo(ex.getMessage()));
		}catch(Throwable exception){
			ExceptionUtils.print(new FailInfo(exception), logger,query);
			return new ResultInfo<>(new FailInfo("查询物流信息失败"));
		}
    }
    
    /**
     * 会员中心 - 订单列表查询
     * 
     * @param memberId
     * @param code
     * @param memberId
     * @param code
     * @return
     */
    public ResultInfo<PageInfo<OrderList4UserDTO>> findOrderList4UserPageByQO(SubOrderQO query){
    	try{
			return new ResultInfo<PageInfo<OrderList4UserDTO>>(salesOrderRemoteService.findOrderList4UserPageByQO(query));
		}catch(OrderServiceException ex){
			return new ResultInfo<>(new FailInfo(ex.getMessage()));
		}catch(Throwable exception){
			ExceptionUtils.print(new FailInfo(exception), logger,query);
			return new ResultInfo<>(new FailInfo("查询物流信息失败"));
		}
    }
    /**
     * 
     * <pre>
     * 查询物流日志信息列表 用户操作  ,接口后续会切换到新的接口
     * </pre>
     * 
     * @param memberId
     *            会员ID
     * @param code
     *            子订单编号或者退货单号，必填
     * @param packageNo
     *            运单编号，选填
     * @return
     */
    public ResultInfo<List<SubOrderExpressInfoDTO>> queryExpressLogInfoByUser(Long memberId, Long code, String packageNo){
    	try{
			return new ResultInfo<List<SubOrderExpressInfoDTO>>(salesOrderRemoteService.queryExpressLogInfoByUser(memberId,code,packageNo));
		}catch(OrderServiceException ex){
			return new ResultInfo<>(new FailInfo(ex.getMessage()));
		}catch(Throwable exception){
			ExceptionUtils.print(new FailInfo(exception), logger,memberId,code,packageNo);
			return new ResultInfo<>(new FailInfo("查询物流信息失败"));
		}
    }
    
    
    public ResultInfo<Map<String, List<OrderCouponDTO>>> getOrderCouponInfo(Long memberId, Integer sourceType,Integer itemType){
    	try{
			return new ResultInfo<Map<String, List<OrderCouponDTO>>>(orderConfirmLocalService.getOrderCouponInfo(memberId,sourceType,itemType));
		}catch(OrderServiceException ex){
			return new ResultInfo<>(new FailInfo(ex.getMessage()));
		}catch(Throwable exception){
			ExceptionUtils.print(new FailInfo(exception), logger,memberId,sourceType,itemType);
			return new ResultInfo<>(new FailInfo("获取优惠券失败"));
		}
    }
    /**
	 * 
	 * <pre>
	 * 订单提交之后信息保存
	 * </pre>
	 * 
	 * @param orderInfoDTO
	 * @param userMenber
	  * @param ip
	  * @param orderSource
	  * @return PaymentInfo
	 */
    public ResultInfo<List<PaymentInfo>> orderSubmit(OrderInfoDTO orderInfoDTO,  MemberInfo userMenber, String ip,Integer orderSource){
    	try{
			return new ResultInfo<List<PaymentInfo>>(orderSubmitLocalService.orderSubmit(orderInfoDTO,userMenber,ip,orderSource));
		}catch(OrderServiceException ex){
			return new ResultInfo<>(new FailInfo(ex.getMessage()));
		}catch(Throwable exception){
			ExceptionUtils.print(new FailInfo(exception), logger,orderInfoDTO,userMenber,ip,orderSource);
			return new ResultInfo<>(new FailInfo("提交失败"));
		}
    }
    /**
     * 
     * <pre>
     * 收货后订单操作,用户操作
     * </pre>
     * 
     * @param memberID
     * @param orderReceiveGoodsDTO
     */
    public ResultInfo<Boolean> operateOrderForReceiveGoodsByUser(Long memberID, OrderReceiveGoodsDTO orderReceiveGoodsDTO){
    	try{
			salesOrderRemoteService.operateOrderForReceiveGoodsByUser(memberID, orderReceiveGoodsDTO);
			return new ResultInfo<Boolean>(Boolean.TRUE);
		}catch(OrderServiceException ex){
			return new ResultInfo<>(new FailInfo(ex.getMessage()));
		}catch(Throwable exception){
			ExceptionUtils.print(new FailInfo(exception), logger,memberID, orderReceiveGoodsDTO);
			return new ResultInfo<>(new FailInfo("确认失败"));
		}
    }
    
    /**
     * 根据（父/子）订单号逻辑删除
     * 
     * @param memberId
     * @param code
     * @return 影响行数
     */
    public ResultInfo<Integer> deleteByCode(Long memberId, Long code){
    	try{
			return new ResultInfo<Integer>(salesOrderRemoteService.deleteByCode(memberId, code));
		}catch(OrderServiceException ex){
			return new ResultInfo<>(new FailInfo(ex.getMessage()));
		}catch(Throwable exception){
			ExceptionUtils.print(new FailInfo(exception), logger,memberId, code);
			return new ResultInfo<>(new FailInfo("删除失败"));
		}
    }
    
	/**
	 * 
	 * <pre>
	 * 计算海淘购物车中海淘商品的价格
	 * </pre>
	 * 
	 * @param cartDTO
	 * @param couponIdList
	 * @return
	 */
    public ResultInfo<SeaOrderItemDTO> calcItemPrice4Sea(Long memberId,List<Long> couponIdList,Integer sourceType){
    	try{
			return new ResultInfo<SeaOrderItemDTO>(orderConfirmLocalService.calcItemPrice4Sea(memberId, couponIdList,sourceType));
		}catch(OrderServiceException ex){
			return new ResultInfo<>(new FailInfo(ex.getMessage()));
		}catch(Throwable exception){
			ExceptionUtils.print(new FailInfo(exception), logger,memberId, couponIdList);
			return new ResultInfo<>(new FailInfo("价格计算失败"));
		}
    }
    
    /**
     * 海淘 - 直接购买
     * @param memberId
     * @param couponIdList
     * @return
     */
    public ResultInfo<SeaOrderItemDTO> calcItemPrice4SeaBuyNow(Long memberId,String buyNowId, List<Long> couponIdList,Integer sourceType){
    	try{
			return new ResultInfo<SeaOrderItemDTO>(orderConfirmLocalService.calcItemPrice4SeaBuyNow(memberId,buyNowId, couponIdList,sourceType));
		}catch(OrderServiceException ex){
			return new ResultInfo<>(new FailInfo(ex.getMessage()));
		}catch(Throwable exception){
			ExceptionUtils.print(new FailInfo(exception), logger,memberId,buyNowId, couponIdList);
			return new ResultInfo<>(new FailInfo("立即购买失败"));
		}
    }
    
//    public ResultInfo<String> buyNow(Long memberId, String ip, Integer platform, CartLineDTO cartLine){
//    	try{
//			return new ResultInfo<String>(buyNowLocalService.buyNow(memberId,ip,platform,cartLine));
//		}catch(OrderServiceException ex){
//			return new ResultInfo<>(new FailInfo(ex.getMessage()));
//		}catch(Throwable exception){
//			ExceptionUtils.print(new FailInfo(exception), logger,memberId,ip,platform,cartLine);
//			return new ResultInfo<String>(new FailInfo("价格计算失败"));
//		}
//    }
    
    public ResultInfo<Map<String, List<OrderCouponDTO>>> getOrderCouponInfoBuyNow(Long memberId, Integer sourceType, Integer itemType, String uuid){
    	try{
			return new ResultInfo<Map<String, List<OrderCouponDTO>>>(orderConfirmLocalService.getOrderCouponInfoBuyNow(memberId,sourceType,itemType,uuid));
		}catch(OrderServiceException ex){
			return new ResultInfo<>(new FailInfo(ex.getMessage()));
		}catch(Throwable exception){
			ExceptionUtils.print(new FailInfo(exception), logger,memberId,sourceType,itemType,uuid);
			return new ResultInfo<>(new FailInfo("获取优惠券失败"));
		}
    }
    
    private PageInfo<OrderList4UserDTO> initPromoterCommision(PageInfo<OrderList4UserDTO> orderPage){
    	if(CollectionUtils.isNotEmpty(orderPage.getRows())){
    		List<Long> orderCodeList = new ArrayList<Long>();
			List<Long> promoterIdList = new ArrayList<Long>();
    		for (OrderList4UserDTO orderInfo : orderPage.getRows()) {
    			if(null == orderInfo.getSubOrder())
    				continue;
				orderCodeList.add(orderInfo.getSubOrder().getOrderCode());
				if(null!=orderInfo.getSubOrder().getShopPromoterId()){
					promoterIdList.add(orderInfo.getSubOrder().getShopPromoterId());
				}
				if(null!=orderInfo.getSubOrder().getPromoterId()){
					promoterIdList.add(orderInfo.getSubOrder().getPromoterId());
				}
			}
			Map<String,Object> params = new HashMap<String,Object>();
			params.put("couponType", CouponType.NO_CONDITION.ordinal());
			params.put(MYBATIS_SPECIAL_STRING.COLUMNS.name(), "order_code in ("+StringUtil.join(orderCodeList,SPLIT_SIGN.COMMA)+")");
			List<OrderPromotion> orderPromotionList = orderPromotionService.queryByParam(params);
			GlobalCommision globalCommision = globalCommisionProxy.queryLastGlobalCommision().getData();
			params.clear();
			params.put(MYBATIS_SPECIAL_STRING.COLUMNS.name(), "promoter_id in ("+StringUtil.join(promoterIdList,SPLIT_SIGN.COMMA)+")");
			Map<Long,Float> commisionRateMap = new HashMap<Long,Float>();
			if(CollectionUtils.isNotEmpty(promoterIdList)){
				List<PromoterInfo> promoterInfoList = promoterInfoProxy.queryByParam(params).getData();
				if(CollectionUtils.isNotEmpty(promoterInfoList)){
					promoterInfoList.forEach(new Consumer<PromoterInfo>(){
						public void accept(PromoterInfo promoterInfo) {
							if(null!=promoterInfo.getCommisionRate() && DssConstant.PROMOTER_TYPE.COUPON.code.equals(promoterInfo.getPromoterType())){
								commisionRateMap.put(promoterInfo.getPromoterId(), promoterInfo.getCommisionRate());
							}else if(DssConstant.PROMOTER_TYPE.DISTRIBUTE.code.equals(promoterInfo.getPromoterType())){
								commisionRateMap.put(promoterInfo.getPromoterId(), globalCommision.getCurrentCommisionRate(promoterInfo));
							}
						}
					});
				}
			}
			Map<Long,Double> couponAmountMap = new HashMap<Long,Double>();
			if(CollectionUtils.isNotEmpty(orderPromotionList)){
				orderPromotionList.forEach(new Consumer<OrderPromotion>(){
					public void accept(OrderPromotion orderPromotion) {
						if(couponAmountMap.containsKey(orderPromotion.getOrderCode())){
							couponAmountMap.put(orderPromotion.getOrderCode(), formatToPrice(add(couponAmountMap.get(orderPromotion.getOrderCode()),orderPromotion.getDiscount())).doubleValue());
						}else{
							couponAmountMap.put(orderPromotion.getOrderCode(), orderPromotion.getDiscount());
						}
					}
				});
			}
			orderPage.getRows().forEach(new Consumer<OrderList4UserDTO>(){
				public void accept(OrderList4UserDTO orderInfo) {
					SubOrder subOrder = orderInfo.getSubOrder();
					if (null == subOrder) {
						return;
					}
					subOrder.setDiscount(0.00d);
					subOrder.setDiscount(couponAmountMap.get(orderInfo.getSubOrder().getOrderCode()));
					subOrder.setCommisionAmoutCoupon(0.00d);
					if(null!=commisionRateMap.get(subOrder.getPromoterId())){
						subOrder.setCommisionAmoutCoupon(formatToPrice(divide(multiply(add(subOrder.getDiscount(),subOrder.getPayTotal()),commisionRateMap.get(subOrder.getPromoterId())),100)).doubleValue());
					}
					subOrder.setCommisionAmoutShop(0.00d);
					if(null!=commisionRateMap.get(subOrder.getShopPromoterId())){
						BigDecimal commisionTotal = BigDecimal.ZERO;
						for(OrderItem orderItem:orderInfo.getOrderItemList()){
							if(orderItem.getCommisionRate()!=null){
								commisionTotal = commisionTotal.add(multiply(add(orderItem.getSubTotal(),orderItem.getCouponAmount()),multiply(orderItem.getCommisionRate(),commisionRateMap.get(subOrder.getShopPromoterId()))));
							}
						}
						subOrder.setCommisionAmoutShop(BigDecimalUtil.toPrice(commisionTotal));
					}
	
					// 如果存在分销店铺重新计算
					if(null!=commisionRateMap.get(subOrder.getShopPromoterId())){
						CommisionDetail detail = new CommisionDetail();
						detail.setPromoterId(subOrder.getShopPromoterId());
						detail.setPromoterType(DssConstant.PROMOTER_TYPE.DISTRIBUTE.code);
						detail.setIndirect(INDIRECT_TYPE.NO.code);
						detail.setBizType(BUSSINESS_TYPE.ORDER.code);
						detail.setOperateType(ACCOUNT_TYPE.INCOMING.code);
						detail.setOrderCode(subOrder.getOrderCode());												
						 List<CommisionDetail> detailist = commisionDetailService.queryByObject(detail);
						 if(detailist != null && !detailist.isEmpty() ){							 
							 Double commision = 0.0d;
							 for( CommisionDetail comiDetail : detailist){
								 commision = commision + comiDetail.getCommision();
							 }
							 subOrder.setCommisionAmoutShop( commision );							 
						 }
					}
										
					
				}
			});
		}
    	return orderPage;
    }
    
    
    
    private PageInfo<OrderList4UserDTO> initPromoterCommision_coupons_shop_scan(PageInfo<OrderList4UserDTO> orderPage){
    	if(CollectionUtils.isNotEmpty(orderPage.getRows())){
    		List<Long> orderCodeList = new ArrayList<Long>();
			List<Long> promoterIdList = new ArrayList<Long>();
    		for (OrderList4UserDTO orderInfo : orderPage.getRows()) {
    			if(null == orderInfo.getSubOrder())
    				continue;
				orderCodeList.add(orderInfo.getSubOrder().getOrderCode());
				if(null!=orderInfo.getSubOrder().getShopPromoterId()){	// 店铺
					promoterIdList.add(orderInfo.getSubOrder().getShopPromoterId());
				}
				if(null!=orderInfo.getSubOrder().getPromoterId()){	//卡券
					promoterIdList.add(orderInfo.getSubOrder().getPromoterId());					
				}
				if(null!=orderInfo.getSubOrder().getScanPromoterId()){	// 扫码
					promoterIdList.add(orderInfo.getSubOrder().getScanPromoterId());										
				}
								
			}
			Map<String,Object> params = new HashMap<String,Object>();
			params.put("couponType", CouponType.NO_CONDITION.ordinal());
			params.put(MYBATIS_SPECIAL_STRING.COLUMNS.name(), "order_code in ("+StringUtil.join(orderCodeList,SPLIT_SIGN.COMMA)+")");
			List<OrderPromotion> orderPromotionList = orderPromotionService.queryByParam(params);
			GlobalCommision globalCommision = globalCommisionProxy.queryLastGlobalCommision().getData();
			params.clear();
			params.put(MYBATIS_SPECIAL_STRING.COLUMNS.name(), "promoter_id in ("+StringUtil.join(promoterIdList,SPLIT_SIGN.COMMA)+")");
			Map<Long,Float> commisionRateMap = new HashMap<Long,Float>();
			if(CollectionUtils.isNotEmpty(promoterIdList)){
				List<PromoterInfo> promoterInfoList = promoterInfoProxy.queryByParam(params).getData();
				if(CollectionUtils.isNotEmpty(promoterInfoList)){
					promoterInfoList.forEach(new Consumer<PromoterInfo>(){
						public void accept(PromoterInfo promoterInfo) {
//							if(null!=promoterInfo.getCommisionRate() && DssConstant.PROMOTER_TYPE.COUPON.code.equals(promoterInfo.getPromoterType())){
							if( DssConstant.PROMOTER_TYPE.COUPON.code.equals(promoterInfo.getPromoterType()) ){
//								commisionRateMap.put(promoterInfo.getPromoterId(), promoterInfo.getCommisionRate());
								commisionRateMap.put(promoterInfo.getPromoterId(), 1.0f);
							}else if(DssConstant.PROMOTER_TYPE.DISTRIBUTE.code.equals(promoterInfo.getPromoterType())){
								commisionRateMap.put(promoterInfo.getPromoterId(), globalCommision.getCurrentCommisionRate(promoterInfo));
							}else if( DssConstant.PROMOTER_TYPE.SCANATTENTION.code.equals( promoterInfo.getPromoterType()) ){
								Float rate = 0.0f;
								if( globalCommision.getScanCommisionRate() !=null && globalCommision.getScanCommisionRate() >0){
									rate = divide(globalCommision.getScanCommisionRate(), 100).floatValue();
								}
								commisionRateMap.put(promoterInfo.getPromoterId(), rate);								
							}
						}
					});
				}
			}
			Map<Long,Double> couponAmountMap = new HashMap<Long,Double>();
			if(CollectionUtils.isNotEmpty(orderPromotionList)){
				orderPromotionList.forEach(new Consumer<OrderPromotion>(){
					public void accept(OrderPromotion orderPromotion) {
						if(couponAmountMap.containsKey(orderPromotion.getOrderCode())){
							couponAmountMap.put(orderPromotion.getOrderCode(), formatToPrice(add(couponAmountMap.get(orderPromotion.getOrderCode()),orderPromotion.getDiscount())).doubleValue());
						}else{
							couponAmountMap.put(orderPromotion.getOrderCode(), orderPromotion.getDiscount());
						}
					}
				});
			}
			orderPage.getRows().forEach(new Consumer<OrderList4UserDTO>(){
				public void accept(OrderList4UserDTO orderInfo) {
					SubOrder subOrder = orderInfo.getSubOrder();
					if (null == subOrder) {
						return;
					}
					subOrder.setDiscount(0.00d);
					subOrder.setDiscount(couponAmountMap.get(orderInfo.getSubOrder().getOrderCode()));
					subOrder.setCommisionAmoutCoupon(0.00d);					
					subOrder.setCommisionAmoutShop(0.00d);
					if( null!=commisionRateMap.get(subOrder.getScanPromoterId()) ){		// 扫码
						BigDecimal commisionTotal = BigDecimal.ZERO;
						for(OrderItem orderItem:orderInfo.getOrderItemList()){
							if(orderItem.getCommisionRate()!=null){
								BigDecimal itemcommison=multiply(add(orderItem.getSubTotal(),orderItem.getCouponAmount()),multiply(orderItem.getCommisionRate(),commisionRateMap.get(subOrder.getScanPromoterId())));
								orderItem.setCommision(BigDecimalUtil.toPrice(itemcommison));
								commisionTotal = commisionTotal.add(itemcommison);
							}else{
								orderItem.setCommision(0.00d);	
							}
						}
						subOrder.setCommisionAmoutScan(BigDecimalUtil.toPrice(commisionTotal));
					}

					if(null!=commisionRateMap.get(subOrder.getPromoterId())){	// 卡券
//						subOrder.setCommisionAmoutCoupon(formatToPrice(divide(multiply(add(subOrder.getDiscount(),subOrder.getPayTotal()),commisionRateMap.get(subOrder.getPromoterId())),100)).doubleValue());
						BigDecimal commisionTotal = BigDecimal.ZERO;
						for(OrderItem orderItem:orderInfo.getOrderItemList()){
							if(orderItem.getCommisionRate()!=null){
								BigDecimal itemcommison =multiply(add(orderItem.getSubTotal(),orderItem.getCouponAmount()),multiply(orderItem.getCommisionRate(),commisionRateMap.get(subOrder.getPromoterId())));
								orderItem.setCommision(BigDecimalUtil.toPrice(itemcommison));
								commisionTotal = commisionTotal.add(itemcommison);
							}else{
								orderItem.setCommision(0.00d);									
							}
						}
						subOrder.setCommisionAmoutCoupon(BigDecimalUtil.toPrice(commisionTotal));
					}
					
					if(null!=commisionRateMap.get(subOrder.getShopPromoterId())){	// 店铺
						BigDecimal commisionTotal = BigDecimal.ZERO;
						for(OrderItem orderItem:orderInfo.getOrderItemList()){
							if(orderItem.getCommisionRate()!=null){								
								BigDecimal itemcommison = multiply(add(orderItem.getSubTotal(),orderItem.getCouponAmount()),multiply(orderItem.getCommisionRate(),commisionRateMap.get(subOrder.getShopPromoterId())));
								orderItem.setCommision(BigDecimalUtil.toPrice(itemcommison));
								commisionTotal = commisionTotal.add(itemcommison);
							}else{
								orderItem.setCommision(0.00d);
							}
						}
						subOrder.setCommisionAmoutShop(BigDecimalUtil.toPrice(commisionTotal));
					} 

					
	
					// 如果存在分销店铺重新计算
					CommisionDetail detail = new CommisionDetail();
					detail.setIndirect(INDIRECT_TYPE.NO.code);
					detail.setBizType(BUSSINESS_TYPE.ORDER.code);
					detail.setOperateType(ACCOUNT_TYPE.INCOMING.code);

					if( null!=commisionRateMap.get(subOrder.getScanPromoterId()) ){		// 重新计算扫码
						detail.setPromoterId(subOrder.getScanPromoterId());
						detail.setPromoterType(DssConstant.PROMOTER_TYPE.SCANATTENTION.code);
						detail.setOrderCode(subOrder.getOrderCode());												
						 List<CommisionDetail> detailist = commisionDetailService.queryByObject(detail);
						 if(detailist != null && !detailist.isEmpty() ){							 
							 Double commision = 0.0d;
							 for( CommisionDetail comiDetail : detailist){
								for (OrderItem orderItem : orderInfo.getOrderItemList()) {
									if (orderItem.getId()!=null && orderItem.getId().equals(comiDetail.getBizCode())) {
										orderItem.setCommision(comiDetail.getCommision());
										 break;
									}
								}
								 commision = commision + comiDetail.getCommision();
							 }
							 subOrder.setCommisionAmoutScan( commision );							 
						 }						
					}	
					if( null!=commisionRateMap.get(subOrder.getPromoterId()) ){			//  重新计算卡券
						detail.setPromoterId(subOrder.getPromoterId());
						detail.setPromoterType(DssConstant.PROMOTER_TYPE.COUPON.code);
						detail.setOrderCode(subOrder.getOrderCode());												
						 List<CommisionDetail> detailist = commisionDetailService.queryByObject(detail);
						 if(detailist != null && !detailist.isEmpty() ){							 
							 Double commision = 0.0d;
							 for( CommisionDetail comiDetail : detailist){
								for (OrderItem orderItem : orderInfo.getOrderItemList()) {
									if (orderItem.getId()!=null && orderItem.getId().equals(comiDetail.getBizCode())) {
										orderItem.setCommision(comiDetail.getCommision());
										 break;
									}
								}
								 commision = commision + comiDetail.getCommision();
							 }
							 subOrder.setCommisionAmoutCoupon( commision );							 
						 }
					}
					if(null!=commisionRateMap.get(subOrder.getShopPromoterId())){
						detail.setPromoterId(subOrder.getShopPromoterId());
						detail.setPromoterType(DssConstant.PROMOTER_TYPE.DISTRIBUTE.code);
						detail.setOrderCode(subOrder.getOrderCode());												
						 List<CommisionDetail> detailist = commisionDetailService.queryByObject(detail);
						 if(detailist != null && !detailist.isEmpty() ){							 
							 Double commision = 0.0d;
							 for( CommisionDetail comiDetail : detailist){
								for(OrderItem orderItem:orderInfo.getOrderItemList()){
									 if( orderItem.getId()!=null && orderItem.getId().equals(comiDetail.getBizCode()) ){
										 orderItem.setCommision(comiDetail.getCommision());
										 break;
									 }
								 }
								 commision = commision + comiDetail.getCommision();
							 }
							 subOrder.setCommisionAmoutShop( commision );							 
						 }
					} 
					
				}
			});
		}
    	return orderPage;
    }

}
