package com.tp.proxy.bse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.model.bse.FrontCategory;
import com.tp.proxy.BaseProxy;
import com.tp.service.IBaseService;
import com.tp.service.bse.IFrontCategoryService;
/**
 * 前台类目表代理层
 * @author szy
 *
 */
@Service
public class FrontCategoryProxy extends BaseProxy<FrontCategory>{

	@Autowired
	private IFrontCategoryService frontCategoryService;

	@Override
	public IBaseService<FrontCategory> getService() {
		return frontCategoryService;
	}
}
