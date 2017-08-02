package com.tp.service.wms.thirdparty;

import com.tp.dto.common.FailInfo;
import com.tp.dto.common.ResultInfo;
import com.tp.dto.wms.SendOrderInfo;
import com.tp.service.wms.thirdparty.ISendOrderHandlerService;
import com.tp.service.wms.thirdparty.ISendOrderService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by ldr on 2016/6/20.
 */
@Service
public class SendOrderHandlerService implements ISendOrderHandlerService {

	@Autowired
    private List<ISendOrderService> sendOrderServices;

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    public ResultInfo<Object> doSend(SendOrderInfo info){
        for(ISendOrderService sendOrderService: sendOrderServices){
            if(sendOrderService.check(info)){
                return sendOrderService.send(info);
            }
        }
        logger.error("[SEND_ORDER_ERROR:NO_SUIT_SERVICE_FOUND,PARAM={}]",info.getStockasn());
        return new ResultInfo<>(new FailInfo("预约失败，该仓库不支持预约功能"));
    }

    public void setSendOrderServices(List<ISendOrderService> sendOrderServices) {
        this.sendOrderServices = sendOrderServices;
    }
}
