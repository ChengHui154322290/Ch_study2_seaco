package com.tp.service.prd;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.dao.BaseDao;
import com.tp.dao.prd.ItemSellerExportInfoDao;
import com.tp.model.prd.ItemSellerExportInfo;
import com.tp.service.BaseService;
import com.tp.service.prd.IItemSellerExportInfoService;

@Service
public class ItemSellerExportInfoService extends BaseService<ItemSellerExportInfo> implements IItemSellerExportInfoService {

	@Autowired
	private ItemSellerExportInfoDao itemSellerExportInfoDao;
	
	@Override
	public BaseDao<ItemSellerExportInfo> getDao() {
		return itemSellerExportInfoDao;
	}

}
