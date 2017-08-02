package com.tp.service.wms;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;
import com.tp.common.dao.BaseDao;
import com.tp.common.vo.wms.WmsConstant;
import com.tp.common.vo.wms.WmsConstant.PutStatus;
import com.tp.dao.wms.StockoutDao;
import com.tp.dao.wms.StockoutInvoiceDao;
import com.tp.dto.common.FailInfo;
import com.tp.dto.common.ResultInfo;
import com.tp.dto.wms.StockoutDto;
import com.tp.dto.wms.StockoutInvoiceDto;
import com.tp.dto.wms.StockoutItem;
import com.tp.model.stg.InventoryOccupy;
import com.tp.model.wms.Stockout;
import com.tp.model.wms.StockoutDetail;
import com.tp.model.wms.StockoutInvoice;
import com.tp.service.BaseService;
import com.tp.service.stg.IInventoryOccupyService;
import com.tp.service.wms.IStockoutDetailService;
import com.tp.service.wms.IStockoutService;
import com.tp.service.wms.thirdparty.IStockoutServiceToTP;
import com.tp.util.StringUtil;

@Service
public class StockoutService extends BaseService<Stockout> implements IStockoutService {

	private static final Logger logger = LoggerFactory.getLogger(StockoutService.class);
	
	@Autowired
	private StockoutDao stockoutDao;
	
	@Autowired
	private IStockoutDetailService stockoutDetailService;
	
	@Autowired
	private StockoutInvoiceDao stockoutInvoiceDao;
	
	@Autowired
	private List<IStockoutServiceToTP> stockoutServiceList;
	
	@Autowired
	private IInventoryOccupyService inventoryOccupyService;
	
	@Override
	public BaseDao<Stockout> getDao() {
		return stockoutDao;
	}
	
	@Override
	public ResultInfo<Stockout> queryStockoutByOrderCode(String orderCode) {
		if (StringUtil.isEmpty(orderCode)) {
			logger.error("[STOCKOUT]查询出库单 - 订单号为空");
			return new ResultInfo<>(new FailInfo("订单号为空"));
		}
		logger.info("[STOCKOUT][orderCode={}]查询出库单", orderCode);
		Map<String, Object> params = new HashMap<>();
		params.put("orderCode", orderCode);
		Stockout stockout = queryUniqueByParams(params);
		if (null == stockout) {
			return new ResultInfo<>(new FailInfo("出库单不存在"));
		}
		params.clear();
		params.put("stockoutId", stockout.getId());
		List<StockoutDetail> stockoutDetails = stockoutDetailService.queryByParamNotEmpty(params);
		stockout.setStockoutDetails(stockoutDetails);
		return new ResultInfo<>(stockout);
	}
	
	@Transactional
	@Override
	public ResultInfo<Boolean> deliverStockoutOrder(StockoutDto stockoutDto) {	
		if (null == stockoutDto.getWarehouse()) {
			logger.error("[STOCKOUT][{}]出库单信息中仓库数据不存在", stockoutDto.getOrderCode());
			return new ResultInfo<>(new FailInfo("仓库信息不存在"));
		}
		if (!stockoutDto.getWarehouse().checkPutStorage() || !stockoutDto.getWarehouse().checkPutCleanOrder()) {
			logger.info("[STOCKOUT][{}]订单不需推送仓库");
			return new ResultInfo<>(Boolean.TRUE);
		}
		//校验参数
		FailInfo failInfo = validateStockout(stockoutDto);
		if (null != failInfo) {
			logger.error("[STOCKOUT][{}]数据校验失败：{}", stockoutDto.getOrderCode(), failInfo.getDetailMessage());
			return new ResultInfo<>(failInfo);
		}
		logger.info("[STOCKOUT][{}]推送出库单到仓库{}", stockoutDto.getOrderCode(), stockoutDto.getWarehouseCode());
		return pushStockoutOrder(stockoutDto);
	}
	
	
	/*-----------------------------------------------------------------------------------------------*/
	/*-----------------------------------------------------------------------------------------------*/
	/**
	 * 处理出库单
	 * @param stockoutDto
	 * @return
	 */
	private ResultInfo<Boolean> pushStockoutOrder(StockoutDto stockoutDto){
		ResultInfo<Boolean> message = null;
		message = checkStockoutInventory(stockoutDto);	//校验库存
		if (!message.isSuccess()) return message;
		Stockout stockout = convertToStockout(stockoutDto);	 //转换
		Map<String, Object> params = new HashMap<>();
		params.put("orderCode", stockoutDto.getOrderCode());
		List<Stockout> stockouts = getDao().queryByParamNotEmpty(params);
		if (CollectionUtils.isNotEmpty(stockouts)) {
			logger.info("[STOCKOUT][{}]出库单已存在,根据库中数据推送第三方仓库", stockoutDto.getOrderCode());
			Stockout oldStockout = stockouts.get(0);
			stockout.setId(oldStockout.getId());
			updateStockout(stockout); //更新最新数据
			//推送出库单至第三方WMS系统
			message = pushStockoutToThirdPartyWMS(stockout);
		}else{
			logger.info("[STOCKOUT][{}]出库单不存在,校验库存后推送第三方仓库", stockoutDto.getOrderCode());			
			if (Boolean.TRUE != message.isSuccess()) return message;
			Stockout newStockout = saveStockout(stockout);
			message = pushStockoutToThirdPartyWMS(newStockout);
		}
		return message;
	}
	
