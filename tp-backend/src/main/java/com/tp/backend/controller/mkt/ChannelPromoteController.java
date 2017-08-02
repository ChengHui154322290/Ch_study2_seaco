package com.tp.backend.controller.mkt;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tp.common.vo.PageInfo;
import com.tp.common.vo.DAOConstant.MYBATIS_SPECIAL_STRING;
import com.tp.common.vo.mkt.ChannelPromoteConstant;
import com.tp.dto.common.ResultInfo;
import com.tp.model.mkt.ChannelPromote;
import com.tp.proxy.mkt.ChannelPromoteProxy;
import com.tp.util.BeanUtil;

/**
 * 渠道推广
 * @author zhuss
 * @2016年4月25日 下午4:58:49
 */
@Controller
@RequestMapping("/mkt/channel/")
public class ChannelPromoteController {
	
	@Autowired
	private ChannelPromoteProxy channelPromoteProxy;

	/**
	 * 渠道推广明细
	 * @param model
	 * @param promoterInfo
	 */
	@RequestMapping("list")
	public void list(Model model,ChannelPromote channelPromote){
		model.addAttribute("channelPromote", channelPromote);
		model.addAttribute("typeList", ChannelPromoteConstant.TYPE.values());
		Map<String, Object> params = BeanUtil.beanMap(channelPromote);
		channelPromote.remove(params);
		if( params.containsKey("channel")){
        	params.put(MYBATIS_SPECIAL_STRING.LIKE.name(), " channel like '%"+params.get("channel")+"%'");  
    		params.remove("channel");
    	}
		params.put(MYBATIS_SPECIAL_STRING.COLUMNS.name(), "unique_id !=''");
		params.put(MYBATIS_SPECIAL_STRING.ORDER_BY.name(), " type desc,channel,create_time desc");
		PageInfo<ChannelPromote> resultInfo =channelPromoteProxy.queryPageByParam(params,new PageInfo<ChannelPromote>(channelPromote.getStartPage(),channelPromote.getPageSize())).getData();
		model.addAttribute("page", resultInfo);
	}
	
	/**
	 * 跟微信同步数据
	 * @return
	 */
	@RequestMapping("sync")
	@ResponseBody
	public ResultInfo<Integer> updateIsFollowList(){
		return channelPromoteProxy.updateIsFollowList();
	}
//	@RequestMapping("syncNew")
//	@ResponseBody
//	public ResultInfo<Integer> updateIsFollowListNew(String choise){
//		return channelPromoteProxy.updateIsFollowListNew(Integer.valueOf(choise));
//	}
	
	/**
	 * 渠道推广统计
	 * @param model
	 * @param promoterInfo
	 */
	@RequestMapping("statistic")
	public void statistic(Model model,ChannelPromote channelPromote){
		model.addAttribute("channelPromote", channelPromote);
		model.addAttribute("typeList", ChannelPromoteConstant.TYPE.values());
		Map<String, Object> params = BeanUtil.beanMap(channelPromote);
		params.put("start", channelPromote.getStart());
		params.put("pageSize", channelPromote.getPageSize());
		if(params.containsKey("channel")){
			params.put("channel", "%"+channelPromote.getChannel()+"%");
		}
		PageInfo<ChannelPromote> resultInfo =channelPromoteProxy.statisticChannelPromote(params,new PageInfo<ChannelPromote>(channelPromote.getStartPage(),channelPromote.getPageSize())).getData();
		model.addAttribute("page", resultInfo);
	}
}
