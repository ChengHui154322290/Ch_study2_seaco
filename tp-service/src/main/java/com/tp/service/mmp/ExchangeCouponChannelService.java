package com.tp.service.mmp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.dao.BaseDao;
import com.tp.dao.mmp.ExchangeCouponChannelDao;
import com.tp.model.mmp.ExchangeCouponChannel;
import com.tp.service.BaseService;
import com.tp.service.mmp.IExchangeCouponChannelService;

@Service
public class ExchangeCouponChannelService extends BaseService<ExchangeCouponChannel> implements IExchangeCouponChannelService {

	@Autowired
	private ExchangeCouponChannelDao exchangeCouponChannelDao;
	
	@Override
	public BaseDao<ExchangeCouponChannel> getDao() {
		return exchangeCouponChannelDao;
	}

}
