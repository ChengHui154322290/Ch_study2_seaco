package com.tp.service.ord.customs.JKF;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;
import com.tp.common.vo.Constant.AUTHOR_TYPE;
import com.tp.common.vo.OrderConstant.CancelCustomsOrderStatus;
import com.tp.common.vo.OrderConstant.ClearanceStatus;
import com.tp.common.vo.OrderConstant.DeclareCustoms;
import com.tp.common.vo.OrderConstant.PutCustomsStatus;
import com.tp.common.vo.OrderConstant.PutCustomsType;
import com.tp.common.vo.customs.JKFConstant;
import com.tp.common.vo.customs.MQCustomsConstant;
import com.tp.common.vo.customs.JKFConstant.CancelOrderAuditStatus;
import com.tp.common.vo.customs.JKFConstant.CheckResult;
import com.tp.common.vo.customs.JKFConstant.GoodsDeclareStatus;
import com.tp.common.vo.customs.JKFConstant.JKFBusinessType;
import com.tp.common.vo.customs.JKFConstant.JKFResultError;
import com.tp.common.vo.ord.LogTypeConstant;
import com.tp.dto.common.ResultInfo;
import com.tp.dto.ord.OrderDeliverDTO;
import com.tp.model.ord.CancelCustomsInfo;
import com.tp.model.ord.CancelCustomsReceiptLog;
import com.tp.model.ord.CustomsClearanceLog;
import com.tp.model.ord.OrderDeclareReceiptLog;
import com.tp.model.ord.OrderDelivery;
import com.tp.model.ord.OrderStatusLog;
import com.tp.model.ord.PersonalgoodsDeclareInfo;
import com.tp.model.ord.PersonalgoodsStatusLog;
import com.tp.model.ord.PersonalgoodsTaxReceipt;
import com.tp.model.ord.SubOrder;
import com.tp.model.ord.JKF.JkfCallbackResponse;
import com.tp.model.ord.JKF.JkfCancelOrderResult;
import com.tp.model.ord.JKF.JkfGoodsDeclarResult;
import com.tp.model.ord.JKF.JkfReceiptResult;
import com.tp.model.ord.JKF.JkfTaxIsNeedResult;
import com.tp.model.ord.JKF.JkfCancelOrderResult.ModifyCancelResult;
import com.tp.model.ord.JKF.JkfGoodsDeclarResult.JkfGoodsDeclar;
import com.tp.model.ord.JKF.JkfReceiptResult.JkfResult;
import com.tp.model.ord.JKF.JkfReceiptResult.JkfResultDetail;
import com.tp.model.ord.JKF.JkfTaxIsNeedResult.JkfTaxIsNeedDto;
import com.tp.mq.RabbitMqProducer;
import com.tp.service.ord.ICancelCustomsInfoService;
import com.tp.service.ord.ICancelCustomsReceiptLogService;
import com.tp.service.ord.ICustomsClearanceLogService;
import com.tp.service.ord.IOrderDeclareReceiptLogService;
import com.tp.service.ord.IOrderStatusLogService;
import com.tp.service.ord.IPersonalgoodsDeclareInfoService;
import com.tp.service.ord.IPersonalgoodsStatusLogService;
import com.tp.service.ord.IPersonalgoodsTaxReceiptService;
import com.tp.service.ord.ISubOrderService;
import com.tp.service.ord.customs.JKF.IJKFCallbackService;
import com.tp.service.stg.IOutputOrderService;
import com.tp.util.StringUtil;

@Service
public class JKFCallbackService implements IJKFCallbackService{

	private static final Logger logger = LoggerFactory.getLogger(JKFCallbackService.class);
	
	@Autowired
	private ISubOrderService subOrderService;
	
	@Autowired
	private IOrderDeclareReceiptLogService orderDeclareReceiptLogService;
	
	@Autowired
	private IPersonalgoodsStatusLogService personalgoodsStatusLogService;
	
	@Autowired
	private IPersonalgoodsDeclareInfoService personalgoodsDeclareInfoService;  
	
