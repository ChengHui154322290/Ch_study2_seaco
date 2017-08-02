package com.tp.proxy.mmp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.model.mmp.TieredGroupJoinInfo;
import com.tp.proxy.BaseProxy;
import com.tp.service.IBaseService;
import com.tp.service.mmp.ITieredGroupJoinInfoService;
/**
 * 阶梯团参团信息表代理层
 * @author szy
 *
 */
@Service
public class TieredGroupJoinInfoProxy extends BaseProxy<TieredGroupJoinInfo>{

	@Autowired
	private ITieredGroupJoinInfoService tieredGroupJoinInfoService;

	@Override
	public IBaseService<TieredGroupJoinInfo> getService() {
		return tieredGroupJoinInfoService;
	}
}
