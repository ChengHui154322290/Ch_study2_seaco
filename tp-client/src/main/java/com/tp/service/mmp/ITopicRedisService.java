package com.tp.service.mmp;

import java.util.List;

import com.tp.dto.common.ResultInfo;

/**

 */
public interface ITopicRedisService {

	/**
	 * 初始化Redis服务
	 */
	public void initRedis();

	/**
	 * 初始化针对最后疯抢的Redis服务
	 */
	//public void initLastRashRedis();

	/**
	 * 保存更新的活动信息
	 * 
	 * @param topicId
	 * @param topicStatus
	 * @return
	 */
	public ResultInfo insertNewPromotion(Long topicId, int topicStatus);
	
	/**
	 * 批量保存更新的活动信息
	 * 
	 * @param topicIds
	 * @param topicStatus
	 * @return
	 */
	public ResultInfo insertNewPromotions(List<Long> topicIds);
	
	/**
	 * 关闭初始化全量Redis锁
	 */
	public void closeRedis();
	
	/**
	 * 关闭初始化针对疯抢处理的Redis锁
	 */
	public void closeLastRashRedis();
}
