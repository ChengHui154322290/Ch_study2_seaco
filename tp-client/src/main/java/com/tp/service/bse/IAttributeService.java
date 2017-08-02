package com.tp.service.bse;

import com.tp.model.bse.Attribute;
import com.tp.service.IBaseService;
/**
  * @author szy
  * 属性值表接口
  */
public interface IAttributeService extends IBaseService<Attribute>{
	/**
	 * 
	 * <pre>
	 *  在ids不为空的情况下插入 attribute 及其相关的小类
	 * </pre>
	 *
	 * @param insertAttributeDO
	 * @param ids
	 * @throws DAOException 
	 */
	void addAttributeAndCateLinked(Attribute insertAttribute, Long[] ids);
	
	/**
	 * 更新
	 * @param insertAttribute
	 * @param ids
	 */
	void updateAttributeAndCateLinked(Attribute insertAttribute, Long[] ids);
}
