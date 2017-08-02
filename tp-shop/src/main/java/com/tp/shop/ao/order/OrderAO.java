package com.tp.shop.ao.order;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.vo.Constant;
import com.tp.common.vo.PageInfo;
import com.tp.common.vo.PaymentConstant;
import com.tp.dto.common.ResultInfo;
import com.tp.dto.ord.OrderInitDto;
import com.tp.dto.ord.OrderReceiveGoodsDTO;
import com.tp.dto.ord.ShoppingCartDto;
import com.tp.dto.ord.remote.OrderDetails4UserDTO;
import com.tp.dto.ord.remote.OrderList4UserDTO;
import com.tp.dto.pay.AppPayData;
import com.tp.enums.common.PlatformEnum;
import com.tp.m.base.MResultVO;
import com.tp.m.base.Page;
import com.tp.m.enums.MResultInfo;
import com.tp.m.enums.OrderEnum;
import com.tp.m.exception.MobileException;
import com.tp.m.query.order.QueryOrder;
import com.tp.m.to.cache.TokenCacheTO;
import com.tp.m.util.JsonUtil;
import com.tp.m.util.StringUtil;
import com.tp.m.vo.order.BuyNowVO;
import com.tp.m.vo.order.OrderDetailVO;
import com.tp.m.vo.order.OrderVO;
import com.tp.m.vo.order.PayOrderLineVO;
import com.tp.m.vo.order.SubmitOrderInfoVO;
import com.tp.m.vo.pay.BasePayVO;
import com.tp.model.dss.ChannelInfo;
import com.tp.model.dss.PromoterInfo;
import com.tp.model.mem.MemberInfo;
import com.tp.model.ord.CartItemInfo;
import com.tp.model.ord.OrderRedeemItem;
import com.tp.model.pay.PaymentGateway;
import com.tp.model.pay.PaymentInfo;
import com.tp.proxy.dss.ChannelInfoProxy;
import com.tp.proxy.dss.PromoterInfoProxy;
import com.tp.proxy.ord.CartProxy;
import com.tp.proxy.ord.OrderInfoProxy;
import com.tp.proxy.ord.OrderProxy;
import com.tp.proxy.ord.OrderRedeemItemProxy;
import com.tp.proxy.pay.AppPaymentProxy;
import com.tp.proxy.pay.PaymentGatewayProxy;
import com.tp.query.pay.AppPaymentCallDto;
import com.tp.shop.ao.dss.PromoterAO;
import com.tp.shop.convert.OrderConvert;
import com.tp.shop.convert.OrderInfoConvert;
import com.tp.shop.convert.PayConvert;
import com.tp.shop.helper.OrderHelper;
import com.tp.shop.helper.RequestHelper;
import com.tp.shop.helper.VersionHelper;


/**
 * 订单业务层
 * @author zhuss
 * @2016年1月7日 下午4:22:58
 */
@Service
public class OrderAO {

	private static final Logger log = LoggerFactory.getLogger(OrderAO.class);
	
	@Autowired
	private OrderInfoProxy orderInfoProxy;
	@Autowired
	private PromoterInfoProxy promoterInfoProxy;
	@Autowired
	private OrderProxy orderProxy;
	@Autowired
	private CartProxy cartProxy;
	@Autowired
	private PaymentGatewayProxy paymentGatewayProxy;
	@Autowired
	private AppPaymentProxy appPaymentProxy;
	@Autowired
	private OrderRedeemItemProxy orderRedeemItemProxy;

	@Autowired
	private OrderHelper orderHelper;
	
	@Autowired
	private ChannelInfoProxy channelInfoProxy;
	@Autowired
	private PromoterAO promoterAO;
	
