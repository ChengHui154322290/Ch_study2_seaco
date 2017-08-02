package com.tp.proxy.sup;

import java.util.List;

import com.tp.dto.common.ResultInfo;
import com.tp.model.sup.SupplierShop;
import com.tp.proxy.BaseProxy;
import com.tp.proxy.mmp.callBack.Callback;
import com.tp.service.IBaseService;
import com.tp.service.sup.ISupplierShopService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
/**
 * 商家平台店铺主表代理层
 * @author szy
 *
 */
@Service
public class SupplierShopProxy extends BaseProxy<SupplierShop>{
	private final static Logger LOGGER = LoggerFactory.getLogger(SupplierShopProxy.class);
	@Autowired
	private ISupplierShopService supplierShopService;

	@Override
	public IBaseService<SupplierShop> getService() {
		return supplierShopService;
	}
	/**
	 * 
	 * getSupplierShopInfo:(根据供应商ID 获取用户ID). <br/>  
	 * TODO(这里描述这个方法适用条件 – 可选).<br/>   
	 *  
	 * @author zhouguofeng  
	 * @param supplierId
	 * @return  
	 * @sinceJDK 1.8
	 */
	public SupplierShop getSupplierShopInfo(Long supplierId){
		return supplierShopService.getSupplierShopInfo(supplierId);
	}
	
	/**
	 * 
	 * getSupplierShopInfoByName:(根据店铺名查找信息). <br/>  
	 * TODO(这里描述这个方法适用条件 – 可选).<br/>   
	 *  
	 * @author zhouguofeng  
	 * @param shopName
	 * @return  
	 * @sinceJDK 1.8
	 */
	public SupplierShop getSupplierShopInfoByObject( SupplierShop supplierShop){
		 List<SupplierShop> supplierShopList =supplierShopService.queryByObject(supplierShop);
		 if(supplierShopList.size()>0){
			 return supplierShopList.get(0);
		 }
		return null;
	}

public 	ResultInfo<List<SupplierShop>>  queryBySupplierIds(List<Long> ids){
	ResultInfo<List<SupplierShop>> result = new ResultInfo<>();
	this.execute(result, new Callback() {
		@Override
		public void process() throws Exception {
			result.setData(supplierShopService.queryBySupplierIds(ids));
		}
	});
	return result;
}
	

}
