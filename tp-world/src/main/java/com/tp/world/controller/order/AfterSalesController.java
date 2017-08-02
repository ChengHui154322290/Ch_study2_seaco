package com.tp.world.controller.order;

import javax.servlet.http.HttpServletRequest;

import com.tp.m.base.MResultVO;
import com.tp.m.base.Page;
import com.tp.m.enums.MResultInfo;
import com.tp.m.enums.ValidFieldType;
import com.tp.m.exception.MobileException;
import com.tp.m.query.order.QueryAfterSales;
import com.tp.m.to.cache.TokenCacheTO;
import com.tp.m.util.AssertUtil;
import com.tp.m.util.JsonUtil;
import com.tp.m.vo.order.AfterSalesVO;
import com.tp.world.ao.order.AfterSalesAO;
import com.tp.world.helper.AuthHelper;
import com.tp.world.helper.RequestHelper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 售后控制器
 * @author zhuss
 * @2016年2月25日 下午7:29:02
 */
@Controller
@RequestMapping("/aftersales")
public class AfterSalesController {
	
	private static final Logger log = LoggerFactory.getLogger(AfterSalesController.class);
  
	@Autowired
	private AfterSalesAO afterSalesAO;
	
	@Autowired
	private AuthHelper authHelper;
	
	/**
	 * 售后-申请
	 * @return
	 */
	@RequestMapping(value="/apply",method = RequestMethod.POST)
	@ResponseBody
	public String apply(HttpServletRequest request){
		try{
			String jsonStr = RequestHelper.getJsonStrByIO(request);
			QueryAfterSales afterSales = (QueryAfterSales) JsonUtil.getObjectByJsonStr(jsonStr, QueryAfterSales.class);
			if(log.isInfoEnabled()){
				log.info("[API接口 - 申请售后 入参] = {}",JsonUtil.convertObjToStr(afterSales));
			}
			AssertUtil.notBlank(afterSales.getReturncount(), MResultInfo.RETURN_COUNT_NULL);
			AssertUtil.notBlank(afterSales.getReturnreason(), MResultInfo.RETURN_REASON_NULL);
			AssertUtil.notBlank(afterSales.getOrdercode(), MResultInfo.ORDER_CODE_NULL);
			AssertUtil.notBlank(afterSales.getLineid(), MResultInfo.ITEM_NULL);
			AssertUtil.notBlank(afterSales.getLinkname(), MResultInfo.RETURN_LINKNAME_NULL);
			AssertUtil.notValid(afterSales.getLinktel(), ValidFieldType.TELEPHONE);
			TokenCacheTO usr = authHelper.authToken(afterSales.getToken());
			afterSales.setUserid(usr.getUid());
			afterSales.setLoginname(usr.getTel());
			MResultVO<MResultInfo> result = afterSalesAO.apply(afterSales);
			if(log.isInfoEnabled()){
				log.info("[API接口 - 申请售后 返回值] = {}",JsonUtil.convertObjToStr(result));
			}
			return JsonUtil.convertObjToStr(result);
		}catch(MobileException me){
			log.error("[API接口 - 申请售后  MobileException] = {}",me.getMessage());
			log.error("[API接口 - 申请售后 返回值] = {}",JsonUtil.convertObjToStr(new MResultVO<>(me)));
			return JsonUtil.convertObjToStr(new MResultVO<>(me));
		}
	}
	
	/**
	 * 修改售后单
	 * @return
	 */
	/*@RequestMapping(value="/update",method = RequestMethod.POST)
	@ResponseBody
	public String update(HttpServletRequest request){
		try{
			String jsonStr = RequestHelper.getJsonStrByIO(request);
			QueryAfterSales afterSales = (QueryAfterSales) JsonUtil.getObjectByJsonStr(jsonStr, QueryAfterSales.class);
			if(log.isInfoEnabled()){
				log.info("[API接口 - 修改售后单 入参] = {}",JsonUtil.convertObjToStr(afterSales));
			}
			AssertUtil.notBlank(afterSales.getAsid(), MResultInfo.RETURN_ID_NULL);
			TokenCache usr = authHelper.authToken(afterSales.getToken());
			afterSales.setUserid(usr.getUid());
			MResultVO<MResultInfo> result = afterSalesAO.update(afterSales);
			if(log.isInfoEnabled()){
				log.info("[API接口 - 修改售后单 返回值] = {}",JsonUtil.convertObjToStr(result));
			}
			return JsonUtil.convertObjToStr(result);
		}catch(MobileException me){
			log.error("[API接口 - 修改售后单  MobileException] = {}",me.getMessage());
			log.error("[API接口 - 修改售后单返回值] = {}",JsonUtil.convertObjToStr(new MResultVO<>(me)));
			return JsonUtil.convertObjToStr(new MResultVO<>(me));
		}
	}*/
	
