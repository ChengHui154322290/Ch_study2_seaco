package com.tp.backend.controller.cms;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.qiniu.QiniuUpload;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.tp.common.vo.Constant;
import com.tp.common.vo.supplier.CommonUtil;
import com.tp.dto.cms.CmsSingleTempleDTO;
import com.tp.dto.cms.CmsSingleTepactivityDTO;
import com.tp.dto.cms.ReturnData;
import com.tp.model.cms.ImportLog;
import com.tp.proxy.cms.SingleTempleProxy;
import com.tp.query.mmp.CmsTopicQuery;
import com.tp.util.DateUtil;

@Controller
@RequestMapping("/cmsSingleTemple")
public class SingleTempleController {
	private final static Log logger = LogFactory.getLog(SingleTempleController.class);
	
	@Autowired
	private SingleTempleProxy singleTempleProxy;
	
	/**
	 * 上传图片地址，是存放在common.properties文件中
	 */
	@Value("${upload.tmp.path}")
	private String uploadTempPath;
	
	/** 文件服务器上的唯一的文件名*/
    private String uniqueFileName="";
    
    /** 密钥 */
    private String secretKey = ""; 
    
    /**文件上传者的信息*/
    public static final String UPLOAD_CREATOR="item_mode";
    
    /** 上传模板文件名 */
	private String realFileName = "";
    
	
	/**
	 * 首页的单品团的模板列表页面
	 * @param 
	 * @return 页面
	 * @throws Exception
	 * @author huangqinbao 2014-12-29 16:17:54
	 */
	@RequestMapping(value={"/listSingletemple"},method=RequestMethod.GET)
	public String listSingletemple(Model model,String singleTempInfo){
		logger.debug("进入单品团模板列表");
		if(singleTempInfo != null){
			JSONObject jSONObject1 = new JSONObject();
			Object obj1 = JSONValue.parse(singleTempInfo);
			jSONObject1 = (JSONObject) obj1;
			
			model.addAttribute("templeName", jSONObject1.get("templeNameBak"));
			model.addAttribute("positionName", jSONObject1.get("positionNameBak"));
			model.addAttribute("status", jSONObject1.get("statusBak"));
			model.addAttribute("platformType", jSONObject1.get("platformTypeBak"));
			model.addAttribute("type", jSONObject1.get("typeBak"));
		}
		return "cms/index/listSingletemple";
	}
	
	/**
	 * 单品团模板查询
	 * @param singleTempleProxy
	 * @return 主键
	 * @throws Exception
	 * @author huangqinbao 2014-12-29 16:17:54
	 */
	@RequestMapping(value={"/querySingleTempleList.htm"},method=RequestMethod.POST)
	@ResponseBody
	public ReturnData querySingleTempleList(Model model,HttpServletRequest request,HttpServletResponse response) {
		String params = request.getParameter("params");
		ReturnData returnData = null;
		JSONObject jSONObject = new JSONObject();
		Object obj = JSONValue.parse(params);
		jSONObject = (JSONObject)obj;
		try {
			JSONArray mapList = singleTempleProxy.querysingleTempleLst(jSONObject);
			returnData = new ReturnData(Boolean.TRUE,mapList);
		} catch (Exception e) {
			returnData = new ReturnData(Boolean.FALSE,null);
			e.printStackTrace();
			logger.error("单品团模板查询出错",e);
		}
    	
    	return returnData;
	}

	
	/**
	 * 单品团模板的删除
	 * @param announceAO
	 * @return 主键
	 * @throws Exception
	 * @author huangqinbao 2014-12-29 16:17:54
	 */
	@RequestMapping(value={"/delSingleTemple.htm"},method=RequestMethod.POST)
	@ResponseBody
	public ReturnData delSingleTemple(Model model,String params) {
		List<Long> ids = new ArrayList<Long>();
		 Object obj = JSONValue.parse(params);
	     JSONArray array = (JSONArray)obj;
	     for(int i=0;i<array.size();i++){
	    	 JSONObject obj2 = (JSONObject)array.get(i);
	    	 ids.add(Long.valueOf(obj2.get("id").toString()));
	     }
	     int counts = 0;
		try {
			counts = singleTempleProxy.delsingleTempleByIds(ids);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("单品团模板删除出错",e);
		}
    	ReturnData returnData = new ReturnData(Boolean.TRUE,counts);
    	return returnData;
	}
	
