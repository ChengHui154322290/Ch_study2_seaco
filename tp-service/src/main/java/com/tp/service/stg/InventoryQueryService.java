/**
 * 
 */
package com.tp.service.stg;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.vo.Constant;
import com.tp.common.vo.PageInfo;
import com.tp.common.vo.StorageConstant;
import com.tp.common.vo.DAOConstant.MYBATIS_SPECIAL_STRING;
import com.tp.common.vo.StorageConstant.App;
import com.tp.dto.common.FailInfo;
import com.tp.dto.common.ResultInfo;
import com.tp.dto.stg.InventoryDto;
import com.tp.dto.stg.query.InventoryDtoQuery;
import com.tp.dto.stg.query.InventoryQuery;
import com.tp.dto.stg.query.SkuInventoryQuery;
import com.tp.model.stg.Inventory;
import com.tp.model.stg.InventoryDistribute;
import com.tp.model.stg.InventoryOccupy;
import com.tp.model.stg.Warehouse;
import com.tp.service.stg.IInventoryCacheService;
import com.tp.service.stg.IInventoryDistributeService;
import com.tp.service.stg.IInventoryOccupyService;
import com.tp.service.stg.IInventoryQueryService;
import com.tp.service.stg.IInventoryService;
import com.tp.service.stg.IWarehouseService;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.vo.Constant.DEFAULTED;
import com.tp.common.vo.PageInfo;
import com.tp.common.vo.StorageConstant;
import com.tp.common.vo.StorageConstant.App;
import com.tp.common.vo.StorageConstant.DistributeBackedStatus;
import com.tp.dto.stg.InventoryDto;
import com.tp.dto.stg.query.InventoryQuery;
import com.tp.dto.stg.query.SkuInventoryQuery;
import com.tp.exception.InventoryServiceException;
import com.tp.model.stg.Inventory;
import com.tp.model.stg.InventoryDistribute;
import com.tp.model.stg.InventoryOccupy;
import com.tp.model.stg.Warehouse;
import com.tp.util.BeanUtil;
import com.tp.util.StringUtil;
/**
 * @author szy
 *
 */
@Service
public class InventoryQueryService implements IInventoryQueryService {

	

	private Logger logger = LoggerFactory.getLogger(InventoryQueryService.class);

	@Autowired
	private IInventoryService inventoryService;

	@Autowired
	private IInventoryDistributeService inventoryDistributeService;

	@Autowired
	private IWarehouseService warehouseService;

	@Autowired
	private IInventoryOccupyService inventoryOccupyService;

