package com.tp.service.bse;

import java.util.List;

import com.tp.model.bse.ClearanceChannels;
import com.tp.service.IBaseService;
/**
  * @author szy
  * 通关渠道信息接口
  */
public interface IClearanceChannelsService extends IBaseService<ClearanceChannels>{

	List<ClearanceChannels> getAllClearanceChannelsByStatus(Integer status);

	List<ClearanceChannels> getClearanceChannelsListByIds(List<Long> seaChannelIdList);

}
