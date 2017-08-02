package com.tp.service.cms;

import java.util.List;

import com.tp.common.vo.PageInfo;
import com.tp.exception.CmsServiceException;
import com.tp.model.cms.ItemElement;
import com.tp.service.IBaseService;
 /**
 * 商品元素 Service
 */
public interface IItemElementService extends IBaseService<ItemElement>{

	
	/**
	 * 批量删除
	 * @param ids
	 * @return
	 * @throws Exception
	 */
	int deleteByIds(List<Long> ids) throws Exception;

	/**
	 * 动态返回 商品元素 分页列表
	 * @param cmsItemElement
	 * @param startPage 起始页
	 * @param pageSize 每页记录数
	 * @return Page<ItemElement>
	 * @throws CmsServiceException
	 
	 */
	PageInfo<ItemElement> queryPageListByItemElementAndStartPageSize(ItemElement cmsItemElement,int startPage,int pageSize);

}
