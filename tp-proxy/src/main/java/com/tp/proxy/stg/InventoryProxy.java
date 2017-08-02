package com.tp.proxy.stg;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.sf.json.JSONObject;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.vo.DAOConstant;
import com.tp.common.vo.PageInfo;
import com.tp.common.vo.prd.ItemConstant;
import com.tp.common.vo.supplier.entry.SupplierType;
import com.tp.dto.common.FailInfo;
import com.tp.dto.common.ResultInfo;
import com.tp.dto.stg.InventoryDto;
import com.tp.dto.stg.InventoryManageDto;
import com.tp.dto.stg.query.InventoryQuery;
import com.tp.model.bse.Brand;
import com.tp.model.prd.ItemSku;
import com.tp.model.stg.Inventory;
import com.tp.model.stg.InventoryManageLog;
import com.tp.model.stg.InventoryOccupy;
import com.tp.model.stg.Warehouse;
import com.tp.model.sup.SupplierInfo;
import com.tp.proxy.BaseProxy;
import com.tp.result.sup.SupplierResult;
import com.tp.service.IBaseService;
import com.tp.service.prd.IItemSkuService;
import com.tp.service.stg.IInventoryOperService;
import com.tp.service.stg.IInventoryQueryService;
import com.tp.service.stg.IInventoryService;
import com.tp.service.stg.IWarehouseService;
import com.tp.service.sup.IPurchasingManagementService;
/**
 * 库存信息表 记录sku的总库存、总销售占用库存信息代理层
 * @author szy
 *
 */
@Service
public class InventoryProxy extends BaseProxy<Inventory>{

	@Autowired
	private IInventoryService inventoryService;
	@Autowired
	private IInventoryOperService inventoryOperService;
	@Autowired
	private IWarehouseService warehouseService;
	@Autowired
	private IInventoryQueryService inventoryQueryService;
	
	@Autowired
	private IItemSkuService itemSkuService;
	
	@Autowired
	private IPurchasingManagementService purchasingManagementService;
	
	@Override
	public IBaseService<Inventory> getService() {
		return inventoryService;
	}
	
	public ResultInfo<PageInfo<InventoryDto>> queryInventoryInfo(InventoryQuery query, PageInfo<InventoryDto> pageInfo){
		//PageInfo<InventoryDto> pageResult = inventoryQueryService.queryAvailableForSaleBySkuSpIdAndWId(query, pageInfo.getPage(), pageInfo.getSize());
		PageInfo<InventoryDto> pageResult = inventoryQueryService.queryPageAvailableInventory(query, pageInfo);
		if (pageResult == null) {
			return new ResultInfo<>(new FailInfo("查询失败"));
		}
		return new ResultInfo<>(pageResult);
	}
	
	/****
	 * 查询库存信息
	 * @param skuCode
	 * @return
	 * @throws Exception
	 */
//	public PageInfo<InventoryDto> selectAvailableForSaleBySku(InventoryDto query,int page,int pageSize){
//		InventoryQuery dtoQuery = new InventoryQuery();		
//		//sku不为空 则只需sku查询
//		if(StringUtils.isNotBlank(skuCode)){
//			dtoQuery.setSku(skuCode);
//			return inventoryQueryService.queryAvailableForSaleBySkuSpIdAndWId(dtoQuery, page, pageSize);
//		}
//		
//		//条形码不为空，根据条形码获得sku列表
//		List<String> skuList = new ArrayList<String>();
//		if(StringUtils.isNotBlank(barcode)){
//			List<ItemSku> itemSkuObjs = itemSkuService.selectSkuListByBarcode(barcode);
//			if(CollectionUtils.isNotEmpty(itemSkuObjs)){
//				for (ItemSku skuDO : itemSkuObjs) {
//					skuList.add(skuDO.getSku());
//				}
//			}
//		}
//		if(skuList.size()>0){
//			List<InventoryQuery> inventoryQueries = new ArrayList<InventoryQuery>();
//			for (String sku : skuList) {
//				InventoryQuery inventoryQuery = new InventoryQuery();
//				inventoryQuery.setSku(sku);
//				inventoryQuery.setSpId(spId);
//				inventoryQueries.add(inventoryQuery);
//			}
//			return inventoryQueryService.queryAvailableForSaleBySkuSpIdAndWIdList(inventoryQueries,page,pageSize);
//		}
//		
//		//条形码为空或条形码下对应的sku列表为空则只按照供应商查询
//		dtoQuery.setSpId(spId);
//		return inventoryQueryService.queryAvailableForSaleBySkuSpIdAndWId(dtoQuery,page,pageSize);
//}



