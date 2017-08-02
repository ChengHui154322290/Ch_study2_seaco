package com.tp.proxy.ord;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.model.ord.CancelItem;
import com.tp.proxy.BaseProxy;
import com.tp.service.IBaseService;
import com.tp.service.ord.ICancelItemService;
/**
 * 取消单子项(商品)代理层
 * @author szy
 *
 */
@Service
public class CancelItemProxy extends BaseProxy<CancelItem>{

	@Autowired
	private ICancelItemService cancelItemService;

	@Override
	public IBaseService<CancelItem> getService() {
		return cancelItemService;
	}
}
