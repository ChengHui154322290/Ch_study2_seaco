/**
 * 
 */
package com.tp.service.mmp;

import com.tp.common.vo.mmp.TopicRedisKeyConstant;
import com.tp.dao.mmp.TopicDao;
import com.tp.dao.mmp.TopicItemDao;
import com.tp.dto.common.FailInfo;
import com.tp.dto.common.ResultInfo;
import com.tp.dto.mmp.enums.DeletionStatus;
import com.tp.dto.mmp.enums.TopicStatus;
import com.tp.exception.ServiceException;
import com.tp.model.mmp.Topic;
import com.tp.model.mmp.TopicItem;
import com.tp.redis.util.JedisCacheUtil;
import com.tp.service.mmp.ITopicRedisService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 *
 */
@Service
public class TopicRedisService implements ITopicRedisService {
	

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private JedisCacheUtil jedisCacheUtil;

	@Autowired
	private RedisTopicBaseService redisTopicBaseService;

	@Autowired
	private TopicDao topicDao;

	@Autowired
	private TopicItemDao topicItemDao;

	private static final String RUN_REDIS_COLLECT = "runPromotionRedisCollect";

	private static final String RUN_RASH_COLLECT = "runPromotionRashCollect";

	@Override
	public void initRedis() {
		boolean lock = jedisCacheUtil.lock(RUN_REDIS_COLLECT);// 获得锁
		if (logger.isDebugEnabled()) {
			logger.info(String.format(
					"[TopicRedisService.initRedis]lock for init redis value:[%s]......"
							+ new Date(), lock));
		}
		if (lock) {
			try {
				if (logger.isDebugEnabled()) {
					logger.info("[initRedis]start init promotion redis......" + new Date());
				}
				redisTopicBaseService.releaseKeyList();
				List<Topic> topicDos = topicDao.getBusAvailableTopicList();
				if (topicDos == null || topicDos.size() == 0) {
					return;
				}
				Map<Long, Integer> expireTimeMap = new HashMap<Long, Integer>();
				List<Long> topicIds = new ArrayList<Long>(topicDos.size());
				Date now = new Date();
				for (Topic topic : topicDos) {
					int expireTime = TopicRedisKeyConstant.DAYS_EXPIRE;
					if (null != topic.getEndTime()) {
						expireTime = calcTimeDiff(topic.getEndTime(), now,
								expireTime);
					}
					expireTimeMap.put(topic.getId(), expireTime);
					redisTopicBaseService.insert(topic, expireTime);
					if (TopicStatus.PASSED.ordinal() == topic.getStatus()) {
						topicIds.add(topic.getId());
					}
				}
				redisTopicBaseService.setAvaliableTopicIds(topicIds);
				List<TopicItem> itemDOs = topicItemDao
						.getTopicItemByTopicIds(topicIds);
				for (TopicItem TopicItem : itemDOs) {
					if (expireTimeMap != null
							&& expireTimeMap.containsKey(TopicItem
									.getTopicId())) {
						redisTopicBaseService.insert(TopicItem,
								expireTimeMap.get(TopicItem.getTopicId()));
					} else {
						redisTopicBaseService.insert(TopicItem,
								TopicRedisKeyConstant.DAYS_EXPIRE);
					}
				}
				if (logger.isDebugEnabled()) {
					logger.info("[initRedis]end init promotion redis......"
							+ new Date());
				}
			} catch (ServiceException e) {
				logger.error(e.getMessage(), e);
			} catch (Exception e) {
				if (lock) {
					closeRedis();
				}
			} finally {
				if (lock) {
					closeRedis();
				}
			}
		}
	}

