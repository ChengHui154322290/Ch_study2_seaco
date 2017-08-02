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

import com.tp.common.vo.BackendConstant;
import com.tp.common.vo.PageInfo;
import com.tp.common.vo.StorageConstant;
import com.tp.common.vo.DAOConstant.MYBATIS_SPECIAL_STRING;
import com.tp.dto.common.FailInfo;
import com.tp.dto.common.ResultInfo;
import com.tp.dto.ord.kuaidi100.Result;
import com.tp.dto.stg.InventoryDto;
import com.tp.model.prd.ItemSku;
import com.tp.model.stg.Inventory;
import com.tp.model.stg.InventoryAdjustLog;
import com.tp.model.stg.InventoryLog;
import com.tp.model.stg.Warehouse;
import com.tp.model.usr.UserInfo;
import com.tp.proxy.BaseProxy;
import com.tp.service.IBaseService;
import com.tp.service.prd.IItemSkuService;
import com.tp.service.stg.IInventoryAdjustLogService;
import com.tp.service.stg.IInventoryLogService;
import com.tp.service.stg.IInventoryOperService;
import com.tp.service.stg.IInventoryQueryService;
import com.tp.service.stg.IInventoryService;
import com.tp.service.stg.IWarehouseService;
/**
 * 盘盈盘亏操作日志代理层
 * @author szy
 *
 */
@Service
public class InventoryAdjustLogProxy extends BaseProxy<InventoryAdjustLog>{

	@Autowired
	private IInventoryAdjustLogService inventoryAdjustLogService;

	@Autowired
	private IInventoryOperService inventoryOperService;
	
	@Autowired
	private IInventoryLogService inventoryLogService;
	
	@Autowired
	private IWarehouseService warehouseService;
	
	@Autowired
	private IInventoryService inventoryService;
	
	@Autowired
	private IInventoryQueryService inventoryQueryService;
	
	@Autowired
	private IItemSkuService itemSkuService;
	
	@Override
	public IBaseService<InventoryAdjustLog> getService() {
		return inventoryAdjustLogService;
	}
		
