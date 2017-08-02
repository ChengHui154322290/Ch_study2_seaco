package com.tp.world.ao.cart;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.vo.Constant;
import com.tp.dto.common.ResultInfo;
import com.tp.dto.ord.ShoppingCartDto;
import com.tp.enums.common.PlatformEnum;
import com.tp.m.base.MResultVO;
import com.tp.m.enums.CartEnum;
import com.tp.m.enums.MResultInfo;
import com.tp.m.exception.MobileException;
import com.tp.m.query.cart.QueryCart;
import com.tp.m.util.JsonUtil;
import com.tp.m.util.StringUtil;
import com.tp.m.vo.cart.CartSupVO;
import com.tp.m.vo.cart.CartVO;
import com.tp.model.ord.CartItemInfo;
import com.tp.proxy.ord.CartProxy;
import com.tp.world.convert.CartConvert;
import com.tp.world.convert.ShoppingCartConvert;
import com.tp.world.helper.RequestHelper;

/**
 * 购物车业务层
 * @author zhuss
 * @2016年1月7日 上午11:13:09
 */
@Service
public class CartAO {
	private static final Logger log = LoggerFactory.getLogger(CartAO.class);
	@Autowired
	private CartProxy cartProxy;
	
	/**
	 * 购物车-添加商品
	 * @param cart
	 * @return
	 */
	public MResultVO<MResultInfo> add(QueryCart cart){
		try{
			log.info("[调用Service接口 - 添加购物车 入参] 商品信息= {}",JsonUtil.convertObjToStr(CartConvert.convertAddCart(cart)));
			log.info("[调用Service接口 - 添加购物车 入参] user = {} 平台= {}", cart.getUserid(), RequestHelper.getPlatformByName(cart.getApptype()).code);
			//ResultInfo<CartLineSimpleDTO> cl = cartInfoProxy.addToCart(CartConvert.convertAddCart(cart), cart.getUserid(), cart.getIp(), RequestHelper.getPlatformByName(cart.getApptype()).code);
			ResultInfo<Integer> resultInfo = cartProxy.insertCartItemInfo(ShoppingCartConvert.convertAddCart(cart));
			if(resultInfo.isSuccess()){
				if(null != resultInfo.getData())if(null != resultInfo.getData() && resultInfo.getData() == 0)return new MResultVO<>(MResultInfo.ADD_FAILED);
				return new MResultVO<>(MResultInfo.ADD_SUCCESS);
			}
			log.error("[调用Service接口 - 购物车添加商品(addToCart) Failed] = {}",resultInfo.getMsg().toString());
			return new MResultVO<>(resultInfo.getMsg().getMessage());
		}catch (MobileException e) {
			log.error("[API接口 - 添加商品进购物车  MobileException]={}", e.getMessage());
			return new MResultVO<>(e);
		}catch(Exception ex){
			log.error("[API接口 - 添加商品进购物车 Exception] = {}",ex);
			return new MResultVO<>(MResultInfo.SYSTEM_ERROR);
		}
	}
	
	/**
	 * 购物车-获取角标数量
	 * @param cart
	 * @return
	 */
	public MResultVO<CartSupVO> supCount(Long userid){
		try{
			//ResultInfo<Integer> count = cartInfoProxy.getCartQuantity(userid);
			ResultInfo<Integer> count = cartProxy.queryQuantityCountByMemberId(userid,0L);
			if(count.isSuccess()){
				return new MResultVO<>(MResultInfo.SUCCESS,new CartSupVO(StringUtil.getStrByObj(count.getData())));
			}
			log.error("[调用Service接口 - 购物车获取角标数量(getCartQuantity) Failed] = {}",count.getMsg().toString());
			return new MResultVO<>(count.getMsg().getMessage());
		}catch (MobileException e) {
			log.error("[API接口 - 购物车获取角标数量  MobileException]={}", e.getMessage());
			return new MResultVO<>(e);
		}catch(Exception ex){
			log.error("[API接口 - 获取购物车角标数量 Exception] = {}",ex);
			return new MResultVO<>(MResultInfo.SYSTEM_ERROR);
		}
	}
	
