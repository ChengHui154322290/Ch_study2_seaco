package com.tp.proxy.sup;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.model.sup.QuotationProduct;
import com.tp.proxy.BaseProxy;
import com.tp.service.IBaseService;
import com.tp.service.sup.IQuotationProductService;
/**
 * 报价单-商品表代理层
 * @author szy
 *
 */
@Service
public class QuotationProductProxy extends BaseProxy<QuotationProduct>{

	@Autowired
	private IQuotationProductService quotationProductService;

	@Override
	public IBaseService<QuotationProduct> getService() {
		return quotationProductService;
	}
}