	/***
	 * 调整库存
	 */
	public ResultInfo<String> adjustLog(InventoryAdjustLog inventoryAdjustLog,UserInfo user){
		/***校验sku是否存在**/
		String sku = inventoryAdjustLog.getSku();
		Long warehouseId = inventoryAdjustLog.getWarehouseId();
		Integer inventory = inventoryAdjustLog.getQuantity();
		Warehouse warehouseObj = warehouseService.queryById(warehouseId);
		if(null == warehouseObj){
			return new ResultInfo<String>(new FailInfo("仓库不存在,请核实。",1010));
		}
		Long spId = warehouseObj.getSpId();
		ItemSku itemSkuObj = new ItemSku();
		itemSkuObj.setSku(sku);
		List<ItemSku> itemSkuObjs = itemSkuService.queryByObject(itemSkuObj);
		String barCode = null;
		if(CollectionUtils.isNotEmpty(itemSkuObjs)){
			barCode = itemSkuObjs.get(0).getBarcode();
		}
		
		Long userId = 0l;
		if(null!=user){
			userId = user.getId();
		}
		
		List<Inventory>  listSku = inventoryOperService.selectInventoryInfo(sku, spId, warehouseId);
		if(CollectionUtils.isEmpty(listSku)){
			// 不存在，增加一条记录
			ResultInfo<String> resultInfo = inventoryOperService.addInventory(sku,spId,warehouseId,inventory);
			if(Boolean.FALSE == resultInfo.isSuccess()){
				resultInfo.getMsg().setCode(1010);
				return new ResultInfo<String>(resultInfo.getMsg());
			}
			String inventoryId = resultInfo.getData();
			//插入日志表数据
			inventoryAdjustLog.setInventoryId(Long.valueOf(inventoryId));
			inventoryAdjustLog.setCreateDate(new Date());
			inventoryAdjustLog.setCreateUserId(userId);
			inventoryAdjustLog.setAction(StorageConstant.AdjustInventoryConstant.INIT);
			inventoryAdjustLog.setModifyUserId(userId);
			inventoryAdjustLogService.insert(inventoryAdjustLog);
			
			InventoryLog inventoryLogObj = new InventoryLog();
			inventoryLogObj.setWhCode(warehouseObj.getCode());
			inventoryLogObj.setSku(sku);
			inventoryLogObj.setBarcode(barCode);
			inventoryLogObj.setSkuCount(inventoryAdjustLog.getQuantity());
			inventoryLogObj.setType(StorageConstant.InputAndOutputType.PA.getCode());
			inventoryLogObj.setDistrictId(warehouseObj.getDistrictId());
			inventoryLogObj.setSupplierId(spId);
			inventoryLogObj.setWarehouseId(warehouseObj.getId());
			inventoryLogObj.setInventory(inventory);
			inventoryLogService.insert(inventoryLogObj);
			
		}else{
			/**盘亏检验是否超过最小库存**/
			Inventory checkInventoryObj = listSku.get(0);
			
			if(StorageConstant.AdjustInventoryConstant.DECREASE_TYPE.equals(inventoryAdjustLog.getAction())){
				if(checkInventoryObj.getInventory() < inventoryAdjustLog.getQuantity()){
					return new ResultInfo<String>(new FailInfo("盘亏库存不能大于现货库存,请核实。", 1010));
				}
				/***修改库存数据***/
				ResultInfo<String> reduceResult = inventoryOperService.reduceRealInventory(
								checkInventoryObj.getSku(), 
								BackendConstant.SpConstant.SP_XIGOU, 
								checkInventoryObj.getWarehouseId(), 
								inventoryAdjustLog.getQuantity());
				
				if( Boolean.TRUE == reduceResult.isSuccess()){
					//插入日志表数据
					inventoryAdjustLog.setInventoryId(checkInventoryObj.getId());
					inventoryAdjustLog.setCreateDate(new Date());
					inventoryAdjustLog.setCreateUserId(userId);
					inventoryAdjustLog.setModifyUserId(userId);
					inventoryAdjustLogService.insert(inventoryAdjustLog);
					
					//记录出入库流水
					InventoryLog inventoryLogObj = new InventoryLog();
					inventoryLogObj.setWhCode(warehouseObj.getCode());
					inventoryLogObj.setSku(checkInventoryObj.getSku());
					inventoryLogObj.setBarcode(barCode);
					inventoryLogObj.setSkuCount(inventoryAdjustLog.getQuantity());
					inventoryLogObj.setType(StorageConstant.InputAndOutputType.PR.getCode());
					inventoryLogObj.setDistrictId(warehouseObj.getDistrictId());
					inventoryLogObj.setSupplierId(checkInventoryObj.getSpId());
					inventoryLogObj.setWarehouseId(warehouseObj.getId());
					inventoryLogObj.setInventory(checkInventoryObj.getInventory() - inventoryAdjustLog.getQuantity());
					inventoryLogService.insert(inventoryLogObj);
				}
			}else{
				ResultInfo<String> increaseResult =	inventoryOperService.increaseRealInventory(
						checkInventoryObj.getSku(), 
						BackendConstant.SpConstant.SP_XIGOU, 
						checkInventoryObj.getWarehouseId(), 
						inventoryAdjustLog.getQuantity());
				
				if(Boolean.TRUE == increaseResult.isSuccess()){
					//插入日志表数据
					inventoryAdjustLog.setInventoryId(checkInventoryObj.getId());
					inventoryAdjustLog.setCreateDate(new Date());
					inventoryAdjustLog.setCreateUserId(userId);
					inventoryAdjustLog.setModifyUserId(userId);
					inventoryAdjustLogService.insert(inventoryAdjustLog);
					
					//记录出入库流水
					InventoryLog inventoryLogObj = new InventoryLog();
					inventoryLogObj.setWhCode(warehouseObj.getCode());
					inventoryLogObj.setSku(checkInventoryObj.getSku());
					inventoryLogObj.setBarcode(barCode);
					inventoryLogObj.setSkuCount(inventoryAdjustLog.getQuantity());
					inventoryLogObj.setType(StorageConstant.InputAndOutputType.PA.getCode());
					inventoryLogObj.setDistrictId(warehouseObj.getDistrictId());
					inventoryLogObj.setSupplierId(checkInventoryObj.getSpId());
					inventoryLogObj.setWarehouseId(warehouseObj.getId());
					inventoryLogObj.setInventory(checkInventoryObj.getInventory()+inventoryAdjustLog.getQuantity());
					inventoryLogService.insert(inventoryLogObj);
				}
			}	
		}
		return new ResultInfo<String>("盘点成功");
	}
	
	public List<InventoryDto> queryWarehouseBySkuCode(String sku){
		if(StringUtils.isBlank(sku)){
			return new ArrayList<InventoryDto>();
		}
		
		ItemSku itemSkuObj = itemSkuService.selectSkuInfoBySkuCode(sku);
		if(null==itemSkuObj){
			return new ArrayList<InventoryDto>();
		}
		
		//List<Inventory> inventoryObjs = inventoryQueryService.queryBySku(sku);
		Map<String, Object> params = new HashMap<>();
		params.put("sku", sku);
		List<Inventory> inventoryObjs = inventoryService.queryByParamNotEmpty(params);
		
		params.clear();
		params.put("spId", itemSkuObj.getSpId());
		List<Warehouse> warehouseObjs = warehouseService.queryByParamNotEmpty(params);
		List<InventoryDto> inventoryDtos = new ArrayList<InventoryDto>();
		InventoryDto inventoryDto = null;
		for (Warehouse warehouseObj : warehouseObjs) {
			inventoryDto = new InventoryDto();
			inventoryDto.setDistrictId(warehouseObj.getDistrictId());
			inventoryDto.setWarehouseName(warehouseObj.getName());
			inventoryDto.setSku(sku);
			inventoryDto.setSpId(itemSkuObj.getSpId());
			inventoryDto.setWarehouseId(warehouseObj.getId());
			if(CollectionUtils.isNotEmpty(inventoryObjs)){
				for (Inventory idto : inventoryObjs) {
					if(idto.getWarehouseId().longValue()==warehouseObj.getId().longValue()){
						inventoryDto.setInventory(idto.getInventory());
						inventoryDto.setOccupy(idto.getOccupy());
						inventoryDto.setRealInventory(idto.getInventory());
					}
				}
			}
			inventoryDtos.add(inventoryDto);
		}
		return inventoryDtos;
	}
	
	
	
