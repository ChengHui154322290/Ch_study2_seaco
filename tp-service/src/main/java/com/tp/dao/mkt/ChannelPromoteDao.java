package com.tp.dao.mkt;

import java.util.List;
import java.util.Map;

import com.tp.common.dao.BaseDao;
import com.tp.dto.mkt.FollowDto;
import com.tp.model.mkt.ChannelPromote;

public interface ChannelPromoteDao extends BaseDao<ChannelPromote> {

	public List<ChannelPromote> statisticChannelPromote(Map<String,Object> params);
	
	public int statisticChannelPromoteCount(Map<String,Object> params);
	
	public List<FollowDto> queryUniqueIdList();
	
	public Integer updateIsFollowList(List<FollowDto> list);
}
