package com.tp.proxy.wx;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.dto.common.FailInfo;
import com.tp.dto.common.ResultInfo;
import com.tp.exception.ServiceException;
import com.tp.model.wx.MessageInfo;
import com.tp.proxy.BaseProxy;
import com.tp.service.IBaseService;
import com.tp.service.wx.IMessageInfoService;

@Service
public class MessageInfoProxy extends BaseProxy<MessageInfo>{

	@Autowired
	private IMessageInfoService messageInfoService;
	
	@Override
	public IBaseService<MessageInfo> getService() {
		return messageInfoService;
	}
	
	public ResultInfo<Boolean> saveMessage(MessageInfo messageInfo){
		try{
			Boolean result = messageInfoService.saveMessage(messageInfo);
			if(result)return new ResultInfo<>(result);
			return new ResultInfo<>(new FailInfo("更新失败"));
		}catch(ServiceException es){
			return new ResultInfo<>(new FailInfo(es.getMessage()));
		}
	}
	
	public String getMessage(String code,String key){
		return messageInfoService.getMessage(code, key);
	}
	
	public ResultInfo<Integer> updateStatus(MessageInfo messageInfo){
		return new ResultInfo<>(messageInfoService.updateStatus(messageInfo));
	}
}
