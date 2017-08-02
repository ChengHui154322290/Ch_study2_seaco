/**
 * 
 */
package com.tp.service.ord.customs.JKF;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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
import com.tp.common.vo.OrderConstant.DeclareCustoms;
import com.tp.common.vo.customs.JKFConstant;
import com.tp.common.vo.customs.CustomsConstant.DeclareCompanyType;
import com.tp.common.vo.customs.JKFConstant.CebFlag;
import com.tp.common.vo.customs.JKFConstant.CheckResult;
import com.tp.common.vo.customs.JKFConstant.TransF;
import com.tp.common.vo.wms.WmsConstant.ExpressCompany;
import com.tp.dto.common.FailInfo;
import com.tp.dto.common.ResultInfo;
import com.tp.model.bse.CustomsDistrictInfo;
import com.tp.model.bse.CustomsUnitInfo;
import com.tp.model.ord.OrderConsignee;
import com.tp.model.ord.OrderItem;
import com.tp.model.ord.PersonalgoodsDeclareInfo;
import com.tp.model.ord.SubOrder;
import com.tp.model.ord.JKF.JkfGoodsDeclareRequest;
import com.tp.model.ord.JKF.JkfReceiptResult;
import com.tp.model.ord.JKF.JkfGoodsDeclareRequest.GoodsDeclare;
import com.tp.model.ord.JKF.JkfGoodsDeclareRequest.GoodsDeclareDetail;
import com.tp.model.ord.JKF.JkfGoodsDeclareRequest.GoodsDeclareModule;
import com.tp.model.ord.JKF.JkfReceiptResult.JkfResult;
import com.tp.model.ord.JKF.JkfReceiptResult.JkfResultDetail;
import com.tp.model.prd.ItemSku;
import com.tp.model.prd.ItemSkuArt;
import com.tp.model.stg.Warehouse;
import com.tp.model.sup.SupplierCustomsRecordation;
import com.tp.redis.util.JedisDBUtil;
import com.tp.service.bse.ICustomsDistrictInfoService;
import com.tp.service.bse.ICustomsUnitInfoService;
import com.tp.service.ord.IJKFSoaService;
import com.tp.service.ord.IOrderConsigneeService;
import com.tp.service.ord.IOrderItemService;
import com.tp.service.ord.customs.JKF.IJKFDeclarePersonalGoodsLocalService;
import com.tp.service.ord.customs.JKF.JKFDeclareOrderLocalService.DeclareException;
import com.tp.service.prd.IItemSkuArtService;
import com.tp.service.prd.IItemSkuService;
import com.tp.service.stg.IWarehouseService;
import com.tp.service.sup.ISupplierCustomsRecordationService;
import com.tp.util.BigDecimalUtil;
import com.tp.util.DateUtil;
import com.tp.util.StringUtil;

/**
 * @author Administrator
 * 个人支付单推送至海关
 * 暂时只支持保税进口
 */
@Service
public class JKFDeclarePersonalGoodsLocalService implements IJKFDeclarePersonalGoodsLocalService{
	
	private static final Logger logger = LoggerFactory.getLogger(JKFDeclarePersonalGoodsLocalService.class);
	
	//抵运港口代码(中国境内)
	private static final String DESTINATION_PORT = "142";
	//贸易国别
	private static final String TRADE_COUNTRY_CODE = "142";
	//成交币种：人民币代码
	private static final String TRADE_CURRENCY_RMB = "142";
	
	/* =================== 个人物品申报单 =========================*/
	private static final String DATE_PATTERN = "yyMMdd";
	/** 索引字符串长度 */
	private static final Integer INDEX_STRING_LENGTH = 8;
	/** 编号长度 */
	private static final Integer CODE_LENGTH = 16;
	/** index **/
	public static final String INDEX_KEY = "pgd_index_key";
	/* ========================================================== */
	//是否加密申报
	@Value("#{meta['JKF.isEncrypt']}")
	private boolean isEncrypt;
	
	//西客电商企业备案编号
	@Value("#{meta['JKF.XG.companyCode']}")
	private String xgCompanyCode;
	
