package com.tp.service.pay;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.dao.BaseDao;
import com.tp.dao.pay.MerchantInfoDao;
import com.tp.model.pay.MerchantInfo;
import com.tp.service.BaseService;
import com.tp.service.pay.IMerchantInfoService;

@Service
public class MerchantInfoService extends BaseService<MerchantInfo> implements IMerchantInfoService {

	@Autowired
	private MerchantInfoDao merchantInfoDao;
	
	@Override
	public BaseDao<MerchantInfo> getDao() {
		return merchantInfoDao;
	}

}
