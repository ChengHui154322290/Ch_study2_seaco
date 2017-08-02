package com.tp.service.ord.customs.JKF;

import static com.tp.util.BigDecimalUtil.toPrice;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.tp.common.vo.Constant;
import com.tp.common.vo.DAOConstant.MYBATIS_SPECIAL_STRING;
import com.tp.common.vo.OrderConstant.PutCustomsStatus;
import com.tp.common.vo.bse.ClearanceChannelsEnum;
import com.tp.common.vo.customs.JKFConstant;
import com.tp.common.vo.customs.JKFConstant.CebFlag;
import com.tp.dto.common.FailInfo;
import com.tp.dto.common.ResultInfo;
import com.tp.model.bse.CustomsDistrictInfo;
import com.tp.model.bse.CustomsUnitInfo;
import com.tp.model.bse.DistrictInfo;
import com.tp.model.ord.OrderConsignee;
import com.tp.model.ord.OrderItem;
import com.tp.model.ord.SubOrder;
import com.tp.model.ord.JKF.JkfImportOrderRequest;
import com.tp.model.ord.JKF.JkfReceiptResult;
import com.tp.model.ord.JKF.JkfImportOrderRequest.JkfGoodsPurchaser;
import com.tp.model.ord.JKF.JkfImportOrderRequest.JkfOrderDetail;
import com.tp.model.ord.JKF.JkfImportOrderRequest.JkfOrderImportHead;
import com.tp.model.ord.JKF.JkfImportOrderRequest.OrderInfo;
import com.tp.model.ord.JKF.JkfReceiptResult.JkfResult;
import com.tp.model.ord.JKF.JkfReceiptResult.JkfResultDetail;
import com.tp.model.pay.CustomsInfo;
import com.tp.model.pay.PaymentInfo;
import com.tp.model.prd.ItemSku;
import com.tp.model.prd.ItemSkuArt;
import com.tp.model.stg.Warehouse;
import com.tp.model.sup.SupplierCustomsRecordation;
import com.tp.service.bse.ICustomsDistrictInfoService;
import com.tp.service.bse.ICustomsUnitInfoService;
import com.tp.service.bse.IDistrictInfoService;
import com.tp.service.ord.IJKFSoaService;
import com.tp.service.ord.IOrderConsigneeService;
import com.tp.service.ord.IOrderItemService;
import com.tp.service.ord.ISubOrderService;
import com.tp.service.ord.customs.JKF.IJKFDeclareOrderLocalService;
import com.tp.service.pay.ICustomsInfoService;
import com.tp.service.pay.IPaymentInfoService;
import com.tp.service.prd.IItemSkuArtService;
import com.tp.service.prd.IItemSkuService;
import com.tp.service.stg.IWarehouseService;
import com.tp.service.sup.ISupplierCustomsRecordationService;
import com.tp.util.BigDecimalUtil;
import com.tp.util.DateUtil;
import com.tp.util.StringUtil;

/**
 *	推送订单和个人物品至浙江电子口岸 
 *	单次申报只允许申报一条 
 */
@Service
public class JKFDeclareOrderLocalService implements IJKFDeclareOrderLocalService{
	
	private static final Logger logger = LoggerFactory.getLogger(JKFDeclareOrderLocalService.class);
	
	//成交币种：人民币代码
	private static final String TRADE_CURRENCY_RMB = "142";

	//发件人姓名
//	private static final String SENDER_NAME = "西客商城";
	//是否加密申报
	@Value("#{meta['JKF.isEncrypt']}")
	private boolean isEncrypt;
	
	//西客电商企业备案编号
	@Value("#{meta['JKF.XG.companyCode']}")
	private String xgCompanyCode;
	
	//西客电商企业备案名称
	@Value("#{meta['JKF.XG.companyName']}")
	private String xgCompanyName;
	
	@Value("#{meta['XG.CC.isSendCeb']}")
	private boolean isSendCeb;
	
	@Autowired
	private ISubOrderService subOrderService;
	
