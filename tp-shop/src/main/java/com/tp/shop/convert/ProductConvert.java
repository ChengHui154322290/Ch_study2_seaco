package com.tp.shop.convert;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import com.alibaba.fastjson.JSONArray;
import com.tp.common.vo.prd.DummyItemConstant;
import com.tp.common.vo.prd.ItemComonConstant;
import com.tp.dto.mmp.enums.SalesPartten;
import com.tp.dto.prd.InfoDetailDto;
import com.tp.dto.prd.SkuDto;
import com.tp.m.enums.ImgEnum;
import com.tp.m.enums.MResultInfo;
import com.tp.m.enums.ProductEnum;
import com.tp.m.exception.MobileException;
import com.tp.m.to.product.SkuDetailTO;
import com.tp.m.to.product.SkuTO;
import com.tp.m.to.product.SpecGroupDetailTO;
import com.tp.m.to.product.SpecGroupTO;
import com.tp.m.to.product.TagTO;
import com.tp.m.util.DateUtil;
import com.tp.m.util.JsonUtil;
import com.tp.m.util.NumberUtil;
import com.tp.m.util.StringUtil;
import com.tp.m.vo.product.DummyProductAttr;
import com.tp.m.vo.product.ProductDetailVO;
import com.tp.model.bse.Spec;
import com.tp.model.bse.SpecGroup;
import com.tp.model.prd.ItemAttribute;
import com.tp.model.prd.ItemDetailSpec;
import com.tp.model.prd.ItemPictures;
import com.tp.result.bse.SpecGroupResult;
import com.tp.shop.helper.ImgHelper;
import com.tp.shop.helper.SwitchHelper;

/**
 * 商品封装类
 * @author zhuss
 * @2016年1月5日 下午3:37:46
 */
public class ProductConvert {
	