	//校验出库单库存使用情况
	private ResultInfo<Boolean> checkStockoutInventory(StockoutDto stockoutDto){
		Map<String, Object> params = new HashMap<>();
 		params.put("orderNo", stockoutDto.getOrderCode());
 		List<InventoryOccupy> inventoryOccupyObjs = inventoryOccupyService.queryByParamNotEmpty(params);
		if (CollectionUtils.isEmpty(inventoryOccupyObjs)) {
			logger.error("[STOCKOUT][{}]出库订单不存在库存冻结记录,无法推送", stockoutDto.getOrderCode());
			return new ResultInfo<>(new FailInfo("没有找到订单冻结库存记录，请检查后重试：订单号->" + stockoutDto.getOrderCode()));
		}
		return new ResultInfo<>(Boolean.TRUE);
	}
	
	//推送出库单至第三方WMS系统
	private ResultInfo<Boolean> pushStockoutToThirdPartyWMS(Stockout stockout){	
		logger.info("[STOCKOUT][{}]出库单推送第三方WMS:{}", stockout.getOrderCode(), JSONObject.toJSONString(stockout));
		if (WmsConstant.PutStatus.SUCCESS.code.equals(stockout.getStatus())) {
			logger.info("[STOCKOUT][{}]出库单已推送成功,不能重复推送", stockout.getOrderCode());
			return new ResultInfo<>(new FailInfo("出库单已成功推送，不需要重复发送"));
		}
		Integer failTimes = (stockout.getFailTimes() == null)?0:stockout.getFailTimes();
		if (failTimes >= WmsConstant.MAX_PUT_TIMES) {
			logger.error("[STOCKOUT][{}]出库单已重复推送{}次,不再重复推送", stockout.getOrderCode(), failTimes);
			return new ResultInfo<>(new FailInfo("单号为" + stockout.getOrderCode() + "的订单已重复推送" + WmsConstant.MAX_PUT_TIMES + "次,不再重复推送！"));
		}
		return sendStockoutOrder(stockout);
	}
	
	private ResultInfo<Boolean> sendStockoutOrder(Stockout stockout){
		for (IStockoutServiceToTP serviceTP : stockoutServiceList) {
			if (serviceTP.check(stockout)) {
				return serviceTP.deliverStockoutOrder(stockout);
			}
		}
		return new ResultInfo<>(new FailInfo("不支持仓库WMS推送"));
	}
	
