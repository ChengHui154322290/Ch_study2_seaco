package com.tp.world.ao.promotion;

import com.tp.common.util.ImageUtil;
import com.tp.common.vo.Constant;
import com.tp.common.vo.PageInfo;
import com.tp.dto.cms.app.AppIndexAdvertReturnData;
import com.tp.dto.cms.app.AppSingleAllInfoDTO;
import com.tp.dto.common.ResultInfo;
import com.tp.dto.mmp.enums.SalesPartten;
import com.tp.dto.sch.enums.Sort;
import com.tp.m.base.MResultVO;
import com.tp.m.base.Page;
import com.tp.m.constant.PageConstant;
import com.tp.m.enums.ImgEnum;
import com.tp.m.enums.MResultInfo;
import com.tp.m.query.home.QueryIndex;
import com.tp.m.util.StringUtil;
import com.tp.m.vo.home.BannerVO;
import com.tp.m.vo.product.ProductVO;
import com.tp.m.vo.topic.TopicVO;
import com.tp.model.mmp.TopicItem;
import com.tp.model.sch.result.ItemResult;
import com.tp.proxy.cms.OffLineGroupbuyProxy;
import com.tp.proxy.mmp.OlgbHsConfigProxy;
import com.tp.proxy.sch.SearchProxy;
import com.tp.query.sch.SearchQuery;
import com.tp.redis.util.JedisCacheUtil;
import com.tp.util.JsonUtil;
import com.tp.world.convert.IndexConvert;
import com.tp.world.helper.ImgHelper;
import com.tp.world.helper.PropertiesHelper;
import com.tp.world.helper.RequestHelper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * Created by ldr on 2016/10/12.
 */
@Service
public class OffLineGroupbuyAO {

    private final int LIMIT = 6;

    public static final String OFF_LINE_GROUP_BUY_CACHE_KEY = "OFF_LINE_GROUP_BUY_CACHE_KEY_";

    Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private OffLineGroupbuyProxy offLineGroupbuyProxy;

    @Autowired
    private PropertiesHelper propertiesHelper;

    @Autowired
    private SearchProxy searchProxy;

    @Autowired
    JedisCacheUtil jedisCacheUtil;

    @Autowired
    private OlgbHsConfigProxy olgbHsConfigProxy;

    /**
     * 线下团购-banner
     *
     * @return
     */
    public MResultVO<List<BannerVO>> getBanners(QueryIndex indexQuery) {
        try {
            boolean isApp = RequestHelper.isAPP(indexQuery.getApptype());
            ResultInfo<AppIndexAdvertReturnData> result = offLineGroupbuyProxy.getAdvert();
            if (result.isSuccess())
                return new MResultVO<>(MResultInfo.SUCCESS, IndexConvert.convertBanners(result.getData(), isApp));
            else {
                logger.error("OFF_LINE_GROUP_BUY_GET_BANNERS_FAILED.RESULT=" + JsonUtil.convertObjToStr(result));
                return new MResultVO<>(MResultInfo.FAILED);
            }
        } catch (Exception e) {
            logger.error("[API接口 - 首页广告位 Exception]={}", e);
            return new MResultVO<>(MResultInfo.CONN_ERROR);
        }
    }

    /**
     * 线下团购-商家列表
     *
     * @return
     */
    public MResultVO<Page<TopicVO>> shopList(QueryIndex indexQuery) {
        try {
            ResultInfo<PageInfo<AppSingleAllInfoDTO>> result = offLineGroupbuyProxy.shopList(StringUtil.getCurpageDefault(indexQuery.getCurpage()), PageConstant.DEFAULT_PAGESIZE);
            if (!result.isSuccess()) {
                logger.error("OFF_LINE_GROUP_BUY_LIST_ERROR,RESULT=" + JsonUtil.convertObjToStr(result));
                return new MResultVO(MResultInfo.FAILED);
            }
            PageInfo<AppSingleAllInfoDTO> singleinfo = result.getData();
            if (null != singleinfo) {
                return new MResultVO<>(MResultInfo.SUCCESS, IndexConvert.convertTopicAndSingle(singleinfo, propertiesHelper.shareProductUrl.trim(),false));
            }
            return new MResultVO<>(MResultInfo.SUCCESS);
        } catch (Exception e) {
            logger.error("[API接口 - 首页今日上新  Exception]", e);
            return new MResultVO<>(MResultInfo.CONN_ERROR);
        }
    }

