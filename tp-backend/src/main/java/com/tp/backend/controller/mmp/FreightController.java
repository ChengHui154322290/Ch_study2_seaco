package com.tp.backend.controller.mmp;

import com.alibaba.fastjson.JSONObject;
import com.tp.common.vo.PageInfo;
import com.tp.dto.common.ResultInfo;
import com.tp.model.mmp.FreightTemplate;
import com.tp.proxy.mmp.FreightTemplateProxy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;


/**
 *
 */
@Controller
@Scope(BeanDefinition.SCOPE_SINGLETON)
@RequestMapping("/freight")
public class FreightController {

	public static final String PROMOTION_FREIGHT_SUBPAGES_EDIT = "promotion/freight/subpages/edit";
	public static final String PROMOTION_FREIGHT_SUBPAGES_ADD = "promotion/freight/subpages/add";
	@Autowired
	private FreightTemplateProxy freightTemplateProxy;
	
	@RequestMapping("/toEdit")
	public String edit(@RequestParam(value = "id") Long id, Model model) {
		ResultInfo<FreightTemplate> ft = freightTemplateProxy.getById(id);
		model.addAttribute("freightTemplate", ft.getData());
		return PROMOTION_FREIGHT_SUBPAGES_EDIT;
	}
	
	@RequestMapping(value="/toAdd")
	public String toAdd() {
		return PROMOTION_FREIGHT_SUBPAGES_ADD;
	}
	
	@RequestMapping(value="/add")
	@ResponseBody
	public ResultInfo add(HttpServletRequest request,
			@RequestParam(value="freightType", required=false) Integer freightType,
			@RequestParam(value="calculateMode", required=false) Integer calculateMode,
			@RequestParam(value="templateName", required=false) String templateName,
			@RequestParam(value="fullBy", required=false) String fullBy,
			@RequestParam(value="feeFullBy", required=false) String feeFullBy,
			@RequestParam(value="fee", required=false) String fee)  {
		Long userId = 0L;
		ResultInfo result =freightTemplateProxy.save(templateName, freightType, calculateMode, fee, fullBy, feeFullBy,userId);
		return result;
	}
	
	@RequestMapping(value="/edit")
	@ResponseBody
	public ResultInfo edit(HttpServletRequest request,
			@RequestParam(value="id", required=false) Long id,
			@RequestParam(value="freightType", required=false) Integer freightType,
			@RequestParam(value="calculateMode", required=false) Integer calculateMode,
			@RequestParam(value="templateName", required=false) String templateName,
			@RequestParam(value="fullBy", required=false) String fullBy,
			@RequestParam(value="feeFullBy", required=false) String feeFullBy,
			@RequestParam(value="fee", required=false) String fee)  {
		Long userId = 0L;
		ResultInfo result =freightTemplateProxy.edit(id, templateName, freightType, calculateMode, fee, fullBy, feeFullBy,userId);
		return result;
	}
	
	@RequestMapping(value="/list")
	public String list(HttpServletRequest request,
			@RequestParam(value="freightType",required=false) Integer freightType,
			@RequestParam(value="calculateMode",required=false) Integer calculateMode,
			@RequestParam(value="templateName",required=false) String templateName) {
		request.setAttribute("freightType", freightType == null ? 2 : freightType);
		request.setAttribute("calculateMode", calculateMode == null ? 2 : calculateMode);
		request.setAttribute("templateName", templateName == null ? "" : templateName);
		return "promotion/freight/freight";
	}

	@RequestMapping(value="/listJSON")
	@ResponseBody
	public JSONObject show(Model model,
			@RequestParam(value="page", defaultValue = "1") Integer page,
			@RequestParam(value="rows", defaultValue = "10") Integer rows,
			@RequestParam(value="freightType",required=false) Integer freightType,
			@RequestParam(value="calculateMode",required=false) Integer calculateMode,
			@RequestParam(value="templateName",required=false) String templateName) throws Exception {
		PageInfo<FreightTemplate> pageObj = freightTemplateProxy.
				getFreightTemplate(templateName, freightType, calculateMode, page, rows);
		JSONObject obj = freightTemplateProxy.toJson(pageObj, rows, page);
		model.addAttribute("freight", obj);
		return obj;
	}

}