	/**
	 * 验证出库单参数
	 */
	private FailInfo validateStockout(StockoutDto stockoutDto){
		if (StringUtil.isEmpty(stockoutDto.getOrderCode())) {
			return new FailInfo("出库单号为空");
		}
		if (null == stockoutDto.getWarehouseId() || StringUtil.isEmpty(stockoutDto.getWarehouseCode())) {
			return new FailInfo("仓库信息为空");
		}
		if (null == stockoutDto.getWarehouse()) {
			return new FailInfo("仓库数据为空");
		}
		if (StringUtil.isEmpty(stockoutDto.getWarehouse().getWmsCode())) {
			return new FailInfo("WMS信息为空");
		}
		if(null == stockoutDto.getOrderCreateTime()){
			return new FailInfo("订单创建时间为空");
		}
		if(null == stockoutDto.getOrderPayTime()){
			return new FailInfo("订单支付时间为空");
		}
		if (null == stockoutDto.getTotalAmount()) {
			return new FailInfo("订单总金额");
		}
		if(null == stockoutDto.getPayAmount()){
			return new FailInfo("订单实际支付金额");
		}
		if(null == stockoutDto.getDiscountAmount()) {
			return new FailInfo("订单优惠金额");
		}
		if (null == stockoutDto.getIsPostagePay()) {
			return new FailInfo("邮费是否到付不存在");
		}
		if (null == stockoutDto.getIsDeliveryPay()) {
			return new FailInfo("是否货到付款");
		}
		if (null == stockoutDto.getIsUrgency()) {
			return new FailInfo("是否紧急订单为空");
		}
		if (StringUtil.isEmpty(stockoutDto.getLogisticsCompanyCode())) {
			return new FailInfo("物流公司编号为空");
		}
		if (StringUtil.isEmpty(stockoutDto.getExpressNo())) {
			return new FailInfo("运单号为空");
		}
		if (StringUtil.isEmpty(stockoutDto.getConsignee())) {
			return new FailInfo("收件人为空");
		}
		if (StringUtil.isEmpty(stockoutDto.getProvince())) {
			return new FailInfo("收件人地址的省份为空");
		}
		if (StringUtil.isEmpty(stockoutDto.getCity())) {
			return new FailInfo("收件人地址的城市为空");
		}
		if (StringUtil.isEmpty(stockoutDto.getArea())) {
			return new FailInfo("收件人地址的区为空");
		}
		if (StringUtil.isEmpty(stockoutDto.getAddress())) {
			return new FailInfo("收件人地址为空");
		}
		if (StringUtil.isEmpty(stockoutDto.getMobile())) {
			return new FailInfo("手机号为空");
		}
		//商品详情
		if (CollectionUtils.isEmpty(stockoutDto.getOrderItemList())) {
			return new FailInfo("出库单商品详情列表为空");
		}else{
			for (StockoutItem item : stockoutDto.getOrderItemList()) {
				if (StringUtil.isEmpty(item.getItemSku())) {
					return new FailInfo("商品详情SKU为空");
				}
				if (StringUtil.isEmpty(item.getItemName())) {
					return new FailInfo("商品详情名称为空");
				}
				if (null == item.getQuantity() || item.getQuantity() <= 0) {
					return new FailInfo("商品详情中数量为空或者商品数量错误");
				}
				if (null == item.getSalesprice()) {
					return new FailInfo("商品详情中售价为空");
				}
			}
		}
		//发票信息
		if (null != stockoutDto.getIsInvoice() && CollectionUtils.isNotEmpty(stockoutDto.getInvoiceInfoList())) {
			for (StockoutInvoiceDto invoiceDto : stockoutDto.getInvoiceInfoList()) {
				if (StringUtil.isEmpty(invoiceDto.getInvoiceTitle())) {
					return new FailInfo("发票抬头为空");
				}
				if (StringUtil.isEmpty(invoiceDto.getInvoiceAmount())) {
					return new FailInfo("发票金额为空");
				}
				if (null == invoiceDto.getInvoiceType()) {
					return new FailInfo("发票类型为空");
				}
			}
		}
		return null;
	}
	
	/**
	 *	保存出库单及详情信息
	 */
	private Stockout saveStockout(Stockout stockout){
		if (null == stockout) {
			return null;
		}
		//保存出库单
		getDao().insert(stockout);
		//保存出库单详情
		for (StockoutDetail detail : stockout.getStockoutDetails()) {
			detail.setStockoutId(stockout.getId());
			stockoutDetailService.insert(detail);
		}
		//保存发票
		if (CollectionUtils.isNotEmpty(stockout.getStockoutInvoices())) {
			for (StockoutInvoice invoice : stockout.getStockoutInvoices()) {
				stockoutInvoiceDao.insert(invoice);
			}
		}
		return stockout;
	}
	
	//更新
	private Stockout updateStockout(Stockout stockout){
		if (stockout == null) {
			return null;
		}
		StockoutDetail outDetail = new StockoutDetail();
		outDetail.setStockoutId(stockout.getId());
		stockoutDetailService.deleteByObject(outDetail);
		StockoutInvoice outInvoice = new StockoutInvoice();
		outInvoice.setOrderCode(stockout.getOrderCode());
		stockoutInvoiceDao.deleteByObject(outInvoice);
		updateById(stockout);
		//保存出库单详情
		for (StockoutDetail detail : stockout.getStockoutDetails()) {
			detail.setStockoutId(stockout.getId());
			stockoutDetailService.insert(detail);
		}
		//保存发票
		if (CollectionUtils.isNotEmpty(stockout.getStockoutInvoices())) {
			for (StockoutInvoice invoice : stockout.getStockoutInvoices()) {
				stockoutInvoiceDao.insert(invoice);
			}
		}
		return stockout;
	}
	
