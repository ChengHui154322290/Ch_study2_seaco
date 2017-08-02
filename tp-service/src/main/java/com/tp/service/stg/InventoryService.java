package com.tp.service.stg;

import java.util.ArrayList;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.dao.BaseDao;
import com.tp.common.vo.StorageConstant;
import com.tp.dao.stg.InventoryDao;
import com.tp.dao.stg.InventoryLogDao;
import com.tp.dto.stg.query.InventoryDtoQuery;
import com.tp.dto.stg.query.InventoryQuery;
import com.tp.dto.wms.StockasnFactWithDetail;
import com.tp.exception.ServiceException;
import com.tp.model.stg.Inventory;
import com.tp.model.stg.InventoryLog;
import com.tp.model.stg.Warehouse;
import com.tp.model.wms.StockasnDetailFact;
import com.tp.service.BaseService;
import com.tp.service.stg.IInventoryService;
import com.tp.service.stg.IWarehouseService;
import com.tp.util.JsonUtil;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.tp.common.dao.BaseDao;
import com.tp.common.vo.PageInfo;
import com.tp.common.vo.StorageConstant;
import com.tp.common.vo.StorageConstant.DistributeBackedStatus;
import com.tp.dao.stg.InventoryDao;
import com.tp.dao.stg.InventoryDistributeDao;
import com.tp.dao.stg.InventoryLogDao;
import com.tp.dto.stg.InventoryDto;
import com.tp.dto.stg.InventoryManageDto;
import com.tp.dto.wms.StockasnFactWithDetail;
import com.tp.exception.ServiceException;
import com.tp.model.stg.Inventory;
import com.tp.model.stg.InventoryDistribute;
import com.tp.model.stg.InventoryLog;
import com.tp.model.stg.Warehouse;
import com.tp.model.wms.StockasnDetailFact;
import com.tp.service.BaseService;
import com.tp.util.JsonUtil;

@Service
public class InventoryService extends BaseService<Inventory> implements IInventoryService {


    @Autowired
    private InventoryDao inventoryDao;

    @Autowired
    private IWarehouseService warehouseService;

    @Autowired
    private InventoryLogDao inventoryLogDao;
    
    @Autowired
    private InventoryDistributeDao inventoryDistributeDao;
    
    @Override
    public BaseDao<Inventory> getDao() {
        return inventoryDao;
    }
    
    

	@Override
	public Integer increaseRealInventory(String sku, Long spId, Long warehouseId, Integer quantity) {
		
		return inventoryDao.increaseRealInventory(sku, spId, warehouseId, quantity);
	}

	@Override
	public void increaseRealInventoryById(Long id, int inventory) {
		
		inventoryDao.increaseRealInventoryById(id, inventory);
	}

	@Override
	public Integer reduceRealInventory(String sku, Long spId, Long warehouseId, Integer quantity) {
		
		return inventoryDao.reduceRealInventory(sku, spId, warehouseId, quantity);
	}

	@Override
	public Integer frozenOccupyInventoryById(Long inventoryId, int occupyInventory) {
		
		return inventoryDao.frozenOccupyInventoryById(inventoryId, occupyInventory);
	}

	@Override
	public void thawOccupyInventoryById(Long inventoryId, Integer occupyInventory) {
		
		inventoryDao.thawOccupyInventoryById(inventoryId, occupyInventory);
	}

	@Override
	public PageInfo<Inventory> queryPageInventoryInfo(List<Inventory> inventoryQueries, PageInfo<Inventory> pageInfo) {
		List<Inventory> rows = inventoryDao.queryPageInventoryInfo(inventoryQueries, pageInfo.getPage(), pageInfo.getSize());
		Long records = inventoryDao.queryCountInventoryInfo(inventoryQueries); 
		pageInfo.setRows(rows);
		pageInfo.setRecords(records.intValue());
		return pageInfo;
	}
	@Override
	public List<Inventory> queryInventoryInfo(List<Inventory> queries) {
		return inventoryDao.queryInventoryInfo(queries);
	}
	@Override
	public PageInfo<Inventory> queryPageInventoryInfoByParam(Map<String, Object> params, PageInfo<Inventory> pageInfo) {
		params.put("start", pageInfo.getStart());
		params.put("pageSize", pageInfo.getSize());
		List<Inventory> rows = inventoryDao.queryPageInventoryInfoByParam(params);
		Long records = inventoryDao.queryCountInventoryInfoByParam(params); 
		pageInfo.setRows(rows);
		pageInfo.setRecords(records.intValue());
		return pageInfo;
	}
	@Override
	public List<Inventory> queryInventoryInfoByParam(Map<String, Object> params) {
		return inventoryDao.queryInventoryInfoByParam(params);
	}
	
