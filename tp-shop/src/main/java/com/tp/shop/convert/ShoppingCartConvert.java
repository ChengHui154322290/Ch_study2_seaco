package com.tp.shop.convert;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;

import com.tp.common.util.ImageDownUtil;
import com.tp.common.vo.Constant;
import com.tp.common.vo.OrderConstant;
import com.tp.dto.ord.PreOrderDto;
import com.tp.dto.ord.ShoppingCartDto;
import com.tp.enums.common.PlatformEnum;
import com.tp.m.base.BaseQuery;
import com.tp.m.enums.CartEnum;
import com.tp.m.enums.ImgEnum;
import com.tp.m.query.cart.QueryCart;
import com.tp.m.to.product.ParamProductTO;
import com.tp.m.util.NumberUtil;
import com.tp.m.util.StringUtil;
import com.tp.m.vo.cart.CartVO;
import com.tp.m.vo.product.ProductVO;
import com.tp.m.vo.product.ProductWithWarehouseVO;
import com.tp.model.ord.CartItemInfo;
import com.tp.model.ord.OrderItem;
import com.tp.shop.helper.ImgHelper;
import com.tp.shop.helper.SwitchHelper;
import com.tp.shop.helper.VersionHelper;

public class ShoppingCartConvert {
	/**
	 * 封装添加购物车入参对象
	 * @param cart
	 * @return
	 */
	public static CartItemInfo convertAddCart(QueryCart cart){
		CartItemInfo cartItemInfo = new CartItemInfo();
		cartItemInfo.setTopicId(StringUtil.getLongByStr(cart.getTid()));
		cartItemInfo.setAreaId(StringUtil.getLongByStr(cart.getRegcode()));
		cartItemInfo.setSkuCode(cart.getSku());
		cartItemInfo.setQuantity(StringUtil.getIntegerByStr(cart.getCount()));
		cartItemInfo.setMemberId(cart.getUserid());
		cartItemInfo.setPlatformId(PlatformEnum.getCodeByName(cart.getApptype()));
		cartItemInfo.setToken(cart.getToken());
		cartItemInfo.setShopId(cart.getShopId());
		//兼容老版本
		if(CollectionUtils.isNotEmpty(cart.getProducts())){
			ParamProductTO pp = cart.getProducts().get(0);
			cartItemInfo.setTopicId(StringUtil.getLongByStr(pp.getTid()));
			cartItemInfo.setSkuCode(pp.getSku());
			cartItemInfo.setQuantity(StringUtil.getIntegerByStr(pp.getCount()));
		}
		return cartItemInfo;
	}
	
	/**
	 * 封装购物车
	 * @return
	 */
	public static CartVO convertCart(ShoppingCartDto cartDto,BaseQuery base){
		boolean handleOldVersion = VersionHelper.before120Version(base);
		CartVO vo = new CartVO();
		if(null !=cartDto){
			vo.setTotalprice(NumberUtil.sfwr(cartDto.getSummation()));
			if (null != cartDto.getPayableAmount() && 0 != cartDto.getPayableAmount())
				vo.setDisprice(NumberUtil.sfwr(cartDto.getDiscountTotal()));
			List<ProductVO> invaliditems = convertCartLines(cartDto.getDeleteCartItemList());
			if(handleOldVersion){
				vo.setProducts(convertOldOrderList(cartDto.getPreSubOrderList())); //旧版本数据
				if(CollectionUtils.isNotEmpty(invaliditems))vo.getProducts().addAll(invaliditems);
			}else{
				vo.setInvaliditems(invaliditems);//失效商品列表
				vo.setMergeitems(convertProductWithWarehouseList(cartDto.getPreSubOrderList(),true));//合并数据
			}
		}
		return vo;
	}
	
	/**
	 * 封装旧版本购物车商品行
	 * @param seaMap
	 * @return
	 */
	public static List<ProductVO> convertOldOrderList(List<PreOrderDto> subOrderList){
		List<ProductVO> productinfo = new ArrayList<>();
		if(CollectionUtils.isNotEmpty(subOrderList)){
			for (PreOrderDto subOrder : subOrderList) {
				List<OrderItem> orderItemList = subOrder.getOrderItemList();
				for(OrderItem orderItem:orderItemList){
					productinfo.add(convertCartLine(orderItem,true));
				}
			}
		}
		return productinfo;
	}

