package com.tp.m.controller.order;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tp.m.ao.order.LogisticAO;
import com.tp.m.base.MResultVO;
import com.tp.m.enums.MResultInfo;
import com.tp.m.exception.MobileException;
import com.tp.m.helper.AuthHelper;
import com.tp.m.helper.RequestHelper;
import com.tp.m.query.order.QueryLogistic;
import com.tp.m.to.cache.TokenCacheTO;
import com.tp.m.util.AssertUtil;
import com.tp.m.util.JsonUtil;
import com.tp.m.vo.order.LogisticCompanyVO;
import com.tp.m.vo.order.LogisticVO;

/**
 * 物流控制器
 * @author zhuss
 * @2016年1月7日 下午4:17:02
 */
@Controller
@RequestMapping("/logistic")
public class LogisticController {
	private static final Logger log = LoggerFactory.getLogger(LogisticController.class);

	@Autowired
	private LogisticAO logisticAO;
	
	@Autowired
	private AuthHelper authHelper;
	
	/**
	 * 物流列表
	 * @return
	 */
	@RequestMapping(value="/list",method = RequestMethod.POST)
	@ResponseBody
	public String getOrderList(HttpServletRequest request){
		try{
			String jsonStr = RequestHelper.getJsonStrByIO(request);
			QueryLogistic logistic = (QueryLogistic) JsonUtil.getObjectByJsonStr(jsonStr, QueryLogistic.class);
			if(log.isInfoEnabled()){
				log.info("[API接口 - 订单跟踪 入参] = {}",JsonUtil.convertObjToStr(logistic));
			}
			AssertUtil.notBlank(logistic.getCode(), MResultInfo.ORDER_CODE_NULL);
			TokenCacheTO usr = authHelper.authToken(logistic.getToken());
			logistic.setUserid(usr.getUid());
			MResultVO<LogisticVO> result = logisticAO.getLogisticList(logistic);
			if(log.isInfoEnabled()){
				log.info("[API接口 - 订单跟踪  返回值] = {}",JsonUtil.convertObjToStr(result));
			}
			return JsonUtil.convertObjToStr(result);
		}catch(MobileException me){
			log.error("[API接口 - 订单跟踪   MobileException] = {}",me.getMessage());
			log.error("[API接口 - 订单跟踪  返回值] = {}",JsonUtil.convertObjToStr(new MResultVO<>(me)));
			return JsonUtil.convertObjToStr(new MResultVO<>(me));
		}
	}
	
	/**
	 * 物流公司列表
	 * @return
	 */
	@RequestMapping(value="/companylist",method = RequestMethod.POST)
	@ResponseBody
	public String getlogcompany(HttpServletRequest request){
		try{
			/*String jsonStr = RequestHelper.getJsonStrByIO(request);
			QueryLogistic logistic = (QueryLogistic) JsonUtil.getObjectByJsonStr(jsonStr, QueryLogistic.class);
			if(log.isInfoEnabled()){
				log.info("[API接口 - 订单跟踪 入参] = {}",JsonUtil.convertObjToStr(logistic));
			}*/
			MResultVO<List<LogisticCompanyVO>> result = logisticAO.getLogisticCompany();
			if(log.isInfoEnabled()){
				log.info("[API接口 - 物流公司列表 返回值] = {}",JsonUtil.convertObjToStr(result));
			}
			return JsonUtil.convertObjToStr(result);
		}catch(MobileException me){
			log.error("[API接口 - 订单跟踪   MobileException] = {}",me.getMessage());
			log.error("[API接口 - 订单跟踪  返回值] = {}",JsonUtil.convertObjToStr(new MResultVO<>(me)));
			return JsonUtil.convertObjToStr(new MResultVO<>(me));
		}
	}
}
