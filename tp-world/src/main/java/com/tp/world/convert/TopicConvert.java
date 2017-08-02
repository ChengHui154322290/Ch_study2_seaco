package com.tp.world.convert;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tp.common.vo.PageInfo;
import com.tp.dto.cms.app.AppSingleAllInfoDTO;
import com.tp.dto.cms.app.AppSingleInfoDTO;
import com.tp.dto.cms.app.query.AppTopItemPageQuery;
import com.tp.dto.cms.temple.Products;
import com.tp.dto.mmp.enums.ProgressStatus;
import com.tp.dto.mmp.enums.TopicStatus;
import com.tp.m.base.Page;
import com.tp.m.constant.PageConstant;
import com.tp.m.enums.ImgEnum;
import com.tp.m.enums.MResultInfo;
import com.tp.m.enums.ProductEnum;
import com.tp.m.enums.TopicEnum;
import com.tp.m.exception.MobileException;
import com.tp.m.query.promotion.QueryTopic;
import com.tp.m.util.JsonUtil;
import com.tp.m.util.NumberUtil;
import com.tp.m.util.StringUtil;
import com.tp.m.vo.product.ProductVO;
import com.tp.m.vo.topic.TopicDetailVO;
import com.tp.m.vo.topic.TopicVO;
import com.tp.world.helper.ImgHelper;

/**
 * 专题封装类
 *
 * @author zhuss
 * @2016年1月10日 下午6:05:34
 */
public class TopicConvert {

    private static final Logger log = LoggerFactory.getLogger(TopicConvert.class);

    /**
     * 封装专题状态
     *
     * @param status：0编辑中             1审批中 2已取消 3审核通过 4已驳回 5终止
     * @param progress：0-未开始1-进行中2已结束
     * @return
     */
    public static TopicEnum.Status convertTopicStatus(Integer status, Integer progress) {
        TopicStatus st = TopicStatus.parse(status);
        ProgressStatus ps = ProgressStatus.parse(progress);
        if (null != st && null != ps) {
            if (st.equals(TopicStatus.PASSED) && ps.equals(ProgressStatus.DOING)) {
                return TopicEnum.Status.NORMAL;
            } else if (st.equals(TopicStatus.PASSED) && ps.equals(ProgressStatus.NotStarted)) {
                return TopicEnum.Status.NO_START;
            } else if (st.equals(TopicStatus.PASSED) && ps.equals(ProgressStatus.FINISHED)) {
                return TopicEnum.Status.NO_END;
            }
            return TopicEnum.Status.INVALID;
        }
        log.error("[专题封装类 - 封装状态   入参] status = {},progress = {}", status, progress);
        throw new MobileException(MResultInfo.SYSTEM_ERROR);
    }

    /**
     * 封装专题对象
     *
     * @param single
     * @return
     */
    public static TopicVO convertTopic(AppSingleInfoDTO single,boolean newVersion) {
        TopicVO t = new TopicVO();
        t.setTid(StringUtil.getStrByObj(single.getSpecialid()));
        t.setName(single.getName());
        if(newVersion){
           String image = StringUtils.isBlank(single.getMobileImage())?single.getImageurl() : single.getMobileImage();
            t.setImgurl(ImgHelper.getImgUrl(image, ImgEnum.Width.WIDTH_750));
        }else {
            t.setImgurl(ImgHelper.getImgUrl(single.getImageurl(), ImgEnum.Width.WIDTH_750));
        }
        return t;
    }

