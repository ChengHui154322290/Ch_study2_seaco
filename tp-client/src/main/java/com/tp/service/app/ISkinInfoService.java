package com.tp.service.app;

import java.util.Date;

import com.tp.model.app.SkinInfo;
import com.tp.service.IBaseService;
/**
  * @author szy 
  * 接口
  */
public interface ISkinInfoService extends IBaseService<SkinInfo>{
	SkinInfo searchSkin(Long skinid,Date nowTime) ;
	

}
