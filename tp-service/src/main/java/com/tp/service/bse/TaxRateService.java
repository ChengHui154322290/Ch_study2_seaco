package com.tp.service.bse;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.dao.BaseDao;
import com.tp.common.vo.Constant;
import com.tp.common.vo.BseConstant.TaxRateEnum;
import com.tp.dao.bse.TaxRateDao;
import com.tp.model.bse.TaxRate;
import com.tp.service.BaseService;
import com.tp.service.bse.ITaxRateService;

@Service
public class TaxRateService extends BaseService<TaxRate> implements ITaxRateService {

	@Autowired
	private TaxRateDao taxRateDao;
	
	@Override
	public BaseDao<TaxRate> getDao() {
		return taxRateDao;
	}

	/* (non-Javadoc)
	 * @see com.tp.service.bse.ITaxRateService#selectIdByTaxRate(double, java.lang.Long)
	 */
	@Override
	public Long selectIdByTaxRate(Double taxRate,Long userId) {
		if(null == taxRate || taxRate.doubleValue() < 0 || taxRate.doubleValue() > 100){ return null;}
		TaxRate taxRateDO = new TaxRate();
		taxRateDO.setRate(taxRate);
		taxRateDO.setType(TaxRateEnum.TARRIFRATE.getType());
		taxRateDO.setStatus(Constant.ENABLED.YES);
		List<TaxRate> list=this.queryByObject(taxRateDO);
		if(CollectionUtils.isEmpty(list)){
			return this.insert(taxRateDO).getId();
		}
		return list.get(0).getId();
	}

}
