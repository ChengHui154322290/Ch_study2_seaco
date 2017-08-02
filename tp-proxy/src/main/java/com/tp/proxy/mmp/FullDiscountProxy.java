package com.tp.proxy.mmp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.model.mmp.FullDiscount;
import com.tp.proxy.BaseProxy;
import com.tp.service.IBaseService;
import com.tp.service.mmp.IFullDiscountService;
/**
 * 优惠政策信息代理层
 * @author szy
 *
 */
@Service
public class FullDiscountProxy extends BaseProxy<FullDiscount>{

	@Autowired
	private IFullDiscountService fullDiscountService;

	@Override
	public IBaseService<FullDiscount> getService() {
		return fullDiscountService;
	}
}
