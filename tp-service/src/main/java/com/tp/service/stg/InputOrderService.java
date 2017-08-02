package com.tp.service.stg;

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
import com.tp.common.vo.StorageConstant;
import com.tp.common.vo.DAOConstant.MYBATIS_SPECIAL_STRING;
import com.tp.common.vo.stg.BMLStorageConstant.InputOrderType;
import com.tp.dao.stg.InputOrderDao;
import com.tp.dao.stg.InputOrderDetailDao;
import com.tp.dto.common.FailInfo;
import com.tp.dto.common.ResultInfo;
import com.tp.dto.stg.InputOrderDetailDto;
import com.tp.dto.stg.InputOrderDto;
import com.tp.dto.stg.BML.ProductInfo;
import com.tp.dto.stg.BML.RequestPurchaseInfo;
import com.tp.dto.stg.BML.ResponseDto;
import com.tp.dto.stg.BML.SkuSyncDto;
import com.tp.model.stg.InputOrder;
import com.tp.model.stg.InputOrderDetail;
import com.tp.model.stg.Warehouse;
import com.tp.service.BaseService;
import com.tp.service.mem.IMailService;
import com.tp.service.stg.IInputOrderService;
import com.tp.service.stg.IWarehouseService;
import com.tp.service.stg.client.BMLSoapClient;
import com.tp.service.stg.client.BeanToXMLUtils;

@Service
public class InputOrderService extends BaseService<InputOrder> implements IInputOrderService {

	@Value("#{meta['soa.username']}")
	String customerId;

	@Value("#{meta['default.warehouse.code']}")
	private String defaultWarehouseCode;

	@Value("#{meta['bml_wh.notify.email']}")
	private String notifyEmail;

	Logger logger = LoggerFactory.getLogger(OutputOrderService.class);

	@Autowired
	private IWarehouseService warehouseService;

	@Autowired
	private InputOrderDao inputOrderDao;

	@Autowired
	private InputOrderDetailDao inputOrderDetailDao;

	@Autowired
	private BMLSoapClient bMLSoapClient;

	@Autowired
	private IMailService mailService;

	@Transactional(propagation = Propagation.REQUIRED)
	public ResultInfo<String> sendInputOrder(InputOrderDto inputOrderDto) throws Exception {
		if (null == inputOrderDto) {
			return new ResultInfo<String>(new FailInfo("inputOrderDto 为空" + " errorcode=001"));
		}
		// 验证参数
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		Validator validator = factory.getValidator();
		Set<ConstraintViolation<InputOrderDto>> violations = validator.validate(inputOrderDto);
		if (CollectionUtils.isNotEmpty(violations)) {
			for (ConstraintViolation<InputOrderDto> constraintViolation : violations) {
				return new ResultInfo<String>(new FailInfo(constraintViolation.getPropertyPath() + ":"
						+ constraintViolation.getMessage() + " errorcode=001"));
			}
			List<InputOrderDetailDto> orderDetailDtos = inputOrderDto.getProducts();
			for (InputOrderDetailDto outputOrderDetailDto : orderDetailDtos) {
				Set<ConstraintViolation<InputOrderDetailDto>> violationDetails = validator
						.validate(outputOrderDetailDto);
				if (CollectionUtils.isNotEmpty(violationDetails)) {
					for (ConstraintViolation<InputOrderDetailDto> constraintViolation : violationDetails) {
						return new ResultInfo<String>(new FailInfo(constraintViolation.getPropertyPath() + ":"
								+ constraintViolation.getMessage() + " errorcode=001"));
					}
				}
			}
		}
		
		String orderCode = InputOrderType.FG.getCode() + inputOrderDto.getOrderCode();
		// 根据单号获得是否已经发送过入库单
		Map<String, Object> params = new HashMap<>();
		params.put("orderCode", orderCode);
		InputOrder inputOrder = this.queryUniqueByParams(params);
		if (inputOrder != null) {
			params.clear();
			params.put("inputOrderId", inputOrder.getId());
			List<InputOrderDetail> inputOrderDetails = inputOrderDetailDao.queryByParamNotEmpty(params);
			return sendInputOrderToWms(inputOrder, inputOrderDetails);
		}

		// 发送入库预约
		Date now = new Date();
		inputOrder = new InputOrder();
		inputOrder.setOrderCode(orderCode);
		inputOrder.setBz(inputOrderDto.getBz());
		inputOrder.setCreateTime(now);
		inputOrder.setCustomerId(customerId);
		inputOrder.setDhrq(inputOrderDto.getDhrq());
		if (inputOrder.getDhrq() == null) {
			inputOrder.setDhrq(now);
		}
		inputOrder.setType(inputOrderDto.getType());

		params.clear();
		params.put("id", inputOrderDto.getWarehouseId());
		// 获得仓库信息
		Warehouse warehouse = warehouseService.queryUniqueByParams(params);
		inputOrder.setWarehouseId(warehouse.getId());
		inputOrder.setWarehouseCode(warehouse.getCode());

		inputOrder.setZdr(inputOrderDto.getZdr());
		inputOrder.setZdrq(inputOrderDto.getZdrq());
		if (inputOrder.getZdrq() == null ) {
			inputOrder.setZdrq(now);
		}
		inputOrder.setStatus(0);
		inputOrder.setFailTimes(0);
		insert(inputOrder);

		List<InputOrderDetailDto> inputOrderDetailDtoList = inputOrderDto.getProducts();
		InputOrderDetail inputOrderDetail = null;
		List<InputOrderDetail> inputOrderDetailList = new ArrayList<InputOrderDetail>();
		for (InputOrderDetailDto inputOrderDetailDto : inputOrderDetailDtoList) {
			inputOrderDetail = new InputOrderDetail();
			inputOrderDetail.setInputOrderId(inputOrder.getId());
			inputOrderDetail.setItemCount(inputOrderDetailDto.getItemCount());
			inputOrderDetail.setItemName(inputOrderDetailDto.getItemName());
			inputOrderDetail.setItemValue(inputOrderDetailDto.getItemValue());
			inputOrderDetail.setRemark(inputOrderDetailDto.getRemark());
			inputOrderDetail.setSkuCode(inputOrderDetailDto.getSku());
			inputOrderDetail.setBarcode(inputOrderDetailDto.getBarcode());
			if( inputOrderDetail.getItemBrandName() == null ){
				inputOrderDetail.setItemBrandName("");
			}
			inputOrderDetailList.add(inputOrderDetail);
		}
		inputOrderDetailDao.batchInsert(inputOrderDetailList);
		return sendInputOrderToWms(inputOrder, inputOrderDetailList);
	}