	@Override
	public ResultInfo insertNewPromotion(Long topicId, int topicStatus) {
		try {
			if (logger.isDebugEnabled()) {
				logger.info("[insertNewPromotion]更新单个活动及活动商品信息............");
				logger.info(String.format("[insertNewPromotion]更新单个活动:id=[%s]",
						topicId));
			}
			Topic selectDO = new Topic();
			selectDO.setId(topicId);
			selectDO.setDeletion(DeletionStatus.NORMAL.ordinal());
			if (topicStatus > -1) {
				selectDO.setStatus(topicStatus);
			}
			List<Topic> topics = topicDao.queryByObject(selectDO);
			if (topics == null || topics.size() < 1) {
				return new ResultInfo(new FailInfo("fail"));
			}
			Topic Topic = topics.get(0);
			Date now = new Date();
			int expireTime = TopicRedisKeyConstant.DAYS_EXPIRE;
			if (null != Topic.getEndTime()) {
				expireTime = calcTimeDiff(Topic.getEndTime(), now, expireTime);
			}
			// TODO:存取异常处理
			redisTopicBaseService.insert(Topic, expireTime);
			redisTopicBaseService.addAvaliableTopicId(topicId);
			TopicItem selectItem = new TopicItem();
			selectItem.setDeletion(DeletionStatus.NORMAL.ordinal());
			selectItem.setTopicId(topicId);
			List<TopicItem> itemDOs = topicItemDao.queryByObject(selectItem);
			for (TopicItem TopicItem : itemDOs) {
				redisTopicBaseService.insert(TopicItem, expireTime);
			}
			Calendar rashRange = Calendar.getInstance();
			rashRange.setTime(now);
			rashRange.add(Calendar.HOUR_OF_DAY, 24);
			// 判断是否符合疯抢条件
			if (null != Topic.getEndTime()
					&& Topic.getEndTime().compareTo(rashRange.getTime()) < 1) {
				//addNewPromotionToRash(Topic);
			}
			if (logger.isDebugEnabled()) {
				logger.info("[insertNewPromotion]更新单个活动及活动商品信息结束............");
			}
		} catch (ServiceException e) {
			logger.error(e.getMessage(), e);
			return new ResultInfo(new FailInfo(e.getMessage()));
		}
		return new ResultInfo();
	}



	@Override
	public void closeRedis() {
		jedisCacheUtil.unLock(RUN_REDIS_COLLECT);// 释放锁
		if (logger.isDebugEnabled()) {
			logger.info("[closeRedis]release lock for init redis......"
					+ new Date());
		}
	}

	@Override
	public void closeLastRashRedis() {
		jedisCacheUtil.unLock(RUN_RASH_COLLECT);// 释放锁
		if (logger.isDebugEnabled()) {
			logger.info("[closeLastRashRedis]release lock for rash redis......"
					+ new Date());
		}
	}

	private int calcTimeDiff(Date endTime, Date now, int expireTime) {
		long diffMillion = endTime.getTime() - now.getTime();
		if (diffMillion > 0) {
			expireTime = Integer.valueOf(String.valueOf(diffMillion / 1000));
		}
		return expireTime;
	}



	@Override
	public ResultInfo insertNewPromotions(List<Long> topicIds) {
		if (logger.isDebugEnabled()) {
			logger.info("[insertNewPromotions]start upgrade batch topic............");
			for (Long tid : topicIds) {
				logger.info("[insertNewPromotions]topic id.....:" + tid);
			}
		}
		List<Topic> topics = topicDao.queryTopicInfoList(topicIds);
		List<TopicItem> topicItems = topicItemDao.getTopicItemByTopicIds(
				topicIds);
		if (topics == null || topics.size() < 1) {
			return new ResultInfo(new FailInfo("fail"));
		}
		List<Long> avaTopicIds = new ArrayList<Long>();
		Map<Long, Integer> topicExpireTimes = new HashMap<Long, Integer>();
		Date now = new Date();
		for (Topic topic : topics) {
			int expireTime = TopicRedisKeyConstant.DAYS_EXPIRE;
			if (null != topic.getEndTime()) {
				expireTime = calcTimeDiff(topic.getEndTime(), now, expireTime);
			}
			topicExpireTimes.put(topic.getId(), expireTime);
			avaTopicIds.add(topic.getId());
		}
		try {
			redisTopicBaseService.insertMore(topics);
			redisTopicBaseService.insertItemMore(topicItems, topicExpireTimes);
			redisTopicBaseService.addAvaliableTopicIds(avaTopicIds);
		} catch (ServiceException e) {
			logger.error(e.getMessage(), e);
		}
		if (logger.isDebugEnabled()) {
			logger.info("[insertNewPromotions]更新单个活动及活动商品信息结束............");
		}
		return new ResultInfo();
	}
}
