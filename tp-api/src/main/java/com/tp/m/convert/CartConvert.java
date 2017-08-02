package com.tp.m.convert;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;

import com.tp.dto.ord.CartDTO;
import com.tp.dto.ord.CartLineDTO;
import com.tp.m.enums.CartEnum;
import com.tp.m.enums.ImgEnum;
import com.tp.m.helper.ImgHelper;
import com.tp.m.helper.SwitchHelper;
import com.tp.m.query.cart.QueryCart;
import com.tp.m.to.product.ParamProductTO;
import com.tp.m.util.NumberUtil;
import com.tp.m.util.StringUtil;
import com.tp.m.vo.cart.CartVO;
import com.tp.m.vo.product.ProductVO;

/**
 * 购物车封装类
 * @author zhuss
 * @2016年1月11日 下午5:23:35
 */
public class CartConvert {
	
	/**
	 * 封装添加购物车入参对象
	 * @param cart
	 * @return
	 */
	public static CartLineDTO convertAddCart(QueryCart cart){
		CartLineDTO cartline = new CartLineDTO();
		cartline.setTopicId(StringUtil.getLongByStr(cart.getTid()));
		cartline.setAreaId(StringUtil.getLongByStr(cart.getRegcode()));
		cartline.setSkuCode(cart.getSku());
		cartline.setQuantity(StringUtil.getIntegerByStr(cart.getCount()));
		return cartline;
	}
	
	
	
	/**
	 * 封装操作购物车入参对象
	 * @param cart
	 * @return
	 */
	public static List<CartLineDTO> convertOptCart(QueryCart cart){
		List<CartLineDTO> cl = new ArrayList<>();
		if(CollectionUtils.isNotEmpty(cart.getProducts())){
			for(ParamProductTO pp : cart.getProducts()){
				CartLineDTO cartline = new CartLineDTO();
				cartline.setTopicId(StringUtil.getLongByStr(pp.getTid()));
				cartline.setAreaId(StringUtil.getLongByStr(cart.getRegcode()));
				cartline.setSkuCode(pp.getSku());
				cartline.setQuantity(StringUtil.getIntegerByStr(pp.getCount()));
				cl.add(cartline);
			}
		}
		return cl;
	}
	
	/**
	 * 封装购物车
	 * @return
	 */
	public static CartVO convertCart(CartDTO cartDto){
		CartVO vo = new CartVO();
		if(null !=cartDto){
			vo.setTotalprice(NumberUtil.sfwr(cartDto.getRealSum()));
			if (null != cartDto.getOriginalSum() && 0 != cartDto.getOriginalSum())
				vo.setDisprice(NumberUtil.sfwr(NumberUtil.sub(cartDto.getOriginalSum(), cartDto.getRealSum())));
			vo.setProducts(convertCartLines(cartDto.getSeaMap()));
		}
		return vo;
	}
	
	/**
	 * 封装购物车商品行
	 * @param seaMap
	 * @return
	 */
	public static List<ProductVO> convertCartLines(Map<String, List<CartLineDTO>> seaMap){
		List<ProductVO> productinfo = new ArrayList<>();
		if(null != seaMap){
			for (Map.Entry<String, List<CartLineDTO>> entry : seaMap.entrySet()) {
				for (CartLineDTO cdl : entry.getValue()) {
					productinfo.add(convertCartLine(cdl,true));
				}
			}
		}
		return productinfo;
	}
	
	/**
	 * 封装购物车商品对象
	 * @param prd
	 * @return
	 */
	public static ProductVO convertCartLine(CartLineDTO cdl,boolean isCart){
		ProductVO item = new ProductVO();
		if(null != cdl){
			if(isCart){
				item.setSelected(cdl.getSelected()!=null &&cdl.getSelected()?StringUtil.ONE:StringUtil.ZERO);
				/*item.setChannel(cdl.getSeaChannelName());
				item.setChannelid(StringUtil.getStrByObj(cdl.getSeaChannel()));*/
			}
			item.setStock(StringUtil.getStrByObj(cdl.getStock()));
			item.setName(cdl.getItemName());
			item.setImgurl(cdl.getItemPic());
			item.setOldprice(NumberUtil.sfwr(cdl.getListPrice()));
			item.setPrice(NumberUtil.sfwr(cdl.getSalePrice()));
			item.setLineprice(NumberUtil.sfwr(cdl.getSubTotal()));
			item.setDiscount(StringUtil.getStrByObj(NumberUtil.sub(StringUtil.getDoubleByStr(item.getPrice()), StringUtil.getDoubleByStr(item.getOldprice()))));
			item.setTid(StringUtil.getStrByObj(cdl.getTopicId()));
			item.setSku(cdl.getSkuCode());
			item.setStatus(CartEnum.CartLineStatus.getStatusByPC(cdl.getStatus()).appcode);
			item.setStatusdesc(CartEnum.CartLineStatus.getStatusByPC(cdl.getStatus()).desc);
			if(SwitchHelper.isShowRate())item.setTaxrate(NumberUtil.sfwr(cdl.getTarrifRate()));
			item.setCount(StringUtil.getStrByObj(cdl.getQuantity()));
			if(null != cdl.getTopicItemInfo()){
				item.setImgurl(ImgHelper.getImgUrl(cdl.getTopicItemInfo().getTopicImage(), ImgEnum.Width.WIDTH_180));
				item.setLimitcount(StringUtil.getStrByObj(cdl.getTopicItemInfo().getLimitAmount()));
			}
		}
		return item;
	}
}
