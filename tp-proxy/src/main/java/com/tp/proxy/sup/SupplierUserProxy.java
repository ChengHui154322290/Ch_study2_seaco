package com.tp.proxy.sup;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.vo.PageInfo;
import com.tp.model.sup.SupplierInfo;
import com.tp.model.sup.SupplierUser;
import com.tp.proxy.BaseProxy;
import com.tp.result.sup.SupplierResult;
import com.tp.service.IBaseService;
import com.tp.service.sup.IPurchasingManagementService;
import com.tp.service.sup.ISupplierUserService;
/**
 * 商家平台用户主表代理层
 * @author szy
 *
 */
@Service
public class SupplierUserProxy extends BaseProxy<SupplierUser>{

	@Autowired
	private ISupplierUserService supplierUserService;
	@Autowired
	private IPurchasingManagementService  purchasingManagementService;

	@Override
	public IBaseService<SupplierUser> getService() {
		return supplierUserService;
	}
	
	/**
     * 根据供应商Id查询商家平台信息
     */
    public SupplierUser getSupplierUserById(Long spId){
    	return supplierUserService.getSupplierUserBySupplierId(spId);
    }
    /**
     * 根据供应商Id查询商家平台信息
     */
    public SupplierUser getSupplierUserByUserId(Long userId){
    	return supplierUserService.getSupplierUserByUserId(userId);
    }
    
    /**
     * 根据供应商用户IDId获取商家平台用户信息
     */
    public SupplierInfo getSupplierInfoByUserId(final Long userId) {
    	Map<String,Object> params = new HashMap<String,Object>();
    	SupplierUser supplierUser= supplierUserService.getSupplierUserByUserId(userId);
    	SupplierResult result = purchasingManagementService.getSupplierListWithCondition(supplierUser.getSupplierId(), null,null, 1, 2);
		if(result.getSupplierInfoList().size()>0){
			SupplierInfo supplierInfo=result.getSupplierInfoList().get(0);
			return supplierInfo;
		}else{
			return null;
		}
    	
    }
    
}


