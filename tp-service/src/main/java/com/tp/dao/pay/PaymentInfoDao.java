package com.tp.dao.pay;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.tp.common.dao.BaseDao;
import com.tp.model.pay.PaymentInfo;

public interface PaymentInfoDao extends BaseDao<PaymentInfo> {
	
	/**
	 * 根据单据编号及类型取消支付,返回0，则取消失败
	 * @param bizId
	 * @param bizType
	 * @return
	 * @throws PaymentServiceException
	 */
	Integer updateCancelPayment(PaymentInfo paymentInfoObj);
	
	
	Integer updatePaymentByPayed(PaymentInfo paymentInfoObj);

	/**
	 * 查找最近的支付信息
	 */
	List<PaymentInfo> selectRecentPaymentInfos(@Param("updateTime")Date updateAfter, @Param("status")Integer notEqualStatus);
}
