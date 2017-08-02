package com.tp.backend.controller.cms;


import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tp.dto.cms.ReturnData;
import com.tp.dto.prd.DetailDto;
import com.tp.model.cms.AdvertiseType;
import com.tp.proxy.cms.AdvertiseProxy;

@Controller
@RequestMapping("/cms")
public class CmsBaseTempleController {
	private final static Log logger = LogFactory.getLog(CmsBaseTempleController.class);
	
	@Autowired
	private AdvertiseProxy advertiseProxy;
	
	@Value("${upload.tmp.path}")
	private String uploadTempPath;
	
	/**
	 * 图片类型列表页面
	 * @param 
	 * @return 页面
	 * @throws Exception
	 */
	@RequestMapping(value={"/listAdvertType"},method=RequestMethod.GET)
	public String listAdvertiseTemp(Model model){
		return "cms/base/listAdvertType";
	}
	
	/**
	 * 图片类型的模板添加页面
	 * @param 
	 * @return 页面
	 * @throws Exception
	 * @author huangqinbao 2014-12-29 16:17:54
	 */
	@RequestMapping(value={"/editAdvertType"},method=RequestMethod.POST)
	public String editAdvertiseTemp(Model model,String params) {
		if(params != null){//此处不为null，表示修改模板
			JSONObject jSONObject = new JSONObject();
			Object obj = JSONValue.parse(params);
			jSONObject = (JSONObject)obj;
			Long id = Long.valueOf(jSONObject.get("id").toString());
			//根据id查询
			AdvertiseType cmsAdvertTypeDO = new AdvertiseType();
			try {
				cmsAdvertTypeDO = advertiseProxy.selectAdvertTypeById(id);
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("图片类型模板查询出错",e);
			}
			model.addAttribute("detail", cmsAdvertTypeDO);
		}
		return "cms/base/addAdvertType";
	}
	
	/**
	 * 图片类型的查询
	 * @param 
	 * @return 页面
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value={"/queryAdvertType"},method=RequestMethod.POST)
	@ResponseBody
	public ReturnData queryAdvertiseTempList(Model model,String params) {
		ReturnData returnData = null;
		AdvertiseType cmsAdvertTypeDO = new AdvertiseType();
		
		JSONObject jSONObject = new JSONObject();
		if(params != null){
			Object obj = JSONValue.parse(params);
			jSONObject = (JSONObject)obj;
			if(jSONObject.get("name") != null){
				cmsAdvertTypeDO.setName(jSONObject.get("name").toString());
			}
			if(jSONObject.get("ident") != null){
				cmsAdvertTypeDO.setIdent(String.valueOf(jSONObject.get("ident")));
			}
			if(jSONObject.get("status") != null){
				cmsAdvertTypeDO.setStatus(jSONObject.get("status").toString());
			}
		}
		
    	JSONArray mapList = new JSONArray();
    	
    	List<AdvertiseType> lst = new ArrayList<AdvertiseType>();
		try {
			lst = advertiseProxy.queryAdvertTypeList(cmsAdvertTypeDO);
		} catch (Exception e) {
			returnData = new ReturnData(Boolean.FALSE,null);
			e.printStackTrace();
			logger.error("图片类型查询错误出错",e);
		}
    	
    	for(int i=0;i<lst.size();i++){
    		AdvertiseType obj = lst.get(i);
    		mapList.add(obj);
    	}
    	
    	returnData = new ReturnData(Boolean.TRUE,mapList);
    	return returnData;
	}
	
	/**
	 * 图片类型的保存
	 * @param 
	 * @return 页面
	 * @throws Exception
	 */
	@RequestMapping(value = "saveAdvertType", method = RequestMethod.POST)
	@ResponseBody
	public String save(DetailDto detailDto, String[] picList, 
			HttpServletRequest request, HttpServletResponse response) {
		Object obj = JSONValue.parse(request.getParameter("params"));
		JSONObject jSONObject = (JSONObject)obj;
		String name = jSONObject.get("name").toString();
		String ident = jSONObject.get("ident").toString();
		String status = jSONObject.get("status").toString();
		String id = null;
		if(jSONObject.get("id") != null && !"".equals(jSONObject.get("id"))){
			id = jSONObject.get("id").toString();
		}
		
		int counts = 0;
		AdvertiseType cmsAdvertTypeDO = new AdvertiseType();
		cmsAdvertTypeDO.setName(name);
		if(!StringUtils.isEmpty(ident)){
			cmsAdvertTypeDO.setIdent(ident);
		}
		cmsAdvertTypeDO.setStatus(status);
		
		try {
			if(StringUtils.isEmpty(id)){
				counts = advertiseProxy.addAdvertType(cmsAdvertTypeDO);
			}else{
				cmsAdvertTypeDO.setId(Long.valueOf(id));
				counts = advertiseProxy.updateAdvertType(cmsAdvertTypeDO);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("图片类型保存错误出错",e);
		}
		if(counts == 0){
			return "001";
		}else if(counts == -1){
			return "002";
		}else{
			return "000";
		}
	}
}
