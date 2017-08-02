package com.tp.world.controller.mkt;

import com.tp.m.base.BaseQuery;
import com.tp.world.ao.mkt.ChannelPromoteAO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * 渠道推广分析
 * @author zhuss
 * @2016年4月26日 上午11:08:09
 */
@Controller
@RequestMapping("/mkt/channel")
public class ChannelPromoteController {
	
	@Autowired
	private ChannelPromoteAO channelPromoteAO;

	@RequestMapping(value="/save",method=RequestMethod.GET)
	public String save(BaseQuery baseQuery,String channel,String uuid,String type){
		System.out.println(channelPromoteAO.getUrl(channel, uuid, type));
		return "redirect:"+channelPromoteAO.getUrl(channel, uuid, type);
	}
}
