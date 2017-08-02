package com.tp.backend.controller.cms;


import java.util.ArrayList;
import java.util.List;

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
import com.tp.model.cms.IndexFeedback;
import com.tp.proxy.cms.IndexFeedbackProxy;


@Controller
@RequestMapping("/cms")
public class FeedbackInfoController {
	private final static Log logger = LogFactory.getLog(FeedbackInfoController.class);
	
	@Autowired
	private IndexFeedbackProxy indexFeedbackProxy;
	
	/**
	 * 反馈信息的列表页面
	 * @param 
	 * @return 页面
	 * @throws Exception
	 * @author huangqinbao 2014-12-29 16:17:54
	 */
	@RequestMapping(value={"/listFeedback"},method=RequestMethod.GET)
	public String listAdvertiseTemp(Model model,String feedbackInfo){
		if(feedbackInfo != null){
			JSONObject jSONObject1 = new JSONObject();
			Object obj1 = JSONValue.parse(feedbackInfo);
			jSONObject1 = (JSONObject) obj1;
			
			model.addAttribute("userId", jSONObject1.get("userIdBak"));
			model.addAttribute("userName", jSONObject1.get("userNameBak"));
		}
		return "cms/index/listIndexFeedback";
	}
	
	/**
	 * 反馈信息的查询
	 * @param 
	 * @return 页面
	 * @throws Exception
	 * @author huangqinbao 2014-12-29 16:17:54
	 */
	@RequestMapping(value={"/queryFeedbackList"},method=RequestMethod.POST)
	@ResponseBody
	public ReturnData queryAdvertiseTempList(Model model,String params) {
		ReturnData returnData = null;
		IndexFeedback cmsIndexFeedbackDO = new IndexFeedback();
		
		try {
			JSONArray mapList = indexFeedbackProxy.queryFeedbackList(params, cmsIndexFeedbackDO);
			returnData = new ReturnData(Boolean.TRUE,mapList);
		} catch (Exception e) {
			returnData = new ReturnData(Boolean.FALSE,null);
			e.printStackTrace();
			logger.error("反馈信息查询出错",e);
		}
    	return returnData;
	}
	
	/**
	 * 反馈信息的查看页面
	 * @param 
	 * @return 页面
	 * @throws Exception
	 * @author huangqinbao 2014-12-29 16:17:54
	 * @throws NumberFormatException 
	 */
	@RequestMapping(value={"/viewFeedback.htm"},method=RequestMethod.POST)
	public String viewFeedback(Model model,String params,String feedbackInfo) {
		if(params != null){//此处不为null，表示修改模板
			JSONObject jSONObject = new JSONObject();
			jSONObject = (JSONObject) JSONValue.parse(params);
			model.addAttribute("id", jSONObject.get("id"));
			IndexFeedback sd = new IndexFeedback();
			try {
				sd = indexFeedbackProxy.queryFeedbackByID(Long.parseLong(jSONObject.get("id").toString()));
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("反馈信息查询错误出错",e);
			}
			model.addAttribute("obj", sd);
		}
		
		if(feedbackInfo != null){
			JSONObject jSONObject1 = new JSONObject();
			Object obj1 = JSONValue.parse(feedbackInfo);
			jSONObject1 = (JSONObject) obj1;
			model.addAttribute("userIdBak", jSONObject1.get("userIdBak"));
			model.addAttribute("userNameBak", jSONObject1.get("userNameBak"));
		}
		return "cms/index/viewIndexFeedback";
	}
	
	/**
	 * 反馈信息的删除
	 * @param 
	 * @return 主键
	 * @throws Exception
	 * @author huangqinbao 2014-12-29 16:17:54
	 */
	@RequestMapping(value={"/delFeedback.htm"},method=RequestMethod.POST)
	@ResponseBody
	public ReturnData delFeedback(Model model,String params){
		ReturnData returnData = null;
		try {
			List<Long> ids = new ArrayList<Long>();
			 Object obj = JSONValue.parse(params);
			 JSONArray array = (JSONArray)obj;
			 for(int i=0;i<array.size();i++){
				 JSONObject obj2 = (JSONObject)array.get(i);
				 ids.add(Long.valueOf(obj2.get("id").toString()));
			 }
			int counts = indexFeedbackProxy.delFeedbackByIds(ids);
			returnData = new ReturnData(Boolean.TRUE,counts);
			
		} catch (Exception e) {
			returnData = new ReturnData(Boolean.FALSE,0);
			e.printStackTrace();
			logger.error("反馈信息的删除出错",e);
		}
		return returnData;
	}

}