	/**
	 * 向仓库发送入库指令
	 * 
	 * @param orderDo
	 * @param detailDOs
	 * @return
	 * @throws Exception 
	 */
	public ResultInfo<String> sendInputOrderToWms(InputOrder inputOrder, List<InputOrderDetail> inputOrderDetailList) throws Exception {
		// ResultInfo<String> message = new ResultInfo<String>(new
		// FailInfo("发送入库指令失败"));

		if (1 == inputOrder.getStatus()) {
			return new ResultInfo<String>(new FailInfo("入库指令已发送成功，不要重复发送"));
		}

		Integer failTimes = inputOrder.getFailTimes();
		failTimes = (failTimes == null ? 0 : failTimes);
		if (failTimes >= StorageConstant.SEND_ORDER_WMS_MAX_FAIL_TIMES.intValue()) {
			return new ResultInfo<String>(
					new FailInfo("已重试" + StorageConstant.SEND_ORDER_WMS_MAX_FAIL_TIMES.intValue() + "次，不再向仓库发送入库指令"));
		}

		RequestPurchaseInfo purchaseInfo = new RequestPurchaseInfo();
		purchaseInfo.setBZ(nullToEmpty(inputOrder.getBz()).toString());
		purchaseInfo.setCustomerId(customerId);
		if (null != inputOrder.getDhrq()) {
			purchaseInfo.setDHRQ(DateFormatUtils.format(inputOrder.getDhrq(), "yyyy-MM-hh HH:mm:ss"));
		}

		purchaseInfo.setOrderCode(inputOrder.getOrderCode());
		purchaseInfo.setType(inputOrder.getType());
		purchaseInfo.setWarehouseid(inputOrder.getWarehouseCode());
		purchaseInfo.setZDR(inputOrder.getZdr());
		if (inputOrder.getZdrq() != null) {
			purchaseInfo.setZDRQ(DateFormatUtils.format(inputOrder.getZdrq(), "yyyy-MM-hh HH:mm:ss"));
		}

		ProductInfo productInfo = null;
		List<ProductInfo> products = new ArrayList<ProductInfo>();
		for (InputOrderDetail inputOrderDetailE : inputOrderDetailList) {
			productInfo = new ProductInfo();
			productInfo.setItemCount(String.valueOf(inputOrderDetailE.getItemCount()));
			productInfo.setItemName(inputOrderDetailE.getItemName());
			if (null != inputOrderDetailE.getItemValue()) {
				productInfo.setItemValue(String.valueOf(inputOrderDetailE.getItemValue()));
			} else {
				productInfo.setItemValue("0");
			}
			productInfo.setRemark(nullToEmpty(inputOrderDetailE.getRemark()).toString());
			productInfo.setSpuCode(inputOrderDetailE.getBarcode());
			products.add(productInfo);
		}
		purchaseInfo.setProducts(products);

		// 进行sku同步
		SkuSyncDto skuSyncDto = null;
		List<SkuSyncDto> skuSyncDtos = new ArrayList<SkuSyncDto>();
		for (ProductInfo product : products) {
			skuSyncDto = new SkuSyncDto();
			skuSyncDto.setName(product.getItemName());
			skuSyncDto.setSkucode(product.getSpuCode());
			skuSyncDto.setDesc(product.getRemark());
			skuSyncDto.setALTERNATESKU1("");
			skuSyncDto.setALTERNATESKU2("");
			skuSyncDto.setBrand("");
			skuSyncDtos.add(skuSyncDto);
		}
		Map<Class<?>, String> classAlias = new HashMap<Class<?>, String>();
		classAlias.put(ArrayList.class, "skus");
		classAlias.put(SkuSyncDto.class, "sku");
		String xmlSkuSyncDto = BeanToXMLUtils.beansToXml(skuSyncDtos, classAlias);
		logger.info("采购订单   {} 开始sku同步 >>>>", purchaseInfo.getOrderCode());

		String result = bMLSoapClient.singleSkuToWms(xmlSkuSyncDto);
		Map<Class<?>, String> classzz = new HashMap<Class<?>, String>();
		classzz.put(ResponseDto.class, "Response");
		ResponseDto dto = (ResponseDto) BeanToXMLUtils.xmlToBean(result, classzz);
		logger.info("采购订单 {} sku同步完成>>>>result: {}", purchaseInfo.getOrderCode(), dto);

		// 发送入库指令
		classAlias = new HashMap<Class<?>, String>();
		classAlias.put(RequestPurchaseInfo.class, "RequestPurchaseInfo");
		classAlias.put(ProductInfo.class, "productInfo");
		String xml = BeanToXMLUtils.beansToXml(purchaseInfo, classAlias);
		logger.info("采购订单   {} 开始发送入库指令 >>>>", purchaseInfo.getOrderCode());
		result = bMLSoapClient.ansToWms(xml);
		// 解析返回结果
		classzz = new HashMap<Class<?>, String>();
		classzz.put(ResponseDto.class, "Response");
		dto = (ResponseDto) BeanToXMLUtils.xmlToBean(result, classzz);
		logger.info("采购订单 {} 发送入库指令完成>>>>result: {}", purchaseInfo.getOrderCode(), dto);
		ResultInfo<String> message = new ResultInfo<String>();
		if (ResponseDto.SUCCESS.equals(dto.getSuccess())) {
			message.setSuccess(Boolean.TRUE);
			inputOrder.setStatus(1);
		} else {
			inputOrder.setStatus(0);
			failTimes++;
			inputOrder.setFailTimes(failTimes);
			message.setSuccess(Boolean.FALSE);
			if (failTimes == StorageConstant.SEND_ORDER_WMS_MAX_FAIL_TIMES.intValue()) {
				// 发送邮件通知相关人员
				if (StringUtils.isNotBlank(notifyEmail)) {
					String[] emails = notifyEmail.split(",");
					StringBuffer body = new StringBuffer();
					body.append("error message:" + message.toString());
					body.append(IMailService.WRAP);
					body.append("采购单号：" + inputOrder.getOrderCode());
					body.append(IMailService.WRAP);
					body.append("批次号：" + inputOrder.getZdr());
					body.append(IMailService.WRAP);
					body.append("仓库：" + defaultWarehouseCode);
					body.append(IMailService.WRAP);
					body.append(IMailService.WRAP);
					body.append("商品信息：>>>>>>>>>>>>>>>>>>>>>>>");
					body.append(IMailService.WRAP);
					body.append(IMailService.WRAP);
					for (InputOrderDetail orderDetail : inputOrderDetailList) {
						body.append("商品：" + orderDetail.getItemName());
						body.append(IMailService.WRAP);
						body.append("商品sku：" + orderDetail.getSkuCode());
						body.append(IMailService.WRAP);
						body.append("商品条形码：" + orderDetail.getBarcode());
						body.append(IMailService.WRAP);
						body.append(IMailService.WRAP);
					}
					mailService.batchSend(emails, StorageConstant.MailProperties.MAIL_NOTIFY_TITLE_FG, body.toString());
				}
			}
		}
		inputOrderDao.updateById(inputOrder);
		message.setData(dto.getDesc());
		return message;
	}

