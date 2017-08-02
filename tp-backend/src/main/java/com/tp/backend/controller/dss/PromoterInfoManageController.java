package com.tp.backend.controller.dss;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tp.backend.controller.AbstractBaseController;
import com.tp.backend.util.CommonDateEditor;
import com.tp.common.vo.DssConstant;
import com.tp.common.vo.MemberConstant;
import com.tp.common.vo.PageInfo;
import com.tp.common.vo.Constant.AUTHOR_TYPE;
import com.tp.common.vo.DAOConstant.MYBATIS_SPECIAL_STRING;
import com.tp.common.vo.DAOConstant.WHERE_ENTRY;
import com.tp.dto.common.FailInfo;
import com.tp.dto.common.ResultInfo;
import com.tp.model.dss.PromoterInfo;
import com.tp.proxy.dss.PromoterInfoProxy;
import com.tp.proxy.usr.PasswordHelper;
import com.tp.util.Base64;
import com.tp.util.StringUtil;

/**
 * 分销、推广人员管理
 * @author szy
 *
 */
@Controller
@RequestMapping("/dss/promoterinfo/")
public class PromoterInfoManageController extends AbstractBaseController {

	@Autowired
	private PromoterInfoProxy promoterInfoProxy;
	@InitBinder
	public void initBinder(ServletRequestDataBinder binder){
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//		binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
		binder.registerCustomEditor(Date.class, new CommonDateEditor());
		initSubBinder(binder);
	}
	public CustomDateEditor initDateEditor(String format){
		SimpleDateFormat dateFormat = new SimpleDateFormat(format);
		return new CustomDateEditor(dateFormat, true);
	}
	public void initSubBinder(ServletRequestDataBinder binder){ 
		
	}
	/**
	 * 查询促销人员列表
	 * @param model
	 * @param promoterInfo
	 */
	@RequestMapping("list")
	public void list(Model model,PromoterInfo promoterInfo){
		model.addAttribute("promoterInfo",promoterInfo);
		model.addAttribute("promoterStatusList", DssConstant.PROMOTER_STATUS.values());
		model.addAttribute("promoterTypeList", DssConstant.PROMOTER_TYPE.values());
		Map<String,Object> params = new HashMap<String,Object>();
		if(StringUtils.isNotBlank(promoterInfo.getPromoterName())){
			params.put("promoterName", promoterInfo.getPromoterName());
		}
		if(StringUtils.isNotBlank(promoterInfo.getMobile())){
			params.put("mobile", promoterInfo.getMobile());
		}
		params.put("promoterStatus", promoterInfo.getPromoterStatus());
		params.put("promoterType", promoterInfo.getPromoterType());
		List<WHERE_ENTRY> whEntries = new ArrayList<>();
		if (null != promoterInfo.getStartTime()) {
			whEntries.add(new WHERE_ENTRY("pass_time", MYBATIS_SPECIAL_STRING.GT, promoterInfo.getStartTime()));
		}
		if (null != promoterInfo.getEndTime()) {
			whEntries.add(new WHERE_ENTRY("pass_time", MYBATIS_SPECIAL_STRING.LT, promoterInfo.getEndTime()));
		}
		params.put(MYBATIS_SPECIAL_STRING.WHERE.name(), whEntries);
		model.addAttribute("resultInfo", promoterInfoProxy.queryPageByParamNotEmpty(params,
				new PageInfo<PromoterInfo>(promoterInfo.getStartPage(),promoterInfo.getPageSize())));
	}
	
	@RequestMapping(value="childrenpage",method=RequestMethod.GET)
	public void childrenPage(Model model,Long parentPromoterId){
		model.addAttribute("parentPromoterId", parentPromoterId);
	}
	@RequestMapping(value="childrenpage",method=RequestMethod.POST)
	@ResponseBody
	public PageInfo<PromoterInfo> childrenPage(Model model,PromoterInfo promoterInfo){
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("parentPromoterId", promoterInfo.getParentPromoterId());
		PageInfo<PromoterInfo> resultInfo = promoterInfoProxy.queryPageByParam(params,new PageInfo<PromoterInfo>(promoterInfo.getStartPage(),promoterInfo.getPageSize())).getData();
		return resultInfo;
	}
	
	@RequestMapping(value="querypromoterlistbylikepromotername")
	@ResponseBody
	public List<PromoterInfo> queryPromoterListByLikePromoterName(Model model,String promoterName,Integer promoterType){
		Map<String,Object> params = new HashMap<String,Object>();
		params.put(MYBATIS_SPECIAL_STRING.LIKE.name(), " (promoter_name like concat('"+promoterName+"','%') or mobile='"+new String(Base64.encode(promoterName.getBytes()))+"')");
		if(promoterType!=null){
			params.put("promoterType", promoterType);
		}
		params.put(MYBATIS_SPECIAL_STRING.LIMIT.name(), 10);
		return 	promoterInfoProxy.queryByParam(params).getData();
	}
	
	@RequestMapping("show")
	public void show(Model model,Long promoterId){
		model.addAttribute("resultInfo", promoterInfoProxy.queryById(promoterId));
	}
	
