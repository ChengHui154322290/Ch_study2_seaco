package com.tp.service.pay;

import java.util.List;
import java.util.Map;

import com.tp.common.vo.PaymentConstant;
import com.tp.common.vo.bse.ClearanceChannelsEnum;
import com.tp.dto.common.ResultInfo;
import com.tp.dto.pay.AppPayData;
import com.tp.dto.pay.cbdata.CallbackResultDto;
import com.tp.dto.pay.cbdata.PayCallbackData;
import com.tp.dto.pay.postdata.AliPayRefundPostData;
import com.tp.dto.pay.postdata.PayPostData;
import com.tp.exception.ServiceException;
import com.tp.model.ord.OrderConsignee;
import com.tp.model.ord.SubOrder;
import com.tp.model.pay.CustomsInfo;
import com.tp.model.pay.PaymentInfo;
import com.tp.model.pay.RefundPayinfo;
import com.tp.result.pay.TradeStatusResult;

/**
 * 各个支付平台的统一接口
 * 
 * @author szy
 *
 */
public interface IPayPlatformService {

	PayPostData getPostData(Long paymentInfoId) throws ServiceException;
	/**
	 * 取支付参数信息
	 * 
	 * @param paymentInfoId payment ID
	 * @param forSdk  是否是APP SDK支付
	 * @param userId (操作用户ID)
	 * @return
	 * @throws ServiceException 
	 */
	AppPayData getAppPayData(Long paymentInfoId, boolean forSdk, String userId) throws ServiceException;

	CallbackResultDto callback(Map<String, String> parameterMap, String gateway) throws ServiceException;

	String refundCallback(Map<String, String> params) throws ServiceException;

	String refund(String gateway, Long bizCode) throws ServiceException;
	
	List<RefundPayinfo> refunds(String gateway, List<String> bizCodes) throws ServiceException;

	TradeStatusResult queryPayStatus(PaymentInfo paymentInfoDO);

	TradeStatusResult queryRefundStatus(RefundPayinfo refundPayinfoDO);

	AliPayRefundPostData getAlipayRefundPostdata();
	CallbackResultDto proccessCallback(Map<String, String> parameterMap, String gateway, PayCallbackData callbackData, PaymentInfo paymentInfo) throws ServiceException;
	
	/**
	 * 手机端：根据支付信息构造对应网关支付请求所需要的参数
	 * 
	 * 
	 * @param gateway
	 * @param paymentInfoId
	 * @param forSdk  true=SDK方式，false=WAP方式
	 * @param userId 操作用户的ID
	 * @param params 需要传入的参数集
	 * @return
	 */
	AppPayData getAppPayDataByParams(Long paymentInfoId, boolean forSDK, String userId, String gateway, Map<String, Object> params);
	void operateAfterCallbackSuccess(PaymentInfo paymentInfo);

	/**
	 * 推送报关信息
	 * @param info
	 */
	ResultInfo declareOrder(SubOrder subOrder, OrderConsignee consignee, CustomsInfo customsInfo);

	/**
	 *查询报关信息
	 * @param subOrder
	 * @param clearance
	 * @return
	 */
	ResultInfo declareQuery(SubOrder subOrder, ClearanceChannelsEnum clearance);
	
	//推送海关
	ResultInfo<Boolean> putPaymentInfoToCustoms(PaymentInfo paymentInfo, SubOrder subOrder, OrderConsignee consignee);
	ResultInfo<Boolean> putPaymentInfoToCustoms(PaymentInfo paymentInfo, SubOrder subOrder, OrderConsignee consignee, CustomsInfo customsInfo);
}
