package com.tp.service.mmp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.dao.BaseDao;
import com.tp.dao.mmp.FullDiscountDao;
import com.tp.model.mmp.FullDiscount;
import com.tp.service.BaseService;
import com.tp.service.mmp.IFullDiscountService;

@Service
public class FullDiscountService extends BaseService<FullDiscount> implements IFullDiscountService {

	@Autowired
	private FullDiscountDao fullDiscountDao;
	
	@Override
	public BaseDao<FullDiscount> getDao() {
		return fullDiscountDao;
	}

}
