package com.tp.proxy.cms;

import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.tp.common.vo.cms.TempleConstant;
import com.tp.dto.cms.CmsRedisIndexRuleDTO;
import com.tp.model.cms.RedisIndexRule;
import com.tp.proxy.BaseProxy;
import com.tp.service.IBaseService;
import com.tp.service.cms.IRuleRedisService;

@Service
public class RuleRedisProxy extends BaseProxy{

	@Autowired
	IRuleRedisService ruleRedisService;
	
	@Override
	public IBaseService getService() {
		return ruleRedisService;
	}
	/**
	 * 启用
	 * @param ids
	 * @return
	 * @throws Exception
	 */
	public int openRuleRedis(List<Long> ids) throws Exception{
		return ruleRedisService.openRuleRedisByIds(ids);
	}
	
	/**
	 * 禁用
	 * @param ids
	 * @return
	 * @throws Exception
	 */
	public int noOpenRuleRedis(List<Long> ids) throws Exception{
		return ruleRedisService.noOpenRuleRedisByIds(ids);
	}
	
	@SuppressWarnings("unchecked")
	public JSONArray queryRuleRedisList(String params, RedisIndexRule cmsRedisIndexRuleDO)
			throws ParseException, Exception {
		JSONObject jSONObject = new JSONObject();
		if(params != null){
			Object obj = JSONValue.parse(params);
			jSONObject = (JSONObject)obj;
			if(!StringUtils.isEmpty(jSONObject.get("functionName"))){
				cmsRedisIndexRuleDO.setFunctionName(jSONObject.get("functionName").toString());
			}
			if(!StringUtils.isEmpty(jSONObject.get("area"))){
				cmsRedisIndexRuleDO.setArea(jSONObject.get("area").toString());
			}
			if(!StringUtils.isEmpty(jSONObject.get("platformType") )){
				cmsRedisIndexRuleDO.setPlatformType(jSONObject.get("platformType").toString());
			}
			if(!StringUtils.isEmpty(jSONObject.get("status") )){
				cmsRedisIndexRuleDO.setStatus(Integer.parseInt(jSONObject.get("status").toString()));
			}
		}
		
		int pageId = Integer.parseInt(TempleConstant.CMS_START_PAGE);
    	int pageSize = Integer.parseInt(TempleConstant.CMS_PAGE_SIZE);
    	if(jSONObject.get("pageId") != null && !"".equals(jSONObject.get("pageId"))){
    		pageId = Integer.parseInt(jSONObject.get("pageId").toString());
    	}
		
    	Map<String, Object> paramMap = new HashMap<String,Object>();
		paramMap.put("functionName", jSONObject.get("titleName"));
		paramMap.put("area", jSONObject.get("position"));
		paramMap.put("platformType", jSONObject.get("startdate"));
		paramMap.put("status", jSONObject.get("enddate"));
		paramMap.put("start", (pageId > 1 ? (pageId - 1) * pageSize : 0));
		paramMap.put("pageSize", pageSize);
		Map<String, Object> map = ruleRedisService.selectRuleRedisPageQuery(paramMap,cmsRedisIndexRuleDO);
		List<CmsRedisIndexRuleDTO> list = (List<CmsRedisIndexRuleDTO>) map.get("list");
		Long counts = (Long) map.get("counts");
		
		int countnum = (int)Math.ceil(counts/Double.parseDouble(pageSize+""));
    	JSONArray mapList = new JSONArray();
    	for(int i=0;i<list.size();i++){
    		CmsRedisIndexRuleDTO obj = list.get(i);
    		obj.setPageNo(pageId);
    		obj.setPageSize(pageSize);
    		obj.setTotalCount(countnum);
    		obj.setTotalCountNum(counts.intValue());
    		mapList.add(obj);
    	}
		return mapList;
	}
	
	public RedisIndexRule selectById(Long id) throws Exception{
		return ruleRedisService.queryById(id);
	}
}
