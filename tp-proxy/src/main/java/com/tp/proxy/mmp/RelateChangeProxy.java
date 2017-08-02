package com.tp.proxy.mmp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.model.mmp.RelateChange;
import com.tp.proxy.BaseProxy;
import com.tp.service.IBaseService;
import com.tp.service.mmp.IRelateChangeService;
/**
 * 关联专题代理层
 * @author szy
 *
 */
@Service
public class RelateChangeProxy extends BaseProxy<RelateChange>{

	@Autowired
	private IRelateChangeService relateChangeService;

	@Override
	public IBaseService<RelateChange> getService() {
		return relateChangeService;
	}
}
