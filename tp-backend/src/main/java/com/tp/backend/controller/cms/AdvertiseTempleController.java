package com.tp.backend.controller.cms;


import java.io.File;
import java.io.IOException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.util.WebUtils;

import com.tp.common.vo.Constant;
import com.tp.dto.cms.ReturnData;
import com.tp.dto.prd.DetailDto;
import com.tp.model.cms.AdvertiseInfo;
import com.tp.proxy.cms.AdvertiseProxy;
import com.tp.util.DateUtil;


@Controller
@RequestMapping("/cmsAdvertIndex")
public class AdvertiseTempleController {
	private final static Log logger = LogFactory.getLog(AdvertiseTempleController.class);
	private final static String ERROR_CODE_UPLOAD = "uploadError";
	
	@Autowired
	private AdvertiseProxy advertiseProxy;
	
	/**
	 * 上传图片地址，是存放在common.properties文件中
	 */
	@Value("${upload.tmp.path}")
	private String uploadTempPath;
	
	/**
	 * 首页的广告的模板列表页面
	 * @param 
	 * @return 页面
	 * @throws Exception
	 */
	@RequestMapping(value={"/listAdvertiseTemp"},method=RequestMethod.GET)
	public String listAdvertiseTemp(Model model,String advertinfo){
		if(advertinfo != null){
			JSONObject jSONObject1 = new JSONObject();
			Object obj1 = JSONValue.parse(advertinfo);
			jSONObject1 = (JSONObject) obj1;
			if(jSONObject1.get("nameBak") != null)
				model.addAttribute("name", URLDecoder.decode(jSONObject1.get("nameBak").toString()));
			model.addAttribute("position", jSONObject1.get("positionBak"));
			model.addAttribute("startdate", jSONObject1.get("startdateBak"));
			model.addAttribute("enddate", jSONObject1.get("enddateBak"));
			model.addAttribute("type", jSONObject1.get("typeBak"));
			model.addAttribute("status", jSONObject1.get("statusBak"));
		}
		return "cms/index/listAdvert";
	}
	
	/**
	 * 首页的广告的模板添加页面
	 * @param 
	 * @return 页面
	 * @throws Exception
	 */
	@RequestMapping(value={"/editAdvertiseTemp"},method=RequestMethod.POST)
	public String editAdvertiseTemp(Model model,String params,String advertinfo,
			HttpServletRequest request){
		if(params != null){//此处不为null，表示修改模板
			JSONObject jSONObject = new JSONObject();
			Object obj = JSONValue.parse(params);
			jSONObject = (JSONObject)obj;
			Long id = Long.valueOf(jSONObject.get("id").toString());
			//根据id查询
			AdvertiseInfo cmsAdvertiseInfoDO = new AdvertiseInfo();
			try {
				cmsAdvertiseInfoDO = advertiseProxy.selectById(id);
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("图片列表查询出错",e);
			}
			if(cmsAdvertiseInfoDO.getStartdate() != null){
				Date startDate = cmsAdvertiseInfoDO.getStartdate();
				String str = DateUtil.formatDateTime(startDate);
				cmsAdvertiseInfoDO.setStartdateStr(str);
			}
			if(cmsAdvertiseInfoDO.getEnddate() != null){
				Date endDate = cmsAdvertiseInfoDO.getEnddate();
				String str = DateUtil.formatDateTime(endDate);
				cmsAdvertiseInfoDO.setEnddateStr(str);
			}
			model.addAttribute("detail", cmsAdvertiseInfoDO);
		}
		
		if(advertinfo != null){
			JSONObject jSONObject1 = new JSONObject();
			Object obj1 = JSONValue.parse(advertinfo);
			jSONObject1 = (JSONObject) obj1;
			model.addAttribute("nameBak", jSONObject1.get("titleName"));
			model.addAttribute("positionBak", jSONObject1.get("position"));
			model.addAttribute("startdateBak", jSONObject1.get("startdate"));
			model.addAttribute("enddateBak", jSONObject1.get("enddate"));
			model.addAttribute("typeBak", jSONObject1.get("type"));
			model.addAttribute("statusBak", jSONObject1.get("status"));
		}
		
		String sessionId = WebUtils.getSessionId(request);
		model.addAttribute("sessionId", sessionId);
		model.addAttribute("bucketURL", Constant.IMAGE_URL_TYPE.cmsimg.url);
        model.addAttribute("bucketname", Constant.IMAGE_URL_TYPE.cmsimg.name());
		return "cms/index/addAdvert";
	}
	
