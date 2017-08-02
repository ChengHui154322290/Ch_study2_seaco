package com.tp.seller.ao.base;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.model.bse.TaxRate;
import com.tp.service.bse.ITaxRateService;

@Service
public class TaxRateAO {
	@Autowired
	private ITaxRateService taxRateService;
	
	public List<TaxRate> queryByObject(TaxRate taxRateDO){
		return taxRateService.queryByObject(taxRateDO);
	}
}
