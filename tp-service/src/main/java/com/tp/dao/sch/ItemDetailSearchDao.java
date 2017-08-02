package com.tp.dao.sch;

import java.util.List;

import com.tp.model.sch.ItemDetailSearch;

/**
 * Created by ldr on 2016/2/16.
 */
public interface ItemDetailSearchDao {

    List<ItemDetailSearch> getByItemIds(List<Long> list);
}
