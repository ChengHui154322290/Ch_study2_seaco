package com.tp.service.cms;

import java.util.List;

import javax.xml.rpc.ServiceException;

import com.tp.common.vo.PageInfo;
import com.tp.exception.CmsServiceException;
import com.tp.model.cms.Position;
import com.tp.service.IBaseService;
 /**
 * 位置管理 Service
 * @author szy
 */
public interface IPositionService extends IBaseService<Position>{

	/**
	 * 批量删除
	 * @param ids
	 * @return
	 * @throws Exception
	 */
	int deleteByIds(List<Long> ids) throws Exception;



	int update(Position cmsPositionDO) throws CmsServiceException;

}
