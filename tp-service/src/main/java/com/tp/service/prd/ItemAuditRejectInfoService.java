package com.tp.service.prd;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.dao.BaseDao;
import com.tp.dao.prd.ItemAuditRejectInfoDao;
import com.tp.model.prd.ItemAuditRejectInfo;
import com.tp.service.BaseService;
import com.tp.service.prd.IItemAuditRejectInfoService;

@Service
public class ItemAuditRejectInfoService extends BaseService<ItemAuditRejectInfo> implements IItemAuditRejectInfoService {

	@Autowired
	private ItemAuditRejectInfoDao itemAuditRejectInfoDao;
	
	@Override
	public BaseDao<ItemAuditRejectInfo> getDao() {
		return itemAuditRejectInfoDao;
	}

}
