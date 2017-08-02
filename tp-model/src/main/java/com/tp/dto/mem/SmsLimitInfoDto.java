/**
 * 
 */
package com.tp.dto.mem;

import com.tp.model.BaseDO;

/**
 * @author Administrator
 *
 */
public class SmsLimitInfoDto extends BaseDO{
	
	private static final long serialVersionUID = -4911568819293016927L;

	/** 每日限制总数(前台) -1表示不限制*/
	private Integer dailyLimitCount;
	
	/** 单个手机号每日次数限制(前台)：-1表示不限制 */
	private Integer singleDailyLimitCount;
	

	public Integer getDailyLimitCount() {
		return dailyLimitCount;
	}

	public void setDailyLimitCount(Integer dailyLimitCount) {
		this.dailyLimitCount = dailyLimitCount;
	}

	public Integer getSingleDailyLimitCount() {
		return singleDailyLimitCount;
	}

	public void setSingleDailyLimitCount(Integer singleDailyLimitCount) {
		this.singleDailyLimitCount = singleDailyLimitCount;
	}
}
