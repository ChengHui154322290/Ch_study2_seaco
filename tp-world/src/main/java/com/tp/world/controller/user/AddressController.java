package com.tp.world.controller.user;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tp.m.base.MResultVO;
import com.tp.m.enums.MResultInfo;
import com.tp.m.enums.ValidFieldType;
import com.tp.m.exception.MobileException;
import com.tp.m.query.user.QueryAddress;
import com.tp.m.query.user.QueryUser;
import com.tp.m.to.cache.TokenCacheTO;
import com.tp.m.util.AssertUtil;
import com.tp.m.util.JsonUtil;
import com.tp.m.util.StringUtil;
import com.tp.m.vo.user.AddressDetailVO;
import com.tp.world.ao.user.AddressAO;
import com.tp.world.helper.AuthHelper;
import com.tp.world.helper.RequestHelper;


/**
 * 用户 - 收货地址控制器
 * @author zhuss
 * @2016年1月3日 下午2:01:38
 */
@Controller
@RequestMapping("/address")
public class AddressController {

	private static final Logger log = LoggerFactory.getLogger(AddressController.class);
	
	@Autowired
	private AddressAO addressAO;
	
	@Autowired
	private AuthHelper authHelper;
	
	/**
	 * 用户-收货地址-列表
	 * @return
	 */
	@RequestMapping(value="/list",method = RequestMethod.POST)
	@ResponseBody
	public String getAddressList(HttpServletRequest request){
		try{
			String jsonStr = RequestHelper.getJsonStrByIO(request);
			QueryUser userTO = (QueryUser) JsonUtil.getObjectByJsonStr(jsonStr, QueryUser.class);
			if(log.isInfoEnabled()){
				log.info("[API接口 - 用户收货地址列表 入参] = {}",JsonUtil.convertObjToStr(userTO));
			}
			TokenCacheTO usr = authHelper.authToken(userTO.getToken());
			MResultVO<List<AddressDetailVO>> result = addressAO.getAddressList(usr.getUid());
			if(log.isInfoEnabled()){
				log.info("[API接口 - 用户收货地址列表 返回值] = {}",JsonUtil.convertObjToStr(result));
			}
			return JsonUtil.convertObjToStr(result);
		}catch(MobileException me){
			log.error("[API接口 - 用户收货地址列表  MobileException] = {}",me.getMessage());
			log.error("[API接口 - 用户收货地址列表  返回值] = {}",JsonUtil.convertObjToStr(new MResultVO<>(me)));
			return JsonUtil.convertObjToStr(new MResultVO<>(me));
		}
	}
	
	/**
	 * 用户-默认收货地址
	 * @return
	 */
	@RequestMapping(value="/default",method = RequestMethod.POST)
	@ResponseBody
	public String getDefaultAddress(HttpServletRequest request){
		try{
			String jsonStr = RequestHelper.getJsonStrByIO(request);
			QueryUser userTO = (QueryUser) JsonUtil.getObjectByJsonStr(jsonStr, QueryUser.class);
			if(log.isInfoEnabled()){
				log.info("[API接口 - 用户默认收货地址 入参] = {}",JsonUtil.convertObjToStr(userTO));
			}
			TokenCacheTO usr = authHelper.authToken(userTO.getToken());
			MResultVO<AddressDetailVO> result = addressAO.defaultAddress(usr.getUid());
			if(log.isInfoEnabled()){
				log.info("[API接口 - 用户默认收货地址 返回值] = {}",JsonUtil.convertObjToStr(result));
			}
			return JsonUtil.convertObjToStr(result);
		}catch(MobileException me){
			log.error("[API接口 - 用户默认收货地址  MobileException] = {}",me.getMessage());
			log.error("[API接口 - 用户默认收货地址  返回值] = {}",JsonUtil.convertObjToStr(new MResultVO<>(me)));
			return JsonUtil.convertObjToStr(new MResultVO<>(me));
		}
	}
	
