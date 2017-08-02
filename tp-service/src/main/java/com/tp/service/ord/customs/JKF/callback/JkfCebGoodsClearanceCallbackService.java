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

import com.alibaba.fastjson.JSONObject;
import com.tp.common.vo.Constant.AUTHOR_TYPE;
import com.tp.common.vo.OrderConstant.ClearanceStatus;
import com.tp.common.vo.OrderConstant.DeclareCustoms;
import com.tp.common.vo.OrderConstant.PutCustomsStatus;
import com.tp.common.vo.OrderConstant.PutCustomsType;
import com.tp.common.vo.customs.MQCustomsConstant;
import com.tp.common.vo.customs.JKFConstant.CebClearanceStatus;
import com.tp.common.vo.customs.JKFConstant.JKFFeedbackType;
import com.tp.common.vo.customs.JKFConstant.JKFResultError;
import com.tp.common.vo.ord.LogTypeConstant;
import com.tp.dto.common.ResultInfo;
import com.tp.model.ord.CustomsClearanceLog;
import com.tp.model.ord.OrderDelivery;
import com.tp.model.ord.OrderStatusLog;
import com.tp.model.ord.PersonalgoodsDeclareInfo;
import com.tp.model.ord.PersonalgoodsStatusLog;
import com.tp.model.ord.SubOrder;
import com.tp.model.ord.JKF.JkfBaseDO;
import com.tp.model.ord.JKF.JkfCallbackResponse;
import com.tp.model.ord.ceb.CebGoodsResponse;
import com.tp.model.ord.ceb.CebGoodsResponse.InventoryReturn;
import com.tp.mq.RabbitMqProducer;
import com.tp.mq.exception.MqClientException;
import com.tp.service.ord.ICustomsClearanceLogService;
import com.tp.service.ord.IOrderStatusLogService;
import com.tp.service.ord.IPersonalgoodsDeclareInfoService;
import com.tp.service.ord.IPersonalgoodsStatusLogService;
import com.tp.service.ord.ISubOrderService;
import com.tp.service.ord.customs.JKF.callback.IJKFClearanceCallbackProcessService;
import com.tp.service.stg.IOutputOrderService;
import com.tp.util.StringUtil;

/**
 * @author Administrator
 *
 */
@Service
public class JkfCebGoodsClearanceCallbackService implements IJKFClearanceCallbackProcessService{

	private static final Logger logger = LoggerFactory.getLogger(JkfCebGoodsClearanceCallbackService.class);
	
	@Autowired
	private IPersonalgoodsDeclareInfoService personalgoodsDeclareInfoService;
	
	@Autowired
	private ISubOrderService subOrderService;
	
	@Autowired
	private IPersonalgoodsStatusLogService personalgoodsStatusLogService;
	
	@Autowired
	private IOrderStatusLogService orderStatusLogService;
	
	@Autowired
	private ICustomsClearanceLogService customsClearanceLogService;
	
	@Autowired
	private IOutputOrderService outputOrderService;
	
	@Autowired
	private RabbitMqProducer rabbitMqProducer;
	
	@Override
	public boolean checkReceiptType(JKFFeedbackType type) {
		if (JKFFeedbackType.CUSTOMS_CEB_CALLBACK_GOODS == type){
			return true;
		}
		return false;
	}

	@Override
	public JkfCallbackResponse processCallback(JkfBaseDO receiptResult) throws Exception {
		CebGoodsResponse response = (CebGoodsResponse)receiptResult;
		if (CollectionUtils.isEmpty(response.getInventoryReturn())){
			logger.error("[CEB_GOODS_CUSTOMS_CALLBACK]总署清单回执数据为空");
			return new JkfCallbackResponse(JKFResultError.INVALID_CONTENT);
		}
		for(InventoryReturn cebReturn : response.getInventoryReturn()){
			processSingleCebGoodsCallback(cebReturn);
		}
		return new JkfCallbackResponse();
	}

