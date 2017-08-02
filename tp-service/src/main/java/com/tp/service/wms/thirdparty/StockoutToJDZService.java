/**
 * 
 */
package com.tp.service.wms.thirdparty;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.tp.common.vo.Constant.SPLIT_SIGN;
import com.tp.common.vo.DAOConstant.MYBATIS_SPECIAL_STRING;
import com.tp.common.vo.bse.ClearanceChannelsEnum;
import com.tp.common.vo.stg.WarehouseConstant.WMSCode;
import com.tp.common.vo.sys.CommonLogConstant.RequestMethod;
import com.tp.common.vo.sys.CommonLogConstant.RestLogType;
import com.tp.common.vo.wms.JDZWmsConstant;
import com.tp.common.vo.wms.WmsConstant;
import com.tp.common.vo.wms.WmsConstant.InvoiceNeed;
import com.tp.common.vo.wms.WmsConstant.PutStatus;
import com.tp.dao.wms.StockoutDao;
import com.tp.dto.common.FailInfo;
import com.tp.dto.common.ResultInfo;
import com.tp.model.prd.ItemSkuArt;
import com.tp.model.sys.RestLog;
import com.tp.model.wms.Stockout;
import com.tp.model.wms.StockoutDetail;
import com.tp.model.wms.StockoutInvoice;
import com.tp.model.wms.jdz.JdzRequestUser;
import com.tp.model.wms.jdz.JdzStockoutOrderDetail;
import com.tp.model.wms.jdz.JdzStockoutOrderRequest;
import com.tp.result.wms.ResultMessage;
import com.tp.service.prd.IItemSkuArtService;
import com.tp.service.sys.IRestLogService;
import com.tp.service.wms.thirdparty.IStockoutServiceToTP;
import com.tp.util.DateUtil;
import com.tp.util.HttpClientUtil;

/**
 * @author Administrator
 * 浙江杭州保税区公共仓
 */
@Service("stockoutToJDZService")
public class StockoutToJDZService implements IStockoutServiceToTP{

	private static final Logger logger = LoggerFactory.getLogger(StockoutToJDZService.class);

	@Value("#{meta['wms.jdz.saleOrderurl']}")
	private String serviceUrl;
	
	@Value("#{meta['wms.jdz.appKey']}")
	private String jdzAppKey;
	
	@Value("#{meta['wms.jdz.password']}")
	private String jdzSecret;
	
	//电商备案编号
	@Value("#{meta['wms.jdz.providerCode']}")
	private String providerCode;
	
	//账册编号
	@Value("#{meta['wms.jdz.manualNo']}")
	private String manualNo;
	
	@Value("#{meta['wms.jdz.goodsOwner']}")
	private String goodsOwner;
	
	@Autowired
	private StockoutDao stockoutDao;
	
	@Autowired
	private IItemSkuArtService itemSkuArtService;
	
	@Autowired
	private IRestLogService restLogService;
	
	
	@Override
	public boolean check(Stockout stockout) {
		if (StringUtils.isEmpty(stockout.getWmsCode()) || !stockout.getWmsCode().equals(WMSCode.JDZ_HZ.code)) {
			return false;
		}
		return true;
	}
	
