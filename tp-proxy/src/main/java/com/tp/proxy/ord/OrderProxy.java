package com.tp.proxy.ord;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.TimeUnit;
import java.util.function.Predicate;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.util.ExceptionUtils;
import com.tp.common.vo.Constant;
import com.tp.common.vo.Constant.DEFAULTED;
import com.tp.common.vo.OrderConstant;
import com.tp.common.vo.Constant.SPLIT_SIGN;
import com.tp.common.vo.DAOConstant.MYBATIS_SPECIAL_STRING;
import com.tp.common.vo.StorageConstant.App;
import com.tp.dto.common.FailInfo;
import com.tp.dto.common.ResultInfo;
import com.tp.dto.mmp.OrderCouponDTO;
import com.tp.dto.ord.HhbShopOrderInfoDTO;
import com.tp.dto.ord.OrderDto;
import com.tp.dto.ord.OrderInitDto;
import com.tp.dto.ord.PreOrderDto;
import com.tp.dto.ord.ShoppingCartDto;
import com.tp.dto.ord.directOrderNB.ProductNBDto;
import com.tp.dto.ord.directOrderNB.RetMessageNBDto;
import com.tp.dto.stg.OccupyInventoryDto;
import com.tp.enums.common.PlatformEnum;
import com.tp.model.dss.ChannelInfo;
import com.tp.model.mem.MemberInfo;
import com.tp.model.ord.CartItemInfo;
import com.tp.model.ord.OrderItem;
import com.tp.model.ord.SubOrder;
import com.tp.model.pay.PaymentInfo;
import com.tp.model.prd.ItemSku;
import com.tp.proxy.dss.ChannelInfoProxy;
import com.tp.proxy.ord.assemble.IOrderAssembleProxy;
import com.tp.proxy.ord.compute.OrderAmountProxy;
import com.tp.proxy.ord.validate.OrderValidateProxy;
import com.tp.proxy.pay.PaymentInfoProxy;
import com.tp.service.mem.IHhbgroupService;
import com.tp.service.mem.IMemberInfoService;
import com.tp.service.mmp.ICouponUserService;
import com.tp.service.ord.IOrderService;
import com.tp.service.ord.ISubOrderService;
import com.tp.service.ord.directOrder.NB.IDirectOrderNBService;
import com.tp.service.prd.IItemSkuService;
import com.tp.service.stg.IInventoryOperService;
import com.tp.util.ThreadUtil;

import net.sf.json.JSONArray;

/**
 * 订单结算下单
 * @author szy
 *
 */
@Service
public class OrderProxy {
	
	private static final Logger logger = LoggerFactory.getLogger(OrderProxy.class);
	
	@Autowired
	private OrderValidateProxy orderValidateProxy;
	@Autowired
	private ICouponUserService couponUserService;
	@Autowired
	private IMemberInfoService memberInfoService;
	@Autowired
	private IInventoryOperService inventoryOperService;
	@Autowired
	private ISubOrderService subOrderService;
	@Autowired
	private IOrderService orderService;
	@Autowired
	private CartProxy cartProxy;
	@Autowired
	private OrderAmountProxy orderAmountProxy;
	@Autowired
	private CartItemInfoProxy cartItemInfoProxy;
	@Autowired
	private IOrderAssembleProxy orderAssembleProxy;
	@Autowired
	private PaymentInfoProxy paymentInfoProxy;
	@Autowired
	private CreateOrderAfterProxy createOrderAfterProxy;
	@Autowired
	private IItemSkuService itemSkuService;
	@Autowired
	private IDirectOrderNBService directOrderNBService;
	@Autowired
	ChannelInfoProxy channelInfoProxy;

	
	@Autowired
	IHhbgroupService  hhbgroupService; 
	
	@Autowired
	Properties settings;
	/** 订单结算
	 * 1.购物车选中商品
	 * 2.收货信息
	 * 3.优惠券
	 * 4.支付方式
	 * 5.应付总额
	 * @param memberId
	 * @param token
	 * @return
	 */
	public ResultInfo<OrderInitDto> preCreateOrder(Long memberId,String token,Integer orderSource,Boolean usedPointSign,Long shopId,String pointType){
		ResultInfo<OrderInitDto> shoppingCartDto = initOrderInitDto(memberId,token,null,orderSource,usedPointSign,shopId,pointType);
		return shoppingCartDto;
	}
	public ResultInfo<OrderInitDto> preCreateOrder(Long memberId,String token,List<Long> couponIdList,Integer orderSource,Boolean usedPointSign,Long shopId,String pointType){
		if(CollectionUtils.isEmpty(couponIdList)){
			return preCreateOrder(memberId,token,orderSource,usedPointSign,shopId,pointType);
		}
		ResultInfo<OrderInitDto>  orderInitDto= initOrderInitDto(memberId,token,couponIdList,orderSource,usedPointSign,shopId,pointType);
		return orderInitDto;
	}
	