	public List<ItemSku> selectSkuInfo(List<String> skuCodes) {
		if(CollectionUtils.isEmpty(skuCodes)){
			return null;
		}
		return itemSkuService.selectSkuListBySkuCode(skuCodes);
	}
	
	/**
	 * 
	 * <pre>
	 *   初始化商家
	 * </pre>
	 *
	 * @throws Exception
	 */
	public List<SupplierInfo> initSupplierList(){
		// 查询所有的品牌
		Brand brand = new Brand();
		brand.setStatus(1);
		List<SupplierType> supplierTypes = new ArrayList<SupplierType>();
		supplierTypes.add(SupplierType.ASSOCIATE);
		SupplierResult result = purchasingManagementService.getSuppliersByTypes(supplierTypes, 1, Integer.MAX_VALUE);
		List<SupplierInfo> supplierList  = new ArrayList<SupplierInfo>();
		SupplierInfo supplier = new SupplierInfo();
		supplier.setId(ItemConstant.SUPPLIER_ID);
		supplier.setName(ItemConstant.SUPPLIER_NAME);
		supplierList.add(supplier);
		List<SupplierInfo> list = result.getSupplierInfoList();
		if(CollectionUtils.isNotEmpty(list)){
			supplierList.addAll(list);
		}		
		return supplierList;
	}



	public PageInfo<InventoryOccupy> queryOccupyInventoryList(String sku,Integer page,Integer size) {
		if(StringUtils.isBlank(sku)){
			return null;
		}
		return inventoryQueryService.queryPageOccupyInfoBySku(sku, page, size);
	}
	
	public InventoryManageDto getInventoryById(Map<String, Object> params) {
		return inventoryService.getInventoryById(params);
	}
	
	public int updateInventory(Inventory inventory) {
		return inventoryOperService.updateInventory(inventory);
	}
	
	/**
	 * 列表查询
	 * @param inputStream 
	 * @param query
	 * @param pageInfo
	 * @return
	 */
	public Workbook exportInventory(FileInputStream inputStream, InventoryQuery query){
		// 获取数据
		List<InventoryDto> list = inventoryQueryService.queryAvailableInventory(query);
		List<String> skuCodes = new ArrayList<String>();
		for (InventoryDto dto : list) {
			skuCodes.add(dto.getSku());
		}
		List<ItemSku> itemResultDto = itemSkuService.selectSkuListBySkuCode(skuCodes);			
		if(null!=itemResultDto){
			for (InventoryDto inventoryDto : list) {
				for (ItemSku resultDto : itemResultDto) {
					if(inventoryDto.getSku().equals(resultDto.getSku())){
						inventoryDto.setBarcode(resultDto.getBarcode());
						inventoryDto.setMainTitle(resultDto.getDetailName());
						inventoryDto.setMainTitle(resultDto.getSpuName());
						inventoryDto.setSpName(resultDto.getSpName());	
					}
				}
			}
		}
		Workbook wb = null; 
		try {
			wb = new XSSFWorkbook(inputStream);
			
			Sheet sheet = wb.getSheetAt(0);
			
			int rowNum = 1;
			for (InventoryDto inventoryDto : list) {
				Row row = sheet.createRow(rowNum);
				// 序号
				row.createCell(0).setCellValue(rowNum++);
				// 供应商	
				row.createCell(1).setCellValue(inventoryDto.getSpName());
				// 仓库
				row.createCell(2).setCellValue(inventoryDto.getWarehouseName());
				// 仓库
				row.createCell(3).setCellValue(inventoryDto.getWarehouseCode());
				// 商品标题
				row.createCell(4).setCellValue(inventoryDto.getMainTitle());
				// SKU
				row.createCell(5).setCellValue(inventoryDto.getSku());
				// 条形码
				row.createCell(6).setCellValue(inventoryDto.getBarcode());
				// 商品总库存数
				row.createCell(7).setCellValue(inventoryDto.getRealInventory());
				// 商品预留库存数
				row.createCell(8).setCellValue(inventoryDto.getReserveInventory());
				// 商品可售库存数
				row.createCell(9).setCellValue(inventoryDto.getInventory());
				// 商品预警库存数
				row.createCell(10).setCellValue(inventoryDto.getWarnInventory());
			}
			
		} catch (IOException e) {
			// 解析Excel出错
			e.printStackTrace();
		} 
		return wb;
	}
	
	
	
	
	
	

