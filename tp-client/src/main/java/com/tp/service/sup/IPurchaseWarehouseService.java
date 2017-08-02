package com.tp.service.sup;

import com.tp.model.sup.PurchaseWarehouse;
import com.tp.service.IBaseService;

import java.util.Map;

/**
  * @author szy
  * 仓库预约单接口
  */
public interface IPurchaseWarehouseService extends IBaseService<PurchaseWarehouse>{

    Integer updateAuditStatusByIds(Map<String,Object> params);

}
