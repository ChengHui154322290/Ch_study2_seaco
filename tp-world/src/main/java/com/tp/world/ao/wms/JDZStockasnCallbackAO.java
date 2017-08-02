package com.tp.world.ao.wms;

import com.tp.proxy.wms.StockasnFactProxy;
import com.tp.result.wms.ResultMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by ldr on 2016/7/5.
 */
@Service
public class JDZStockasnCallbackAO {

    @Autowired
    private StockasnFactProxy stockasnFactProxy;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public ResultMessage purchaseFactOrder(String serviceId, String content) {
        try {
            ResultMessage rm = stockasnFactProxy.purchaseFactOrder(serviceId, content);
            return rm;
        } catch (Exception e) {
            logger.error("STOCK_ASN_FACT_ERROR:", e);
            return new ResultMessage(false, "SYSTEM-ERROR");
        }


    }

}
