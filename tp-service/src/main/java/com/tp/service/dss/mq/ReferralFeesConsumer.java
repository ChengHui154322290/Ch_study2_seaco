package com.tp.service.dss.mq;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.tp.common.util.ExceptionUtils;
import com.tp.common.vo.Constant;
import com.tp.common.vo.DssConstant;
import com.tp.dto.common.FailInfo;
import com.tp.model.dss.PromoterInfo;
import com.tp.model.dss.RefereeDetail;
import com.tp.model.pay.PaymentInfo;
import com.tp.mq.MqMessageCallBack;
import com.tp.service.dss.IPromoterInfoService;
import com.tp.service.dss.IRefereeDetailService;
import com.tp.util.JsonUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * 注册成功后获取是否是拉新用户，如果是则提佣金
 * @author szy
 *
 */
@Service
public class ReferralFeesConsumer implements MqMessageCallBack {
	
	private static final Logger logger = LoggerFactory.getLogger(ReferralFeesConsumer.class);

	//拉新佣金
	@Value("#{meta['referral.fess']}")
	private Double referralFess=50.00d;
	@Autowired
	private IRefereeDetailService refereeDetailService;
	@Autowired
	private IPromoterInfoService promoterInfoService;
	@Override
	public boolean execute(Object o) {
		if(o!=null && o instanceof PaymentInfo ){
			PaymentInfo paymentInfo = (PaymentInfo)o;

			logger.info("[RECEIVE_DSS_PAY_SUCCESS_CALLBACK_]"+ JsonUtil.convertObjToStr(paymentInfo));
			if(paymentInfo.getBizCode()!=null){


				PromoterInfo promoterInfo = new PromoterInfo();

				Long memberId = Long.parseLong(paymentInfo.getCreateUser());
				PromoterInfo query = new PromoterInfo();
				query.setMemberId(memberId);
				query.setPromoterType(1);
				promoterInfo = promoterInfoService.queryUniqueByObject(query);
				logger.info("QUERY_PROMOTER_INFO_BY_MEMBER_ID: MEMBER_ID = {},RESULT={}",memberId, JSON.toJSONString(paymentInfo));
				if(paymentInfo == null){
					promoterInfo = promoterInfoService.queryById(promoterInfo.getPromoterIdByBizCode(paymentInfo.getBizCode()));
				}
				if(promoterInfo==null){
					ExceptionUtils.print(new FailInfo("根据支付信息不能找到分销员信息"), logger,o);
					return Boolean.TRUE;
				}
				PromoterInfo promoter = new PromoterInfo();
				promoter.setPromoterId(promoterInfo.getPromoterId());
				promoter.setPromoterStatus(DssConstant.PROMOTER_STATUS.EN_PASS.code);
				promoter.setUpdateUser(paymentInfo.getCreateUser());
				promoter.setUpdateTime(new Date());
				promoter.setPassTime(new Date());
				promoterInfoService.updateNotNullById(promoter);
				if(paymentInfo.getAmount()!=null && paymentInfo.getAmount()>50){
					referail(promoterInfo);
				}
			}
		}
		return true;
	}

	private void referail(PromoterInfo promoterInfo){
		if(promoterInfo.getParentPromoterId()==null){
			return;
		}
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("memberId", promoterInfo.getPromoterId());
		Integer count = refereeDetailService.queryByParamCount(params);
		if(count>0){
			return;
		}
		RefereeDetail refereeDetail = new RefereeDetail();
		refereeDetail.setCommision(referralFess);
		refereeDetail.setCreateUser(Constant.AUTHOR_TYPE.MEMBER+promoterInfo.getPromoterName());
		refereeDetail.setMemberId(promoterInfo.getPromoterId());
		refereeDetail.setPromoterId(promoterInfo.getParentPromoterId());
		try{
			refereeDetailService.insert(refereeDetail);
		}catch(Throwable throwable){
			ExceptionUtils.print(new FailInfo(throwable), logger, promoterInfo);
		}
	}
}
