package com.tp.service.bse;

import java.util.List;

import com.tp.model.bse.Spec;
import com.tp.service.IBaseService;
/**
  * @author szy
  * 规格表接口
  */
public interface ISpecService extends IBaseService<Spec>{
	/**
	 * 新增
	 * @param spec
	 * @param ids
	 * @return
	 */
	public void addSpecAndSpecLinkMethod(Spec spec, Long[] ids);
	
	/**
	 * 更新
	 * @param insertSpec
	 * @param ids
	 */
	public void updateSpecAndLinked(Spec insertSpec, Long[] ids);
	
	public void deleteSpecGroupLinked(Long specGroupId, Long specId);

	public List<Spec> selectListSpec(List<Long> idsList, Integer i);
}