    /**
     * 封装首页专题(包括单品团和主题团)
     *
     * @param single
     * @return
     */
    public static TopicVO convertTopicAndSingle(AppSingleAllInfoDTO single, String shareUrl,boolean newVersion) {
        TopicVO t = new TopicVO();
        t.setTid(StringUtil.getStrByObj(single.getSpecialid()));
        t.setName(single.getSpecialName());
        if(newVersion){
            String image = StringUtils.isBlank(single.getMobileImage())?single.getImageurl().get(0) : single.getMobileImage();
            t.setImgurl(ImgHelper.getImgUrl(image, ImgEnum.Width.WIDTH_750));
        }else {
            t.setImgurl(ImgHelper.getImgUrl(single.getImageurl().get(0), ImgEnum.Width.WIDTH_750));
        }
        t.setType(StringUtil.TWO);
        t.setTopicpoint(StringUtils.isBlank(single.getTopicPoint()) ? "欢迎光临本店!" : single.getTopicPoint());
        t.setNotice(StringUtils.isBlank(single.getText()) ? "欢迎光临本店!" : single.getText());
        t.setShoplogo(ImgHelper.getImgUrl(single.getShopLogo(), ImgEnum.Width.WIDTH_180));
        //封装单品团信息
        if (single.isSingle()) {
            t.setImgurl(ImgHelper.getImgUrl(single.getMobileImage(), ImgEnum.Width.WIDTH_750));
            t.setChannel(single.getChannelName());
            t.setCountryimg(ImgHelper.getImgUrl(single.getCountryImageUrl(), ImgEnum.Width.WIDTH_30));
            t.setCountryname(single.getCountryName());
            t.setFeature(single.getTopicPoint());
            t.setOldprice(NumberUtil.sfwr(single.getOldprice()));
            t.setPrice(NumberUtil.sfwr(single.getPrice()));
            t.setShareurl(shareUrl);
            t.setSku(single.getSku());
            t.setType(StringUtil.ONE);
        } else {
            List<Products> topicItemList = single.getTopicItemList();
            if (CollectionUtils.isNotEmpty(topicItemList)) {
                List<ProductVO> itemlist = new ArrayList<>();
                for (Products topicItem : topicItemList) {
                    ProductVO vo = new ProductVO();
                    vo.setName(topicItem.getName());
                    vo.setImgurl(ImgHelper.getImgUrl(topicItem.getImgsrc(), ImgEnum.Width.WIDTH_360));
                    vo.setOldprice(NumberUtil.sfwr(topicItem.getLastValue()));
                    vo.setPrice(NumberUtil.sfwr(topicItem.getNowValue()));
                    vo.setDiscount(NumberUtil.calDiscount(StringUtil.getDoubleByStr(vo.getPrice()), StringUtil.getDoubleByStr(vo.getOldprice())));
                    vo.setTid(topicItem.getTopicid());
                    vo.setSku(topicItem.getSku());
                    if (StringUtil.isNotBlank(topicItem.getType()))
                        vo.setStatus(getStatusByPrdType(topicItem.getType()).code);
                    itemlist.add(vo);
                }
                t.setItemlist(itemlist);
            }

        }
        return t;
    }


    /**
     * @param single
     * @return
     */
    public static TopicVO convertTopicAndSingleForDSS(AppSingleAllInfoDTO single, String shareUrl,boolean newVersion) {
        TopicVO t = new TopicVO();
        t.setTid(StringUtil.getStrByObj(single.getSpecialid()));
        t.setName(single.getSpecialName());
        if(newVersion){
            String image = StringUtils.isBlank(single.getMobileImage())?single.getImageurl().get(0) : single.getMobileImage();
            t.setImgurl(ImgHelper.getImgUrl(image, ImgEnum.Width.WIDTH_750));
        }else {
            t.setImgurl(ImgHelper.getImgUrl(single.getImageurl().get(0), ImgEnum.Width.WIDTH_750));
        }
        t.setType(StringUtil.TWO);
        //封装单品团信息
        if (single.isSingle()) {
			t.setImgurl(ImgHelper.getImgUrl(single.getImageurl().get(0),ImgEnum.Width.WIDTH_180));
//			t.setName( " " );
            t.setChannel(single.getChannelName());
            t.setCountryimg(ImgHelper.getImgUrl(single.getCountryImageUrl(), ImgEnum.Width.WIDTH_30));
            t.setCountryname(single.getCountryName());
            t.setFeature(single.getTopicPoint());
            t.setOldprice(NumberUtil.sfwr(single.getOldprice()));
            t.setPrice(NumberUtil.sfwr(single.getPrice()));
            t.setShareurl(shareUrl);
            t.setSku(single.getSku());
            t.setCommision(NumberUtil.sfwr(single.getCommission()));
            t.setType(StringUtil.ONE);
        } else {
            List<Products> topicItemList = single.getTopicItemList();
            if (CollectionUtils.isNotEmpty(topicItemList)) {
                List<ProductVO> itemlist = new ArrayList<>();
                for (Products topicItem : topicItemList) {
                    ProductVO vo = new ProductVO();
                    vo.setName(topicItem.getName());
                    vo.setImgurl(ImgHelper.getImgUrl(topicItem.getImgsrc(), ImgEnum.Width.WIDTH_360));
                    vo.setOldprice(NumberUtil.sfwr(topicItem.getLastValue()));
                    vo.setPrice(NumberUtil.sfwr(topicItem.getNowValue()));
                    vo.setDiscount(NumberUtil.calDiscount(StringUtil.getDoubleByStr(vo.getPrice()), StringUtil.getDoubleByStr(vo.getOldprice())));
                    vo.setTid(topicItem.getTopicid());
                    vo.setSku(topicItem.getSku());
                    vo.setCommision(NumberUtil.sfwr(topicItem.getCommision()));
                    if (StringUtil.isNotBlank(topicItem.getType()))
                        vo.setStatus(getStatusByPrdType(topicItem.getType()).code);
                    itemlist.add(vo);
                }
                t.setItemlist(itemlist);
            }

        }
        return t;
    }

