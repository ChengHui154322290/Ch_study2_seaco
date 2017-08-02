package com.tp.model.wms;

import java.io.Serializable;

import java.util.Date;
import java.util.List;

import com.tp.common.annotation.Id;
import com.tp.common.annotation.Virtual;
import com.tp.common.vo.wms.WmsConstant;
import com.tp.model.BaseDO;
/**
  * @author szy
  * 运单号申请表
  */
public class WaybillApplication extends BaseDO implements Serializable {

	private static final long serialVersionUID = 1464762739759L;

	/** 数据类型bigint(20)*/
	@Id
	private Long id;
	
	/**快递企业编码 数据类型varchar(30)*/
	private String logisticsCode;
	
	/**快递企业名称 数据类型varchar(30)*/
	private String logisticsName;
	
	/**申请总数 数据类型bigint(20)*/
	private Long amount;
	
	/**申请的运单号区间下限 数据类型bigint(20)*/
	private Long waybillLow;
	
	/**申请的运单号区间上限 数据类型bigint(20)*/
	private Long waybillUp;
	
	/**实际数量 数据类型bigint(20)*/
	private Long actualAmount;
	
	/**申请状态：0成功1失败 数据类型tinyint(1)*/
	private Integer status;
	
	/**申请返回结果 数据类型varchar(300)*/
	private String resultMsg;
	
	/**申请时间 数据类型datetime*/
	private Date applyTime;
	
	/**创建时间 数据类型datetime*/
	private Date createTime;
	
	/**更新时间 数据类型datetime*/
	private Date updateTime;
	
	/**备注 数据类型varchar(255)*/
	private String remark;
	
	@Virtual
	private List<WaybillDetail> details;
	
	public String getStatusStr(){
		return status == 0 ? "成功":"失败";
	}
	
	public Long getId(){
		return id;
	}
	public String getLogisticsCode(){
		return logisticsCode;
	}
	public String getLogisticsName(){
		return logisticsName;
	}
	public Long getAmount(){
		return amount;
	}
	public Long getWaybillLow(){
		return waybillLow;
	}
	public Long getWaybillUp(){
		return waybillUp;
	}
	public Long getActualAmount(){
		return actualAmount;
	}
	public Integer getStatus(){
		return status;
	}
	public String getResultMsg(){
		return resultMsg;
	}
	public Date getApplyTime(){
		return applyTime;
	}
	public Date getCreateTime(){
		return createTime;
	}
	public Date getUpdateTime(){
		return updateTime;
	}
	public String getRemark(){
		return remark;
	}
	public void setId(Long id){
		this.id=id;
	}
	public void setLogisticsCode(String logisticsCode){
		this.logisticsCode=logisticsCode;
	}
	public void setLogisticsName(String logisticsName){
		this.logisticsName=logisticsName;
	}
	public void setAmount(Long amount){
		this.amount=amount;
	}
	public void setWaybillLow(Long waybillLow){
		this.waybillLow=waybillLow;
	}
	public void setWaybillUp(Long waybillUp){
		this.waybillUp=waybillUp;
	}
	public void setActualAmount(Long actualAmount){
		this.actualAmount=actualAmount;
	}
	public void setStatus(Integer status){
		this.status=status;
	}
	public void setResultMsg(String resultMsg){
		this.resultMsg=resultMsg;
	}
	public void setApplyTime(Date applyTime){
		this.applyTime=applyTime;
	}
	public void setCreateTime(Date createTime){
		this.createTime=createTime;
	}
	public void setUpdateTime(Date updateTime){
		this.updateTime=updateTime;
	}
	public void setRemark(String remark){
		this.remark=remark;
	}
	public List<WaybillDetail> getDetails() {
		return details;
	}
	public void setDetails(List<WaybillDetail> details) {
		this.details = details;
	}
}