	@Override
	public List<InventoryDto> queryAvailableInventory(String sku, Long warehouseId){
		// TODO ""
		InventoryQuery query = new InventoryQuery();
		query.setSku(sku);
		query.setWarehouseId(warehouseId);
		return queryAvailableInventory(query);
	}
	@Override
	public List<InventoryDto> queryAvailableInventory(InventoryQuery query){
		// TODO ""
		return queryInventoryInfoByParam(BeanUtil.beanMap(query));
	}
	@Override
	public List<InventoryDto> queryAvailableInventory(List<InventoryQuery> queries){
		// TODO ""
		if(CollectionUtils.isEmpty(queries)) return new ArrayList<>();
		List<Inventory> inventoryList = new ArrayList<>();
		for(InventoryQuery query : queries){
			inventoryList.add(convertInventoryQueryToInventory(query));
		}
		return queryInventoryInfo(inventoryList);
	}
	@Override
	public PageInfo<InventoryDto> queryPageAvailableInventory(InventoryQuery inventoryQuery, PageInfo<InventoryDto> pageInfo){
		// TODO ""
		Map<String,Object> params = BeanUtil.beanMap(inventoryQuery);
		PageInfo<Inventory> inventoryPage = inventoryService.queryPageInventoryInfoByParam(params, new PageInfo<>(pageInfo.getPage(), pageInfo.getSize()));
		if (CollectionUtils.isEmpty(inventoryPage.getRows())) return pageInfo;
		List<InventoryDto> inventoryDtos = new ArrayList<InventoryDto>();
		for (Inventory inventory : inventoryPage.getRows()) {
			inventoryDtos.add(initInventoryDto(inventory));
		}
		inventoryDtos = setWarehouseInfo(inventoryDtos); //库存信息
		pageInfo.setRecords(inventoryPage.getRecords());
		pageInfo.setRows(inventoryDtos);
		pageInfo.setTotal(inventoryPage.getTotal());
		return pageInfo;
	}
	/** 
	 * 批量查询可售库存 
	 * key: bizId_SKU
	 * value : salableInventory
	 * */
	@Override
	public Map<String, Integer> querySalableInventory(List<SkuInventoryQuery> skuInventoryQueries){
		// TODO ""
		if(CollectionUtils.isEmpty(skuInventoryQueries)) return new HashMap<>();
		if(false == validateCheckInventoryParameter(skuInventoryQueries)){
			throw new InventoryServiceException("库存不足");
		}
		Map<Integer, List<SkuInventoryQuery>> splitMap = splitSkuInventoryQueryByPreOccupySign(skuInventoryQueries);
		Map<String, Integer> preOccupyInventoryMap = queryPreOccupySalableInventory(splitMap.get(DEFAULTED.YES));
		Map<String, Integer> unPreOccupyInventoryMap = queryUnPreOccupySalableInventory(splitMap.get(DEFAULTED.NO));
		Map<String, Integer> salableInventoryMap = new HashMap<>();
		skuInventoryQueries.forEach(new Consumer<SkuInventoryQuery>() {
			@Override
			public void accept(SkuInventoryQuery t) {	
				String preKey = t.getBizId() + StorageConstant.straight + t.getSku();
				if(t.isBizPreOccupy()){		
					Integer inventory = preOccupyInventoryMap.get(DEFAULTED.YES + StorageConstant.straight + preKey);
					inventory = (inventory == null) ? 0 : inventory;
					salableInventoryMap.put(preKey, inventory);
				}else{
					String queryKey = DEFAULTED.NO + StorageConstant.straight + 
							t.getWarehouseId() + StorageConstant.straight + t.getSku();
					Integer inventory = unPreOccupyInventoryMap.get(queryKey);
					inventory = (inventory == null) ? 0 : inventory;
					salableInventoryMap.put(preKey, inventory);
				}
			}
		});
		return salableInventoryMap;
	}
	/** 
	 * 检查库存是否充足 
	 * key: bizId_SKU
	 * value: true库存充足false库存不足
	 * NOTE: 非预占库存方式： 首先统计当前查询列表中相同记录的所需总库存,若总库存小于可售库存,则所有记录库存不足
	 * */
	@Override
	public Map<String, Boolean> checkSalableInventory(List<SkuInventoryQuery> skuInventoryQueries){
		// TODO ""
		if(CollectionUtils.isEmpty(skuInventoryQueries)) return new HashMap<>();
		if(false == validateCheckInventoryParameter(skuInventoryQueries)){
			throw new InventoryServiceException("库存不足");
		}
		Map<Integer, List<SkuInventoryQuery>> splitMap = splitSkuInventoryQueryByPreOccupySign(skuInventoryQueries);
		Map<String, Integer> preOccupyInventoryMap = queryPreOccupySalableInventory(splitMap.get(DEFAULTED.YES));
		Map<String, Integer> unPreOccupyInventoryMap = queryUnPreOccupySalableInventory(splitMap.get(DEFAULTED.NO));
		Map<String, Integer> unPreOccupyTotalCheckInventoryMap = collectUnPreOccupyCheckInventory(splitMap.get(DEFAULTED.NO));
		Map<String, Boolean> checkResultMap = new HashMap<>();
		skuInventoryQueries.forEach(new Consumer<SkuInventoryQuery>() {
			@Override
			public void accept(SkuInventoryQuery t) {
				String preKey = t.getBizId() + StorageConstant.straight + t.getSku();
				if(t.isBizPreOccupy()){
					Integer inventory = preOccupyInventoryMap.get(DEFAULTED.YES + StorageConstant.straight + preKey);
					inventory = (inventory == null)?0:inventory;
					checkResultMap.put(preKey, t.getQuantity() <= inventory);
				}else{
					String checkKey = DEFAULTED.NO + StorageConstant.straight + 
							t.getWarehouseId() + StorageConstant.straight + t.getSku();
					Integer inventory = unPreOccupyInventoryMap.get(checkKey);
					inventory = (inventory == null)?0:inventory;
					Integer checkTotalInventory = unPreOccupyTotalCheckInventoryMap.get(checkKey);
					checkTotalInventory = (checkTotalInventory == null)?t.getQuantity():checkTotalInventory;
					checkResultMap.put(preKey, checkTotalInventory <= inventory);
				}
			}
		});
		return checkResultMap;
	}
	@Override
	public PageInfo<InventoryOccupy> queryPageOccupyInfoBySku(String sku, Integer pageNo, Integer pageSize) {
		// TODO ""
		InventoryOccupy inventoryOccupyObj = new InventoryOccupy();
		inventoryOccupyObj.setSku(sku);
		return inventoryOccupyService.queryPageByObject(inventoryOccupyObj, new PageInfo<InventoryOccupy>(pageNo, pageSize));
	}