	/**
	 * 获取提交订单页面的信息
	 * @return
	 */
	public MResultVO<SubmitOrderInfoVO> getSubmitOrderInfo(QueryOrder order){
		try{
			List<Long> couponIdList = new ArrayList<Long>();
			if(StringUtil.isNotBlank(order.getCid()))couponIdList.add(StringUtil.getLongByStr(order.getCid()));
			String token = order.getToken();
			if(StringUtil.isNotBlank(order.getUuid())){
				token = order.getUuid();
			}
			//优惠券集合
			if(CollectionUtils.isNotEmpty(order.getCidlist())){
				for(String cid : order.getCidlist()){
					couponIdList.add(StringUtil.getLongByStr(cid));
				}
			}
			ResultInfo<OrderInitDto> orderInitDto = orderProxy.preCreateOrder(order.getUserid(), token,couponIdList,PlatformEnum.getCodeByName(order.getApptype()),Boolean.TRUE.toString().equals(order.getUsedPointSign()),promoterAO.getPromoterIdByChannelCode(order.getChannelcode()),order.getPointType());
			if(orderInitDto.success){
				SubmitOrderInfoVO vo = OrderInfoConvert.convertSubmitOrderInfo(orderInitDto.getData(),order);
				return new MResultVO<>(MResultInfo.SUCCESS,vo); 
			}
			log.error("[调用Service接口 - 获取提交订单页面的信息(calcItemPrice4Sea) FAILED] ={}",orderInitDto.getMsg().toString());
			return new MResultVO<>(orderInitDto.getMsg().getMessage());
		}catch(MobileException ex){
			log.error("[API接口 - 获取提交订单页面的信息 MobileException] = {}",ex.getMessage());
			return new MResultVO<>(ex);
		}catch(Exception ex){
			log.error("[API接口 - 获取提交订单页面的信息 Exception] = {}",ex);
			return new MResultVO<>(MResultInfo.SYSTEM_ERROR);
		}
	}
	
	/**
	 * 提交订单
	 * @param order
	 * @return
	 */
	public MResultVO<List<PayOrderLineVO>> submit(QueryOrder order,TokenCacheTO usr){
		try{
			OrderInitDto orderInitDto = new OrderInitDto();
			convertSumitInfo(orderInitDto,order);
			orderInitDto.setOrderSource(RequestHelper.getPlatformByName(order.getApptype()).code);
			ResultInfo<List<PaymentInfo>> resultInfo = orderProxy.createOrder(orderInitDto);
			if(resultInfo.isSuccess()){
				List<PaymentInfo> porders = resultInfo.getData();
				return new MResultVO<>(MResultInfo.SUBMIT_SUCCESS,OrderConvert.convertUnPayOrders(porders,order)); 
			}
			log.error("[调用Service接口 - 提交订单(orderSubmit) FAILED] ={}",resultInfo.getMsg().toString());
			Integer code = resultInfo.getMsg() == null? -1 :resultInfo.getMsg().getCode() == null ?-1:resultInfo.getMsg().getCode() == 0 ?-1 :resultInfo.getMsg().getCode();
			return new MResultVO<>(String.valueOf(code),resultInfo.getMsg().getMessage());
		}catch(MobileException ex){
			log.error("[API接口 - 提交订单 MobileException] = {}",ex.getMessage());
			return new MResultVO<>(ex);
		}catch(Exception ex){
			log.error("[API接口 - 提交订单 Exception] = {}",ex);
			return new MResultVO<>(MResultInfo.SUBMIT_FAILED);
		}
	}
	
