package com.tp.m.controller.advert;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tp.m.base.MResultVO;
import com.tp.m.enums.MResultInfo;
import com.tp.m.helper.RequestHelper;
import com.tp.m.util.JsonUtil;
import com.tp.model.bse.AdvertLog;
import com.tp.service.bse.IAdvertLogService;

/**
 * 广告日志
 * 
 * @author zhougf
 *
 */
@Controller
@RequestMapping("/advert")
public class AdvertLogController {

	private static Logger log = LoggerFactory.getLogger(AdvertLogController.class);

	@Autowired
	private IAdvertLogService advertLogService;

	/**
	 * 记录广告信息
	 * 
	 * @return
	 */
	@RequestMapping(value = "/recordAdvert", method = RequestMethod.POST)
	@ResponseBody
	public String getProvList(HttpSession session, HttpServletRequest request) {
		String jsonStr = RequestHelper.getJsonStrByIO(request);
		AdvertLog advertLog = (AdvertLog) JsonUtil.getObjectByJsonStr(jsonStr, AdvertLog.class);
		advertLog.setAdvertPlatName(advertLog.getAdvertPlatCode());
		String userIp = RequestHelper.getIpAddr(request);
		advertLog.setRemoteAddr(userIp);
		advertLog.setCreateUser("admin");
		advertLog.setUpdateUser("admin");
		advertLog.setCreateTime(new Date());
		advertLog = advertLogService.insert(advertLog);
		return JsonUtil.convertObjToStr(new MResultVO<>(MResultInfo.LOGIN_SUCCESS, advertLog));
	}

}