	@Autowired
	private IJKFSoaService jkfSoaService;
	
	@Autowired
	private IOrderConsigneeService orderConsigneeService;
	
	@Autowired
	private IOrderItemService orderItemService;
	
	@Autowired
	private IItemSkuArtService itemSkuArtService;
	
	@Autowired
	private ICustomsDistrictInfoService customsDistrictInfoService;
	
	@Autowired
	private ICustomsUnitInfoService customsUnitInfoService;
	
	@Autowired
	private IWarehouseService warehouseService;
	
	@Autowired
	private ISupplierCustomsRecordationService supplierCustomsRecordationService;
	
	@Autowired
	private IPaymentInfoService paymentInfoService;
	
	@Autowired
	private ICustomsInfoService customsInfoService;
	
	@Autowired
	private IDistrictInfoService districtInfoService;
	
	@Autowired
	private IItemSkuService itemSkuService;
	
	/**
	 * 推送订单至电子口岸 
	 */
	public ResultInfo<Boolean> pushOrderInfo(SubOrder subOrder){
		
		//收件人信息
		OrderConsignee consignee = orderConsigneeService.selectOneByOrderId(subOrder.getParentOrderId());
		if (null == consignee) {
			return new ResultInfo<>(new FailInfo("收件人信息不存在"));
		}
		//收件地址所在区县
		DistrictInfo consigneeDistrict = districtInfoService.queryById(consignee.getCountyId());
		if (consigneeDistrict == null) {
			return new ResultInfo<>(new FailInfo("收件人信息错误"));
		}
		//商品行信息
		List<OrderItem> lineList = orderItemService.selectListBySubId(subOrder.getId());
		if(CollectionUtils.isEmpty(lineList)){
			return new ResultInfo<>(new FailInfo("订单商品信息不存在"));
		}
		//仓库信息
		Warehouse warehouse = warehouseService.queryById(subOrder.getWarehouseId());
		if (null == warehouse) {
			return new ResultInfo<>(new FailInfo("仓库信息不存"));
		}
		
		CustomsInfo payCustomsInfo = getCustomsInfo(subOrder);
		if(payCustomsInfo == null){
			logger.error("[PUSH_ORDERINFO][{}][gateway={}]海关信息不存在",subOrder.getOrderCode(),subOrder.getPayWay());
			return new ResultInfo<>(new FailInfo("支付平台海关信息不存在"));
		}
		//商家备案信息
		SupplierCustomsRecordation customsRecordation = new SupplierCustomsRecordation();
		customsRecordation.setSupplierId(subOrder.getSupplierId());
		customsRecordation.setCustomsChannelId(subOrder.getSeaChannel());
		customsRecordation.setStatus(1);
		List<SupplierCustomsRecordation> scrList = supplierCustomsRecordationService.queryByObject(customsRecordation);
		if (CollectionUtils.isEmpty(scrList)) {
			logger.error("[PUSH_ORDERINFO][{}][spId={}][customsChannelId={}]海关信息不存在",
					subOrder.getOrderCode(),subOrder.getSupplierId(),subOrder.getSeaChannel());
			return new ResultInfo<>(new FailInfo("供应商海关备案信息不存在"));
		}
		//支付信息
		PaymentInfo paymentInfo = paymentInfoService.queryPaymentInfoByBizCode(subOrder.getOrderCode());
		if (paymentInfo == null) {
			return new ResultInfo<>(new FailInfo("支付信息不存在"));
		}
		//商品海关备案信息
		List<String> skuCodes = new ArrayList<>();
		for (OrderItem orderItem : lineList) {
			skuCodes.add(orderItem.getSkuCode());
		}
		Map<String, ItemSkuArt> sku2ItemArtMap = getItemSkuArtInfo(skuCodes, subOrder.getSeaChannel());	
		//海关产地信息以及海关计量单位信息
		Map<String, CustomsDistrictInfo> sku2DistrictInfoMap = getCustomsDistrictInfo(lineList);
		Map<String, CustomsUnitInfo> sku2UnitInfoMap = getCustomsUnitInfo(skuCodes);
		/*------------------申报 ---------------------------------*/
		FailInfo failInfo = validateBuildImportOrdeInfo(subOrder, consignee, lineList, sku2ItemArtMap, 
				sku2DistrictInfoMap, sku2UnitInfoMap, warehouse, scrList.get(0), paymentInfo, payCustomsInfo);
		if (failInfo != null) {
			logger.error("[PUSH_ORDERINFO][{}]数据校验不通过:{}",subOrder.getOrderCode(),failInfo.getDetailMessage());
			return new ResultInfo<>(failInfo);
		}
		//组装数据
		OrderInfo orderInfo = buildImportOrderInfo(subOrder, consignee, lineList, sku2ItemArtMap, 
				sku2DistrictInfoMap, sku2UnitInfoMap, warehouse, scrList.get(0), paymentInfo, payCustomsInfo, consigneeDistrict);	
		//申报日志
		JkfImportOrderRequest request = new JkfImportOrderRequest();
		request.getHead().setBusinessType(JKFConstant.JKFBusinessType.IMPORTORDER.type);
		request.getBody().getOrderInfoList().add(orderInfo);					
		//申报
		ResultInfo<JkfReceiptResult> jKFResult = jkfSoaService.orderDeclare(request, isEncrypt);
		if (!jKFResult.isSuccess()) {	//申报失败
			logger.error("[PUSH_ORDERINFO][{}]申报订单数据异常:{}", subOrder.getOrderCode(), jKFResult.getMsg().getMessage());
			return new ResultInfo<>(new FailInfo("推送订单失败:推送请求异常"));
		}
		JkfResult result = jKFResult.getData().getBody().getList().get(0);							
		//更新推送状态
		if (null != result) {			
			StringBuffer stringBuffer = new StringBuffer();
			for (JkfResultDetail detail : result.getResultList()) {
				stringBuffer.append(detail.getResultInfo()); 
			}	
			if(JKFConstant.CheckResult.SUCCESS.result.equals(Integer.valueOf(result.getChkMark()))){
				logger.info("[PUSH_ORDERINFO][{}]推送订单成功", subOrder.getOrderCode());
				return new ResultInfo<>(Boolean.TRUE);
			}else{
				logger.error("[PUSH_ORDERINFO][{}]推送订单失败,结果:{}", subOrder.getOrderCode(), stringBuffer.toString());
				return new ResultInfo<>(new FailInfo(stringBuffer.toString()));
			}
		}
		return new ResultInfo<>(new FailInfo("推送订单失败"));
	}
	
