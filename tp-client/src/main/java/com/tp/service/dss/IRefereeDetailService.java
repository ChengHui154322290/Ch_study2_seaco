package com.tp.service.dss;

import java.util.List;

import com.tp.model.dss.RefereeDetail;
import com.tp.service.IBaseService;
/**
  * @author szy 
  * 推荐新人佣金明细表接口
  */
public interface IRefereeDetailService extends IBaseService<RefereeDetail>{
	List<RefereeDetail> queryRefereeByDetailCode( List<Long> detaiCodeList);
}
