/**
 * 
 */
package com.tp.service.stg;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;
import com.tp.common.vo.Constant;
import com.tp.common.vo.Constant.AUTHOR_TYPE;
import com.tp.common.vo.Constant.DEFAULTED;
import com.tp.common.vo.DAOConstant.MYBATIS_SPECIAL_STRING;
import com.tp.common.vo.StorageConstant.App;
import com.tp.common.vo.StorageConstant.DistributeBackedStatus;
import com.tp.common.vo.StorageConstant.InputAndOutputType;
import com.tp.dao.stg.InventoryManageLogDao;
import com.tp.dto.common.FailInfo;
import com.tp.dto.common.ResultInfo;
import com.tp.dto.stg.InventoryApply;
import com.tp.dto.stg.InventoryDto;
import com.tp.dto.stg.OccupyInventoryDto;
import com.tp.exception.InventoryServiceException;
import com.tp.model.stg.Inventory;
import com.tp.model.stg.InventoryDistribute;
import com.tp.model.stg.InventoryLog;
import com.tp.model.stg.InventoryManageLog;
import com.tp.model.stg.InventoryOccupy;
import com.tp.model.stg.Warehouse;
import com.tp.util.StringUtil;
/**
 * @author szy
 *
 */
@Service
public class InventoryOperService implements IInventoryOperService {
	private Logger logger = LoggerFactory.getLogger(InventoryOperService.class);

	@Autowired
	private IInventoryService inventoryService;
	@Autowired
	private IInventoryDistributeService inventoryDistributeService;
	@Autowired
	private IWarehouseService warehouseService;
	@Autowired
	private IInventoryOccupyService inventoryOccupyService;

	@Value("#{meta['default.warehouse.code']}")
	private String defaultWarehouseCode;

	@Autowired
	private IInventoryCacheService inventoryCacheService;
	@Autowired
	private IInventoryLogService inventoryLogService;
	@Autowired
	private InventoryManageLogDao inventoryManageLogDao;
	
	
	
	
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public ResultInfo<Boolean> requestInventory(App app, String bizId, String sku, int inventory, Long spId,
			Long warehouseId, boolean isPreOccupy){
		InventoryApply apply = new InventoryApply();
		apply.setApp(app);
		apply.setSku(sku);
		apply.setInventory(inventory);
		apply.setSpId(spId);
		apply.setWarehouseId(warehouseId);
		apply.setPreOccupy(isPreOccupy);
		apply.setBizId(bizId);
		return requestInventory(apply);
	}
	
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public ResultInfo<Boolean> requestInventory(InventoryApply apply) {
		FailInfo failInfo = validateInventoryApply(apply);
		if(failInfo != null) return new ResultInfo<>(failInfo);
		if(apply.isPreOccupy()){ // 预留
			return requestPreOccupyInventory(apply);	
		}else{
			return requestUnPreOccupyInventory(apply);
		}
	}
	
	private FailInfo validateInventoryApply(InventoryApply apply){
		if(StringUtils.isBlank(apply.getBizId()) || StringUtils.isBlank(apply.getSku()) || 
				apply.getSpId() == null || apply.getApp() == null || 
				apply.getWarehouseId() == null || apply.getInventory() == null){
			return new FailInfo("参数错误");
		}
		return null;
	}
	
