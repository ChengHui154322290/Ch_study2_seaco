package com.tp.dao.mmp;

import java.util.List;

import com.tp.common.dao.BaseDao;
import com.tp.model.mmp.Area;

public interface AreaDao extends BaseDao<Area> {


    /**
     * 根据ID更新 区域全部属性
     *
     * @param area
     * @return 更新行数
     * @
     * @author szy
     */
    Integer update(Area area) ;


    /**
     * 动态更新 区域部分属性，包括全部
     *
     * @param area
     * @return 更新行数
     * @
     * @author szy
     */
    Integer updateDynamic(Area area) ;



    /**
     * 根据 区域 动态返回记录数
     *
     * @param area
     * @return 记录条数
     * @
     * @author szy
     */
    Long selectCountDynamic(Area area) ;

    /**
     * 根据 区域 动态返回 区域 列表
     *
     * @param area
     * @return List<Area>
     * @
     * @author szy
     */
    List<Area> selectDynamic(Area area) ;

    /**
     * 根据 区域 动态返回 区域 Limit 列表
     *
     * @param area
     *            start,pageSize属性必须指定
     * @return List<Area>
     * @
     * @author szy
     */
    List<Area> selectDynamicPageQuery(Area area) ;


}
