package com.tp.m.ao.product;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.vo.Constant;
import com.tp.common.vo.ProductConstant.RATE_TYPE;
import com.tp.dto.common.ResultInfo;
import com.tp.dto.mmp.enums.SalesPartten;
import com.tp.dto.mmp.enums.TopicType;
import com.tp.dto.ord.directOrderNB.RetMessageNBDto;
import com.tp.dto.prd.InfoDetailDto;
import com.tp.m.ao.promotion.TopicAO;
import com.tp.m.base.MResultVO;
import com.tp.m.convert.ProductConvert;
import com.tp.m.enums.MResultInfo;
import com.tp.m.exception.MobileException;
import com.tp.m.helper.AuthHelper;
import com.tp.m.helper.PropertiesHelper;
import com.tp.m.query.product.QueryProduct;
import com.tp.m.to.cache.TokenCacheTO;
import com.tp.m.util.NumberUtil;
import com.tp.m.util.StringUtil;
import com.tp.m.vo.product.ProductDetailVO;
import com.tp.m.vo.topic.TopicDetailVO;
import com.tp.model.dss.PromoterInfo;
import com.tp.model.prd.ItemSku;
import com.tp.model.sup.SupplierShop;
import com.tp.proxy.dss.PromoterInfoProxy;
import com.tp.proxy.prd.ItemProxy;
import com.tp.proxy.sup.SupplierShopProxy;
import com.tp.service.dss.IPromoterInfoService;
import com.tp.service.ord.directOrder.NB.IDirectOrderNBService;
import com.tp.service.prd.IItemSkuService;

/**
 * 商品业务层
 *
 * @author zhuss
 * @2016年1月4日 下午6:54:47
 */
@Service
public class ProductAO {

    private static final Logger log = LoggerFactory.getLogger(ProductAO.class);

    @Autowired
    private PropertiesHelper propertiesHelper;

    @Autowired
    private ItemProxy itemProxy;

    @Autowired
    private AuthHelper authHelper;

    @Autowired
    private PromoterInfoProxy promoterInfoProxy;
    
    @Autowired
    private IItemSkuService itemSkuService;
  
    @Autowired
    private IPromoterInfoService promoterInfoService;

	@Autowired
	private TopicAO topicAO;
	
	@Autowired
	private IDirectOrderNBService directOrderNBService;
	
	@Autowired
	Properties settings;
    /**
     * 商品详情
     *
     * @param product
     * @return
     */
    public MResultVO<ProductDetailVO> getProductDetail(QueryProduct product) {
        try {
            String shareUrl = propertiesHelper.shareProductUrl.trim().replace("TID", product.getTid()).replace("SKU", product.getSku());
            InfoDetailDto itemInfo = itemProxy.queryItemSkuTopicInfoForAPPHaiTao(product.getSku(), product.getTid());
            if(itemInfo.getSalesPattern() != null && itemInfo.getSalesPattern().equals(SalesPartten.OFF_LINE_GROUP_BUY.getValue())){
                shareUrl = propertiesHelper.offLineGroupbuyShareUrl.trim().replace("TID", product.getTid()).replace("SKU", product.getSku());
            }
            // for dss 佣金
            if(product.getPromoterId() != null){     
            	PromoterInfo promoter =  promoterInfoService.queryById(product.getPromoterId());    		
            	ItemSku qrysku = new ItemSku();
            	qrysku.setSku( itemInfo.getSku() );
            	List<ItemSku> skuList = itemSkuService.queryByObject( qrysku );
            	if(skuList != null && !skuList.isEmpty() && promoter!=null){
            		Double commisionRate = skuList.get(0).getCommisionRate()==null ? 0.0d : skuList.get(0).getCommisionRate();
    				Double commision = promoterInfoService.getCurrentCommision2(promoter,  itemInfo.getXgPrice(), commisionRate );
    				itemInfo.setCommision(commision);	
            	}  
            }
            
            if (null != itemInfo) {
                ProductDetailVO vo = ProductConvert.convertProductDetail(itemInfo, shareUrl, product.getTid());
               //商品类型 
                vo.setItemType(itemInfo.getItemType());
               //设置商品税率说明
                vo = setProductTaxDescription(itemInfo, vo);
                //商品详情不用展示返佣信息
               // setDistributionAmount(product, itemInfo, vo);
                
                //店铺信息  
                MResultVO<TopicDetailVO> result=topicAO.getTopicDetail( Long.valueOf(product.getTid()));
                TopicDetailVO topicDetail = result.getData();
                if(topicDetail != null && topicDetail.getType() != null && 
                		topicDetail.getSupplierId()!=null&& TopicType.SUPPLIER_SHOP.ordinal() == topicDetail.getType()){//判断是否店铺
                	vo.setShopName(topicDetail.getShopName());//店铺名称
                	vo.setLogoPath(topicDetail.getLogoPath());//logo地址
                	vo.setMobileImage(topicDetail.getMobileImage());//专场图片
                	vo.setIntroMobile(topicDetail.getIntroMobile());//店铺介绍
                	vo.setShopImagePath(topicDetail.getShopImagePath());//店铺头图片
                    vo.setAddr(topicDetail.getAddr());
                    vo.setTel(topicDetail.getTel());
                    vo.setPrestime(topicDetail.getPrestime());
                }
                String sku = product.getSku();
                Map<String, Object> params = new HashMap<String, Object>();
                params.put("sku", sku);
                ItemSku itemSku = itemSkuService.queryUniqueByParams(params);
                if(settings.getProperty("HWZY_SPID").equals(String.valueOf(itemSku.getSpId()))){
	                List<String> articleCodeList = new ArrayList<String>();
//	                articleCodeList.add(itemSku.getArticleCode());
	                articleCodeList.add(itemSku.getGoodsCode());
	                RetMessageNBDto retMessageNBDto = null;
	                if(settings.getProperty("HWZY_ON&OFF").equals("true")){
	                	retMessageNBDto = directOrderNBService.getProductStoreNB(articleCodeList);
	                }else{
	                	retMessageNBDto = directOrderNBService.getProductStoreNBTest(articleCodeList);
                	}	
	                if(retMessageNBDto.getResult().equals("F")){
	                	vo.setInventoryCount(0);
	                }else{
	                	vo.setInventoryCount(retMessageNBDto.getProducts().get(0).getStoreNumber());
	                }
                }
                return new MResultVO<>(MResultInfo.SUCCESS, vo);
            }
            return new MResultVO<>(MResultInfo.FAILED);
        } catch (MobileException e) {
            log.error("[API接口 - 商品详情  MobileException]={}", e.getMessage());
            return new MResultVO<>(e);
        } catch (Exception e) {
            log.error("[API接口 - 商品详情  Exception]={}", e);
            return new MResultVO<>(MResultInfo.CONN_ERROR);
        }
    }