	//西客电商企业备案名称
	@Value("#{meta['JKF.XG.companyName']}")
	private String xgCompanyName;
	
	//担保企业编号(由西客贸易担保)
	@Value("#{meta['JKF.assureCode']}")
	private String assureCode;
		
	//是否推送总署
	@Value("#{meta['XG.CC.isSendCeb']}")
	private boolean isSendCeb;
	
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
	private JedisDBUtil jedisDBUtil;
	
	@Autowired
	private IWarehouseService warehouseService;
	
	@Autowired
	private ISupplierCustomsRecordationService supplierCustomsRecordationService;
	
	@Autowired
	private IItemSkuService itemSkuService;
	
	
	/** 生成个人物品申报单 */
	@Override
	public PersonalgoodsDeclareInfo createPersonalgoodsDeclareInfo(SubOrder subOrder,
			ExpressCompany expressCompany, String waybillNo) {
		PersonalgoodsDeclareInfo declareInfo = new PersonalgoodsDeclareInfo();
		declareInfo.setOrderCode(subOrder.getOrderCode());
		declareInfo.setDeclareCustoms(DeclareCustoms.JKF.code);
		declareInfo.setDeclareNo(subOrder.getOrderCode().toString());
		declareInfo.setPreEntryNo(getPreEntryNo());
		declareInfo.setCompanyNo(expressCompany.commonCode);
		declareInfo.setCompanyName(expressCompany.desc);
		declareInfo.setExpressNo(waybillNo);
		declareInfo.setCreateTime(new Date());
		declareInfo.setUpdateTime(new Date());
		return declareInfo;
	}
	
