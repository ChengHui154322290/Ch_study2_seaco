package com.tp.proxy.app;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.model.app.StatisticQrcode;
import com.tp.proxy.BaseProxy;
import com.tp.service.IBaseService;
import com.tp.service.app.IStatisticQrcodeService;
/**
 *  二维码扫描统计代理层
 * @author szy
 *
 */
@Service
public class StatisticQrcodeProxy extends BaseProxy<StatisticQrcode>{

	@Autowired
	private IStatisticQrcodeService statisticQrcodeService;

	@Override
	public IBaseService<StatisticQrcode> getService() {
		return statisticQrcodeService;
	}
}