	/**
	 * 首页的单品团创建页面
	 * @param 
	 * @return 页面
	 * @throws Exception
	 * @author huangqinbao 2014-12-29 16:17:54
	 */
	@RequestMapping(value={"/addSingletemple"},method=RequestMethod.POST)
	public String addSingletemple(Model model,String singleTempInfo){
		if(singleTempInfo != null){
			JSONObject jSONObject1 = new JSONObject();
			Object obj1 = JSONValue.parse(singleTempInfo);
			jSONObject1 = (JSONObject) obj1;
			
			model.addAttribute("templeNameBak", jSONObject1.get("templeName"));
			model.addAttribute("positionNameBak", jSONObject1.get("positionName"));
			model.addAttribute("statusBak", jSONObject1.get("status"));
			model.addAttribute("platformTypeBak", jSONObject1.get("platformType"));
			model.addAttribute("typeBak", jSONObject1.get("type"));
		}
		return "cms/index/addSingletemple";
	}
	
	/**
	 * 首页的单品团修改模板页面
	 * @param 
	 * @return 页面
	 * @throws Exception
	 * @author huangqinbao 2014-12-29 16:17:54
	 */
	@RequestMapping(value={"/updateSingletemple"},method=RequestMethod.POST)
	public String updateSingletemple(Model model,String params,String singleTempInfo) {
		Object obj = JSONValue.parse(params);//主表
		JSONArray jSONObject = (JSONArray)obj;
		Long id = Long.valueOf(((JSONObject)jSONObject.get(0)).get("id").toString());
		//通过id查询出所有的信息
		CmsSingleTempleDTO cmsSingleTempleDTO = new CmsSingleTempleDTO();
		cmsSingleTempleDTO.setDr(0);
		cmsSingleTempleDTO.setId(id);
    	List<CmsSingleTempleDTO> list = new ArrayList<CmsSingleTempleDTO>();
		try {
			cmsSingleTempleDTO.setPageSize(10000);//此处设置不分页，所以页大小设最大，即不分页
			list = singleTempleProxy.querysingleTempleList(cmsSingleTempleDTO);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("单品团模板查询出错",e);
		}
    	model.addAttribute("detaillist", list);
    	model.addAttribute("templeName", list.get(0).getTempleName());
    	model.addAttribute("templePath", list.get(0).getPath());
    	if("启用".equals(list.get(0).getStatus())){
    		model.addAttribute("status", "0");
    	}else{
    		model.addAttribute("status", "1");
    	}
    	model.addAttribute("type", list.get(0).getType());
    	model.addAttribute("platformType", list.get(0).getPlatformType());
    	model.addAttribute("id", id);
    	
    	if(singleTempInfo != null){
			JSONObject jSONObject1 = new JSONObject();
			Object obj1 = JSONValue.parse(singleTempInfo);
			jSONObject1 = (JSONObject) obj1;
			
			model.addAttribute("templeNameBak", jSONObject1.get("templeName"));
			model.addAttribute("positionNameBak", jSONObject1.get("positionName"));
			model.addAttribute("statusBak", jSONObject1.get("status"));
			model.addAttribute("platformTypeBak", jSONObject1.get("platformType"));
			model.addAttribute("typeBak", jSONObject1.get("type"));
		}
    	
		return "cms/index/addSingletempleList";
	}
	
