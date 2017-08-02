package com.tp.service.ord.customs.JKF.callback;

import java.util.Date;

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
import com.tp.common.vo.customs.JKFConstant.GoodsDeclareStatus;
import com.tp.common.vo.customs.JKFConstant.JKFBusinessType;
import com.tp.common.vo.customs.JKFConstant.JKFFeedbackType;
import com.tp.common.vo.customs.JKFConstant.JKFResultError;
import com.tp.common.vo.ord.LogTypeConstant;
import com.tp.model.ord.CustomsClearanceLog;
import com.tp.model.ord.OrderStatusLog;
import com.tp.model.ord.PersonalgoodsDeclareInfo;
import com.tp.model.ord.PersonalgoodsStatusLog;
import com.tp.model.ord.SubOrder;
import com.tp.model.ord.JKF.JkfBaseDO;
import com.tp.model.ord.JKF.JkfCallbackResponse;
import com.tp.model.ord.JKF.JkfGoodsDeclarResult;
import com.tp.model.ord.JKF.JkfGoodsDeclarResult.JkfGoodsDeclar;
import com.tp.service.ord.ICustomsClearanceLogService;
import com.tp.service.ord.IOrderStatusLogService;
import com.tp.service.ord.IPersonalgoodsDeclareInfoService;
import com.tp.service.ord.IPersonalgoodsStatusLogService;
import com.tp.service.ord.ISubOrderService;
import com.tp.service.ord.customs.JKF.callback.IJKFClearanceCallbackProcessService;
import com.tp.util.StringUtil;

/**
 * 海关审单回执 
 */
@Service
public class JKFDeclareGoodsCallbackProcessService implements IJKFClearanceCallbackProcessService{

	private static final Logger logger = LoggerFactory.getLogger(JKFDeclareGoodsCallbackProcessService.class);
	
	@Autowired
	private IPersonalgoodsDeclareInfoService personalgoodsDeclareInfoService;
	
	@Autowired
	private ISubOrderService subOrderService;
	
	@Autowired
	private IPersonalgoodsStatusLogService personalgoodsStatusLogService;
	
	@Autowired
	private ICustomsClearanceLogService customsClearanceLogService;
	
	@Autowired
	private IOrderStatusLogService orderStatusLogService;
	

	
	@Override
	public boolean checkReceiptType(JKFFeedbackType type) {
		if (JKFFeedbackType.CUSTOMS_DECLARE_GOODS_CALLBACK == type){
			return true;
		}
		return false;
	}

	/** 审单回执 */
	@Override
	@Transactional
	public JkfCallbackResponse processCallback(JkfBaseDO receiptResult) throws Exception{
		JkfGoodsDeclarResult goodsDeclarResult = (JkfGoodsDeclarResult)receiptResult;
		JkfCallbackResponse response = validateResult(goodsDeclarResult);
		if (response != null) return response;
		
		JkfGoodsDeclar decl = goodsDeclarResult.getBody().getJkfGoodsDeclar();	
		String preEntryNo = goodsDeclarResult.getBody().getJkfSign().getBusinessNo();	
		
		PersonalgoodsDeclareInfo pgdecl = 
				personalgoodsDeclareInfoService.queryPersonalgoodsDeclareInfoByPreEntryNo(preEntryNo);
		SubOrder subOrder = subOrderService.selectOneByCode(Long.valueOf(pgdecl.getOrderCode()));
		personalgoodsStatusLogService.insert(createPersonalgoodsStatusLog(decl, pgdecl));
		
		if (false == checkClearanceStatusWithAuditStatus(subOrder)) return new JkfCallbackResponse();
		
		logger.info("[CLEARANCE_CALLBACK][{}]海关审单回执：{}", subOrder.getOrderCode(), decl.getApproveComment());		
		ClearanceStatus status = GoodsDeclareStatus.checkResult(decl.getApproveResult());	
		if (status == null) return new JkfCallbackResponse();
	
		updateSubOrderByAuditResult(status, subOrder);
		updatePersonalgoodsDeclareInfo(pgdecl, decl);
		customsClearanceLogService.insert(createCustomsClearanceLog(decl, subOrder.getOrderCode()));
		orderStatusLogService.insert(createOrderStatusLog(subOrder, status.getDesc()));
		if (ClearanceStatus.AUDIT_SUCCESS == status) {
			logger.info("[CLEARANCE_CALLBACK]杭州海关已放行，具体应以总署放行通知为准（公告2017.01.18）");
		}
		return new JkfCallbackResponse();
	}

	private boolean checkClearanceStatusWithAuditStatus(SubOrder subOrder){
		Integer clearanceStatus = subOrder.getClearanceStatus();
		if(ClearanceStatus.hasAudit(clearanceStatus)){
			logger.error("[CLEARANCE_CALLBACK][{}]海关已反馈,重复回执，当前状态：{}", 
					subOrder.getOrderCode(), ClearanceStatus.getClearanceDescByCode(clearanceStatus));
			return false;
		}
		if(false == ClearanceStatus.PUT_SUCCESS.code.equals(clearanceStatus)){
			logger.error("[CLEARANCE_CALLBACK][{}]清关申报单据未推送成功,当前状态：{}", 
					subOrder.getOrderCode(), ClearanceStatus.getClearanceDescByCode(clearanceStatus));
			return false;
		}
		return true;
	}
	
