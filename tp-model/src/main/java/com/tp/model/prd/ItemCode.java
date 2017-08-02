package com.tp.model.prd;

import java.io.Serializable;

import com.tp.common.annotation.Id;
import com.tp.model.BaseDO;
/**
  * @author szy
  * 商品编码生成表
  */
public class ItemCode extends BaseDO implements Serializable {

	private static final long serialVersionUID = 1450873698777L;

	/**主键 数据类型bigint(18)*/
	@Id
	private Long id;
	
	/**小类编号,spu编码,prdid编码 数据类型varchar(16)*/
	private String code;
	
	/**对应下面最大数值，默认为0 数据类型int(4)*/
	private Integer value;
	
	
	public Long getId(){
		return id;
	}
	public String getCode(){
		return code;
	}
	public Integer getValue(){
		return value;
	}
	public void setId(Long id){
		this.id=id;
	}
	public void setCode(String code){
		this.code=code;
	}
	public void setValue(Integer value){
		this.value=value;
	}
}
