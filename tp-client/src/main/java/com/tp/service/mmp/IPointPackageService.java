package com.tp.service.mmp;

import java.util.Map;

import com.tp.model.mmp.PointPackage;
import com.tp.service.IBaseService;
/**
  * @author szy 
  * 会员积分打包记录接口
  */
public interface IPointPackageService extends IBaseService<PointPackage>{

	/**
	 * 使用积分
	 * @param pointPackage
	 * @return 返回积分包使用积分详情
	 */
	public Map<PointPackage,Integer> updatePointByMinusPoint(PointPackage pointPackage);

	/**
	 * 加积分（或返还积分）
	 * @param pointPackage
	 * @return
	 */
	public Integer updatePointByAddPoint(PointPackage pointPackage);

}
