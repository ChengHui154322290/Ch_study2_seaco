package com.tp.proxy.prd;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.model.prd.ItemAuditRejectInfo;
import com.tp.proxy.BaseProxy;
import com.tp.service.IBaseService;
import com.tp.service.prd.IItemAuditRejectInfoService;
/**
 * 驳回详情代理层
 * @author szy
 *
 */
@Service
public class ItemAuditRejectInfoProxy extends BaseProxy<ItemAuditRejectInfo>{

	@Autowired
	private IItemAuditRejectInfoService itemAuditRejectInfoService;

	@Override
	public IBaseService<ItemAuditRejectInfo> getService() {
		return itemAuditRejectInfoService;
	}
}
