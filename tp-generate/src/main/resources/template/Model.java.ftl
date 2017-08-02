package com.tp.model.${modelFixed};

import java.io.Serializable;

<#if hasDate>
import java.util.Date;
</#if>

import com.tp.common.annotation.Id;
import com.tp.model.BaseDO;
/**
  * @author szy
  * ${modelMark}
  */
public class ${modelName} extends BaseDO implements Serializable {

	private static final long serialVersionUID = ${datetime}L;

	<#list properties as property>
	/**${property.mark}*/<#if property.primary>
	@Id</#if>
	private ${property.type} ${property.name};
	
	</#list>
	
	<#list properties as property>
	public ${property.type} get${property.methodName}(){
		return ${property.name};
	}
	</#list>
	<#list properties as property>
	public void set${property.methodName}(${property.type} ${property.name}){
		this.${property.name}=${property.name};
	}
	</#list>
}
