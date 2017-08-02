package com.tp.service.mmp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.dao.BaseDao;
import com.tp.dao.mmp.PointPackageDetailDao;
import com.tp.model.mmp.PointPackageDetail;
import com.tp.service.BaseService;
import com.tp.service.mmp.IPointPackageDetailService;

@Service
public class PointPackageDetailService extends BaseService<PointPackageDetail> implements IPointPackageDetailService {

	@Autowired
	private PointPackageDetailDao pointPackageDetailDao;
	
	@Override
	public BaseDao<PointPackageDetail> getDao() {
		return pointPackageDetailDao;
	}

}
