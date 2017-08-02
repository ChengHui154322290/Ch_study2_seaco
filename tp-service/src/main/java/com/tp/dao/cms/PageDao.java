package com.tp.dao.cms;

import java.util.List;

import com.tp.common.dao.BaseDao;
import com.tp.dto.cms.query.CmsPageQuery;
import com.tp.model.cms.Page;

public interface PageDao extends BaseDao<Page> {

	Long selectCountDynamic(CmsPageQuery query);

	List<Page> selectDynamicPageQuery(CmsPageQuery query);

	int deleteByIds(List<Long> ids);

}
