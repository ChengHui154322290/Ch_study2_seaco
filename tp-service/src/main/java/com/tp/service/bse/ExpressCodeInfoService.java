package com.tp.service.bse;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.dao.BaseDao;
import com.tp.dao.bse.ExpressCodeInfoDao;
import com.tp.dao.bse.ExpressInfoDao;
import com.tp.model.bse.ExpressCodeInfo;
import com.tp.model.bse.ExpressInfo;
import com.tp.service.BaseService;
import com.tp.service.bse.IExpressCodeInfoService;

@Service
public class ExpressCodeInfoService extends BaseService<ExpressCodeInfo> implements IExpressCodeInfoService {

	@Autowired
	private ExpressCodeInfoDao expressCodeInfoDao;
	
	@Autowired
	private ExpressInfoDao expressInfoDao;
	
	@Override
	public BaseDao<ExpressCodeInfo> getDao() {
		return expressCodeInfoDao;
	}
	
	@Override
	public List<ExpressInfo> selectAllExpressCode() {
		ExpressInfo expressInfo = new ExpressInfo();
		return expressInfoDao.queryByObject(expressInfo);
	}
}
