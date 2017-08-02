/**
 * 
 */
package com.tp.model.mem;

import java.util.HashMap;
import java.util.Map;

import com.tp.model.BaseDO;

/**
 * @author Administrator
 * 短信统计数据
 */
public class SmsStatisticsInfo extends BaseDO{
	
	private static final long serialVersionUID = -6462549250511058913L;
	/** 发送总数 */
	private Integer amount;
	
	/** 手机号统计列表 */
	private Map<String, SmsStatisticsItem> mobileStatistics;
	
	/** 每日统计 */
	private Map<String, Integer> dailyStatistics;
	
	
	public SmsStatisticsInfo() {
		amount = 0;
		mobileStatistics = new HashMap<>();
		dailyStatistics = new HashMap<>();
	}

	public Integer getAmount() {
		return amount;
	}

	public void setAmount(Integer amount) {
		this.amount = amount;
	}

	public Map<String, SmsStatisticsItem> getMobileStatistics() {
		return mobileStatistics;
	}

	public void setMobileStatistics(Map<String, SmsStatisticsItem> mobileStatistics) {
		this.mobileStatistics = mobileStatistics;
	}

	public Map<String, Integer> getDailyStatistics() {
		return dailyStatistics;
	}

	public void setDailyStatistics(Map<String, Integer> dailyStatistics) {
		this.dailyStatistics = dailyStatistics;
	}
}
