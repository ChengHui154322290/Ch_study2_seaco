package com.tp.proxy.mmp.groupbuy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.model.mmp.GroupbuyJoin;
import com.tp.proxy.BaseProxy;
import com.tp.service.IBaseService;
import com.tp.service.mmp.groupbuy.IGroupbuyJoinService;
/**
 * 阶梯团参团信息表代理层
 * @author szy
 *
 */
@Service
public class GroupbuyJoinProxy extends BaseProxy<GroupbuyJoin>{

	@Autowired
	private IGroupbuyJoinService groupbuyJoinService;

	@Override
	public IBaseService<GroupbuyJoin> getService() {
		return groupbuyJoinService;
	}
}
