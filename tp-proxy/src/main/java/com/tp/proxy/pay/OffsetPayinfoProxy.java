package com.tp.proxy.pay;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.model.pay.OffsetPayinfo;
import com.tp.proxy.BaseProxy;
import com.tp.service.IBaseService;
import com.tp.service.pay.IOffsetPayinfoService;
/**
 * 代理层
 * @author szy
 *
 */
@Service
public class OffsetPayinfoProxy extends BaseProxy<OffsetPayinfo>{

	@Autowired
	private IOffsetPayinfoService offsetPayinfoService;

	@Override
	public IBaseService<OffsetPayinfo> getService() {
		return offsetPayinfoService;
	}
}
