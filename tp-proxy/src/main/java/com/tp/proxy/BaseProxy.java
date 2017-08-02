package com.tp.proxy;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tp.common.util.ExceptionUtils;
import com.tp.common.vo.PageInfo;
import com.tp.dto.common.FailInfo;
import com.tp.dto.common.ResultInfo;
import com.tp.proxy.mmp.callBack.AbstractProxy;
import com.tp.service.IBaseProxy;
import com.tp.service.IBaseService;
/**
 * 对服务层返回的值进行封装，各个模块的业务调用在代理类实现，严禁在service层写除查询外跨模块的操作，严禁调用其它模块的DAO
 * 代理层只有一个返回类型ResultInfo
 * @author szy
 *
 * @param <T>
 * @param <E>
 */
public abstract class BaseProxy<T> extends AbstractProxy implements IBaseProxy<T> {
	protected final Logger logger = LoggerFactory.getLogger(getClass());
	
	public abstract IBaseService<T> getService();

	@Override
	public ResultInfo<T> insert(T obj) {
		try{
			return new ResultInfo<>(getService().insert(obj));
		}catch(Throwable exception){
			FailInfo failInfo = ExceptionUtils.print(new FailInfo(exception), logger,obj);
			return new ResultInfo<>(failInfo);
		}
	}

	@Override
	public ResultInfo<Integer> updateNotNullById(T obj) {
		try{
			return new ResultInfo<>(getService().updateNotNullById(obj));
		}catch(Throwable exception){
			FailInfo failInfo = ExceptionUtils.print(new FailInfo(exception), logger,obj);
			return new ResultInfo<>(failInfo);
		}
	}

	@Override
	public ResultInfo<Integer> updateById(T obj) {
		try{
			return new ResultInfo<>(getService().updateById(obj));
		}catch(Throwable exception){
			FailInfo failInfo = ExceptionUtils.print(new FailInfo(exception), logger,obj);
			return new ResultInfo<>(failInfo);
		}
	}

	@Override
	public ResultInfo<Integer> deleteById(Number id) {
		try{
			return new ResultInfo<>(getService().deleteById(id));
		}catch(Throwable exception){
			FailInfo failInfo = ExceptionUtils.print(new FailInfo(exception), logger,id);
			return new ResultInfo<>(failInfo);
		}
	}

	@Override
	public ResultInfo<Integer> deleteByObject(T obj) {
		try{
			return new ResultInfo<>(getService().deleteByObject(obj));
		}catch(Throwable exception){
			FailInfo failInfo = ExceptionUtils.print(new FailInfo(exception), logger,obj);
			return new ResultInfo<>(failInfo);
		}
	}

	@Override
	public ResultInfo<Integer> deleteByParamNotEmpty(Map<String, Object> param) {
		try{
			return new ResultInfo<>(getService().deleteByParamNotEmpty(param));
		}catch(Throwable exception){
			FailInfo failInfo = ExceptionUtils.print(new FailInfo(exception), logger,param);
			return new ResultInfo<>(failInfo);
		}
	}

	@Override
	public ResultInfo<Integer> deleteByParam(Map<String, Object> param) {
		try{
			return new ResultInfo<>(getService().deleteByParam(param));
		}catch(Throwable exception){
			FailInfo failInfo = ExceptionUtils.print(new FailInfo(exception), logger,param);
			return new ResultInfo<>(failInfo);
		}
	}

	@Override
	public ResultInfo<T> queryById(Number id) {
		try{
			return new ResultInfo<>(getService().queryById(id));
		}catch(Throwable exception){
			FailInfo failInfo = ExceptionUtils.print(new FailInfo(exception), logger,id);
			return new ResultInfo<>(failInfo);
		}
	}

	@Override
	public ResultInfo<List<T>> queryByObject(T obj) {
		try{
			return new ResultInfo<>(getService().queryByObject(obj));
		}catch(Throwable exception){
			FailInfo failInfo = ExceptionUtils.print(new FailInfo(exception), logger,obj);
			return new ResultInfo<>(failInfo);
		}
	}

