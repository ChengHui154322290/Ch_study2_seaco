package com.tp.scheduler.order;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.tp.model.ord.SubOrder;
import com.tp.scheduler.AbstractJobRunnable;
import com.tp.scheduler.JobConstant;
import com.tp.service.ord.remote.ISalesOrderRemoteService;
import com.tp.service.usr.IUserInfoService;

@Component
public class PutPayedUnPutSubOrderJob extends AbstractJobRunnable {
	private static final Logger LOG = LoggerFactory.getLogger(PutPayedUnPutSubOrderJob.class);
	private static final String CURRENT_JOB_PREFIXED = "putpayedunputsuborderjob";
	
	@Autowired
	private ISalesOrderRemoteService salesOrderRemoteService;
	@Autowired
	private IUserInfoService userInfoService;
	@Autowired
	JobConstant jobConstant;
	@Override
	@Transactional
	public void execute() {
		LOG.info("推送支付后未推送的订单到仓库begin......");
		String[] unPutOrder = jobConstant.getUnPutPayedOrder();
		if(null!=unPutOrder){
			for(String code:unPutOrder){
				try{
					SubOrder subOrder = salesOrderRemoteService.findSubOrderByCode(Long.valueOf(code));
					salesOrderRemoteService.putWareHouseShippingBySubOrder(subOrder);
				}catch(Exception e){
					LOG.error("{} 推送失败 {}", code,e);
				}
			}
		}
		LOG.info("推送支付后未推送的订单到仓库end......");
	}

	@Override
	public String getFixed() {
		return CURRENT_JOB_PREFIXED;
	}
}
