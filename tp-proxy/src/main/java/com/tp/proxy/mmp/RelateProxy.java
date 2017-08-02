package com.tp.proxy.mmp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.model.mmp.Relate;
import com.tp.proxy.BaseProxy;
import com.tp.service.IBaseService;
import com.tp.service.mmp.IRelateService;
/**
 * 关联专题代理层
 * @author szy
 *
 */
@Service
public class RelateProxy extends BaseProxy<Relate>{

	@Autowired
	private IRelateService relateService;

	@Override
	public IBaseService<Relate> getService() {
		return relateService;
	}
}
