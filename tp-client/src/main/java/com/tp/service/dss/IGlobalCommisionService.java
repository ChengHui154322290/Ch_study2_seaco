package com.tp.service.dss;

import com.tp.model.dss.GlobalCommision;
import com.tp.service.IBaseService;
/**
  * @author szy 
  * 全局佣金设置表接口
  */
public interface IGlobalCommisionService extends IBaseService<GlobalCommision>{

	/**
	 * 获取最后设置的比率
	 * @return
	 */
	GlobalCommision queryLastGlobalCommision();	
}
