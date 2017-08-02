package com.tp.model.mmp;

import java.io.Serializable;

import java.util.Date;

import com.tp.common.annotation.Id;
import com.tp.model.BaseDO;
/**
  * @author szy
  * 库存交互流水账
  */
public class TopicInventoryAccBook extends BaseDO implements Serializable {

	private static final long serialVersionUID = 1451440579111L;

	/** 数据类型bigint(18)*/
	@Id
	private Long id;
	
	/**业务编号 数据类型bigint(18)*/
	private Long bizId;
	
	/**内部业务类型 1活动 2活动变更单 数据类型int(11)*/
	private Integer bizType;
	
	/**活动商品项id 数据类型bigint(18)*/
	private Long itemId;
	
	/**sku 数据类型varchar(30)*/
	private String sku;
	
	/**供应商Id 数据类型bigint(18)*/
	private Long spId;
	
	/**仓库id 数据类型bigint(18)*/
	private Long warehouseId;
	
	/**操作类型 1新增 2编辑 3删除 4活动终止  5回滚库存 6回滚终止 数据类型int(1)*/
	private Integer operType;
	
	/**操作数量 数据类型int(4)*/
	private Integer operAmount;
	
	/**操作结果 1 - 成功  2 - 失败 数据类型int(1)*/
	private Integer operResult;
	
	/**调用接口异常编号 数据类型varchar(50)*/
	private String failedCode;
	
	/**调用接口异常信息 数据类型varchar(150)*/
	private String failedMsg;
	
	/**操作人id 数据类型bigint(18)*/
	private Long operId;
	
	/**操作人姓名 数据类型varchar(50)*/
	private String operator;
	
	/**操作时间 数据类型datetime*/
	private Date operTime;
	
	/**备注 数据类型varchar(100)*/
	private String remark;
	
	
	public Long getId(){
		return id;
	}
	public Long getBizId(){
		return bizId;
	}
	public Integer getBizType(){
		return bizType;
	}
	public Long getItemId(){
		return itemId;
	}
	public String getSku(){
		return sku;
	}
	public Long getSpId(){
		return spId;
	}
	public Long getWarehouseId(){
		return warehouseId;
	}
	public Integer getOperType(){
		return operType;
	}
	public Integer getOperAmount(){
		return operAmount;
	}
	public Integer getOperResult(){
		return operResult;
	}
	public String getFailedCode(){
		return failedCode;
	}
	public String getFailedMsg(){
		return failedMsg;
	}
	public Long getOperId(){
		return operId;
	}
	public String getOperator(){
		return operator;
	}
	public Date getOperTime(){
		return operTime;
	}
	public String getRemark(){
		return remark;
	}
	public void setId(Long id){
		this.id=id;
	}
	public void setBizId(Long bizId){
		this.bizId=bizId;
	}
	public void setBizType(Integer bizType){
		this.bizType=bizType;
	}
	public void setItemId(Long itemId){
		this.itemId=itemId;
	}
	public void setSku(String sku){
		this.sku=sku;
	}
	public void setSpId(Long spId){
		this.spId=spId;
	}
	public void setWarehouseId(Long warehouseId){
		this.warehouseId=warehouseId;
	}
	public void setOperType(Integer operType){
		this.operType=operType;
	}
	public void setOperAmount(Integer operAmount){
		this.operAmount=operAmount;
	}
	public void setOperResult(Integer operResult){
		this.operResult=operResult;
	}
	public void setFailedCode(String failedCode){
		this.failedCode=failedCode;
	}
	public void setFailedMsg(String failedMsg){
		this.failedMsg=failedMsg;
	}
	public void setOperId(Long operId){
		this.operId=operId;
	}
	public void setOperator(String operator){
		this.operator=operator;
	}
	public void setOperTime(Date operTime){
		this.operTime=operTime;
	}
	public void setRemark(String remark){
		this.remark=remark;
	}
}
