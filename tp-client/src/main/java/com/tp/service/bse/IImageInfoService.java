package com.tp.service.bse;

import com.tp.model.bse.ImageInfo;
import com.tp.service.IBaseService;

import java.util.List;

/**
  * @author szy 
  * 接口
  */
public interface IImageInfoService extends IBaseService<ImageInfo>{

    List<ImageInfo> queryListWithId(Long id);

}
