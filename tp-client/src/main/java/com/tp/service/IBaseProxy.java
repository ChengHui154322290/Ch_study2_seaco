package com.tp.service;

import java.util.Map;

import com.tp.common.vo.PageInfo;
import com.tp.dto.common.ResultInfo;

public interface IBaseProxy<T>{
	/**
	 * 插入数据
	 * @param obj
	 */
	 ResultInfo<?> insert(T obj); 
	 /**
	  * 根据主键更新数据中不为空的属
	  * @param obj
	  * @return
	  */
	 ResultInfo<?> updateNotNullById(T obj);
	 
	 /**
	  * 根据主键更新数据的全部属
	  * @param obj
	  * @return
	  */
	 ResultInfo<?> updateById(T obj);
	 
	 /**
	  * 根据主键删除数据
	  * @param id
	  * @return
	  */
	 ResultInfo<?> deleteById(Number id);
	 
	 /**
	  * 根据对象中不为空的条件删除数据
	  * @param id
	  * @return
	  */
	 ResultInfo<?> deleteByObject(T obj);
	 
	 /**
	  * 根据参数中不为空的条件删除数据,key对应dto中的属性
	  * @param id
	  * @return
	  */
	 ResultInfo<?> deleteByParamNotEmpty(Map<String,Object> param);
	 
	 /**
	  * 根据参数中条件删除数据,key对应dto中的属性
	  * @param id
	  * @return
	  */
	 ResultInfo<?> deleteByParam(Map<String,Object> param);
	 
	 /**
	  * 根据主键查询数据
	  * @param id
	  * @return
	  */
	 ResultInfo<?> queryById(Number id);
	 
	 
	 /**
	  * 根据对象中不为空的属性查询列表
	  * @param id
	  * @return
	  */
	 ResultInfo<?> queryByObject(T obj);
	 
	 /**
	  * 返回单个对象
	  * @param obj
	  * @return
	  */
	 ResultInfo<?> queryUniqueByObject(T obj);
	 /**
	  * 返回单个对象
	  * @param obj
	  * @return
	  */
	 ResultInfo<?> queryUniqueByParams(Map<String,Object> params);
	 /**
	  * 根据参数中不为空的属性查询列表，key对应dto中的属性
	  * @param id
	  * @return
	  */
	 ResultInfo<?> queryByParamNotEmpty(Map<String,Object> params);
	 /**
	  * 根据参数查询列表，key对应dto中的属性
	  * @param id
	  * @return
	  */
	 ResultInfo<?> queryByParam(Map<String,Object> params);
	 
	 /**
	  * 根据对象中不为空的属性查询总数
	  * @param id
	  * @return
	  */
	 ResultInfo<?> queryByObjectCount(T obj);
	 /**
	  * 根据参数中不为空的属性查询总数，key对应dto中的属性
	  * @param id
	  * @return
	  */
	 ResultInfo<?> queryByParamNotEmptyCount(Map<String,Object> params);
	 /**
	  * 根据参数查询总数，key对应dto中的属性
	  * @param id
	  * @return
	  */
	 ResultInfo<?> queryByParamCount(Map<String,Object> params);
	 
	 /**
	  * 根据对象中不为空的属性查询列表
	  * @param id
	  * @return
	  */
	 ResultInfo<?> queryPageByObject(T obj,PageInfo<T> info);
	 /**
	  * 根据参数中不为空的属性查询列表，key对应dto中的属性
	  * @param id
	  * @return
	  */
	 ResultInfo<?> queryPageByParamNotEmpty(Map<String,Object> params,PageInfo<T> info);
	 /**
	  * 根据参数查询列表，key对应dto中的属性
	  * @param id
	  * @return
	  */
	 ResultInfo<?> queryPageByParam(Map<String,Object> params,PageInfo<T> info);
	 /**
	  * 根据参数查询列表，key对应dto中的属性
	  * @param id
	  * @return
	  */
	 ResultInfo<?> queryPageByParams(Map<String, Object> hashMap,
				PageInfo<T> pageInfo);
}
