/**
 * NewHeight.com Inc.
 * Copyright (c) 2007-2014 All Rights Reserved.
 */
package com.tp.service.ord.local;

import java.util.List;

import com.tp.model.ord.FisherDeliveryLog;

/**
 * <pre>
 * 费舍尔ECM本地接口
 * </pre>
 * 
 * @author szy
 * @time 2015-4-20 上午10:47:19
 */
public interface IFisherLocalService {
	/**
	 * 
	 * <pre>
	 * 添加发货日志
	 * </pre>
	 * 
	 */
	void addSendOrderLog(List<FisherDeliveryLog> fisherDeliveryLogList);
}
