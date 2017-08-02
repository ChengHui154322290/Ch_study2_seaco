package com.tp.backend.controller.customerService;

import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanMap;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tp.backend.controller.AbstractBaseController;
import com.tp.common.util.OrderUtils;
import com.tp.common.vo.PageInfo;
import com.tp.common.vo.ord.RefundConstant;
import com.tp.dto.common.FailInfo;
import com.tp.dto.common.ResultInfo;
import com.tp.dto.ord.remote.OrderLine4ExcelDTO;
import com.tp.model.mem.MemberInfo;
import com.tp.model.ord.CancelItem;
import com.tp.model.ord.OffsetInfo;
import com.tp.model.ord.RefundInfo;
import com.tp.model.ord.RefundLog;
import com.tp.model.ord.RejectInfo;
import com.tp.model.ord.RejectItem;
import com.tp.model.ord.SubOrder;
import com.tp.model.pay.PaymentGateway;
import com.tp.model.pay.PaymentInfo;
import com.tp.model.pay.RefundPayinfo;
import com.tp.proxy.mem.MemberInfoProxy;
import com.tp.proxy.ord.CancelInfoProxy;
import com.tp.proxy.ord.OffsetInfoProxy;
import com.tp.proxy.ord.OrderInfoProxy;
import com.tp.proxy.ord.RefundInfoProxy;
import com.tp.proxy.ord.RejectInfoProxy;
import com.tp.proxy.ord.SubOrderProxy;
import com.tp.proxy.pay.PaymentInfoProxy;
import com.tp.query.ord.RefundQuery;
import com.tp.util.BeanUtil;
import com.tp.util.DateUtil;

/**
 * 退款管理
 * @author szy
 *
 */
@Controller
@RequestMapping("/customerservice/refund/")
public class RefundManageController extends AbstractBaseController {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	@Autowired
	private RefundInfoProxy refundInfoProxy;
	@Autowired
	private RejectInfoProxy rejectInfoProxy;
	@Autowired
	private CancelInfoProxy cancelInfoProxy;
	@Autowired
	private OffsetInfoProxy offsetInfoProxy;
	@Autowired
	private MemberInfoProxy memberInfoProxy;
	@Autowired
	private OrderInfoProxy orderInfoProxy;
	@Autowired
	private SubOrderProxy subOrderProxy;
	@Autowired
	private PaymentInfoProxy paymentInfoProxy;
	@Value(value="${alipay.refund.url}")
	private String alipayRefundUrl;
	
	@RequestMapping(value={"list","pagelist"})
	public void list(Model model,RefundQuery refundQuery){
		 PageInfo<RefundInfo> refundInfoPageInfo = refundInfoProxy.queryByQuery(refundQuery);
		 
		 List<PaymentGateway> paymentGatewayList = paymentInfoProxy.queryPaymentGatewayList();
		 
		 if(refundInfoPageInfo != null && CollectionUtils.isNotEmpty(refundInfoPageInfo.getRows())){
			Double totalAmount = 0d;
			for(RefundInfo refundInfo : refundInfoPageInfo.getRows()){
				totalAmount += (refundInfo.getRefundAmount() == null ? 0d : refundInfo.getRefundAmount());
			}
			model.addAttribute("totalAmount", totalAmount);
		 }
		 
		 model.addAttribute("refundInfoPageInfo", refundInfoPageInfo);
		 model.addAttribute("query", refundQuery);
		 model.addAttribute("refundStatusList", RefundConstant.REFUND_STATUS.values());
		 model.addAttribute("refundTypes", RefundConstant.REFUND_TYPE.values());
		 model.addAttribute("paymentGatewayList", paymentGatewayList);
	}
	
	@RequestMapping(value={"financeaudit","audit"},method=RequestMethod.POST)
	@ResponseBody
	public ResultInfo<?> audit(Model model,Long refundId,Boolean success,  Boolean isAuditComplete){
		if(null==refundId){
			return new ResultInfo<>(new FailInfo("没有参数"));
		}
		RefundInfo refundInfo = refundInfoProxy.queryById(refundId).getData();
		
		if( isAuditComplete != null && isAuditComplete == true){
			return refundInfoProxy.audit(refundInfo.getRefundCode(), getUserName(), success, true);			
		}else{			
			return refundInfoProxy.audit(refundInfo.getRefundCode(), getUserName(), success, false);
		}
		
	}

	@RequestMapping(value={"auditcomplete"},method=RequestMethod.GET)
	public void auditCompleteShow(Model model,Long refundId){
		model.addAttribute("auditcomplete",true);
		show(model,refundId);
	}
	
