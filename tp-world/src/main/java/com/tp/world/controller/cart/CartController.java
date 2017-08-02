package com.tp.world.controller.cart;

import javax.servlet.http.HttpServletRequest;

import com.tp.m.base.MResultVO;
import com.tp.m.enums.CartEnum;
import com.tp.m.enums.MResultInfo;
import com.tp.m.exception.MobileException;
import com.tp.m.query.cart.QueryCart;
import com.tp.m.to.cache.TokenCacheTO;
import com.tp.m.util.AssertUtil;
import com.tp.m.util.JsonUtil;
import com.tp.m.vo.cart.CartSupVO;
import com.tp.m.vo.cart.CartVO;
import com.tp.world.ao.cart.CartAO;
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
 * 购物车控制器
 * @author zhuss
 * @2016年1月7日 上午11:09:49
 */
@Controller
@RequestMapping("/cart")
public class CartController {

	private static final Logger log = LoggerFactory.getLogger(CartController.class);
	
	@Autowired
	private CartAO cartAO;
	
	@Autowired
	private AuthHelper authHelper;
	
	/**
	 * 购物车-添加商品
	 * @return
	 */
	@RequestMapping(value="/add",method = RequestMethod.POST)
	@ResponseBody
	public String add(HttpServletRequest request){
		try{
			String jsonStr = RequestHelper.getJsonStrByIO(request);
			QueryCart cart = (QueryCart) JsonUtil.getObjectByJsonStr(jsonStr, QueryCart.class);
			if(log.isInfoEnabled()){
				log.info("[API接口 - 添加商品进购物车 入参] = {}",JsonUtil.convertObjToStr(cart));
			}
			AssertUtil.notBlank(cart.getSku(), MResultInfo.ITEM_SKU_NULL);
			AssertUtil.notBlank(cart.getTid(), MResultInfo.TOPIC_ID_NULL);
			AssertUtil.notBlank(cart.getCount(), MResultInfo.ITEM_COUNT_NULL);
			AssertUtil.notBlank(cart.getRegcode(), MResultInfo.AREA_ID_NULL);
			TokenCacheTO usr = authHelper.authToken(cart.getToken());
			cart.setUserid(usr.getUid());
			cart.setIp(RequestHelper.getIpAddr(request));
			MResultVO<MResultInfo> result = cartAO.add(cart);
			if(log.isInfoEnabled()){
				log.info("[API接口 - 添加商品进购物车 返回值] = {}",JsonUtil.convertObjToStr(result));
			}
			return JsonUtil.convertObjToStr(result);
		}catch(MobileException me){
			log.error("[API接口 - 添加商品进购物车  MobileException] = {}",me.getMessage());
			log.error("[API接口 - 添加商品进购物车 返回值] = {}",JsonUtil.convertObjToStr(new MResultVO<>(me)));
			return JsonUtil.convertObjToStr(new MResultVO<>(me));
		}
	}
	
	/**
	 * 购物车-角标数量
	 * @return
	 */
	@RequestMapping(value="/supcount",method = RequestMethod.POST)
	@ResponseBody
	public String supCount(HttpServletRequest request){
		try{
			String jsonStr = RequestHelper.getJsonStrByIO(request);
			QueryCart cart = (QueryCart) JsonUtil.getObjectByJsonStr(jsonStr, QueryCart.class);
			if(log.isInfoEnabled()){
				log.info("[API接口 - 购物车角标数量 入参] = {}",JsonUtil.convertObjToStr(cart));
			}
			TokenCacheTO usr = authHelper.authToken(cart.getToken());
			MResultVO<CartSupVO> result = cartAO.supCount(usr.getUid());
			if(log.isInfoEnabled()){
				log.info("[API接口 - 购物车角标数量 返回值] = {}",JsonUtil.convertObjToStr(result));
			}
			return JsonUtil.convertObjToStr(result);
		}catch(MobileException me){
			log.error("[API接口 - 购物车角标数量  MobileException] = {}",me.getMessage());
			log.error("[API接口 - 购物车角标数量 返回值] = {}",JsonUtil.convertObjToStr(new MResultVO<>(me)));
			return JsonUtil.convertObjToStr(new MResultVO<>(me));
		}
	}
	
	/**
	 * 购物车-加载获取商品列表
	 * @return
	 */
	@RequestMapping(value="/load",method = RequestMethod.POST)
	@ResponseBody
	public String load(HttpServletRequest request){
		try{
			String jsonStr = RequestHelper.getJsonStrByIO(request);
			QueryCart cart = (QueryCart) JsonUtil.getObjectByJsonStr(jsonStr, QueryCart.class);
			if(log.isInfoEnabled()){
				log.info("[API接口 - 购物车加载 入参] = {}",JsonUtil.convertObjToStr(cart));
			}
			TokenCacheTO usr = authHelper.authToken(cart.getToken());
			cart.setUserid(usr.getUid());
			cart.setIp(RequestHelper.getIpAddr(request));
			MResultVO<CartVO> result = cartAO.load(cart);
			if(log.isInfoEnabled()){
				log.info("[API接口 - 购物车加载 返回值] = {}",JsonUtil.convertObjToStr(result));
			}
			return JsonUtil.convertObjToStr(result);
		}catch(MobileException me){
			log.error("[API接口 - 购物车加载  MobileException] = {}",me.getMessage());
			log.error("[API接口 - 购物车加载 返回值] = {}",JsonUtil.convertObjToStr(new MResultVO<>(me)));
			return JsonUtil.convertObjToStr(new MResultVO<>(me));
		}
	}
	
	/**
	 * 购物车-操作购物车
	 * @return
	 */
	@RequestMapping(value="/operation",method = RequestMethod.POST)
	@ResponseBody
	public String operation(HttpServletRequest request){
		String operationName = "";
		try{
			String jsonStr = RequestHelper.getJsonStrByIO(request);
			QueryCart cart = (QueryCart) JsonUtil.getObjectByJsonStr(jsonStr, QueryCart.class);
			operationName = CartEnum.CheckType.getDescByCode(cart.getType());
			if(log.isInfoEnabled()){
				log.info("[API接口 - {}购物车 入参] = {}",operationName,JsonUtil.convertObjToStr(cart));
			}
			AssertUtil.notScope(cart.getType(), CartEnum.CheckType.class,MResultInfo.TYPE_ERROR);
			
			/*if(!cart.getType().equals(CartEnum.CheckType.CHECK_ALL.code)&&!cart.getType().equals(CartEnum.CheckType.CHECK_CANCEL_ALL.code)){
				AssertUtil.notEmpty(cart.getProducts(), MResultInfo.ITEM_NULL);
				cart.setTid(cart.getProducts().get(0).getTid());
				cart.setSku(cart.getProducts().get(0).getSku());
			}*/
			TokenCacheTO usr = authHelper.authToken(cart.getToken());
			cart.setUserid(usr.getUid());
			cart.setIp(RequestHelper.getIpAddr(request));
			MResultVO<CartVO> result = cartAO.operation(cart,operationName);
			if(log.isInfoEnabled()){
				log.info("[API接口 - {}购物车 返回值] = {}",CartEnum.CheckType.getDescByCode(cart.getType()),JsonUtil.convertObjToStr(result));
			}
			return JsonUtil.convertObjToStr(result);
		}catch(MobileException me){
			log.error("[API接口 - {}购物车  MobileException] = {}",operationName,me.getMessage());
			log.error("[API接口 - {}购物车 返回值]= {}",operationName,JsonUtil.convertObjToStr(new MResultVO<>(me)));
			return JsonUtil.convertObjToStr(new MResultVO<>(me));
		}
	}
}