	/**
	 * 合并提交订单
	 * @param order
	 * @return
	 */
	public MResultVO<BasePayVO> mergeSubmit(QueryOrder order,TokenCacheTO usr){
		try{
			OrderInitDto orderInitDto = new OrderInitDto();
			convertSumitInfo(orderInitDto,order);
			orderInitDto.setOrderSource(RequestHelper.getPlatformByName(order.getApptype()).code);
			log.info("ORDER_MERGE_SUBMIT_PARAM={}", JsonUtil.convertObjToStr(orderInitDto));
			ResultInfo<List<PaymentInfo>> resultInfo = orderProxy.createOrder(orderInitDto);
			if(resultInfo.isSuccess()){
				List<PaymentInfo> paymentInfoList = resultInfo.getData();
				log.info("==================支付信息订单个数=================",paymentInfoList.size());

				if(CollectionUtils.isNotEmpty(paymentInfoList)){

					PaymentInfo paymentInfo = paymentInfoList.get(0);
					AppPaymentCallDto apc = new AppPaymentCallDto();
					apc.setGateway(order.getPayway());
					boolean isSdk = RequestHelper.isAPP(order.getApptype());
					apc.setSdk(isSdk);
					apc.setPaymentId(paymentInfo.getPaymentId());
					apc.setUserId(order.getUserid().toString());
					//微信浏览器的微信支付
					if((StringUtil.equals(order.getPayway(), PaymentConstant.GATEWAY_TYPE.WEIXIN.code)||
							StringUtil.equals(order.getPayway(), PaymentConstant.GATEWAY_TYPE.WEIXIN_EXTERNAL.code))&&!isSdk){
						Map<String,Object> params = new HashMap<>();
						params.put("openid", order.getOpenid());
						apc.setParams(params);
					}

					//订单中有海淘自营，则切换weixin到weixinExternal
					if( StringUtils.equals(order.getPayway(),PaymentConstant.GATEWAY_TYPE.WEIXIN.code)){
						boolean hasCOMMON_SEA= orderHelper.hasCOMMON_SEA(paymentInfo);
						if (hasCOMMON_SEA){
							order.setPayway(PaymentConstant.GATEWAY_TYPE.WEIXIN_EXTERNAL.code);
						}
					}
					//第三方商城支付
					if (StringUtil.isNotBlank(order.getChannelcode())) {
						Map<String, Object> params =  (apc.getParams() == null) ? (new HashMap<String, Object>()) : apc.getParams();
						params.put("channelCode", order.getChannelcode());
						apc.setParams(params);
					}

					paymentInfo.setGatewayCode(order.getPayway());
					AppPayData payData = appPaymentProxy.getAppData(apc);
					if(isSdk)return new MResultVO<>(MResultInfo.SUCCESS,PayConvert.convertAppPayInfo(payData, paymentInfo,order.getApptype()));
					else return new MResultVO<>(MResultInfo.SUCCESS,PayConvert.convertWapPayInfo(payData, paymentInfo));
				}
			}
			log.error("[调用Service接口 - 提交订单(orderSubmit) FAILED] ={}",resultInfo.getMsg().toString());
			Integer code = resultInfo.getMsg() == null? -1 :resultInfo.getMsg().getCode() == null ?-1:resultInfo.getMsg().getCode() == 0 ?-1 :resultInfo.getMsg().getCode();
			return new MResultVO<>(String.valueOf(code),resultInfo.getMsg().getMessage());
		}catch(MobileException ex){
			log.error("[API接口 - 提交订单 MobileException] = {}",ex.getMessage());
			return new MResultVO<>(ex);
		}catch(Exception ex){
			log.error("[API接口 - 提交订单 Exception] = {}",ex);
			return new MResultVO<>(MResultInfo.SUBMIT_FAILED);
		}
	}
	
	public void convertSumitInfo(OrderInitDto orderInitDto,QueryOrder order){
		try{
			OrderConvert.convertSumit(orderInitDto,order);
			if(StringUtil.isNotBlank(order.getPayway())){
				PaymentGateway getway = new PaymentGateway();
				getway.setGatewayCode(order.getPayway());
				getway = paymentGatewayProxy.queryUniqueByObject(getway).getData();
				orderInitDto.setPayWay(getway.getGatewayId());
			}
			//渠道商（第三方商城）
			orderInitDto.setChannelCode(order.getChannelcode());
			if ((StringUtil.isBlank(order.getShopMobile()) && StringUtil.isNotBlank(order.getChannelcode()))
				|| "hhb".equalsIgnoreCase(order.getChannelcode())) {
				ResultInfo<ChannelInfo> resultInfo = channelInfoProxy.getChannelInfoByCode(order.getChannelcode());
				if (resultInfo.isSuccess() && resultInfo.getData() != null && resultInfo.getData().getPromoterInfo() != null) {
					orderInitDto.setShopPromoterId(resultInfo.getData().getPromoterInfo().getPromoterId());
				}
			}
			orderInitDto.setShopId(promoterAO.getPromoterIdByChannelCode(order.getChannelcode()));
			orderInitDto.setPointType(order.getPointType());
			orderInitDto.setCheckAuth(!VersionHelper.before130Version(order));
			
		}catch(Exception ex){
			log.error("[API接口 - 封装提交订单入参 Exception] = {}",ex);
			throw new MobileException(MResultInfo.SUBMIT_FAILED);
		}
	}
	
