package com.tp.backend.controller.cms;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tp.dto.cms.CoordsImagesHrefDTO;
import com.tp.dto.cms.ReturnData;


/**
 * 
 * <pre>
 * 	cms管理界面
 * </pre>
 *
 * @author huangqinbao
 */

@Controller
@RequestMapping("/cmstemplet")
public class CmsOptionController {
	
	private static final Logger  logger = LoggerFactory.getLogger(CmsOptionController.class);
	
	@InitBinder  
	protected void initBinder(HttpServletRequest request,  
	            ServletRequestDataBinder binder) throws Exception {   
	      DateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
	      CustomDateEditor dateEditor = new CustomDateEditor(fmt, true);  
	      binder.registerCustomEditor(Date.class, dateEditor);  
	}  
	
	/**
	 * <pre>
	 * 	后台管理界面
	 * </pre>
	 * @return
	 */
	@RequestMapping("/cmsIndexOpt")
	public String uploadFile(){
		return "/cms/manage/cmsIndexOpt";
	}
	
	/**
	 * 页面刷新
	 * @param 
	 * @return 页面
	 * @throws Exception
	 * @author huangqinbao 2014-12-29 16:17:54
	 */
	@RequestMapping(value={"/rushXigouIndex"},method=RequestMethod.POST)
	@ResponseBody
	public ReturnData rushXigouIndex(Model model,String params) {
		ReturnData returnData = null;
		/*//需要往队列里面发送页面刷新的信号
		boolean flag = true;
		try {
			flag = cmsOptionAO.rushXgIndex();
		} catch (Exception e) {
			logger.error("刷新首页缓存失败", e);
			e.printStackTrace();
		}
		if(flag){
			returnData = new ReturnData(Boolean.TRUE,"首页刷新成功");
		}else{
			returnData = new ReturnData(Boolean.FALSE,"首页刷新失败");
		}*/
		returnData = new ReturnData(Boolean.TRUE,"首页刷新成功");
    	return returnData;
	}
	
	@RequestMapping("/subview")
	public String subview(Model model,String params){
		List<CoordsImagesHrefDTO> lst = new ArrayList<CoordsImagesHrefDTO>();
		 Object obj = JSONValue.parse(params);
		 JSONArray array = (JSONArray)obj;
		 for(int i=0;i<array.size();i++){
			 JSONObject obj2 = (JSONObject)array.get(i);
			 CoordsImagesHrefDTO coordsImagesHrefDTO = new CoordsImagesHrefDTO();
			 coordsImagesHrefDTO.setCoords(obj2.get("coords").toString());
			 coordsImagesHrefDTO.setHref(obj2.get("href").toString());
			 lst.add(coordsImagesHrefDTO);
		 }
		 model.addAttribute("pageList", lst);
		return "/cms/manage/imageViewbk";
	}
	
}