	/**
	 * 首页的单品团添加活动的页面
	 * @param 
	 * @return 页面
	 * @throws Exception
	 */
	@RequestMapping(value={"/addSingleActivity"},method=RequestMethod.POST)
	public String addSingleActivity(Model model,String params,String singleTempInfo) {
		Object obj = JSONValue.parse(params);//主表
		JSONObject jSONObject = (JSONObject)obj;
		Long id = Long.valueOf(jSONObject.get("id").toString());
		String templeName = jSONObject.get("templeName").toString();
		String positionName = jSONObject.get("positionName").toString();
		String positionSize = jSONObject.get("positionSize").toString();
		String positionSort = jSONObject.get("positionSort").toString();
		model.addAttribute("templeName", templeName);
		model.addAttribute("positionName", positionName);
		model.addAttribute("positionSize", positionSize);
		model.addAttribute("positionSort", positionSort);
		
		List<CmsSingleTepactivityDTO> singleTepactivityDOList = new ArrayList<CmsSingleTepactivityDTO>();
		try {
			singleTepactivityDOList = singleTempleProxy.addSingleNode(id);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("单品团添加活动页面查询出错",e);
		}
		
		model.addAttribute("detaillist", singleTepactivityDOList);
		model.addAttribute("tempnodeid", id);
		
		if(singleTempInfo != null){
			JSONObject jSONObject1 = new JSONObject();
			Object obj1 = JSONValue.parse(singleTempInfo);
			jSONObject1 = (JSONObject) obj1;
			model.addAttribute("templeNameBak", jSONObject1.get("templeName"));
			model.addAttribute("positionNameBak", jSONObject1.get("positionName"));
			model.addAttribute("statusBak", jSONObject1.get("status"));
			model.addAttribute("platformTypeBak", jSONObject1.get("platformType"));
			model.addAttribute("typeBak", jSONObject1.get("type"));
		}
		
		return "cms/index/addSingletempleActity";
	}

	/**
	 * 首页的单品团模板提交
	 * @param 
	 * @return 页面
	 * @throws Exception
	 * @author huangqinbao 2014-12-29 16:17:54
	 */
	@RequestMapping(value = "saveSingleTemp/{params}", method = RequestMethod.POST)
	@ResponseBody
	public String save(@RequestParam(value="fieName",required=false)String fieName,
			@PathVariable String params,
			HttpServletRequest request,Model model) {
		int counts = 0;
		
		CmsSingleTempleDTO cmsSingleTempleDTO = new CmsSingleTempleDTO();
		String templeNameId = request.getParameter("idd");
		String templeName = request.getParameter("templeName");
		String templePath = request.getParameter("templePath");
		String status = request.getParameter("status");
		String platformType = request.getParameter("platformType");
		String type = request.getParameter("type");
		if(templeNameId != null && !"".equals(templeNameId)){
			cmsSingleTempleDTO.setId(Long.valueOf(templeNameId));
		}
		cmsSingleTempleDTO.setTempleName(templeName);
		cmsSingleTempleDTO.setPath(templePath);
		if(status != null){
			cmsSingleTempleDTO.setStatus(status);
		}else{
			cmsSingleTempleDTO.setStatus("0");
		}
		cmsSingleTempleDTO.setPlatformType(platformType);
		cmsSingleTempleDTO.setType(type);
		
		try {
			/**
			 * 图片上传，并返回文件名
			 * 暂时去除文件上传功能
			 */
			File retFile = null;
			if(request instanceof MultipartHttpServletRequest){
			    MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest)request;
			    MultipartFile multipartFile = multipartRequest.getFile(fieName); 
			    if(null == multipartFile || multipartFile.isEmpty()){
			    	logger.info("找不到文件："+fieName);
			    	return"0";
			    }
			      long fileSize = multipartFile.getSize();
			      //上传的文件名
			      realFileName = multipartFile.getOriginalFilename(); 
			      /*Map<String,Object> fileSizeCheckMap = CommonUtil.checkFileSize(fileSize,multipartFile.getOriginalFilename());*/
			      String newName = generateFileName();
			      String format = CommonUtil.getFileFormat(multipartFile.getOriginalFilename());
			      
			      String savePath = request.getSession().getServletContext().getRealPath(uploadTempPath);
			      File destFile = new File(savePath);
			      if(!destFile.exists()){
			          destFile.mkdirs();
			      }
			      retFile = new File(destFile,newName+"."+format);
			      FileUtils.copyInputStreamToFile(multipartFile.getInputStream(), retFile);
			      //文件上传
			      uniqueFileName = uploadFile(retFile, format);
			      if(uniqueFileName!=null && !"".equals(uniqueFileName)){
			    	  cmsSingleTempleDTO.setPath(uniqueFileName);
			    	  //保存模板上传日志
			    	  ImportLog importTempleLogDO = initImportLog();
			    	  Long udloadTempId = singleTempleProxy.saveImportTempleLog(importTempleLogDO);
			    	  if(udloadTempId > 0){
			    		  cmsSingleTempleDTO.setUploadTempleId(udloadTempId);
			    	  }
			      }
			}
			    
			counts = singleTempleProxy.insertSingleTemple(params, counts, cmsSingleTempleDTO);
		} catch (IOException e) {
			e.printStackTrace();
			logger.error("模板上传出错",e);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("模板的保存出错",e);
		}
		
