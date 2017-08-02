package com.tp.service.wms.thirdparty;

import com.tp.dto.common.ResultInfo;
import com.tp.dto.wms.SendOrderInfo;

/**
 * Created by ldr on 2016/6/20.
 */
public interface ISendOrderHandlerService {

    ResultInfo<Object> doSend(SendOrderInfo info);
}
