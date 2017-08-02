package com.tp.dao.bse;

import java.util.List;

import com.tp.common.dao.BaseDao;
import com.tp.model.bse.Brand;

public interface BrandDao extends BaseDao<Brand> {

    List<Brand> selectBrandListByLikeBrandDo(Brand brand);

}
