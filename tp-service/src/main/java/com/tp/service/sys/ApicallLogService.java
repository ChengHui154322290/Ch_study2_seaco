package com.tp.service.sys;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.dao.BaseDao;
import com.tp.dao.sys.ApicallLogDao;
import com.tp.dto.common.FailInfo;
import com.tp.dto.common.ResultInfo;
import com.tp.dto.sys.ApicallResendData;
import com.tp.dto.sys.apilog.JKFApicallResendData;
import com.tp.dto.sys.apilog.STOApicallResendData;
import com.tp.model.sys.ApicallLog;
import com.tp.service.BaseService;
import com.tp.service.sys.IApicallLogService;
import com.tp.util.HttpClientUtil;
import com.tp.util.StringUtil;


@Service
public class ApicallLogService extends BaseService<ApicallLog> implements IApicallLogService {

	@Autowired
	private ApicallLogDao apicallLogDao;
	
	@Override
	public BaseDao<ApicallLog> getDao() {
		return apicallLogDao;
	}

	@Override
	public ResultInfo<Boolean> resendApicall(Long logId) {
		ApicallLog log = getDao().queryById(logId);
		if (log == null) return new ResultInfo<>(new FailInfo("API日志不存在"));
		
		ApicallResendData data = constructResendData(log);
		if (data == null || StringUtil.isEmpty(data.getUrl()) || 
				(data.getRequestParams() == null && StringUtil.isEmpty(data.getRequestContent()))){
			return new ResultInfo<>(new FailInfo("API请求类型不允许重发"));
		}		
		try{
			String response = null;
			if(data.getRequestParams() != null){
				response = HttpClientUtil.postData(data.getUrl(), data.getRequestParams(), "UTF-8"); 
			}else{
				response = HttpClientUtil.postData(data.getUrl(), data.getRequestContent(), data.getContentType(), null);
			}
			logger.info("重发请求响应：{}", response);
			return new ResultInfo<>(Boolean.TRUE);
		}catch(Throwable e){
			logger.error("重发请求失败", e);
		}
		return new ResultInfo<>(new FailInfo("重发失败"));
	}
	
	public ApicallResendData constructResendData(ApicallLog log){
		String uri = log.getUri();
		if(uri.contains("/jkf/")){ //JKF
			return new JKFApicallResendData(log);
		}else if(uri.contains("/sto/")){ //STO
			return new STOApicallResendData(log);
		}
		return null;
	}
}
