package com.tp.service.mkt;

import java.util.Map;

import com.tp.common.vo.PageInfo;
import com.tp.model.mkt.ChannelPromote;
import com.tp.service.IBaseService;
/**
  * @author szy 
  * 渠道推广接口
  */
public interface IChannelPromoteService extends IBaseService<ChannelPromote>{

	public PageInfo<ChannelPromote> statisticChannelPromote(Map<String, Object> params,PageInfo<ChannelPromote> info);
	
	/**
	 * 更新用户关注状态
	 * @return
	 */
	public Integer updateIsFollowList();
//	public Integer updateIsFollowListNew(Integer choise);
	
	/**
	 * 检查渠道是否存在
	 * @param code
	 * @param type
	 * @return
	 */
	public ChannelPromote checkChannel2Exist(String channel,int type);
	
}
