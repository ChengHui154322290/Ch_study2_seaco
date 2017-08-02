package com.tp.backend.controller.cms;

import java.io.UnsupportedEncodingException;
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
import org.springframework.web.util.WebUtils;

import com.tp.common.vo.Constant;
import com.tp.common.vo.PageInfo;
import com.tp.common.vo.cms.AdvertTypeAPPEnum;
import com.tp.common.vo.cms.ElementEnum;
import com.tp.dto.cms.CmsActivityElementDTO;
import com.tp.dto.cms.CmsPictureElementDTO;
import com.tp.dto.cms.ReturnData;
import com.tp.model.cms.DefinedElement;
import com.tp.model.cms.Position;
import com.tp.model.cms.SeoElement;
import com.tp.model.cms.WrittenElement;
import com.tp.proxy.cms.ActivityElementProxy;
import com.tp.proxy.cms.DefinedElementProxy;
import com.tp.proxy.cms.PictureElementProxy;
import com.tp.proxy.cms.PositionProxy;
import com.tp.proxy.cms.SeoElementProxy;
import com.tp.proxy.cms.WrittenElementProxy;


@Controller
@Scope(BeanDefinition.SCOPE_SINGLETON)
@RequestMapping("/cms")
public class TempletPositionController {
	private final static Log logger = LogFactory
			.getLog(TempletPositionController.class);

	@Autowired
	private PositionProxy positionProxy;
	
	@Autowired
	private ActivityElementProxy activityElementProxy;
	
	@Autowired
	private WrittenElementProxy writtenElementProxy;
	
	@Autowired
	PictureElementProxy pictureElementProxy;
	
	@Autowired
	DefinedElementProxy definedElementProxy;
	@Autowired
	SeoElementProxy seoElementProxy;

	@InitBinder  
	protected void initBinder(HttpServletRequest request,  
	            ServletRequestDataBinder binder) throws Exception {   
	      DateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
	      CustomDateEditor dateEditor = new CustomDateEditor(fmt, true);  
	      binder.registerCustomEditor(Date.class, dateEditor);  
	}  
	
