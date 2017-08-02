package com.tp.common.dao;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tp.common.annotation.Id;
import com.tp.common.annotation.Virtual;
import com.tp.common.vo.DAOConstant;
import com.tp.common.vo.DAOConstant.MYBATIS_SPECIAL_STRING;
import com.tp.util.StringUtil;

public final class DtoUtil {
	private static final Logger logger = LoggerFactory.getLogger(DtoUtil.class);
	public final static String SPLIT_DAO_RESULT_SIGN="|";
	public final static Map<String,Class<?>> FIELD_TYPE_MAP = new HashMap<String,Class<?>>();
	static{
		FIELD_TYPE_MAP.put("Double", Double.class);
		FIELD_TYPE_MAP.put("Short", Short.class);
		FIELD_TYPE_MAP.put("Long", Long.class);
		FIELD_TYPE_MAP.put("Float", Float.class);
		FIELD_TYPE_MAP.put("Integer", Integer.class);
		FIELD_TYPE_MAP.put("Byte", Byte.class);
		FIELD_TYPE_MAP.put("String", String.class);
		FIELD_TYPE_MAP.put("Character", Character.class);
		FIELD_TYPE_MAP.put("sDate", Date.class);
		FIELD_TYPE_MAP.put("Boolean", Boolean.class);
		FIELD_TYPE_MAP.put("Date", java.util.Date.class);
		FIELD_TYPE_MAP.put("double", double.class);
		FIELD_TYPE_MAP.put("short", short.class);
		FIELD_TYPE_MAP.put("long", long.class);
		FIELD_TYPE_MAP.put("float", float.class);
		FIELD_TYPE_MAP.put("int", int.class);
		FIELD_TYPE_MAP.put("byte", byte.class);
		FIELD_TYPE_MAP.put("char", char.class);
		FIELD_TYPE_MAP.put("boolean", boolean.class);
	}
	public static final String SPACE_TABLE_NAME="SPACE_TABLE_NAME";
	/**
	 * 用于存放POJO的列信息
	 */
	private final static Map<Class<? extends Serializable>,Map<String,String>> columnMap = new ConcurrentHashMap<Class<? extends Serializable>, Map<String,String>>();
	
	public final static Map<String,MapperWrap> DAO_METHOD_MAP = new ConcurrentHashMap<String,MapperWrap>();
	public final static Map<String,MapperWrap> DAO_RESULT_MAP = new ConcurrentHashMap<String,MapperWrap>();
	
	/**
	 * 获取POJO对应的表名 需要POJO中的属性定义@Table(name)
	 * 
	 * @return
	 */
	public static String tableName(Serializable obj) {
		MapperWrap mapperWrap = DtoUtil.DAO_RESULT_MAP.get(obj.getClass().getName());
		
		return mapperWrap.getTableName();
	}

	/**
	 * 获取POJO中的主键字段名 需要定义@Id
	 * 
	 * @return
	 */
	public static String id(Serializable obj) {
		for (Field field : obj.getClass().getDeclaredFields()) {
			if (null != field.getAnnotation(Id.class))
				return field.getName();
		}
		if(obj.getClass().equals(Long.class) || obj.getClass().equals(long.class) || obj.getClass().equals(int.class)|| obj.getClass().equals(Integer.class)){
			return "id";
		}
		throw new RuntimeException("undefine " + obj.getClass().getName()
				+ " @Id");
	}

