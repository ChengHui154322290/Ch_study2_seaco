package com.tp.proxy.sys;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.util.ExceptionUtils;
import com.tp.common.vo.PageInfo;
import com.tp.common.vo.DAOConstant.MYBATIS_SPECIAL_STRING;
import com.tp.common.vo.DAOConstant.WHERE_ENTRY;
import com.tp.dto.common.FailInfo;
import com.tp.dto.common.ResultInfo;
import com.tp.dto.ord.ShoppingCartDto;
import com.tp.model.sys.ApicallLog;
import com.tp.proxy.BaseProxy;
import com.tp.service.IBaseService;
import com.tp.service.sys.IApicallLogService;
import com.tp.util.BeanUtil;
import com.tp.util.StringUtil;
/**
 * API请求日志表代理层
 * @author szy
 *
 */
@Service
public class ApicallLogProxy extends BaseProxy<ApicallLog>{

	private static final Logger logger = LoggerFactory.getLogger(ApicallLogProxy.class);
	
	@Autowired
	private IApicallLogService apicallLogService;

	@Override
	public IBaseService<ApicallLog> getService() {
		return apicallLogService;
	}
	
	public ResultInfo<Boolean> resendApicall(Long id){
		return apicallLogService.resendApicall(id);
	}
	
	public ResultInfo<PageInfo<ApicallLog>> queryApiCallLog(ApicallLog log, PageInfo<ApicallLog> pageInfo){
		try {
			Map<String, Object> params = BeanUtil.beanMap(log);
			List<WHERE_ENTRY> whEntries = new ArrayList<>();
			if (log.getStartTime() != null) {
				whEntries.add(new WHERE_ENTRY("request_time", MYBATIS_SPECIAL_STRING.GT, log.getStartTime()));
			}
			if (log.getEndTime() != null) {
				whEntries.add(new WHERE_ENTRY("request_time", MYBATIS_SPECIAL_STRING.LT, log.getEndTime()));
			}
			if (StringUtil.isNotEmpty(log.getUri())) {
				params.remove("uri");
				params.put(MYBATIS_SPECIAL_STRING.LIKE.name(), " uri like concat('%', '" + log.getUri() + "', '%') ");
			}
			params.remove("startTime");params.remove("endTime");
			params.put(MYBATIS_SPECIAL_STRING.WHERE.name(), whEntries);
			return new ResultInfo<>(apicallLogService.queryPageByParamNotEmpty(params, pageInfo));
		} catch (Exception e) {
			FailInfo failInfo = ExceptionUtils.println(new FailInfo(e), logger, log);
			return new ResultInfo<PageInfo<ApicallLog>>(failInfo);
		}	
	}
}
