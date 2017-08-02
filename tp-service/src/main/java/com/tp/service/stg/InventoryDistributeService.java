package com.tp.service.stg;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.dao.BaseDao;
import com.tp.common.vo.StorageConstant.DistributeBackedStatus;
import com.tp.dao.stg.InventoryDistributeDao;
import com.tp.model.stg.InventoryDistribute;
import com.tp.service.BaseService;
import com.tp.service.stg.IInventoryDistributeService;

@Service
public class InventoryDistributeService extends BaseService<InventoryDistribute> implements IInventoryDistributeService {
	


	@Autowired
	private InventoryDistributeDao inventoryDistributeDao;
	
	@Override
	public BaseDao<InventoryDistribute> getDao() {
		return inventoryDistributeDao;
	}
	

	@Override
	public void increaseInventoryDistributeById(Long id, int inventory) {
		// TODO Auto-generated method stub
		inventoryDistributeDao.increaseInventoryDistributeById(id, inventory);
	}
	
	@Override
	public InventoryDistribute increaseInventoryDistribute(InventoryDistribute distribute, int inventory) {
		inventoryDistributeDao.increaseInventoryDistributeById(distribute.getId(), inventory);
		distribute.setInventory(distribute.getInventory() + inventory);
		distribute.setBizInventory(distribute.getBizInventory() + inventory);
		return distribute;
	}

	@Override
	public void updateBackedStatus(Long id, int backed) {
		// TODO Auto-generated method stub
		inventoryDistributeDao.updateBackedStatus(id, backed);
	}

	@Override
	public void reduceInventoryDistributeById(Long id, int inventory) {
		// TODO Auto-generated method stub
		inventoryDistributeDao.reduceInventoryDistributeById(id, inventory);
	}
	@Override
	public InventoryDistribute reduceInventoryDistribute(InventoryDistribute distribute, int inventory) {
		distribute.setInventory(distribute.getInventory() - inventory);
		distribute.setBizInventory(distribute.getBizInventory() - inventory);
		inventoryDistributeDao.reduceInventoryDistributeById(distribute.getId(), inventory);
		return distribute;
	}

	@Override
	public int forzenDistInventory(Long id, int inventory) {
		// TODO Auto-generated method stub
		return inventoryDistributeDao.forzenDistInventory(id, inventory);
	}

	@Override
	public void thawDistInventory(Long id, Integer inventory) {
		// TODO Auto-generated method stub
		inventoryDistributeDao.thawDistInventory(id, inventory);
	}
	
	@Override
	public List<InventoryDistribute> queryInventoryDistributes(List<InventoryDistribute> queries) {
		// TODO Auto-generated method stub
		return inventoryDistributeDao.queryInventoryDistributes(queries);
	}
	
	@Override
	public Integer reduceOccupyDistributeById(Long distributeId, Integer inventory) {
		// TODO Auto-generated method stub
		return inventoryDistributeDao.reduceOccupyDistributeById(distributeId, inventory);
	}

	@Override
	public void backDistributeInventoryById(Long id) {
		InventoryDistribute updateObj = new InventoryDistribute();
		updateObj.setId(id);
		updateObj.setInventory(0);
		updateObj.setBizInventory(0);
		updateObj.setBacked(DistributeBackedStatus.backed.getStatus());
		updateObj.setModifyTime(new Date());
		inventoryDistributeDao.updateNotNullById(updateObj);
	}
	
	@Override
	public InventoryDistribute backDistributeInventory(InventoryDistribute distribute) {
		distribute.setId(distribute.getId());
		distribute.setInventory(0);
		distribute.setBizInventory(0);
		distribute.setBacked(DistributeBackedStatus.backed.getStatus());
		distribute.setModifyTime(new Date());
		inventoryDistributeDao.updateNotNullById(distribute);
		return distribute;
	}

	@Override
	public List<InventoryDistribute> queryInventoryDistributesByInventoryId(Long inventoryId) {
		InventoryDistribute query = new InventoryDistribute();
		query.setInventoryId(inventoryId);
		return inventoryDistributeDao.queryByObject(query);
	}
//
//
//	@Autowired
//	private InventoryDistributeDao inventoryDistributeDao;
//	
//	@Override
//	public BaseDao<InventoryDistribute> getDao() {
//		return inventoryDistributeDao;
//	}
//
//	@Override
//	public void increaseInventoryDistributeById(Long id, int inventory) {
//		// TODO Auto-generated method stub
//		inventoryDistributeDao.increaseInventoryDistributeById(id, inventory);
//	}
//
//	@Override
//	public void updateBackedStatus(Long id, int backed) {
//		// TODO Auto-generated method stub
//		inventoryDistributeDao.updateBackedStatus(id, backed);
//	}
//
//	@Override
//	public void reduceInventoryDistributeById(Long id, int inventory) {
//		// TODO Auto-generated method stub
//		inventoryDistributeDao.reduceInventoryDistributeById(id, inventory);
//	}
//
//	@Override
//	public int forzenDistInventory(Long id, int inventory) {
//		// TODO Auto-generated method stub
//		return inventoryDistributeDao.forzenDistInventory(id, inventory);
//	}
//
//	@Override
//	public void thawDistInventory(Long id, Integer inventory) {
//		// TODO Auto-generated method stub
//		inventoryDistributeDao.thawDistInventory(id, inventory);
//	}
//
//	@Override
//	public List<InventoryDistribute> selectByAppAndSkuAndBizIds(List<Map<String, String>> params) {
//		// TODO Auto-generated method stub
//		return inventoryDistributeDao.selectByAppAndSkuAndBizIds(params);
//	}
//
//	@Override
//	public void reduceOccupyDistribute(String app, String bizId, String sku, Integer inventory) {
//		// TODO Auto-generated method stub
//		inventoryDistributeDao.reduceOccupyDistribute(app, bizId, sku, inventory);
//	}

}
