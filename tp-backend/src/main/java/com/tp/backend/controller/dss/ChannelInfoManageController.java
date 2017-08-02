package com.tp.backend.controller.dss;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tp.backend.controller.AbstractBaseController;
import com.tp.common.util.ptm.EncryptionUtil;
import com.tp.common.vo.PageInfo;
import com.tp.dto.common.FailInfo;
import com.tp.dto.common.ResultInfo;
import com.tp.model.dss.ChannelInfo;
import com.tp.model.dss.CommisionDetail;
import com.tp.proxy.dss.ChannelInfoProxy;
import com.tp.util.StringUtil;

/**
 * 佣金明细管理
 * @author szy
 *
 */
@Controller
@RequestMapping("/dss/channelinfo/")
public class ChannelInfoManageController extends AbstractBaseController {

	@Autowired
	private ChannelInfoProxy channelInfoProxy;
	
	@RequestMapping(value="list",method=RequestMethod.GET)
	public void index(Model model,CommisionDetail commisionDetail,Integer fixed){
	}
	
	@RequestMapping(value="list",method=RequestMethod.POST)
	@ResponseBody
	public PageInfo<ChannelInfo> list(Model model, ChannelInfo channelInfo){
		ResultInfo<PageInfo<ChannelInfo>> result = channelInfoProxy.queryPageByObject(channelInfo,
				new PageInfo<ChannelInfo>(channelInfo.getStartPage(),channelInfo.getPageSize()));
		return result.getData();
	}
	
	@RequestMapping(value="createtoken",method=RequestMethod.POST)
	@ResponseBody
	public ResultInfo<Integer> createToken(Model model,Integer channelId){
		
		ChannelInfo channelInfo = channelInfoProxy.queryById(channelId).getData();
		if(null==channelInfo){
			return new ResultInfo<Integer>(new FailInfo("没有找到渠道"));
		}
		if(StringUtil.isNotBlank(channelInfo.getToken())){
			return new ResultInfo<Integer>(new FailInfo("token已存在，请先清空token"));
		}
		channelInfo.setChannelId(channelId);
		channelInfo.setToken(EncryptionUtil.encrptMD5("SEAGOOR"+channelId+System.nanoTime()));
		return channelInfoProxy.updateNotNullById(channelInfo);
	}
}
