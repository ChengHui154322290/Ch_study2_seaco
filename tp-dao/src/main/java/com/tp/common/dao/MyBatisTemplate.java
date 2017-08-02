package com.tp.common.dao;
import static com.tp.common.dao.DtoUtil.SPACE_TABLE_NAME;
import static com.tp.common.dao.DtoUtil.caculationColumnList;
import static com.tp.common.dao.DtoUtil.id;
import static com.tp.common.dao.DtoUtil.queryColumn;
import static com.tp.common.dao.DtoUtil.returnInsertColumnsDefine;
import static com.tp.common.dao.DtoUtil.returnInsertColumnsName;
import static com.tp.common.dao.DtoUtil.returnUpdateSet;
import static com.tp.common.dao.DtoUtil.returnUpdateSetFull;
import static com.tp.common.dao.DtoUtil.tableName;
import static com.tp.common.dao.DtoUtil.whereColumn;
import static com.tp.common.dao.DtoUtil.whereColumnNotEmpty;
import static com.tp.common.dao.DtoUtil.whereColumnNotNull;
import static org.apache.ibatis.jdbc.SqlBuilder.BEGIN;
import static org.apache.ibatis.jdbc.SqlBuilder.DELETE_FROM;
import static org.apache.ibatis.jdbc.SqlBuilder.FROM;
import static org.apache.ibatis.jdbc.SqlBuilder.INSERT_INTO;
import static org.apache.ibatis.jdbc.SqlBuilder.ORDER_BY;
import static org.apache.ibatis.jdbc.SqlBuilder.SELECT;
import static org.apache.ibatis.jdbc.SqlBuilder.SET;
import static org.apache.ibatis.jdbc.SqlBuilder.SQL;
import static org.apache.ibatis.jdbc.SqlBuilder.UPDATE;
import static org.apache.ibatis.jdbc.SqlBuilder.VALUES;
import static org.apache.ibatis.jdbc.SqlBuilder.WHERE;

import java.io.Serializable;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.tp.common.vo.DAOConstant;

public class MyBatisTemplate<T  extends Serializable> {
	private final static Log log = LogFactory.getLog(MyBatisTemplate.class);
	
	public String  insert(T obj) {
		BEGIN();
		INSERT_INTO(tableName(obj));
		caculationColumnList(obj);
		VALUES(returnInsertColumnsName(obj), returnInsertColumnsDefine(obj)); 
        return SQL();
    }  
    
	public String updateById(T obj) {  
        String idname = id(obj);  
        BEGIN();  
        UPDATE(tableName(obj));  
        caculationColumnList(obj); 
        SET(returnUpdateSetFull(obj));  
        WHEREID(idname); 
        return SQL();  
    }
    public String updateNotNullById(T obj) {  
        String idname = id(obj);  
          
        BEGIN();  
          
        UPDATE(tableName(obj));  
        caculationColumnList(obj); 
        SET(returnUpdateSet(obj));  
        WHEREID(idname);
        return SQL();  
    }  
      
    public String deleteById(T obj) {  
        String idname = id(obj);  
        BEGIN();
        DELETE_FROM(DtoUtil.DAO_RESULT_MAP.get(obj.getClass().getName()).getTableName());
        WHEREID(idname);
        return SQL();  
    }  

    public String deleteByObject(T obj){
    	caculationColumnList(obj); 
        BEGIN();
        DELETE_FROM(tableName(obj));
        WrapWhere(whereColumnNotNull(obj));
        return SQL();  
    }
    public String deleteByParamNotEmpty(Map<String,Object> param){
    	removeEmpty(param);
    	BEGIN();
    	Serializable obj = (Serializable) param.get(SPACE_TABLE_NAME);
    	String limit ="";
    	if(param.containsKey(DAOConstant.MYBATIS_SPECIAL_STRING.LIMIT.name())){
    		limit=addlimit(param);
    	}
        DELETE_FROM(tableName(obj));
        param.remove(SPACE_TABLE_NAME);
        if(param!=null && param.values()!=null && param.values().size()>0)
        	WrapWhere(whereColumnNotEmpty(param));
        return SQL()+limit; 
    }
    public String deleteByParam(Map<String,Object> param){
    	BEGIN();
    	Serializable obj = (Serializable) param.get(SPACE_TABLE_NAME);
    	String limit ="";
    	if(param.containsKey(DAOConstant.MYBATIS_SPECIAL_STRING.LIMIT.name())){
    		limit=addlimit(param);
    	}
        DELETE_FROM(tableName(obj));
        param.remove(SPACE_TABLE_NAME);
        if(param!=null && param.values()!=null && param.values().size()>0)
        	WrapWhere(whereColumn(param));
        return SQL()+limit; 
    }
    
