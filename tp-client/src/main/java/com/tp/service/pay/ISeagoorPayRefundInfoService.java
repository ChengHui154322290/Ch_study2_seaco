package com.tp.service.pay;

import com.tp.common.vo.PageInfo;
import com.tp.model.pay.SeagoorPayRefundInfo;
import com.tp.service.IBaseService;

import java.util.Map;

/**
  * @author szy 
  * 接口
  */
public interface ISeagoorPayRefundInfoService extends IBaseService<SeagoorPayRefundInfo>{

    PageInfo<SeagoorPayRefundInfo> queryByParamsForPage(Map<String,Object> param);

}
