package com.tp.service.ptm;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.dao.BaseDao;
import com.tp.dao.ptm.PlatformAccountDao;
import com.tp.dao.ptm.PlatformSupplierRelationDao;
import com.tp.model.ptm.PlatformAccount;
import com.tp.model.ptm.PlatformSupplierRelation;
import com.tp.service.BaseService;
import com.tp.service.ptm.IPlatformSupplierRelationService;

@Service
public class PlatformSupplierRelationService extends BaseService<PlatformSupplierRelation> implements IPlatformSupplierRelationService {

	@Autowired
	private PlatformSupplierRelationDao platformSupplierRelationDao;
	@Autowired
	private PlatformAccountDao platformAccountDao;
	
	@Override
	public BaseDao<PlatformSupplierRelation> getDao() {
		return platformSupplierRelationDao;
	}

	/* (non-Javadoc)
	 * @see com.tp.service.ptm.IPlatformSupplierRelationService#selectListByAppkey(java.lang.Long)
	 */
	@Override
	public List<PlatformSupplierRelation> selectListByAppkey(String appkey) {
		PlatformAccount accountQuery = new PlatformAccount();
		accountQuery.setAppkey(appkey);
		List<PlatformAccount> account = platformAccountDao.queryByObject(accountQuery);
		if(CollectionUtils.isEmpty(account)) {
			return null;
		}
		
		PlatformSupplierRelation qo = new PlatformSupplierRelation();
		qo.setAccountId(account.get(0).getId());
		return platformSupplierRelationDao.queryByObject(qo);
	}
}