	@RequestMapping(value="save",method=RequestMethod.GET)
	public void save(Model model,Long promoterId){
		ResultInfo<PromoterInfo> promoterInfo = null;
		if(null==promoterId){
			promoterInfo = new ResultInfo<PromoterInfo>(new PromoterInfo());
		}else{
			promoterInfo = promoterInfoProxy.queryById(promoterId);
		}
		model.addAttribute("resultInfo", promoterInfo);
		model.addAttribute("genderList", MemberConstant.GENDER.values());
		if(promoterId == null){			
			model.addAttribute("promoteTypeList", DssConstant.PROMOTER_BACKEND_TYPE.values());			
		}else{
			model.addAttribute("promoteTypeList", DssConstant.PROMOTER_TYPE.values());			
		}		
		model.addAttribute("credentialTypeList", DssConstant.CARD_TYPE.values());
	}
	
	@RequestMapping(value="save",method=RequestMethod.POST)
	@ResponseBody
	public ResultInfo<?> save(Model model,PromoterInfo promoterInfo){
		if(!StringUtil.isNullOrEmpty(promoterInfo.getPassWord())){
			 Boolean passIsSafe = PasswordHelper.checkPassWord(promoterInfo.getPassWord());
			 if(!passIsSafe){
				 return new ResultInfo<Boolean>(new FailInfo("密码必须包含字母数字以及特殊字符(10-20位),特殊字符包含~!@#$%^&*()_+=-"));
			 }
		}
		//验证
		if(promoterInfo.getPromoterId()==null){
			promoterInfo.setPassTime(new Date());
			//promoterInfo.setPromoterType(DssConstant.PROMOTER_TYPE.COUPON.code);
			promoterInfo.setCreateUser(AUTHOR_TYPE.USER_OPERATER+super.getUserName());
			promoterInfo.setUpdateUser(promoterInfo.getCreateUser());
			promoterInfo.setPromoterStatus(DssConstant.PROMOTER_STATUS.EN_PASS.code);
			return promoterInfoProxy.insert(promoterInfo);
		}else{
			promoterInfo.setPassWord(null);
			return promoterInfoProxy.updateNotNullById(promoterInfo);
		}
	}
	
	@RequestMapping(value="savecompany",method=RequestMethod.GET)
	public void saveCompany(Model model,Long promoterId){
		ResultInfo<PromoterInfo> promoterInfo = null;
		if(null==promoterId){
			promoterInfo = new ResultInfo<PromoterInfo>(new PromoterInfo());
		}else{
			promoterInfo = promoterInfoProxy.queryById(promoterId);
		}
		model.addAttribute("resultInfo", promoterInfo);
	}
	
	@RequestMapping(value="savecompany",method=RequestMethod.POST)
	@ResponseBody
	public ResultInfo<?> saveCompany(Model model,PromoterInfo promoterInfo){
		if(!StringUtil.isNullOrEmpty(promoterInfo.getPassWord())){
			 Boolean passIsSafe = PasswordHelper.checkPassWord(promoterInfo.getPassWord());
			 if(!passIsSafe){
				 return new ResultInfo<Boolean>(new FailInfo("密码必须包含字母数字以及特殊字符(10-20位),特殊字符包含~!@#$%^&*()_+=-"));
			 }
		}
		//验证
		if(promoterInfo.getPromoterId()==null){
			promoterInfo.setPassTime(new Date());
			promoterInfo.setPromoterType(DssConstant.PROMOTER_TYPE.COMPANY.code);
			promoterInfo.setCreateUser(AUTHOR_TYPE.USER_OPERATER+super.getUserName());
			promoterInfo.setUpdateUser(promoterInfo.getCreateUser());
			promoterInfo.setPromoterStatus(DssConstant.PROMOTER_STATUS.EN_PASS.code);
			return promoterInfoProxy.insert(promoterInfo);
		}else{
			promoterInfo.setPassWord(null);
			return promoterInfoProxy.updateNotNullById(promoterInfo);
		}
	}
	
	@RequestMapping(value="updatepassword",method=RequestMethod.POST)
	@ResponseBody
	public ResultInfo<Integer> updatePassword(Model model,PromoterInfo promoterInfo){
		return promoterInfoProxy.updatePassword(promoterInfo);
	}
	
	@RequestMapping("updatepromoterstatus")
	@ResponseBody
	public ResultInfo<Integer> updatePromoterStatus(Model model,Long promoterId,Integer promoterStatus){
		PromoterInfo promoterInfo = new PromoterInfo();
		promoterInfo.setPromoterId(promoterId);
		promoterInfo.setPromoterStatus(promoterStatus);
		return promoterInfoProxy.updateNotNullById(promoterInfo);
	}
	
	@RequestMapping("updatepageshow")
	@ResponseBody
	public ResultInfo<Integer> updatePageShow(Model model,Long promoterId,Integer pageShow){
		PromoterInfo promoterInfo = new PromoterInfo();
		promoterInfo.setPromoterId(promoterId);
		promoterInfo.setPageShow(pageShow);
		return promoterInfoProxy.updateNotNullById(promoterInfo);
	}
}