	/** 推送个人物品申报单至电子口岸 */
	public ResultInfo<Boolean> pushPersonalGoodsInfoToJKF(PersonalgoodsDeclareInfo pDecl, SubOrder subOrder){
		if (pDecl == null){
			return new ResultInfo<>(new FailInfo("清关单不存在"));
		}
		//收件人信息
		OrderConsignee consignee = orderConsigneeService.selectOneByOrderId(subOrder.getParentOrderId());
		if (null == consignee) {
			return new ResultInfo<>(new FailInfo("收件人信息不存在"));
		}
		//订单行信息
		List<OrderItem> lineList = orderItemService.selectListBySubId(subOrder.getId());
		if (CollectionUtils.isEmpty(lineList)) {
			return new ResultInfo<>(new FailInfo("订单行信息不存在"));
		}
		//仓库信息
		Warehouse warehouse = warehouseService.queryById(subOrder.getWarehouseId());
		if (null == warehouse) {
			return new ResultInfo<>(new FailInfo("仓库信息不存"));
		}
		//商家备案信息
		SupplierCustomsRecordation customsRecordation = new SupplierCustomsRecordation();
		customsRecordation.setSupplierId(subOrder.getSupplierId());
		customsRecordation.setCustomsChannelId(subOrder.getSeaChannel());
		customsRecordation.setStatus(1);
		List<SupplierCustomsRecordation> scrList = supplierCustomsRecordationService.queryByObject(customsRecordation);
		if (CollectionUtils.isEmpty(scrList)) {
			logger.error("[PUSH_PERSONALGOODS_INFO][{}][spId={}]供应商备案信息不存在", 
					subOrder.getOrderCode(), customsRecordation.getSupplierId());
			return new ResultInfo<>(new FailInfo("供应商海关备案信息不存在"));
		}
		//商品海关备案信息	
		List<String> skuCodes = new ArrayList<>();	
		for(OrderItem item : lineList){
			skuCodes.add(item.getSkuCode());
		}
		Map<String, ItemSkuArt> sku2ItemArtMap = getItemSkuArtInfo(skuCodes, subOrder.getSeaChannel());
		//海关产地信息以及海关计量单位信息
		Map<String, CustomsDistrictInfo> sku2DistrictInfoMap = getCustomsDistrictInfo(lineList);
		Map<String, CustomsUnitInfo> sku2UnitInfoMap = getCustomsUnitInfo(skuCodes);	
		/*------------------------- 申报 -------------------------------------------*/
		FailInfo failInfo = validateBuildPersonalgoodsDecl(pDecl, subOrder, consignee, lineList, sku2ItemArtMap, sku2DistrictInfoMap, sku2UnitInfoMap, warehouse, scrList.get(0));
		if (null != failInfo) {
			logger.error("[PUSH_PERSONALGOODS_INFO][{}]数据校验不通过:{}", subOrder.getOrderCode(), failInfo.getDetailMessage());
			return new ResultInfo<>(failInfo);
		}
		GoodsDeclareModule module = buildPersonalGoodsDecl(pDecl, subOrder, consignee, lineList, sku2ItemArtMap, sku2DistrictInfoMap, sku2UnitInfoMap, warehouse, scrList.get(0));
		JkfGoodsDeclareRequest request = new JkfGoodsDeclareRequest();
		request.getHead().setBusinessType(JKFConstant.JKFBusinessType.PERSONAL_GOODS_DECLAR.type);
		request.getBody().getGoodsDeclareModuleList().add(module);
		//申报数据
		ResultInfo<JkfReceiptResult> jKFResult = jkfSoaService.personalGoodsDeclare(request, isEncrypt);
		if (!jKFResult.isSuccess()) {
			logger.error("[PUSH_PERSONALGOODS_INFO][{}]清单申报异常，error:{}", 
					subOrder.getOrderCode(), jKFResult.getMsg().getMessage());
			return new ResultInfo<>(new FailInfo("个人物品推送失败"));
		}
		JkfResult result = jKFResult.getData().getBody().getList().get(0);
		if (null != result) {
			StringBuffer stringBuffer = new StringBuffer();
			for (JkfResultDetail detail : result.getResultList()) {
				stringBuffer.append(detail.getResultInfo()); 
			}
			if(CheckResult.SUCCESS.result.equals(Integer.valueOf(result.getChkMark()))){
				//更新订单推送状态	
				logger.info("[PUSH_PERSONALGOODS_INFO][{}]清关单推送成功", subOrder.getOrderCode());
				return new ResultInfo<>(Boolean.TRUE);
			}else{	//推送失败
				logger.error("[PUSH_PERSONALGOODS_INFO][{}]清关单推送失败:{}", subOrder.getOrderCode(), stringBuffer.toString());
				return new ResultInfo<>(new FailInfo("推送失败：" + stringBuffer.toString()));
			}
		}
		return new ResultInfo<>(new FailInfo("清单申报失败"));		
	}
	
