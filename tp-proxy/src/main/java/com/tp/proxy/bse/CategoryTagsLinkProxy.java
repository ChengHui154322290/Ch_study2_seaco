package com.tp.proxy.bse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.model.bse.CategoryTagsLink;
import com.tp.proxy.BaseProxy;
import com.tp.service.IBaseService;
import com.tp.service.bse.ICategoryTagsLinkService;
/**
 * 品类与标签关联信息表代理层
 * @author szy
 *
 */
@Service
public class CategoryTagsLinkProxy extends BaseProxy<CategoryTagsLink>{

	@Autowired
	private ICategoryTagsLinkService categoryTagsLinkService;

	@Override
	public IBaseService<CategoryTagsLink> getService() {
		return categoryTagsLinkService;
	}
}
