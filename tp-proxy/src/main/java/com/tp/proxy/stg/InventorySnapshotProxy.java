package com.tp.proxy.stg;

import java.beans.Beans;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.vo.Constant;
import com.tp.common.vo.PageInfo;
import com.tp.common.vo.DAOConstant.MYBATIS_SPECIAL_STRING;
import com.tp.common.vo.DAOConstant.WHERE_ENTRY;
import com.tp.model.bse.DistrictInfo;
import com.tp.model.stg.InventorySnapshot;
import com.tp.model.stg.Warehouse;
import com.tp.proxy.BaseProxy;
import com.tp.service.IBaseService;
import com.tp.service.bse.IDistrictInfoService;
import com.tp.service.stg.IInventorySnapshotService;
import com.tp.service.stg.IWarehouseService;
import com.tp.util.BeanUtil;

/**
 * 库存信息表 记录sku的总库存、总销售占用库存信息代理层
 * 
 * @author szy
 *
 */
@Service
public class InventorySnapshotProxy extends BaseProxy<InventorySnapshot> {

	@Autowired
	private IInventorySnapshotService inventorySnapshotService;

	@Override
	public IBaseService<InventorySnapshot> getService() {
		return inventorySnapshotService;
	}

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private IWarehouseService warehouseService;

	@Autowired
	private IDistrictInfoService districtInfoService;

	/***
	 * 查询库存快照信息
	 * 
	 * @param inventorySnapshot
	 * @param startPage
	 * @param pageSize
	 * @return
	 */
	public PageInfo<InventorySnapshot> selectSnapshot(InventorySnapshot inventorySnapshot, Integer startPage, Integer pageSize) {
		if (inventorySnapshot == null) {
			inventorySnapshot = new InventorySnapshot();
		}
		
		Map<String, Object> params = BeanUtil.beanMap(inventorySnapshot);
		List<WHERE_ENTRY> whEntries = new ArrayList<>();
		if (null != inventorySnapshot.getCreateBeginTime()) {
			whEntries.add(new WHERE_ENTRY("create_time", MYBATIS_SPECIAL_STRING.GT, inventorySnapshot.getCreateBeginTime()));
		}
		if (null != inventorySnapshot.getCreateEndTime()) {
			whEntries.add(new WHERE_ENTRY("create_time", MYBATIS_SPECIAL_STRING.LT, inventorySnapshot.getCreateEndTime()));
		}
		params.put(MYBATIS_SPECIAL_STRING.WHERE.name(), whEntries);
		params.remove("createBeginTime");
		params.remove("createEndTime");
		PageInfo<InventorySnapshot> page = inventorySnapshotService.queryPageByParam(params, 
				new PageInfo<InventorySnapshot>(startPage, pageSize));

		if (page == null || CollectionUtils.isEmpty(page.getRows()))
			return new PageInfo<>();

		List<Long> warehouseIds = new ArrayList<>();
		for (InventorySnapshot sdo : page.getRows()) {
			if (sdo.getWarehouseId() != null) {
				warehouseIds.add(sdo.getWarehouseId());
			}
		}

		List<Long> districtIds = new ArrayList<>();
		for (InventorySnapshot sdo : page.getRows()) {
			if (sdo.getDistrictId() != null) {
				districtIds.add(sdo.getDistrictId());
			}
		}

		if (CollectionUtils.isNotEmpty(districtIds)) {
			params.clear();
			params.put(MYBATIS_SPECIAL_STRING.COLUMNS.name(),
					"id in(" + StringUtils.join(districtIds, Constant.SPLIT_SIGN.COMMA) + ")");
			List<DistrictInfo> listD = districtInfoService.queryByParamNotEmpty(params);
			Map<Long, String> mapDIDtoName = new HashMap<>();
			if (CollectionUtils.isNotEmpty(listD)) {
				for (DistrictInfo districtInfo : listD) {
					mapDIDtoName.put(districtInfo.getId(), districtInfo.getName());
				}
				List<InventorySnapshot> inventorySnapshotObjs = page.getRows();
				for (int i = 0; i < inventorySnapshotObjs.size(); i++) {
					String tmpName = mapDIDtoName.get(inventorySnapshotObjs.get(i).getDistrictId());
					if (null != tmpName) {
						inventorySnapshotObjs.get(i).setAddress(tmpName);
					}
				}
			}
		}

		if (CollectionUtils.isNotEmpty(warehouseIds)) {
			List<Warehouse> listw = warehouseService.queryByIds(warehouseIds);
			if (CollectionUtils.isNotEmpty(listw)) {
				Map<Long, String> mapWIDtoName = new HashMap<>();
				for (Warehouse warehouse : listw) {
					mapWIDtoName.put(warehouse.getId(), warehouse.getName());
				}
				List<InventorySnapshot> inventorySnapshotObjs = page.getRows();
				for (int i = 0; i < inventorySnapshotObjs.size(); i++) {
					String tmpName = mapWIDtoName.get(inventorySnapshotObjs.get(i).getWarehouseId());
					if (null != tmpName) {
						inventorySnapshotObjs.get(i).setWareHouseName(tmpName);
					}
				}
			}
		}

		return page;
	}

