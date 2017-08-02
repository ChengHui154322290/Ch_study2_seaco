package com.tp.backend.controller.cms;


import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tp.dto.cms.ReturnData;
import com.tp.model.cms.AnnounceInfo;
import com.tp.proxy.cms.AnnounceProxy;

@Controller
@RequestMapping("/cmsIndex")
public class AnnounTempleController {
	private final static Log logger = LogFactory.getLog(AnnounTempleController.class);
	
	@Autowired
	private AnnounceProxy announceProxy;
	
	
	/**
	 * 首页的广告资讯的模板列表页面
	 * @param consigneeAddressDO
	 * @return 页面
	 * @throws Exception
	 */
	@RequestMapping(value={"/listAnnouncement"},method=RequestMethod.GET)
	public String listAnnouncement(Model model,String announceInfo){
		if(announceInfo != null){
			JSONObject jSONObject1 = new JSONObject();
			Object obj1 = JSONValue.parse(announceInfo);
			jSONObject1 = (JSONObject) obj1;
			model.addAttribute("titleName", jSONObject1.get("titleNameBak"));
			model.addAttribute("status", jSONObject1.get("statusBak"));
			model.addAttribute("type", jSONObject1.get("typeBak"));
		}
		return "cms/index/listAnnouncement";
	}
	
	/**
	 * 首页的广告资讯的模板列表页面
	 * @param consigneeAddressDO
	 * @return 页面
	 * @throws Exception
	 */
	@RequestMapping(value={"/listPostAnnouncement"},method=RequestMethod.POST)
	public String listPostAnnouncement(Model model,Long itemId){
		return "cms/index/listAnnouncement";
	}
	private JSONArray stringToObject(String params){
		Object obj = JSONValue.parse(params);
		JSONArray array = (JSONArray)obj;
		return array;
	}
	/**
	 * 首页的广告资讯的模板创建和修改页面
	 * @param consigneeAddressDO
	 * @return 页面
	 * @throws Exception
	 * @throws NumberFormatException 
	 */
	@RequestMapping(value={"/addAnnouncement.htm"},method=RequestMethod.POST)
	public String addAnnouncement(Model model,String params,String announceInfo) {
		if(params != null){//此处不为null，表示修改模板
			JSONObject jSONObject = new JSONObject();
			JSONArray array = stringToObject(params);
			jSONObject = (JSONObject)array.get(0);
			model.addAttribute("id", jSONObject.get("id"));
			AnnounceInfo sd = new AnnounceInfo();
			try {
				sd = announceProxy.queryAnnounceInfoByID(Long.parseLong(jSONObject.get("id").toString()));
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("公告资讯查询错误出错",e);
			}
			model.addAttribute("title", sd.getTitle());
			model.addAttribute("content", sd.getContent());
			model.addAttribute("status", sd.getStatus());
			model.addAttribute("sort", sd.getSort());
			model.addAttribute("type", sd.getType());
			model.addAttribute("link", sd.getLink());
		}
		
		if(announceInfo != null){
			JSONObject jSONObject1 = new JSONObject();
			Object obj1 = JSONValue.parse(announceInfo);
			jSONObject1 = (JSONObject) obj1;
			model.addAttribute("titleNameBak", jSONObject1.get("titleNameBak"));
			model.addAttribute("statusBak", jSONObject1.get("statusBak"));
			model.addAttribute("typeBak", jSONObject1.get("typeBak"));
		}
		return "cms/index/addAnnouncement";
	}
	
	/**
	 * 广告管理列表页面
	 * @param consigneeAddressDO
	 * @return 页面
	 * @throws Exception
	 */
	@RequestMapping(value={"/listAdvert.htm"},method=RequestMethod.GET)
	public String listAdvert(Model model,Long itemId){
		return "cms/index/listAdvert";
	}
	
	/**
	 * 广告管理新增页面
	 * @param consigneeAddressDO
	 * @return 页面
	 * @throws Exception
	 */
	@RequestMapping(value={"/addAdvert.htm"},method=RequestMethod.GET)
	public String addAdvert(Model model,Long itemId){
		return "cms/index/addAdvert";
	}
	
	/**
	 * 公告资讯的查询
	 * @param announceProxy
	 * @return 主键
	 * @throws Exception
	 * @author huangqinbao 2014-12-29 16:17:54
	 */
	@RequestMapping(value={"/queryAnnounce.htm"},method=RequestMethod.POST)
	@ResponseBody
	public ReturnData queryAnnounce(Model model,HttpServletRequest request,HttpServletResponse response) {
		String params = request.getParameter("params");
		ReturnData returnData = null;
		JSONObject jSONObject = new JSONObject();
		Object obj = JSONValue.parse(params);
		jSONObject = (JSONObject)obj;
    	
		try {
			JSONArray mapList = announceProxy.selectAnnounceInfo(jSONObject);
			returnData = new ReturnData(Boolean.TRUE,mapList);
		} catch (Exception e) {
			returnData = new ReturnData(Boolean.FALSE,null);
			e.printStackTrace();
			logger.error("公告资讯查询错误出错",e);
		}
    	
    	return returnData;
	}

	/**
	 * 公告资讯的删除
	 * @param announceProxy
	 * @return 主键
	 * @throws Exception
	 */
	@RequestMapping(value={"/delAnnounce.htm"},method=RequestMethod.POST)
	@ResponseBody
	public ReturnData delAnnounce(Model model,String params){
		ReturnData returnData = null;
		try {
			List<Long> ids = new ArrayList<Long>();
			 Object obj = JSONValue.parse(params);
			 JSONArray array = (JSONArray)obj;
			 for(int i=0;i<array.size();i++){
				 JSONObject obj2 = (JSONObject)array.get(i);
				 ids.add(Long.valueOf(obj2.get("id").toString()));
			 }
			int counts = announceProxy.delAnnounceByIds(ids);
			returnData = new ReturnData(Boolean.TRUE,counts);
			
		} catch (Exception e) {
			returnData = new ReturnData(Boolean.FALSE,0);
			e.printStackTrace();
			logger.error("公告资讯的删除出错",e);
		}
		return returnData;
	}
	
	/**
	 * 公告资讯的创建和修改的提交按钮事件
	 * @param announceProxy
	 * @return 主键
	 * @throws Exception
	 */
	@RequestMapping(value={"/addAnnounce.htm"},method=RequestMethod.POST)
	@ResponseBody
	public ReturnData addAnnouncete(Model model,String params,HttpServletRequest request){
		JSONObject jSONObject = new JSONObject();
		Object obj = JSONValue.parse(params);
		jSONObject = (JSONObject)obj;
		int counts = 0;
		ReturnData returnData = null;
		try {
			counts = announceProxy.addAnnounceInfo(jSONObject);
			returnData = new ReturnData(Boolean.TRUE,counts);
		} catch (Exception e) {
			returnData = new ReturnData(Boolean.FALSE,"公告资讯的保存出错");
			e.printStackTrace();
			logger.error("公告资讯的保存出错",e);
		}
		if(counts == -1){
			returnData = new ReturnData(Boolean.FALSE,"公告资讯中出现违禁词");
			logger.error("公告资讯的保存出现违禁词");
		}
    	return returnData;
	}

	
}
