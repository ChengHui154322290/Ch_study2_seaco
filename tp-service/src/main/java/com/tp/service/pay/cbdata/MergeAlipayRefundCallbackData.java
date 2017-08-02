package com.tp.service.pay.cbdata;


import java.io.Serializable;
import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tp.dto.pay.cbdata.RefundCallbackData;
import com.tp.util.DateUtil;

public class MergeAlipayRefundCallbackData implements RefundCallbackData, Serializable{
	protected Logger log = LoggerFactory.getLogger(getClass());
	/**
	 * 
	 */
	private static final long serialVersionUID = 195127974399137096L;
	private Map<String, String> sParams;
	public MergeAlipayRefundCallbackData(Map<String, String> sParams){
		this.sParams = sParams;
	}
	
	@Override
	public String getRefundNo() {
		return sParams.get("out_return_no");
	}

	@Override
	public boolean isAlipayRefund() {
		return false;
	}
	public List<List<String>> getAlipayRefundResultDetail(){
		return null;
	}
	@Override
	public String getGatewayTradeNo() {
		return null;
	}

	@Override
	public String getRefundSerial() {
		return sParams.get("out_return_no");
	}

	@Override
	public boolean isSuccess() {
		return "REFUND_SUCCESS".equals(sParams.get("refund_status"));
	}

	@Override
	public String getMessage() {
		return sParams.get("error_code");
	}

	@Override
	public String getCallbackInfo() {
		return sParams.get("error_code");
	}

	@Override
	public long getPaymentAmount() {
		String txt = sParams.get("return_amount");
		return txt == null ? 0L : Long.valueOf(txt);
	}

	@Override
	public String getPaymentGateway() {
		return "支付宝网关";
	}

	@Override
	public Date getCallBackTime() {
		String txt = sParams.get("notify_time");
		try {
			return StringUtils.isEmpty(txt) ? null : DateUtil.parse(txt, DateUtil.NEW_FORMAT);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public String getCreateUserID() {
		return "liuzhoujing@meitunmama.com";
	}

}
