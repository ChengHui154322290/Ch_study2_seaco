/**
 * 
 */
package com.tp.m.controller.activity;

import java.util.Calendar;
import java.util.Map;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tp.dto.mmp.enums.TopicType;
import com.tp.m.ao.activity.ActivityAO;
import com.tp.m.base.MResultVO;
import com.tp.m.enums.MResultInfo;
import com.tp.m.helper.AuthHelper;
import com.tp.m.helper.RequestHelper;
import com.tp.m.to.cache.TokenCacheTO;
import com.tp.m.util.JsonUtil;
import com.tp.util.DateUtil;

/**
 * @author ll
 *
 */
@Controller
@RequestMapping("/activity")
public class ActivityController {
	private static final Logger log = LoggerFactory.getLogger(ActivityController.class);
	@Autowired
	ActivityAO activityAO;
	@Autowired
	private AuthHelper authHelper;
	
	private static String[] keys = new String[]{"328活动大促专场","328小西福利秒杀","328小西福利专享券"};
	/**
	 * 主页抓狗活动
	 * @return
	 */
//	@RequestMapping("/2016032801")
	@ResponseBody
	public String activity20160328(HttpServletRequest request, HttpSession session) {
		if(isActivityEnd("20160328000000", "20160402000000")) {
			return JsonUtil.convertObjToStr(new MResultVO<>("活动已结束")); 
		}
		String jsonStr = RequestHelper.getJsonStrByIO(request);
		Map<String, String> ps = (Map<String, String>) JsonUtil.getObjectByJsonStr(jsonStr, Map.class);
		TokenCacheTO usr = authHelper.authToken(ps.get("token"));
		int randomKey = new Random().nextInt(2);
		MResultVO<Object> couponResult;
		
		if(randomKey == 2) {
			couponResult = activityAO.sendCoupon2UserUnique(usr.getTel(),keys[2]);
			if("1".equals(couponResult.getCode())) {
				log.info(usr.getTel()+"获取一个优惠券");
				return JsonUtil.convertObjToStr(couponResult); 
			}
			else {
				randomKey = new Random().nextInt(2);
				MResultVO<String> result  = null;
				if(randomKey != 0) {
					result = activityAO.getTopicLinkByTopicName(keys[randomKey], TopicType.SINGLE.ordinal(), usr.getUid());
					if(!"0".equals(result.getCode())) {
						result = activityAO.getTopicLinkByTopicName(keys[0], TopicType.THEME.ordinal(), usr.getUid());
					}
					return JsonUtil.convertObjToStr(result);
				}
				return JsonUtil.convertObjToStr(activityAO.getTopicLinkByTopicName(keys[randomKey], TopicType.THEME.ordinal(), usr.getUid()));
			}
		}
		else {
			MResultVO<String> result  = null;
			if(randomKey != 0) {
				result = activityAO.getTopicLinkByTopicName(keys[randomKey], TopicType.SINGLE.ordinal(), usr.getUid());
				if(!"0".equals(result.getCode())) {
					result = activityAO.getTopicLinkByTopicName(keys[0], TopicType.THEME.ordinal(), usr.getUid());
				}
				return JsonUtil.convertObjToStr(result);
			}
			return JsonUtil.convertObjToStr(activityAO.getTopicLinkByTopicName(keys[randomKey], TopicType.THEME.ordinal(), usr.getUid()));
		}
	}
	
	@RequestMapping("/**")
	@ResponseBody
	public String activity() {
		return JsonUtil.convertObjToStr(new MResultVO<>("无此活动")); 
	}
	
	/**
	 * 随机生成数 是否触发活动小狗出现
	 */
	@RequestMapping("/rddog")
	@ResponseBody
	public String random(HttpSession session) {
		if(isActivityEnd("20160328000000", "20160402000000")) {
			return JsonUtil.convertObjToStr(new MResultVO<>("活动已结束")); 
		}
		Random rd = new Random(Calendar.getInstance().getTimeInMillis());
		int rdresult = rd.nextInt(100);
		if(rdresult < 100) {
			return JsonUtil.convertObjToStr(new MResultVO<>(MResultInfo.OPERATION_SUCCESS));
		}
		else {
			return JsonUtil.convertObjToStr(new MResultVO<>(MResultInfo.OPERATION_FAILED));
		}
	}
	
	
	
	private boolean isActivityEnd(String d1, String d2) {
		Long start = DateUtil.parse(d1, DateUtil.LONG_FORMAT).getTime();
		Long end = DateUtil.parse(d2, DateUtil.LONG_FORMAT).getTime();
		Long currentTime = Calendar.getInstance().getTimeInMillis();
		if(currentTime >= start && currentTime <= end) {
			return false;
		}
		return true;
	}
}