	/**
	 * 
	 * 推送订单信息至杭州电子口岸
	 * 
	 * 一次处理多次申报，单次申报只允许申报一条
	 * 查询待申报的订单数据（代发货订单，待推送订单）(待发货的订单申报至海关，清关通过后发货)
	 */
	@Override
	public boolean pushOrderInfo(List<SubOrder> unDeclareOrders) {
		if (CollectionUtils.isEmpty(unDeclareOrders)) {
			logger.debug("[PUSH_ORDERINFO]待推送订单为空");
			return false;
		}
		for (SubOrder subOrder : unDeclareOrders) {
			Integer putCustomStatus = subOrder.getPutOrderStatus();
			try {
				ResultInfo<Boolean> declareResult = pushOrderInfo(subOrder);
				if (!subOrder.getPutOrder() 
						|| PutCustomsStatus.SUCCESS.code.equals(putCustomStatus)
						|| PutCustomsStatus.AUDIT_SUCCESS.code.equals(putCustomStatus)) {
					continue;
				}
				if (Boolean.TRUE != declareResult.isSuccess()) {
 					logger.error("[PUSH_ORDERINFO][{}]推送失败,原因：" + declareResult.getMsg().getDetailMessage(), subOrder.getOrderCode());					
 					putCustomStatus = PutCustomsStatus.FAIL.code;
				}else{
	 				//推送订单成功
	 				putCustomStatus = PutCustomsStatus.SUCCESS.code;
				}
				//更新状态
				SubOrder so = new SubOrder();
				so.setId(subOrder.getId());
				so.setPutOrderStatus(putCustomStatus);
				subOrderService.updateNotNullById(so);
			}catch (Exception e) {
				logger.error("[PUSH_ORDERINFO][{}]推送订单失败", subOrder.getOrderCode(), e);	
			}
		}
		return true;
	}
	
