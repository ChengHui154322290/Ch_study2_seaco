package com.tp.backend.controller.stg;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.tp.backend.controller.AbstractBaseController;
import com.tp.common.vo.DAOConstant.MYBATIS_SPECIAL_STRING;
import com.tp.common.vo.PageInfo;
import com.tp.dto.common.ResultInfo;
import com.tp.dto.stg.InventoryDto;
import com.tp.dto.stg.InventoryManageDto;
import com.tp.dto.stg.query.InventoryQuery;
import com.tp.model.prd.ItemSku;
import com.tp.model.stg.Inventory;
import com.tp.model.stg.InventoryManageLog;
import com.tp.model.stg.Warehouse;
import com.tp.model.sup.SupplierInfo;
import com.tp.proxy.prd.ItemSkuProxy;
import com.tp.proxy.stg.InventoryLogProxy;
import com.tp.proxy.stg.InventoryManageLogProxy;
import com.tp.proxy.stg.InventoryProxy;
import com.tp.proxy.stg.WarehouseProxy;
import com.tp.proxy.sup.QuotationImportProxy;
import com.tp.util.DateUtil;


/**
 * 库存管理
 *
 */
@Controller
@RequestMapping("storage/inventory/manage")
public class InventoryManageController extends AbstractBaseController{
	
	@Autowired
	private InventoryProxy inventoryProxy;
	@Autowired
	private InventoryLogProxy inventoryLogProxy;
	@Autowired
	private InventoryManageLogProxy inventoryManageLogProxy;
	@Autowired
	private WarehouseProxy warehouseProxy;
	
	@Autowired
	private ItemSkuProxy skuProxy;
	
	
	/***
	 * @param model
	 * @param specDO
	 * @throws Exception 
	 */
	@RequestMapping(value = "/list")
	public ModelAndView list(InventoryQuery query,String chooseSection,Integer usableInventory, Integer page, Integer size) throws Exception{	
		ModelAndView modelAndView = new ModelAndView("storage/inventory/manage/list");
		// 1. 查询条件
		if(chooseSection!=null){
			modelAndView.addObject("chooseSection", chooseSection);
			if("min".equals(chooseSection)){
				query.setMinUsableInventory(usableInventory);
				modelAndView.addObject("usableInventory",usableInventory);
			}
			if("max".equals(chooseSection)){
				query.setMaxUsableInventory(usableInventory);
				modelAndView.addObject("usableInventory",usableInventory);
			}
		}
		if(query!=null){
			if(StringUtils.isBlank(query.getSku())){
				query.setSpuName(null);
			}
			modelAndView.addObject("query",query);
		}
		
		// 2. 查询列表
		ResultInfo<PageInfo<InventoryDto>> pageResult = inventoryProxy.queryInventoryInfo(query,new PageInfo<>(page, size));
		PageInfo<InventoryDto> data = pageResult.getData();
		List<InventoryDto> list = data.getRows();			
		List<String> skuCodes = new ArrayList<String>();
		for (InventoryDto dto : list) {
			skuCodes.add(dto.getSku());
		}
		List<ItemSku> itemResultDto = inventoryProxy.selectSkuInfo(skuCodes);			
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
		data.setRows(list);

		modelAndView.addObject("page",data);
		// 3. 查询条件
		// 3.1 供应商
		List<SupplierInfo> supplierList = inventoryProxy.initSupplierList();
		modelAndView.addObject("supplierList",supplierList);
		// 3.2 sku/条形码/spuName
		ResultInfo<List<ItemSku>> skuResult = skuProxy.queryByObject(new ItemSku());
		modelAndView.addObject("skuList",skuResult.getData());
		return modelAndView;
	}
	
	@RequestMapping("toUpdate")
	public ModelAndView toUpdate(String inventoryId){
		ModelAndView modelAndView = new ModelAndView("/storage/inventory/manage/update");
		
		InventoryQuery query = new InventoryQuery();
		query.setId(Long.parseLong(inventoryId));
		ResultInfo<PageInfo<InventoryDto>> pageResult = inventoryProxy.queryInventoryInfo(query ,new PageInfo<>(null, 10));
		List<InventoryDto> rows = pageResult.getData().getRows();
		if(rows.size()==1){
			InventoryDto inventoryDto = rows.get(0);
			List<String> skuCodes = new ArrayList<String>();
			skuCodes.add(inventoryDto.getSku());
			List<ItemSku> itemResultDto = inventoryProxy.selectSkuInfo(skuCodes);
			if(itemResultDto.size()==1){
				ItemSku itemSku = itemResultDto.get(0);
				inventoryDto.setBarcode(itemSku.getBarcode());
				inventoryDto.setMainTitle(itemSku.getDetailName());
				inventoryDto.setSpName(itemSku.getSpName());
			}
			modelAndView.addObject("inventory",inventoryDto);
		}
		return modelAndView;
	}
	