	public void export(HttpServletRequest request, HttpServletResponse response, InventorySnapshot inventorySnapshot)
			throws InvalidFormatException, IOException {
		inventorySnapshot.setExportCount(1000);
		// 查询数据
		List<InventorySnapshot> exportList = inventorySnapshotService.querySnapshotForExport(inventorySnapshot);

		List<InventorySnapshot> wareHouseList = inventorySnapshotService
				.queryDistinctWarehouseIdForExport(inventorySnapshot);

		HSSFWorkbook wb = new HSSFWorkbook();
		HSSFRow row;
		HSSFSheet sheet = setExportStyle(wb);
		if (CollectionUtils.isNotEmpty(exportList)) {
			// 合并相同仓库ID
			List<Long> wareHouseIds = new ArrayList<>();
			List<Long> districtIds = new ArrayList<>();
			Map<Long, Warehouse> wareHouseMap = new HashMap<>();

			Map<Long, String> districtInfoMap = new HashMap<>();

			if (CollectionUtils.isNotEmpty(wareHouseList)) {

				for (InventorySnapshot ido : wareHouseList) {
					if (!wareHouseIds.contains(ido.getWarehouseId())) {
						wareHouseIds.add(ido.getWarehouseId());
					}
				}

				for (InventorySnapshot ido : exportList) {
					if (!districtIds.contains(ido.getDistrictId())) {
						districtIds.add(ido.getDistrictId());
					}
				}

				if (CollectionUtils.isNotEmpty(districtIds)) {
					Map<String, Object> params = new HashMap<>();
					params.put(MYBATIS_SPECIAL_STRING.COLUMNS.name(),
							"id in(" + StringUtils.join(districtIds, Constant.SPLIT_SIGN.COMMA) + ")");
					List<DistrictInfo> listD = districtInfoService.queryByParamNotEmpty(params);
					for (DistrictInfo ddo : listD) {
						districtInfoMap.put(ddo.getId(), ddo.getName());
					}
				}

				if (CollectionUtils.isNotEmpty(wareHouseIds)) {
					List<Warehouse> listw = warehouseService.queryByIds(wareHouseIds);
					if (CollectionUtils.isNotEmpty(listw)) {
						for (Warehouse wdo : listw) {
							wareHouseMap.put(wdo.getId(), wdo);
						}
					}
				}
			}

			for (int i = 0; i < exportList.size(); i++) {
				row = sheet.createRow((int) i + 1);
				InventorySnapshot edo = (InventorySnapshot) exportList.get(i);
				// 第四步，创建单元格，并设置值
				row.createCell(0).setCellValue(edo.getId());
				row.createCell(1).setCellValue(edo.getSku());
				if (wareHouseMap.containsKey(edo.getWarehouseId())) {
					row.createCell(2).setCellValue(wareHouseMap.get(edo.getWarehouseId()).getAddress());
				}
				if (districtInfoMap.containsKey(edo.getDistrictId())) {
					row.createCell(3).setCellValue(districtInfoMap.get(edo.getDistrictId()).toString());
				}

				row.createCell(4).setCellValue(edo.getInventory());
				row.createCell(5).setCellValue(edo.getOccupy());
				if (edo.getSnapTime() != null) {
					row.createCell(6).setCellValue(DateFormatUtils.format(edo.getSnapTime(), "yyyy-MM-dd HH:mm:ss"));
				} else {
					row.createCell(6).setCellValue("");
				}

			}
		}

		setResponseInfo(response, wb);
	}

	/***
	 * 设置输出流
	 * 
	 * @param response
	 * @param wb
	 * @throws IOException
	 */
	private void setResponseInfo(HttpServletResponse response, HSSFWorkbook wb) throws IOException {
		response.setContentType("text/html;charset=utf-8");
		OutputStream ouputStream = response.getOutputStream();
		try {
			response.setHeader("Cache-Control", "no-cache");
			response.setHeader("Pragma", "no-cache");
			response.setDateHeader("Expires", -1);
			response.setCharacterEncoding("utf-8");
			response.addHeader("Content-Disposition",
					"attachment; filename=".concat(DateFormatUtils.format(new Date(), "yyyy-MM-dd")).concat(".xls"));
			wb.write(ouputStream);
			ouputStream.flush();
			ouputStream.close();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			e.printStackTrace();
		} finally {
			if (ouputStream != null) {
				try {
					ouputStream.close();
				} catch (IOException e) {
					logger.error(e.getMessage(), e);
				}
			}
		}
	}

	/***
	 * 设置导出样式
	 * 
	 * @param wb
	 * @return
	 */
	private HSSFSheet setExportStyle(HSSFWorkbook wb) {
		HSSFSheet sheet = wb.createSheet("盘点记录");
		HSSFRow row = sheet.createRow((int) 0);
		HSSFCellStyle style = wb.createCellStyle();
		style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		HSSFCell cell = row.createCell(0);
		cell.setCellValue("序号");
		cell.setCellStyle(style);
		cell = row.createCell(1);
		cell.setCellValue("SKU");
		cell.setCellStyle(style);
		cell = row.createCell(2);
		cell.setCellValue("区域");
		cell.setCellStyle(style);
		cell = row.createCell(3);
		cell.setCellValue("仓库");
		cell.setCellStyle(style);
		cell = row.createCell(4);
		cell.setCellValue("现货库存");
		cell.setCellStyle(style);
		cell = row.createCell(5);
		cell.setCellValue("占用库存");
		cell.setCellStyle(style);
		cell = row.createCell(6);
		cell.setCellValue("创建时间");
		cell.setCellStyle(style);
		return sheet;
	}

}
