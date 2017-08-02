/**
 * 
 */
package com.tp.service.wms.thirdparty;

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

import com.alibaba.fastjson.JSONObject;
import com.tp.common.vo.sys.CommonLogConstant.RequestMethod;
import com.tp.common.vo.sys.CommonLogConstant.RestLogType;
import com.tp.common.vo.wms.STOWmsWaybillConstant;
import com.tp.common.vo.wms.WmsWaybillConstant;
import com.tp.common.vo.wms.WmsConstant.ExpressCompany;
import com.tp.common.vo.wms.WmsWaybillConstant.PutStatus;
import com.tp.dao.wms.logistics.WaybillInfoDao;
import com.tp.dto.common.FailInfo;
import com.tp.dto.common.ResultInfo;
import com.tp.model.sys.RestLog;
import com.tp.model.wms.WaybillApplication;
import com.tp.model.wms.WaybillDetail;
import com.tp.model.wms.WaybillInfo;
import com.tp.model.wms.logistics.StoWaybillInfoRequest;
import com.tp.result.wms.WaybillResultInfo;
import com.tp.service.sys.IRestLogService;
import com.tp.service.wms.thirdparty.IWaybillToTPService;
import com.tp.util.Base64;
import com.tp.util.HttpClientUtil;
import com.tp.util.StringUtil;

/**
 * @author Administrator
 *	申通快递运单服务
 */
@Service
public class WaybillToSTOService implements IWaybillToTPService{

	private static final Logger logger = LoggerFactory.getLogger(WaybillToSTOService.class);
	
	//用户名
	@Value("#{meta['wms.sto.user']}")
	private String stoUserName;
	
	//密码
	@Value("#{meta['wms.sto.password']}")
	private String stoPassword;
	
	//签名
	@Value("#{meta['wms.sto.sign']}")
	private String stoSign;
	
	@Value("#{meta['wms.sto.getbillurl']}")
	private String stoGetBillUrl;
	
	@Value("#{meta['wms.sto.sendOrderUrl']}")
	private String stoSendOrderUrlOfBS; //保税
	
	@Value("#{meta['wms.sto.zysendOrderUrl']}")
	private String stoSendOrderUrlOfZY; //直邮
	
	@Value("#{meta['XG.CC.isSendCeb']}")
	private boolean isSendCeb;
	
	@Autowired
	private WaybillInfoDao waybillInfoDao;
	
	@Autowired
	private IRestLogService restLogService;
	
	
	@Override
	public boolean checkApplyWaybill(ExpressCompany company, int count) {
		if (ExpressCompany.STO.commonCode.equals(company.getCommonCode())) {
			return true;
		}
		return false;
	}

	@Override
	public boolean checkPushWaybillInfo(WaybillInfo waybillInfo) {
		if (ExpressCompany.STO.commonCode.equals(waybillInfo.getLogisticsCode())) {
			return true;
		}
		return false;
	}
	