	/**
	 * 位置列表的查询
	 * @param 
	 * @return 页面
	 * @throws Exception
	 * @author huangqinbao 2014-12-29 16:17:54
	 */
	@RequestMapping(value={"/listPositionOpr"},method=RequestMethod.GET)
	public String listPositionOpr(Model model, Position query,String advertinfo){
		if(query == null){
			query.setStatus(0);
			query = new Position();
		}
		query.setStatus(0);
		if(advertinfo != null && !"".equals(advertinfo)){
			JSONObject jSONObject1 = new JSONObject();
			Object obj1 = JSONValue.parse(advertinfo);
			jSONObject1 = (JSONObject) obj1;
			
			if(jSONObject1.get("pageNameBak") != null){
				try {
					query.setPageName(new String(jSONObject1.get("pageNameBak").toString().getBytes("iso8859-1"),"utf-8"));
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
			}
			if(jSONObject1.get("templeNameBak") != null){
				try {
					query.setTempleName(new String(jSONObject1.get("templeNameBak").toString().getBytes("iso8859-1"),"utf-8"));
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
			}
			if(jSONObject1.get("positionNameBak") != null){
				try {
					query.setPositionName(new String(jSONObject1.get("positionNameBak").toString().getBytes("iso8859-1"),"utf-8"));
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
			}
		}
		
		PageInfo<Position> cmsPositionDO = positionProxy.getPositionList(query);
		
		if (cmsPositionDO != null) {
			model.addAttribute("pageList", cmsPositionDO);
		}
		
		model.addAttribute("query", query);
		return "cms/templet/positionList";
	}
	
	/**
	 * 查询位置列表
	 * @param 
	 * @return 页面
	 * @throws Exception
	 * @author huangqinbao 2014-12-29 16:17:54
	 */
	@RequestMapping(value={"/queryPositionList"},method=RequestMethod.POST)
	public String queryPositionList(Model model, Position query){
		PageInfo<Position> cmsPositionDO = positionProxy.getPositionList(query);
		if (cmsPositionDO != null) {
			model.addAttribute("pageList", cmsPositionDO);
			model.addAttribute("query", query);
		}
		return "cms/templet/positionList";
	}
	private JSONArray stringToObject(String params){
		Object obj = JSONValue.parse(params);
		JSONArray array = (JSONArray)obj;
		return array;
	}
	/**
	 * 位置创建和修改页面
	 * @param consigneeAddressDO
	 * @return 页面
	 * @throws Exception
	 * @author huangqinbao 2014-12-29 16:17:54
	 * @throws NumberFormatException 
	 */
	@RequestMapping(value={"/addPosition.htm"},method=RequestMethod.POST)
	public String addPosition(Model model,String params,String positionInfo) {
		if(params != null){//此处不为null，表示修改模板
			JSONObject jSONObject = new JSONObject();
			JSONArray array = stringToObject(params);
			jSONObject = (JSONObject)array.get(0);
			Position sd = new Position();
			try {
				sd = positionProxy.getById(Long.parseLong(jSONObject.get("id").toString()));
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("页面管理查询出错",e);
			}
			model.addAttribute("page", sd);
		}
		
		if(positionInfo != null){
			JSONObject jSONObject1 = new JSONObject();
			Object obj1 = JSONValue.parse(positionInfo);
			jSONObject1 = (JSONObject) obj1;
			model.addAttribute("pageNameBak", jSONObject1.get("pageName"));
			model.addAttribute("templeNameBak", jSONObject1.get("templeName"));
			model.addAttribute("positionNameBak", jSONObject1.get("positionName"));
		}
		return "cms/templet/positionEdit";
	}
	
	/**
	 * 添加，模板跳转页面
	 * @param 
	 * @return 页面
	 * @throws Exception
	 */
	@RequestMapping(value={"/addSingleActivity"},method=RequestMethod.POST)
	public String addSingleActivity(Model model,String params,String singleTempInfo,
			HttpServletRequest request) {
		Object obj = JSONValue.parse(params);//主表
		JSONObject jSONObject = (JSONObject)obj;
		Long positionId = Long.valueOf(jSONObject.get("positionId").toString());
		/*Long pageId = Long.valueOf(jSONObject.get("pageId").toString());
		Long templeId = Long.valueOf(jSONObject.get("templeId").toString());*/
		String pageName = jSONObject.get("pageName").toString();
		String templeName = jSONObject.get("templeName").toString();
		String positionName = jSONObject.get("positionName").toString();
		Integer elementType = Integer.parseInt(jSONObject.get("elementType").toString());
		model.addAttribute("positionId", positionId);
		model.addAttribute("pageName", pageName);
		model.addAttribute("templeName", templeName);
		model.addAttribute("positionName", positionName);
		model.addAttribute("bucketURL", Constant.IMAGE_URL_TYPE.cmsimg.url);
        model.addAttribute("bucketname", Constant.IMAGE_URL_TYPE.cmsimg.name());
        model.addAttribute("actTypeList", AdvertTypeAPPEnum.values());
		//下面是带查询条件
		if(jSONObject.get("pageNameBak") != null){
			String pageNameBak = jSONObject.get("pageNameBak").toString();
			model.addAttribute("pageNameBak", pageNameBak);
		}
		if(jSONObject.get("templeNameBak") != null){
			String templeNameBak = jSONObject.get("templeNameBak").toString();
			model.addAttribute("templeNameBak", templeNameBak);
		}
		if(jSONObject.get("positionNameBak") != null){
			String positionNameBak = jSONObject.get("positionNameBak").toString();
			model.addAttribute("positionNameBak", positionNameBak);
		}
		
		String str = "";
		if(ElementEnum.ACTIVITY.getValue() == elementType){
			//元素类型为活动
			List<CmsActivityElementDTO> cmsActivityElementDTO = new ArrayList<CmsActivityElementDTO>();
			try {
				cmsActivityElementDTO = activityElementProxy.addActivityElement(positionId);
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("活动元素添加页面查询出错",e);
			}
			
			model.addAttribute("detaillist", cmsActivityElementDTO);
			
			String sessionId = WebUtils.getSessionId(request);
			model.addAttribute("sessionId", sessionId);
			
			str = "cms/templet/activityElementEdit";
			
		}else if(ElementEnum.WRITTEN.getValue() == elementType){
			//元素类型为文字
			List<WrittenElement> cmsWrittenElementDOList = new ArrayList<WrittenElement>();
			
			try {
				cmsWrittenElementDOList = writtenElementProxy.getWrittenElement(positionId);
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("文字元素添加页面查询出错",e);
			}
			
			model.addAttribute("detaillist", cmsWrittenElementDOList);
			
			str = "cms/templet/writtenElementEdit";
		}else if(ElementEnum.PICTURE.getValue() == elementType){
			//元素类型图片
			List<CmsPictureElementDTO> cmsPictureElementDOList = new ArrayList<CmsPictureElementDTO>();
			
			try {
				cmsPictureElementDOList = pictureElementProxy.getPictureElement(positionId);
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("图片元素添加页面查询出错",e);
			}
			
			model.addAttribute("detaillist", cmsPictureElementDOList);
			
			String sessionId = WebUtils.getSessionId(request);
			model.addAttribute("sessionId", sessionId);
			
			str = "cms/templet/pictureElementEdit";
		}else if(ElementEnum.DEFINED.getValue() == elementType){
			//元素类型：自定义
			List<DefinedElement> cmsDefinedElementDOList = new ArrayList<DefinedElement>();
			
			try {
				cmsDefinedElementDOList = definedElementProxy.getDefinedElement(positionId);
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("自定义元素添加页面查询出错",e);
			}
			
			model.addAttribute("detaillist", cmsDefinedElementDOList);
			
			str = "cms/templet/definedElementEdit";
		}else if(ElementEnum.SEO.getValue() == elementType){
			//元素类型：自定义
			List<SeoElement> cmsSeoElementDOList = new ArrayList<SeoElement>();
			
			try {
				cmsSeoElementDOList = seoElementProxy.getDefinedElement(positionId);
			} catch (Exception e) {
				logger.error("自定义元素添加页面查询出错",e);
			}
			
			model.addAttribute("detaillist", cmsSeoElementDOList);
			
			str = "cms/templet/seoElementEdit";
		}
		return str;
	}
	
	/**
	 * 模板的删除
	 * @param announceAO
	 * @return 主键
	 * @throws Exception
	 * @author huangqinbao 2014-12-29 16:17:54
	 */
	@RequestMapping(value={"/delPosition.htm"},method=RequestMethod.POST)
	@ResponseBody
	public ReturnData delPosition(Model model,String params){
		ReturnData returnData = null;
		try {
			List<Long> ids = new ArrayList<Long>();
			 Object obj = JSONValue.parse(params);
			 JSONArray array = (JSONArray)obj;
			 for(int i=0;i<array.size();i++){
				 JSONObject obj2 = (JSONObject)array.get(i);
				 ids.add(Long.valueOf(obj2.get("id").toString()));
			 }
			int counts = positionProxy.delByIds(ids);
			if(counts == -1){
				returnData = new ReturnData(Boolean.FALSE,"删除失败，请先移除位置下面的数据");
			}else{
				returnData = new ReturnData(Boolean.TRUE,"删除成功");
			}
			
		} catch (Exception e) {
			returnData = new ReturnData(Boolean.FALSE,"删除失败，请联系管理员");
			e.printStackTrace();
			logger.error("位置管理的删除出错",e);
		}
		return returnData;
	}
	
	/**
	 * 模板位置提交按钮事件
	 * @param 
	 * @return 主键
	 * @throws Exception
	 * @author huangqinbao 2014-12-29 16:17:54
	 */
	@RequestMapping(value={"/savePositionTemp.htm"},method=RequestMethod.POST)
	@ResponseBody
	public ReturnData savePositionTemp(Model model,Position cmsPositionDO,HttpServletRequest request){
		ReturnData returnData = null;
		if(cmsPositionDO.getId()!=null && cmsPositionDO.getId()>0){
			int lid = positionProxy.updateSubmit(cmsPositionDO);
			if(lid == -1){
				returnData = new ReturnData(Boolean.FALSE,"已经达到位置个数的上限，请检查模板的元素个数");
				logger.error("位置管理保存报错");
			}else if(lid < 1){
				returnData = new ReturnData(Boolean.FALSE,"位置管理保存报错");
				logger.error("位置管理保存报错");
			}else{
				returnData = new ReturnData(Boolean.TRUE,lid);
			}
		}else{
			long lid = positionProxy.addSubmit(cmsPositionDO);
			if(lid == -1){
				returnData = new ReturnData(Boolean.FALSE,"已经达到位置个数的上限，请检查模板的元素个数");
				logger.error("位置管理保存报错");
			}else if(lid < 1){
				returnData = new ReturnData(Boolean.FALSE,"位置管理保存报错");
				logger.error("位置管理保存报错");
			}else{
				returnData = new ReturnData(Boolean.TRUE,lid);
			}
		}
    	return returnData;
	}
}