	@Override
	public List<Inventory> queryInventoryByWarehouseId(Long warehouseId) {
		// TODO ""
		Map<String, Object> params = new HashMap<>();
		params.put("warehouseId", warehouseId);
		return inventoryService.queryByParamNotEmpty(params);
	}
	@Override
	public Integer querySalableInventory(App app, String bizId, String sku, Long warehouseId, boolean isPreOccupyInventory){
		// TODO ""
		SkuInventoryQuery skuInventoryQuery = new SkuInventoryQuery();
		skuInventoryQuery.setApp(app);
		skuInventoryQuery.setBizId(bizId);
		skuInventoryQuery.setWarehouseId(warehouseId);
		skuInventoryQuery.setSku(sku);
		skuInventoryQuery.setBizPreOccupy(isPreOccupyInventory);
		return querySalableInventory(skuInventoryQuery);
	}
	/** 查询可销售库存数量 */
	@Override
	public Integer querySalableInventory(SkuInventoryQuery skuInventoryQuery){
		if(false == validateCheckInventoryParameter(skuInventoryQuery)) throw new InventoryServiceException("参数错误");
		if(skuInventoryQuery.isBizPreOccupy()){ //预占库存模式
			InventoryDistribute distributeQuery = convertSkuQueryToInventoryDistribute(skuInventoryQuery);
//			distributeQuery.setBacked(DistributeBackedStatus.unbacked.getStatus());
			InventoryDistribute distribute = inventoryDistributeService.queryUniqueByObject(distributeQuery);
			if(distribute == null) return 0;//库存分配记录不存在返回0
			return distribute.getInventory();
		}else{
			Inventory inventoryQuery = convertSkuQueryToInventory(skuInventoryQuery);
			Inventory inventory = inventoryService.queryUniqueByObject(inventoryQuery);
			if(inventory == null) return 0; //库存记录不存在返回0
			return inventory.getAvailableInventory();
		}
	}
	/** 检查商品可售库存是否充足 */
	@Override
	public boolean checkInventoryQuantity(App app, String bizId, String sku, Long warehouseId, boolean isPreOccupyInventory, Integer quantity){
		logger.info("检查库存参数 --> app:"+app+"; bizId:"+bizId+"; sku:"+sku+"; warehouseId:"+warehouseId+"; isPreOccupyInventory:"+isPreOccupyInventory+"; quantity"+quantity);
		SkuInventoryQuery skuInventoryQuery = new SkuInventoryQuery();
		skuInventoryQuery.setApp(app);
		skuInventoryQuery.setBizId(bizId);
		skuInventoryQuery.setWarehouseId(warehouseId);
		skuInventoryQuery.setSku(sku);
		skuInventoryQuery.setBizPreOccupy(isPreOccupyInventory);
		skuInventoryQuery.setQuantity(quantity);
		return checkInventoryQuantity(skuInventoryQuery);
	}
	/** 检查商品可售库存是否充足 */
	@Override
	public boolean checkInventoryQuantity(SkuInventoryQuery skuInventoryQuery){
		logger.info("检查库存参数 --> app:"+skuInventoryQuery.getAppName()+"; bizId:"+skuInventoryQuery.getBizId()+"; sku:"+skuInventoryQuery.getSku()+"; warehouseId:"+skuInventoryQuery.getWarehouseId()+"; isPreOccupyInventory:"+skuInventoryQuery.isBizPreOccupy()+"; quantity"+skuInventoryQuery.getQuantity());
		if(false == validateCheckInventoryParameter(skuInventoryQuery)){
			throw new InventoryServiceException("查询参数错误");
		}
		Integer salableInventory = querySalableInventory(skuInventoryQuery);
		if(salableInventory >= skuInventoryQuery.getQuantity()){
			return true;
		}
		return false;
	}

