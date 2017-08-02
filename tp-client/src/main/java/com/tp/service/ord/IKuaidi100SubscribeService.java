package com.tp.service.ord;

import java.util.List;

import com.tp.model.ord.Kuaidi100Subscribe;
import com.tp.service.IBaseService;
/**
  * @author szy
  * 快递100订阅记录，即推送快递单号给快递100平台接口
  */
public interface IKuaidi100SubscribeService extends IBaseService<Kuaidi100Subscribe>{

	void batchInsert(List<Kuaidi100Subscribe> kuaidi100SubscribeDOList);

}
