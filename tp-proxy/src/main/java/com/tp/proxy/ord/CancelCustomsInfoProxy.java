package com.tp.proxy.ord;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.model.ord.CancelCustomsInfo;
import com.tp.proxy.BaseProxy;
import com.tp.service.IBaseService;
import com.tp.service.ord.ICancelCustomsInfoService;
/**
 * 取消海淘单海关申报代理层
 * @author szy
 *
 */
@Service
public class CancelCustomsInfoProxy extends BaseProxy<CancelCustomsInfo>{

	@Autowired
	private ICancelCustomsInfoService cancelCustomsInfoService;

	@Override
	public IBaseService<CancelCustomsInfo> getService() {
		return cancelCustomsInfoService;
	}
}
