package com.tp.service.prd;

import com.tp.model.prd.ItemDesc;
import com.tp.service.IBaseService;
/**
  * @author szy
  * 商品介绍接口
  */
public interface IItemDescService extends IBaseService<ItemDesc>{

	ItemDesc selectByDetailIdFromMaster(Long id);

}
