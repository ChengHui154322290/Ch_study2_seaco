package com.tp.service.prd;

import com.tp.model.prd.ItemDescMobile;
import com.tp.service.IBaseService;
/**
  * @author szy
  * 商品介绍手机版接口
  */
public interface IItemDescMobileService extends IBaseService<ItemDescMobile>{

	ItemDescMobile selectByDetailFromMaster(Long id);

}
