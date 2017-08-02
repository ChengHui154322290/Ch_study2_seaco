package com.tp.service.pay;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.rpc.ServiceException;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.dao.BaseDao;
import com.tp.common.vo.MqMessageConstant;
import com.tp.common.vo.PaymentConstant;
import com.tp.common.vo.OrderConstant.OrderType;
import com.tp.dao.pay.PaymentGatewayDao;
import com.tp.dao.pay.PaymentInfoDao;
import com.tp.dao.pay.PaymentLogDao;
import com.tp.dto.pay.PayPaymentSimpleDTO;
import com.tp.model.pay.PaymentGateway;
import com.tp.model.pay.PaymentInfo;
import com.tp.model.pay.PaymentLog;
import com.tp.mq.RabbitMqProducer;
import com.tp.mq.exception.MqClientException;
import com.tp.service.BaseService;
import com.tp.service.ord.IOrderRedeemItemService;
import com.tp.service.pay.IPaymentInfoService;
import com.tp.util.DateUtil;

@Service
public class PaymentInfoService extends BaseService<PaymentInfo> implements IPaymentInfoService {

	@Autowired
	private PaymentInfoDao paymentInfoDao;

	@Autowired
	private PaymentLogDao paymentLogDao;
	
	@Autowired
	private PaymentGatewayDao paymentGatewayDao;

	@Autowired
	private RabbitMqProducer rabbitMqProducer;
	@Autowired
	IOrderRedeemItemService  orderRedeemItemService;
	@Override
	public BaseDao<PaymentInfo> getDao() {
		return paymentInfoDao;
	}
	
	@Override
	public PaymentInfo insertPaymentInfo(PayPaymentSimpleDTO dto) throws ServiceException {
		PaymentInfo paymentInfoObj = new PaymentInfo();
		Date currentDate = new Date();
		paymentInfoObj.setBizCode(dto.getBizCode());
		paymentInfoObj.setBizType(dto.getBizType());
		paymentInfoObj.setCreateTime(new Date());
		paymentInfoObj.setCreateUser(String.valueOf(dto.getUserId()));
		paymentInfoObj.setUpdateTime(new Date());
		paymentInfoObj.setUpdateUser(String.valueOf(dto.getUserId()));
		paymentInfoObj.setBizCreateTime(dto.getBizCreateTime());
		paymentInfoObj.setAmount(dto.getAmount());
		paymentInfoObj.setPaymentTradeNo(String.valueOf(dto.getBizCode()));
		paymentInfoObj.setSerial(dto.getBizCode().toString());
		paymentInfoObj.setActionIp(dto.getActionIP());
		paymentInfoObj.setPaymentType(PaymentConstant.PAYMENT_TYPE.ORDER.code);
		paymentInfoObj.setOrderType(dto.getOrderType());
		paymentInfoObj.setChannelId(dto.getChannelId());
		paymentInfoObj.setTaxFee(dto.getTaxFee());
		paymentInfoObj.setFreight(dto.getFreight());
		paymentInfoObj.setRealName(dto.getRealName());
		paymentInfoObj.setIdentityType(dto.getIdentityType());
		paymentInfoObj.setIdentityCode(dto.getIdentityCode());
		paymentInfoObj.setGatewayId(dto.getGatewayId());
		Long gatewayId = Long.valueOf(dto.getGatewayId());
		try {
			PaymentGateway gw = paymentGatewayDao.queryById(gatewayId);
			paymentInfoObj.setGatewayCode(gw.getGatewayCode());
			paymentInfoObj.setGatewayName(gw.getGatewayName());
		} catch (Exception e) {
			logger.error("", e);
		}
		insert(paymentInfoObj);
		return paymentInfoObj;
	}

