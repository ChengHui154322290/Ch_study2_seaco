<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="com.tp.dao.${modelFixed}.${modelName}Dao">
	<resultMap type="com.tp.model.${modelFixed}.${modelName}" id="${modelNameMin}">
		<#list properties as property>
			<#if property.primary>
				<id column="${property.column}" property="${property.name}"/>
			<#else>
				<result column="${property.column}" property="${property.name}"/>
			</#if>
		</#list>
	</resultMap>
	
	<sql id="columns">
		<#list properties as property><#if property_index &gt; 0>,</#if>${property.column}</#list>
	</sql>
</mapper>