	/**
	 * 封装商品详情
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static ProductDetailVO convertProductDetail(InfoDetailDto itemInfo,String shareUrl,String tid){
		ProductDetailVO vo = new ProductDetailVO();
		ProductEnum.Status statusenmu = getStatusByPrdSta(itemInfo.getStatus(),
				itemInfo.getOutOfStock() == null ? true : false);
		//未开始
		if(null != itemInfo.getStoretime() && itemInfo.getStoretime() > DateUtil.getTimestamp()){
			statusenmu = ProductEnum.Status.TOPIC_NO_START;
		}
		//已结束
		if(null != itemInfo.getEndtime() && itemInfo.getEndtime() < DateUtil.getTimestamp()){
			statusenmu = ProductEnum.Status.TOPIC_NO_END;
		}
		vo.setStatus(statusenmu.code);
		vo.setStatusdesc(statusenmu.desc);
		// 当商品的状态为已作废则 商品信息为空
		if (StringUtil.equals(statusenmu.code,ProductEnum.Status.ITEM_NO_USE.code))return vo;
		SkuDto skuDto = itemInfo.getSkuDto();
		if(null != skuDto){
			vo.setStock(StringUtil.getStrByObj(skuDto.getQuantity()));
			int stock = skuDto.getQuantity() == null?0:skuDto.getQuantity();
			if(stock == 0&& !ProductEnum.Status.TOPIC_BACKORDERED.code.equals(vo.getStatus())) {
				vo.setStatus(ProductEnum.Status.ITEM_OUT_OF_STOCK.code);
				vo.setStatusdesc(ProductEnum.Status.ITEM_OUT_OF_STOCK.desc);
			}
		}
		vo.setSalespattern(StringUtil.getStrByObj(itemInfo.getSalesPattern()));
		vo.setShareurl(shareUrl);
		vo.setCount(StringUtil.getStrByObj(itemInfo.getQuantity()));
		vo.setCountryimg(ImgHelper.getImgUrl(itemInfo.getCountryImagePath(), ImgEnum.Width.WIDTH_30));
		vo.setCountryname(itemInfo.getCountryName());
		vo.setDetail(ImgHelper.replaceImgInHTML(itemInfo.getDetailDesc(), ImgEnum.Width.WIDTH_750));
		vo.setFeature(itemInfo.getSubTitle());
		List<ItemPictures> pictures = itemInfo.getItemPicturesList();
		if(CollectionUtils.isNotEmpty(pictures)){
			vo.setImgurl(ImgHelper.getImgUrl(pictures.get(0).getPicture(), ImgEnum.Width.WIDTH_75));
			List<String> imglist = new ArrayList<>();
			for(ItemPictures pic : pictures){
				imglist.add(ImgHelper.getImgUrl(pic.getPicture(), ImgEnum.Width.WIDTH_750));
			}
			vo.setImglist(imglist);
		}
		vo.setLimitcount(StringUtil.getStrByObj(itemInfo.getLimitCount()));
		vo.setName(itemInfo.getMainTitle());
		vo.setOldprice(NumberUtil.sfwr(itemInfo.getBasicPrice()));
		//当商品已下架，商品价格就是原价
		vo.setPrice(NumberUtil.sfwr(itemInfo.getXgPrice()));
		vo.setCommision( NumberUtil.sfwr(itemInfo.getCommision()) );
		vo.setChannel(itemInfo.getSendType());
		vo.setSku(itemInfo.getSku());
		vo.setTaxrate(StringUtil.getStrByObj(itemInfo.getTaxRate()));
		vo.setTaxfee(StringUtil.getStrByObj(itemInfo.getRateFee()));
		vo.setTaxdesc(SwitchHelper.taxDesc().replaceFirst("\\{\\d\\}", itemInfo.getRateName()).replaceFirst("\\{\\d\\}", StringUtil.getStrByObj(itemInfo.getTaxRate())));
		String skuJsonStr = itemInfo.getListSkus();
		if(StringUtil.isNotBlank(skuJsonStr)){
			vo.setSkus(convertSkus(skuJsonStr));
		}
		vo.setSpecs(convertSpecs(itemInfo.getSpecGroupList()));
		vo.setTid(tid);
		//立即购买和加入购物车按钮展示
		vo.setPurchasepage(itemInfo.getPurchasePage());
		//解析标签
		if(StringUtil.isNotBlank(itemInfo.getItemTags())){
			List<TagTO> tags = new ArrayList<>();
			tags = (List<TagTO>) JsonUtil.convListByJsonStr(itemInfo.getItemTags(),TagTO.class);
			vo.setTags(tags);
		}
		//已售数量
		if(0 != itemInfo.getSalesCount())vo.setSalescountdesc("已售"+itemInfo.getSalesCount()+"件");

		vo.setUnit( itemInfo.getItemInfo() !=null ? itemInfo.getItemInfo().getUnitName() !=null ? itemInfo.getItemInfo().getUnitName():"":"");

		//处理服务商品的属性
		List<DummyProductAttr> dummyProductAttrs = new ArrayList<>();
		if(itemInfo.getSalesPattern() !=null && itemInfo.getSalesPattern().equals(SalesPartten.OFF_LINE_GROUP_BUY.getValue())){

			List<ItemAttribute> itemAttributeList = itemInfo.getItemAttribute();

			Iterator<ItemAttribute> itemAttributeIterator = itemAttributeList.iterator();
			String s = null;
			String e = null;
			boolean ie = false;
			while (itemAttributeIterator.hasNext()){
				ItemAttribute itemAttribute = itemAttributeIterator.next();
				if(itemAttribute.getCustom() == null || itemAttribute.getCustom() != 2){
					itemAttributeIterator.remove();
				}
				if(itemAttribute.getAttrKey().equals(DummyItemConstant.effecttimestart)) {
					s =  itemAttribute.getAttrValue() ;
					itemAttributeIterator.remove();
				}else if(itemAttribute.getAttrKey().equals(DummyItemConstant.effectTimeEnd)){
					e =  itemAttribute.getAttrValue() ;
					itemAttributeIterator.remove();
				}else if(itemAttribute.getAttrKey().equals(DummyItemConstant.includeFestival)){
					ie = StringUtils.equals(itemAttribute.getAttrValue(),"1")? true:false;
					itemAttributeIterator.remove();
				}

			}
			if(s !=null && e != null){
				StringBuilder builder = new StringBuilder().append(s).append("到").append(e).append(ie?"(节假日通用)":"");
				addList(dummyProductAttrs,"有效期",builder.toString());
			}


			for(ItemAttribute itemAttribute: itemAttributeList){
				if(StringUtils.isBlank(itemAttribute.getAttrValue())) continue;
				addList(dummyProductAttrs,itemAttribute.getAttrKey(),itemAttribute.getAttrValue());
			}
		vo.setDummyattr(dummyProductAttrs);
		}
		return vo;
	}

	private static void addList(List<DummyProductAttr> list, String key, String val){

		for(DummyProductAttr attr: list){
			if(attr.getName().equals(key)){
				if(attr.getCols()==null) attr.setCols(new ArrayList<String>());
				attr.getCols().add(val);
				return;
			}
		}

		List<String> listF = new ArrayList<>();
		listF.add(val);
		list.add(new DummyProductAttr(key,listF));


	}
	
	/**
	 * 封装SKU集合
	 * @return
	 */
	public static List<SkuTO> convertSkus(String skuJsonStr){
		List<SkuTO> skulist = new ArrayList<>();
		List<SkuDto> skuArray = JSONArray.parseArray(skuJsonStr, SkuDto.class);
		if(CollectionUtils.isNotEmpty(skuArray)){
			for(SkuDto dto : skuArray){ //sku
				SkuTO sku = new SkuTO();
				sku.setSku(dto.getSku());
				String specsStr= dto.getListSpec(); 
				if(StringUtil.isNotBlank(specsStr)){
					sku.setSkudetails(convertSkuDetails(specsStr));
				}
				skulist.add(sku);  
			}
		}
		return skulist;
	}
	
