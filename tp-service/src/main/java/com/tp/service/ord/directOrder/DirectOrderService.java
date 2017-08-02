package com.tp.service.ord.directOrder;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.tp.common.vo.DAOConstant;
import com.tp.common.vo.OrderConstant;
import com.tp.common.vo.Constant.AUTHOR_TYPE;
import com.tp.common.vo.OrderConstant.ClearanceStatus;
import com.tp.common.vo.bse.ClearanceChannelsEnum;
import com.tp.dto.common.FailInfo;
import com.tp.dto.common.ResultInfo;
import com.tp.dto.ord.directOrderNB.ProductNBDto;
import com.tp.dto.ord.directOrderNB.RetMessageNBDto;
import com.tp.model.ord.OrderDelivery;
import com.tp.model.ord.OrderItem;
import com.tp.model.ord.SubOrder;
import com.tp.model.prd.ItemSku;
import com.tp.service.ord.IOrderItemService;
import com.tp.service.ord.ISubOrderService;
import com.tp.service.ord.OrderItemService;
import com.tp.service.ord.directOrder.NB.IDirectOrderNBService;
import com.tp.service.prd.IItemSkuService;
import com.tp.service.prd.ItemSkuService;
import com.tp.service.stg.IOutputOrderService;

@Service
public class DirectOrderService implements IDirectOrderService {

	private static final Logger LOG = LoggerFactory.getLogger(DirectOrderService.class);
	
//	private final Integer RETURN_TRUE = 1;
//	private final Integer RETURN_FALSE = 0;
	
	@Autowired
	private ISubOrderService subOrderService;
	@Autowired
	private IDirectOrderNBService directOrderNBService;
	@Autowired
    private IOutputOrderService outputOrderService;
	@Autowired
    private IOrderItemService orderItemService;
	@Autowired
    private IItemSkuService itemSkuService;
//	@Autowired
//	Properties settings;
	
	@Value("#{meta['HWZY_WAREHOUSE']}")
	private Long hwzyWarehouseId = 0l;
	@Value("#{meta['HWZY_ON&OFF']}")
	private String hwzyFlag = "";
	
	/**
	 * 手动推送直邮订单
	 * @param directOrderDto
	 * @return
	 */
	@Override
	public ResultInfo<Boolean> pushDirectOrderToCustoms(SubOrder subOrder){
		ResultInfo<Boolean> resultInfo = new ResultInfo<>();
		if(!hwzyWarehouseId.equals(subOrder.getWarehouseId())){ //不是特定仓库的订单不推送海外直邮
			LOG.error("[{}]订单不是第三方海外直邮的订单，不需要推送", subOrder.getOrderCode());
			return new ResultInfo<>(new FailInfo("订单不是第三方海外直邮的订单，不需要推送"));
		}
		/*-----------------------------------基本信息校验-------------------------------------*/
		if (OrderConstant.ORDER_STATUS.DELIVERY.code != subOrder.getOrderStatus()) {
			LOG.error("[{}]订单不是待发货状态，不需要推送", subOrder.getOrderCode());
			return new ResultInfo<>(new FailInfo("订单不是待发货状态，不需要推送"));
		}
		if (ClearanceStatus.PUT_SUCCESS.code.equals(subOrder.getDirectOrderStatus())) {
			LOG.error("[{}]已推送成功，不需要重复推送", subOrder.getOrderCode());
			return new ResultInfo<>(new FailInfo("订单已推送成功,不需要重复推送"));
		}
		if(hwzyWarehouseId.equals(subOrder.getWarehouseId())){
			List<ItemSku> itemSkuList = getItemSku(subOrder);
			RetMessageNBDto retMessageDto = null;
			if(hwzyFlag.equals("true")){
				retMessageDto = directOrderNBService.pushDirectOrderNB(subOrder,itemSkuList);
			}else{
				retMessageDto = directOrderNBService.pushDirectOrderNBTest(subOrder,itemSkuList);
			}
			
			resultInfo = afterPushToNB(subOrder,retMessageDto);
		}
		return resultInfo;		
	}
	
