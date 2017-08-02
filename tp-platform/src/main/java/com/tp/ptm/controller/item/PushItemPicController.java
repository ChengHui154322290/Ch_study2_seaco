package com.tp.ptm.controller.item;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tp.common.vo.ptm.ErrorCodes.SystemError;
import com.tp.dto.prd.PushItemPicDto;
import com.tp.dto.ptm.ReturnData;
import com.tp.ptm.annotation.Authority;
import com.tp.ptm.ao.item.PushItemInfoServiceAO;
import com.tp.ptm.ao.item.PushItemPicServiceAO;

@RequestMapping("/item")
@Controller
public class PushItemPicController {

	@Autowired
	private PushItemInfoServiceAO pushItemInfoServiceAO;
	
	@Autowired
	private PushItemPicServiceAO pushItemPicAo;
	
	private Logger logger = LoggerFactory.getLogger(PushItemPicController.class);
	
	@RequestMapping(value="/pushItemPic",method = RequestMethod.POST)
	@ResponseBody
	@Authority
	public ReturnData pushItemPic(HttpServletRequest request, HttpServletResponse response, @RequestBody PushItemPicDto pushItemPicDto ){
		
		String appKey = request.getParameter("appkey");
		Long currentUserId= pushItemInfoServiceAO.getCurrentUserIdByAppKey(appKey);
		if(null ==currentUserId){
			logger.info("未获取到用户信息");
		} else {
			logger.info(currentUserId.toString());
		}
		if(currentUserId == null){
			logger.error("未获取到banckend user 关联的当前用户id");
			return new ReturnData(Boolean.FALSE, SystemError.PARAM_ERROR.code, SystemError.PARAM_ERROR.cnName);
		}
		ReturnData rtData = new ReturnData(Boolean.TRUE);
		if (pushItemPicDto != null ) {
			rtData	=pushItemPicAo.pushItemPic(pushItemPicDto,request,currentUserId,appKey);
		}
		else {
			rtData = new ReturnData(Boolean.FALSE, SystemError.PARAM_ERROR.code,"未获取到json数据");
		}
		
		if(!rtData.getIsSuccess()){
			logger.info("商品插入不成功,返回错误信息为:"+rtData.getData().toString());
		}
		return rtData;
	}
	
	
}
