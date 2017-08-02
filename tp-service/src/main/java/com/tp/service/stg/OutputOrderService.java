package com.tp.service.stg;

import java.net.MalformedURLException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import javax.xml.rpc.ServiceException;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.tp.common.dao.BaseDao;
import com.tp.common.vo.Constant;
import com.tp.common.vo.StorageConstant;
import com.tp.common.vo.DAOConstant.MYBATIS_SPECIAL_STRING;
import com.tp.common.vo.StorageConstant.InputAndOutputType;
import com.tp.common.vo.StorageConstant.OutputOrderType;
import com.tp.common.vo.StorageConstant.StorageType;
import com.tp.common.vo.stg.BMLStorageConstant.OutputOrderStatus;
import com.tp.dao.stg.InventoryDao;
import com.tp.dao.stg.InventoryDistributeDao;
import com.tp.dao.stg.InventoryOccupyDao;
import com.tp.dao.stg.OutputOrderDao;
import com.tp.dao.stg.OutputOrderDetailDao;
import com.tp.dto.OrderOperatorErrorDTO;
import com.tp.dto.common.FailInfo;
import com.tp.dto.common.ResultInfo;
import com.tp.dto.stg.InventoryDto;
import com.tp.dto.stg.OutputOrderDetailDto;
import com.tp.dto.stg.OutputOrderDto;
import com.tp.dto.stg.ResultOrderDeliverDTO;
import com.tp.dto.stg.BML.RequestOrderInfo;
import com.tp.dto.stg.BML.RequestOrderItem;
import com.tp.dto.stg.BML.ResponseDto;
import com.tp.dto.stg.BML.SkuSyncDto;
import com.tp.model.ord.OrderDelivery;
import com.tp.model.stg.Inventory;
import com.tp.model.stg.InventoryDistribute;
import com.tp.model.stg.InventoryLog;
import com.tp.model.stg.InventoryOccupy;
import com.tp.model.stg.OutputOrder;
import com.tp.model.stg.OutputOrderDetail;
import com.tp.model.stg.Warehouse;
import com.tp.model.stg.vo.feedback.ResponseVO;
import com.tp.mq.RabbitMqProducer;
import com.tp.service.BaseService;
import com.tp.service.mem.IMailService;
import com.tp.service.mem.MailService;
import com.tp.service.stg.IInventoryLogService;
import com.tp.service.stg.IInventoryQueryService;
import com.tp.service.stg.IOutputOrderService;
import com.tp.service.stg.IWarehouseService;
import com.tp.service.stg.client.BMLSoapClient;
import com.tp.service.stg.client.BeanToXMLUtils;
import com.tp.util.StringUtil;

@Service
public class OutputOrderService extends BaseService<OutputOrder> implements IOutputOrderService {

	@Autowired
	private OutputOrderDao outputOrderDao;
	
	@Override
	public BaseDao<OutputOrder> getDao() {
		return outputOrderDao;
	}
	Logger logger = LoggerFactory.getLogger(OutputOrderService.class);

	@Value("#{meta['soa.username']}")
	String customerId;
	
	@Value("#{meta['default.warehouse.code']}")
	private String defaultWarehouseCode;
	
	@Value("#{meta['bml_wh.notify.email']}")
	private String notifyEmail;
	
	@Autowired
	private IMailService mailService;
	
	@Autowired
	private IInventoryOperService inventoryOperService;
	
	@Autowired
	private OutputOrderDetailDao outputOrderDetailDao;
	
	@Autowired
	private IWarehouseService warehouseService;
	
	@Autowired
	private BMLSoapClient bmlSoapClient;
	
	@Autowired
	private InventoryDistributeDao inventoryDistributeDao;
	
	@Autowired
	private IInventoryQueryService inventoryQueryService;
	
	@Autowired
	private InventoryOccupyDao inventoryOccupyDao;
	
	@Autowired
	private InventoryDao inventoryDao;
	
	@Autowired
	private IInventoryLogService inventoryLogService;
	
