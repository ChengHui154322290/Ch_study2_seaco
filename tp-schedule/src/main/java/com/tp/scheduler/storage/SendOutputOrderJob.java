package com.tp.scheduler.storage;

import java.net.MalformedURLException;
import java.rmi.RemoteException;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.tp.common.vo.StorageConstant;
import com.tp.model.stg.OutputOrder;
import com.tp.model.stg.OutputOrderDetail;
import com.tp.redis.util.JedisCacheUtil;
import com.tp.service.stg.IOutputOrderService;

/**
 * 重发失败出库指令，最多三次
 * @author szy
 *
 */
@Component
public class SendOutputOrderJob {

	private Logger logger = LoggerFactory.getLogger(SendOutputOrderJob.class);
	
	@Autowired
	private IOutputOrderService outputOrderService;
	
	private static final String RUN_WORK_KEY = "TASK_SENDOUTPUTORDERJOB";//每个定时任务一个key

	@Autowired
	JedisCacheUtil jedisCacheUtil;
	
	public void sendOutputOrderService(){
		boolean lock = jedisCacheUtil.lock(RUN_WORK_KEY);// 获得锁
		if(!lock){
			return;
		}
		try {
			int maxTime = StorageConstant.SEND_ORDER_WMS_MAX_FAIL_TIMES.intValue();
			int limitSize = 20;
			logger.info("查询需要重新发送出库指令的订单（采购退货/销售订单）>>>>>>>>>>>>>>>>>");
			List<OutputOrder> orderDOs = outputOrderService.selectFailOutputOrder(maxTime,limitSize);
			if(CollectionUtils.isEmpty(orderDOs)){
				logger.info("没有查询到需要重新发送出库指令的订单（采购退货/销售订单）<<<<<<<<<<<<<<<");
				return;
			}
			logger.info("查询需要重新发送出库指令的订单（采购退货/销售订单） size:{}",orderDOs.size());
			
			for (OutputOrder outputOrder : orderDOs) {
				List<OutputOrderDetail> orderDetailDOs = outputOrderService.selectOuputorderDetailByOrderId(outputOrder.getId());
				if(CollectionUtils.isEmpty(orderDetailDOs)){
					continue;
				}
				logger.info("重新发送出库指令,单号：{}",outputOrder.getOrderCode());
				outputOrderService.sendOutputOrder(outputOrder, orderDetailDOs);
			}
			logger.info("重新发送出库指令的订单（采购退货/销售订单）完成<<<<<<<<<<<<<<<<<<");
		} catch (RemoteException e) {
		} catch (MalformedURLException e) {
		} catch (Exception e) {
		} finally {
			if (lock) {
				jedisCacheUtil.unLock(RUN_WORK_KEY);// 释放锁
			}
		}
	}
	
	public void close() {
		jedisCacheUtil.unLock(RUN_WORK_KEY);// 释放锁
	}
}
