package com.tp.service.wx;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.shiro.util.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.dao.BaseDao;
import com.tp.common.vo.wx.MessageConstant;
import com.tp.dao.wx.MessageInfoDao;
import com.tp.dto.wx.KVDto;
import com.tp.exception.ServiceException;
import com.tp.model.wx.MessageInfo;
import com.tp.service.BaseService;
import com.tp.service.wx.IMessageInfoService;
import com.tp.service.wx.cache.MessageCache;
import com.tp.util.StringUtil;

@Service
public class MessageInfoService extends BaseService<MessageInfo> implements IMessageInfoService{

	@Autowired
	private MessageInfoDao messageInfoDao;
	
	@Autowired
	private MessageCache messageCache;
	
	@Override
	public BaseDao<MessageInfo> getDao() {
		return messageInfoDao;
	}
	
	public String getTriggerMsg(String code,String key){
		MessageInfo m = new MessageInfo();
		m.setIsDel(0);
		m.setStatus(1);
		m.setCode(code);
		if(StringUtil.equalsIgnoreCase(code, MessageConstant.SCENE.CLICK.getCode()) && StringUtil.isNotBlank(key)){
			m.setcKey(key);
		}
		List<MessageInfo> result = messageInfoDao.queryByObject(m);
		if(!CollectionUtils.isEmpty(result)){
			return result.get(0).getContent();
		}
		return null;
	}
	
	@Override
	public List<KVDto> getKeywordMsg() {
		MessageInfo m = new MessageInfo();
		m.setIsDel(0);
		m.setStatus(1);
		m.setCode(MessageConstant.SCENE.KEYWORD.getCode());
		List<MessageInfo> result = messageInfoDao.queryByObject(m);
		List<KVDto> l = new ArrayList<>();
		if(!CollectionUtils.isEmpty(result)){
			for(MessageInfo mi : result){
				l.add(new KVDto(mi.getName(),mi.getContent()));
			}
		}
		return l;
	}

	@Override
	public String getMessage(String code, String key) {
		if(StringUtil.equalsIgnoreCase(code, MessageConstant.SCENE.SUBSCRIBE.getCode())){
			return messageCache.getMessageSubscribeCache();
		}else if(StringUtil.equalsIgnoreCase(code, MessageConstant.SCENE.CLICK.getCode()) && StringUtil.isNotBlank(key)){
			return messageCache.getMessageClickCache(key);
		}else if(StringUtil.equalsIgnoreCase(code, MessageConstant.SCENE.OFFLINE.getCode())){
			return messageCache.getMessageOfflineCache();
		}else if(StringUtil.equalsIgnoreCase(code, MessageConstant.SCENE.KEYWORD.getCode())&& StringUtil.isNotBlank(key)){
			List<KVDto> msgList = messageCache.getMessageKeywordCache();
			if(!CollectionUtils.isEmpty(msgList)){
				for(KVDto kv : msgList){
					String[] keys = kv.getKey().split("_");
					for(String k : keys){
						if(StringUtil.equalsIgnoreCase(k.trim(), key)){
							return kv.getValue();
						}
					}
				}
			}
		}
		return StringUtil.EMPTY;
	}

	@Override
	public Integer updateStatus(MessageInfo messageInfo) {
		Integer r = messageInfoDao.updateNotNullById(messageInfo);
		if(r>0){
			messageInfo = messageInfoDao.queryById(messageInfo.getId());
			if(messageInfo.getStatus() == 0)messageCache.delMessageCache(messageInfo.getCode(), messageInfo.getcKey());
			else messageCache.updateMessageCache(messageInfo.getCode(), messageInfo.getcKey(), messageInfo.getContent());
		}
		return r;
	}

	@Override
	public Boolean saveMessage(MessageInfo messageInfo) {
		if(messageInfo.getId()==null){
			if(StringUtil.isBlank(messageInfo.getCode()))throw new ServiceException("场景不能为空");
			//除去click事件的关注欢迎语只能有一条
			if(!StringUtil.equalsIgnoreCase(messageInfo.getCode(), MessageConstant.SCENE.CLICK.getCode()) &&!StringUtil.equalsIgnoreCase(messageInfo.getCode(), MessageConstant.SCENE.KEYWORD.getCode())){
				MessageInfo m = new MessageInfo();
				m.setIsDel(0);
				m.setCode(messageInfo.getCode());
				List<MessageInfo> result = messageInfoDao.queryByObject(m);
				if(!CollectionUtils.isEmpty(result)) throw new ServiceException("菜单点击事件或关键字回复除外的消息只能有一条");
			}
			messageInfo.setIsDel(0);
			messageInfo.setStatus(0);
			messageInfo.setCreateTime(new Date());
			messageInfo.setModifyTime(new Date());
			messageInfoDao.insert(messageInfo);
			return  messageInfo== null?false:true;
		}else{
			messageInfo.setModifyTime(new Date());
			messageCache.updateMessageCache(messageInfo.getCode(), messageInfo.getcKey(), messageInfo.getContent());
			return messageInfoDao.updateNotNullById(messageInfo) > 0?true:false;
		}
	}
}