	@RequestMapping(value={"audit"},method=RequestMethod.GET)
	public void auditShow(Model model, Long refundId){
		model.addAttribute("audit",true);
		show(model,refundId);
	}
	@RequestMapping("show")
	public void show(Model model,Long refundId){
		if(null==refundId){
			return;
		}
		RefundInfo refundInfo = refundInfoProxy.queryById(refundId).getData();
		List<RefundLog> logList = refundInfoProxy.findRefundLogByRefundCode(refundInfo.getRefundCode());
		if(RefundConstant.REFUND_TYPE.REPEAT_PAYMENT.code.intValue() == refundInfo.getRefundType().intValue()){
			model.addAttribute("refundInfo", refundInfo);
			model.addAttribute("errorMessage", "重复支付无详细信息");
			return;
		}
		SubOrder subOrder = subOrderProxy.findSubOrderByCode(refundInfo.getOrderCode());
		
		SubOrder subOrderDto = new SubOrder();
		BeanUtils.copyProperties(subOrder, subOrderDto);
		MemberInfo user = memberInfoProxy.queryById(subOrder.getMemberId()).getData();
		RejectInfo rejectInfo = rejectInfoProxy.queryRejectByRefundNo(refundInfo.getRefundCode());
		RefundPayinfo refundPayInfo = paymentInfoProxy.queryRefundPayinfoByRefundNo(refundInfo.getRefundCode());
		PaymentInfo paymentInfo = null;
		if(refundPayInfo==null){
			Long orderNo = subOrder.getOrderCode();
			if(OrderUtils.isSeaOrder(subOrder.getType())){
				orderNo = subOrder.getOrderCode();
			}
			paymentInfo = paymentInfoProxy.queryPaymentInfoByOrderNo(orderNo);
		}else{
			paymentInfo = refundPayInfo.getPaymentInfo();
		}
		
		if(null!=rejectInfo){
			OffsetInfo OffsetInfo = offsetInfoProxy.queryByCode(rejectInfo.getOffsetCode());
			List<RejectItem> rejectItemList = rejectInfo.getRejectItemList();
			model.addAttribute("OffsetInfo", OffsetInfo);
			model.addAttribute("itemList", rejectItemList);
			BigDecimal amount = BigDecimal.ZERO;
			for(RejectItem item:rejectItemList){
				amount = amount.add(BigDecimal.valueOf(item.getItemUnitPrice()).multiply(BigDecimal.valueOf(item.getItemQuantity()))).setScale(2, BigDecimal.ROUND_HALF_UP);
			}
			model.addAttribute("itemAmount", amount.doubleValue());
		}else{
			List<CancelItem> cancelItemList = cancelInfoProxy.queryCancelItemList(refundInfo.getOrderCode());
			model.addAttribute("itemList", cancelItemList);
			model.addAttribute("itemAmount", refundInfo.getRefundAmount());
		}
		
		model.addAttribute("refundInfo", refundInfo);
		model.addAttribute("logList", logList);
		model.addAttribute("subOrder", subOrderDto);
		model.addAttribute("userObj", user);
		model.addAttribute("rejectInfo", rejectInfo);
		model.addAttribute("refundPayInfo", refundPayInfo);
		model.addAttribute("paymentInfo", paymentInfo);
	}
	@RequestMapping("refundbatch")
	@ResponseBody
	public ResultInfo<?> refundBatch(String[] refundCodeGroup){
//		Map<String, String> gatewayMap = new HashMap<String, String>();
//		List<PaymentGateway> paymentGatewayList = paymentInfoProxy.queryPaymentGatewayList();
//		for(PaymentGateway paymentGateway : paymentGatewayList){
//			gatewayMap.put(String.valueOf(paymentGateway.getGatewayId()), paymentGateway.getGatewayCode());
//		}
//		StringBuilder refundNos = new StringBuilder();
//		
//		for(String refundNoAndGatewayId : refundCodeGroup){
//			String refundNo = refundNoAndGatewayId.split("-")[0];
//			ResultInfo<?> returnData=refundInfoProxy.audit(Long.parseLong(refundNo), getUserName(), Boolean.TRUE,alipayRefundUrl);
//			if(!returnData.success){
//				return returnData;
//			}
//		}
//		for(String refundNoAndGatewayId : refundCodeGroup){
//			String refundNo = refundNoAndGatewayId.split("-")[0];
//			String gateway = refundNoAndGatewayId.split("-")[1];
//			if(!"alipayDirect".equals(gatewayMap.get(gateway))){
//				ResultInfo<Boolean> msg = paymentInfoProxy.refund(gatewayMap.get(gateway), refundNo);
//				if(!msg.success){
//					refundInfoProxy.operateAfterRefund(Long.parseLong(refundNo), Boolean.FALSE);
//				}
//				else {
//					if("alipayInternational".equals(gatewayMap.get(gateway)))
//						refundInfoProxy.operateAfterRefund(Long.parseLong(refundNo), Boolean.TRUE);
//				}
//			}
//			else{
//				refundNos.append(refundNo+"-");
//			}
//		}
//		return StringUtils.isEmpty(refundNos.toString()) ? new ResultInfo<>(new FailInfo()) : new ResultInfo<>(alipayRefundUrl + "?refundNos=" + refundNos.toString());
		
		
		Map<String, String> gatewayMap = new HashMap<String, String>();
		List<PaymentGateway> paymentGatewayList = paymentInfoProxy.queryPaymentGatewayList();
		for(PaymentGateway paymentGatewayDO : paymentGatewayList){
			gatewayMap.put(String.valueOf(paymentGatewayDO.getGatewayId()), paymentGatewayDO.getGatewayCode());
		}
//		StringBuilder refundNos = new StringBuilder();
		
		for(String refundNoAndGatewayId : refundCodeGroup){
			String refundNo = refundNoAndGatewayId.split("-")[0];
			ResultInfo<?> returnData;
			try {
				returnData = refundInfoProxy.audit(Long.valueOf(refundNo), getUserName(), true, false);
			} catch (Exception e) {
				logger.error(e.getMessage(),e);
				return new ResultInfo<>(new FailInfo());
			}
		}
		return new ResultInfo<>("成功");
	}
	