	@Override
	public ResultInfo<T> queryUniqueByObject(T obj) {
		try{
			return new ResultInfo<>(getService().queryUniqueByObject(obj));
		}catch(Throwable exception){
			FailInfo failInfo = ExceptionUtils.print(new FailInfo(exception), logger,obj);
			return new ResultInfo<>(failInfo);
		}
	}

	@Override
	public ResultInfo<T> queryUniqueByParams(Map<String, Object> params) {
		try{
			return new ResultInfo<>(getService().queryUniqueByParams(params));
		}catch(Throwable exception){
			FailInfo failInfo = ExceptionUtils.print(new FailInfo(exception), logger,params);
			return new ResultInfo<>(failInfo);
		}
	}

	@Override
	public ResultInfo<List<T>> queryByParamNotEmpty(Map<String, Object> params) {
		try{
			return new ResultInfo<>(getService().queryByParamNotEmpty(params));
		}catch(Throwable exception){
			FailInfo failInfo = ExceptionUtils.print(new FailInfo(exception), logger,params);
			return new ResultInfo<>(failInfo);
		}
	}

	@Override
	public ResultInfo<List<T>> queryByParam(Map<String, Object> params) {
		try{
			return new ResultInfo<>(getService().queryByParam(params));
		}catch(Throwable exception){
			FailInfo failInfo = ExceptionUtils.print(new FailInfo(exception), logger,params);
			return new ResultInfo<>(failInfo);
		}
	}

	@Override
	public ResultInfo<Integer> queryByObjectCount(T obj) {
		try{
			return new ResultInfo<>(getService().queryByObjectCount(obj));
		}catch(Throwable exception){
			FailInfo failInfo = ExceptionUtils.print(new FailInfo(exception), logger,obj);
			return new ResultInfo<>(failInfo);
		}
	}

	@Override
	public ResultInfo<Integer> queryByParamNotEmptyCount(Map<String, Object> params) {
		try{
			return new ResultInfo<>(getService().queryByParamNotEmptyCount(params));
		}catch(Throwable exception){
			FailInfo failInfo = ExceptionUtils.print(new FailInfo(exception), logger,params);
			return new ResultInfo<>(failInfo);
		}
	}

	@Override
	public ResultInfo<Integer> queryByParamCount(Map<String, Object> params) {
		try{
			return new ResultInfo<>(getService().queryByParamCount(params));
		}catch(Throwable exception){
			FailInfo failInfo = ExceptionUtils.print(new FailInfo(exception), logger,params);
			return new ResultInfo<>(failInfo);
		}
	}

	@Override
	public ResultInfo<PageInfo<T>> queryPageByObject(T obj, PageInfo<T> info) {
		try{
			return new ResultInfo<>(getService().queryPageByObject(obj,info));
		}catch(Throwable exception){
			FailInfo failInfo = ExceptionUtils.print(new FailInfo(exception), logger,obj,info);
			return new ResultInfo<>(failInfo);
		}
	}

	@Override
	public ResultInfo<PageInfo<T>> queryPageByParamNotEmpty(Map<String, Object> params,
			PageInfo<T> info) {
		try{
			return new ResultInfo<>(getService().queryPageByParamNotEmpty(params,info));
		}catch(Throwable exception){
			FailInfo failInfo = ExceptionUtils.print(new FailInfo(exception), logger,params,info);
			return new ResultInfo<>(failInfo);
		}
	}

	@Override
	public ResultInfo<PageInfo<T>> queryPageByParam(Map<String, Object> params,
			PageInfo<T> info) {
		try{
			return new ResultInfo<>(getService().queryPageByParam(params,info));
		}catch(Throwable exception){
			FailInfo failInfo = ExceptionUtils.print(new FailInfo(exception), logger,params,info);
			return new ResultInfo<>(failInfo);
		}
	}

	@Override
	public ResultInfo<PageInfo<T>> queryPageByParams(Map<String, Object> hashMap,
			PageInfo<T> pageInfo) {
		try{
			return new ResultInfo<>(getService().queryPageByParams(hashMap,pageInfo));
		}catch(Throwable exception){
			FailInfo failInfo = ExceptionUtils.print(new FailInfo(exception), logger,hashMap,pageInfo);
			return new ResultInfo<>(failInfo);
		}
	}
	
}
