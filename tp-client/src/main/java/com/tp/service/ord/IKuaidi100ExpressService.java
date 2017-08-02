package com.tp.service.ord;

import java.util.List;

import com.tp.model.ord.Kuaidi100Express;
import com.tp.service.IBaseService;
/**
  * @author szy
  * 快递100推送的快递日志记录接口
  */
public interface IKuaidi100ExpressService extends IBaseService<Kuaidi100Express>{

	Integer batchInsert(List<Kuaidi100Express> kuaidi100ExpressDOList);

	List<Kuaidi100Express> selectListBySubOrderCodeAndPackageNo(
			List<Kuaidi100Express> kuaidi100ExpressDOList);

	Integer deleteOldExpressInfo(Long code, String packageNo);

	List<Kuaidi100Express> selectListByRejectNoAndPackageNo(List<Kuaidi100Express> kuaidi100ExpressDOList);

}