	//校验
	private FailInfo validateBuildImportOrdeInfo(SubOrder subOrder, OrderConsignee consignee, List<OrderItem> lines, 
			Map<String, ItemSkuArt> sku2ItemArtMap, Map<String, CustomsDistrictInfo> sku2DistrictInfoMap, Map<String, 
			CustomsUnitInfo> sku2UnitInfoMap, Warehouse warehouse, SupplierCustomsRecordation supplierCustoms, 
			PaymentInfo paymentInfo, CustomsInfo payCustomsInfo){		
		if (StringUtil.isEmpty(warehouse.getSenderCountryCode())) {
			return new FailInfo("发件人国别不能为空");
		}		
		if (StringUtil.isEmpty(warehouse.getSenderName())) {
			return new FailInfo("发件人姓名不能为空");
		}
		if (warehouse.getPostMode() == null) {
			return new FailInfo("发货方式不能为空");
		}
		if (StringUtil.isEmpty(warehouse.getLogisticsCode())) {
			return new FailInfo("物流企业编号不能为空");
		}
		if (StringUtil.isEmpty(warehouse.getLogisticsName())) {
			return new FailInfo("物流企业名称不能为空");
		}
		if (StringUtil.isEmpty(supplierCustoms.getRecordationName())) {
			return new FailInfo("商家备案名称不能为空");
		}
		if (StringUtil.isEmpty(supplierCustoms.getRecordationNum())) {
			return new FailInfo("商家备案编号不能为空");
		}
		for (int i = 0; i < lines.size(); i++) {
			OrderItem orderItem = lines.get(i);
			ItemSkuArt art = sku2ItemArtMap.get(orderItem.getSkuCode());
			if (null == art) {
				logger.error("[PUSH_ORDERINFO][{}]商品行数据 sku={} 不存在海关备案信息", subOrder.getOrderCode(), orderItem.getSkuCode());
				return new FailInfo("海关备案信息不存在");
			}
			CustomsDistrictInfo cdInfo = sku2DistrictInfoMap.get(orderItem.getSkuCode());
			if (null == cdInfo) {
				logger.error("[PUSH_ORDERINFO][{}]商品行数据sku={}不存在产地信息", subOrder.getOrderCode(), orderItem.getSkuCode());
				return new FailInfo("产地信息不存在");
			}
			CustomsUnitInfo cuInfo = sku2UnitInfoMap.get(orderItem.getSkuCode());
			if (null == cuInfo) {
				logger.error("[PUSH_ORDERINFO][{}]商品行数据sku={}不存在计量单位信息", subOrder.getOrderCode(), orderItem.getSkuCode());
				return new FailInfo("计量单位信息不存在");
			}
		}
		if (StringUtil.isEmpty(paymentInfo.getPaymentCustomsNo())) {
			return new FailInfo("支付交易单号为空");
		}
		if (StringUtil.isEmpty(payCustomsInfo.getPayplatCode())) {
			return new FailInfo("支付平台海关备案号为空");
		}
		if (StringUtil.isEmpty(payCustomsInfo.getPayplatName())){
			return new FailInfo("支付平台海关备案名称为空");
		}
		return null;
	}

