package com.tp.service.bse;

import com.tp.model.bse.TaxRate;
import com.tp.service.IBaseService;
/**
  * @author szy
  * 税率表接口
  */
public interface ITaxRateService extends IBaseService<TaxRate>{

	/**
	 * @param d
	 * @param currentUserId
	 * @return
	 */
	Long selectIdByTaxRate(Double d, Long currentUserId);

}
