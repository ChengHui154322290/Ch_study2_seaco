package com.tp.service.ord;

import java.util.List;

import com.tp.model.ord.FisherDeliveryLog;
import com.tp.service.IBaseService;
/**
  * @author szy
  * 费舍尔发货日志表接口
  */
public interface IFisherDeliveryLogService extends IBaseService<FisherDeliveryLog>{
	public void addSendOrderLog(List<FisherDeliveryLog> fisherDeliveryLogList);
}
