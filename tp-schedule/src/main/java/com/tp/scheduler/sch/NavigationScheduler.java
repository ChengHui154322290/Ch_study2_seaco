package com.tp.scheduler.sch;

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
public class NavigationScheduler extends AbstractJobRunnable {

    @Autowired
    private INavigationCategoryService navigationCategoryService;

    private static final String fix = "navigationscheduler";

    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @Override
    public void execute() {
        long b = System.currentTimeMillis();
        LOGGER.info("NAVIGATION_SCHEDULE_START__");
        try {
            navigationCategoryService.freshNav();
        } catch (Exception e) {
            LOGGER.error("NAVIGATION_SCHEDULE_ERROR:", e);
        }
        LOGGER.info("NAVIGATION_SCHEDULE_END.COSTA_MILLIS:" + (System.currentTimeMillis() - b));
    }

    @Override
    public String getFixed() {
        return fix;
    }
}
