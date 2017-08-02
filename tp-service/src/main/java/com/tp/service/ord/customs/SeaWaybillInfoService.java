/**
 * 
 */
package com.tp.service.ord.customs;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.vo.OrderConstant.OrderType;
import com.tp.common.vo.OrderConstant.PutCustomsStatus;
import com.tp.common.vo.OrderConstant.PutCustomsType;
import com.tp.common.vo.bse.ClearanceChannelsEnum;
import com.tp.common.vo.ord.LogTypeConstant;
import com.tp.common.vo.wms.WmsConstant.ExpressCompany;
import com.tp.common.vo.wms.WmsWaybillConstant.WaybillType;
import com.tp.dto.common.FailInfo;
import com.tp.dto.common.ResultInfo;
import com.tp.dto.wms.logistics.WaybillInfoDto;
import com.tp.dto.wms.logistics.WaybillItemInfoDto;
import com.tp.model.ord.CustomsClearanceLog;
import com.tp.model.ord.OrderConsignee;
import com.tp.model.ord.OrderItem;
import com.tp.model.ord.OrderStatusLog;
import com.tp.model.ord.PersonalgoodsDeclareInfo;
import com.tp.model.ord.SubOrder;
import com.tp.model.stg.Warehouse;
import com.tp.model.wms.WaybillDetail;
import com.tp.service.ord.ICustomsClearanceLogService;
import com.tp.service.ord.IOrderConsigneeService;
import com.tp.service.ord.IOrderItemService;
import com.tp.service.ord.IOrderStatusLogService;
import com.tp.service.ord.IPersonalgoodsDeclareInfoService;
import com.tp.service.ord.ISubOrderService;
import com.tp.service.ord.customs.ISeaCustomsInfoDeliveryService;
import com.tp.service.ord.customs.ISeaWaybillInfoService;
import com.tp.service.stg.IWarehouseService;
import com.tp.service.wms.logistics.IWaybillApplicationService;
import com.tp.service.wms.logistics.IWaybillInfoService;
import com.tp.util.StringUtil;

/**
 * @author Administrator
 * 运单申报
 */
@Service
public class SeaWaybillInfoService implements ISeaWaybillInfoService,ISeaCustomsInfoDeliveryService{

	private static final Logger logger = LoggerFactory.getLogger(SeaWaybillInfoService.class);
	
	@Autowired
	private IWaybillApplicationService waybillApplicationService;
	
	@Autowired
	private IOrderConsigneeService orderConsigneeService;
	
	@Autowired
	private IOrderItemService orderItemService; 
	
	@Autowired
	private IWaybillInfoService waybillInfoService;
	
	@Autowired
	private IOrderStatusLogService orderStatusLogService;
	
	@Autowired
	private ISubOrderService subOrderService;
	
	@Autowired
	private IWarehouseService warehouseService; 
	
	@Autowired
	private ICustomsClearanceLogService customsClearanceLogService;
	
	@Autowired
	private IPersonalgoodsDeclareInfoService pgDeclareInfoService;
	
	@Override
	public ResultInfo<Boolean> delivery(SubOrder subOrder) {
		Long orderCode = subOrder.getOrderCode();
		ResultInfo<Boolean> pushResult = null;
		
		if (!ClearanceChannelsEnum.HANGZHOU.id.equals(subOrder.getSeaChannel())) {
			logger.error("[PUSH_WAYBILL_INFO][{}]{}不支持推送运单", orderCode, subOrder.getSeaChannelName());
			return new ResultInfo<>(new FailInfo("订单所在保税区不支持申报"));
		}
		
		Warehouse warehouse = warehouseService.queryById(subOrder.getWarehouseId());
		if (warehouse == null) {
			return new ResultInfo<>(new FailInfo("查询仓库信息失败"));
		}
		
		ExpressCompany expressCompany = getLogisticsInfo(warehouse);
		if (expressCompany == null) {
			return new ResultInfo<>(new FailInfo("查询物流公司信息失败"));
		}
		
		ResultInfo<WaybillDetail> resultWaybill = applyWaybillNoForOrder(orderCode.toString(), expressCompany);
		if (!resultWaybill.isSuccess() || resultWaybill.getData() == null) {
			logger.info("[PUSH_WAYBILL_INFO][{}]申请运单号失败：{}", orderCode, 
					resultWaybill.getMsg() != null ? resultWaybill.getMsg().getDetailMessage():"数据为空");
			return new ResultInfo<>(new FailInfo("请求运单号失败"));
		}
		
		pushResult = pushWaybillInfo(subOrder, expressCompany, resultWaybill.getData().getWaybillNo().toString());	
		PutCustomsStatus putCustomsStatus = pushResult.isSuccess()?PutCustomsStatus.SUCCESS : PutCustomsStatus.FAIL;
		logger.error("[PUSH_WAYBILL_INFO][{}]推送运单结果：{}", orderCode, putCustomsStatus.getDesc());
		
		//更新
		updateSuborderWithDeliveryStatus(subOrder, putCustomsStatus);
		customsClearanceLogService.insert(createCustomsClearanceLog(subOrder, pushResult));
		return pushResult;
	}

