package com.tp.m.controller.order;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tp.m.ao.order.YiQiFaOrderAO;
import com.tp.m.base.MResultVO;
import com.tp.m.exception.MobileException;
import com.tp.m.helper.RequestHelper;
import com.tp.m.query.order.YiQiFaQueryOrder;
import com.tp.m.util.JsonUtil;

@Controller
@RequestMapping("/order")
public class YiQiFaOrderController {

	private static final Logger log = LoggerFactory.getLogger(YiQiFaOrderController.class);
	
	@Autowired
	private YiQiFaOrderAO yiQiFaOrderAO;
	
	/**
	 * 订单列表
	 * @return
	 */
	@RequestMapping(value="/list/yiqifa")
	@ResponseBody
	public String getOrderList(HttpServletRequest request,YiQiFaQueryOrder queryOrder){
		queryOrder.setRequestIp(RequestHelper.getIpAddr(request));
		try{
			if(log.isInfoEnabled()){
				log.info("[API接口 - 订单列表 入参] = {}",JsonUtil.convertObjToStr(queryOrder));
			}
			String result = yiQiFaOrderAO.queryOrderListByChannelYiQiFaParams(queryOrder);
			if(log.isInfoEnabled()){
				log.info("[API接口 - 订单列表 返回值] = {}",result);
			}
			return JsonUtil.convertObjToStr(result);
		}catch(MobileException me){
			log.error("[API接口 - 订单列表  MobileException] = {}",me.getMessage());
			log.error("[API接口 - 订单列表 返回值] = {}",JsonUtil.convertObjToStr(new MResultVO<>(me)));
			return JsonUtil.convertObjToStr(new MResultVO<>(me));
		}
	}
}