	/**
	 * 封装SKU下的规格集合
	 * @param skuSpecJsonStr
	 * @return
	 */
	public static List<SkuDetailTO> convertSkuDetails(String skuSpecJsonStr){
		List<SkuDetailTO> skudetails = new ArrayList<>();
		List<ItemDetailSpec> specList = JSONArray.parseArray(skuSpecJsonStr, ItemDetailSpec.class);
		if(CollectionUtils.isNotEmpty(specList)){
			for(ItemDetailSpec spe : specList){ //sku对应的规格信息
				skudetails.add(new SkuDetailTO(StringUtil.getStrByObj(spe.getSpecGroupId()),StringUtil.getStrByObj(spe.getSpecId())));
			}
			Collections.sort(skudetails, new Comparator<SkuDetailTO>() {
				public int compare(SkuDetailTO sku1, SkuDetailTO sku2) {
					return sku1.getGroupid().compareTo(sku2.getGroupid());
				}
			});
		}
		return skudetails;
	}
	
	/**
	 * 封装规格集合
	 * @param specs
	 * @return
	 */
	public static List<SpecGroupTO> convertSpecs(List<SpecGroupResult> specs){
		List<SpecGroupTO> speclist = new ArrayList<>();
		if(CollectionUtils.isNotEmpty(specs)){
			for(SpecGroupResult sg : specs){
				SpecGroup specGroup = sg.getSpecGroup();
				if(null != specGroup){
					SpecGroupTO sgt = new SpecGroupTO();
					sgt.setGroupname(specGroup.getName());
					sgt.setGroupid(StringUtil.getStrByObj(specGroup.getId()));
					sgt.setGroupdetails(convertSpecDetails(sg.getSpecDoList()));
					speclist.add(sgt);
				}
			}
			Collections.sort(speclist, new Comparator<SpecGroupTO>() {
				public int compare(SpecGroupTO sg1, SpecGroupTO sg2) {
					return sg1.getGroupid().compareTo(sg2.getGroupid());
				}
			});
		}
		return speclist;
	}
	
	/**
	 * 封装规格详情集合
	 * @return
	 */
	public static List<SpecGroupDetailTO> convertSpecDetails(List<Spec> specDoList){
		List<SpecGroupDetailTO> sgdlist = new ArrayList<>();
		if(CollectionUtils.isNotEmpty(specDoList)){
			for(Spec sp : specDoList){
				sgdlist.add(new SpecGroupDetailTO(StringUtil.getStrByObj(sp.getId()),sp.getSpec()));
			}
		}
		return sgdlist;
	}
	
	/**
	 * 封装商品的状态
	 * @param status: 0下架1上架2商品作废(上架包括正常的和已抢光的) isHava是否有库存
	 * @return
	 */
	public static ProductEnum.Status getStatusByPrdSta(String status,boolean isHava){
		if(StringUtil.isBlank(status)) throw new MobileException(MResultInfo.ITEM_ERROR);
		if(StringUtil.equals(status, ProductEnum.Status.NORMAL.code)&&isHava)return ProductEnum.Status.NORMAL; //已上架并有库存
		if(StringUtil.equals(status, ItemComonConstant.TOPIC_BACKORDERED))return ProductEnum.Status.TOPIC_BACKORDERED; //已下架 - 有商详
		if(StringUtil.equals(status, ItemComonConstant.ITEM_UNDERCARRIAGE))return ProductEnum.Status.ITEM_UNDERCARRIAGE; //已下架 - 有商详
		if(StringUtil.equals(status, ProductEnum.Status.NORMAL.code)&&!isHava)return ProductEnum.Status.ITEM_OUT_OF_STOCK; //已上架并没有库存- 有商详
		if(StringUtil.equals(status, ItemComonConstant.ITEM_NO_USE))return ProductEnum.Status.ITEM_NO_USE; //已作废 - 没有商详
		return ProductEnum.Status.NORMAL;
	}
}