	private FailInfo validateBuildPersonalgoodsDecl(PersonalgoodsDeclareInfo decl, SubOrder subOrder, 
			OrderConsignee orderConsignee,  List<OrderItem> lines, 
			Map<String, ItemSkuArt> sku2ItemArtMap, Map<String, CustomsDistrictInfo> sku2DistrictInfoMap, Map<String, 
			CustomsUnitInfo> sku2UnitInfoMap, Warehouse warehouse, SupplierCustomsRecordation supplierCustoms){
		if (warehouse.getImportType() == null || warehouse.getImportType() == -1) {
			return new FailInfo("进口类型不能为空");
		}
		if (StringUtil.isEmpty(warehouse.getDeclareType())) {
			return new FailInfo("申报类型不存在");
		}
		if (null == DeclareCompanyType.getDeclareCompanyTypeDescByCode(Integer.parseInt(warehouse.getDeclareType()))) {
			return new FailInfo("申报类型错误");
		}
		if (StringUtil.isEmpty(warehouse.getDeclareCompanyCode())) {
			return new FailInfo("报关企业编码不能为空");
		}
		if (StringUtil.isEmpty(warehouse.getDeclareCompanyName())) {
			return new FailInfo("报关企业名称不能为空");
		}
		if (StringUtil.isEmpty(supplierCustoms.getRecordationName())) {
			return new FailInfo("电商企业备案名称不能为空");
		}
		if (StringUtil.isEmpty(supplierCustoms.getRecordationNum())) {
			return new FailInfo("电商企业备案编号不能为空");
		}
		if (StringUtil.isEmpty(warehouse.getIoSeaport())) {
			return new FailInfo("进出口岸不能为空");
		}
		if (StringUtil.isEmpty(warehouse.getDeclSeaport())) {
			return new FailInfo("申报口岸不能为空");
		}
		if (StringUtil.isEmpty(warehouse.getCustomsField())) {
			return new FailInfo("码头货场不能为空");
		}
		if (StringUtil.isEmpty(warehouse.getSenderName())) {
			return new FailInfo("发件人不能为空");
		}
		if (StringUtil.isEmpty(warehouse.getSenderCountryCode())) {
			return new FailInfo("发件人国家不能为空");
		}
		if (StringUtil.isEmpty(warehouse.getSenderCity())) {
			return new FailInfo("发件人城市不能为空");
		}	
		if (StringUtil.isEmpty(warehouse.getLogisticsCode())) {
			return new FailInfo("物流企业备案编码不能为空");
		}
		if (StringUtil.isEmpty(warehouse.getLogisticsName())) {
			return new FailInfo("物流企业备案名称不能为空");
		}
		
		if (warehouse.getImportType() == 1){
			if (StringUtil.isEmpty(warehouse.getAccountBookNo())) {
				return new FailInfo("账册编号不能为空");
			}
			if (StringUtil.isEmpty(warehouse.getApplicationFormNo())) {
				return new FailInfo("申请单编号不能为空");
			}
			if (StringUtil.isEmpty(warehouse.getStorageCode())) {
				return new FailInfo("仓储企业编码不能为空");
			}
			if (StringUtil.isEmpty(warehouse.getStorageName())) {
				return new FailInfo("仓储企业名称不能为空");
			}
		}else if(warehouse.getImportType() == 0){ //直邮
			if (StringUtil.isEmpty(decl.getBillNo())){
				return new FailInfo("总提运单号不能为空");
			}
			if (StringUtil.isEmpty(decl.getVoyageNo())){
				return new FailInfo("航班号不能为空");
			}
			if (StringUtil.isEmpty(decl.getTrafNo())){
				return new FailInfo("运输工具编号不能为空");
			}
		}
		return null;
	}
	
	
	/** 构建个人物品申报请求数据 */
	private GoodsDeclareModule buildPersonalGoodsDecl(PersonalgoodsDeclareInfo decl, SubOrder subOrder, 
			OrderConsignee orderConsignee,  List<OrderItem> lines, 
			Map<String, ItemSkuArt> sku2ItemArtMap, Map<String, CustomsDistrictInfo> sku2DistrictInfoMap, Map<String, 
			CustomsUnitInfo> sku2UnitInfoMap, Warehouse warehouse, SupplierCustomsRecordation supplierCustoms){
		GoodsDeclareModule module = new GoodsDeclareModule();
		//组装JKFSign
		JkfGoodsDeclareRequest.JkfSign sign = module.getJkfSign();
		sign.setCompanyCode(getSenderCode(warehouse));  //发送方备案号
		sign.setBusinessType(JKFConstant.JKFBusinessType.PERSONAL_GOODS_DECLAR.type);
		sign.setDeclareType("1");
		sign.setBusinessNo(decl.getPreEntryNo());
		sign.setNote("");
		sign.setCebFlag(getCebFlag());
		
		//组装GoodsDeclare
		GoodsDeclare goodsDeclare = module.getGoodsDeclare();
		//保税进口或者直邮进口应该由供应商类型决定还是由仓库类型决定
		goodsDeclare.setImportType(decl.getImportType() != null ? decl.getImportType().toString() : "1");
		
		goodsDeclare.setAccountBookNo(warehouse.getAccountBookNo());
		goodsDeclare.setIeFlag("I");
		goodsDeclare.setPreEntryNumber(decl.getPreEntryNo());
		goodsDeclare.setInOutDateStr(DateUtil.format(new Date(), DateUtil.NEW_FORMAT));


		goodsDeclare.setDeclareCompanyType(DeclareCompanyType.getDeclareCompanyTypeDescByCode(Integer.parseInt(warehouse.getDeclareType())));
		goodsDeclare.setDeclareCompanyCode(warehouse.getDeclareCompanyCode());
		goodsDeclare.setDeclareCompanyName(warehouse.getDeclareCompanyName());
		goodsDeclare.seteCommerceCode(supplierCustoms.getRecordationNum());
		goodsDeclare.seteCommerceName(supplierCustoms.getRecordationName());
		goodsDeclare.setOrderNo(subOrder.getOrderCode().toString());		
		goodsDeclare.setWayBill(decl.getExpressNo());
		
		//贸易国:B2C资金流入的账户
		//总署接口要求：保税模式下，贸易国或者起运地必须为中国
		goodsDeclare.setTradeCountry(getTradeCountry(warehouse)); 
		goodsDeclare.setDestinationPort(DESTINATION_PORT);
		goodsDeclare.setIePort(warehouse.getIoSeaport());
		
		//总署逻辑要求保税区的运输方式必须为保税区,直邮需要按照实际填写
		goodsDeclare.setTrafMode(getTranfMode(warehouse, decl.getImportType()));
		
		goodsDeclare.setTrafName("");
		goodsDeclare.setVoyageNo(decl.getVoyageNo());
		goodsDeclare.setTrafNo(decl.getTrafNo());
		goodsDeclare.setBillNo(decl.getBillNo());
		
		Integer totalCount = 0;
		for (OrderItem orderItem : lines) {
			totalCount += (orderItem.getWrapQuantity()*orderItem.getQuantity());
		}
		goodsDeclare.setPackNo(totalCount);	//表体独立包装的物品总数，不考虑物品计量单位
		if(isDeclareZS()){
			goodsDeclare.setPackNo(1);//海关总署要求固定写1
		}
		
		goodsDeclare.setGrossWeight(getWeight(lines, 0));
		goodsDeclare.setNetWeight(getWeight(lines, 1));
		goodsDeclare.setWarpType("2");
		goodsDeclare.setDeclPort(warehouse.getDeclSeaport());
		goodsDeclare.setEnteringPerson("9999");
		goodsDeclare.setEnteringCompanyName("9999");
		goodsDeclare.setDeclarantNo("");
		goodsDeclare.setCustomsField(warehouse.getCustomsField());
		goodsDeclare.setSenderName(warehouse.getSenderName());
		goodsDeclare.setConsignee(orderConsignee.getName());
		goodsDeclare.setSenderCountry(warehouse.getSenderCountryCode());
		goodsDeclare.setSenderCity(warehouse.getSenderCity());
		
		//身份证
		goodsDeclare.setPaperType("1");
		goodsDeclare.setPaperNumber("");

		goodsDeclare.setCurrCode(TRADE_CURRENCY_RMB);
		goodsDeclare.setMainGName(lines.get(0).getSpuName());

		goodsDeclare.setApplicationFormNo(warehouse.getApplicationFormNo());
		goodsDeclare.setIsAuthorize("1");
		
		goodsDeclare.setInternalAreaCompanyNo(warehouse.getStorageCode());
		goodsDeclare.setInternalAreaCompanyName(warehouse.getStorageName());	
		goodsDeclare.setCompanyCode(xgCompanyCode);
		goodsDeclare.setCompanyName(xgCompanyName);
		goodsDeclare.setLogisCompanyCode(warehouse.getLogisticsCode());
		goodsDeclare.setLogisCompanyName(warehouse.getLogisticsName());
		
		goodsDeclare.setConsigneeAddress(getConsigneeAddress(orderConsignee));
		goodsDeclare.setPurchaserTelNumber(orderConsignee.getMobile());
		goodsDeclare.setBuyerIdType("1");
		goodsDeclare.setBuyerIdNumber(orderConsignee.getIdentityCard());
		goodsDeclare.setBuyerName(orderConsignee.getName());
		goodsDeclare.setFeeAmount(getOriginalFreightFee(lines).toString());
		goodsDeclare.setInsureAmount("0");
		goodsDeclare.setAssureCode(assureCode);
		
		Double worth = getOriginalFreightFee(lines); //价值：表体商品总价+运费+保费
		for (int i = 0; i < lines.size(); i++) {
			OrderItem orderItem = lines.get(i);
			ItemSkuArt art = sku2ItemArtMap.get(orderItem.getSkuCode());
			if (null == art) {
				logger.error("[PUSH_PERSONALGOODS_INFO][{}]商品行数据sku={}不存在海关备案信息", subOrder.getOrderCode(), orderItem.getSkuCode());
				throw new DeclareException("海关备案信息不存在");
			}
			CustomsDistrictInfo cdInfo = sku2DistrictInfoMap.get(orderItem.getSkuCode());
			if (null == cdInfo) {
				logger.error("[PUSH_PERSONALGOODS_INFO][{}]商品行数据sku={}不存在产地信息", subOrder.getOrderCode(), orderItem.getSkuCode());
				throw new DeclareException("产地信息不存在");
			}
			CustomsUnitInfo cuInfo = sku2UnitInfoMap.get(orderItem.getSkuCode());
			if (null == cuInfo) {
				logger.error("[PUSH_PERSONALGOODS_INFO][{}]商品行数据sku={}不存在计量单位信息", subOrder.getOrderCode(), orderItem.getSkuCode());
				throw new DeclareException("计量单位信息不存在");
			}
			GoodsDeclareDetail detail = new GoodsDeclareDetail();
			detail.setGoodsOrder(i + 1);
			detail.setCodeTs(art.getHsCode());
			detail.setItemRecordNo(art.getArticleNumber());
			detail.setGoodsItemNo(orderItem.getSkuCode());
			detail.setItemName(orderItem.getSpuName());
			detail.setGoodsName(art.getItemDeclareName());
			detail.setGoodsModel(art.getItemFeature());
			detail.setOriginCountry(cdInfo.getCode());
			detail.setTradeCurr(TRADE_CURRENCY_RMB);
			detail.setGoodsUnit(cuInfo.getCode());
			detail.setFirstUnit(art.getItemFirstUnitCode());
			Double firstCount = BigDecimal.valueOf(art.getItemFirstUnitCount()).multiply(BigDecimal.valueOf(orderItem.getQuantity())).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
			detail.setFirstCount(firstCount);
			if (!StringUtil.isEmpty(art.getItemSecondUnitCode())) {
				detail.setSecondUnit(art.getItemSecondUnitCode());
				Double secondCount = BigDecimal.valueOf(art.getItemSecondUnitCount()).multiply(BigDecimal.valueOf(orderItem.getQuantity())).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
				detail.setSecondCount(secondCount);
				logger.error("[buildPersonalGoodsDecl]firstCount = {}, firstUnit = {}, secondUnit = {}, secondCount = {}", 
						firstCount, art.getItemFirstUnitCount(), art.getItemSecondUnitCount(), secondCount);
			}
			
			detail.setProductRecordNo(art.getItemRecordNo());
			detail.setBarCode(orderItem.getBarCode());
			
			BigDecimal totalPrice = BigDecimal.valueOf(orderItem.getSalesPrice()).multiply(BigDecimal.valueOf(orderItem.getQuantity())).setScale(2, BigDecimal.ROUND_HALF_UP);			
			detail.setDeclPrice(orderItem.getSalesPrice());
			detail.setDeclTotalPrice(totalPrice.doubleValue());
			detail.setDeclareCount(orderItem.getQuantity()*orderItem.getUnitQuantity()); //实际数量，按计量单位算，多件装实际数量
			worth = totalPrice.add(BigDecimal.valueOf(worth)).doubleValue();
			module.getGoodsDeclareDetails().add(detail);
		}
		goodsDeclare.setWorth(worth);
		return module;
	}
	
