package com.tp.backend.controller.stg;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.tp.proxy.stg.BMLSoaProxy;
import com.tp.result.stg.StockResult;

/**
 * 库存查询（标杆）
 * @author 付磊
 * 2015年1月8日 下午8:36:35
 *
 */
@Controller
@RequestMapping("storage/bml/inventory/")
public class BMLInventoryController {
	
	@Autowired
	private BMLSoaProxy bmlSoaProxy;
	
	@RequestMapping("index")
	public String index(){
		return "storage/bml/inventory/list";
	}
	
	/***
	 * 
	 * @param model
	 * @param specDO
	 * @throws Exception 
	 */
	@RequestMapping(value = "/list")
	public ModelAndView list(Model model,
			@RequestParam(value = "skuCode", defaultValue = "")  String skuCode	) throws Exception{
		ModelAndView mv = new ModelAndView();
		if(StringUtils.isNotBlank(skuCode)){
			List<StockResult> stockResults = bmlSoaProxy.searchInventory(skuCode);
			mv.addObject("stockResults",stockResults);
		}else{
			model.addAttribute("errorMsg", "请输入sku");
		}
		mv.addObject("skuCode", skuCode);
	    mv.setViewName("/storage/bml/inventory/list");
	    return mv;
	}
}
