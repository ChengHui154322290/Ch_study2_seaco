package com.tp.seller.ao.item;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.dto.prd.enums.AuditResultEnum;
import com.tp.model.prd.ItemSku;
import com.tp.result.prd.ItemAuditDetailResuslt;
import com.tp.service.prd.IItemAuditDetailService;
import com.tp.service.prd.IItemSkuService;

@Service
public class AtuditDetailAO {

	@Autowired
	private IItemAuditDetailService itemAuditDetailService;
	@Autowired
	private IItemSkuService itemSkuService;
	
	public 	List<ItemAuditDetailResuslt> getAtuditDetailResultList(Long skuId){
		List<ItemAuditDetailResuslt> list = itemAuditDetailService.selectAuditDetailBySellerItemSkuId(skuId);
		return list;
	}
	
	/**
	 * 
	 * <pre>
	 * 将拒绝类型封装为map
	 * </pre>
	 *
	 * @return
	 */
	public Map<String, String> initRejectType() {
		
		Map<String, String> rejectTypes = new HashMap<String, String>();
		AuditResultEnum[] values = AuditResultEnum.values();
		for (AuditResultEnum sTax : values) {
			rejectTypes.put(sTax.getValue().toString(), sTax.getKey());
		}
		return rejectTypes;
	}

	public List<ItemAuditDetailResuslt> getAtuditDetailResultListBySkuCode(String skuCode) {
		ItemSku skuDO =new ItemSku();
		skuDO.setSku(skuCode);
		List<ItemSku> list = itemSkuService.queryByObject(skuDO);
		if(CollectionUtils.isNotEmpty(list)){
			Long skuId = list.get(0).getId();
			return this.getAtuditDetailResultList(skuId);
		} else {
			return null;
		}	
	}
}