	/**
	 * 将 null值转为空字符串
	 * 
	 * @param object
	 * @return
	 */
	private Object nullToEmpty(Object object) {
		if (null == object) {
			return "";
		}
		return object;
	}

	public List<InputOrder> selectFailInputOrder(int maxTime, int limitSize) {
		Map<String, Object> params = new HashMap<>();
		params.put(MYBATIS_SPECIAL_STRING.COLUMNS.name(), " status = 0 and fail_times < " + maxTime + " and fail_times > 0");
		params.put(MYBATIS_SPECIAL_STRING.LIMIT.name(), limitSize);
		return inputOrderDao.queryPageByParamNotEmpty(params); //12.24tmp
	}

	public List<InputOrderDetail> selectOrderDetailByOrderId(Long inputOrderId) {
		Map<String, Object> params = new HashMap<>();
		params.put("inputOrderId", inputOrderId);
		return inputOrderDetailDao.queryByParamNotEmpty(params);
	}

	public List<InputOrder> selectInputOrderByOrderCode(String orderCode) {
		Map<String, Object> params = new HashMap<>();
		params.put("orderCode", orderCode);
		return queryByParamNotEmpty(params);
	}

	@Override
	public BaseDao<InputOrder> getDao() {
		return inputOrderDao;
	}

}