	@Autowired
	private IPersonalgoodsTaxReceiptService personalgoodsTaxReceiptService;
	
	@Autowired
	private IOrderStatusLogService orderStatusLogService;
	
    @Autowired
    private RabbitMqProducer rabbitMqProducer;
    
    @Autowired
    private ICancelCustomsInfoService cancelCustomsInfoService;
    
    @Autowired
    private ICancelCustomsReceiptLogService cancelCustomsReceiptLogService;
    
    @Autowired
    private ICustomsClearanceLogService customsClearanceLogService;
    
    @Autowired
    private IOutputOrderService outputOrderService;
	
	/**通关平台的回执（业务检验时的异步回执）（包括订单回执，个人物品信息回执及其他回执）
	 * @throws Exception **/
	@Override
	@Transactional
	public JkfCallbackResponse customsDeclareCallback(JkfReceiptResult receiptResult) throws Exception {
		if (CollectionUtils.isEmpty(receiptResult.getBody().getList())) {
			logger.error("[CUSTOMS_CALLBACK]数据为空");
			return new JkfCallbackResponse(JKFResultError.INVALID_CONTENT);
		}
		for (JkfResult result : receiptResult.getBody().getList()) {
			String businessType = result.getBusinessType();
			if (JKFBusinessType.IMPORTORDER.type.equals(businessType)) {
				//订单申报回执
				processOrderDeclCallback(result);
			}else if(JKFBusinessType.PERSONAL_GOODS_DECLAR.type.equals(businessType)){
				//个人物品申报回执
				processPersonalGoodsDeclCallback(result);
			}else {
				logger.error("[CUSTOMS_CALLBACK][businessType={}]未知的业务类型", businessType);
			}
		}
		return new JkfCallbackResponse();
	}