	//构建推送海关订单请求数据
	private OrderInfo buildImportOrderInfo(SubOrder subOrder, OrderConsignee consignee, List<OrderItem> lines, 
			Map<String, ItemSkuArt> sku2ItemArtMap, Map<String, CustomsDistrictInfo> sku2DistrictInfoMap, Map<String, 
			CustomsUnitInfo> sku2UnitInfoMap, Warehouse warehouse, SupplierCustomsRecordation supplierCustoms, 
			PaymentInfo paymentInfo, CustomsInfo payCustomsInfo, DistrictInfo consigneeDistrict){
		OrderInfo orderInfo = new OrderInfo();
		//组装JKFSign
		JkfImportOrderRequest.JkfSign sign = orderInfo.getJkfSign();
		sign.setCompanyCode(xgCompanyCode); //发送方备案号
		sign.setBusinessType(JKFConstant.JKFBusinessType.IMPORTORDER.type);
		sign.setDeclareType("1");
		sign.setBusinessNo(subOrder.getOrderCode().toString());
		sign.setNote("");
		//是否申报总署
		sign.setCebFlag(getCebFlag());
		
		//组装jkfOrderImportHead
		JkfOrderImportHead jkfOrderImportHead = orderInfo.getJkfOrderImportHead();
		jkfOrderImportHead.setCompanyCode(xgCompanyCode); //电商平台备案号
		jkfOrderImportHead.setCompanyName(xgCompanyName); //电商平台备案号
		jkfOrderImportHead.setIeFlag("I");
		jkfOrderImportHead.setPayType("03");
		jkfOrderImportHead.setPayCompanyCode(payCustomsInfo.getPayplatCode());
		jkfOrderImportHead.setPayCompanyName(payCustomsInfo.getPayplatName()); //总署版新增
		jkfOrderImportHead.setPayNumber(paymentInfo.getPaymentCustomsNo());
		jkfOrderImportHead.setOrderTotalAmount(subOrder.getOriginalTotal());//货款+运费+税费+保费	
		jkfOrderImportHead.setOrderGoodsAmount(getOrderGoodsAmount(lines));	//货款完税价格
		/*--2016-10-19升级新增 --*/
		jkfOrderImportHead.setDiscount(getOrderDiscount(subOrder));
		jkfOrderImportHead.setBatchNumbers("");
		jkfOrderImportHead.setConsigneeDitrict(consigneeDistrict.getNationalCode()); //收件地址区县代号
		/*--2016-10-19升级新增 --*/
		jkfOrderImportHead.setOrderNo(subOrder.getOrderCode().toString());
//		jkfOrderImportHead.setOrderTaxAmount(subOrder.getTaxFee());
		jkfOrderImportHead.setOrderTaxAmount(getOriginalTaxFee(lines));
		jkfOrderImportHead.setFeeAmount(getOriginalFreightFee(lines));
		jkfOrderImportHead.setInsureAmount(0.0);
		jkfOrderImportHead.seteCommerceCode(supplierCustoms.getRecordationNum()); //电商企业备案号
		jkfOrderImportHead.seteCommerceName(supplierCustoms.getRecordationName()); //电商企业备案名称
		jkfOrderImportHead.setTradeTime(DateUtil.format(subOrder.getPayTime(), "yyyy-mm-dd HH:mm:ss"));
		jkfOrderImportHead.setCurrCode(TRADE_CURRENCY_RMB);
		jkfOrderImportHead.setTotalAmount(jkfOrderImportHead.getOrderGoodsAmount());	//与订单货款一致
		jkfOrderImportHead.setConsignee(subOrder.getConsigneeName());
		jkfOrderImportHead.setConsigneeTel(subOrder.getConsigneeMobile());
		jkfOrderImportHead.setConsigneeAddress(convertOrderConsignee(consignee));
		jkfOrderImportHead.setPostMode(warehouse.getPostMode().toString());
		Integer totalCount = 0;
		for (OrderItem orderItem : lines) {
			totalCount += (orderItem.getWrapQuantity()*orderItem.getQuantity());
		}
		jkfOrderImportHead.setTotalCount(totalCount); //包裹中独立包装的物品总数，不考虑物品计量单位
		jkfOrderImportHead.setSenderCountry(warehouse.getSenderCountryCode());
		jkfOrderImportHead.setSenderName(warehouse.getSenderName());		
		jkfOrderImportHead.setPurchaserId(subOrder.getMemberId().toString());
		jkfOrderImportHead.setLogisCompanyCode(warehouse.getLogisticsCode());  //物流企业备案号
		jkfOrderImportHead.setLogisCompanyName(warehouse.getLogisticsName());  //物流企业备案名称
		jkfOrderImportHead.setUserProcotol(JkfOrderImportHead.USER_PROCOTOL);
		
		//订单表体信息
		for (int i = 0; i < lines.size(); i++) {
			OrderItem orderItem = lines.get(i);
			ItemSkuArt art = sku2ItemArtMap.get(orderItem.getSkuCode());
			CustomsDistrictInfo cdInfo = sku2DistrictInfoMap.get(orderItem.getSkuCode());
			CustomsUnitInfo cuInfo = sku2UnitInfoMap.get(orderItem.getSkuCode());
			JkfOrderDetail detail = new JkfOrderDetail();	
			detail.setGoodsOrder(i + 1);
			detail.setCodeTs(art.getHsCode());
			detail.setGoodsName(art.getItemDeclareName());
			//产地信息
			detail.setOriginCountry(cdInfo.getCode());
			//计量单位信息
			detail.setGoodsUnit(cuInfo.getCode());
			//成交价
			detail.setUnitPrice(orderItem.getSalesPrice());
			detail.setGoodsCount(orderItem.getQuantity());		
			orderInfo.getJkfOrderDetailList().add(detail);
			/*--2016-10-19升级新增 --*/
			detail.setCurrency(TRADE_CURRENCY_RMB);
			detail.setBarCode(orderItem.getBarCode());
			detail.setNote("");
			/*--2016-10-19升级新增 --*/
		}	
		//购买人信息
		JkfGoodsPurchaser purchaser = orderInfo.getJkfGoodsPurchaser();
		purchaser.setId(subOrder.getMemberId().toString());
		purchaser.setName(subOrder.getConsigneeName());
		purchaser.setTelNumber(subOrder.getConsigneeMobile());
		purchaser.setPaperType("01");//身份证
		purchaser.setPaperNumber(consignee.getIdentityCard());
		
		return orderInfo;
	}
	
