package com.tp.model.ord;

import java.io.Serializable;
import java.util.Date;

import com.tp.common.annotation.Id;
import com.tp.common.vo.ord.OrderReceiptConstant.ReceiptTitleType;
import com.tp.model.BaseDO;
import com.tp.model.ord.OrderReceipt;
/**
  * @author szy
  * 订单发票表
  */
public class OrderReceipt extends BaseDO implements Serializable {

	private static final long serialVersionUID = 1451468597513L;

	/** 数据类型bigint(20)*/
	@Id
	private Long id;
	
	/**父订单ID 数据类型bigint(20)*/
	private Long parentOrderId;
	
	/**父订单代码 数据类型bigint(20)*/
	private Long parentOrderCode;
	
	/**发票类型（1：普通纸质。2：电子票。3：增值税发票） 数据类型tinyint(3)*/
	private Integer type;
	
	/**抬头类型（1.个人 2.公司） 数据类型tinyint(3)*/
	private Integer titleType;
	
	/**发票抬头 数据类型varchar(50)*/
	private String title;
	
	/**发票内容类型（1：明细。2：办公用品。3：电脑配件。4：耗材） 数据类型tinyint(3)*/
	private Integer contentType;
	
	/**创建时间 数据类型datetime*/
	private Date createTime;
	
	/**修改时间 数据类型datetime*/
	private Date updateTime;
	
	/**
	 * 获取发票抬头 
	 * 
	 * @param receipt
	 * @return
	 */
	public static String getTitle(OrderReceipt receipt) {
		if (null != receipt) {
			if (ReceiptTitleType.PERSON.code.equals(receipt.getTitleType())) {	// 个人
				return ReceiptTitleType.PERSON.cnName;
			} else {	// 公司
				return receipt.getTitle();
			}
		}
		return null;
	}	
	public Long getId(){
		return id;
	}
	public Long getParentOrderId(){
		return parentOrderId;
	}
	public Long getParentOrderCode(){
		return parentOrderCode;
	}
	public Integer getType(){
		return type;
	}
	public Integer getTitleType(){
		return titleType;
	}
	public String getTitle(){
		return title;
	}
	public Integer getContentType(){
		return contentType;
	}
	public Date getCreateTime(){
		return createTime;
	}
	public Date getUpdateTime(){
		return updateTime;
	}
	public void setId(Long id){
		this.id=id;
	}
	public void setParentOrderId(Long parentOrderId){
		this.parentOrderId=parentOrderId;
	}
	public void setParentOrderCode(Long parentOrderCode){
		this.parentOrderCode=parentOrderCode;
	}
	public void setType(Integer type){
		this.type=type;
	}
	public void setTitleType(Integer titleType){
		this.titleType=titleType;
	}
	public void setTitle(String title){
		this.title=title;
	}
	public void setContentType(Integer contentType){
		this.contentType=contentType;
	}
	public void setCreateTime(Date createTime){
		this.createTime=createTime;
	}
	public void setUpdateTime(Date updateTime){
		this.updateTime=updateTime;
	}
}