	/** 
	 * 	清关单审单回执 
	 */
	@Override
	@Transactional
	public JkfCallbackResponse goodsDeclareCallback(JkfGoodsDeclarResult goodsDeclarResult) {
		String busineType = goodsDeclarResult.getHead().getBusinessType();
		if (!JKFConstant.JKFBusinessType.PERSONAL_GOODS_DECLAR.type.equals(busineType)) {
			logger.error("[CLEARANCE_CALLBACK][businessType={}]业务类型不正确", busineType);
			return new JkfCallbackResponse(JKFResultError.INVALID_BUSINESS_TYPE);
		}
		String businessNo = goodsDeclarResult.getBody().getJkfSign().getBusinessNo();
		if (StringUtil.isEmpty(businessNo)) {
			logger.error("[CLEARANCE_CALLBACK]业务代码为空");
			return new JkfCallbackResponse(JKFResultError.INVALID_REQUEST_PARAM);
		}
		
		JkfGoodsDeclar decl = goodsDeclarResult.getBody().getJkfGoodsDeclar();		
		//业务处理
		Map<String, Object> params = new HashMap<>();
		params.put("preEntryNo", businessNo);
		PersonalgoodsDeclareInfo pgdecl = personalgoodsDeclareInfoService.queryUniqueByParams(params);
		if (null == pgdecl) {
			logger.error("[CLEARANCE_CALLBACK][businessNO = {}]清关单数据不存在", businessNo);
			return new JkfCallbackResponse();
		}
		//查询相关子订单
		SubOrder subOrder = subOrderService.selectOneByCode(Long.valueOf(pgdecl.getOrderCode()));	
		if (null == subOrder) {
			logger.error("[CLEARANCE_CALLBACK][orderCode={}]子订单不存在", pgdecl.getOrderCode());
			return new JkfCallbackResponse();
		}		
		logger.error("[CLEARANCE_CALLBACK][{}]开始处理海关回执", pgdecl.getOrderCode());
		//个人物品申报日志
		PersonalgoodsStatusLog personalgoodsStatusLog = new PersonalgoodsStatusLog();
		personalgoodsStatusLog.setOrderCode(pgdecl.getOrderCode());
		personalgoodsStatusLog.setDeclareNo(pgdecl.getDeclareNo());
		personalgoodsStatusLog.setCustomsCode(DeclareCustoms.JKF.code);
		personalgoodsStatusLog.setCustomsDeclareNo(decl.getPersonalGoodsFormNo());
		personalgoodsStatusLog.setType(0); //0是回调
		personalgoodsStatusLog.setResult(decl.getApproveResult());
		personalgoodsStatusLog.setResultDesc(GoodsDeclareStatus.getNameByCode(decl.getApproveResult()));
		personalgoodsStatusLog.setResultDetail(decl.getApproveComment());
		personalgoodsStatusLog.setResData(JSONObject.toJSONString(decl));
		personalgoodsStatusLog.setCreateTime(new Date());
		Integer cStatus = subOrder.getClearanceStatus();
		if (ClearanceStatus.AUDIT_SUCCESS.code.equals(cStatus) ||
				ClearanceStatus.AUDIT_FAILED.code.equals(cStatus)) {
			logger.error("[CLEARANCE_CALLBACK][{}]海关已反馈，重复回执", subOrder.getOrderCode(), JSONObject.toJSON(decl));
			personalgoodsStatusLog.setRemark("海关重复反馈");
		}else{
			StringBuffer sbMessage = new StringBuffer("海关审核 - ");
			ClearanceStatus clearanceStatus = null;
			if (GoodsDeclareStatus.DECL_UNACCEPT.code.equals(decl.getApproveResult())) {
				logger.error("[CLEARANCE_CALLBACK][{}]清关失败：{}", subOrder.getOrderCode(), decl.getApproveComment());
				clearanceStatus = ClearanceStatus.AUDIT_FAILED;
				sbMessage.append("清关失败:" + decl.getApproveComment());
				//清关日志
				insertClearanceCustomsLog(subOrder, 0, sbMessage.toString(), PutCustomsType.CLEARANCE);
				processPersonalGoodsCustomsFail(pgdecl.getOrderCode());									//处理失败的情况					
			}else if (GoodsDeclareStatus.DECL_AUTO_PASS.code.equals(decl.getApproveResult()) ||		//放行
					   GoodsDeclareStatus.DECL_DIRECT_PASS.code.equals(decl.getApproveResult()) ||
					   GoodsDeclareStatus.DECL_MANUAL_PASS.code.equals(decl.getApproveResult())) {
				logger.error("[CLEARANCE_CALLBACK][{}]清关放行：{}", subOrder.getOrderCode(), decl.getApproveComment());
				// 清关通过
				clearanceStatus = ClearanceStatus.AUDIT_SUCCESS;
				//海关放行,可发货
				sbMessage.append("清关放行:" + decl.getApproveComment());
				insertClearanceCustomsLog(subOrder, 1, sbMessage.toString(), PutCustomsType.CLEARANCE);
				processPersonalGoodsCustomsSuccess(subOrder, pgdecl);	
			}
			//更新订单状态
			if(clearanceStatus != null){
				SubOrder so = new SubOrder();
				so.setId(subOrder.getId());
				so.setClearanceStatus(clearanceStatus.getCode());
				so.setUpdateTime(new Date());
				insertOrderStatusLog(subOrder, sbMessage.toString());
				subOrderService.updateNotNullById(so);
				//更新个人物品申报单中的海关编号
				if (StringUtil.isEmpty(pgdecl.getPersonalgoodsNo())) {
					PersonalgoodsDeclareInfo nDecl = new PersonalgoodsDeclareInfo();
					nDecl.setId(pgdecl.getId());
					nDecl.setPersonalgoodsNo(decl.getPersonalGoodsFormNo());
					personalgoodsDeclareInfoService.updateNotNullById(nDecl);
				}
			}							
		}
		personalgoodsStatusLogService.insert(personalgoodsStatusLog);
		logger.error("[CLEARANCE_CALLBACK][{}]海关回执处理完成", subOrder.getOrderCode());
		return new JkfCallbackResponse();
	}

