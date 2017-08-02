/**
 * 
 */
package com.tp.shop.controller.dss;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tp.m.base.MResultVO;
import com.tp.m.enums.MResultInfo;
import com.tp.m.exception.MobileException;
import com.tp.m.util.AssertUtil;
import com.tp.m.util.JsonUtil;
import com.tp.m.vo.promoter.ChannelInfoVO;
import com.tp.shop.ao.dss.ChannelAO;
import com.tp.shop.helper.RequestHelper;

/**
 * @author Administrator
 *
 */
@Controller
@RequestMapping("/dss/channel/")
public class ChannelInfoController {
	
	private static final Logger log = LoggerFactory.getLogger(ChannelInfoController.class);
	
	@Autowired
	private ChannelAO channelAO;
	
	@RequestMapping(value = "getinfo", method = {RequestMethod.POST, RequestMethod.GET})
	@ResponseBody
	public String getChannelInfo(HttpServletRequest request){
		try {
			String channelCode = RequestHelper.getChannelCode(request);
			AssertUtil.notBlank(channelCode, MResultInfo.PARAM_ERROR);
			MResultVO<ChannelInfoVO> channelInfo = channelAO.getChannelInfo(channelCode);
			if(log.isInfoEnabled()){
				log.info("[API接口 -渠道信息 返回值] = {}",JsonUtil.convertObjToStr(channelInfo));
			}
			return JsonUtil.convertObjToStr(channelInfo);
		}catch(MobileException me){
			log.error("[API接口 - 渠道信息  MobileException] = {}",me.getMessage());
			return JsonUtil.convertObjToStr(new MResultVO<>(MResultInfo.PARAM_ERROR));
		}
	}
}