	/**
	 * 获取用户订单列表
	 * @param order
	 * @return
	 */
	public MResultVO<Page<OrderVO>> getOrderList(QueryOrder order){
		try{
			ResultInfo<PageInfo<OrderList4UserDTO>> orders = orderInfoProxy.findOrderList4UserPage(OrderConvert.convertPageOrderQuery(order));
			if(orders.isSuccess()){
				PageInfo<OrderList4UserDTO> pageOrders = orders.getData();
				return new MResultVO<>(MResultInfo.SUCCESS,OrderConvert.convertPageOrderList(pageOrders));
			}
			log.error("[调用Service接口 - 获取订单列表(findOrderList4UserPage) FAILED] ={}",orders.getMsg().toString());
			return new MResultVO<>(orders.getMsg().getMessage());
		}catch(MobileException ex){
			log.error("[API接口 - 获取订单列表 MobileException] = {}",ex.getMessage());
			return new MResultVO<>(ex);
		}catch(Exception ex){
			log.error("[API接口 - 获取订单列表 Exception] = {}",ex);
			return new MResultVO<>(MResultInfo.SYSTEM_ERROR);
		}
	}
	
	/**
	 * 订单详情
	 * @param order
	 * @return
	 */
	public MResultVO<OrderDetailVO> detail(QueryOrder order){
		try{
			ResultInfo<OrderDetails4UserDTO> detail = orderInfoProxy.findOrderDetails4User(order.getUserid(),StringUtil.getLongByStr(order.getOrdercode()));
			if(detail.isSuccess()){
				OrderDetails4UserDTO orderDetail = detail.getData();
				OrderRedeemItem  orderRedeemItem=new OrderRedeemItem();
				orderRedeemItem.setOrderCode(Long.valueOf(order.getOrdercode()));
				ResultInfo<List<OrderRedeemItem>> orderRedeemItemList=orderRedeemItemProxy.queryByObject(orderRedeemItem);
				
				return new MResultVO<>(MResultInfo.SUCCESS,OrderConvert.convertDetail(orderDetail,orderRedeemItemList.getData()));
			}
			log.error("[调用Service接口 - 订单详情(findOrderDetails4User) FAILED] ={}",detail.getMsg().toString());
			return new MResultVO<>(detail.getMsg().getMessage());
		}catch(MobileException ex){
			log.error("[API接口 - 订单详情 MobileException] = {}",ex.getMessage());
			return new MResultVO<>(ex);
		}catch(Exception ex){
			log.error("[API接口 - 订单详情 Exception] = {}",ex);
			return new MResultVO<>(MResultInfo.SYSTEM_ERROR);
		}
	}
	
	
	/**
	 * 取消和删除订单
	 * @param order
	 * @return
	 */
	public MResultVO<MResultInfo> calordel(QueryOrder order,TokenCacheTO usr){
		try{
			if(order.getType().equals(OrderEnum.CalOrDelType.CANCEL.code)){ //取消订单
				MemberInfo memberInfo = new MemberInfo();
				memberInfo.setNickName(usr.getName());
				memberInfo.setId(usr.getUid());
				ResultInfo<Boolean> r = orderInfoProxy.cancelOrderByMember(StringUtil.getLongByStr(order.getOrdercode()), memberInfo);
				if(r.isSuccess()){
					if(r.getData())return new MResultVO<>(MResultInfo.CANCEL_SUCCESS);
					return new MResultVO<>(MResultInfo.CANCEL_FAILED);
				}
				log.error("[调用Service接口 - 取消订单(cancelOrderByMember) FAILED] ={}",r.getMsg().toString());
				return new MResultVO<>(r.getMsg().getMessage());
			}else if(order.getType().equals(OrderEnum.CalOrDelType.DEL.code)){//删除订单
				ResultInfo<Integer> re = orderInfoProxy.deleteByCode(order.getUserid(),StringUtil.getLongByStr(order.getOrdercode()));
				if(re.isSuccess()){
					if(re.getData() > 0)return new MResultVO<>(MResultInfo.DEL_SUCCESS);
					return new MResultVO<>(MResultInfo.DEL_FAILED);
				}
				log.error("[调用Service接口 - 删除订单(deleteByCode)] ={}",re.getMsg().toString());
				return new MResultVO<>(re.getMsg().getMessage());
			}
		}catch(MobileException ex){
			log.error("[API接口 - 取消和删除订单 MobileException] = {}",ex.getMessage());
			return new MResultVO<>(ex);
		}catch(Exception ex){
			log.error("[API接口 - 取消和删除订单 Exception] = {}",ex);
		}
		return new MResultVO<>(MResultInfo.OPERATION_FAILED);
	}
	
