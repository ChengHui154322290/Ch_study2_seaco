/**
 * 
 */
package com.tp.service.ord.customs;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.tp.common.vo.OrderConstant;
import com.tp.common.vo.Constant.SPLIT_SIGN;
import com.tp.common.vo.DAOConstant.MYBATIS_SPECIAL_STRING;
import com.tp.common.vo.OrderConstant.ClearanceStatus;
import com.tp.model.ord.SubOrder;
import com.tp.service.mem.IMailService;
import com.tp.service.mem.MailService;
import com.tp.service.ord.ISubOrderService;
import com.tp.service.ord.customs.ISeaCustomsDeliveryMonitorService;
import com.tp.util.StringUtil;

/**
 * @author Administrator
 * 订单申报
 */
@Service
public class SeaCustomsDeliveryMonitorService implements ISeaCustomsDeliveryMonitorService{

	private static final Logger logger = LoggerFactory.getLogger(SeaCustomsDeliveryMonitorService.class);
		
	private static final String REPORT_TITLE = "全球购订单清关报告";
	
	private static final String REPORT_TEMPLAT = "西客商城有%d个订单申报海关失败,订单号为：%s。请及时处理。后台地址：http://backend.51seaco.com";
	
	@Autowired
	private ISubOrderService subOrderService;
	
	@Autowired
	private IMailService mailService;
	
	@Value("#{meta['XG.Clearance.ReportMail']}")
	private String reportMail;
	
	@Override
	public void sendCustomsClearanceReport() {
		Map<String, Object> params =  new HashMap<>();
		params.put("type", OrderConstant.OrderType.COMMON_SEA.code);
		params.put("orderStatus", OrderConstant.ORDER_STATUS.DELIVERY.code);
		List<Integer> clearanceStatuss = new ArrayList<>(Arrays.asList(ClearanceStatus.PUT_FAILED.code, ClearanceStatus.AUDIT_FAILED.code));
		params.put(MYBATIS_SPECIAL_STRING.COLUMNS.name(), 
				"clearance_status in (" + StringUtil.join(clearanceStatuss, SPLIT_SIGN.COMMA) + ")");
		List<SubOrder> subOrders = subOrderService.queryByParam(params);
		if (CollectionUtils.isEmpty(subOrders)) return;
		
		//生成报告
		String clearanceReport = createClearanceReport(subOrders);
		logger.info("清关报告内容：{}, {}", clearanceReport, reportMail);
		//发送报告
		sendClearanceReport(clearanceReport);
	}
	
	private String createClearanceReport(List<SubOrder> subOrders){
		Integer failCount = subOrders.size();
		List<Long> subOrderCodes = new ArrayList<>();
		for(SubOrder subOrder : subOrders){
			subOrderCodes.add(subOrder.getOrderCode());
		}
		String failOrderCodes = StringUtil.join(subOrderCodes, MailService.WRAP);
		return String.format(REPORT_TEMPLAT, failCount, failOrderCodes);
	}
	
	private void sendClearanceReport(String report){
		if (StringUtil.isEmpty(report)) return;
		String[] emails = reportMail.split(",");
		mailService.batchSend(emails, REPORT_TITLE, report);
	}

}