	/**
	 * 定时任务调推送直邮订单
	 */
	@Override
	public void pushDirectOrderForJob() {
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("sea_channel", ClearanceChannelsEnum.HWZY.id);
		params.put("order_status", OrderConstant.ORDER_STATUS.DELIVERY.code);
		params.put("warehouse_id", hwzyWarehouseId);//只查需要推送订单的仓库id
		params.put("direct_order_status", "0");//只查需要推送订单的仓库id
//		List<Integer> statusList = new ArrayList<Integer>();
//		statusList.add(0);
//		statusList.add(2);
//		List<DAOConstant.WHERE_ENTRY> whereList = new ArrayList<DAOConstant.WHERE_ENTRY>();
//      whereList.add(new DAOConstant.WHERE_ENTRY("direct_order_status", DAOConstant.MYBATIS_SPECIAL_STRING.INLIST,statusList));
//      params.put(DAOConstant.MYBATIS_SPECIAL_STRING.WHERE.name(), whereList);
        
		List<SubOrder> subOrderList = subOrderService.queryByParam(params);
		for(SubOrder subOrder:subOrderList){
			try{
				List<ItemSku> itemSkuList = getItemSku(subOrder);
				RetMessageNBDto retMessageDto = null;
				if(hwzyFlag.equals("true")){
					retMessageDto = directOrderNBService.pushDirectOrderNB(subOrder,itemSkuList);
				}else{
					retMessageDto = directOrderNBService.pushDirectOrderNBTest(subOrder,itemSkuList);
				}
				ResultInfo<Boolean> resultInfo = afterPushToNB(subOrder,retMessageDto);
			}catch(Exception e){
				e.printStackTrace();
				LOG.error("推送海外直邮订单异常:", e.getMessage());
			}
		}
	}
	
	/**
	 * 获取运单信息
	 */
	@Override
	public void searchDirectOrderForJob(){
		Integer status = 1;
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("sea_channel", ClearanceChannelsEnum.HWZY.id);
		params.put("order_status", OrderConstant.ORDER_STATUS.DELIVERY.code);
		params.put("direct_order_status", 1);//只查推送成功的订单
//		params.put("warehouse_id", hwzyWarehouseId);//
		List<SubOrder> subOrderList = subOrderService.queryByParam(params);
		if(null != subOrderList && subOrderList.size()>0){
			for(SubOrder subOrder:subOrderList){
				if(hwzyWarehouseId.equals(subOrder.getWarehouseId())){
					RetMessageNBDto retMessage = directOrderNBService.getOrderMessageNB(String.valueOf(subOrder.getOrderCode()));
					if("LowStock".equals(retMessage.getOrderStatus())){//接口返回状态为  库存不足
//						status = 7;
					}else if("Delivered".equals(retMessage.getOrderStatus())){//接口返回状态为  已发货
//						status = 5;
//						subOrder.setDirectOrderStatus(status);
//						subOrderService.updateNotNullById(subOrder);
						try {
							String logistics = "";
							if(OrderConstant.directOrderLogistics.YTO.code.equals(retMessage.getLogistics())){
								logistics = OrderConstant.directOrderLogistics.YTO.desc;
							}else if(OrderConstant.directOrderLogistics.ZTO.code.equals(retMessage.getLogistics())){
								logistics = OrderConstant.directOrderLogistics.ZTO.desc;
							}else if(OrderConstant.directOrderLogistics.STO.code.equals(retMessage.getLogistics())){
								logistics = OrderConstant.directOrderLogistics.STO.desc;
							}else if(OrderConstant.directOrderLogistics.YUNDA.code.equals(retMessage.getLogistics())){
								logistics = OrderConstant.directOrderLogistics.YUNDA.desc;
							}else if(OrderConstant.directOrderLogistics.SF.code.equals(retMessage.getLogistics())){
								logistics = OrderConstant.directOrderLogistics.SF.desc;
							}else if(OrderConstant.directOrderLogistics.EMS.code.equals(retMessage.getLogistics())){
								logistics = OrderConstant.directOrderLogistics.EMS.desc;
							}
							ResultInfo<Boolean> resultInfo = outputOrderService.exWarehouseService(createOrderDelivery(subOrder,logistics,logistics().get(retMessage.getLogistics()),retMessage.getLogisticsNumber()));
							if (!resultInfo.isSuccess()){
								LOG.error("海外直邮订单发货失败:", resultInfo.getMsg().getMessage());
							}
						} catch (Exception e) {
							LOG.error("海外直邮订单发货异常:", e.getMessage());
							e.printStackTrace();
						}
					}else if("Canceled".equals(retMessage.getOrderStatus())){//接口返回状态为  已取消
//						status = 0;
//						subOrder.setOrderStatus(status);
//						subOrderService.updateNotNullById(subOrder);
					}
				}
			}
		}
	}
	
