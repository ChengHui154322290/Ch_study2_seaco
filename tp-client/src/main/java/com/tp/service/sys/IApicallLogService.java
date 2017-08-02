package com.tp.service.sys;

import com.tp.dto.common.ResultInfo;
import com.tp.model.sys.ApicallLog;
import com.tp.service.IBaseService;
/**
  * @author szy 
  * API请求日志表接口
  */
public interface IApicallLogService extends IBaseService<ApicallLog>{

	/**
	 * API请求重发
	 * @param logId
	 * @return  
	 */
	ResultInfo<Boolean> resendApicall(Long logId);
}
