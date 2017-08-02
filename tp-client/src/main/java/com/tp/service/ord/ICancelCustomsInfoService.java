package com.tp.service.ord;

import com.tp.model.ord.CancelCustomsInfo;
import com.tp.service.IBaseService;
/**
  * @author szy 
  * 取消海淘单海关申报接口
  */
public interface ICancelCustomsInfoService extends IBaseService<CancelCustomsInfo>{

	/** 查询取消单 */
	CancelCustomsInfo selectByOrderCode(Long subCode);
}
