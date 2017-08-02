package com.tp.service.pay;

import com.tp.dto.pay.AppPayData;
import com.tp.exception.ServiceException;
import com.tp.query.pay.AppPaymentCallDto;

/**
 * 提供给手机端的支付接口
 *
 */
public interface IAppPaymentService {

	/**
	 * 提供给APP-SDK的支付信息
	 * @param paymentCallDto
	 * @return
	 * @throws ServiceException 
	 */
	AppPayData getAppData(AppPaymentCallDto paymentCallDto) throws ServiceException;
}
