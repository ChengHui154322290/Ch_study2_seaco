package com.tp.model.sup;

import java.io.Serializable;

import java.util.Date;

import com.tp.common.annotation.Id;
import com.tp.model.BaseDO;
/**
  * @author zhouguofeng
  * 商家平台店铺主表
  */
public class SupplierShop extends BaseDO implements Serializable {

	private static final long serialVersionUID = 1474515516675L;

	/** 数据类型bigint(11)*/
	@Id
	private Long id;
	
	/** 数据类型bigint(11)*/
	private Long supplierId;
	
	/**店铺名 数据类型varchar(60)*/
	private String shopName;
	
	/**logo图片地址 数据类型varchar(100)*/
	private String logoPath;
	
	/**移动首页图片 数据类型varchar(255)*/
	private String mobileImage;
	
	/**店铺介绍 数据类型text*/
	private String introMobile;
	
	/**搜索标签1 数据类型varchar(100)*/
	private String searchTitle1;
	
	/**搜索标签2 数据类型varchar(100)*/
	private String searchTitle2;
	
	/**搜索标签3 数据类型varchar(100)*/
	private String searchTitle3;
	
	/**搜索标签4 数据类型varchar(100)*/
	private String searchTitle4;
	
	/**备注 数据类型text*/
	private String description;
	
	/**状体（1：启用 0：禁用） 数据类型tinyint(1)*/
	private Integer status;
	
	/**创建时间 数据类型datetime*/
	private Date createTime;
	
	/**更新时间 数据类型datetime*/
	private Date updateTime;
	
	/**创建操作者id 数据类型varchar(60)*/
	private String createUser;
	
	/**更新操作者id 数据类型varchar(60)*/
	private String updateUser;
	
	/**店铺头图片*/
	private String shopImagePath;
	
	
	/**营业时间*/
	private String businessTime;
	
	
	/**店铺经度*/
	private Float longitude;
	
	/**店铺纬度*/
	private Float latitude;
	
	/**店铺电话*/
	private String shopTel;
	
	/**店铺地址*/
	private String shopAddress;
	
	public Long getId(){
		return id;
	}
	public Long getSupplierId(){
		return supplierId;
	}
	public String getShopName(){
		return shopName;
	}
	public String getLogoPath(){
		return logoPath;
	}
	public String getMobileImage(){
		return mobileImage;
	}
	public String getIntroMobile(){
		return introMobile;
	}
	public String getSearchTitle1(){
		return searchTitle1;
	}
	public String getSearchTitle2(){
		return searchTitle2;
	}
	public String getSearchTitle3(){
		return searchTitle3;
	}
	public String getSearchTitle4(){
		return searchTitle4;
	}
	public String getDescription(){
		return description;
	}
	public Integer getStatus(){
		return status;
	}
	public Date getCreateTime(){
		return createTime;
	}
	public Date getUpdateTime(){
		return updateTime;
	}
	public String getCreateUser(){
		return createUser;
	}
	public String getUpdateUser(){
		return updateUser;
	}
	public void setId(Long id){
		this.id=id;
	}
	public void setSupplierId(Long supplierId){
		this.supplierId=supplierId;
	}
	public void setShopName(String shopName){
		this.shopName=shopName;
	}
	public void setLogoPath(String logoPath){
		this.logoPath=logoPath;
	}
	public void setMobileImage(String mobileImage){
		this.mobileImage=mobileImage;
	}
	public void setIntroMobile(String introMobile){
		this.introMobile=introMobile;
	}
	public void setSearchTitle1(String searchTitle1){
		this.searchTitle1=searchTitle1;
	}
	public void setSearchTitle2(String searchTitle2){
		this.searchTitle2=searchTitle2;
	}
	public void setSearchTitle3(String searchTitle3){
		this.searchTitle3=searchTitle3;
	}
	public void setSearchTitle4(String searchTitle4){
		this.searchTitle4=searchTitle4;
	}
	public void setDescription(String description){
		this.description=description;
	}
	public void setStatus(Integer status){
		this.status=status;
	}
	public void setCreateTime(Date createTime){
		this.createTime=createTime;
	}
	public void setUpdateTime(Date updateTime){
		this.updateTime=updateTime;
	}
	public void setCreateUser(String createUser){
		this.createUser=createUser;
	}
	public void setUpdateUser(String updateUser){
		this.updateUser=updateUser;
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
	
	

	/**  
	 * businessTime.  
	 *  
	 * @return  the businessTime  
	 * @since  JDK 1.8  
	 */
	public String getBusinessTime() {
		return businessTime;
	}
	/**  
	 * businessTime.  
	 *  
	 * @param   businessTime    the businessTime to set  
	 * @since  JDK 1.8  
	 */
	public void setBusinessTime(String businessTime) {
		this.businessTime = businessTime;
	}
	
	/**  
	 * longitude.  
	 *  
	 * @return  the longitude  
	 * @since  JDK 1.8  
	 */
	public Float getLongitude() {
		return longitude;
	}
	/**  
	 * longitude.  
	 *  
	 * @param   longitude    the longitude to set  
	 * @since  JDK 1.8  
	 */
	public void setLongitude(Float longitude) {
		this.longitude = longitude;
	}
	/**  
	 * latitude.  
	 *  
	 * @return  the latitude  
	 * @since  JDK 1.8  
	 */
	public Float getLatitude() {
		return latitude;
	}
	/**  
	 * latitude.  
	 *  
	 * @param   latitude    the latitude to set  
	 * @since  JDK 1.8  
	 */
	public void setLatitude(Float latitude) {
		this.latitude = latitude;
	}
	/**  
	 * shopTel.  
	 *  
	 * @return  the shopTel  
	 * @since  JDK 1.8  
	 */
	public String getShopTel() {
		return shopTel;
	}
	/**  
	 * shopTel.  
	 *  
	 * @param   shopTel    the shopTel to set  
	 * @since  JDK 1.8  
	 */
	public void setShopTel(String shopTel) {
		this.shopTel = shopTel;
	}
	/**  
	 * shopAddress.  
	 *  
	 * @return  the shopAddress  
	 * @since  JDK 1.8  
	 */
	public String getShopAddress() {
		return shopAddress;
	}
	/**  
	 * shopAddress.  
	 *  
	 * @param   shopAddress    the shopAddress to set  
	 * @since  JDK 1.8  
	 */
	public void setShopAddress(String shopAddress) {
		this.shopAddress = shopAddress;
	}
	/**  
	 * serialversionuid.  
	 *  
	 * @return  the serialversionuid  
	 * @since  JDK 1.8  
	 */
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
}