	/** 专场库存还库 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public ResultInfo<Boolean> backRequestInventory(App app, String bizId) {
		if (StringUtils.isBlank(bizId) || null == app) {
			return new ResultInfo<Boolean>(new FailInfo("参数错误"));
		}
		return backRequestInventory(app, bizId, null, null);		
	}
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public ResultInfo<Boolean> backRequestInventoryBySku(App app, String bizId, String sku) {
		if (StringUtils.isBlank(bizId) || StringUtils.isBlank(sku) || app == null) {
			return new ResultInfo<>(new FailInfo("参数错误"));
		}
		return backRequestInventory(app, bizId, sku, null);
	}
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public ResultInfo<Boolean> backRequestInventoryBySkuAndWarehouse(App app, String bizId, String sku,Long warehouseId) {
		if (StringUtils.isBlank(bizId) || StringUtils.isBlank(sku) || app == null) {
			return new ResultInfo<Boolean>(new FailInfo("参数错误"));
		}
		return backRequestInventory(app, bizId, sku, warehouseId);
	}	
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public ResultInfo<Boolean> backRequestSpecInventoryBySku(App app, String bizId, String sku, int backInvetory) {
		if (StringUtils.isBlank(bizId) || StringUtils.isBlank(sku) || app == null || backInvetory < 1) {
			return new ResultInfo<Boolean>(new FailInfo("调用错误，参数不正确", 3));
		}
		Map<String, Object> params = new HashMap<>();
		params.put("app", app.getName());
		params.put("bizId", bizId);
		params.put("sku", sku);
		InventoryDistribute distributeObj = inventoryDistributeService.queryUniqueByParams(params);
		if(distributeObj == null){
			return new ResultInfo<>(new FailInfo("还库失败，库存申请记录不存在"));
		}
		Integer totalInventory = distributeObj.getInventory();
		Integer occupyCount = distributeObj.getOccupy();
		if(totalInventory < backInvetory){
			return new ResultInfo<>(new FailInfo("退还库存大于当前剩余库存"));
		}
		//全部归还
		if(totalInventory == backInvetory){
			backRequestInventory(distributeObj);
			return new ResultInfo<>(Boolean.TRUE);
		}
		//部分归还
		inventoryDistributeService.reduceInventoryDistribute(distributeObj, backInvetory);
		Inventory inventoryObj = inventoryService.getInventoryById(distributeObj.getInventoryId());
		setDistributeInventory(inventoryObj.getInventoryDistributes(), distributeObj);
		inventoryService.increaseRealInventoryById(inventoryObj.getId(), backInvetory);
		inventoryObj.setInventory(inventoryObj.getInventory() + backInvetory);
		inventoryLogService.insert(initInventoryLog(inventoryObj, sku, InputAndOutputType.PH, backInvetory));

//		String logRemark = String.format("活动[%d]商品[%s]返还部分库存[%d]订单占用库存[%d]", 
//				distributeObj.getBizId(),distributeObj.getSku(),backInvetory,occupyCount);
		String logRemark = "活动["+distributeObj.getBizId()+"]商品["+distributeObj.getSku()+"]返还部分库存["+backInvetory+"]订单占用库存["+occupyCount+"]"; 
		inventoryManageLogDao.insert(initInventoryManageLog(inventoryService.getInventoryDetailInfo(inventoryObj), 2, logRemark));
		return new ResultInfo<>(Boolean.TRUE);
	}
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public Map<String, ResultInfo<String>> batchOccupyInventory(List<OccupyInventoryDto> inventoryDtos) {
		if (CollectionUtils.isEmpty(inventoryDtos)) {
			return null;
		}
		Map<String, ResultInfo<String>> result = new HashMap<String, ResultInfo<String>>();
		for (OccupyInventoryDto occupyInventoryDto : inventoryDtos) {
			result.put(occupyInventoryDto.getSku(), occupyInventory(occupyInventoryDto));		
		}
		return result;
	}	

		
	/** 
	 * 下单后占用库存，多次调用将增加该订单占用的库存数量
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public ResultInfo<String> occupyInventory(OccupyInventoryDto occupyInventoryDto) {
		if(occupyInventoryDto.isPreOccupy()){
			return occupyInventoryWithPreOccupy(occupyInventoryDto.getApp(), occupyInventoryDto.getBizId(),
					occupyInventoryDto.getSku(), occupyInventoryDto.getInventory(),
					occupyInventoryDto.getOrderCode().toString());
		}else{
			return occupyInventoryWithoutPreOccupy(occupyInventoryDto.getApp(), occupyInventoryDto.getSku(), 
					occupyInventoryDto.getInventory(), occupyInventoryDto.getOrderCode().toString(), 
					occupyInventoryDto.getWarehouseId());	
		}
	}
	
	//取消库存占用
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public ResultInfo<String> unoccupyInventory(Long orderCode) {
		if (null == orderCode) {
			return new ResultInfo<String>(new FailInfo("参数错误"));
		}
		Map<String, Object> params = new HashMap<>();
		params.put("orderNo", orderCode);
		List<InventoryOccupy> inventoryOccupyObjs = inventoryOccupyService.queryByParamNotEmpty(params);
		if (CollectionUtils.isEmpty(inventoryOccupyObjs)) {
			return new ResultInfo<String>(new FailInfo("操作失败,库存冻结记录不存在"));
		}
		List<Long> occupyIds = new ArrayList<Long>();		
		inventoryOccupyObjs.forEach(new Consumer<InventoryOccupy>() {
			@Override
			public void accept(InventoryOccupy t) {
				ResultInfo<Boolean> result = unoccupyInventory(t);
				if(Boolean.TRUE == result.isSuccess()){
					occupyIds.add(t.getId());
				}else{
					logger.error("解冻库存失败,{},错误信息：{}",t.getId(), result.getMsg().getMessage());
				}
			}
		});
		if(CollectionUtils.isNotEmpty(occupyIds)){
			params.clear();
			params.put(MYBATIS_SPECIAL_STRING.COLUMNS.name(),
					" id in(" + StringUtil.join(occupyIds, Constant.SPLIT_SIGN.COMMA) + ")");
			inventoryOccupyService.deleteByParamNotEmpty(params);
		}
		return new ResultInfo<String>("操作成功");
	}
	
	//增加可销售库存
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public ResultInfo<String> increaseRealInventory(String sku, Long spId, Long warehouseId, Integer quantity) {
		if (StringUtils.isBlank(sku) || spId == null || quantity == null || warehouseId == null) {
			return new ResultInfo<String>(new FailInfo("调用错误，参数不能为空"));
		}
		Integer count = inventoryService.increaseRealInventory(sku, spId, warehouseId, quantity);
		if(count < 1){
			return new ResultInfo<>(new FailInfo("增加可销售库存失败"));
		}
		return new ResultInfo<String>("增加可销售库存成功");
	}
	//减少可销售库存
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public ResultInfo<String> reduceRealInventory(String sku, Long spId, Long warehouseId, Integer quantity) {
		if (StringUtils.isBlank(sku) || spId == null || quantity == null||warehouseId ==null) {
			return new ResultInfo<String>(new FailInfo("调用错误，参数不能为空"));
		}
		Integer count = inventoryService.reduceRealInventory(sku, spId, warehouseId, quantity);
		if(count < 1){
			return new ResultInfo<>(new FailInfo("减少可销售库存失败"));
		}
		return new ResultInfo<String>("减少可销售库存失败");
	}
	@Transactional(propagation = Propagation.REQUIRED)
	@Override
	public ResultInfo<String> batchUnoccupyInventory(List<Long> orderCodes) {
		for (Long code : orderCodes) {
			ResultInfo<String> message = unoccupyInventory(code);
			if (Boolean.FALSE == message.isSuccess()) {
				return message;
			}
		}
		return new ResultInfo<String>("解冻库存成功");
	}
	/** 增加库存记录 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public ResultInfo<String> addInventory(String sku, Long spId, Long warehouseId, Integer inventory) {
		if (StringUtils.isBlank(sku) || null == spId || null == warehouseId || null == inventory) {
			return new ResultInfo<String>(new FailInfo("参数错误"));
		}
		if (inventory.intValue() < 0) {
			return new ResultInfo<String>(new FailInfo("入库数量不能小于0"));
		}
		List<Inventory> inventoryObjs = selectInventoryInfo(sku, spId, warehouseId);
		if (CollectionUtils.isNotEmpty(inventoryObjs)) {
			return new ResultInfo<String>(new FailInfo("库存记录已存在"));
		}
		Warehouse warehouseObj = warehouseService.queryById(warehouseId);
		if (warehouseObj == null) {
			return new ResultInfo<String>(new FailInfo("仓库不存在"));
		}
		if (warehouseObj.getSpId().longValue() != spId) {
			return new ResultInfo<String>(new FailInfo("仓库不属于当前供应商"));
		}
		Inventory inventoryObj = new Inventory();
		inventoryObj.setCreateTime(new Date());
		inventoryObj.setDistrictId(warehouseObj.getDistrictId());
		inventoryObj.setInventory(inventory);
		inventoryObj.setModifyTime(new Date());
		inventoryObj.setOccupy(0);
		inventoryObj.setReject(0);
		inventoryObj.setSku(sku);
		inventoryObj.setSpId(spId);
		inventoryObj.setWarehouseId(warehouseId);
		inventoryObj = inventoryService.insert(inventoryObj);
		return new ResultInfo<String>(inventoryObj.getId().toString());	
	}
	/**
	 * 根据sku、商家id、仓库获得 获得商家的某个商品的库存信息，
	 * 
	 * @param sku
	 * @param spId
	 * @param warehouseId
	 * @return 
	 */
	@Override
	public List<Inventory> selectInventoryInfo(String sku, Long spId, Long warehouseId) {
		Map<String, Object> params = new HashMap<>();
		params.put("sku", sku);
		params.put("spId", spId);
		params.put("warehouseId", warehouseId);
		List<Inventory> inventories = inventoryService.queryByParamNotEmpty(params);
		if(CollectionUtils.isNotEmpty(inventories)){
			inventories.forEach(new Consumer<Inventory>() {
				@Override
				public void accept(Inventory t) {
					List<InventoryDistribute> distributes = 
							inventoryDistributeService.queryInventoryDistributesByInventoryId(t.getId());
					t.setInventoryDistributes(CollectionUtils.isEmpty(distributes)?new ArrayList<>():distributes);
				}
			});
		}
		return inventories;
	}
	/** 订单发货 */
	@Override
	public ResultInfo<Boolean> reduceInventoryForOrderDelivery(Long orderCode){
		if (null == orderCode) {
			return new ResultInfo<>(new FailInfo("参数错误"));
		}
		Map<String, Object> params = new HashMap<>();
		params.put("orderNo", orderCode);
		List<InventoryOccupy> inventoryOccupyObjs = inventoryOccupyService.queryByParamNotEmpty(params);
		if (CollectionUtils.isEmpty(inventoryOccupyObjs)) {
			return new ResultInfo<>(new FailInfo("操作失败,库存冻结记录不存在"));
		}
		List<Long> occupyIds = new ArrayList<Long>();
		for(InventoryOccupy occupy : inventoryOccupyObjs){
			ResultInfo<Boolean> result = reduceInventoryForOrderDelivery(occupy);
			if(result.isSuccess()){
				occupyIds.add(occupy.getId());
			}
		}
		if(CollectionUtils.isNotEmpty(occupyIds)){
			params.clear();
			params.put(MYBATIS_SPECIAL_STRING.COLUMNS.name(),
					" id in(" + StringUtil.join(occupyIds, Constant.SPLIT_SIGN.COMMA) + ")");
			inventoryOccupyService.deleteByParamNotEmpty(params);
		}
		return new ResultInfo<>(Boolean.TRUE);
	}
	
	
	