	/**
	 * 下单
     * 1.验证传入参数
	 * 2.准备要创建订单的数据
	 * 3.验证
	 * 4.计算订单、子订单金额
	 * 5.拆分数据
	 * 6.组装订单信息
	 * 7.减库存
	 * 8.更新优惠券状态
	 * 9.订单信息插入数据库
	 * 10.推送支付信息
	 * 13.发送下单成功消息，异步拆分运费及优惠券等其它信息
	 * 11.清除购物车中数据
	 * 12.异常时返还库存及优惠券
	 * 
	 * @param orderInitDto
	 * @return
	 */
	public ResultInfo<List<PaymentInfo>> createOrder(OrderInitDto orderInitDto){
		OrderInitDto tempOrder = new OrderInitDto();
		ShoppingCartDto shoppingCartDto =  cartProxy.getCartDto(orderInitDto.getMemberId(), orderInitDto.getToken(),orderInitDto.getShopId());
		synItem(shoppingCartDto);
		shoppingCartDto = deleteUnselectedItem(shoppingCartDto);
		BeanUtils.copyProperties(orderInitDto,tempOrder);
		BeanUtils.copyProperties(shoppingCartDto,orderInitDto);
		copyTempOrder(tempOrder,orderInitDto);
		/** 对接意大利查询库存接口**/
		List<Map<String, String>> list = new ArrayList<Map<String, String>>(); 
		List<CartItemInfo> cartItemInfoList = shoppingCartDto.getCartItemInfoList();
		for(CartItemInfo cartItemInfo :cartItemInfoList){
			if(cartItemInfo.getSelected() == 1){
				String sku = cartItemInfo.getSkuCode();
				Integer quantity = cartItemInfo.getQuantity();
				Map<String, String> map = new HashMap<String, String>();
				map.put("sku", sku);
				map.put("quantity", String.valueOf(quantity));
				list.add(map);
			}
		}
		logger.info("============================调查询意大利库存接口");
		FailInfo invFailInfo = validateInventoryOL(list);//调查询意大利库存接口
		if(null!=invFailInfo){
			return new ResultInfo<>(invFailInfo);
		}
		/** 对接意大利查询库存接口 end **/
		FailInfo failInfo = orderValidateProxy.validateParameters(orderInitDto);
		if(null!=failInfo){
			return new ResultInfo<>(failInfo);
		}
		orderInitDto = getOrderInitInfo(orderInitDto);
		ResultInfo<OrderInitDto> orderInitDtoResultInfo = initOrderInitDto(orderInitDto);
		if(!orderInitDtoResultInfo.success){
			return new ResultInfo<>(orderInitDtoResultInfo.getMsg());
		}
		orderInitDto = initOrderFirstMinus(orderInitDto);
		failInfo = orderValidateProxy.validateFastOrderDeliveryAddress(orderInitDto,null);
		failInfo = orderValidateProxy.validate(orderInitDto, failInfo);
		failInfo = orderValidateProxy.validateCouponListLock(orderInitDto, failInfo);
		if(null!=failInfo){
			return new ResultInfo<>(failInfo);
		}
		orderInitDto = orderAmountProxy.computeAmount(orderInitDto);
		OrderDto orderDto = orderAssembleProxy.assembleOrder(orderInitDto);
		failInfo = minusStock(orderDto);
		if(null!=failInfo){
			return new ResultInfo<>(failInfo);
		}
		try{
			//第三方商城积分接入  扣除积分--start
			String chanelCode=orderInitDto.getChannelCode();
			ChannelInfo chanelInfo=channelInfoProxy.getChannelInfoByCode(chanelCode).getData();
			if(chanelInfo!=null && "1".equals(chanelInfo.getIsUsedPoint())){//是否使用自己商城的积分 并发送订单信息
 			    String openId=memberInfoService.getMemberInfoByMobile(orderInitDto.getMemberInfo().getMobile()).getTpin();
				HhbShopOrderInfoDTO  hhbOrderInfo=new HhbShopOrderInfoDTO();
				hhbOrderInfo.setCode(String.valueOf(orderDto.getOrderInfo().getParentOrderCode()));//订单编号
				hhbOrderInfo.setCash(orderDto.getOrderInfo().getTotal());//应付金额
				hhbOrderInfo.setIntegral(0D);//使用积分数
				hhbOrderInfo.setBalance(Double.valueOf(orderDto.getOrderInfo().getTotalPoint()));//惠币支付
				hhbOrderInfo.setTotalMoney(orderDto.getOrderInfo().getOriginalTotal());
				hhbOrderInfo.setType("1");//购买商品
				hhbOrderInfo.setReturnMoney(orderInitDto.getReturnMoney());//返回佣金总金额
				hhbOrderInfo.setOpenId(openId);
				Map<String,String> sendResult=memberInfoService.sendOrderToThirdShop(chanelCode, hhbOrderInfo);//发送订单到第三方商城
				if(!"true".equals(MapUtils.getString(sendResult, "success"))){//调用失败
					failInfo=new FailInfo(MapUtils.getString(sendResult, "msg"));
					return new ResultInfo<>(failInfo);
				}
			}
			//第三方商城积分接入 扣除积分--end
			orderDto = orderService.insertOrder(orderDto);
			ResultInfo<List<PaymentInfo>> paymentInfoList = paymentInfoProxy.batchInsert(orderDto.getPaymentInfoList());
			createOrderAfterProxy.excute(orderDto, orderInitDto);
			return paymentInfoList;
		}catch(Throwable throwable){
			failInfo = ExceptionUtils.print(new FailInfo(throwable), logger,orderDto);
			List<Long> orderCodeList = new ArrayList<Long>();
			for(SubOrder subOrder:orderDto.getSubOrderList()){
				orderCodeList.add(subOrder.getOrderCode());
			}
			inventoryRollback(orderCodeList,2);
			return new ResultInfo<>(failInfo);
		}
	}	
	
