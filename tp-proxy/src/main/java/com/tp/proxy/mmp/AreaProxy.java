package com.tp.proxy.mmp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.model.mmp.Area;
import com.tp.proxy.BaseProxy;
import com.tp.service.IBaseService;
import com.tp.service.mmp.IAreaService;
/**
 * 区域代理层
 * @author szy
 *
 */
@Service
public class AreaProxy extends BaseProxy<Area>{

	@Autowired
	private IAreaService areaService;

	@Override
	public IBaseService<Area> getService() {
		return areaService;
	}
}