	/**
	 * 售后列表及详情
	 * @return
	 */
	@RequestMapping(value="/list2detail",method = RequestMethod.POST)
	@ResponseBody
	public String list2Detail(HttpServletRequest request){
		try{
			String jsonStr = RequestHelper.getJsonStrByIO(request);
			QueryAfterSales afterSales = (QueryAfterSales) JsonUtil.getObjectByJsonStr(jsonStr, QueryAfterSales.class);
			if(log.isInfoEnabled()){
				log.info("[API接口 - 售后列表及详情 入参] = {}",JsonUtil.convertObjToStr(afterSales));
			}
			TokenCacheTO usr = authHelper.authToken(afterSales.getToken());
			afterSales.setUserid(usr.getUid());
			MResultVO<Page<AfterSalesVO>> result = afterSalesAO.list2Detail(afterSales);
			if(log.isInfoEnabled()){
				log.info("[API接口 - 售后列表及详情 返回值] = {}",JsonUtil.convertObjToStr(result));
			}
			return JsonUtil.convertObjToStr(result);
		}catch(MobileException me){
			log.error("[API接口 - 售后列表及详情  MobileException] = {}",me.getMessage());
			log.error("[API接口 - 售后列表及详情 返回值] = {}",JsonUtil.convertObjToStr(new MResultVO<>(me)));
			return JsonUtil.convertObjToStr(new MResultVO<>(me));
		}
	}
	
	/**
	 * 提交物流单号
	 * @return
	 */
	@RequestMapping(value="/submitlogistic",method = RequestMethod.POST)
	@ResponseBody
	public String submitLogistic(HttpServletRequest request){
		try{
			String jsonStr = RequestHelper.getJsonStrByIO(request);
			QueryAfterSales afterSales = (QueryAfterSales) JsonUtil.getObjectByJsonStr(jsonStr, QueryAfterSales.class);
			if(log.isInfoEnabled()){
				log.info("[API接口 - 提交物流单号 入参] = {}",JsonUtil.convertObjToStr(afterSales));
			}
			AssertUtil.notBlank(afterSales.getAsid(), MResultInfo.RETURN_ID_NULL);
			AssertUtil.notBlank(afterSales.getLogisticcode(), MResultInfo.RETURN_LOGCODE_NULL);
			AssertUtil.notBlank(afterSales.getCompany(), MResultInfo.RETURN_COMPANY_NULL);
			AssertUtil.notBlank(afterSales.getCompanycode(), MResultInfo.RETURN_COMPANY_NULL);
			TokenCacheTO usr = authHelper.authToken(afterSales.getToken());
			afterSales.setUserid(usr.getUid());
			MResultVO<MResultInfo> result = afterSalesAO.submitLogistic(afterSales);
			if(log.isInfoEnabled()){
				log.info("[API接口 - 提交物流单号 返回值] = {}",JsonUtil.convertObjToStr(result));
			}
			return JsonUtil.convertObjToStr(result);
		}catch(MobileException me){
			log.error("[API接口 - 提交物流单号  MobileException] = {}",me.getMessage());
			log.error("[API接口 - 提交物流单号 返回值] = {}",JsonUtil.convertObjToStr(new MResultVO<>(me)));
			return JsonUtil.convertObjToStr(new MResultVO<>(me));
		}
	}
	
	/**
	 * 取消或撤销售后申请
	 * @return
	 */
	@RequestMapping(value="/cancel",method = RequestMethod.POST)
	@ResponseBody
	public String cancel(HttpServletRequest request){
		try{
			String jsonStr = RequestHelper.getJsonStrByIO(request);
			QueryAfterSales afterSales = (QueryAfterSales) JsonUtil.getObjectByJsonStr(jsonStr, QueryAfterSales.class);
			if(log.isInfoEnabled()){
				log.info("[API接口 - 取消售后 入参] = {}",JsonUtil.convertObjToStr(afterSales));
			}
			AssertUtil.notBlank(afterSales.getAsid(), MResultInfo.RETURN_ID_NULL);
			TokenCacheTO usr = authHelper.authToken(afterSales.getToken());
			afterSales.setUserid(usr.getUid());
			MResultVO<MResultInfo> result = afterSalesAO.cancel(afterSales);
			if(log.isInfoEnabled()){
				log.info("[API接口 - 取消售后 返回值] = {}",JsonUtil.convertObjToStr(result));
			}
			return JsonUtil.convertObjToStr(result);
		}catch(MobileException me){
			log.error("[API接口 - 取消售后  MobileException] = {}",me.getMessage());
			log.error("[API接口 - 取消售后 返回值] = {}",JsonUtil.convertObjToStr(new MResultVO<>(me)));
			return JsonUtil.convertObjToStr(new MResultVO<>(me));
		}
	}
}