	/**
	 * 减库存
	 * @param orderDto
	 * @return
	 */
	public FailInfo minusStock(OrderDto orderDto){
		List<OccupyInventoryDto> occupyInventoryList = new ArrayList<OccupyInventoryDto>();
		for(OrderItem orderItem:orderDto.getOrderItemList()){
			occupyInventoryList.add(setReduceStockLists(orderItem));
		}
		Map<String, ResultInfo<String>> resultMessageMap = inventoryOperService.batchOccupyInventory(occupyInventoryList);
		if(MapUtils.isNotEmpty(resultMessageMap)){
		Iterator<String> storageMessageKit = resultMessageMap.keySet().iterator();
			while (storageMessageKit.hasNext()) {
				String storageMessageKey = (String) storageMessageKit.next();
				ResultInfo<String> tempMessage = resultMessageMap.get(storageMessageKey);
				if(null!=tempMessage.getMsg()){
					return tempMessage.getMsg();
				}
			}
		}
		return null;
	}
	
	/**
	 * 获取减库存参数
	 * @param orderItem
	 * @return
	 */
	private OccupyInventoryDto setReduceStockLists(OrderItem orderItem) {
		OccupyInventoryDto occupyInventoryDto = new OccupyInventoryDto();
		occupyInventoryDto.setApp(App.PROMOTION);// 请求类型
		occupyInventoryDto.setBizId(orderItem.getTopicId().toString());// 设置业务ID,对应促销ID
		occupyInventoryDto.setInventory(orderItem.getQuantity());
		occupyInventoryDto.setSku(orderItem.getSkuCode());
		occupyInventoryDto.setOrderCode(orderItem.getOrderCode());
		occupyInventoryDto.setWarehouseId(orderItem.getWarehouseId());
		occupyInventoryDto.setPreOccupy(DEFAULTED.YES.equals(orderItem.getTopicInventoryFlag())); //是否预占库存
		return occupyInventoryDto;

	} 
	
	/**
	 * 库存回滚
	 * @param orderCode
	 */
	private void inventoryRollback(final List<Long> subOrderCodeList,Integer interval) {
		Runnable runnable = new Runnable(){
			@Override
			public void run() {
				try{
					inventoryOperService.batchUnoccupyInventory(subOrderCodeList);// 库存回滚接口
				}catch(Throwable throwable){
					logger.error(" 库存回滚接口调用失败,入参:{},错误信息:{}", JSONArray.fromObject(subOrderCodeList).toString(), throwable);
					if(interval<=128){
						try {
							TimeUnit.MINUTES.sleep(interval);
						} catch (InterruptedException e) {
						}
						inventoryRollback(subOrderCodeList,interval*2);
					}
				}
			}
			
		};
		ThreadUtil.excAsync(runnable,false);
	}
	
