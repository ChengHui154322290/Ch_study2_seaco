package com.tp.dao.cms;

import java.util.List;

import com.tp.common.dao.BaseDao;
import com.tp.model.cms.RedisIndexRule;

public interface RedisIndexRuleDao extends BaseDao<RedisIndexRule> {

	int openByIds(List<Long> ids);

	int noOpenByIds(List<Long> ids);

}