    	return counts+"";
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
		 CmsSingleTepactivityDTO cmsSingleTepactivityDTO = new CmsSingleTepactivityDTO();
		 cmsSingleTepactivityDTO.setActivityId(Long.valueOf(jSONObject.get("id").toString()));
		 cmsSingleTepactivityDTO.setSingleTepnodeId(Long.valueOf(jSONObject.get("singleTepnodeId").toString()));
	   	int counts = 0;
	   	ReturnData returnData = null;
		try {
			counts = singleTempleProxy.delActivityByID(cmsSingleTepactivityDTO);
			returnData = new ReturnData(Boolean.TRUE,counts);
		} catch (Exception e) {
			returnData = new ReturnData(Boolean.FALSE,counts);
			e.printStackTrace();
			logger.error("删除活动id出错",e);
		}
	   	return returnData;
	}
	
	/**
	 * 添加活动id，即节点下面的活动id
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "addActivityByID", method = RequestMethod.POST)
	@ResponseBody
	public ReturnData addActivityByID(Model model,String params) {
		 Object obj = JSONValue.parse(params);
		 JSONObject jSONObject = (JSONObject)obj;
		 CmsSingleTepactivityDTO cmsSingleTepactivityDTO = new CmsSingleTepactivityDTO();
		 cmsSingleTepactivityDTO.setActivityId(Long.valueOf(jSONObject.get("id").toString()));
		 cmsSingleTepactivityDTO.setSingleTepnodeId(Long.valueOf(jSONObject.get("singleTepnodeId").toString()));
		 
		 int counts = 0;
		try {
			if(jSONObject.get("startdate") != null && !"".equals(jSONObject.get("startdate"))){
				 cmsSingleTepactivityDTO.setStartdate(DateUtil.parse(jSONObject.get("startdate").toString(),DateUtil.NEW_FORMAT));
			 }
			 if(jSONObject.get("enddate") != null && !"".equals(jSONObject.get("enddate"))){
				 cmsSingleTepactivityDTO.setEnddate(DateUtil.parse(jSONObject.get("enddate").toString(),DateUtil.NEW_FORMAT));
			 }
			 counts = singleTempleProxy.insertActivityId(cmsSingleTepactivityDTO);
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
			JSONArray mapList = singleTempleProxy.queryTopicLst(query, jSONObject);
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
	 * <pre>
	 * 生成importLog对象
	 * </pre>
	 * @return
	 */
	private ImportLog initImportLog() {
		ImportLog importLog = new ImportLog();
		importLog.setRealFileName(realFileName);
		importLog.setSecretKey(secretKey);
		importLog.setFileName(uniqueFileName);
		importLog.setCreateTime(new Date());
		importLog.setCreateUserId(0l);
		return importLog;
	}
	
	public static void main(String[] args) {
		
	}
	
}