	@Override
	public boolean checkDelivery(SubOrder subOrder) {
		if (!subOrder.getPutWaybill()){
			logger.error("[PUSH_WAYBILL_INFO][{}]运单不需要推送", subOrder.getOrderCode());
			return false;
		}
		if (PutCustomsStatus.isSuccess(subOrder.getPutWaybillStatus())) {
			logger.info("[PUSH_WAYBILL_INFO][{}]运单不需要重复推送", subOrder.getOrderCode());
			return false;
		}
		return true;
	}

	@Override
	public ResultInfo<Boolean> prepareDelivery(SubOrder subOrder) {
		Long orderCode = subOrder.getOrderCode();
//		if (false == PutCustomsStatus.isInit(subOrder.getPutWaybillStatus())){
//			logger.info("[PUSH_WAYBILL_INFO][{}]运单推送状态未初始化", orderCode);
//			return new ResultInfo<>(new FailInfo("运单推送状态未初始化"));
//		}
		// 检查清关单
		PersonalgoodsDeclareInfo pgDeclareInfo = pgDeclareInfoService
				.queryUniquePersonalGoodsDeclByOrderCode(orderCode.toString());
		if (pgDeclareInfo == null) {
			return new ResultInfo<>(new FailInfo("清关单数据不存在"));
		}

		// 校验直邮逻辑
		if (pgDeclareInfo.getImportType() != null && pgDeclareInfo.getImportType() == 0) {// 直邮
			boolean canDelivery = StringUtils.isNotEmpty(pgDeclareInfo.getBillNo())
					&& StringUtils.isNotEmpty(pgDeclareInfo.getVoyageNo())
					&& StringUtils.isNotEmpty(pgDeclareInfo.getTrafNo());
			if (canDelivery == false) {
				logger.error("直邮订单未确定航班号等信息，暂不能推送运单");
				return new ResultInfo<>(new FailInfo("直邮订单未确定航班号等信息，暂不能推送运单"));
			}
		}
		return new ResultInfo<>(Boolean.TRUE);
	}
	
	private void updateSuborderWithDeliveryStatus(SubOrder subOrder, PutCustomsStatus status){
		SubOrder so = new SubOrder();
		so.setId(subOrder.getId());
		so.setPutWaybillStatus(status.code);
		subOrderService.updateNotNullById(so);
	}
	
	private CustomsClearanceLog createCustomsClearanceLog(SubOrder subOrder, ResultInfo<Boolean> pushResult){
		CustomsClearanceLog log = new CustomsClearanceLog();
		log.setCustomsCode("");
		log.setOrderCode(subOrder.getOrderCode());
		log.setType(PutCustomsType.WAYBILL_DECLARE.getIndex());
		log.setResult(pushResult.isSuccess() == true ? 1 : 0);
		log.setResultDesc(pushResult.isSuccess() == true ? "推送成功":pushResult.getMsg().getDetailMessage());
		log.setCreateTime(new Date());			
		return log;
	}
	
	/** 获取物流信息 */
	private ExpressCompany getLogisticsInfo(Warehouse warehouse){
		if (StringUtils.isEmpty(warehouse.getLogistics())) {
			return null;
		}
		return ExpressCompany.getCompanyByCommonCode(warehouse.getLogistics());
	}
	