	/**
	 * 普通封装购物车商品行
	 * @param seaMap
	 * @return
	 */
	public static List<ProductVO> convertCartLines(List<CartItemInfo> cartItemInfoList){
		List<ProductVO> productinfo = new ArrayList<>();
		if(CollectionUtils.isNotEmpty(cartItemInfoList)){
			for (CartItemInfo cartItemInfo : cartItemInfoList) {
				productinfo.add(convertCartLine(cartItemInfo,true));
			}
		}
		return productinfo;
	}
	
	/**
	 * 封装购物车商品对象
	 * @param prd
	 * @return
	 */
	public static ProductVO convertCartLine(CartItemInfo cartItemInfo,boolean isCart){
		ProductVO item = new ProductVO();
		if(null != cartItemInfo){
			if(isCart){
				item.setSelected(Constant.SELECTED.YES.equals(cartItemInfo.getSelected())?StringUtil.ONE:StringUtil.ZERO);
				/*item.setChannel(cdl.getSeaChannelName());
				item.setChannelid(StringUtil.getStrByObj(cdl.getSeaChannel()));*/
			}
			item.setStock(StringUtil.getStrByObj(cartItemInfo.getStock()));
			item.setName(cartItemInfo.getItemName());
			String img = ImageDownUtil.getOriginalImage(Constant.IMAGE_URL_TYPE.item.url, cartItemInfo.getItemPic());
			item.setImgurl(ImgHelper.getImgUrl(img, ImgEnum.Width.WIDTH_180));
			item.setOldprice(NumberUtil.sfwr(cartItemInfo.getListPrice()));
			item.setPrice(NumberUtil.sfwr(cartItemInfo.getSalePrice()));
			item.setLineprice(NumberUtil.sfwr(null!=cartItemInfo.getSubTotal()?cartItemInfo.getSubTotal():0d));
			item.setDiscount(StringUtil.getStrByObj(NumberUtil.sub(StringUtil.getDoubleByStr(item.getPrice()), StringUtil.getDoubleByStr(item.getOldprice()))));
			item.setTid(StringUtil.getStrByObj(cartItemInfo.getTopicId()));
			item.setSku(cartItemInfo.getSkuCode());
			item.setStatus(CartEnum.CartLineStatus.getStatusByPC(cartItemInfo.getStatus()).appcode);
			item.setStatusdesc(CartEnum.CartLineStatus.getStatusByPC(cartItemInfo.getStatus()).desc);
			if(SwitchHelper.isShowRate())item.setTaxrate(NumberUtil.sfwr(cartItemInfo.getTarrifRate()));
			item.setCount(StringUtil.getStrByObj(cartItemInfo.getQuantity()));
			if(null != cartItemInfo.getTopicItem()){
				//item.setImgurl(ImgHelper.getImgUrl(cartItemInfo.getTopicItem().getTopicImage(), ImgEnum.Width.WIDTH_180));
				item.setLimitcount(StringUtil.getStrByObj(cartItemInfo.getTopicItem().getLimitAmount()));
			}
		}
		return item;
	}
	