	@RequestMapping("/exporttemplate")
	public void exportTemplate(Model model,RefundQuery refundQuery, HttpServletResponse response) throws Exception{ 
		response.setHeader("Content-disposition", "attachment; filename=refundlist.xls");
        response.setContentType("application/x-download");
		try {
			refundQuery.setPageSize(10000);
			PageInfo<RefundInfo> dataList = refundInfoProxy.queryByQuery(refundQuery);
			String templatePath = "/WEB-INF/classes/template/refundlist.xls";
			String fileName = "refundlist_" + DateUtil.format(new Date(), "yyyyMMddHHmmss") + ".xls";
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("list",dataList.getRows());
			super.exportXLS(map, templatePath, fileName,response);
		} catch (Exception e) {
			logger.error("退款单导出异常", e);
		}
	}
	
	private void outPutList(List<RefundInfo> refundList, OutputStream fout){
        HSSFWorkbook wb = new HSSFWorkbook();  
        HSSFSheet sheet = wb.createSheet("退款单"+new Random(10000));  
        HSSFRow row = sheet.createRow(0);  
        HSSFCellStyle style = wb.createCellStyle();  
        style.setAlignment(HSSFCellStyle.ALIGN_LEFT);
        String[] cellNames = new String[]{"订单编号","退款编号","退款类型","商品编号","商品名称","退货数量","退款金额","创建时间","退款状态","支付方式"};
        for(int i=0;i<cellNames.length;i++){
        	 HSSFCell cell = row.createCell(i);
        	 cell.setCellValue(cellNames[i]);  
             cell.setCellStyle(style); 
        } 
        if(CollectionUtils.isNotEmpty(refundList)){
	        for (int i = 0; i < refundList.size(); i++){  
	            row = sheet.createRow(i + 1);  
	            RefundInfo refundInfo =refundList.get(i);
	            row.createCell(0).setCellValue(refundInfo.getOrderCode());
	            row.createCell(1).setCellValue(refundInfo.getRefundCode());  
	            row.createCell(2).setCellValue(refundInfo.getZhRefundType());
	            if(CollectionUtils.isNotEmpty(refundInfo.getItemList())){
	            	String goodNos = "",goodNames = "",refundAmount = " ";
	            	for(Object obj : refundInfo.getItemList()){
	            		if(obj instanceof CancelItem){
	            			CancelItem cancelItem = (CancelItem) obj;
	            			goodNos += cancelItem.getItemSkuCode();
	            			goodNames += cancelItem.getItemName() + " ";
	            			refundAmount += cancelItem.getItemRefundQuantity();
	            		}
	            		if(obj instanceof RejectItem){
	            			RejectItem cancelItem = (RejectItem) obj;
	            			goodNos += cancelItem.getItemSkuCode() + " ";
	            			goodNames += cancelItem.getItemName() + " ";
	            			refundAmount += cancelItem.getItemRefundQuantity() + " ";
	            		}
	            	}
	            	row.createCell(3).setCellValue(goodNos);  
		            row.createCell(4).setCellValue(goodNames); 
		            row.createCell(5).setCellValue(refundAmount);
	            }
	            row.createCell(6).setCellValue(refundInfo.getRefundAmount());
	            row.createCell(7).setCellValue(refundInfo.getCreateTime());
	            row.createCell(8).setCellValue(refundInfo.getZhRefundStatus());
	            row.createCell(9).setCellValue(refundInfo.getGatewayName());
	        } 
        }
        try{  
            wb.write(fout);  
            fout.flush();
            fout.close();  
        }  
        catch (Exception e)  
        {  
        	logger.error(e.getMessage(), e);
            e.printStackTrace();  
        } 
	}
}
