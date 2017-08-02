package com.tp.service.pay;

import com.tp.common.vo.PageInfo;
import com.tp.model.pay.SeagoorPayInfo;
import com.tp.service.IBaseService;

import java.util.List;
import java.util.Map;

/**
  * @author szy 
  * 接口
  */
public interface ISeagoorPayInfoService extends IBaseService<SeagoorPayInfo>{

   PageInfo<SeagoorPayInfo> queryByParamForPage(Map<String,Object> param);

}