	private CustomsInfo getCustomsInfo(SubOrder subOrder){
		CustomsInfo customsInfo = new CustomsInfo();
		customsInfo.setChannelsId(ClearanceChannelsEnum.HANGZHOU.id);	
		customsInfo.setGatewayId(subOrder.getPayWay());
		return customsInfoService.queryUniqueByObject(customsInfo);
	}
	private Map<String, ItemSkuArt> getItemSkuArtInfo(List<String> skuCodes, Long seaChannel){
		Map<String, ItemSkuArt> sku2ItemArtMap = new HashMap<>();
		Map<String, Object> params = new HashMap<>();
		params.put("bonded_area", seaChannel);
		params.put(MYBATIS_SPECIAL_STRING.COLUMNS.name(), "sku in(" + StringUtil.join(skuCodes, Constant.SPLIT_SIGN.COMMA) + ")");
		List<ItemSkuArt> artList = itemSkuArtService.queryByParam(params);			
		if (CollectionUtils.isNotEmpty(artList)) {
			for (ItemSkuArt itemSkuArt : artList) {
				sku2ItemArtMap.put(itemSkuArt.getSku(), itemSkuArt);
			}
		}
		return sku2ItemArtMap;
	}	
	private Map<String, CustomsUnitInfo> getCustomsUnitInfo(List<String> skuCodes){
		Map<String, CustomsUnitInfo> sku2UnitInfoMap = new HashMap<>();
		ResultInfo<List<ItemSku>> itemSkuResult = itemSkuService.queryItemListBySkus(skuCodes);
		if (!itemSkuResult.isSuccess() && CollectionUtils.isEmpty(itemSkuResult.getData())) return sku2UnitInfoMap;		
		List<Long> unitIds = new ArrayList<>();
		for(ItemSku sku : itemSkuResult.getData()){		
			if(sku.getUnitId() != null) unitIds.add(sku.getUnitId());
		}
		Map<Long, CustomsUnitInfo> customsInfoMap = customsUnitInfoService.queryCustomsUnitInfo(unitIds);
		for(ItemSku itemSku : itemSkuResult.getData()){
			if (itemSku.getUnitId() != null && customsInfoMap.get(itemSku.getUnitId()) != null) {
				sku2UnitInfoMap.put(itemSku.getSku(), customsInfoMap.get(itemSku.getUnitId()));
			}
		}
		return sku2UnitInfoMap;
	}
	private Map<String, CustomsDistrictInfo> getCustomsDistrictInfo(List<OrderItem> lines){
		Map<String, CustomsDistrictInfo> infoMap = new HashMap<>();
		List<Long> countryIds = new ArrayList<>();
		for(OrderItem item : lines){
			countryIds.add(item.getCountryId());
		}
		Map<Long, CustomsDistrictInfo> districtId2InfoMap = 
				customsDistrictInfoService.queryCustomsDistrictInfo(countryIds);
		for(OrderItem item : lines){
			if(StringUtil.isNoneBlank(item.getSkuCode()) && item.getCountryId() != null &&
					districtId2InfoMap.get(item.getCountryId()) != null){
				infoMap.put(item.getSkuCode(), districtId2InfoMap.get(item.getCountryId()));	
			}
		}
		return infoMap;
	}
	/*---------------------------------------------------------------------------------*/
	