	private OrderInitDto getOrderInitInfo(OrderInitDto orderInitDto) {
		List<OrderCouponDTO> orderCouponList = couponUserService.queryCouponUserByIds(orderInitDto.getCouponIds());
		orderInitDto.setOrderCouponList(orderCouponList);
		MemberInfo memberInfo = memberInfoService.queryById(orderInitDto.getMemberId());
		orderInitDto.setMemberAccount(memberInfo.getNickName());
		orderInitDto.setMemberInfo(memberInfo);
		// 0617 dss
		orderInitDto.setPromoterId(memberInfo.getPromoterId());
		orderInitDto.setScanPromoterId( memberInfo.getScanPromoterId() );
		if(orderInitDto.getShopPromoterId()==null){
			orderInitDto.setShopPromoterId(memberInfo.getShopPromoterId());
		} 
		orderInitDto.setPointType(orderInitDto.getPointType());
		return orderInitDto;
	}
	
	private ResultInfo<OrderInitDto> initOrderInitDto(OrderInitDto orderInitDto){
		return initOrderInitDto(orderInitDto.getMemberId(),orderInitDto.getToken(),orderInitDto.getCouponIds(),orderInitDto.getOrderSource(),orderInitDto.getUsedPointSign(),orderInitDto.getShopId(),orderInitDto.getPointType());
	}
	
	private ResultInfo<OrderInitDto> initOrderInitDto(Long memberId,String token,List<Long> couponIdList,Integer platformType,Boolean usedPointSign,Long shopId,String pointType){
		OrderInitDto orderInitDto = new OrderInitDto();
		ShoppingCartDto shoppingCartDto = cartProxy.getCartDto(memberId, token,shopId);
		if(CollectionUtils.isEmpty(shoppingCartDto.getPreSubOrderList())){
			return new ResultInfo<OrderInitDto>(new FailInfo("购物车里没有有效商品"));
		}
		synItem(shoppingCartDto);
		shoppingCartDto = cartItemInfoProxy.initCart(shoppingCartDto);
		shoppingCartDto = deleteUnselectedItem(shoppingCartDto);
		cartProxy.setCacheCart(shoppingCartDto);
		BeanUtils.copyProperties(shoppingCartDto, orderInitDto);
		orderInitDto.setMemberId(memberId);
		orderInitDto.setToken(token);
		orderInitDto.setCouponIds(couponIdList);
		orderInitDto.setOrderSource(platformType);
		orderInitDto.setPlatformType(platformType);
		orderInitDto.setUsedPointSign(usedPointSign);
		orderInitDto.setPointType(pointType);
		orderInitDto.setChannelCode(pointType);
		
		FailInfo failInfo = orderValidateProxy.validate(orderInitDto,null);
		if(failInfo!=null){
			return new ResultInfo<OrderInitDto>(failInfo);
		}
		orderInitDto = initOrderFirstMinus(orderInitDto);
		orderInitDto = orderAmountProxy.computeAmount(orderInitDto);
		return new ResultInfo<OrderInitDto>(orderInitDto);
	}
	
	/**
     * 首次下单减
     * @param seaOrderItemDTO
     * @return
     */
	public OrderInitDto initOrderFirstMinus(OrderInitDto orderInitDto){
		if(orderInitDto.getOrderSource()!=null && (PlatformEnum.IOS.code == orderInitDto.getOrderSource() || PlatformEnum.ANDROID.code==orderInitDto.getOrderSource())){
    		Map<String,Object> params = new HashMap<String,Object>();
        	params.put("memberId", orderInitDto.getMemberId());
        	params.put(MYBATIS_SPECIAL_STRING.COLUMNS.name(), " source in ("+PlatformEnum.IOS.code+SPLIT_SIGN.COMMA+PlatformEnum.ANDROID.code+") and order_status !="+OrderConstant.ORDER_STATUS.CANCEL.getCode());
        	Integer count = subOrderService.queryByParamCount(params);
        	if(count==0){
        		orderInitDto.setFirstMinus(Boolean.TRUE);
        	}
    	}
		return orderInitDto;
	}
	
	/**
	 * 同步子订单中商品项的选中状态
	 * @param shoppingCartDto
	 * @return
	 */
	public ShoppingCartDto synItem(ShoppingCartDto shoppingCartDto){
		for(PreOrderDto subOrder:shoppingCartDto.getPreSubOrderList()){
			for(OrderItem orderItem:subOrder.getOrderItemList()){
				for(CartItemInfo cartItemInfo:shoppingCartDto.getCartItemInfoList()){
					if(orderItem.getSkuCode().equals(cartItemInfo.getSkuCode())
					&& ((orderItem.getTopicId()==null && cartItemInfo.getTopicId()==null)
					    ||(orderItem.getTopicId()!=null && cartItemInfo.getTopicId()!=null && orderItem.getTopicId().equals(cartItemInfo.getTopicId())))){
						cartItemInfo.setSelected(orderItem.getSelected());
						cartItemInfo.setQuantity(orderItem.getQuantity());
					}
				}
			}
		}
		return shoppingCartDto;
	}
	
