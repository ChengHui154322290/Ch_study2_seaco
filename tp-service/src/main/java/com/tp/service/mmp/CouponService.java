package com.tp.service.mmp;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.tp.common.dao.BaseDao;
import com.tp.common.vo.Constant;
import com.tp.common.vo.PageInfo;
import com.tp.common.vo.Constant.SPLIT_SIGN;
import com.tp.common.vo.DAOConstant.MYBATIS_SPECIAL_STRING;
import com.tp.common.vo.mmp.CouponAuditConstant;
import com.tp.common.vo.mmp.ProcessingErrorMessage;
import com.tp.dao.mmp.CouponAuditDao;
import com.tp.dao.mmp.CouponDao;
import com.tp.dto.common.ResultInfo;
import com.tp.dto.mmp.CouponDto;
import com.tp.dto.mmp.enums.CouponStatus;
import com.tp.exception.ServiceException;
import com.tp.model.mmp.Coupon;
import com.tp.model.mmp.CouponAudit;
import com.tp.model.mmp.CouponRange;
import com.tp.redis.util.JedisDBUtil;
import com.tp.service.BaseService;
import com.tp.service.mmp.ICouponRangeService;
import com.tp.service.mmp.ICouponService;
import com.tp.util.StringUtil;

@Service
public class CouponService extends BaseService<Coupon> implements ICouponService {

	@Autowired
	private CouponDao couponDao;

	@Autowired
	private CouponAuditDao couponAuditDao;
	
	@Autowired
	private ICouponRangeService couponRangeService;
	@Autowired
	private JedisDBUtil jedisDBUtil;
	
	@Override
	public BaseDao<Coupon> getDao() {
		return couponDao;
	}



	@Override
	public CouponDto getCouponInfosById(Long id) throws ServiceException {
		try {
			return couponDao.getCouponInfosById(id);
		} catch (ServiceException e) {
			logger.error("error",e);
		}
		return new CouponDto();
	}


	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public ResultInfo refuseCoupon(Long couponId, Long userId, String userName, String remark)  throws ServiceException{
		if (null == couponId || 0 == couponId) {
			throw new ServiceException(
					ProcessingErrorMessage.VALID_COUPON_ID);
		}
		try {
			// 更新状态
			Coupon Coupon = new Coupon();
			Coupon.setId(couponId);
			Coupon.setStatus(CouponStatus.REFUSED.ordinal() );
			//Coupon.setModifyTime(new Date());
			couponDao.updateNotNullById(Coupon);

			// 新增执行动作记录
			this.saveAuditLog(couponId, CouponStatus.REFUSED.ordinal(), userId, userName, remark);
		} catch (ServiceException e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(
					ProcessingErrorMessage.REFUSE_COUPON_FAILD);
		}
		return new ResultInfo();
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public ResultInfo approveCoupon(Long couponId, Long userId, String userName)  throws ServiceException{
		if (null == couponId || 0 == couponId) {
			throw new ServiceException(
					ProcessingErrorMessage.VALID_COUPON_ID);
		}
		try {
			// 更新状态
			Coupon Coupon = new Coupon();
			Coupon.setId(couponId);
			Coupon.setStatus(CouponStatus.PASSED.ordinal() );
			//Coupon.setModifyTime(new Date());
			couponDao.updateNotNullById(Coupon);

			// 新增执行动作记录
			this.saveAuditLog(couponId, CouponStatus.PASSED.ordinal(), userId, userName, null);
		} catch (ServiceException e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(
					ProcessingErrorMessage.PASSED_COUPON_FAILD);
		}
		return new ResultInfo();
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public ResultInfo cancelCoupon(Long couponId, Long userId, String userName)  throws ServiceException{
		if (null == couponId || 0 == couponId) {
			throw new ServiceException(
					ProcessingErrorMessage.VALID_COUPON_ID);
		}
		try {
			// 更新状态
			Coupon Coupon = new Coupon();
			Coupon.setId(couponId);
			Coupon.setStatus(CouponStatus.CANCELED.ordinal() );
			//Coupon.setModifyTime(new Date());
			couponDao.updateNotNullById(Coupon);

			// 新增执行动作记录
			this.saveAuditLog(couponId, CouponStatus.CANCELED.ordinal(), userId, userName, null);
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
	private void saveAuditLog(Long couponId, Integer status, Long userId, String userName, String remark) throws ServiceException {

		CouponAudit auditLog = new CouponAudit();
		auditLog.setCouponId(couponId);
		auditLog.setAuditId(userId);
		auditLog.setAuditName(userName);
		auditLog.setCreateTime(new Date());
		auditLog.setCreateId(userId);
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
		couponAuditDao.insert(auditLog);
	}

	@Override
	public List<CouponAudit> queryCouponAudit(Long id) throws ServiceException {
		CouponAudit auditLog = new CouponAudit();
		auditLog.setCouponId(id);
		try {
			return couponAuditDao.queryByObject(auditLog);
		} catch (ServiceException e) {
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public PageInfo<Coupon> queryPageByObjectWithLike(Coupon coupon, PageInfo<Coupon> pageInfo) {
		Long total = couponDao.queryByObjectWithLikeCount(coupon);
		coupon.setStartPage(pageInfo.getPage());
		coupon.setPageSize(pageInfo.getSize());
		List<Coupon> list = couponDao.queryByObjectWithLike(coupon);
		pageInfo.setRows(list);
		pageInfo.setRecords(total.intValue());
		return pageInfo;
	}
	
	@Override
	public List<Coupon> queryCouponByCouponIdList(List<Long> idList) {
		if (CollectionUtils.isEmpty(idList)) {
			return new ArrayList<Coupon>();
		}
		Map<String, Object> params = new HashMap<String, Object>();
		params.put(MYBATIS_SPECIAL_STRING.COLUMNS.name(), " id in (" + StringUtil.join(idList, SPLIT_SIGN.COMMA) + ")");
		List<Coupon> couponList = couponDao.queryByParam(params);
		if (CollectionUtils.isNotEmpty(couponList)) {
			params.put(MYBATIS_SPECIAL_STRING.COLUMNS.name(),
					" coupon_id in (" + StringUtil.join(idList, SPLIT_SIGN.COMMA) + ")");
			List<CouponRange> couponRangeList = couponRangeService.queryByParam(params);
			if (CollectionUtils.isNotEmpty(couponRangeList)) {
				for (Coupon coupon : couponList) {
					for (CouponRange couponRange : couponRangeList) {
						if (coupon.getId().equals(couponRange.getCouponId())) {
							coupon.getCouponRangeList().add(couponRange);
						}
					}
				}
			}
		}
		return couponList;
	}
	
	@Override
	public String queryOfflineCouponCode(String couponCodeKey) {
		String code = (String) jedisDBUtil.getDB("offlineCouponCode:"+couponCodeKey);
		if(StringUtil.isBlank(code)){
			code = couponCodeKey;
			if(couponCodeKey.length()>4){
				code = couponCodeKey.substring(couponCodeKey.length()-4);
			}
			jedisDBUtil.setDB("offlineCouponCode:"+couponCodeKey,code, 60*60*24*30);
		}
		//处理二维码无法正常显示
		
		return code;
	}



	@Override
	public Boolean activeCoupon(Coupon coupon) {
		couponDao.updateNotNullById(coupon);
		// 新增执行动作记录
		saveAuditLog(coupon.getId(), CouponStatus.PASSED.ordinal(), coupon.getUpdateUserId(), coupon.getOperator(), Constant.ENABLED.YES.equals(coupon.getActiveStatus())?"激活实体卡":"准备制作实体卡");
		return true;
	}
}
