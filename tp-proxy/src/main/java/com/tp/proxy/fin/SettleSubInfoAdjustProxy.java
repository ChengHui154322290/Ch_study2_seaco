package com.tp.proxy.fin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.model.fin.SettleSubInfoAdjust;
import com.tp.proxy.BaseProxy;
import com.tp.service.IBaseService;
import com.tp.service.fin.ISettleSubInfoAdjustService;
/**
 * 财务调整金额表代理层
 * @author szy
 *
 */
@Service
public class SettleSubInfoAdjustProxy extends BaseProxy<SettleSubInfoAdjust>{

	@Autowired
	private ISettleSubInfoAdjustService settleSubInfoAdjustService;

	@Override
	public IBaseService<SettleSubInfoAdjust> getService() {
		return settleSubInfoAdjustService;
	}
}
