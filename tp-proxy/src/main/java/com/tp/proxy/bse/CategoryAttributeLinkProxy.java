package com.tp.proxy.bse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.model.bse.CategoryAttributeLink;
import com.tp.proxy.BaseProxy;
import com.tp.service.IBaseService;
import com.tp.service.bse.ICategoryAttributeLinkService;
/**
 * 类别-属性-中间表代理层
 * @author szy
 *
 */
@Service
public class CategoryAttributeLinkProxy extends BaseProxy<CategoryAttributeLink>{

	@Autowired
	private ICategoryAttributeLinkService categoryAttributeLinkService;

	@Override
	public IBaseService<CategoryAttributeLink> getService() {
		return categoryAttributeLinkService;
	}
}