	@Autowired
	private RabbitMqProducer rabbitMqProducer;
	
	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public ResultInfo<String> deliverOutputOrder(OutputOrderDto orderDto,StorageType type)throws Exception {
		if(null==type){
			return new ResultInfo<String>(new FailInfo("订单类型不能为空"));
		}
		if(type!=StorageType.COMMON){
			//平台商家订单不处理
			return new ResultInfo<String>("发货成功");
		}
		if(null==orderDto){
			return new ResultInfo<String>(new FailInfo("orderDto 为空"));
		}
		//验证参数
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		Validator validator = factory.getValidator();
		Set<ConstraintViolation<OutputOrderDto>> violations = validator.validate(orderDto); 
		if(CollectionUtils.isNotEmpty(violations)){
			for (ConstraintViolation<OutputOrderDto> constraintViolation : violations) {
				return new ResultInfo<String>(new FailInfo(constraintViolation.getPropertyPath()+":"+constraintViolation.getMessage()));
			}
			List<OutputOrderDetailDto> orderDetailDtos =  orderDto.getOrderDetailDtoList();
			for (OutputOrderDetailDto outputOrderDetailDto : orderDetailDtos) {
				Set<ConstraintViolation<OutputOrderDetailDto>> violationDetails = validator.validate(outputOrderDetailDto); 
				if(CollectionUtils.isNotEmpty(violationDetails)){
					for (ConstraintViolation<OutputOrderDetailDto> constraintViolation : violationDetails) {
						return new ResultInfo<String>(new FailInfo(constraintViolation.getPropertyPath()+":"+constraintViolation.getMessage()));
					}
				}
			}
		}
		
		orderDto.setOrderType(OutputOrderType.CM);
		orderDto.setOrderCode(OutputOrderType.CM+orderDto.getOrderCode());
		ResultInfo<String> message = doProcessOutputOrder(orderDto,type);
		logger.info("发送商品订单出库指令结果：{} ",message);
		return message;
	}
	