	@Override
	public void reduceOccupyInventoryById(Long inventoryId, Integer inventory) {		
		inventoryDao.reduceOccupyInventoryById(inventoryId, inventory);
	}

    @Transactional
    @Override
    public Integer increaseInventoryForStockasnFact(StockasnFactWithDetail stockasnFactWithDetail) {
        int count = 0;
        if (stockasnFactWithDetail == null || stockasnFactWithDetail.getStockasnDetailFacts() == null || stockasnFactWithDetail.getStockasnDetailFacts().isEmpty()) {
            return 0;
        }

        Warehouse warehouse = warehouseService.queryById(stockasnFactWithDetail.getStockasnFact().getWarehouseId());

        if (warehouse == null) {
            logger.error("WAREHOUSE_NOT_EXIT_ERROR.WAREHOUSE_ID:" + stockasnFactWithDetail.getStockasnFact().getWarehouseId());
            throw new ServiceException("仓库不存在");
        }

        List<Inventory> tempList = new ArrayList<>(stockasnFactWithDetail.getStockasnDetailFacts().size());

        Date cur = new Date();
        for (StockasnDetailFact detailFact : stockasnFactWithDetail.getStockasnDetailFacts()) {
            Inventory inventory = new Inventory();
            inventory.setSku(detailFact.getSku());
            inventory.setSpId(warehouse.getSpId());
            inventory.setWarehouseId(warehouse.getId());
            inventory.setDistrictId(warehouse.getDistrictId());
            inventory.setInventory(detailFact.getQuantity());
            inventory.setCreateTime(cur);
            inventory.setModifyTime(cur);
            inventory.setFreeze(0);
            inventory.setOccupy(0);
            inventory.setReject(0);
            inventory.setSample(0);
            tempList.add(inventory);
        }

        List<Inventory> forUpdate = new ArrayList<>();
        List<Inventory> forInsert = new ArrayList<>();
        List<Inventory> inventoryListDB = inventoryDao.queryInventoryInfo(tempList);

        for (Inventory temp : tempList) {
            if (contains(inventoryListDB, temp)) {
                forUpdate.add(temp);
            } else {
                forInsert.add(temp);
            }
        }
        List<InventoryLog> inventoryLogList = new ArrayList<>();
        assembleLog(stockasnFactWithDetail, forInsert, inventoryLogList);
        assembleLog(stockasnFactWithDetail, forUpdate, inventoryLogList);

        Integer ec = inventoryLogDao.queryCountByList(inventoryLogList);

        if (ec != null && ec == inventoryLogList.size()) {
            logger.warn("INCREASE_INVENTORY_FRO_STOCK_ASC_FACT_WARN:EXIST_THE_SAME_LOGS_FOR_THIS_OPERATION,IGNORE_THIS_OPERATION.PARAM={}", JsonUtil.convertObjToStr(stockasnFactWithDetail));
            logger.warn("INCREASE_INVENTORY_FRO_STOCK_ASC_FACT_WARN:EXIST_THE_SAME_LOGS_FOR_THIS_OPERATION,IGNORE_THIS_OPERATION.PARAM={}", JsonUtil.convertObjToStr(stockasnFactWithDetail));
            logger.warn("INCREASE_INVENTORY_FRO_STOCK_ASC_FACT_WARN:EXIST_THE_SAME_LOGS_FOR_THIS_OPERATION,IGNORE_THIS_OPERATION.PARAM={}", JsonUtil.convertObjToStr(stockasnFactWithDetail));
            //TODO send msg or email to notice
            return 0;
           
        }else if(ec !=null && ec.intValue()>0){
            logger.warn("INCREASE_INVENTORY_FRO_STOCK_ASC_FACT_WARN:EXIST_PART_SAME_LOGS_FOR_THIS_OPERATION,IGNORE_THIS_OPERATION.PARAM={}", JsonUtil.convertObjToStr(stockasnFactWithDetail));
            logger.warn("INCREASE_INVENTORY_FRO_STOCK_ASC_FACT_WARN:EXIST_PART_SAME_LOGS_FOR_THIS_OPERATION,IGNORE_THIS_OPERATION.PARAM={}", JsonUtil.convertObjToStr(stockasnFactWithDetail));
            logger.warn("INCREASE_INVENTORY_FRO_STOCK_ASC_FACT_WARN:EXIST_PART_SAME_LOGS_FOR_THIS_OPERATION,IGNORE_THIS_OPERATION.PARAM={}", JsonUtil.convertObjToStr(stockasnFactWithDetail));
        }
        inventoryLogDao.insertBatch(inventoryLogList);

        if (!CollectionUtils.isEmpty(forInsert)) {
            count += inventoryDao.batchInsert(forInsert);
        }
        if (!CollectionUtils.isEmpty(forUpdate)) {
            for (Inventory inventory : forUpdate) {
                count += inventoryDao.increaseRealInventory(inventory.getSku(), inventory.getSpId(), inventory.getWarehouseId(), inventory.getInventory());
            }
        }

        return count;
    }
    
