package com.tp.service.ord;

import com.tp.dto.ord.kuaidi100.PushExpressInfoRequest;
import com.tp.dto.ord.kuaidi100.SubscribeResult;

public interface IExpressForKuaidi100Service {
	/**
	 * 
	 * <pre>
	 * 推送快递单号相关信息给快递100
	 * </pre>
	 * 
	 * @param pushReq
	 * @return
	 */
	SubscribeResult pushExpressInfoToKuaidi100(PushExpressInfoRequest pushReq);
}
