package com.tp.scheduler.order;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.tp.common.vo.OrderConstant;
import com.tp.common.vo.PageInfo;
import com.tp.common.vo.DAOConstant.MYBATIS_SPECIAL_STRING;
import com.tp.dto.ord.OrderReceiveGoodsDTO;
import com.tp.model.ord.SubOrder;
import com.tp.scheduler.AbstractJobRunnable;
import com.tp.scheduler.JobConstant;
import com.tp.service.ord.IKuaidi100ExpressService;
import com.tp.service.ord.remote.ISalesOrderRemoteService;

@Component
public class ReceivedGoodsJob extends AbstractJobRunnable {

	private static final Logger LOG = LoggerFactory.getLogger(ReceivedGoodsJob.class);
	private static final String CURRENT_JOB_PREFIXED = "receivedgoods";
	private static final Integer RECEIVED_DAYS = 15;
	
	@Autowired
	private ISalesOrderRemoteService salesOrderRemoteService;
	@Autowired
	private IKuaidi100ExpressService kuaidi100ExpressService;
	@Autowired
	JobConstant jobConstant;
	@Override
	public void execute() {
		LOG.info("ReceivedGoodsJob begin running");
		int start = 1;
		int size = 0;
		Map<String,Object> params = new HashMap<String,Object>();
		do{
			size = 0;
			PageInfo<SubOrder> subOrderPage = salesOrderRemoteService.querySubOrderPageByReceived(getReceivedDays(), start++, 1000);
			List<SubOrder> subOrderList = subOrderPage.getRows();
			if(CollectionUtils.isNotEmpty(subOrderList)){
				OrderReceiveGoodsDTO orderReceiveGoodsDTO = null;
				for(SubOrder subOrder:subOrderList){
					if(!OrderConstant.OrderType.BUY_COUPONS.code.equals(subOrder.getType())){
						params.put("orderCode", subOrder.getOrderCode());
						params.put(MYBATIS_SPECIAL_STRING.COLUMNS.name(), " data_context like '%签收%'");
						Integer count = kuaidi100ExpressService.queryByParamCount(params);
						if(count>0){
							orderReceiveGoodsDTO = new OrderReceiveGoodsDTO();
							orderReceiveGoodsDTO.setReceiptDate(new Date());
							orderReceiveGoodsDTO.setSubOrderCode(subOrder.getOrderCode());
							try{
								salesOrderRemoteService.operateOrderForReceiveGoods(orderReceiveGoodsDTO);
							}catch(Exception e){
								LOG.error("order code = {} received is error ! {}",subOrder.getOrderCode(),e.getMessage());
							}
						}
					}
				}
				size = subOrderList.size();
				if(size<1000){
					size=0;
				}
			}
		}while(size>0);
		LOG.info("ReceivedGoodsJob end run");
	}

	@Override
	public String getFixed() {
		return CURRENT_JOB_PREFIXED;
	}
	
	private Integer getReceivedDays(){
		Integer days = jobConstant.getReceivedDays();
		if(null==days){
			return RECEIVED_DAYS;
		}
		return days;
	}
}
