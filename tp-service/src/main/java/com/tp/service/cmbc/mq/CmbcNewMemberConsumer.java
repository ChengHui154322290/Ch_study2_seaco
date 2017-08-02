package com.tp.service.cmbc.mq;



import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.tp.dto.cmbc.MemberCMBCDto;
import com.tp.mq.MqMessageCallBack;
import com.tp.service.ord.IOrderForCMBCService;

/**
 * 民生银行用户注册推送
 * @author zhs
 *
 */
@Service
public class CmbcNewMemberConsumer implements MqMessageCallBack {
	
	private static final Logger logger = LoggerFactory.getLogger(CmbcNewMemberConsumer.class);
	

	@Autowired
	private IOrderForCMBCService orderForCMBCService;

	@Override
	public boolean execute(Object o) {
		try{
			MemberCMBCDto member = (MemberCMBCDto)o;				
			return orderForCMBCService.pushNewMemberToCMBC(member);			
		}catch(Exception e){
			logger.error(e.getMessage());
			return true;
		}
	}

}
