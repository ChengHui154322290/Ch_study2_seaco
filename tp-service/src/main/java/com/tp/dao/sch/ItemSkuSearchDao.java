package com.tp.dao.sch;

import java.util.List;

import com.tp.model.sch.ItemSkuSearch;

/**
 * Created by ldr on 2016/2/16.
 */
public interface ItemSkuSearchDao {

    List<ItemSkuSearch> getBySkus(List<String> list);
}