	/**
	 * 解析excel数据
	 * @param workbook
	 */
	public JSONObject importTemplate(Workbook workbook,String userName ) {
		JSONObject result = new JSONObject();
		//获得当前sheet工作表  
        Sheet sheet = workbook.getSheetAt(0); 
        if(sheet == null){  
        	logger.error("模板错误");
    		result.put("status", false);
    		result.put("msg", "模板错误");
    		return result;
        } 
        // 校验标题
        String[] title = {"sku","仓库编码","初始库存","预留库存","预警库存"};
        Row titleRow = sheet.getRow(0);
        for(int i=0;i<5;i++){
        	String cellValue = getCellValue(titleRow.getCell(i));
        	if(cellValue==null||"".equals(cellValue.trim())||!title[i].equals(cellValue.trim())){
        		logger.error("模板错误");
        		result.put("status", false);
        		result.put("msg", "模板错误");
        		return result;
        	}
        }
        
        // 解析导入数据
        //Set<String> warehouseNameSet=new HashSet<String>();
        Set<String> warehouseCodeSet=new HashSet<String>();
        Set<String> skuSet=new HashSet<String>();
        Set<String> skuAndwIdSet=new HashSet<String>();
        String skuAndwId = "";
        ArrayList<Inventory> inventoryList = new ArrayList<Inventory>(); // 库存容器
        int startRowNum=1;
        int startCellNum=0;
        try {
        	int lastRowNum = sheet.getLastRowNum(); // 获取最后一行
            for(startRowNum=1;startRowNum<=lastRowNum;startRowNum++){
            	Row row = sheet.getRow(startRowNum);
            	Inventory inventory = new Inventory();
            	for(startCellNum=0;startCellNum<5;startCellNum++){
            		Cell cell = row.getCell(startCellNum);
            		String cellValue = getCellValue(cell).trim();
            		if("".equals(cellValue)){
            			logger.error("导入库存错误：第"+(startRowNum+1)+"行第"+(startCellNum+1)+"列数据有误");
                		result.put("status", false);
                		result.put("msg", "导入库存错误：第"+(startRowNum+1)+"行第"+(startCellNum+1)+"列数据有误");
                		return result;
            		}
            		switch (startCellNum) {
    				case 0:// sku
    					inventory.setSku(cellValue);
    					skuSet.add(cellValue);
    					skuAndwId = cellValue;
    					break;
    				case 1:// 仓库编码
    					//warehouseNameSet.add("'"+cellValue+"'");
    					warehouseCodeSet.add("'"+cellValue+"'");
    					//inventory.setWarehouseName(cellValue);
    					inventory.setWarehouseCode(cellValue);
    					skuAndwIdSet.add(skuAndwId+"_"+cellValue);
    					break;
    				case 2:// 初始库存
    					inventory.setInventory(Integer.parseInt(cellValue));
    					break;
    				case 3:// 预留库存
    					inventory.setReserveInventory(Integer.parseInt(cellValue));
    					break;
    				case 4:// 预警库存
    					inventory.setWarnInventory(Integer.parseInt(cellValue));
    					break;
    				default:
    					break;
    				}
                }
            	if(inventory.getInventory()<0){
            		logger.error("导入库存失败：初始库存不能小于0 -- sku："+inventory.getSku());
            		result.put("status", false);
            		result.put("msg", "导入库存失败：初始库存不能小于0 -- sku："+inventory.getSku());
            		return result;
            	}
            	if(inventory.getInventory()<inventory.getReserveInventory()){
            		logger.error("导入库存失败：初始库存不能小于预留库存 -- sku："+inventory.getSku());
            		result.put("status", false);
            		result.put("msg", "导入库存失败：初始库存不能小于预留库存 -- sku："+inventory.getSku());
            		return result;
            	}
            	if(skuAndwIdSet.size()!=startRowNum){
                	logger.error("导入库存错误：SKU+仓库编号重复 -- "+inventory.getSku()+"_"+inventory.getWarehouseCode());
            		result.put("status", false);
            		result.put("msg", "导入库存错误：SKU+仓库编号重复 -- "+inventory.getSku()+"_"+inventory.getWarehouseCode());
            		return result;
                }
            	inventoryList.add(inventory);
            }
            
		} catch (Exception e) {
			logger.error("导入库存错误：第"+(startRowNum+1)+"行第"+(startCellNum+1)+"列数据有误");
    		result.put("status", false);
    		result.put("msg", "导入库存错误：第"+(startRowNum+1)+"行第"+(startCellNum+1)+"列数据有误");
    		return result;
		}
        List<String> warehouseCodeList = new ArrayList<String>(warehouseCodeSet); // 
        //List<String> warehouseNameList = new ArrayList<String>(warehouseNameSet); // 
        List<String> skuList = new ArrayList<String>(skuSet); // 
        
	    // 组装数据 -- 仓库
		Map<String, Object> params = new HashMap<String, Object>();
		List<DAOConstant.WHERE_ENTRY> whereList = new ArrayList<DAOConstant.WHERE_ENTRY>();
		whereList.add(new DAOConstant.WHERE_ENTRY("code", DAOConstant.MYBATIS_SPECIAL_STRING.INLIST, warehouseCodeList));
		params.put(DAOConstant.MYBATIS_SPECIAL_STRING.WHERE.name(), whereList);
		List<Warehouse> warehouses = warehouseService.queryByParam(params);
		HashMap<String, Warehouse> warehouseTempMap = new HashMap<String, Warehouse>(); // sku临时表
		for (Warehouse warehouse : warehouses) {
			warehouseTempMap.put(warehouse.getCode(), warehouse);
		}

        // 组装数据 -- sku
        params.clear();
		whereList.clear();
        whereList.add(new DAOConstant.WHERE_ENTRY("sku", DAOConstant.MYBATIS_SPECIAL_STRING.INLIST, skuList));
        params.put(DAOConstant.MYBATIS_SPECIAL_STRING.WHERE.name(), whereList);
		List<ItemSku> itemSkus = itemSkuService.queryByParam(params); // 查询sku信息
		HashMap<String, ItemSku> itemSkuTempMap = new HashMap<String, ItemSku>(); // sku临时表
		for (ItemSku itemSku : itemSkus) {
			itemSkuTempMap.put(itemSku.getSku(), itemSku);
		}
		
        // 查询数据库中已有数据
        Map<String,Integer> skuwInventoryTempMap = new HashMap<String,Integer>();
        List<Inventory> list = inventoryService.queryByParamNotEmpty(new HashMap<String, Object>());
        for (Inventory inventory : list) {
        	String key = inventory.getSku()+"_"+inventory.getWarehouseId();
        	skuwInventoryTempMap.put(key, 1);
		}
        
		// 组装数据，并校验
		Date date = new Date();
		ArrayList<InventoryManageLog> InventoryManageLogList = new ArrayList<InventoryManageLog>(); // 库存日志容器
		for (Inventory inventory : inventoryList) {
			String sku = inventory.getSku();
			//String warehouseName = inventory.getWarehouseName();
			String warehouseCode = inventory.getWarehouseCode();
			
			// 获取仓库信息
			Warehouse warehouse = null;
			if(warehouseTempMap.containsKey(warehouseCode)&&warehouseTempMap.get(warehouseCode)!=null){
				warehouse = warehouseTempMap.get(warehouseCode);
			}else{
		        result.put("status", false);
				result.put("msg", "导入库存错误：无此仓库信息 -- "+warehouseCode);
				return result;
			}
			
			// 获取sku信息
			ItemSku itemSku = null;
			if(itemSkuTempMap.containsKey(sku)&&itemSkuTempMap.get(sku)!=null){
				itemSku = itemSkuTempMap.get(sku);
			}else{
		        result.put("status", false);
				result.put("msg", "导入库存错误：无此sku信息 -- "+sku);
				return result;
			}
			
			// 校验数据库中是否已有此库存记录
			if(skuwInventoryTempMap.containsKey(sku+"_"+warehouse.getId())){
				result.put("status", false);
				result.put("msg", "导入库存失败：已经存在此数据 sku-"+sku+";"+"仓库编码-"+warehouseCode);
				return result;
			}
			
			// 校验 sku 供应商 仓库信息一致
			if(warehouse.getSpId()==itemSku.getSpId()){
				inventory.setSpId(warehouse.getSpId());
				inventory.setWarehouseId(warehouse.getId());
				inventory.setDistrictId(warehouse.getDistrictId());
				inventory.setCreateTime(date);
				inventory.setModifyTime(date);
				// 日志
				InventoryManageLog inventoryManageLog = new InventoryManageLog(inventory, itemSku.getSpuName(), warehouse.getName(), itemSku.getSpName());
				inventoryManageLog.setChangeType(1); // 更改标志：1：新增，2：修改；3：删除'
				inventoryManageLog.setCreateUser(userName);
				InventoryManageLogList.add(inventoryManageLog);
			}else{
				result.put("status", false);
				result.put("msg", "导入库存错误：信息不一致  sku("+sku+")-供应商("+itemSku.getSpName()+"); 仓库编码("+warehouseCode+")-供应商("+warehouse.getSpName()+");");
				return result;
			}
		}
		
		int count = inventoryOperService.importInventory(inventoryList,InventoryManageLogList);
        if(count==1){
            result.put("status", true);
    		result.put("msg", "导入成功："+inventoryList.size()+"条数据");
        }else{
            result.put("status", true);
    		result.put("msg", "导入失败");
        }
		return result;
		
	}
	
