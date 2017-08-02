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
import com.tp.common.vo.StorageConstant.OutputOrderType;
import com.tp.dto.common.FailInfo;
import com.tp.dto.common.ResultInfo;
import com.tp.dto.stg.WarehouseOrderProductRewriteDTO;
import com.tp.dto.stg.WarehouseOrderRewriteDTO;
import com.tp.model.bse.ExpressCodeInfo;
import com.tp.model.bse.ExpressInfo;
import com.tp.model.ord.OrderDelivery;
import com.tp.model.stg.InventoryLog;
import com.tp.model.stg.OutputBack;
import com.tp.model.stg.OutputOrder;
import com.tp.model.stg.OutputOrderDetail;
import com.tp.model.stg.Warehouse;
import com.tp.model.stg.vo.feedback.OutputBackVO;
import com.tp.model.stg.vo.feedback.OutputBacksVO;
import com.tp.model.stg.vo.feedback.SkuVO;
import com.tp.mq.RabbitMqProducer;
import com.tp.service.bse.IExpressCodeInfoService;
import com.tp.service.bse.IExpressInfoService;
import com.tp.service.stg.IInventoryLogService;
import com.tp.service.stg.IOutputOrderService;
import com.tp.service.stg.ISoToWmsFeedBackService;
import com.tp.service.stg.IWarehouseService;

/**
 * 出库反馈AO
 * 
 * @author
 *
 */
@Service
public class SoToWmsFeedBackProxy {

	@Autowired
	private ISoToWmsFeedBackService soToWmsFeedBackService;
	
	@Autowired
	private IOutputOrderService outputOrderService;
	
	@Autowired
	private IWarehouseService warehouseService;
	
	@Autowired
	private IInventoryLogService inventoryLogService;
	
	Logger logger = LoggerFactory.getLogger(SoToWmsFeedBackProxy.class);
	
	@Autowired
	private IExpressCodeInfoService expressCodeInfoService;
	
	@Autowired
	private IExpressInfoService expressInfoService;
	
	//@Autowired
	RabbitMqProducer rabbitMqProducer;

	//@Value("${default.warehouse.code}")
	private String default_warehouse_code;
	
