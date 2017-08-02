package com.tp.service.sch;

import com.tp.model.sch.Search;
import com.tp.service.IBaseService;

/**
 * 搜索数据采集服务
 * Created by ldr on 2016/2/18.
 */
public interface ISearchDataService extends IBaseService<Search> {

    void process();

    /**
     * 全量采集商品数据
     */
    void processItemDataTotal();

    /**
     * 增量采集商品数据
     */
    void processItemData();

    /**
     * 增量采集店铺数据
     */
    void processShopData();

    void processShopDataTotal();
}
