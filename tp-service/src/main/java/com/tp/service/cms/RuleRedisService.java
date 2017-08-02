package com.tp.service.cms;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.dao.BaseDao;
import com.tp.dao.cms.RedisIndexRuleDao;
import com.tp.model.cms.RedisIndexRule;
import com.tp.service.BaseService;
import com.tp.service.cms.IRuleRedisService;


/**
* 广告管理 Service
* @author szy
*/
@Service(value="ruleRedisService")
public class RuleRedisService extends BaseService<RedisIndexRule> implements IRuleRedisService{

	@Autowired
	RedisIndexRuleDao redisIndexRuleDao;

	@Override
	public Map<String, Object> selectRuleRedisPageQuery(Map<String, Object> paramMap, RedisIndexRule cmsRedisIndexRuleDO) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("counts", redisIndexRuleDao.queryByObjectCount(cmsRedisIndexRuleDO));
		map.put("list", redisIndexRuleDao.queryByParam(paramMap));
		return map;
	}

	@Override
	public int openRuleRedisByIds(List<Long> ids) throws Exception {
		int count = redisIndexRuleDao.openByIds(ids);
		return count;
	}

	@Override
	public int noOpenRuleRedisByIds(List<Long> ids) throws Exception {
		int count = redisIndexRuleDao.noOpenByIds(ids);
		return count;
	}

	@Override
	public BaseDao<RedisIndexRule> getDao() {
		return redisIndexRuleDao;
	}
}
