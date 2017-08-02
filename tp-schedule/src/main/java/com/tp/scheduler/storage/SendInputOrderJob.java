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
import com.tp.model.stg.InputOrder;
import com.tp.model.stg.InputOrderDetail;
import com.tp.redis.util.JedisCacheUtil;
import com.tp.service.stg.IInputOrderService;

/**
 * 重发失败的入库指令，最多三次
 * @author szy
 *
 */
@Component
public class SendInputOrderJob {

	private Logger logger = LoggerFactory.getLogger(SendInputOrderJob.class);
	
	@Autowired
	private IInputOrderService inputOrderService;
	
	private static final String RUN_WORK_KEY = "TASK_SENDINPUTORDERJOB";//每个定时任务一个key

	@Autowired
	JedisCacheUtil jedisCacheUtil;
	
	public void sendInputOrderService(){
		boolean lock = jedisCacheUtil.lock(RUN_WORK_KEY);// 获得锁
		if(!lock){
			return;
		}
		try {
			int maxTime = StorageConstant.SEND_ORDER_WMS_MAX_FAIL_TIMES.intValue();
			int limitSize = 20;
			logger.info("查询需要重新发送入库指令的订单（采购订单）>>>>>>>>>>");
			List<InputOrder> orderDOs = inputOrderService.selectFailInputOrder(maxTime,limitSize);
			if(CollectionUtils.isEmpty(orderDOs)){
				logger.info("没有查询到需要重新发送入库指令的订单（采购订单）<<<<<<<<<<<<<<<<");
				return;
			}
			logger.info("查询需要重新发送入库指令的订单（采购订单） size:{}",orderDOs.size());
		
			for (InputOrder inputOrderDO : orderDOs) {
				List<InputOrderDetail> orderDetailDOs = inputOrderService.selectOrderDetailByOrderId(inputOrderDO.getId());
				if(CollectionUtils.isEmpty(orderDetailDOs)){
					continue;
				}
				logger.info("重新发送入库指令,单号：{}",inputOrderDO.getOrderCode());
				inputOrderService.sendInputOrderToWms(inputOrderDO, orderDetailDOs);
			}
			logger.info("重新发送入库指令的订单（采购订单）完成<<<<<<<<<<<");
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
