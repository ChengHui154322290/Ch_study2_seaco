package com.tp.dao.sch;

import java.util.List;

import com.tp.model.sch.ItemDetailSalesCountSearch;

/**
 * Created by ldr on 2016/2/16.
 */
public interface ItemDetailSalesCountSearchDao {

    List<ItemDetailSalesCountSearch> getSalesCountByItemDetailIds(List<Long> list);
}
