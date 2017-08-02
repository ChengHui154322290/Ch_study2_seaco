/**
 * 
 */
package com.tp.service.ord.customs.JKF.callback;

import java.util.Date;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;
import com.tp.common.vo.OrderConstant.ClearanceStatus;
import com.tp.common.vo.OrderConstant.DeclareCustoms;
import com.tp.common.vo.OrderConstant.PutCustomsStatus;
import com.tp.common.vo.OrderConstant.PutCustomsType;
import com.tp.common.vo.customs.JKFConstant.CheckResult;
import com.tp.common.vo.customs.JKFConstant.JKFBusinessType;
import com.tp.common.vo.customs.JKFConstant.JKFFeedbackType;
import com.tp.common.vo.customs.JKFConstant.JKFResultError;
import com.tp.common.vo.ord.LogTypeConstant;
import com.tp.model.ord.CustomsClearanceLog;
import com.tp.model.ord.OrderDeclareReceiptLog;
import com.tp.model.ord.OrderStatusLog;
import com.tp.model.ord.PersonalgoodsDeclareInfo;
import com.tp.model.ord.PersonalgoodsStatusLog;
import com.tp.model.ord.SubOrder;
import com.tp.model.ord.JKF.JkfBaseDO;
import com.tp.model.ord.JKF.JkfCallbackResponse;
import com.tp.model.ord.JKF.JkfReceiptResult;
import com.tp.model.ord.JKF.JkfReceiptResult.JkfResult;
import com.tp.model.ord.JKF.JkfReceiptResult.JkfResultDetail;
import com.tp.service.ord.ICustomsClearanceLogService;
import com.tp.service.ord.IOrderDeclareReceiptLogService;
import com.tp.service.ord.IOrderStatusLogService;
import com.tp.service.ord.IPersonalgoodsDeclareInfoService;
import com.tp.service.ord.IPersonalgoodsStatusLogService;
import com.tp.service.ord.ISubOrderService;
import com.tp.service.ord.customs.JKF.callback.IJKFClearanceCallbackProcessService;
import com.tp.util.StringUtil;

/**
 * @author Administrator
 * 通关平台审单回执
 */
@Service
public class JKFDeclareResultCallbackProcessService implements IJKFClearanceCallbackProcessService{

	private static final Logger logger = LoggerFactory.getLogger(JKFDeclareResultCallbackProcessService.class);
	
	@Autowired
	private ISubOrderService subOrderService;
	
	@Autowired
	private IOrderDeclareReceiptLogService orderDeclareReceiptLogService;
	
	@Autowired
	private IPersonalgoodsDeclareInfoService personalgoodsDeclareInfoService;
	
	@Autowired
	private ICustomsClearanceLogService customsClearanceLogService;
	
	@Autowired
	private IOrderStatusLogService orderStatusLogService;
	
	@Autowired
	private IPersonalgoodsStatusLogService personalgoodsStatusLogService;
	
	
	@Override
	public boolean checkReceiptType(JKFFeedbackType type) {
		if (JKFFeedbackType.CUSTOMS_DECLARE_RESULT_CALLBACK == type){
			return true;
		}
		return false;
	}

	@Override
	@Transactional
	public JkfCallbackResponse processCallback(JkfBaseDO baseDO) throws Exception {
		JkfReceiptResult receiptResult = (JkfReceiptResult)baseDO;
		if (CollectionUtils.isEmpty(receiptResult.getBody().getList())) {
			logger.error("[CUSTOMS_CALLBACK]数据为空");
			return new JkfCallbackResponse(JKFResultError.INVALID_CONTENT);
		}
		for (JkfResult result : receiptResult.getBody().getList()) {
			processSingleResult(result);
		}
		return new JkfCallbackResponse();
	}
	
	private void processSingleResult(JkfResult result) throws Exception{	
		result = preProcessSingleResult(result);
		if (result == null) return;	
		JKFBusinessType businessType = JKFBusinessType.getBusinessTypeByType(result.getBusinessType());
		if (JKFBusinessType.IMPORTORDER == businessType) {
			//订单申报回执
			processOrderDeclCallback(result);
		}else if(JKFBusinessType.PERSONAL_GOODS_DECLAR == businessType){
			//个人物品申报回执
			processPersonalGoodsDeclCallback(result);
		}else {
			logger.error("[CUSTOMS_CALLBACK][businessType={}]未知的业务类型", businessType);
		}
	}	
	/**
	 *	处理订单申报电子口岸回执 
	 * @throws Exception 
	 */
	private void processOrderDeclCallback(JkfResult result) throws Exception{	
		Long orderCode = Long.valueOf(result.getBusinessNo());
		SubOrder subOrder = subOrderService.selectOneByCode(orderCode);
		
		orderDeclareReceiptLogService.insert(createOrderDeclareReceiptLog(result));
		if (false == checkOrderCallbackWithDeclareStatus(subOrder)) return;
		
		boolean auditResult = CheckResult.isSuccess(Integer.valueOf(result.getChkMark()));
		updateSubOrderByAuditResult(auditResult, subOrder, PutCustomsType.ORDER_DECLARE);
		
		String sbMessage = auditResult ? "订单审核通过":"订单审核不通过";
		orderStatusLogService.insert(createOrderStatusLog(subOrder, sbMessage));
		customsClearanceLogService.insert(createCustomsClearanceLog(result, orderCode));
	}

