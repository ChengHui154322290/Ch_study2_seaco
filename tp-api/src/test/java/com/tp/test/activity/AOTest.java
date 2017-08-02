package com.tp.test.activity;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.gson.Gson;
import com.tp.dto.mmp.enums.TopicType;
import com.tp.m.ao.activity.ActivityAO;
import com.tp.test.base.BaseTest;

public class AOTest extends BaseTest{
	@Autowired
	private ActivityAO activityAO;
	
	@Test
	public void getCache(){
		System.out.println(new Gson().toJson(activityAO.sendCoupon2UserUnique("18221092883", "西客送温暖")));
	}
	@Test
	public void getTopic (){
		System.out.println(activityAO.getTopicLinkByTopicName("库", TopicType.SINGLE.ordinal(), 66867L));
	}
	
}