	/**
	 * 推送出库订单 
	 * sku是商品在海关备案的料号，也就是我们自己系统的货号，仓库方认为是条形码。
	 */
	@Override
	public ResultInfo<Boolean> deliverStockoutOrder(Stockout stockout){
		logger.info("[STOCKOUT][{}]推送公共仓出库单...", stockout.getOrderCode());
		//参数验证
		FailInfo failInfo = validate(stockout);
		if (null != failInfo) {
			logger.error("[STOCKOUT][{}]公共仓出库单数据校验错误:{}", 
					stockout.getOrderCode(), failInfo.getDetailMessage());
			return new ResultInfo<>(failInfo);
		}
		//组装数据
		List<String> skuCodes = new ArrayList<>();
		for (StockoutDetail detail : stockout.getStockoutDetails()) {
			skuCodes.add(detail.getItemSku());
		}
		//查询海关备案信息
		Map<String, Object> params = new HashMap<>();
		params.put(MYBATIS_SPECIAL_STRING.COLUMNS.name(), 
				"bonded_area = " + ClearanceChannelsEnum.HANGZHOU.id + " and sku in(" + StringUtils.join(skuCodes, SPLIT_SIGN.COMMA) + ")");
		List<ItemSkuArt> arts = itemSkuArtService.queryByParamNotEmpty(params);
		if (CollectionUtils.isEmpty(arts)) {
			logger.error("[STOCKOUT][{}]待推送商品杭州口岸备案信息为空：{}",
					stockout.getOrderCode(), StringUtils.join(skuCodes, SPLIT_SIGN.COMMA));
			return new ResultInfo<>(new FailInfo("海关备案信息不存在"));
		}
		Map<String, ItemSkuArt> sku2ArtMap = new HashMap<>();
		for (ItemSkuArt itemSkuArt : arts) {
			sku2ArtMap.put(itemSkuArt.getSku(), itemSkuArt);
		}
		ResultInfo<Boolean> message = new ResultInfo<>(Boolean.TRUE);
		try {
			//SKU转换
			JdzStockoutOrderRequest request = convertJdzStockoutOrderRequest(stockout, sku2ArtMap);
			//发送数据
			String serviceId = getServiceId();
			String json = JSONArray.toJSONString(request);
//			String content = JDZHelper.AESEncrypt.encrytor(json); //取消加密（2016.6.23）
			Map<String, String> headers = new HashMap<>();
			headers.put("serviceId", serviceId);
			logger.info("[STOCKOUT][{}]推送订单至公共仓,原始数据：{}", stockout.getOrderCode(), json);
			String response = HttpClientUtil.postData(serviceUrl, json, "application/json", headers);
			logger.info("[STOCKOUT][{}]推送订单至公共仓,返回结果：{}", stockout.getOrderCode(), response);
			addRestfulLog(serviceUrl, serviceId, json, RestLogType.D_JDZ_STOCKOUT.code, response);
			ResultMessage result = (ResultMessage)JSONObject.parseObject(response, ResultMessage.class);
			Stockout so = new Stockout();
			so.setId(stockout.getId());
			if (null == result) {
				logger.error("[STOCKOUT][{}]推送公共仓出库单失败", stockout.getOrderCode());
				return new ResultInfo<>(new FailInfo("推送出库单失败"));
			}
			if (result.isSuccess()) {
				logger.info("[STOCKOUT][{}]推送出库单至公共仓成功", stockout.getOrderCode());
				so.setStatus(PutStatus.SUCCESS.code);
				message = new ResultInfo<>(Boolean.TRUE);
			}else{
				logger.error("[STOCKOUT][{}]推送出库单至公共仓失败,原因：{}", stockout.getOrderCode(), result.toString());
				so.setStatus(PutStatus.FAIL.code);
				so.setFailTimes(stockout.getFailTimes() + 1);
				so.setErrorMsg(result.getError());
				message = new ResultInfo<>(new FailInfo("推送出库单失败,原因:" + result.getError()));
			}
			stockoutDao.updateNotNullById(so);
			return message;
		} catch (Exception e) {
			logger.error("[STOCKOUT][{}]推送出库订单异常", stockout.getOrderCode(), e);
			return new ResultInfo<>(new FailInfo("推送出库订单异常"));
		}
	}
	
	/**
	 * 参数验证（只需要验证该平台必需的参数）
	 */
	private FailInfo validate(Stockout stockout){
		if (null == stockout) {
			return new FailInfo("出库单数据错误");
		}
		if (!WMSCode.JDZ_HZ.getCode().equals(stockout.getWmsCode())) {
			return new FailInfo("仓库编号错误");
		}
		if (StringUtils.isEmpty(stockout.getLogisticsCompanyCode())) {
			return new FailInfo("物流公司编号为空");
		}
		if (StringUtils.isEmpty(stockout.getExpressNo())) {
			return new FailInfo("物流单号为空");
		}
		if (null != stockout.getIsInvoice() 
				&& InvoiceNeed.YES.code.equals(stockout.getIsInvoice())
				&& CollectionUtils.isNotEmpty(stockout.getStockoutInvoices())) {
			for (StockoutInvoice invoice : stockout.getStockoutInvoices()) {
				if (StringUtils.isEmpty(invoice.getInvoiceAmount())) {
					return new FailInfo("发票金额不能为空");
				}
				if (StringUtils.isEmpty(invoice.getInvoiceTitle())) {
					return new FailInfo("发票抬头不能为空");
				}
			}
		}
		if (CollectionUtils.isEmpty(stockout.getStockoutDetails())) {
			return new FailInfo("出库单详情为空");
		}
		return null;
	}
	
