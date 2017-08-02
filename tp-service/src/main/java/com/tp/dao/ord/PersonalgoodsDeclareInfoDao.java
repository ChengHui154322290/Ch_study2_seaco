package com.tp.dao.ord;

import java.util.List;
import java.util.Map;

import com.tp.common.dao.BaseDao;
import com.tp.model.ord.PersonalgoodsDeclareInfo;

public interface PersonalgoodsDeclareInfoDao extends BaseDao<PersonalgoodsDeclareInfo> {
	/**
	 *	查询未申报个人物品 
	 */
	List<PersonalgoodsDeclareInfo> queryUndeclaredPersonalGoods(Map<String, Object> map);
	
	Integer updateDirectmailPersonalgoodsDeclareInfos(List<PersonalgoodsDeclareInfo> infos);
}