	/**
	 * 删除未选中的商品项
	 * @return
	 */
	public ShoppingCartDto deleteUnselectedItem(ShoppingCartDto shoppingCartDto){
		List<PreOrderDto> subOrderList = shoppingCartDto.getPreSubOrderList();
		for(int i=0;i<subOrderList.size();i++){
			PreOrderDto subOrder = subOrderList.get(i);
			subOrder.getOrderItemList().removeIf(new Predicate<OrderItem>(){
				public boolean test(OrderItem orderItem) {
					return !Constant.SELECTED.YES.equals(orderItem.getSelected());
				}
			});
			if(CollectionUtils.isEmpty(subOrder.getOrderItemList())){
				subOrderList.remove(i);
				i--;
			}
		}
		List<PreOrderDto> newSubOrderList =new ArrayList<PreOrderDto>();
		for(int i=0;i<subOrderList.size();i++){
			if(null != subOrderList.get(i)){
				newSubOrderList.add(subOrderList.get(i));
			}
		}
		shoppingCartDto.setPreSubOrderList(newSubOrderList);
		List<CartItemInfo> newCartItemInfoList = new ArrayList<CartItemInfo>();
		for(CartItemInfo cartItemInfo:shoppingCartDto.getCartItemInfoList()){
			if(cartItemInfo.getSelected() ==1 ){
				newCartItemInfoList.add(cartItemInfo);
			}
		}
		shoppingCartDto.setCartItemInfoList(newCartItemInfoList);
		return shoppingCartDto;
	}
	
	private void copyTempOrder(final OrderInitDto tempOrder,final OrderInitDto orderInitDto){
		orderInitDto.setConsigneeId(tempOrder.getConsigneeId());
		orderInitDto.setIp(tempOrder.getIp());
		orderInitDto.setOrderSource(tempOrder.getOrderSource());
		orderInitDto.setCouponIds(tempOrder.getCouponIds());
		orderInitDto.setGroupId(tempOrder.getGroupId());
		orderInitDto.setShopPromoterId(tempOrder.getShopPromoterId());
		orderInitDto.setChannelCode(tempOrder.getChannelCode());
		orderInitDto.setUuid(tempOrder.getUuid());
		orderInitDto.setTpin(tempOrder.getTpin());
		orderInitDto.setUsedPointSign(tempOrder.getUsedPointSign());
	}
	
	/**
	 * 调意大利接口查询库存
	 * @return
	 */
	private FailInfo validateInventoryOL(List<Map<String, String>> list){
		Map<String,Object> params = new HashMap<String,Object>();
		List<String> articleCodeList = new ArrayList<String>();
		for(Map<String,String> map:list){
			articleCodeList.clear();
			params.clear();
			params.put("sku", map.get("sku"));
			ItemSku itemSku = itemSkuService.queryUniqueByParams(params);
			logger.info("=============================================itemSku.getSpId()  ： "+itemSku.getSpId().toString());
			logger.info("=============================================settings.getProperty('HWZY_SPID') :"+settings.getProperty("HWZY_SPID"));
			logger.info("=============================================if() :"+String.valueOf(settings.getProperty("HWZY_SPID").equals(String.valueOf(itemSku.getSpId()))));
			if(settings.getProperty("HWZY_SPID").equals(String.valueOf(itemSku.getSpId()))){
				articleCodeList.add(itemSku.getGoodsCode());
				
				RetMessageNBDto retMessageNBDto = null;
				if(settings.getProperty("HWZY_ON&OFF").equals("true")){
					retMessageNBDto = directOrderNBService.getProductStoreNB(articleCodeList);
				}else{
					retMessageNBDto = directOrderNBService.getProductStoreNBTest(articleCodeList);
				}
				if("F".equals(retMessageNBDto.getResult())){
//					return new FailInfo(retMessageNBDto.getResultMsg());
					logger.info("================================"+retMessageNBDto.getResultMsg()+"==================================");
					return new FailInfo(itemSku.getDetailName()+"的库存不足！");
//					return new FailInfo("sku为"+map.get("sku") +"的商品库存不足！");
				}
				if(Integer.valueOf(map.get("quantity")) > retMessageNBDto.getProducts().get(0).getStoreNumber()){
					return new FailInfo(itemSku.getDetailName()+"的库存不足！");
//					return new FailInfo("sku为"+map.get("sku") +"的商品库存不足！");
				}
			}
		}
		return null;
	}
}
