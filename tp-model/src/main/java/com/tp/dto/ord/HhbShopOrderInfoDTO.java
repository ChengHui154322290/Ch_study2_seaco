/**  
 * Project Name:xg-model  
 * File Name:HhbShopOnderInfo.java  
 * Package Name:com.tp.dto.ord  
 * Date:2016年11月23日上午9:09:42  
 * Copyright (c) 2016, seagoor All Rights Reserved.  
 *  
*/

package com.tp.dto.ord;

import java.io.Serializable;

/**
 * ClassName:HhbShopOnderInfo <br/>
 * Function: 惠惠宝商城订单回传实体类 <br/>
 * Reason: TODO ADD REASON. <br/>
 * Date: 2016年11月23日 上午9:09:42 <br/>
 * 
 * @author zhouguofeng
 * @version
 * @since JDK 1.8
 * @see
 */
public class HhbShopOrderInfoDTO implements Serializable {
	/**  
	 * serialVersionUID:TODO(用一句话描述这个变量表示什么).  
	 * @sinceJDK 1.8  
	 */
	private static final long serialVersionUID = 1L;
	/** 当前微信openId（ */
	private String openId;
	/** 0表示退货，1表示购买商品 */
	private String type;
	/** 订单编号 */
	private String code;
	/**总金额*/
	private Double totalMoney;
	/** 支付的现金金额 */
	private Double cash;
	/** 使用惠币金额 */
	private Double balance;
	/** 使用积分金额 */
	private Double integral;
	/**返佣金额*/
	private Double returnMoney;

	public String getOpenId() {
		return openId;
	}

	public void setOpenId(String openId) {
		this.openId = openId;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	

	public Double getTotalMoney() {
		return totalMoney;
	}

	public void setTotalMoney(Double totalMoney) {
		this.totalMoney = totalMoney;
	}

	public Double getCash() {
		return cash;
	}

	public void setCash(Double cash) {
		this.cash = cash;
	}

	public Double getBalance() {
		return balance;
	}

	public void setBalance(Double balance) {
		this.balance = balance;
	}

	public Double getIntegral() {
		return integral;
	}

	public void setIntegral(Double integral) {
		this.integral = integral;
	}

	public Double getReturnMoney() {
		return returnMoney;
	}

	public void setReturnMoney(Double returnMoney) {
		this.returnMoney = returnMoney;
	}
    
}
