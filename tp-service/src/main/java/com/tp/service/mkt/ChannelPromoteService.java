package com.tp.service.mkt;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.dao.BaseDao;
import com.tp.common.vo.PageInfo;
import com.tp.dao.mkt.ChannelPromoteDao;
import com.tp.dto.mkt.FollowDto;
import com.tp.model.mkt.ChannelPromote;
import com.tp.service.BaseService;
import com.tp.service.mkt.IChannelPromoteService;
import com.tp.service.wx.IUserManagerService;

@Service
public class ChannelPromoteService extends BaseService<ChannelPromote> implements IChannelPromoteService {

	@Autowired
	private ChannelPromoteDao channelPromoteDao;
	
	@Autowired
	private IUserManagerService userManagerService;
	
	@Override
	public BaseDao<ChannelPromote> getDao() {
		return channelPromoteDao;
	}

	@Override
	public PageInfo<ChannelPromote> statisticChannelPromote(Map<String, Object> params,PageInfo<ChannelPromote> info) {
		List<ChannelPromote> rows = channelPromoteDao.statisticChannelPromote(params);
		int count = channelPromoteDao.statisticChannelPromoteCount(params);
		PageInfo<ChannelPromote> pageResult = new PageInfo<ChannelPromote>();
		if(CollectionUtils.isNotEmpty(rows)){
			pageResult.setRows(rows);
			pageResult.setPage(info.getPage());
			pageResult.setRecords(count);
			pageResult.setSize(rows.size());
		}
		return pageResult;
	}

	@Override
	public Integer updateIsFollowList() {
		List<FollowDto> sqlList = channelPromoteDao.queryUniqueIdList();
		if(CollectionUtils.isNotEmpty(sqlList)){
			List<FollowDto> uptFollow = new ArrayList<>(); //需要修改的用户列表
			List<String> wxList = userManagerService.queryUserList();
			Iterator<FollowDto> iterator = sqlList.iterator();
			while(iterator.hasNext()) {
				FollowDto f = iterator.next();
				for(String str:wxList){
					if(f.getUnique_id().equals(str)){
						if(f.getIs_follow() == 0){
							f.setIs_follow(1);
							uptFollow.add(f);
						}
						iterator.remove();
						break;
					}
				}
			}
			for(FollowDto f : sqlList){
				if(f.getIs_follow() == 1){
					f.setIs_follow(0);
					uptFollow.add(f);
				}
			}
			//follow.retainAll(wxList);
			if(CollectionUtils.isNotEmpty(uptFollow))return channelPromoteDao.updateIsFollowList(uptFollow);
		}
		return 0;
	}

//	@Override
//	public Integer updateIsFollowListNew(Integer choise) {
//		List<FollowDto> sqlList = channelPromoteDao.queryUniqueIdList();
//		if(CollectionUtils.isNotEmpty(sqlList)){
//			List<FollowDto> uptFollow = new ArrayList<>(); //需要修改的用户列表
//			List<String> wxList = userManagerService.queryUserList();
//			Iterator<FollowDto> iterator = sqlList.iterator();
//			while(iterator.hasNext()) {
//				FollowDto f = iterator.next();
//				for(String str:wxList){
//					if(f.getUnique_id().equals(str)){
//						if(f.getIs_follow() == 0){
//							f.setIs_follow(1);
//							uptFollow.add(f);
//						}
//						iterator.remove();
//						break;
//					}
//				}
//			}
//			for(FollowDto f : sqlList){
//				if(f.getIs_follow() == 1){
//					f.setIs_follow(0);
//					uptFollow.add(f);
//				}
//			}
//			//follow.retainAll(wxList);
//			if(CollectionUtils.isNotEmpty(uptFollow))return channelPromoteDao.updateIsFollowList(uptFollow);
//		}
//		return 0;
//	}
	
	@Override
	public ChannelPromote checkChannel2Exist(String channel, int type) {
		ChannelPromote channelPromote = new ChannelPromote();
		channelPromote.setChannel(channel);
		channelPromote.setType(type);
		channelPromote.setUniqueId("");
		List<ChannelPromote> list = channelPromoteDao.queryByObject(channelPromote);
		if(list == null || list.isEmpty())return channelPromote;
		return list.get(0);
	}
}
