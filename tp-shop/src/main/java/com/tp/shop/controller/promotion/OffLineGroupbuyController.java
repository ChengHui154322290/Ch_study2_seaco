package com.tp.shop.controller.promotion;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tp.m.base.BaseQuery;
import com.tp.m.base.MResultVO;
import com.tp.m.base.Page;
import com.tp.m.query.home.QueryIndex;
import com.tp.m.util.JsonUtil;
import com.tp.m.vo.home.BannerVO;
import com.tp.m.vo.product.ProductVO;
import com.tp.m.vo.topic.TopicVO;
import com.tp.shop.ao.promotion.OffLineGroupbuyAO;

/**
 * Created by ldr on 2016/10/12.
 */
@Service
@RequestMapping("/offlinegb")
public class OffLineGroupbuyController {

    @Autowired
    private OffLineGroupbuyAO offLineGroupbuyAO;

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * 线下团购-banner
     *
     * @return
     */
    @ResponseBody
    @RequestMapping(value = {"/banner"}, method = RequestMethod.GET)
    public String getBanners(QueryIndex indexQuery) {
        if (logger.isInfoEnabled()) {
            logger.info("[OFF_LINE_GROUP_BUY_BANNER_PARAM:] = {}", JsonUtil.convertObjToStr(indexQuery));
        }
        MResultVO<List<BannerVO>> reuslt = offLineGroupbuyAO.getBanners(indexQuery);
        /*if(log.isInfoEnabled()){
			log.info("[API接口 - 广告位信息 返回值] = {}",JsonUtil.convertObjToStr(reuslt));
		}*/
        return JsonUtil.convertObjToStr(reuslt);
    }

    /**
     * 线下团购-商家列表
     *
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/shoplist", method = RequestMethod.GET)
    public String getTodayNew(QueryIndex indexQuery) {
        if (logger.isInfoEnabled()) {
            logger.info("[OFF_LINE_GROUP_BUY_SHOP_LIST_PARAM] = {}", JsonUtil.convertObjToStr(indexQuery));
        }
        MResultVO<Page<TopicVO>> result = offLineGroupbuyAO.shopList(indexQuery);

        return JsonUtil.convertObjToStr(result);
    }

    /**
     * 线下团购-热门商品
     *
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/hotsale", method = RequestMethod.GET)
    public String getTodayNew(BaseQuery baseQuery) {
        if (logger.isInfoEnabled()) {
            logger.info("[OFF_LINE_GROUP_BUY_HOT_SALE_PARAM] = {}", JsonUtil.convertObjToStr(baseQuery));
        }
        MResultVO<List<ProductVO>> result = offLineGroupbuyAO.hotSale();
        return JsonUtil.convertObjToStr(result);
    }

}
