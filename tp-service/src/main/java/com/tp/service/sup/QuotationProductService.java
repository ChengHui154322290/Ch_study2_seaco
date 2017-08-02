package com.tp.service.sup;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.dao.BaseDao;
import com.tp.dao.sup.QuotationProductDao;
import com.tp.model.sup.QuotationProduct;
import com.tp.service.BaseService;
import com.tp.service.sup.IQuotationProductService;

@Service
public class QuotationProductService extends BaseService<QuotationProduct> implements IQuotationProductService {

	@Autowired
	private QuotationProductDao quotationProductDao;
	
	@Override
	public BaseDao<QuotationProduct> getDao() {
		return quotationProductDao;
	}

}
