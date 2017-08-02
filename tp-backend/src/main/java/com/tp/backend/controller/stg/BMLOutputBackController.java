package com.tp.backend.controller.stg;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.tp.proxy.stg.BMLSoaProxy;
import com.tp.result.stg.OutputBackShipResult;

/**
 * 发货信息查询
 * @author 付磊
 * 2015年1月8日 下午8:36:35
 *
 */
@Controller
@RequestMapping("storage/bml/ooback/")
public class BMLOutputBackController {

	@Autowired
	private BMLSoaProxy bmlSoaProxy;
	
	@RequestMapping("index")
	public String index(){
		return "storage/bml/ooback/list";
	}
	
	@RequestMapping("list")
	public String list(String orderCode,String orderType,Model model){
		model.addAttribute("orderCode", orderCode);
		model.addAttribute("orderType", orderType);
		if(StringUtils.isBlank(orderCode)){
			model.addAttribute("errorMsg", "请输入订单号");
		}else if(StringUtils.isBlank(orderType)){
			model.addAttribute("errorMsg", "请选择订单类型");
		}else{
			orderCode = orderType+orderCode;
			List<OutputBackShipResult> shipResults = bmlSoaProxy.getOutputBackShipResult(orderCode);
			if(CollectionUtils.isNotEmpty(shipResults)){
				model.addAttribute("shipResults", shipResults.get(0));
			}
		}
		
		return "storage/bml/ooback/list";
	}
}
