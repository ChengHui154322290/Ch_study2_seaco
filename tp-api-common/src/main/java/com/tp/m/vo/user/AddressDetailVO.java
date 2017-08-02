package com.tp.m.vo.user;

/**
 * 用户收货地址
 * @author zhuss
 * @2016年1月4日 下午3:33:50
 */
public class AddressDetailVO extends AddressVO{
	private static final long serialVersionUID = -1126373574194501644L;
	private String name;//收货人姓名
	private String tel;//收货人联系手机号
	private Long provinceid;
	private String province;
	private Long cityid;
	private String city;
	private Long districtid;
	private String district;
	private Long streetid;
	private String street;
	//private String zipcode;//邮政编码
	private String info;//具体的地址信息
	private String fullinfo;
	private String isdefault;//是否默认收货地址 0否1是
	
	private String identityCard;//身份证号
	private String frontimg;//正面
	private String backimg;//反面
	private String iscertificated;//是否已认证 0 否 1是

	public String getIscertificated() {
		return iscertificated;
	}

	public void setIscertificated(String iscertificated) {
		this.iscertificated = iscertificated;
	}

	public String getFrontimg() {
		return frontimg;
	}

	public void setFrontimg(String frontimg) {
		this.frontimg = frontimg;
	}

	public String getBackimg() {
		return backimg;
	}

	public void setBackimg(String backimg) {
		this.backimg = backimg;
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getTel() {
		return tel;
	}
	public void setTel(String tel) {
		this.tel = tel;
	}
	public Long getProvinceid() {
		return provinceid;
	}
	public void setProvinceid(Long provinceid) {
		this.provinceid = provinceid;
	}
	public String getProvince() {
		return province;
	}
	public void setProvince(String province) {
		this.province = province;
	}
	public Long getCityid() {
		return cityid;
	}
	public void setCityid(Long cityid) {
		this.cityid = cityid;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public Long getDistrictid() {
		return districtid;
	}
	public void setDistrictid(Long districtid) {
		this.districtid = districtid;
	}
	public String getDistrict() {
		return district;
	}
	public void setDistrict(String district) {
		this.district = district;
	}
	public Long getStreetid() {
		return streetid;
	}
	public void setStreetid(Long streetid) {
		this.streetid = streetid;
	}
	public String getStreet() {
		return street;
	}
	public void setStreet(String street) {
		this.street = street;
	}
	public String getInfo() {
		return info;
	}
	public void setInfo(String info) {
		this.info = info;
	}
	public String getFullinfo() {
		return fullinfo;
	}
	public void setFullinfo(String fullinfo) {
		this.fullinfo = fullinfo;
	}
	public String getIsdefault() {
		return isdefault;
	}
	public void setIsdefault(String isdefault) {
		this.isdefault = isdefault;
	}
	public String getIdentityCard() {
		return identityCard;
	}
	public void setIdentityCard(String identityCard) {
		this.identityCard = identityCard;
	}
}
