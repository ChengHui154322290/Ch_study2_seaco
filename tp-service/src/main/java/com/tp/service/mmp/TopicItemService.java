package com.tp.service.mmp;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.tp.common.dao.BaseDao;
import com.tp.common.vo.mmp.ProcessingErrorMessage;
import com.tp.common.vo.mmp.TopicAuditLogConstant;
import com.tp.common.vo.mmp.TopicChangeAuditLogConstant;
import com.tp.dao.mmp.*;
import com.tp.dto.common.FailInfo;
import com.tp.dto.common.ResultInfo;
import com.tp.dto.mmp.enums.DeletionStatus;
import com.tp.dto.mmp.enums.LockStatus;
import com.tp.dto.mmp.enums.SalesPartten;
import com.tp.dto.mmp.enums.TopicStatus;
import com.tp.dto.prd.enums.ItemStatusEnum;
import com.tp.dto.prd.mq.PromotionItemMqDto;
import com.tp.exception.ServiceException;
import com.tp.model.mmp.*;
import com.tp.model.usr.UserInfo;
import com.tp.service.BaseService;
import com.tp.service.mmp.ITopicItemService;
import com.tp.service.mmp.ITopicRedisService;

import org.springframework.util.CollectionUtils;

@Service
public class TopicItemService extends BaseService<TopicItem> implements ITopicItemService {

	@Autowired
	private TopicItemDao topicItemDao;

	@Autowired
	private TopicItemChangeDao topicItemChangeDao;

	@Autowired
	private TopicChangeDao topicChangeDao;

	@Autowired
	private ITopicRedisService topicRedisService;

	@Autowired
	private TopicAuditLogDao topicAuditLogDao;

	@Autowired
	private TopicOperateLogDao topicOperateLogDao;

	@Autowired
	private TopicDao topicDao;
	
	@Override
	public BaseDao<TopicItem> getDao() {
		return topicItemDao;
	}



	@Override
	public List<TopicItem> getTopicItemDOByTopicId(Long tid) {
		TopicItem tItemDO = new TopicItem();
		tItemDO.setTopicId(tid);
		List<TopicItem> pItems = queryByObject(tItemDO);
		List<TopicItem> pItemsRedis = getTopicItemDOFromRedis(tid);
		if (null != pItemsRedis && 0 < pItemsRedis.size()) {
			if (!pItems.addAll(pItemsRedis)) {
				// TODO:合并数据集失败异常
			}
		}
		return pItems;
	}

	@Override
	public List<TopicItem> getTopicItemDOFromRedis(Long tid) {
//		DBJedisList<TopicItem> itemsDO = getInsertRedis(tid);
//		return itemsDO.getList();
		return null;
	}

	@Override
	public boolean insertTopicItemToRedis(TopicItem tItemDO) {
//		DBJedisList<TopicItem> redis = getInsertRedis(tItemDO.getTopicId());
//		if (redis.contains(tItemDO)) {
//			redis.remove(tItemDO);
//		}
//		return (null != redis && null != tItemDO && 0 != redis.add(tItemDO));
		return true;
	}

	@Override
	public boolean deleteTopicItemFromRedis(TopicItem tItemDO) {
//		DBJedisList<TopicItem> redis = getInsertRedis(tItemDO.getTopicId());
//		return (null != tItemDO && null != redis && redis.contains(tItemDO) && redis
//				.remove(tItemDO));
		return true;
	}

	@Override
	public void clearTopicItemFromRedis(Long tid) {
//		DBJedisList<TopicItem> redis = getInsertRedis(tid);
//		redis.clear();
	}

	@Override
	public List<TopicItem> getDeleteTopicItemDOFromRedis(Long tid) {
//		DBJedisList<TopicItem> itemsDO = getInsertRedis(tid);
//		return itemsDO.getList();
		return null;
	}

	@Override
	public boolean insertDeleteTopicItemToRedis(TopicItem tItemDO) {
//		DBJedisList<TopicItem> redis = getDeleteRedis(tItemDO.getTopicId());
//		if (redis.contains(tItemDO)) {
//			redis.remove(tItemDO);
//		}
//		return (null != redis && null != tItemDO && 0 != redis.add(tItemDO));
		return true;
	}

