package com.tp.dao.sch;

import java.util.List;

import com.tp.model.sch.SupplierShopSearch;

/**
 * Created by ldr on 9/23/2016.
 */
public interface SupplierShopSearchDao {

    List<SupplierShopSearch> getAllSupplierShops();

    List<SupplierShopSearch> getSupplierShopsBySupplierIds(List<Long> list);
}
