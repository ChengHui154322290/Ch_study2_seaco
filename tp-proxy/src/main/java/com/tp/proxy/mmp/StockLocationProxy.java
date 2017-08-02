package com.tp.proxy.mmp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.model.mmp.StockLocation;
import com.tp.proxy.BaseProxy;
import com.tp.service.IBaseService;
import com.tp.service.mmp.IStockLocationService;
/**
 * 库位代理层
 * @author szy
 *
 */
@Service
public class StockLocationProxy extends BaseProxy<StockLocation>{

	@Autowired
	private IStockLocationService stockLocationService;

	@Override
	public IBaseService<StockLocation> getService() {
		return stockLocationService;
	}
}
