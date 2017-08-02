package com.tp.proxy.sch;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.model.sch.SearchOperateLog;
import com.tp.proxy.BaseProxy;
import com.tp.service.IBaseService;
import com.tp.service.sch.ISearchOperateLogService;
/**
 * 代理层
 * @author szy
 *
 */
@Service
public class SearchOperateLogProxy extends BaseProxy<SearchOperateLog>{

	@Autowired
	private ISearchOperateLogService searchOperateLogService;

	@Override
	public IBaseService<SearchOperateLog> getService() {
		return searchOperateLogService;
	}
}
