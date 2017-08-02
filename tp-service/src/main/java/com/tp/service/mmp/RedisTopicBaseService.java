/**
 * 
 */
package com.tp.service.mmp;


import com.tp.common.vo.mmp.TopicRedisKeyConstant;
import com.tp.exception.ServiceException;
import com.tp.model.mmp.Topic;
import com.tp.model.mmp.TopicItem;
import com.tp.redis.util.JedisCacheUtil;
import com.tp.result.mmp.TopicItemInfoResult;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 *
 */
@Service
public class RedisTopicBaseService {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private JedisCacheUtil jedisCacheUtil;

	public void releaseKeyList()  {
		if (jedisCacheUtil.keyExists(TopicRedisKeyConstant.PR_PROMOTION_KEYS)) {
			jedisCacheUtil
					.deleteCacheKey(TopicRedisKeyConstant.PR_PROMOTION_KEYS);
		}
		if (jedisCacheUtil
				.keyExists(TopicRedisKeyConstant.PR_PROMOTION_ITEM_KEYS)) {
			jedisCacheUtil
					.deleteCacheKey(TopicRedisKeyConstant.PR_PROMOTION_ITEM_KEYS);
		}
		if (jedisCacheUtil
				.keyExists(TopicRedisKeyConstant.PR_PROMOTION_AVALIABLE_KEYS)) {
			jedisCacheUtil
					.deleteCacheKey(TopicRedisKeyConstant.PR_PROMOTION_AVALIABLE_KEYS);
		}
		if (jedisCacheUtil.keyExists(TopicRedisKeyConstant.PR_FOR_ITEM_KEYS)) {
			jedisCacheUtil
					.deleteCacheKey(TopicRedisKeyConstant.PR_FOR_ITEM_KEYS);
		}
	}