	/**
	 * 解析excel数据 -- 批量修改
	 * @param workbook
	 */
	public JSONObject importUpdateTemplate(Workbook workbook,String userName ) {
		JSONObject result = new JSONObject();
		//获得当前sheet工作表  
        Sheet sheet = workbook.getSheetAt(0); 
        if(sheet == null){  
        	logger.error("模板错误");
    		result.put("status", false);
    		result.put("msg", "模板错误");
    		return result;
        } 
        // 校验标题
        String[] title = {"sku","仓库编码","总库存","预留库存","预警库存"};
        Row titleRow = sheet.getRow(0);
        for(int i=0;i<5;i++){
        	String cellValue = getCellValue(titleRow.getCell(i));
        	if(cellValue==null||"".equals(cellValue.trim())||!title[i].equals(cellValue.trim())){
        		logger.error("模板错误");
        		result.put("status", false);
        		result.put("msg", "模板错误");
        		return result;
        	}
        }
        
        // 解析导入数据
        //Set<String> warehouseNameSet=new HashSet<String>();
        Set<String> warehouseCodeSet=new HashSet<String>();
        Set<String> skuSet=new HashSet<String>();
        Set<String> skuAndwIdSet=new HashSet<String>();
        String skuAndwId = "";
        ArrayList<Inventory> inventoryList = new ArrayList<Inventory>(); // 库存容器
        int startRowNum=1;
        int startCellNum=0;
        try {
        	int lastRowNum = sheet.getLastRowNum(); // 获取最后一行
            for(startRowNum=1;startRowNum<=lastRowNum;startRowNum++){
            	Row row = sheet.getRow(startRowNum);
            	Inventory inventory = new Inventory();
            	for(startCellNum=0;startCellNum<5;startCellNum++){
            		Cell cell = row.getCell(startCellNum);
            		String cellValue = getCellValue(cell).trim();
            		if("".equals(cellValue)){
            			logger.error("导入库存错误：第"+(startRowNum+1)+"行第"+(startCellNum+1)+"列数据有误");
                		result.put("status", false);
                		result.put("msg", "导入库存错误：第"+(startRowNum+1)+"行第"+(startCellNum+1)+"列数据有误");
                		return result;
            		}
            		switch (startCellNum) {
    				case 0:// sku
    					inventory.setSku(cellValue);
    					skuSet.add(cellValue);
    					skuAndwId = cellValue;
    					break;
    				case 1:// 仓库编码
    					//warehouseNameSet.add("'"+cellValue+"'");
    					warehouseCodeSet.add("'"+cellValue+"'");
    					//inventory.setWarehouseName(cellValue);
    					inventory.setWarehouseCode(cellValue);
    					skuAndwIdSet.add(skuAndwId+"_"+cellValue);
    					break;
    				case 2:// 总库存
    					inventory.setInventory(Integer.parseInt(cellValue));
    					break;
    				case 3:// 预留库存
    					inventory.setReserveInventory(Integer.parseInt(cellValue));
    					break;
    				case 4:// 预警库存
    					inventory.setWarnInventory(Integer.parseInt(cellValue));
    					break;
    				default:
    					break;
    				}
                }
            	if(inventory.getInventory()<0){
            		logger.error("导入库存失败：初始库存不能小于0 -- sku："+inventory.getSku());
            		result.put("status", false);
            		result.put("msg", "导入库存失败：初始库存不能小于0 -- sku："+inventory.getSku());
            		return result;
            	}
            	if(inventory.getInventory()<inventory.getReserveInventory()){
            		logger.error("导入库存失败：初始库存不能小于预留库存 -- sku："+inventory.getSku());
            		result.put("status", false);
            		result.put("msg", "导入库存失败：初始库存不能小于预留库存 -- sku："+inventory.getSku());
            		return result;
            	}
            	if(skuAndwIdSet.size()!=startRowNum){
                	logger.error("导入库存错误：SKU+仓库编号重复 -- "+inventory.getSku()+"_"+inventory.getWarehouseCode());
            		result.put("status", false);
            		result.put("msg", "导入库存错误：SKU+仓库编号重复 -- "+inventory.getSku()+"_"+inventory.getWarehouseCode());
            		return result;
                }
            	inventoryList.add(inventory);
            }
            
		} catch (Exception e) {
			logger.error("导入库存错误：第"+(startRowNum+1)+"行第"+(startCellNum+1)+"列数据有误");
    		result.put("status", false);
    		result.put("msg", "导入库存错误：第"+(startRowNum+1)+"行第"+(startCellNum+1)+"列数据有误");
    		return result;
		}
        List<String> warehouseCodeList = new ArrayList<String>(warehouseCodeSet); // 
        //List<String> warehouseNameList = new ArrayList<String>(warehouseNameSet); // 
        List<String> skuList = new ArrayList<String>(skuSet); // 
        
	    // 组装数据 -- 仓库
		Map<String, Object> params = new HashMap<String, Object>();
		List<DAOConstant.WHERE_ENTRY> whereList = new ArrayList<DAOConstant.WHERE_ENTRY>();
		whereList.add(new DAOConstant.WHERE_ENTRY("code", DAOConstant.MYBATIS_SPECIAL_STRING.INLIST, warehouseCodeList));
		params.put(DAOConstant.MYBATIS_SPECIAL_STRING.WHERE.name(), whereList);
		List<Warehouse> warehouses = warehouseService.queryByParam(params);
		HashMap<String, Warehouse> warehouseTempMap = new HashMap<String, Warehouse>(); // sku临时表
		for (Warehouse warehouse : warehouses) {
			warehouseTempMap.put(warehouse.getCode(), warehouse);
		}

        // 组装数据 -- sku
        params.clear();
		whereList.clear();
        whereList.add(new DAOConstant.WHERE_ENTRY("sku", DAOConstant.MYBATIS_SPECIAL_STRING.INLIST, skuList));
        params.put(DAOConstant.MYBATIS_SPECIAL_STRING.WHERE.name(), whereList);
		List<ItemSku> itemSkus = itemSkuService.queryByParam(params); // 查询sku信息
		HashMap<String, ItemSku> itemSkuTempMap = new HashMap<String, ItemSku>(); // sku临时表
		for (ItemSku itemSku : itemSkus) {
			itemSkuTempMap.put(itemSku.getSku(), itemSku);
		}
		
        // 查询数据库中已有数据
//        Map<String,Inventory> skuwInventoryTempMap = new HashMap<String,Inventory>();
//        List<Inventory> list = inventoryService.queryByParamNotEmpty(new HashMap<String, Object>());
		Map<String,InventoryDto> skuwInventoryTempMap = new HashMap<String,InventoryDto>();
        InventoryQuery query = new InventoryQuery();
		// 获取数据
     	List<InventoryDto> list = inventoryQueryService.queryAvailableInventory(query );
        for (InventoryDto inventory : list) {
        	String key = inventory.getSku()+"_"+inventory.getWarehouseId();
        	skuwInventoryTempMap.put(key, inventory);
		}
        
		// 组装数据，并校验
		Date date = new Date();
		ArrayList<InventoryManageLog> InventoryManageLogList = new ArrayList<InventoryManageLog>(); // 库存日志容器
		for (Inventory inventory : inventoryList) {
			String sku = inventory.getSku();
			//String warehouseName = inventory.getWarehouseName();
			String warehouseCode = inventory.getWarehouseCode();
			
			// 获取仓库信息
			Warehouse warehouse = null;
			if(warehouseTempMap.containsKey(warehouseCode)&&warehouseTempMap.get(warehouseCode)!=null){
				warehouse = warehouseTempMap.get(warehouseCode);
			}else{
		        result.put("status", false);
				result.put("msg", "导入库存错误：无此仓库信息 -- "+warehouseCode);
				return result;
			}
			
			// 获取sku信息
			ItemSku itemSku = null;
			if(itemSkuTempMap.containsKey(sku)&&itemSkuTempMap.get(sku)!=null){
				itemSku = itemSkuTempMap.get(sku);
			}else{
		        result.put("status", false);
				result.put("msg", "导入库存错误：无此sku信息 -- "+sku);
				return result;
			}
			
			// 校验数据库中是否已有此库存记录
			if(skuwInventoryTempMap.containsKey(sku+"_"+warehouse.getId())){
				InventoryDto inventoryDB = skuwInventoryTempMap.get(sku+"_"+warehouse.getId());
				Integer occupy = inventoryDB.getOccupy();
				Integer currInventory = inventory.getInventory()-occupy;
				if(currInventory-inventory.getReserveInventory()>=0){
					inventory.setId(inventoryDB.getId());
					inventory.setInventory(currInventory);
				}else{
					result.put("status", false);
					result.put("msg", "导入库存失败：总库存不能小于冻结库存+预留库存，冻结库存-"+occupy+" sku-"+sku+";"+"仓库编码-"+warehouseCode);
					return result;
				}
				
			}else{
				result.put("status", false);
				result.put("msg", "导入库存失败：不存在此数据 sku-"+sku+";"+"仓库编码-"+warehouseCode);
				return result;
			}
			
			// 校验 sku 供应商 仓库信息一致
			if(warehouse.getSpId()==itemSku.getSpId()){
				inventory.setSpId(warehouse.getSpId());
				inventory.setWarehouseId(warehouse.getId());
				inventory.setDistrictId(warehouse.getDistrictId());
				inventory.setCreateTime(date);
				inventory.setModifyTime(date);
				// 日志
				InventoryManageLog inventoryManageLog = new InventoryManageLog(inventory, itemSku.getSpuName(), warehouse.getName(), itemSku.getSpName());
				inventoryManageLog.setChangeType(2); // 更改标志：1：新增，2：修改；3：删除'
				inventoryManageLog.setCreateUser(userName);
				InventoryManageLogList.add(inventoryManageLog);
			}else{
				result.put("status", false);
				result.put("msg", "导入库存错误：信息不一致  sku("+sku+")-供应商("+itemSku.getSpName()+"); 仓库编码("+warehouseCode+")-供应商("+warehouse.getSpName()+");");
				return result;
			}
		}
		
		int count = inventoryOperService.importUpdateInventory(inventoryList,InventoryManageLogList);
        if(count==1){
            result.put("status", true);
    		result.put("msg", "导入成功："+inventoryList.size()+"条数据");
        }else{
            result.put("status", true);
    		result.put("msg", "导入失败");
        }
		return result;
		
	}
	/**
	 * 解析excel单元格
	 * @param cell
	 * @return
	 */
    public static String getCellValue(Cell cell){  
        String cellValue = "";  
        if(cell == null){  
            return cellValue;  
        }  
        //把数字当成String来读，避免出现1读成1.0的情况  
        if(cell.getCellType() == Cell.CELL_TYPE_NUMERIC){  
            cell.setCellType(Cell.CELL_TYPE_STRING);  
        }  
        //判断数据的类型  
        switch (cell.getCellType()){  
            case Cell.CELL_TYPE_NUMERIC: //数字  
                cellValue = String.valueOf(cell.getNumericCellValue());  
                break;  
            case Cell.CELL_TYPE_STRING: //字符串  
                cellValue = String.valueOf(cell.getStringCellValue());  
                break;  
            case Cell.CELL_TYPE_BOOLEAN: //Boolean  
                cellValue = String.valueOf(cell.getBooleanCellValue());  
                break;  
            case Cell.CELL_TYPE_FORMULA: //公式  
                cellValue = String.valueOf(cell.getCellFormula());  
                break;  
            case Cell.CELL_TYPE_BLANK: //空值   
                cellValue = "";  
                break;  
            case Cell.CELL_TYPE_ERROR: //故障  
                //cellValue = "非法字符";
                break;  
            default:  
                //cellValue = "未知类型";  
                break;  
        }  
        return cellValue;  
    }  
}
