package com.tp.service.wms.thirdparty;

import com.tp.dto.common.ResultInfo;
import com.tp.dto.wms.SendOrderInfo;

/**
 * 推送仓库预约单
 * Created by ldr on 2016/6/20.
 */
public interface ISendOrderService {

    ResultInfo<Object> send(SendOrderInfo info);

    boolean check(SendOrderInfo info);

}
