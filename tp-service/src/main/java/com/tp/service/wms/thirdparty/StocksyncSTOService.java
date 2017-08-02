/**
 * 
 */
package com.tp.service.wms.thirdparty;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tp.common.vo.Constant.SPLIT_SIGN;
import com.tp.common.vo.DAOConstant.MYBATIS_SPECIAL_STRING;
import com.tp.common.vo.bse.ClearanceChannelsEnum;
import com.tp.common.vo.stg.WarehouseConstant.WMSCode;
import com.tp.dao.wms.StocksyncInfoDao;
import com.tp.dto.common.FailInfo;
import com.tp.dto.common.ResultInfo;
import com.tp.model.prd.ItemSku;
import com.tp.model.prd.ItemSkuArt;
import com.tp.model.stg.Inventory;
import com.tp.model.stg.Warehouse;
import com.tp.model.wms.StocksyncInfo;
import com.tp.service.prd.IItemSkuArtService;
import com.tp.service.prd.IItemSkuService;
import com.tp.service.stg.IInventoryService;
import com.tp.service.stg.IWarehouseService;
import com.tp.service.wms.thirdparty.IStocksyncTPService;
import com.tp.util.StringUtil;

/**
 * @author Administrator
 * 申通仓库库存同步
 */
@Service("stocksyncSTOService")
public class StocksyncSTOService implements IStocksyncTPService{
	
	private static final Logger logger = LoggerFactory.getLogger(StocksyncSTOService.class);
	
	@Autowired
	private IWarehouseService warehouseService;
	
	@Autowired
	private IItemSkuArtService itemSkuArtService;
	
	@Autowired
	private IInventoryService inventoryService;
	
	@Autowired
	private StocksyncInfoDao stocksyncInfoDao;
	
	@Autowired
	private IItemSkuService itemSkuService;
	
	@Override
	public boolean check(List<StocksyncInfo> syInfos, WMSCode wmsCode) {
		if (WMSCode.STO_HZ.code.equals(wmsCode.getCode())) {
			return true;
		}
		return false;
	}
	
	/**
	 * 库存同步：1.数据校验(对应系统SKU) 2. 查询当前SKU库存 3. 更新库存 4. 更新记录入库 
	 * 申通自营仓的备案SKU与系统SKU 一一对应
	 */
	@Transactional
	@Override
	public ResultInfo<Boolean> syncSkuInventory(List<StocksyncInfo> syInfos) {
		logger.info("[STOCK_SKU_SYNC]申通仓库存同步...");
		if (CollectionUtils.isEmpty(syInfos)) {
			return new ResultInfo<>(Boolean.TRUE);
		}	
		for (StocksyncInfo stocksyncInfo : syInfos) {
			ResultInfo<Boolean> resultInfo = syncSkuInventory(stocksyncInfo);
			if (!resultInfo.isSuccess()) {
				logger.error("[STOCK_SKU_SYNC][articleNumber={}][wmsCode={}]库存同步错误", 
						stocksyncInfo.getStockSku(), stocksyncInfo.getWmsCode());
			}
		}
		logger.info("[STOCK_SKU_SYNC]申通仓库存同步结束");
 		return new ResultInfo<>(Boolean.TRUE);
	}
	
	//库存同步
	private ResultInfo<Boolean> syncSkuInventory(StocksyncInfo syncInfo){
		ItemSku itemSku = querySkuByArticleNumber(ClearanceChannelsEnum.HANGZHOU.id, syncInfo.getStockSku());
		if (itemSku == null) {
			logger.error("[STOCK_SKU_SYNC][article_number={}]sku信息不存在", syncInfo.getStockSku());
			return new ResultInfo<>(new FailInfo("商品信息不存在"));
		}
		Warehouse warehouse = queryWarehouseByWmsCode(WMSCode.STO_HZ.code, itemSku.getSpId());
		if (warehouse == null) {
			logger.error("[STOCK_SKU_SYNC][spId={}][wmsCode={}]仓库不存在", itemSku.getSpId(), WMSCode.STO_HZ.code);
			return new ResultInfo<>(new FailInfo("仓库信息不存在"));
		}
		Inventory inventoryQuery = new Inventory();
		inventoryQuery.setWarehouseId(warehouse.getId());
		inventoryQuery.setSku(itemSku.getSku());
		Inventory inventory = inventoryService.queryUniqueByObject(inventoryQuery);
		if (inventory == null) {
			logger.error("[STOCK_SKU_SYNC][warehouseId={}][sku={}]库存信息不存在", warehouse.getId(), itemSku.getSku());
			return new ResultInfo<>(new FailInfo("库存信息不存在"));
		}
		syncInfo.setSku(itemSku.getSku());
		syncInfo.setSkuName(itemSku.getDetailName());
		syncInfo.setInventory(Long.valueOf(inventory.getInventory()));
		syncInfo.setWhId(warehouse.getId());
		syncInfo.setWmsCode(WMSCode.STO_HZ.code);
		syncInfo.setSyncTime(new Date());
		syncInfo.setCreateTime(new Date());
		stocksyncInfoDao.insert(syncInfo);
		
		syncInventory(syncInfo);
		return new ResultInfo<>(Boolean.TRUE);
	}
	
	private Warehouse queryWarehouseByWmsCode(String wmsCode, Long spId){
		Warehouse warehouseQuery = new Warehouse();
		warehouseQuery.setSpId(spId);
		warehouseQuery.setWmsCode(wmsCode);
		return warehouseService.queryUniqueByObject(warehouseQuery);
	}
	
	private ItemSku querySkuByArticleNumber(Long bondedArea, String articleNumber){
		ItemSkuArt queryArt = new ItemSkuArt();
		queryArt.setBondedArea(bondedArea);
		queryArt.setArticleNumber(articleNumber);
		ItemSkuArt uniqueArt = itemSkuArtService.queryUniqueByObject(queryArt);
		if (uniqueArt == null) {
			return null;
		}
		return itemSkuService.queryById(uniqueArt.getId());
	}
	
	//库存同步（发送消息）
	private void syncInventory(StocksyncInfo info){
		
	}
}
