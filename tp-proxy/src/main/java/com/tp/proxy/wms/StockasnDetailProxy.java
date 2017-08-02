package com.tp.proxy.wms;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.model.wms.StockasnDetail;
import com.tp.proxy.BaseProxy;
import com.tp.service.IBaseService;
import com.tp.service.wms.IStockasnDetailService;
/**
 * 公共仓入库订单明细代理层
 * @author szy
 *
 */
@Service
public class StockasnDetailProxy extends BaseProxy<StockasnDetail>{

	@Autowired
	private IStockasnDetailService stockasnDetailService;

	@Override
	public IBaseService<StockasnDetail> getService() {
		return stockasnDetailService;
	}
}