    public String queryById(T obj){
    	String clazzName = obj.getClass().getName();
    	DtoUtil.MapperWrap mapperWrap = DtoUtil.DAO_RESULT_MAP.get(clazzName);
    	BEGIN();
    	SELECT(queryColumn(mapperWrap.getColumnMap()));
    	FROM(mapperWrap.getTableName()+" "+mapperWrap.getResultTypeSimpleName());
    	WHEREID(mapperWrap.getIdColumn(),mapperWrap.getIdParams());
        return SQL();
    }
    public String queryByObject(T obj){
    	caculationColumnList(obj); 
    	BEGIN();
    	SELECT(queryColumn(obj));
    	FROM(tableName(obj)+" "+obj.getClass().getSimpleName());
    	WrapWhere(whereColumnNotNull(obj));
    	return SQL();
    }
    
    public String queryByParamNotEmpty(Map<String,Object> param){
    	try{
	    	removeEmpty(param);
	    	Serializable obj =(Serializable) param.get(SPACE_TABLE_NAME);
	    	param.remove(SPACE_TABLE_NAME);
	    	Object orderBy = param.get(DAOConstant.MYBATIS_SPECIAL_STRING.ORDER_BY.name());
	    	param.remove(DAOConstant.MYBATIS_SPECIAL_STRING.ORDER_BY.name());
	    	caculationColumnList(obj); 
	    	BEGIN();
	    	SELECT(queryColumn(obj));
	    	FROM(tableName(obj)+" "+obj.getClass().getSimpleName());
	    	if(!param.isEmpty()){
	    		String whereSql = whereColumnNotEmpty(param);
	    		WrapWhere(whereSql);
	    	}
	        if(null!=orderBy)
	        ORDER_BY(orderBy.toString());
	        String sql = SQL()+setLimit(param);
	        log.debug(sql);
	        return sql;
    	}catch(Exception e){
    		log.error("出错了 ！"+param);
    		e.printStackTrace();
    		return null;
    	}
    }

	public String queryByParam(Map<String,Object> param){
		try{
		Serializable obj =(Serializable) param.get(SPACE_TABLE_NAME);
		Object orderBy = param.get(DAOConstant.MYBATIS_SPECIAL_STRING.ORDER_BY.name());
    	param.remove(SPACE_TABLE_NAME);
    	param.remove(DAOConstant.MYBATIS_SPECIAL_STRING.ORDER_BY.name());
    	caculationColumnList(obj); 
    	BEGIN();
    	SELECT(queryColumn(obj));
    	FROM(tableName(obj)+" "+obj.getClass().getSimpleName());
    	if(!param.isEmpty()){
    		String whereSql = whereColumn(param);
    		WrapWhere(whereSql);
    	}
        if(null!=orderBy)
            ORDER_BY(orderBy.toString());
        String sql = SQL()+setLimit(param);
		log.debug(sql);
		return sql;
		}catch(Exception e){
			log.error("出错了！"+param);
			e.printStackTrace();
			return null;
		}
    }

	public String queryByObjectCount(T obj) {
		caculationColumnList(obj);
		BEGIN();
		SELECT(" count(*) total ");
		FROM(tableName(obj) + " " + obj.getClass().getSimpleName());
		WrapWhere(whereColumnNotNull(obj));
		return SQL();
	}

	public String queryByParamNotEmptyCount(Map<String, Object> param) {
		try{
		removeEmpty(param);
		Serializable obj = (Serializable)param.remove(SPACE_TABLE_NAME);
		caculationColumnList(obj);
		BEGIN();
		SELECT(" count(*) total ");
		FROM(tableName(obj) + " " + obj.getClass().getSimpleName());
		Object orderBy = param.remove(DAOConstant.MYBATIS_SPECIAL_STRING.ORDER_BY.name());
		if(!param.isEmpty()){
    		String whereSql = whereColumnNotEmpty(param);
    		WrapWhere(whereSql);
    	}
		param.put(DAOConstant.MYBATIS_SPECIAL_STRING.ORDER_BY.name(), orderBy);
		String sql = SQL();
		log.debug(sql);
		return sql;
		}catch(Exception e){
			log.error("出错了！"+param);
			e.printStackTrace();
			return null;
		}
	}

