package com.tp.service.ptm;

import java.util.List;

import com.tp.model.ptm.PlatformSupplierRelation;
import com.tp.service.IBaseService;
/**
  * @author szy
  * 开放平台供应商关联表接口
  */
public interface IPlatformSupplierRelationService extends IBaseService<PlatformSupplierRelation>{

	/**
	 * @param appkey
	 * @return
	 */
	List<PlatformSupplierRelation> selectListByAppkey(String appkey);
}
