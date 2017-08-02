package com.tp.m.to.cache;

import com.tp.m.base.BaseTO;

/**
 * token的缓存对象
 * @author zhuss
 * @2016年1月3日 下午5:43:29
 */
public class TokenCacheTO implements BaseTO{

	private static final long serialVersionUID = -6011683470945403092L;

	private String tel;
	private String name;
	private Long uid;
	private Long promoterId;
	
	
	public TokenCacheTO() {
		super();
	}
	public TokenCacheTO(String tel, String name, Long uid) {
		super();
		this.tel = tel;
		this.name = name;
		this.uid = uid;
	}
	public String getTel() {
		return tel;
	}
	public void setTel(String tel) {
		this.tel = tel;
	}
	public Long getUid() {
		return uid;
	}
	public void setUid(Long uid) {
		this.uid = uid;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
}
