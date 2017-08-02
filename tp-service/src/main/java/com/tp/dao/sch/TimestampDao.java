package com.tp.dao.sch;

import org.apache.ibatis.annotations.Param;

import java.util.Date;

/**
 * Created by ldr on 2016/3/1.
 */
public interface TimestampDao {

    Date getTimestamp(@Param("code")String code);

    Integer insert(@Param("timestamp") Date timestamp,@Param("code")String code);

    Integer update(@Param("timestamp") Date timestamp,@Param("code")String code);

}
