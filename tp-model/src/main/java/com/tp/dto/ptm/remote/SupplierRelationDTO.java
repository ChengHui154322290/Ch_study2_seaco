package com.tp.dto.ptm.remote;
import org.springframework.beans.BeanUtils;

import com.tp.model.ptm.PlatformSupplierRelation;

/**
 * 供应商关系DTO
 * 
 * @author 项硕
 * @version 2015年5月7日 下午3:51:58
 */
public class SupplierRelationDTO extends PlatformSupplierRelation {

	private static final long serialVersionUID = 823478536517129535L;
	
	private String supplierName;
	
	/**
	 * DO转DTO
	 * 
	 * @param relation
	 * @return
	 */
	public static SupplierRelationDTO toDTO(PlatformSupplierRelation relation) {
		if (null != relation) {
			SupplierRelationDTO dto = new SupplierRelationDTO();
			BeanUtils.copyProperties(relation, dto);
			return dto;
		}
		return null;
	}
	
	public String getSupplierName() {
		return supplierName;
	}
	public void setSupplierName(String supplierName) {
		this.supplierName = supplierName;
	}
}
