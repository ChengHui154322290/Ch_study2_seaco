package com.tp.backend.controller.cms;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tp.dto.cms.ReturnData;
import com.tp.model.cms.DefinedElement;
import com.tp.proxy.cms.DefinedElementProxy;
import com.tp.util.DateUtil;


@Controller
@Scope(BeanDefinition.SCOPE_SINGLETON)
@RequestMapping("/cmstemplet")
public class TempletDefinedElementController {
	private final static Log logger = LogFactory.getLog(TempletDefinedElementController.class);
	
	
	@Autowired
	private DefinedElementProxy definedElementProxy;
	
	@InitBinder  
	protected void initBinder(HttpServletRequest request,  
	            ServletRequestDataBinder binder) throws Exception {   
	      DateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
	      CustomDateEditor dateEditor = new CustomDateEditor(fmt, true);  
	      binder.registerCustomEditor(Date.class, dateEditor);  
	}  
	
	/**
	 * 自定义元素列表的查询
	 * @param 
	 * @return 页面
	 * @throws Exception
	 * @author huangqinbao 2014-12-29 16:17:54
	 */
	@RequestMapping(value={"/listDefinedElement"},method=RequestMethod.GET)
	public String listTempleOpr(Model model,HttpServletRequest request,Long positionId,
			String pageName,String templeName,String positionName){
		List<DefinedElement> lst = new ArrayList<DefinedElement>();
		try {
			lst = definedElementProxy.getDefinedElement(positionId);
		} catch (Exception e) {
			logger.error("自定义元素列表的查询出错",e);
			e.printStackTrace();
		}
		model.addAttribute("detaillist", lst);
		model.addAttribute("positionId", positionId);
		model.addAttribute("pageName", pageName);
		model.addAttribute("templeName", templeName);
		model.addAttribute("positionName", positionName);
		
		model.addAttribute("pageNameBak", request.getParameter("pageNameBak"));
		model.addAttribute("templeNameBak", request.getParameter("templeNameBak"));
		model.addAttribute("positionNameBak", request.getParameter("positionNameBak"));
		
		return "cms/templet/definedElementEdit";
	}
	
	/**
	 * 自定义元素列表的编辑
	 * @param 
	 * @return 页面
	 * @throws Exception
	 * @author huangqinbao 2014-12-29 16:17:54
	 */
	@RequestMapping(value={"/editDefinedElement"},method=RequestMethod.POST)
	public String editDefinedElement(Model model,String params,String adv){
		Object obj = JSONValue.parse(params);
		JSONObject jSONObject = (JSONObject)obj;
		
		Long positionId = Long.parseLong(jSONObject.get("positionId").toString());
		String pageName = jSONObject.get("pageName").toString();
		String templeName = jSONObject.get("templeName").toString();
		String positionName = jSONObject.get("positionName").toString();
		
		Long id = Long.parseLong(jSONObject.get("id").toString());
		
		DefinedElement mod = new DefinedElement();
		mod.setId(id);
		mod = definedElementProxy.selectById(id);
		
		List<DefinedElement> lst = new ArrayList<DefinedElement>();
		try {
			lst = definedElementProxy.getDefinedElement(positionId);
		} catch (Exception e) {
			logger.error("自定义元素列表的查询出错",e);
			e.printStackTrace();
		}
		model.addAttribute("mod", mod);
		model.addAttribute("detaillist", lst);
		model.addAttribute("positionId", positionId);
		model.addAttribute("pageName", pageName);
		model.addAttribute("templeName", templeName);
		model.addAttribute("positionName", positionName);
		
		if(adv != null){
			Object obj1 = JSONValue.parse(adv);
			JSONObject jSONObject1 = (JSONObject)obj1;
			model.addAttribute("pageNameBak", jSONObject1.get("pageNameBak"));
			model.addAttribute("templeNameBak", jSONObject1.get("templeNameBak"));
			model.addAttribute("positionNameBak", jSONObject1.get("positionNameBak"));
		}
		
		return "cms/templet/definedElementEdit";
	}
	
