package com.tp.service.pay.cbdata;

import java.io.Serializable;
import java.text.ParseException;
import java.util.Date;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tp.dto.pay.cbdata.PayCallbackData;

public class AliPayCallbackData implements PayCallbackData, Serializable {
	Logger logger = LoggerFactory.getLogger(getClass());

	/**
     * 
     */
	private static final long serialVersionUID = 195127974399137096L;
	private Map<String, String> sParams;
	public AliPayCallbackData(Map<String, String> sParams) {
		this.sParams = sParams;
	}

	@Override
	public String getPaymentTradeNo() {
		return sParams.get("out_trade_no");
	}

	@Override
	public String getGatewayTradeNo() {
		return sParams.get("trade_no");
	}

	@Override
	public boolean isSuccess() {
		if("TRADE_FINISHED".equals(sParams.get("trade_status")) || "TRADE_SUCCESS".equals(sParams.get("trade_status")))
			return true;
		return false;
	}

	@Override
	public String getMessage() {
		return null;
	}

	@Override
	public String getCallbackInfo() {
		return sParams.get("error_code");
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
		try {
			return StringUtils.isEmpty(txt) ? null : DateUtils.parseDate(txt,
					"yyyy-MM-dd HH:mm:ss");
		} catch (ParseException e) {
			logger.error(e.getMessage(), e);
			return null;
		}
	}

	@Override
	public String getCreateUserID() {
		String userid = null;
		if (sParams.get("seller_id") != null)
			userid = sParams.get("seller_id");
		else if (sParams.get("seller_acount_name") != null)
			userid = sParams.get("seller_acount_name");
		else
			userid = sParams.get("buyer_email");
		return userid;
	}

	public static void main(String[] args) throws Exception {
		System.out.println(DateUtils.parseDate("2015-03-17 12:18:00",
				"yyyy-MM-dd hh:mm:ss"));
		System.out.println(DateUtils.parseDate("2015-03-17 13:18:00",
				"yyyy-MM-dd h:mm:ss"));
		System.out.println(DateUtils.parseDate("2015-03-17 13:18:00",
				"yyyy-MM-dd h:mm:ss"));
	}

}
