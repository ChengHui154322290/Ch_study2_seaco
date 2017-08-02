package com.tp.common.dao;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ResultMap;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tp.common.annotation.Id;
import com.tp.datasource.ContextHolder;

@Intercepts({
		@Signature(
				type = Executor.class, method = "update", 
				args = { MappedStatement.class,Object.class }),
		@Signature(
				type = Executor.class, method = "query", 
				args = { MappedStatement.class,Object.class, RowBounds.class, ResultHandler.class}) 
		})
public class BaseDaoInterceptor implements Interceptor {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(BaseDaoInterceptor.class);
	private static final Method[] methods = BaseDao.class.getMethods();
	
	private static final Map<Class<?>,Field> ID_FIELD_MAP = new ConcurrentHashMap<Class<?>,Field>();

	public Object intercept(Invocation invocation) throws Throwable {
		MappedStatement statement = (MappedStatement) invocation.getArgs()[0];
		Object parameter = (Object) invocation.getArgs()[1];
		
		String sqlId = statement.getId();
		String methodId = sqlId.substring(sqlId.lastIndexOf(".")+1, sqlId.length());
		setDataSource(statement,parameter);
		DtoUtil.MapperWrap mapperWrap = setSqlMapper(sqlId,statement,parameter);
		if(null==mapperWrap){
			return invocation.proceed();
		}
		
		Class<?> currentClass = mapperWrap.getDaoClass();
		Class<?> resultType = mapperWrap.getResultType();
		if (!isBaseMethod(currentClass, methodId)) {
			return invocation.proceed();
		}
		setPrimaryKey(statement,mapperWrap);
		setPrimaryValue(parameter, invocation, resultType, methodId);
		setResultClass(statement, resultType, methodId);
		setClass(parameter, resultType);
		return invocation.proceed();
	}

	public Object plugin(Object target) {
		if (target instanceof Executor) {
			return Plugin.wrap(target, this);
		} else {
			return target;
		}
	}

	public void setProperties(Properties properties) {

	}
	@SuppressWarnings("unused")
	private void setPrimaryKey(final MappedStatement statement,final DtoUtil.MapperWrap mapperWrap){
		String[] keys = statement.getKeyProperties();
		String[] keyColumns = statement.getKeyColumns();
		keys[0]=mapperWrap.getIdParams();
		if(StringUtils.isNoneBlank(mapperWrap.getIdColumn())){
			keyColumns = new String[]{mapperWrap.getIdColumn()};
		}
	}
	
	private void setPrimaryValue(Object parameter, Invocation invocation, Class<?> paramClass,
			String methodId) throws InstantiationException, IllegalAccessException, NoSuchFieldException, SecurityException {
		if ("queryById".equals(methodId) || "deleteById".equals(methodId)) {
			Object param = paramClass.newInstance();
			Field field = ID_FIELD_MAP.get(paramClass);
			if(field==null){
				for(Field fd:paramClass.getDeclaredFields()){
					if(fd.getAnnotation(Id.class)!=null){
						field = fd;
						ID_FIELD_MAP.put(paramClass, field);
						break;
					}
				}
			}
			if(field==null){
				throw new RuntimeException(paramClass.getName()+"没有找到主键解释@Id");
			}
			field.setAccessible(true);
			if (field.getType().getSimpleName().equals(Long.class.getSimpleName())) {
				if (parameter instanceof Integer) {
					field.set(param, ((Integer) parameter).longValue());
				} else {
					field.set(param, parameter);
				}
			} else {
				if (parameter instanceof Long) {
					field.set(param, ((Long) parameter).intValue());
				} else {
					field.set(param, parameter);
				}
			}
			invocation.getArgs()[1] = param;
			parameter = param;
		}
	}

	private void setResultClass(MappedStatement statement, Class<?> currentClass, String methodId) 
			throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException{
		if (methodId.startsWith("query") && !methodId.endsWith("Count")) {
			List<ResultMap> resultMaps = statement.getResultMaps();
			ResultMap resultMap = resultMaps.get(0);
			Field rf = resultMap.getClass().getDeclaredField("type");
			rf.setAccessible(Boolean.TRUE);
			rf.set(resultMap, currentClass);
		} else if (methodId.endsWith("Count")) {
			ResultMap resultMap = statement.getResultMaps().get(0);
			Field rf = resultMap.getClass().getDeclaredField("type");
			rf.setAccessible(Boolean.TRUE);
			rf.set(resultMap, Integer.class);
		}
	}

	@SuppressWarnings("unchecked")
	private void setClass(Object parameter, Class<?> currentClass) throws InstantiationException,
			IllegalAccessException, ClassNotFoundException {
		if (parameter instanceof Map) {
			((Map<String,Object>) parameter).put(DtoUtil.SPACE_TABLE_NAME, currentClass.newInstance());
		}
	}

	private boolean isBaseMethod(Class<?> currentClass, String methodId)
			throws ClassNotFoundException {

		Boolean isBaseMethod = Boolean.FALSE;
		for (Method method : methods) {
			String methodName = method.getName();
			if (methodId.equals(methodName)) {
				isBaseMethod = Boolean.TRUE;
			}
		}

		if (!isBaseMethod) {
			return Boolean.FALSE;
		}

		isBaseMethod = Boolean.FALSE;
		for (Class<?> clazz : currentClass.getInterfaces()) {
			if (clazz.equals(BaseDao.class)) {
				isBaseMethod = Boolean.TRUE;
			}
		}
		return isBaseMethod;
	}

