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
import com.tp.model.cms.WrittenElement;
import com.tp.proxy.cms.TempleProxy;
import com.tp.proxy.cms.WrittenElementProxy;
import com.tp.util.DateUtil;

@Controller
@Scope(BeanDefinition.SCOPE_SINGLETON)
@RequestMapping("/cmstemplet")
public class TempletWritElementController {
	private final static Log logger = LogFactory.getLog(TempletWritElementController.class);
	
	@Autowired
	private TempleProxy templeProxy;
	
	@Autowired
	private WrittenElementProxy writtenElementProxy;
	
	@InitBinder  
	protected void initBinder(HttpServletRequest request,  
	            ServletRequestDataBinder binder) throws Exception {   
	      DateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
	      CustomDateEditor dateEditor = new CustomDateEditor(fmt, true);  
	      binder.registerCustomEditor(Date.class, dateEditor);  
	}  
	
	/**
	 * 文字元素列表的查询
	 * @param 
	 * @return 页面
	 * @throws Exception
	 * @author huangqinbao 2014-12-29 16:17:54
	 */
	@RequestMapping(value={"/listWrittenElement"},method=RequestMethod.GET)
	public String listTempleOpr(Model model,HttpServletRequest request, Long positionId,
			String pageName,String templeName,String positionName){
		List<WrittenElement> lst = new ArrayList<WrittenElement>();
		try {
			lst = writtenElementProxy.getWrittenElement(positionId);
		} catch (Exception e) {
			logger.error("文字元素列表的查询出错",e);
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
		
		return "cms/templet/writtenElementEdit";
	}
	
	
	/**
	 * 文字元素的删除
	 * @param announceAO
	 * @return 主键
	 * @throws Exception
	 * @author huangqinbao 2014-12-29 16:17:54
	 */
	@RequestMapping(value={"/delWrittenElement.htm"},method=RequestMethod.POST)
	@ResponseBody
	public ReturnData delTemplet(Model model,String params){
		ReturnData returnData = null;
		try {
			Object obj = JSONValue.parse(params);
			JSONObject jSONObject = (JSONObject)obj;
			Integer counts = writtenElementProxy.deleteById(Long.valueOf(jSONObject.get("id").toString())).getData();
			returnData = new ReturnData(Boolean.TRUE,counts);
			
		} catch (Exception e) {
			returnData = new ReturnData(Boolean.FALSE,0);
			e.printStackTrace();
			logger.error("页面的删除出错",e);
		}
		return returnData;
	}
	
	/**
	 * 文字元素管理提交按钮事件
	 * @param 
	 * @return 主键
	 * @throws Exception
	 * @author huangqinbao 2014-12-29 16:17:54
	 * @throws Exception 
	 */
	@RequestMapping(value={"/saveWrittenElement.htm"},method=RequestMethod.POST)
	@ResponseBody
	public ReturnData saveTemplet(Model model,String params,HttpServletRequest request) throws Exception{
		Object obj = JSONValue.parse(params);
		JSONObject jSONObject = (JSONObject)obj;
		
		WrittenElement cmsWrittenElementDO = new WrittenElement();
		
		if(!StringUtils.isEmpty(jSONObject.get("positionId"))){
			cmsWrittenElementDO.setPositionId(Long.valueOf(jSONObject.get("positionId").toString()));
		 }
		if(!StringUtils.isEmpty(jSONObject.get("name"))){
			cmsWrittenElementDO.setName(jSONObject.get("name").toString());
		 }
		if(!StringUtils.isEmpty(jSONObject.get("link"))){
			cmsWrittenElementDO.setLink(jSONObject.get("link").toString());
		 }
		if(!StringUtils.isEmpty(jSONObject.get("startdate"))){
			cmsWrittenElementDO.setStartdate(DateUtil.parse(jSONObject.get("startdate").toString(), DateUtil.NEW_FORMAT));
		 }
		if(!StringUtils.isEmpty(jSONObject.get("enddate"))){
			cmsWrittenElementDO.setEnddate(DateUtil.parse(jSONObject.get("enddate").toString(), DateUtil.NEW_FORMAT));
		 }
		if(!StringUtils.isEmpty(jSONObject.get("status"))){
			cmsWrittenElementDO.setStatus(Integer.parseInt(jSONObject.get("status").toString()));
		 }
		if(!StringUtils.isEmpty(jSONObject.get("id"))){
			cmsWrittenElementDO.setId(Long.parseLong(jSONObject.get("id").toString()));
		 }
		
		ReturnData returnData = null;
		if(cmsWrittenElementDO.getId()!=null && cmsWrittenElementDO.getId()>0){
			Integer lid = writtenElementProxy.updateSubmit(cmsWrittenElementDO);
			if(lid < 1){
				returnData = new ReturnData(Boolean.FALSE,"文字元素保存报错");
				logger.error("文字元素保存报错");
			}else{
				returnData = new ReturnData(Boolean.TRUE,"保存成功");
			}
		}else{
			cmsWrittenElementDO.setCreater(1l);
			cmsWrittenElementDO.setCreateTime(new Date());
			cmsWrittenElementDO.setModifier(1l);
			cmsWrittenElementDO.setModifyTime(new Date());
			Long lid = writtenElementProxy.addSubmit(cmsWrittenElementDO);
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
