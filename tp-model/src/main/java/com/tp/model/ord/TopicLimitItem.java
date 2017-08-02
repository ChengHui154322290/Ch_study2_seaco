package com.tp.model.ord;

import java.io.Serializable;

import com.tp.common.annotation.Id;
import com.tp.model.BaseDO;
/**
  * @author szy
  * 活动限购订单商品统计表
  */
public class TopicLimitItem extends BaseDO implements Serializable {

	private static final long serialVersionUID = 1451468597559L;

	/**主键 数据类型bigint(14)*/
	@Id
	private Long id;
	
	/**限购活动ID 数据类型bigint(14)*/
	private Long topicId;
	
	/**SKU编号 数据类型varchar(50)*/
	private String skuCode;
	
	/**限购对象值 数据类型varchar(16)*/
	private String limitValue;
	
	/**限购类型 数据类型tinyint(4)*/
	private Integer limitType;
	
	/**已买数量 数据类型int(5)*/
	private Integer buyedQuantity;
	
	
	public Long getId(){
		return id;
	}
	public Long getTopicId(){
		return topicId;
	}
	public String getSkuCode(){
		return skuCode;
	}
	public String getLimitValue(){
		if(this.limitValue==null){
			return "";
		}
		return limitValue;
	}
	public Integer getLimitType(){
		return limitType;
	}
	public Integer getBuyedQuantity(){
		return buyedQuantity;
	}
	public void setId(Long id){
		this.id=id;
	}
	public void setTopicId(Long topicId){
		this.topicId=topicId;
	}
	public void setSkuCode(String skuCode){
		this.skuCode=skuCode;
	}
	public void setLimitValue(String limitValue){
		this.limitValue=limitValue;
	}
	public void setLimitType(Integer limitType){
		this.limitType=limitType;
	}
	public void setBuyedQuantity(Integer buyedQuantity){
		this.buyedQuantity=buyedQuantity;
	}
}
