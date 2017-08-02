/**
 * 
 */
package com.tp.proxy.mem;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.util.ExceptionUtils;
import com.tp.common.vo.PageInfo;
import com.tp.dto.common.FailInfo;
import com.tp.dto.common.ResultInfo;
import com.tp.model.mem.SmsWhiteInfo;
import com.tp.proxy.BaseProxy;
import com.tp.service.IBaseService;
import com.tp.service.mem.ISmsStatisticsService;
import com.tp.service.mem.ISmsWhiteInfoService;

/**
 * @author Administrator
 *
 */
@Service
public class SmsWhiteInfoProxy extends BaseProxy{

	@Autowired
	private ISmsWhiteInfoService smsWhiteInfoService;
	
	@Autowired ISmsStatisticsService smsStatisticsService;
	
	@Override
	public IBaseService getService() {
		return null;
	}

	public ResultInfo<SmsWhiteInfo> querySmsWhiteInfo(){
		try {
			return smsWhiteInfoService.querySmsWhiteInfo();
		} catch (Throwable exception) {
			FailInfo failInfo = ExceptionUtils.print(new FailInfo(exception), logger, "");
			return new ResultInfo<>(failInfo);
		}
	}
	
	public ResultInfo<PageInfo<String>> queryWhiteInfo(String mobile, PageInfo<String> pageInfo){
		try {
			return new ResultInfo<>(smsWhiteInfoService.queryWhiteInfo(mobile, pageInfo));
		} catch (Throwable exception) {
			FailInfo failInfo = ExceptionUtils.print(new FailInfo(exception), logger, mobile);
			return new ResultInfo<>(failInfo);
		}
	}
	
	public ResultInfo<Boolean> updateSmsWhiteInfoStatus(Integer status){
		try {
			return smsWhiteInfoService.updateSmsWhiteInfoStatus(status);
		} catch (Throwable exception) {
			FailInfo failInfo = ExceptionUtils.print(new FailInfo(exception), logger, status);
			return new ResultInfo<>(failInfo);
		}
	}


	public ResultInfo<Boolean> addWhiteMobile(String mobile){
		try {
			return smsWhiteInfoService.addWhiteMobile(mobile);
		} catch (Throwable exception) {
			FailInfo failInfo = ExceptionUtils.print(new FailInfo(exception), logger, mobile);
			return new ResultInfo<>(failInfo);
		}
	}
	

	public ResultInfo<Boolean> delWhiteMobile(String mobile){
		try {
			return smsWhiteInfoService.delWhiteMobile(mobile);
		} catch (Throwable exception) {
			FailInfo failInfo = ExceptionUtils.print(new FailInfo(exception), logger, mobile);
			return new ResultInfo<>(failInfo);
		}
	}
}
