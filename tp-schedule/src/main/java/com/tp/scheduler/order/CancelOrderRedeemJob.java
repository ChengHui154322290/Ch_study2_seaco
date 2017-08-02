package com.tp.scheduler.order;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.tp.common.vo.Constant;
import com.tp.model.ord.OrderInfo;
import com.tp.model.ord.OrderRedeemItem;
import com.tp.model.ord.SubOrder;
import com.tp.proxy.ord.SubOrderProxy;
import com.tp.scheduler.AbstractJobRunnable;
import com.tp.scheduler.JobConstant;
import com.tp.service.ord.IOrderRedeemItemService;
import com.tp.service.ord.remote.IOrderCancelRemoteService;

/**
 * 处理过去兑换码任务调度
 * @author zgf
 *
 */
@Component
public class CancelOrderRedeemJob extends AbstractJobRunnable{
	private static final Logger LOG = LoggerFactory.getLogger(CancelOrderRedeemJob.class);
	private static final String CURRENT_JOB_PREFIXED = "cancelOrderRedeemJob";
	
	
	@Autowired
	private IOrderCancelRemoteService orderCancelRemoteService;
	@Autowired
	private IOrderRedeemItemService orderRedeemItemService;
	@Autowired
	private SubOrderProxy  subOrderProxy;
	@Autowired
	JobConstant jobConstant;
	@Override
	@Transactional
	public void execute(){
		LOG.info("兑换码过期自动退单job启动....");
		List<OrderRedeemItem>  OrderRedeemItems= orderRedeemItemService.getOverdueOrderRedeemItem();
		if(CollectionUtils.isNotEmpty(OrderRedeemItems)){
			Set<Long> orderCodeSet = new HashSet<Long>();  //存放订单编号
			for(OrderRedeemItem OrderRedeemItem:OrderRedeemItems){
				orderCodeSet.add(Long.valueOf(OrderRedeemItem.getOrderCode()));
			}
			Iterator<Long> orderCodeiterator=orderCodeSet.iterator();
			while(orderCodeiterator.hasNext()){ 
			   long orderCode=orderCodeiterator.next();//订单编号
			   SubOrder subOrderParam=new SubOrder();
			   subOrderParam.setOrderCode(orderCode);//订单编号查询
			   List<SubOrder>  subOrderList=  subOrderProxy.queryByObject(subOrderParam).getData();
			  if(subOrderList!=null && subOrderList.size()>0){
				  SubOrder  subOrder=subOrderList.get(0);
				  orderCancelRemoteService.cancelOrderRedeemByJob(orderCode, subOrder.getMemberId(), Constant.AUTHOR_TYPE.JOB+"子订单");
			  }
			  
			}
		}else{
			LOG.info("未发现过期的兑换码信息！");
		}
		
		
		LOG.info("取消过期未支付订单job完成");
	}
	
	@Override
	public String getFixed() {
		return CURRENT_JOB_PREFIXED;
	}

}
