package com.tp.service.stg;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.dao.BaseDao;
import com.tp.common.vo.StorageConstant;
import com.tp.dao.stg.WarehouseDao;
import com.tp.dto.common.FailInfo;
import com.tp.dto.common.ResultInfo;
import com.tp.dto.stg.WarehouseAreaDto;
import com.tp.model.stg.Warehouse;
import com.tp.redis.util.JedisCacheUtil;
import com.tp.service.BaseService;
import com.tp.service.stg.IWarehouseService;

@Service
public class WarehouseService extends BaseService<Warehouse> implements IWarehouseService {

	@Autowired
	private WarehouseDao warehouseDao;

	@Override
	public BaseDao<Warehouse> getDao() {
		return warehouseDao;
	}
	@Autowired
	private JedisCacheUtil jedisCacheUtil;

	/** 全国的id **/
	private static final String CHINA_ID = "196";

	@Override
	public Warehouse insert(Warehouse warehouseObj) {
		warehouseDao.insert(warehouseObj);
		warehouseObj.setId(warehouseObj.getId());
		jedisCacheUtil.setCache(StorageConstant.STORAGE_WAREHOUSE_PRE_ + warehouseObj.getId(), warehouseObj,
				Integer.MAX_VALUE);
		jedisCacheUtil.setCache(StorageConstant.STORAGE_WAREHOUSE_PRE_ + warehouseObj.getCode(), warehouseObj,
				Integer.MAX_VALUE);
		return warehouseObj;
	}

	public int update(Warehouse warehouseObj, boolean isAllField) {
		Integer resultID = null;
		Warehouse warehouse = null;
		resultID = (Integer) warehouseDao.updateNotNullById(warehouseObj);
		warehouse = warehouseDao.queryById(warehouseObj.getId());
		jedisCacheUtil.setCache(StorageConstant.STORAGE_WAREHOUSE_PRE_ + warehouseObj.getId(), warehouse,
				Integer.MAX_VALUE);
		jedisCacheUtil.setCache(StorageConstant.STORAGE_WAREHOUSE_PRE_ + warehouseObj.getCode(), warehouse,
				Integer.MAX_VALUE);
		return resultID;
	}

	@Override
	public int deleteById(Long id) {
		Warehouse warehouseObj = queryById(id);
		Integer resultID = (Integer) warehouseDao.deleteById(id);
		jedisCacheUtil.deleteCacheKey(StorageConstant.STORAGE_WAREHOUSE_PRE_ + id);
		if (null != warehouseObj) {
			jedisCacheUtil.deleteCacheKey(StorageConstant.STORAGE_WAREHOUSE_PRE_ + warehouseObj.getCode());
		}
		return resultID;
	}

	@Override
	public Warehouse queryById(Number id) {
		if (null == id) {
			return null;
		}
		Warehouse cache = (Warehouse) jedisCacheUtil.getCache(StorageConstant.STORAGE_WAREHOUSE_PRE_ + id);
		if (null != cache) {
			return cache;
		} else {
			Warehouse warehouse = warehouseDao.queryById(id);
			jedisCacheUtil.setCache(StorageConstant.STORAGE_WAREHOUSE_PRE_ + warehouse.getId(), warehouse,
					Integer.MAX_VALUE);
			jedisCacheUtil.setCache(StorageConstant.STORAGE_WAREHOUSE_PRE_ + warehouse.getCode(), warehouse,
					Integer.MAX_VALUE);
			return warehouse;
		}
	}

	@Override
	public Map<Long, ResultInfo<String>> checkWarehouseArea(WarehouseAreaDto warehouseAreaDto) {
		if (null == warehouseAreaDto) {
			return null;
		} else if (CollectionUtils.isEmpty(warehouseAreaDto.getWarehouseIds())) {
			return null;
		} else if (null == warehouseAreaDto.getProvinceId() || null == warehouseAreaDto.getCityId()
				|| null == warehouseAreaDto.getCityId()) {
			return null;
		}
		Map<Long, ResultInfo<String>> result = new HashMap<Long, ResultInfo<String>>();
		List<Long> warehourseIds = warehouseAreaDto.getWarehouseIds();
		for (Long warehourseId : warehourseIds) {
			ResultInfo<String> resultMsg = checkOneWarehouseArea(warehouseAreaDto, warehourseId);
			result.put(warehourseId, resultMsg);
		}
		return result;
	}

	/**
	 * 
	 * <pre>
	 * 校验单个仓库配送区域
	 * </pre>
	 *
	 * @param warehouseAreaDto
	 * @param warehouseId
	 * @return ResultMessage
	 * @throws StorageServiceException
	 */
	private ResultInfo<String> checkOneWarehouseArea(WarehouseAreaDto warehouseAreaDto, Long warehouseId) {
		ResultInfo<String> msg = new ResultInfo<String>("");
		// 查询区Id
		Long regionId = warehouseAreaDto.getRegionId();
		Long provinceId = warehouseAreaDto.getProvinceId();
		Long cityId = warehouseAreaDto.getCityId();
		Long countyId = warehouseAreaDto.getCountyId();
		Long streetId = warehouseAreaDto.getStreetId();

		Warehouse warehouse = warehouseDao.queryById(warehouseId);
		if (null == warehouse) {
			msg.setMsg(new FailInfo("仓库不存在"));
			return msg;
		}

		String deliverAddr = warehouse.getDeliverAddr();
		String[] ids = deliverAddr.split(",");
		for (String id : ids) {
			if (CHINA_ID.equals(id)) {
				return msg;
			} else if (id.equals(regionId.toString())) {
				return msg;
			} else if (id.equals(provinceId.toString())) {
				return msg;
			} else if (id.equals(cityId.toString())) {
				return msg;
			} else if (id.equals(countyId.toString())) {
				return msg;
			}
			if (null != streetId) {
				if (id.equals(streetId.toString())) {
					return msg;
				}
			}
		}
		msg.setMsg(new FailInfo("校验不成功,仓库不能配送此地址"));
		return msg;
	}

	@Override
	public Warehouse selectByCode(String code) {
		if (StringUtils.isBlank(code)) {
			return null;
		}
		Warehouse cache = (Warehouse) jedisCacheUtil.getCache(StorageConstant.STORAGE_WAREHOUSE_PRE_ + code);
		if (null == cache) {
			Map<String, Object> params = new HashMap<>();
			params.put("code", code);
			List<Warehouse> warehouseObjs = warehouseDao.queryByParamNotEmpty(params);
			cache = warehouseObjs.get(0);
			if (null != cache) {
				jedisCacheUtil.setCache(StorageConstant.STORAGE_WAREHOUSE_PRE_ + code, cache, Integer.MAX_VALUE);
			}
		}
		return cache;
	}
	
	@Override
	public List<Warehouse> queryByIds(List<Long> ids){
		List<Warehouse> warehouses = new ArrayList<>();
		for (Long id : ids) {
			if(null != id) 
				warehouses.add(queryById(id));
		}
		return warehouses;
	}

	@Override
	public List<Warehouse> queryByCodes(List<String> codes) {
		if(org.springframework.util.CollectionUtils.isEmpty(codes)){
			return Collections.emptyList();
		}
		List<Warehouse>  warehouseList = new ArrayList<>();
		for(String code :codes){
			if(StringUtils.isBlank(code)){
				continue;
			}
			Warehouse warehouse = new Warehouse();
			warehouse.setCode(code);
			warehouseList.addAll(this.queryByObject(warehouse));
		}
		return warehouseList;
	}
}
