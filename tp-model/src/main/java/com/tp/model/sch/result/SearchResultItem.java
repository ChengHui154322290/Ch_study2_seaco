package com.tp.model.sch.result;

import com.tp.model.BaseDO;


/**
 * Created by ldr on 2016/2/17.
 */
public class SearchResultItem extends BaseDO {

    private static final long serialVersionUID = -8808746401505328731L;

    private Long id;
    /**
     * 商品名
     */
    private String item_name;
    /**
     * 专场Id
     */
    private Long topic_id;
    /**
     * sku
     */
    private String sku;
    /**
     * spu
     */
    private String spu;
    /**
     * 商品Id
     */
    private String item_id;
    /**
     *条码
     */
    private String bar_code;
    /**
     *平台
     */
    private String platform;
    /**
     *促销价
     */
    private Double topic_price;
    /**
     *销售价
     */
    private Double sale_price;
    /**
     * 当前记录状态 用于同步数据
     */
    private Integer status;
    /**
     * 商品状态0下架 1 上架
     */
    private Integer item_status;
    /**
     * 库存
     */
    private Integer inventory;
    /**
     * 商品销售数量
     */
    private Integer sales_count;
    /**
     * 评论数
     */
    private Integer comment_count;
    /**
     * 评分
     */
    private  Float rating;
    /**
     * hits
     */
    private Long hits;
    /**
     *商品图
     */
    private String item_img;
    /**
     * 品牌Id
     */
    private Long brand_id;
    /**
     * 品牌名称
     */
    private String brand_name;
    /**
     * 大类Id
     */
    private Long l_category_id;

    private String l_category_name;
    /**
     *中类Id
     */
    private Long m_category_id;

    private String m_category_name;
    /**
     *小类Id
     */
    private Long s_category_id;

    private String s_category_name;
    /**
     * 通过渠道id
     */
    private Long channel_id;
    /**
     * 通关渠道name
     */
    private String channel_name;
    /**
     * 国家id
     */
    private Long country_id;
    /**
     * 国家name
     */
    private String country_name;
    /**
     * 规格Id集合
     */
    private String spec_ids;
    /**
     * 规格详情
     */
    private String spec_details;
    /**
     *供应商Id
     */
    private Long supplier_id;
    /**
     * 限购数量
     */
    private Integer limit_amount;
    /**
     * 专场开始时间
     */
    private Long topic_start;
    /**
     * 专场结束时间
     */
    private Long topic_end;

    private Long create_time;

    private Long update_time;

    private String index_name;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getItem_name() {
        return item_name;
    }

    public void setItem_name(String item_name) {
        this.item_name = item_name;
    }

    public Long getTopic_id() {
        return topic_id;
    }

    public void setTopic_id(Long topic_id) {
        this.topic_id = topic_id;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getSpu() {
        return spu;
    }

    public void setSpu(String spu) {
        this.spu = spu;
    }

    public String getItem_id() {
        return item_id;
    }

    public void setItem_id(String item_id) {
        this.item_id = item_id;
    }

    public String getBar_code() {
        return bar_code;
    }

    public void setBar_code(String bar_code) {
        this.bar_code = bar_code;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public Double getTopic_price() {
        return topic_price;
    }

    public void setTopic_price(Double topic_price) {
        this.topic_price = topic_price;
    }

    public Double getSale_price() {
        return sale_price;
    }

    public void setSale_price(Double sale_price) {
        this.sale_price = sale_price;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getInventory() {
        return inventory;
    }

    public void setInventory(Integer inventory) {
        this.inventory = inventory;
    }

    public Integer getSales_count() {
        return sales_count;
    }

    public void setSales_count(Integer sales_count) {
        this.sales_count = sales_count;
    }

    public Integer getComment_count() {
        return comment_count;
    }

    public void setComment_count(Integer comment_count) {
        this.comment_count = comment_count;
    }

    public Float getRating() {
        return rating;
    }

    public void setRating(Float rating) {
        this.rating = rating;
    }

    public Long getHits() {
        return hits;
    }

    public void setHits(Long hits) {
        this.hits = hits;
    }

    public String getItem_img() {
        return item_img;
    }

    public void setItem_img(String item_img) {
        this.item_img = item_img;
    }

    public Long getBrand_id() {
        return brand_id;
    }

    public void setBrand_id(Long brand_id) {
        this.brand_id = brand_id;
    }

    public String getBrand_name() {
        return brand_name;
    }

    public void setBrand_name(String brand_name) {
        this.brand_name = brand_name;
    }

    public Long getL_category_id() {
        return l_category_id;
    }

    public void setL_category_id(Long l_category_id) {
        this.l_category_id = l_category_id;
    }

    public String getL_category_name() {
        return l_category_name;
    }

    public void setL_category_name(String l_category_name) {
        this.l_category_name = l_category_name;
    }

    public Long getM_category_id() {
        return m_category_id;
    }

    public void setM_category_id(Long m_category_id) {
        this.m_category_id = m_category_id;
    }

    public String getM_category_name() {
        return m_category_name;
    }

    public void setM_category_name(String m_category_name) {
        this.m_category_name = m_category_name;
    }

    public Long getS_category_id() {
        return s_category_id;
    }

    public void setS_category_id(Long s_category_id) {
        this.s_category_id = s_category_id;
    }

    public String getS_category_name() {
        return s_category_name;
    }

    public void setS_category_name(String s_category_name) {
        this.s_category_name = s_category_name;
    }

    public Long getChannel_id() {
        return channel_id;
    }

    public void setChannel_id(Long channel_id) {
        this.channel_id = channel_id;
    }

    public String getChannel_name() {
        return channel_name;
    }

    public void setChannel_name(String channel_name) {
        this.channel_name = channel_name;
    }

    public Long getCountry_id() {
        return country_id;
    }

    public void setCountry_id(Long country_id) {
        this.country_id = country_id;
    }

    public String getCountry_name() {
        return country_name;
    }

    public void setCountry_name(String country_name) {
        this.country_name = country_name;
    }

    public String getSpec_ids() {
        return spec_ids;
    }

    public void setSpec_ids(String spec_ids) {
        this.spec_ids = spec_ids;
    }

    public String getSpec_details() {
        return spec_details;
    }

    public void setSpec_details(String spec_details) {
        this.spec_details = spec_details;
    }

    public Long getSupplier_id() {
        return supplier_id;
    }

    public void setSupplier_id(Long supplier_id) {
        this.supplier_id = supplier_id;
    }

    public Integer getLimit_amount() {
        return limit_amount;
    }

    public void setLimit_amount(Integer limit_amount) {
        this.limit_amount = limit_amount;
    }

    public Long getTopic_start() {
        return topic_start;
    }

    public void setTopic_start(Long topic_start) {
        this.topic_start = topic_start;
    }

    public Long getTopic_end() {
        return topic_end;
    }

    public void setTopic_end(Long topic_end) {
        this.topic_end = topic_end;
    }

    public Long getCreate_time() {
        return create_time;
    }

    public void setCreate_time(Long create_time) {
        this.create_time = create_time;
    }

    public Long getUpdate_time() {
        return update_time;
    }

    public void setUpdate_time(Long update_time) {
        this.update_time = update_time;
    }

    public String getIndex_name() {
        return index_name;
    }

    public void setIndex_name(String index_name) {
        this.index_name = index_name;
    }

	public Integer getItem_status() {
		return item_status;
	}

	public void setItem_status(Integer item_status) {
		this.item_status = item_status;
	}
}
