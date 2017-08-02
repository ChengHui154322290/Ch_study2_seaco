package com.tp.service.mmp;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.tp.common.dao.BaseDao;
import com.tp.common.util.mmp.BeanUtil;
import com.tp.common.vo.mmp.CouponAuditConstant;
import com.tp.common.vo.mmp.ProcessingErrorMessage;
import com.tp.dao.mmp.CouponSendAuditDao;
import com.tp.dao.mmp.CouponSendDao;
import com.tp.dto.common.ResultInfo;
import com.tp.dto.mmp.enums.CouponSendStatus;
import com.tp.dto.mmp.enums.CouponStatus;
import com.tp.exception.ServiceException;
import com.tp.model.mmp.CouponSend;
import com.tp.model.mmp.CouponSendAudit;
import com.tp.service.BaseService;
import com.tp.service.mmp.ICouponSendService;

@Service
public class CouponSendService extends BaseService<CouponSend> implements ICouponSendService {

	@Autowired
	private CouponSendDao couponSendDao;

	@Autowired
	private CouponSendAuditDao couponSendAuditDao;
	
	@Override
	public BaseDao<CouponSend> getDao() {
		return couponSendDao;
	}


	@Override
	public ResultInfo stopCouponSend(Long couponSendId, Long userId,
										String userName) throws ServiceException {
		if (null == couponSendId || 0 == couponSendId) {
			throw new ServiceException(
					ProcessingErrorMessage.VALID_COUPON_ID);
		}
		try {
			// 更新状态
			CouponSend CouponSend = new CouponSend();
			CouponSend.setId(couponSendId);
			CouponSend.setStatus(CouponSendStatus.STOP.ordinal() );
			CouponSend.setModifyTime(new Date());
			couponSendDao.updateNotNullById(CouponSend);

			// 新增执行动作记录
			this.saveAuditLog(couponSendId, CouponSendStatus.STOP.ordinal(), userId, userName, null);
		} catch (ServiceException e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(
					ProcessingErrorMessage.STOPCOUPON_FAILD);
		}
		return new ResultInfo();
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public ResultInfo refuseCouponSend(Long couponSendId, Long userId, String userName, String remark)  throws ServiceException{
		if (null == couponSendId || 0 == couponSendId) {
			throw new ServiceException(
					ProcessingErrorMessage.VALID_COUPON_ID);
		}
		try {
			// 更新状态
			CouponSend CouponSend = new CouponSend();
			CouponSend.setId(couponSendId);
			CouponSend.setStatus(CouponSendStatus.REFUSED.ordinal() );
			CouponSend.setModifyTime(new Date());
			couponSendDao.updateNotNullById(CouponSend);

			// 新增执行动作记录
			this.saveAuditLog(couponSendId, CouponSendStatus.REFUSED.ordinal(), userId, userName, remark);
		} catch (ServiceException e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(
					ProcessingErrorMessage.REFUSE_COUPON_FAILD);
		}
		return new ResultInfo();
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public ResultInfo approveCouponSend(Long couponSendId, Long userId, String userName, List<String> res)  throws ServiceException{
		if (null == couponSendId || 0 == couponSendId) {
			throw new ServiceException(
					ProcessingErrorMessage.VALID_COUPON_ID);
		}
		try {
			// 更新状态
			CouponSend CouponSend = new CouponSend();
			CouponSend.setId(couponSendId);
			CouponSend.setStatus(CouponSendStatus.PASSED.ordinal() );
			CouponSend.setModifyTime(new Date());
			if(res != null && res.size() > 0 ){
				StringBuffer sb = new StringBuffer();
				for(String resline : res){
					sb.append(resline);
					sb.append(";");
				}
				CouponSend.setSendResult(res.toString());
			} else
				CouponSend.setSendResult("发放成功");
			couponSendDao.updateNotNullById(CouponSend);

			// 新增执行动作记录
			this.saveAuditLog(couponSendId, CouponSendStatus.PASSED.ordinal(), userId, userName, null);
		} catch (ServiceException e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(
					ProcessingErrorMessage.PASSED_COUPON_FAILD);
		}
		return new ResultInfo();
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public ResultInfo cancelCouponSend(Long couponSendId, Long userId, String userName)  throws ServiceException{
		if (null == couponSendId || 0 == couponSendId) {
			throw new ServiceException(
					ProcessingErrorMessage.VALID_COUPON_ID);
		}
		try {
			// 更新状态
			CouponSend CouponSend = new CouponSend();
			CouponSend.setId(couponSendId);
			CouponSend.setStatus(CouponSendStatus.CANCELED.ordinal() );
			CouponSend.setModifyTime(new Date());
			couponSendDao.updateNotNullById(CouponSend);

			// 新增执行动作记录
			this.saveAuditLog(couponSendId, CouponSendStatus.CANCELED.ordinal(), userId, userName, null);
		} catch (ServiceException e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(
					ProcessingErrorMessage.CANCELED_COUPON_FAILD);
		}
		return new ResultInfo();
	}

	/**
	 * 保存审批记录
	 *
	 * @param topicId
	 *            活动Id
	 * @param status
	 *            活动状态
	 * @param userId
	 *            当前编辑人员Id
	 * @param userName
	 *            当前编辑人员名
	 * @throws ServiceException
	 */
	private void saveAuditLog(Long couponSendId, Integer status, Long userId, String userName, String remark) throws ServiceException {

		CouponSendAudit auditLog = new CouponSendAudit();
		auditLog.setCouponSendId(couponSendId);
		auditLog.setAuditId(userId);
		auditLog.setAuditName(userName);
		auditLog.setCreateTime(new Date());
		auditLog.setCreateUser(userName);
		if(remark != null)
			auditLog.setRemark(remark);
		if (CouponStatus.STOP.ordinal() == status) {
			auditLog.setAuditOperation(CouponAuditConstant.STATUS_TERMINATION);
		} else if (CouponStatus.AUDITING.ordinal()  == status) {
			auditLog.setAuditOperation(CouponAuditConstant.STATUS_AUDITING_VALUE);
		} else if (CouponStatus.CANCELED.ordinal() == status) {
			auditLog.setAuditOperation(CouponAuditConstant.STATUS_CANCELED_VALUE);
		} else if (CouponStatus.PASSED.ordinal() == status) {
			auditLog.setAuditOperation(CouponAuditConstant.STATUS_AUDITED_VALUE);
		} else if (CouponStatus.REFUSED.ordinal() == status) {
			auditLog.setAuditOperation(CouponAuditConstant.STATUS_REFUSED_VALUE);
		} else {
			return;
		}
		BeanUtil.processNullField(auditLog);
		couponSendAuditDao.insert(auditLog);
	}

	@Override
	public List<CouponSendAudit> queryCouponSendAudit(Long id) throws ServiceException {
		CouponSendAudit auditLog = new CouponSendAudit();
		auditLog.setCouponSendId(id);
		try {
			return couponSendAuditDao.queryByObject(auditLog);
		} catch (ServiceException e) {
			throw new ServiceException(e.getMessage());
		}
	}

}
