/**
 * 
 */
package com.tp.model.mem;

import java.util.HashMap;
import java.util.Map;

import com.tp.model.BaseDO;

/**
 * @author Administrator
 * 单个手机号统计
 */
public class SmsStatisticsItem extends BaseDO{

	private static final long serialVersionUID = -6070305929656673943L;

	/** 手机号 */
	private String mobile;
	
	/** 总短信次数 */
	private Integer totalAmount;
	
	/** 每日统计 */
	private Map<String, Integer> dailyStatistics;
	
	public SmsStatisticsItem() {
		this.mobile = null;
		this.totalAmount = null;
		this.dailyStatistics = null;
	}
	
	public SmsStatisticsItem(String mobile){
		this.mobile = mobile;
		this.totalAmount = 0;
		this.dailyStatistics = new HashMap<>();
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public Integer getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(Integer totalAmount) {
		this.totalAmount = totalAmount;
	}

	public Map<String, Integer> getDailyStatistics() {
		return dailyStatistics;
	}

	public void setDailyStatistics(Map<String, Integer> dailyStatistics) {
		this.dailyStatistics = dailyStatistics;
	}
}
