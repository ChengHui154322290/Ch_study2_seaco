/**
 * 
 */
package com.tp.service.mmp.remote;


import java.util.List;
import java.util.Map;

import com.tp.exception.ServiceException;
import com.tp.model.mmp.Topic;

/**
 *
 */
public interface IRemoteCommonService {
	
	/**
	 * 根据活动序号，获取活动信息
	 * @param tid
	 * @return
	 */
	Map<Long,Topic> getTopicInfosByIds(List<Long> tids) throws ServiceException;
}
