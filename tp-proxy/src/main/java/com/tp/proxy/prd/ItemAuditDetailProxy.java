package com.tp.proxy.prd;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.model.prd.ItemAuditDetail;
import com.tp.proxy.BaseProxy;
import com.tp.service.IBaseService;
import com.tp.service.prd.IItemAuditDetailService;
/**
 * 商家平台审核表代理层
 * @author szy
 *
 */
@Service
public class ItemAuditDetailProxy extends BaseProxy<ItemAuditDetail>{

	@Autowired
	private IItemAuditDetailService itemAuditDetailService;

	@Override
	public IBaseService<ItemAuditDetail> getService() {
		return itemAuditDetailService;
	}
}