	@RequestMapping("save")
	public void save(HttpServletResponse response,Inventory inventory){
		JSONObject result = new JSONObject();
		//ResultInfo<Integer> updateNotNullById = inventoryProxy.updateNotNullById(inventory);
		int count = inventoryProxy.updateInventory(inventory);
		if(count==1){
			result.put("flag", "success");
			result.put("msg", "修改成功");
			// 保存日志
			Map<String,Object> params = new HashMap<String, Object>();
			params.put("id", inventory.getId());
			InventoryManageDto inventoryManageDto = inventoryProxy.getInventoryById(params);
			inventoryManageDto.setInventory(inventory.getInventory());
			inventoryManageDto.setOccupy(inventory.getOccupy());
			InventoryManageLog inventoryManageLog = new InventoryManageLog(inventoryManageDto);
			inventoryManageLog.setCreateTime(new Date());
			inventoryManageLog.setCreateUser(getUserName());
			inventoryManageLog.setChangeType(2); // 更改标志：1：新增，2：修改；3：删除
			inventoryManageLogProxy.insert(inventoryManageLog);
		}else{
			result.put("flag", "fail");
			result.put("msg", "修改失败");
		}

		render(response, result.toString());
	}
	
	/**
	 * 日志列表
	 * @param inventoryId
	 * @param page
	 * @param size
	 * @return
	 */
	@RequestMapping("showLog")
	public String showLog(Model model, Long inventoryId,Integer page, Integer size){
		PageInfo<InventoryManageLog> info = new PageInfo<InventoryManageLog>(page,size);
		if(inventoryId!=null){
			InventoryManageLog obj = new InventoryManageLog();
			obj.setInventoryId(inventoryId);
			ResultInfo<PageInfo<InventoryManageLog>> result = inventoryManageLogProxy.queryPageByObject(obj, info);
			model.addAttribute("page", result.getData());
			model.addAttribute("inventoryId", inventoryId);
		}
		return "/storage/inventory/manage/showLog";
	}
	

	/**
	 * 商品详情查询 -- 条件
	 * @param model
	 * @param spuName
	 * @param sku
	 * @return
	 */
	@RequestMapping(value="querySkuDetail")
	@ResponseBody
	public List<ItemSku> queryPromoterListByLikePromoterName(Model model,String spuName,String sku,String spId){
		
		Map<String,Object> params = new HashMap<String,Object>();
		if(StringUtils.isNotBlank(spId)){
			params.put("spId", spId);
		}
		params.put(MYBATIS_SPECIAL_STRING.LIKE.name(), " (spu_name like concat('%',concat('"+spuName+"','%')) or sku like concat('"+spuName+"','%') or barcode like concat('"+spuName+"','%'))");
		if(sku!=null){
			params.put("sku", sku);
		}
		params.put(MYBATIS_SPECIAL_STRING.LIMIT.name(), 10);
		List<ItemSku> data = skuProxy.queryByParam(params).getData();
		
		return data;
	}
	
	
	@RequestMapping("toAdd")
	public ModelAndView toAdd(String inventoryId){
		ModelAndView modelAndView = new ModelAndView("/storage/inventory/manage/add");
		List<SupplierInfo> supplierList = inventoryProxy.initSupplierList();
		modelAndView.addObject("supplierList",supplierList);
		return modelAndView;
	}
	
	@RequestMapping("add")
	public void add(HttpServletResponse response,Inventory inventory,String spuName,String warehouseName,String spName){
		JSONObject result = new JSONObject();
		
		Map<String, Object> params = new HashMap<String, Object>();
		if(inventory!=null){
			if(inventory.getSpId()!=null){
				params.put("sp_id", inventory.getSpId());
			}
			if(inventory.getWarehouseId()!=null){
				params.put("warehouse_id", inventory.getWarehouseId());
			}
			if(StringUtils.isNotBlank(inventory.getSku())){
				params.put("sku", inventory.getSku());
			}
		}
		// 查询是否有此记录
		ResultInfo<Integer> resultInfo = inventoryProxy.queryByParamCount(params);
		if(resultInfo.getData()>0){
			result.put("flag", "fail");
			result.put("msg", "此商品已有库存 sku:"+inventory.getSku());
		}else{
			// 插入库存
			inventory.setCreateTime(new Date());
			inventory.setModifyTime(new Date());
			ResultInfo<Inventory> insert = inventoryProxy.insert(inventory);
			Long id = insert.getData().getId();
			inventory.setId(id);
			
			// 保存日志
			InventoryManageLog inventoryManageLog = new InventoryManageLog(inventory, spuName, warehouseName, spName);
			inventoryManageLog.setCreateTime(new Date());
			inventoryManageLog.setCreateUser(getUserName());
			inventoryManageLog.setChangeType(1); // 更改标志：1：新增，2：修改；3：删除
			inventoryManageLogProxy.insert(inventoryManageLog);
			result.put("flag", "success");
			result.put("msg", "保存成功");
		}
		
		render(response, result.toString());
	}
	/**
	 * 页面跳转 导入弹出框
	 * @return
	 */
	@RequestMapping("popTemplate")
	public ModelAndView popTemplate(){
		return new ModelAndView("storage/inventory/manage/popTemplate");
	}
	/**
	 * 页面跳转 批量修改弹出框
	 * @return
	 */
	@RequestMapping("popUpdateTemplate")
	public ModelAndView popUpdateTemplate(){
		return new ModelAndView("storage/inventory/manage/popUpdateTemplate");
	}
	
