package com.tp.backend.controller.cms;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.simple.JSONArray;
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

import com.tp.common.vo.PageInfo;
import com.tp.dto.cms.ReturnData;
import com.tp.model.cms.Temple;
import com.tp.proxy.cms.TempleProxy;

@Controller
@Scope(BeanDefinition.SCOPE_SINGLETON)
@RequestMapping("/cms")
public class TempletController {
	private final static Log logger = LogFactory.getLog(TempletController.class);
	
	@Autowired
	private TempleProxy templeProxy;
	
	@InitBinder  
	protected void initBinder(HttpServletRequest request,  
	            ServletRequestDataBinder binder) throws Exception {   
	      DateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
	      CustomDateEditor dateEditor = new CustomDateEditor(fmt, true);  
	      binder.registerCustomEditor(Date.class, dateEditor);  
	}  
	
	/**
	 * 模板列表的查询
	 * @param 
	 * @return 页面
	 * @throws Exception
	 * @author huangqinbao 2014-12-29 16:17:54
	 */
	@RequestMapping(value={"/listTempleOpr"},method=RequestMethod.GET)
	public String listTempleOpr(Model model, Temple query){
		if(query == null){
			query = new Temple();
		}
		PageInfo<Temple> cmsTempleDO = templeProxy.getTempletList(query);
		if (cmsTempleDO != null) {
			model.addAttribute("pageList", cmsTempleDO);
			model.addAttribute("query", query);
		}
		return "cms/templet/templetList";
	}
	
	/**
	 * 查询模板列表
	 * @param 
	 * @return 页面
	 * @throws Exception
	 * @author huangqinbao 2014-12-29 16:17:54
	 */
	@RequestMapping(value={"/queryTempleList"},method=RequestMethod.POST)
	public String queryTempleList(Model model, Temple query){
		PageInfo<Temple> cmsTempleDO = templeProxy.getTempletList(query);
		if (cmsTempleDO != null) {
			model.addAttribute("pageList", cmsTempleDO);
			model.addAttribute("query", query);
		}
		return "cms/templet/templetList";
	}
	
	/**
	 * 模块管理的查询（返回字符串），给模块管理弹出框，通过页面名称查询到模块列表
	 * @param 
	 * @return 页面
	 * @throws Exception
	 * @author huangqinbao 2014-12-29 16:17:54
	 */
	@RequestMapping(value={"/queryTempletString"},method=RequestMethod.POST)
	@ResponseBody
	public ReturnData queryTempletString(Model model,String params) {
		ReturnData returnData = null;
		if(params != null){
			JSONObject jSONObject1 = new JSONObject();
			Object obj1 = JSONValue.parse(params);
			jSONObject1 = (JSONObject) obj1;
			Temple query = new Temple();
			query.setStatus(0);
			if(jSONObject1.get("name") != null){
				query.setTempleName(jSONObject1.get("name").toString());
			}
			if(!StringUtils.isEmpty(jSONObject1.get("pageId"))){
				query.setPageId(Long.parseLong(jSONObject1.get("pageId").toString()));
			}
			
			try {
				PageInfo<Temple> cmsPageDO = templeProxy.getTempletList(query);
				returnData = new ReturnData(Boolean.TRUE,cmsPageDO.getRows());
			} catch (Exception e) {
				e.printStackTrace();
				returnData = new ReturnData(Boolean.FALSE,null);
				logger.error("模块管理查询出错",e);
			}
		}
		
    	return returnData;
	}
	private JSONArray stringToObject(String params){
		Object obj = JSONValue.parse(params);
		JSONArray array = (JSONArray)obj;
		return array;
	}
	/**
	 * 模板创建和修改页面
	 * @param consigneeAddressDO
	 * @return 页面
	 * @throws Exception
	 * @author huangqinbao 2014-12-29 16:17:54
	 * @throws NumberFormatException 
	 */
	@RequestMapping(value={"/addTemplet.htm"},method=RequestMethod.POST)
	public String addTemplet(Model model,String params,String templetInfo) {
		if(params != null){//此处不为null，表示修改模板
			JSONObject jSONObject = new JSONObject();
			JSONArray array = stringToObject(params);
			jSONObject = (JSONObject)array.get(0);
			Temple sd = new Temple();
			try {
				sd = templeProxy.getById(Long.parseLong(jSONObject.get("id").toString()));
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("页面管理查询出错",e);
			}
			model.addAttribute("page", sd);
		}
		
		if(templetInfo != null){
			JSONObject jSONObject1 = new JSONObject();
			Object obj1 = JSONValue.parse(templetInfo);
			jSONObject1 = (JSONObject) obj1;
			model.addAttribute("pageCodeBak", jSONObject1.get("pageCodeBak"));
			model.addAttribute("pageNameBak", jSONObject1.get("pageNameBak"));
			model.addAttribute("statusBak", jSONObject1.get("statusBak"));
		}
		return "cms/templet/templetEdit";
	}
	
	/**
	 * 模板的删除
	 * @param announceAO
	 * @return 主键
	 * @throws Exception
	 * @author huangqinbao 2014-12-29 16:17:54
	 */
	@RequestMapping(value={"/delTemplet.htm"},method=RequestMethod.POST)
	@ResponseBody
	public ReturnData delTemplet(Model model,String params){
		ReturnData returnData = null;
		try {
			List<Long> ids = new ArrayList<Long>();
			 Object obj = JSONValue.parse(params);
			 JSONArray array = (JSONArray)obj;
			 for(int i=0;i<array.size();i++){
				 JSONObject obj2 = (JSONObject)array.get(i);
				 ids.add(Long.valueOf(obj2.get("id").toString()));
			 }
			int counts = templeProxy.delByIds(ids);
			if(counts == -1){
				returnData = new ReturnData(Boolean.FALSE,"删除失败，请先移除模块下面的模块");
			}else{
				returnData = new ReturnData(Boolean.TRUE,"删除成功");
			}
			
		} catch (Exception e) {
			returnData = new ReturnData(Boolean.FALSE,0);
			e.printStackTrace();
			logger.error("模块的删除出错",e);
		}
		return returnData;
	}
	
	/**
	 * 模板提交按钮事件
	 * @param 
	 * @return 主键
	 * @throws Exception
	 * @author huangqinbao 2014-12-29 16:17:54
	 */
	@RequestMapping(value={"/saveTemplet.htm"},method=RequestMethod.POST)
	@ResponseBody
	public ReturnData saveTemplet(Model model,Temple cmsTempleDO,HttpServletRequest request){
		ReturnData returnData = null;
		if(cmsTempleDO.getId()!=null && cmsTempleDO.getId()>0){
			Integer lid = templeProxy.updateSubmit(cmsTempleDO);
			if(lid < 1){
				returnData = new ReturnData(Boolean.FALSE,"模板管理保存报错");
				logger.error("模板管理保存报错");
			}else{
				returnData = new ReturnData(Boolean.TRUE,lid);
			}
		}else{
			Long lid = templeProxy.addSubmit(cmsTempleDO);
			if(lid < 1){
				returnData = new ReturnData(Boolean.FALSE,"模板管理保存报错");
				logger.error("模板管理保存报错");
			}else{
				returnData = new ReturnData(Boolean.TRUE,lid);
			}
		}
    	return returnData;
	}
}