	public String queryByParamCount(Map<String, Object> param) {
		try{
		Serializable obj = (Serializable) param.get(SPACE_TABLE_NAME);
		param.remove(SPACE_TABLE_NAME);
		caculationColumnList(obj);
		BEGIN();
		SELECT(" count(*) total ");
		FROM(tableName(obj) + " " + obj.getClass().getSimpleName());
		Object orderBy = param.remove(DAOConstant.MYBATIS_SPECIAL_STRING.ORDER_BY.name());
		if(!param.isEmpty()){
    		String whereSql = whereColumn(param);
    		WrapWhere(whereSql);
    	}
		param.put(DAOConstant.MYBATIS_SPECIAL_STRING.ORDER_BY.name(), orderBy);
		String sql = SQL();
		log.debug(sql);
		return sql;
		}catch(Exception e){
			log.error("出错了！"+param);
			e.printStackTrace();
			return null;
		}
	}
	
    public String queryPageByParamNotEmpty(Map<String,Object> param){
    	removeEmpty(param);
    	Serializable obj =(Serializable)param.remove(SPACE_TABLE_NAME); 
    	String limit = addlimit(param);
    	Object orderBy = param.remove(DAOConstant.MYBATIS_SPECIAL_STRING.ORDER_BY.name());
    	caculationColumnList(obj);
    	BEGIN();
    	SELECT(queryColumn(obj));
    	FROM(tableName(obj)+" "+obj.getClass().getSimpleName());
    	if(!param.isEmpty()){
    		String whereSql = whereColumnNotEmpty(param);
    		WrapWhere(whereSql);
    	}
    	if(orderBy!=null){
    		ORDER_BY(orderBy.toString());
    	}
        return  SQL()+limit; 
    }

	public String queryPageByParam(Map<String,Object> param){
		Serializable obj =(Serializable)param.remove(SPACE_TABLE_NAME);
    	String limit = addlimit(param);
    	Object orderBy = param.remove(DAOConstant.MYBATIS_SPECIAL_STRING.ORDER_BY.name());
    	caculationColumnList(obj); 
    	BEGIN();
    	SELECT(queryColumn(obj));
    	FROM(tableName(obj)+" "+obj.getClass().getSimpleName());
    	if(!param.isEmpty()){
    		String whereSql = whereColumn(param);
    		WrapWhere(whereSql);
    	}
    	if(orderBy!=null){
    		ORDER_BY(orderBy.toString());
    	}
        return SQL()+limit; 
    }
	
	@SuppressWarnings("unchecked")
	public String queryPageByParams(Map<String,Object> param){
		Serializable obj =(Serializable)param.remove(SPACE_TABLE_NAME);
    	Object orderBy = param.remove(DAOConstant.MYBATIS_SPECIAL_STRING.ORDER_BY.name());
    	caculationColumnList(obj); 
    	BEGIN();
    	SELECT(queryColumn(obj));
    	FROM(tableName(obj)+" "+obj.getClass().getSimpleName());
    	if(!((Map<?,?>)param.get("params")).isEmpty())    		
    		WrapWhere(whereColumn((Map<String,Object>)param.get("params")));
    	if(orderBy!=null){
    		ORDER_BY(orderBy.toString());
    	}
        return SQL(); 
    }
	
	public void WHEREID(final String idname){
    	WHERE(idname.replaceAll("([A-Z])", "_$1").toLowerCase() + "=#{" + idname + "}");
    }
	
    public void WHEREID(final String idColumn,final String idname){
    	WHERE(idColumn+ "=#{" + idname + "}");
    }
    
    public String addlimit(Map<String,Object> param){
    	String subsql =" "+ DAOConstant.MYBATIS_SPECIAL_STRING.LIMIT.name()+" ";
    	Object obj =param.remove(DAOConstant.MYBATIS_SPECIAL_STRING.LIMIT.name());
    	if(null==obj){
    		subsql = subsql+"0,10";
    	}else{
    		subsql = subsql+obj;
    	}
    	
    	return subsql;
    }
    
    public String setLimit(Map<String,Object> param){
    	Object obj =param.remove(DAOConstant.MYBATIS_SPECIAL_STRING.LIMIT.name());
    	if(null!=obj){
    		return " limit "+obj;
    	}
    	return "";
    }
    
    private void removeEmpty(Map<String,Object> params){
    	Iterator<String> iterator = params.keySet().iterator();
    	while(iterator.hasNext()){
    		String key = iterator.next();
    		if(params.get(key)==null){
    			params.remove(key);
    			iterator = params.keySet().iterator();
    		}
    	}
    }
    
    private void WrapWhere(String sql){
    	if(StringUtils.isNotBlank(sql)){
    		WHERE(sql);
    	}
    }
}
