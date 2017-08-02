package com.tp.backend.controller.stg;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 出库汇总（标杆）
 * @author 付磊
 * 2015年1月8日 下午8:36:35
 *
 */
@Controller
@RequestMapping("storage/bml/ooordersumary/")
public class BMLOutputOrderSummaryController {

	@RequestMapping("list")
	public String list(){
		return "storage/bml/ooordersumary/list";
	}
}
