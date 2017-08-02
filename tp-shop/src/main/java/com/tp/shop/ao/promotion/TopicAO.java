package com.tp.shop.ao.promotion;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.vo.Constant;
import com.tp.common.vo.PageInfo;
import com.tp.dto.cms.temple.Products;
import com.tp.dto.common.ResultInfo;
import com.tp.dto.mmp.TopicItemBrandCategoryDTO;
import com.tp.dto.mmp.enums.TopicType;
import com.tp.m.base.MResultVO;
import com.tp.m.base.Page;
import com.tp.m.enums.MResultInfo;
import com.tp.m.exception.MobileException;
import com.tp.m.query.promotion.QueryTopic;
import com.tp.m.vo.product.ProductVO;
import com.tp.m.vo.topic.TopicDetailVO;
import com.tp.model.sup.SupplierShop;
import com.tp.proxy.cms.SingleTempleProxy;
import com.tp.proxy.dss.PromoterInfoProxy;
import com.tp.proxy.sup.SupplierShopProxy;
import com.tp.shop.ao.dss.PromoterAO;
import com.tp.shop.convert.TopicConvert;
import com.tp.shop.helper.PropertiesHelper;

/**
 * 专题业务层
 * @author zhuss
 * @2016年1月4日 下午6:54:47
 */
@Service
public class TopicAO {
	private static final Logger log=LoggerFactory.getLogger(TopicAO.class);
	
	@Autowired
	private PropertiesHelper propertiesHelper;
	
	@Autowired
	private SingleTempleProxy singleTempleProxy;
	
	@Autowired
	PromoterAO promoterAO;
	
	@Autowired
	PromoterInfoProxy promoterInfoProxy;
	@Autowired
	private  SupplierShopProxy supplierShopProxy;
	
	/**
	 * 专题详情
	 * @param tid
	 * @return
	 */
	public MResultVO<TopicDetailVO> getTopicDetail(Long tid,String channelCode){
		try {
			String shareUrl = propertiesHelper.shareTopicUrl.trim().replace("TID", tid.toString()).replace("CHANNELCODE", channelCode);
			TopicItemBrandCategoryDTO topic = singleTempleProxy.getISingleBusTemService().loadTopiInHtmlApp(tid);
			if(null != topic){
				TopicDetailVO t = TopicConvert.convertTopicDetail(topic.getTopic(),shareUrl,channelCode);
				/**
				 * 供应商店铺改造  start
				 */
				Long supplierId=topic.getTopic().getSupplierId();//供应商ID
				Integer  type=topic.getTopic().getType();
				t.setType(type);
				if(supplierId!=null && supplierId!=0  && type.equals(TopicType.SUPPLIER_SHOP.ordinal())){//类型为店铺时
					SupplierShop supplierShop=supplierShopProxy.getSupplierShopInfo(supplierId);//获取店铺信息
					if(supplierShop!=null){
						t.setLogoPath(Constant.IMAGE_URL_TYPE.item.url+supplierShop.getLogoPath());//首页logo
						t.setMobileImage(Constant.IMAGE_URL_TYPE.item.url+supplierShop.getMobileImage());//专场图片
						t.setShopName(supplierShop.getShopName());	//店铺名称
						t.setIntroMobile(supplierShop.getIntroMobile());//店铺介绍
						t.setSupplierId(supplierShop.getSupplierId());//供应商ID
						t.setShopImagePath(Constant.IMAGE_URL_TYPE.item.url+supplierShop.getShopImagePath());//头图片
						t.setShareurl("http://m.51seaco.com/group_shop.html?tid="+tid);
						t.setPrestime(supplierShop.getBusinessTime());
						t.setAddr(supplierShop.getShopAddress());
						t.setTel(supplierShop.getShopTel());
						t.setNotice("欢迎光临本店,开业大酬宾,首单一折,二单2折,三单3折...");
						t.setLatitude(null);
						t.setLatitude(null);
					}
				}
				return new MResultVO<>(MResultInfo.SUCCESS,t); 
			}
			return new MResultVO<>(MResultInfo.FAILED);
		} catch (MobileException e) {
			log.error("[API接口 - 专题详情 MobileException]={}", e.getMessage());
			return new MResultVO<>(e);
		} catch (Exception e) {
			log.error("[API接口 - 专题详情 Exception]={}", e);
			return new MResultVO<>(MResultInfo.CONN_ERROR);
		}
	}
	
	/**
	 * 专题 - 商品列表
	 * @param topic
	 * @return
	 */
	public MResultVO<Page<ProductVO>> getTopicItemList(QueryTopic topic){
		try {			
//			topic.setPromoterId(null);			
			ResultInfo<PageInfo<Products>> ps = singleTempleProxy.loadTopiInfocHtmlApp(TopicConvert.convertTopItem(topic));
			if(ps.isSuccess()){
				return new MResultVO<>(MResultInfo.SUCCESS,TopicConvert.convertTopicItemList(ps.getData()));
			}
			log.error("[调用Service接口 - 专题商品列表(loadTopiInfocHtmlApp) Failed] = {}",ps.getMsg().toString());
			return new MResultVO<>(ps.getMsg().getMessage());
		}catch (MobileException e) {
			log.error("[API接口 - 专题商品列表 MobileException]={}", e.getMessage());
			return new MResultVO<>(e);
		} catch (Exception e) {
			log.error("[API接口 - 专题商品列表 Exception]={}", e);
			return new MResultVO<>(MResultInfo.CONN_ERROR);
		}
	}
}
