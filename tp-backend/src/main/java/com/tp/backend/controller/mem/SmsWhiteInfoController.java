/**
 * 
 */
package com.tp.backend.controller.mem;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tp.backend.controller.AbstractBaseController;
import com.tp.common.vo.PageInfo;
import com.tp.dto.common.ResultInfo;
import com.tp.dto.mem.SmsLimitInfoDto;
import com.tp.model.mem.SmsWhiteInfo;
import com.tp.proxy.mem.SmsStatisticsInfoProxy;
import com.tp.proxy.mem.SmsWhiteInfoProxy;

/**
 * @author Administrator
 * 短信白名单
 */
@Controller
@RequestMapping("/mem/smswhite/")
public class SmsWhiteInfoController extends AbstractBaseController{
	
	@Autowired
	private SmsWhiteInfoProxy smsWhiteInfoProxy;
	
	@Autowired
	private SmsStatisticsInfoProxy smsStatisticsInfoProxy;
	
	@RequestMapping("list")
	public String smswhileList(Model model, String mobile, Integer page, Integer size){
		model.addAttribute("resultInfo", smsWhiteInfoProxy.queryWhiteInfo(mobile, new PageInfo<>(page, size)));
		model.addAttribute("mobile", mobile);
		model.addAttribute("status", smsWhiteInfoProxy.querySmsWhiteInfo().getData().getStatus());
		return "/mem/smswhite/list";
	}
	
	//开关
	@RequestMapping("updatesmswhiteStatus")
	@ResponseBody
	public ResultInfo<Boolean> updatesmswhiteStatus(Model model, Integer status){
		return smsWhiteInfoProxy.updateSmsWhiteInfoStatus(status);
	}
	
	//增加白名单
	@RequestMapping("addSmsWhite")
	@ResponseBody
	public ResultInfo<Boolean> addSmsWhite(Model model, String mobile){
		return smsWhiteInfoProxy.addWhiteMobile(mobile);
	}
	
	//删除白名单
	@RequestMapping("delSmsWhite")
	@ResponseBody
	public ResultInfo<Boolean> delSmsWhite(Model model, String mobile){
		return smsWhiteInfoProxy.delWhiteMobile(mobile);
	}
	
	@RequestMapping("smslimit")
	public String smsLimit(Model model){
		ResultInfo<SmsLimitInfoDto> smsResult = smsStatisticsInfoProxy.querySmsLimitInfo();
		if (smsResult.isSuccess() && smsResult.getData() != null) {
			model.addAttribute("limitInfo", smsResult.getData());
		}
		return "/mem/smswhite/limit";
	}
	//更新限制次数
	@RequestMapping("updateLimitData")
	@ResponseBody
	public ResultInfo<Boolean> updateLimitCount(Model model, SmsLimitInfoDto limitInfoDto){
		return smsStatisticsInfoProxy.updateLimitInfo(limitInfoDto);
	}
	
	@RequestMapping("smsStatistics")
	public String smsStatistics(Model model, String statisticsTime, Integer page, Integer size){
		model.addAttribute("resultInfo", smsStatisticsInfoProxy.queryDailyStatistics(statisticsTime, new PageInfo<>(page, size)));
		model.addAttribute("statisticsTime", statisticsTime);
		return "/mem/smswhite/statistics";
	}
}