	/**
	 * 出库反馈 1.增加 ASNDetailsVO 2.通知订单系统发货物流信息
	 * 
	 * @param ASNDetailsVO
	 */
	public ResultInfo<String> operateOutputBack(OutputBacksVO outputBacks) {
		
		Map<String, Object> params = new HashMap<>();
		
		String orderCode = outputBacks.getOutputBack().getOrderNo();
		OutputOrderType outputOrderType = null;
		OutputOrderType orderType = soToWmsFeedBackService.selectOutputTypeByOrderCode(outputBacks.getOutputBack().getOrderNo());
		
		OutputBackVO outputBack = outputBacks.getOutputBack();
		String carrierId = outputBack.getCarrierID();
		ExpressInfo expressInfo = null;
		if(StringUtils.isNotBlank(carrierId)){
			params.clear();
			params.put("whExpressCode", carrierId);
			params.put("whCode", default_warehouse_code);
			ExpressCodeInfo expressCodeInfo = expressCodeInfoService.queryUniqueByParams(params);
			if (null != expressCodeInfo) {
				expressInfo = expressInfoService.queryById(expressCodeInfo.getExpressId());
			}
		}
		
		if(null!=orderType&&orderType.getCode().equals(OutputOrderType.CM.getCode())){
			//销售订单，通知订单系统商品已发货
			outputOrderType = OutputOrderType.CM;
			
			String orderNo = outputBack.getOrderNo();
			orderNo = orderNo.substring(OutputOrderType.CM.getCode().length());
			
			OrderDelivery orderDeliver = new OrderDelivery();
			orderDeliver.setOrderCode(Long.valueOf(orderNo));
			orderDeliver.setPackageNo(outputBack.getShipNo()); // 运单号
			orderDeliver.setDeliveryTime(outputBack.getShipTime());
			orderDeliver.setWeight(outputBack.getWeight());
			if(null	!=	expressInfo){
				orderDeliver.setCompanyId(expressInfo.getCode());
				orderDeliver.setCompanyName(expressInfo.getName());
			}
			
			try {
				logger.info("执行商品出库 OrderNo：{}" ,orderCode);
				ResultInfo<Boolean> message = outputOrderService.exWarehouseService(orderDeliver);
				logger.info("执行商品出库 结果 OrderNo：{} {} " ,orderCode, message);
			} catch (Exception e) {
				logger.error("执行商品出库 失败 OrderNo：{} 错误信息：{}" , outputBack.getOrderNo(), e.getMessage());
				return new  ResultInfo<String>(new FailInfo("服务器内部异常"));
			}
		}else if(null!=orderType&&orderType.getCode().equals(OutputOrderType.TT.getCode())){
			//采购退货出库，通知采购系统，商品已经出库
			outputOrderType = OutputOrderType.TT;
			// 退货出库反馈
			int beginIndex = OutputOrderType.TT.getCode().length();
			orderCode = orderCode.substring(beginIndex);
			WarehouseOrderRewriteDTO dto = new WarehouseOrderRewriteDTO();
			dto.setOrderType(OutputOrderType.TT.getCode());
			try {
				dto.setWarehouseOrderId(Long.parseLong(orderCode));
				List<SkuVO> skuList = outputBacks.getOutputBack().getSend();
				List<WarehouseOrderProductRewriteDTO> orderProductRewriteDTOs = new ArrayList<WarehouseOrderProductRewriteDTO>();
				WarehouseOrderProductRewriteDTO orderProductRewriteDTO = null;
				for (SkuVO sku : skuList) {
					orderProductRewriteDTO = new WarehouseOrderProductRewriteDTO();
					orderProductRewriteDTO.setBatchNumber(sku.getVendor());
					logger.info("出库反馈完成，通知供应商系统：{} 批次号 ：{}", outputBacks.getOutputBack().getOrderNo(),sku.getVendor());
					orderProductRewriteDTO.setSkuCode(sku.getSkuCode());
					orderProductRewriteDTO.setStorageCount(Long.valueOf(sku.getSkuNum()));
					orderProductRewriteDTOs.add(orderProductRewriteDTO);
				}
				dto.setProductList(orderProductRewriteDTOs);
				logger.info("出库反馈完成，通知供应商系统：{}", outputBacks.getOutputBack().getOrderNo());
				// 发送到消息系统
				rabbitMqProducer.sendP2PMessage(StorageConstant.STORAGE_SUPPLIER_OUTPUT_QUEUE_P2P, dto);
			} catch (Exception e) {
				logger.error("调用采购退货反馈接口异常：单号 {} 错误信息：{}",orderCode,e.getMessage());
				return new ResultInfo<String>(new FailInfo("服务器内部异常"));
			}			
		}else{
			logger.error("不支持的反馈类型：单号 {} 类型 {}",orderCode,orderType);
			return new ResultInfo<String>(new FailInfo("服务器内部异常"));
		}
		
		try {
			// 记录库存出库日志
			Long districtId = null;
			Long warehouseId = null;
			String defaultWarehouseCode = outputBacks.getOutputBack().getBgNo();
			if(StringUtils.isNotBlank(defaultWarehouseCode)){
				List<String> codes = new ArrayList<String>();
				codes.add(defaultWarehouseCode);
				params.clear();
				params.put("code", defaultWarehouseCode);
				List<Warehouse> warehouseDOs = warehouseService.queryByParamNotEmpty(params);
				if(CollectionUtils.isNotEmpty(warehouseDOs)){
					districtId = warehouseDOs.get(0).getDistrictId();
					warehouseId = warehouseDOs.get(0).getId();
				}
			}
			String orderTypeInfo = null;
			if(null!=orderType&&orderType.getCode().equals(OutputOrderType.CM.getCode())){
				orderTypeInfo = InputAndOutputType.SO.getCode();
			}else if(null!=orderType&&orderType.getCode().equals(OutputOrderType.TT.getCode())){
				orderTypeInfo = InputAndOutputType.RO.getCode();
			}
				
			OutputBackVO outputBackVO = outputBacks.getOutputBack();
			List<SkuVO> detailVOs = outputBackVO.getSend();
			List<InventoryLog> inventoryObjs = new ArrayList<InventoryLog>();
			InventoryLog inventoryLogObj = null;
			for (SkuVO detailVO : detailVOs) {
				inventoryLogObj = new InventoryLog();
				inventoryLogObj.setWhCode(default_warehouse_code);
				inventoryLogObj.setOrderCode(outputBackVO.getOrderNo());
				inventoryLogObj.setShipCode(outputBackVO.getShipNo());
				inventoryLogObj.setSku(detailVO.getSkuCode());
				inventoryLogObj.setSkuCount((int)detailVO.getSkuNum());
				inventoryLogObj.setBarcode(detailVO.getBarcode());
				if(StringUtils.isNotBlank(orderTypeInfo)){
					inventoryLogObj.setType(orderTypeInfo);
				}
				inventoryLogObj.setBatchNo(detailVO.getVendor());
				if(null!=expressInfo){
					inventoryLogObj.setExpressId(expressInfo.getId());
					inventoryLogObj.setExpressName(expressInfo.getName());
				}
				inventoryLogObj.setDistrictId(districtId);
				inventoryLogObj.setWarehouseId(warehouseId);
				inventoryLogObj.setCreateTime(new Date());
				inventoryObjs.add(inventoryLogObj);
			}
			logger.info("出库反馈完成，保存出库流水：{} type {}",outputBacks.getOutputBack().getOrderNo(),(outputOrderType==null?"":outputOrderType.getCode()));
			inventoryLogService.insertBatch(inventoryObjs);
		} catch (Exception e) {
			logger.error("出库反馈完成，保存出库流水出错：{} type：{}",outputBacks.getOutputBack().getOrderNo(),(outputOrderType==null?"":outputOrderType.getCode()));
			logger.error("出库反馈完成，保存出库流水出错 错误：{}",e.getMessage());
		}
		try {
			soToWmsFeedBackService.saveBackInfo(outputBacks);
		} catch (Exception e) {
			logger.error("保存反馈信息失敗" + e.getMessage(), e);
			return new ResultInfo<String>(new FailInfo("服务器内部异常"));
		}
		return new ResultInfo<String>("反馈成功");
	}

