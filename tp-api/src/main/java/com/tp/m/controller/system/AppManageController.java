package com.tp.m.controller.system;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tp.m.ao.system.AppManageAO;
import com.tp.m.base.MResultVO;
import com.tp.m.enums.MResultInfo;
import com.tp.m.exception.MobileException;
import com.tp.m.query.system.QueryAppManage;
import com.tp.m.util.AssertUtil;
import com.tp.m.util.JsonUtil;
import com.tp.m.vo.system.AppManageVO;

/**
 * APP管理控制器
 * @author zhuss
 * @2016年1月13日 下午4:20:52
 */
@Controller
@RequestMapping("/app")
public class AppManageController {
	
	private static Logger log = LoggerFactory.getLogger(AppManageController.class);
	
	@Autowired
	private AppManageAO appManageAO;

	/**
	 * 检查是否是最新版本
	 * @return
	 */
	@RequestMapping(value = "/checknew",method = RequestMethod.GET)
	@ResponseBody
	public String getProvList(QueryAppManage appManage){
		try{
			log.info("[API接口 - 检查是否是最新版本 入参] = {}",JsonUtil.convertObjToStr(appManage));
			AssertUtil.notBlank(appManage.getApptype(), MResultInfo.APPTYPE_NULL);
			AssertUtil.notBlank(appManage.getAppversion(), MResultInfo.APPVERSION_NULL);
			MResultVO<AppManageVO> result  = appManageAO.queryVersionIsNew(appManage);
			log.info("[API接口 - 检查是否是最新版本 返回值] = {}",JsonUtil.convertObjToStr(result));
			return JsonUtil.convertObjToStr(result);
		}catch(MobileException m){
			log.error("API接口 - 检查是否是最新版本 MobileException",m);
			return JsonUtil.convertObjToStr(new MResultVO<>(m));
		}
		
	}
}
