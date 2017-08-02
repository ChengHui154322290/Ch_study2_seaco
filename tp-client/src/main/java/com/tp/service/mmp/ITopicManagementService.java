package com.tp.service.mmp;

import java.util.List;

import com.tp.dto.common.ResultInfo;
import com.tp.dto.mmp.TopicInventoryExchangeDTO;
import com.tp.model.mmp.PolicyChange;
import com.tp.model.mmp.PolicyInfo;
import com.tp.model.mmp.Topic;
import com.tp.model.mmp.TopicChange;
import com.tp.model.mmp.TopicCoupon;
import com.tp.model.mmp.TopicCouponChange;
import com.tp.model.mmp.TopicItem;
import com.tp.model.mmp.TopicItemChange;
import com.tp.model.mmp.TopicPromoterChange;
import com.tp.result.mem.app.ResultMessage;

/**
 * 专场活动管理服务
 * 
 *
 */
public interface ITopicManagementService {

	/**
	 * 保存专场活动
	 * 
	 * @param topic
	 * @param topicItems
	 * @param removeTopicItemIds
	 * @param relateTopicIds
	 * @param removeRelateTopicIds
	 * @param policy
	 * @return
	 */
	ResultInfo saveTopic(Topic topic, List<TopicItem> topicItems,
			Long[] removeTopicItemIds, Long[] relateTopicIds,
			Long[] removeRelateTopicIds, List<TopicCoupon> topicCoupons,
			Long[] removeTopicCouponIds, PolicyInfo policyInfo, Long userId,
			String userName,Integer reserveInventoryFlag) throws Exception;

	/**
	 * 提交专场活动
	 * 
	 * @param topic
	 * @param topicItems
	 * @param removeTopicItemIds
	 * @param relateTopicIds
	 * @param removeRelateTopicIds
	 * @param policy
	 * @return
	 */
	ResultInfo submitTopic(Topic topic, List<TopicItem> topicItems,
			Long[] removeTopicItemIds, Long[] relateTopicIds,
			Long[] removeRelateTopicIds, List<TopicCoupon> topicCoupons,
			Long[] removeTopicCouponIds, PolicyInfo policy, Long userId,
			String userName,Integer reserveInventoryFlag) throws Exception;

	/**
	 * 创建新的专场活动
	 * 
	 * @param topicDO
	 * @return
	 */
	ResultInfo createTopic(Topic topic) ;

	/**
	 * 生成活动变更单
	 * 
	 * @param topicChange
	 * @param topicItemChanges
	 * @param relateTopicIds
	 * @param policy
	 * @param userId
	 * @param userName
	 * @return
	 * @
	 */
	Long generateTopicChange(TopicChange topicChange,
			List<TopicItemChange> topicItemChanges,
			List<TopicCouponChange> topicCouponChanges,
			List<Long> relateTopicIds, PolicyChange policy, 
			List<TopicPromoterChange> topicPromoterChangeList,
			Long userId,
			String userName) ;

	/**
	 * 保存专场活动变更单
	 * 
	 * @param topic
	 * @param topicItems
	 * @param removeTopicItemIds
	 * @param relateTopicIds
	 * @param removeRelateTopicIds
	 * @param policy
	 * @return
	 */
	ResultInfo saveTopicChange(TopicChange topicChange,
			List<TopicItemChange> topicItemChanges,
			Long[] removeTopicItemChangeIds, Long[] relateTopicIds,
			Long[] removeRelateTopicIds, List<TopicCouponChange> topicCoupons,
			Long[] removeTopicCouponIds, PolicyChange policy, Long userId,
			String userName) throws Exception;

	/**
	 * 提交专场活动变更单
	 * 
	 * @param topic
	 * @param topicItems
	 * @param removeTopicItemIds
	 * @param relateTopicIds
	 * @param removeRelateTopicIds
	 * @param policy
	 * @return
	 */
	ResultInfo submitTopicChange(TopicChange topicChange,
			List<TopicItemChange> topicItemChanges,
			Long[] removeTopicItemChangeIds, Long[] relateTopicIds,
			Long[] removeRelateTopicIds, List<TopicCouponChange> topicCoupons,
			Long[] removeTopicCouponIds, PolicyChange policy, Long userId,
			String userName) throws Exception;

