package com.tp.model.sup;

import java.io.Serializable;

import java.util.Date;

import com.tp.common.annotation.Id;
import com.tp.model.BaseDO;

/**
 * @author szy
 *         报价单-商品表
 */
public class QuotationProduct extends BaseDO implements Serializable {

    private static final long serialVersionUID = 1450836274735L;

    /**
     * 数据类型bigint(11)
     */
    @Id
    private Long id;

    /**
     * 报价单id 数据类型bigint(11)
     */
    private Long quotationId;

    /**
     * 供应商id 数据类型bigint(11)
     */
    private Long supplierId;

    /**
     * 商品编号 数据类型varchar(100)
     */
    private String productCode;

    /**
     * 品牌id 数据类型bigint(11)
     */
    private Long brandId;

    /**
     * 品牌名称 数据类型varchar(80)
     */
    private String brandName;

    /**
     * 大类id 数据类型varchar(45)
     */
    private String bigId;

    /**
     * 大类名称 数据类型varchar(80)
     */
    private String bigName;

    /**
     * 中类id 数据类型varchar(45)
     */
    private String midId;

    /**
     * 中类名称 数据类型varchar(80)
     */
    private String midName;

    /**
     * 小类id 数据类型varchar(45)
     */
    private String smallId;

    /**
     * 小类名称 数据类型varchar(80)
     */
    private String smallName;

    /**
     * 数据类型varchar(100)
     */
    private String spu;

    /**
     * 数据类型varchar(100)
     */
    private String prdid;

    /**
     * 数据类型varchar(100)
     */
    private String sku;

    /**
     * 条形码 数据类型varchar(100)
     */
    private String barCode;

    /**
     * 商品名称 数据类型varchar(255)
     */
    private String productName;

    /**
     * 商品单位 数据类型varchar(45)
     */
    private String productUnit;

    /**
     * 标准价 数据类型double
     */
    private Double standardPrice;

    /**
     * 销售价格 数据类型double
     */
    private Double salePrice;

    /**
     * 供货价格 数据类型double
     */
    private Double supplyPrice;

    /**
     * 佣金比例 数据类型double
     */
    private Double commissionPercent;

    /**
     * 箱规 数据类型varchar(255)
     */
    private String boxProp;

    /**
     * 商品规格 数据类型varchar(255)
     */
    private String productProp;

    /**
     * 审核状态 数据类型int(8)
     */
    private Integer auditStatus;

    /**
     * 状体（1：启用 0：禁用） 数据类型tinyint(1)
     */
    private Integer status;

    /**
     * 创建时间 数据类型datetime
     */
    private Date createTime;

    /**
     * 更新时间 数据类型datetime
     */
    private Date updateTime;

    /**
     * 创建操作者id 数据类型varchar(32)
     */
    private String createUser;

    /**
     * 更新操作者id 数据类型varchar(32)
     */
    private String updateUser;
    /**
     * 商品裸价
     */
    private Double basePrice;
    /**
     * 运费
     */
    private Double freight;
    /**
     * 跨境综合税率
     */
    private Double mulTaxRate;
    /**
     * 行邮税税率
     */
    private Double tarrifTaxRate;
    /**
     * 包邮包税代发价
     */
    private Double sumPrice;

    public Double getBasePrice() {
        return basePrice;
    }

    public void setBasePrice(Double basePrice) {
        this.basePrice = basePrice;
    }

    public Double getFreight() {
        return freight;
    }

    public void setFreight(Double freight) {
        this.freight = freight;
    }

    public Double getMulTaxRate() {
        return mulTaxRate;
    }

    public void setMulTaxRate(Double mulTaxRate) {
        this.mulTaxRate = mulTaxRate;
    }

    public Double getTarrifTaxRate() {
        return tarrifTaxRate;
    }

    public void setTarrifTaxRate(Double tarrifTaxRate) {
        this.tarrifTaxRate = tarrifTaxRate;
    }

    public Double getSumPrice() {
        return sumPrice;
    }

    public void setSumPrice(Double sumPrice) {
        this.sumPrice = sumPrice;
    }

    public Long getId() {
        return id;
    }

    public Long getQuotationId() {
        return quotationId;
    }

    public Long getSupplierId() {
        return supplierId;
    }

    public String getProductCode() {
        return productCode;
    }

    public Long getBrandId() {
        return brandId;
    }

    public String getBrandName() {
        return brandName;
    }

    public String getBigId() {
        return bigId;
    }

    public String getBigName() {
        return bigName;
    }

    public String getMidId() {
        return midId;
    }

    public String getMidName() {
        return midName;
    }

    public String getSmallId() {
        return smallId;
    }

    public String getSmallName() {
        return smallName;
    }

    public String getSpu() {
        return spu;
    }

    public String getPrdid() {
        return prdid;
    }

    public String getSku() {
        return sku;
    }

    public String getBarCode() {
        return barCode;
    }

    public String getProductName() {
        return productName;
    }

    public String getProductUnit() {
        return productUnit;
    }

    public Double getStandardPrice() {
        return standardPrice;
    }

    public Double getSalePrice() {
        return salePrice;
    }

    public Double getSupplyPrice() {
        return supplyPrice;
    }

    public Double getCommissionPercent() {
        return commissionPercent;
    }

    public String getBoxProp() {
        return boxProp;
    }

    public String getProductProp() {
        return productProp;
    }

    public Integer getAuditStatus() {
        return auditStatus;
    }

    public Integer getStatus() {
        return status;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public String getCreateUser() {
        return createUser;
    }

    public String getUpdateUser() {
        return updateUser;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setQuotationId(Long quotationId) {
        this.quotationId = quotationId;
    }

    public void setSupplierId(Long supplierId) {
        this.supplierId = supplierId;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public void setBrandId(Long brandId) {
        this.brandId = brandId;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public void setBigId(String bigId) {
        this.bigId = bigId;
    }

    public void setBigName(String bigName) {
        this.bigName = bigName;
    }

    public void setMidId(String midId) {
        this.midId = midId;
    }

    public void setMidName(String midName) {
        this.midName = midName;
    }

    public void setSmallId(String smallId) {
        this.smallId = smallId;
    }

    public void setSmallName(String smallName) {
        this.smallName = smallName;
    }

    public void setSpu(String spu) {
        this.spu = spu;
    }

    public void setPrdid(String prdid) {
        this.prdid = prdid;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public void setBarCode(String barCode) {
        this.barCode = barCode;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public void setProductUnit(String productUnit) {
        this.productUnit = productUnit;
    }

    public void setStandardPrice(Double standardPrice) {
        this.standardPrice = standardPrice;
    }

    public void setSalePrice(Double salePrice) {
        this.salePrice = salePrice;
    }

    public void setSupplyPrice(Double supplyPrice) {
        this.supplyPrice = supplyPrice;
    }

    public void setCommissionPercent(Double commissionPercent) {
        this.commissionPercent = commissionPercent;
    }

    public void setBoxProp(String boxProp) {
        this.boxProp = boxProp;
    }

    public void setProductProp(String productProp) {
        this.productProp = productProp;
    }

    public void setAuditStatus(Integer auditStatus) {
        this.auditStatus = auditStatus;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }

    public void setUpdateUser(String updateUser) {
        this.updateUser = updateUser;
    }
}
