package com.tp.model.mmp;

import java.io.Serializable;

import com.tp.common.annotation.Id;
import com.tp.model.BaseDO;
/**
  * @author szy
  * 库存
  */
public class StockStatus extends BaseDO implements Serializable {

	private static final long serialVersionUID = 1451440579109L;

	/**库位编码 数据类型char(32)*/
	@Id
	private String locationCode;
	
	/**商品编码 数据类型char(32)*/
	@Id
	private String goodsCode;
	
	/**包裹条形码 数据类型char(32)*/
	@Id
	private String packBcode;
	
	/**更新时间(UTC毫秒) 数据类型bigint(20)*/
	private Long updateTime;
	
	/**数量 数据类型decimal(18,4)*/
	private Double qty;
	
	/**数量单位 数据类型char(32)*/
	private String qtyUnit;
	
	
	public String getLocationCode(){
		return locationCode;
	}
	public String getGoodsCode(){
		return goodsCode;
	}
	public String getPackBcode(){
		return packBcode;
	}
	public Long getUpdateTime(){
		return updateTime;
	}
	public Double getQty(){
		return qty;
	}
	public String getQtyUnit(){
		return qtyUnit;
	}
	public void setLocationCode(String locationCode){
		this.locationCode=locationCode;
	}
	public void setGoodsCode(String goodsCode){
		this.goodsCode=goodsCode;
	}
	public void setPackBcode(String packBcode){
		this.packBcode=packBcode;
	}
	public void setUpdateTime(Long updateTime){
		this.updateTime=updateTime;
	}
	public void setQty(Double qty){
		this.qty=qty;
	}
	public void setQtyUnit(String qtyUnit){
		this.qtyUnit=qtyUnit;
	}
}