	@Override
	public void clearDeleteTopicItemFromRedis(Long tid) {
//		DBJedisList<TopicItem> redis = getDeleteRedis(tid);
//		redis.clear();
		
	}

	@Override
	public Integer getMaxTopicItemSortIndex(Long topicId) {
		return topicItemDao.getMaxTopicItemSortIndex(topicId);
	}

	@Override
	public void clearAllTopicItemFromRedis(Long tid) {
		clearTopicItemFromRedis(tid);
		clearDeleteTopicItemFromRedis(tid);
	}

//	private DBJedisList<TopicItem> getInsertRedis(Long tid) {
//		DBJedisList<TopicItem> redis = new DBJedisList<TopicItem>(
//				"TopicItem_Add", tid.intValue());
//		return redis;
//	}
//
//	private DBJedisList<TopicItem> getDeleteRedis(Long tid) {
//		DBJedisList<TopicItem> redis = new DBJedisList<TopicItem>(
//				"TopicItem_Del", tid.intValue());
//		return redis;
//	}

	@Override
	public List<String> getSkuListByIdList(List<Long> ids) {
		return topicItemDao.getSkuListByIdList(ids);
	}

	@Override
	public List<TopicItem> getTopicItemByTopicIds(List<Long> topicIds) {
		return topicItemDao.getTopicItemByTopicIds(topicIds);
	}

	@Override
    public List<TopicItem> getTopicItemByTopicId_Top10(List<Long> topicIds){
		return topicItemDao.getTopicItemByTopicId_Top10(topicIds);    	
    }

	@Override
    public List<TopicItem> getTopicItemByTopicId(List<Long> topicIds){
		return topicItemDao.getTopicItemByTopicId(topicIds);    	
    }
	
