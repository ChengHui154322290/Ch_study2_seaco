package com.tp.service.pay;

import java.util.List;
import java.util.Map;

import com.tp.dto.common.ResultInfo;
import com.tp.dto.pay.AppPayData;
import com.tp.dto.pay.cbdata.CallbackResultDto;
import com.tp.dto.pay.postdata.AliPayRefundPostData;
import com.tp.dto.pay.postdata.PayPostData;
import com.tp.dto.pay.query.TradeStatusQuery;
import com.tp.dto.pay.servicestatus.ServiceStatusDto;
import com.tp.exception.ServiceException;
import com.tp.model.ord.OrderConsignee;
import com.tp.model.ord.SubOrder;
import com.tp.model.pay.PaymentInfo;
import com.tp.model.pay.RefundPayinfo;
import com.tp.result.pay.TradeStatusResult;

public interface IPaymentService {
	String REQUEST_CONTENT_NAME = "XG_REQUEST_CONTENT";

	/**
	 * 付款前检查支付情况，顺带返回支付网关的信息
	 * 
	 * @param paymentId
	 * @return
	 */
	PaymentInfo checkPayment(Long paymentId);

	/**
	 * ＰＣ端：根据支付信息构造对应网关支付请求所需要的参数
	 * 
	 * @param gateway
	 * @param paymentInfoId
	 * @return
	 * @throws ServiceException 
	 */
	PayPostData getPostData(String gateway, Long paymentInfoId) throws ServiceException;

	/**
	 * 手机端：根据支付信息构造对应网关支付请求所需要的参数
	 * 
	 * @param gateway
	 * @param paymentInfoId
	 * @param forSdk
	 *            true=SDK方式，false=WAP方式
	 * @param userId 操作用户的ID
	 * @return
	 * @throws ServiceException 
	 */
	AppPayData getAppPayData(String gateway, Long paymentInfoId, boolean forSdk, String userId) throws ServiceException;

	/**
	 * 处理支付平台的支付回调请求
	 * 
	 * @param gateway
	 * @param parameterMap
	 * @return
	 * @throws ServiceException 
	 */
	CallbackResultDto callback(String gateway, Map<String, String> parameterMap) throws ServiceException;

	/**
	 * 查询订单是在支付平台的支付状态及结果
	 * 
	 * @param query
	 * @return
	 * @throws ServiceException
	 */
	TradeStatusResult queryTradeStatus(TradeStatusQuery query);

	/**
	 * 根据单据编号及类型取消支付, 若已支付则返回0－取消失败
	 * 
	 * @param bizId
	 * @param bizType
	 * @return
	 * @throws ServiceException 
	 */
	Integer updateCancelPaymentInfo(Long bizCode, Integer bizType,
			String cancelIp, String cancelUser);

	/**
	 * 处理支付平台的退款回调请求
	 * 
	 * @param gateway
	 * @param params
	 * @return
	 * @throws ServiceException
	 */
	String refundCallback(String gateway, Map<String, String> params) throws ServiceException;

	/**
	 * 根据支付信息和网关退款， 返回退款信息，成功返回null.
	 * 
	 * @param gateway
	 *            网关code
	 * @param refundNo
	 *            支付网关的交易号
	 * @throws ServiceException
	 */
	String refund(String gateway, Long refundNo) throws ServiceException;

	/**
	 * 根据支付ID获取信息
	 * 
	 * @param paymentId
	 * @return
	 */
	PaymentInfo selectById(Long paymentId);

	/**
	 * 根据支付ID获取信息
	 * 
	 * @param paymentId
	 * @return
	 */
	List<PaymentInfo> selectByIds(List<Long> paymentIds);

	/**
	 * 获取近期待确认的退款
	 * 
	 * @return
	 */
	List<RefundPayinfo> selectRecentRefund2Confirm();

	/**
	 * 取出准备付款的支付信息
	 * 
	 * @param paymentId
	 * @param gatewayId
	 *            　如果不为空，若和原来的支付方式不一样，会修改支付方式
	 * @return
	 */
	PaymentInfo try2Pay(Long paymentId, Long gatewayId);

	AliPayRefundPostData getAlipayRefundPostdata();

	/**
	 * 检查给定支付网关的退款状态 注：微信没有退款回调，需要手动触发
	 * 
	 * @param gatewayId
	 *            为null里检查所有
	 */
	void checkRefundStatus(Long gatewayId);

	/**
	 * 检查所有支付网关的退款状态
	 */
	void checkRefundStatus();

	/**
	 * 主动检查支付中的订单是否已经成功了
	 */
	void checkPaymentStatus();

	/**
	 * 运维使用：检查服务运行状态
	 * 
	 * @return
	 */
	ServiceStatusDto checkServiceStatus();

	String getPropertiesValue(String key);

	PaymentInfo queryPaymentInfoByBizCode(String bizCode);

	PaymentInfo checkPaymentByGateway(Long paymentId, String gateway);
	/**
	 * 手机端：根据支付信息构造对应网关支付请求所需要的参数
	 * 
	 * @param gateway
	 * @param paymentInfoId
	 * @param forSdk true=SDK方式，false=WAP方式
	 * @param userId 操作用户的ID
	 * @param params 需要传入的参数集
	 * @return
	 */
	AppPayData getAppPayDataByParams(String gateway, Long paymentInfoId, boolean b, String userId, Map<String, Object> params);
	
	/**
	 * 支付单报关 
	 */
	ResultInfo<PaymentInfo> putPaymentInfoToCustoms(SubOrder subOrder, OrderConsignee consignee);
}
