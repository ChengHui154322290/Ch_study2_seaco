package com.tp.service.cms;

import java.util.List;

import javax.xml.rpc.ServiceException;

import com.tp.common.vo.PageInfo;
import com.tp.model.cms.WrittenElement;
import com.tp.service.IBaseService;
 /**
 * 文字元素 Service
 * @author szy
 */
public interface IWrittenElementService extends IBaseService<WrittenElement>{



	
	/**
	 * 批量删除
	 * @param ids
	 * @return
	 * @throws Exception
	 */
	int deleteByIds(List<Long> ids) throws Exception;


	/**
	 * 动态返回 文字元素 分页列表
	 * @param cmsWrittenElementDO
	 * @return Page<WrittenElement>
	 * @throws ServiceException
	 * @author szy
	 */
	PageInfo<WrittenElement> queryPageListByWrittenElement(WrittenElement cmsWrittenElementDO);

	/**
	 * 动态返回 文字元素 分页列表
	 * @param cmsWrittenElementDO
	 * @param startPage 起始页
	 * @param pageSize 每页记录数
	 * @return Page<WrittenElement>
	 * @throws ServiceException
	 * @author szy
	 */
	PageInfo<WrittenElement> queryPageListByWrittenElementAndStartPageSize(WrittenElement cmsWrittenElementDO,int startPage,int pageSize);

}
