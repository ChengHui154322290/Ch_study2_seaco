package com.tp.dao.sch;

import java.util.List;

import com.tp.model.sch.ItemSearch;

/**
 * Created by ldr on 2016/2/16.
 */
public interface ItemSearchDao   {

    List<ItemSearch> getByItemIds(List<Long> list);
}
