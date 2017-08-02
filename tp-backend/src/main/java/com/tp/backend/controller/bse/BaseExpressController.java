package com.tp.backend.controller.bse;

import java.util.Date;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tp.common.vo.PageInfo;
import com.tp.dto.common.ResultInfo;
import com.tp.model.bse.ExpressInfo;
import com.tp.proxy.bse.ExpressInfoProxy;

@Controller
@RequestMapping("/basedata/express")
public class BaseExpressController {

	static final private Integer ADD_EXPRESS = 1;
	static final private Integer UPDATE_EXPRESS=2;
	@Autowired
	private ExpressInfoProxy expressInfoProxy;

	@RequestMapping({ "/list" })
	public String list(
			Model model,
			ExpressInfo expressInfoDO,Integer page,Integer size) {
		PageInfo<ExpressInfo> pageInfo = expressInfoProxy.queryPageByObject(expressInfoDO,new PageInfo<ExpressInfo>(page, size)).getData();
		model.addAttribute("page", pageInfo);
		model.addAttribute("expressInfoDO", expressInfoDO);
		if (CollectionUtils.isEmpty(pageInfo.getRows())) {
			model.addAttribute("noRecoders", "没有记录");
		}
		return "basedata/express/list";
	}

	@RequestMapping(value = "/add")
	public void add() {
	}

	@RequestMapping("/save")
	@ResponseBody
	public ResultInfo<Boolean> save(ExpressInfo expressInfo) {
		ResultInfo<Boolean> message = expressInfoProxy.validateData(expressInfo, ADD_EXPRESS);
		if (Boolean.TRUE == message.isSuccess()) {
			ExpressInfo insertExpressInfo = new ExpressInfo();
			insertExpressInfo.setCode(expressInfo.getCode());
			insertExpressInfo.setName(expressInfo.getName());
			insertExpressInfo.setSortNo(expressInfo.getSortNo());
			insertExpressInfo.setModifyTime(new Date());
			insertExpressInfo.setCreateTime(new Date());
			expressInfoProxy.insert(insertExpressInfo);
			return new ResultInfo<Boolean>(Boolean.TRUE);
		}
		return message;
	}

	@RequestMapping(value = "/edit")
	public String edit(Model model, @RequestParam(value = "id") Long id){
		if (null == id) {
			return "/basedata/express/edit";
		}
		ExpressInfo expressInfoDO = expressInfoProxy.queryById(id).getData();
		model.addAttribute("expressInfoDO", expressInfoDO);
		return "/basedata/express/edit";
	}

	@RequestMapping("/update")
	@ResponseBody
	public ResultInfo<Boolean> update(ExpressInfo expressInfo) {
		ResultInfo<Boolean> message = expressInfoProxy.validateData(expressInfo, UPDATE_EXPRESS);
		if (Boolean.TRUE == message.isSuccess()) {			
			ExpressInfo updateExpressInfo = new ExpressInfo();
			updateExpressInfo.setId(expressInfo.getId());
			updateExpressInfo.setCode(expressInfo.getCode());
			updateExpressInfo.setName(expressInfo.getName());
			updateExpressInfo.setSortNo(expressInfo.getSortNo());
			updateExpressInfo.setModifyTime(new Date());
			expressInfoProxy.updateNotNullById(updateExpressInfo);
			return new ResultInfo<Boolean>(Boolean.TRUE);
		}
		return message;
	}

}
