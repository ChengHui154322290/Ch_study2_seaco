/**
 * 
 */
package com.tp.backend.controller.sys;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tp.backend.controller.AbstractBaseController;
import com.tp.common.vo.PageInfo;
import com.tp.common.vo.sys.CommonLogConstant.RestLogType;
import com.tp.dto.common.ResultInfo;
import com.tp.model.sys.ApicallLog;
import com.tp.model.sys.RestLog;
import com.tp.proxy.sys.ApicallLogProxy;
import com.tp.proxy.sys.RestLogProxy;

/**
 * @author Administrator
 *
 */
@Controller
@RequestMapping("/sys/log/")
public class ApicallLogController{

	@Autowired
	private ApicallLogProxy apicallLogProxy;
	
	@Autowired
	private RestLogProxy restLogProxy;
	
	@InitBinder
	public void init(HttpServletRequest request, ServletRequestDataBinder binder) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		dateFormat.setLenient(false);
		binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
	}
	
	@RequestMapping("apiloglist")
	public String callbackLog(Model model, ApicallLog log, Integer page, Integer size){
		ResultInfo<PageInfo<ApicallLog>> pageResult = apicallLogProxy.queryApiCallLog(log, new PageInfo<>(page, size));
		model.addAttribute("page", pageResult.getData());
		model.addAttribute("log", log);
		return "/sys/log/apicallback";
	}
	
	@RequestMapping("resendapi")
	@ResponseBody
	public ResultInfo<Boolean> resendApi(Model model, Long id){
		return apicallLogProxy.resendApicall(id);
	}
	
	
	@RequestMapping("restloglist")
	public String restLog(Model model, RestLog log, Integer page, Integer size){
		ResultInfo<PageInfo<RestLog>> pageResult = restLogProxy.queryRestfulLog(log, new PageInfo<>(page, size));
		model.addAttribute("page", pageResult.getData());
		model.addAttribute("log", log);
		model.addAttribute("restTypeList", RestLogType.values());
		return "/sys/log/restlog";
	}
}
