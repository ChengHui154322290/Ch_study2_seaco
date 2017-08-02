package com.tp.dao.sch;

import java.util.List;

import com.tp.model.sch.ClearanceChannelSearch;

/**
 * Created by ldr on 2016/3/2.
 */
public interface ClearanceChannelSearchDao {

    List<ClearanceChannelSearch> getByChannelIds(List<Long> list);
}
