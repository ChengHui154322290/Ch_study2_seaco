/**
 *
 */
package com.tp.service.stg.mq;

import com.tp.dto.wms.StockasnFactWithDetail;
import com.tp.mq.MqMessageCallBack;
import com.tp.service.mmp.ITopicItemService;
import com.tp.service.stg.IInventoryService;
import com.tp.util.JsonUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service(value = "stockAsnNoticeInventoryListenerCallback")
public class StockAsnNoticeInventoryListenerCallback implements MqMessageCallBack {

    @Autowired
    private IInventoryService inventoryService;

    private Logger logger = LoggerFactory.getLogger(this.getClass());


    @SuppressWarnings("unchecked")
    @Override
    public boolean execute(Object o) {
        try {
            if (logger.isDebugEnabled()) {
                logger.info("MQ-StockAsnNoticeInventoryListenerCallback-START");
            }
            StockasnFactWithDetail stockasnFactWithDetail = (StockasnFactWithDetail) o;
            Integer count = inventoryService.increaseInventoryForStockasnFact(stockasnFactWithDetail);
            if (logger.isDebugEnabled()) {
                logger.info("MQ-StockAsnNoticeInventoryListenerCallback-END.COUNT:" + count);
            }

        } catch (Exception e) {
            logger.error("MQ-StockAsnNoticeInventoryListenerCallback-ERROR", e);
            logger.error("MQ-StockAsnNoticeInventoryListenerCallback-ERROR:PARAM=:" + JsonUtil.convertObjToStr(o));
            return false;
        }
        return true;
    }

}
