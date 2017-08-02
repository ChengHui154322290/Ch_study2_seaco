package com.tp.scheduler.promotion.scheduler;

import com.tp.scheduler.AbstractJobRunnable;
import com.tp.service.mmp.groupbuy.IGroupbuyGroupService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by ldr on 2016/3/18.
 */
@Component
public class GroupbuyGroupScheduler extends AbstractJobRunnable {

    private static  final String fix = "groupbuygroupschedule";

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private IGroupbuyGroupService groupbuyGroupService;

    @Override
    public void execute() {
        try {
            Integer count = groupbuyGroupService.updateExpiredGroup();
            logger.info("GROUPBUY_UPDATE_EXPIRED_GROUP_SUCCESS,COUNT="+count);
        }catch (Exception e){
            logger.error("GROUPBUY_UPDATE_EXPIRED_GROUP_ERROR",e);
        }
    }

    @Override
    public String getFixed() {
        return fix;
    }
}
