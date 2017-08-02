package com.tp.backend.controller.mem;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tp.common.vo.PageInfo;
import com.tp.common.vo.mem.UriConstant;
import com.tp.dto.cms.result.SubmitResultInfo;
import com.tp.dto.common.FailInfo;
import com.tp.dto.common.ResultInfo;
import com.tp.exception.ServiceException;
import com.tp.model.mem.PointRuleConfig;
import com.tp.proxy.mem.PointInfoProxy;
import com.tp.util.StringUtil;



@Controller
@Scope(BeanDefinition.SCOPE_SINGLETON)
@RequestMapping(UriConstant.USER.SPACE)
public class UserPointController {
	
	
	@Autowired
	private PointInfoProxy pointInfoProxy;
	
	@RequestMapping(value="/point/register")
	public String register(Model model) throws Exception {
		return "user/point/register";
	}
	
	@RequestMapping(value="/point/add")
	@ResponseBody
	public SubmitResultInfo add(HttpServletRequest request,
			@RequestParam(value="sceneCode", required=false) String sceneCode,
			@RequestParam(value="platform", required=false) String platform,
			@RequestParam(value="point", required=false) String point,
			@RequestParam(value="isExpiry", required=false) Integer isExpiry,
			@RequestParam(value="createEndTime", required=false) String createEndTime,
			@RequestParam(value="createBeginTime", required=false) String createBeginTime) throws ParseException {
		ResultInfo result = insertAndEdit(point, isExpiry, createBeginTime, createEndTime);
		if(result != null){
			return new SubmitResultInfo(result.getMsg().getMessage(), result.getMsg().getCode());
		}
		try{
			pointInfoProxy.insert(platform, sceneCode, point, isExpiry, createBeginTime, createEndTime);
		}catch(ServiceException e){
			return new SubmitResultInfo<>(e.getMessage(), e.getErrorCode());
		}
		return new SubmitResultInfo<>("添加成功", 100);
	}
	public ResultInfo insertAndEdit(String point, Integer isExpiry, String createBeginTime, String createEndTime)  throws ParseException {
		ResultInfo result = new ResultInfo();
		result.setSuccess(false);
		if(StringUtils.isEmpty(point)){
			FailInfo fail = new FailInfo();
			fail.setCode(910);
			fail.setMessage("积分数不得为空");
			result.setMsg(fail);
			return result;
		}
		if(!this.isNumberStr(point)){
			FailInfo fail = new FailInfo();
			fail.setCode(910);
			fail.setMessage("积分数必须为数值");
			result.setMsg(fail);
			return result;
		}
		if(Integer.valueOf(point).intValue() <= 0) {
			FailInfo fail = new FailInfo();
			fail.setCode(910);
			fail.setMessage("积分数必须大于0");
			result.setMsg(fail);
			return result;
		}
		if(1 == isExpiry) {
			if(StringUtils.isEmpty(createBeginTime) || StringUtils.isEmpty(createEndTime)) {
				FailInfo fail = new FailInfo();
				fail.setCode(910);
				fail.setMessage("生效时间不得为空");
				result.setMsg(fail);
				return result;
			} else {
				Date beginTime = strToDate(createBeginTime);
				Date endTime = strToDate(createEndTime);
				Date now = new Date();
				if(beginTime.after(endTime)){
					FailInfo fail = new FailInfo();
					fail.setCode(910);
					fail.setMessage("起始时间不得大于结束时间");
					result.setMsg(fail);
					return result;
				}
				if(endTime.before(now)){
					FailInfo fail = new FailInfo();
					fail.setCode(910);
					fail.setMessage("结束时间不得小于当前时间");
					result.setMsg(fail);
					return result;
				}
			}
		}
		return null;
	 }
	 
	private boolean isNumberStr(String str) {
		boolean isNumber = true;
		try {
			Long.valueOf(str);
		} catch(Exception ex) {
			isNumber = false;
		}
		return isNumber;
	}
	
