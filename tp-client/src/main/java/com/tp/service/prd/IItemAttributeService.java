package com.tp.service.prd;

import java.util.List;

import com.tp.model.prd.ItemAttribute;
import com.tp.service.IBaseService;
/**
  * @author szy
  * 商品的属性组接口
  */
public interface IItemAttributeService extends IBaseService<ItemAttribute>{

	List<ItemAttribute> selectAttrListByDetailIdFromMaster(Long id);

}
