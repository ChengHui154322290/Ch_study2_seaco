package com.tp.dao.sch;

import com.tp.common.dao.BaseDao;
import com.tp.model.sch.SearchShop;

import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

public interface SearchShopDao extends BaseDao<SearchShop> {

    List<SearchShop> getAll();

    Integer updateRecordStatusToDel(@Param("list") List<Long> list, @Param("updateTime") Date updateTime);

    Integer batchInsert(@Param("list") List<SearchShop> list);

    List<SearchShop> queryByUpdateTimeAndRecordStatus(@Param("updateTime")Date updateTime,@Param("recordStatus")List<Integer> recordStatus);

    Integer updateRecordStatusByIds(@Param("ids")List<Long> ids,@Param("recordStatus") Integer recordStatus);

    Integer deleteByIds(@Param("ids")List<Long> ids);

    Integer deleteTotal();
}