	//根据是否预占库存分组
	private Map<Integer, List<SkuInventoryQuery>> splitSkuInventoryQueryByPreOccupySign(List<SkuInventoryQuery> skuInventoryQueries){
		List<SkuInventoryQuery> preOccupyInventory = new ArrayList<>();
		List<SkuInventoryQuery> unPreOccupyInventory = new ArrayList<>();
		for(SkuInventoryQuery skuInventoryQuery : skuInventoryQueries){
			if(skuInventoryQuery.isBizPreOccupy()){
				preOccupyInventory.add(skuInventoryQuery);
			}else{
				unPreOccupyInventory.add(skuInventoryQuery);
			}
		}
		Map<Integer, List<SkuInventoryQuery>> map = new HashMap<>();
		map.put(DEFAULTED.YES, preOccupyInventory);
		map.put(DEFAULTED.NO, unPreOccupyInventory);
		return map;
	}
	//查询预占库存方式的商品可销售库存
	//key: 1_bizId_sku
	private Map<String, Integer> queryPreOccupySalableInventory(List<SkuInventoryQuery> skuInventoryQueries){
		if(CollectionUtils.isEmpty(skuInventoryQueries)) return new HashMap<>();
		List<InventoryDistribute> distributes = new ArrayList<>();
		for(SkuInventoryQuery skuInventoryQuery : skuInventoryQueries){
			InventoryDistribute distribute = convertSkuQueryToInventoryDistribute(skuInventoryQuery);
			distribute.setBacked(DistributeBackedStatus.unbacked.getStatus());
			distributes.add(distribute);
		}
		List<InventoryDistribute> distributeObjs = inventoryDistributeService.queryInventoryDistributes(distributes);
		Map<String, Integer> resultMap = new HashMap<>();
		if(CollectionUtils.isNotEmpty(distributeObjs)){
			distributeObjs.forEach(new Consumer<InventoryDistribute>() {
				@Override
				public void accept(InventoryDistribute t) {
					String key = DEFAULTED.YES + StorageConstant.straight + 
								t.getBizId() + StorageConstant.straight + t.getSku();
					resultMap.put(key, t.getInventory());
				}
			});
		}
		return resultMap;
	}
	//查询不是预占库存方式的商品可销售库存
	//key: 0_warehouseId_sku
	private Map<String, Integer> queryUnPreOccupySalableInventory(List<SkuInventoryQuery> skuInventoryQueries){
		if(CollectionUtils.isEmpty(skuInventoryQueries)) return new HashMap<>();
		List<Inventory> inventories = new ArrayList<>();
		for(SkuInventoryQuery skuInventoryQuery : skuInventoryQueries){
			inventories.add(convertSkuQueryToInventory(skuInventoryQuery));
		}
		List<Inventory> inventoryObjs = inventoryService.queryInventoryInfo(inventories);
		Map<String, Integer> resultMap = new HashMap<>();
		if(CollectionUtils.isNotEmpty(inventoryObjs)){
			inventoryObjs.forEach(new Consumer<Inventory>() {
				@Override
				public void accept(Inventory t) {
					String key = DEFAULTED.NO + StorageConstant.straight + 
							t.getWarehouseId() + StorageConstant.straight + t.getSku();
					resultMap.put(key, t.getAvailableInventory());	
				}
			});
		}
		return resultMap;
	}
	
	
	//非预占库存方式下，库存校验需汇总相同库存记录的库存数量
	//key: 0_warehouseId_sku
	private Map<String, Integer> collectUnPreOccupyCheckInventory(List<SkuInventoryQuery> skuInventoryQueries){
		Map<String, Integer> collectResultMap = new HashMap<>();
		skuInventoryQueries.forEach(new Consumer<SkuInventoryQuery>() {
			@Override
			public void accept(SkuInventoryQuery t) {
				if(false == t.isBizPreOccupy()){
					String key = DEFAULTED.NO + StorageConstant.straight + 
							t.getWarehouseId() + StorageConstant.straight + t.getSku();
					Integer checkInventory = collectResultMap.get(key);
					checkInventory = (checkInventory == null) ? t.getQuantity() : (checkInventory+t.getQuantity());
					collectResultMap.put(key, checkInventory);
				}
			}
		});
		return collectResultMap;
	}	
	/**
	 * 查询可销售库存 
	 * @param spId,sku,warehouseId
	 */
	private List<InventoryDto> queryInventoryInfo(List<Inventory> queries){
		if (CollectionUtils.isEmpty(queries)) return null;
		List<Inventory> inventoryObjs = inventoryService.queryInventoryInfo(queries);
		if (CollectionUtils.isEmpty(inventoryObjs)) return null;
		List<InventoryDto> inventoryDtos = new ArrayList<InventoryDto>();
		for (Inventory inventory : inventoryObjs) {
			inventoryDtos.add(initInventoryDto(inventory));
		}
		inventoryDtos = setWarehouseInfo(inventoryDtos); //库存信息
		return inventoryDtos;	
	}
	private List<InventoryDto> queryInventoryInfoByParam(Map<String, Object> param){
		List<Inventory> inventoryObjs = inventoryService.queryInventoryInfoByParam(param);
		if (CollectionUtils.isEmpty(inventoryObjs)) return null;
		List<InventoryDto> inventoryDtos = new ArrayList<InventoryDto>();
		for (Inventory inventory : inventoryObjs) {
			inventoryDtos.add(initInventoryDto(inventory));
		}
		inventoryDtos = setWarehouseInfo(inventoryDtos); //库存信息
		return inventoryDtos;	
	}
	/* 可用库存数据转换 */
	private InventoryDto initInventoryDto(Inventory inventory){
		return inventoryService.getInventoryDetailInfo(inventory);
	}
	//设置仓库信息
	private List<InventoryDto> setWarehouseInfo(List<InventoryDto> inventoryList){
		if(CollectionUtils.isEmpty(inventoryList)) return inventoryList;
		List<Long> whIdList = new ArrayList<>();
		inventoryList.forEach(new Consumer<InventoryDto>() {
			@Override
			public void accept(InventoryDto t) {
				whIdList.add(t.getWarehouseId());
			}
		});
		List<Warehouse> warehouses = warehouseService.queryByIds(whIdList);
		for (InventoryDto dto : inventoryList) {
			warehouses.forEach(new Consumer<Warehouse>() {
				@Override
				public void accept(Warehouse t) {
					if(t.getId().equals(dto.getWarehouseId())){
						dto.setWarehouseName(t.getName());
						dto.setWarehouseCode(t.getCode());
					}
				}
			});
		}
		return inventoryList;
	}
	//查询参数转换
	private Inventory convertInventoryQueryToInventory(InventoryQuery query){
		Inventory inventory = new Inventory();
		if(StringUtil.isNotEmpty(query.getSku())){
			inventory.setSku(query.getSku());	
		}
		if(query.getWarehouseId() != null){
			inventory.setWarehouseId(query.getWarehouseId());	
		}
		if(query.getSpId() != null){
			inventory.setSpId(query.getSpId());	
		}
		return inventory;
	}
	//查询参数转换
	private Inventory convertSkuQueryToInventory(SkuInventoryQuery query){
		Inventory inventory = new Inventory();
		if(StringUtil.isNotEmpty(query.getSku())){
			inventory.setSku(query.getSku());	
		}
		if(query.getWarehouseId() != null){
			inventory.setWarehouseId(query.getWarehouseId());	
		}
		return inventory;
	}
	//校验库存查询参数
	private InventoryDistribute convertSkuQueryToInventoryDistribute(SkuInventoryQuery query){
		InventoryDistribute distribute = new InventoryDistribute();
		if(StringUtil.isNotEmpty(query.getSku())){
			distribute.setSku(query.getSku());	
		}
		if(query.getApp() != null){
			distribute.setApp(query.getAppName());
		}
		if(StringUtil.isNotEmpty(query.getBizId())){
			distribute.setBizId(query.getBizId());
		}
		return distribute;
	}
	//校验库存查询参数
	private boolean validateCheckInventoryParameter(List<SkuInventoryQuery> skuInventoryQueries){
		for(SkuInventoryQuery skuInventoryQuery : skuInventoryQueries){
			if(false == validateCheckInventoryParameter(skuInventoryQuery)){
				return false;
			}
		}
		return true;
	}
	