	/**
	 * 确认订单(确认收货)
	 * @param order
	 * @return
	 */
	public MResultVO<MResultInfo> confirm(QueryOrder order){
		try{
			OrderReceiveGoodsDTO dto = new OrderReceiveGoodsDTO();
			dto.setSubOrderCode(StringUtil.getLongByStr(order.getOrdercode()));
			ResultInfo<Boolean> res = orderInfoProxy.operateOrderForReceiveGoodsByUser(order.getUserid(),dto);
			if(res.isSuccess()){
				if(res.getData())return new MResultVO<>(MResultInfo.CONFIRE_SUCCESS);
				return new MResultVO<>(MResultInfo.CONFIRE_FAILED);
			}
			log.error("[调用Service接口 - 确认收货(operateOrderForReceiveGoodsByUser) FAILED] ={}",res.getMsg().toString());
			return new MResultVO<>(MResultInfo.CONFIRE_FAILED);
		}catch(MobileException ex){
			log.error("[API接口 - 确认收货 MobileException] = {}",ex.getMessage());
			return new MResultVO<>(ex);
		}catch(Exception ex){
			log.error("[API接口 - 确认收货 Exception] = {}",ex);
			return new MResultVO<>(MResultInfo.SYSTEM_ERROR);
		}
	}
	
	/**
	 * 立即购买
	 * @param order
	 * @return
	 */
	public MResultVO<BuyNowVO> buyNow(QueryOrder order){
		try{
			CartItemInfo cartItemInfo = new CartItemInfo();
			cartItemInfo.setTopicId(StringUtil.getLongByStr(order.getTid()));
			cartItemInfo.setAreaId(StringUtil.getLongByStr(order.getRegcode()));
			cartItemInfo.setSkuCode(order.getSku());
			cartItemInfo.setQuantity(StringUtil.getIntegerByStr(order.getCount()));
			cartItemInfo.setMemberId(order.getUserid());
			cartItemInfo.setIp(order.getIp());
			cartItemInfo.setPlatformId(PlatformEnum.getCodeByName(order.getApptype()));
			cartItemInfo.setGroupId(StringUtil.getLongByStr(order.getGid()));
			cartItemInfo.setSelected(Constant.SELECTED.YES);
			cartItemInfo.setShopId(promoterAO.getPromoterIdByChannelCode(order.getChannelcode()));
			if(StringUtil.isNotBlank(order.getShopMobile()) && !"hhb".equalsIgnoreCase(order.getChannelcode())){
				Map<String,Object> params = new HashMap<String,Object>();
				params.put("mobile", order.getShopMobile());
				PromoterInfo promoterInfo = promoterInfoProxy.queryUniqueByParams(params).getData();
				if(null!=promoterInfo){
					cartItemInfo.setShopPromoterId(promoterInfo.getPromoterId());
				}
			}
			ResultInfo<ShoppingCartDto> result = cartProxy.queryCartByTocken(cartItemInfo);
			//ResultInfo<String> result  = orderInfoProxy.buyNow(order.getUserid() , order.getIp(), RequestHelper.getPlatformByName(order.getApptype()).code,cartLine);
			if(result.isSuccess()){
				String uuid = result.getData().getToken();
				if(StringUtil.isNotBlank(uuid))return new MResultVO<>(MResultInfo.OPERATION_SUCCESS,new BuyNowVO(uuid));
			}
			log.error("[调用Service接口 - 立即购买(buyNow) FAILED] ={}",result.getMsg().toString());
			return new MResultVO<>(result.getMsg().getMessage());
		}catch(MobileException ex){
			log.error("[API接口 - 立即购买 MobileException] = {}",ex.getMessage());
			return new MResultVO<>(ex);
		}catch(Exception ex){
			log.error("[API接口 - 立即购买 Exception] = {}",ex);
			return new MResultVO<>(MResultInfo.SYSTEM_ERROR);
		}
	}
}
