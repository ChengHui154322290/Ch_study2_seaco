package com.tp.test.cmbc;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.tp.common.vo.MqMessageConstant;
import com.tp.common.vo.PageInfo;
import com.tp.model.mem.MemberInfo;
import com.tp.model.mkt.ChannelPromote;
import com.tp.mq.RabbitMqProducer;
import com.tp.mq.exception.MqClientException;
import com.tp.service.mkt.IChannelPromoteService;
import com.tp.test.BaseTest;

public class CMBCTest extends BaseTest{

	@Autowired
	RabbitMqProducer rabbitMqProducer;
	
	@Test
	public void testcmbcnewregister(){
		MemberInfo info = new MemberInfo();
		info.setEmail("zahfdsf1213");
		try {
			rabbitMqProducer.sendP2PMessage(MqMessageConstant.CMBC_NEW_REGISTER, info);
		} catch (MqClientException e) {
			e.printStackTrace();
		}
	}
	
}
