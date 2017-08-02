package com.tp.scheduler.promotion.scheduler;

import com.tp.proxy.mmp.TopicItemSynProxy;
import com.tp.scheduler.AbstractJobRunnable;
import com.tp.service.bse.INavigationCategoryService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by ldr on 2016/2/29.
 */
@Component
public class SynItemInfoToTopicItemScheduler extends AbstractJobRunnable {

    @Autowired
    private TopicItemSynProxy topicItemSynProxy;

    private static final String fix = "syniteminfototopicitemscheduler";

    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @Override
    public void execute() {
        long b = System.currentTimeMillis();
        LOGGER.info("SYN_ITEM_INFO_TO_TOPIC_ITEM_SCHEDULE");
        try {
           topicItemSynProxy.syn(1,null);
        } catch (Exception e) {
            LOGGER.error("SYN_ITEM_INFO_TO_TOPIC_ITEM_SCHEDULE_ERROR:", e);
        }
        LOGGER.info("SYN_ITEM_INFO_TO_TOPIC_ITEM_SCHEDULE_END.COSTA_MILLIS:" + (System.currentTimeMillis() - b));
    }

    @Override
    public String getFixed() {
        return fix;
    }
}