	/**
	 * 组装数据 
	 */
	private Stockout convertToStockout(StockoutDto dto){
		Stockout stockout = new Stockout();
		
		//订单信息
		stockout.setOrderCode(dto.getOrderCode());
		stockout.setOrderType(null);
		stockout.setOrderCreateTime(dto.getOrderCreateTime());
		stockout.setPayTime(dto.getOrderPayTime());
		stockout.setTotalAmount(dto.getTotalAmount());
		stockout.setPayAmount(dto.getPayAmount());
		stockout.setDiscount(dto.getDiscountAmount());
		stockout.setPostage(dto.getPostAmount());
		stockout.setIsPostagePay(dto.getIsPostagePay());
		stockout.setIsDeliveryPay(dto.getIsDeliveryPay());
		stockout.setIsUrgency(dto.getIsUrgency());
		
		//仓库信息
		stockout.setWarehouseId(dto.getWarehouseId());
		stockout.setWarehouseCode(dto.getWarehouseCode());
		stockout.setWarehouseName(dto.getWarehouseName());
		stockout.setWmsCode(dto.getWarehouse().getWmsCode());
		stockout.setWmsName(dto.getWarehouse().getWmsName());
		//物流信息
		stockout.setLogisticsCompanyCode(dto.getLogisticsCompanyCode());
		stockout.setLogisticsCompanyName(dto.getLogisticsCompanyName());
		stockout.setExpressNo(dto.getExpressNo());
		
		//会员信息
		stockout.setMemberId(dto.getMemberId());
		stockout.setMemberName(dto.getMemberName());
		
		//收件人信息
		stockout.setConsignee(dto.getConsignee());
		stockout.setPostCode(dto.getPostCode());
		stockout.setProvince(dto.getProvince());
		stockout.setCity(dto.getCity());
		stockout.setArea(dto.getArea());
		stockout.setAddress(dto.getAddress());
		stockout.setMobile(dto.getMobile());
		stockout.setTel(dto.getTel());
		
		//发票信息
		stockout.setIsInvoice(dto.getIsInvoice());
		if (null != dto.getIsInvoice() && WmsConstant.InvoiceNeed.YES.code.equals(dto.getIsInvoice())) {	
			if (CollectionUtils.isNotEmpty(dto.getInvoiceInfoList())) {
				List<StockoutInvoice> invoices = new ArrayList<>();
				for (StockoutInvoiceDto invoiceDto : dto.getInvoiceInfoList()) {
					StockoutInvoice invoice = new StockoutInvoice();
					invoice.setOrderCode(dto.getOrderCode());
					invoice.setInvoiceCode(invoiceDto.getInvoiceCode());
					invoice.setInvoiceNo(invoiceDto.getInvoiceNo());
					invoice.setInvoiceType(invoiceDto.getInvoiceType());
					invoice.setInvoiceTitle(invoiceDto.getInvoiceTitle());
					invoice.setInvoiceAmount(invoiceDto.getInvoiceAmount());
					invoice.setInvoiceContent(invoiceDto.getInvoiceContent());
					invoice.setCreateTime(new Date());
					invoices.add(invoice);
				}
				stockout.setStockoutInvoices(invoices);
			}
		}
		
		//商品详情
		List<StockoutDetail> details = new ArrayList<>();
		for (StockoutItem stockoutItem : dto.getOrderItemList()) {
			StockoutDetail detail = new StockoutDetail();
			detail.setItemSku(stockoutItem.getItemSku());
			detail.setItemName(stockoutItem.getItemName());
			detail.setItemBarcode(stockoutItem.getBarCode());
			detail.setQuantity(stockoutItem.getQuantity());
			detail.setActualPrice(stockoutItem.getActualPrice());
			detail.setSalesPrice(stockoutItem.getSalesprice());
			detail.setDiscountAmount(stockoutItem.getDiscountAmount());
			detail.setCreateTime(new Date());
			details.add(detail);
		}
		stockout.setStockoutDetails(details);
		stockout.setRemark("");
		stockout.setStatus(PutStatus.FAIL.code);
		stockout.setFailTimes(0);
		stockout.setErrorMsg("");
		stockout.setCreateTime(new Date());
		return stockout;
	}
}
