package com.tp.proxy.bse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.model.bse.CategoryCertLink;
import com.tp.proxy.BaseProxy;
import com.tp.service.IBaseService;
import com.tp.service.bse.ICategoryCertLinkService;
/**
 * 经营品类所需要的资质代理层
 * @author szy
 *
 */
@Service
public class CategoryCertLinkProxy extends BaseProxy<CategoryCertLink>{

	@Autowired
	private ICategoryCertLinkService categoryCertLinkService;

	@Override
	public IBaseService<CategoryCertLink> getService() {
		return categoryCertLinkService;
	}
}