	private Date strToDate(String dateStr) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date date = sdf.parse(dateStr);
		return date;
	}
	@RequestMapping(value="/point/edit")
	@ResponseBody
	public SubmitResultInfo edit(HttpServletRequest request,
			@RequestParam(value="id", required=false) Long id,
			@RequestParam(value="platform", required=false) String platform,
			@RequestParam(value="state", required=false) String state,
			@RequestParam(value="point", required=false) String point,
			@RequestParam(value="isExpiry", required=false) Integer isExpiry,
			@RequestParam(value="createEndTime", required=false) String createEndTime,
			@RequestParam(value="createBeginTime", required=false) String createBeginTime) throws Exception {
		ResultInfo result = insertAndEdit(point, isExpiry, createBeginTime, createEndTime);
		if(result != null){
			return new SubmitResultInfo(result.getMsg().getMessage(), result.getMsg().getCode());
		}
		try{
			pointInfoProxy.edit(id, platform, state, point, isExpiry, createBeginTime, createEndTime);
		}catch(ServiceException e){
			return new SubmitResultInfo<>(e.getMessage(), e.getErrorCode());
		}
		return new SubmitResultInfo("修改成功", 100);
	}
	
	@RequestMapping(value="/point/delete")
	@ResponseBody
	public SubmitResultInfo delete(HttpServletRequest request, @RequestParam(value="id", required=false) Long id) throws Exception {
		pointInfoProxy.delete(id);
		return new SubmitResultInfo("删除成功", 100);
	}
	
	@RequestMapping(value="/point/register/listJSON", method=RequestMethod.GET, produces=MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public JSONObject showRegister(Model model,
			@RequestParam(value="page", defaultValue = "1") Integer page,
			@RequestParam(value="rows", defaultValue = "10") Integer rows,
			@RequestParam(value="point",required=false) String point,
			@RequestParam(value="isExpiry",required=false) Integer isExpiry,
			@RequestParam(value="createBeginTime",required=false) String createBeginTime,
			@RequestParam(value="createEndTime",required=false) String createEndTime) throws Exception {
		JSONObject obj = new JSONObject();
		PageInfo<PointRuleConfig> pageObj = pointInfoProxy.list("1", point, isExpiry, createBeginTime, createEndTime, page, rows);
		obj = pointInfoProxy.toJson(pageObj, rows, page);
		model.addAttribute("review", obj);
		return obj;
	}
	
	@RequestMapping(value="/point/my/listJSON", method=RequestMethod.GET, produces=MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public JSONObject showMy(Model model,
			@RequestParam(value="page", defaultValue = "1") Integer page,
			@RequestParam(value="rows", defaultValue = "10") Integer rows,
			@RequestParam(value="point",required=false) String point,
			@RequestParam(value="isExpiry",required=false) Integer isExpiry,
			@RequestParam(value="createBeginTime",required=false) String createBeginTime,
			@RequestParam(value="createEndTime",required=false) String createEndTime) throws Exception {
		JSONObject obj = new JSONObject();
		PageInfo<PointRuleConfig> pageObj = pointInfoProxy.
				list("5", point, isExpiry, createBeginTime, createEndTime, page, rows);
		obj = pointInfoProxy.toJson(pageObj, rows, page);
		model.addAttribute("review", obj);
		return obj;
	}
	
	@RequestMapping(value="/point/baby/listJSON", method=RequestMethod.GET, produces=MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public JSONObject showBaby(Model model,
			@RequestParam(value="page", defaultValue = "1") Integer page,
			@RequestParam(value="rows", defaultValue = "10") Integer rows,
			@RequestParam(value="point",required=false) String point,
			@RequestParam(value="isExpiry",required=false) Integer isExpiry,
			@RequestParam(value="createBeginTime",required=false) String createBeginTime,
			@RequestParam(value="createEndTime",required=false) String createEndTime) throws Exception {
		JSONObject obj = new JSONObject();
		PageInfo<PointRuleConfig> pageObj = pointInfoProxy.
				list("6", point, isExpiry, createBeginTime, createEndTime, page, rows);
		obj = pointInfoProxy.toJson(pageObj, rows, page);
		model.addAttribute("review", obj);
		return obj;
	}
	
	@RequestMapping(value="/point/register/list")
	public String registerList(HttpServletRequest request,
			@RequestParam(value="point",required=false) String point,
			@RequestParam(value="sceneCode", required=false) String sceneCode,
			@RequestParam(value="isExpiry",required=false) Integer isExpiry,
			@RequestParam(value="createBeginTime",required=false) String createBeginTime,
			@RequestParam(value="createEndTime",required=false) String createEndTime) {
		request.setAttribute("point", StringUtil.isNullOrEmpty(point) ? null : point);
		request.setAttribute("sceneCode", StringUtil.isNullOrEmpty(sceneCode) ? null : sceneCode);
		request.setAttribute("isExpiry", StringUtil.isNullOrEmpty(isExpiry) ? 5 : isExpiry);
		request.setAttribute("createBeginTime", StringUtil.isNullOrEmpty(createBeginTime) ? null : createBeginTime);
		request.setAttribute("createEndTime", StringUtil.isNullOrEmpty(createEndTime) ? null : createEndTime);
		return "user/point/register";
	}
	
	@RequestMapping(value="/point/baby/list")
	public String babyList(HttpServletRequest request,
			@RequestParam(value="point",required=false) String point,
			@RequestParam(value="sceneCode", required=false) String sceneCode,
			@RequestParam(value="isExpiry",required=false) Integer isExpiry,
			@RequestParam(value="createBeginTime",required=false) String createBeginTime,
			@RequestParam(value="createEndTime",required=false) String createEndTime) {
		request.setAttribute("point", StringUtil.isNullOrEmpty(point) ? null : point);
		request.setAttribute("sceneCode", StringUtil.isNullOrEmpty(sceneCode) ? null : sceneCode);
		request.setAttribute("isExpiry", StringUtil.isNullOrEmpty(isExpiry) ? 5 : isExpiry);
		request.setAttribute("createBeginTime", StringUtil.isNullOrEmpty(createBeginTime) ? null : createBeginTime);
		request.setAttribute("createEndTime", StringUtil.isNullOrEmpty(createEndTime) ? null : createEndTime);
		return "user/point/baby";
	}
	
	@RequestMapping(value="/point/my/list")
	public String myList(HttpServletRequest request,
			@RequestParam(value="point",required=false) String point,
			@RequestParam(value="sceneCode", required=false) String sceneCode,
			@RequestParam(value="isExpiry",required=false) Integer isExpiry,
			@RequestParam(value="createBeginTime",required=false) String createBeginTime,
			@RequestParam(value="createEndTime",required=false) String createEndTime) {
		request.setAttribute("point", StringUtil.isNullOrEmpty(point) ? null : point);
		request.setAttribute("sceneCode", StringUtil.isNullOrEmpty(sceneCode) ? null : sceneCode);
		request.setAttribute("isExpiry", StringUtil.isNullOrEmpty(isExpiry) ? 5 : isExpiry);
		request.setAttribute("createBeginTime", StringUtil.isNullOrEmpty(createBeginTime) ? null : createBeginTime);
		request.setAttribute("createEndTime", StringUtil.isNullOrEmpty(createEndTime) ? null : createEndTime);
		return "user/point/my";
	}
	
	@RequestMapping("/point/toShow")
	public String toShow(@RequestParam(value = "id") Long id, Model model) throws Exception {
		PointRuleConfig configDO = pointInfoProxy.show(id);
		model.addAttribute("point", configDO);
		return "user/point/subpages/edit";
	}
	
	@RequestMapping(value="/point/my")
	public String my(Model model) throws Exception {
		return "user/point/my";
	}
	
	@RequestMapping(value="/point/baby")
	public String baby(Model model) throws Exception {
		return "user/point/baby";
	}

	@RequestMapping(value="/point/toAdd")
	public String toAdd(Model model) {
		return "user/point/subpages/add";
	}
	
	@RequestMapping(value="/point/toAddBaby")
	public String toAddBaby(Model model) {
		return "user/point/subpages/addBaby";
	}
	
	@RequestMapping(value="/point/toAddMy")
	public String toAddMy(Model model) {
		return "user/point/subpages/addMy";
	}
}
