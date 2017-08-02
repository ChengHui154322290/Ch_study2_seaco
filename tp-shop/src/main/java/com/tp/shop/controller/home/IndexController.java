package com.tp.shop.controller.home;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tp.m.base.MResultVO;
import com.tp.m.base.Page;
import com.tp.m.enums.MResultInfo;
import com.tp.m.query.home.QueryIndex;
import com.tp.m.util.AssertUtil;
import com.tp.m.util.JsonUtil;
import com.tp.m.vo.home.BannerVO;
import com.tp.m.vo.home.IndexModuleVO;
import com.tp.m.vo.topic.TopicVO;
import com.tp.shop.ao.home.IndexAO;
import com.tp.shop.helper.RequestHelper;

/**
 * 首页控制器
 * @author zhuss
 */
@Controller
@RequestMapping("/index")
public class IndexController {
	private static Logger log = LoggerFactory.getLogger(IndexController.class);
	
	@Autowired
	private IndexAO indexAO;

	
	/**
	 * 首页-广告位信息
	 * @return
	 */
	@RequestMapping(value={"/getbanners"}, method=RequestMethod.GET)
	@ResponseBody
	public String getBanners(HttpServletRequest request, QueryIndex indexQuery){
		if(log.isInfoEnabled()){
			log.info("[API接口 - 广告位信息 入参] = {}",JsonUtil.convertObjToStr(indexQuery));
		}
		indexQuery.setChannelcode(RequestHelper.getChannelCode(request));
		
		AssertUtil.notBlank(indexQuery.getChannelcode(), MResultInfo.SYSTEM_ERROR);
		MResultVO<List<BannerVO>> reuslt=indexAO.getBanners(indexQuery);
		/*if(log.isInfoEnabled()){
			log.info("[API接口 - 广告位信息 返回值] = {}",JsonUtil.convertObjToStr(reuslt));
		}*/
		return JsonUtil.convertObjToStr(reuslt);
	}
	
	/**
	 * 首页-今日上新
	 * @return
	 */
	@RequestMapping(value="/gettodaynew", method=RequestMethod.GET)
	@ResponseBody
	public String getTodayNew(HttpServletRequest request, QueryIndex indexQuery){
		indexQuery.setChannelcode(RequestHelper.getChannelCode(request));
		if(log.isInfoEnabled()){
			log.info("[API接口 - 今日上新 入参] = {}",JsonUtil.convertObjToStr(indexQuery));
		}
			
		AssertUtil.notBlank(indexQuery.getChannelcode(), MResultInfo.SYSTEM_ERROR);
		MResultVO<Page<TopicVO>> reuslt = indexAO.topicAndSingleGroup(indexQuery);
		if(log.isInfoEnabled()){
			log.info("[API接口 - 今日上新 返回值] = {}",JsonUtil.convertObjToStr(reuslt));
		}
		return JsonUtil.convertObjToStr(reuslt);
	}
	
	/**
	 * 首页- 模块信息
	 * @return
	 */
	@RequestMapping(value="/module", method=RequestMethod.GET)
	@ResponseBody
	public String getLabs(HttpServletRequest request, QueryIndex indexQuery){
		if(log.isInfoEnabled()){
			log.info("[API接口 - 首页模块 入参] = {}",JsonUtil.convertObjToStr(indexQuery));
		}
		indexQuery.setChannelcode(RequestHelper.getChannelCode(request));
		AssertUtil.notBlank(indexQuery.getChannelcode(), MResultInfo.SYSTEM_ERROR);
		MResultVO<IndexModuleVO> result = indexAO.queryIndexModular(indexQuery);
		log.info("[API接口 - 首页模块 出参] = {}",JsonUtil.convertObjToStr(result));
		return JsonUtil.convertObjToStr(result);
	}
}
