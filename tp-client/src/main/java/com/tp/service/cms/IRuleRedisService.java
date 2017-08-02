package com.tp.service.cms;

import java.util.List;
import java.util.Map;

import com.tp.model.cms.RedisIndexRule;
import com.tp.service.IBaseService;

/**
*/
public interface IRuleRedisService extends IBaseService<RedisIndexRule>{

	/**
	 * 
	 * @param paramMap
	 * @param cmsRedisIndexRuleDO
	 * @return
	 * @throws Exception
	 */
	Map<String, Object> selectRuleRedisPageQuery(Map<String, Object> paramMap,RedisIndexRule cmsRedisIndexRuleDO) throws Exception;
	
	/**
	 * 启动
	 * @param ids
	 * @return
	 * @throws Exception
	 */
	int openRuleRedisByIds(List<Long> ids) throws Exception;
	
	/**
	 * 禁用
	 * @param ids
	 * @return
	 * @throws Exception
	 */
	int noOpenRuleRedisByIds(List<Long> ids) throws Exception;
	
}
