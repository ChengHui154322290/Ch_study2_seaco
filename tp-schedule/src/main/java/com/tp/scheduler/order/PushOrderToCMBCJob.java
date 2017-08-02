package com.tp.scheduler.order;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.tp.common.vo.OrderConstant;
import com.tp.model.ord.SubOrder;
import com.tp.redis.util.JedisCacheUtil;
import com.tp.scheduler.AbstractJobRunnable;
import com.tp.scheduler.JobConstant;
import com.tp.service.ord.IOrderForCMBCService;
import com.tp.util.DateUtil;

/**
 * 推送订单任务调度
 * @author szy
 *
 */
@Component
public class PushOrderToCMBCJob extends AbstractJobRunnable{
	private static final Logger LOG = LoggerFactory.getLogger(PushOrderToCMBCJob.class);
	private static final String CURRENT_JOB_PREFIXED = "pushordertocmbcjob";		
	private final static String PUSHORDER_FAILPUSH_KEY = "pushordertocmbc-failedpush";
	private final static String PUSHORDER_LASTPUSHTIME_KEY = "pushordertocmbc-lastpushtime";
		
	private final static Long  IntervalTime = 1000*60*60*24L;
	
	
	@Autowired
	private  JobConstant jobConstant;
			
	@Autowired
	private JedisCacheUtil jedisCacheUtil;
	
	@Autowired
	private IOrderForCMBCService orderForCMBCService;
	
	@Override
	public void execute(){
		
		try{
			List<SubOrder> curfailedlist = new ArrayList<SubOrder>();
			List<SubOrder> lastfailedlist = (List<SubOrder>)jedisCacheUtil.getCache(PUSHORDER_FAILPUSH_KEY);		
			Date lastDate = (Date)jedisCacheUtil.getCache(PUSHORDER_LASTPUSHTIME_KEY);	

			if(lastfailedlist != null && !lastfailedlist.isEmpty() ){
				List<SubOrder> failedList = orderForCMBCService.pushSubOrderToCMBC(lastfailedlist);		
				curfailedlist.addAll(failedList);
			}
			
			Date startDate = new Date();				
			Date endDate = new Date();						
			if(lastDate != null){
				startDate = lastDate;			
			}else{
				startDate.setTime( startDate.getTime() - IntervalTime);
			}
					
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("startdate", DateUtil.formatDateTime(startDate));
			params.put("enddate", DateUtil.formatDateTime(endDate));
			params.put("channelcode", OrderConstant.CHANNEL_CODE.cmbc.name() );			
			List<SubOrder> suborderList = orderForCMBCService.getSubOrderByTime(params);
			List<SubOrder> failedList = orderForCMBCService.pushSubOrderToCMBC(suborderList);
			curfailedlist.addAll(failedList);
			
			jedisCacheUtil.setCache(PUSHORDER_LASTPUSHTIME_KEY, endDate, 1000*60*60*24);
			jedisCacheUtil.setCache(PUSHORDER_FAILPUSH_KEY, curfailedlist, 1000*60*60*24 );
			
		}catch(Exception e){
			LOG.info( e.getMessage() );
		}	
	}
	
	@Override
	public String getFixed() {
		return CURRENT_JOB_PREFIXED;
	}

}
