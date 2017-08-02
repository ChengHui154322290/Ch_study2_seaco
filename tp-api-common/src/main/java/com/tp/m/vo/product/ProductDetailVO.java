package com.tp.m.vo.product;

import java.util.List;
import java.util.Map;

import com.tp.m.to.product.SkuTO;
import com.tp.m.to.product.SpecGroupTO;
import com.tp.m.to.product.TagTO;

public class ProductDetailVO extends ProductVO{

	private static final long serialVersionUID = -1743833798096433768L;

	private List<String> imglist;
	private String countryimg;//国家图片
	private String countryname;//国家名称
	private String feature;//特色
	private String shareurl;//分享url
	private String detail;//商品详情(HTML)
	private String itemType;// 商品类型
	private List<SkuTO> skus ;//SKU集合
	private List<SpecGroupTO> specs;//规格集合
	private String purchasepage;//立即购买按钮展示   ONLY表示只有立即购买 BOTH表示有立即购买和加入购物车）
	
	private List<TagTO> tags;
	
	private String salescountdesc;//已售数量描述(已售1000件)
    
	private Long supplierId;//供应商ID
	private String logoPath;//logo图片地址
	private String mobileImage;//*移动首页图片
	private String introMobile;//*店铺介绍 
	private String shopName;//店铺名 
	private String shopImagePath;//店铺头图片
	private String prestime;
	private String addr;
	private String tel;
	private String unit;
	private String imgurl;//for offline gb
	private String salespattern;
	
	private Integer inventoryCount;//意大利商品库存
	
	public Integer getInventoryCount() {
		return inventoryCount;
	}

	public void setInventoryCount(Integer inventoryCount) {
		this.inventoryCount = inventoryCount;
	}

	public String getSalespattern() {
		return salespattern;
	}

	public void setSalespattern(String salespattern) {
		this.salespattern = salespattern;
	}

	@Override
	public String getImgurl() {
		return imgurl;
	}

	@Override
	public void setImgurl(String imgurl) {
		this.imgurl = imgurl;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public String getTel() {
		return tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

	public String getPrestime() {
		return prestime;
	}

	public void setPrestime(String prestime) {
		this.prestime = prestime;
	}

	public String getAddr() {
		return addr;
	}

	public void setAddr(String addr) {
		this.addr = addr;
	}

	private List<DummyProductAttr> dummyattr;

	public List<DummyProductAttr> getDummyattr() {
		return dummyattr;
	}

	public void setDummyattr(List<DummyProductAttr> dummyattr) {
		this.dummyattr = dummyattr;
	}

	/**
	 * shopImagePath.  
	 *  
	 * @return  the shopImagePath  
	 * @since  JDK 1.8  
	 */
	public String getShopImagePath() {
		return shopImagePath;
	}
	/**  
	 * shopImagePath.  
	 *  
	 * @param   shopImagePath    the shopImagePath to set  
	 * @since  JDK 1.8  
	 */
	public void setShopImagePath(String shopImagePath) {
		this.shopImagePath = shopImagePath;
	}
	public List<String> getImglist() {
		return imglist;
	}
	public void setImglist(List<String> imglist) {
		this.imglist = imglist;
	}
	public String getCountryimg() {
		return countryimg;
	}
	public void setCountryimg(String countryimg) {
		this.countryimg = countryimg;
	}
	public String getCountryname() {
		return countryname;
	}
	public void setCountryname(String countryname) {
		this.countryname = countryname;
	}
	public String getFeature() {
		return feature;
	}
	public void setFeature(String feature) {
		this.feature = feature;
	}
	public String getShareurl() {
		return shareurl;
	}
	public void setShareurl(String shareurl) {
		this.shareurl = shareurl;
	}
	public String getDetail() {
		return detail;
	}
	public void setDetail(String detail) {
		this.detail = detail;
	}
	public List<SkuTO> getSkus() {
		return skus;
	}
	public void setSkus(List<SkuTO> skus) {
		this.skus = skus;
	}
	public List<SpecGroupTO> getSpecs() {
		return specs;
	}
	public void setSpecs(List<SpecGroupTO> specs) {
		this.specs = specs;
	}
	public String getPurchasepage() {
		return purchasepage;
	}
	public void setPurchasepage(String purchasepage) {
		this.purchasepage = purchasepage;
	}
	public List<TagTO> getTags() {
		return tags;
	}
	public void setTags(List<TagTO> tags) {
		this.tags = tags;
	}
	public String getSalescountdesc() {
		return salescountdesc;
	}
	public void setSalescountdesc(String salescountdesc) {
		this.salescountdesc = salescountdesc;
	}
	/**  
	 * supplierId.  
	 *  
	 * @return  the supplierId  
	 * @since  JDK 1.8  
	 */
	public Long getSupplierId() {
		return supplierId;
	}
	/**  
	 * supplierId.  
	 *  
	 * @param   supplierId    the supplierId to set  
	 * @since  JDK 1.8  
	 */
	public void setSupplierId(Long supplierId) {
		this.supplierId = supplierId;
	}
	/**  
	 * logoPath.  
	 *  
	 * @return  the logoPath  
	 * @since  JDK 1.8  
	 */
	public String getLogoPath() {
		return logoPath;
	}
	/**  
	 * logoPath.  
	 *  
	 * @param   logoPath    the logoPath to set  
	 * @since  JDK 1.8  
	 */
	public void setLogoPath(String logoPath) {
		this.logoPath = logoPath;
	}
	/**  
	 * mobileImage.  
	 *  
	 * @return  the mobileImage  
	 * @since  JDK 1.8  
	 */
	public String getMobileImage() {
		return mobileImage;
	}
	/**  
	 * mobileImage.  
	 *  
	 * @param   mobileImage    the mobileImage to set  
	 * @since  JDK 1.8  
	 */
	public void setMobileImage(String mobileImage) {
		this.mobileImage = mobileImage;
	}
	/**  
	 * introMobile.  
	 *  
	 * @return  the introMobile  
	 * @since  JDK 1.8  
	 */
	public String getIntroMobile() {
		return introMobile;
	}
	/**  
	 * introMobile.  
	 *  
	 * @param   introMobile    the introMobile to set  
	 * @since  JDK 1.8  
	 */
	public void setIntroMobile(String introMobile) {
		this.introMobile = introMobile;
	}
	/**  
	 * shopName.  
	 *  
	 * @return  the shopName  
	 * @since  JDK 1.8  
	 */
	public String getShopName() {
		return shopName;
	}
	/**  
	 * shopName.  
	 *  
	 * @param   shopName    the shopName to set  
	 * @since  JDK 1.8  
	 */
	public void setShopName(String shopName) {
		this.shopName = shopName;
	}
	public String getItemType() {
		return itemType;
	}
	public void setItemType(String itemType) {
		this.itemType = itemType;
	}
	
	
}