	/**
	 * 出库单转换
	 * @throws Exception 
	 */
	private JdzStockoutOrderRequest convertJdzStockoutOrderRequest(Stockout stockout, Map<String, ItemSkuArt> sku2ArtMap) throws Exception{
		JdzStockoutOrderRequest request = new JdzStockoutOrderRequest();
		request.setOrderCode(stockout.getOrderCode());
		request.setWarehouseCode(stockout.getWmsCode());
		request.setFromType("1");
		request.setFromCode("1");
		request.setPlatformCode("2");//其他
		request.setPlatformName("其他");
		request.setIsUrgency(stockout.getIsUrgency()); //普通订单
		request.setOrderTime(DateUtil.format(stockout.getOrderCreateTime(), "yyyy-MM-dd HH:mm:ss"));
		request.setPayTime(DateUtil.format(stockout.getPayTime(), "yyyy-MM-dd HH:mm:ss"));
		request.setAuditTime(DateUtil.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
		request.setAuditorCode("01");
		
		//物流信息
		request.setLogisticsCompanyCode(JDZWmsConstant.ExpressCompany.getJdzCodeByCommonCode(stockout.getLogisticsCompanyCode()));
		request.setLogisticsCompanyName(JDZWmsConstant.ExpressCompany.getJdzNameByCommonCode(stockout.getLogisticsCompanyCode()));
		request.setExpressNo(stockout.getExpressNo());
		
		request.setPostage(stockout.getPostage().toString());
		request.setIsDeliveryPay(stockout.getIsDeliveryPay());
		request.setShopCode("01");
		request.setShopName("西客商城");
		
		//收货人信息
		request.setMember(stockout.getMemberName());
		request.setConsignee(stockout.getConsignee());
		request.setPostcode(stockout.getPostCode());
		request.setProvinceName(stockout.getProvince());
		request.setCityName(stockout.getCity());
		request.setAreaName(stockout.getArea());
		request.setAddress(stockout.getAddress());
		request.setMobile(stockout.getMobile());
		request.setTel(stockout.getMobile());
		request.setSellersMessage("卖家好评");
		request.setBuyerMessage("买家好评");
		request.setMerchantMessage("商家好评");
		request.setInternalMemo("内部标签");
		request.setAmountReceivable(stockout.getTotalAmount());
		request.setActualPayment(stockout.getPayAmount());
		request.setIsPostagePay(stockout.getIsPostagePay());
		
		//发票信息
		request.setIsInvoice(stockout.getIsInvoice());
		if (WmsConstant.InvoiceNeed.YES.code.equals(stockout.getIsInvoice()) &&
				CollectionUtils.isNotEmpty(stockout.getStockoutInvoices())) {
			String invoiceName = "";
			String invoicePrice = "";
			String invoiceText = "";
			for (StockoutInvoice invoice : stockout.getStockoutInvoices()) {
				invoiceName += invoice.getInvoiceTitle() + ";";
				invoicePrice += invoice.getInvoiceAmount() + ";";
				invoiceText += invoice.getInvoiceContent() + ";";
			}
			request.setInvoiceName(invoiceName);
			request.setInvoicePrice(invoicePrice);
			request.setInvoiceText(invoiceText);
		}
		
		//账户信息
		request.setProviderCode(providerCode);
		request.setGoodsOwner(goodsOwner);
		//备用字段
		request.setRemain1("Remain1");
		request.setRemain2("Remain2");
		request.setRemain3("Remain3");
		request.setRemain4("Remain4");
		request.setRemain5("Remain5");
		
		//出库单详情
//		int i = 201;
		List<JdzStockoutOrderDetail> items = new ArrayList<>();
		for (StockoutDetail stockoutDetail : stockout.getStockoutDetails()) {
			JdzStockoutOrderDetail detail = new JdzStockoutOrderDetail();
			ItemSkuArt art = sku2ArtMap.get(stockoutDetail.getItemSku());
			if (null == art || StringUtils.isEmpty(art.getArticleNumber())) {
				logger.error("[STOCKOUT][{}]商品{}不存在杭州保税区备案信息", stockoutDetail.getItemSku());
				throw new Exception("不存在杭州保税区备案信息");
			}
			detail.setSku(art.getArticleNumber());	  //公共仓SKU
//			detail.setSku("" + i++);
			detail.setQty(stockoutDetail.getQuantity());
			detail.setPrice(stockoutDetail.getSalesPrice());
			detail.setNsku(stockoutDetail.getItemSku());  //平台SKU
			detail.setActualAmount(0.0);
			detail.setDiscount(0.0);
			detail.setAmountPayable(0.0);
			detail.setManualId(manualNo);
			detail.setRemain2("Remain2");
			detail.setRemain3("Remain3");
			detail.setRemain4("Remain4");
			detail.setRemain5("Remain5");
			items.add(detail);
		}
		request.setItems(items);
		return request;
	}
	
	
	
	private String getServiceId(){
		JdzRequestUser user = new JdzRequestUser();
		user.setAppkey(jdzAppKey);
		user.setSecret(JdzRequestUser.encryptSecret(jdzSecret));
		return JSONObject.toJSONString(user);
	}
	
	//写入restful日志
	private void addRestfulLog(String url, String serviceId, String data, String type, String result){
		try {
			Map<String, String> params = new HashMap<>();
			params.put("serviceId", serviceId);
			params.put("data", data);
			String content = JSONObject.toJSONString(params);
			RestLog log = new RestLog(url, type, RequestMethod.N_HTTP_POST.code, content, result);
			restLogService.insert(log);
		} catch (Exception e) {
			logger.error("写入REST日志异常", e);
		}
	}
}
