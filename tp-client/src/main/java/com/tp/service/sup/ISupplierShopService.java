
package com.tp.service.sup;

import com.tp.dto.sup.SupplierShopDto;
import com.tp.model.sup.SupplierShop;
import com.tp.service.IBaseService;

import java.util.List;

/**
  * @author zhouguofeng 
  * 商家平台店铺主表接口
  */
public interface ISupplierShopService extends IBaseService<SupplierShop>{
	/**
	 * 
	 * getSupplierShopInfo:(根据供应商ID获取供应商店铺信息). <br/>  
	 *  
	 * @author zhouguofeng  
	 * @param supplierId
	 * @return  
	 * @sinceJDK 1.8
	 */
	public SupplierShop getSupplierShopInfo(Long supplierId);

	List<SupplierShop> queryBySupplierIds(List<Long> list);

}
