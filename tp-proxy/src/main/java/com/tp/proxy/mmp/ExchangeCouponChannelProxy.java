package com.tp.proxy.mmp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.model.mmp.ExchangeCouponChannel;
import com.tp.proxy.BaseProxy;
import com.tp.service.IBaseService;
import com.tp.service.mmp.IExchangeCouponChannelService;
/**
 * 活动兑换码主表代理层
 * @author szy
 *
 */
@Service
public class ExchangeCouponChannelProxy extends BaseProxy<ExchangeCouponChannel>{

	@Autowired
	private IExchangeCouponChannelService exchangeCouponChannelService;

	@Override
	public IBaseService<ExchangeCouponChannel> getService() {
		return exchangeCouponChannelService;
	}
}
