package com.tp.service.sup;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.dao.BaseDao;
import com.tp.dao.sup.SupplierShopDao;
import com.tp.dto.prd.DetailOpenDto;
import com.tp.dto.prd.InfoOpenDto;
import com.tp.dto.prd.ItemDetailOpenDto;
import com.tp.dto.prd.PicturesOpenDto;
import com.tp.dto.sup.SupplierShopDto;
import com.tp.model.sup.SupplierShop;
import com.tp.service.BaseService;
import com.tp.service.sup.ISupplierShopService;

import org.springframework.util.CollectionUtils;

@Service
public class SupplierShopService extends BaseService<SupplierShop> implements ISupplierShopService {

	@Autowired
	private SupplierShopDao supplierShopDao;
	
	@Override
	public BaseDao<SupplierShop> getDao() {
		return supplierShopDao;
	}
     
	@Override
	public SupplierShop getSupplierShopInfo(Long supplierId) {
		 SupplierShop  supplierShop=new  SupplierShop();
		 supplierShop.setSupplierId(supplierId);
		 List<SupplierShop> supplierShopList =supplierShopDao.queryByObject(supplierShop);
		 if(supplierShopList.size()>0){
			 return supplierShopList.get(0);
		 }
		return null;
	}

public 	List<SupplierShop> queryBySupplierIds(List<Long> ids){
	if(CollectionUtils.isEmpty(ids)) return Collections.EMPTY_LIST;
		return  supplierShopDao.queryBySupplierIds(ids);
	}


}
