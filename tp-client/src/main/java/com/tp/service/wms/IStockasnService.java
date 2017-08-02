package com.tp.service.wms;

import com.tp.dto.common.ResultInfo;
import com.tp.dto.wms.SendOrderInfo;
import com.tp.model.wms.Stockasn;
import com.tp.service.IBaseService;

import java.util.List;

/**
  * @author szy 
  * 入库订单接口
  */
public interface IStockasnService extends IBaseService<Stockasn>{

    ResultInfo<Object> sentWarehouseOrder(SendOrderInfo info);

    Integer updateStatusToSuccess(List<Long> ids);

}
