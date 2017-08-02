package com.tp.dao.cms;

import com.tp.common.dao.BaseDao;
import com.tp.dto.cms.CmsSingleTepactivityDTO;
import com.tp.model.cms.SingleTepactivity;

public interface SingleTepactivityDao extends BaseDao<SingleTepactivity> {

	int deleteByTempleNodeId(CmsSingleTepactivityDTO cmsSingleTepactivityDTO);

	Long selectIsExistid(CmsSingleTepactivityDTO cmsSingleTepactivityDTO);

	int insertActivityId(CmsSingleTepactivityDTO cmsSingleTepactivityDTO);

	void updateTopicChangeDynamic(SingleTepactivity cmsSingleTepactivityDO);

}
