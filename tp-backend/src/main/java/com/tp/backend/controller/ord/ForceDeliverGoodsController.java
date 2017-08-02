package com.tp.backend.controller.ord;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.tp.backend.controller.AbstractBaseController;
import com.tp.dto.OrderOperatorErrorDTO;
import com.tp.dto.common.FailInfo;
import com.tp.dto.common.ResultInfo;
import com.tp.dto.ord.OrderDeliverDTO;
import com.tp.dto.ord.OrderDeliverListDTO;
import com.tp.dto.stg.ResultOrderDeliverDTO;
import com.tp.model.ord.OrderDelivery;
import com.tp.model.ord.SubOrder;
import com.tp.model.usr.UserInfo;
import com.tp.service.ord.remote.ISalesOrderRemoteService;
/**
 * 未发货的订单发货时出错，后台强制发货
 * @author szy
 *
 */
@Controller
@RequestMapping("/salesorder/")
public class ForceDeliverGoodsController  extends AbstractBaseController{

	private static final Logger LOG = LoggerFactory.getLogger(ForceDeliverGoodsController.class);
	@Autowired
	private ISalesOrderRemoteService salesOrderRemoteService;
	
	@RequestMapping(value="forcedelivergoods",method = RequestMethod.GET)
	public void index(Model model){
		
	}
	
	@RequestMapping(value="forcedelivergoods",method = RequestMethod.POST)
	public void deliveryGoods(Model model, OrderDeliverListDTO deliver){
		UserInfo user = getUserInfo();
		if(null == deliver.getDeliverList()){
			model.addAttribute("resultData", new ResultInfo<Boolean>(new FailInfo("数据不存在", 1)));
			return;
		}
		if(CollectionUtils.isEmpty(deliver.getDeliverList())){
			model.addAttribute("resultData", new ResultInfo<Boolean>(new FailInfo("数据为空", 2)));
			return;
		}
		if(deliver.getDeliverList().size()>100){
			model.addAttribute("resultData", new ResultInfo<Boolean>(new FailInfo("强制发货数量超过100", 3)));
			return;
		}
		for(OrderDeliverDTO orderDeliverDTO: deliver.getDeliverList()){
			orderDeliverDTO.setCreateUserId(user.getId());
		}
		LOG.info("backend delivery goods userName="+user.getUserName()+" userId="+user.getId()+" userLoginName="+user.getLoginName()+" ip+"+user.getLastLoginIp()+" deliverys >>"+deliver.toString());
		List<Long> subOrderCodeList = new ArrayList<>();
		List<OrderDelivery> orderDeliveries = new ArrayList<>();
		for(OrderDeliverDTO orderDeliverDTO: deliver.getDeliverList()){
			subOrderCodeList.add(Long.valueOf(orderDeliverDTO.getSubOrderCode()));
			OrderDelivery orderDeliveryObj = new OrderDelivery();
			orderDeliveryObj.setOrderCode(Long.valueOf(orderDeliverDTO.getSubOrderCode()));
			orderDeliveryObj.setCompanyName(orderDeliverDTO.getCompanyName());
			orderDeliveryObj.setCompanyId(orderDeliverDTO.getCompanyId());
			orderDeliveryObj.setPackageNo(orderDeliverDTO.getPackageNo());
			orderDeliveryObj.setCreateUser(orderDeliverDTO.getCreateUserId().toString());
			orderDeliveryObj.setCreateTime(new Date());
			orderDeliveries.add(orderDeliveryObj);
		}
		ResultOrderDeliverDTO results = salesOrderRemoteService.operateOrderListForDeliver(orderDeliveries);		
		List<SubOrder> subOrderList = salesOrderRemoteService.findSubOrderDTOListBySubCodeList(subOrderCodeList);
		
		Map<String, OrderOperatorErrorDTO> errorMap = new HashMap<String, OrderOperatorErrorDTO>();
		if(CollectionUtils.isNotEmpty(subOrderList)){
			if(results==null){
				results = new ResultOrderDeliverDTO();
			}
			if(CollectionUtils.isEmpty(results.getOrderOperatorErrorList())){
				results.setOrderOperatorErrorList(new ArrayList<OrderOperatorErrorDTO>());
			}else{
				for(OrderOperatorErrorDTO error:results.getOrderOperatorErrorList()){
					errorMap.put("" + error.getSubOrderCode(), error);
				}
			}
			for(SubOrder subOrder:subOrderList){
				OrderOperatorErrorDTO error= errorMap.get(subOrder.getOrderCode() + "");
				if(error==null){
					error = new OrderOperatorErrorDTO(subOrder.getOrderCode(),null,"发货成功");
					errorMap.put("" + subOrder.getOrderCode(), error);
				}
				error.setOrderStatus(subOrder.getStatusStr());
			}
			List<OrderOperatorErrorDTO> errors = results.getOrderOperatorErrorList();
			errors.clear();
			for(OrderOperatorErrorDTO error:errorMap.values()){
				errors.add(error);
			}
		}
		
		model.addAttribute("results", results);
	}
}