    @Override
    public Inventory getInventoryById(Long id){
    	Inventory inventory = queryById(id);
    	if(inventory == null) return null;
    	InventoryDistribute query = new InventoryDistribute();
    	query.setInventoryId(id);
    	inventory.setInventoryDistributes(inventoryDistributeDao.queryByObject(query));
    	return inventory;
    }
    
    /** 查询库存详细数据 */
    @Override
    public InventoryDto getInventoryDetailInfoById(Long inventoryId){
    	Inventory inventory = queryById(inventoryId);
    	if(inventory == null) return new InventoryDto();
    	InventoryDistribute query = new InventoryDistribute();
    	query.setInventoryId(inventoryId);
    	inventory.setInventoryDistributes(inventoryDistributeDao.queryByObject(query));
    	return initInventoryDto(inventory);
    }
    /** 查询库存详细数据 */
    @Override
    public InventoryDto getInventoryDetailInfo(Inventory inventory){
    	if(CollectionUtils.isEmpty(inventory.getInventoryDistributes())){
        	InventoryDistribute query = new InventoryDistribute();
        	query.setInventoryId(inventory.getId());
        	inventory.setInventoryDistributes(inventoryDistributeDao.queryByObject(query));
    	}
    	return initInventoryDto(inventory);
    }
    
    private InventoryDto initInventoryDto(Inventory inventory){
		InventoryDto dto = new InventoryDto();
		// 获取特殊活动占用库存
		Integer preOccupyCount = 0;
		Integer preOrderOuccpyCount = 0; //特殊活动订单占用库存(已结束)
		if (!CollectionUtils.isEmpty(inventory.getInventoryDistributes())) {
			for (InventoryDistribute ivddo : inventory.getInventoryDistributes()) {
				if(DistributeBackedStatus.unbacked.getStatus().equals(ivddo.getBacked())){
					preOccupyCount += ivddo.getBizInventory();	
				}else{
					preOrderOuccpyCount += ivddo.getOccupy();
				}
			}
		}
		dto.setDistrictId(inventory.getDistrictId());
		dto.setSku(inventory.getSku());
		dto.setSpId(inventory.getSpId());
		dto.setWarehouseId(inventory.getWarehouseId());
		Integer occoupyCount = inventory.getOccupy() + preOccupyCount + preOrderOuccpyCount; 
		//占用库存 = 特殊活动预占库存(未结束) + 非特殊活动订单占用库存 + 特殊活动订单占用库存(已结束) 
		dto.setOccupy(occoupyCount);
		//总库存 = 可销售库存 + 预留库存 + 占用库存
		dto.setRealInventory(inventory.getInventory() + occoupyCount);
		//可销售库存 = 剩余库存 - 预留库存
		dto.setInventory(inventory.getAvailableInventory());
		dto.setReserveInventory(inventory.getReserveInventory());
		dto.setWarnInventory(inventory.getWarnInventory());
		dto.setId(inventory.getId());
		return dto;
    }
    
