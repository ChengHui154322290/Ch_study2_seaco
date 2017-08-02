package com.tp.model.mmp;

import java.io.Serializable;

import com.tp.common.annotation.Id;
import com.tp.model.BaseDO;
/**
  * @author szy
  * 商品
  */
public class Goods extends BaseDO implements Serializable {

	private static final long serialVersionUID = 1451440579108L;

	/**商品编码  数据类型char(32)*/
	@Id
	private String goodsCode;
	
	/**商品名 数据类型varchar(100)*/
	private String goodsName;
	
	
	public String getGoodsCode(){
		return goodsCode;
	}
	public String getGoodsName(){
		return goodsName;
	}
	public void setGoodsCode(String goodsCode){
		this.goodsCode=goodsCode;
	}
	public void setGoodsName(String goodsName){
		this.goodsName=goodsName;
	}
}
