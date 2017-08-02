package com.tp.proxy.mmp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.model.mmp.StockIo;
import com.tp.proxy.BaseProxy;
import com.tp.service.IBaseService;
import com.tp.service.mmp.IStockIoService;
/**
 * 出入库代理层
 * @author szy
 *
 */
@Service
public class StockIoProxy extends BaseProxy<StockIo>{

	@Autowired
	private IStockIoService stockIoService;

	@Override
	public IBaseService<StockIo> getService() {
		return stockIoService;
	}
}
