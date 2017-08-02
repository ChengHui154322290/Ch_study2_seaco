package com.tp.proxy.bse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.model.bse.CategoryCopy;
import com.tp.proxy.BaseProxy;
import com.tp.service.IBaseService;
import com.tp.service.bse.ICategoryCopyService;
/**
 * 商品类别代理层
 * @author szy
 *
 */
@Service
public class CategoryCopyProxy extends BaseProxy<CategoryCopy>{

	@Autowired
	private ICategoryCopyService categoryCopyService;

	@Override
	public IBaseService<CategoryCopy> getService() {
		return categoryCopyService;
	}
}
