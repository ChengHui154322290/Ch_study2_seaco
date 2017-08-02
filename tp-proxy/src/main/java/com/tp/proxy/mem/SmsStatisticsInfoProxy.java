package com.tp.proxy.mem;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.util.ExceptionUtils;
import com.tp.common.vo.PageInfo;
import com.tp.dto.common.FailInfo;
import com.tp.dto.common.ResultInfo;
import com.tp.dto.mem.SmsLimitInfoDto;
import com.tp.dto.mem.SmsStatisticsItemDto;
import com.tp.proxy.BaseProxy;
import com.tp.service.IBaseService;
import com.tp.service.mem.ISmsStatisticsService;

@Service
public class SmsStatisticsInfoProxy extends BaseProxy{

	@Autowired
	private ISmsStatisticsService smsStatisticsService;
	
	@Override
	public IBaseService getService() {
		return null;
	}
	
	public ResultInfo<SmsLimitInfoDto> querySmsLimitInfo(){
		try {
			return new ResultInfo<>(smsStatisticsService.queryLimitInfo());
		} catch (Throwable exception) {
			FailInfo failInfo = ExceptionUtils.print(new FailInfo(exception), logger, "");
			return new ResultInfo<>(failInfo);
		}	
	}
	
	public ResultInfo<PageInfo<SmsStatisticsItemDto>> queryDailyStatistics(String dayTime, PageInfo<SmsStatisticsItemDto> pageInfo){
		try {
			return new ResultInfo<>(smsStatisticsService.queryDailyStatistics(dayTime, pageInfo));
		} catch (Throwable exception) {
			FailInfo failInfo = ExceptionUtils.print(new FailInfo(exception), logger, dayTime);
			return new ResultInfo<>(failInfo);
		}	
	}
	
	public ResultInfo<Boolean> updateLimitInfo(SmsLimitInfoDto dto){
		try {
			return smsStatisticsService.updateLimitInfo(dto);
		} catch (Throwable exception) {
			FailInfo failInfo = ExceptionUtils.print(new FailInfo(exception), logger, dto);
			return new ResultInfo<>(failInfo);
		}
	}
}
