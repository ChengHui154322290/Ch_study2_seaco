package com.tp.proxy.stg;

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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.vo.Constant;
import com.tp.common.vo.PageInfo;
import com.tp.common.vo.DAOConstant.MYBATIS_SPECIAL_STRING;
import com.tp.common.vo.DAOConstant.WHERE_ENTRY;
import com.tp.common.vo.StorageConstant.InputAndOutputType;
import com.tp.model.bse.DistrictInfo;
import com.tp.model.stg.InventoryLog;
import com.tp.model.stg.Warehouse;
import com.tp.proxy.BaseProxy;
import com.tp.service.IBaseService;
import com.tp.service.bse.IDistrictInfoService;
import com.tp.service.stg.IInventoryLogService;
import com.tp.service.stg.IWarehouseService;
import com.tp.util.BeanUtil;

/**
 * 出入库流水信息表代理层
 * 
 * @author szy
 *
 */
@Service
public class InventoryLogProxy extends BaseProxy<InventoryLog> {

	@Autowired
	private IInventoryLogService inventoryLogService;

	@Autowired
	private IWarehouseService warehouseService;

	@Autowired
	private IDistrictInfoService districtInfoService;

	@Override
	public IBaseService<InventoryLog> getService() {
		return inventoryLogService;
	}

	/***
	 * 查询库存盘点列表
	 * 
	 * @param inventoryLog
	 * @param startPage
	 * @param pageSize
	 * @return
	 */
	public PageInfo<InventoryLog> selectInventoryLog(InventoryLog inventoryLog, Integer startPage, Integer pageSize) {
		if (inventoryLog == null) {
			inventoryLog = new InventoryLog();
		}
		Map<String, Object> params = BeanUtil.beanMap(inventoryLog);
		List<WHERE_ENTRY> whEntries = new ArrayList<>();
		if (null != inventoryLog.getCreateBeginTime()) {
			whEntries.add(new WHERE_ENTRY("create_time", MYBATIS_SPECIAL_STRING.GT, inventoryLog.getCreateBeginTime()));
		}
		if (null != inventoryLog.getCreateEndTime()) {
			whEntries.add(new WHERE_ENTRY("create_time", MYBATIS_SPECIAL_STRING.LT, inventoryLog.getCreateEndTime()));
		}
		params.put(MYBATIS_SPECIAL_STRING.WHERE.name(), whEntries);
		params.remove("createBeginTime");
		params.remove("createEndTime");
		PageInfo<InventoryLog> pageInfo = inventoryLogService.queryPageByParam(params,
				new PageInfo<InventoryLog>(startPage, pageSize));
		if (pageInfo != null && CollectionUtils.isNotEmpty(pageInfo.getRows())) {
			List<Long> warehouseIds = new ArrayList<>();
			for (InventoryLog sdo : pageInfo.getRows()) {
				if (sdo.getWarehouseId() != null) {
					warehouseIds.add(sdo.getWarehouseId());
				}

			}
			List<Long> districtIds = new ArrayList<>();
			for (InventoryLog sdo : pageInfo.getRows()) {
				if (sdo.getDistrictId() != null) {
					districtIds.add(sdo.getDistrictId());
				}
			}

			if (CollectionUtils.isNotEmpty(districtIds)) {
				params.clear();
				params.put(MYBATIS_SPECIAL_STRING.COLUMNS.name(),
						"id in(" + StringUtils.join(districtIds, Constant.SPLIT_SIGN.COMMA) + ")");
				List<DistrictInfo> listD = districtInfoService.queryByParamNotEmpty(params);
				if (CollectionUtils.isNotEmpty(listD)) {
					List<InventoryLog> inventoryLogObjs = pageInfo.getRows();
					HashMap<Long, String> mapDIdToName = new HashMap<>();
					for (DistrictInfo ddo : listD) {
						mapDIdToName.put(ddo.getId(), ddo.getName());
					}

					for (int i = 0; i < inventoryLogObjs.size(); i++) {
						String tmpName = mapDIdToName.get(inventoryLogObjs.get(i).getDistrictId());
						if (null != tmpName) {
							inventoryLogObjs.get(i).setAddress(tmpName);
						}
					}
				}
			}

			if (CollectionUtils.isNotEmpty(warehouseIds)) {
				List<Warehouse> listw = warehouseService.queryByIds(warehouseIds);
				if (CollectionUtils.isNotEmpty(listw)) {
					HashMap<Long, String> mapWIdToName = new HashMap<>();
					for (Warehouse wdo : listw) {
						mapWIdToName.put(wdo.getId(), wdo.getName());
					}
					List<InventoryLog> inventoryLogObjs = pageInfo.getRows();
					for (int i = 0; i < inventoryLogObjs.size(); i++) {
						String wName = mapWIdToName.get(inventoryLogObjs.get(i).getWarehouseId());
						if (null != wName) {
							inventoryLogObjs.get(i).setWareHouseName(wName);
						}
					}
				}
			}
		}
		return pageInfo;
	}

