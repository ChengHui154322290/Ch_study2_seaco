/**
 * 
 */
package com.tp.service.ord.customs.JKF;

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

import com.tp.common.vo.OrderConstant.CancelCustomsOrderStatus;
import com.tp.common.vo.OrderConstant.DeclareCustoms;
import com.tp.common.vo.OrderConstant.PutCustomsStatus;
import com.tp.common.vo.customs.JKFConstant;
import com.tp.common.vo.customs.JKFConstant.JKFBusinessType;
import com.tp.dto.common.FailInfo;
import com.tp.dto.common.ResultInfo;
import com.tp.model.ord.CancelCustomsInfo;
import com.tp.model.ord.PersonalgoodsDeclareInfo;
import com.tp.model.ord.SubOrder;
import com.tp.model.ord.JKF.JkfCancelOrderRequest;
import com.tp.model.ord.JKF.JkfReceiptResult;
import com.tp.model.ord.JKF.JkfCancelOrderRequest.JkfSign;
import com.tp.model.ord.JKF.JkfCancelOrderRequest.ModifyCancel;
import com.tp.model.ord.JKF.JkfReceiptResult.JkfResult;
import com.tp.model.ord.JKF.JkfReceiptResult.JkfResultDetail;
import com.tp.service.ord.ICancelCustomsInfoService;
import com.tp.service.ord.IJKFSoaService;
import com.tp.service.ord.IPersonalgoodsDeclareInfoService;
import com.tp.service.ord.customs.JKF.IJKFCancelOrderService;
import com.tp.util.StringUtil;

/**
 * @author Administrator
 * 海淘自营取消订单
 */
@Service
public class JKFCancelOrderService implements IJKFCancelOrderService{

	private static final Logger logger = LoggerFactory.getLogger(JKFCancelOrderService.class);
	
	//跨境电商平台企业备案编号
	@Value("#{meta['JKF.companyCodeOfKFPlatform']}")
	private String companyCodeOfKFPlatform;
	
	//西客商城自营商家备案编号
	@Value("#{meta['JKF.ecompanyCodeOfKFPlatform']}")
	private String ecompanyCodeOfKFPlatform;
	
	@Value("#{meta['JKF.isEncrypt']}")
	private boolean isEncrypt;
	
	@Autowired
	private IPersonalgoodsDeclareInfoService personalgoodsDeclareInfoService;
	
	@Autowired
	private IJKFSoaService jKFSoaService;
	
