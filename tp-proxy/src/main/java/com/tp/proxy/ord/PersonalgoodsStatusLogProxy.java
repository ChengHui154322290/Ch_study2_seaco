package com.tp.proxy.ord;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.model.ord.PersonalgoodsStatusLog;
import com.tp.proxy.BaseProxy;
import com.tp.service.IBaseService;
import com.tp.service.ord.IPersonalgoodsStatusLogService;
/**
 * 个人物品申报单审单状态日志(包括回执与主动查询)代理层
 * @author szy
 *
 */
@Service
public class PersonalgoodsStatusLogProxy extends BaseProxy<PersonalgoodsStatusLog>{

	@Autowired
	private IPersonalgoodsStatusLogService personalgoodsStatusLogService;

	@Override
	public IBaseService<PersonalgoodsStatusLog> getService() {
		return personalgoodsStatusLogService;
	}
}
