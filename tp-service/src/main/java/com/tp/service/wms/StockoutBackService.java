package com.tp.service.wms;

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
import com.tp.common.vo.Constant.AUTHOR_TYPE;
import com.tp.common.vo.stg.WarehouseConstant.WMSCode;
import com.tp.dao.wms.StockoutBackDao;
import com.tp.dao.wms.StockoutBackDetailDao;
import com.tp.dto.common.FailInfo;
import com.tp.dto.common.ResultInfo;
import com.tp.model.ord.OrderDelivery;
import com.tp.model.wms.Stockout;
import com.tp.model.wms.StockoutBack;
import com.tp.model.wms.StockoutBackDetail;
import com.tp.service.BaseService;
import com.tp.service.stg.IOutputOrderService;
import com.tp.service.wms.IStockoutBackService;
import com.tp.service.wms.IStockoutService;
import com.tp.service.wms.thirdparty.IStockoutBackTPService;

@Service
public class StockoutBackService extends BaseService<StockoutBack> implements IStockoutBackService {

	private static final Logger logger = LoggerFactory.getLogger(StockoutBackService.class);
	
	@Autowired
	private StockoutBackDao stockoutBackDao;
	
	@Autowired
	private StockoutBackDetailDao stockoutBackDetailDao;
	
	@Autowired
	private IStockoutService stockoutService; 
	
	@Autowired
	private IOutputOrderService outputOrderService;
	
	@Autowired
	private List<IStockoutBackTPService> stockoutBackServiceList;
	
	@Override
	public BaseDao<StockoutBack> getDao() {
		return stockoutBackDao;
	}

	/**
	 * 出库单反馈： 1. 验证数据与出库单是否相符;  2. 通知订单系统已发货; 3. 反馈单入库; 
	 * @param stockoutBack
	 * @return
	 * @throws Exception 
	 */
	@Override
	@Transactional
	public ResultInfo<Boolean> processStockoutBack(StockoutBack stockoutBack) throws Exception {
		logger.info("[STOCKOUT_BACK][{}]出库单回执开始处理：{}", stockoutBack.getOrderCode(), JSONObject.toJSONString(stockoutBack));
		Map<String, Object> params = new HashMap<>();
		params.put("orderCode", stockoutBack.getOrderCode());
		List<StockoutBack> sobList = stockoutBackDao.queryByParam(params);
		if (CollectionUtils.isNotEmpty(sobList)) {
			StockoutBack sob = sobList.get(0);
			if (sob.getStatus() == 1) {	//已经成功处理
				logger.error("[STOCKOUT_BACK][{}]出库单回执已处理,重复反馈.", stockoutBack.getOrderCode());
				return new ResultInfo<>(Boolean.TRUE);
			}
			stockoutBack.setId(sob.getId());
		}
		//数据校验
		ResultInfo<StockoutBack> validateResult = validateStockoutBack(stockoutBack);	
		if (!validateResult.isSuccess()) {
			logger.error("[STOCKOUT_BACK][{}]数据验证失败：{}", 
					stockoutBack.getOrderCode(), validateResult.getMsg().getDetailMessage());
			return new ResultInfo<>(validateResult.getMsg());
		}
		//通知发货
		stockoutBack = validateResult.getData();
		FailInfo failInfo = deliveryOrder(stockoutBack);
		if (failInfo != null) {
			stockoutBack.setStatus(0);
			stockoutBack.setMessage(failInfo.getDetailMessage());
		}else{
			stockoutBack.setStatus(1);
		}
		// 反馈单入库
		logger.info("[STOCKOUT_BACK][{}]发货操作完毕,执行出库单回执入库", stockoutBack.getOrderCode());
		saveStockoutBack(stockoutBack);
		return new ResultInfo<>(Boolean.TRUE);		
	}

	@Override
	public ResultInfo<Boolean> processStockoutBack(List<StockoutBack> stockoutBacks) throws Exception {
		if (CollectionUtils.isEmpty(stockoutBacks)) {
			return new ResultInfo<>(new FailInfo("反馈单列表为空"));
		}
		for (StockoutBack stockoutBack : stockoutBacks) {
			processStockoutBack(stockoutBack);
		}
		return new ResultInfo<>(Boolean.TRUE);
	}