	/* 校验查询参数 */
	private boolean validateCheckInventoryParameter(SkuInventoryQuery skuInventoryQuery){
		if((false == skuInventoryQuery.isBizPreOccupy() && 
				StringUtil.isNotEmpty(skuInventoryQuery.getSku()) && 
				skuInventoryQuery.getWarehouseId() != null && StringUtils.isNotEmpty(skuInventoryQuery.getBizId())) || 
				(skuInventoryQuery.isBizPreOccupy() && 
						skuInventoryQuery.getApp() != null && 
						StringUtil.isNotEmpty(skuInventoryQuery.getSku()) &&
						StringUtil.isNotEmpty(skuInventoryQuery.getBizId()))){
			return true;
		}
		return false;
	}
	
	
	
//	
//	private Logger logger = LoggerFactory.getLogger(InventoryQueryService.class);
//
//	@Autowired
//	private IInventoryService inventoryService;
//
//	@Autowired
//	private IInventoryDistributeService inventoryDistributeService;
//
//	@Autowired
//	private IWarehouseService warehouseService;
//
//	@Autowired
//	private IInventoryCacheService inventoryCacheService;
//
//	@Autowired
//	private IInventoryOccupyService inventoryOccupyService;
//
//	@Override
//	public List<InventoryDto> selectAvailableForSaleBySku(String sku) {
//		if (StringUtils.isBlank(sku)) {
//			return null;
//		}
//
//		return selectAvailableForSaleBySkuAndWhId(sku, null);
//	}
//
//	@Override
//	public List<InventoryDto> selectAvailableForSaleBySkuAndWhId(String sku, Long warehouseId) {
//		List<InventoryDtoQuery> dtoQueries = new ArrayList<InventoryDtoQuery>();
//		InventoryDtoQuery dtoQuery = new InventoryDtoQuery();
//		dtoQuery.setSku(sku);
//		dtoQuery.setWarehouseId(warehouseId);
//		dtoQueries.add(dtoQuery);
//		return selectAvailableForSaleBySkuAndWhIdList(dtoQueries);
//	}
//
//	@Override
//	public PageInfo<InventoryDto> queryAvailableForSaleBySkuSpIdAndWId(InventoryQuery dtoQuery, int page,
//			int pageSize) {
//		if (pageSize > 50) {
//			pageSize = 50;
//		}
//		Map<String, Object> params = new HashMap<>();
//		params.put("sku", dtoQuery.getSku());
//		params.put("spId", dtoQuery.getSpId());
//		PageInfo<Inventory> pageInfo = inventoryService.queryPageByParamNotEmpty(params,
//				new PageInfo<Inventory>(page, pageSize));
//		List<Inventory> inventoryObjs = pageInfo.getRows();
//		if (!CollectionUtils.isNotEmpty(inventoryObjs))
//			return null;
//		List<InventoryDto> dtos = new ArrayList<InventoryDto>();
//		InventoryDto dto = null;
//		for (Inventory ido : inventoryObjs) {
//			dto = new InventoryDto();
//			dto.setDistrictId(ido.getDistrictId());
//			dto.setRealInventory(ido.getInventory());
//			dto.setOccupy(ido.getOccupy());
//			dto.setSku(ido.getSku());
//			dto.setSpId(ido.getSpId());
//			dto.setWarehouseId(ido.getWarehouseId());
//			Warehouse warehouseObj = warehouseService.queryById(ido.getWarehouseId());
//			dto.setWarehouseName(warehouseObj.getName());
//
//			// 获得该仓库已经分配出去的库存信息
//			params.clear();
//			params.put("inventoryId", ido.getId());
//			List<InventoryDistribute> hasDistributeDOs = inventoryDistributeService.queryByParamNotEmpty(params);
//			// 已分配库存
//			Integer totalPSInventory = 0;
//			if (CollectionUtils.isNotEmpty(hasDistributeDOs)) {
//				for (InventoryDistribute ivddo : hasDistributeDOs) {
//					totalPSInventory += ivddo.getInventory();
//				}
//			}
//
//			int left = ido.getInventory() - ido.getOccupy() - totalPSInventory;
//			left = (left <= 0 ? 0 : left);
//			dto.setInventory(left);
//			dtos.add(dto);
//		}
//		PageInfo<InventoryDto> pi = new PageInfo<InventoryDto>();
//		pi.setRows(dtos);
//		pi.setPage(page);
//		pi.setSize(pageSize);		
//		pi.setRecords(pageInfo.getRecords());
//		return pi;
//	}
//
//	@Override
//	public PageInfo<InventoryDto> queryAvailableForSaleBySkuSpIdAndWIdList(List<InventoryQuery> inventoryQueries,
//			int page, int pageSize) {
//		if (pageSize > 50) {
//			pageSize = 50;
//		}
//		List<Inventory> inventoryObjs = inventoryService.selectPageBySkuAndSpIdList(inventoryQueries, page, pageSize);
//		if (CollectionUtils.isNotEmpty(inventoryObjs)) {
//			Long totalCount = inventoryService.selectCountBySkuAndSpIdList(inventoryQueries);
//
//			PageInfo<InventoryDto> pageInfo = new PageInfo<InventoryDto>();
//			List<InventoryDto> dtos = new ArrayList<InventoryDto>();
//			InventoryDto dto = null;
//			for (Inventory ido : inventoryObjs) {
//				dto = new InventoryDto();
//				dto.setDistrictId(ido.getDistrictId());
//				dto.setRealInventory(ido.getInventory());
//				dto.setOccupy(ido.getOccupy());
//				dto.setSku(ido.getSku());
//				dto.setSpId(ido.getSpId());
//				dto.setWarehouseId(ido.getWarehouseId());
//
//				Warehouse warehouseObj = warehouseService.queryById(ido.getWarehouseId());
//				dto.setWarehouseName(warehouseObj.getName());
//
//				// 获得该仓库已经分配出去的库存信息
//				Map<String, Object> params = new HashMap<>();
//				params.put("inventoryId", ido.getId());
//				List<InventoryDistribute> hasDistributeDOs = inventoryDistributeService.queryByParamNotEmpty(params);
//				// 已分配库存
//				Integer totalPSInventory = 0;
//				if (CollectionUtils.isNotEmpty(hasDistributeDOs)) {
//					for (InventoryDistribute ivddo : hasDistributeDOs) {
//						totalPSInventory += ivddo.getInventory();
//					}
//				}
//
//				int left = ido.getInventory() - ido.getOccupy() - totalPSInventory;
//				left = (left <= 0 ? 0 : left);
//				dto.setInventory(left);
//				dtos.add(dto);
//			}
//			pageInfo.setRows(dtos);
//			pageInfo.setPage(page);
//			pageInfo.setSize(pageSize);
//			pageInfo.setRecords(totalCount.intValue());
//			return pageInfo;
//		}
//
//		return null;
//	}
//
//	@Override
//	public int selectInvetory(App app, String bizId, String sku) {
//		Integer inventory = inventoryCacheService.selectInevntoryFromCache(app, bizId, sku);
//		if (null != inventory) {
//			return inventory.intValue();
//		}
//		Map<String, Object> params = new HashMap<>();
//		params.put("app", app.getName());
//		params.put("bizId", bizId);
//		params.put("sku", sku);
//		params.put("backed", Constant.DELECTED.NO);
//		try {
//			List<InventoryDistribute> distributeObjs = inventoryDistributeService.queryByParamNotEmpty(params);
//			if (CollectionUtils.isNotEmpty(distributeObjs)) {
//				InventoryDistribute distributeObj = distributeObjs.get(0);
//				inventory = distributeObj.getInventory();
//				inventoryCacheService.setInventoryCache(app, bizId, sku, inventory);
//				return inventory;
//			}
//		} catch (Exception e) {
//			logger.error(e.getMessage());
//		}
//		return 0;
//	}
//
//	@Override
//	public Map<String, Integer> batchSelectInventory(List<SkuInventoryQuery> queries) {
//		Map<String, Integer> inventoryMap = new HashMap<String, Integer>();
////		for (SkuInventoryQuery skuInventoryQuery : queries) {
////			ResultInfo<String> message = validate(skuInventoryQuery);
////			if (Boolean.FALSE == message.isSuccess()) {
////				// 参数验证不通过直接返回
////				return new HashMap<String, Integer>();
////			}
////		}
//
//		List<Map<String, String>> params = new ArrayList<Map<String, String>>();
//		for (SkuInventoryQuery skuInventoryQuery : queries) {
//			App app = skuInventoryQuery.getApp();
//			String sku = skuInventoryQuery.getSku();
//			String bizId = skuInventoryQuery.getBizId();
//			Integer inventory = inventoryCacheService.selectInevntoryFromCache(app, bizId, sku);
//			if (null == inventory) {
//				Map<String, String> param = new HashMap<String, String>();
//				param.put("app", app.getName());
//				param.put("sku", sku);
//				param.put("bizId", bizId);
//				params.add(param);
//				continue;
//			}
//			inventoryMap.put(bizId + StorageConstant.straight + sku, inventory);
//		}
//		if (CollectionUtils.isEmpty(params)) {
//			return inventoryMap;
//		}
//		List<InventoryDistribute> distributeObjs = inventoryDistributeService.selectByAppAndSkuAndBizIds(params);
//		if (CollectionUtils.isNotEmpty(distributeObjs)) {
//			for (InventoryDistribute inventoryDistributeDO : distributeObjs) {
//				App app = App.valueOf(inventoryDistributeDO.getApp());
//				String bizId = inventoryDistributeDO.getBizId();
//				String sku = inventoryDistributeDO.getSku();
//				Integer inventory = inventoryDistributeDO.getInventory();
//				inventoryMap.put(bizId + StorageConstant.straight + sku, inventory);
//				inventoryCacheService.setInventoryCache(app, bizId, sku, inventory);
//			}
//		}
//		return inventoryMap;
//	}
//
//	@Override
//	public List<Inventory> queryBySkuList(List<String> skuList){
//		if (CollectionUtils.isEmpty(skuList)) {
//			return null;
//		}
//		Map<String, Object> params = new HashMap<>();
//		params.put(MYBATIS_SPECIAL_STRING.COLUMNS.name(),
//				" sku in(" + StringUtils.join(skuList, Constant.SPLIT_SIGN.COMMA) + ")");
//		try {
//			return inventoryService.queryByParamNotEmpty(params);
//		} catch (Exception e) {
//			logger.error(e.toString());
//			return null;
//		}
//	}
//
//	@Override
//	public List<Inventory> queryBySku(String sku) {
//		Map<String, Object> params = new HashMap<>();
//		params.put("sku", sku);
//		return inventoryService.queryByParamNotEmpty(params);
//	}
//
//	@Override
//	public Map<String, ResultInfo<String>> checkAllInventory(List<SkuInventoryQuery> queries) {
//		if (CollectionUtils.isEmpty(queries)) {
//			return null;
//		}
////		for (SkuInventoryQuery skuInventoryQuery : queries) {
////			ResultInfo<String> message = validate(skuInventoryQuery);
////			if (Boolean.FALSE == message.isSuccess()) {
////				// 参数验证不通过直接返回
////				return new HashMap<String, ResultInfo<String>>();
////			}
////		}
//
//		List<Map<String, String>> params = new ArrayList<Map<String, String>>();
//		Map<String, Integer> quantityMap = new HashMap<String, Integer>();
//		Map<String, Integer> cachedInventory = new HashMap<String, Integer>();
//		for (SkuInventoryQuery skuInventoryQuery : queries) {
//			App app = skuInventoryQuery.getApp();
//			String bizId = skuInventoryQuery.getBizId();
//			String sku = skuInventoryQuery.getSku();
//
//			String key = bizId.concat(StorageConstant.straight).concat(sku);
//			quantityMap.put(key, skuInventoryQuery.getQuantity());
//
//			// 从缓存获得库存
//			Integer cacheInventory = inventoryCacheService.selectInevntoryFromCache(app, bizId, sku);
//			if (null == cacheInventory) {
//				Map<String, String> param = new HashMap<String, String>();
//				param.put("app", app.getName());
//				param.put("sku", sku);
//				param.put("bizId", bizId);
//				params.add(param);
//				continue;
//			}
//			cachedInventory.put(key, cacheInventory);
//		}
//
//		if (CollectionUtils.isNotEmpty(params)) {
//			// 缓存中未获得到库存，从数据库获得库存
//			List<InventoryDistribute> distributeObjs = inventoryDistributeService.selectByAppAndSkuAndBizIds(params);
//			if (CollectionUtils.isNotEmpty(distributeObjs)) {
//				for (InventoryDistribute inventoryDistributeDO : distributeObjs) {
//					App app = App.valueOf(inventoryDistributeDO.getApp());
//					String bizId = inventoryDistributeDO.getBizId();
//					String sku = inventoryDistributeDO.getSku();
//					String key = bizId.concat(StorageConstant.straight).concat(sku);
//					Integer inventory = inventoryDistributeDO.getInventory();
//					cachedInventory.put(key, inventory);
//					inventoryCacheService.setInventoryCache(app, bizId, sku, inventory);
//				}
//			}
//		}
//
//		Map<String, ResultInfo<String>> resultMap = new HashMap<String, ResultInfo<String>>();
//		for (Entry<String, Integer> inventoryMap : cachedInventory.entrySet()) {
//			ResultInfo<String> message = new ResultInfo<String>();
//			String key = inventoryMap.getKey();
//			Integer inventory = inventoryMap.getValue();
//			// 库存小于采购数量
//			if (inventory.intValue() >= quantityMap.get(key).intValue()) {
//				message.setData("库存充足");
//			} else {
//				if (inventory.intValue() < 0) {
//					inventory = 0;
//				}
//				message.setMsg(new FailInfo("库存不足"));
//			}
//			String[] keys = key.split(StorageConstant.straight);
//			resultMap.put(keys[1], message);
//		}
//		return resultMap;
//	}
//
//	public List<InventoryDto> selectAvailableForSaleBySkuSpIdAndWId(InventoryDtoQuery dtoQuery) {
//		List<InventoryDtoQuery> dtoQueries = new ArrayList<InventoryDtoQuery>();
//		dtoQueries.add(dtoQuery);
//		return selectAvailableForSaleBySkuAndWhIdList(dtoQueries);
//	}
//
//	@Override
//	public List<InventoryDto> selectAvailableForSaleBySkuAndWhIdList(List<InventoryDtoQuery> dtoQueries) {
//		if (CollectionUtils.isEmpty(dtoQueries)) {
//			return null;
//		}
//		// 验证参数
////		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
////		Validator validator = factory.getValidator();
////		List<InventoryDtoQuery> cleanQuery = new ArrayList<InventoryDtoQuery>();
////		for (InventoryDtoQuery inventoryDtoQuery : dtoQueries) {
////			Set<ConstraintViolation<InventoryDtoQuery>> violations = validator.validate(inventoryDtoQuery);
////			if (CollectionUtils.isNotEmpty(violations)) {
////				continue;
////			}
////			cleanQuery.add(inventoryDtoQuery);
////		}
//
//		try {
//
//			List<Inventory> inventoryObjs = inventoryService.selectBySkuAndWhIdList(dtoQueries);
//			if (CollectionUtils.isEmpty(inventoryObjs)) {
//				return null;
//			}
//
//			List<InventoryDto> inventoryDtos = new ArrayList<InventoryDto>();
//			InventoryDto inventoryDto = null;
//			for (Inventory item : inventoryObjs) {
//				inventoryDto = new InventoryDto();
//				inventoryDto.setDistrictId(item.getDistrictId());
//				inventoryDto.setSku(item.getSku());
//				inventoryDto.setSpId(item.getSpId());
//				inventoryDto.setWarehouseId(item.getWarehouseId());
//				inventoryDto.setOccupy(item.getOccupy());
//				inventoryDto.setRealInventory(item.getInventory());
//
//				Warehouse warehouseObj = warehouseService.queryById(item.getWarehouseId());
//				inventoryDto.setWarehouseName(warehouseObj.getName());
//
//				// 获得该仓库已经分配出去的库存信息
//				Map<String, Object> params = new HashMap<>();
//				params.put("inventoryId", item.getId());
//				List<InventoryDistribute> hasDistributeDOs = inventoryDistributeService.queryByParamNotEmpty(params);
//				// 已分配库存
//				Integer totalPSInventory = 0;
//				if (CollectionUtils.isNotEmpty(hasDistributeDOs)) {
//					for (InventoryDistribute ivddo : hasDistributeDOs) {
//						totalPSInventory += ivddo.getInventory();
//					}
//				}
//
//				int left = item.getInventory() - item.getOccupy() - totalPSInventory;
//				left = (left <= 0 ? 0 : left);
//				inventoryDto.setInventory(left);
//				inventoryDtos.add(inventoryDto);
//			}
//			return inventoryDtos;
//		} catch (Exception e) {
//		}
//		return null;
//	}
//
//	@Override
//	public boolean checkInventoryQuantity(App app, String bizId, String sku, Integer quantity) {
//		int inventory = selectInvetory(app, bizId, sku);
//		if (inventory - quantity.intValue() >= 0) {
//			return true;
//		}
//		logger.warn("topic_item_inventory_out,bizId"+bizId+",sku:"+sku);
//		return false;
//	}
//
//	@Override
//	public PageInfo<InventoryOccupy> queryPageOccupyInfoBySku(String sku, Integer pageNo, Integer pageSize) {
//		InventoryOccupy inventoryOccupyObj = new InventoryOccupy();
//		inventoryOccupyObj.setSku(sku);
//		return inventoryOccupyService.queryPageByObject(inventoryOccupyObj, new PageInfo<InventoryOccupy>(pageNo, pageSize));
//	}
//
//	@Override
//	public List<InventoryDistribute> queryHasInventoryBizInfo(App app, String sku) {
//		if (StringUtils.isEmpty(sku) || app == null) {
//			return null;
//		}
//		Map<String, Object> params = new HashMap<>();
//		params.put("sku", sku);
//		params.put("app", app.name());
//		params.put(MYBATIS_SPECIAL_STRING.COLUMNS.name(), " inventory > 0 ");
//		return inventoryDistributeService.queryByParamNotEmpty(params);
//	}
//
//	@Override
//	public List<Inventory> queryInventoryByWarehouseId(Long warehouseId) {
//		Map<String, Object> params = new HashMap<>();
//		params.put("warehouseId", warehouseId);
//		return inventoryService.queryByParamNotEmpty(params);
//	}

//	public static <T> ResultInfo<String> validate(T t) {
//		// 验证参数
//		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
//		Validator validator = factory.getValidator();
//		Set<ConstraintViolation<T>> violations = validator.validate(t);
//		if (CollectionUtils.isNotEmpty(violations)) {
//			for (ConstraintViolation<T> constraintViolation : violations) {
//				ResultInfo<String> message = new ResultInfo<String>(
//						constraintViolation.getPropertyPath() + ":" + constraintViolation.getMessage());
//				return message;
//			}
//		}
//		return new ResultInfo<String>("true");
//	}
}