	/**
	 * 采购退货出库
	 */
	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public ResultInfo<String> returnOutputOrder(OutputOrderDto orderDto)throws Exception {
		if(null==orderDto){
			return new ResultInfo<>(new FailInfo("orderDto 为空"));
		}
		//验证参数
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		Validator validator = factory.getValidator();
		Set<ConstraintViolation<OutputOrderDto>> violations = validator.validate(orderDto); 
		if(CollectionUtils.isNotEmpty(violations)){
			for (ConstraintViolation<OutputOrderDto> constraintViolation : violations) {
				return new ResultInfo<>(new FailInfo(constraintViolation.getPropertyPath()+":"+constraintViolation.getMessage()));
			}
		}
		
		Warehouse warehouseObj = warehouseService.selectByCode(defaultWarehouseCode);
		Long warehouseId = warehouseObj.getId();
		//验证库存是否可以退货
		List<OutputOrderDetailDto> detailDtos = orderDto.getOrderDetailDtoList();
		for (OutputOrderDetailDto outputOrderDetailDto : detailDtos) {
			String sku = outputOrderDetailDto.getSku();
			List<InventoryDto> inventoryDtos = inventoryQueryService.queryAvailableInventory(sku, warehouseId);
			//List<InventoryDto> inventoryDtos = inventoryQueryService.selectAvailableForSaleBySkuAndWhId(sku, warehouseId);
			for (InventoryDto inventoryDto : inventoryDtos) {
				if(!(inventoryDto.getInventory().intValue()>0)&&inventoryDto.getSpId()==0){
					return new ResultInfo<>(new FailInfo("商品 (sku:"+inventoryDto.getSku()+") 库存不足"));
				}
			}
		}
		
		orderDto.setOrderType(OutputOrderType.TT);
		orderDto.setOrderCode(OutputOrderType.TT+orderDto.getOrderCode());
		ResultInfo<String> message = doProcessOutputOrder(orderDto,null);
		logger.info("发送采购订单退货出库指令结果：{} ",message);
		return message;
	}

	
	private ResultInfo<String> doProcessOutputOrder(OutputOrderDto orderDto,StorageType type) {
		Map<String, Object> params = new HashMap<>();
		params.put("orderCode", orderDto.getOrderCode());
		try {
			List<OutputOrder> outputOrderObjs = outputOrderDao.queryByParamNotEmpty(params);
			if(orderDto.getOrderType().getCode().equals(OutputOrderType.CM.getCode())){
				//已发送过指令
				if(CollectionUtils.isNotEmpty(outputOrderObjs)){
					//已经发送过出库指令,重新发送出库指令
					OutputOrder orderDO = outputOrderObjs.get(0);
					params.clear();
					params.put("outputOrderId", orderDO.getId());
					List<OutputOrderDetail> orderDetailDOs = outputOrderDetailDao.queryByParamNotEmpty(params);
					//重新发送出库指令到仓库
					return sendOutputOrder(orderDO, orderDetailDOs);
				}
				ResultInfo<String> result = saveOutputOrderInfo(orderDto,OutputOrderType.CM);
				if(Boolean.TRUE == result.isSuccess()){
					return new ResultInfo<String>("发货成功");
				}
				return result;
			}else if(orderDto.getOrderType().getCode().equals(OutputOrderType.TT.getCode())){
				if(CollectionUtils.isNotEmpty(outputOrderObjs)){
					//已经发送过出库指令,重新发送出库指令
					OutputOrder orderDO = outputOrderObjs.get(0);
					params.clear();
					params.put("outputOrderId", orderDO.getId());
					List<OutputOrderDetail> orderDetailDOs = outputOrderDetailDao.queryByParamNotEmpty(params);
					//重新发送出库指令到仓库
					return sendOutputOrder(orderDO, orderDetailDOs);
				}
				return saveOutputOrderInfo(orderDto,OutputOrderType.TT);
			}else{
				return new ResultInfo<String>(new FailInfo("暂不支持的出库类型"));
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
		} 
		return new ResultInfo<String>(new FailInfo("发送出库单指令异常"));
	}
	
	/**
	 * 保存发送的出库订单信息
	 * @param orderDto
	 * @param thirdpart
	 * @return
	 * @throws RemoteException
	 * @throws MalformedURLException
	 * @throws DAOException
	 * @throws ServiceException
	 */
	private ResultInfo<String> saveOutputOrderInfo(OutputOrderDto orderDto,OutputOrderType orderType)
			throws RemoteException,MalformedURLException,Exception {
		
		Date now = new Date();
		// 记录发送的指令信息日志
		OutputOrder outputOrderObj = new OutputOrder();
		outputOrderObj.setAddress(orderDto.getAddress());
		outputOrderObj.setCity(orderDto.getCity());
		outputOrderObj.setCreateTime(now);
		outputOrderObj.setCustomerName(orderDto.getCustomerName());
		outputOrderObj.setDistrict(orderDto.getDistrict());
		outputOrderObj.setExpectedTime(orderDto.getExpectedTime());
		outputOrderObj.setFreight(orderDto.getFreight());
		outputOrderObj.setIsCashsale(orderDto.getIsCashsale());
		outputOrderObj.setIssuePartyId(orderDto.getIssuePartyId());
		outputOrderObj.setIssuePartyName(orderDto.getIssuePartyName());
		outputOrderObj.setItemValue(orderDto.getItemsValue());
		outputOrderObj.setMobile(orderDto.getMobile());
		outputOrderObj.setName(orderDto.getName());
		outputOrderObj.setOrderCode(orderDto.getOrderCode());
		outputOrderObj.setPayment(orderDto.getPayment());
		outputOrderObj.setPayTime(orderDto.getPayTime());
		outputOrderObj.setPhone(orderDto.getPhone());
		outputOrderObj.setPostCode(orderDto.getPostCode());
		outputOrderObj.setPriority(orderDto.getPriority());
		outputOrderObj.setProv(orderDto.getProv());
		outputOrderObj.setRemark(orderDto.getRemark());
		outputOrderObj.setRequiredTime(orderDto.getRequiredTime());
		outputOrderObj.setServiceCharge(orderDto.getServiceCharge());
		outputOrderObj.setShipping(orderDto.getShipping());
		outputOrderObj.setSystemId(orderDto.getOrderCode());
		outputOrderObj.setWebsite(orderDto.getWebsite());
		outputOrderObj.setOrderType(orderDto.getOrderType().getCode());
		
		List<OutputOrderDetailDto> detailDtos = orderDto.getOrderDetailDtoList();
		
		outputOrderObj.setCustomerId(customerId);
		//获得仓库信息
		Warehouse warehouseObj = warehouseService.selectByCode(defaultWarehouseCode);
		if(null!=warehouseObj){
			outputOrderObj.setWarehouseId(warehouseObj.getId());
			outputOrderObj.setWarehouseCode(warehouseObj.getCode());
		}
		
		outputOrderObj.setStatus(0);
		outputOrderObj.setFailTimes(0);
		outputOrderDao.insert(outputOrderObj);
		List<OutputOrderDetail> detailObjs = new ArrayList<OutputOrderDetail>();
		
		List<InventoryOccupy> inventoryOccupyObjs = null;
		Map<String, Object> params = new HashMap<>();
		if(orderType.getCode().equals(OutputOrderType.CM.getCode())){
			//商品订单
			// 验证商品自营还是商家发货
			String originOrderCode = outputOrderObj.getOrderCode().substring(orderType.getCode().length());
			params.clear();
			params.put("orderNo", originOrderCode);
			inventoryOccupyObjs = inventoryOccupyDao.queryByParamNotEmpty(params);
			if(CollectionUtils.isEmpty(inventoryOccupyObjs)){
				throw new Exception("没有找到订单冻结库存记录，请检查后重试：订单号》》》》"+originOrderCode);
			}
		}
		
		OutputOrderDetail detailObj = null;
		for (OutputOrderDetailDto oodos : detailDtos) {
			detailObj = new OutputOrderDetail();
			detailObj.setCreateTime(now);
			detailObj.setItemCount(oodos.getItemCount());
			detailObj.setItemName(oodos.getItemName());
			detailObj.setItemValue(oodos.getItemValue());
			detailObj.setOutputOrderId(outputOrderObj.getId());
			detailObj.setBatchNo(oodos.getBatchNo());
			detailObj.setSkuCode(oodos.getSku());
			detailObj.setBarcode(oodos.getBarcode());
			if(CollectionUtils.isNotEmpty(inventoryOccupyObjs)){
				for (InventoryOccupy occupyDO : inventoryOccupyObjs) {
					if(occupyDO.getApp().equals(oodos.getApp().getName())
							&&occupyDO.getBizId().equals(oodos.getBizId())
							&&occupyDO.getSku().equals(oodos.getSku())){
						detailObj.setInventoryDistId(occupyDO.getInventoryDistId());
						break;
					}
				}
			}
			detailObjs.add(detailObj);
		}
		outputOrderDetailDao.insertBatch(detailObjs);
		
		return sendOutputOrder(outputOrderObj, detailObjs);
	}


	/**
	 * 将 null值转为空字符串
	 * @param object
	 * @return
	 */
	private Object nullToEmpty(Object object){
		if(null==object){
			return "";
		}
		return object;
	}
	
	@Override
	public ResultInfo<String> cancelOutputOrder(Long orderCodeL){
		//  取消订单
		String orderCode = OutputOrderType.CM.getCode()+orderCodeL.toString();
		Map<String, Object> params = new HashMap<>();
		params.put("orderCode", orderCode);
		try {
			List<OutputOrder> orderDOs = outputOrderDao.queryByParamNotEmpty(params);
			if(CollectionUtils.isEmpty(orderDOs)){
				return new ResultInfo<String>(new FailInfo("取消订单失败,未找到发货订单记录， orderCode:"+orderCode));
			}
			ResponseDto responseDto = bmlSoapClient.searchOrderStatus(orderCode);
			if(OutputOrderStatus.OP90.getCode().equals(responseDto.getCode())){
				//如果仓库已经取消了这个订单，则返回取消成功
				return new ResultInfo<>(responseDto.getDesc());
			}
			// 发送指令
			ResponseVO responseVO = bmlSoapClient.cancelOrderRX(orderCode);
			
			if(responseVO.getSuccess().equalsIgnoreCase("false")){
				return new ResultInfo<String>(new FailInfo(responseVO.getDesc() + responseVO.getCode()));
			}else{
				return new ResultInfo<String>(responseVO.getDesc());
			}
		} catch (Exception e) {
			logger.error("取消订单失败 {} 错误：{}",orderCode,e.getMessage());
			return new ResultInfo<String>(new FailInfo("取消订单失败:"+e.getMessage()+" orderCode:"+orderCode));
		} 
	}
	

	@Override
	public ResultInfo<String> sendOutputOrder(OutputOrder outputOrderObj,
			List<OutputOrderDetail> detailObjs) throws RemoteException,MalformedURLException,Exception  {
		String isCashSale = outputOrderObj.getIsCashsale();//是否需要开票
		if(1 == outputOrderObj.getStatus()){
			return new ResultInfo<String>(new FailInfo("出库指令已发送成功，不要重复发送"));
		}
		
		//发票信息校验
		if(StorageConstant.INVOICE_REQ.equals(isCashSale)
						||StorageConstant.INVOICE_REQSPEC.equals(isCashSale)){
			if(null==outputOrderObj.getItemValue()){
				return new ResultInfo<String>(new FailInfo("需要开发票的订单，订单金额必须要存在"));
			}
		}
		
		Integer failTimes = outputOrderObj.getFailTimes();
		failTimes = (failTimes==null?0:failTimes);
		if(failTimes>=StorageConstant.SEND_ORDER_WMS_MAX_FAIL_TIMES.intValue()){
			return new ResultInfo<String>(new FailInfo("已重试"+StorageConstant.SEND_ORDER_WMS_MAX_FAIL_TIMES.intValue()+"次，不再向仓库发送出库指令"));
		}
		
		List<RequestOrderItem> orderItems = new ArrayList<RequestOrderItem>();
		RequestOrderItem orderItem = null;
		for (OutputOrderDetail oodos : detailObjs) {
			orderItem = new RequestOrderItem();
			orderItem.setItemCount(String.valueOf(oodos.getItemCount()));
			orderItem.setItemName(oodos.getItemName());
			orderItem.setVender(nullToEmpty(oodos.getBatchNo()).toString());
			
			if(null!=oodos.getItemValue()){
				orderItem.setItemValue(String.valueOf(oodos.getItemValue()));
			}else{
				//发票信息校验
				if(StorageConstant.INVOICE_REQ.equals(isCashSale)
								||StorageConstant.INVOICE_REQSPEC.equals(isCashSale)){
					return new ResultInfo<String>(new FailInfo("需要开发票的订单，订单到商品的价格必须要存在"));
				}else{
					orderItem.setItemValue("0");
				}				
			}
			orderItem.setSpuCode(oodos.getBarcode());
			orderItems.add(orderItem);
		}
		//组装向仓库发送指令的数据
		RequestOrderInfo orderInfo = new RequestOrderInfo();
		orderInfo.setCustomerId(outputOrderObj.getCustomerId());
		orderInfo.setCustomerName(nullToEmpty(outputOrderObj.getCustomerName()).toString());
		orderInfo.setAddress(outputOrderObj.getAddress());
		orderInfo.setProv(outputOrderObj.getProv());
		orderInfo.setCity(outputOrderObj.getCity());
		orderInfo.setDistrict(nullToEmpty(outputOrderObj.getDistrict()).toString());
		if(null!=outputOrderObj.getExpectedTime()){
			orderInfo.setExpectedTime(DateFormatUtils.format(outputOrderObj.getExpectedTime(),"yyyy-MM-dd HH:mm:ss"));
		}else{
			orderInfo.setExpectedTime("");
		}
		//运费模板
		orderInfo.setFreight(outputOrderObj.getFreight()==null ? 0:outputOrderObj.getFreight());
		orderInfo.setIsCashsale(nullToEmpty(outputOrderObj.getIsCashsale()).toString());//无需开票/需要开票/需开专票
		orderInfo.setWebsite(nullToEmpty(outputOrderObj.getWebsite()).toString());//发票抬头
		orderInfo.setServiceCharge(outputOrderObj.getServiceCharge()==null ? 0 : outputOrderObj.getServiceCharge());//折扣金额 
		orderInfo.setIssuePartyId(nullToEmpty(outputOrderObj.getIssuePartyId()).toString());
		orderInfo.setIssuePartyName(nullToEmpty(outputOrderObj.getIssuePartyName()).toString());
		orderInfo.setItemsValue(outputOrderObj.getItemValue());
		orderInfo.setMobile(outputOrderObj.getMobile());
		orderInfo.setName(outputOrderObj.getName());
		orderInfo.setOrderCode(outputOrderObj.getOrderCode());
		orderInfo.setPayment(nullToEmpty(outputOrderObj.getPayment()).toString());
		
		
		if(null!=outputOrderObj.getPayTime()){
			orderInfo.setPayTime(DateFormatUtils.format(outputOrderObj.getPayTime(),"yyyy-MM-dd HH:mm:ss"));
		}else{
			orderInfo.setPayTime("");
		}
		orderInfo.setOrderTime(orderInfo.getPayTime());
		orderInfo.setPhone(nullToEmpty(outputOrderObj.getPhone()).toString());
		orderInfo.setPostcode(outputOrderObj.getPostCode());
		orderInfo.setPriority(nullToEmpty(outputOrderObj.getPriority()).toString());
		orderInfo.setRemark(nullToEmpty(outputOrderObj.getRemark()).toString());
		if(null!=outputOrderObj.getRequiredTime()){
			orderInfo.setRequiredTime(DateFormatUtils.format(outputOrderObj.getRequiredTime(),"yyyy-MM-dd HH:mm:ss"));
		}else{
			orderInfo.setRequiredTime("");
		}
		
		
		orderInfo.setShipping(outputOrderObj.getShipping());
		orderInfo.setSystemId(outputOrderObj.getOrderCode());
		orderInfo.setOrderType(outputOrderObj.getOrderType());
		orderInfo.setWarehouseid(outputOrderObj.getWarehouseCode());
		
		orderInfo.setItems(orderItems);
		
		
		//进行sku同步
		SkuSyncDto skuSyncDto = null;
		List<SkuSyncDto> skuSyncDtos = new ArrayList<SkuSyncDto>();
		for (RequestOrderItem product : orderItems) {
			skuSyncDto = new SkuSyncDto();
			skuSyncDto.setName(product.getItemName());
			skuSyncDto.setSkucode(product.getSpuCode());
			skuSyncDto.setDesc(product.getVender());
			skuSyncDto.setALTERNATESKU1("");
			skuSyncDto.setALTERNATESKU2("");
			skuSyncDto.setBrand("");
			skuSyncDtos.add(skuSyncDto);
		}
		Map<Class<?>, String> syncclassAlias = new HashMap<Class<?>, String>();
		syncclassAlias.put(ArrayList.class, "skus");
		syncclassAlias.put(SkuSyncDto.class, "sku");
		/*String xmlSkuSyncDto = BeanToXMLUtils.beansToXml(skuSyncDtos, syncclassAlias);
		logger.info("销售出库订单   {} 开始sku同步 >>>>",orderInfo.getOrderCode());*/
		ResultInfo<String> message = null;
		try {
			/*String syncresult = bmlSoapClient.singleSkuToWms(xmlSkuSyncDto);
			Map<Class<?>, String> syncclasszz = new HashMap<Class<?>, String>();
			syncclasszz.put(ResponseDto.class, "Response");
			ResponseDto syncdto =(ResponseDto) BeanToXMLUtils.xmlToBean(syncresult, syncclasszz);
			logger.info("销售出库订单 {} sku同步完成>>>>result: {}",orderInfo.getOrderCode(),syncdto);*/
			
		
			Map<Class<?>, String> classAlias = new HashMap<Class<?>, String>();
			classAlias.put(RequestOrderInfo.class, "RequestOrderInfo");
			classAlias.put(RequestOrderItem.class, "item");
			String xml = BeanToXMLUtils.beansToXml(orderInfo, classAlias);
			xml = "<RequestOrderList>"+xml+"</RequestOrderList>";
			
			logger.info("订单  {} 开始发送出库指令 >>>>",orderInfo.getOrderCode());
			
			logger.info("发货的参数： {}" ,xml);
			String result  = bmlSoapClient.soToWms(xml);
			
			//解析返回结果
			Map<Class<?>, String> classzz = new HashMap<Class<?>, String>();
			classzz.put(ResponseDto.class, "Response");
			ResponseDto dto =(ResponseDto) BeanToXMLUtils.xmlToBean(result, classzz);
			logger.info("订单 {} 发送出库指令完成>>>>result: {}",orderInfo.getOrderCode(),dto);
			

			if(ResponseDto.SUCCESS.equals(dto.getSuccess())){
				message = new ResultInfo<String>("发送出库指令成功");
				outputOrderObj.setStatus(1);
			}else{
				message = new ResultInfo<String>(new FailInfo("发送出库指令失败"));
				outputOrderObj.setStatus(0);
				failTimes++;
				outputOrderObj.setFailTimes(failTimes);
				if(failTimes==StorageConstant.SEND_ORDER_WMS_MAX_FAIL_TIMES.intValue()){
					// 发送邮件给相关人员
					if(StringUtils.isNotBlank(notifyEmail)){
						String[] emails = notifyEmail.split(",");
						StringBuffer body = new StringBuffer();
						body.append("error message:"+message.toString());
						body.append(MailService.WRAP);
						body.append("销售订单单号："+outputOrderObj.getOrderCode());
						body.append(MailService.WRAP);
						body.append("收件人："+outputOrderObj.getName());
						body.append(MailService.WRAP);
						body.append("收件人手机："+outputOrderObj.getMobile());
						body.append(MailService.WRAP);
						body.append("收货地址："+outputOrderObj.getAddress());
						body.append(MailService.WRAP);
						body.append("仓库："+defaultWarehouseCode);
						body.append(MailService.WRAP);
						body.append(MailService.WRAP);
						body.append("商品信息：>>>>>>>>>>>>>>>>>>>>>>>");
						body.append(MailService.WRAP);
						body.append(MailService.WRAP);
						for (OutputOrderDetail oodos : detailObjs) {
							body.append("商品："+oodos.getItemName());
							body.append(MailService.WRAP);
							body.append("商品条形码："+oodos.getBarcode());
							body.append(MailService.WRAP);
							body.append("商品sku："+oodos.getSkuCode());
							body.append(MailService.WRAP);
							body.append("商品数量："+oodos.getItemCount());
							body.append(MailService.WRAP);
							body.append(MailService.WRAP);
						}
						mailService.batchSend(emails, StorageConstant.MailProperties.MAIL_NOTIFY_TITLE_FG, body.toString());
					}
				}
			}
			outputOrderDao.updateById(outputOrderObj);
		} catch (Exception e) {
			throw new RemoteException(e.getMessage());
		}	
		return message;
	}

	@Transactional(propagation=Propagation.REQUIRED)
	public ResultInfo<Boolean> exWarehouseService(OrderDelivery orderDelivery) throws Exception{
		
		FailInfo failInfo = validate(orderDelivery);
		if(failInfo!=null){
			return new ResultInfo<Boolean>(failInfo);
		}
		ResultInfo<Boolean> resultInfo = inventoryOperService.reduceInventoryForOrderDelivery(orderDelivery.getOrderCode());
		if(!resultInfo.isSuccess()){
			logger.error("{}订单发货库存操作失败,直接发消息到订单,由订单验证", orderDelivery.getOrderCode());
		}
		rabbitMqProducer.sendP2PMessage(StorageConstant.STORAGE_SALESORDER_OUTPUT_TASK_QUEUE_P2P, orderDelivery);			
		return new ResultInfo<Boolean>(Boolean.TRUE);
//		
//		
//		FailInfo failInfo = validate(orderDelivery);
//		if(failInfo!=null){
//			return new ResultInfo<Boolean>(failInfo);
//		}
//		String orderNo = orderDelivery.getOrderCode().toString();
//		// 验证商品自营还是商家发货
//		Map<String, Object> params = new HashMap<>();
//		params.put("orderNo", orderNo);
//		try{
//			List<InventoryOccupy> inventoryOccupyObjs = inventoryOccupyDao.queryByParamNotEmpty(params);
//			if(CollectionUtils.isEmpty(inventoryOccupyObjs)){
//				logger.error("{} 没有找到订单冻结库存记录,直接发消息到订单，由订单验证",params);
//				rabbitMqProducer.sendP2PMessage(StorageConstant.STORAGE_SALESORDER_OUTPUT_TASK_QUEUE_P2P, orderDelivery);
//				return new ResultInfo<Boolean>(new FailInfo("没有找到订单冻结记录，直接对符合条件的订单发货"));
//			}
//			// 扣减相关库存信息
//			// 第三方商家订单直接扣减库存 不发送出库指令 不包含发送指令信息
//			List<Long> occupyIds = new ArrayList<Long>();
//			for (InventoryOccupy occupy : inventoryOccupyObjs) {
//				occupyIds.add(occupy.getId());
//				
//				String app = occupy.getApp();
//				String bizId = occupy.getBizId();
//				String sku = occupy.getSku();
//				
//				logger.info("商家订单扣减分配库存：bizId = {} sku = {} order ={} inventory={}",bizId,sku,orderNo,occupy.getInventory());
//				inventoryDistributeDao.reduceOccupyDistribute(app, bizId, sku, occupy.getInventory());
//				
//				InventoryDistribute distributeDO = new InventoryDistribute();
//				distributeDO.setApp(app);
//				distributeDO.setBizId(bizId);
//				distributeDO.setSku(sku);
//				params.clear();
//				params.put("app", app);
//				params.put("bizId", bizId);
//				params.put("sku", sku);
//				List<InventoryDistribute> distributeObjs = inventoryDistributeDao.queryByParamNotEmpty(params);
//				if(CollectionUtils.isEmpty(distributeObjs)){
//					continue;
//				}
//				distributeDO = distributeObjs.get(0);
//				
//				Long inventoryId = distributeDO.getInventoryId();
//				logger.info("商家订单扣减现货库存：bizId = {} sku = {} order ={} inventory={}",bizId,sku,orderNo,occupy.getInventory());
//				inventoryDao.reduceInventoryAndOccupy(inventoryId, occupy.getInventory());
//				//记录日志
//				Warehouse warehouseObj = warehouseService.queryById(distributeDO.getWarehouseId());
//				InventoryLog inventoryLogObj = new InventoryLog();
//				inventoryLogObj.setWhCode(warehouseObj.getCode());
//				inventoryLogObj.setSku(sku);
//				inventoryLogObj.setSkuCount(occupy.getInventory());
//				
//				if(warehouseObj.getSpId().longValue()==0){
//					inventoryLogObj.setType(InputAndOutputType.SO.getCode());
//				}else{
//					inventoryLogObj.setType(InputAndOutputType.PXS.getCode());
//				}
//				inventoryLogObj.setDistrictId(warehouseObj.getDistrictId());
//				inventoryLogObj.setSupplierId(warehouseObj.getSpId());
//				inventoryLogObj.setWarehouseId(warehouseObj.getId());
//				//获得最新库存
//				Inventory inventoryObj = inventoryDao.queryById(inventoryId);
//				inventoryLogObj.setInventory(inventoryObj.getInventory());
//				inventoryLogService.insert(inventoryLogObj);
//			}
//			if(occupyIds.size()>0) {
//				//删除库存冻结记录
//				params.clear();
//				params.put(MYBATIS_SPECIAL_STRING.COLUMNS.name(), "id in (" + StringUtils.join(occupyIds, Constant.SPLIT_SIGN.COMMA) + ")");
//				inventoryOccupyDao.deleteByParamNotEmpty(params);
//			}
//
//			logger.info("nofity salesorder system has output OrderNo：" + orderNo);
//			// 发送到消息系统
//			rabbitMqProducer.sendP2PMessage(StorageConstant.STORAGE_SALESORDER_OUTPUT_TASK_QUEUE_P2P, orderDelivery);
//			logger.info("nofity salesorder system has output successed OrderNo：" + orderNo);
//			
//			return new ResultInfo<Boolean>(Boolean.TRUE);
//		}catch(Exception e){
//			logger.error("出库失败 > {} ",e.getMessage());
//			return new ResultInfo<Boolean>(new FailInfo(e));
//		}
	}
	

	@Override
	public ResultOrderDeliverDTO batchExWarehouseService(List<OrderDelivery> orderDeliverys){
		ResultOrderDeliverDTO deliverDTO = new ResultOrderDeliverDTO();
		Integer errorSize = 0;
		List<OrderDelivery> errorDataList = new ArrayList<OrderDelivery>();
		List<OrderOperatorErrorDTO> orderOperatorErrorList = new ArrayList<OrderOperatorErrorDTO>();
		for (OrderDelivery orderDelivery : orderDeliverys) {
			boolean hasError = false;
			String errorMsg = "";
			try {
				ResultInfo<Boolean> message = exWarehouseService(orderDelivery);
				if(!message.success){
					hasError = true;
					errorMsg = message.getMsg().getMessage();
				}
			} catch (Exception e) {
				logger.error("商品出库出现异常 order = {} error = {}", orderDelivery.getOrderCode(), e.getMessage());
				hasError = true;
				errorMsg = e.getMessage();
			}
			
			if(hasError){
				errorSize++;
				errorDataList.add(orderDelivery);
				OrderOperatorErrorDTO errorDTO = new OrderOperatorErrorDTO(orderDelivery.getOrderCode(), null, errorMsg);
				orderOperatorErrorList.add(errorDTO);
			}
		}
		deliverDTO.setErrorDataList(errorDataList);
		deliverDTO.setErrorSize(errorSize);
		deliverDTO.setOrderOperatorErrorList(orderOperatorErrorList);
		return deliverDTO;
	}

	
	@Override
	public List<OutputOrder> selectFailOutputOrder(int maxTime, int limitSize) {
		return outputOrderDao.selectFailOutputOrder(maxTime,limitSize);
	}

	@Override
	public List<OutputOrderDetail> selectOuputorderDetailByOrderId(Long outputOrderId) {
		Map<String, Object> params = new HashMap<>();
		params.put("outputOrderId", outputOrderId);
		return outputOrderDetailDao.queryByParamNotEmpty(params);
	}

	@Override
	public List<OutputOrder> selectOutputOrderByOrderCode(String orderCode) {
		Map<String, Object> params = new HashMap<>();
		params.put("orderCode", orderCode);
		return outputOrderDao.queryByParamNotEmpty(params);
	}

	public FailInfo validate(OrderDelivery orderDelivery){
		if(null==orderDelivery){
			return new FailInfo(201100,"发货信息不存在");
		}
		if(null==orderDelivery.getOrderCode()){
			return new FailInfo(201101,"订单编号不存在");
		}
		if(StringUtil.isBlank(orderDelivery.getCompanyName())){
			return new FailInfo(201102,"快递公司名称为空");
		}
		if(StringUtil.isBlank(orderDelivery.getCompanyId())){
			return new FailInfo(201103,"快递编号为空");
		}
		if(StringUtil.isBlank(orderDelivery.getPackageNo())){
			return new FailInfo(201104,"运单号为空");
		}
		return null;
	}
	
	
}
