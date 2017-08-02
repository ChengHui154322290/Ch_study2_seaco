package com.tp.service.mmp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.dao.BaseDao;
import com.tp.dao.mmp.TopicAuditLogDao;
import com.tp.model.mmp.TopicAuditLog;
import com.tp.service.BaseService;
import com.tp.service.mmp.ITopicAuditLogService;

@Service
public class TopicAuditLogService extends BaseService<TopicAuditLog> implements ITopicAuditLogService {

	@Autowired
	private TopicAuditLogDao topicAuditLogDao;
	
	@Override
	public BaseDao<TopicAuditLog> getDao() {
		return topicAuditLogDao;
	}

}
