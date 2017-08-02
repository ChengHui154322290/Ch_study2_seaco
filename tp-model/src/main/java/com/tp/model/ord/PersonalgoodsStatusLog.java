package com.tp.model.ord;

import java.io.Serializable;

import java.util.Date;

import com.tp.common.annotation.Id;
import com.tp.model.BaseDO;
/**
  * @author szy
  * 个人物品申报单审单状态日志(包括回执与主动查询)
  */
public class PersonalgoodsStatusLog extends BaseDO implements Serializable {

	private static final long serialVersionUID = 1465203515899L;

	/**主键ID 数据类型bigint(20)*/
	@Id
	private Long id;
	
	/**订单号 数据类型bigint(20)*/
	private Long orderCode;
	
	/**申报单号 数据类型varchar(20)*/
	private String declareNo;
	
	/**海关生成申报单编号 数据类型varchar(30)*/
	private String customsDeclareNo;
	
	/**海关编号 数据类型varchar(30)*/
	private String customsCode;
	
	/**类型：0回调1查询 数据类型tinyint(1)*/
	private Integer type;
	
	/**结果 数据类型varchar(20)*/
	private String result;
	
	/**结果描述 数据类型varchar(20)*/
	private String resultDesc;
	
	/**详情 数据类型varchar(512)*/
	private String resultDetail;
	
	/**回执原始数据 数据类型varchar(2048)*/
	private String resData;
	
	/**备注 数据类型varchar(255)*/
	private String remark;
	
	/**创建时间 数据类型datetime*/
	private Date createTime;
	
	
	public Long getId(){
		return id;
	}
	public Long getOrderCode(){
		return orderCode;
	}
	public String getDeclareNo(){
		return declareNo;
	}
	public String getCustomsDeclareNo(){
		return customsDeclareNo;
	}
	public String getCustomsCode(){
		return customsCode;
	}
	public Integer getType(){
		return type;
	}
	public String getResult(){
		return result;
	}
	public String getResultDesc(){
		return resultDesc;
	}
	public String getResultDetail(){
		return resultDetail;
	}
	public String getResData(){
		return resData;
	}
	public String getRemark(){
		return remark;
	}
	public Date getCreateTime(){
		return createTime;
	}
	public void setId(Long id){
		this.id=id;
	}
	public void setOrderCode(Long orderCode){
		this.orderCode=orderCode;
	}
	public void setDeclareNo(String declareNo){
		this.declareNo=declareNo;
	}
	public void setCustomsDeclareNo(String customsDeclareNo){
		this.customsDeclareNo=customsDeclareNo;
	}
	public void setCustomsCode(String customsCode){
		this.customsCode=customsCode;
	}
	public void setType(Integer type){
		this.type=type;
	}
	public void setResult(String result){
		this.result=result;
	}
	public void setResultDesc(String resultDesc){
		this.resultDesc=resultDesc;
	}
	public void setResultDetail(String resultDetail){
		this.resultDetail=resultDetail;
	}
	public void setResData(String resData){
		this.resData=resData;
	}
	public void setRemark(String remark){
		this.remark=remark;
	}
	public void setCreateTime(Date createTime){
		this.createTime=createTime;
	}
}