	@Override
	public List<PaymentInfo> insertPaymentInfoList(List<PayPaymentSimpleDTO> payPaymentSimpleDTOs) {
		List<PaymentInfo> result = new ArrayList<PaymentInfo>();
		Long parentPaymentId = null;
		for (PayPaymentSimpleDTO dto : payPaymentSimpleDTOs) {
			PaymentInfo paymentInfoDO = new PaymentInfo();
			Date currentDate = new Date();
			paymentInfoDO.setBizCode(dto.getBizCode());
			paymentInfoDO.setBizType(dto.getBizType());
			paymentInfoDO.setCreateTime(currentDate);
			paymentInfoDO.setCreateUser(String.valueOf(dto.getUserId()));
			paymentInfoDO.setUpdateTime(currentDate);
			paymentInfoDO.setUpdateUser(String.valueOf(dto.getUserId()));
			paymentInfoDO.setBizCreateTime(dto.getBizCreateTime());
			paymentInfoDO.setAmount(dto.getAmount());
			paymentInfoDO.setPaymentTradeNo(String.valueOf(dto.getBizCode()));
			paymentInfoDO.setSerial(paymentInfoDO.getPaymentTradeNo());
			paymentInfoDO.setActionIp(dto.getActionIP());
			paymentInfoDO.setPaymentType(PaymentConstant.PAYMENT_TYPE.ORDER.code);
			paymentInfoDO.setGatewayId(dto.getGatewayId() != null ? Long.valueOf(dto.getGatewayId()) : 0L);
			if(dto.getBizType().intValue() == PaymentConstant.BIZ_TYPE.MERGEORDER.code.intValue()){
				paymentInfoDO.setOrderType(-1L);
				paymentInfoDO.setChannelId(-1L);
			}
			else{
				paymentInfoDO.setOrderType(dto.getOrderType());
				paymentInfoDO.setChannelId(dto.getChannelId());
			}
			paymentInfoDO.setTaxFee(dto.getTaxFee());
			paymentInfoDO.setFreight(dto.getFreight());
			paymentInfoDO.setRealName(dto.getRealName());
			paymentInfoDO.setIdentityType(dto.getIdentityType());
			paymentInfoDO.setIdentityCode(dto.getIdentityCode());
			if(parentPaymentId != null){
				paymentInfoDO.setPrtPaymentId(parentPaymentId);
			}
			else{
				paymentInfoDO.setPrtPaymentId(0L);
			}
			PaymentInfo pay = insert(paymentInfoDO);
			
			if(parentPaymentId == null) {
				result.add(paymentInfoDO);
			}
			
			if(paymentInfoDO.getBizType().intValue() == PaymentConstant.BIZ_TYPE.MERGEORDER.code.intValue()){
				parentPaymentId = pay.getPaymentId();
			}
			
		}
		return result;
	}

	@Override
	public PaymentInfo insert(PaymentInfo paymentInfoObj) {
			if (null == paymentInfoObj.getCanceled())
				paymentInfoObj.setCanceled(0);
			paymentInfoObj.setStatus(PaymentConstant.PAYMENT_STATUS.NO_PAY.code);
			if (paymentInfoObj.getAmount() <= 0) {
				// 小于０的支付，需要算成功支付
				paymentInfoObj.setStatus(PaymentConstant.PAYMENT_STATUS.PAYED.code);
				paymentInfoObj.setCallbackInfo("no need to pay");
				paymentInfoObj.setCallbackTime(new Date());
				
				
				//团购券逻辑 ---start
                logger.info("生成的兑换码信息开始------"+"OrderType.BUY_COUPONS.code:"+OrderType.BUY_COUPONS.code+"----paymentInfoObj.getOrderType().intValue():"+paymentInfoObj.getOrderType().intValue());
                logger.info("OrderType.BUY_COUPONS.code==paymentInfoObj.getOrderType().intValue()-----:"+(OrderType.BUY_COUPONS.code==paymentInfoObj.getOrderType().intValue()));
        		if(OrderType.BUY_COUPONS.code==paymentInfoObj.getOrderType().intValue()){//团购券入口
        			Long orderCode=paymentInfoObj.getBizCode();//订单编号
        			if(PaymentConstant.PAYMENT_STATUS.PAYED.code.equals(paymentInfoObj.getStatus())){//支付成功
        				String reddemCodes=orderRedeemItemService.generateAndSaveRedeemInfo( orderCode);
        				logger.info("生成的兑换码信息："+reddemCodes);
        			}
        		}
        		  //团购券逻辑 ---end
			}
			paymentInfoDao.insert(paymentInfoObj);

			// 记录创建支付信息
			Long paymentId = paymentInfoObj.getPaymentId();
			PaymentLog paymentLogDO = new PaymentLog(paymentId, PaymentConstant.OBJECT_TYPE.PAYMENT.code,
					PaymentConstant.PAY_ACTION_NAME.CREATE.cnName,
					new StringBuffer("根据").append(PaymentConstant.BIZ_TYPE.getCnName(paymentInfoObj.getBizType()))
							.append(paymentInfoObj.getBizCode()).append("创建支付信息").toString(),
					paymentInfoObj.getActionIp(), new Date(), paymentInfoObj.getCreateUser());
			paymentLogDO.setPartTable(DateUtil.formatDate(paymentInfoObj.getCreateTime(), "yy"));
			paymentLogDao.insert(paymentLogDO);

			if (paymentInfoObj.getAmount() <= 0 && !PaymentConstant.BIZ_TYPE.MERGEORDER.code.equals(paymentInfoObj.getBizType())) {
				try {
					logger.info("小于０的，直接通知成功：订单支付回调开始：orderId={}", paymentInfoObj.getBizCode());
					if(PaymentConstant.BIZ_TYPE.DSS.code.equals(paymentInfoObj.getBizType())){
						rabbitMqProducer.sendP2PMessage(MqMessageConstant.REGISTER_PROMOTER_SUCCESS, paymentInfoObj);
					}else{
						rabbitMqProducer.sendP2PMessage(PaymentConstant.PAYMENT_INFO_STATUS_QUEUE, paymentInfoObj);
					}
					logger.info("订单支付回调完成：orderId={}", paymentInfoObj.getBizCode());
				} catch (MqClientException e) {
					logger.error("payment-{} of gateway {} fail to send mq message!", paymentId,
							paymentInfoObj.getGatewayId());
				}
			}
			return paymentInfoObj;
	}

