package com.tp.service.ord.mq;

import net.sf.json.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.mq.MqMessageCallBack;
import com.tp.service.ord.ISubOrderService;

/**
 * 海淘商品海关审核状态更新
 * @author szy
 *
 */
@Service
public class SeaOrderAuditStatusChangeCallback  implements MqMessageCallBack{

	private static final Logger LOG = LoggerFactory.getLogger(SeaOrderAuditStatusChangeCallback.class);
	@Autowired
	private ISubOrderService subOrderService;
	@Override
	public boolean execute(Object o) {
		if(o instanceof String){
			try{
			JSONObject jsonObj = JSONObject.fromObject((String)o);
			Long orderNo = jsonObj.getLong("orderNum");
			String statusCode = jsonObj.getString("statusCode");
			Boolean isSuccess = jsonObj.getBoolean("isSuccess");
			String message = jsonObj.getString("message");
			subOrderService.updateOrderPutStatus(orderNo, isSuccess, "审核状态："+statusCode+message);
			}catch(Exception e){
				LOG.error("{} update putStatus is error!{}",o,e);
			}
		}else{
			LOG.error("{} not is String json!",o.toString());
		}
		return Boolean.TRUE;
	}

}
