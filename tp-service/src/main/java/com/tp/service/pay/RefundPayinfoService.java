package com.tp.service.pay;

import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Random;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.dao.BaseDao;
import com.tp.common.vo.Constant;
import com.tp.common.vo.PaymentConstant;
import com.tp.common.vo.DAOConstant.MYBATIS_SPECIAL_STRING;
import com.tp.dao.pay.PaymentInfoDao;
import com.tp.dao.pay.PaymentLogDao;
import com.tp.dao.pay.RefundPayinfoDao;
import com.tp.model.pay.PaymentInfo;
import com.tp.model.pay.PaymentLog;
import com.tp.model.pay.RefundPayinfo;
import com.tp.service.BaseService;
import com.tp.service.pay.IRefundPayinfoService;
import com.tp.util.DateUtil;
import com.tp.util.StringUtil;

@Service
public class RefundPayinfoService extends BaseService<RefundPayinfo> implements IRefundPayinfoService {

	//@Autowired
	Properties paymentConfig;
	@Autowired
	private RefundPayinfoDao refundPayinfoDao;
	@Autowired
	private PaymentInfoDao paymentInfoDao;
	@Autowired
	private PaymentLogDao paymentLogDao;
	
	@Override
	public BaseDao<RefundPayinfo> getDao() {
		return refundPayinfoDao;
	}
	@Override
	public RefundPayinfo insert(RefundPayinfo refundPayinfo) {
		refundPayinfo.setSerial(refundPayinfo.getBizCode()+""+new Random().nextInt(1000));
		refundPayinfo.setNotified(0);
		refundPayinfo.setCreateTime(new Date());
		refundPayinfo.setUpdateTime(new Date());
		
		refundPayinfoDao.insert(refundPayinfo);
		PaymentLog paymentLogDO = new PaymentLog(refundPayinfo.getPaymentId(),PaymentConstant.OBJECT_TYPE.REFUND.code,PaymentConstant.PAY_ACTION_NAME.REFUND_CREATE.cnName,
				new StringBuffer("根据").append(PaymentConstant.BIZ_TYPE.getCnName(refundPayinfo.getBizType())).append(refundPayinfo.getBizCode()).append("创建退款信息").toString(),
				"alipay", new Date(),refundPayinfo.getCreateUser());
		paymentLogDO.setPartTable(DateUtil.formatDate(new Date(), "yy"));
		paymentLogDao.insert(paymentLogDO);
		return refundPayinfo;
	}

	@Override
	public int updateById(RefundPayinfo refundPayinfo){
		
		int row = refundPayinfoDao.updateNotNullById(refundPayinfo);
		PaymentLog paymentLogDO = new PaymentLog(refundPayinfo.getPaymentId(),PaymentConstant.OBJECT_TYPE.REFUND.code,PaymentConstant.PAY_ACTION_NAME.REFUND_UPDATE.cnName,
				new StringBuffer("根据").append(PaymentConstant.BIZ_TYPE.getCnName(refundPayinfo.getBizType())).append(refundPayinfo.getBizCode()).append("更新退款信息").toString(),
				"alipay", new Date(),refundPayinfo.getCreateUser());
		paymentLogDO.setPartTable(DateUtil.formatDate(new Date(), "yy"));
		paymentLogDao.insert(paymentLogDO);
		return row;
	}

	@Override
	public RefundPayinfo selectByRefundNo(Long refundNo) {
		
		RefundPayinfo refundPayinfoObj = new RefundPayinfo();
		refundPayinfoObj.setBizCode(refundNo);
		Map<String, Object> params = new HashMap<>();
		params.put("bizCode", refundNo);
		List<RefundPayinfo> refundPayinfoList = refundPayinfoDao.queryPageByParamNotEmpty(params);
		if(CollectionUtils.isNotEmpty(refundPayinfoList)){
			refundPayinfoObj = refundPayinfoList.get(0);
			PaymentInfo paymentInfo = paymentInfoDao.queryById(refundPayinfoObj.getPaymentId());
			refundPayinfoObj.setPaymentInfo(paymentInfo);
			return refundPayinfoObj;
		}
		return null;
	}

	@Override
	public List<RefundPayinfo> selectByRefundNos(List<Long> refundNos) {
		Map<String, Object> params = new HashMap<>();
		params.put(MYBATIS_SPECIAL_STRING.COLUMNS.name(), " biz_code in(" + StringUtil.join(refundNos, Constant.SPLIT_SIGN.COMMA) + ")");
		return queryByParamNotEmpty(params);
	}
	@Override
	public File getRefundFile(List<Long> refundNos){
		List<RefundPayinfo> refunds = selectByRefundNos(refundNos);
		StringBuilder sb = new StringBuilder();
		for (RefundPayinfo rdo : refunds) {
			PaymentInfo paymentInfoDO = paymentInfoDao.queryById(rdo.getPaymentId());
			sb.append(paymentConfig.get("")+"|" +
						paymentInfoDO.getPaymentTradeNo() + "|" +
						paymentInfoDO.getGatewayTradeNo() + "|" +
						"RMB" + "|" +
						rdo.getAmount() + "|" +
						DateUtil.format(new Date(), DateUtil.LONG_FORMAT) +
						"\n"
			);
		}	
		return null;
	}
	@Override
	public int updateDynamicByUnrefunded(RefundPayinfo refundPayinfo) {
		return refundPayinfoDao.updateDynamicByUnrefunded(refundPayinfo);
	}
}