	public OutputBack selectByOrderCode(String orderCode) {
		if(StringUtils.isBlank(orderCode)){
			return null;
		}	
		return soToWmsFeedBackService.selectByOrderCode(orderCode);
	}

	public OutputOrder selectOutputOrderByOrderCode(String orderCode) {
		if(StringUtils.isBlank(orderCode)){
			return null;
		}
		List<OutputOrder> dos = outputOrderService.selectOutputOrderByOrderCode(orderCode);
		if(CollectionUtils.isEmpty(dos)){
			return null;
		}		
		return dos.get(0);
	}
	/**
	 * 根据入库单id获得入库明细中 barcode和sku信息
	 * @param outputOrderId
	 * @return
	 * key为barcode （条形码） 值为sku
	 */
	public Map<String, String> selectSkuByBarcodeFromInputorderDetails(Long outputOrderId) {
		List<OutputOrderDetail> detailObjs = outputOrderService.selectOuputorderDetailByOrderId(outputOrderId);
		if(CollectionUtils.isEmpty(detailObjs)){
			return new HashMap<String, String>();
		}
		Map<String, String> skuBarcodeMap = new HashMap<String, String>();
		for (OutputOrderDetail outputOrderDetailObj : detailObjs) {
			skuBarcodeMap.put(outputOrderDetailObj.getBarcode(), outputOrderDetailObj.getSkuCode());
		}
		return skuBarcodeMap;
	}
}