	/** 海关税费是否征收回执 **/
	@Override
	@Transactional
	public JkfCallbackResponse taxIsNeedCallback(JkfTaxIsNeedResult taxResult) {
		String busineType = taxResult.getHead().getBusinessType();
		if (!JKFConstant.JKFBusinessType.TAXISNEED.type.equals(busineType)) {
			logger.error("[TAX_CALLBACK][businessType={}]业务类型不正确", busineType);
			return new JkfCallbackResponse(JKFResultError.INVALID_BUSINESS_TYPE);
		}
		String businessNo = taxResult.getBody().getJkfSign().getBusinessNo();
		if (StringUtil.isEmpty(businessNo) || StringUtil.isEmpty(taxResult.getBody().getJkfTaxIsNeedDto().getPersonalGoodsFormNo())) {
			logger.error("[TAX_CALLBACK]业务代码为空");
			return new JkfCallbackResponse(JKFResultError.INVALID_REQUEST_PARAM);
		}
		// 流水入库
		JkfTaxIsNeedResult.JkfSign sign = taxResult.getBody().getJkfSign();
		JkfTaxIsNeedDto dto = taxResult.getBody().getJkfTaxIsNeedDto();
		//业务处理
		Map<String, Object> params = new HashMap<>();
		params.put("personalgoodsNo", dto.getPersonalGoodsFormNo());
//		params.put("expressNo", sign.getBusinessNo());
		params.put("preEntryNo", sign.getBusinessNo());
		PersonalgoodsDeclareInfo pgdecl = personalgoodsDeclareInfoService.queryUniqueByParams(params);
		if (null == pgdecl) {
			logger.error("[TAX_CALLBACK][businessNO = {}][goodsFormNo={}]原申报清单不存在", 
					businessNo, dto.getPersonalGoodsFormNo());
			return new JkfCallbackResponse();
		}
		
		//入库
		PersonalgoodsTaxReceipt receipt = new PersonalgoodsTaxReceipt();
		receipt.setOrderCode(pgdecl.getOrderCode());
		receipt.setPersonalgoodsNo(dto.getPersonalGoodsFormNo());
		receipt.setExpressNo(sign.getBusinessNo());
		receipt.setIsTax(Integer.parseInt(dto.getIsNeed()));
		receipt.setTaxAmount(Double.valueOf(dto.getTaxAmount()));
		receipt.setCreateTime(new Date());
		personalgoodsTaxReceiptService.insert(receipt);
		logger.info("[TAX_CALLBACK][{}]税费回执处理完成", pgdecl.getOrderCode());
		return new JkfCallbackResponse();
	}	
	
