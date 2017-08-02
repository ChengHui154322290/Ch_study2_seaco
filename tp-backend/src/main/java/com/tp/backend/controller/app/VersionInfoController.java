package com.tp.backend.controller.app;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tp.backend.controller.AbstractBaseController;
import com.tp.common.vo.DAOConstant;
import com.tp.common.vo.PageInfo;
import com.tp.common.vo.DAOConstant.MYBATIS_SPECIAL_STRING;
import com.tp.common.vo.app.VersionConstant;
import com.tp.dto.common.FailInfo;
import com.tp.dto.common.ResultInfo;
import com.tp.exception.AppServiceException;
import com.tp.model.app.VersionInfo;
import com.tp.proxy.app.VersionInfoProxy;
import com.tp.util.BeanUtil;

/**
 * APP版本管理
 * @author zhuss
 * @2016年4月06日 下午1:23:44
 */
@Controller
@RequestMapping("/app/version/")
public class VersionInfoController extends AbstractBaseController {
	
	private  Logger logger = LoggerFactory.getLogger(VersionInfoController.class);
	
	@Autowired
	private VersionInfoProxy versionInfoProxy;
	
	/**
	 * 版本信息列表
	 * @param model
	 * @param promoterInfo
	 */
	@RequestMapping("list")
	public void list(Model model,VersionInfo versionInfo){
		model.addAttribute("versionInfo", versionInfo);
		model.addAttribute("platformList", VersionConstant.VERSION_PLATFORM.values());
		model.addAttribute("statusList", VersionConstant.VERSION_STATUS.values());
		Map<String, Object> param = BeanUtil.beanMap(versionInfo);
		/*if(MapUtils.isNotEmpty(param))versionInfo.remove(param);*/
		List<DAOConstant.WHERE_ENTRY> whereList = new ArrayList<DAOConstant.WHERE_ENTRY>();
		param.put(MYBATIS_SPECIAL_STRING.WHERE.name(), whereList);
		param.put(MYBATIS_SPECIAL_STRING.ORDER_BY.name(), "create_time desc");
		PageInfo<VersionInfo> resultInfo =versionInfoProxy.queryPageByParam(param,new PageInfo<VersionInfo>(versionInfo.getStartPage(),versionInfo.getPageSize())).getData();
		model.addAttribute("page", resultInfo);
	}
	
	/**
	 * 跳转版本消息编辑页
	 * @param model
	 * @param id
	 */
	@RequestMapping(value="save",method=RequestMethod.GET)
	public void save(Model model,Long id){
		ResultInfo<VersionInfo> versionInfo = null;
		if(null==id){
			versionInfo = new ResultInfo<VersionInfo>(new VersionInfo());
		}else{
			versionInfo = versionInfoProxy.queryById(id);
		}
		model.addAttribute("resultInfo", versionInfo);
		model.addAttribute("platformList", VersionConstant.VERSION_PLATFORM.values());
	}
	
	/**
	 * 保存版本消息记录
	 * @param model
	 * @param promoterInfo
	 * @return
	 */
	@RequestMapping(value="save",method=RequestMethod.POST)
	@ResponseBody
	public ResultInfo<?> save(VersionInfo versionInfo){
		try{
			versionInfo.setModifyTime(new Date());
			versionInfo.setModifyUser(super.getUserName());
			return versionInfoProxy.save(versionInfo);
		}catch(AppServiceException ase){
			logger.error("[APP版本管理 - 保存版本消息 AppServiceException] ={}",ase.getMessage()); 
			return new ResultInfo<>(new FailInfo(ase.getMessage()));
		}catch(Exception e){
			logger.error("[APP版本管理 - 保存版本消息 AppServiceException] ={}",e); 
			return new ResultInfo<>(new FailInfo("保存失败"));
		}
	}
	
	/**
	 * 修改状态
	 * @return
	 */
	@RequestMapping("uptstatus")
	@ResponseBody
	public ResultInfo<Integer> updateVersionStatus(Model model,Long id,Integer status){
		if(null == id || null == status) return new ResultInfo<>(new FailInfo("参数异常,必选字段为空"));
		VersionInfo versionInfo = new VersionInfo();
		versionInfo.setId(id);
		versionInfo.setStatus(status);
		versionInfo.setModifyTime(new Date());
		versionInfo.setModifyUser(super.getUserName());
		if(status == 1) versionInfo.setPushTime(new Date());
		return versionInfoProxy.updateNotNullById(versionInfo);
	}
}
