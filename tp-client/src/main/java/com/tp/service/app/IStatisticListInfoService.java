package com.tp.service.app;

import com.tp.model.app.StatisticListInfo;
import com.tp.service.IBaseService;
/**
  * @author szy
  * 请求每个列表和详情页面文件时，后台返回结果（页面数据或者错误）时统计，主要用于统计每个页面的瞬间开销，请求成功失败的情况等信息接口
  */
public interface IStatisticListInfoService extends IBaseService<StatisticListInfo>{

}