	/** 取消订单海关回执（废弃） **/
	@Override
	@Transactional
	@Deprecated
	public JkfCallbackResponse cancelOrderCallback(JkfCancelOrderResult cancelOrderResult) {
		String busineType = cancelOrderResult.getHead().getBusinessType();
		if (!JKFConstant.JKFBusinessType.MODIFY_CANCEL.type.equals(busineType)) {
			logger.error("[DELETE_ORDER_CALLBACK][businessType={}]业务类型不正确", busineType);
			return new JkfCallbackResponse(JKFResultError.INVALID_BUSINESS_TYPE);
		}
		String businessNo = cancelOrderResult.getBody().getModifyCancelResult().getBusinessNo();
		if (StringUtil.isEmpty(businessNo)) {
			logger.error("[DELETE_ORDER_CALLBACK]业务代码为空");
			return new JkfCallbackResponse(JKFResultError.INVALID_REQUEST_PARAM);
		}
		Map<String, Object> params = new HashMap<>();
		params.put("orderCode", businessNo);
		List<CancelCustomsInfo> cancelInfos = cancelCustomsInfoService.queryByParamNotEmpty(params);
		if (CollectionUtils.isEmpty(cancelInfos)) {
			logger.error("[DELETE_ORDER_CALLBACK][orderCode={}]删单信息不存在", businessNo);
			return new JkfCallbackResponse(JKFResultError.SYSTEM_EXCEPTION);
		}
		//子订单
		SubOrder subOrder = subOrderService.selectOneByCode(Long.valueOf(businessNo));
		if (null == subOrder) {
			logger.error("[DELETE_ORDER_CALLBACK][{}]子订单信息不存在]", businessNo);
			return new JkfCallbackResponse(JKFResultError.SYSTEM_EXCEPTION);
		}
		ModifyCancelResult cancelResult = cancelOrderResult.getBody().getModifyCancelResult();
		JkfCallbackResponse message = null;
		//日志及参数
		CancelCustomsInfo cInfo = new CancelCustomsInfo();
		cInfo.setId(cancelInfos.get(0).getId());
		CancelCustomsReceiptLog log = new CancelCustomsReceiptLog();
		log.setOrderCode(Long.valueOf(businessNo));
		log.setCustomsCode(DeclareCustoms.JKF.code);
		log.setApproveResult(Integer.parseInt(cancelResult.getApproveResult()));
		log.setApproveComment(cancelResult.getApproveComment());
		log.setResMsg(JSONObject.toJSONString(cancelResult));
		log.setCreateTime(new Date());
		if (CancelOrderAuditStatus.SUCCESS.code.equals(cancelResult.getApproveResult())) {
			//审核通过
			insertOrderStatusLog(subOrder, "取消订单申报海关审核通过：" + cancelResult.getApproveComment());
			cInfo.setCancelStatus(CancelCustomsOrderStatus.AUDIT_SUCCESS.code);
			message = new JkfCallbackResponse();
		}else if (CancelOrderAuditStatus.FAILED.code.equals(cancelResult.getApproveResult())) {
			//审核失败
			insertOrderStatusLog(subOrder, "取消订单申报海关审核不通过:" + cancelResult.getApproveComment());
			cInfo.setCancelStatus(CancelCustomsOrderStatus.AUDIT_FAILED.code);
			message = new JkfCallbackResponse();
		}else{
			//未知审核结果
			logger.error("[DELETE_ORDER_CALLBACK]返回未知审核类型:{},{}", cancelResult.getApproveResult(), cancelResult.getApproveComment());
			message = new JkfCallbackResponse(JKFResultError.B_UNKNOWN_BUSINESS_TYPE);
		}
		cancelCustomsInfoService.updateById(cInfo);
		cancelCustomsReceiptLogService.insert(log);
		return message;
	}
	
	
	/*--------------------------------------------------------------------------------------------------*/
	/*--------------------------------------------------------------------------------------------------*/
	/*--------------------------------------------------------------------------------------------------*/
	
	
	/**
	 *	处理订单申报电子口岸回执 
	 * @throws Exception 
	 */
	private void processOrderDeclCallback(JkfResult result) throws Exception{	
		if (StringUtil.isEmpty(result.getBusinessNo())) {
			logger.error("[CUSTOMS_CALLBACK][ORDER_CALLBACK]业务编码为空");
			throw new Exception("businessNo编码为空");
		}
		//查询相关子订单
		String orderCode = result.getBusinessNo();
		logger.info("[CUSTOMS_CALLBACK][ORDER_CALLBACK][{}]开始处理订单回执...", orderCode);
		SubOrder subOrder = subOrderService.selectOneByCode(Long.valueOf(result.getBusinessNo()));	
		if (null == subOrder) {
			logger.error("[CUSTOMS_CALLBACK][ORDER_CALLBACK][{}]订单不存在", orderCode);
			return;
		}
		//流水入库
		StringBuffer sbBuffer = new StringBuffer();
		for (JkfResultDetail detail : result.getResultList()) {
			sbBuffer.append(detail.getResultInfo());
		}
		logger.info("[CUSTOMS_CALLBACK][ORDER_CALLBACK][{}]回执数据:{}", orderCode, sbBuffer.toString());
		//回执日志入库
		Integer declareResult = Integer.parseInt(result.getChkMark());
		OrderDeclareReceiptLog declareReceiptLog = new OrderDeclareReceiptLog();
		declareReceiptLog.setOrderCode(subOrder.getOrderCode());
		declareReceiptLog.setDeclareNo(result.getBusinessNo());
		declareReceiptLog.setCustomsCode(DeclareCustoms.JKF.code);
		declareReceiptLog.setResult(declareResult);
		declareReceiptLog.setResultDetail(sbBuffer.toString());
		declareReceiptLog.setResMsg(JSONObject.toJSONString(result));
		declareReceiptLog.setRemark(result.getNote());
		declareReceiptLog.setCreateTime(new Date());
		orderDeclareReceiptLogService.insert(declareReceiptLog);
		
		//清关日志
		insertClearanceCustomsLog(subOrder, declareResult == 1?1:0, "电子口岸回执结果:" + sbBuffer.toString(), PutCustomsType.ORDER_DECLARE);
		Integer clearanceStatus = subOrder.getClearanceStatus();
		if (ClearanceStatus.AUDIT_SUCCESS.getCode().equals(clearanceStatus) ||
				ClearanceStatus.AUDIT_FAILED.getCode().equals(clearanceStatus)) {
			logger.error("[CUSTOMS_CALLBACK][ORDER_CALLBACK][{}]海关已处理,重复回执", orderCode);
			return;
		}
		//已经处理过通关平台的反馈
		Integer putOrderStatus = subOrder.getPutOrderStatus();
		if (PutCustomsStatus.AUDIT_SUCCESS.getCode().equals(putOrderStatus) ||
				PutCustomsStatus.AUDIT_FAIL.getCode().equals(putOrderStatus)) {		
			logger.error("[CUSTOMS_CALLBACK][ORDER_CALLBACK][{}]口岸已反馈,重复回执", orderCode);
		}else{
			//更新状态
			StringBuffer sbMessage = new StringBuffer(" 订单申报 - ");
			ClearanceStatus cs = null;
			PutCustomsStatus status = null;
			if (CheckResult.SUCCESS.result.equals(result.getChkMark())) {	//通关平台审核成功					
				status = PutCustomsStatus.AUDIT_SUCCESS;
				sbMessage.append("通关平台审核通过：" + sbBuffer.toString());// 设置日志内容
			}else{
				status = PutCustomsStatus.AUDIT_FAIL;
				cs = ClearanceStatus.AUDIT_FAILED;
				sbMessage.append("通关平台审核不通过：" + sbBuffer.toString());// 设置日志内容
			}
			//更新状态
			SubOrder so = new SubOrder();
			so.setId(subOrder.getId());
			if (cs != null) {
				so.setClearanceStatus(cs.getCode());
			}
			so.setPutOrderStatus(status.getCode());
			so.setUpdateTime(new Date());
			insertOrderStatusLog(subOrder, sbMessage.toString());
			subOrderService.updateNotNullById(so);
		}	
		logger.info("[CUSTOMS_CALLBACK][ORDER_CALLBACK][{}]订单回执处理结束", orderCode);
	}
	