	/*---------------------------------------------------------------------------------------------------------*/
	//订单发货
	private ResultInfo<Boolean> reduceInventoryForOrderDelivery(InventoryOccupy occupy){
		if(DEFAULTED.YES.equals(occupy.getOccupyDistribute())){ /* 扣减分配记录中的占用库存数量 */
			InventoryDistribute distributeObj = inventoryDistributeService.queryById(occupy.getInventoryDistId());
			if (null == distributeObj) {
				logger.error("库存分配记录不存在: distributeId = {}", occupy.getInventoryDistId());
				return new ResultInfo<>(new FailInfo("库存分配记录不存在"));
			}
			Integer count = inventoryDistributeService.reduceOccupyDistributeById(distributeObj.getId(), occupy.getInventory());
			if(count < 1){
				logger.error("订单发货 更新占用库存失败");
				return new ResultInfo<>(new FailInfo("订单发货更新占用库存失败"));
			}
			Inventory inventoryObj = inventoryService.queryById(distributeObj.getInventoryId());		
			InventoryLog log = initInventoryLog(inventoryObj, occupy.getSku(), InputAndOutputType.PXS, occupy.getInventory());
			inventoryLogService.insert(log); //日志	
			return new ResultInfo<>(Boolean.TRUE);
		}else{ /* 扣减总库存中的占用库存数量 */
			Inventory inventoryObj = inventoryService.queryById(occupy.getInventoryId());
			if(inventoryObj == null){
				return new ResultInfo<>(new FailInfo("库存信息不存在"));
			}
			inventoryService.reduceOccupyInventoryById(inventoryObj.getId(), occupy.getInventory());
			InventoryLog log = initInventoryLog(inventoryObj, occupy.getSku(), InputAndOutputType.PXS, occupy.getInventory());
			inventoryLogService.insert(log); //日志
			return new ResultInfo<>(Boolean.TRUE);	
		}
	}
	//非预占库存方式调用
	private ResultInfo<String> occupyInventoryWithoutPreOccupy(App app, String sku, int inventory, String orderCode, Long warehouseId) {
		if (StringUtils.isBlank(sku) || app == null || inventory < 1 || warehouseId == null) {
			return new ResultInfo<String>(new FailInfo("参数错误"));
		}
		Map<String, Object> params = new HashMap<>();
		params.put("warehouse_id", warehouseId);
		params.put("sku", sku);
		Inventory inventoryObj = inventoryService.queryUniqueByParams(params);
		if (inventoryObj == null) {
			return new ResultInfo<>("操作失败,库存记录不存在");
		}
		// 分配库存
		Integer leftInventory = inventoryObj.getAvailableInventory() - inventory;
		if (leftInventory < 0) {
			return new ResultInfo<String>(new FailInfo("库存不足，剩余库存数" + inventoryObj.getAvailableInventory()));
		}
		int result = inventoryService.frozenOccupyInventoryById(inventoryObj.getId(), inventory);
		if (result < 1) { // 未更新掉任何库存，返回库存不足
			return new ResultInfo<String>(new FailInfo("库存不足，剩余库存数" + inventoryObj.getAvailableInventory()));
		}
		InventoryOccupy inventoryOccupy = initInventoryOccupy(app, null, inventory, orderCode, sku, null, DEFAULTED.NO,
				inventoryObj.getId());
		inventoryOccupyService.insert(inventoryOccupy);
		logger.info("冻结库存成功: {}", JSONObject.toJSON(inventoryOccupy));
		return new ResultInfo<String>("操作成功");
	}
	//预占库存方式调用
	private ResultInfo<String> occupyInventoryWithPreOccupy(App app, String bizId, String sku, int inventory, String orderCode) {
		if (StringUtils.isBlank(bizId) || StringUtils.isBlank(sku) || app == null || inventory < 1) {
			return new ResultInfo<String>(new FailInfo("参数错误"));
		}
		Map<String, Object> params = new HashMap<>();
		params.put("app", app.getName());
		params.put("bizId", bizId);
		params.put("sku", sku);
		params.put("backed", DistributeBackedStatus.unbacked.getStatus());
		InventoryDistribute distributeObj = inventoryDistributeService.queryUniqueByParams(params);
		if(distributeObj == null){
			return new ResultInfo<>("操作失败,库存分配记录不存在");
		}
		// 分配库存
		Integer leftInventory = distributeObj.getInventory() - inventory;
		if (leftInventory < 0) {
			return new ResultInfo<String>(new FailInfo("库存不足，剩余库存数" + distributeObj.getInventory()));
		}
		int result = inventoryDistributeService.forzenDistInventory(distributeObj.getId(), inventory);
		if (result < 1) { // 未更新掉任何库存，返回库存不足
			return new ResultInfo<String>(new FailInfo("库存不足，剩余库存数" + distributeObj.getInventory()));
		}
		
		InventoryOccupy inventoryOccupy = initInventoryOccupy(app,bizId,inventory,orderCode,sku,
				distributeObj.getId(), DEFAULTED.YES, distributeObj.getInventoryId());
		inventoryOccupyService.insert(inventoryOccupy);
		logger.info("冻结库存成功: {}", JSONObject.toJSON(inventoryOccupy));
		return new ResultInfo<String>("操作成功");
	}
	/** 预占库存分配 */
	private ResultInfo<Boolean> requestPreOccupyInventory(InventoryApply apply){
		Warehouse warehouse = warehouseService.queryById(apply.getWarehouseId());
		if(warehouse == null){
			return new ResultInfo<>(new FailInfo("仓库信息不存在"));
		}
		
		List<Inventory> inventorys = selectInventoryInfo(apply.getSku(), apply.getSpId(), apply.getWarehouseId());
		if (CollectionUtils.isEmpty(inventorys)) {
			//初始化一条库存记录
			inventoryService.insert(initInventory(apply.getSku(), apply.getSpId(), apply.getInventory(), warehouse));
			return new ResultInfo<>(new FailInfo("库存不足，剩余库存数0"));
		}
		logger.info("预占库存活动分配库存：{}，{}", JSONObject.toJSON(apply), JSONObject.toJSON(inventorys.get(0)));
		return doDistributeInventory(apply.getApp(), apply.getBizId(), apply.getSku(), apply.getInventory(), inventorys.get(0));
	}
	/** 
	 * 非预占库存模式下校验库存记录是否存在 
	 * 若不存在，插入一条库存记录
	 **/
	private ResultInfo<Boolean> requestUnPreOccupyInventory(InventoryApply apply){
		Warehouse warehouse = warehouseService.queryById(apply.getWarehouseId());
		if(warehouse == null){
			return new ResultInfo<>(new FailInfo("仓库信息不存在"));
		}
		
		List<Inventory> inventorys = selectInventoryInfo(apply.getSku(), apply.getSpId(), apply.getWarehouseId());
		if (CollectionUtils.isEmpty(inventorys)) {//初始化一条库存记录
			inventoryService.insert(initInventory(apply.getSku(), apply.getSpId(), 0, warehouse));
			return new ResultInfo<>(new FailInfo(apply.getSku()+"库存不足"));
		}else{
			Inventory inventory = inventorys.get(0);
			if(inventory.getAvailableInventory() <= 0){
				return new ResultInfo<>(new FailInfo(apply.getSku()+"库存不足"));
			}
		}
		return new ResultInfo<>(Boolean.TRUE);
	}
	
	/** 初始商品的总库存记录 */
	private Inventory initInventory(String sku, Long spId, Integer inventory, Warehouse warehouse){
		Inventory inventoryObj = new Inventory();
		inventoryObj.setCreateTime(new Date());
		inventoryObj.setDistrictId(warehouse.getDistrictId());
		inventoryObj.setInventory(inventory);
		inventoryObj.setModifyTime(new Date());
		inventoryObj.setOccupy(0);
		inventoryObj.setReject(0);
		inventoryObj.setSku(sku);
		inventoryObj.setSpId(spId);
		inventoryObj.setWarehouseId(warehouse.getId());
		return inventoryObj;
	}
	/**
	 * 执行库存分配(特殊活动预占库存)
	 *  1. 判断总库存是否足够
	 *  2. 增加或更新活动预占库存记录
	 *  3. 扣减总库存并记录日志
	 * @param app
	 * @param bizId
	 * @param sku
	 * @param inventory
	 * @param inventoryObj
	 * @return
	 */
	private ResultInfo<Boolean> doDistributeInventory(App app, String bizId, String sku, int inventory, Inventory inventoryObj) {
		// 验证可分配库存是否足够
		Integer availableInventory = inventoryObj.getAvailableInventory();
		if (availableInventory < inventory) {
			Integer leftInventory = availableInventory - inventory;
			leftInventory = (leftInventory <= 0 ? 0 : leftInventory.intValue());
			return new ResultInfo<>(new FailInfo("库存不足，剩余库存数" + leftInventory));
		}
		InventoryDistribute distributeObj = getDistributeInventory(inventoryObj.getInventoryDistributes(), 
				app.getName(),bizId, inventoryObj.getWarehouseId(), sku);
		if (null == distributeObj) { //不存在库存分配记录
			distributeObj = initDistributeInventory(inventoryObj, app, bizId, sku, inventory);
			logger.info("分配库存信息：{}", JSONObject.toJSON(distributeObj));
			distributeObj = inventoryDistributeService.insert(distributeObj);
			inventoryObj.getInventoryDistributes().add(distributeObj);
		} else {// 已分配过库存，增加库存
			logger.info("增加分配库存数量=" + inventory + "=" + app.getName() + "=" + bizId + "=" + sku);
			inventoryDistributeService.increaseInventoryDistribute(distributeObj, inventory); // 更新
			if (DistributeBackedStatus.backed.getStatus().equals(distributeObj.getBacked())) {// 为某个活动重新分配库存，则更改还库存状态为未还库
				inventoryDistributeService.updateBackedStatus(distributeObj.getId(),DistributeBackedStatus.unbacked.getStatus());
			}
		}
		//总库存扣减掉特殊活动预占的库存数
		Integer updateCount = inventoryService.reduceRealInventory(inventoryObj.getSku(), inventoryObj.getSpId(),  
				inventoryObj.getWarehouseId(), inventory);
		inventoryObj.setInventory(inventoryObj.getInventory() - inventory);
		if(updateCount < 1) throw new InventoryServiceException("更新库存异常");
		//插入日志
		inventoryLogService.insert(initInventoryLog(inventoryObj, sku, InputAndOutputType.PD, inventory));
		String logRemark = String.format("活动[%s]商品[%s]占用库存[%d]", bizId,sku,inventory);
		inventoryManageLogDao.insert(initInventoryManageLog(inventoryService.getInventoryDetailInfo(inventoryObj), 2, logRemark));
		return new ResultInfo<>(Boolean.TRUE);
	}
	
	private InventoryManageLog initInventoryManageLog(InventoryDto dto, Integer changeType, String remark){
		InventoryManageLog log = new InventoryManageLog();
		log.setInventoryId(dto.getId());
		log.setSku(dto.getSku());
		log.setInventory(dto.getRealInventory());
		log.setOccupy(dto.getOccupy());
		log.setReserveInventory(dto.getReserveInventory());
		log.setWarnInventory(dto.getWarnInventory());
		log.setReject(0);
		log.setSample(0);
		log.setFreeze(0);
		log.setWarehouseId(dto.getWarehouseId());
		log.setWarehouseName(dto.getWarehouseName());
		log.setSpId(dto.getSpId());
		log.setDistrictId(dto.getDistrictId());
		log.setChangeType(changeType);
		log.setRemark(remark);
		log.setCreateTime(new Date());
		log.setCreateUser(AUTHOR_TYPE.SYSTEM);
		return log;
	}
	
