package com.tp.service.pay;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.tp.common.vo.PaymentConstant;
import com.tp.dao.pay.PaymentInfoDao;
import com.tp.dao.pay.PaymentLogDao;
import com.tp.dto.pay.AppPayData;
import com.tp.exception.ServiceException;
import com.tp.model.pay.PaymentGateway;
import com.tp.model.pay.PaymentInfo;
import com.tp.model.pay.PaymentLog;
import com.tp.query.pay.AppPaymentCallDto;
import com.tp.service.pay.IAppPaymentService;
import com.tp.service.pay.IPaymentGatewayService;
import com.tp.service.pay.IPaymentService;
import com.tp.util.DateUtil;


/**
 * 专给手机端提供的，只做一个业务调用；具体实现参见<{@link IPaymentService}
 *
 */
@Service(value="appPaymentService")
public class AppPaymentService implements IAppPaymentService {
	Logger logger = LoggerFactory.getLogger(AppPaymentService.class);

	@Autowired
	IPaymentService paymentService;
	@Autowired
	PaymentInfoDao paymentInfoDao;
	@Autowired
	IPaymentGatewayService paymentGatewayService;
	@Autowired
	PaymentLogDao paymentLogDao;

	@Value("${weixinExternalPay.enabled}")
	private String weixinExternalPayEnabled;
	
	public AppPayData getAppData(AppPaymentCallDto paymentCallDto) throws ServiceException {

		if( weixinExternalPayEnabled !=null && weixinExternalPayEnabled.equals("true")&& paymentCallDto.getGateway()!=null && paymentCallDto.getGateway().equals(PaymentConstant.GATEWAY_TYPE.WEIXIN.code)){
			//logger.info("USE_WEIXIN_EXTERNAL_PAY_REPLACE_WEIXIN_PAY");
			//paymentCallDto.setGateway(PaymentConstant.GATEWAY_TYPE.WEIXIN_EXTERNAL.code);
		}

		if(checkPayment(paymentCallDto.getPaymentId())) {
			logger.error("支付单号{}已支付成功", paymentCallDto.getPaymentId());
			throw new ServiceException("支付单号"+paymentCallDto.getPaymentId()+"已支付成功");
		}
		if(!checkGateway(paymentCallDto.getPaymentId(), paymentCallDto.getGateway())){
			logger.error("支付单号{}无法使用此支付方式{}", paymentCallDto.getPaymentId(),paymentCallDto.getGateway());
			throw new ServiceException("支付单号"+paymentCallDto.getPaymentId()+"无法使用此支付方式"+paymentCallDto.getGateway());
		}
		logger.info("获取App支付参数集：{}", paymentCallDto);
		if(paymentCallDto.isSdk()) {
			return getAppSdkDataByUser(paymentCallDto);
		}
		else{
			return getAppWapDataByUser(paymentCallDto);
		}
	}
	

	private AppPayData getAppSdkDataByUser(AppPaymentCallDto paymentCallDto) throws ServiceException {
		updatePaymentInfo(paymentCallDto.getPaymentId(), paymentCallDto.getGateway());
		String gateway = paymentCallDto.getGateway();
		if("union".equalsIgnoreCase(gateway))
			gateway += "App";
		
		AppPayData data = paymentService.getAppPayData(gateway, paymentCallDto.getPaymentId(), true, paymentCallDto.getUserId());
		return data;
	}

	private AppPayData getAppWapDataByUser(AppPaymentCallDto paymentCallDto) throws ServiceException {
		updatePaymentInfo(paymentCallDto.getPaymentId(), paymentCallDto.getGateway());
		String gateway = paymentCallDto.getGateway();
		if("union".equalsIgnoreCase(gateway))
			gateway += "App";
		if("weixin".equals(gateway) || 
				PaymentConstant.GATEWAY_TYPE.WEIXIN_EXTERNAL.code.equals(gateway) || 
				(paymentCallDto.getParams() != null && paymentCallDto.getParams().get("channelCode") != null) || ( paymentCallDto.getParams()!=null && paymentCallDto.getParams().get("sysSource")!=null)){
			return paymentService.getAppPayDataByParams(gateway, paymentCallDto.getPaymentId(), false, null, paymentCallDto.getParams());
		}else{
			return paymentService.getAppPayData(gateway, paymentCallDto.getPaymentId(), false, paymentCallDto.getUserId());
		}
	}
	