	/*----------------------------------------------------------------------------------------------*/
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
	/*----------------------------------------------------------------------------------------------*/
	//获取运费
	private Double getOriginalFreightFee(List<OrderItem> lines){
		Double totalFee = 0.0;
		for (OrderItem orderItem : lines) {
			Double fee = orderItem.getOrigFreight();
			totalFee = BigDecimalUtil.add(totalFee, fee).doubleValue();
		}
		return BigDecimalUtil.toPrice(BigDecimal.valueOf(totalFee));	
	}
	
	/** 获取收货地址 */
	private String getConsigneeAddress(OrderConsignee orderConsignee){
		StringBuffer sbBuffer = new StringBuffer();
		sbBuffer.append(orderConsignee.getProvinceName());
		sbBuffer.append(orderConsignee.getCityName());
		sbBuffer.append(orderConsignee.getCountyName());
		sbBuffer.append(orderConsignee.getTownName());
		sbBuffer.append(orderConsignee.getAddress());
		return sbBuffer.toString();
	}
	
	/** 毛重(公斤)(克 -> 公斤) 0是毛重1是净重*/
	private Double getWeight(List<OrderItem> lines, int type){
		if (CollectionUtils.isEmpty(lines)) {
			return 0.0;
		}
		Double weight = 0.0;
		for (OrderItem orderItem : lines) {
			Double w = (type == 0?orderItem.getWeight():orderItem.getWeightNet());
			weight = BigDecimal.valueOf(weight).add(BigDecimal.valueOf(w).multiply(BigDecimal.valueOf(orderItem.getQuantity()))).doubleValue();
		}
		return BigDecimal.valueOf(weight/1000).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
	}
	
