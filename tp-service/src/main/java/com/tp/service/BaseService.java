package com.tp.service;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tp.common.annotation.Virtual;
import com.tp.common.dao.BaseDao;
import com.tp.common.vo.DAOConstant;
import com.tp.common.vo.PageInfo;
import com.tp.datasource.ContextHolder;
import com.tp.service.IBaseService;

public abstract class BaseService<T> implements IBaseService<T> {
	
	protected final Logger logger = LoggerFactory.getLogger(getClass());
	
	public abstract BaseDao<T> getDao();
	
	public T insert(T obj) {
		getDao().insert(obj);
		return obj;
	}

	public int updateNotNullById(T obj) {
		return getDao().updateNotNullById(obj);
	}

	public int updateById(T obj) {
		return getDao().updateById(obj);
	}

	public int deleteById(Number id) {
		return getDao().deleteById(id);
	}
	

	public int deleteByObject(T obj) {
		return getDao().deleteByObject(obj);
	}

	public int deleteByParamNotEmpty(Map<String, Object> param) {
		return getDao().deleteByParamNotEmpty(param);
	}

	public int deleteByParam(Map<String, Object> param) {
		return getDao().deleteByParam(param);
	}

	public T queryById(Number id) {
		return getDao().queryById(id);
	}
	

	public List<T> queryByObject(T obj) {
		return (List<T>) getDao().queryByObject(obj);
	}

	public List<T> queryByParamNotEmpty(Map<String, Object> params) {
		return getDao().queryByParamNotEmpty(params);
	}

	public List<T> queryByParam(Map<String, Object> params) {
		return getDao().queryByParam(params);
	}

	public Integer queryByObjectCount(T obj) {
		return getDao().queryByObjectCount(obj);
	}

	public Integer queryByParamNotEmptyCount(Map<String, Object> params) {
		return getDao().queryByParamNotEmptyCount(params);
	}

	public Integer queryByParamCount(Map<String, Object> params) {
		return getDao().queryByParamCount(params);
	}

	public PageInfo<T> queryPageByObject(T obj, PageInfo<T> info) {
		Map<String,Object> params = getValuesByObject(obj);
		return queryPageByParamNotEmpty(params,info);
	}

	public PageInfo<T> queryPageByParamNotEmpty(Map<String, Object> params,
			PageInfo<T> info) {
		info.setRecords(queryByParamNotEmptyCount(params));
		setLimit(params,info);
		info.setRows(getDao().queryPageByParamNotEmpty(params));
		return info;
	}

	public PageInfo<T> queryPageByParam(Map<String, Object> params, PageInfo<T> info) {
		info.setRecords(queryByParamCount(params));
		setLimit(params,info);
		info.setRows(getDao().queryPageByParam(params));
		return info;
	}
	
	public PageInfo<T> queryPageByParams(Map<String, Object> params, PageInfo<T> info) {
		info.setRows(getDao().queryPageByParams(params,info,ContextHolder.DATA_SOURCE_KEY.SLAVE_SALE_ORDER_DATA_SOURCE));
		return info;
	}
	
	public void setLimit(final Map<String,Object> params,final PageInfo<T> info){
		Integer begin = (info.getPage()-1)*info.getSize();
		if(begin==null || (begin!=null &&begin<0)){
			begin=0;
		}
		params.put(DAOConstant.MYBATIS_SPECIAL_STRING.LIMIT.name(), begin+","+info.getSize());
	}
	public Map<String,Object> getValuesByObject(T obj){
		Field[] fields = obj.getClass().getDeclaredFields();
		Map<String,Object> map = new HashMap<String,Object>();
		for (Field f : fields) {
			Object Virtual = f.getAnnotation(Virtual.class);
			if (Modifier.isStatic(f.getModifiers())
					|| Modifier.isFinal(f.getModifiers()) || Virtual!=null)
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
			if (value != null && ( !(value instanceof String) || (value instanceof String && StringUtils.isNotBlank((String)value))))
				map.put(f.getName(), value);
		}
		return map;
	}

	public T queryUniqueByObject(T obj) {
		List<T> list = queryByObject(obj);
		if(list == null || list.isEmpty())
			return null;
		return list.get(0);
	}
	public T queryUniqueByParams(Map<String,Object> params) {
		List<T> list = queryByParam(params);
		if(list == null || list.isEmpty())
			return null;
		return list.get(0);
	}
	
	protected Map<String,Object> getValuesByParamObject(Object obj){
		Field[] fields = obj.getClass().getDeclaredFields();
		Map<String,Object> map = new HashMap<String,Object>();
		for (Field f : fields) {
			Object Virtual = f.getAnnotation(Virtual.class);
			if (Modifier.isStatic(f.getModifiers())
					|| Modifier.isFinal(f.getModifiers()) || Virtual!=null)
				continue;
			Object value = null;
			try {
				f.setAccessible(true);
				value = f.get(obj);
				if(value!=null && value instanceof String && StringUtils.isBlank(value.toString())){
					value = null;
				}
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
			if (value != null)
				map.put(f.getName(), value);
		}
		return map;
	}
}
