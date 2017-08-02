package com.tp.proxy.wms;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.model.wms.StockasnDetailFact;
import com.tp.proxy.BaseProxy;
import com.tp.service.IBaseService;
import com.tp.service.wms.IStockasnDetailFactService;
/**
 * 代理层
 * @author szy
 *
 */
@Service
public class StockasnDetailFactProxy extends BaseProxy<StockasnDetailFact>{

	@Autowired
	private IStockasnDetailFactService stockasnDetailFactService;

	@Override
	public IBaseService<StockasnDetailFact> getService() {
		return stockasnDetailFactService;
	}
}
