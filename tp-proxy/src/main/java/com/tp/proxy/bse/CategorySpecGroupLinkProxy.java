package com.tp.proxy.bse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.model.bse.CategorySpecGroupLink;
import com.tp.proxy.BaseProxy;
import com.tp.service.IBaseService;
import com.tp.service.bse.ICategorySpecGroupLinkService;
/**
 *  小类与规格关联表
代理层
 * @author szy
 *
 */
@Service
public class CategorySpecGroupLinkProxy extends BaseProxy<CategorySpecGroupLink>{

	@Autowired
	private ICategorySpecGroupLinkService categorySpecGroupLinkService;

	@Override
	public IBaseService<CategorySpecGroupLink> getService() {
		return categorySpecGroupLinkService;
	}
}
