package com.tp.backend.controller.cms;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.shiro.SecurityUtils;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.util.WebUtils;

import com.qiniu.QiniuUpload;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.tp.common.vo.Constant;
import com.tp.common.vo.cms.AdvertTypeAPPEnum;
import com.tp.dto.cms.CmsPictureElementDTO;
import com.tp.dto.cms.ReturnData;
import com.tp.exception.ServiceException;
import com.tp.model.cms.PictureElement;
import com.tp.model.usr.UserInfo;
import com.tp.proxy.cms.PictureElementProxy;
import com.tp.util.DateUtil;

@Controller
@Scope(BeanDefinition.SCOPE_SINGLETON)
@RequestMapping("/cmstemplet")
public class TempletPicElementController {
	private final static Log logger = LogFactory.getLog(TempletPicElementController.class);
	
	
	@Autowired
	private PictureElementProxy pictureElementProxy;
	
	@Value("${upload.tmp.path}")
	private String uploadTempPath;
	
	
	@InitBinder  
	protected void initBinder(HttpServletRequest request,  
	            ServletRequestDataBinder binder) throws Exception {   
	      DateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
	      CustomDateEditor dateEditor = new CustomDateEditor(fmt, true);  
	      binder.registerCustomEditor(Date.class, dateEditor);  
	}  
	
	/**
	 * 图片元素的查询
	 * @param 
	 * @return 页面
	 * @throws Exception
	 */
	@RequestMapping(value={"/listPicElement"},method=RequestMethod.GET)
	public String listTempleOpr(HttpServletRequest request, HttpServletResponse response,
			Model model, Long positionId,
			String pageName,String templeName,String positionName){
		List<CmsPictureElementDTO> lst = new ArrayList<CmsPictureElementDTO>();
		try {
			lst = pictureElementProxy.getPictureElement(positionId);
			model.addAttribute("pageName", pageName==null?null : new String(pageName.getBytes("iso8859-1"),"utf-8"));
			model.addAttribute("templeName", templeName==null?null : new String(templeName.getBytes("iso8859-1"),"utf-8"));
			model.addAttribute("positionName", templeName==null?null : new String(templeName.getBytes("iso8859-1"),"utf-8"));
		} catch (Exception e) {
			logger.error("图片元素列表的查询出错",e);
			e.printStackTrace();
		}
		model.addAttribute("detaillist", lst);
		model.addAttribute("positionId", positionId);
		
		model.addAttribute("pageNameBak", request.getParameter("pageNameBak"));
		model.addAttribute("templeNameBak", request.getParameter("templeNameBak"));
		model.addAttribute("positionNameBak", request.getParameter("positionNameBak"));
		model.addAttribute("actTypeList", AdvertTypeAPPEnum.values());
		
		String sessionId = WebUtils.getSessionId(request);
		model.addAttribute("sessionId", sessionId);
		
		return "cms/templet/pictureElementEdit";
	}
	
	
	/**
	 * 图片元素的删除
	 * @param announceAO
	 * @return 主键
	 * @throws Exception
	 */
	@RequestMapping(value={"/delPicElement.htm"},method=RequestMethod.POST)
	@ResponseBody
	public ReturnData delTemplet(Model model,String params){
		ReturnData returnData = null;
		try {
			Object obj = JSONValue.parse(params);
			JSONObject jSONObject = (JSONObject)obj;
			int counts = pictureElementProxy.delById(Long.valueOf(jSONObject.get("id").toString()));
			returnData = new ReturnData(Boolean.TRUE,counts);
			
		} catch (Exception e) {
			returnData = new ReturnData(Boolean.FALSE,0);
			e.printStackTrace();
			logger.error("图片元素的删除出错",e);
		}
		return returnData;
	}
	
	/**
	 * 图片元素提交按钮事件
	 * @param 
	 * @return 主键
	 * @throws Exception
	 */
	@RequestMapping(value={"/savePicElement.htm"},method=RequestMethod.POST)
	@ResponseBody
	public String saveTemplet(Model model,String[] picList, 
			HttpServletRequest request, HttpServletResponse response) {
		String advertname = request.getParameter("name");
		String startdateStr = request.getParameter("startdate");
		String enddateStr = request.getParameter("enddate");
		Integer status = Integer.parseInt(request.getParameter("status"));
		String attr = request.getParameter("attr");
		String actName = request.getParameter("actName");
		String link = request.getParameter("link");
		String picSrc = request.getParameter("picSrc");
		String rollPicSrc = request.getParameter("rollPicSrc");
		String sku = request.getParameter("sku");
		String actType = request.getParameter("actType");
		String activityid = request.getParameter("activityId");
		
		Date startdate = DateUtil.parse(startdateStr, DateUtil.NEW_FORMAT);
		Date enddate = DateUtil.parse(enddateStr, DateUtil.NEW_FORMAT);
		Long counts = 0l; 
		int ct = 0;
		PictureElement cmsPictureElementDO = new PictureElement();
		cmsPictureElementDO.setName(advertname);
		cmsPictureElementDO.setEnddate(enddate);
		cmsPictureElementDO.setStartdate(startdate);
		cmsPictureElementDO.setStatus(status);
		
		if(attr == null){
			cmsPictureElementDO.setAttr("");
		}else{
			cmsPictureElementDO.setAttr(attr);
		}
		
		if(actName == null){
			cmsPictureElementDO.setActname("");
		}else{
			cmsPictureElementDO.setActname(actName);
		}

		if(link == null){
			cmsPictureElementDO.setLink("");
		}else{
			cmsPictureElementDO.setLink(link);
		}
		
		cmsPictureElementDO.setPicSrc(picSrc);
		if(rollPicSrc == null){
			cmsPictureElementDO.setRollpicsrc("");
		}else{
			cmsPictureElementDO.setRollpicsrc(rollPicSrc);
		}
		
		cmsPictureElementDO.setSku(sku);
		cmsPictureElementDO.setActtype(actType);
		if(activityid != null && !"".equals(activityid)){
			cmsPictureElementDO.setActivityid(Long.parseLong(activityid));
		}
		
		Long positionId = Long.parseLong(request.getParameter("positionId"));
		cmsPictureElementDO.setPositionId(positionId);
		
		// 图片上传操作
		if(picList != null && picList.length>0){
			//List<String> picListTmp = uploadPictures(picList, request);
			cmsPictureElementDO.setPicSrc(picList[0]);
		}
		
		//保存或修改操作
		try {
				cmsPictureElementDO.setCreater(1l);
				cmsPictureElementDO.setCreateTime(new Date());
				cmsPictureElementDO.setModifier(1l);
				cmsPictureElementDO.setModifyTime(new Date());
			if(request.getParameter("picId") == null || "".equals(request.getParameter("picId"))){


				counts = pictureElementProxy.addPicEmelent(cmsPictureElementDO);
			}else{
				cmsPictureElementDO.setId(Long.valueOf(request.getParameter("picId")));
				ct = pictureElementProxy.updatePicEmelent(cmsPictureElementDO);
			}
		}catch (Exception e) {
			e.printStackTrace();
			logger.error("图片保存出错",e);
		}
		
		if(counts == 0 && ct == 0){
			return "fail";
		}else{
			return "success";
		}
	}
	
