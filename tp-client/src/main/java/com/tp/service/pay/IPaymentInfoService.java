package com.tp.service.pay;

import java.util.Date;
import java.util.List;

import javax.xml.rpc.ServiceException;

import com.tp.common.vo.PaymentConstant.OBJECT_TYPE;
import com.tp.common.vo.PaymentConstant.PAY_ACTION_NAME;
import com.tp.dto.pay.PayPaymentSimpleDTO;
import com.tp.model.pay.PaymentInfo;
import com.tp.service.IBaseService;
/**
  * @author szy
  * 支付信息表接口
  */
public interface IPaymentInfoService extends IBaseService<PaymentInfo>{


	String refund(String gatewayCode, Long refundNo);
	/**
	 * 根据支付数据保存支付信息
	 * @param payPaymentSimpleDTO
	 * @return
	 * @throws ServiceException
	 */
	PaymentInfo insertPaymentInfo(PayPaymentSimpleDTO payPaymentSimpleDTO) throws ServiceException;
	
	/**
	 * 根据支付数据保存支付信息
	 * @param payPaymentSimpleDTOs
	 * @return
	 * @throws ServiceException
	 */
	List<PaymentInfo> insertPaymentInfoList(List<PayPaymentSimpleDTO> payPaymentSimpleDTOs);
	
	/**
	 * 根据单据编号、单据类型查询支付信息 
	 * @param bizCode
	 * @param bizType
	 * @return
	 */
	PaymentInfo queryPaymentInfoByBizCodeAndBizType(Long bizCode,Integer bizType);
	
	/**
	 * 根据单据编号查询支付信息 
	 * @param bizCode
	 * @return
	 */
	PaymentInfo queryPaymentInfoByBizCode(Long bizCode);
	/**
	 * @param paymentInfoObj
	 * @param actionName
	 * @param objectType
	 * @return
	 * @throws ServiceException
	 */
	int update(PaymentInfo paymentInfoObj, PAY_ACTION_NAME actionName, OBJECT_TYPE objectType) throws ServiceException;
	
	/**
	 * 根据单据编号及类型取消支付,返回0，则取消失败
	 * @param bizId
	 * @param bizType
	 * @return
	 * @throws PaymentServiceException
	 */
	int updateCancelPayment(PaymentInfo paymentInfoObj);
	
	
	int updatePaymentByPayed(PaymentInfo paymentInfoObj);

	/**
	 * 查找最近的支付信息
	 */
	List<PaymentInfo> selectRecentPaymentInfos(Date createdAfter, Integer notEqualStatus);
}