	/**
	 * 下载模板
	 * @return
	 */
	@RequestMapping("downloadTemplate")
	public void downloadTemplate(HttpServletResponse response){
		// 实际位置
		String path =  QuotationImportProxy.class.getResource("/").getPath()+"template/import_inventory.xlsx";
        // 下载文件名
		String fileName = "inventory_" + DateUtil.format(new Date(), "yyyyMMddHHmmss") + ".xlsx";
       
        // 1.设置文件ContentType类型，这样设置，会自动判断下载文件类型
        response.setContentType("multipart/form-data");
        // 2.设置文件头：最后一个参数是设置下载文件名
        response.setHeader("Content-Disposition", "attachment;fileName="
                + fileName);
        response.addHeader("Content-Type", "application/vnd.ms-excel");
        OutputStream out;
        // 通过文件路径获得File对象(假如此路径中有一个download.pdf文件)
        File file = new File(path);
        try {
            FileInputStream inputStream = new FileInputStream(file);
            // 3.通过response获取OutputStream对象(out)
            out = response.getOutputStream();
            byte[] buffer = new byte[512];
            int b = inputStream.read(buffer);
            while (b != -1) {
                // 4.写到输出流(out)中
                out.write(buffer, 0, b);
                b = inputStream.read(buffer);
            }
            inputStream.close();
            out.close();
            out.flush();

        } catch (IOException e) {
            e.printStackTrace();
        }
	}
	/**
	 * 下载批量修改模板
	 * @return
	 */
	@RequestMapping("downloadUpdateTemplate")
	public void downloadUpdateTemplate(HttpServletResponse response){
		// 实际位置
		String path =  QuotationImportProxy.class.getResource("/").getPath()+"template/import_update_inventory.xlsx";
        // 下载文件名
		String fileName = "inventory_update_" + DateUtil.format(new Date(), "yyyyMMddHHmmss") + ".xlsx";
       
        // 1.设置文件ContentType类型，这样设置，会自动判断下载文件类型
        response.setContentType("multipart/form-data");
        // 2.设置文件头：最后一个参数是设置下载文件名
        response.setHeader("Content-Disposition", "attachment;fileName="
                + fileName);
        response.addHeader("Content-Type", "application/vnd.ms-excel");
        OutputStream out;
        // 通过文件路径获得File对象(假如此路径中有一个download.pdf文件)
        File file = new File(path);
        try {
            FileInputStream inputStream = new FileInputStream(file);
            // 3.通过response获取OutputStream对象(out)
            out = response.getOutputStream();
            byte[] buffer = new byte[512];
            int b = inputStream.read(buffer);
            while (b != -1) {
                // 4.写到输出流(out)中
                out.write(buffer, 0, b);
                b = inputStream.read(buffer);
            }
            inputStream.close();
            out.close();
            out.flush();

        } catch (IOException e) {
            e.printStackTrace();
        }
	}
	
