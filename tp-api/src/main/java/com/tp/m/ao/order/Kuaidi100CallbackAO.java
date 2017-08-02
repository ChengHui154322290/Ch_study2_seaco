/**
 * NewHeight.com Inc.
 * Copyright (c) 2007-2014 All Rights Reserved.
 */
package com.tp.m.ao.order;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.dto.ord.kuaidi100.ExpressInfo;
import com.tp.dto.ord.kuaidi100.ExpressResult;
import com.tp.service.ord.local.IKuaidi100LocalService;


/**
 * <pre>
 * 快递100平台处理AO
 * </pre>
 */
@Service
public class Kuaidi100CallbackAO {
	@Autowired
	private IKuaidi100LocalService kuaidi100LocalService;
	/**
	 * 
	 * <pre>
	 * 保存快递信息
	 * </pre>
	 * 
	 * @param expressInfo
	 * @return
	 */
	public ExpressResult saveExpressInfo(String subOrderCode, ExpressInfo expressInfo) {
		return kuaidi100LocalService.saveExpressInfo(Long.valueOf(subOrderCode), expressInfo);
	}
}
