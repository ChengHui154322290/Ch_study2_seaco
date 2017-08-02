package com.tp.service.pay;

import com.tp.model.pay.CustomsInfo;
import com.tp.service.IBaseService;
/**
  * @author szy 
  * 海关信息接口
  */
public interface ICustomsInfoService extends IBaseService<CustomsInfo>{

	/**
	 * @param gatewayId
	 * @param channelId
	 * @return
	 */
	boolean isNeedPush(Long gatewayId, Long channelId);
	CustomsInfo getCustomsInfo(Long gatewayId, Long channelId);
}
