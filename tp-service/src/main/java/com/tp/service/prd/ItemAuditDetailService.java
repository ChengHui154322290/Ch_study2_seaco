package com.tp.service.prd;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tp.common.dao.BaseDao;
import com.tp.dao.prd.ItemAuditDetailDao;
import com.tp.dao.prd.ItemAuditRejectInfoDao;
import com.tp.model.prd.ItemAuditDetail;
import com.tp.model.prd.ItemAuditRejectInfo;
import com.tp.result.prd.ItemAuditDetailResuslt;
import com.tp.service.BaseService;
import com.tp.service.prd.IItemAuditDetailService;

@Service
public class ItemAuditDetailService extends BaseService<ItemAuditDetail> implements IItemAuditDetailService {

	@Autowired
	private ItemAuditDetailDao itemAuditDetailDao;
	@Autowired
	private ItemAuditRejectInfoDao itemAuditRejectInfoDao;
	
	@Override
	public BaseDao<ItemAuditDetail> getDao() {
		return itemAuditDetailDao;
	}

	@Override
	@Transactional
	public void insertAuditDetail(ItemAuditDetail ItemAuditDetail){		
		if(null == ItemAuditDetail){
			return;
		}
		itemAuditDetailDao.insert(ItemAuditDetail);
		Long id=ItemAuditDetail.getId();
		String[] listRejectKey = ItemAuditDetail.getListRejectKey();
		if(ArrayUtils.isEmpty(listRejectKey) || ItemAuditDetail.getAuditResult().equals("A")){
		    return;	
		}
		for (String rejectKey : listRejectKey) {
			 Integer keyId = Integer.parseInt(rejectKey);
			 ItemAuditRejectInfo ItemAuditRejectInfo = new ItemAuditRejectInfo();
			 ItemAuditRejectInfo.setAuditDetailId(id);
			 ItemAuditRejectInfo.setRejectType(keyId);
			 itemAuditRejectInfoDao.insert(ItemAuditRejectInfo);
		}
	
	}

	@Override
	public List<ItemAuditDetailResuslt> selectAuditDetailBySellerItemSkuId(
			Long skuId) {
		if(null == skuId){
			return null;
		}
		ItemAuditDetail  ItemAuditDetail=new ItemAuditDetail();
		ItemAuditDetail.setSellerSkuId(skuId);
	    List<ItemAuditDetail> list = new ArrayList<ItemAuditDetail>();
	    Map<String,Object> params = new HashMap<String,Object>();
	    params.put("sellerSkuId", skuId);
	    list = itemAuditDetailDao.queryByObject(ItemAuditDetail);
	    if(CollectionUtils.isEmpty(list)){
	    	return null;
	    }
	    List<ItemAuditDetailResuslt> resultList= new ArrayList<ItemAuditDetailResuslt>();
		params.clear();
	    for (ItemAuditDetail auditDetail : list) {
	    	ItemAuditDetailResuslt auditDetailResuslt = new ItemAuditDetailResuslt();
	    		
	    	auditDetailResuslt.setAuditDetail(auditDetail);
	    		if(auditDetail.getAuditResult().equals("R")){
	    			List<ItemAuditRejectInfo> listOfRejectInfo =new ArrayList<ItemAuditRejectInfo>();
	    			params.put("auditDetailId", auditDetail.getId());
	    			listOfRejectInfo = itemAuditRejectInfoDao.queryByParam(params);
	    		     if(CollectionUtils.isNotEmpty(listOfRejectInfo)){
	    		    	 List<Integer> keyList =new ArrayList<Integer>();
	    		    	 for (ItemAuditRejectInfo rejectInfo : listOfRejectInfo) {
	    		    		 keyList.add(rejectInfo.getRejectType());
						}
	    		    	 auditDetailResuslt.setRejectTypeList(keyList);
	    		     }
	    		}
	    		resultList.add(auditDetailResuslt);
		}
		return resultList;
	}

}
