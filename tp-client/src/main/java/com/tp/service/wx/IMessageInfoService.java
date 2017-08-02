package com.tp.service.wx;

import java.util.List;

import com.tp.dto.wx.KVDto;
import com.tp.model.wx.MessageInfo;
import com.tp.service.IBaseService;

public interface IMessageInfoService extends IBaseService<MessageInfo>{

	public String getTriggerMsg(String code,String key);
	
	public List<KVDto> getKeywordMsg();
	
	public String getMessage(String code,String key);
	
	public Integer updateStatus(MessageInfo messageInfo);
	
	public Boolean saveMessage(MessageInfo messageInfo);
	
}
