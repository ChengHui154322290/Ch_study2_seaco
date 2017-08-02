package com.tp.service.sup;

import java.math.BigDecimal;
import java.util.List;

import com.tp.common.vo.supplier.entry.AuditStatus;
import com.tp.common.vo.supplier.entry.SupplierBusinessType;
import com.tp.common.vo.supplier.entry.SupplierType;
import com.tp.dto.common.ResultInfo;
import com.tp.dto.stg.WarehouseOrderRewriteDTO;
import com.tp.query.sup.SupplierQuery;
import com.tp.result.sup.SupplierCustomsRecordationResult;
import com.tp.result.sup.SupplierResult;

/**
 * {采购管理模块对外接口} <br>
 * Create on : 2015年1月9日 下午4:45:06<br>
 *
 * @author szy
 * @version 0.0.1
 */
public interface IPurchasingManagementService {

    /**
     * {根据供应商id和sku获取商品销售价格}.
     *
     * @param sku 商品SKU
     * @param supplierId 供应商id
     * @return BigDecimal 销售价格
     */
    BigDecimal getProductSalesPrice(String sku, long supplierId);

    /**
     * 根据条件查询供应商信息(默认审核状态为通过的供应商)
     *
     * @param supplierId 供应商id
     * @param supplierName 供应商名称
     * @param supplierType 供应商类型
     * @param startPage 当前页
     * @param pageSize 每页大小
     * @return SupplierResult
     * SupplierResult getSupplierListWidthCondition
     */

    SupplierResult getSupplierListWithCondition(Long supplierId, List<SupplierType> supplierTypes, String supplierName, int startPage, int pageSize);

    /**
     * {根据西客商城/商家 查询供应商原始类型}.
     *
     * @param supplierBusinessType
     * @return List<SupplierType>
     */
    List<SupplierType> getSupplierTypes(SupplierBusinessType supplierBusinessType);

    /**
     * 仓库根据预约单回写采购和代销订单相关信息 仓库预约单状态回写
     *
     * @return
     */
    ResultInfo<?> writeWarehoseInfoToOrder(WarehouseOrderRewriteDTO warehouseOrder);

    /**
     * 仓库根据预约单回写采购退货单和代销退货单相关信息
     *
     * @param warehouseOrder
     * @return
     */
    ResultInfo<?> writeWarehoseRefundInfoToOrder(WarehouseOrderRewriteDTO warehouseOrder);

    /**
     * 获取特殊的供应商信息
     *
     * @return
     */
    SupplierResult getSpecialSupplier();

    /**
     * 根据供应商id获取供应商列表
     *
     * @param supplierId
     * @return id,supplierType,SuplierTypeName,name
     * @throws DAOException
     */
    SupplierResult getSuppliersByIds(List<Long> supplierId);
    
    /**
     * 获取供应商渠道信息
     * 
     * @return
     * @throws DAOException
     */
    public SupplierCustomsRecordationResult getSupplierCustomsRecordation(List<SupplierQuery> queryList);

    /**
     * 根据供应商id获取供应商列表
     *
     * @param supplierId
     * @return id,supplierType,SuplierTypeName,name
     * @throws DAOException
     */
    SupplierResult getSuppliersByTypes(List<SupplierType> supplierTypes, int startPage, int pageSize);

    /**
     * 根据供应商查询品牌id
     * 
     * @param supplierId
     * @return
     */
	List<Long> getSupplierBrandIds(Long supplierId);
	
	/**
	 * 
	 * 
	 * @param supplierName
	 * @return
	 */
	SupplierResult fuzzyQuerySupplierByName(String supplierName, int startPage, int pageSize);

	/**
	 * 模糊查询所有供应商状态   没有经过审核状态筛选和使用状态筛选
	 * 
	 * @param supplierName
	 * @param startPage
	 * @param pageSize
	 * @return
	 */
	SupplierResult fuzzyQueryAllSupplierByName(String supplierName,
			int startPage, int pageSize);
	
	/**
	 * 模糊查询所有供应商状态   没有经过审核状态筛选和使用状态筛选
	 * 
	 * @param supplierName
	 * @param startPage
	 * @param pageSize
	 * @return
	 */
	SupplierResult fuzzyQuerySupplier(Long supplierId, List<SupplierType> supplierTypes, String supplierName, int startPage, int pageSize);

	/**
	 * 根据供应商列获取供应商
	 * 
	 * @param idList
	 * @param auditStatus
	 * @param status
	 * @return
	 * @throws DAOException
	 */
	SupplierResult getUsedSuppliersByIds(List<Long> idList,AuditStatus auditStatus, Integer status);

}
