package com.tp.proxy.ord;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.model.ord.RejectItem;
import com.tp.proxy.BaseProxy;
import com.tp.service.IBaseService;
import com.tp.service.ord.IRejectItemService;
/**
 * 退货单子项(商品)代理层
 * @author szy
 *
 */
@Service
public class RejectItemProxy extends BaseProxy<RejectItem>{

	@Autowired
	private IRejectItemService rejectItemService;

	@Override
	public IBaseService<RejectItem> getService() {
		return rejectItemService;
	}
}