	@Override
	public boolean updateTopicItemByUpdatedItemInfo(
			List<PromotionItemMqDto> mqDtos) {
		try {
			Map<String, PromotionItemMqDto> promotionItemInfos = new HashMap<String, PromotionItemMqDto>();
			List<String> skuLists = new ArrayList<String>();
			List<Long> topicIds = new ArrayList<Long>();
			for (PromotionItemMqDto mqDto : mqDtos) {
				if (mqDto != null) {
					skuLists.add(mqDto.getSkuCode());
					promotionItemInfos.put(mqDto.getSkuCode(), mqDto);
				}
			}
			if (promotionItemInfos.size() == 0) {
				logger.error("[updateTopicItemByUpdatedItemInfo]empty promotion item info......");
			}
			List<TopicItem> items = topicItemDao.getTopicItemBySkuList(skuLists);
			if (items == null || items.size() == 0) {
				return true;
			}
			for (TopicItem TopicItem : items) {
				if (TopicItem == null
						|| !promotionItemInfos
						.containsKey(TopicItem.getSku())) {
					continue;
				}
				PromotionItemMqDto mqDto = promotionItemInfos.get(TopicItem.getSku());
				TopicItem topicItem = new TopicItem();
				topicItem.setId(TopicItem.getId());
				topicItem.setName(mqDto.getMainTitle());
				topicItem.setSalePrice(mqDto.getBasicPrice());
				topicItem.setUpdateTime(new Date());
				topicItem.setTopicImage(mqDto.getMainPic());
				topicItem.setApplyAgeId(mqDto.getApplyAgeId());
				topicItem.setBrandId(mqDto.getBrandId());
				topicItem.setLargeCateoryId(mqDto.getLargeCateId());
				topicItem.setMiddleCategoryId(mqDto.getMiddleCateId());
				topicItem.setCategoryId(mqDto.getSmallCateId());
				if(!TopicItem.getItemStatus().equals(mqDto.getStatus())){
					topicItem.setItemStatus(mqDto.getStatus());
					topicItem.setListingTime(mqDto.getUpdateSkuDate());
				}
				topicItemDao.updateNotNullById(topicItem);
				topicIds.add(TopicItem.getTopicId());
			}
			return true;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return false;
	}

	@Override
	public ResultInfo lockTopicItem(Long topicItemId,UserInfo user ) {
		try {
			TopicItem itemDO = topicItemDao.queryById(topicItemId);
			if (itemDO == null
					|| itemDO.getDeletion() == DeletionStatus.DELETED.ordinal()) {
				logger.error("[lockTopicItem]topic item is invalid or deleted");
				return new ResultInfo(new FailInfo(ProcessingErrorMessage.INVALID_TOPIC_ITEM_INFO));
			}
			if (itemDO.getLockStatus() == null) {
				logger.error("[lockTopicItem]topic item lock status is null......id:"
						+ itemDO.getId());
				return new ResultInfo(new FailInfo(ProcessingErrorMessage.COMMON_ERROR));

			}
			if (itemDO.getLockStatus() == LockStatus.LOCK.ordinal()) {
				logger.error("[lockTopicItem]topic item already lock......id:"
						+ itemDO.getId());
			} else {
				itemDO.setLockStatus(LockStatus.LOCK.ordinal());
				itemDO.setUpdateTime(new Date());
				topicItemDao.updateNotNullById(itemDO);

				saveLog(user, itemDO,"LOCK-ITEM");

			}
			return new ResultInfo();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			
			return new ResultInfo(new FailInfo(ProcessingErrorMessage.COMMON_ERROR));

		}
	}

	private void saveLog(UserInfo user, TopicItem itemDO,String operation) {
		try {
			TopicOperateLog operateLog = new TopicOperateLog();
			operateLog.setTopicId(itemDO.getTopicId());
			operateLog.setCreateUserId(user.getId());
			operateLog.setCreateUserName(user.getUserName());
			operateLog.setCreateTime(new Date());
			operateLog.setRemark("");
			operateLog.setType(operation);
			operateLog.setContent("SKU:"+itemDO.getSku());
			topicOperateLogDao.insert(operateLog);
		}catch (Exception e){
			logger.error("TOPIC_ITEM.SAVE_TOPIC_ITEM_LOG_ERROR.",e);
		}

	}

	@Override
	public ResultInfo releaseTopicItem(Long topicItemId,UserInfo user ) {
		try {
			TopicItem itemDO = topicItemDao.queryById(topicItemId);
			if (itemDO == null
					|| itemDO.getDeletion() == DeletionStatus.DELETED.ordinal()) {
				logger.error("[releaseTopicItem]topic item is invalid or deleted");
				return new ResultInfo(new FailInfo(ProcessingErrorMessage.INVALID_TOPIC_ITEM_INFO));
			}
			if (itemDO.getLockStatus() == null) {
				logger.error("[releaseTopicItem]topic item lock status is null......id:"
						+ itemDO.getId());
				return new ResultInfo(new FailInfo(ProcessingErrorMessage.COMMON_ERROR));
			}
			if (itemDO.getLockStatus() == LockStatus.UNLOCK.ordinal()) {
				logger.error("[releaseTopicItem]topic item is release......id:"
						+ itemDO.getId());
			}
			TopicItemChange changeItemDO = new TopicItemChange();
			changeItemDO.setChangeTopicItemId(topicItemId);
			List<TopicItemChange> topicItemChanges = topicItemChangeDao.queryByObject(changeItemDO);
			for (TopicItemChange TopicItemChange : topicItemChanges) {
				TopicChange tcDO = topicChangeDao.queryById(TopicItemChange.getTopicChangeId());
				if (tcDO.getStatus() != TopicStatus.PASSED.ordinal()
						&& tcDO.getStatus() != TopicStatus.CANCELED.ordinal()) {
					return new ResultInfo(new FailInfo(ProcessingErrorMessage.ITEM_CHANGE_HAS_ORDER));
				}
			}
			itemDO.setLockStatus(LockStatus.UNLOCK.ordinal());
			itemDO.setUpdateTime(new Date());
			topicItemDao.updateNotNullById(itemDO);

			saveLog(user,itemDO, "RELEASE-ITEM");

			return new ResultInfo();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return new ResultInfo(new FailInfo(ProcessingErrorMessage.COMMON_ERROR));
		}
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public ResultInfo batchLockTopicItem(Long topicId, Long userId,String userName) {
		try {
			batchLockTopicItemWithTrans(topicId, userId, userName);
			saveLog(topicId, userId, userName);
			return new ResultInfo();
		} catch (Exception e) {
			logger.error("error",e);
			throw new ServiceException(e);
		}
	}

	private void saveLog(Long topicId, Long userId, String userName) {
		try {
			TopicOperateLog operateLog = new TopicOperateLog();
			operateLog.setTopicId(topicId);
			operateLog.setCreateUserId(userId);
			operateLog.setCreateUserName(userName);
			operateLog.setCreateTime(new Date());
			operateLog.setRemark("");
			operateLog.setType("BATCH-LOCK-ITEM");
			operateLog.setContent("");
			topicOperateLogDao.insert(operateLog);
		}catch (Exception e){
			logger.error("TOPIC_ITEM.SAVE_OPERATE_LOG_ERROR,",e);
		}

	}

	private void batchLockTopicItemWithTrans(Long topicId, Long userId, String userName) throws ServiceException {
			TopicItem updateItem = new TopicItem();
			updateItem.setTopicId(topicId);
			updateItem.setDeletion(DeletionStatus.NORMAL.ordinal());
			updateItem.setLockStatus(LockStatus.UNLOCK.ordinal());
			updateItem.setTopicId(topicId);
			List<TopicItem> tiDOs = topicItemDao.queryByObject(updateItem);
			for (TopicItem TopicItem : tiDOs) {
				TopicItem.setLockStatus(LockStatus.LOCK.ordinal());
				TopicItem.setUpdateTime(new Date());
				topicItemDao.updateNotNullById(TopicItem);
			}
	}
	
	
    public List<TopicItem> getTopicItemFileterByDSS(Long promoterid, Long topicid, 
    		Integer deletion, Integer lockstatus, Integer start, Integer pagesize ){
    	
    	Map<String, Object> params = new HashMap<String, Object>();    	
		params.put("promoterid", promoterid);
		params.put("topicid", topicid);
		params.put("deletion", deletion);
		params.put("lockstatus", lockstatus);
		params.put("start", start);			    			
		params.put("size", pagesize);
		params.put("itemStatus", ItemStatusEnum.ONLINE.getValue());
		return topicItemDao.getTopicItemFileterByDSS(params);
				
    }



	@Override
	public List<TopicItem> getValidTopicItemBySku(String sku) {
		return topicItemDao.getValidTopicItemBySku(sku);
	}


	@Override
	public int updateTopicItemByUpdatedItemInfoVersion2(List<PromotionItemMqDto> itemMqDtos) {

            if (CollectionUtils.isEmpty(itemMqDtos)) return 0;
            List<String> skuLists = new ArrayList<String>();
            for (PromotionItemMqDto mqDto : itemMqDtos) {
                skuLists.add(mqDto.getSkuCode());
            }

            List<TopicItem> items = topicItemDao.getTopicItemBySkuList(skuLists);
            if (CollectionUtils.isEmpty(items)) return 0;
            for (TopicItem TopicItem : items) {

            	Topic topic = topicDao.queryById(TopicItem.getTopicId());
				if(topic.getSalesPartten() != null && topic.getSalesPartten().equals(SalesPartten.GROUP_BUY.getValue())){
					logger.info("[SYN_ITEM_INFO_TO_PROMOTION_SKIP_GROUP_BUY_ITEM:TOPIC_ITEM_ID={}]",TopicItem.getId());
					continue;
				}

                PromotionItemMqDto mqDto = getDTOs(itemMqDtos, TopicItem);
                TopicItem topicItem = new TopicItem();
                topicItem.setId(TopicItem.getId());
                topicItem.setName(mqDto.getMainTitle());
                topicItem.setSalePrice(mqDto.getBasicPrice());
                topicItem.setUpdateTime(new Date());
                topicItem.setTopicImage(mqDto.getMainPic());
                //topicItem.setApplyAgeId(mqDto.getApplyAgeId());
                topicItem.setBrandId(mqDto.getBrandId());
                topicItem.setLargeCateoryId(mqDto.getLargeCateId());
                topicItem.setMiddleCategoryId(mqDto.getMiddleCateId());
                topicItem.setCategoryId(mqDto.getSmallCateId());
                topicItem.setItemStatus(mqDto.getStatus());
                topicItemDao.updateNotNullById(topicItem);
            }
		 return items.size();
	}

	private PromotionItemMqDto getDTOs(List<PromotionItemMqDto> itemMqDtos, TopicItem TopicItem) {
		for(PromotionItemMqDto dto: itemMqDtos){
            if(TopicItem.getSku().equals(dto.getSkuCode()))
                return  dto;
        }
		return null;
	}
}
