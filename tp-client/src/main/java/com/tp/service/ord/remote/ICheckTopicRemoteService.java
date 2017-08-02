/**
 * 
 */
package com.tp.service.ord.remote;

import com.tp.dto.common.ResultInfo;
import com.tp.dto.ord.remote.TopicPolicyDTO;



/**
 * @author szy
 *
 */
public interface ICheckTopicRemoteService {
	// 活动check接口
	public ResultInfo<Boolean> checkTopicPolicy(TopicPolicyDTO topicPolicyDTO);

	 int getBoughtQuantityForGroup(Long topicId, String sku, Long memberId);

	 int getBoughtCountWithGroupId(Long groupId,Long memberId);
}
