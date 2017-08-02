package com.tp.proxy.cms;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.vo.PageInfo;
import com.tp.model.cms.ItemElement;
import com.tp.proxy.BaseProxy;
import com.tp.service.IBaseService;
import com.tp.service.cms.IItemElementService;
/**
 * 商品元素表代理层
 * @author szy
 *
 */
@Service
public class ItemElementProxy extends BaseProxy<ItemElement>{

	@Autowired
	private IItemElementService itemElementService;

	@Override
	public IBaseService<ItemElement> getService() {
		return itemElementService;
	}
	public PageInfo<ItemElement> queryPageListByItemElementAndStartPageSize(ItemElement cmsItemElement,int startPage,int pageSize){
		return itemElementService.queryPageListByItemElementAndStartPageSize(cmsItemElement, startPage, pageSize);
	}
	
	public int deleteByIds(List<Long> ids) throws Exception {
		return itemElementService.deleteByIds(ids);
	}
}