    private void assembleLog(StockasnFactWithDetail stockasnFactWithDetail, List<Inventory> forInsert, List<InventoryLog> inventoryLogList) {
        for (Inventory inventory : forInsert) {
            InventoryLog log = new InventoryLog();
            log.setSku(inventory.getSku());
            log.setDistrictId(inventory.getDistrictId());
            log.setOrderCode(stockasnFactWithDetail.getStockasnFact().getOrderCode());
            log.setAddress(StringUtils.EMPTY);
            log.setBarcode(StringUtils.EMPTY);
            log.setBatchNo(StringUtils.EMPTY);
            log.setCreateTime(new Date());
            log.setSkuCount(inventory.getInventory());
            log.setSupplierId(inventory.getSpId());
            log.setWarehouseId(inventory.getWarehouseId());
            log.setWhCode(stockasnFactWithDetail.getStockasnFact().getWarehouseCode());
            log.setType(StorageConstant.InputAndOutputType.CO.getCode());
            List<StockasnDetailFact> list = stockasnFactWithDetail.getStockasnDetailFacts();
            for(StockasnDetailFact stockasnDetailFact : list){
            	if(inventory.getSku().equals(stockasnDetailFact.getSku()) &&
            			inventory.getWarehouseId().equals(stockasnFactWithDetail.getStockasnFact().getWarehouseId())){
            		log.setInventory(stockasnDetailFact.getQuantity());
                }
            }
            inventoryLogList.add(log);
        }
    }

    private boolean contains(List<Inventory> inventoryList, Inventory own) {
        if (CollectionUtils.isEmpty(inventoryList)) return false;

        for (Inventory inventory : inventoryList) {
            if (StringUtils.equals(inventory.getSku(), own.getSku()) && inventory.getWarehouseId().equals(own.getWarehouseId()) && inventory.getSpId().equals(own.getSpId())) {
                return true;
            }
        }

        return false;
    }
	@Override
	public PageInfo<InventoryManageDto> getPageByParamNotEmpty(Map<String, Object> params, PageInfo<InventoryManageDto> pageInfo) {
		params.put("startPage",(pageInfo.getPage()-1)*pageInfo.getSize());
		params.put("pageSize",pageInfo.getSize());
		
		List<InventoryManageDto> list = inventoryDao.getInventoryList(params);
		int count = inventoryDao.getInventoryListCount(params);
		pageInfo.setRecords(count);
		pageInfo.setRows(list);
		return pageInfo;
	}
	