	@Override
	public WaybillApplication applyWaybill(ExpressCompany company, int count) {
		logger.info("[APPLY_WAYBILLS][申通快递]向申通快递批量申请运单号：{}", company);
		WaybillApplication application = new WaybillApplication();
		application.setAmount(Long.valueOf(count));
		application.setLogisticsCode(company.getCommonCode());
		application.setLogisticsName(company.getDesc());
		application.setCreateTime(new Date());
		application.setApplyTime(new Date());
		application.setUpdateTime(new Date());
		try {		
			Map<String, String> params = new HashMap<>();
			params.put("User", stoUserName);
			params.put("Password", stoPassword);
			params.put("Sign", stoSign);
			params.put("len", count + "");	
			logger.info("[APPLY_WAYBILLS][申通快递][url={}][param={}]批量请求运单号", stoGetBillUrl, params);
			String response = HttpClientUtil.postData(stoGetBillUrl, params, "UTF-8");
			logger.info("[APPLY_WAYBILLS][申通快递]申请快递单号返回数据:{}", response);
			addRestfulLog(stoGetBillUrl, params, RestLogType.R_STO_APPLYWAYBILL.code, response);
			response = response.replace("\\\"", "\"").replaceAll("^\"(.+)\"$", "$1");
			
			WaybillResultInfo result = (WaybillResultInfo)JSONObject.parseObject(response, WaybillResultInfo.class);	
			application.setResultMsg(result.getResultMsg());
			if (!STOWmsWaybillConstant.ErrorCode.SUCCESS.code.equals(result.getResultCode())) {	//成功
				application.setStatus(1);
				return application;
			}
			String[] spilt = StringUtil.split(result.getResultMsg(), "-");
			if (spilt.length != 2) {
				logger.error("[APPLY_WAYBILLS][申通快递]申请快递单号数据异常：{}", result.getResultMsg());
				application.setStatus(1);
				return application;
			}
			Long waybillLow = Long.valueOf(spilt[0]);
			Long waybillUp = Long.valueOf(spilt[1]);
			Long actualCount = waybillUp - waybillLow + 1;
			List<WaybillDetail> details = new ArrayList<>();
			for (Long i = waybillLow; i <= waybillUp; i++) {
				WaybillDetail detail = new WaybillDetail();
				detail.setWaybillNo(i);
				detail.setStatus(WmsWaybillConstant.WaybillStatus.UNUSE.code);
				detail.setCreateTime(new Date());
				detail.setUpdateTime(new Date());
				detail.setLogisticsCode(company.getCommonCode());
				detail.setLogisticsName(company.getDesc());
				detail.setCreateTime(new Date());
				detail.setUpdateTime(new Date());
				details.add(detail);
			}
			application.setActualAmount(actualCount);
			application.setDetails(details);
			application.setWaybillLow(waybillLow);
			application.setWaybillUp(waybillUp);
			application.setStatus(0); //0成功1失败
			logger.info("[APPLY_WAYBILLS][申通快递]批量申请运单成功:{}", JSONObject.toJSONString(application));
			return application;
		} catch (Exception e) {
			logger.error("[APPLY_WAYBILLS][申通快递]申请快递单号数据异常", e);
			application.setStatus(1);
			return application;
		}
	}

	/**
	 * 推送运单 
	 */
	@Override
	public ResultInfo<Boolean> pushWaybillInfo(WaybillInfo waybillInfo) {
		logger.info("[PUSH_WAYBILL][申通快递]推送运单数据:{}", JSONObject.toJSONString(waybillInfo));
		//参数验证
		ResultInfo<Boolean> validateResult = validate(waybillInfo);
		if (Boolean.TRUE != validateResult.isSuccess()) {
			logger.error("[PUSH_WAYBILL][申通快递]数据校验失败:{}", validateResult.getMsg().getDetailMessage());
			return validateResult;
		}
		//参数转换
		StoWaybillInfoRequest stoRequest = convertSTOWaybillInfoRequest(waybillInfo);
		WaybillInfo wbInfo = new WaybillInfo();
		wbInfo.setId(waybillInfo.getId());
		ResultInfo<Boolean> message = null;
		try {
			Map<String, String> params = new HashMap<>();
			String json = JSONObject.toJSONString(stoRequest);
			params.put("Sign", stoSign);		
			params.put("User", stoUserName);
			params.put("Password", stoPassword);			
			params.put("type", STOWmsWaybillConstant.STOWaybillType.getSTOTypeByCommonCode(waybillInfo.getType()).toString());
			params.put("JSON_OBJ", Base64.encode(json.getBytes()));
			logger.info("[PUSH_WAYBILL][申通快递] - 发送报关或者运单数据:{}", params);
			String response = sendRequest(params, waybillInfo);
			response = response.replace("\\\"", "\"").replaceAll("^\"(.+)\"$", "$1");
			WaybillResultInfo result = (WaybillResultInfo)JSONObject.parseObject(response, WaybillResultInfo.class);
			if (STOWmsWaybillConstant.ErrorCode.SUCCESS.code.equals(result.getResultCode())) {
				logger.info("[PUSH_WAYBILL][申通快递]发送报关或者运单数据成功");
				wbInfo.setStatus(PutStatus.SUCCESS.code);
				message = new ResultInfo<>(Boolean.TRUE);
			}else{
				logger.error("[PUSH_WAYBILL][申通快递]运单号{},订单号{}发送报关或者运单数据失败,errormsg:{}", 
						waybillInfo.getWaybillNo(), waybillInfo.getOrderCode(), result.getResultMsg());
				wbInfo.setStatus(PutStatus.FAIL.code);
				wbInfo.setFailTimes(waybillInfo.getFailTimes() + 1);
				wbInfo.setErrorMsg(result.getResultMsg());
				message = new ResultInfo<>(new FailInfo("推送申通运单失败," + result.getResultMsg()));
			}
			waybillInfoDao.updateNotNullById(wbInfo);
		} catch (Exception e) {
			logger.error("[PUSH_WAYBILL][申通快递]推送申通运单数据异常", e);
			message = new ResultInfo<>(new FailInfo("推送申通运单数据异常"));
		}
		return message;
	}
	
