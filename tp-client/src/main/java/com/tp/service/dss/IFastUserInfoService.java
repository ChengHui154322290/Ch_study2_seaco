package com.tp.service.dss;

import com.tp.model.dss.FastUserInfo;
import com.tp.service.IBaseService;
/**
  * @author szy 
  * 速购人员信息表接口
  */
public interface IFastUserInfoService extends IBaseService<FastUserInfo>{

	public void sendUrgeOrderSms(Long memberId);
}
