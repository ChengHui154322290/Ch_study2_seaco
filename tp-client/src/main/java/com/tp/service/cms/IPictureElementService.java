package com.tp.service.cms;

import java.util.List;

import com.tp.common.vo.PageInfo;
import com.tp.dto.cms.CmsPictureElementDTO;
import com.tp.exception.CmsServiceException;
import com.tp.model.cms.PictureElement;
import com.tp.service.IBaseService;
 /**
 * 图片元素 Service
 */
public interface IPictureElementService extends IBaseService<PictureElement>{

	
	/**
	 * 批量删除
	 * @param ids
	 * @return
	 * @throws Exception
	 */
	int deleteByIds(List<Long> ids) throws Exception;

	/**
	 * 根据  图片元素 动态返回记录数
	 * @param cmsPictureElement
	 * @return 记录数
	 * @throws CmsServiceException
	 * 
	 */


	/**
	 * 动态返回 图片元素 分页列表
	 * @param cmsPictureElement
	 * @param startPage 起始页
	 * @param pageSize 每页记录数
	 * @return Page<PictureElement>
	 * @throws CmsServiceException
	 * 
	 */
	PageInfo<CmsPictureElementDTO> queryPageListByPictureElementAndStartPageSize(PictureElement cmsPictureElement,int startPage,int pageSize);

}
