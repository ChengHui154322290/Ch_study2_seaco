package com.tp.test.mkt;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.tp.common.vo.PageInfo;
import com.tp.model.mkt.ChannelPromote;
import com.tp.service.mkt.IChannelPromoteService;
import com.tp.test.BaseTest;

public class ChannelPromoteTest extends BaseTest{

	@Autowired
	private IChannelPromoteService channelPromoteService;
	
	@Test
	public void statisticChannelPromote(){
		Map<String, Object> params = new HashMap<>();
		params.put("start", 1);
		params.put("pageSize", 10);
		PageInfo<ChannelPromote> result = channelPromoteService.statisticChannelPromote(null, new PageInfo<>());
		System.out.println(result.getRows());
	}
	
	@Test
	public void updateIsFollowList(){
		System.out.println("==================="+channelPromoteService.updateIsFollowList());
	}
}