	@Override
	public int updateById(PaymentInfo paymentInfoObj) {
		return update(paymentInfoObj, PaymentConstant.PAY_ACTION_NAME.UPDATE, PaymentConstant.OBJECT_TYPE.PAYMENT);
	}

	@Override
	public int update(PaymentInfo paymentInfoObj, PaymentConstant.PAY_ACTION_NAME actionName,
			PaymentConstant.OBJECT_TYPE objectType) {
		paymentInfoObj.setUpdateTime(new Date());
		int row = paymentInfoDao.updateNotNullById(paymentInfoObj);

		PaymentLog paymentLogDO = new PaymentLog(paymentInfoObj.getPaymentId(), objectType.code, actionName.cnName,
				new StringBuffer("根据").append(PaymentConstant.BIZ_TYPE.getCnName(paymentInfoObj.getBizType()))
						.append(paymentInfoObj.getBizCode()).append(actionName.cnName).toString(),
				paymentInfoObj.getActionIp(), new Date(), paymentInfoObj.getCreateUser());
		paymentLogDO.setPartTable(DateUtil.formatDate(new Date(), "yy"));
		paymentLogDao.insert(paymentLogDO);
		return row;
	}

	public PaymentInfo queryPaymentInfoByBizCodeAndBizType(Long bizCode, Integer bizType) {
		Map<String, Object> params = new HashMap<>();
		params.put("bizCode", bizCode);
		params.put("bizType", bizType);
		List<PaymentInfo> infoList = paymentInfoDao.queryPageByParamNotEmpty(params);
		return CollectionUtils.isNotEmpty(infoList) ? infoList.get(0):null;
	}

	@Override
	public PaymentInfo queryPaymentInfoByBizCode(Long bizCode) {
		return queryPaymentInfoByBizCodeAndBizType(bizCode, null);
	}

	@Override
	public String refund(String gatewayCode, Long refundNo) {
		return null;
	}
	
	@Override
	public int updateCancelPayment(PaymentInfo paymentInfoObj){
		return paymentInfoDao.updateCancelPayment(paymentInfoObj);
	}
	
	@Override
	public int updatePaymentByPayed(PaymentInfo paymentInfoObj){
		return paymentInfoDao.updatePaymentByPayed(paymentInfoObj);
	}
	
	@Override
	public List<PaymentInfo> selectRecentPaymentInfos(Date updatedAfter, Integer notEqualStatus){
		return paymentInfoDao.selectRecentPaymentInfos(updatedAfter, notEqualStatus);
	}
}
