package com.tp.proxy.stg;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.vo.StorageConstant;
import com.tp.common.vo.StorageConstant.OutputOrderType;
import com.tp.dto.stg.OrderInvoiceDTO;
import com.tp.model.stg.OutputOrderInvoice;
import com.tp.model.stg.vo.feedback.InvoiceVO;
import com.tp.model.stg.vo.feedback.InvoicesVO;
import com.tp.mq.RabbitMqProducer;
import com.tp.mq.exception.MqClientException;
import com.tp.service.stg.IOutputOrderInvoiceService;

/**
 * 
 * @author szy
 *
 */
@Service
public class InvoiceProxy {
	
	//@Autowired
	RabbitMqProducer rabbitMqProducer;

	@Autowired                      
	private IOutputOrderInvoiceService outputOrderInvoiceService;
	
	Logger logger = LoggerFactory.getLogger(InvoiceProxy.class);
	
	public void insert(InvoicesVO invoicesVO) {
		try {
			logger.info("invoicesvo");
			if(null!=invoicesVO){
				InvoiceVO invoiceVO = invoicesVO.getInvoice(); 
				String orderNo = invoiceVO.getOrderNo().substring(OutputOrderType.CM.getCode().length());
				invoiceVO.setOrderNo(orderNo);
				if(null!=invoiceVO){
					Map<String, Object> params = new HashMap<>();
					params.put("orderNo", orderNo);
					List<OutputOrderInvoice> list = outputOrderInvoiceService.queryByParamNotEmpty(params);
					if(CollectionUtils.isNotEmpty(list)){
						logger.debug("亲,发票已经存在,系统暂不支持处理重复的发票信息,传入发票信息:{}",invoiceVO.toString());
					}else{
						OutputOrderInvoice outputOrderInvoiceDO = new OutputOrderInvoice();
						outputOrderInvoiceDO.setAmount(invoiceVO.getAmount());
						outputOrderInvoiceDO.setOrderNo(invoiceVO.getOrderNo());
						outputOrderInvoiceDO.setInvoiceCode(invoiceVO.getInvoiceCode());
						outputOrderInvoiceDO.setInvoiceNo(invoiceVO.getInvoiceNo());
						outputOrderInvoiceDO.setInvoiceType(invoiceVO.getInvoiceType());
						outputOrderInvoiceDO.setTitle(invoiceVO.getTitle());
						outputOrderInvoiceDO.setInvoiceTime(invoiceVO.getInvoiceTime());
						outputOrderInvoiceService.insert(outputOrderInvoiceDO);
						OrderInvoiceDTO orderInvoiceDTO = new OrderInvoiceDTO();
						BeanUtils.copyProperties(invoiceVO, orderInvoiceDTO);
						rabbitMqProducer.sendP2PMessage(StorageConstant.STORAGE_SALESORDER_INVOICE_TASK_QUEUE_P2P, orderInvoiceDTO);
					}
				}
			}
		} catch (MqClientException e) {
			logger.error(e.getMessage());
		}
		
	}
}
