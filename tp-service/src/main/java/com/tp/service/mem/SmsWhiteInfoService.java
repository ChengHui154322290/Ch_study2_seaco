/**
 * 
 */
package com.tp.service.mem;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.vo.PageInfo;
import com.tp.dto.common.FailInfo;
import com.tp.dto.common.ResultInfo;
import com.tp.model.mem.SmsWhiteInfo;
import com.tp.redis.util.JedisDBUtil;
import com.tp.service.mem.ISmsStatisticsService;
import com.tp.service.mem.ISmsWhiteInfoService;
import com.tp.util.StringUtil;

/**
 * @author Administrator
 * 短信白名单
 */
@Service
public class SmsWhiteInfoService implements ISmsWhiteInfoService{

	private static final String SMS_WHITEINFO_KEY = "sms-whiteinfo-data";
	
	private static final Logger logger = LoggerFactory.getLogger(SmsWhiteInfoService.class);
	
	@Autowired
	private JedisDBUtil jedisDBUtil;
	
	@Autowired
	private ISmsStatisticsService smsStatisticsService;

	@Override
	public ResultInfo<SmsWhiteInfo> querySmsWhiteInfo(){
		SmsWhiteInfo smsWhiteInfo = getSmsWhiteInfo();
		if (null == smsWhiteInfo) {
			logger.error("查询状态 - 获取白名单异常");
			return new ResultInfo<>(new FailInfo("获取白名单异常"));
		}
		return new ResultInfo<>(smsWhiteInfo);
	}
	
	@Override
	public PageInfo<String> queryWhiteInfo(String mobile, PageInfo<String> pageInfo) {
		if (pageInfo == null) {
			pageInfo = new PageInfo<>();
		}
		SmsWhiteInfo smsWhiteInfo = getSmsWhiteInfo();
		if (null == smsWhiteInfo) {
			logger.error("检验短信白名单 - 获取白名单异常 - {}", mobile);
			return pageInfo;
		}
		List<String> rows = new ArrayList<>();
		Integer totalCount = 0;
		Integer totalPage = 0;
		if (StringUtil.isEmpty(mobile)) {
			Integer size = smsWhiteInfo.getMobileList().size();
			pageInfo.setRecords(size);
			Integer begin = (pageInfo.getPage() - 1)*pageInfo.getSize();
			if (begin == null || (begin != null && begin < 0)) {
				begin = 0;
			}
			Integer end = ((begin + pageInfo.getSize()) >= size) ? size : (begin + pageInfo.getSize());
			rows = smsWhiteInfo.getMobileList().subList(begin, end);
			totalCount = size;
			totalPage = pageInfo.getTotal();
		}else{
			String m = StringUtil.trim(mobile);
			if (!smsWhiteInfo.getMobileList().contains(m)) {
				return pageInfo;
			}else{
				rows.add(m);
				totalCount = 1;totalPage = 1;
			}
		}
		pageInfo.setRecords(totalCount);		
		pageInfo.setRows(rows);
		pageInfo.setTotal(totalPage);
		return pageInfo;
	}
	
	
	@Override
	public boolean checkSendSms(String mobile) {
		SmsWhiteInfo smsWhiteInfo = getSmsWhiteInfo();
		if (null == smsWhiteInfo) {
			logger.error("检验短信白名单 - 获取白名单异常 - {}", mobile);
			return false;
		}
		if (1 == smsWhiteInfo.getStatus()) {  //开启状态校验是否在名单内
			String m = StringUtil.trim(mobile);
			if (!smsWhiteInfo.getMobileList().contains(m)) { //不在白名单内
				return false;
			}
		}
		//每日限制(不允许发送)
		if (!smsStatisticsService.checkDailyLimit(mobile)) {
			return false;
		}
		return true;
	}

	//更新状态：0关闭1开启
	@Override
	public ResultInfo<Boolean> updateSmsWhiteInfoStatus(Integer status) {
		if (status != 0 && status != 1) {
			return new ResultInfo<>(new FailInfo("状态错误"));
		}
		SmsWhiteInfo smsWhiteInfo = getSmsWhiteInfo();
		if (null == smsWhiteInfo) {
			logger.error("更新白名单状态 - 获取白名单异常 - {}", status);
			return new ResultInfo<>(new FailInfo("获取白名单异常"));
		}
		smsWhiteInfo.setStatus(status);
		smsWhiteInfo.setUpdateTime(new Date());
		if (!setSmsWhiteInfo(smsWhiteInfo)) {
			return new ResultInfo<>(new FailInfo("更新异常"));
		}
		return new ResultInfo<>(Boolean.TRUE);
	}

	@Override
	public ResultInfo<Boolean> addWhiteMobile(String mobile) {
		if (StringUtil.isEmpty(mobile)) {
			return new ResultInfo<>(new FailInfo("手机号错误"));
		}
		SmsWhiteInfo smsWhiteInfo = getSmsWhiteInfo();
		if (null == smsWhiteInfo) {
			logger.error("增加白名单 - 获取白名单异常 - {}", mobile);
			return new ResultInfo<>(new FailInfo("获取白名单异常"));
		}
		String m = StringUtil.trim(mobile);
		if (smsWhiteInfo.getMobileList().contains(m)) { //已存在白名单内
			return new ResultInfo<>(new FailInfo("已在白名单内"));
		}
		smsWhiteInfo.getMobileList().add(m);
		smsWhiteInfo.setUpdateTime(new Date());
		if (!setSmsWhiteInfo(smsWhiteInfo)) {
			return new ResultInfo<>(new FailInfo("更新异常"));
		}
		return new ResultInfo<>(Boolean.TRUE);
	}

	@Override
	public ResultInfo<Boolean> delWhiteMobile(String mobile) {
		if (StringUtil.isEmpty(mobile)) {
			return new ResultInfo<>(new FailInfo("手机号错误"));
		}
		SmsWhiteInfo smsWhiteInfo = getSmsWhiteInfo();
		if (null == smsWhiteInfo) {
			logger.error("删除白名单 - 获取白名单异常 - {}", mobile);
			return new ResultInfo<>(new FailInfo("获取白名单异常"));
		}
		String m = StringUtil.trim(mobile);
		if (!smsWhiteInfo.getMobileList().contains(m)) { //不存在白名单内
			return new ResultInfo<>(new FailInfo("不在白名单内"));
		}
		smsWhiteInfo.getMobileList().remove(m);
		smsWhiteInfo.setUpdateTime(new Date());
		if (!setSmsWhiteInfo(smsWhiteInfo)) {
			return new ResultInfo<>(new FailInfo("更新异常"));
		}
		return new ResultInfo<>(Boolean.TRUE);
	}
	
	//获取白名单
	private SmsWhiteInfo getSmsWhiteInfo(){
		SmsWhiteInfo whiteInfo = (SmsWhiteInfo) jedisDBUtil.getDB(SMS_WHITEINFO_KEY);
		if (null == whiteInfo) {
			whiteInfo = new SmsWhiteInfo();
			if(false == jedisDBUtil.setDB(SMS_WHITEINFO_KEY, whiteInfo)){
				return null;
			}
		}
		return whiteInfo;
	}
	
	//更新状态
	private boolean setSmsWhiteInfo(SmsWhiteInfo smsWhiteInfo){
		return jedisDBUtil.setDB(SMS_WHITEINFO_KEY, smsWhiteInfo);
	}
}