    /**
     * 热销商品
     *
     * @return
     */
    public MResultVO<List<ProductVO>> hotSale() {
        try {

            Object obj = jedisCacheUtil.getCache(OFF_LINE_GROUP_BUY_CACHE_KEY);
            if (obj != null) {
               // return new MResultVO<>(MResultInfo.SUCCESS, (List<ProductVO>) obj);
            }

            SearchQuery query = new SearchQuery();
            query.setSalesPattern(SalesPartten.OFF_LINE_GROUP_BUY.getValue());
            query.setSort(Sort.SALES_COUNT_DESC.getCode());
            query.setStartPage(1);
            query.setPageSize(6);
            ResultInfo<PageInfo<ItemResult>> searchResult = searchProxy.search(query);
            if (!searchResult.isSuccess()) {
                logger.error("OFF_LINE_GROUP_BUY_HOT_SALE_SEARCH_ERROR.RESULT=" + JsonUtil.convertObjToStr(searchResult));
                return new MResultVO<List<ProductVO>>(MResultInfo.SUCCESS, Collections.EMPTY_LIST);
            }

            List<ProductVO> productVOs = new ArrayList<>();
            for (ItemResult itemResult : searchResult.getData().getRows()) {
                ProductVO vo = new ProductVO();
                vo.setName(itemResult.getItem_name());
                vo.setSku(itemResult.getSku());
                vo.setTid(itemResult.getTopic_id().toString());
                vo.setImgurl(ImgHelper.getImgUrl(itemResult.getItem_img(), ImgEnum.Width.WIDTH_360));
                vo.setSalescountdesc(itemResult.getSales_count() == null ? "" : itemResult.getSales_count() <= 0 ? "" : "已售" + itemResult.getSales_count());
                vo.setPrice(itemResult.getTopic_price().toString());
                vo.setOldprice(itemResult.getSale_price().toString());
                vo.setShopname(itemResult.getShop_name());
                productVOs.add(vo);
            }

            List<TopicItem> configedItem ;
            ResultInfo<List<TopicItem>> resultInfo = olgbHsConfigProxy.getConfig();
            if(resultInfo.isSuccess()){
                configedItem = resultInfo.getData();
            }else {
                configedItem = Collections.EMPTY_LIST;
            }

            List<ProductVO> list =  merge(productVOs,configedItem);


            jedisCacheUtil.setCache(OFF_LINE_GROUP_BUY_CACHE_KEY, list, 120);

            return new MResultVO<>(MResultInfo.SUCCESS, list);


        } catch (Exception e) {
            logger.error("OFF_LINE_GROUP_BUY_HOT_SALE_ERROR", e);
            return new MResultVO<List<ProductVO>>(MResultInfo.SUCCESS, Collections.EMPTY_LIST);
        }


    }

    private List<ProductVO> merge(List<ProductVO> productVOs,List<TopicItem> topicItems){
        if(CollectionUtils.isEmpty(topicItems)) return productVOs;

        if(topicItems.size()>LIMIT) topicItems = topicItems.subList(0,LIMIT);


        Iterator<ProductVO> iterator = productVOs.iterator();
        while (iterator.hasNext()){
            ProductVO vo = iterator.next();
            for(TopicItem topicItem: topicItems){
                if(vo.getSku().equals(topicItem.getSku())){
                    iterator.remove();
                    break;
                }
            }
        }

        List<ProductVO> tar = new ArrayList<>();
        int balance = LIMIT - topicItems.size();

        if(balance == 0) {
            return  convertToProductVO(topicItems);
        }else {
            if(productVOs.size()> balance){
                productVOs = productVOs.subList(0,balance);
            }


            Iterator<TopicItem> itemIterator = topicItems.iterator();
            Iterator<ProductVO> voIterator = productVOs.iterator();
            TopicItem tarItem = null;
            ProductVO tarVo = null;
            if(itemIterator.hasNext()) tarItem = itemIterator.next();
            if(voIterator.hasNext()) tarVo = voIterator.next();

            for(int i = 0;i<LIMIT;i++){
                if(tarItem!=null){
                    if(tarItem.getSortIndex()-1 <= i || tarVo==null){
                        tar.add( convertToProductVO(tarItem));
                        if(itemIterator.hasNext()) {
                            tarItem = itemIterator.next();
                        }else {
                            tarItem=null;
                        }
                    }else {
                        if(tarVo != null){
                            tar.add(copy(tarVo));
                            if(voIterator.hasNext()){
                                tarVo = voIterator.next();
                            }else {
                                tarVo = null;
                            }
                        }
                    }
                }else {
                    if(tarVo != null){
                        tar.add(copy(tarVo));
                        if(voIterator.hasNext()){
                            tarVo = voIterator.next();
                        }else {
                            tarVo = null;
                        }
                    }
                }
            }
        }
        return tar;
    }

    protected  List<ProductVO> convertToProductVO(List<TopicItem> items){
        List<ProductVO>list = new ArrayList<>();
        for(TopicItem item: items){
            list.add(convertToProductVO(item));
        }
        return list;
    }

    protected ProductVO convertToProductVO(TopicItem item){
        ProductVO vo = new ProductVO();
        vo.setName(item.getName());
        vo.setTid(item.getTopicId().toString());
        vo.setSku(item.getSku());
        vo.setPrice(StringUtil.getStrByObj(item.getTopicPrice().toString()));
        vo.setOldprice(StringUtil.getStrByObj(item.getSalePrice()));
        vo.setSalescountdesc(item.getSaledAmount() <= 0? "" : "已售"+item.getSaledAmount());
        vo.setImgurl(ImgHelper.getImgUrl(ImageUtil.getImgFullUrl(Constant.IMAGE_URL_TYPE.item,item.getTopicImage()), ImgEnum.Width.WIDTH_360));
        vo.setShopname(item.getTopic() ==null ?"" : item.getTopic().getName());
        return vo;
    }

    protected ProductVO copy(ProductVO vo){
        ProductVO cp = new ProductVO();
        cp.setName(vo.getName());
        cp.setTid(vo.getTid());
        cp.setSku(vo.getSku());
        cp.setPrice(vo.getPrice());
        cp.setOldprice(vo.getOldprice());
        cp.setSalescountdesc(vo.getSalescountdesc());
        cp.setImgurl(vo.getImgurl());
        cp.setShopname(vo.getShopname());
        return cp;
    }
}
