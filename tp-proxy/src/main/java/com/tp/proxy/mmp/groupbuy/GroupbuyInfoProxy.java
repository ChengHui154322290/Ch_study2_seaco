package com.tp.proxy.mmp.groupbuy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.model.mmp.GroupbuyInfo;
import com.tp.proxy.BaseProxy;
import com.tp.service.IBaseService;
import com.tp.service.mmp.groupbuy.IGroupbuyInfoService;
/**
 * 代理层
 * @author szy
 *
 */
@Service
public class GroupbuyInfoProxy extends BaseProxy<GroupbuyInfo>{

	@Autowired
	private IGroupbuyInfoService groupbuyInfoService;

	@Override
	public IBaseService<GroupbuyInfo> getService() {
		return groupbuyInfoService;
	}
}
