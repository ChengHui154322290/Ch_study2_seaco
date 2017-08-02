package com.tp.service.bse;

import java.util.List;

import com.tp.model.bse.Brand;
import com.tp.service.IBaseService;
/**
  * @author szy
  * 商品品牌接口
  */
public interface IBrandService extends IBaseService<Brand>{
	List<Brand> selectListBrand(List<Long> ids, Integer status);

	List<Brand>selectBrandListByLikeBrandDo(Brand brand);

	/**
	 * @param trim
	 * @param currentUserId
	 * @return
	 */
	Long selectIdByName(String trim, Long currentUserId);
}
