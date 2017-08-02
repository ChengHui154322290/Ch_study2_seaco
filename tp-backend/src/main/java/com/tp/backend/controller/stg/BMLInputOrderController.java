package com.tp.backend.controller.stg;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.tp.common.vo.stg.BMLStorageConstant.InputOrderType;
import com.tp.proxy.stg.BMLSoaProxy;
import com.tp.result.stg.ASNsResult;


/**
 * 入库信息查询（标杆）
 * @author 付磊
 * 2015年1月8日 下午8:36:35
 *
 */
@Controller
@RequestMapping("storage/bml/inputorder/")
public class BMLInputOrderController {

	@Autowired
	private BMLSoaProxy bmlSoaProxy;
	
	@RequestMapping("index")
	public String index(){
		return "storage/bml/inputorder/list";
	}
	
	@RequestMapping("list")
	public String list(String orderCode,Model model){
		model.addAttribute("orderCode", orderCode);
		if(StringUtils.isBlank(orderCode)){
			model.addAttribute("errorMsg", "请输入订单号");
		}else{
			orderCode = InputOrderType.FG.getCode()+orderCode;
			List<ASNsResult> asNsResults = bmlSoaProxy.selectInputOrderByOrderCode(orderCode);
			model.addAttribute("asNsResults", asNsResults);
		}
		
		return "storage/bml/inputorder/list";
	}
}
