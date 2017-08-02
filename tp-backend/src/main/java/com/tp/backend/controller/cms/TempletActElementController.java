package com.tp.backend.controller.cms;

import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.util.WebUtils;

import com.tp.dto.cms.CmsActivityElementDTO;
import com.tp.dto.cms.ReturnData;
import com.tp.model.cms.ActivityElement;
import com.tp.proxy.DfsProxy;
import com.tp.proxy.cms.ActivityElementProxy;
import com.tp.query.mmp.CmsTopicQuery;
import com.tp.util.DateUtil;

@Controller
@Scope(BeanDefinition.SCOPE_SINGLETON)
@RequestMapping("/cmstemplet")
public class TempletActElementController {
	private final static Log logger = LogFactory.getLog(TempletActElementController.class);
	
	@Autowired
	private ActivityElementProxy activityElementProxy;
	
	@Autowired
	private DfsProxy dfsProxy;
	
	/**
	 * 上传图片地址，是存放在common.properties文件中
	 */
	@Value("${upload.tmp.path}")
	private String uploadTempPath;
	
    
    /**文件上传者的信息*/
    public static final String UPLOAD_CREATOR="item_mode";
    
    
    /**
	 * 活动元素列表的查询
	 * @param 
	 * @return 页面
	 * @throws Exception
	 */
	@RequestMapping(value={"/listActElement"},method=RequestMethod.GET)
	public String listActElement(HttpServletRequest request, 
			Model model, Long positionId,
			String pageName,String templeName,String positionName){
		List<CmsActivityElementDTO> lst = new ArrayList<CmsActivityElementDTO>();
		try {
			lst = activityElementProxy.addActivityElement(positionId);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("活动元素添加页面查询出错",e);
		}
		
		String sessionId = WebUtils.getSessionId(request);
		model.addAttribute("sessionId", sessionId);
		
		model.addAttribute("detaillist", lst);
		model.addAttribute("positionId", positionId);
		try {
			model.addAttribute("pageName", pageName != null ?new String(pageName.getBytes("iso8859-1"),"utf-8") : "");
			model.addAttribute("templeName", templeName != null ?new String(templeName.getBytes("iso8859-1"),"utf-8") : "");
			model.addAttribute("positionName", positionName != null ?new String(positionName.getBytes("iso8859-1"),"utf-8") : "");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
		model.addAttribute("pageNameBak", request.getParameter("pageNameBak"));
		model.addAttribute("templeNameBak", request.getParameter("templeNameBak"));
		model.addAttribute("positionNameBak", request.getParameter("positionNameBak"));
		
		return "cms/templet/activityElementEdit";
	}

	/**
	 * 删除活动id，即节点下面的活动id
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "delActivityByID", method = RequestMethod.POST)
	@ResponseBody
	public ReturnData delActivityByID(Model model,String params) {
		 Object obj = JSONValue.parse(params);
		 JSONObject jSONObject = (JSONObject)obj;
		 /*ActivityElement cmsActivityElementDO = new ActivityElement();
		 cmsActivityElementDO.setId(Long.valueOf(jSONObject.get("id").toString()));*/
	   	int counts = 0;
	   	ReturnData returnData = null;
		try {
			//counts = activityElementProxy.delActivityByID(cmsActivityElementDO);
			counts = activityElementProxy.delActivityByID(Long.valueOf(jSONObject.get("id").toString()));
			returnData = new ReturnData(Boolean.TRUE,counts);
		} catch (Exception e) {
			returnData = new ReturnData(Boolean.FALSE,counts);
			e.printStackTrace();
			logger.error("删除活动id出错",e);
		}
	   	return returnData;
	}
	
