package com.tp.proxy.bse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.model.bse.FrontCategoryLog;
import com.tp.proxy.BaseProxy;
import com.tp.service.IBaseService;
import com.tp.service.bse.IFrontCategoryLogService;
/**
 * 前台类目操作日志表代理层
 * @author szy
 *
 */
@Service
public class FrontCategoryLogProxy extends BaseProxy<FrontCategoryLog>{

	@Autowired
	private IFrontCategoryLogService frontCategoryLogService;

	@Override
	public IBaseService<FrontCategoryLog> getService() {
		return frontCategoryLogService;
	}
}
