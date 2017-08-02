package com.tp.dao.mmp;

import com.tp.common.dao.BaseDao;
import com.tp.model.mmp.GroupbuyGroup;

import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface GroupbuyGroupDao extends BaseDao<GroupbuyGroup> {

    Integer queryProcessingGroupCount(@Param("topicId") Long topicId, @Param("memberId") Long memberId);

    int updateFactAmountById(Map<String, Object> param);

    List<GroupbuyGroup> queryByIds(List<Long> list);

    List<GroupbuyGroup> queryProcessingGroup();

    Integer terminateExpiredGroup(List<Long> list);

    List<GroupbuyGroup> queries(GroupbuyGroup groupbuyGroup);

    Integer queriesCount(GroupbuyGroup groupbuyGroup);

    Integer querySuccessGroupCount(List<Long> list);




}
