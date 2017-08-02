/**
 * 
 */
package com.tp.service.mmp.remote;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.vo.StorageConstant;
import com.tp.common.vo.mmp.ProcessingErrorMessage;
import com.tp.dao.mmp.TopicDao;
import com.tp.dao.mmp.TopicItemDao;
import com.tp.dto.mmp.TopicItemBbtDTO;
import com.tp.dto.mmp.enums.DeletionStatus;
import com.tp.enums.common.PlatformEnum;
import com.tp.exception.ServiceException;
import com.tp.model.mmp.Topic;
import com.tp.model.mmp.TopicItem;
import com.tp.model.stg.InventoryDistribute;
import com.tp.redis.util.JedisCacheUtil;
import com.tp.service.mmp.remote.IRemoteForItemService;
import com.tp.service.stg.IInventoryQueryService;

/**

 */
@Service(value = "remoteForItemService")
public class RemoteForItemService implements IRemoteForItemService {

	private Log logger = LogFactory.getLog(this.getClass());

	@Autowired
	private TopicItemDao itemDAO;

	@Autowired
	private TopicDao topicDAO;

	@Autowired
	private IInventoryQueryService inventoryQueryService;

	@Autowired
	private JedisCacheUtil jedisCacheUtil;

	
	@Override
	public TopicItemBbtDTO getTopicItemBySku(String sku) {
		System.out.println("此方法已注");
		logger.info("此方法已注");
		return null;
//		if (StringUtils.isBlank(sku)) {
//			logger.error(ProcessingErrorMessage.PROMOTION_ITEM_SKU_FORMAT_ERROR_MSG);
//			throw new ServiceException(
//					ProcessingErrorMessage.PROMOTION_ITEM_SKU_FORMAT_ERROR_CODE);
//		}
//		// String key = TopicRedisKeyConstant.PR_PROMOTION_BBT_TI_KEYS
//		// + TopicRedisKeyConstant.TOPIC_INFO_SPLIT + sku;
//		TopicItemBbtDTO lowPriceItem = null;
//		// if (jedisCacheUtil.keyExists(key)) {
//		// lowPriceSku = (TopicItemDO) jedisCacheUtil.getCache(key);
//		// if (lowPriceSku != null) {
//		// return lowPriceSku;
//		// }
//		// }
//		List<InventoryDistribute> distributeDOs = inventoryQueryService
//				.queryHasInventoryBizInfo(StorageConstant.App.PROMOTION, sku);
//		Set<Long> inventoryTopicIds = new HashSet<Long>();
//		if (distributeDOs == null || distributeDOs.size() == 0) {
//			logger.error(String.format(ProcessingErrorMessage.PROMOTION_ITEM_SKU_NOENOUGH_INVENTORY_MSG,sku));
//			throw new ServiceException(
//					ProcessingErrorMessage.PROMOTION_ITEM_SKU_NOENOUGH_INVENTORY_CODE);
//		}
//		for (InventoryDistribute inventoryDistributeDO : distributeDOs) {
//			if (inventoryDistributeDO != null) {
//				Long topicId = Long.valueOf(inventoryDistributeDO.getBizId());
//				inventoryTopicIds.add(topicId);
//			}
//		}
//		TopicItem selectItemDO = new TopicItem();
//		selectItemDO.setDeletion(DeletionStatus.NORMAL.ordinal());
//		selectItemDO.setSku(sku);
//		List<TopicItem> itemInfos = itemDAO
//				.getTopicItemInfoBySku(selectItemDO);
//		List<Long> avaliableTopicIds = topicDAO.getAvailablePCAndWACTopicId();
//		if (avaliableTopicIds == null || avaliableTopicIds.size() == 0) {
//			logger.error(String.format(ProcessingErrorMessage.PROMOTION_ITEM_SKU_NOVALID_TOPIC_MSG,sku));
//			throw new ServiceException(
//					ProcessingErrorMessage.PROMOTION_ITEM_SKU_NOVALID_TOPIC_CODE);
//		}
//		for (TopicItem item : itemInfos) {
//			if (avaliableTopicIds.contains(item.getTopicId())
//					&& inventoryTopicIds.contains(item.getTopicId())) {
//				lowPriceItem = new TopicItemBbtDTO();
//				lowPriceItem.setId(item.getId());
//				lowPriceItem.setTopicId(item.getTopicId());
//				lowPriceItem.setName(item.getName());
//				lowPriceItem.setSku(item.getSku());
//				lowPriceItem.setTopicImage(item.getTopicImage());
//				lowPriceItem.setTopicPrice(item.getTopicPrice());
//				lowPriceItem.setSalePrice(item.getSalePrice());
//				break;
//			}
//		}
//		if (lowPriceItem != null) {
//			try {
//				Topic topic = topicDAO.queryById(lowPriceItem.getTopicId());
//				if (topic != null) {
//					if (!StringUtils.isBlank(topic.getPlatformStr())) {
//						if (topic.getPlatformStr().contains(
//								String.valueOf(PlatformEnum.ALL.getCode()))) {
//							lowPriceItem.setIsPc(true);
//							lowPriceItem.setIsWap(true);
//						} else if (topic.getPlatformStr().contains(
//								String.valueOf(PlatformEnum.PC.getCode()))) {
//							lowPriceItem.setIsPc(true);
//						} else if (topic.getPlatformStr().contains(
//								String.valueOf(PlatformEnum.WAP.getCode()))) {
//							lowPriceItem.setIsWap(true);
//						}
//					}
//				}
//			} catch (Exception e) {
//				logger.error(e.getMessage(), e);
//			}
//		}
//		return lowPriceItem;
	}

}
