package com.tp.dao.mmp;

import com.tp.common.dao.BaseDao;
import com.tp.model.mmp.GroupbuyInfo;

import java.util.List;
import java.util.Map;

public interface GroupbuyInfoDao extends BaseDao<GroupbuyInfo> {

    List<GroupbuyInfo> queryByTopicIdsForApp(Map<String,Object> param);

    List<GroupbuyInfo> queryByTopicIds(List<Long> ids);

    List<GroupbuyInfo> queryByIds(List<Long> list);


}
