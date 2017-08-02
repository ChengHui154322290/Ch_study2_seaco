package com.tp.service.bse;

import java.util.List;

import com.tp.model.bse.DictionaryInfo;
import com.tp.service.IBaseService;
/**
  * @author szy
  * 数据字典：信息接口
  */
public interface IDictionaryInfoService extends IBaseService<DictionaryInfo>{

	List<DictionaryInfo> selectListByIds(List<Long> ids);

}