	public AppPayData getAppWapDataByParam(String gateway, Long paymentInfoId, String plat, Map<String, Object> params) {
		if(!checkPayment(paymentInfoId)) {
			logger.error("支付单号{}已支付成功", paymentInfoId);
			throw new ServiceException("支付单号"+paymentInfoId+"已支付成功");
		}
		if(checkGateway(paymentInfoId, gateway)){
			logger.error("支付单号{}无法使用此支付方式{}", paymentInfoId,gateway);
			throw new ServiceException("支付单号"+paymentInfoId+"无法使用此支付方式"+gateway);
		}
		updatePaymentInfo(paymentInfoId, gateway);
		if("union".equalsIgnoreCase(gateway))
			gateway += "App";
		logger.info("获取支付参数集：gateway[{}],paymentId[{}],plat[{}], params[{}]", gateway, paymentInfoId,plat, params);
		AppPayData data = paymentService.getAppPayDataByParams(gateway, paymentInfoId, false, null, params);
		return data;
	}
	
	private boolean checkGateway(Long paymentId, String gateway){
//		PaymentInfo paymentInfoDO = paymentService.checkPaymentByGateway(paymentId, gateway);
		return true;//paymentInfoDO.getGatewayCode() != null;
	}
	private boolean checkPayment(Long paymentId) {
		PaymentInfo oldPaymentInfoObj = paymentService.selectById(paymentId);
		if(PaymentConstant.PAYMENT_STATUS.PAYED.code.equals(oldPaymentInfoObj.getStatus()) && oldPaymentInfoObj.getAmount() > 0) {
			return true;
		}
		return false;
	}
	
	private void updatePaymentInfo(Long paymentInfoId, String gateway) throws ServiceException{
		PaymentInfo oldPaymentInfoObj = paymentService.selectById(paymentInfoId);
		Map<String, Object> params = new HashMap<>();
		params.put("gatewayCode", gateway);
		List<PaymentGateway> list = paymentGatewayService.queryByParamNotEmpty(params);
		PaymentGateway gatewayObj = null;
		if(CollectionUtils.isNotEmpty(list)){
			gatewayObj = list.get(0);
		}
		else{
			logger.error("gateway code[{}]找不到对应的数据", gateway);
			throw new ServiceException("gateway code["+ gateway +"]找不到对应的数据");
		}
		if(!gatewayObj.getGatewayId().equals(oldPaymentInfoObj.getGatewayId())){
			Date now = new Date();
			PaymentInfo toBeUpdated = new PaymentInfo();
			toBeUpdated.setPaymentId(paymentInfoId);
			toBeUpdated.setGatewayId(gatewayObj.getGatewayId());
			toBeUpdated.setStatus(PaymentConstant.PAYMENT_STATUS.PAYING.code);
			toBeUpdated.setUpdateTime(new Date());
			try {
				paymentInfoDao.updateNotNullById(toBeUpdated);
			} catch (Exception e) {
				logger.error("更新支付方式失败", e);
				throw new ServiceException(e);
			}
			
			PaymentLog paymentLogDO = new PaymentLog(
					paymentInfoId, PaymentConstant.OBJECT_TYPE.PAYMENT.code,
					"修改支付方式", "修改" + PaymentConstant.BIZ_TYPE.getCnName(oldPaymentInfoObj.getBizType())
							+ "编号" + oldPaymentInfoObj.getBizCode() + " 支付方式："
							+ oldPaymentInfoObj.getGatewayId() + " => "
							+ gatewayObj.getGatewayId(), "", new Date(),
					gatewayObj.getGatewayCode());
			paymentLogDO.setPartTable(DateUtil.format(now, "yy"));
			try {
				paymentLogDao.insert(paymentLogDO);
			} catch (Exception e) {
				logger.error("插入log失败", e);
			}
		}
		
	}
	
}