	/**
	 * 封装购物车商品对象
	 * @param prd
	 * @return
	 */
	public static ProductVO convertCartLine(OrderItem orderItem,boolean isCart){
		ProductVO item = new ProductVO();
		if(isCart){
			item.setSelected(Constant.SELECTED.YES.equals(orderItem.getSelected())?StringUtil.ONE:StringUtil.ZERO);
		}
		item.setStock(StringUtil.getStrByObj(orderItem.getStock()));
		item.setName(orderItem.getSpuName());
		String img = ImageDownUtil.getOriginalImage(Constant.IMAGE_URL_TYPE.item.url, orderItem.getImg());
		item.setImgurl(ImgHelper.getImgUrl(img, ImgEnum.Width.WIDTH_180));
		item.setOldprice(NumberUtil.sfwr(orderItem.getListPrice()));
		item.setPrice(NumberUtil.sfwr(orderItem.getPrice()));
		item.setLineprice(NumberUtil.sfwr(null!=orderItem.getItemAmount()?orderItem.getItemAmount():0d));
		item.setDiscount(StringUtil.getStrByObj(NumberUtil.sub(StringUtil.getDoubleByStr(item.getPrice()), StringUtil.getDoubleByStr(item.getOldprice()))));
		item.setTid(StringUtil.getStrByObj(orderItem.getTopicId()));
		item.setSku(orderItem.getSkuCode());
		item.setStatus(CartEnum.CartLineStatus.getStatusByPC(orderItem.getStatus()).appcode);
		item.setStatusdesc(CartEnum.CartLineStatus.getStatusByPC(orderItem.getStatus()).desc);
		if(SwitchHelper.isShowRate())item.setTaxrate(NumberUtil.sfwr(orderItem.getTaxRate()));
		item.setCount(StringUtil.getStrByObj(orderItem.getQuantity()));
		item.setLimitcount(StringUtil.getStrByObj(orderItem.getLimitCount()));
		return item;
	}
	
	/**
	 * 封装拆分的商品信息集合
	 * @param seaorderitem
	 * @return
	 */
	public static List<ProductWithWarehouseVO> convertProductWithWarehouseList(List<PreOrderDto> subOrderList,boolean isCart){
		List<ProductWithWarehouseVO> productinfo = new ArrayList<>();
		if(CollectionUtils.isNotEmpty(subOrderList)){
			for(PreOrderDto subOrder:subOrderList){
				productinfo.add(convertProductWithWarehouse(subOrder,isCart));
			}
		}
		return productinfo;
	}
	
	/**
	 * 封装拆分的商品信息
	 * @param warhouse
	 * @return
	 */
	public static ProductWithWarehouseVO convertProductWithWarehouse(PreOrderDto subOrder,boolean isCart){
		ProductWithWarehouseVO pw = new ProductWithWarehouseVO();
		if(null != subOrder){
			if(!isCart){
				pw.setFreight(NumberUtil.sfwr(subOrder.getFreight()));
				pw.setItemsprice(NumberUtil.sfwr(subOrder.getItemTotal()));
				pw.setStoragetype(StringUtil.getStrByObj(subOrder.getStorageType()));
			}
			pw.setChannel(subOrder.getSeaChannelName());
			pw.setChannelcode(String.valueOf(subOrder.getSeaChannel()));
			//编码为空--->国内直发
			if(subOrder.getSeaChannel()==null || StringUtil.isBlank(subOrder.getSeaChannelName())){
				pw.setChannelcode("GNZF");
				pw.setChannel(OrderConstant.OrderType.DOMESTIC.cnName);
			}
			pw.setFreight(NumberUtil.sfwr(subOrder.getFreight()));
			pw.setPrice(NumberUtil.sfwr(subOrder.getTotal()));
			pw.setTaxes(NumberUtil.sfwr(subOrder.getTaxFee()));
			pw.setWarehouseid(StringUtil.getStrByObj(subOrder.getWarehouseId()));
			pw.setWarehousename(pw.getChannel());
			pw.setItemsprice(NumberUtil.sfwr(subOrder.getItemTotal()));
			pw.setStoragetype(StringUtil.getStrByObj(subOrder.getStorageType()));
			//海外直邮  税率免费
			if(StringUtil.equals(subOrder.getStorageType(), OrderConstant.OrderType.OVERSEASMAIL.code)){
				pw.setIsfreetax(StringUtil.ONE);
			}else pw.setIsfreetax(StringUtil.ZERO);
			List<ProductVO> products = new ArrayList<>();
			List<OrderItem> orderItemList = subOrder.getOrderItemList();
			if(CollectionUtils.isNotEmpty(orderItemList)){
				for(OrderItem cl : orderItemList)
				products.add(convertCartLine(cl,isCart));
			}
			pw.setProducts(products);
		}
		return pw;
	}
}
