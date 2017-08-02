package com.tp.proxy.mem;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.model.mem.MemberTradeInfo;
import com.tp.proxy.BaseProxy;
import com.tp.service.IBaseService;
import com.tp.service.mem.IMemberTradeInfoService;
/**
 * 用户交易方式信息表代理层
 * @author szy
 *
 */
@Service
public class MemberTradeInfoProxy extends BaseProxy<MemberTradeInfo>{

	@Autowired
	private IMemberTradeInfoService memberTradeInfoService;

	@Override
	public IBaseService<MemberTradeInfo> getService() {
		return memberTradeInfoService;
	}
}