	private static boolean isNull(Serializable obj, String fieldname) {
		try {
			Field field = obj.getClass().getDeclaredField(fieldname);
			return isNull(obj, field);
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (NoSuchFieldException e) {
            try {
                Field field = obj.getClass().getSuperclass().getDeclaredField(fieldname);
                return isNull(obj, field);
            } catch (NoSuchFieldException e1) {
                e1.printStackTrace();
            }
        } catch (IllegalArgumentException e) {
			e.printStackTrace();
		}

		return false;
	}

	private static boolean isNull(Serializable obj, Field field) {
		try {
			field.setAccessible(true);
			return field.get(obj) == null;
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}

		return false;
	}

	/**
	 * 用于计算类定义 需要POJO中的属性定义@Column(name)
	 */
	public static void caculationColumnList(final Serializable obj) {
		Class<? extends Serializable> className = obj.getClass();
		if (columnMap.containsKey(className))
			return;

		Field[] fields = className.getDeclaredFields();
		Map<String,String> fieldMap = new HashMap<String,String>();
		for (Field field : fields) {
			Virtual notColumn =field.getAnnotation(Virtual.class);
			boolean isStatic = Modifier.isStatic(field.getModifiers());
			boolean isFinal= Modifier.isFinal(field.getModifiers());
			boolean isTrans = Modifier.isTransient(field.getModifiers());
			boolean isPrimitive =field.getType().isPrimitive() || FIELD_TYPE_MAP.containsValue(field.getType());
			if(null!=notColumn || isStatic || isFinal ||!isPrimitive || isTrans){
				continue;
			}
			String fieldName = field.getName();
			String column = fieldName.replaceAll("([A-Z])", "_$1").toLowerCase();
			fieldMap.put(fieldName, column);
		}
		logger.info("className:"+className+"  fields:"+fieldMap);
		columnMap.put(className, fieldMap);
		Class<?> parentClass = className.getSuperclass();
		if(null!=parentClass && !parentClass.getSimpleName().equals("Serializable")){
			fields = className.getSuperclass().getDeclaredFields();
			for (Field field : fields) {
				Virtual notColumn =field.getAnnotation(Virtual.class);
				boolean isStatic = Modifier.isStatic(field.getModifiers());
				boolean isFinal= Modifier.isFinal(field.getModifiers());
				boolean isPrimitive =field.getType().isPrimitive() || FIELD_TYPE_MAP.containsValue(field.getType());
				if(null!=notColumn || isStatic || isFinal ||!isPrimitive){
					continue;
				}
				String fieldName = field.getName();
				String column = fieldName.replaceAll("([A-Z])", "_$1").toLowerCase();
				fieldMap.put(fieldName, column);
			}
		}
	}
	public static void putField(final Field field,final Map<String,Field> fieldMap){
		Virtual notColumn =field.getAnnotation(Virtual.class);
		boolean isStatic = Modifier.isStatic(field.getModifiers());
		boolean isFinal= Modifier.isFinal(field.getModifiers());
		boolean isPrimitive =field.getType().isPrimitive() || FIELD_TYPE_MAP.containsValue(field.getType());
		if(null!=notColumn || isStatic || isFinal ||!isPrimitive){
			return;
		}
		fieldMap.put(field.getName(), field);
	}
	
	public static void putFieldColumn(final Field field,final Map<String,String> fieldMap){
		Virtual notColumn =field.getAnnotation(Virtual.class);
		boolean isStatic = Modifier.isStatic(field.getModifiers());
		boolean isFinal= Modifier.isFinal(field.getModifiers());
		boolean isPrimitive =field.getType().isPrimitive() || FIELD_TYPE_MAP.containsValue(field.getType());
		if(null!=notColumn || isStatic || isFinal ||!isPrimitive){
			return;
		}
		String fieldName = field.getName();
		String column = fieldName.replaceAll("([A-Z])", "_$1").toLowerCase();
		fieldMap.put(fieldName, column);
	}
	
	@SuppressWarnings("unchecked")
	public static void caculationColumnList(final Class<?> className) {
		if (columnMap.containsKey(className))
			return;

		Field[] fields = className.getDeclaredFields();
		Map<String,String> fieldMap = new HashMap<String,String>();
		for (Field field : fields) {
			Virtual notColumn =field.getAnnotation(Virtual.class);
			boolean isStatic = Modifier.isStatic(field.getModifiers());
			boolean isFinal= Modifier.isFinal(field.getModifiers());
			boolean isPrimitive =field.getType().isPrimitive() || FIELD_TYPE_MAP.containsValue(field.getType());
			if(null!=notColumn || isStatic || isFinal ||!isPrimitive){
				continue;
			}
			String fieldName = field.getName();
			String column = fieldName.replaceAll("([A-Z])", "_$1").toLowerCase();
			fieldMap.put(fieldName, column);
		}

		columnMap.put((Class<? extends Serializable>) className, fieldMap);
		Class<?> parentClass = className.getSuperclass();
		if(null!=parentClass && !parentClass.getSimpleName().equals("Serializable")){
			fields = className.getSuperclass().getDeclaredFields();
			for (Field field : fields) {
				Virtual notColumn =field.getAnnotation(Virtual.class);
				boolean isStatic = Modifier.isStatic(field.getModifiers());
				boolean isFinal= Modifier.isFinal(field.getModifiers());
				boolean isPrimitive =field.getType().isPrimitive() || FIELD_TYPE_MAP.containsValue(field.getType());
				if(null!=notColumn || isStatic || isFinal ||!isPrimitive){
					continue;
				}
				String fieldName = field.getName();
				String column = fieldName.replaceAll("([A-Z])", "_$1").toLowerCase();
				fieldMap.put(fieldName, column);
			}
		}
	}
	
	/**
	 * Where条件信息
	 * 
	 * @author szy
	 * 
	 */
	public class WhereColumn {
		public String name;
		public boolean isString;

		public WhereColumn(String name, boolean isString) {
			this.name = name;
			this.isString = isString;
		}
	}

	/**
	 * 用于获取Insert的字段累加
	 * 
	 * @return
	 */
	public static String returnInsertColumnsName(final Serializable obj) {
		StringBuilder sb = new StringBuilder();

		Map<String,String> fieldMap = columnMap.get(obj.getClass());
		Iterator<String> iterator= fieldMap.keySet().iterator();
		int i = 0;
		while(iterator.hasNext()){
			String fieldname=iterator.next();
			if (isNull(obj, fieldname) && !fieldname.contains("createTime") && !fieldname.contains("updateTime"))
				continue;
			if(i++!=0){
				sb.append(',');
			}
			sb.append(fieldMap.get(fieldname));
		}
		return sb.toString();
	}

	/**
	 * 用于获取Insert的字段映射累加
	 * 
	 * @return
	 */
	public static String returnInsertColumnsDefine(final Serializable obj) {
		StringBuilder sb = new StringBuilder();

		Map<String,String> fieldMap = columnMap.get(obj.getClass());
		Iterator<String> iterator= fieldMap.keySet().iterator();
		int i = 0;
		while(iterator.hasNext()){
			String fieldname=iterator.next();
			boolean isTime = fieldname.equalsIgnoreCase("createTime") || fieldname.equalsIgnoreCase("updateTime");
			if ((!isTime) && isNull(obj, fieldname))
				continue;
			if(i++!=0){
				sb.append(',');
			}
			if(isTime){
				sb.append("NOW()");
			}else{
				sb.append("#{").append(fieldname).append('}');
			}
		}
		return sb.toString();
	}

	/**
	 * 用于获取Update Set的字段累加
	 * 
	 * @return
	 */
	public static String returnUpdateSetFull(final Serializable obj) {
		StringBuilder sb = new StringBuilder();

		Map<String,String> fieldMap = columnMap.get(obj.getClass());
		int i = 0;
		for (Map.Entry<String, String> column : fieldMap.entrySet()) {
			boolean isUpdateTime = column.getKey().equalsIgnoreCase("updateTime");
			boolean isCreateTime = column.getKey().equalsIgnoreCase("createTime");
			if (i++ != 0)
				sb.append(',');
			if(isUpdateTime){
				sb.append("update_time=NOW()");
			}else if(isCreateTime && isNull(obj, column.getKey())){
				sb.append("create_time=NOW()");
			}else{
				sb.append(column.getValue()).append("=#{").append(column.getKey()).append('}');
			}
			
		}
		return sb.toString();
	}

	/**
	 * 用于获取Update Set的字段累加
	 * 
	 * @return
	 */
	public static String returnUpdateSet(final Serializable obj) {
		StringBuilder sb = new StringBuilder();

		Map<String,String> fieldMap = columnMap.get(obj.getClass());
		int i = 0;
		for (Map.Entry<String, String> column : fieldMap.entrySet()) {
			String key = column.getKey();
			boolean isUpdateTime = key.equalsIgnoreCase("updateTime");
			if (isNull(obj, key) && !isUpdateTime)
				continue;

			if (i++ != 0)
				sb.append(',');
			if(isUpdateTime){
				sb.append("update_time=NOW()");
			}else{
				sb.append(column.getValue()).append("=#{").append(column.getKey()).append('}');
			}
		}
		return sb.toString();
	}
	
	/**
	 * 用于获取select、delete的条件组装
	 * @return
	 */
	public static String whereColumnNotNull(final Serializable obj) {
		StringBuilder sb = new StringBuilder();
		Map<String,String> fieldMap = columnMap.get(obj.getClass());
		int i = 0;
		for (Map.Entry<String, String> column : fieldMap.entrySet()) {
			if (isNull(obj, column.getKey()))
				continue;
			if (i++ != 0)
				sb.append(" AND ");
			sb.append(column.getValue()).append("=#{").append(column.getKey()+"}");
		}
		String sqlstr = sb.toString().trim().replaceAll("AND\\s+AND", "AND").replaceAll("^\\s*(AND)?(.*)$", "$2").replaceAll("^(.*)(AND)\\s*$", " $1 ");
		if(StringUtils.isNoneBlank(sqlstr)){
			return sqlstr;
		}
		return null;
	}
	
	/**
	 * 用于获取select、delete的条件组装
	 * @return
	 */
	public static String whereColumn(final Map<String,Object> param) {
		StringBuilder sb = new StringBuilder();
		int i = 0;
		for (Map.Entry<String, Object> column : param.entrySet()) {
			if (i++ != 0)
				sb.append(" AND ");
			if(!DAOConstant.MYBATIS_SPECIAL_STRING.list().contains(column.getKey().toUpperCase())){
				sb.append(column.getKey().replaceAll("([A-Z])", "_$1").toLowerCase()).append("=#{").append(column.getKey()+"}");
			}else if(DAOConstant.MYBATIS_SPECIAL_STRING.LIKE.name().equalsIgnoreCase(column.getKey())){
				sb.append(column.getValue());
			} else if (DAOConstant.MYBATIS_SPECIAL_STRING.COLUMNS.name().equalsIgnoreCase(column.getKey())) {
				sb.append(column.getValue());
			}else if(DAOConstant.MYBATIS_SPECIAL_STRING.INLIST.name().equalsIgnoreCase(column.getKey())){
				sb.append(addInList(param));
			}else if(DAOConstant.MYBATIS_SPECIAL_STRING.WHERE.name().equalsIgnoreCase(column.getKey())){
				sb.append(addWhere(param));
			}
		}
		String sqlstr = sb.toString().trim().replaceAll("AND\\s+AND", "AND").replaceAll("^\\s*(AND)?(.*)$", "$2").replaceAll("^(.*)(AND)\\s*$", " $1 ");
		if(StringUtils.isNoneBlank(sqlstr)){
			return sqlstr;
		}
		return null;
	}
	/**
	 * 用于获取select、delete的条件组装
	 * @return
	 */
	public static String whereColumnNotEmpty(final Map<String,Object> param) {
		StringBuilder sb = new StringBuilder();
		int i = 0;
		for (Map.Entry<String, Object> column : param.entrySet()) {
			if (column.getValue()==null || StringUtil.isBlank(column.getValue().toString()))
				continue;
			if (i++ != 0)
				sb.append(" AND ");
			if(!DAOConstant.MYBATIS_SPECIAL_STRING.list().contains(column.getKey().toUpperCase())){
				sb.append(column.getKey().replaceAll("([A-Z])", "_$1").toLowerCase()).append("=#{").append(column.getKey()+"}");
			}else if(DAOConstant.MYBATIS_SPECIAL_STRING.LIKE.name().equalsIgnoreCase(column.getKey())){
				sb.append(column.getValue());
			} else if (DAOConstant.MYBATIS_SPECIAL_STRING.COLUMNS.name().equalsIgnoreCase(column.getKey())) {
				sb.append(column.getValue());
			}else if(DAOConstant.MYBATIS_SPECIAL_STRING.INLIST.name().equalsIgnoreCase(column.getKey())){
				sb.append(addInList(param));
			}else if(DAOConstant.MYBATIS_SPECIAL_STRING.WHERE.name().equalsIgnoreCase(column.getKey())){
				sb.append(addWhere(param));
			}
		}
		String sqlstr = sb.toString().trim().replaceAll("AND\\s+AND", "AND").replaceAll("^\\s*(AND)?(.*)$", "$2").replaceAll("^(.*)(AND)\\s*$", " $1 ");
		if(StringUtils.isNoneBlank(sqlstr)){
			return sqlstr;
		}
		return null;
	}
	
	public static String queryColumn(final Serializable obj){
		StringBuilder sb = new StringBuilder();

		Map<String,String> fieldMap = columnMap.get(obj.getClass());
		int i = 0;
		for (Map.Entry<String, String> column : fieldMap.entrySet()) {
			if (i++ != 0)
				sb.append(',');
			sb.append(column.getValue()).append(" as ").append(column.getKey());
		}
		return sb.toString();
	}
	
	public static String queryColumn(final Map<String,String> fieldColumnMap){
		StringBuilder sb = new StringBuilder();
		int i = 0;
		for (Map.Entry<String, String> column : fieldColumnMap.entrySet()) {
			if (i++ != 0)
				sb.append(',');
			sb.append(column.getValue()).append(" as ").append(column.getKey());
		}
		return sb.toString();
	}
	/*
	public Integer getId(Serializable obj) {
		return 0;
	}*/

	/**
	 * 打印类字段信息
	 */
	public static String objString(final Serializable obj) {
		Field[] fields = obj.getClass().getDeclaredFields();
		StringBuilder sb = new StringBuilder();
		sb.append('[');
		for (Field f : fields) {
			if (Modifier.isStatic(f.getModifiers())
					|| Modifier.isFinal(f.getModifiers()))
				continue;
			Object value = null;
			try {
				f.setAccessible(true);
				value = f.get(obj);
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
			if (value != null)
				sb.append(f.getName()).append('=').append(value).append(',');
		}
		sb.append(']');

		return sb.toString();
	}
	
	 public static String addInList(final Map<String,Object> param){
	    	Object object = param.get(MYBATIS_SPECIAL_STRING.INLIST.name());
	    	if(object !=null && object instanceof Map){
	    		@SuppressWarnings("unchecked")
				Map<String,List<?>> inMap = (Map<String,List<?>>)object;
	    		if(MapUtils.isNotEmpty(inMap)){
	    			StringBuffer subSql = new StringBuffer("");
	    			for(Map.Entry<String,List<?>> entry:inMap.entrySet()){
	    				String key = entry.getKey();
	    				List<?> value = entry.getValue();
	    				if(CollectionUtils.isNotEmpty(value)){
	    					for(Object v:value){
	        					if(v instanceof Number){
	        						subSql.append(",").append(v);
	        					}else{
	        						subSql.append(",").append(v).append("\"");
	        					}
	        				}
	    					subSql.replace(0, 1, key.replaceAll("([A-Z])", "_$1").toLowerCase()+" in (");
	    					subSql.append(")");
	    				}
	    			}
	    			return subSql.toString();
	    		}
	    	}
	    	return "";
	    }
	public static String addWhere(final Map<String,Object> param){
		Object object = param.get(MYBATIS_SPECIAL_STRING.WHERE.name());
    	if(object !=null && object instanceof List){
    		StringBuffer sqlStr = new StringBuffer();
    		@SuppressWarnings("unchecked")
			List<?> list = (List<?>)object;
			for(Object obj:list){
				String subStr = obj.toString();
				if(obj instanceof DAOConstant.WHERE_ENTRY && StringUtil.isNoneBlank(subStr)){
					sqlStr.append(" AND ").append(subStr);
				}
			}
			return sqlStr.toString().replaceFirst("AND", "");
    	}
    	return "";
	}
	static class MapperWrap implements Serializable{
		/**
		 * 
		 */
		private static final long serialVersionUID = -7578889926308207995L;
		private String key;
		private String daoClassName;
		private String tableName;
		private String idColumn;
		private String idParams;
		private Map<String,String> columnMap = new HashMap<String,String>();
		private Map<String,Field> fieldMap = new HashMap<String,Field>();
		private String paramsTypeName;
		private String resultTypeSimpleName;
		private Class<?> daoClass;
		private Class<?> paramsType;
		private Class<?>  resultType= paramsType;
		
		public String getKey() {
			return key;
		}
		public void setKey(String key) {
			this.key = key;
		}
		public String getDaoClassName() {
			return daoClassName;
		}
		public void setDaoClassName(String daoClassName) {
			this.daoClassName = daoClassName;
		}
		public String getTableName() {
			return tableName;
		}
		public void setTableName(String tableName) {
			this.tableName = tableName;
		}
		public String getIdColumn() {
			return idColumn;
		}
		public void setIdColumn(String idColumn) {
			this.idColumn = idColumn;
		}
		public String getIdParams() {
			return idParams;
		}
		public void setIdParams(String idParams) {
			this.idParams = idParams;
		}
		public Map<String, String> getColumnMap() {
			return columnMap;
		}
		public void setColumnMap(Map<String, String> columnMap) {
			this.columnMap = columnMap;
		}
		public String getParamsTypeName() {
			return paramsTypeName;
		}
		public void setParamsTypeName(String paramsTypeName) {
			this.paramsTypeName = paramsTypeName;
		}
		public Class<?> getDaoClass() {
			return daoClass;
		}
		public void setDaoClass(Class<?> daoClass) {
			this.daoClass = daoClass;
		}
		public Class<?> getParamsType() {
			return paramsType;
		}
		public void setParamsType(Class<?> paramsType) {
			this.paramsType = paramsType;
		}
		public Class<?> getResultType() {
			return resultType;
		}
		public void setResultType(Class<?> resultType) {
			this.resultType = resultType;
		}
		public Map<String, Field> getFieldMap() {
			return fieldMap;
		}
		public void setFieldMap(Map<String, Field> fieldMap) {
			this.fieldMap = fieldMap;
		}
		public String getResultTypeSimpleName() {
			return resultTypeSimpleName;
		}
		public void setResultTypeSimpleName(String resultTypeSimpleName) {
			this.resultTypeSimpleName = resultTypeSimpleName;
		}
		
	}
	
}

