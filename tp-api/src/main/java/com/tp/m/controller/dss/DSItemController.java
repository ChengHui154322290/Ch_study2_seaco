package com.tp.m.controller.dss;

import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tp.common.vo.DssConstant;
import com.tp.dto.mmp.distribution.DistributionItemQuery;
import com.tp.m.ao.dss.DSItemAO;
import com.tp.m.ao.dss.PromoterAO;
import com.tp.m.base.MResultVO;
import com.tp.m.base.Page;
import com.tp.m.query.promotion.QueryTopic;
import com.tp.m.vo.product.ProductVO;
import com.tp.util.JsonUtil;

/**
 * Created by ldr on 2016/4/19.
 */
@Controller
public class DSItemController {
    @Autowired
    private DSItemAO dsItemAO;
    @Autowired
    private PromoterAO promoterAO;

    @ResponseBody
    @RequestMapping("/dss/items")
    public String getItems(QueryTopic query){

        int page = 0;
        if(NumberUtils.isNumber(query.getCurpage())){
            page = Integer.parseInt(query.getCurpage());
        }
        Long shopPromoterId = promoterAO.authPromoter(query.getToken(),DssConstant.PROMOTER_TYPE.DISTRIBUTE.code);
        if(null==shopPromoterId){
        	return JsonUtil.convertObjToStr(new MResultVO<Page<ProductVO>>(""));
        }
        DistributionItemQuery dq8 = new DistributionItemQuery();
        dq8.setShopPromoterId(shopPromoterId);
        dq8.setPage(page);
        
       MResultVO<Page<ProductVO>> result =  dsItemAO.getDItems(dq8);
        return JsonUtil.convertObjToStr(result);
    }

}
