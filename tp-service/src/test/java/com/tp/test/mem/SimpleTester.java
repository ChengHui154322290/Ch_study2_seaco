package com.tp.test.mem;

import java.io.IOException;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.GetMethod;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.google.gson.Gson;
import com.tp.common.util.mem.SmsException;
import com.tp.common.vo.mem.MemberUnionType;
import com.tp.dto.mem.MemCallDto;
import com.tp.enums.common.PlatformEnum;
import com.tp.enums.common.SourceEnum;
import com.tp.exception.ServiceException;
import com.tp.model.mem.MemberDetail;
import com.tp.model.mem.MemberInfo;
import com.tp.redis.util.JedisCacheUtil;
import com.tp.service.cms.ISingleBusTemService;
import com.tp.service.cms.app.IHaitaoAppService;
import com.tp.service.mem.ConsigneeAddressService;
import com.tp.service.mem.IConsigneeAddressService;
import com.tp.service.mem.IMemberDetailService;
import com.tp.service.mem.IMemberInfoService;
import com.tp.service.mem.ISendSmsService;
import com.tp.service.pay.IAppPaymentService;
import com.tp.service.pay.IPayPlatformService;
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath*:beans.xml" })
public class SimpleTester {

	@Autowired
	private IConsigneeAddressService consigneeAddressService;
	
	@Autowired
	ISendSmsService sendSmsService;
	@Autowired
	IMemberInfoService memberInfoService;
	@Autowired
	IHaitaoAppService haitaoAppService;
	@Autowired
	ISingleBusTemService singleBusTemService;
	@Autowired
	IPayPlatformService weixinPayPlatformService;
	@Autowired
	IAppPaymentService appPaymentService;
	@Autowired
	IMemberDetailService memberDetailService;
	@Autowired
	private JedisCacheUtil jedisCacheUtil;
	@Test
	public void testConsigneeAddressService() {
		System.out.println(consigneeAddressService.getDefaultAddress(68921L));
	}

	@Test
	public void testSendMsg(){
		try {
			sendSmsService.sendSms4App("18221092882", 10,"192.168.1.235");
		} catch (SmsException e) {
			e.printStackTrace();
		}
	}
	@Test
	public void testRedis(){
//													15800917796:app_receive_coupon_sms_code
		System.out.println(jedisCacheUtil.getCache("15800917996:app_receive_coupon_sms_code"));
	}

	@Test
	public void hitaoAppService(){
		try {
			System.out.println(weixinPayPlatformService.getAppPayData(12286L, true,"27745"));
		} catch (ServiceException e) {
			e.printStackTrace();
		}
	}
	@Test
	public void simpleTest() {
		MemCallDto dto = new MemCallDto();
//		dto.setMobile("18221092883");
//		dto.setPassword("123456");
		dto.setUnionVal("oJZmXwJl-IMQvCy_YlvAGjuYVqVI");
		dto.setUnionType(MemberUnionType.WEIXIN_UN);
//		dto.setNickName("乡下人");
		dto.setIp("192.168.1.46");
//		dto.setUserName("874050651");
		dto.setPlatform(PlatformEnum.IOS);
		dto.setSource(SourceEnum.XG);
		System.out.println(new Gson().toJson( memberInfoService.unionLogin(dto)));
	}
	
	@Test
	public void testBindMobile(){
		MemCallDto dto = new MemCallDto();
		dto.setMobile("18221092883");
//		dto.setPassword("123456");
//		dto.setUnionVal("87405065");
//		dto.setUnionType(MemberUnionType.QQ);
		dto.setNickName("乡下人");
		dto.setMemberId(68995L);
		dto.setIp("192.168.1.46");
//		dto.setUserName("87405065");
		dto.setPlatform(PlatformEnum.WAP);
		dto.setSource(SourceEnum.XG);
		System.out.println(new Gson().toJson( memberInfoService.bindMobile(dto)));
	}
	@Test
	public void updatePasswordTest() {
		MemCallDto dto = new MemCallDto();
		dto.setMobile("18521516574");
		dto.setPassword("123456");
		dto.setIp("192.168.1.46");
		dto.setSmsCode(123456);
//		dto.set
		
		memberInfoService.updatePasswordApp(dto);
	}

	@Test
	public void memberInfoTester(){
		MemCallDto registerDto = new MemCallDto();
		registerDto.setIp("127.0.0.1");
		registerDto.setMobile("");
		
		MemberInfo query = new MemberInfo();
		query.setMobile("18521516574");
		MemberDetail mem = memberDetailService.selectByUid(68953L);
		System.out.println(mem);
	}

	public static void main(String[] args) {
		HttpClient client = new HttpClient();
		GetMethod m = new GetMethod("http://tj.meituan.com/");
		try {
			client.executeMethod(m);
			System.out.println(m.getResponseBodyAsString());
		} catch (HttpException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	private static String mergePasswordAndSalt(String password, Object salt,
			boolean strict) {
		if (password == null) {
			password = "";
		}
		if (strict && (salt != null)) {
			if ((salt.toString().lastIndexOf("{") != -1)
					|| (salt.toString().lastIndexOf("}") != -1)) {
				throw new IllegalArgumentException(
						"Cannot use { or } in salt.toString()");
			}
		}
		if ((salt == null) || "".equals(salt)) {
			return password;
		} else {
			return password + "{" + salt.toString() + "}";
		}
	}
}