	/**
	 * 购物车 - 加载
	 * @param cart
	 * @return
	 */
	public MResultVO<CartVO> load(QueryCart cart) {
		try {
			//ResultInfo<CartDTO> cartDto = cartInfoProxy.findMemberCart(cart.getUserid(), CartConstant.TYPE_SEA);
			ResultInfo<ShoppingCartDto> cartDto = cartProxy.queryCartByMemberId(cart.getUserid(), cart.getIp(), StringUtil.getLongByStr(cart.getRegcode()), PlatformEnum.getCodeByName(cart.getApptype()), cart.getToken(),0L);
			if(cartDto.isSuccess()){
				return new MResultVO<>(MResultInfo.SUCCESS,ShoppingCartConvert.convertCart(cartDto.getData(),cart));
			}
			log.error("[调用Service接口 - 购物车加载(findMemberCart) Failed] = {}",cartDto.getMsg().toString());
			return new MResultVO<>(cartDto.getMsg().getMessage());
		}catch (MobileException e) {
			log.error("[API接口 - 购物车加载 MobileException]={}", e.getMessage());
			return new MResultVO<>(e);
		} catch (Exception e) {
			log.error("[API接口 - 购物车加载  Exception] = {}", e);
			return new MResultVO<>(MResultInfo.CONN_ERROR);
		}
	}
	
	/**
	 * 购物车 - 检查操作获取金额
	 * @param cart
	 * @return
	 */
	public MResultVO<CartVO> operation(QueryCart cart,String operationName) {
		try {
			ResultInfo<ShoppingCartDto> result = new ResultInfo<ShoppingCartDto>();
			CartItemInfo cartItemInfo = ShoppingCartConvert.convertAddCart(cart);
			if(cart.getType().equals(CartEnum.CheckType.CHECK_ALL.code)){//全选
				result = cartProxy.selectAllCartItem(cart.getUserid(),cart.getToken(),Boolean.TRUE,0L);
			}else if(cart.getType().equals(CartEnum.CheckType.CHECK_CANCEL_ALL.code)){//取消全选
				result = cartProxy.selectAllCartItem(cart.getUserid(),cart.getToken(),Boolean.FALSE,0L);
			}else if(cart.getType().equals(CartEnum.CheckType.CHECK_RADIO.code)){//单选
				cartItemInfo.setSelected(Constant.SELECTED.YES);
				result = cartProxy.selectCartItem(cartItemInfo);
			}else if(cart.getType().equals(CartEnum.CheckType.CHECK_CANCEL_RADIO.code)){//取消单选
				cartItemInfo.setSelected(Constant.SELECTED.NO);
				result = cartProxy.selectCartItem(cartItemInfo);
			}else if(cart.getType().equals(CartEnum.CheckType.CHECK_MODIFY.code)){//更新数量
				//cartItemInfo.setQuantity(StringUtil.getIntegerByStr(cart.getProducts().get(0).getCount()));
				result = cartProxy.refreshItem(cartItemInfo);
			}else if(cart.getType().equals(CartEnum.CheckType.CHECK_DEL.code)){//删除
				result = cartProxy.deleteCartItem(cartItemInfo);
			}else if(cart.getType().equals(CartEnum.CheckType.CHECK_SUBMIT.code)){//结算校验
				result = cartProxy.operateTotal(cart.getUserid(), cart.getToken(),0L);
			}
			if(result.isSuccess()){
				return new MResultVO<>(MResultInfo.SUCCESS,ShoppingCartConvert.convertCart(result.getData(),cart));
			}
			log.error("[调用Service接口 - {}购物车 Failed] ={}",operationName,result.getMsg().toString());
			return new MResultVO<>(result.getMsg().getMessage(),load(cart).getData());
		} catch (MobileException e) {
			log.error("[API接口 - {}购物车 MobileException]={}",operationName, e.getMessage());
			return new MResultVO<>(e);
		}catch (Exception e) {
			log.error("[API接口 - {}购物车  Exception] = {}",operationName, e);
			return new MResultVO<>(MResultInfo.SYSTEM_ERROR);
		}
	}
}
