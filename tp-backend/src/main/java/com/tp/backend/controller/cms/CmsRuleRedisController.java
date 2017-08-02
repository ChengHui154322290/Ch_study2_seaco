package com.tp.backend.controller.cms;


import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

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
import com.tp.model.cms.RedisIndexRule;
import com.tp.proxy.cms.RuleRedisProxy;

@Controller
@RequestMapping("/cmsRuleRedis")
public class CmsRuleRedisController {
	private final static Log logger = LogFactory.getLog(CmsRuleRedisController.class);
	
	@Autowired
	RuleRedisProxy ruleRedisProxy;
	
	/**
	 * 缓存规则的模板列表页面
	 * @param 
	 * @return 页面
	 * @throws Exception
	 * @author huangqinbao 2014-12-29 16:17:54
	 */
	@RequestMapping(value={"/listRuleRedisTemp"},method=RequestMethod.GET)
	public String listAdvertiseTemp(Model model,String ruleRedisinfo){
		if(ruleRedisinfo != null){
			JSONObject jSONObject = new JSONObject();
			Object obj = JSONValue.parse(ruleRedisinfo);
			jSONObject = (JSONObject) obj;
			
			model.addAttribute("functionName", jSONObject.get("functionNameBak"));
			model.addAttribute("area", jSONObject.get("areaBak"));
			model.addAttribute("platformType", jSONObject.get("platformTypeBak"));
			model.addAttribute("status", jSONObject.get("statusBak"));
		}
		return "cms/index/listRuleRedis";
	}
	
	
	/**
	 * 缓存规则的查询
	 * @param 
	 * @return 页面
	 * @throws Exception
	 * @author huangqinbao 2014-12-29 16:17:54
	 */
	@RequestMapping(value={"/queryRuleRedisTempList"},method=RequestMethod.POST)
	@ResponseBody
	public ReturnData queryAdvertiseTempList(Model model,String params) {
		ReturnData returnData = null;
		RedisIndexRule cmsRedisIndexRuleDO = new RedisIndexRule();
		
		try {
			JSONArray mapList = ruleRedisProxy.queryRuleRedisList(params, cmsRedisIndexRuleDO);
			returnData = new ReturnData(Boolean.TRUE,mapList);
		} catch (Exception e) {
			returnData = new ReturnData(Boolean.FALSE,null);
			e.printStackTrace();
			logger.error("缓存规则的查询出错",e);
		}
    	return returnData;
	}

	
	/**
	 * 启用
	 * @param announceAO
	 * @return 主键
	 * @throws Exception
	 * @author huangqinbao 2014-12-29 16:17:54
	 */
	@RequestMapping(value={"/openRuleRedis.htm"},method=RequestMethod.POST)
	@ResponseBody
	public ReturnData openAdertise(Model model,String params) {
		ReturnData returnData = null;
		
		List<Long> ids = new ArrayList<Long>();
		 Object obj = JSONValue.parse(params);
	     JSONArray array = (JSONArray)obj;
	     for(int i=0;i<array.size();i++){
	    	 JSONObject obj2 = (JSONObject)array.get(i);
	    	 ids.add(Long.valueOf(obj2.get("id").toString()));
	     }
    	int counts = 0;
		try {
			counts = ruleRedisProxy.openRuleRedis(ids);
			returnData = new ReturnData(Boolean.TRUE,counts);
		} catch (Exception e) {
			returnData = new ReturnData(Boolean.FALSE,counts);
			e.printStackTrace();
			logger.error("启用报错",e);
		}
    	
    	return returnData;
	}
	
	/**
	 * 禁用
	 * @param announceAO
	 * @return 主键
	 * @throws Exception
	 * @author huangqinbao 2014-12-29 16:17:54
	 */
	@RequestMapping(value={"/noOpenRuleRedis.htm"},method=RequestMethod.POST)
	@ResponseBody
	public ReturnData noOpenAdertise(Model model,String params) {
		ReturnData returnData = null;
		List<Long> ids = new ArrayList<Long>();
		 Object obj = JSONValue.parse(params);
	     JSONArray array = (JSONArray)obj;
	     for(int i=0;i<array.size();i++){
	    	 JSONObject obj2 = (JSONObject)array.get(i);
	    	 ids.add(Long.valueOf(obj2.get("id").toString()));
	     }
    	int counts = 0;
		try {
			counts = ruleRedisProxy.noOpenRuleRedis(ids);
			returnData = new ReturnData(Boolean.TRUE,counts);
		} catch (Exception e) {
			returnData = new ReturnData(Boolean.FALSE,counts);
			e.printStackTrace();
			logger.error("禁用出错",e);
		}
    	
    	return returnData;
	}
	
	/**
	 * 缓存规则添加模板
	 * @param 
	 * @return 页面
	 * @throws Exception
	 * @author huangqinbao 2014-12-29 16:17:54
	 */
	@RequestMapping(value={"/editRuleRedisTemp"},method=RequestMethod.POST)
	public String editAdvertiseTemp(Model model,String params,String ruleRedisinfo,
			HttpServletRequest request){
		if(params != null){
			JSONObject jSONObject = new JSONObject();
			Object obj = JSONValue.parse(params);
			jSONObject = (JSONObject)obj;
			Long id = Long.valueOf(jSONObject.get("id").toString());
			//根据id查询
			RedisIndexRule cmsRedisIndexRuleDO = new RedisIndexRule();
			try {
				cmsRedisIndexRuleDO = ruleRedisProxy.selectById(id);
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("缓存规则查询出错",e);
			}
			model.addAttribute("detail", cmsRedisIndexRuleDO);
		}
		
		if(ruleRedisinfo != null){
			JSONObject jSONObject1 = new JSONObject();
			Object obj1 = JSONValue.parse(ruleRedisinfo);
			jSONObject1 = (JSONObject) obj1;
			model.addAttribute("functionNameBak", jSONObject1.get("functionName"));
			model.addAttribute("areaBak", jSONObject1.get("area"));
			model.addAttribute("platformTypeBak", jSONObject1.get("platformType"));
			model.addAttribute("statusBak", jSONObject1.get("status"));
		}
		
		/*String sessionId = WebUtils.getSessionId(request);
		model.addAttribute("sessionId", sessionId);*/
		
		return "cms/index/addRuleRedis";
	}
}
