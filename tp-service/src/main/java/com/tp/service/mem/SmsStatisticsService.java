package com.tp.service.mem;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.vo.PageInfo;
import com.tp.dto.common.ResultInfo;
import com.tp.dto.mem.SmsLimitInfoDto;
import com.tp.dto.mem.SmsStatisticsItemDto;
import com.tp.model.mem.SmsStatisticsInfo;
import com.tp.model.mem.SmsStatisticsItem;
import com.tp.redis.util.JedisDBUtil;
import com.tp.service.mem.ISmsStatisticsService;
import com.tp.util.DateUtil;
import com.tp.util.StringUtil;

/**
 * 短信统计 
 */
@Service
public class SmsStatisticsService implements ISmsStatisticsService{

	private static final String SMS_STATISTICS_KEY = "sms-statistics-data";
	
	//每日总数
	private static final String SMS_LIMIT_DAILY_KEY = "sms-limit-daily-amount";
	//每日每号总数
	private static final String SMS_LIMIT_SINGLE_DAILY_KEY = "sms-limit-single-daily-amount";
	
	private static final Logger logger = LoggerFactory.getLogger(SmsStatisticsService.class);
	
	@Autowired
	private JedisDBUtil jedisDBUtil;

	@Override
	public boolean checkDailyLimit(String mobile){
		SmsStatisticsInfo smsStatisticsInfo = getSmsStatisticsInfo();
		if (null == smsStatisticsInfo) {
			logger.error("短信统计 - 校验每日次数限制 - {}", mobile);
			return true;
		}	
		SmsLimitInfoDto smsLimitInfoDto = queryLimitInfo();
		String dayTime = DateUtil.format(new Date(), "yyyy-MM-dd");
		if (smsLimitInfoDto.getDailyLimitCount() != null && smsLimitInfoDto.getDailyLimitCount() != -1) {
			Integer daily = smsStatisticsInfo.getDailyStatistics().get(dayTime);
			if (daily != null && daily >= smsLimitInfoDto.getDailyLimitCount()) {
				return false;
			}
		}
		
		if (smsLimitInfoDto.getSingleDailyLimitCount() != null && smsLimitInfoDto.getSingleDailyLimitCount() != -1) {
			SmsStatisticsItem item = smsStatisticsInfo.getMobileStatistics().get(mobile);
			Integer singleDaily = null;
			if (item != null && 
					(singleDaily = item.getDailyStatistics().get(dayTime)) != null && 
					singleDaily >= smsLimitInfoDto.getSingleDailyLimitCount()) {
				return false;
			}
		}
		return true;
	}
	
	@Override
	public void statisticsSmsSend(String mobile, boolean success){
		if (StringUtil.isEmpty(mobile)) {
			logger.error("短信统计 - 手机号为空");
			return;
		}
		SmsStatisticsInfo smsStatisticsInfo = getSmsStatisticsInfo();
		if (null == smsStatisticsInfo) {
			logger.error("短信统计 - 获取统计数据异常 - {}", mobile);
			return;
		}
		
		String dayTime = DateUtil.format(new Date(), "yyyy-MM-dd");
		if (success) {
			//总数及每日数量
			smsStatisticsInfo.setAmount(smsStatisticsInfo.getAmount() + 1);
			Integer daily = smsStatisticsInfo.getDailyStatistics().get(dayTime);
			if (daily == null) {
				daily = 0;
			}
			smsStatisticsInfo.getDailyStatistics().put(dayTime, daily + 1);
			
			//单手机号数量
			SmsStatisticsItem item = smsStatisticsInfo.getMobileStatistics().get(mobile);
			if (item == null) {
				item = new SmsStatisticsItem(mobile);
			}
			item.setTotalAmount(item.getTotalAmount() + 1);
			Integer mobileDaily = item.getDailyStatistics().get(dayTime);
			if (mobileDaily == null) {
				mobileDaily = 0;
			}
			item.getDailyStatistics().put(dayTime, mobileDaily + 1);
			smsStatisticsInfo.getMobileStatistics().put(mobile, item);
		}
		
		if (!setSmsStatisticsInfo(smsStatisticsInfo)) {
			logger.error("短信统计 - 更新异常");
		}
	}
	
