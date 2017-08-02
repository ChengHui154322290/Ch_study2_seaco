package com.tp.result.bse;

import java.io.Serializable;

/**
 * 
 * <pre>
 * 
 * </pre>
 *
 * @author szy
 * @version 0.0.1
 */
public class AreaTreeDTO implements Serializable {
	
	
	private static final long serialVersionUID = 4483514509080495117L;

	/** 主键*/
	private Long id;
	
	/** 父id*/
	private Long pId;
	
	/** 级别 :2-国家；3-区，4-省，5-市，6-县，7-街道*/
	private Integer type ;
	
	/** 名称*/
	private String name ;

	/**
	 * Getter method for property <tt>id</tt>.
	 * 
	 * @return property value of id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * Setter method for property <tt>id</tt>.
	 * 
	 * @param id value to be assigned to property id
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * Getter method for property <tt>pId</tt>.
	 * 
	 * @return property value of pId
	 */
	public Long getpId() {
		return pId;
	}

	/**
	 * Setter method for property <tt>pId</tt>.
	 * 
	 * @param pId value to be assigned to property pId
	 */
	public void setpId(Long pId) {
		this.pId = pId;
	}

	/**
	 * Getter method for property <tt>type</tt>.
	 * 
	 * @return property value of type
	 */
	public Integer getType() {
		return type;
	}

	/**
	 * Setter method for property <tt>type</tt>.
	 * 
	 * @param type value to be assigned to property type
	 */
	public void setType(Integer type) {
		this.type = type;
	}

	/**
	 * Getter method for property <tt>name</tt>.
	 * 
	 * @return property value of name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Setter method for property <tt>name</tt>.
	 * 
	 * @param name value to be assigned to property name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/** 
	 * @return
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "AreaTreeDTO [id=" + id + ", pId=" + pId + ", type=" + type
				+ ", name=" + name + "]";
	}
}
