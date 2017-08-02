package com.tp.service.prd;

import java.util.List;

import com.tp.model.prd.ItemDetailSpec;
import com.tp.service.IBaseService;
/**
  * @author szy
  * 商品销售规格信息接口
  */
public interface IItemDetailSpecService extends IBaseService<ItemDetailSpec>{
	
	/**
	 * 通过id数组查询 
	 * @param ids
	 * @return
	 */
	List<ItemDetailSpec> selectDetailSpecListByDetailIds(List<Long> ids);

	/**
	 *  强制主库查询规格信息
	 * @param detialIds
	 * @return
	 */
	List<ItemDetailSpec> selectDetailSpecListByDetailIdsFromMaster(List<Long> detialIds);

}