	private void processSingleCebGoodsCallback(InventoryReturn goodsReturn) throws Exception{
		goodsReturn = preProcessSingleCebGoodsCallback(goodsReturn);
		if (goodsReturn == null) return ;
		String preNo = goodsReturn.getCopNo();
		PersonalgoodsDeclareInfo pgInfo = 
				personalgoodsDeclareInfoService.queryPersonalgoodsDeclareInfoByPreEntryNo(preNo);
		//查询相关子订单
		Long orderCode = Long.valueOf(pgInfo.getOrderCode());
		SubOrder subOrder = subOrderService.selectOneByCode(orderCode);
		
		personalgoodsStatusLogService.insert(createPersonalgoodsStatusLog(goodsReturn, pgInfo));
		customsClearanceLogService.insert(createCustomsClearanceLog(goodsReturn, subOrder.getOrderCode()));
		
		if (false == checkClearanceStatusWithAuditStatus(subOrder)) return;
		logger.info("[CLEARANCE_CALLBACK][{}]海关审单回执：{}", subOrder.getOrderCode(), goodsReturn.getReturnInfo());		
		ClearanceStatus status = CebClearanceStatus.checkClearanceAudit(Integer.valueOf(goodsReturn.getReturnStatus()));	
		if (status == null) return;
	
		updateSubOrderByAuditResult(status, subOrder);
		updatePersonalgoodsDeclareInfo(pgInfo, goodsReturn);
		orderStatusLogService.insert(createOrderStatusLog(subOrder, status.getDesc()));
		if (ClearanceStatus.AUDIT_SUCCESS == status) {
			logger.info("[CEB_CUSTOMS_CALLBACK]总署已放行, 推送订单到仓库系统");
			processClearanceSuccess(subOrder, pgInfo);
		}
	}
	/**
	 * 清关通过
	 * 直邮订单：直接发货
	 * 保税订单：推送订单到保税仓
	 * @throws Exception 
	 */
	private void processClearanceSuccess(SubOrder subOrder, PersonalgoodsDeclareInfo pgInfo) throws Exception{
		if (pgInfo.getImportType() != null && pgInfo.getImportType() == 0){ //直邮订单清关之后直接发货
			processDirectmailClearanceSuccess(pgInfo);
		}else{
			processBondedClearanceSuccess(subOrder);	
		}		
	}	
	private void processDirectmailClearanceSuccess(PersonalgoodsDeclareInfo pgInfo) throws Exception{
		ResultInfo<Boolean> resultInfo = outputOrderService.exWarehouseService(createOrderDelivery(pgInfo));
		if (!resultInfo.isSuccess()){
			logger.error("[CLEARANCE_CALLBACK][{}]直邮订单发货失败:", resultInfo.getMsg().getMessage());
		}
	}
	private void processBondedClearanceSuccess(SubOrder subOrder) throws MqClientException{
		rabbitMqProducer.sendP2PMessage(MQCustomsConstant.SEAORDER_STOCKOUT_P2P_QUEUE, subOrder);
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
	
	private CustomsClearanceLog createCustomsClearanceLog(InventoryReturn goodsReturn, Long orderCode){
		Integer result = Integer.valueOf(goodsReturn.getReturnStatus());
		String message = goodsReturn.getReturnInfo();
		if (StringUtil.isEmpty(message)){
			message = CebClearanceStatus.getDescByCode(result);
		}
		message = "【总署】" + message;
		Integer declareResult = 2;
		if (CebClearanceStatus.isAccept(result)){
			declareResult = 1;
		}else if(CebClearanceStatus.isReject(result)){
			declareResult = 0;
		}else if (CebClearanceStatus.isPushFail(result)){
			declareResult = 0;
		}else if (CebClearanceStatus.isPushSuccess(result)){
			declareResult = 1;
		}
		CustomsClearanceLog log = new CustomsClearanceLog();
		log.setOrderCode(orderCode);
		log.setResult(declareResult);
		log.setResultDesc(message);
		log.setType(PutCustomsType.CLEARANCE.getIndex());
		log.setCustomsCode(DeclareCustoms.CEB.code);
		log.setCreateTime(new Date());
		return log;
	}
	
	private void updatePersonalgoodsDeclareInfo(PersonalgoodsDeclareInfo pgInfo, InventoryReturn goodsReturn){
		if(StringUtil.isNotEmpty(pgInfo.getPersonalgoodsNo())) return;
		PersonalgoodsDeclareInfo nDecl = new PersonalgoodsDeclareInfo();
		nDecl.setId(pgInfo.getId());
		nDecl.setPersonalgoodsNo(goodsReturn.getInvtNo());
		personalgoodsDeclareInfoService.updateNotNullById(nDecl);
	}
	
	private void updateSubOrderByAuditResult(ClearanceStatus status, SubOrder subOrder){
		//更新状态
		SubOrder so = new SubOrder();
		so.setId(subOrder.getId());
		so.setClearanceStatus(status.getCode());
		if (ClearanceStatus.PUT_FAILED == status){
			so.setPutPersonalgoodsStatus(PutCustomsStatus.FAIL.getCode());
		}else if (ClearanceStatus.AUDIT_FAILED == status){ //清单审核失败
			so.setPutPersonalgoodsStatus(PutCustomsStatus.AUDIT_FAIL.getCode());
		}
		so.setUpdateTime(new Date());
		subOrderService.updateNotNullById(so);		
	}

	private PersonalgoodsStatusLog createPersonalgoodsStatusLog(InventoryReturn goodsReturn, PersonalgoodsDeclareInfo pgInfo){
		PersonalgoodsStatusLog personalgoodsStatusLog = new PersonalgoodsStatusLog();
		personalgoodsStatusLog.setOrderCode(pgInfo.getOrderCode());
		personalgoodsStatusLog.setDeclareNo(pgInfo.getDeclareNo());
		personalgoodsStatusLog.setCustomsCode(DeclareCustoms.CEB.code);
		personalgoodsStatusLog.setCustomsDeclareNo(goodsReturn.getCopNo());
		personalgoodsStatusLog.setType(0); //0是回调
		personalgoodsStatusLog.setResult(goodsReturn.getReturnStatus());
		personalgoodsStatusLog.setResultDesc(CebClearanceStatus.getDescByCode(Integer.valueOf(goodsReturn.getReturnStatus())));
		personalgoodsStatusLog.setResultDetail(goodsReturn.getReturnInfo());
		personalgoodsStatusLog.setResData(JSONObject.toJSONString(goodsReturn));
		personalgoodsStatusLog.setCreateTime(new Date());
		return personalgoodsStatusLog;
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
	
	private InventoryReturn preProcessSingleCebGoodsCallback(InventoryReturn goodsReturn){
		boolean validate = validateReturn(goodsReturn);
		if (false == validate) return null;
		return goodsReturn;
	}
	
	private boolean validateReturn(InventoryReturn goodsReturn){
		String preEntryNo = goodsReturn.getCopNo();
		if (StringUtil.isEmpty(preEntryNo)){
			logger.error("总署回执预录入编号为空");
			return false;
		}
		PersonalgoodsDeclareInfo info = 
				personalgoodsDeclareInfoService.queryPersonalgoodsDeclareInfoByPreEntryNo(preEntryNo);
		if (null == info){
			logger.error("[CUSTOMS_CALLBACK][{}]清关单不存在");
			return false;
		}
		Long orderCode = info.getOrderCode();
		SubOrder subOrder = subOrderService.selectOneByCode(orderCode);	
		if (null == subOrder) {
			logger.error("[CUSTOMS_CALLBACK][{}]订单不存在", orderCode);
			return false;
		}
		return true;
	}
}
