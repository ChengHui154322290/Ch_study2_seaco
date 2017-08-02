package com.tp.service.cms;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.dao.BaseDao;
import com.tp.common.vo.PageInfo;
import com.tp.dao.cms.ItemElementDao;
import com.tp.model.cms.ItemElement;
import com.tp.service.BaseService;
import com.tp.service.cms.IItemElementService;

@Service(value="itemElementService")
public class ItemElementService extends BaseService<ItemElement> implements IItemElementService {

	private Log logger = LogFactory.getLog(this.getClass());

	@Autowired
	private ItemElementDao itemElementDao;


	public PageInfo<ItemElement> queryPageListByItemElementAndStartPageSize(ItemElement cmsItemElementDO,int startPage,int pageSize){
		if (cmsItemElementDO != null && startPage>0 && pageSize>0) {
			cmsItemElementDO.setStartPage(startPage);
			cmsItemElementDO.setPageSize(pageSize);
			PageInfo<ItemElement> pageInfo = new PageInfo<ItemElement>();
			pageInfo = queryPageByObject(cmsItemElementDO, pageInfo);
			return pageInfo;
		}
		return new PageInfo<ItemElement>();
	}
	
	@Override
	public int deleteByIds(List<Long> ids) throws Exception {
		int count = 0;
		try {
			count = itemElementDao.deleteByIds(ids);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("批量删除报错", e);
		}
		return count;
	}

	@Override
	public BaseDao<ItemElement> getDao() {
		return itemElementDao;
	}

}