	/**
	 *	跨境电商平台处理个人物品申报电子口岸回执 
	 * @throws Exception 
	 */
	private void processPersonalGoodsDeclCallback(JkfResult result) throws Exception{
		String businessNo = result.getBusinessNo();
		if (StringUtil.isEmpty(businessNo)) {
			logger.error("[CUSTOMS_CALLBACK][PERSONALGOODS_CALLBACK]业务代码为空");
			throw new Exception("业务代码为空");
		}	
		// 业务处理
		Map<String, Object> params = new HashMap<>();
		params.put("preEntryNo", businessNo);
		PersonalgoodsDeclareInfo pgdecl = personalgoodsDeclareInfoService.queryUniqueByParams(params);
		if (null == pgdecl) {
			logger.error("[CUSTOMS_CALLBACK][PERSONALGOODS_CALLBACK]原申报清关单不存在，businessNO = {}", businessNo);
			return;
		}	
		//查询相关子订单
		SubOrder subOrder = subOrderService.selectOneByCode(Long.valueOf(pgdecl.getOrderCode()));
		if (null == subOrder) {
			logger.error("[CUSTOMS_CALLBACK][PERSONALGOODS_CALLBACK][{}]子订单信息不存在", pgdecl.getOrderCode());
			return;
		}
		//流水入库
		StringBuffer sbBuffer = new StringBuffer();
		for (JkfResultDetail detail : result.getResultList()) {
			sbBuffer.append(detail.getResultInfo());
		}
		//个人物品申报日志
		Integer declareResult = Integer.parseInt(result.getChkMark());
		PersonalgoodsStatusLog personalgoodsStatusLog = new PersonalgoodsStatusLog();
		personalgoodsStatusLog.setOrderCode(pgdecl.getOrderCode());
		personalgoodsStatusLog.setDeclareNo(pgdecl.getDeclareNo());
		personalgoodsStatusLog.setCustomsCode(DeclareCustoms.JKF.code);
		personalgoodsStatusLog.setType(0); //0是回调		
		personalgoodsStatusLog.setResult(result.getChkMark());
		personalgoodsStatusLog.setResultDesc(CheckResult.getCnDesc(declareResult));
		personalgoodsStatusLog.setResultDetail(sbBuffer.toString());
		personalgoodsStatusLog.setResData(JSONObject.toJSONString(result));
		personalgoodsStatusLog.setCreateTime(new Date());
		personalgoodsStatusLogService.insert(personalgoodsStatusLog);
		//清关日志
		insertClearanceCustomsLog(subOrder, declareResult == 1?1:0, "电子口岸回执结果:" + sbBuffer.toString(), PutCustomsType.PERSONALGOODS_DECLARE);
		Integer clearanceStatus = subOrder.getClearanceStatus();
		if (ClearanceStatus.AUDIT_FAILED.code.equals(clearanceStatus) || ClearanceStatus.AUDIT_SUCCESS.code.equals(clearanceStatus)) {
			logger.error("[CUSTOMS_CALLBACK][PERSONALGOODS_CALLBACK][{}]海关已处理，重复回执", subOrder.getOrderCode());
			return;
		}
			
		if (PutCustomsStatus.AUDIT_SUCCESS.getCode().equals(subOrder.getPutPersonalgoodsStatus()) ||
				PutCustomsStatus.AUDIT_FAIL.getCode().equals(subOrder.getPutPersonalgoodsStatus())) {		
			logger.error("[CUSTOMS_CALLBACK][PERSONALGOODS_CALLBACK][{}]口岸已反馈，重复回执", subOrder.getOrderCode());
		}else{
			StringBuffer sbMessage = new StringBuffer(" 个人物品申报单 - ");
			PutCustomsStatus status = null;
			ClearanceStatus cs = null;
			if (CheckResult.SUCCESS.result.equals(Integer.parseInt(result.getChkMark()))) {	//通关平台审核成功		
				logger.info("[CUSTOMS_CALLBACK][PERSONALGOODS_CALLBACK][{}]口岸审核通过", subOrder.getOrderCode());
				sbMessage.append("通关平台审核通过");	
				status = PutCustomsStatus.AUDIT_SUCCESS;
			}else{
				logger.info("[CUSTOMS_CALLBACK][PERSONALGOODS_CALLBACK][{}]口岸审核不通过：",
						subOrder.getOrderCode(), sbBuffer.toString());
				sbMessage.append("通关平台审核不通过 - " + sbBuffer.toString());// 设置日志内容
				cs = ClearanceStatus.AUDIT_FAILED;
				status = PutCustomsStatus.AUDIT_FAIL;
			}
			//更新状态
			SubOrder so = new SubOrder();
			so.setId(subOrder.getId());
			if (cs != null) {
				so.setClearanceStatus(cs.getCode());
			}
			so.setPutPersonalgoodsStatus(status.getCode());
			so.setUpdateTime(new Date());
			insertOrderStatusLog(subOrder, sbMessage.toString());
			subOrderService.updateNotNullById(so);
		}
		logger.info("[CUSTOMS_CALLBACK][PERSONALGOODS_CALLBACK][{}]回执处理完成", subOrder.getOrderCode());
	}
	