	private InventoryLog initInventoryLog(Inventory updatedInventory, String sku, InputAndOutputType type, Integer inventory){
		InventoryLog inventoryLogObj = new InventoryLog();
		inventoryLogObj.setSku(sku);
		inventoryLogObj.setSkuCount(inventory);
		inventoryLogObj.setType(type.getCode());
		inventoryLogObj.setDistrictId(updatedInventory.getDistrictId());
		inventoryLogObj.setSupplierId(updatedInventory.getSpId());
		inventoryLogObj.setWarehouseId(updatedInventory.getWarehouseId());
		inventoryLogObj.setInventory(updatedInventory.getInventory());
		return inventoryLogObj;
	}
	
	private InventoryDistribute initDistributeInventory(Inventory inventory, App app, String bizId, String sku, int inventoryCount){
		InventoryDistribute distributeObj = new InventoryDistribute();
		distributeObj.setCreateTime(new Date());
		distributeObj.setApp(app.getName());
		distributeObj.setBizId(bizId);
		distributeObj.setInventoryId(inventory.getId());
		distributeObj.setSku(sku);
		distributeObj.setOccupy(0);
		distributeObj.setDistrictId(inventory.getDistrictId());
		distributeObj.setWarehouseId(inventory.getWarehouseId());
		distributeObj.setBacked(DistributeBackedStatus.unbacked.getStatus());
		distributeObj.setInventory(inventoryCount);
		distributeObj.setBizInventory(inventoryCount);
		distributeObj.setModifyTime(new Date());
		return distributeObj;
	}
	
	private InventoryDistribute getDistributeInventory(List<InventoryDistribute> inventoryDistributes,
			String appName, String bizId, Long warehouseId, String sku){
		for(InventoryDistribute distribute : inventoryDistributes){
			if(distribute.getApp().equals(appName) && distribute.getBizId().equals(bizId) &&
					distribute.getWarehouseId().equals(warehouseId) && distribute.getSku().equals(sku)){
				return distribute;
			}
		}
		return null;
	}
	private void setDistributeInventory(List<InventoryDistribute> distributeList, InventoryDistribute obj){
		for(int i = 0; i < distributeList.size(); i++){
			if(distributeList.get(i).getId().equals(obj.getId())){
				distributeList.set(i, obj);
			}
		}
	}
	
	/* 库存归还 */
	private ResultInfo<Boolean> backRequestInventory(App app, String bizId, String sku, Long warehouseId){
		Map<String, Object> params = new HashMap<>();
		params.put("app", app.getName());
		params.put("bizId", bizId);
		params.put("sku", sku);
		params.put("warehouseId",warehouseId);
		List<InventoryDistribute> distributeObjs = inventoryDistributeService.queryByParamNotEmpty(params);
		if(CollectionUtils.isEmpty(distributeObjs)){
			return new ResultInfo<>(new FailInfo("还库失败，库存申请记录不存在"));
		}
		distributeObjs.forEach(new Consumer<InventoryDistribute>() {
			@Override
			public void accept(InventoryDistribute t) {
				backRequestInventory(t);
			}
		});
		return new ResultInfo<>(Boolean.TRUE);
	}
	/* 库存归还 */
	private void backRequestInventory(InventoryDistribute distribute){
		Integer remainInventory = distribute.getInventory();
		Integer occupy = distribute.getOccupy();
		Integer bizInventory = distribute.getBizInventory();
		if(remainInventory >= 0 && DistributeBackedStatus.unbacked.getStatus().equals(distribute.getBacked())){
			inventoryDistributeService.backDistributeInventory(distribute);//更新预占库存状态
			Inventory inventoryObj = inventoryService.getInventoryById(distribute.getInventoryId());
			setDistributeInventory(inventoryObj.getInventoryDistributes(), distribute);
			if(remainInventory > 0){ //剩余库存增加到总库存
				inventoryService.increaseRealInventoryById(inventoryObj.getId(), remainInventory);
				inventoryObj.setInventory(inventoryObj.getInventory() + remainInventory);
				inventoryLogService.insert(initInventoryLog(inventoryObj, distribute.getSku(), InputAndOutputType.PH, remainInventory));	
			}
			String logRemark = String.format("活动[%s]商品[%s]退回库存[%d]订单占用库存[%d]核销库存[%d]", 
					distribute.getBizId(),distribute.getSku(),remainInventory,occupy, bizInventory - remainInventory - occupy);
			inventoryManageLogDao.insert(initInventoryManageLog(inventoryService.getInventoryDetailInfo(inventoryObj), 2, logRemark));
		}
	}
	private InventoryOccupy initInventoryOccupy(App app, String bizId, int inventory, 
			String orderCode, String sku, Long distributeId, Integer occupyDistribute, Long inventoryId){
		InventoryOccupy inventoryOccupyObj = new InventoryOccupy();
		inventoryOccupyObj.setApp(app.getName());
		inventoryOccupyObj.setBizId(bizId);
		inventoryOccupyObj.setCreateTime(new Date());
		inventoryOccupyObj.setInventory(inventory);
		inventoryOccupyObj.setOrderNo(orderCode);
		inventoryOccupyObj.setSku(sku);
		inventoryOccupyObj.setInventoryDistId(distributeId);
		inventoryOccupyObj.setInventoryId(inventoryId);
		inventoryOccupyObj.setOccupyDistribute(occupyDistribute);
		return inventoryOccupyObj;
	}
	
	//取消库存占用
	private ResultInfo<Boolean> unoccupyInventory(InventoryOccupy occupy){
		if(DEFAULTED.YES.equals(occupy.getOccupyDistribute())){ /* （取消订单）解冻预占库存记录中的库存 */
			InventoryDistribute distributeObj = inventoryDistributeService.queryById(occupy.getInventoryDistId());
			if (null == distributeObj) {
				return new ResultInfo<>(new FailInfo("预占库存记录不存在"));
			}
			if (DistributeBackedStatus.unbacked.getStatus().equals(distributeObj.getBacked())) {
				inventoryDistributeService.thawDistInventory(distributeObj.getId(), occupy.getInventory());
			}else{ //已归还预占记录，需把库存归还给总库存
				Inventory inventoryObj = inventoryService.queryById(distributeObj.getInventoryId());
				inventoryService.increaseRealInventoryById(distributeObj.getInventoryId(), occupy.getInventory());
				inventoryDistributeService.reduceOccupyDistributeById(distributeObj.getId(), occupy.getInventory());
				inventoryObj.setInventory(inventoryObj.getInventory() + occupy.getInventory());
				InventoryLog log = initInventoryLog(inventoryObj, occupy.getSku(), InputAndOutputType.QX, occupy.getInventory());
				inventoryLogService.insert(log); //日志
			}
			return new ResultInfo<>(Boolean.TRUE);
		}else{ /* （取消订单）解冻总库存记录中的库存 */
			Inventory inventoryObj = inventoryService.queryById(occupy.getInventoryId());
			if(inventoryObj == null){
				return new ResultInfo<>(new FailInfo("库存信息不存在"));
			}
			inventoryService.thawOccupyInventoryById(inventoryObj.getId(), occupy.getInventory());
			inventoryObj.setInventory(inventoryObj.getInventory() + occupy.getInventory());
			InventoryLog log = initInventoryLog(inventoryObj, occupy.getSku(), InputAndOutputType.QX, occupy.getInventory());
			inventoryLogService.insert(log); //日志
			return new ResultInfo<>(Boolean.TRUE);	
		}	
	}
	@Override
	public Integer updateInventory(Inventory inventory) {
		int count = 0;
		Integer totalInventory = inventory.getInventory();// 总库存数
		Integer occupy = inventory.getOccupy();// 活动冻结库存
		Integer reserveInventory = inventory.getReserveInventory(); // 预留库存数
		inventory.setOccupy(null);
		if(totalInventory>=(occupy+reserveInventory)){
			inventory.setInventory(totalInventory-occupy); // 可售库存+预留库存
			count = inventoryService.updateNotNullById(inventory);
		}
		
		return count;
	}

