package com.tp.scheduler.dss;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.tp.scheduler.AbstractJobRunnable;
import com.tp.service.mmp.IPointDetailService;
import com.tp.util.DateUtil;

/**
 * 积分作废
 * @author szy
 *
 */
@Component
public class PointDiscardJob  extends AbstractJobRunnable{

	private static final Logger LOG = LoggerFactory.getLogger(PointDiscardJob.class);
	private static final String CURRENT_JOB_PREFIXED = "PointDiscardJob";

	@Autowired
	private IPointDetailService pointDetailService;
	
	@Override
	public void execute() {
		LOG.info("上年度积分作废....");
		Integer packageTime = DateUtil.getYear(new Date())-2;
		pointDetailService.updatePointByDiscard(packageTime);
	}

	@Override
	public String getFixed() {
		return CURRENT_JOB_PREFIXED;
	}
}
