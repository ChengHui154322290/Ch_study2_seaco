package com.tp.dao.sch;

import java.util.List;

import com.tp.model.sch.BrandSearch;

/**
 * Created by ldr on 2016/2/18.
 */
public interface BrandSearchDao {

     List<BrandSearch> getByBrandIds(List<Long > list);

     BrandSearch queryById(Long brandId);
}
