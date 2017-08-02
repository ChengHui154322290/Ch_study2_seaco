package com.tp.dao.sch;

import java.util.List;

import com.tp.model.sch.InventorySearch;
import com.tp.model.sch.TopicItemSearch;

/**
 * Created by ldr on 2016/2/16.
 */
public interface InventorySearchDao {

    List<InventorySearch> getItemsInventory(List<TopicItemSearch> list);
}
