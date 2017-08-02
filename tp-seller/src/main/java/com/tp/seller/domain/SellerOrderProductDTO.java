package com.tp.seller.domain;

/**
 * 商家订单商品信息
 *
 * @author yfxie
 */
public class SellerOrderProductDTO extends SellerBaseDTO {

    /**
     *
     */
    private static final long serialVersionUID = -3227081416245465596L;

    /** 商品名称 */
    private String productName;

    /** 商品编号 */
    private String productCode;
    /** 商品条形码 */
    private String barCode;

    /** 数量 */
    private Integer quantity;

    /** 单价 */
    private Double price;

    /** 小计 */
    private Double total;
    private String brandName;

    /** 单位 */
    private String unit;

    /** 活动编号 */
    private Long topicId;
    /** 备案号（海关） */
    private String productCodeHaitao;
    /** 优惠金额 */
    private Double discount;
    /** 实付价 */
    private Double realPrice;
    /** 商品重量 */
    private Double weight;

    public String getUnit() {
        return unit;
    }

    public void setUnit(final String unit) {
        this.unit = unit;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(final Integer quantity) {
        this.quantity = quantity;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(final Double price) {
        this.price = price;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(final String productCode) {
        this.productCode = productCode;
    }

    public String getBarCode() {
        return barCode;
    }

    public void setBarCode(final String barCode) {
        this.barCode = barCode;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(final String productName) {
        this.productName = productName;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(final Double total) {
        this.total = total;
    }

    public Long getTopicId() {
        return topicId;
    }

    public void setTopicId(final Long topicId) {
        this.topicId = topicId;
    }

    public String getProductCodeHaitao() {
        return productCodeHaitao;
    }

    public void setProductCodeHaitao(final String productCodeHaitao) {
        this.productCodeHaitao = productCodeHaitao;
    }

    public Double getDiscount() {
        return discount;
    }

    public void setDiscount(final Double discount) {
        this.discount = discount;
    }

    public Double getRealPrice() {
        return realPrice;
    }

    public void setRealPrice(final Double realPrice) {
        this.realPrice = realPrice;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(final String brandName) {
        this.brandName = brandName;
    }

    public Double getWeight() {
        return weight;

    }

    public void setWeight(final Double weight) {
        this.weight = weight;

    }

}