	private String sendRequest(Map<String, String> params, WaybillInfo waybillInfo) throws Exception{
		String url = null;
		if (waybillInfo.getImportType() != null && waybillInfo.getImportType() == 0){
			url = stoSendOrderUrlOfZY;
		}else{
			url = stoSendOrderUrlOfBS;
		}
		String response = HttpClientUtil.postData(url, params, "UTF-8");
		logger.info("[PUSH_WAYBILL][申通快递] - 发送报关或者运单数据返回数据:{}", response);
		addRestfulLog(url, params, RestLogType.D_STO_WAYBILLINFO.code, response);
		return response;
	}
	
	//参数转换
	private StoWaybillInfoRequest convertSTOWaybillInfoRequest(WaybillInfo waybillInfo){
		StoWaybillInfoRequest request = new StoWaybillInfoRequest();
		//基本信息
		request.setBill(waybillInfo.getWaybillNo());
		request.setGrossWeight(waybillInfo.getGrossWeight());
		request.setNetWeight(waybillInfo.getNetWeight());
		request.setOrdercode(waybillInfo.getOrderCode());
		request.setPackNo(waybillInfo.getPackAmount());
		
		//收件人信息
		request.setConsignee(waybillInfo.getConsignee());
		request.setConsigneeTel(waybillInfo.getMobile());
		String area = waybillInfo.getProvince() + waybillInfo.getCity() + waybillInfo.getArea();
		request.setConsigneeArea(area);
		request.setConsigneeAddress(area + waybillInfo.getAddress());
		
		//商品信息
		request.setGoodsName(waybillInfo.getMainGoodsName());
		request.setPackNo(waybillInfo.getPackAmount());
		if (isSendZS()){
			request.setPackNo("1");//总署接口要求包裹数据固定写1	
		}
		
		request.setWorth(waybillInfo.getWorth());
		
		//发件人
		request.setSendArea(waybillInfo.getSendRoughArea());
		
		//直邮信息
		request.setTotalWayBill(waybillInfo.getDeliveryNo());
		
		return request;
	}
	
	//参数验证
	private ResultInfo<Boolean> validate(WaybillInfo waybillInfo){
		if (null == waybillInfo.getType()) {
			return new ResultInfo<>(new FailInfo("运单类型为空"));
		}
		if (StringUtil.isEmpty(waybillInfo.getConsignee())) {
			return new ResultInfo<>(new FailInfo("收件人名称为空"));
		}
		if (StringUtil.isEmpty(waybillInfo.getProvince())) {
			return new ResultInfo<>(new FailInfo("收件人地址省份为空"));
		}
		if (StringUtil.isEmpty(waybillInfo.getCity())) {
			return new ResultInfo<>(new FailInfo("收件人地址城市为空"));
		}
		if (StringUtil.isEmpty(waybillInfo.getArea())) {
			return new ResultInfo<>(new FailInfo("收件人地址区域为空"));
		}
		if (StringUtil.isEmpty(waybillInfo.getAddress())) {
			return new ResultInfo<>(new FailInfo("收件人地址为空"));
		}
		if (StringUtil.isEmpty(waybillInfo.getMobile())) {
			return new ResultInfo<>(new FailInfo("收件人电话为空"));
		}
		if (StringUtil.isEmpty(waybillInfo.getGrossWeight())) {
			return new ResultInfo<>(new FailInfo("包裹毛重为空"));
		}
		if (StringUtil.isEmpty(waybillInfo.getNetWeight())) {
			return new ResultInfo<>(new FailInfo("包裹净重为空"));
		}
		if (StringUtil.isEmpty(waybillInfo.getSendRoughArea())) {
			return new ResultInfo<>(new FailInfo("发货模糊地址为空"));
		}
		if (StringUtil.isEmpty(waybillInfo.getWorth())) {
			return new ResultInfo<>(new FailInfo("包裹价值为空"));
		}
		return new ResultInfo<>(Boolean.TRUE);
	}
	//写入restful日志
	private void addRestfulLog(String url, Map<String, String> params, String type, String result){
		try {
			String content = JSONObject.toJSONString(params);
			RestLog log = new RestLog(url, type, RequestMethod.N_HTTP_POST.code, content, result);
			restLogService.insert(log);
		} catch (Exception e) {
			logger.error("写入REST日志异常", e);
		}
	}
	//是否发生总署
	private boolean isSendZS(){
		return isSendCeb;
	}
}
