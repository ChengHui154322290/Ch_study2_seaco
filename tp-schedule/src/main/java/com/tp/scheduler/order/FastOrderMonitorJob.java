package com.tp.scheduler.order;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.tp.common.util.mem.SmsException;
import com.tp.common.vo.Constant;
import com.tp.common.vo.FastConstant;
import com.tp.common.vo.OrderConstant;
import com.tp.common.vo.Constant.SPLIT_SIGN;
import com.tp.common.vo.DAOConstant.MYBATIS_SPECIAL_STRING;
import com.tp.model.dss.FastUserInfo;
import com.tp.model.ord.SubOrder;
import com.tp.proxy.dss.FastUserInfoProxy;
import com.tp.proxy.ord.SubOrderProxy;
import com.tp.scheduler.AbstractJobRunnable;
import com.tp.service.mem.ISendSmsService;
import com.tp.util.StringUtil;

/**
 * 速购订单监控
 * @author szy
 *
 */
@Component
public class FastOrderMonitorJob extends AbstractJobRunnable {

	private static final Logger LOG = LoggerFactory.getLogger(FastOrderMonitorJob.class);
	
	@Autowired
	private SubOrderProxy subOrderProxy;
	@Autowired
	private ISendSmsService sendSmsService;
	@Autowired
	private FastUserInfoProxy fastUserInfoProxy;
	
	@Value("#{config['FastOrderMonitorJob.warehouseIdList']}")
	private String warehouseIdList;
	@Value("#{config['FastOrderMonitorJob.smscontent']}")
	private String content;
	
	@Override
	public void execute() {
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("type", OrderConstant.FAST_ORDER_TYPE);
		params.put("orderStatus", OrderConstant.ORDER_STATUS.TRANSFER.code);
		List<SubOrder> subOrderList = subOrderProxy.queryByParam(params).getData();
		params.put("orderStatus", OrderConstant.ORDER_STATUS.DELIVERY.code);
		List<SubOrder> unDeliveryOrderList = subOrderProxy.queryByParam(params).getData();
		
		List<Long> warehouseIdList = new ArrayList<Long>();
		Map<Long,Integer> warehouseMap = new HashMap<Long,Integer>();
		Map<Long,Integer> warehouseUnMap = new HashMap<Long,Integer>();
		if(CollectionUtils.isNotEmpty(subOrderList)){
			for(SubOrder subOrder:subOrderList){
				Long warehouseId = subOrder.getWarehouseId();
				warehouseIdList.add(warehouseId);
				Integer count = warehouseMap.get(warehouseId);
				if(count==null){
					count=0;
				}
				warehouseMap.put(warehouseId, ++count);
			}
		}
		if(CollectionUtils.isNotEmpty(unDeliveryOrderList)){
			for(SubOrder subOrder:unDeliveryOrderList){
				Long warehouseId = subOrder.getWarehouseId();
				warehouseIdList.add(warehouseId);
				Integer count = warehouseUnMap.get(warehouseId);
				if(count==null){
					count=0;
				}
				warehouseUnMap.put(warehouseId, ++count);
			}
		}
		if(CollectionUtils.isNotEmpty(warehouseIdList) && this.warehouseIdList!=null){
			String[] warehouseIdArray = this.warehouseIdList.split(SPLIT_SIGN.COMMA);
			if(warehouseIdArray!=null && warehouseIdArray.length>0){
				warehouseIdList.removeIf(new Predicate<Long>(){
					public boolean test(Long t) {
						for(String whId:warehouseIdArray){
							if(String.valueOf(t).equals(whId)){
								return false;
							}
						}
						return true;
					}
					
				});
			}
		}
		if(CollectionUtils.isNotEmpty(warehouseIdList)){
			params.clear();
			params.put(MYBATIS_SPECIAL_STRING.COLUMNS.name(), " warehouse_id in ("+StringUtil.join(warehouseIdList, SPLIT_SIGN.COMMA)+")");
			params.put("userType", FastConstant.USER_TYPE.MANAGER.code);
			params.put("shopType", FastConstant.SHOP_TYPE.FAST_BUY.code);
			params.put("enabled", Constant.ENABLED.YES);
			List<FastUserInfo> userInfoList = fastUserInfoProxy.queryByParamNotEmpty(params).getData();
			if(CollectionUtils.isNotEmpty(userInfoList)){
				for(FastUserInfo fastUserInfo:userInfoList){
					try {
						Integer unReceive = warehouseMap.get(fastUserInfo.getWarehouseId()) == null ? 0 : warehouseMap.get(fastUserInfo.getWarehouseId());
						Integer unDelivery = warehouseUnMap.get(fastUserInfo.getWarehouseId()) == null ? 0 : warehouseUnMap.get(fastUserInfo.getWarehouseId());
						String shopName = StringUtil.isEmpty(fastUserInfo.getWarehouseName()) == true ? "" : fastUserInfo.getWarehouseName();
						LOG.info("速购监控订单短信发送:" + fastUserInfo.getMobile() + String.format(content, shopName, unReceive,unDelivery));
						sendSmsService.sendSms(fastUserInfo.getMobile(), String.format(content,shopName,unReceive,unDelivery), null);
					} catch (SmsException e) {
						LOG.error("速购监控订单，发送短信失败,\r\n{}",e);
					}
				}
			}
		}
	}

	@Override
	public String getFixed() {
		return this.getClass().getSimpleName();
	}

}
