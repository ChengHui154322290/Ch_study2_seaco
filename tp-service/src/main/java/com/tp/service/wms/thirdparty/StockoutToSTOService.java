/**
 * 
 */
package com.tp.service.wms.thirdparty;

import java.util.ArrayList;
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
import com.tp.common.vo.wms.STOWmsConstant;
import com.tp.common.vo.wms.WmsConstant.PutStatus;
import com.tp.dao.wms.StockoutDao;
import com.tp.dto.common.FailInfo;
import com.tp.dto.common.ResultInfo;
import com.tp.model.prd.ItemSkuArt;
import com.tp.model.sys.RestLog;
import com.tp.model.wms.Stockout;
import com.tp.model.wms.StockoutDetail;
import com.tp.model.wms.sto.StoStockoutOrderDetail;
import com.tp.model.wms.sto.StoStockoutOrderRequest;
import com.tp.result.wms.StoWMSResultMessage;
import com.tp.service.prd.IItemSkuArtService;
import com.tp.service.sys.IRestLogService;
import com.tp.service.wms.thirdparty.IStockoutServiceToTP;
import com.tp.service.wms.thirdparty.sto.STOSoapClient;
import com.tp.util.Base64;

/**
 * @author Administrator
 * 申通仓
 */
@Service("stockoutToSTOService")
public class StockoutToSTOService implements IStockoutServiceToTP{
	
	private static final Logger logger = LoggerFactory.getLogger(StockoutToSTOService.class);
	
	private static final String SEND_WAREHOUSE = "西客商城";
	private static final String MERCHANT_NO = "XGGJ";

	@Value("#{meta['wms.zt.username']}")
	private String userName;
	
	@Value("#{meta['wms.zt.password']}")
	private String password;
	
	@Autowired
	private IItemSkuArtService itemSkuArtService;
	
	@Autowired
	private STOSoapClient stoSoapClient;
	
	@Autowired
	private StockoutDao stockoutDao;
	
	@Autowired
	private IRestLogService restLogService;
	
	@Override
	public boolean check(Stockout stockout) {
		if (StringUtils.isEmpty(stockout.getWmsCode()) || !stockout.getWmsCode().equals(WMSCode.STO_HZ.code)) {
			return false;
		}
		return true;
	}
	
