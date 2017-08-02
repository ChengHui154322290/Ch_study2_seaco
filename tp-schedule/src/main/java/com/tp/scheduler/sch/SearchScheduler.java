package com.tp.scheduler.sch;

import com.tp.scheduler.AbstractJobRunnable;
import com.tp.service.sch.IDocService;
import com.tp.service.sch.ISearchDataService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by ldr on 2016/2/18.
 */
@Component
public class SearchScheduler extends AbstractJobRunnable {

    private static final String CURRENT_JOB_PREFIXED = "searchscheduler";

    @Autowired
    private ISearchDataService searchDataService;

    @Autowired
    private IDocService docService;

    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());


    @Override
    public void execute() {
        long b = System.currentTimeMillis();
        LOGGER.info("SEARCH_SCHEDULER_START__");
        try {
            searchDataService.processItemData();
            docService.updateItemDoc();
        } catch (Exception e) {
            LOGGER.error("SEARCH_SCHEDULER_ERROR:", e);
        }
        try {
           // searchDataService.processShopData();
           // docService.updateShopDoc();
        } catch (Exception e) {
            LOGGER.error("SEARCH_SCHEDULER_ERROR:", e);
        }

        LOGGER.info("SEARCH_SCHEDULER_END.COSTA_MILLIS:" + (System.currentTimeMillis() - b));

    }

    @Override
    public String getFixed() {
        return CURRENT_JOB_PREFIXED;
    }
}
