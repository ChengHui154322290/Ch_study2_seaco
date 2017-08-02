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
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tp.common.vo.PageInfo;
import com.tp.dto.cms.ReturnData;
import com.tp.dto.cms.query.CmsPageQuery;
import com.tp.model.cms.Page;
import com.tp.proxy.cms.PageProxy;

/**
 * 页面管理
 * @author hqb
 *
 */
@Controller
@Scope(BeanDefinition.SCOPE_SINGLETON)
@RequestMapping("/cms")
public class TempletPageController {
	private final static Log logger = LogFactory.getLog(TempletPageController.class);
	
	@Autowired
	private PageProxy pageProxy;
	
	@InitBinder  
	protected void initBinder(HttpServletRequest request,  
	            ServletRequestDataBinder binder) throws Exception {   
	      DateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
	      CustomDateEditor dateEditor = new CustomDateEditor(fmt, true);  
	      binder.registerCustomEditor(Date.class, dateEditor);  
	}  
	
	/**
	 * 页面管理列表的查询
	 * @param 
	 * @return 页面
	 * @throws Exception
	 * @author huangqinbao 2014-12-29 16:17:54
	 */
	@RequestMapping(value={"/listPageOpr"},method=RequestMethod.GET)
	public String listPageOpr(Model model,CmsPageQuery query){
		if(query == null){
			query = new CmsPageQuery();
		}
		PageInfo<Page> cmsPageDO = pageProxy.getPageList(query);
		if (cmsPageDO != null) {
			model.addAttribute("pageList", cmsPageDO);
			model.addAttribute("query", query);
		}
		return "cms/templet/pageList";
	}
	
	/**
	 * 查询页面管理列表
	 * @param 
	 * @return 页面
	 * @throws Exception
	 * @author huangqinbao 2014-12-29 16:17:54
	 */
	@RequestMapping(value={"/queryPageList"},method=RequestMethod.POST)
	public String queryMasterPlanOrderList(Model model, CmsPageQuery query){
		PageInfo<Page> cmsPageDO = pageProxy.getPageList(query);
		if (cmsPageDO != null) {
			model.addAttribute("pageList", cmsPageDO);
			model.addAttribute("query", query);
		}
		return "cms/templet/pageList";
	}
	
	/**
	 * 页面管理的查询（返回字符串），给页面管理弹出框，通过页面名称查询到页面列表
	 * @param 
	 * @return 页面
	 * @throws Exception
	 * @author huangqinbao 2014-12-29 16:17:54
	 */
	@RequestMapping(value={"/queryPageString"},method=RequestMethod.POST)
	@ResponseBody
	public ReturnData queryPageString(Model model,String params) {
		ReturnData returnData = null;
		if(params != null){//此处不为null，表示修改模板
			JSONObject jSONObject1 = new JSONObject();
			Object obj1 = JSONValue.parse(params);
			jSONObject1 = (JSONObject) obj1;
			CmsPageQuery query = new CmsPageQuery();
			query.setStatus(0);
			if(jSONObject1.get("name") != null){
				query.setPageName(jSONObject1.get("name").toString());
			}
			
			try {
				PageInfo<Page> cmsPageDO = pageProxy.getPageList(query);
				returnData = new ReturnData(Boolean.TRUE,cmsPageDO.getRows());
			} catch (Exception e) {
				e.printStackTrace();
				returnData = new ReturnData(Boolean.FALSE,null);
				logger.error("页面管理查询出错",e);
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
	 * 页面创建和修改页面
	 * @param consigneeAddressDO
	 * @return 页面
	 * @throws Exception
	 * @author huangqinbao 2014-12-29 16:17:54
	 * @throws NumberFormatException 
	 */
	@RequestMapping(value={"/addOrEditPageTemplet.htm"},method=RequestMethod.POST)
	public String addOrEditPageTemplet(Model model,String params,String pageInfo) {
		if(params != null){//此处不为null，表示修改模板
			JSONObject jSONObject = new JSONObject();
			JSONArray array = stringToObject(params);
			jSONObject = (JSONObject)array.get(0);
			//model.addAttribute("id", jSONObject.get("id"));
			Page sd = new Page();
			try {
				sd = pageProxy.getPageById(Long.parseLong(jSONObject.get("id").toString()));
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("页面管理查询出错",e);
			}
			model.addAttribute("page", sd);
		}
		
		if(pageInfo != null){
			JSONObject jSONObject1 = new JSONObject();
			Object obj1 = JSONValue.parse(pageInfo);
			jSONObject1 = (JSONObject) obj1;
			model.addAttribute("pageCodeBak", jSONObject1.get("pageCodeBak"));
			model.addAttribute("pageNameBak", jSONObject1.get("pageNameBak"));
			model.addAttribute("statusBak", jSONObject1.get("statusBak"));
		}
		return "cms/templet/pageEdit";
	}
	
	/**
	 * 页面管理的删除
	 * @param announceAO
	 * @return 主键
	 * @throws Exception
	 * @author huangqinbao 2014-12-29 16:17:54
	 */
	@RequestMapping(value={"/delPage.htm"},method=RequestMethod.POST)
	@ResponseBody
	public ReturnData delPage(Model model,String params){
		ReturnData returnData = null;
		try {
			List<Long> ids = new ArrayList<Long>();
			 Object obj = JSONValue.parse(params);
			 JSONArray array = (JSONArray)obj;
			 for(int i=0;i<array.size();i++){
				 JSONObject obj2 = (JSONObject)array.get(i);
				 ids.add(Long.valueOf(obj2.get("id").toString()));
			 }
			int counts = pageProxy.delPageByIds(ids);
			if(counts == -1){
				returnData = new ReturnData(Boolean.FALSE,"删除失败，请先移除页面下面的模块");
			}else{
				returnData = new ReturnData(Boolean.TRUE,"删除成功");
			}
		} catch (Exception e) {
			returnData = new ReturnData(Boolean.FALSE,"删除失败，请联系管理员");
			e.printStackTrace();
			logger.error("页面的删除出错",e);
		}
		return returnData;
	}
	
	/**
	 * 页面提交按钮事件
	 * @param 
	 * @return 主键
	 * @throws Exception
	 * @author huangqinbao 2014-12-29 16:17:54
	 */
	@RequestMapping(value={"/savePageTemp.htm"},method=RequestMethod.POST)
	@ResponseBody
	public ReturnData savePageTemp(Model model,Page cmsPageDO,HttpServletRequest request){
		ReturnData returnData = null;
		if(cmsPageDO.getId()!=null && cmsPageDO.getId()>0){
			Integer lid = pageProxy.updateSubmit(cmsPageDO);
			if(lid < 1){
				returnData = new ReturnData(Boolean.FALSE,"页面管理保存报错");
				logger.error("页面管理保存报错");
			}else{
				returnData = new ReturnData(Boolean.TRUE,lid);
			}
		}else{
			Long lid = pageProxy.addSubmit(cmsPageDO);
			if(lid < 1){
				returnData = new ReturnData(Boolean.FALSE,"页面管理保存报错");
				logger.error("页面管理保存报错");
			}else{
				returnData = new ReturnData(Boolean.TRUE,lid);
			}
		}
		return returnData;
	}
}
