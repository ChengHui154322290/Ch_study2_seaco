package com.tp.service.sup;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.dao.BaseDao;
import com.tp.dao.sup.SupplierUserDao;
import com.tp.model.sup.SupplierUser;
import com.tp.service.BaseService;
import com.tp.service.sup.ISupplierUserService;

@Service
public class SupplierUserService extends BaseService<SupplierUser> implements ISupplierUserService {

	@Autowired
	private SupplierUserDao supplierUserDao;
	
	@Override
	public BaseDao<SupplierUser> getDao() {
		return supplierUserDao;
	}

    /** 
     * 根据供应商Id获取商家平台信息
     */
    @Override
    public SupplierUser getSupplierUserBySupplierId(final Long spId) {
    	Map<String,Object> params = new HashMap<String,Object>();
    	params.put("supplierId", spId);
    	return super.queryUniqueByParams(params);
    }
    
    /**
     * 根据供应商用户IDId获取商家平台用户信息
     */
    public SupplierUser getSupplierUserByUserId(final Long userId) {
    	Map<String,Object> params = new HashMap<String,Object>();
    	params.put("id", userId);
    	return super.queryUniqueByParams(params);
    }
    /**
     * 根据供应商用户IDId获取商家平台用户信息
     */
    public SupplierUser getSupplierUserByUserName(final String name) {
    	Map<String,Object> params = new HashMap<String,Object>();
    	params.put("loginName", name);
    	return super.queryUniqueByParams(params);
    }
    
    
}
