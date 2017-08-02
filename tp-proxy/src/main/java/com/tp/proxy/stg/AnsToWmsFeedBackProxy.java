package com.tp.proxy.stg;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.vo.StorageConstant;
import com.tp.common.vo.StorageConstant.InputAndOutputType;
import com.tp.common.vo.stg.BMLStorageConstant.InputOrderType;
import com.tp.dto.stg.WarehouseOrderProductRewriteDTO;
import com.tp.dto.stg.WarehouseOrderRewriteDTO;
import com.tp.model.stg.InputBack;
import com.tp.model.stg.InputOrder;
import com.tp.model.stg.InputOrderDetail;
import com.tp.model.stg.InventoryLog;
import com.tp.model.stg.Warehouse;
import com.tp.model.stg.vo.feedback.ASNDetailsVO;
import com.tp.model.stg.vo.feedback.ASNsVO;
import com.tp.model.stg.vo.feedback.DetailVO;
import com.tp.mq.RabbitMqProducer;
import com.tp.service.stg.IAnsToWmsFeedBackService;
import com.tp.service.stg.IInputOrderService;
import com.tp.service.stg.IInventoryLogService;
import com.tp.service.stg.IWarehouseService;

/**
 * 入库反馈AO
 * 
 * @author
 *
 */
@Service
public class AnsToWmsFeedBackProxy {

	@Autowired
	private IAnsToWmsFeedBackService ansToWmsFeedBackService;
	
	@Autowired
	private IInputOrderService inputOrderService;
	
	@Autowired
	private IInventoryLogService inventoryLogService;
	
	@Autowired
	private IWarehouseService warehouseService;
	
	//@Value("${default.warehouse.code}")
	private String defaultWarehouseCode;
	
	//@Autowired
	RabbitMqProducer rabbitMqProducer;
	
