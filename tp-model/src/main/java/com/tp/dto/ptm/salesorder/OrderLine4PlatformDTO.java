package com.tp.dto.ptm.salesorder;

import java.io.Serializable;

import com.tp.dto.ord.remote.OrderLineDTO;
import com.tp.model.ord.OrderItem;


/**
 * 开放平台对外订单行DTO
 * 
 * @author 项硕
 * @version 2015年5月15日 下午3:34:20
 */
public class OrderLine4PlatformDTO implements Serializable {

    private static final long serialVersionUID = 823478536517129535L;

    private String name;
    private String sku;
    private String barCode;
    private String productCode;
    private String unit;
    private Integer quantity;
    private Double price;
    private Double weight;
    private String brand;
    private String supplier;
    private Long supplierId;
    private String taxCode; // 行邮税号

    public OrderLine4PlatformDTO() {
    }

    public OrderLine4PlatformDTO(OrderItem line) {
        if (null != line) {
            name = line.getSpuName();
            sku = line.getSkuCode();
            barCode = line.getBarCode();
            productCode = line.getProductCode();
            unit = line.getUnit();
            quantity = line.getQuantity();
            price = line.getPrice();
            weight = line.getWeight();
            brand = line.getBrandName();
            supplier = line.getSupplierName();
            supplierId = line.getSupplierId();
            taxCode = line.getParcelTaxId();
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getBarCode() {
        return barCode;
    }

    public void setBarCode(String barCode) {
        this.barCode = barCode;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getSupplier() {
        return supplier;
    }

    public void setSupplier(String supplier) {
        this.supplier = supplier;
    }

    public Long getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(Long supplierId) {
        this.supplierId = supplierId;
    }

    public String getTaxCode() {
        return taxCode;
    }

    public void setTaxCode(String taxCode) {
        this.taxCode = taxCode;
    }

}
