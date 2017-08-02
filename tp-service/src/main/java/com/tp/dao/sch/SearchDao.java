package com.tp.dao.sch;

import com.tp.common.dao.BaseDao;
import com.tp.model.sch.Search;

import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

public interface SearchDao extends BaseDao<Search> {

    Integer delAll();

    Integer batchInsert(List<Search> list);

    List<Search> getAll();

    List<Search> getAllNormal();

    List<Search> getAllDel();

    Integer deleteByIds(List<Long> ids);

    Integer updateStatusToDelByTopicItemIds(@Param("date")Date date,@Param("ids") List<Long> ids);

    Date timestamp();

    List<Search> getUpdatedByUpdateTime(@Param("timestamp") Date date);

    Integer updateDataById(Search search);
}
