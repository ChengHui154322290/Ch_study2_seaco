package com.tp.proxy.dss;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.util.ExceptionUtils;
import com.tp.common.vo.Constant;
import com.tp.common.vo.FastConstant;
import com.tp.common.vo.PageInfo;
import com.tp.common.vo.Constant.SPLIT_SIGN;
import com.tp.common.vo.DAOConstant.MYBATIS_SPECIAL_STRING;
import com.tp.dto.common.FailInfo;
import com.tp.dto.common.ResultInfo;
import com.tp.model.dss.FastUserInfo;
import com.tp.model.stg.Warehouse;
import com.tp.proxy.BaseProxy;
import com.tp.redis.util.JedisCacheUtil;
import com.tp.service.IBaseService;
import com.tp.service.dss.IFastUserInfoService;
import com.tp.service.stg.IWarehouseService;
import com.tp.util.StringUtil;
/**
 * 速购人员信息表代理层
 * @author szy
 *
 */
@Service
public class FastUserInfoProxy extends BaseProxy<FastUserInfo>{

	public static final String FAST_USER_INFO_KEY = "seller:user:info:";
	@Autowired
	private IFastUserInfoService fastUserInfoService;
	@Autowired
	private IWarehouseService warehouseService;

	@Autowired
	private JedisCacheUtil JedisCacheUtil;
	
	
	@Override
	public IBaseService<FastUserInfo> getService() {
		return fastUserInfoService;
	}
	
	@Override
	public ResultInfo<PageInfo<FastUserInfo>> queryPageByParamNotEmpty(Map<String, Object> params,
			PageInfo<FastUserInfo> info) {
		return initWarehouseName(super.queryPageByParamNotEmpty(params, info));
	}
	
	@Override
	public ResultInfo<List<FastUserInfo>> queryByParamNotEmpty(Map<String, Object> params){
		ResultInfo<List<FastUserInfo>> result = super.queryByParamNotEmpty(params);
		if (result.isSuccess()) return new ResultInfo<>(initWarehouseName(result.getData()));
		return result;
	}
	
	private ResultInfo<PageInfo<FastUserInfo>> initWarehouseName(ResultInfo<PageInfo<FastUserInfo>> result){
		if(result.success && CollectionUtils.isNotEmpty(result.data.getRows())){
			result.data.setRows(initWarehouseName(result.data.getRows()));
		}
		return result;
	}
	
	private List<FastUserInfo> initWarehouseName(List<FastUserInfo> fastUserInfos){
		if (CollectionUtils.isEmpty(fastUserInfos)) return fastUserInfos;
		List<Long> warehouseIdList =new ArrayList<Long>();
		for(FastUserInfo fastUserInfo:fastUserInfos){
			warehouseIdList.add(fastUserInfo.getWarehouseId());
		}
		Map<String,Object> params = new HashMap<String,Object>();
		params.put(MYBATIS_SPECIAL_STRING.COLUMNS.name(), " id in ("+StringUtil.join(warehouseIdList, SPLIT_SIGN.COMMA)+")");
		List<Warehouse> warehouseList = warehouseService.queryByParam(params);
		for(FastUserInfo fastUserInfo:fastUserInfos){
			for(Warehouse warehouse:warehouseList){
				if(warehouse.getId().equals(fastUserInfo.getWarehouseId())){
					fastUserInfo.setWarehouseName(warehouse.getName());
				}
			}
		}
		return fastUserInfos;
	}
	
	public ResultInfo<List<FastUserInfo>> queryFastUserInfoListByMobile(String mobile){
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("mobile", mobile);
		params.put("enabled", Constant.ENABLED.YES);
		params.put("shopType", FastConstant.SHOP_TYPE.FAST_BUY.code);
		ResultInfo<List<FastUserInfo>> result = queryByParam(params);
		if(result.success && CollectionUtils.isNotEmpty(result.getData())){
			for(FastUserInfo fastUserInfo:result.getData()){
				setFastUserInfoCacheByMobile(fastUserInfo);
			}
		}
		return result;
	}
	
	public void setFastUserInfoCacheByMobile(FastUserInfo fastUserInfo){
		JedisCacheUtil.setCache(FAST_USER_INFO_KEY+SPLIT_SIGN.COLON+fastUserInfo.getShopType()+SPLIT_SIGN.COLON+fastUserInfo.getMobile(), fastUserInfo.getWarehouseId());
	}
	
	public Long queryFastUserWarehouseIdbyMobile(String mobile,Integer shopType){
		Long warehouseId =(Long)JedisCacheUtil.getCache(FAST_USER_INFO_KEY+SPLIT_SIGN.COLON+shopType+SPLIT_SIGN.COLON+mobile);
		if(null==warehouseId){
			queryFastUserInfoListByMobile(mobile);
			warehouseId =(Long)JedisCacheUtil.getCache(FAST_USER_INFO_KEY+SPLIT_SIGN.COLON+shopType+SPLIT_SIGN.COLON+mobile);
		}
		return warehouseId;
	}
	
	public ResultInfo<Integer> deleteById(Number id) {
		try{
			FastUserInfo fastUserInfo = fastUserInfoService.queryById(id);
			if(fastUserInfo!=null){
				JedisCacheUtil.deleteCacheKey(FAST_USER_INFO_KEY+SPLIT_SIGN.COLON+fastUserInfo.getShopType()+SPLIT_SIGN.COLON+fastUserInfo.getMobile());
			}
			Integer result = fastUserInfoService.deleteById(id);
			return new ResultInfo<>(result);
		}catch(Throwable exception){
			FailInfo failInfo = ExceptionUtils.print(new FailInfo(exception), logger,id);
			return new ResultInfo<>(failInfo);
		}
	}
}
