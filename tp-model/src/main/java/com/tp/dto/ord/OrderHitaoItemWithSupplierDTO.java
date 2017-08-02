/**
 *
 */
package com.tp.dto.ord;

import java.util.List;

/**
 * @author szy
 */
public class OrderHitaoItemWithSupplierDTO implements BaseDTO {
    private static final long serialVersionUID = 4997009327849765267L;

    /** 供应商ID */
    private Long supplierId;

    /** 供应商运费模版ID */
    private Long freightTempleteId;

    /** 供应商名称 */
    private String supplierName;

    /** 海淘订单商品，根据仓库拆分 */
    private List<OrderHitaoItemWithStorageDTO> orderHitaoItemWithStorageList;

    public Long getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(final Long supplierId) {
        this.supplierId = supplierId;
    }

    public Long getFreightTempleteId() {
        return freightTempleteId;
    }

    public void setFreightTempleteId(final Long freightTempleteId) {
        this.freightTempleteId = freightTempleteId;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(final String supplierName) {
        this.supplierName = supplierName;
    }

    public static long getSerialversionuid() {
        return serialVersionUID;
    }

    public List<OrderHitaoItemWithStorageDTO> getOrderHitaoItemWithStorageList() {
        return orderHitaoItemWithStorageList;
    }

    public void setOrderHitaoItemWithStorageList(final List<OrderHitaoItemWithStorageDTO> orderHitaoItemWithStorageList) {
        this.orderHitaoItemWithStorageList = orderHitaoItemWithStorageList;
    }

}