	private ResultInfo<StockoutBack> validateStockoutBack(StockoutBack stockoutBack) {
		ResultInfo<Stockout> queryResult = stockoutService.queryStockoutByOrderCode(stockoutBack.getOrderCode());
		if (!queryResult.isSuccess()) {
			logger.error("[STOCKOUT_BACK][{}]出库单不存在：{}", stockoutBack.getOrderCode(), queryResult.getMsg().getDetailMessage());
			return new ResultInfo<>(new FailInfo("对应出库单不存在"));
		}
		// 验证实际出库回执数据与出库单是否相符(第三方仓库做相关逻辑处理)
		Stockout stockout = queryResult.getData();
		ResultInfo<StockoutBack> processResult = null;
		for (IStockoutBackTPService service : stockoutBackServiceList) {
			if (service.check(stockoutBack, stockout)) {
				processResult = service.processStockoutBack(stockoutBack, stockout);
			}
		}
		if (processResult == null) {
			logger.error("[STOCKOUT_BACK][{}]仓库类型未支持：{}", stockoutBack.getOrderCode(), stockout.getWmsCode());
			return new ResultInfo<>(new FailInfo("仓库未对接"));
		}
		if (!processResult.isSuccess()) {
			logger.error("[STOCKOUT_BACK][{}]第三方仓库{}处理出库反馈失败：{}", 
					stockoutBack.getOrderCode(), stockout.getWmsCode(),processResult.getMsg().getDetailMessage());
			return processResult;
		}
		return processResult;
	}
	
	private FailInfo deliveryOrder(StockoutBack stockoutBack) throws Exception{
		Stockout stockout = stockoutService.queryStockoutByOrderCode(stockoutBack.getOrderCode()).getData();
		logger.info("[STOCKOUT_BACK][{}]出库单数据校验完成，通知订单系统发货...", stockoutBack.getOrderCode());
		// 通知订单系统已发货
		OrderDelivery orderDelivery = new OrderDelivery();
		orderDelivery.setCompanyId(stockout.getLogisticsCompanyCode());
		orderDelivery.setCompanyName(stockout.getLogisticsCompanyName());
		orderDelivery.setOrderCode(Long.valueOf(stockout.getOrderCode()));
		orderDelivery.setWeight(stockoutBack.getWeight());
		orderDelivery.setPackageNo(stockout.getExpressNo());
		orderDelivery.setCreateUser(AUTHOR_TYPE.SYSTEM);	// 设置处理人
		orderDelivery.setDeliveryTime(new Date());			// 设置发货时间
		ResultInfo<Boolean> deliveryResult = outputOrderService.exWarehouseService(orderDelivery);
		if (!deliveryResult.isSuccess()) {
			logger.error("[STOCKOUT_BACK][{}]发货失败：{}", stockout.getOrderCode(), deliveryResult.getMsg().getDetailMessage());
			return deliveryResult.getMsg();
		}
		return null;
	}
	/**
	 * 保存出库单
	 * @param stockoutBack
	 * @return
	 */
	private void saveStockoutBack(StockoutBack stockoutBack){
		if (stockoutBack.getId() != null) {
			//新增
			updateById(stockoutBack);
			StockoutBackDetail detail = new StockoutBackDetail();
			detail.setStockoutBackId(stockoutBack.getId());
			stockoutBackDetailDao.deleteByObject(detail);
			if (CollectionUtils.isNotEmpty(stockoutBack.getDetails())) {
				for (StockoutBackDetail d : stockoutBack.getDetails()) {
					d.setStockoutBackId(stockoutBack.getId());
				}
				stockoutBackDetailDao.insertDetails(stockoutBack.getDetails());
			}
		}else{
			//保存
			insert(stockoutBack);
			if (CollectionUtils.isNotEmpty(stockoutBack.getDetails())) {
				for (StockoutBackDetail detail : stockoutBack.getDetails()) {
					detail.setStockoutBackId(stockoutBack.getId());
				}
				stockoutBackDetailDao.insertDetails(stockoutBack.getDetails());
			}
		}	
	}
}
