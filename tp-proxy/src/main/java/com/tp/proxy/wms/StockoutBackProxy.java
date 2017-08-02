package com.tp.proxy.wms;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.tp.common.util.ExceptionUtils;
import com.tp.dto.common.FailInfo;
import com.tp.dto.common.ResultInfo;
import com.tp.model.wms.StockoutBack;
import com.tp.proxy.BaseProxy;
import com.tp.service.IBaseService;
import com.tp.service.wms.IStockoutBackService;
/**
 * 出库单回执代理层
 * @author szy
 *
 */
@Service
public class StockoutBackProxy extends BaseProxy<StockoutBack>{

	@Autowired
	private IStockoutBackService stockoutBackService;

	@Override
	public IBaseService<StockoutBack> getService() {
		return stockoutBackService;
	}
	
	/**
	 * 根据订单号查询出库单回执
	 * @param orderCode
	 * @return
	 */
	public ResultInfo<StockoutBack> queryStockoutBackByOrderCode(String orderCode){
		Map<String, Object> params = new HashMap<>();
		params.put("orderCode", orderCode);
		try {
			List<StockoutBack> backs = getService().queryByParam(params);
			if (CollectionUtils.isEmpty(backs)) {
				return new ResultInfo<>();
			}
			return new ResultInfo<>(backs.get(0));
		} catch (Exception e) {
			ExceptionUtils.print(new FailInfo(e),logger, orderCode);
			return new ResultInfo<>(new FailInfo("查询出库单回执异常"));
		}
	}
	
	/**
	 * 处理出库单回执
	 * @param stockoutBack
	 * @return 
	 */
	public ResultInfo<Boolean> processStockoutBack(StockoutBack stockoutBack){
		try {
			return stockoutBackService.processStockoutBack(stockoutBack);
		} catch (Exception e) {
			ExceptionUtils.print(new FailInfo(e),logger, JSONObject.toJSONString(stockoutBack));
			return new ResultInfo<>(new FailInfo("处理出库单回执异常"));
		}
	}
	
	public void processStockoutBack(List<StockoutBack> stockoutBackList){
		try {
			stockoutBackService.processStockoutBack(stockoutBackList);
		} catch (Exception e) {
			ExceptionUtils.print(new FailInfo(e),logger, JSONObject.toJSONString(stockoutBackList));
		}
	}
}
