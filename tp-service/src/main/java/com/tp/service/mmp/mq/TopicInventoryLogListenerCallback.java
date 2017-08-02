/**
 *
 */
package com.tp.service.mmp.mq;

import com.tp.model.mmp.TopicInventoryAccBook;
import com.tp.mq.MqMessageCallBack;
import com.tp.service.mmp.ITopicInventoryAccBookService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TopicInventoryLogListenerCallback implements MqMessageCallBack {

    @Autowired
    private ITopicInventoryAccBookService topicInventoryAccBookService;

    private Logger logger = LoggerFactory.getLogger(this.getClass());


    @Override
    public boolean execute(Object o) {
        try {
            if (logger.isDebugEnabled()) {
                logger.info("start log topic inventory record.......");
            }
            if (o instanceof TopicInventoryAccBook) {
                TopicInventoryAccBook accBookDO = (TopicInventoryAccBook) o;
                accBookDO.setId(null);
                TopicInventoryAccBook topicInventoryAccBook = topicInventoryAccBookService.insert(accBookDO);
                if (topicInventoryAccBook == null || topicInventoryAccBook.getId() == null || topicInventoryAccBook.getId() == 0) {
                    return false;
                }
            } else {
                return false;
            }
            if (logger.isDebugEnabled()) {
                logger.info("log topic inventory record end.......");
            }
            return true;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return false;
        }
    }

}
