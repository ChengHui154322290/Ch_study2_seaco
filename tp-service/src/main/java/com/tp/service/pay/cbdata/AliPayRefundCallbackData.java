package com.tp.service.pay.cbdata;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tp.dto.pay.cbdata.RefundCallbackData;
import com.tp.util.DateUtil;

public class AliPayRefundCallbackData implements RefundCallbackData, Serializable{
	protected Logger log = LoggerFactory.getLogger(getClass());
	/**
	 * 
	 */
	private static final long serialVersionUID = 195127974399137096L;
	private Map<String, String> sParams;
	public AliPayRefundCallbackData(Map<String, String> sParams){
		this.sParams = sParams;
	}
	
	@Override
	public String getRefundNo() {
		return sParams.get("batch_no").substring(8, sParams.get("batch_no").length());
	}

	@Override
	public boolean isAlipayRefund() {
		return true;
	}
	public List<List<String>> getAlipayRefundResultDetail(){
		List<List<String>> resultDetail = new ArrayList<List<String>>();
		log.info("result_details {}", sParams.get("result_details"));
		if(sParams.get("result_details") == null)
			return null;
		String[] results =  sParams.get("result_details").split("#");
		for(String result : results){
			String[] details = result.split("\\^");
			List<String> detailList = new ArrayList<String>();
			for(String detail : details){
				detailList.add(detail);
			}
			resultDetail.add(detailList);
		}
		return resultDetail;
	}
	@Override
	public String getGatewayTradeNo() {
		return sParams.get("trade_no");
	}

	@Override
	public String getRefundSerial() {
		return sParams.get("batch_no");
	}

	@Override
	public boolean isSuccess() {
		return true;
	}

	@Override
	public String getMessage() {
		return null;
	}

	@Override
	public String getCallbackInfo() {
		return sParams.get("result_details");
	}

	@Override
	public long getPaymentAmount() {
		String txt = sParams.get("total_fee");
		return txt == null ? 0L : Long.valueOf(txt);
	}

	@Override
	public String getPaymentGateway() {
		return "支付宝网关";
	}

	@Override
	public Date getCallBackTime() {
		String txt = sParams.get("notify_time");
		return StringUtils.isEmpty(txt) ? null : DateUtil.parse(txt, DateUtil.NEW_FORMAT);
	}

	@Override
	public String getCreateUserID() {
		return "mobile@xigouguoji.com";
	}

}
