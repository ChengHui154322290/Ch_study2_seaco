package com.tp.model.mmp;

import java.io.Serializable;

import com.tp.common.annotation.Id;
import com.tp.model.BaseDO;
/**
  * @author szy
  * 出入库
  */
public class StockIo extends BaseDO implements Serializable {

	private static final long serialVersionUID = 1451440579109L;

	/**id 数据类型bigint(20)*/
	@Id
	private Long ioId;
	
	/**库位编码 数据类型char(32)*/
	private String locationCode;
	
	/**更新时间(UTC毫秒) 数据类型bigint(20)*/
	private Long updateTime;
	
	/**商品编码 数据类型char(32)*/
	private String goodsCode;
	
	/**包裹条形码 数据类型char(32)*/
	private String packBcode;
	
	/**1:入库 -1:出库 数据类型tinyint(4)*/
	private Integer inoutFlg;
	
	/**数量 数据类型decimal(18,4)*/
	private Double qty;
	
	/**数量单位 数据类型char(32)*/
	private String qtyUnit;
	
	
	public Long getIoId(){
		return ioId;
	}
	public String getLocationCode(){
		return locationCode;
	}
	public Long getUpdateTime(){
		return updateTime;
	}
	public String getGoodsCode(){
		return goodsCode;
	}
	public String getPackBcode(){
		return packBcode;
	}
	public Integer getInoutFlg(){
		return inoutFlg;
	}
	public Double getQty(){
		return qty;
	}
	public String getQtyUnit(){
		return qtyUnit;
	}
	public void setIoId(Long ioId){
		this.ioId=ioId;
	}
	public void setLocationCode(String locationCode){
		this.locationCode=locationCode;
	}
	public void setUpdateTime(Long updateTime){
		this.updateTime=updateTime;
	}
	public void setGoodsCode(String goodsCode){
		this.goodsCode=goodsCode;
	}
	public void setPackBcode(String packBcode){
		this.packBcode=packBcode;
	}
	public void setInoutFlg(Integer inoutFlg){
		this.inoutFlg=inoutFlg;
	}
	public void setQty(Double qty){
		this.qty=qty;
	}
	public void setQtyUnit(String qtyUnit){
		this.qtyUnit=qtyUnit;
	}
}
