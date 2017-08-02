/**
 * NewHeight.com Inc.
 * Copyright (c) 2007-2014 All Rights Reserved.
 */
package com.tp.service.ord.local;

import com.tp.dto.ord.kuaidi100.ExpressInfo;
import com.tp.dto.ord.kuaidi100.ExpressResult;

/**
 * <pre>
 * 快递100本地服务
 * </pre>
 * 
 * @author szy
 * @time 2015-2-2 下午6:03:34
 */
public interface IKuaidi100LocalService {
	/**
	 * 
	 * <pre>
	 * 推送快递信息到快递100平台
	 * </pre>
	 * 
	 */
	void pushExpressToKuaidi100();

	/**
	 * <pre>
	 * 保存快递信息
	 * </pre>
	 * 
	 * @param code
	 * @param expressInfo
	 * @return
	 */
	ExpressResult saveExpressInfo(Long code, ExpressInfo expressInfo);
}
