package com.tp.service.prd;

import java.util.List;

import com.tp.model.prd.ItemAuditDetail;
import com.tp.result.prd.ItemAuditDetailResuslt;
import com.tp.service.IBaseService;
/**
  * @author szy
  * 商家平台审核表接口
  */
public interface IItemAuditDetailService extends IBaseService<ItemAuditDetail>{

	void insertAuditDetail(ItemAuditDetail auditMessage);

	List<ItemAuditDetailResuslt> selectAuditDetailBySellerItemSkuId(Long skuId);

}
