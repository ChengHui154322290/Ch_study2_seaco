package com.tp.scheduler.order;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.tp.common.util.OrderUtils;
import com.tp.common.vo.Constant;
import com.tp.model.mem.MemberInfo;
import com.tp.model.ord.OrderInfo;
import com.tp.model.ord.SubOrder;
import com.tp.scheduler.AbstractJobRunnable;
import com.tp.scheduler.JobConstant;
import com.tp.service.mem.IMemberInfoService;
import com.tp.service.ord.remote.IOrderCancelRemoteService;
import com.tp.service.ord.remote.ISalesOrderRemoteService;

/**
 * 取消订单任务调度
 * @author szy
 *
 */
@Component
public class CancelOrderJob extends AbstractJobRunnable{
	private static final Logger LOG = LoggerFactory.getLogger(CancelOrderJob.class);
	private static final String CURRENT_JOB_PREFIXED = "cancelorder";
	
	private static final int UN_PAYED_EXPIRED_MINUTE_DEFAULT = 60;
	
	@Autowired
	private IOrderCancelRemoteService orderCancelRemoteService;
	@Autowired
	private ISalesOrderRemoteService OrderInfoRemoteService;
	@Autowired
	private IMemberInfoService memberInfoService;
	@Autowired
	JobConstant jobConstant;
	@Override
	@Transactional
	public void execute(){
		Integer expired = jobConstant.getCancelUnpayMinute();
		if(null==expired){
			expired =UN_PAYED_EXPIRED_MINUTE_DEFAULT;
		}
		LOG.info("取消{}分钟未支付订单job启动....", expired);
		List<SubOrder> subOrderList = OrderInfoRemoteService.querySubOrderBySeaOrderUnPayIsExpired(expired);
		if(CollectionUtils.isNotEmpty(subOrderList))
			for(SubOrder subOrder:subOrderList){
				try{
					Long memberId = subOrder.getMemberId();
					orderCancelRemoteService.cancelOrderByJob(subOrder.getOrderCode(), memberId, Constant.AUTHOR_TYPE.JOB+"子订单");
				}catch(Exception e){
					LOG.error("orderCode = {} 取消出错 {}",new Object[]{subOrder.getOrderCode(),e});
				}
			}
		else{
			LOG.info("不存在过期未支付订单");
		}
		
		LOG.info("取消过期未支付订单job完成");
	}
	
	@Override
	public String getFixed() {
		return CURRENT_JOB_PREFIXED;
	}

}
