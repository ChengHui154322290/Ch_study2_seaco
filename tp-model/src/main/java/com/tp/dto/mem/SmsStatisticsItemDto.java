/**
 * 
 */
package com.tp.dto.mem;

import com.tp.model.BaseDO;

/**
 * @author Administrator
 *
 */
public class SmsStatisticsItemDto extends BaseDO{

	private static final long serialVersionUID = 3444684303587768583L;

	private String dayTime;
	
	private Integer dailyAmount;

	public String getDayTime() {
		return dayTime;
	}

	public void setDayTime(String dayTime) {
		this.dayTime = dayTime;
	}

	public Integer getDailyAmount() {
		return dailyAmount;
	}

	public void setDailyAmount(Integer dailyAmount) {
		this.dailyAmount = dailyAmount;
	}
}
