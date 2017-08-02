package com.tp.backend.controller.stg;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.tp.common.vo.stg.BMLStorageConstant.OutputOrderStatus;
import com.tp.dto.common.FailInfo;
import com.tp.dto.common.ResultInfo;
import com.tp.proxy.stg.BMLSoaProxy;
import com.tp.result.stg.ResponseResult;


/**
 * 订单状态查询（标杆）
 * 
 * @author 付磊 2015年1月8日 下午8:36:35
 *
 */
@Controller
@RequestMapping("storage/bml/ooorder-status/")
public class BMLOutputOrderStatusController {

	@Autowired
	private BMLSoaProxy bMLSoaProxy;

	@RequestMapping("index")
	public String index() {
		return "storage/bml/ooorderstatus/index";
	}

	@RequestMapping("search")
	public String search(String orderCode, String orderType, Model model) {
		if (StringUtils.isBlank(orderCode)) {
			model.addAttribute("resultMessage", new ResultInfo<Boolean>(new FailInfo("请输入订单号")));
			return "storage/bml/ooorderstatus/index";
		}
		if (StringUtils.isBlank(orderType)) {
			model.addAttribute("resultMessage", new ResultInfo<Boolean>(new FailInfo("请选择订单类型")));
			return "storage/bml/ooorderstatus/index";
		}
		model.addAttribute("orderCode", orderCode);
		model.addAttribute("orderType", orderType);
		orderCode = orderType+orderCode;
		ResponseResult resultMessage = bMLSoaProxy.queryOrderStatusByOrderCode(orderCode);
		model.addAttribute("resultMessage", resultMessage);
		if(resultMessage.getSuccess().equals(Boolean.TRUE.toString())&&StringUtils.isBlank(resultMessage.getDesc())){
			resultMessage.setDesc(OutputOrderStatus.getDescByCode(resultMessage.getCode()));
		}
		return "storage/bml/ooorderstatus/index";
	}
}