	Logger logger = LoggerFactory.getLogger(AnsToWmsFeedBackProxy.class);
	/**
	 * 入库反馈 增加 ASNDetailsVO
	 * 
	 * @param ASNDetailsVO
	 */
	public void insert(ASNDetailsVO asnDetails) {
		
		try {
			ansToWmsFeedBackService.insert(asnDetails);
		} catch (Exception e1) {
			logger.error("入库单反馈失败" + e1.getMessage(), e1);
		}
		// 入库反馈
		String orderCode = asnDetails.getASNs().getCustmorOrderNo();
		int beginIndex = InputOrderType.FG.getCode().length();
		orderCode = orderCode.substring(beginIndex);
		WarehouseOrderRewriteDTO dto = new WarehouseOrderRewriteDTO();
		dto.setOrderType(InputOrderType.FG.getCode());
		try {
			dto.setWarehouseOrderId(Long.parseLong(orderCode));
			List<DetailVO> detailVOs = asnDetails.getASNs().getDetails();
			List<WarehouseOrderProductRewriteDTO> orderProductRewriteDTOs = new ArrayList<WarehouseOrderProductRewriteDTO>();
			WarehouseOrderProductRewriteDTO orderProductRewriteDTO = null;
			for (DetailVO detailVO : detailVOs) {
				orderProductRewriteDTO = new WarehouseOrderProductRewriteDTO();
				orderProductRewriteDTO.setBatchNumber(detailVO.getLotatt07());
				orderProductRewriteDTO.setSkuCode(detailVO.getSkuCode());
				orderProductRewriteDTO.setStorageCount((long)detailVO.getReceivedQty());
				orderProductRewriteDTOs.add(orderProductRewriteDTO);
			}
			dto.setProductList(orderProductRewriteDTOs);
			logger.info("入库反馈完成，回调供应商系统：{}",asnDetails.getASNs().getCustmorOrderNo());
			// 发送到消息系统
			rabbitMqProducer.sendP2PMessage(StorageConstant.STORAGE_SUPPLIER_INPUT_QUEUE_P2P, dto);
		} catch (Exception e) {
			logger.error("调用采购单入库反馈接口异常：单号 {} 错误信息：{}",asnDetails.getASNs().getCustmorOrderNo() ,e.getMessage());
		}
		
		//  存储出入库流水
		try {
			Long districtId = null;
			Long warehouseId = null;
			if(StringUtils.isNotBlank(defaultWarehouseCode)){
				Map<String, Object> params = new HashMap<>();
				params.put("code", defaultWarehouseCode);
				List<Warehouse> warehouseDOs = warehouseService.queryByParamNotEmpty(params);
				if(CollectionUtils.isNotEmpty(warehouseDOs)){
					districtId = warehouseDOs.get(0).getDistrictId();
					warehouseId = warehouseDOs.get(0).getId();
				}
			}
			List<DetailVO> detailVOs = asnDetails.getASNs().getDetails();
			ASNsVO asNsVO = asnDetails.getASNs();
			List<InventoryLog> inventoryDOs = new ArrayList<InventoryLog>();
			InventoryLog inventoryLogDO = null;
			for (DetailVO detailVO : detailVOs) {
				inventoryLogDO = new InventoryLog();
				inventoryLogDO.setWhCode(asNsVO.getASNNo());
				inventoryLogDO.setOrderCode(asNsVO.getCustmorOrderNo());
				inventoryLogDO.setSku(detailVO.getSkuCode());
				inventoryLogDO.setSkuCount((int)detailVO.getReceivedQty());
				inventoryLogDO.setType(InputAndOutputType.CO.getCode());
				inventoryLogDO.setDistrictId(districtId);
				inventoryLogDO.setWarehouseId(warehouseId);
				inventoryLogDO.setBatchNo(detailVO.getLotatt07());
				inventoryLogDO.setWarehouseId(warehouseId);
				inventoryLogDO.setCreateTime(new Date());
				inventoryLogDO.setBarcode(detailVO.getBarcode());
				inventoryDOs.add(inventoryLogDO);
			}
			logger.info("入库反馈完成，保存入库流水：{}",asnDetails.getASNs().getCustmorOrderNo());
			inventoryLogService.insertBatch(inventoryDOs);
		} catch (Exception e) {
			logger.info("入库反馈完成，保存入库流水出错：{} 错误：{}",asnDetails.getASNs().getCustmorOrderNo(),e.getMessage());
		}				
	}

	public InputBack selectByOrderCode(String orderCode) {
		if(StringUtils.isEmpty(orderCode)){
			return null;
		}
		
		return ansToWmsFeedBackService.selectByOrderCode(orderCode);
	}

	/**
	 * 根据入库单id获得入库明细中 barcode和sku信息
	 * @param inputOrderId
	 * @return
	 * key为barcode （条形码） 值为sku
	 */
	public Map<String, String> selectSkuByBarcodeFromInputorderDetails(Long inputOrderId){
		List<InputOrderDetail> detailDOs = inputOrderService.selectOrderDetailByOrderId(inputOrderId);
		if(CollectionUtils.isEmpty(detailDOs)){
			return new HashMap<String, String>();
		}
		Map<String, String> skuBarcodeMap = new HashMap<String, String>();
		for (InputOrderDetail inputOrderDetailDO : detailDOs) {
			skuBarcodeMap.put(inputOrderDetailDO.getBarcode(), inputOrderDetailDO.getSkuCode());
		}
		return skuBarcodeMap;
	}
	
	public InputOrder selectInputOrderByOrderCode(String orderCode) {
		if(StringUtils.isEmpty(orderCode)){
			return null;
		}
		//验证是否有入库单
		List<InputOrder> inputOrderDOs = inputOrderService.selectInputOrderByOrderCode(orderCode);
		if(CollectionUtils.isEmpty(inputOrderDOs)){
			return null;
		}
		return inputOrderDOs.get(0);
	}
}