    private void setDistributionAmount(QueryProduct product, InfoDetailDto itemInfo, ProductDetailVO vo) {
        try{
        if(  itemInfo.getSalesPattern() !=null && itemInfo.getSalesPattern().equals(SalesPartten.DISTRIBUTION.getValue()) && isDisPromoter(product)){
            vo.setDisamount(NumberUtil.sfwr( StringUtils.isBlank(itemInfo.getCommisionRate())? 0: new BigDecimal(itemInfo.getCommisionRate()).multiply(new BigDecimal(vo.getPrice())).doubleValue()));
        }else {
            vo.setDisamount(null);
        }
        }catch (Exception e){
            vo.setDisamount(null);
            log.error("[SET_ITEM_DISTRIBUTION_AMOUNT_ERROR,]",e);
        }
    }

    private boolean isDisPromoter(QueryProduct product) {
        try {
            if (StringUtils.isBlank(product.getToken())) return false;
            TokenCacheTO usr = authHelper.authToken(product.getToken());
            PromoterInfo promoterInfo = new PromoterInfo();
            promoterInfo.setMemberId(usr.getUid());
            ResultInfo<List<PromoterInfo>> result = promoterInfoProxy.queryByObject(promoterInfo);
            if (!result.isSuccess() || CollectionUtils.isEmpty(result.getData())) {
                return false;
            }
            for (PromoterInfo p : result.getData()) {
                if (1 == p.getPromoterType()) {            //店铺推广员
                    return true;
                }
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }
    
    //设置税率描述
    private ProductDetailVO setProductTaxDescription(InfoDetailDto itemInfo, ProductDetailVO vo){
    	if (RATE_TYPE.POSTAL.code.equals(itemInfo.getRateType())){
    		vo.setTaxdesc(propertiesHelper.postalTaxDesc.replaceFirst("\\{\\d\\}", StringUtil.getStrByObj(itemInfo.getTaxRate())));
    	}else if(RATE_TYPE.TAXFREE.code.equals(itemInfo.getRateType())){
    		vo.setTaxdesc(propertiesHelper.freeTaxDesc);
    	}else{
    		vo.setTaxdesc(propertiesHelper.synthesisTaxDesc.replaceFirst("\\{\\d\\}", itemInfo.getRateName())
    				.replaceFirst("\\{\\d\\}", StringUtil.getStrByObj(itemInfo.getTaxRate())));
    	}
    	return vo;
    }
}
