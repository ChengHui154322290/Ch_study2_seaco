package com.tp.dao.mmp;

import com.tp.common.dao.BaseDao;
import com.tp.model.mmp.TopicOperateLog;

import java.util.List;

public interface TopicOperateLogDao extends BaseDao<TopicOperateLog> {

    Integer insertBatch(List<TopicOperateLog> list);

}