	/**
	 * 保存上传的图片到服务器
	 * 
	 * @param pictures
	 * @return
	 */
	private List<String> uploadPictures(String[] pictures,
			HttpServletRequest request) {
		List<String> fileNames = new ArrayList<String>();
		if(pictures!=null){
			for (String pic : pictures) {
				String realPath = request.getSession().getServletContext().getRealPath(pic);
				try {
					String fileName = uploadFile(new File(realPath), realPath.substring(realPath.lastIndexOf(".") + 1));
					fileNames.add(fileName);
				} catch (QiniuException e) {
					e.printStackTrace();
				}
			}
		}
		
		return fileNames;
	}
	
	/**
	 * 卷帘图片的上传事件，会弹出上传图片弹出框
	 * 
	 * @param request
	 * @param file
	 * @return
	 */
	@RequestMapping(value = "/upload/Image", method = RequestMethod.GET)
	public String uploadImageMode(ModelMap model,
			HttpServletRequest request) {

		model.addAttribute("itemIndex", 1);
		
		String sessionId = WebUtils.getSessionId(request);
		model.addAttribute("sessionId", sessionId);
		
		return "cms/templet/rollUploadPic";
	}
	
	/**
	 * 卷帘图片的上传
	 * 
	 * @param request
	 * @param file
	 * @return
	 */
	@RequestMapping(value = "/uploadImage")
	@ResponseBody
	public String uploadImage(HttpServletRequest request) {

		String jsonValue = "";
		try {
			UserInfo user = (UserInfo)SecurityUtils.getSubject().getPrincipal();
			if(null == user){
				throw new ServiceException("登录用户信息异常");
			}
			String realPath = request.getSession().getServletContext().getRealPath("/");
			String fileName = uploadFile((MultipartHttpServletRequest) request, realPath, user.getUserName());
			JSONObject json = new JSONObject();
			json.put("file", fileName);
			json.put("file_src", Constant.IMAGE_URL_TYPE.cmsimg.url+fileName);
			/*if (!StringUtils.isBlank(fileName)) {
				json.put("fullPath", dfsDomainUtil.getFileFullUrl(fileName));
			}*/
			jsonValue = json.toJSONString();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return jsonValue;
	}
	
	/**
	 * 处理上传文件
	 * 
	 * @param file
	 * @param realPath
	 * @return
	 * @throws Exception
	 */
	private String uploadFile(MultipartHttpServletRequest request, String realPath, String userName) throws Exception {

		String filePath = realPath + uploadTempPath;
		File destFile = new File(filePath);
		if (!destFile.exists()) {
			if (!destFile.mkdirs()) {
				logger.error("图片上传路径配置错误");
				return null;
			}
		}
		MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
		Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();
		// 上传文件名称
		String fileName = null;
		for (Map.Entry<String, MultipartFile> entity : fileMap.entrySet()) {
			MultipartFile mf = entity.getValue();
			fileName = mf.getOriginalFilename();
			if (fileName.lastIndexOf(".") >= 0) {
				fileName = UUID.randomUUID().toString() + "."
						+ fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
			}
			File picFile = new File(filePath, fileName);
			if (!picFile.exists()) {
				picFile.mkdirs();
			}
			try {
				mf.transferTo(picFile);
				fileName = uploadFile(picFile, fileName.substring(fileName.lastIndexOf(".") + 1));
			} catch (IOException e) {
				fileName = null;
				logger.error("文件上传时保存出错！");
			}
		}

		return fileName;
	}
	@Autowired
	private QiniuUpload uploader;
	public String uploadFile(File file, String format) throws QiniuException {
	       String targetName = UUID.randomUUID().toString().replaceAll("-", "");
	       Response response= uploader.uploadFile(file.getAbsolutePath(), targetName+"."+format,Constant.IMAGE_URL_TYPE.cmsimg.name());
	      if(response.isOK()){
	       	return targetName+"."+format;
	      }
	       return  null;
	 }
}
