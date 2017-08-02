package com.tp.dto.wms;

import java.util.List;

import com.tp.model.BaseDO;
import com.tp.model.stg.Warehouse;
import com.tp.model.sup.PurchaseInfo;
import com.tp.model.sup.PurchaseProduct;
import com.tp.model.sup.PurchaseWarehouse;
import com.tp.model.sup.SupplierInfo;
import com.tp.model.usr.UserInfo;
import com.tp.model.wms.Stockasn;
import com.tp.model.wms.StockasnDetail;

/**
 * 推送仓库预约单信息
 * Created by ldr on 2016/6/20.
 */
public class SendOrderInfo extends BaseDO {

    private static final long serialVersionUID = -7217791225857096870L;

    /**
     * 仓库预约单
     */
    private PurchaseWarehouse purchaseWarehouse;

    /**
     * 采购[代销]订单[退货单]
     */
    private PurchaseInfo purchaseInfo;

    /**
     * 采购[代销]订单[退货单]-商品
     */
    private List<PurchaseProduct> purchaseProducts;

    /**
     * 供应商信息
     */
    private SupplierInfo supplierInfo;

    /**
     * 仓库信息
     */
    private Warehouse warehouse;

    /**
     * 入库单
     */
    private Stockasn stockasn;

    /**
     * 入库单商品信息
     */
    private List<StockasnDetail> stockasnDetails;

    private UserInfo user;

    public UserInfo getUser() {
        return user;
    }

    public void setUser(UserInfo user) {
        this.user = user;
    }

    public Stockasn getStockasn() {
        return stockasn;
    }

    public void setStockasn(Stockasn stockasn) {
        this.stockasn = stockasn;
    }

    public List<StockasnDetail> getStockasnDetails() {
        return stockasnDetails;
    }

    public void setStockasnDetails(List<StockasnDetail> stockasnDetails) {
        this.stockasnDetails = stockasnDetails;
    }

    public PurchaseWarehouse getPurchaseWarehouse() {
        return purchaseWarehouse;
    }

    public void setPurchaseWarehouse(PurchaseWarehouse purchaseWarehouse) {
        this.purchaseWarehouse = purchaseWarehouse;
    }

    public PurchaseInfo getPurchaseInfo() {
        return purchaseInfo;
    }

    public void setPurchaseInfo(PurchaseInfo purchaseInfo) {
        this.purchaseInfo = purchaseInfo;
    }

    public List<PurchaseProduct> getPurchaseProducts() {
        return purchaseProducts;
    }

    public void setPurchaseProducts(List<PurchaseProduct> purchaseProducts) {
        this.purchaseProducts = purchaseProducts;
    }

    public SupplierInfo getSupplierInfo() {
        return supplierInfo;
    }

    public void setSupplierInfo(SupplierInfo supplierInfo) {
        this.supplierInfo = supplierInfo;
    }

    public Warehouse getWarehouse() {
        return warehouse;
    }

    public void setWarehouse(Warehouse warehouse) {
        this.warehouse = warehouse;
    }
}
