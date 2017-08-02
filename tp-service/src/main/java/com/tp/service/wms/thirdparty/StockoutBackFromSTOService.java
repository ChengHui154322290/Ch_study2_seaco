/**
 * 
 */
package com.tp.service.wms.thirdparty;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.tp.common.vo.stg.WarehouseConstant.WMSCode;
import com.tp.dto.common.FailInfo;
import com.tp.dto.common.ResultInfo;
import com.tp.model.wms.Stockout;
import com.tp.model.wms.StockoutBack;
import com.tp.service.wms.thirdparty.IStockoutBackTPService;

/**
 * @author Administrator
 *
 */
@Service("stockoutBackFromSTOService")
public class StockoutBackFromSTOService implements IStockoutBackTPService{

	private static final Logger logger = LoggerFactory.getLogger(StockoutBackFromSTOService.class);
	
	@Override
	public boolean check(StockoutBack stockoutBack, Stockout stockout) {
		if (WMSCode.STO_HZ.code.equals(stockoutBack.getWmsCode()) && 
				WMSCode.STO_HZ.code.equals(stockout.getWmsCode())) {
			return true;
		}
		return false;
	}
	
	@Override
	public ResultInfo<StockoutBack> processStockoutBack(StockoutBack stockoutBack, Stockout stockout) {
		if (!stockout.getExpressNo().equals(stockoutBack.getExpressNo())) {
			logger.error("[STOCKOUT_BACK][{}]申通仓出库单回执运单号不匹配, stockout-expressno={}, stockoutback-expressno={}", 
					stockoutBack.getOrderCode(), stockout.getExpressNo(), stockoutBack.getExpressNo());
			return new ResultInfo<>(new FailInfo("运单号错误"));
		}
		stockoutBack.setLogisticsCompanyCode(stockout.getLogisticsCompanyCode());
		stockoutBack.setLogisticsCompanyName(stockout.getLogisticsCompanyName());
		stockoutBack.setWarehouseCode(stockout.getWarehouseCode());
		stockoutBack.setStatus(1);
		stockoutBack.setWeight(0.0);
		stockoutBack.setAuditTime(new Date());
		stockoutBack.setCreateTime(new Date());
		return new ResultInfo<>(stockoutBack);
	}



}
