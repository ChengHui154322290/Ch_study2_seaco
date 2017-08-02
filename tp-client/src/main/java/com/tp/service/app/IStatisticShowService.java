package com.tp.service.app;

import com.tp.model.app.StatisticShow;
import com.tp.service.IBaseService;
/**
  * @author szy
  * 展示量统计 用于统计移动端应用上用户点击某个模块或者进入某个页面的频次，各终端可以根据需要定义cli参数的值，如”首页“表示用户进入了首页，”搜索“表示用户进入了搜索页接口
  */
public interface IStatisticShowService extends IBaseService<StatisticShow>{

}