	@Autowired
	private ICancelCustomsInfoService cancelCustomsInfoService;
	
	
	/**
	 * 取消海淘订单（向海关申报） 
	 */
	@Override
	public ResultInfo<Boolean> cancelSeaOrder(SubOrder subOrder, String cancelReason) {
		if (StringUtil.isEmpty(cancelReason)) {
			return new ResultInfo<>(new FailInfo("取消原因不能为空"));
		}
		//申报单
		PersonalgoodsDeclareInfo pgInfo = 
				personalgoodsDeclareInfoService.queryUniquePersonalGoodsDeclByOrderCode(subOrder.getOrderCode().toString());
		if (pgInfo == null) {
			logger.error("订单{}个人物品申报单不存在", subOrder.getOrderCode());
			return new ResultInfo<>(new FailInfo("个人物品申报单不存在"));
		}
		//取消记录
		CancelCustomsInfo cancelCustomsInfo = null;
		Map<String, Object> params = new HashMap<>();
		params.put("orderCode", subOrder.getOrderCode());
		List<CancelCustomsInfo> cancelInfos = cancelCustomsInfoService.queryByParamNotEmpty(params);
		if (CollectionUtils.isNotEmpty(cancelInfos)) {
			cancelCustomsInfo = cancelInfos.get(0);
		}else{
			cancelCustomsInfo = new CancelCustomsInfo();
			cancelCustomsInfo.setCustomsCode(DeclareCustoms.JKF.code);
			cancelCustomsInfo.setOrderCode(subOrder.getOrderCode());
			cancelCustomsInfo.setExpressNo(pgInfo.getExpressNo());
			cancelCustomsInfo.setExpressCompanyCode(pgInfo.getCompanyNo());
			cancelCustomsInfo.setSupplierId(subOrder.getSupplierId());
			cancelCustomsInfo.setSupplierName(subOrder.getSupplierName());
			cancelCustomsInfo.setCreateTime(new Date());
			cancelCustomsInfo.setCancelStatus(CancelCustomsOrderStatus.PUSH_FAILED.code);
			cancelCustomsInfoService.insert(cancelCustomsInfo);
		}
		//组装数据
		JkfCancelOrderRequest request = buildRequest(subOrder, pgInfo, cancelReason);
		CancelCustomsInfo cInfo = new CancelCustomsInfo();
		cInfo.setId(cancelCustomsInfo.getId());
		ResultInfo<Boolean> pushResultInfo = null;
		try {
			ResultInfo<JkfReceiptResult> resultInfo = jKFSoaService.cancelOrderDeclare(request, isEncrypt);
			if (!resultInfo.isSuccess()) {
				logger.error("申报取消海淘订单异常:" + resultInfo.getMsg().getDetailMessage());
				cInfo.setCancelStatus(CancelCustomsOrderStatus.PUSH_FAILED.code);
				pushResultInfo = new ResultInfo<>(new FailInfo("申报取消海淘订单异常"));
			}else{
				JkfResult result = resultInfo.getData().getBody().getList().get(0);							
				//更新推送状态
				if (null != result) {			
					StringBuffer stringBuffer = new StringBuffer();
					for (JkfResultDetail detail : result.getResultList()) {
						stringBuffer.append(detail.getResultInfo()); 
					}	
					if(JKFConstant.CheckResult.SUCCESS.result.equals(Integer.valueOf(result.getChkMark()))){
						logger.error("推送取消海淘订单{}成功", subOrder.getOrderCode());
						cInfo.setCancelStatus(CancelCustomsOrderStatus.PUSH_SUCCESS.code);
						pushResultInfo = new ResultInfo<>(Boolean.TRUE);
					}else{
						logger.error("推送取消海淘订单{}失败,结果:{}", subOrder.getOrderCode(), stringBuffer.toString());
						cInfo.setCancelStatus(CancelCustomsOrderStatus.PUSH_FAILED.code);
						pushResultInfo = new ResultInfo<>(new FailInfo(stringBuffer.toString()));
					}
				}else{
					cInfo.setCancelStatus(CancelCustomsOrderStatus.PUSH_FAILED.code);
					pushResultInfo = new ResultInfo<>(new FailInfo("推送取消海淘订单异常")); 
				}	
			}			
		} catch (Exception e) {
			logger.error("推送取消海淘订单异常", e);
			cInfo.setCancelStatus(CancelCustomsOrderStatus.PUSH_FAILED.code);
			pushResultInfo = new ResultInfo<>(new FailInfo("取消海淘订单失败"));
		}
		cancelCustomsInfoService.updateNotNullById(cInfo);
		return pushResultInfo;
	}
	
	/**
	 * 组装数据 
	 */
	private JkfCancelOrderRequest buildRequest(SubOrder subOrder, PersonalgoodsDeclareInfo declareInfo, String cancelReason){
		JkfCancelOrderRequest request = new JkfCancelOrderRequest();
		request.getHead().setBusinessType(JKFBusinessType.MODIFY_CANCEL.type);
		
		//sign
		JkfSign sign = request.getBody().getJkfSign();
		sign.setBusinessNo(subOrder.getOrderCode().toString());
		sign.setBusinessType(JKFBusinessType.MODIFY_CANCEL.type);
		sign.setCompanyCode(companyCodeOfKFPlatform);
		sign.setDeclareType("1");
		
		//数据
		List<ModifyCancel> cancelObjs = request.getBody().getModifyCancelList();
		ModifyCancel cancel = new ModifyCancel();
		cancel.seteCommerceCode(ecompanyCodeOfKFPlatform);
		cancel.seteCompanyCode(companyCodeOfKFPlatform);
		cancel.setSubCarriageNo(declareInfo.getExpressNo());
		cancel.setReason(cancelReason);
		cancelObjs.add(cancel);
		
		return request;
	}
	
}