	/**
	 * 广告列表的查询
	 * @param 
	 * @return 页面
	 * @throws Exception
	 */
	@RequestMapping(value={"/queryAdvertiseTempList"},method=RequestMethod.POST)
	@ResponseBody
	public ReturnData queryAdvertiseTempList(Model model,String params) {
		ReturnData returnData = null;
		AdvertiseInfo cmsAdvertiseInfoDO = new AdvertiseInfo();
		
		try {
			JSONArray mapList = advertiseProxy.queryAdvertList(params, cmsAdvertiseInfoDO);
			returnData = new ReturnData(Boolean.TRUE,mapList);
		} catch (Exception e) {
			returnData = new ReturnData(Boolean.FALSE,null);
			e.printStackTrace();
			logger.error("广告列表查询出错",e);
		}
    	return returnData;
	}

	
	/**
	 * 图片上传到本地
	 * @param request
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value="/uploadItemFiles",method=RequestMethod.POST,produces="text/json")
	@ResponseBody
	public String uploadMultiFiles(HttpServletRequest request) {
		String savePath = request.getSession().getServletContext().getRealPath(uploadTempPath);
		if(StringUtils.isBlank(savePath)){
			logger.error("图片上传路径配置错误");
			return null;
		}
		MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest)request;
		Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();
		//上传文件名称
		String fileName = null;
		JSONObject json = new JSONObject();
		for(Map.Entry<String, MultipartFile> entity : fileMap.entrySet()){
			MultipartFile mf = entity.getValue();
			fileName = mf.getOriginalFilename();
			json.put("fileName", mf.getOriginalFilename());
			if (fileName.lastIndexOf(".") >= 0){
				fileName = UUID.randomUUID().toString() + "." + 
						fileName.substring(fileName.lastIndexOf(".")+1).toLowerCase();
			}
			File file = new File(savePath, fileName);
			
			if(!file.exists()){
				file.mkdirs();
			}
			try{
				mf.transferTo(file);
			}catch(IOException e){
				fileName = null;
				logger.error("文件上传时保存出错！");
			}
		}
		
		if(StringUtils.isBlank(fileName)){
			json.put("type", "error");
			json.put("errorCode", ERROR_CODE_UPLOAD);
		}else{
			json.put("path", uploadTempPath+fileName);
			json.put("type", "success");
		}
		return json.toJSONString();
	}
	
	/**
	 * 图片保存
	 * @param detailDto
	 * @param picList
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "saveAdvertiseTemp", method = RequestMethod.POST)
	@ResponseBody
	public String save(DetailDto
			detailDto, String[] picList, 
			HttpServletRequest request, HttpServletResponse response) {
//		String position = request.getParameter("position");
		String advertname = request.getParameter("advertname");
		String startdateStr = request.getParameter("startdate");
		String enddateStr = request.getParameter("enddate");
		String status = request.getParameter("status");
		String id = request.getParameter("idd");
		String path = request.getParameter("path");
		String type = request.getParameter("type");
		String sort = request.getParameter("sort");
		String platformType = request.getParameter("platformType");
		
		Date startdate = null;
		Date enddate = null;
		try {
			startdate = DateUtils.parseDate(startdateStr, new String[] { "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd" });
			enddate = DateUtils.parseDate(enddateStr, new String[] { "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd" });
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		int counts = 0;
		AdvertiseInfo cmsAdvertiseInfoDO = new AdvertiseInfo();
		cmsAdvertiseInfoDO.setAdvertname(advertname);
		cmsAdvertiseInfoDO.setEnddate(enddate);
		cmsAdvertiseInfoDO.setPath(path);
		cmsAdvertiseInfoDO.setStartdate(startdate);
		cmsAdvertiseInfoDO.setStatus(status);
		cmsAdvertiseInfoDO.setType(type);
		if(!StringUtils.isEmpty(sort)){
			cmsAdvertiseInfoDO.setSort(Integer.parseInt(sort));
		}
		cmsAdvertiseInfoDO.setPlatformType(Integer.valueOf(platformType));
		
		String activityid = request.getParameter("activityid");
		String time = request.getParameter("time");
		if(!StringUtils.isEmpty(activityid)){
			cmsAdvertiseInfoDO.setActivityid(Long.parseLong(activityid));
		}else{
			cmsAdvertiseInfoDO.setActivityid(null);
		}
		
		if(!StringUtils.isEmpty(time)){
			cmsAdvertiseInfoDO.setTime(Integer.parseInt(time));
		}else{
			cmsAdvertiseInfoDO.setTime(null);
		}
		
		if(request.getParameter("actType") != null){
			cmsAdvertiseInfoDO.setActtype(request.getParameter("actType").toString());
		}else{
			cmsAdvertiseInfoDO.setActtype("");
		}
		
		if(request.getParameter("sku") != null){
			cmsAdvertiseInfoDO.setSku(request.getParameter("sku").toString());
		}else{
			cmsAdvertiseInfoDO.setSku("");
		}
		
		if(request.getParameter("link") != null){
			cmsAdvertiseInfoDO.setLink(request.getParameter("link").toString());
		}else{
			cmsAdvertiseInfoDO.setLink("");
		}
		
		if(request.getParameter("ident") != null){
			cmsAdvertiseInfoDO.setIdent(request.getParameter("ident"));
		}
		
		// 图片上传操作
		if(picList != null){
			cmsAdvertiseInfoDO.setPath(picList[0]);
		}
		
		//保存或修改操作
		try {
			if(StringUtils.isEmpty(id)){
				advertiseProxy.addAdvertiseByIds(cmsAdvertiseInfoDO);
			}else{
				cmsAdvertiseInfoDO.setId(Long.valueOf(id));
				counts = advertiseProxy.updateAdvertiseByIds(cmsAdvertiseInfoDO);
			}
			return "success";
		}catch (Exception e) {
			e.printStackTrace();
			logger.error("图片保存出错",e);
			return e.getMessage();
		}
	}
	
	/**
	 * 启用
	 * @param announceAO
	 * @return 主键
	 * @throws Exception
	 * @author huangqinbao 2014-12-29 16:17:54
	 */
	@RequestMapping(value={"/openAdertise.htm"},method=RequestMethod.POST)
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
			counts = advertiseProxy.openAdvert(ids);
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
	@RequestMapping(value={"/noOpenAdertise.htm"},method=RequestMethod.POST)
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
			counts = advertiseProxy.noOpenAdvert(ids);
			returnData = new ReturnData(Boolean.TRUE,counts);
		} catch (Exception e) {
			returnData = new ReturnData(Boolean.FALSE,counts);
			e.printStackTrace();
			logger.error("禁用出错",e);
		}
    	
    	return returnData;
	}
}