    /**
     * 封装专题详情对象
     *
     * @return
     */
    public static TopicDetailVO convertTopicDetail(com.tp.model.mmp.Topic topic, String shareUrl) {
        TopicDetailVO t = new TopicDetailVO();
        TopicEnum.Status status = TopicEnum.Status.INVALID;
        if (null != topic) {
            status = convertTopicStatus(topic.getStatus(), topic.getProgress());
            t.setName(topic.getName());
            t.setTophtml(ImgHelper.replaceImgInHTML(topic.getIntroMobile(), ImgEnum.Width.WIDTH_750));
            t.setShareurl(shareUrl);
        }
        t.setStatus(status.code);
        t.setStatusdesc(status.desc);
        return t;
    }

    /**
     * 封装专题下的商品列表
     *
     * @param t
     * @return
     */
    public static Page<ProductVO> convertTopicItemList(PageInfo<Products> ps) {
        Page<ProductVO> pages = new Page<>();
        List<ProductVO> tlist = new ArrayList<>();
        List<Products> prds = ps.getRows();
        if (null != ps) {
            if (CollectionUtils.isNotEmpty(prds)) {
                for (Products prd : prds) {
                    tlist.add(convertProduct(prd));
                }
                pages.setFieldTPageCount(tlist, ps.getPage(), ps.getTotal());
            }
            pages.setCurpage(ps.getPage());
        }
        return pages;
    }

    /**
     * 封装商品对象
     *
     * @param prd
     * @return
     */
    public static ProductVO convertProduct(Products prd) {
        ProductVO item = new ProductVO();
        item.setName(prd.getName());
        item.setImgurl(ImgHelper.getImgUrl(prd.getImgsrc(), ImgEnum.Width.WIDTH_360));
        item.setOldprice(NumberUtil.sfwr(prd.getLastValue()));
        item.setPrice(NumberUtil.sfwr(prd.getNowValue()));
        item.setDiscount(NumberUtil.calDiscount(StringUtil.getDoubleByStr(item.getPrice()), StringUtil.getDoubleByStr(item.getOldprice())));
        item.setTid(prd.getTopicid());
        item.setSku(prd.getSku());
        item.setCommision(NumberUtil.sfwr(prd.getCommision()));
        item.setSalescountdesc(prd.getSalesCount() == null ? "" : prd.getSalesCount() == 0 ? "" : "已售" + prd.getSalesCount());
        if (StringUtil.isNotBlank(prd.getType())) item.setStatus(getStatusByPrdType(prd.getType()).code);
        return item;
    }

    /**
     * 封装查询条件
     *
     * @param topic
     * @return
     */
    public static AppTopItemPageQuery convertTopItem(QueryTopic topic) {
        AppTopItemPageQuery topItemQuery = new AppTopItemPageQuery();
        topItemQuery.setIsascending(StringUtil.isBlank(topic.getSort()) == true ? StringUtil.ZERO : topic.getSort());
        topItemQuery.setSpecialid(Long.valueOf(topic.getTid()));// 专场id
        topItemQuery.setCurpage(StringUtil.getCurpageDefault(topic.getCurpage()));
        topItemQuery.setPageSize(PageConstant.DEFAULT_PAGESIZE);
        // by zhs for dss
        if (topic.getPromoterId() != null) {
            topItemQuery.setPromoterId(topic.getPromoterId());
        }
        if (log.isInfoEnabled()) {
            log.info("[封装调用Proxy专题下的商品列表 - 查询条件] ={}", JsonUtil.convertObjToStr(topItemQuery));
        }
        return topItemQuery;
    }

    /**
     * 封装商品的状态
     *
     * @param type：ruball 已抢光  over已下架 outof暂时缺货  chance还有机会  normal 正常  noStart 未开售 editing 编辑中
     * @return
     */
    public static ProductEnum.Status getStatusByPrdType(String type) {
        if (type.trim().equals("normal")) return ProductEnum.Status.NORMAL;
        if (type.trim().equals("over")) return ProductEnum.Status.ITEM_UNDERCARRIAGE;
        else if (type.trim().equals("ruball")) return ProductEnum.Status.ITEM_OUT_OF_STOCK;
        if (type.trim().equals("editing")) return ProductEnum.Status.TOPIC_BACKORDERED;
        return ProductEnum.Status.ITEM_NO_USE;
    }
}
