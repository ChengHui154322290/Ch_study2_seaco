package com.tp.shop.controller.promotion;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tp.dto.common.ResultInfo;
import com.tp.m.base.MResultVO;
import com.tp.m.base.Page;
import com.tp.m.enums.MResultInfo;
import com.tp.m.exception.MobileException;
import com.tp.m.query.promotion.QueryTopic;
import com.tp.m.util.AssertUtil;
import com.tp.m.util.JsonUtil;
import com.tp.m.util.StringUtil;
import com.tp.m.vo.product.ProductVO;
import com.tp.m.vo.topic.TopicDetailVO;
import com.tp.model.dss.PromoterInfo;
import com.tp.shop.ao.dss.DSSUserAO;
import com.tp.shop.ao.dss.PromoterAO;
import com.tp.shop.ao.promotion.TopicAO;
import com.tp.shop.helper.RequestHelper;

/**
 * 专题控制器
 * @author zhuss
 * @2016年1月4日 下午6:44:40
 */
@Controller
@RequestMapping("/topic")
public class TopicController {

	private static final Logger log = LoggerFactory.getLogger(TopicController.class);
	
	@Autowired
	private TopicAO topicAO;
	@Autowired
	private PromoterAO promoterAO;
	@Autowired
	private DSSUserAO dSSUserAO;
	
	
	/**
	 * 专场详情
	 * @param topic
	 * @return
	 */
	@RequestMapping(value="/detail", method=RequestMethod.GET)
	@ResponseBody
	public String getTopicDetail(QueryTopic topic,HttpServletRequest request){
		try{
			String channelCode = RequestHelper.getChannelCode(request);
			if(log.isInfoEnabled()){
				log.info("[API接口 -专场详情 入参] = {}",JsonUtil.convertObjToStr(topic));
			}
			AssertUtil.notBlank(topic.getTid(), MResultInfo.TOPIC_ID_NULL);
			MResultVO<TopicDetailVO> result = topicAO.getTopicDetail(StringUtil.getLongByStr(topic.getTid()),channelCode);
			if(log.isInfoEnabled()){
				log.info("[API接口 -专场详情 返回值] = {}",JsonUtil.convertObjToStr(result));
			}
			return JsonUtil.convertObjToStr(result);
		}catch(MobileException me){
			log.error("[API接口 - 专场详情  MobileException] = {}",me.getMessage());
			return JsonUtil.convertObjToStr(new MResultVO<>(MResultInfo.PARAM_ERROR));
		}
	}
	
	/**
	 * 专场 - 商品列表
	 * @param topic
	 * @return
	 */
	@RequestMapping(value="/products", method=RequestMethod.GET)
	@ResponseBody
	public String getTopicItemList(QueryTopic topic,HttpServletRequest request){
		try{
			if(log.isInfoEnabled()){
				log.info("[API接口 -专场商品列表 入参] = {}",JsonUtil.convertObjToStr(topic));
			}
			AssertUtil.notBlank(topic.getTid(), MResultInfo.TOPIC_ID_NULL);
			
			Long promoterId = null;
			if(StringUtils.isNotBlank(topic.getShopmobile())){
				ResultInfo<PromoterInfo> reltPromoter= dSSUserAO.getPromoterInfo( topic.getShopmobile() );
				if(reltPromoter.isSuccess() && reltPromoter.getData()!=null){
					promoterId = reltPromoter.getData().getPromoterId();
					topic.setPromoterId(promoterId);
				}
			}else{
				promoterId = promoterAO.authPromoter(topic.getToken(), RequestHelper.getChannelCode(request));
				topic.setPromoterId(promoterId);
			}

			MResultVO<Page<ProductVO>> result = topicAO.getTopicItemList(topic);
			if(log.isInfoEnabled()){
				log.info("[API接口 -专场商品列表 返回值] = {}",JsonUtil.convertObjToStr(result));
			}
			return JsonUtil.convertObjToStr(result);
		}catch(MobileException me){
			log.error("[API接口 - 专场商品列表  MobileException] = {}",me.getMessage());
			return JsonUtil.convertObjToStr(new MResultVO<>(MResultInfo.PARAM_ERROR));
		}
	}
}