	/***
	 * 导出盘点信息
	 * @param request
	 * @param response
	 * @param fileId
	 * @param id
	 * @param status
	 * @throws InvalidFormatException
	 * @throws IOException
	 */
	public void export(HttpServletRequest request,
			HttpServletResponse response,InventoryAdjustLog inventoryAdjustLog)
			throws InvalidFormatException, IOException {
		inventoryAdjustLog.setExportCount(1000);
		//查询数据
		List<InventoryAdjustLog>  exportList =  inventoryAdjustLogService.queryAdjustLogForExport(inventoryAdjustLog);
		HSSFWorkbook wb = new HSSFWorkbook();  
		HSSFRow row;
		HSSFSheet sheet = setExportStyle(wb);  
		if(CollectionUtils.isNotEmpty(exportList)){
	        for (int i = 0; i < exportList.size(); i++)  
	        {  
	            row = sheet.createRow((int) i + 1);  
	            InventoryAdjustLog edo = (InventoryAdjustLog) exportList.get(i);  
	            // 第四步，创建单元格，并设置值  
	            row.createCell(0).setCellValue(edo.getId());  
	            row.createCell(1).setCellValue(edo.getSku());  
	            if(StorageConstant.AdjustInventoryConstant.INCREASE_TYPE.equals(edo.getAction())){
	            	   row.createCell(2).setCellValue("盘盈");  
	            }else{
	            	  row.createCell(2).setCellValue("盘亏");  
	            }
	            row.createCell(3).setCellValue(edo.getQuantity());  
	            if(edo.getCreateDate() !=null){
		            row.createCell(4).setCellValue(DateFormatUtils.format(edo.getCreateDate(), "yyyy-MM-dd HH:mm:ss")  );  
	            }else{
	            	 row.createCell(4).setCellValue("");
	            }
	            row.createCell(5).setCellValue(edo.getRemark());  
	        }  
		}
		
		setResponseInfo(response, wb);
	}



	/***
	 * 设置输出流
	 * @param response
	 * @param wb
	 * @throws IOException
	 */
	private void setResponseInfo(HttpServletResponse response, HSSFWorkbook wb)
			throws IOException {
		response.setContentType("text/html;charset=utf-8");
		OutputStream ouputStream = response.getOutputStream();  
		try {
			response.setHeader("Cache-Control", "no-cache");
			response.setHeader("Pragma", "no-cache");
			response.setDateHeader("Expires", -1);
			response.setCharacterEncoding("utf-8");
			response.addHeader("Content-Disposition", "attachment; filename=".concat(DateFormatUtils.format(new Date(), "yyyy-MM-dd")).concat(".xls")); 
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
	 * @param wb
	 * @return
	 */
	private HSSFSheet setExportStyle(HSSFWorkbook wb) {
		HSSFSheet sheet = wb.createSheet("盘点记录");  
		
	    HSSFRow row = sheet.createRow((int) 0);  
        HSSFCellStyle style = wb.createCellStyle();  
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 创建一个居中格式  
        @SuppressWarnings("deprecation")
		HSSFCell cell = row.createCell(0);  
        cell.setCellValue("序号");  
        cell.setCellStyle(style);  
        cell = row.createCell(1);  
        cell.setCellValue("SKU");  
        cell.setCellStyle(style);  
        cell =row.createCell(2);  
        cell.setCellValue("类型");  
        cell.setCellStyle(style);  
        cell = row.createCell(3);  
        cell.setCellValue("盘点数量");  
        cell.setCellStyle(style);  
        cell = row.createCell(4);  
        cell.setCellValue("盘点时间");  
        cell.setCellStyle(style);  
        cell = row.createCell(5);  
        cell.setCellValue("备注");  
        cell.setCellStyle(style);
		return sheet;
	}

	public List<Warehouse> queryWarehouseByIds(List<Long> warehouseIds) {
		if(CollectionUtils.isEmpty(warehouseIds)){
			return new ArrayList<Warehouse>();
		}
		return warehouseService.queryByIds(warehouseIds);
	}
}
