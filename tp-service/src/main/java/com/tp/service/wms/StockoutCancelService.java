package com.tp.service.wms;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.dao.BaseDao;
import com.tp.dao.wms.StockoutCancelDao;
import com.tp.dto.common.FailInfo;
import com.tp.dto.common.ResultInfo;
import com.tp.model.wms.Stockout;
import com.tp.model.wms.StockoutCancel;
import com.tp.service.BaseService;
import com.tp.service.wms.IStockoutCancelService;
import com.tp.service.wms.IStockoutService;
import com.tp.service.wms.thirdparty.IStockoutCancelToTPService;

@Service
public class StockoutCancelService extends BaseService<StockoutCancel> implements IStockoutCancelService {

	private static final Logger logger = LoggerFactory.getLogger(StockoutCancelService.class);
	
	@Autowired
	private StockoutCancelDao stockoutCancelDao;
	
	@Autowired
	private IStockoutService stockoutService;
	
	@Autowired
	private List<IStockoutCancelToTPService> stockoutCancelServiceList; 
	
	@Override
	public BaseDao<StockoutCancel> getDao() {
		return stockoutCancelDao;
	}
	/**
	 * 公共仓暂时不需要推送取消出货单,因出货单为最终出货单,一旦仓库方返回OK，不允许取消 
	 */
	public ResultInfo<Boolean> cancelOutputOrder(String orderCode){
		//  取消订单
		Map<String, Object> params = new HashMap<>();
		params.put("orderCode", orderCode);
		try {
			List<Stockout> stockoutObjs = stockoutService.queryByParamNotEmpty(params);
			if(CollectionUtils.isEmpty(stockoutObjs)){
				return new ResultInfo<>(new FailInfo("取消订单失败,未找到出库单记录,orderCode:"+orderCode));
			}
			params.clear();
			params.put("orderCode", orderCode);
			params.put("success", 1); //取消成功的
			List<StockoutCancel> cancels = getDao().queryByParamNotEmpty(params);
			if (CollectionUtils.isNotEmpty(cancels)) {
				return new ResultInfo<>(new FailInfo("取消订单失败,存在已成功取消的记录"));
			}
			ResultInfo<StockoutCancel> message = cancelOrder(stockoutObjs.get(0));
			//推送成功
			if (Boolean.TRUE == message.isSuccess() && null != message.getData()) {
				StockoutCancel cancel = message.getData();
				ResultInfo<Boolean> resultInfo = null;
				if (cancel.getSuccess() == 1) {
					resultInfo = new ResultInfo<>(Boolean.TRUE);					
				}else{
					resultInfo = new ResultInfo<>(new FailInfo(cancel.getError()));
				}
				getDao().insert(cancel); //插入取消记录
				return resultInfo;
			}else{
				logger.error("取消订单失败，原因:" + message.getMsg().getDetailMessage());
				return new ResultInfo<>(message.getMsg());
			}
		} catch (Exception e) {
			logger.error("取消订单失败 {} 错误：{}",orderCode,e.getMessage());
			return new ResultInfo<>(new FailInfo("取消订单失败:"+e.getMessage()+" orderCode:"+orderCode));
		} 
	}
	//取消订单
	private ResultInfo<StockoutCancel> cancelOrder(Stockout stockout){
		for (IStockoutCancelToTPService iStockoutCancelToTPService : stockoutCancelServiceList) {
			if (iStockoutCancelToTPService.check(stockout)) {
				return iStockoutCancelToTPService.cancelOrder(stockout.getOrderCode());
			}
		}
		return new ResultInfo<>(new FailInfo("取消订单失败,仓库类型不支持取消"));
	}
	
}
