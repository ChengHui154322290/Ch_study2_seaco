package com.tp.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Model implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8466614731406364658L;

	/**bean类名称*/
	private String modelName;
	/**bean对象名称*/
	private String modelNameMin;
	/**bean所在包名*/
	private String modelFixed;
	/**bean说明*/
	private String modelMark;
	
	private String datetime = System.currentTimeMillis()+"";
	

	/**bean 属性集*/
	private List<Property> properties = new ArrayList<Property>();

	public Boolean getHasDate(){
		for(Property property:properties){
			if(property!=null && "Date".equals(property.getType())){
				return Boolean.TRUE;
			}
		}
		return Boolean.FALSE;
	}
	public String getDatetime() {
		return datetime;
	}
	
	public String getModelName() {
		return modelName;
	}

	public void setModelName(String modelName) {
		this.modelName = modelName;
	}

	public String getModelNameMin() {
		return modelNameMin;
	}

	public void setModelNameMin(String modelNameMin) {
		this.modelNameMin = modelNameMin;
	}

	public String getModelFixed() {
		return modelFixed;
	}

	public void setModelFixed(String modelFixed) {
		this.modelFixed = modelFixed;
	}

	public String getModelMark() {
		return modelMark;
	}

	public void setModelMark(String modelMark) {
		this.modelMark = modelMark;
	}

	public List<Property> getProperties() {
		return properties;
	}

	public void setProperties(List<Property> properties) {
		this.properties = properties;
	}
	
}