	private Class<?> getClass(String className,Collection<Class<?>> clazzes) throws ClassNotFoundException {
		for(Class<?> clazz : clazzes){
			if(className.equals(clazz.getName())){
				return clazz;
			}
		}
		return null;
	}
	
	private Class<?> getResultType(Class<?> clazzDao){
		Type[] params = clazzDao.getGenericInterfaces();
		Type tc = null;
		if(params!=null && params.length>0 && params[0] instanceof ParameterizedType){
			tc = ((ParameterizedType)params[0]).getActualTypeArguments()[0];
		}
		return (Class<?>)tc;
	}
	
	private Map<String,Field> getResultMapping(Class<?> resultType){
		Field[] fields = resultType.getDeclaredFields();
		Map<String,Field> resultColumnMap = new HashMap<String,Field>();
		for (Field field : fields) {
			DtoUtil.putField(field,resultColumnMap);
		}
		Class<?> parentClass = resultType.getSuperclass();
		if(null!=parentClass && !parentClass.getSimpleName().equals("Serializable")){
			fields = resultType.getSuperclass().getDeclaredFields();
			for (Field field : fields) {
				DtoUtil.putField(field,resultColumnMap);
			}
		}
		return resultColumnMap;
	}
	
	private Map<String,String> getResultColumn(Map<String,Field> resultMapping){
		Map<String,String> resultColumnMap = new HashMap<String,String>();
		for(Map.Entry<String,Field> entry : resultMapping.entrySet()){
			Field field = entry.getValue();
			DtoUtil.putFieldColumn(field,resultColumnMap);
		}
		return resultColumnMap;
	}
	
	private DtoUtil.MapperWrap setSqlMapper(final String sqlId,final MappedStatement statement,final Object parameter) throws ClassNotFoundException{
		String daoClassName = sqlId.substring(0, sqlId.lastIndexOf("."));
		if(!DtoUtil.DAO_METHOD_MAP.containsKey(daoClassName)){
			String className = sqlId.substring(0, sqlId.lastIndexOf("."));
			Class<?> currentClass = getClass(className,statement.getConfiguration().getMapperRegistry().getMappers());
			DtoUtil.MapperWrap mapperWrap = new DtoUtil.MapperWrap();
			Class<?> resultType = getResultType(currentClass);
			if(resultType==null){
				return null;
			}
			Field field = ID_FIELD_MAP.get(resultType);
			if(field==null){
				for(Field fd:resultType.getDeclaredFields()){
					if(fd.getAnnotation(Id.class)!=null){
						field = fd;
						ID_FIELD_MAP.put(resultType, field);
						break;
					}
				}
			}
			if(field==null){
				LOGGER.error(resultType.getName()+"没有找到主键解释@Id");
				throw new RuntimeException(resultType.getName()+"没有找到主键解释@Id");
			}
			String[] urlarray = resultType.getName().split("\\.");
			String paramId = field.getName();
			mapperWrap.setKey(resultType.getName());
			mapperWrap.setDaoClassName(daoClassName);
			mapperWrap.setDaoClass(currentClass);
			mapperWrap.setResultType(resultType);
			mapperWrap.setResultTypeSimpleName(resultType.getSimpleName());
			mapperWrap.setTableName(urlarray[urlarray.length-2]+"_"+mapperWrap.getResultTypeSimpleName().replaceAll("([A-Z])", "_$1").replaceFirst("_", "").toLowerCase());
			mapperWrap.setParamsType(resultType);
			mapperWrap.setParamsTypeName(resultType.getName());
			mapperWrap.setIdParams(paramId);
			mapperWrap.setIdColumn(paramId.replaceAll("([A-Z])", "_$1").toLowerCase());
			mapperWrap.setFieldMap(this.getResultMapping(resultType));
			mapperWrap.setColumnMap(this.getResultColumn(mapperWrap.getFieldMap()));
			DtoUtil.DAO_METHOD_MAP.put(daoClassName, mapperWrap);
			DtoUtil.DAO_RESULT_MAP.put(mapperWrap.getKey(), mapperWrap);
		}
		return DtoUtil.DAO_METHOD_MAP.get(daoClassName);
	}
	
	private void setDataSource(final MappedStatement statement,final Object parameter){
		if(SqlCommandType.SELECT.equals(statement.getSqlCommandType())){
			ContextHolder.setCustomerKey(ContextHolder.DATA_SOURCE_KEY.SLAVE_SALE_ORDER_DATA_SOURCE);
		}else{
			ContextHolder.setCustomerKey(ContextHolder.DATA_SOURCE_KEY.MASTER_SALE_ORDER_DATA_SOURCE);
		}
		if(parameter instanceof Map  && MapUtils.isNotEmpty(((Map<?,?>) parameter))){
			Map<?,?> params = ((Map<?,?>) parameter);
			ContextHolder.DATA_SOURCE_KEY dataSourceKey =ContextHolder.DATA_SOURCE_KEY.SLAVE_SALE_ORDER_DATA_SOURCE;
			for(Object key:params.keySet()){
				if("dataSourceKey".equals(key)){
					dataSourceKey = (ContextHolder.DATA_SOURCE_KEY)params.get(key);
					ContextHolder.setCustomerKey(dataSourceKey);
				}
			}
			params.remove("dataSourceKey");
		}
	}
}
