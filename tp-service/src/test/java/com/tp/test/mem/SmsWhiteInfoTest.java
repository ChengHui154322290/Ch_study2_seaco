/**
 * 
 */
package com.tp.test.mem;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.tp.dto.mem.SmsLimitInfoDto;
import com.tp.service.mem.ISmsStatisticsService;
import com.tp.service.mem.ISmsWhiteInfoService;
import com.tp.test.BaseTest;

/**
 * @author Administrator
 *
 */
public class SmsWhiteInfoTest extends BaseTest{

	@Autowired
	private ISmsWhiteInfoService smsWhiteInfoService;
	
	@Autowired
	private ISmsStatisticsService smsStatisticsService;
	
	@Test
	public void testCheckMobile(){
		String mobile = "13761373570";
		if (smsWhiteInfoService.checkSendSms(mobile)) {
			System.out.println(mobile + "可以发送短信");
		}else{
			System.out.println(mobile + "不允许发送短信");
		}
	}
	
	@Test
	public void testStatistics(){
		smsStatisticsService.statisticsSmsSend("13761373570", true);
	}
	
	@Test
	public void testSetLimit(){
		SmsLimitInfoDto dto = new SmsLimitInfoDto();
		dto.setDailyLimitCount(4);
		dto.setSingleDailyLimitCount(4);
		smsStatisticsService.updateLimitInfo(dto);
	}
	
}
