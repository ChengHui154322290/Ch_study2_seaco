package com.tp.service.bse;

import java.util.List;

import com.tp.model.bse.ExpressCodeInfo;
import com.tp.model.bse.ExpressInfo;
import com.tp.service.IBaseService;

/**
  * @author szy
  * 快递100物流公司编号与仓库系统的物流编号对照表接口
  */
public interface IExpressCodeInfoService extends IBaseService<ExpressCodeInfo>{

	
	/**
	 * 查询所有快递公司信息
	 * @return
	 */
	public List<ExpressInfo> selectAllExpressCode();

}
