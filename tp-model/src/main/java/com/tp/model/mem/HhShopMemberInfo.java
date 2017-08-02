/**  
 * Project Name:xg-model  
 * File Name:HhMemberInfo.java  
 * Package Name:com.tp.model.mem  
 * Date:2016年11月18日下午4:39:50  
 * Copyright (c) 2016, seagoor All Rights Reserved.  
 *  
*/

package com.tp.model.mem;

import java.io.Serializable;

/**
 * ClassName:HhMemberInfo <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON. <br/>
 * Date: 2016年11月18日 下午4:39:50 <br/>
 * 
 * @author zhouguofeng
 * @version
 * @since JDK 1.8
 * @see
 */
public class HhShopMemberInfo implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Long id;// 用户ID
	
	private String openId;// 微信号
	
	private String name;// 姓名
	
	private String phone; // 注册手机号码
	
	private Double hMoney;// 可用惠币余额
	
	private String address;//地址
	
	private String contact;//联系人
	
	

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getOpenId() {
		return openId;
	}

	public void setOpenId(String openId) {
		this.openId = openId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public Double gethMoney() {
		return hMoney;
	}

	public void sethMoney(Double hMoney) {
		this.hMoney = hMoney;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getContact() {
		return contact;
	}

	public void setContact(String contact) {
		this.contact = contact;
	}
	
   
}
