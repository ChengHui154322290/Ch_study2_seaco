package com.tp.ptm.support;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.tp.common.vo.ptm.CacheKeyConstants;
import com.tp.redis.util.JedisCacheUtil;


/**
 * 频率计数器
 * 
 * @author 项硕
 * @version 2015年5月15日 下午11:13:17
 */
@Service("frequencyCounter")
public class FrequencyCounter {
	
	private static final Logger log = LoggerFactory.getLogger(FrequencyCounter.class);

	@Autowired
	private JedisCacheUtil jedisCacheUtil;
	
	/**
	 * 是否超载
	 * 
	 * @param appkey
	 * @param bizType
	 * @param seconds
	 * @param times
	 * @return
	 */
	public boolean overload(String appkey, BusinessType bizType, int seconds, int times) {
		Assert.notNull(bizType);
		
		StringBuilder sb = new StringBuilder(CacheKeyConstants.KEY_PREFIX);
		sb.append(appkey);
		sb.append(bizType.toString());
		String key = sb.toString();
		
		try {
			return jedisCacheUtil.watchMethodCall(key, Integer.valueOf(times).longValue(), seconds);
		} catch (Exception e) {
			log.error("是否超载调用redis失败", e);
		}
		return true;
	}
	
	/**
	 * 业务类型
	 * 
	 * @author 项硕
	 * @version 2015年5月15日 下午11:23:22
	 */
	public enum BusinessType {
		/** 查询订单 */
		QUERY_ORDER,	
		/** 发货 */
		DELIVER_GOODS,
		/** 查询快递公司 */
		QUERY_EXPRESS_COMPANY,
		/** 修改运单号 */
		MODIFY_EXPRESS_NO,
		/** 批量修改运单号 */
		BATCH_MODIFY_EXPRESS_NO,
		/**快递跟踪查询*/
		QUERY_ORDER_EXPRESS_LOG;
	}
}