	@Override
	public InventoryManageDto getInventoryById(Map<String, Object> params) {
		return inventoryDao.getInventoryById(params);
	}
	
//
//
//    @Autowired
//    private InventoryDao inventoryDao;
//
//    @Autowired
//    private IWarehouseService warehouseService;
//
//    @Autowired
//    private InventoryLogDao inventoryLogDao;
//
//    @Override
//    public BaseDao<Inventory> getDao() {
//        return inventoryDao;
//    }
//
//	@Override
//	public Integer increaseRealInventory(String sku, Long spId, Long warehouseId, Integer quantity) {
//		
//		return inventoryDao.increaseRealInventory(sku, spId, warehouseId, quantity);
//	}
//
//	@Override
//	public void increaseRealInventoryById(Long id, int inventory) {
//		
//		inventoryDao.increaseRealInventoryById(id, inventory);
//	}
//
//	@Override
//	public Integer reduceRealInventory(String sku, Long spId, Long warehouseId, Integer quantity) {
//		
//		return inventoryDao.reduceRealInventory(sku, spId, warehouseId, quantity);
//	}
//
//	@Override
//	public void frozenOccupyInventoryById(Long inventoryId, int occupyInventory) {
//		
//		inventoryDao.frozenOccupyInventoryById(inventoryId, occupyInventory);
//	}
//
//	@Override
//	public void thawOccupyInventoryById(Long inventoryId, Integer occupyInventory) {
//		
//		inventoryDao.thawOccupyInventoryById(inventoryId, occupyInventory);
//	}
//
//	@Override
//	public List<Inventory> selectPageBySkuAndSpIdList(List<InventoryQuery> inventoryQueries, Integer page,
//			Integer pageSize) {
//		int start = (page < 0 || pageSize <= 0) ? 0:(page - 1)*pageSize;
//		return inventoryDao.selectPageBySkuAndSpIdList(inventoryQueries, start, pageSize);
//	}
//
//	@Override
//	public Long selectCountBySkuAndSpIdList(List<InventoryQuery> inventoryQueries) {
//		
//		return inventoryDao.selectCountBySkuAndSpIdList(inventoryQueries);
//	}
//
//	@Override
//	public List<Inventory> selectBySkuAndWhIdList(List<InventoryDtoQuery> cleanQuery) {
//		
//		return inventoryDao.selectBySkuAndWhIdList(cleanQuery);
//	}
//
//	@Override
//	public void reduceInventoryAndOccupy(Long inventoryId, Integer inventory) {
//		
//		inventoryDao.reduceInventoryAndOccupy(inventoryId, inventory);
//	}
//
//    @Transactional
//    @Override
//    public Integer increaseInventoryForStockasnFact(StockasnFactWithDetail stockasnFactWithDetail) {
//        int count = 0;
//        if (stockasnFactWithDetail == null || stockasnFactWithDetail.getStockasnDetailFacts() == null || stockasnFactWithDetail.getStockasnDetailFacts().isEmpty()) {
//            return 0;
//        }
//
//        Warehouse warehouse = warehouseService.queryById(stockasnFactWithDetail.getStockasnFact().getWarehouseId());
//
//        if (warehouse == null) {
//            logger.error("WAREHOUSE_NOT_EXIT_ERROR.WAREHOUSE_ID:" + stockasnFactWithDetail.getStockasnFact().getWarehouseId());
//            throw new ServiceException("仓库不存在");
//        }
//
//        List<Inventory> tempList = new ArrayList<>(stockasnFactWithDetail.getStockasnDetailFacts().size());
//
//        Date cur = new Date();
//        for (StockasnDetailFact detailFact : stockasnFactWithDetail.getStockasnDetailFacts()) {
//            Inventory inventory = new Inventory();
//            inventory.setSku(detailFact.getSku());
//            inventory.setSpId(warehouse.getSpId());
//            inventory.setWarehouseId(warehouse.getId());
//            inventory.setDistrictId(warehouse.getDistrictId());
//            inventory.setInventory(detailFact.getQuantity());
//            inventory.setCreateTime(cur);
//            inventory.setModifyTime(cur);
//            inventory.setFreeze(0);
//            inventory.setOccupy(0);
//            inventory.setReject(0);
//            inventory.setSample(0);
//            tempList.add(inventory);
//        }
//
//        List<Inventory> forUpdate = new ArrayList<>();
//        List<Inventory> forInsert = new ArrayList<>();
//        List<Inventory> inventoryListDB = inventoryDao.queryByInventoriesForStockasn(tempList);
//
//        for (Inventory temp : tempList) {
//            if (contains(inventoryListDB, temp)) {
//                forUpdate.add(temp);
//            } else {
//                forInsert.add(temp);
//            }
//        }
//        List<InventoryLog> inventoryLogList = new ArrayList<>();
//        assembleLog(stockasnFactWithDetail, forInsert, inventoryLogList);
//        assembleLog(stockasnFactWithDetail, forUpdate, inventoryLogList);
//
//        Integer ec = inventoryLogDao.queryCountByList(inventoryLogList);
//
//        if (ec != null && ec == inventoryLogList.size()) {
//            logger.warn("INCREASE_INVENTORY_FRO_STOCK_ASC_FACT_WARN:EXIST_THE_SAME_LOGS_FOR_THIS_OPERATION,IGNORE_THIS_OPERATION.PARAM={}", JsonUtil.convertObjToStr(stockasnFactWithDetail));
//            logger.warn("INCREASE_INVENTORY_FRO_STOCK_ASC_FACT_WARN:EXIST_THE_SAME_LOGS_FOR_THIS_OPERATION,IGNORE_THIS_OPERATION.PARAM={}", JsonUtil.convertObjToStr(stockasnFactWithDetail));
//            logger.warn("INCREASE_INVENTORY_FRO_STOCK_ASC_FACT_WARN:EXIST_THE_SAME_LOGS_FOR_THIS_OPERATION,IGNORE_THIS_OPERATION.PARAM={}", JsonUtil.convertObjToStr(stockasnFactWithDetail));
//            //TODO send msg or email to notice
//            return 0;
//           
//        }else if(ec !=null && ec.intValue()>0){
//            logger.warn("INCREASE_INVENTORY_FRO_STOCK_ASC_FACT_WARN:EXIST_PART_SAME_LOGS_FOR_THIS_OPERATION,IGNORE_THIS_OPERATION.PARAM={}", JsonUtil.convertObjToStr(stockasnFactWithDetail));
//            logger.warn("INCREASE_INVENTORY_FRO_STOCK_ASC_FACT_WARN:EXIST_PART_SAME_LOGS_FOR_THIS_OPERATION,IGNORE_THIS_OPERATION.PARAM={}", JsonUtil.convertObjToStr(stockasnFactWithDetail));
//            logger.warn("INCREASE_INVENTORY_FRO_STOCK_ASC_FACT_WARN:EXIST_PART_SAME_LOGS_FOR_THIS_OPERATION,IGNORE_THIS_OPERATION.PARAM={}", JsonUtil.convertObjToStr(stockasnFactWithDetail));
//        }
//        inventoryLogDao.insertBatch(inventoryLogList);
//
//        if (!CollectionUtils.isEmpty(forInsert)) {
//            count += inventoryDao.batchInsert(forInsert);
//        }
//        if (!CollectionUtils.isEmpty(forUpdate)) {
//            for (Inventory inventory : forUpdate) {
//                count += inventoryDao.increaseRealInventory(inventory.getSku(), inventory.getSpId(), inventory.getWarehouseId(), inventory.getInventory());
//            }
//        }
//
//        return count;
//    }
//
//    private void assembleLog(StockasnFactWithDetail stockasnFactWithDetail, List<Inventory> forInsert, List<InventoryLog> inventoryLogList) {
//        for (Inventory inventory : forInsert) {
//            InventoryLog log = new InventoryLog();
//            log.setSku(inventory.getSku());
//            log.setDistrictId(inventory.getDistrictId());
//            log.setOrderCode(stockasnFactWithDetail.getStockasnFact().getOrderCode());
//            log.setAddress(StringUtils.EMPTY);
//            log.setBarcode(StringUtils.EMPTY);
//            log.setBatchNo(StringUtils.EMPTY);
//            log.setCreateTime(new Date());
//            log.setSkuCount(inventory.getInventory());
//            log.setSupplierId(inventory.getSpId());
//            log.setWarehouseId(inventory.getWarehouseId());
//            log.setWhCode(stockasnFactWithDetail.getStockasnFact().getWarehouseCode());
//            log.setType(StorageConstant.InputAndOutputType.CO.getCode());
//            List<StockasnDetailFact> list = stockasnFactWithDetail.getStockasnDetailFacts();
//            for(StockasnDetailFact stockasnDetailFact : list){
//            	if(inventory.getSku().equals(stockasnDetailFact.getSku()) &&
//            			inventory.getWarehouseId().equals(stockasnFactWithDetail.getStockasnFact().getWarehouseId())){
//            		log.setInventory(stockasnDetailFact.getQuantity());
//                }
//            }
//            inventoryLogList.add(log);
//        }
//    }
//
//    private boolean contains(List<Inventory> inventoryList, Inventory own) {
//        if (CollectionUtils.isEmpty(inventoryList)) return false;
//
//        for (Inventory inventory : inventoryList) {
//            if (StringUtils.equals(inventory.getSku(), own.getSku()) && inventory.getWarehouseId().equals(own.getWarehouseId()) && inventory.getSpId().equals(own.getSpId())) {
//                return true;
//            }
//        }
//
//        return false;
//    }
}
