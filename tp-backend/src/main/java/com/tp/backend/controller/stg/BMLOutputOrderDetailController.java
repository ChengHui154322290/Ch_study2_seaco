package com.tp.backend.controller.stg;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.tp.proxy.stg.BMLSoaProxy;
import com.tp.result.stg.OrdersResult;

/**
 * 出库明细查询(标杆）
 * @author 付磊
 * 2015年1月8日 下午8:36:35
 *
 */
@Controller
@RequestMapping("storage/bml/ooorderdetail")
public class BMLOutputOrderDetailController {

	@Autowired
	private BMLSoaProxy bmlSoaProxy;
	
	@RequestMapping("index")
	public String index(){
		return "storage/bml/ooorderdetail/list";
	}
	
	@RequestMapping("list")
	public String list(String orderCode,String orderType,Model model){
		model.addAttribute("orderCode", orderCode);
		model.addAttribute("orderType", orderType);
		if(StringUtils.isEmpty(orderCode)){
			model.addAttribute("errorMsg", "请输入订单编号");
		}else if(StringUtils.isEmpty(orderType)){
			model.addAttribute("errorMsg", "请输选择订单类型");
		}else{
			orderCode = orderType+orderCode;
			List<OrdersResult> ordersResults = bmlSoaProxy.selectOrderDetailByCode(orderCode);
			model.addAttribute("ordersResults", ordersResults);
		}
		
		return "storage/bml/ooorderdetail/list";
	}
}
