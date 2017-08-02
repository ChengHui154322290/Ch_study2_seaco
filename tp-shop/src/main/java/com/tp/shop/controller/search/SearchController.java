/**
 * NewHeight.com Inc.
 * Copyright (c) 2008-2010 All Rights Reserved.
 */
package com.tp.shop.controller.search;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tp.m.base.MResultVO;
import com.tp.m.base.PageForSearch;
import com.tp.m.exception.MobileException;
import com.tp.m.query.search.QuerySearch;
import com.tp.m.util.JsonUtil;
import com.tp.m.vo.search.NavigationVO;
import com.tp.m.vo.search.SearchConditionVO;
import com.tp.m.vo.search.SearchItemVO;
import com.tp.m.vo.search.SearchShopVO;
import com.tp.shop.ao.search.SearchAO;

/**
 * 搜索控制器
 * @author zhuss
 * @2016年3月2日 上午11:42:03
 */
@Controller
@RequestMapping("/search")
public class SearchController {
	private final Logger log = LoggerFactory.getLogger(SearchController.class);
	
	@Autowired
	private SearchAO searchAO;
	
	/**
	 * 搜索-导航(分类和品牌)
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/navigation",method = RequestMethod.GET)
	public String navigation(){
		try{
			MResultVO<NavigationVO> result = searchAO.navigation();
			/*if(log.isInfoEnabled()){
				log.info("[API接口 - 搜索导航 返回值] = {}",JsonUtil.convertObjToStr(result));
			}*/
			return JsonUtil.convertObjToStr(result);
		}catch(MobileException me){
			log.error("[API接口 - 搜索导航  MobileException] = {}",me.getMessage());
			log.error("[API接口 - 搜索导航 返回值] = {}",JsonUtil.convertObjToStr(new MResultVO<>(me)));
			return JsonUtil.convertObjToStr(new MResultVO<>(me));
		}
	}
	
	
	/**
	 * 执行搜索
	 * @param queryTO
	 * @return
	 */
	@ResponseBody
	@RequestMapping(method = RequestMethod.GET)
	public String search(QuerySearch search){
		try{
			if(log.isInfoEnabled()){
				log.info("[API接口 - 执行搜索 入参] = {}",JsonUtil.convertObjToStr(search));
			}
			MResultVO<PageForSearch<SearchItemVO,List<SearchShopVO>>> result = searchAO.search(search);
			/*if(log.isInfoEnabled()){
				log.info("[API接口 - 执行搜索 返回值] = {}",JsonUtil.convertObjToStr(result));
			}*/
			return JsonUtil.convertObjToStr(result);
		}catch(MobileException me){
			log.error("[API接口 - 执行搜索  MobileException] = {}",me.getMessage());
			log.error("[API接口 - 执行搜索 返回值] = {}",JsonUtil.convertObjToStr(new MResultVO<>(me)));
			return JsonUtil.convertObjToStr(new MResultVO<>(me));
		}
	}
	
	/**
	 * 搜索结果 -筛选
	 * @param queryTO
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/condition",method = RequestMethod.GET)
	public String condition(QuerySearch search){
		try{
			if(log.isInfoEnabled()){
				log.info("[API接口 - 搜索结果 -筛选 入参] = {}",JsonUtil.convertObjToStr(search));
			}
			MResultVO<List<SearchConditionVO>> result = searchAO.condition(search);
			/*if(log.isInfoEnabled()){
				log.info("[API接口 - 搜索结果 -筛选 返回值] = {}",JsonUtil.convertObjToStr(result));
			}*/
			return JsonUtil.convertObjToStr(result);
		}catch(MobileException me){
			log.error("[API接口 - 搜索结果 -筛选  MobileException] = {}",me.getMessage());
			log.error("[API接口 - 搜索结果 -筛选 返回值] = {}",JsonUtil.convertObjToStr(new MResultVO<>(me)));
			return JsonUtil.convertObjToStr(new MResultVO<>(me));
		}
	}
}
