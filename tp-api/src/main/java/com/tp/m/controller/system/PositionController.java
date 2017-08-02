package com.tp.m.controller.system;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tp.m.ao.system.PositionAO;
import com.tp.m.base.MResultVO;
import com.tp.m.enums.MResultInfo;
import com.tp.m.exception.MobileException;
import com.tp.m.query.system.QueryPosition;
import com.tp.m.to.system.CityTO;
import com.tp.m.to.system.ProvinceTO;
import com.tp.m.util.AssertUtil;
import com.tp.m.util.JsonUtil;
import com.tp.m.util.StringUtil;
import com.tp.m.vo.system.ProvincesVO;

/**
 * 地理位置
 * @author zhuss
 *
 */
@Controller
@RequestMapping("/position")
public class PositionController {
	
	private static Logger log = LoggerFactory.getLogger(PositionController.class);

	@Autowired
	private PositionAO positionAO;
	
	/**
	 * 获得省份列表
	 * @return
	 */
	@RequestMapping(value = "/getprovlist",method = RequestMethod.GET)
	@ResponseBody
	public String getProvList(QueryPosition query){
		MResultVO<List<ProvincesVO>> reuslt = positionAO.getProvList();
		if(log.isInfoEnabled()){
			log.info("[API接口 - 获得省份列表 返回值] = {}",JsonUtil.convertObjToStr(reuslt));
		}
		return JsonUtil.convertObjToStr(reuslt);
	}
	
	/**
	 * 获取城市列表
	 * @return
	 */
	@RequestMapping(value = "/getarealist",method = RequestMethod.GET)
	@ResponseBody
	public String getCityList(QueryPosition position){
		try{
			if(log.isInfoEnabled()){
				log.info("[API接口 - 获取城市列表 入参] = {}",JsonUtil.convertObjToStr(position));
			}
			AssertUtil.notBlank(position.getId(), MResultInfo.ADDRESS_CITY_NULL);
			MResultVO<List<CityTO>> reuslt =positionAO.getCityList(StringUtil.getLongByStr(position.getId()));
			if(log.isInfoEnabled()){
				log.info("[API接口 - 获取城市列表 返回值] = {}",JsonUtil.convertObjToStr(reuslt));
			}
			return JsonUtil.convertObjToStr(reuslt);
		}catch(MobileException me){
			log.error("[API接口 - 获取城市列表  MobileException] = {}",me.getMessage());
			log.error("[API接口 - 获取城市列表  返回值] = {}",JsonUtil.convertObjToStr(new MResultVO<>(me)));
			return JsonUtil.convertObjToStr(new MResultVO<>(me));
		}
		
	}
	
	@RequestMapping(value = "/getareatree",method = RequestMethod.GET)
	@ResponseBody
	public String getAreaTree(QueryPosition position){
		try{
			if(StringUtil.isBlank(position.getId())){
				MResultVO<List<ProvincesVO>> result = positionAO.getProvList();
				if(result.success()){
					List<ProvincesVO> list = result.getData();
					List<CityTO> clist = new ArrayList<CityTO>();
					for(ProvincesVO province:list){
						for(ProvinceTO pc:province.getInfo()){
							CityTO city = new CityTO();
							city.setCode(pc.getProvcode());
							city.setName(pc.getProvince());
							clist.add(city);
						}
					}
					return JsonUtil.convertObjToStr(clist);
				}
				return "[]";
			}
			MResultVO<List<CityTO>> reuslt =positionAO.getCityList(StringUtil.getLongByStr(position.getId()));
			return JsonUtil.convertObjToStr(reuslt.getData());
		}catch(MobileException me){
			log.error("[API接口 - 获取城市列表  MobileException] = {}",me.getMessage());
			log.error("[API接口 - 获取城市列表  返回值] = {}",JsonUtil.convertObjToStr(new MResultVO<>(me)));
			return JsonUtil.convertObjToStr(new MResultVO<>(me));
		}
	}
}