	private boolean checkOrderCallbackWithDeclareStatus(SubOrder subOrder){
		Integer clearanceStatus = subOrder.getClearanceStatus();
		if (ClearanceStatus.hasAudit(clearanceStatus)){
			logger.error("[CUSTOMS_CALLBACK][{}]申报单海关已审核，当前审核结果：{}, 重复反馈", 
					subOrder.getOrderCode(), ClearanceStatus.getClearanceDescByCode(clearanceStatus));
			return false;
		}
		Integer orderDeclareStatus = subOrder.getPutOrderStatus();
		if (false == PutCustomsStatus.isSuccess(orderDeclareStatus)){
			logger.error("[CUSTOMS_CALLBACK][{}]订单不是推送成功状态,回执无效,当前推单状态：{}", 
					subOrder.getOrderCode(), PutCustomsStatus.getCustomsStatusDescByCode(orderDeclareStatus));
			return false;
		}
		return true;
	}
	
	/**
	 *	跨境电商平台处理个人物品申报电子口岸回执 
	 * @throws Exception 
	 */
	private void processPersonalGoodsDeclCallback(JkfResult result) throws Exception{		
		PersonalgoodsDeclareInfo pgInfo = personalgoodsDeclareInfoService.queryPersonalgoodsDeclareInfoByPreEntryNo(result.getBusinessNo());
		//查询相关子订单
		Long orderCode = Long.valueOf(pgInfo.getOrderCode());
		SubOrder subOrder = subOrderService.selectOneByCode(orderCode);
		
		personalgoodsStatusLogService.insert(createPersonalgoodsStatusLog(result, subOrder));
		
		if (false == checkPersonalgoodsCallbackWithDeclareStatus(subOrder)) return;
		
		boolean auditResult = CheckResult.isSuccess(Integer.valueOf(result.getChkMark()));
		updateSubOrderByAuditResult(auditResult, subOrder, PutCustomsType.PERSONALGOODS_DECLARE);
		
		String sbMessage = auditResult ? "清单审核通过":"清单审核不通过";
		orderStatusLogService.insert(createOrderStatusLog(subOrder, sbMessage));	
		customsClearanceLogService.insert(createCustomsClearanceLog(result, orderCode));
	}
	
	private boolean checkPersonalgoodsCallbackWithDeclareStatus(SubOrder subOrder){
		Integer clearanceStatus = subOrder.getClearanceStatus();
		if (ClearanceStatus.hasAudit(clearanceStatus)){
			logger.error("[CUSTOMS_CALLBACK][{}]申报单海关已审核，当前审核结果：{}, 重复反馈", 
					subOrder.getOrderCode(), ClearanceStatus.getClearanceDescByCode(clearanceStatus));
			return false;
		}
		Integer personalgoodsDeclareStatus = subOrder.getPutPersonalgoodsStatus();
		if (false == PutCustomsStatus.isSuccess(personalgoodsDeclareStatus)){
			logger.error("[CUSTOMS_CALLBACK][{}]个人物品申报单不是推送成功状态,回执无效，当前推单状态为：{}",
					subOrder.getOrderCode(), PutCustomsStatus.getCustomsStatusDescByCode(personalgoodsDeclareStatus));
			return false;
		}
		return true;
	}
	
	
	
	private JkfResult preProcessSingleResult(JkfResult result) throws Exception{
		if(false == validateResult(result)) return null;
		return result;
	}
	
	private PersonalgoodsStatusLog createPersonalgoodsStatusLog(JkfResult result, SubOrder subOrder){
		StringBuffer sbBuffer = new StringBuffer();
		for (JkfResultDetail detail : result.getResultList()) {
			sbBuffer.append(detail.getResultInfo());
		}
		Integer declareResult = Integer.parseInt(result.getChkMark());
		PersonalgoodsStatusLog personalgoodsStatusLog = new PersonalgoodsStatusLog();
		personalgoodsStatusLog.setCustomsDeclareNo(result.getBusinessNo());
		personalgoodsStatusLog.setOrderCode(subOrder.getOrderCode());
		personalgoodsStatusLog.setDeclareNo(subOrder.getOrderCode().toString());
		personalgoodsStatusLog.setCustomsCode(DeclareCustoms.JKF.code);
		personalgoodsStatusLog.setType(0); //0是回调		
		personalgoodsStatusLog.setResult(result.getChkMark());
		personalgoodsStatusLog.setResultDesc(CheckResult.getCnDesc(declareResult));
		personalgoodsStatusLog.setResultDetail(sbBuffer.toString());
		personalgoodsStatusLog.setResData(JSONObject.toJSONString(result));
		personalgoodsStatusLog.setCreateTime(new Date());	
		return personalgoodsStatusLog;
	}
	
