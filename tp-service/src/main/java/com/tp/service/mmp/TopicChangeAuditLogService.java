package com.tp.service.mmp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.dao.BaseDao;
import com.tp.dao.mmp.TopicChangeAuditLogDao;
import com.tp.model.mmp.TopicChangeAuditLog;
import com.tp.service.BaseService;
import com.tp.service.mmp.ITopicChangeAuditLogService;

@Service
public class TopicChangeAuditLogService extends BaseService<TopicChangeAuditLog> implements ITopicChangeAuditLogService {

	@Autowired
	private TopicChangeAuditLogDao topicChangeAuditLogDao;
	
	@Override
	public BaseDao<TopicChangeAuditLog> getDao() {
		return topicChangeAuditLogDao;
	}



}
