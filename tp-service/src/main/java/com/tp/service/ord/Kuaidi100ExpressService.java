package com.tp.service.ord;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.dao.BaseDao;
import com.tp.dao.ord.Kuaidi100ExpressDao;
import com.tp.model.ord.Kuaidi100Express;
import com.tp.service.BaseService;
import com.tp.service.ord.IKuaidi100ExpressService;

@Service
public class Kuaidi100ExpressService extends BaseService<Kuaidi100Express> implements IKuaidi100ExpressService {

	@Autowired
	private Kuaidi100ExpressDao kuaidi100ExpressDao;
	
	@Override
	public BaseDao<Kuaidi100Express> getDao() {
		return kuaidi100ExpressDao;
	}
	@Override
	public Integer batchInsert(List<Kuaidi100Express> kuaidi100ExpressDOList) {
		return kuaidi100ExpressDao.batchInsert(kuaidi100ExpressDOList);
	}

	@Override
	public List<Kuaidi100Express> selectListBySubOrderCodeAndPackageNo(List<Kuaidi100Express> kuaidi100ExpressDOList) {
		return kuaidi100ExpressDao.selectListBySubOrderCodeAndPackageNo(kuaidi100ExpressDOList);
	}

	@Override
	public List<Kuaidi100Express> selectListByRejectNoAndPackageNo(List<Kuaidi100Express> kuaidi100ExpressDOList) {
		return kuaidi100ExpressDao.selectListByRejectNoAndPackageNo(kuaidi100ExpressDOList);
	}
	@Override
	public Integer deleteOldExpressInfo(Long code, String packageNo) {
		return kuaidi100ExpressDao.deleteOldExpressInfo(code, packageNo);
	}
}