	/**
	 * 推送运单至第三方快递公司
	 */
	@Override
	public ResultInfo<Boolean> pushWaybillInfo(SubOrder subOrder, ExpressCompany expressCompany, String expressNo){
		logger.info("[PUSH_WAYBILL_INFO][{}][company={}][expressNo={}]推送运单至第三方快递公司", 
				subOrder.getOrderCode(),expressCompany.getDesc(), expressNo);
		if (!subOrder.getPutWaybill()) {
			return new ResultInfo<>(new FailInfo("订单不需要推送物流信息"));
		}
		if (StringUtil.isEmpty(expressNo)) {
			return new ResultInfo<>(new FailInfo("运单号为空"));
		}
		//收件人信息
		OrderConsignee consignee = orderConsigneeService.selectOneByOrderId(subOrder.getParentOrderId());
		if (null == consignee) {
			return new ResultInfo<>(new FailInfo("收件人信息不存在"));
		}
		//订单行信息
		List<OrderItem> orderItemList = orderItemService.selectListBySubId(subOrder.getId());
		if (CollectionUtils.isEmpty(orderItemList)) {
			return new ResultInfo<>(new FailInfo("订单行数据为空"));
		}
		//仓库信息
		Warehouse warehouse = warehouseService.queryById(subOrder.getWarehouseId());
		if(null == warehouse){
			return new ResultInfo<>(new FailInfo("仓库数据为空"));
		}
		
		//清单数据（获取直邮相关信息）
		PersonalgoodsDeclareInfo query = new PersonalgoodsDeclareInfo();
		query.setOrderCode(subOrder.getOrderCode());
		PersonalgoodsDeclareInfo pgInfo = pgDeclareInfoService.queryUniqueByObject(query);
		if (pgInfo == null) {
			logger.error("[PUSH_WAYBILL_INFO][{}]订单对应的清关单数据不存在", subOrder.getOrderCode());
			return new ResultInfo<>(new FailInfo("订单对应的清关单不存在"));
		}
		
		//状态变更日志
		OrderStatusLog orderStatusLog = new OrderStatusLog();
		orderStatusLog.setParentOrderCode(subOrder.getParentOrderCode());
		orderStatusLog.setOrderCode(subOrder.getOrderCode());
		orderStatusLog.setPreStatus(subOrder.getOrderStatus());
		orderStatusLog.setCurrStatus(subOrder.getOrderStatus());
		orderStatusLog.setCreateTime(new Date());
		orderStatusLog.setCreateUserId(1L);
		orderStatusLog.setCreateUserName("系统调度");
		orderStatusLog.setCreateUserType(LogTypeConstant.LOG_TYPE.SYSTEM.code);	
		
		//组装信息
		ResultInfo<Boolean> message = null;
		WaybillInfoDto waybillInfoDTO = buildWaybillInfoDTO(subOrder, consignee, orderItemList, expressCompany, expressNo, warehouse, pgInfo);
		ResultInfo<Boolean> pushResult = waybillInfoService.deliverWaybillInfo(waybillInfoDTO);		
		if (Boolean.TRUE == pushResult.isSuccess()) {
			orderStatusLog.setContent("订单运单推送第三方物流企业：推送成功");
			message = new ResultInfo<>(Boolean.TRUE);
		}else{
			orderStatusLog.setContent("订单运单推送第三方物流企业：" + pushResult.getMsg().getDetailMessage());
			message = new ResultInfo<>(new FailInfo(pushResult.getMsg().getDetailMessage()));
		}
		//更新数据,日志入库
		orderStatusLogService.insert(orderStatusLog);
		return message;
	}
	
	
	/**
	 * 推送运单至第三方（单个依次推送）
	 */
	@Override
	public boolean pushWaybillInfos(List<SubOrder> subOrders, ExpressCompany expressCompany) {
		if (CollectionUtils.isEmpty(subOrders)) {
			logger.error("运单推送第三方 - 待推送订单数据为空");
			return false;
		}
		for (SubOrder subOrder : subOrders) {
			try {				
				if (!subOrder.getPutWaybill() ||
						PutCustomsStatus.SUCCESS.code.equals(subOrder.getPutWaybillStatus()) ||
						PutCustomsStatus.AUDIT_SUCCESS.code.equals(subOrder.getPutWaybillStatus())) {
					continue;
				}				
				ResultInfo<WaybillDetail> waybillDetailResult = applyWaybillNoForOrder(subOrder.getOrderCode().toString(), expressCompany);
				if (!waybillDetailResult.isSuccess()) {
					logger.error("订单{}申请运单号失败:" + waybillDetailResult.getMsg().getDetailMessage());
					continue;
				}
				String expressNo = waybillDetailResult.getData().getWaybillNo().toString();
				ResultInfo<Boolean> pushResult = pushWaybillInfo(subOrder, expressCompany, expressNo);
				PutCustomsStatus status = null;
				if (pushResult.isSuccess()) {
					status = PutCustomsStatus.SUCCESS;
				}else{
					status = PutCustomsStatus.FAIL;
				}
				SubOrder so = new SubOrder();
				so.setId(subOrder.getId());	
				so.setPutWaybillStatus(status.code);
				subOrderService.updateNotNullById(so);
			} catch (WaybillException we){
				logger.error("运单推送第三方 - 异常", we);
			} catch (Exception e) {
				logger.error("运单推送第三方 - 异常", e);
			}
		}
		return true;
	}
	
	/**
	 * 申请运单号 
	 */
	@Override
	public ResultInfo<WaybillDetail> applyWaybillNoForOrder(String OrderCode, ExpressCompany expressCompany) {
		if (StringUtil.isEmpty(OrderCode)) {
			return new ResultInfo<>(new FailInfo("订单号为空"));
		}
		if(null == expressCompany){
			return new ResultInfo<>(new FailInfo("快递公司信息为空"));
		}
		try {
			return waybillApplicationService.applyWaybillNoForOrder(OrderCode, expressCompany);
		} catch (Exception e) {
			logger.error("申请运单号异常", e);
			return new ResultInfo<>(new FailInfo(e));
		}
	}
	
