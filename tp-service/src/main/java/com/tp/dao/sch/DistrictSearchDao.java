package com.tp.dao.sch;

import java.util.List;

import com.tp.model.sch.DistrictSearch;

/**
 * Created by ldr on 2016/3/2.
 */
public interface DistrictSearchDao {

    List<DistrictSearch> getByIds(List<Long> list);



}
