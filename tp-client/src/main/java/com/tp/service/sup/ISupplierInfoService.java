package com.tp.service.sup;

import java.util.List;

import com.tp.dto.common.ResultInfo;
import com.tp.model.sup.AuditRecords;
import com.tp.model.sup.SupplierAttach;
import com.tp.model.sup.SupplierInfo;
import com.tp.result.sup.SupplierDTO;
import com.tp.service.IBaseService;
/**
  * @author szy
  * 供应商主表接口
  */
public interface ISupplierInfoService extends IBaseService<SupplierInfo>{
	/**
     * <pre>
     * 供应商审核
     * </pre>
     *
     * @param supplierDo
     * @param status
     * @param record
     * @return
     * @throws Exception
     */
	ResultInfo<Boolean> auditSupplier(final SupplierInfo supplierInfo, final Integer auditStatus, final AuditRecords record);
	/**
     * <pre>
     * 保存供应商信息
     * </pre>
     *
     * @param supplierAttachDTO
     * @return
     */
	ResultInfo<Boolean> saveSupplierLicenInfo(SupplierAttach supplierAttach,Long supplierId);
	
	/**
	 * 查询供应商全部信息 
	 * @param supplierId
	 * @return
	 */
	SupplierDTO queryAllInfoBySupplierId(final Long supplierId);
	/**
     * <pre>
     * 保存供应商基本信息
     * </pre>
     *
     * @param supplierAttachDTO
     * @return
     */
	ResultInfo<Long> saveSupplierBaseInfo(final SupplierDTO supplieDTO);
	
	/**
	 * 
	 * @param supplierDTO
	 * @return
	 */
	ResultInfo<Boolean> updateSupplierInfo(final SupplierDTO supplierDTO);
	
	/**
	 * 根据供应商ID列表获取供应商信息列表
	 * @param ids
	 * @return
	 */
	List<SupplierInfo> querySupplierInfoByIds(List<Long> ids);
	
}