	//更新限制条件
	@Override
	public ResultInfo<Boolean> updateLimitInfo(SmsLimitInfoDto limitInfo) {
		if (limitInfo.getDailyLimitCount() != null && limitInfo.getDailyLimitCount() >= -1) {
			setLimitInfo(SMS_LIMIT_DAILY_KEY, limitInfo.getDailyLimitCount());
		}
		if (limitInfo.getSingleDailyLimitCount() != null && limitInfo.getSingleDailyLimitCount() >= -1) {
			setLimitInfo(SMS_LIMIT_SINGLE_DAILY_KEY, limitInfo.getSingleDailyLimitCount());
		}
		return new ResultInfo<>(Boolean.TRUE);
	}
	
	@Override
	public SmsLimitInfoDto queryLimitInfo(){
		SmsLimitInfoDto limitInfoDto = new SmsLimitInfoDto();
		limitInfoDto.setDailyLimitCount(getLimitInfo(SMS_LIMIT_DAILY_KEY));
		limitInfoDto.setSingleDailyLimitCount(getLimitInfo(SMS_LIMIT_SINGLE_DAILY_KEY));
		return limitInfoDto;
	}
	//统计
	@Override
	public PageInfo<SmsStatisticsItemDto> queryDailyStatistics(String dayTime, PageInfo<SmsStatisticsItemDto> pageInfo) {
		SmsStatisticsInfo smsStatisticsInfo = getSmsStatisticsInfo();
		if (null == smsStatisticsInfo) {
			logger.error("短信统计 - 获取统计数据异常 - {}", dayTime);
			return pageInfo;
		}
		Integer totalCount = 0;
		Integer totalPage = 0;
		List<String> keyRows = new ArrayList<>();
		if (StringUtil.isEmpty(dayTime)) {
			Integer size = smsStatisticsInfo.getDailyStatistics().size();
			List<String> keyList = new ArrayList<>(smsStatisticsInfo.getDailyStatistics().keySet());
			pageInfo.setRecords(size);
			Integer begin = (pageInfo.getPage() - 1)*pageInfo.getSize();
			if (begin == null || (begin != null && begin < 0)) {
				begin = 0;
			}
			Integer end = ((begin + pageInfo.getSize()) >= size) ? size : (begin + pageInfo.getSize());
			keyRows = keyList.subList(begin, end);
			totalCount = size;
			totalPage = pageInfo.getTotal();
		}else{
			String m = StringUtil.trim(dayTime);
			if (null == smsStatisticsInfo.getDailyStatistics().get(m)) {
				return pageInfo;
			}else{
				keyRows.add(m);
				totalCount = 1;totalPage = 1;
			}
		}
		List<SmsStatisticsItemDto> itemDtoList = new ArrayList<>();
		for (String dt : keyRows) {
			SmsStatisticsItemDto item = new SmsStatisticsItemDto();
			item.setDayTime(dt);
			item.setDailyAmount(smsStatisticsInfo.getDailyStatistics().get(dt));
			itemDtoList.add(item);
		}
		pageInfo.setRecords(totalCount);		
		pageInfo.setRows(itemDtoList);
		pageInfo.setTotal(totalPage);
		return pageInfo;
	}
	
	
	/*-------------------------------------------------------------------------*/
	//获取统计数据
	private SmsStatisticsInfo getSmsStatisticsInfo(){
		SmsStatisticsInfo si = (SmsStatisticsInfo) jedisDBUtil.getDB(SMS_STATISTICS_KEY);
		if (null == si) {
			si = new SmsStatisticsInfo();
			if(false == jedisDBUtil.setDB(SMS_STATISTICS_KEY, si)){
				return null;
			}
		}
		return si;
	}
	
	//更新状态
	private boolean setSmsStatisticsInfo(SmsStatisticsInfo si){
		return jedisDBUtil.setDB(SMS_STATISTICS_KEY, si);
	}
	
	private Integer getLimitInfo(String key){
		Integer v = (Integer)jedisDBUtil.getDB(key);
		if (null == v) {
			v = -1;	//不限制
			if (false == jedisDBUtil.setDB(key, v)) {
				return null;
			}
		}
		return v;
	}
	private boolean setLimitInfo(String key, Integer v){
		return jedisDBUtil.setDB(key, v);
	}
}
