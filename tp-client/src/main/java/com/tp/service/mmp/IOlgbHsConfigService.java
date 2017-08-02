package com.tp.service.mmp;

import com.tp.model.mmp.OlgbHsConfig;
import com.tp.service.IBaseService;

import java.util.List;

/**
  * @author szy 
  * 接口
  */
public interface IOlgbHsConfigService extends IBaseService<OlgbHsConfig>{

    int updateStatusByIds(List<Long>ids);

}