	private ResultInfo<Boolean> afterPushToNB(SubOrder subOrder,RetMessageNBDto retMessageDto){
		ResultInfo<Boolean> resultInfo = new ResultInfo<>();
		if ("F".equals(retMessageDto.getResult())){
			LOG.error("推送海外直邮订单失败:", retMessageDto.getResultMsg());
			subOrder.setDirectOrderStatus(2);
			resultInfo = new ResultInfo<>(new FailInfo("推送海外直邮订单失败:"+retMessageDto.getResultMsg()));
		}else{
			LOG.error("推送海外直邮订单成功！");
			subOrder.setDirectOrderStatus(1);
			resultInfo = new ResultInfo<>(Boolean.TRUE);
		}
		subOrderService.updateNotNullById(subOrder);
		
		return resultInfo;
	}
	
	private OrderDelivery createOrderDelivery(SubOrder subOrder,String logisticsCode,String logisticsName,String logisticsNumber){
		OrderDelivery orderDelivery = new OrderDelivery();
		orderDelivery.setCompanyId(logisticsCode);
		orderDelivery.setCompanyName(logisticsName);
		orderDelivery.setOrderCode(subOrder.getOrderCode());
		orderDelivery.setPackageNo(logisticsNumber);	// 物流单号  
		orderDelivery.setCreateUser(AUTHOR_TYPE.SYSTEM);	// 设置处理人
		orderDelivery.setDeliveryTime(new Date());			// 设置发货时间
		return orderDelivery;
	}
	
	//海外直邮订单状态获取，快递公司信息
	private Map<String,String> logistics(){
		Map<String,String> logisticsMap = new HashMap<String,String>();
		logisticsMap.put("YTO","圆通");
		logisticsMap.put("ZTO","中通");
		logisticsMap.put("STO","申通");
		logisticsMap.put("YUNDA","韵达");
		logisticsMap.put("SF","顺丰");
		logisticsMap.put("EMS","邮政速递");
		return logisticsMap;
	}
	
	/**
	 * 根据订单信息获取订单中的商品信息
	 */
	private List<ItemSku> getItemSku(SubOrder subOrder) {
		Long orderCode = subOrder.getOrderCode();
		Map<String, Object> params = new HashMap<String,Object>();
		params.put("order_code", orderCode);
		List<OrderItem> orderItemList = orderItemService.queryByParam(params);
		List<ItemSku> itemSkuList = new ArrayList<ItemSku>();
		for(OrderItem orderItem:orderItemList){
			params.clear();
			params.put("sku", orderItem.getSkuCode());
			ItemSku itemSku = itemSkuService.queryUniqueByParams(params);
			itemSkuList.add(itemSku);
		}
		
		return itemSkuList;
	}
}
