package com.tp.m.controller.product;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tp.dto.common.ResultInfo;
import com.tp.m.ao.dss.DSSUserAO;
import com.tp.m.ao.product.ProductAO;
import com.tp.m.base.MResultVO;
import com.tp.m.enums.MResultInfo;
import com.tp.m.exception.MobileException;
import com.tp.m.query.product.QueryProduct;
import com.tp.m.util.AssertUtil;
import com.tp.m.util.JsonUtil;
import com.tp.m.vo.product.ProductDetailVO;
import com.tp.model.dss.PromoterInfo;

/**
 * 商品控制器
 * @author zhuss
 * @2016年1月5日 下午3:33:28
 */
@Controller
@RequestMapping("/product")
public class ProductController {

	private static final Logger log = LoggerFactory.getLogger(ProductController.class);
	
	@Autowired
	private ProductAO productAO;

	@Autowired
	private DSSUserAO dSSUserAO;

	/**
	 * 商品详情
	 * @param topic
	 * @return
	 */
	@RequestMapping(value="/detail", method=RequestMethod.GET)
	@ResponseBody
	public String getProductDetail(QueryProduct product){
		try{
			if(log.isInfoEnabled()){
				log.info("[API接口 -商品详情 入参] = {}",JsonUtil.convertObjToStr(product));
			}
			AssertUtil.notBlank(product.getSku(), MResultInfo.ITEM_SKU_NULL);
			AssertUtil.notBlank(product.getTid(), MResultInfo.TOPIC_ID_NULL);
			
			Long promoterId = null;
			if(product.getShopmobile() != null){
				ResultInfo<PromoterInfo> reltPromoter= dSSUserAO.getPromoterInfo( product.getShopmobile() );
				if(reltPromoter.isSuccess() && reltPromoter.getData()!=null){
					promoterId = reltPromoter.getData().getPromoterId();
					product.setPromoterId(promoterId);
				}
			}			

			MResultVO<ProductDetailVO> result = productAO.getProductDetail(product);
			if(log.isInfoEnabled()){
				log.info("[API接口 -商品详情 返回值] = {}",JsonUtil.convertObjToStr(result));
			}
			return JsonUtil.convertObjToStr(result);
		}catch(MobileException me){
			log.error("[API接口 - 商品详情  MobileException] = {}",me.getMessage());
			return JsonUtil.convertObjToStr(new MResultVO<>(MResultInfo.PARAM_ERROR));
		}
	}
}
