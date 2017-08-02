package com.tp.service.cms;


/**
 * CMS Redis Service
 * 
 * @author szy
 */
public interface CmsIndexRedisPromotionService {

	/**
	 * promotion反推数据，传递"ALL",全量更新
	 */
	public void updateALLIndexRedis();
	
	/**
	 * promotion反推数据，传递单个缓存key值,单量更新
	 */
	public void updateSingleIndexRedis();
	
}