	/**
	 * 复制指定专场活动
	 * 
	 * @param topicId
	 * @return
	 */
	ResultInfo copyTopic(Long topicId, Long userId, String userName) throws Exception
	;

	/**
	 * 取消指定专场活动
	 * 
	 * @param topicId
	 *            活动id
	 * @param userId
	 *            当前用户id
	 * @param userName
	 *            当前用户名
	 * @return
	 */
	ResultInfo cancelTopic(Long topicId, Long userId, String userName) throws Exception;

	/**
	 * 取消指定活动变更单
	 * 
	 * @param topicChangeId
	 * @param userId
	 * @param userName
	 * @return
	 * @
	 */
	ResultInfo cancelTopicChange(Long topicChangeId, Long userId,
			String userName) throws Exception;

	/**
	 * 终止专场活动
	 * 
	 * @param topicId
	 *            活动id
	 * @param userId
	 *            当前用户id
	 * @param userName
	 *            当前用户名
	 * @return
	 */
	ResultInfo terminateTopic(Long topicId, Long userId, String userName) throws Exception;

	/**
	 * 审批专场活动
	 * 
	 * @param topicId
	 *            活动id
	 * @param userId
	 *            当前用户id
	 * @param userName
	 *            当前用户名
	 * @return
	 */
	ResultInfo approveTopic(Long topicId, Long userId, String userName);

	/**
	 * 审批变更单通过，同时合并变更单数据到活动
	 * 
	 * @param topicChangeId
	 * @param userId
	 * @param userName
	 * @return
	 * @
	 */
	ResultInfo approveTopicChange(Long topicChangeId, Long userId,
			String userName) ;

	/**
	 * 驳回指定专题
	 * 
	 * @param topicId
	 *            活动id
	 * @param userId
	 *            当前用户id
	 * @param userName
	 *            当前用户名
	 * @return
	 */
	ResultInfo refuseTopic(Long topicId, Long userId, String userName)
			;

	/**
	 * 驳回活动变更单
	 * 
	 * @param topicChangeId
	 * @param userId
	 * @param userName
	 * @return
	 * @
	 */
	ResultInfo refuseTopicChange(Long topicChangeId, Long userId,
			String userName) ;

	/**
	 * 获取最大排序序号
	 * 
	 * @return
	 */
	Integer getMaxTopicInfoSortIndex() ;

	/**
	 * 锁定增加库存
	 * 
	 * @param topicItemId
	 * @param amount
	 * @return
	 */
	ResultInfo requestAddStock(Long topicItemId, int amount, boolean isTopic, Long userId, String userName) throws Exception
	;

	/**
	 * 锁定减少库存
	 * 
	 * @param topicItemId
	 * @param amount
	 * @return
	 */
	ResultInfo backStock(Long topicItemId, int amount, Long userId, String userName) throws Exception
	;

	/**
	 * 锁定增加库存(变更单用)
	 * 
	 * @param topicItemChangeId
	 * @param amount
	 * @return
	 */
	ResultInfo requestAddStockForChangeOrder(Long topicItemChangeId,
			int amount, Long userId, String userName) throws Exception;

	/**
	 * 锁定减少库存(变更单用)
	 * 
	 * @param topicItemChangeId
	 * @param amount
	 * @return
	 */
	ResultInfo backStockForChangeOrder(Long topicItemChangeId, int amount, Long userId, String userName) throws Exception;

	ResultMessage checkAvailableStock(List<TopicInventoryExchangeDTO> exchangeItems) ;

	ResultInfo requestStorageAmount(List<TopicInventoryExchangeDTO> exchangeItems) throws Exception;
}
