package com.tp.proxy.sup;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.model.sup.PurchaseProduct;
import com.tp.proxy.BaseProxy;
import com.tp.service.IBaseService;
import com.tp.service.sup.IPurchaseProductService;
/**
 * 采购[代销]订单[退货单]-商品表代理层
 * @author szy
 *
 */
@Service
public class PurchaseProductProxy extends BaseProxy<PurchaseProduct>{

	@Autowired
	private IPurchaseProductService purchaseProductService;

	@Override
	public IBaseService<PurchaseProduct> getService() {
		return purchaseProductService;
	}
}