	/**
	 * 更新订单状态以及写入订单状态日志 
	 */
//	private void updateOrderPutStatus(Long subOrderCode, ClearanceStatus clearanceStatus, String message) {
//		SubOrder subOrder = subOrderService.selectOneByCode(subOrderCode);
//		if(null==subOrder){
//			logger.error("未找到订单号为{}的子订单信息",subOrderCode);
//			return;
//		}
//		subOrder.setClearanceStatus(clearanceStatus.code);
//		subOrder.setUpdateTime(new Date());
//		subOrderService.updateNotNullById(subOrder);
//		insertOrderStatusLog(subOrder, message);
//	}
	
	private void insertOrderStatusLog(SubOrder subOrder, String message){
		// 添加订单状态日志信息
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
		orderStatusLog.setContent("海淘审核返回信息："+message);
		orderStatusLogService.insert(orderStatusLog);
	}
	
	/** 清关相关日志 */
	private void insertClearanceCustomsLog(SubOrder subOrder, int result, String detail, PutCustomsType type){
		CustomsClearanceLog log = new CustomsClearanceLog();
		log.setOrderCode(subOrder.getOrderCode());
		log.setResult(result);
		log.setResultDesc(detail);
		log.setType(type.index);
		log.setCustomsCode(DeclareCustoms.JKF.code);
		log.setCreateTime(new Date());
		customsClearanceLogService.insert(log);
	}
	
