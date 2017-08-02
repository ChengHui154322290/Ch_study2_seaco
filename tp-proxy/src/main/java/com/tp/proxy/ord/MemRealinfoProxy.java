package com.tp.proxy.ord;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.model.ord.MemRealinfo;
import com.tp.proxy.BaseProxy;
import com.tp.service.IBaseService;
import com.tp.service.ord.IMemRealinfoService;
/**
 * 代理层
 * @author szy
 *
 */
@Service
public class MemRealinfoProxy extends BaseProxy<MemRealinfo>{

	@Autowired
	private IMemRealinfoService memRealinfoService;

	@Override
	public IBaseService<MemRealinfo> getService() {
		return memRealinfoService;
	}
}