	private Double getOrderDiscount(SubOrder subOrder){
		return toPrice(BigDecimalUtil.subtract(subOrder.getOriginalTotal(), subOrder.getTotal()));
	}
	
	private Double getOriginalFreightFee(List<OrderItem> lines){
		Double totalFee = 0.0;
		for (OrderItem orderItem : lines) {
			Double fee = orderItem.getOrigFreight();
			totalFee = BigDecimalUtil.add(totalFee, fee).doubleValue();
		}
		return BigDecimalUtil.toPrice(BigDecimal.valueOf(totalFee));	
	}
	
	private Double getOriginalTaxFee(List<OrderItem> lines){
		Double totalFee = 0.0;
		for (OrderItem orderItem : lines) {
			Double fee = orderItem.getOrigTaxFee();
//			Double fee = toPrice(divide(multiply(multiply(orderItem.getSalesPrice(),orderItem.getQuantity()),orderItem.getTaxRate()),100,6));
			totalFee = BigDecimalUtil.add(totalFee, fee).doubleValue();
		}
		return BigDecimalUtil.toPrice(BigDecimal.valueOf(totalFee));
	}
	
	/** 订单货款 **/
	private Double getOrderGoodsAmount(List<OrderItem> lines){
		if (CollectionUtils.isEmpty(lines)) {
			return 0.0;
		}
		Double amount = 0.0;
		for (OrderItem orderItem : lines) {
			amount = BigDecimal.valueOf(amount).add(BigDecimal.valueOf(orderItem.getSalesPrice()).multiply(BigDecimal.valueOf(orderItem.getQuantity()))).doubleValue();
		}
		return amount;
	}
	
	/** 设置收货人地址 **/
	private String convertOrderConsignee(OrderConsignee orderConsignee){
		StringBuffer sb = new StringBuffer();
		if(null != orderConsignee){	
			sb.append(orderConsignee.getProvinceName());
			sb.append(orderConsignee.getCityName());
			sb.append(orderConsignee.getCountyName());
			sb.append(orderConsignee.getTownName());
			sb.append(orderConsignee.getAddress());	
		}
		return sb.toString();
	}
	
	public static class DeclareException extends RuntimeException{
		
		private static final long serialVersionUID = 3328253147991549969L;

		public DeclareException(String errorMsg) {
			super(errorMsg);
		}
	}
	
	private String getCebFlag(){
		if (isDelcareZS()) {
			return CebFlag.DECLARE_ZS_ZJPORT.flag;
		}else{
			return CebFlag.DECLARE_HZ.flag;
		}
	}
	
	private boolean isDelcareZS(){
		return isSendCeb;
	}
}
