/**
 * 
 */
package com.tp.service.mem;

import com.tp.common.vo.PageInfo;
import com.tp.dto.common.ResultInfo;
import com.tp.dto.mem.SmsLimitInfoDto;
import com.tp.dto.mem.SmsStatisticsItemDto;

/**
 * @author Administrator
 * 短信统计
 */
public interface ISmsStatisticsService {

	/**
	 * 短信发送统计
	 * @param mobile
	 * @param success true发送成功false发送失败 
	 */
	void statisticsSmsSend(String mobile, boolean success);
	
	/**
	 * 更新限制条件
	 */
	ResultInfo<Boolean> updateLimitInfo(SmsLimitInfoDto limitInfo);
	
	/**
	 * 查询限制条件 
	 */
	SmsLimitInfoDto queryLimitInfo();
	
	/**
	 * 查询统计数据 
	 */
	PageInfo<SmsStatisticsItemDto> queryDailyStatistics(String dayTime, PageInfo<SmsStatisticsItemDto> pageInfo);
	
	/**
	 * 校验限制
	 */
	boolean checkDailyLimit(String mobile);
}
