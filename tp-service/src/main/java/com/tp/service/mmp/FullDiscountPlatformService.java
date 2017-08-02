package com.tp.service.mmp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.dao.BaseDao;
import com.tp.dao.mmp.FullDiscountPlatformDao;
import com.tp.model.mmp.FullDiscountPlatform;
import com.tp.service.BaseService;
import com.tp.service.mmp.IFullDiscountPlatformService;

@Service
public class FullDiscountPlatformService extends BaseService<FullDiscountPlatform> implements IFullDiscountPlatformService {

	@Autowired
	private FullDiscountPlatformDao fullDiscountPlatformDao;
	
	@Override
	public BaseDao<FullDiscountPlatform> getDao() {
		return fullDiscountPlatformDao;
	}

}