	/**
	 * 自定义元素的删除
	 * @param announceAO
	 * @return 主键
	 * @throws Exception
	 * @author huangqinbao 2014-12-29 16:17:54
	 */
	@RequestMapping(value={"/delDefinedElement.htm"},method=RequestMethod.POST)
	@ResponseBody
	public ReturnData delTemplet(Model model,String params){
		ReturnData returnData = null;
		try {
			Object obj = JSONValue.parse(params);
			JSONObject jSONObject = (JSONObject)obj;
			int counts = definedElementProxy.delById(Long.valueOf(jSONObject.get("id").toString()));
			returnData = new ReturnData(Boolean.TRUE,counts);
			
		} catch (Exception e) {
			returnData = new ReturnData(Boolean.FALSE,0);
			e.printStackTrace();
			logger.error("自定义元素的删除出错",e);
		}
		return returnData;
	}
	
	/**
	 * 自定义元素管理提交按钮事件
	 * @param 
	 * @return 主键
	 * @throws Exception
	 * @author huangqinbao 2014-12-29 16:17:54
	 * @throws Exception 
	 */
	@RequestMapping(value={"/saveDefinedElement.htm"},method=RequestMethod.POST)
	@ResponseBody
	public ReturnData saveTemplet(Model model,String params,HttpServletRequest request) throws Exception{
		Object obj = JSONValue.parse(params);
		JSONObject jSONObject = (JSONObject)obj;
		
		DefinedElement cmsDefinedElementDO = new DefinedElement();
		
		if(!StringUtils.isEmpty(jSONObject.get("positionId"))){
			cmsDefinedElementDO.setPositionId(Long.valueOf(jSONObject.get("positionId").toString()));
		 }
		if(!StringUtils.isEmpty(jSONObject.get("name"))){
			cmsDefinedElementDO.setName(jSONObject.get("name").toString());
		 }
		if(!StringUtils.isEmpty(jSONObject.get("startdate"))){
			cmsDefinedElementDO.setStartdate(DateUtil.parse(jSONObject.get("startdate").toString(), DateUtil.NEW_FORMAT));
		 }
		if(!StringUtils.isEmpty(jSONObject.get("enddate"))){
			cmsDefinedElementDO.setEnddate(DateUtil.parse(jSONObject.get("enddate").toString(), DateUtil.NEW_FORMAT));
		 }
		if(!StringUtils.isEmpty(jSONObject.get("status"))){
			cmsDefinedElementDO.setStatus(Integer.parseInt(jSONObject.get("status").toString()));
		 }
		if(!StringUtils.isEmpty(jSONObject.get("id"))){
			cmsDefinedElementDO.setId(Long.parseLong(jSONObject.get("id").toString()));
		 }
		if(!StringUtils.isEmpty(jSONObject.get("content"))){
			cmsDefinedElementDO.setContent(jSONObject.get("content").toString());
		 }
		
		ReturnData returnData = null;
		if(cmsDefinedElementDO.getId()!=null && cmsDefinedElementDO.getId()>0){
			Integer lid = definedElementProxy.updateSubmit(cmsDefinedElementDO);
			if(lid < 1){
				returnData = new ReturnData(Boolean.FALSE,"自定义元素保存报错");
				logger.error("自定义元素保存报错");
			}else{
				returnData = new ReturnData(Boolean.TRUE,"保存成功");
			}
		}else{
			cmsDefinedElementDO.setCreater(1l);
			cmsDefinedElementDO.setCreateTime(new Date());
			cmsDefinedElementDO.setModifier(1l);
			cmsDefinedElementDO.setModifyTime(new Date());
			Long lid = definedElementProxy.addSubmit(cmsDefinedElementDO);
			if(lid < 1){
				returnData = new ReturnData(Boolean.FALSE,"模板管理保存报错");
				logger.error("模板管理保存报错");
			}else{
				returnData = new ReturnData(Boolean.TRUE,"保存成功");
			}
		}
    	return returnData;
	}
}