	private void updateSubOrderByAuditResult(boolean result, SubOrder subOrder, PutCustomsType type){
		//更新状态
		SubOrder so = new SubOrder();
		so.setId(subOrder.getId());
		if (false == result) {
			so.setClearanceStatus(ClearanceStatus.AUDIT_FAILED.getCode());
		}
		PutCustomsStatus status = result ? PutCustomsStatus.AUDIT_SUCCESS : PutCustomsStatus.AUDIT_FAIL;
		if(PutCustomsType.ORDER_DECLARE == type){
			so.setPutOrderStatus(status.getCode());	
		}else if(PutCustomsType.PERSONALGOODS_DECLARE == type){
			so.setPutPersonalgoodsStatus(status.getCode());
		}
		so.setUpdateTime(new Date());
		subOrderService.updateNotNullById(so);		
	}
	
	private OrderStatusLog createOrderStatusLog(SubOrder subOrder, String resultMessage){
		OrderStatusLog orderStatusLog = new OrderStatusLog();
		orderStatusLog.setParentOrderCode(subOrder.getParentOrderCode());
		orderStatusLog.setOrderCode(subOrder.getOrderCode());
		orderStatusLog.setPreStatus(subOrder.getOrderStatus());
		orderStatusLog.setCurrStatus(subOrder.getOrderStatus());
		orderStatusLog.setCreateTime(new Date());
		orderStatusLog.setCreateUserId(1L);
		orderStatusLog.setCreateUserName("系统回调消息");
		orderStatusLog.setCreateUserType(LogTypeConstant.LOG_TYPE.SYSTEM.code);
		orderStatusLog.setName("海淘订单审核信息");
		orderStatusLog.setContent("海淘审核返回信息："+resultMessage);
		return orderStatusLog;
	}
	
	private CustomsClearanceLog createCustomsClearanceLog(JkfResult result, Long orderCode){
		Integer declareResult = Integer.parseInt(result.getChkMark());
		StringBuffer sbBuffer = new StringBuffer();
		for (JkfResultDetail detail : result.getResultList()) {
			sbBuffer.append(detail.getResultInfo()).append(";");
		}
		JKFBusinessType businessType = JKFBusinessType.getBusinessTypeByType(result.getBusinessType());
		CustomsClearanceLog log = new CustomsClearanceLog();
		log.setOrderCode(orderCode);
		log.setResult(declareResult == 1?1:0);
		log.setResultDesc(sbBuffer.toString());
		log.setType(getPutCustomsTypeByBusinessType(businessType).getIndex());
		log.setCustomsCode(DeclareCustoms.JKF.code);
		log.setCreateTime(new Date());
		return log;
	}
	
	private PutCustomsType getPutCustomsTypeByBusinessType(JKFBusinessType type){
		if (JKFBusinessType.IMPORTORDER == type) return PutCustomsType.ORDER_DECLARE;
		if (JKFBusinessType.PERSONAL_GOODS_DECLAR == type) return PutCustomsType.PERSONALGOODS_DECLARE;
		return null;
	}
	
	private OrderDeclareReceiptLog createOrderDeclareReceiptLog(JkfResult result){
		String orderCode = result.getBusinessNo();
		StringBuffer sbBuffer = new StringBuffer();
		for (JkfResultDetail detail : result.getResultList()) {
			sbBuffer.append(detail.getResultInfo()).append(";");
		}
		Integer declareResult = Integer.parseInt(result.getChkMark());
		OrderDeclareReceiptLog declareReceiptLog = new OrderDeclareReceiptLog();
		declareReceiptLog.setOrderCode(Long.valueOf(orderCode));
		declareReceiptLog.setDeclareNo(orderCode);
		declareReceiptLog.setCustomsCode(DeclareCustoms.JKF.code);
		declareReceiptLog.setResult(declareResult);
		declareReceiptLog.setResultDetail(sbBuffer.toString());
		declareReceiptLog.setResMsg(JSONObject.toJSONString(result));
		declareReceiptLog.setRemark(result.getNote());
		declareReceiptLog.setCreateTime(new Date());
		return declareReceiptLog;
	}
		
	private boolean validateResult(JkfResult result) throws Exception{
		JKFBusinessType businessType = JKFBusinessType.getBusinessTypeByType(result.getBusinessType());
		if (businessType == null) return false;
		
		Long orderCode = null;
		if (StringUtil.isEmpty(result.getBusinessNo())) {
			logger.error("[CUSTOMS_CALLBACK]业务编码为空");
			throw new Exception("businessNo编码为空");
		}
		if (JKFBusinessType.PERSONAL_GOODS_DECLAR == businessType){
			PersonalgoodsDeclareInfo info = 
					personalgoodsDeclareInfoService.queryPersonalgoodsDeclareInfoByPreEntryNo(result.getBusinessNo());
			if (null == info){
				logger.error("[CUSTOMS_CALLBACK][{}]清关单不存在");
				return false;
			}
			orderCode = info.getOrderCode();
		}else{
			orderCode = Long.valueOf(result.getBusinessNo());
		}
		SubOrder subOrder = subOrderService.selectOneByCode(orderCode);	
		if (null == subOrder) {
			logger.error("[CUSTOMS_CALLBACK][{}]订单不存在", orderCode);
			return false;
		}
		return true;
	}
}