	@RequestMapping(value={"/addActivityByID.htm"},method=RequestMethod.POST)
	@ResponseBody
	public ReturnData saveTemplet(Model model,String[] picList, 
			HttpServletRequest request, HttpServletResponse response) {
		ActivityElement cmsActivityElementDO = new ActivityElement();
		String startdatebak = request.getParameter("startdatebak");
		String enddatebak = request.getParameter("enddatebak");
		String statusbak = request.getParameter("statusbak");
		String linkbak = request.getParameter("linkbak");
		String seqbak = request.getParameter("seqbak");
		String picture = request.getParameter("picture");
		String id = request.getParameter("layer_Id");
		String activityId = request.getParameter("activity_layer_Id");
		String positionId = request.getParameter("position_layer_Id");
		 
		if(!StringUtils.isEmpty(positionId)){
			cmsActivityElementDO.setPositionId(Long.valueOf(positionId));
		}
		
		if(!StringUtils.isEmpty(picture)){
			 cmsActivityElementDO.setPicture(picture);
		 }else{
			 cmsActivityElementDO.setPicture("");
		 }
		
		long counts = 0;
		try {
			// 图片上传操作
			if(picList != null){
				List<String> picListTmp = uploadPictures(picList, request);
				cmsActivityElementDO.setPicture(picListTmp.get(0));
				logger.error("上传图片的路径地址："+picListTmp.get(0));
			}
			
			//启用时间
			if(!StringUtils.isEmpty(startdatebak)){
				cmsActivityElementDO.setStartdate(DateUtil.parse(startdatebak, DateUtil.NEW_FORMAT));
			 }
			//失效时间
			 if(!StringUtils.isEmpty(enddatebak)){
				 cmsActivityElementDO.setEnddate(DateUtil.parse(enddatebak, DateUtil.NEW_FORMAT));
			 }
			 
			 if(!StringUtils.isEmpty(linkbak)){
				 cmsActivityElementDO.setLink(linkbak);
			 }else{
				 cmsActivityElementDO.setLink("");
			 }
			 if(!StringUtils.isEmpty(seqbak)){
				 cmsActivityElementDO.setSeq(Integer.valueOf(seqbak));
			 }else{
				 cmsActivityElementDO.setSeq(1);
			 }
			 
			 if(!StringUtils.isEmpty(statusbak)){
				 cmsActivityElementDO.setStatus(Integer.parseInt(statusbak));
			 }
			 
			 if(!StringUtils.isEmpty(id)){
				 cmsActivityElementDO.setModifyTime(new Date());
				 
				 cmsActivityElementDO.setId(Long.valueOf(id));
				 counts = activityElementProxy.updateActivity(cmsActivityElementDO);
			 }else{
				 cmsActivityElementDO.setCreater(1l);
				 cmsActivityElementDO.setCreateTime(new Date());
				 cmsActivityElementDO.setModifier(1l);
				 cmsActivityElementDO.setModifyTime(new Date());
				 
				 cmsActivityElementDO.setActivityId(Long.parseLong(activityId));
				 counts = activityElementProxy.insertActivityId(cmsActivityElementDO);
			 }
			 
			 
		} catch (ParseException e) {
			counts = -1;
			e.printStackTrace();
			logger.error("日期格式化出错",e);
		} catch (Exception e) {
			counts = -1;
			e.printStackTrace();
			logger.error("模板节点的保存出错",e);
		}
	   	
	   	ReturnData returnData = null;
	   	if(counts>0){
	   		returnData = new ReturnData(Boolean.TRUE,counts);
	   	}else if(counts==-1){
	   		returnData = new ReturnData(Boolean.FALSE,"模板节点保存出错，请联系管理员");
	   	}else{
	   		returnData = new ReturnData(Boolean.FALSE,"该活动已经存在在模板中，不能重复添加");
	   	}
	   	return returnData;
	   	
	}
	
	/**
	 * 查询促销那边数据
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "getCmsTopicInfoLixt", method = RequestMethod.POST)
	@ResponseBody
	public ReturnData getCmsTopicInfoLixt(Model model,String params) {
		CmsTopicQuery query = new CmsTopicQuery();
		ReturnData returnData = null;
		
		Object obj = JSONValue.parse(params);
		JSONObject jSONObject = (JSONObject)obj;
		
		try {
			JSONArray mapList = activityElementProxy.queryTopicLst(query, jSONObject);
			returnData = new ReturnData(Boolean.TRUE,mapList);
		} catch (Exception e) {
			returnData = new ReturnData(Boolean.FALSE,null);
			e.printStackTrace();
			logger.error("查询促销数据的保存出错",e);
		}
		
	   	return returnData;
	}

	
	/**
     * 生成文件名称
     * uuid
     * 
     * @return
     */
    public static String generateFileName(){
        return UUID.randomUUID().toString();
    }

	
	/**
	 * 保存上传的图片到服务器
	 * 
	 * @param pictures
	 * @return
	 */
	private List<String> uploadPictures(String[] pictures,
			HttpServletRequest request) {
		List<String> picRealPath = new ArrayList<String>();
		if(pictures!=null){
			for (String pic : pictures) {
				String realPath = request.getSession().getServletContext()
						.getRealPath(pic);
				picRealPath.add(realPath);
			}
		}
		return dfsProxy.uploadPictures(picRealPath);
	}
	
	/**
	 * 图片的上传事件，会弹出上传图片弹出框
	 * @param request
	 * @param file
	 * @return
	 */
	@RequestMapping(value = "/upload_act/Image", method = RequestMethod.GET)
	public String uploadImage(ModelMap model,
			HttpServletRequest request) {

		model.addAttribute("itemIndex", 1);
		
		String sessionId = WebUtils.getSessionId(request);
		model.addAttribute("sessionId", sessionId);
		
		return "cms/templet/actElementUploadPic";
	}
}
