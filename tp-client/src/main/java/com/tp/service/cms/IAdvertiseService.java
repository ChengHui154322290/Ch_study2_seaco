package com.tp.service.cms;

import java.util.List;
import java.util.Map;

import com.tp.model.cms.AdvertiseInfo;
import com.tp.model.cms.AdvertiseType;
import com.tp.service.IBaseService;

/**
* 广告管理 Service
* @author szy
*/
public interface IAdvertiseService extends IBaseService<AdvertiseInfo> {

	
	
	/**********************************下面是代码下移之前*************************************/
	List<AdvertiseInfo> queryAdvertiseInfo(AdvertiseInfo cmsAdvertiseInfoDO) throws Exception;
	
	List<AdvertiseType> queryAdvertType(AdvertiseType advertTypeDO) throws Exception;
	
	int deleteAdvertiseByIds(List<Long> ids) throws Exception;
	
	int openAdvertiseByIds(List<Long> ids) throws Exception;
	
	int noOpenAdvertiseByIds(List<Long> ids) throws Exception;
	
	AdvertiseInfo addAdvertiseByIds(AdvertiseInfo cmsAdvertiseInfoDO) throws Exception;
	
	int addAdvertType(AdvertiseType cmsAdvertTypeDO) throws Exception;
	
	int updateAdvertType(AdvertiseType cmsAdvertTypeDO) throws Exception;
	
	int updateAdvertiseByIds(AdvertiseInfo cmsAdvertiseInfoDO) throws Exception;
	
	AdvertiseInfo selectById(Long id) throws Exception;
	
	AdvertiseType selectAdvertTypeById(Long id) throws Exception;
	
	Map<String, Object> selectAdvertPageQuery(Map<String, Object> paramMap,AdvertiseInfo cmsAdvertiseInfoDO) throws Exception;
	/***************************************************************************************/
	
}