	/**
	 * 用户-收货地址-新增
	 * @return
	 */
	@RequestMapping(value="/add",method = RequestMethod.POST)
	@ResponseBody
	public String addAddress(HttpServletRequest request){
		try{
			String jsonStr = RequestHelper.getJsonStrByIO(request);
			QueryAddress address = (QueryAddress) JsonUtil.getObjectByJsonStr(jsonStr, QueryAddress.class);
			if(log.isInfoEnabled()){
				log.info("[API接口 - 新增用户收货地址 入参] = {}",JsonUtil.convertObjToStr(address));
			}
			AssertUtil.notBlank(address.getName(), MResultInfo.ADDRESS_NAME_NULL);
			AssertUtil.notBlank(address.getProvid(), MResultInfo.ADDRESS_PROV_NULL);
			AssertUtil.notBlank(address.getCityid(), MResultInfo.ADDRESS_CITY_NULL);
			AssertUtil.notBlank(address.getInfo(), MResultInfo.ADDRESS_INFO_NULL);
			AssertUtil.notValid(address.getTel(), ValidFieldType.TELEPHONE);
			TokenCacheTO usr = authHelper.authToken(address.getToken());
			address.setUserid(usr.getUid());
			MResultVO<AddressDetailVO> result = addressAO.modifyAddress(address);
			if(log.isInfoEnabled()){
				log.info("[API接口 - 新增用户收货地址 返回值] = {}",JsonUtil.convertObjToStr(result));
			}
			return JsonUtil.convertObjToStr(result);
		}catch(MobileException me){
			log.error("[API接口 - 新增用户收货地址  MobileException] = {}",me.getMessage());
			log.error("[API接口 - 新增用户收货地址  返回值] = {}",JsonUtil.convertObjToStr(new MResultVO<>(me)));
			return JsonUtil.convertObjToStr(new MResultVO<>(me));
		}
	}
	
	/**
	 * 用户-收货地址-编辑
	 * @return
	 */
	@RequestMapping(value="/edit",method = RequestMethod.POST)
	@ResponseBody
	public String editAddress(HttpServletRequest request){
		try{
			String jsonStr = RequestHelper.getJsonStrByIO(request);
			QueryAddress address = (QueryAddress) JsonUtil.getObjectByJsonStr(jsonStr, QueryAddress.class);
			if(log.isInfoEnabled()){
				log.info("[API接口 - 编辑用户收货地址 入参] = {}",JsonUtil.convertObjToStr(address));
			}
			AssertUtil.notBlank(address.getAid(), MResultInfo.ADDRESS_ID_NULL);
			TokenCacheTO usr = authHelper.authToken(address.getToken());
			address.setUserid(usr.getUid());
			MResultVO<AddressDetailVO> result = addressAO.modifyAddress(address);
			if(log.isInfoEnabled()){
				log.info("[API接口 - 编辑用户收货地址 返回值] = {}",JsonUtil.convertObjToStr(result));
			}
			return JsonUtil.convertObjToStr(result);
		}catch(MobileException me){
			log.error("[API接口 - 编辑用户收货地址  MobileException] = {}",me);
			log.error("[API接口 - 编辑用户收货地址  返回值] = {}",JsonUtil.convertObjToStr(new MResultVO<>(me)));
			return JsonUtil.convertObjToStr(new MResultVO<>(me));
		}
	}
	
	/**
	 * 用户-收货地址-删除
	 * @return
	 */
	@RequestMapping(value="/del",method = RequestMethod.POST)
	@ResponseBody
	public String delAddress(HttpServletRequest request){
		try{
			String jsonStr = RequestHelper.getJsonStrByIO(request);
			QueryAddress address = (QueryAddress) JsonUtil.getObjectByJsonStr(jsonStr, QueryAddress.class);
			if(log.isInfoEnabled()){
				log.info("[API接口 - 删除用户收货地址 入参] = {}",JsonUtil.convertObjToStr(address));
			}
			AssertUtil.notBlank(address.getAid(), MResultInfo.ADDRESS_ID_NULL);
			TokenCacheTO usr = authHelper.authToken(address.getToken());
			MResultVO<MResultInfo> result = addressAO.delAddress(StringUtil.getLongByStr(address.getAid()),usr.getUid());
			if(log.isInfoEnabled()){
				log.info("[API接口 - 删除用户收货地址 返回值] = {}",JsonUtil.convertObjToStr(result));
			}
			return JsonUtil.convertObjToStr(result);
		}catch(MobileException me){
			log.error("[API接口 - 删除用户收货地址  MobileException] = {}",me.getMessage());
			log.error("[API接口 - 删除用户收货地址   返回值] = {}",JsonUtil.convertObjToStr(new MResultVO<>(me)));
			return JsonUtil.convertObjToStr(new MResultVO<>(me));
		}
	}
}
