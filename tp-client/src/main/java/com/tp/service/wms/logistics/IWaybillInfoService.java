package com.tp.service.wms.logistics;

import com.tp.dto.common.ResultInfo;
import com.tp.dto.wms.logistics.WaybillInfoDto;
import com.tp.model.wms.WaybillInfo;
import com.tp.service.IBaseService;
/**
  * @author szy 
  * 运单报关及发运接口
  */
public interface IWaybillInfoService extends IBaseService<WaybillInfo>{
	
	/**
	 * 运单推送 
	 * @param dto
	 * @return
	 */
	ResultInfo<Boolean> deliverWaybillInfo(WaybillInfoDto dto);
}
