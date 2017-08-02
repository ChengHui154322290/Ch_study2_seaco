package com.tp.service.cms.redis;


/**
 * CMS Redis Service
 * 
 * @author szy
 */
public interface ICmsRedisService {

	/**
	 * 初始化Redis服务（主要包括公告资讯，广告管理查询）
	 */
	public void initRedis();

	/**
	 * 关闭初始化全量Redis锁（主要包括公告资讯，广告管理查询）
	 */
	public void closeRedis();
	
	/**
	 * 每5分钟检查一次redis，防止redis重启或者数据失效（主要包括公告资讯，广告管理查询）
	 */
	public void checkAdvanceRedis();

	/**
	 * 关闭检查redisRedis锁（主要包括公告资讯，广告管理查询）
	 */
	public void closeCheckAdvanceRedis();
	
}
