package com.tp.service.ord;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.dao.BaseDao;
import com.tp.common.vo.Constant;
import com.tp.common.vo.MqMessageConstant;
import com.tp.common.vo.Constant.DOCUMENT_TYPE;
import com.tp.common.vo.ord.RefundConstant;
import com.tp.dao.ord.RefundInfoDao;
import com.tp.dao.ord.RefundLogDao;
import com.tp.model.ord.RefundInfo;
import com.tp.model.ord.RefundLog;
import com.tp.mq.RabbitMqProducer;
import com.tp.mq.exception.MqClientException;
import com.tp.service.BaseService;
import com.tp.service.IDocumentNumberGenerator;
import com.tp.service.ord.IRefundInfoService;
import com.tp.service.ord.IRejectInfoService;
import com.tp.util.CodeCreateUtil;

@Service
public class RefundInfoService extends BaseService<RefundInfo> implements IRefundInfoService {

	@Autowired
	private RefundInfoDao refundInfoDao;
	@Autowired
	private RefundLogDao refundLogDao;
	@Autowired
	private IRejectInfoService rejectInfoService;
	@Autowired
	private IDocumentNumberGenerator documentNumberGenerator;
	@Autowired
	private RabbitMqProducer rabbitMqProducer;
	
	@Override
	public BaseDao<RefundInfo> getDao() {
		return refundInfoDao;
	}
	
	@Override
	public RefundInfo insert(RefundInfo refundInfo){
		if(refundInfo.getRefundAmount().doubleValue() == 0){
			return null;
		}
		refundInfo.setCreateTime(new Date());
		refundInfo.setRefundCode(CodeCreateUtil.initRefundCode());
		refundInfo.setUpdateTime(new Date());
		if(refundInfo.getUpdateUser() == null)
			refundInfo.setUpdateUser("--");
		refundInfo.setRefundStatus(RefundConstant.REFUND_STATUS.APPLY.code);
		refundInfoDao.insert(refundInfo);
		Long key = refundInfo.getRefundId();
		refundInfo.setRefundId(key);
		
		RefundLog refundLog = new RefundLog();
		refundLog.setRefundCode(refundInfo.getRefundCode());
		refundLog.setOrderCode(refundInfo.getOrderCode());
		refundLog.setActionType(RefundConstant.REFUND_LOG_TYPE.APPLY.code);
		refundLog.setOldRefundStatus(0);
		refundLog.setCurrentRefundStatus(RefundConstant.REFUND_STATUS.APPLY.code);
		refundLog.setLogContent("生成退款单");
		refundLog.setOperatorName(refundInfo.getCreateUser());
		refundLog.setOperatorType(Constant.LOG_AUTHOR_TYPE.MEMBER.code);
		refundLog.setCreateTime(new Date());
		refundLogDao.insert(refundLog);
		return refundInfo;
	}
	
	@Override
	public RefundInfo selectByRefundNo(Long refundCode) {
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("refundCode", refundCode);
		return super.queryUniqueByParams(params);
	}

	@Override
	public void operateAfterRefund(Long refundCode, Boolean isSuccess) {
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("refundCode", refundCode);
		RefundInfo oldRefundInfo = super.queryUniqueByParams(params);
		
		if(null!=oldRefundInfo){
			//数据已退款成功则不再做处理
			if(RefundConstant.REFUND_STATUS.AUDITED.code.intValue() == oldRefundInfo.getRefundStatus().intValue()){
				logger.info("退款已完成，重复通知");
				return;
			}
			RefundInfo toBeUpdated = new RefundInfo();
			toBeUpdated.setRefundId(oldRefundInfo.getRefundId());
			if(isSuccess)
				toBeUpdated.setRefundStatus(RefundConstant.REFUND_STATUS.AUDITED.code);
			else
				toBeUpdated.setRefundStatus(RefundConstant.REFUND_STATUS.FAIL.code);
			refundInfoDao.updateNotNullById(toBeUpdated);
			
			
			RefundLog refundLog = new RefundLog();
			refundLog.setRefundCode(refundCode);
			refundLog.setOrderCode(oldRefundInfo.getOrderCode());
			refundLog.setActionType(RefundConstant.REFUND_LOG_TYPE.REFUND.code);
			refundLog.setOldRefundStatus(oldRefundInfo.getRefundStatus());
			refundLog.setCurrentRefundStatus(toBeUpdated.getRefundStatus());
			if(isSuccess)
				refundLog.setLogContent("退款完成，金额已原路返回");
			else
				refundLog.setLogContent("退款失败");
			refundLog.setOperatorName("system");
			refundLog.setOperatorType(Constant.LOG_AUTHOR_TYPE.SYSTEM.code);
			refundLog.setCreateTime(new Date());
			refundLogDao.insert(refundLog);
			if(RefundConstant.REFUND_TYPE.REJECT.code.equals(oldRefundInfo.getRefundType())){
				rejectInfoService.refundResult(refundCode, isSuccess);
				if(isSuccess){
					try {
						rabbitMqProducer.sendP2PMessage(MqMessageConstant.REFUND_SUCCESS, oldRefundInfo);
					} catch (MqClientException e) {
						logger.error("退款成功后发送消息失败:{}", e);
					}
				}
			}
		}
	}

}
