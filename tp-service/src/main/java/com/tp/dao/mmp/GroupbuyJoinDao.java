package com.tp.dao.mmp;

import com.tp.common.dao.BaseDao;
import com.tp.model.mmp.GroupbuyJoin;

import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface GroupbuyJoinDao extends BaseDao<GroupbuyJoin> {

    List<Long> myJoin(Long memberId);

    List<Long> myJoinWithTopicId(@Param("topicId") Long topicId,@Param("memberId")Long memberId);

}
