package com.tp.service.cms.app;

import com.tp.common.vo.PageInfo;
import com.tp.dto.cms.app.AppIndexAdvertReturnData;
import com.tp.dto.cms.app.AppSingleAllInfoDTO;

/**
 * Created by ldr on 2016/10/12.
 */
public interface IOffLineGroupbuyService {

    /**
     * 线下团购首页轮播图
     * @return
     */
    AppIndexAdvertReturnData getAdvert() throws Exception;

    /**
     * 线下团购商家列表
     * @param pagestart
     * @param pagesize
     * @return
     */
    PageInfo<AppSingleAllInfoDTO> shopList(int pagestart, int pagesize);

}
