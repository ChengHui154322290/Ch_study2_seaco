package com.tp.scheduler.order;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.tp.common.util.mem.Sms;
import com.tp.model.mem.MemberInfo;
import com.tp.model.ord.OrderInfo;
import com.tp.scheduler.AbstractJobRunnable;
import com.tp.scheduler.JobConstant;
import com.tp.service.dss.IPromoterInfoService;
import com.tp.service.mem.IMemberInfoService;
import com.tp.service.mem.ISendSmsService;
import com.tp.service.ord.remote.ISalesOrderRemoteService;
import com.tp.util.StringUtil;

/**
 * 用户下单后15分钟内未付款发短信提醒任务调度
 * 
 * @author szy
 *
 */
@Component
public class MobileMessageRemindJob extends AbstractJobRunnable {
	private static final Logger LOG = LoggerFactory.getLogger(MobileMessageRemindJob.class);
	private static final String CURRENT_JOB_PREFIXED = "sendmobilemessage";

	private static final int UN_PAYED_EXPIRED_MINUTE_DEFAULT = 15;

	@Autowired
	private ISendSmsService sendSmsService;
	@Autowired
	private ISalesOrderRemoteService orderInfoRemoteService;
	@Autowired
	private IMemberInfoService memberInfoService;
    @Autowired
	private IPromoterInfoService promoterInfoService;
	@Autowired
	JobConstant jobConstant;

	@Override
	@Transactional
	public void execute() {
		Integer expired = jobConstant.getSendMobileMessageMinute();
		if (null == expired) {
			expired = UN_PAYED_EXPIRED_MINUTE_DEFAULT;
		}
		LOG.info("用户下单后15分钟内未付款发短信提醒job启动....");
		List<OrderInfo> orderInfoList = orderInfoRemoteService.querySalesOrderByUnPayOverFifteenMinutes(expired);
		
		if (CollectionUtils.isNotEmpty(orderInfoList)) {

			List<Long> memberIds = new ArrayList<Long>();

			// 根据父订单list组装memberId list
			for (OrderInfo orderInfoDO : orderInfoList) {
				memberIds.add(orderInfoDO.getMemberId());
			}

			// 根据memberId list查询 user list
			Map<Long, String> userMap = new HashMap<Long, String>();
			List<MemberInfo> users = memberInfoService.selectByIds(memberIds);
			if (CollectionUtils.isNotEmpty(users)) {
				for (MemberInfo user : users) {
					userMap.put(user.getId(), user.getMobile());
				}
			}

			// 未支付的订单，15分钟后发送短信提醒
			List<Sms> smsList = new ArrayList<Sms>();
			for (OrderInfo orderInfo : orderInfoList) {

				Long memberId = orderInfo.getMemberId();
				String content = String.format("亲爱的会员：您的订单%s已生成，请在15分钟内完成付款，过时订单将自动取消！", orderInfo.getParentOrderCode());
				if(StringUtil.isNoneBlank(orderInfo.getChannelCode())){
					content = "【"+promoterInfoService.queryShortNameByChannelCode(orderInfo.getChannelCode())+"】"+content;
				}
				smsList.add(new Sms(userMap.get(memberId), content));
			}
			try {
				LOG.debug("调用发送短信接口入参：smsList：{}", JSONArray.fromObject(smsList).toString());
				sendSmsService.batchSendSms(smsList);
			} catch (Exception e) {
				LOG.error("调批量发送短信接口发送失败：" + e);
			}

		} else {
			LOG.info("不存在用户下单后15分钟内未支付订单");
		}
		LOG.info("用户下单后15分钟内未付款发短信提醒job完成");
	}

	@Override
	public String getFixed() {
		return CURRENT_JOB_PREFIXED;
	}

}
