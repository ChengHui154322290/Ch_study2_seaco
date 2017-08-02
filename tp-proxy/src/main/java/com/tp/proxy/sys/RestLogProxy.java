package com.tp.proxy.sys;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.util.ExceptionUtils;
import com.tp.common.vo.PageInfo;
import com.tp.common.vo.DAOConstant.MYBATIS_SPECIAL_STRING;
import com.tp.common.vo.DAOConstant.WHERE_ENTRY;
import com.tp.dto.common.FailInfo;
import com.tp.dto.common.ResultInfo;
import com.tp.model.sys.RestLog;
import com.tp.proxy.BaseProxy;
import com.tp.service.IBaseService;
import com.tp.service.sys.IRestLogService;
import com.tp.util.BeanUtil;
/**
 * 对外REST请求日志表代理层
 * @author szy
 *
 */
@Service
public class RestLogProxy extends BaseProxy<RestLog>{

	@Autowired
	private IRestLogService restLogService;

	@Override
	public IBaseService<RestLog> getService() {
		return restLogService;
	}
	
	public ResultInfo<PageInfo<RestLog>> queryRestfulLog(RestLog log, PageInfo<RestLog> pageInfo){
		try {
			Map<String, Object> params = BeanUtil.beanMap(log);
			List<WHERE_ENTRY> whEntries = new ArrayList<>();
			if (log.getStartTime() != null) {
				whEntries.add(new WHERE_ENTRY("request_time", MYBATIS_SPECIAL_STRING.GT, log.getStartTime()));
			}
			if (log.getEndTime() != null) {
				whEntries.add(new WHERE_ENTRY("request_time", MYBATIS_SPECIAL_STRING.LT, log.getEndTime()));
			}
			params.remove("startTime");params.remove("endTime");
			params.put(MYBATIS_SPECIAL_STRING.WHERE.name(), whEntries);
			return new ResultInfo<>(restLogService.queryPageByParamNotEmpty(params, pageInfo));
		} catch (Exception e) {
			FailInfo failInfo = ExceptionUtils.println(new FailInfo(e), logger, log);
			return new ResultInfo<PageInfo<RestLog>>(failInfo);
		}	
	}
}