	@SuppressWarnings("unchecked")
	public Long insert(Topic Topic, int expireTime) {
		List<String> topicIds = (List<String>) jedisCacheUtil
				.getCache(TopicRedisKeyConstant.PR_PROMOTION_KEYS);
		if (null == topicIds) {
			topicIds = new ArrayList<String>();
		}
		if (Topic == null || Topic.getId() == null) {
			return null;
		}
		String topicKey = TopicRedisKeyConstant.PR_PROMOTION_PREFIX
				+ String.valueOf(Topic.getId());
		if (expireTime > TopicRedisKeyConstant.DAYS_EXPIRE) {
			expireTime = TopicRedisKeyConstant.DAYS_EXPIRE;
		}
		boolean result = jedisCacheUtil.setCache(topicKey, Topic, expireTime);
		if (result) {
			logger.debug(String.format("save topic id:[%s]", Topic.getId()));
			processingInsertTopic(Topic);
			if (!topicIds.contains(topicKey)) {
				topicIds.add(topicKey);
				jedisCacheUtil.setCache(
						TopicRedisKeyConstant.PR_PROMOTION_KEYS, topicIds,
						TopicRedisKeyConstant.DAYS_EXPIRE);
			}
			return Topic.getId();
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	public boolean insertMore(List<Topic> topicDOs) {
		if (topicDOs == null || topicDOs.size() == 0) {
			return true;
		}
		List<String> topicIds = (List<String>) jedisCacheUtil
				.getCache(TopicRedisKeyConstant.PR_PROMOTION_KEYS);
		if (null == topicIds) {
			topicIds = new ArrayList<String>();
		}
		Date now = new Date();
		for (Topic Topic : topicDOs) {
			int expireTime = TopicRedisKeyConstant.DAYS_EXPIRE;
			if (null != Topic.getEndTime()) {
				expireTime = calcTimeDiff(Topic.getEndTime(), now, expireTime);
			}
			String topicKey = TopicRedisKeyConstant.PR_PROMOTION_PREFIX
					+ String.valueOf(Topic.getId());
			if (expireTime > TopicRedisKeyConstant.DAYS_EXPIRE) {
				expireTime = TopicRedisKeyConstant.DAYS_EXPIRE;
			}
			boolean result = jedisCacheUtil.setCache(topicKey, Topic,
					expireTime);
			if (result) {
				processingInsertTopic(Topic);
				if (!topicIds.contains(topicKey)) {
					topicIds.add(topicKey);
				}
			}
		}

		return jedisCacheUtil.setCache(TopicRedisKeyConstant.PR_PROMOTION_KEYS,
				topicIds, TopicRedisKeyConstant.DAYS_EXPIRE);
	}

	@SuppressWarnings("unchecked")
	public Long insertItemMore(List<TopicItem> topicItemDOs,
			Map<Long, Integer> expireTimes) {
		if (topicItemDOs == null || topicItemDOs.size() == 0) {
			return null;
		}
		for (TopicItem TopicItem : topicItemDOs) {
			String topicItemKey = TopicRedisKeyConstant.PR_PROMOTION_ITEM_PREFIX
					+ String.valueOf(TopicItem.getId());
			int expireTime = TopicRedisKeyConstant.DAYS_EXPIRE;
			if (expireTimes.containsKey(TopicItem.getTopicId())) {
				expireTime = expireTimes.get(TopicItem.getTopicId());
			}
			boolean result = jedisCacheUtil.setCache(topicItemKey, TopicItem,
					expireTime);
			if (result) {
				processingInsertTopicItem(TopicItem, expireTime);
				return TopicItem.getId();
			}
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	public Long insert(TopicItem TopicItem, int expireTime)
			{
		if (TopicItem == null || TopicItem.getTopicId() == null
				|| StringUtils.isBlank(TopicItem.getSku())) {
			// throw new DAOException("活动商品信息无效");
			return null;
		}
		String topicItemKey = TopicRedisKeyConstant.PR_PROMOTION_ITEM_PREFIX
				+ String.valueOf(TopicItem.getId());
		if (expireTime > TopicRedisKeyConstant.DAYS_EXPIRE) {
			expireTime = TopicRedisKeyConstant.DAYS_EXPIRE;
		}
		boolean result = jedisCacheUtil.setCache(topicItemKey, TopicItem,
				expireTime);
		if (result) {
			processingInsertTopicItem(TopicItem, expireTime);
			return TopicItem.getId();
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	public Integer deleteTopicById(Long id) {
		if (null == id) {
			throw new ServiceException("指定活动序号异常");
		}
		Long resultKey = 0L;
		String topicKey = TopicRedisKeyConstant.PR_PROMOTION_PREFIX
				+ String.valueOf(id);
		if (jedisCacheUtil.keyExists(topicKey)) {
			resultKey = jedisCacheUtil.deleteCacheKey(topicKey);
			if (jedisCacheUtil
					.keyExists(TopicRedisKeyConstant.PR_PROMOTION_KEYS)) {
				List<String> topicIds = (List<String>) jedisCacheUtil
						.getCache(TopicRedisKeyConstant.PR_PROMOTION_KEYS);
				if (topicIds.contains(topicKey)) {
					topicIds.remove(topicKey);
					jedisCacheUtil.setCache(
							TopicRedisKeyConstant.PR_PROMOTION_KEYS, topicIds,
							TopicRedisKeyConstant.DAYS_EXPIRE);
				}
			}
		} else {
			this.recordLostTopic(id);
		}

		return null != resultKey ? 1 : 0;
	}

	public Integer deleteTopicItem(Long topicId, String sku)
			{
		Long resultKey = 0L;
		String topicItemKey = topicId + TopicRedisKeyConstant.TOPIC_INFO_SPLIT
				+ sku;
		if (jedisCacheUtil.keyExists(topicItemKey)) {
			resultKey = jedisCacheUtil.deleteCacheKey(topicItemKey);
		} else {
			this.recordLostTopicItem(topicItemKey);
		}
		return null != resultKey ? 1 : 0;
	}

	public TopicItem selectTopicItemById(Long topicId, String sku)
			{
		String topicItemKey = topicId + TopicRedisKeyConstant.TOPIC_INFO_SPLIT
				+ sku;
		if (jedisCacheUtil.keyExists(topicItemKey)) {
			return getTopicItem(jedisCacheUtil.getCache(topicItemKey));
		} else {
			this.recordLostTopicItem(topicItemKey);
			throw new ServiceException("查询数据不存在");
		}
	}

	public Topic selectTopicById(Long id) {
		if (jedisCacheUtil.keyExists(String.valueOf(id))) {
			return (Topic) jedisCacheUtil.getCache(String.valueOf(id));
		} else {
			this.recordLostTopic(id);
			throw new ServiceException("查询数据不存在");
		}
	}

	public boolean setLastRash(List<Long> rashIds, String key) {
		return jedisCacheUtil.setCache(key, rashIds,
				TopicRedisKeyConstant.RASH_EXPIRE);
	}

	@SuppressWarnings("unchecked")
	public boolean addLastRash(Long rashId, String key) {
		List<Long> rashIds = (List<Long>) jedisCacheUtil.getCache(key);
		if (rashIds == null) {
			rashIds = new ArrayList<Long>();
		}
		if (!rashIds.contains(rashId)) {
			rashIds.add(rashId);
			return jedisCacheUtil.setCache(key, rashIds,
					TopicRedisKeyConstant.RASH_EXPIRE);
		}
		return true;
	}

	public boolean setAvaliableTopicIds(List<Long> topicIds) {
		return jedisCacheUtil.setCache(
				TopicRedisKeyConstant.PR_PROMOTION_AVALIABLE_KEYS, topicIds,
				TopicRedisKeyConstant.DAYS_EXPIRE);
	}

	@SuppressWarnings("unchecked")
	public boolean addAvaliableTopicId(Long topicId) {
		List<Long> topicIds = (List<Long>) jedisCacheUtil
				.getCache(TopicRedisKeyConstant.PR_PROMOTION_AVALIABLE_KEYS);
		if (topicIds == null) {
			topicIds = new ArrayList<Long>();
		}
		if (!topicIds.contains(topicId)) {
			topicIds.add(topicId);
			return jedisCacheUtil.setCache(
					TopicRedisKeyConstant.PR_PROMOTION_AVALIABLE_KEYS,
					topicIds, TopicRedisKeyConstant.DAYS_EXPIRE);
		}
		return true;
	}

	@SuppressWarnings("unchecked")
	public boolean addAvaliableTopicIds(List<Long> topicIds) {
		List<Long> avaTopicIds = (List<Long>) jedisCacheUtil
				.getCache(TopicRedisKeyConstant.PR_PROMOTION_AVALIABLE_KEYS);
		if (avaTopicIds == null) {
			avaTopicIds = new ArrayList<Long>();
		}
		boolean hasChange = false;
		for (Long tid : topicIds) {
			if (!avaTopicIds.contains(tid)) {
				avaTopicIds.add(tid);
				hasChange = true;
			}
		}
		if (hasChange) {
			return jedisCacheUtil.setCache(
					TopicRedisKeyConstant.PR_PROMOTION_AVALIABLE_KEYS,
					avaTopicIds, TopicRedisKeyConstant.DAYS_EXPIRE);
		}
		return true;
	}

	private void processingInsertTopic(Topic topic) {
		// List<Long> auditKey = null;
		// if (jedisCacheUtil.keyExists(PR_PROMOTION_AVALIABLE_KEYS)) {
		// auditKey = (List<Long>) jedisCacheUtil
		// .getCache(PR_PROMOTION_AVALIABLE_KEYS);
		// } else {
		// auditKey = new ArrayList<Long>();
		// }
		// if (auditKey != null && topic != null
		// && !auditKey.contains(topic.getId())) {
		// auditKey.add(topic.getId());
		// jedisCacheUtil.setCache(PR_PROMOTION_AVALIABLE_KEYS, auditKey,
		// DAYS_EXPIRE);
		// }
	}

	private void processingInsertTopicItem(TopicItem topicItem, int expireTime) {
		// Map<String, String> pItemKeyForItems = null;
		// if (jedisCacheUtil != null
		// && jedisCacheUtil
		// .keyExists(TopicRedisKeyConstant.PR_FOR_ITEM_KEYS)) {
		// pItemKeyForItems = (Map<String, String>) jedisCacheUtil
		// .getCache(TopicRedisKeyConstant.PR_FOR_ITEM_KEYS);
		// }
		// if (pItemKeyForItems == null) {
		// pItemKeyForItems = new HashMap<String, String>();
		// }
		// logger.debug(String.format("save topicItem for item id:[%s]",
		// topicItem.getTopicId() + TopicRedisKeyConstant.TOPIC_INFO_SPLIT
		// + topicItem.getSku()));
		// pItemKeyForItems.put(
		// String.valueOf(topicItem.getTopicId()
		// + TopicRedisKeyConstant.TOPIC_INFO_SPLIT
		// + topicItem.getSku()),
		// TopicRedisKeyConstant.PR_PROMOTION_ITEM_PREFIX
		// + topicItem.getId());
		// jedisCacheUtil.setCache(TopicRedisKeyConstant.PR_FOR_ITEM_KEYS,
		// pItemKeyForItems, TopicRedisKeyConstant.DAYS_EXPIRE);
		processingTopicItemForItem(topicItem, expireTime);
	}

	private void processingTopicItemForItem(TopicItem topicItem,
			int expireTime) {
		if (topicItem == null) {
			return;
		}
		if (jedisCacheUtil == null
				|| !jedisCacheUtil
						.keyExists(TopicRedisKeyConstant.PR_PROMOTION_PREFIX
								+ String.valueOf(topicItem.getTopicId()))) {
			return;
		}
		Topic topic = (Topic) jedisCacheUtil
				.getCache(TopicRedisKeyConstant.PR_PROMOTION_PREFIX
						+ String.valueOf(topicItem.getTopicId()));
		TopicItemInfoResult result = new TopicItemInfoResult();
		result.setSku(topicItem.getSku());
		result.setItemId(topicItem.getItemId());
		if (topicItem.getStockAmount() != null)
			result.setStockAmount(topicItem.getStockAmount());
		result.setLimitAmount(topicItem.getLimitAmount());
		result.setTopicPrice(topicItem.getTopicPrice());
		result.setTopicImage(topicItem.getTopicImage());
		result.setSalePrice(topicItem.getSalePrice());
		result.setTopicId(topicItem.getTopicId());
		result.setTopicName(topic.getName());
		result.setTopicStatus(topic.getStatus());
		result.setLastingType(topic.getLastingType());
		result.setStartTime(topic.getStartTime());
		result.setEndTime(topic.getEndTime());
		result.setTopicType(topic.getType());
		result.setStockLocationId(topicItem.getStockLocationId());
		result.setStockLocationName(topicItem.getStockLocation());
		String storeKey = topicItem.getTopicId()
				+ TopicRedisKeyConstant.TOPIC_INFO_SPLIT + topicItem.getSku()
				+ TopicRedisKeyConstant.TOPIC_INFO_SPLIT + String.valueOf(topicItem.getStockLocationId());
		String storeKey2 = topicItem.getTopicId()
				+ TopicRedisKeyConstant.TOPIC_INFO_SPLIT + topicItem.getSku();
		jedisCacheUtil.setCache(storeKey,
				result, expireTime);
		jedisCacheUtil.setCache(storeKey2,
				result, expireTime);
	}

	// private void processingUpdateTopic(Topic topic) {
	//
	// }

	// @SuppressWarnings("unchecked")
	// private void processingUpldateTopicItem(TopicItem topicItem) {
	// Map<String, String> pItemKeyForItems = new HashMap<String, String>();
	// if (jedisCacheUtil != null
	// && jedisCacheUtil
	// .keyExists(TopicRedisKeyConstant.PR_FOR_ITEM_KEYS)) {
	// pItemKeyForItems = (Map<String, String>) jedisCacheUtil
	// .getCache(TopicRedisKeyConstant.PR_FOR_ITEM_KEYS);
	// }
	// if (null != pItemKeyForItems) {
	// pItemKeyForItems.put(
	// String.valueOf(topicItem.getTopicId()
	// + TopicRedisKeyConstant.TOPIC_INFO_SPLIT
	// + topicItem.getSku()),
	// TopicRedisKeyConstant.PR_PROMOTION_ITEM_PREFIX
	// + topicItem.getId());
	// }
	// }

	private boolean setTopicItemForItem(TopicItem topicItem) {
		if (null == topicItem || null == topicItem.getId()
				|| null == topicItem.getTopicId()
				|| StringUtils.isBlank(topicItem.getSku())) {
			return false;
		}
		String forItemKey = String.valueOf(topicItem.getTopicId())
				+ TopicRedisKeyConstant.TOPIC_INFO_SPLIT + topicItem.getSku();
		String pItemKey = TopicRedisKeyConstant.PR_PROMOTION_ITEM_PREFIX
				+ String.valueOf(topicItem.getId());
		return jedisCacheUtil.setCache(forItemKey, pItemKey);
	}

	// public Long selectCountDynamic(Topic Topic) {
	// return null;
	// }
	//
	// public List<Topic> selectDynamic(Topic Topic) {
	// if (null == Topic) {
	// throw new ServiceException("过滤条件对象不允许为空!");
	// }
	// if (jedisCacheUtil.keyExists(RedisTopicDAO.PR_PROMOTIONITEM_KEYS)) {
	// List<Topic> returnTopics = new ArrayList<Topic>();
	// @SuppressWarnings("unchecked")
	// List<Long> topicIds = (List<Long>) jedisCacheUtil
	// .getCache(RedisTopicDAO.PR_PROMOTIONITEM_KEYS);
	// for (Long topicId : topicIds) {
	// if (jedisCacheUtil.keyExists(String.valueOf(topicId))) {
	// Topic topicCache = getTopic(jedisCacheUtil
	// .getCache(String.valueOf(topicId)));
	// // 匹配符合要求的对象
	// if (null == topicCache) {
	// throw new ServiceException("获取的促销活动为空");
	// }
	// if (null != Topic.getId() && 0 != Topic.getId()
	// && Topic.getId() != topicCache.getId()) {
	// continue;
	// }
	// if (!StringUtils.isBlank(Topic.getName())
	// && !StringUtils.isBlank(topicCache.getName())
	// && !topicCache.getName()
	// .contains(Topic.getName())) {
	// continue;
	// }
	// if (!StringUtils.isBlank(Topic.getNumber())
	// && !StringUtils.isBlank(topicCache.getNumber())
	// && !topicCache.getNumber().contains(
	// Topic.getNumber())) {
	// continue;
	// }
	// if (!StringUtils.isBlank(Topic.getAreaStr())
	// && !StringUtils.isBlank(topicCache.getAreaStr())
	// && !topicCache.getAreaStr().contains(
	// Topic.getAreaStr())) {
	// continue;
	// }
	// if (!StringUtils.isBlank(Topic.getBrandName())
	// && !StringUtils.isBlank(topicCache.getBrandName())
	// && !topicCache.getBrandName().contains(
	// Topic.getBrandName())) {
	// continue;
	// }
	// } else {
	// recordLostId(topicId);
	// }
	// }
	// }
	// TODO Auto-generated method stub
	// return null;
	// }

	/**
	 * 记录丢失的topicId
	 * 
	 * @param topicId
	 */
	@SuppressWarnings("unchecked")
	private void recordLostTopic(Long topicId) {
		List<Long> topicLostIds = null;
		if (!jedisCacheUtil
				.keyExists(TopicRedisKeyConstant.PR_PROMOTION_LOST_KEYS)) {
			topicLostIds = new ArrayList<Long>();
		} else {
			topicLostIds = (List<Long>) jedisCacheUtil
					.getCache(TopicRedisKeyConstant.PR_PROMOTION_LOST_KEYS);
		}
		if (null != topicLostIds && !topicLostIds.contains(topicId)) {
			topicLostIds.add(topicId);
			jedisCacheUtil.setCache(
					TopicRedisKeyConstant.PR_PROMOTION_LOST_KEYS, topicLostIds,
					TopicRedisKeyConstant.DAYS_EXPIRE);
		}
	}

	/**
	 * 记录丢失的topicId
	 * 
	 * @param topicId
	 */
	@SuppressWarnings("unchecked")
	private void recordLostTopicItem(String topicItemKey) {
		List<String> topicItemLost = null;
		if (!jedisCacheUtil
				.keyExists(TopicRedisKeyConstant.PR_PROMOTION_ITEM_LOST_KEYS)) {
			topicItemLost = new ArrayList<String>();
		} else {
			topicItemLost = (List<String>) jedisCacheUtil
					.getCache(TopicRedisKeyConstant.PR_PROMOTION_ITEM_LOST_KEYS);
		}
		if (null != topicItemLost && !topicItemLost.contains(topicItemKey)) {
			topicItemLost.add(topicItemKey);
			jedisCacheUtil.setCache(
					TopicRedisKeyConstant.PR_PROMOTION_ITEM_LOST_KEYS,
					topicItemLost);
		}
	}

	private Topic getTopic(Object obj) {
		if (obj == null) {
			return null;
		}
		if (obj instanceof Topic) {
			return (Topic) obj;
		}
		return null;
	}

	private TopicItem getTopicItem(Object obj) {
		if (obj == null) {
			return null;
		}
		if (obj instanceof TopicItem) {
			return (TopicItem) obj;
		}
		return null;
	}

	private int calcTimeDiff(Date endTime, Date now, int expireTime) {
		long diffMillion = endTime.getTime() - now.getTime();
		if (diffMillion > 0) {
			expireTime = Integer.valueOf(String.valueOf(diffMillion / 1000));
		}
		return expireTime;
	}
}
