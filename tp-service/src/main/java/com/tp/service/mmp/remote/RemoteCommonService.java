/**
 * 
 */
package com.tp.service.mmp.remote;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.dao.mmp.TopicDao;
import com.tp.dto.mmp.enums.ErrorCodeType;
import com.tp.exception.ServiceException;
import com.tp.model.mmp.Topic;
import com.tp.service.mmp.remote.IRemoteCommonService;

/**
 *
 */
@Service(value = "remoteCommonService")
public class RemoteCommonService implements IRemoteCommonService {

	@Autowired
	private TopicDao topicDAO;


	@Override
	public Map<Long, Topic> getTopicInfosByIds(List<Long> tids)
			throws ServiceException {
		try {
			Map<Long, Topic> topics = new HashMap<Long, Topic>();
			for (Long tid : tids) {
				Topic topic = topicDAO.queryById(tid);
				//删除无用信息
				topic.setIntro(null);
				topic.setIntroMobile(null);
				topic.setImage(null);
				topic.setImageHitao(null);
				topic.setImageInterested(null);
				topic.setImageMobile(null);
				topic.setImageNew(null);
				topic.setPcImage(null);
				topic.setPcInterestImage(null);
				topic.setMobileImage(null);
				topic.setHaitaoImage(null);
				topic.setMallImage(null);
				topic.setCreateTime(null);
				topic.setUpdateTime(null);
				topics.put(topic.getId(), topic);
			}
			return topics;
		} catch (Exception e) {
			throw new ServiceException(
					String.valueOf(ErrorCodeType.GET_TOPIC_BY_ID.ordinal()));
		}
	}

}
