package com.tp.service.bse;

import java.util.List;

import com.tp.dto.ord.remote.ExpressInfoDTO;
import com.tp.model.bse.ExpressInfo;
import com.tp.result.bse.ExpressCompanyDTO;
import com.tp.service.IBaseService;
/**
  * @author szy
  * 快递公司信息表接口
  */
public interface IExpressInfoService extends IBaseService<ExpressInfo>{

	ExpressInfo selectByCode(String code);

	List<ExpressCompanyDTO> selectByNameOrCode(String name, String code);

	/**
	 * 获得所有快递公司信息
	 * @return
	 */
	List<ExpressInfo> selectAllExpress();

	/**
	 * @param code
	 * @param packageNo
	 * @return
	 */
	List<ExpressInfoDTO> queryExpressLogInfo(Long code, String packageNo);

}