	private void updatePersonalgoodsDeclareInfo(PersonalgoodsDeclareInfo pgInfo, JkfGoodsDeclar result){
		if(StringUtil.isNotEmpty(pgInfo.getPersonalgoodsNo())) return;
		PersonalgoodsDeclareInfo nDecl = new PersonalgoodsDeclareInfo();
		nDecl.setId(pgInfo.getId());
		nDecl.setPersonalgoodsNo(result.getPersonalGoodsFormNo());
		personalgoodsDeclareInfoService.updateNotNullById(nDecl);
	}
	
	private void updateSubOrderByAuditResult(ClearanceStatus status, SubOrder subOrder){
		//以总署放行为准
		if (ClearanceStatus.AUDIT_FAILED != status && ClearanceStatus.PUT_FAILED != status) return; 
		//更新状态
		SubOrder so = new SubOrder();
		so.setId(subOrder.getId());
		so.setClearanceStatus(status.getCode());
		if (ClearanceStatus.PUT_FAILED == status){
			so.setPutPersonalgoodsStatus(PutCustomsStatus.FAIL.getCode());
		}else if(ClearanceStatus.AUDIT_FAILED == status){
			so.setPutPersonalgoodsStatus(PutCustomsStatus.AUDIT_FAIL.code);
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
	
	private PersonalgoodsStatusLog createPersonalgoodsStatusLog(JkfGoodsDeclar result, PersonalgoodsDeclareInfo pgInfo){
		PersonalgoodsStatusLog personalgoodsStatusLog = new PersonalgoodsStatusLog();
		personalgoodsStatusLog.setOrderCode(pgInfo.getOrderCode());
		personalgoodsStatusLog.setDeclareNo(pgInfo.getDeclareNo());
		personalgoodsStatusLog.setCustomsCode(DeclareCustoms.JKF.code);
		personalgoodsStatusLog.setCustomsDeclareNo(result.getPersonalGoodsFormNo());
		personalgoodsStatusLog.setType(0); //0是回调
		personalgoodsStatusLog.setResult(result.getApproveResult());
		personalgoodsStatusLog.setResultDesc(GoodsDeclareStatus.getNameByCode(result.getApproveResult()));
		personalgoodsStatusLog.setResultDetail(result.getApproveComment());
		personalgoodsStatusLog.setResData(JSONObject.toJSONString(result));
		personalgoodsStatusLog.setCreateTime(new Date());
		return personalgoodsStatusLog;
	}
	
	private CustomsClearanceLog createCustomsClearanceLog(JkfGoodsDeclar result, Long orderCode){
		String message = result.getApproveComment();
		if (StringUtil.isEmpty(message)){
			message = GoodsDeclareStatus.getNameByCode(result.getApproveResult());
		}
		Integer declareResult = 2;
		if (GoodsDeclareStatus.isPass(result.getApproveResult())){
			declareResult = 1;
		}else if(GoodsDeclareStatus.isReject(result.getApproveResult())){
			declareResult = 0;
		}
		CustomsClearanceLog log = new CustomsClearanceLog();
		log.setOrderCode(orderCode);
		log.setResult(declareResult);
		log.setResultDesc(message);
		log.setType(PutCustomsType.CLEARANCE.getIndex());
		log.setCustomsCode(DeclareCustoms.JKF.code);
		log.setCreateTime(new Date());
		return log;
	}
	
	private JkfCallbackResponse validateResult(JkfGoodsDeclarResult result){
		String busineType = result.getHead().getBusinessType();
		if (!JKFBusinessType.PERSONAL_GOODS_DECLAR.type.equals(busineType)) {
			logger.error("[CLEARANCE_CALLBACK][businessType={}]业务类型不正确", busineType);
			return new JkfCallbackResponse(JKFResultError.INVALID_BUSINESS_TYPE);
		}
		String businessNo = result.getBody().getJkfSign().getBusinessNo();
		if (StringUtil.isEmpty(businessNo)) {
			logger.error("[CLEARANCE_CALLBACK]业务代码为空");
			return new JkfCallbackResponse(JKFResultError.INVALID_REQUEST_PARAM);
		}	
		//业务处理
		PersonalgoodsDeclareInfo pgdecl = 
				personalgoodsDeclareInfoService.queryPersonalgoodsDeclareInfoByPreEntryNo(businessNo);
		if (null == pgdecl) {
			logger.error("[CLEARANCE_CALLBACK][businessNO = {}]清关单数据不存在", businessNo);
			return new JkfCallbackResponse(JKFResultError.BUSINESS_SERVER_EXCEPTION);
		}
		//查询相关子订单
		SubOrder subOrder = subOrderService.selectOneByCode(Long.valueOf(pgdecl.getOrderCode()));	
		if (null == subOrder) {
			logger.error("[CLEARANCE_CALLBACK][orderCode={}]子订单不存在", pgdecl.getOrderCode());
			return new JkfCallbackResponse(JKFResultError.BUSINESS_SERVER_EXCEPTION);
		}	
		
		return null;
	}
}
