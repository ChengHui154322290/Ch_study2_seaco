package com.tp.service.cms;

import java.util.List;
import java.util.Map;

import com.tp.model.cms.AnnounceInfo;

/**
* 公告资讯管理 Service
*/
public interface IAnnounceService {

	
	
	/*******************************下面是代码下移之前************************************************/
	List<AnnounceInfo> queryAnnounceInfo(AnnounceInfo cmsAnnounceInfoDO) throws Exception;
	
	int deleteAnnounceByIds(List<Long> ids) throws Exception;
	
	int addAnnounceByIds(AnnounceInfo cmsAnnounceInfoDO) throws Exception;
	
	int updateAnnounceByIds(AnnounceInfo cmsAnnounceInfoDO) throws Exception;
	
	public AnnounceInfo queryAnnounceInfoByID(Long id) throws Exception;
		
	Map<String, Object> selectAnnouncePageQuery(Map<String, Object> paramMap,AnnounceInfo cmsAnnounceInfoDO) throws Exception;
	/*******************************************************************************/
	
}
