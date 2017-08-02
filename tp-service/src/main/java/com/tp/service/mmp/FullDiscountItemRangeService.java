package com.tp.service.mmp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.dao.BaseDao;
import com.tp.dao.mmp.FullDiscountItemRangeDao;
import com.tp.model.mmp.FullDiscountItemRange;
import com.tp.service.BaseService;
import com.tp.service.mmp.IFullDiscountItemRangeService;

@Service
public class FullDiscountItemRangeService extends BaseService<FullDiscountItemRange> implements IFullDiscountItemRangeService {

	@Autowired
	private FullDiscountItemRangeDao fullDiscountItemRangeDao;
	
	@Override
	public BaseDao<FullDiscountItemRange> getDao() {
		return fullDiscountItemRangeDao;
	}

}
