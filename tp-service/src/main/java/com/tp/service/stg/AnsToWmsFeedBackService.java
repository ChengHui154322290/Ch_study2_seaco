/**
 * 
 */
package com.tp.service.stg;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.tp.common.vo.stg.BMLStorageConstant;
import com.tp.dao.stg.InputBackSkuDao;
import com.tp.dao.stg.InventoryDao;
import com.tp.dao.stg.WarehouseDao;
import com.tp.model.stg.InputBack;
import com.tp.model.stg.InputBackSku;
import com.tp.model.stg.Inventory;
import com.tp.model.stg.Warehouse;
import com.tp.model.stg.vo.feedback.ASNDetailsVO;
import com.tp.model.stg.vo.feedback.ASNsVO;
import com.tp.model.stg.vo.feedback.DetailVO;
import com.tp.service.stg.IAnsToWmsFeedBackService;
import com.tp.service.stg.IInputBackService;
import com.tp.util.DateUtil;

/**
 * @author szy
 *
 */
@Service
public class AnsToWmsFeedBackService implements IAnsToWmsFeedBackService {

	private Log logger = LogFactory.getLog(this.getClass());

	@Autowired
	private IInputBackService inputBackService;

	@Autowired
	private InputBackSkuDao inputBackSkuDao;

	@Autowired
	private WarehouseDao warehouseDao;

	@Autowired
	private InventoryDao inventoryDao;

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public Long insert(ASNDetailsVO aSNDetailsVO){
		ASNsVO aSNsVO = aSNDetailsVO.getASNs();
		// pre data
		InputBack inputBack = new InputBack();
		long warehouseId = 0L;
		long districtId = 0L;
		Map<String, Object> params = new HashMap<>();
		params.put("code", BMLStorageConstant.WAREHOUSE_CODE);
		List<Warehouse> warehouseList = warehouseDao.queryByParam(params);
		Warehouse warehouse = warehouseList.get(0);
		warehouseId = warehouse.getId();
		districtId = warehouse.getDistrictId();
		Long spId = warehouse.getSpId();

		inputBack.setWarehouseId(warehouseId);
		inputBack.setWarehouseCode(BMLStorageConstant.WAREHOUSE_CODE);
		inputBack.setAsnno(aSNsVO.getASNNo());
		inputBack.setCustomerOrderNo(aSNsVO.getCustmorOrderNo());
		inputBack.setExpectedArriveTime(aSNsVO.getExpectedArriveTime());
		inputBack.setCreateTime(DateUtil.getDateAfterDays(0));
		// 保存入库反馈记录
		inputBackService.insert(inputBack);
		Long inputBackId = inputBack.getId();
		List<DetailVO> detailList = aSNsVO.getDetails();
		List<InputBackSku> inputBackSkuList = new ArrayList<InputBackSku>();
		for (DetailVO detail : detailList) {
			logger.debug("正在保存入库反馈 SKU: " + detail.getSkuCode());
			InputBackSku inputBackSku = new InputBackSku();
			inputBackSku.setInputBackId(inputBackId);
			inputBackSku.setSkuCode(detail.getSkuCode());
			inputBackSku.setBarcode(detail.getBarcode());
			inputBackSku.setReceivedTime(DateUtil.formatDate(detail.getReceivedTime(), "yyyy-MM-dd HH:mm:ss"));
			inputBackSku.setExpectedQty(detail.getExpectedQty());
			inputBackSku.setReceivedQty(detail.getReceivedQty());
			inputBackSku.setLotatt01(detail.getLotatt01());
			inputBackSku.setLotatt02(detail.getLotatt02());
			inputBackSku.setLotatt03(detail.getLotatt03());
			inputBackSku.setLotatt04(detail.getLotatt04());
			inputBackSku.setLotatt05(detail.getLotatt05());
			inputBackSku.setLotatt06(detail.getLotatt06());
			inputBackSku.setLotatt07(detail.getLotatt07()); // 批次号
			inputBackSku.setLotatt08(detail.getLotatt08());
			inputBackSku.setLotatt09(detail.getLotatt09());
			inputBackSku.setLotatt10(detail.getLotatt10());
			inputBackSku.setLotatt11(detail.getLotatt11());
			inputBackSku.setLotatt12(detail.getLotatt12());
			inputBackSku.setCreateTime(DateUtil.getDateAfterDays(0));
			inputBackSkuList.add(inputBackSku);
		}
		inputBackSkuDao.insertBatch(inputBackSkuList);
		// 更新总库存信息
		for (InputBackSku detail : inputBackSkuList) {
			String sku = detail.getSkuCode();
			logger.debug("正在更新总库存 SKU:" + sku);
			Inventory inventory = new Inventory();
			inventory.setSku(sku);
			inventory.setWarehouseId(warehouseId);
			inventory.setSpId(warehouse.getSpId());

			List<Inventory> inventoryList = inventoryDao.queryByObject(inventory);
			if (inventoryList != null && inventoryList.size() > 0) {
				logger.debug("正在更新总库存[有库存记录，增加总库存] SKU:" + detail.getSkuCode());
				// 增加总库存
				inventoryDao.increaseRealInventory(sku, spId, warehouseId, detail.getReceivedQty().intValue());
			} else {
				// 无总库存信息，则插入总库存信息表
				logger.debug("正在更新总库存[无库存记录，新插入数据] SKU:" + detail.getSkuCode());
				double inventoryVal = detail.getReceivedQty();
				inventory.setInventory((int) inventoryVal);
				inventory.setOccupy(0);
				inventory.setReject(0);
				inventory.setSku(detail.getSkuCode());
				inventory.setWarehouseId(warehouseId);
				inventory.setSpId(spId);
				inventory.setDistrictId(districtId);
				inventory.setCreateTime(DateUtil.getDateAfterDays(0));
				inventory.setModifyTime(DateUtil.getDateAfterDays(0));
				inventoryDao.insert(inventory);
			}
		}
		return 0L;
	}

	@Override
	public InputBack selectByOrderCode(String orderCode) {
		Map<String, Object> params = new HashMap<>();
		params.put("customerOrderNo", orderCode);
		return inputBackService.queryUniqueByParams(params);
	}

}