	/**
	 * 推送出库订单 
	 * SKU是备案料号，是属于自造，并不是条形码
	 */
	@Override
	public ResultInfo<Boolean> deliverStockoutOrder(Stockout stockout){
		logger.info("[STOCKOUT][{}]推送出库单至申通WMS...:{}", 
				stockout.getOrderCode(), JSONObject.toJSONString(stockout));
		//参数验证
		FailInfo failInfo = validate(stockout);
		if (null != failInfo){
			logger.error("[STOCKOUT][{}]参数校验失败：{}",
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
				"bonded_area = " + ClearanceChannelsEnum.HANGZHOU.id + 
				" and sku in(" + StringUtils.join(skuCodes, SPLIT_SIGN.COMMA) + ")");
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
		Stockout so = new Stockout();
		so.setId(stockout.getId());
		ResultInfo<Boolean> message = new ResultInfo<>(Boolean.TRUE);
		try {
			//SKU转换
			StoStockoutOrderRequest request = convertStoStockoutOrderRequest(stockout, sku2ArtMap);
			//发送数据
			String json = JSONArray.toJSONString(request);
			String jsonData = Base64.encode(json.getBytes());
			logger.info("[STOCKOUT][{}]推送数据至申通WMS,原始数据：{}", stockout.getOrderCode(), json);
			String response = stoSoapClient.importOrder(jsonData);
			logger.info("[STOCKOUT][{}]推送数据至申通WMS,返回结果：{}", stockout.getOrderCode(), response);
			addRestfulLog(stoSoapClient.getUrl(), jsonData, RestLogType.D_STO_STOCKOUT.code, response);
			StoWMSResultMessage result = (StoWMSResultMessage)JSONObject.parseObject(response, StoWMSResultMessage.class);
			if (null == result) {
				logger.error("[STOCKOUT][{}]推送数据至申通WMS失败",stockout.getOrderCode());
				return new ResultInfo<>(new FailInfo("推送出库单失败"));
			}
			if (result.isSuccess()) {
				logger.info("[STOCKOUT][{}]推送出库单至申通WMS成功", stockout.getOrderCode());
				so.setStatus(PutStatus.SUCCESS.code);
				message = new ResultInfo<>(Boolean.TRUE);
			}else{
				logger.info("[STOCKOUT][{}]推送出库单至申通WMS失败,原因：{}", stockout.getOrderCode(), result.toString());
				so.setStatus(PutStatus.FAIL.code);
				so.setFailTimes(stockout.getFailTimes() + 1);
				so.setErrorMsg(result.getMsg());
				message = new ResultInfo<>(new FailInfo("推送出库单失败,原因:" + result.getMsg()));
			}		
		} catch (Exception e) {
			logger.error("[STOCKOUT][{}]推送出库订单异常", stockout.getOrderCode(), e);
			so.setStatus(PutStatus.FAIL.code);
			so.setFailTimes(stockout.getFailTimes() + 1);
			so.setErrorMsg("推送异常");			
			message = new ResultInfo<>(new FailInfo("推送出库订单异常"));
		}
		stockoutDao.updateNotNullById(so);
		return message;
	}
	
	/**
	 * 参数验证（只需要验证该平台必需的参数）
	 */
	private FailInfo validate(Stockout stockout){
		if (null == stockout) {
			return new FailInfo("出库单数据错误");
		}
		if (StringUtils.isEmpty(stockout.getLogisticsCompanyCode())) {
			return new FailInfo("物流公司编号为空");
		}
		if (StringUtils.isEmpty(stockout.getExpressNo())) {
			return new FailInfo("物流单号为空");
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
	private StoStockoutOrderRequest convertStoStockoutOrderRequest(Stockout stockout, Map<String, ItemSkuArt> sku2ArtMap) throws Exception{
		StoStockoutOrderRequest request = new StoStockoutOrderRequest();
		request.setUserName(userName);
		request.setPassword(password);
		request.setOrderList(new ArrayList<StoStockoutOrderDetail>());
		
		List<StoStockoutOrderDetail> soList = request.getOrderList();	
		for (StockoutDetail soDetail : stockout.getStockoutDetails()) {
			StoStockoutOrderDetail detail = new StoStockoutOrderDetail();
			detail.setTxLogisticID(stockout.getOrderCode());
			
			//收件人信息
			detail.setReceiveMan(stockout.getConsignee());
			detail.setReceiveProvince(stockout.getProvince());
			detail.setReceiveCity(stockout.getCity());
			detail.setReceiveCounty(stockout.getArea());
			detail.setReceiveManAddress(stockout.getAddress());
			detail.setReceiveManPhone(stockout.getMobile());
			//购买人
			detail.setBuyerName(stockout.getConsignee());
			detail.setBuyerIdNumber("000000000000000000");
			
			//商品信息
			ItemSkuArt art = sku2ArtMap.get(soDetail.getItemSku());
			if (null == art || StringUtils.isEmpty(art.getArticleNumber())) {
				logger.error("商品{}不存在杭州保税区备案信息", soDetail.getItemSku());
				throw new Exception("不存在杭州保税区备案信息");
			}
			detail.setItemSku(art.getArticleNumber());	  //公共仓SKU
			detail.setItemName(soDetail.getItemName());
			detail.setItemCount(soDetail.getQuantity());
			detail.setUnitPrice(0.0);
			detail.setAllPrice(0.0);
			detail.setFeeAmount(0.0);
			detail.setInsureAmount(0.0);
			detail.setItemWeight(0.0);
		
			//物流
			detail.setCarrier(STOWmsConstant.ExpressCompany.getStoCodeByCommonCode(stockout.getLogisticsCompanyCode()));
			detail.setMailNo(stockout.getExpressNo());
			
			//其他参数
			detail.setSendWarehouse(SEND_WAREHOUSE);
			detail.setMerchantNum(MERCHANT_NO);
			detail.setPc("");
			soList.add(detail);
		}
		return request;
	}
	
	//写入restful日志
	private void addRestfulLog(String url, String content, String type, String result){
		try {
			RestLog log = new RestLog(url, type, RequestMethod.WEBSERVICE.code, content, result);
			restLogService.insert(log);
		} catch (Exception e) {
			logger.error("写入REST日志异常", e);
		}
	}
}
