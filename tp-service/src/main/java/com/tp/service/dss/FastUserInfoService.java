package com.tp.service.dss;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.function.BiConsumer;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.dao.BaseDao;
import com.tp.common.util.mem.Sms;
import com.tp.common.vo.Constant;
import com.tp.common.vo.FastConstant;
import com.tp.dao.dss.FastUserInfoDao;
import com.tp.model.dss.FastUserInfo;
import com.tp.model.mem.MemberInfo;
import com.tp.service.BaseService;
import com.tp.service.dss.IFastUserInfoService;
import com.tp.service.mem.IMemberInfoService;
import com.tp.service.mem.ISendSmsService;
import com.tp.util.StringUtil;
import com.tp.util.ThreadUtil;

@Service
public class FastUserInfoService extends BaseService<FastUserInfo> implements IFastUserInfoService {

	private static final Map<String,Integer> SEND_MOBILE_MAP = new ConcurrentHashMap<String,Integer>();
	private static volatile Boolean sendSmsSign = false;
	static{
		sendMsm();
	}
	@Autowired
	private FastUserInfoDao fastUserInfoDao;
	@Autowired
	private IMemberInfoService memberInfoService;
	private static ISendSmsService sendSmsService;
	@Override
	public BaseDao<FastUserInfo> getDao() {
		return fastUserInfoDao;
	}

	@Autowired(required = true)
	public void setUserAccessor(ISendSmsService sendSmsService) {
		FastUserInfoService.sendSmsService = sendSmsService;
	}
	@Override
	public void sendUrgeOrderSms(Long warehouseId){
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("userType", FastConstant.USER_TYPE.MANAGER.code);
		params.put("warehouseId", warehouseId);
		params.put("enabled", Constant.ENABLED.YES);
		FastUserInfo fastUserInfo = this.queryUniqueByParams(params);
		if(fastUserInfo!=null && StringUtils.isNotBlank(fastUserInfo.getMobile())){
			String mobile = fastUserInfo.getMobile();
			Integer count = SEND_MOBILE_MAP.get(mobile);
			SEND_MOBILE_MAP.put(mobile, (count!=null?count:0)+1);
			sendSmsSign=true;
		}
	}
	
	public static void sendMsm(){
		Runnable runnable = new Runnable(){
			@Override
			public void run() {
				while(true){
					if(sendSmsSign){
						if(MapUtils.isNotEmpty(SEND_MOBILE_MAP)){
							final List<Sms> smsList = new ArrayList<Sms>();
							SEND_MOBILE_MAP.forEach(new BiConsumer<String,Integer>(){
								public void accept(String t, Integer u) {
									Sms sms = new Sms();
									sms.setMobile(t);
									sms.setSendTime(new Date());
									sms.setContent("5分钟内共有"+u+"位速购会员催单，请你及时处理");
									smsList.add(sms);
								}
							});
							sendSmsService.batchSendSms(smsList);
							SEND_MOBILE_MAP.clear();
						}
						try {
							TimeUnit.MINUTES.sleep(5);
						} catch (InterruptedException e) {
						}
					}
				}
			}
		};
		ThreadUtil.excAsync(runnable,false);
	}
}
