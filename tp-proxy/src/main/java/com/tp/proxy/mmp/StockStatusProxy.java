package com.tp.proxy.mmp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.model.mmp.StockStatus;
import com.tp.proxy.BaseProxy;
import com.tp.service.IBaseService;
import com.tp.service.mmp.IStockStatusService;
/**
 * 库存代理层
 * @author szy
 *
 */
@Service
public class StockStatusProxy extends BaseProxy<StockStatus>{

	@Autowired
	private IStockStatusService stockStatusService;

	@Override
	public IBaseService<StockStatus> getService() {
		return stockStatusService;
	}
}
