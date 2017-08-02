/**
 * 
 */
package com.tp.service.mem;

import com.tp.common.vo.PageInfo;
import com.tp.dto.common.ResultInfo;
import com.tp.model.mem.SmsWhiteInfo;

/**
 * @author Administrator
 *
 */
public interface ISmsWhiteInfoService {
	/**
	 * 查询白名单
	 * @param mobile
	 * @return page
	 */
	PageInfo<String> queryWhiteInfo(String mobile, PageInfo<String> pageInfo);
	
	/**
	 * 查询白名单 
	 */
	ResultInfo<SmsWhiteInfo> querySmsWhiteInfo();
	
	
	/** 
	 * 校验是否可以发短信 
	 * 1. 白名单是否开启
	 * 2. 是否在白名单内
	 * @param mobile 
	 * @return true允许发送 false禁止发送
	 */
	boolean checkSendSms(String mobile);
	
	/** 
	 * 更新白名单状态 
	 * @param staus 0关闭1开启
	 */
	ResultInfo<Boolean> updateSmsWhiteInfoStatus(Integer status);

	/**
	 * 增加白名单
	 * @param mobile 
	 */
	ResultInfo<Boolean> addWhiteMobile(String mobile);
	
	/**
	 * 删除白名单
	 * @param mobile 
	 */
	ResultInfo<Boolean> delWhiteMobile(String mobile);
}