	/*----------------------------------------------------------------------------------------------*/
    /**
     * 生成个人物品申报单的流水号：4位电商编号+14位企业流水，电商平台/物流企业生成后发送服务平台
     */
	@Override
    public String getPreEntryNo(){
    	String dateStr = dateString();
    	String indexStr = indexString();
    	StringBuilder sb = new StringBuilder(CODE_LENGTH);
    	String ecompanyCode = xgCompanyCode.substring(xgCompanyCode.length() - 4);
    	return sb.append(ecompanyCode).append(dateStr).append(indexStr).toString();
    }
    
	/**
	 * 日期字符串
	 */
	private String dateString() {
		Calendar currentTime = Calendar.getInstance();
		return new SimpleDateFormat(DATE_PATTERN).format(currentTime.getTime());
	}

	/**
	 * 自增码
	 */
	private String indexString() {
		Long index = jedisDBUtil.incr(INDEX_KEY);
		if (null == index) {
			logger.error("生成个人物品申报序列编号异常：redis服务器获取自增值为空");
			index = System.currentTimeMillis();
		}
		String idxStr = index.toString();
		int len = idxStr.length();
		StringBuilder sb = new StringBuilder(idxStr);
		if (len < INDEX_STRING_LENGTH) {
			return String.format("%08d", index);
		} else {
			return sb.delete(0,sb.length()-INDEX_STRING_LENGTH).toString();
		}
	}
	
	private String getSenderCode(Warehouse warehouse){
		if(isDeclareZS()){
			return warehouse.getDeclareCompanyCode(); //推送总署情况下,清单由报关企业推送
		}else{
			return xgCompanyCode;
		}
	}
	
	private String getCebFlag(){
		if(isDeclareZS()){
			return CebFlag.DECLARE_ZS_ZJPORT.flag;
		}else{
			return CebFlag.DECLARE_HZ.flag;
		}
	}
	
	private boolean isDeclareZS(){
		return isSendCeb;
	}
	
	private String getTradeCountry(Warehouse warehouse){
		if (StringUtil.isEmpty(warehouse.getTradeCountry())){
			return TRADE_COUNTRY_CODE;
		}
		return warehouse.getTradeCountry();
	}
	
	private String getTranfMode(Warehouse warehouse, Integer importType){
		if (importType == null) return TransF.BONDED_AREA.code;
		if(StringUtil.isEmpty(warehouse.getTrafMode())){
			if(1 == importType) {
				return TransF.BONDED_AREA.code;
			}else if (0 == importType) {
				return TransF.AIR_TRANS.code;
			}
		}
		return warehouse.getTrafMode();
	}
}