	/**
	 * 导出数据
	 * @return
	 */
	@RequestMapping("exportInventory")
	public void exportInventory(HttpServletResponse response,InventoryQuery query,String chooseSection,Integer usableInventory){
		// 1. 查询条件
		if(chooseSection!=null){
			if("min".equals(chooseSection)){
				query.setMinUsableInventory(usableInventory);
			}
			if("max".equals(chooseSection)){
				query.setMaxUsableInventory(usableInventory);
			}
		}
		if(query!=null){
			if(StringUtils.isBlank(query.getSku())){
				query.setSpuName(null);
			}
		}
		
        // 下载文件名
		String fileName = "inventory_" + DateUtil.format(new Date(), "yyyyMMddHHmmss") + ".xlsx";
        response.setContentType("multipart/form-data");
        response.setHeader("Content-Disposition", "attachment;fileName=" + fileName);
        response.addHeader("Content-Type", "application/vnd.ms-excel");
        try {
    		// 读取模板
    		String path =  QuotationImportProxy.class.getResource("/").getPath()+"template/export_inventory.xlsx";
    		FileInputStream inputStream = new FileInputStream(new File(path));
    		Workbook workbook = inventoryProxy.exportInventory(inputStream,query);
    		
    		OutputStream output = response.getOutputStream();
            BufferedOutputStream bufferedOutPut = new BufferedOutputStream(output);
            bufferedOutPut.flush();
            workbook.write(bufferedOutPut);
            bufferedOutPut.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
	}
	
	/**
	 * 导入文件 
	 * @return
	 */
	@RequestMapping("importTemplate")
	public void importTemplate(MultipartFile inventoryFile,HttpServletResponse response){
		try {
			//获得文件名  
	        String fileName = inventoryFile.getOriginalFilename();  
	        //创建Workbook工作薄对象，表示整个excel  
	        Workbook workbook = null;  
            //获取excel文件的io流  
            InputStream is = inventoryFile.getInputStream();  
            if(fileName.endsWith("xls")||fileName.endsWith("xlsx")){
            	//根据文件后缀名不同(xls和xlsx)获得不同的Workbook实现类对象  
                if(fileName.endsWith("xls")){  
                    workbook = new HSSFWorkbook(is);  //2003
                }else if(fileName.endsWith("xlsx")){  
                    workbook = new XSSFWorkbook(is);  //2007
                }
                JSONObject result = inventoryProxy.importTemplate(workbook,getUserName());
                response.setContentType("text/html;charset=UTF-8");
        		response.getWriter().write(result.toString());
            }else{
            	JSONObject result = new JSONObject();
    			result.put("status", false);
        		result.put("msg", "请上传excel文件");
        		response.setContentType("text/html;charset=UTF-8");
         		response.getWriter().write(result.toString());
            }
		} catch (IOException e) {
			JSONObject result = new JSONObject();
			result.put("status", false);
    		result.put("msg", "Excel文件解析错误");
    		response.setContentType("text/html;charset=UTF-8");
    		try {
				response.getWriter().write(result.toString());
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
		} 
	}
	
	/**
	 * 批量修改
	 * @return
	 */
	@RequestMapping("importUpdateTemplate")
	public void importUpdateTemplate(MultipartFile inventoryFile,HttpServletResponse response){
		try {
			//获得文件名  
	        String fileName = inventoryFile.getOriginalFilename();  
	        //创建Workbook工作薄对象，表示整个excel  
	        Workbook workbook = null;  
            //获取excel文件的io流  
            InputStream is = inventoryFile.getInputStream();  
            if(fileName.endsWith("xls")||fileName.endsWith("xlsx")){
            	//根据文件后缀名不同(xls和xlsx)获得不同的Workbook实现类对象  
                if(fileName.endsWith("xls")){  
                    workbook = new HSSFWorkbook(is);  //2003
                }else if(fileName.endsWith("xlsx")){  
                    workbook = new XSSFWorkbook(is);  //2007
                }
                JSONObject result = inventoryProxy.importUpdateTemplate(workbook,getUserName());
                response.setContentType("text/html;charset=UTF-8");
        		response.getWriter().write(result.toString());
            }else{
            	JSONObject result = new JSONObject();
    			result.put("status", false);
        		result.put("msg", "请上传excel文件");
        		response.setContentType("text/html;charset=UTF-8");
         		response.getWriter().write(result.toString());
            }
		} catch (IOException e) {
			JSONObject result = new JSONObject();
			result.put("status", false);
    		result.put("msg", "Excel文件解析错误");
    		response.setContentType("text/html;charset=UTF-8");
    		try {
				response.getWriter().write(result.toString());
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
		} 
	}
	
	/**
	 * 根据供应商ID 查询仓库列表
	 * @param response
	 * @param warehouse
	 * @return
	 */
	@RequestMapping("getWarehouseBySupplierId")
	@ResponseBody
	public List<Warehouse> getWarehouseBySupplierId(HttpServletResponse response,Warehouse warehouse){
		if(warehouse.getSpId()!=null){
			ResultInfo<List<Warehouse>> resultInfo = warehouseProxy.queryByObject(warehouse);
			if(resultInfo!=null){
				List<Warehouse> list = resultInfo.getData();
				return list;
			}
		}
		return new ArrayList<Warehouse>();
	}
	
	private void render(HttpServletResponse response,String text){
		
		response.setContentType("application/json;charset=UTF-8");
		try {
			response.getWriter().write(text);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
