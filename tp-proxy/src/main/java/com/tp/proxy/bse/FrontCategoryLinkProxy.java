package com.tp.proxy.bse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.model.bse.FrontCategoryLink;
import com.tp.proxy.BaseProxy;
import com.tp.service.IBaseService;
import com.tp.service.bse.IFrontCategoryLinkService;
/**
 * 前台类目跳转链接表代理层
 * @author szy
 *
 */
@Service
public class FrontCategoryLinkProxy extends BaseProxy<FrontCategoryLink>{

	@Autowired
	private IFrontCategoryLinkService frontCategoryLinkService;

	@Override
	public IBaseService<FrontCategoryLink> getService() {
		return frontCategoryLinkService;
	}
}