	/**
	 * 批量导入库存
	 * @param inventoryList 库存列表
	 * @param inventoryManageLogList 日志列表
	 * @return
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public int importInventory(List<Inventory> inventoryList,List<InventoryManageLog> inventoryManageLogList) {
		int size = inventoryList.size();
		int size2 = inventoryManageLogList.size();
		if(size==size2){
			for(int i=0;i<size;i++){
				Inventory inventory = inventoryList.get(i);
				InventoryManageLog inventoryManageLog = inventoryManageLogList.get(i);
				// 保存库存
				Inventory insert = inventoryService.insert(inventory);
				// 保存库存日志
				inventoryManageLog.setInventoryId(insert.getId());
				inventoryManageLogDao.insert(inventoryManageLog);
			}
			return 1;
		}else{
			return 0;
		}
	}
	
	/**
	 * 批量修改库存
	 * @param inventoryList 库存列表
	 * @param inventoryManageLogList 日志列表
	 * @return
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public int importUpdateInventory(List<Inventory> inventoryList,List<InventoryManageLog> inventoryManageLogList) {
		int size = inventoryList.size();
		int size2 = inventoryManageLogList.size();
		if(size==size2){
			for(int i=0;i<size;i++){
				Inventory inventory = inventoryList.get(i);
				InventoryManageLog inventoryManageLog = inventoryManageLogList.get(i);
				// 保存库存y
				inventoryService.updateNotNullById(inventory);
				// 保存库存日志
				inventoryManageLog.setInventoryId(inventory.getId());
				inventoryManageLogDao.insert(inventoryManageLog);
			}
			return 1;
		}else{
			return 0;
		}
	}
	
	
//	
//	private Logger logger = LoggerFactory.getLogger(InventoryOperService.class);
//
//	@Autowired
//	private IInventoryService inventoryService;
//	@Autowired
//	private IInventoryDistributeService inventoryDistributeService;
//	@Autowired
//	private IWarehouseService warehouseService;
//	@Autowired
//	private IInventoryOccupyService inventoryOccupyService;
//
//	@Value("#{meta['default.warehouse.code']}")
//	private String defaultWarehouseCode;
//
//	@Autowired
//	private IInventoryCacheService inventoryCacheService;
//	@Autowired
//	private IInventoryLogService inventoryLogService;
//
//	@Override
//	@Transactional(propagation = Propagation.REQUIRED)
//	public ResultInfo<String> requestInventory(App app, String bizId, String sku, int inventory, Long spId,
//			Long warehouseId) {
//
//		if (StringUtils.isBlank(bizId) || StringUtils.isBlank(sku) || spId == null || app == null || inventory < 1) {
//			return new ResultInfo<String>(new FailInfo("调用错误，参数不正确", 3));
//		}
//		if (inventory < 1) {
//			return new ResultInfo<String>(new FailInfo("申请库存数量不能小于1", 3));
//		}
//		Warehouse warehouse = warehouseService.queryById(warehouseId);
//		if (warehouse == null) {
//			return new ResultInfo<>(new FailInfo("仓库信息不存在"));
//		}
//		try {
//			if (warehouse.checkPutStorage()) { //需要推送WMS的仓库不允许随便分配
//				// 对接WMS
//				return requestSelfInventory(app, bizId, sku, inventory, spId, warehouseId);
//			} else {
//				// 未对接WMS
//				return requestDealersInventory(app, bizId, sku, inventory, spId, warehouseId);
//			}
//		} catch (Exception e) {
//			logger.error("分配库存失败，app={} bizId={} sku={} message={}", app, bizId, sku, e.getMessage());
//			return new ResultInfo<String>(new FailInfo(new Exception(
//					"分配库存失败，app=" + app + " bizId=" + bizId + " sku=" + sku + " message=" + e.getMessage())));
//		}
//	}
//
//	/**
//	 * 自营商品仓库
//	 * 
//	 * @param app
//	 * @param bizId
//	 * @param sku
//	 * @param inventory
//	 * @param spId
//	 * @param warehouseId
//	 * @return
//	 */
//	private ResultInfo<String> requestSelfInventory(App app, String bizId, String sku, int inventory, Long spId,
//			Long warehouseId) {
//		if (null == warehouseId) {
//			return new ResultInfo<String>(new FailInfo("调用错误，参数不正确", 3));
//		}
//
//		List<Inventory> inventorys = selectInventoryInfo(sku, spId, warehouseId);
//		if (CollectionUtils.isEmpty(inventorys)) {
//			return new ResultInfo<String>(new FailInfo("库存不足，剩余库存数" + 0, 1));
//		}
//		logger.info(" 进入活动库存分配   app:{},bizId:{},sku:{},inventory:{},inventoryObject:{}",app, bizId, sku, inventory, JSON.toJSONString(inventorys.get(0)));
//		return doDistributeInventory(app, bizId, sku, inventory, inventorys.get(0));
//	}
//
//	/**
//	 * 执行库存分配
//	 * 
//	 * @param app
//	 * @param bizId
//	 * @param sku
//	 * @param inventory
//	 * @param inventoryObj
//	 * @return
//	 * @throws DAOException
//	 */
//	private ResultInfo<String> doDistributeInventory(App app, String bizId, String sku, int inventory,
//			Inventory inventoryObj) {
//		// 现货库存
//		Integer totalNSInventory = inventoryObj.getInventory();
//		// 占用库存
//		Integer totalZSInventory = inventoryObj.getOccupy();
//
//		// 获得该仓库已经分配出去的库存信息
//		Map<String, Object> params = new HashMap<>();
//		params.put("inventoryId", inventoryObj.getId());
//		List<InventoryDistribute> hasDistributes = inventoryDistributeService.queryByParamNotEmpty(params);
//		// 已分配库存
//		Integer totalPSInventory = 0;
//		InventoryDistribute distributeObj = null;
//		if (CollectionUtils.isNotEmpty(hasDistributes)) {
//			for (InventoryDistribute ivdObj : hasDistributes) {
//				totalPSInventory += ivdObj.getInventory();
//				if (ivdObj.getApp().equals(app.getName()) && ivdObj.getBizId().equals(bizId)
//						&& ivdObj.getSku().equals(sku)
//						&& ivdObj.getWarehouseId().longValue() == inventoryObj.getWarehouseId().longValue()) {
//					distributeObj = ivdObj;
//				}
//			}
//		}
//
//		Integer leftInventory = totalNSInventory - totalZSInventory - totalPSInventory;
//		// 验证剩余库存是否足够分配
//		if (leftInventory.intValue() < inventory) {
//			leftInventory = (leftInventory <= 0 ? 0 : leftInventory.intValue());
//			return new ResultInfo<String>(new FailInfo("库存不足，剩余库存数" + leftInventory));
//		}
//		// 分配或增加库存
//		Date now = new Date();
//		if (null == distributeObj) {
//			distributeObj = new InventoryDistribute();
//			distributeObj.setCreateTime(now);
//			distributeObj.setApp(app.getName());
//			distributeObj.setBizId(bizId);
//			distributeObj.setInventoryId(inventoryObj.getId());
//			distributeObj.setSku(sku);
//			distributeObj.setOccupy(0);
//			distributeObj.setDistrictId(inventoryObj.getDistrictId());
//			distributeObj.setWarehouseId(inventoryObj.getWarehouseId());
//			distributeObj.setBacked(0);
//			distributeObj.setInventory(inventory);
//			distributeObj.setModifyTime(now);
//			logger.info("分配库存=" + inventory + "=" + app.getName() + "=" + bizId + "=" + sku);
//			// 插入
//			inventoryDistributeService.insert(distributeObj);
//		} else {
//			// 已分配过库存，增加库存
//			logger.info("增加分配库存数量=" + inventory + "=" + app.getName() + "=" + bizId + "=" + sku);
//			// 更新
//			inventoryDistributeService.increaseInventoryDistributeById(distributeObj.getId(), inventory);
//			if (distributeObj.getBacked() == DistributeBackedStatus.backed.getStatus()) {
//				// 为某个活动重新分配库存，则更改还库存状态为未还库
//				inventoryDistributeService.updateBackedStatus(distributeObj.getId(),
//						DistributeBackedStatus.unbacked.getStatus());
//			}
//		}
//		// 清除库存缓存
//		inventoryCacheService.removeInventoryCache(app, sku, bizId);
//		return new ResultInfo<String>("分配成功");
//	}
//
//	/**
//	 * 商家商品仓库
//	 * 
//	 * @param app
//	 * @param bizId
//	 * @param sku
//	 * @param inventory
//	 * @param spId
//	 * @param warehouseId
//	 * @return
//	 */
//	private ResultInfo<String> requestDealersInventory(App app, String bizId, String sku, int inventory, Long spId,
//			Long warehouseId) {
//		Warehouse warehouseObj = null;
//		Map<String, Object> params = new HashMap<>();
//		if (null == warehouseId) {
//			params.put("spId", spId);
//			List<Warehouse> warehouseObjs = warehouseService.queryByParamNotEmpty(params);
//			if (CollectionUtils.isEmpty(warehouseObjs)) {
//				return new ResultInfo<String>(new FailInfo("分配库存失败，商家没有仓库信息", 4));
//			}
//			warehouseObj = warehouseObjs.get(0);
//			warehouseId = warehouseObj.getId();
//		} else {
//			warehouseObj = warehouseService.queryById(warehouseId);
//		}
//		if (null == warehouseObj) {
//			logger.error("查询商家库存为空，spid={} warehouseId={}", spId, warehouseId);
//			return new ResultInfo<String>(new FailInfo("分配库存失败，商家没有仓库信息", 4));
//		}
//		Date now = new Date();
//		// 获得剩余库存信息
//		List<Inventory> inventoryObjs = selectInventoryInfo(sku, spId, warehouseId);
//		Inventory inventoryObj = null;
//		InventoryLog inventoryLogObj = new InventoryLog();
//		Integer realInventory = null;
//		//商家虚拟
//		if (CollectionUtils.isEmpty(inventoryObjs)) {
//			// 执行入库
//			inventoryObj = new Inventory();
//			inventoryObj.setCreateTime(now);
//			inventoryObj.setDistrictId(warehouseObj.getDistrictId());
//			inventoryObj.setInventory(inventory);
//			inventoryObj.setModifyTime(now);
//			inventoryObj.setOccupy(0);
//			inventoryObj.setReject(0);
//			inventoryObj.setSku(sku);
//			inventoryObj.setSpId(spId);
//			inventoryObj.setWarehouseId(warehouseId);
//			inventoryService.insert(inventoryObj);
//			realInventory = inventory;
//		} else {
//			// 执行加库
//			inventoryObj = inventoryObjs.get(0);
//			realInventory = inventoryObj.getInventory().intValue() + inventory;
//			inventoryService.increaseRealInventoryById(inventoryObj.getId(), inventory);
//			inventoryObj.setInventory(realInventory);
//		}
//
//		inventoryLogObj.setWhCode(warehouseObj.getCode());
//		inventoryLogObj.setSku(inventoryObj.getSku());
//		inventoryLogObj.setSkuCount(inventory);
//		inventoryLogObj.setType(InputAndOutputType.PO.getCode());
//		inventoryLogObj.setDistrictId(warehouseObj.getDistrictId());
//		inventoryLogObj.setSupplierId(inventoryObj.getSpId());
//		inventoryLogObj.setWarehouseId(warehouseObj.getId());
//		inventoryLogObj.setInventory(realInventory);
//		inventoryLogService.insert(inventoryLogObj);
//		logger.info(" 进入活动库存分配   app:{},bizId:{},sku:{},inventory:{},inventoryObject:{}",app, bizId, sku, inventory, JSON.toJSONString(inventoryObj));
//		return doDistributeInventory(app, bizId, sku, inventory, inventoryObj);
//	}
//
//	/**
//	 * 根据sku、商家id、仓库获得 获得商家的某个商品的库存信息，
//	 * 
//	 * @param sku
//	 * @param spId
//	 * @param warehouseId
//	 * @return
//	 */
//	@Override
//	public List<Inventory> selectInventoryInfo(String sku, Long spId, Long warehouseId) {
//		Map<String, Object> params = new HashMap<>();
//		params.put("sku", sku);
//		params.put("spId", spId);
//		params.put("warehouseId", warehouseId);
//		return inventoryService.queryByParamNotEmpty(params);
//	}
//
//	@Override
//	@Transactional(propagation = Propagation.REQUIRED)
//	public ResultInfo<String> backRequestInventory(App app, String bizId) {
//		if (StringUtils.isBlank(bizId) || null == app) {
//			return new ResultInfo<String>(new FailInfo("调用错误，参数不正确", 3));
//		}
//		Map<String, Object> params = new HashMap<>();
//		try {
//			params.put("app", app.getName());
//			params.put("bizId", bizId);
//			List<InventoryDistribute> distributeObjs = inventoryDistributeService.queryByParamNotEmpty(params);
//			if (CollectionUtils.isNotEmpty(distributeObjs)) {
//				List<String> skuList = new ArrayList<String>();
//				for (InventoryDistribute inventoryDistributeObj : distributeObjs) {
//					int leftInvetory = inventoryDistributeObj.getInventory();
//					if (leftInvetory == 0 && inventoryDistributeObj.getBacked() == 1) {
//						continue;
//					}
//					String sku = inventoryDistributeObj.getSku();
//					inventoryDistributeObj.setInventory(0);
//					inventoryDistributeObj.setBacked(1);
//					inventoryDistributeObj.setModifyTime(new Date());
//					inventoryDistributeService.updateById(inventoryDistributeObj);
//
//					logger.info("商家归还库存=" + leftInvetory + "=" + app.getName() + "=" + bizId + "=" + sku);
//
//					if (leftInvetory == 0) {
//						continue;
//					}
//
//					Inventory inventoryObj = inventoryService.queryById(inventoryDistributeObj.getInventoryId());
//					Warehouse warehouseObj = warehouseService.queryById(inventoryObj.getWarehouseId());
//					if (null != inventoryObj && !warehouseObj.checkPutStorage()) { //不需要推送WMS的虚拟库存
//						// 虚拟库存，减去未销售的数量
//						logger.info("商家归还库存减去现货库存=" + leftInvetory + " app=" + app.getName() + " warehouseId="
//								+ inventoryObj.getWarehouseId() + " sku=" + inventoryObj.getSku());
//						inventoryService.reduceRealInventory(inventoryObj.getSku(), inventoryObj.getSpId(),
//								inventoryObj.getWarehouseId(), leftInvetory);
//						// 记录还库日志						
//						InventoryLog inventoryLogObj = new InventoryLog();
//						inventoryLogObj.setWhCode(warehouseObj.getCode());
//						inventoryLogObj.setSku(sku);
//						inventoryLogObj.setSkuCount(leftInvetory);
//						inventoryLogObj.setType(InputAndOutputType.PH.getCode());
//						inventoryLogObj.setDistrictId(warehouseObj.getDistrictId());
//						inventoryLogObj.setSupplierId(inventoryObj.getSpId());
//						inventoryLogObj.setWarehouseId(warehouseObj.getId());
//						inventoryLogObj.setInventory(inventoryObj.getInventory() - leftInvetory);
//						inventoryLogService.insert(inventoryLogObj);
//					}
//					skuList.add(sku);
//				}
//				// 清除库存缓存
//				inventoryCacheService.batchRemoveInventoryCache(skuList, app, bizId);
//				return new ResultInfo<String>("还库成功");
//			}
//			return new ResultInfo<String>(new FailInfo("还库失败，该活动未申请库存", 1));
//
//		} catch (Exception e) {
//			logger.error("归还库存失败 app={} bizId={} message={}", app, bizId, e.getMessage());
//			return new ResultInfo<String>(new FailInfo(
//					new Exception("归还库存失败 app=" + app + " bizId=" + bizId + " message=" + e.getMessage())));
//		}
//	}
//
//	@Override
//	@Transactional(propagation = Propagation.REQUIRED)
//	public ResultInfo<String> backRequestInventoryBySku(App app, String bizId, String sku) {
//		if (StringUtils.isBlank(bizId) || StringUtils.isBlank(sku) || app == null) {
//			return new ResultInfo<String>(new FailInfo("调用错误，参数不正确", 3));
//		}
//		Map<String, Object> params = new HashMap<>();
//		params.put("app", app.getName());
//		params.put("bizId", bizId);
//		params.put("sku", sku);
//		return getStringResultInfo(app, bizId, sku, params);
//	}
//
//	@Override
//	@Transactional(propagation = Propagation.REQUIRED)
//	public ResultInfo<String> backRequestInventoryBySkuAndWarehouse(App app, String bizId, String sku,Long warehouseId) {
//		if (StringUtils.isBlank(bizId) || StringUtils.isBlank(sku) || app == null) {
//			return new ResultInfo<String>(new FailInfo("调用错误，参数不正确", 3));
//		}
//		Map<String, Object> params = new HashMap<>();
//		params.put("app", app.getName());
//		params.put("bizId", bizId);
//		params.put("sku", sku);
//		params.put("warehouseId",warehouseId);
//		return getStringResultInfo(app, bizId, sku, params);
//	}
//
//	private ResultInfo<String> getStringResultInfo(App app, String bizId, String sku, Map<String, Object> params) {
//		try {
//			List<InventoryDistribute> distributeObjs = inventoryDistributeService.queryByParamNotEmpty(params);
//			if (CollectionUtils.isNotEmpty(distributeObjs)) {
//				List<String> skuList = new ArrayList<String>();
//				for (InventoryDistribute inventoryDistributeObj : distributeObjs) {
//					int leftInvetory = inventoryDistributeObj.getInventory();
//					inventoryDistributeObj.setInventory(0);
//					inventoryDistributeObj.setBacked(1);
//					inventoryDistributeObj.setModifyTime(new Date());
//					inventoryDistributeService.updateById(inventoryDistributeObj);
//					logger.info("商家归还库存=" + leftInvetory + "=" + app.getName() + "=" + bizId + "="
//							+ inventoryDistributeObj.getSku());
//					Inventory inventoryObj = inventoryService.queryById(inventoryDistributeObj.getInventoryId());
//					Warehouse warehouseObj = warehouseService.queryById(inventoryObj.getWarehouseId());
//					if (null != inventoryObj && !warehouseObj.checkPutStorage()) { //不需要推送WMS的虚拟库存
//						logger.info("商家归还库存减去现货库存=" + leftInvetory + " spp=" + app.getName() + " warehouseId="
//								+ inventoryObj.getWarehouseId() + " sku=" + inventoryObj.getSku());
//						// 商家虚拟库存，减去未销售的数量
//						inventoryService.reduceRealInventory(inventoryObj.getSku(), inventoryObj.getSpId(),
//								inventoryObj.getWarehouseId(), leftInvetory);
//						// 记录还库日志
//						InventoryLog inventoryLogObj = new InventoryLog();
//						inventoryLogObj.setWhCode(warehouseObj.getCode());
//						inventoryLogObj.setSku(sku);
//						inventoryLogObj.setSkuCount(leftInvetory);
//						inventoryLogObj.setType(InputAndOutputType.PH.getCode());
//						inventoryLogObj.setDistrictId(warehouseObj.getDistrictId());
//						inventoryLogObj.setSupplierId(inventoryObj.getSpId());
//						inventoryLogObj.setWarehouseId(warehouseObj.getId());
//						inventoryLogObj.setInventory(inventoryObj.getInventory() - leftInvetory);
//						inventoryLogService.insert(inventoryLogObj);
//					}
//					skuList.add(sku);
//				}
//				// 清除库存缓存
//				inventoryCacheService.batchRemoveInventoryCache(skuList, app, bizId);
//				return new ResultInfo<String>("还库成功");
//			}
//			return new ResultInfo<String>(new FailInfo("还库失败，该商品未申请库存", 1));
//
//		} catch (Exception e) {
//			logger.error("归还库存失败 app={} bizId={} sku={} message={}", app, bizId, sku, e.getMessage());
//			return new ResultInfo<String>(new FailInfo(new Exception(
//					"归还库存失败 app=" + app + " sku=" + sku + " bizId=" + bizId + " message=" + e.getMessage())));
//		}
//	}
//
//	@Override
//	public ResultInfo<String> backRequestSpecInventoryBySku(App app, String bizId, String sku, int invetory) {
//		if (StringUtils.isBlank(bizId) || StringUtils.isBlank(sku) || app == null || invetory < 1) {
//			return new ResultInfo<String>(new FailInfo("调用错误，参数不正确", 3));
//		}
//		Map<String, Object> params = new HashMap<>();
//		params.put("app", app.getName());
//		params.put("bizId", bizId);
//		params.put("sku", sku);
//		try {
//			List<InventoryDistribute> distributeObjs = inventoryDistributeService.queryByParamNotEmpty(params);
//			if (CollectionUtils.isNotEmpty(distributeObjs)) {
//				InventoryDistribute inventoryDistributeObj = distributeObjs.get(0);
//				int leftInvetory = inventoryDistributeObj.getInventory();
//				if (leftInvetory < invetory) {
//					return new ResultInfo<String>(new FailInfo("退还库存大于当前剩余库存", 3));
//				}
//				if (leftInvetory == invetory) {
//					// 全部退还了
//					return backRequestInventoryBySku(app, bizId, sku);
//				}
//
//				inventoryDistributeService.reduceInventoryDistributeById(inventoryDistributeObj.getId(), invetory);
//
//				logger.info("商家归还库存=" + leftInvetory + "=" + app.getName() + "=" + bizId + "="
//						+ inventoryDistributeObj.getSku() + " 归还数量invetory=" + invetory);
//				Inventory inventoryObj = inventoryService.queryById(inventoryDistributeObj.getInventoryId());
//				Warehouse warehouseObj = warehouseService.queryById(inventoryObj.getWarehouseId());
//				if (null != inventoryObj && !warehouseObj.checkPutStorage()) {
//					logger.info("商家归还库存减去现货库存=" + leftInvetory + " app=" + app.getName() + " warehouseId="
//							+ inventoryObj.getWarehouseId() + " sku=" + inventoryObj.getSku() + "  归还数量invetory="
//							+ invetory);
//					// 商家虚拟库存，减去未销售的数量
//					inventoryService.reduceRealInventory(inventoryObj.getSku(), inventoryObj.getSpId(),
//							inventoryObj.getWarehouseId(), invetory);
//					// 记录还库日志					
//					InventoryLog inventoryLogObj = new InventoryLog();
//					inventoryLogObj.setWhCode(warehouseObj.getCode());
//					inventoryLogObj.setSku(sku);
//					inventoryLogObj.setSkuCount(invetory);
//					inventoryLogObj.setType(InputAndOutputType.PH.getCode());
//					inventoryLogObj.setDistrictId(warehouseObj.getDistrictId());
//					inventoryLogObj.setSupplierId(inventoryObj.getSpId());
//					inventoryLogObj.setWarehouseId(warehouseObj.getId());
//					inventoryLogObj.setInventory(inventoryObj.getInventory() - invetory);
//					inventoryLogService.insert(inventoryLogObj);
//				}
//				// 清除库存缓存
//				inventoryCacheService.removeInventoryCache(app, sku, bizId);
//				return new ResultInfo<String>("还库成功");
//			}
//			return new ResultInfo<String>(new FailInfo("还库失败，该商品未申请库存", 1));
//		} catch (Exception e) {
//			logger.error("归还库存失败 app={} bizId={} sku={} message={}", app, bizId, sku, e.getMessage());
//			return new ResultInfo<String>(new FailInfo(new Exception(
//					"归还库存失败 app=" + app + " sku=" + sku + " bizId=" + bizId + " message=" + e.getMessage())));
//		}
//	}
//
//	@Override
//	@Transactional(propagation = Propagation.REQUIRED)
//	public ResultInfo<String> occupyInventory(App app, String bizId, String sku, int inventory, String orderCode) {
//		if (StringUtils.isBlank(bizId) || StringUtils.isBlank(sku) || app == null || inventory < 1) {
//			return new ResultInfo<String>(new FailInfo("调用错误，参数不正确", 3));
//		}
//		try {
//			// 获得该业务已经分配的库存信息
//			InventoryDistribute inventoryDistributeObj = new InventoryDistribute();
//			inventoryDistributeObj.setApp(app.getName());
//			inventoryDistributeObj.setBizId(bizId);
//			inventoryDistributeObj.setSku(sku);
//
//			Map<String, Object> params = new HashMap<>();
//			params.put("app", app.getName());
//			params.put("bizId", bizId);
//			params.put("sku", sku);
//			params.put("backed", 0);//是否归还库存为未归还
//			List<InventoryDistribute> distributeObjs = inventoryDistributeService.queryByParamNotEmpty(params);
//			Integer totalPSInventory = 0;
//			InventoryDistribute distributeObj = null;
//			if (CollectionUtils.isNotEmpty(distributeObjs)) {
//				distributeObj = distributeObjs.get(0);
//				totalPSInventory = distributeObj.getInventory();
//			}
//
//			// 分配库存
//			Integer leftInventory = totalPSInventory - inventory;
//			if (leftInventory.intValue() < 0) {
//				return new ResultInfo<String>(new FailInfo("库存不足，剩余库存数" + totalPSInventory, 1));
//			}
//			Date now = new Date();
//
//			int result = inventoryDistributeService.forzenDistInventory(distributeObj.getId(), inventory);
//
//			if (result != 1) {
//				// 未更新掉任何库存，返回库存不足
//				return new ResultInfo<String>(new FailInfo("库存不足，剩余库存数" + totalPSInventory, 1));
//			}
//
//			inventoryService.frozenOccupyInventoryById(distributeObj.getInventoryId(), inventory);
//
//			// 多次冻结会有多条记录
//			InventoryOccupy inventoryOccupyObj = new InventoryOccupy();
//			inventoryOccupyObj.setApp(app.getName());
//			inventoryOccupyObj.setBizId(bizId);
//			inventoryOccupyObj.setCreateTime(now);
//			inventoryOccupyObj.setInventory(inventory);
//			inventoryOccupyObj.setOrderNo(orderCode);
//			inventoryOccupyObj.setSku(sku);
//			inventoryOccupyObj.setInventoryDistId(distributeObj.getId());
//			inventoryOccupyService.insert(inventoryOccupyObj);
//
//			logger.info("冻结库存成功 frozenOccupyInventoryById >>>>>> order={} sku={} biz={} inventory={}", orderCode, sku,
//					bizId, inventory);
//			// 清除库存缓存
//			inventoryCacheService.removeInventoryCache(app, sku, bizId);
//			return new ResultInfo<String>("占用库存成功");
//		} catch (Exception e) {
//			logger.error(e.getMessage());
//			throw e;
//			//return new ResultInfo<String>(new FailInfo(e));
//		}
//
//	}
//
//	@Override
//	@Transactional(propagation = Propagation.REQUIRED)
//	public ResultInfo<String> unoccupyInventory(Long orderCode) {
//		if (null == orderCode) {
//			return new ResultInfo<String>(new FailInfo("调用错误，参数不正确", 3));
//		}
//		Map<String, Object> params = new HashMap<>();
//		params.put("orderNo", orderCode);
//		try {
//			List<InventoryOccupy> inventoryOccupyObjs = inventoryOccupyService.queryByParamNotEmpty(params);
//			if (CollectionUtils.isEmpty(inventoryOccupyObjs)) {
//				return new ResultInfo<String>(new FailInfo("取消失败，冻结记录未找到", 1));
//			}
//			List<Long> occupyIds = new ArrayList<Long>();
//			Integer totalCancelInventory = 0;
//			for (InventoryOccupy item : inventoryOccupyObjs) {
//				totalCancelInventory = item.getInventory();
//
//				String bizId = item.getBizId();
//				String sku = item.getSku();
//				Long inventoryDistId = item.getInventoryDistId();
//
//				InventoryDistribute distributeObj = inventoryDistributeService.queryById(inventoryDistId);
//
//				if (null == distributeObj) {
//					logger.error("取消失败，冻结记录未找到  orderno:" + item.getOrderNo() + " sku:" + sku + " totalCancelInventory="
//							+ totalCancelInventory);
//					continue;
//				}
//
//				Long inventoryId = distributeObj.getInventoryId();
//
//				if (distributeObj.getBacked() == 0) {
//					// 活动已结束 并未归还库存
//					logger.info("自营订单 取消订单时，解冻库存，减少商品活动库存的冻结库存数量，增加可销售库存数量  orderno:" + item.getOrderNo() + " sku:"
//							+ sku + " occupy:" + distributeObj.getOccupy() + " totalCancelInventory="
//							+ totalCancelInventory);
//					inventoryDistributeService.thawDistInventory(inventoryDistId, totalCancelInventory);
//				}
//				logger.info("取消订单，解冻占用库存  orderno:" + item.getOrderNo() + " sku:" + sku + " occupy:"
//						+ distributeObj.getOccupy() + " totalCancelInventory=" + totalCancelInventory);
//				inventoryService.thawOccupyInventoryById(inventoryId, totalCancelInventory);
//
//				occupyIds.add(item.getId());
//				// 清除库存缓存
//				inventoryCacheService.removeInventoryCache(App.PROMOTION, sku, bizId);
//			}
//			if (occupyIds.size() > 0) {
//				params.clear();
//				params.put(MYBATIS_SPECIAL_STRING.COLUMNS.name(),
//						" id in(" + StringUtil.join(occupyIds, Constant.SPLIT_SIGN.COMMA) + ")");
//				inventoryOccupyService.deleteByParamNotEmpty(params);
//			}
//			return new ResultInfo<String>("解冻库存成功");
//		} catch (Exception e) {
//			logger.error(e.getMessage());
//			return new ResultInfo<String>(new FailInfo(e));
//		}
//	}
//
//	@Override
//	@Transactional(propagation = Propagation.REQUIRED)
//	public Map<String, ResultInfo<String>> batchOccupyInventory(List<OccupyInventoryDto> inventoryDtos) {
//		if (CollectionUtils.isEmpty(inventoryDtos)) {
//			return null;
//		}
//		Map<String, ResultInfo<String>> result = new HashMap<String, ResultInfo<String>>();
//		ResultInfo<String> message = null;
//		for (OccupyInventoryDto occupyInventoryDto : inventoryDtos) {
//			try {
//				message = occupyInventory(occupyInventoryDto.getApp(), occupyInventoryDto.getBizId(),
//						occupyInventoryDto.getSku(), occupyInventoryDto.getInventory(),
//						"" + occupyInventoryDto.getOrderCode());
//				result.put(occupyInventoryDto.getSku(), message);
//			} catch (Exception e) {
//				logger.error(e.getMessage());
//				result.put(occupyInventoryDto.getSku(), new ResultInfo<String>(new FailInfo(e)));
//			}
//		}
//		return result;
//	}
//
//	@Override
//	@Transactional(propagation = Propagation.REQUIRED)
//	public ResultInfo<String> increaseRealInventory(String sku, Long spId, Long warehouseId, Integer quantity) {
//		if (StringUtils.isBlank(sku) || spId == null || quantity == null) {
//			return new ResultInfo<String>(new FailInfo("调用错误，参数不能为空", 3));
//		}
//		if (null == warehouseId) {
//			Map<String, Object> params = new HashMap<>();
//			params.put("code", defaultWarehouseCode);
//			// 获得仓库信息
//			List<Warehouse> warehouses = warehouseService.queryByParamNotEmpty(params);
//			if (CollectionUtils.isEmpty(warehouses)) {
//				return new ResultInfo<String>(new FailInfo("调用错误，参数不能为空", 3));
//			}
//			warehouseId = warehouses.get(0).getId();
//		}
//		inventoryService.increaseRealInventory(sku, spId, warehouseId, quantity);
//		return new ResultInfo<String>("盘盈库存成功");
//	}
//
//	@Override
//	@Transactional(propagation = Propagation.REQUIRED)
//	public ResultInfo<String> reduceRealInventory(String sku, Long spId, Long warehouseId, Integer quantity) {
//		if (StringUtils.isBlank(sku) || spId == null || quantity == null) {
//			return new ResultInfo<String>(new FailInfo("调用错误，参数不能为空", 3));
//		}
//		if (null == warehouseId) {
//			Map<String, Object> params = new HashMap<>();
//			params.put("code", defaultWarehouseCode);
//			// 获得仓库信息
//			List<Warehouse> warehouses = warehouseService.queryByParamNotEmpty(params);
//			if (CollectionUtils.isEmpty(warehouses)) {
//				return new ResultInfo<String>(new FailInfo("调用错误，参数不能为空", 3));
//			}
//			warehouseId = warehouses.get(0).getId();
//		}
//		inventoryService.reduceRealInventory(sku, spId, warehouseId, quantity);
//		return new ResultInfo<String>("盘亏库存成功");
//	}
//
//	@Transactional(propagation = Propagation.REQUIRED)
//	@Override
//	public ResultInfo<String> batchUnoccupyInventory(List<Long> orderCodes) {
//		for (Long code : orderCodes) {
//			ResultInfo<String> message = unoccupyInventory(code);
//			if (Boolean.FALSE == message.isSuccess()) {
//				return message;
//			}
//		}
//		return new ResultInfo<String>("解冻库存成功");
//	}
//
//	@Override
//	public ResultInfo<String> addInventory(String sku, Long spId, Long warehouseId, Integer inventory) {
//		if (StringUtils.isBlank(sku) || null == spId || null == warehouseId || null == inventory) {
//			return new ResultInfo<String>(new FailInfo("参数错误", 1));
//		}
//		if (inventory.intValue() < 0) {
//			return new ResultInfo<String>(new FailInfo("入库数量不能小于0", 2));
//		}
//		List<Inventory> inventoryObjs = selectInventoryInfo(sku, spId, warehouseId);
//		if (CollectionUtils.isNotEmpty(inventoryObjs)) {
//			return new ResultInfo<String>(new FailInfo("库存记录已存在", 3));
//		}
//		try {
//			Warehouse warehouseObj = warehouseService.queryById(warehouseId);
//			if (warehouseObj == null) {
//				return new ResultInfo<String>(new FailInfo("仓库不存在", 4));
//			}
//			if (warehouseObj.getSpId().longValue() != spId) {
//				return new ResultInfo<String>(new FailInfo("仓库不属于当前供应商", 4));
//			}
//			Inventory inventoryObj = new Inventory();
//			inventoryObj.setCreateTime(new Date());
//			inventoryObj.setDistrictId(warehouseObj.getDistrictId());
//			inventoryObj.setInventory(inventory);
//			inventoryObj.setModifyTime(new Date());
//			inventoryObj.setOccupy(0);
//			inventoryObj.setReject(0);
//			inventoryObj.setSku(sku);
//			inventoryObj.setSpId(spId);
//			inventoryObj.setWarehouseId(warehouseId);
//			inventoryObj = inventoryService.insert(inventoryObj);
//
//			return new ResultInfo<String>(inventoryObj.getId().toString());
//		} catch (Exception e) {
//			logger.error("查询仓库信息出错：{} error = {}", warehouseId, e.getMessage());
//			return new ResultInfo<String>(new FailInfo("服务器异常", 5));
//		}
//	}
}