	/** 处理订单申报失败（电子口岸）（如退单） **/
	private void processOrderDeclFail(Long orderCode){
		try {
			
		} catch (Exception e) {
			logger.error("[处理订单申报失败（电子口岸） 异常]", e);
		}
	}
	
	/**处理个人物品申报失败（电子口岸）**/
	private void processPersonalGoodsDeclFail(Long orderCode){
		try {
			
		} catch (Exception e) {
			logger.error("[处理个人物品申报失败（电子口岸）异常]", e);
		}
	}
	
	/**
	 * 处理个人物品申报海关审核失败(清关失败，退款)
	 */
	private void processPersonalGoodsCustomsFail(Long orderCode){
		try {
			
		} catch (Exception e) {
			logger.error("[处理个人物品申报海关审核失败 异常]", e);
		}
	}
	
	/**
	 * 清关成功,发货
	 */
	private void processPersonalGoodsCustomsSuccess(SubOrder subOrder, PersonalgoodsDeclareInfo pgInfo){
		try {
			if (pgInfo.getImportType() != null && pgInfo.getImportType() == 0){ //直邮订单清关之后直接发货
				ResultInfo<Boolean> resultInfo = outputOrderService.exWarehouseService(createOrderDelivery(pgInfo));
				if (!resultInfo.isSuccess()){
					logger.error("[CLEARANCE_CALLBACK][{}]直邮订单发货失败:", resultInfo.getMsg().getMessage());
				}
			}else{
				logger.info("[CLEARANCE_CALLBACK][{}]清关通过,发送货物出库通知", subOrder.getOrderCode());
				rabbitMqProducer.sendP2PMessage(MQCustomsConstant.SEAORDER_STOCKOUT_P2P_QUEUE, subOrder);	
			}
		} catch (Exception e) {
			logger.error("[CLEARANCE_CALLBACK][{}]清关通过,发送货物出库通知异常", subOrder.getOrderCode(), e);
		}
	}
	
	private OrderDelivery createOrderDelivery(PersonalgoodsDeclareInfo pgInfo){
		OrderDelivery orderDelivery = new OrderDelivery();
		orderDelivery.setCompanyId(pgInfo.getCompanyNo());
		orderDelivery.setCompanyName(pgInfo.getCompanyName());
		orderDelivery.setOrderCode(pgInfo.getOrderCode());
		orderDelivery.setPackageNo(pgInfo.getExpressNo());
		orderDelivery.setCreateUser(AUTHOR_TYPE.SYSTEM);	// 设置处理人
		orderDelivery.setDeliveryTime(new Date());			// 设置发货时间
		return orderDelivery;
	}
}
