package com.tp.proxy.mmp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.model.mmp.TieredGroupInfo;
import com.tp.proxy.BaseProxy;
import com.tp.service.IBaseService;
import com.tp.service.mmp.ITieredGroupInfoService;
/**
 * 阶梯团信息表代理层
 * @author szy
 *
 */
@Service
public class TieredGroupInfoProxy extends BaseProxy<TieredGroupInfo>{

	@Autowired
	private ITieredGroupInfoService tieredGroupInfoService;

	@Override
	public IBaseService<TieredGroupInfo> getService() {
		return tieredGroupInfoService;
	}
}