	/***
	 * 导出盘点信息
	 * 
	 * @param request
	 * @param response
	 * @param status
	 * @throws InvalidFormatException
	 * @throws IOException
	 */
	public void export(HttpServletRequest request, HttpServletResponse response, InventoryLog inventoryLog)
			throws InvalidFormatException, IOException {
		inventoryLog.setExportCount(1000);
		// 查询数据
		List<InventoryLog> exportList = inventoryLogService.queryLogForExport(inventoryLog);

		List<InventoryLog> wareHouseList = inventoryLogService.queryDistinctWareHouseIdForExport(inventoryLog);

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

				for (InventoryLog ido : wareHouseList) {
					if (!wareHouseIds.contains(ido.getWarehouseId())) {
						wareHouseIds.add(ido.getWarehouseId());
					}
				}
				for (InventoryLog ido : exportList) {
					if (!districtIds.contains(ido.getDistrictId())) {
						districtIds.add(ido.getDistrictId());
					}
				}

				if (CollectionUtils.isNotEmpty(districtIds)) {
					Map<String, Object> params = new HashMap<>();
					params.put(MYBATIS_SPECIAL_STRING.COLUMNS.name(), "id in(" + StringUtils.join(districtIds, Constant.SPLIT_SIGN.COMMA) + ")");
					List<DistrictInfo> listD = districtInfoService.queryByParam(params);
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
				InventoryLog edo = (InventoryLog) exportList.get(i);
				// 第四步，创建单元格，并设置值
				row.createCell(0).setCellValue(edo.getId());
				row.createCell(1).setCellValue(edo.getSku());
				row.createCell(2).setCellValue(edo.getSkuCount());
				row.createCell(3).setCellValue(InputAndOutputType.getDescByCode(edo.getType()));
				if (districtInfoMap.containsKey(edo.getDistrictId())) {
					row.createCell(4).setCellValue(districtInfoMap.get(edo.getDistrictId()).toString());
				}
				if (wareHouseMap.containsKey(edo.getWarehouseId())) {
					row.createCell(8).setCellValue(wareHouseMap.get(edo.getWarehouseId()).getName());
				}

				row.createCell(5).setCellValue(edo.getWhCode());
				row.createCell(6).setCellValue(edo.getOrderCode());
				row.createCell(7).setCellValue(edo.getBatchNo());
				if (edo.getCreateTime() != null) {
					row.createCell(9).setCellValue(DateFormatUtils.format(edo.getCreateTime(), "yyyy-MM-dd HH:mm:ss"));
				} else {
					row.createCell(9).setCellValue("");
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
		style.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 创建一个居中格式
		HSSFCell cell = row.createCell(0);
		cell.setCellValue("序号");
		cell.setCellStyle(style);
		cell = row.createCell(1);
		cell.setCellValue("SKU");
		cell.setCellStyle(style);
		cell = row.createCell(2);
		cell.setCellValue("SKU数量");
		cell.setCellStyle(style);
		cell = row.createCell(3);
		cell.setCellValue("分类");
		cell.setCellStyle(style);
		cell = row.createCell(4);
		cell.setCellValue("区域");
		cell.setCellStyle(style);
		cell = row.createCell(5);
		cell.setCellValue("单号");
		cell.setCellStyle(style);
		cell = row.createCell(6);
		cell.setCellValue("订单号");
		cell.setCellStyle(style);
		cell = row.createCell(7);
		cell.setCellValue("批次号");
		cell.setCellStyle(style);
		cell = row.createCell(8);
		cell.setCellValue("仓库");
		cell.setCellStyle(style);
		cell = row.createCell(9);
		cell.setCellValue("创建时间");
		cell.setCellStyle(style);
		return sheet;
	}

}
