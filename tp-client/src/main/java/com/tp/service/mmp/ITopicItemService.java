package com.tp.service.mmp;

import java.util.List;
import java.util.Map;

import com.tp.dto.common.ResultInfo;
import com.tp.dto.prd.mq.PromotionItemMqDto;
import com.tp.model.mmp.TopicItem;
import com.tp.model.usr.UserInfo;
import com.tp.service.IBaseService;

/**
 * 促销商品 Service
 * 
 */
public interface ITopicItemService extends IBaseService<TopicItem> {
	/**
	 * 根据活动id获取活动商品清单(包括缓存数据)
	 * 
	 * @param tid
	 *            活动id
	 * @return 活动商品清单
	 */
	List<TopicItem> getTopicItemDOByTopicId(Long tid);

	/**
	 * 根据活动id获取活动商品清单(只取缓存数据)
	 * 
	 * @param tid
	 *            活动id
	 * @return 活动商品清单
	 */
	List<TopicItem> getTopicItemDOFromRedis(Long tid);

	/**
	 * 新增活动商品到清单(临时缓存)
	 * 
	 * @param pItemDO
	 *            活动商品
	 * @return
	 */
	boolean insertTopicItemToRedis(TopicItem pItem);

	/**
	 * 删除清单中的活动商品
	 * 
	 * @param pItemDO
	 *            活动商品
	 * @return
	 */
	boolean deleteTopicItemFromRedis(TopicItem pItem);

	/**
	 * 清空指定活动id下的活动商品(临时缓存) 编辑页面第一次加载时，调用清空临时缓存
	 * 
	 * @param tid
	 *            活动id
	 */
	void clearTopicItemFromRedis(Long tid);

	/**
	 * 根据活动id获取待删除活动商品清单(只取缓存数据)
	 * 
	 * @param tid
	 *            活动id
	 * @return 活动商品清单
	 */
	List<TopicItem> getDeleteTopicItemDOFromRedis(Long tid);

	/**
	 * 新增标记为删除的活动商品到清单(待删除商品临时缓存)
	 * 
	 * @param pItemDO
	 *            活动商品
	 * @return
	 */
	boolean insertDeleteTopicItemToRedis(TopicItem pItem);

	/**
	 * 清空指定活动id下标记为删除的活动商品(待删除商品临时缓存) 编辑页面第一次加载时，调用清空临时缓存
	 * 
	 * @param tid
	 *            活动id
	 */
	void clearDeleteTopicItemFromRedis(Long tid);

	/**
	 * 删除增删改的Redis
	 * 
	 * @param tid
	 */
	void clearAllTopicItemFromRedis(Long tid);

	/**
	 * 获取最大排序序号
	 * 
	 * @param topicId
	 * @return
	 */
	Integer getMaxTopicItemSortIndex(Long topicId);

	/**
	 * 根据Id列表，获取sku集合
	 * 
	 * @param ids
	 * @return
	 */
	List<String> getSkuListByIdList(List<Long> ids);

	/**
	 * 根据活动Id获取活动商品
	 * 
	 * @param topicIds
	 * @return
	 */
	List<TopicItem> getTopicItemByTopicIds(List<Long> topicIds);

	/**
	 * 根据传入商品信息，更新活动商品信息
	 * 
	 * @param itemMqDtos
	 * @return
	 */
	boolean updateTopicItemByUpdatedItemInfo(List<PromotionItemMqDto> itemMqDtos);

	/**
	 * 锁定活动商品Id
	 * 
	 * @param topicItemId
	 * @return
	 */
	ResultInfo lockTopicItem(Long topicItemId,UserInfo user);
	
	/**
	 * 解锁活动商品Id
	 * 
	 * @param topicItemId
	 * @return
	 */
	ResultInfo releaseTopicItem(Long topicItemId,UserInfo user);
	
	/**
	 * 批量锁定活动商品
	 * @param topicId
	 * @return
	 */
	ResultInfo batchLockTopicItem(Long topicId, Long userId,String userName);
	
	
	/**
	 * 获取过滤后活动商品
	 * @param promoterid
	 * @param topicid
	 * @param deletion
	 * @param lockstatus
	 * @param start
	 * @param pagesize
	 * @return
	 */
    List<TopicItem> getTopicItemFileterByDSS(Long promoterid, Long topicid, Integer deletion, Integer lockstatus, Integer start, Integer pagesize );
    
    /**
     * 根据sku找到 状态有效的活动商品
     * @param param
     * @return
     */
    List<TopicItem> getValidTopicItemBySku(String sku);
    
    
	/**
	 * 取品牌团前10个商品信息
	 * @param topicIds
	 * @return
	 */
    List<TopicItem> getTopicItemByTopicId_Top10(List<Long> topicIds);
    
	/**
	 * 取品牌团商品信息
	 * @param topicIds
	 * @return
	 */
    List<TopicItem> getTopicItemByTopicId(List<Long> topicIds);

	int  updateTopicItemByUpdatedItemInfoVersion2(List<PromotionItemMqDto> itemMqDtos);
}