	/**
	 * 组装运单信息 
	 */
	private WaybillInfoDto buildWaybillInfoDTO(
			SubOrder subOrder, OrderConsignee consignee, List<OrderItem> orderItems, 
			ExpressCompany company, String waybillNo, Warehouse warehouse, PersonalgoodsDeclareInfo pgInfo){
		
		WaybillInfoDto waybillInfoDto = new WaybillInfoDto();
		waybillInfoDto.setOrderCode(subOrder.getOrderCode().toString());
		waybillInfoDto.setOrderType(OrderType.getOrderTypeByCode(subOrder.getType()));
		waybillInfoDto.setWaybillType(WaybillType.DECLARE);
		
		waybillInfoDto.setLogisticsCode(company.getCommonCode());
		waybillInfoDto.setLogisticsName(company.getDesc());
		waybillInfoDto.setWaybillNo(waybillNo);
		
		waybillInfoDto.setGrossWeight(getGrossWeight(orderItems).toString());
		waybillInfoDto.setNetWeight(getNetWeight(orderItems).toString());
		waybillInfoDto.setPackAmount(getPackAmount(orderItems).toString());
		waybillInfoDto.setIsPostagePay(1);
		waybillInfoDto.setIsPostagePay(1);
		waybillInfoDto.setSendProvince("");
		waybillInfoDto.setSendCity("");
		waybillInfoDto.setSendArea("");
		waybillInfoDto.setSendAddress("");
		waybillInfoDto.setSendRoughArea(warehouse.getSenderCity());
		waybillInfoDto.setSenderCompany("");
		waybillInfoDto.setSenderName("");
		waybillInfoDto.setSenderTel("");
		
		waybillInfoDto.setConsignee(consignee.getName());
		waybillInfoDto.setPostCode(consignee.getPostcode());
		waybillInfoDto.setProvince(consignee.getProvinceName());
		waybillInfoDto.setCity(consignee.getCityName());
		waybillInfoDto.setArea(consignee.getCountyName());
		waybillInfoDto.setAddress(consignee.getTownName() + consignee.getAddress());
		waybillInfoDto.setMobile(consignee.getMobile());
		waybillInfoDto.setTel(consignee.getTelephone());
		
		//直邮信息
		waybillInfoDto.setImportType(pgInfo.getImportType());
		waybillInfoDto.setVoyageNo(pgInfo.getVoyageNo());
		waybillInfoDto.setDeliveryNo(pgInfo.getBillNo());
	
		List<WaybillItemInfoDto> items = new ArrayList<>();
		for (OrderItem item : orderItems) {
			WaybillItemInfoDto dto = new WaybillItemInfoDto();
			dto.setItemName(item.getSpuName());
			dto.setItemSku(item.getSkuCode());
			dto.setQuantity(item.getQuantity()*item.getUnitQuantity());//按照计量单位算，多件装商品需要实际数量
			dto.setSalesprice(item.getSalesPrice());
			items.add(dto);
		}
		waybillInfoDto.setDetails(items);	
		return waybillInfoDto;
	}
	
	//毛重
	private Double getGrossWeight(List<OrderItem> lines){
		if (CollectionUtils.isEmpty(lines)) {
			return 0.0;
		}
		Double weight = 0.0;
		for (OrderItem orderItem : lines) {
			weight = BigDecimal.valueOf(weight).add(BigDecimal.valueOf(orderItem.getWeight()).multiply(BigDecimal.valueOf(orderItem.getQuantity()))).doubleValue();
		}
		return weight;
	}
	//净重
	private Double getNetWeight(List<OrderItem> lines){
		if (CollectionUtils.isEmpty(lines)) {
			return 0.0;
		}
		Double weight = 0.0;
		for (OrderItem orderItem : lines) {
			weight = BigDecimal.valueOf(weight).add(BigDecimal.valueOf(orderItem.getWeightNet()).multiply(BigDecimal.valueOf(orderItem.getQuantity()))).doubleValue();
		}
		return weight;
	}
	
	//商品数量
	private Integer getPackAmount(List<OrderItem> lines){
		Integer count = 0;
		for (OrderItem orderItem : lines) {
			count += orderItem.getQuantity();
		}
		return count;
	}
	
	public static class WaybillException extends RuntimeException{

		private static final long serialVersionUID = -963332399265943654L;

		public WaybillException(String errorMsg) {
			super(errorMsg);
		}
	}

	@Override
	public boolean checkPutCustomsType(PutCustomsType type) {
		if (PutCustomsType.WAYBILL_DECLARE == type){
			return true;
		}
		return false;
	}
}
