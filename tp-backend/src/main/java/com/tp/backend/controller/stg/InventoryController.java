package com.tp.backend.controller.stg;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.tp.backend.controller.AbstractBaseController;
import com.tp.common.vo.PageInfo;
import com.tp.common.vo.StorageConstant.InputAndOutputType;
import com.tp.dto.common.ResultInfo;
import com.tp.dto.stg.InventoryDto;
import com.tp.dto.stg.query.InventoryQuery;
import com.tp.model.prd.ItemSku;
import com.tp.model.stg.InventoryLog;
import com.tp.model.stg.InventoryOccupy;
import com.tp.model.sup.SupplierInfo;
import com.tp.proxy.stg.InventoryLogProxy;
import com.tp.proxy.stg.InventoryProxy;


/**
 * 库存查询（自营仓库及商家仓库）
 * @author 付磊
 * 2015年1月8日 下午8:36:35
 *
 */
@Controller
@RequestMapping("storage/inventory/")
public class InventoryController extends AbstractBaseController{
	
	@Autowired
	private InventoryProxy inventoryProxy;
	@Autowired
	private InventoryLogProxy inventoryLogProxy;
	
	/***
	 * 
	 * @param model
	 * @param specDO
	 * @throws Exception 
	 */
	@RequestMapping(value = "/list")
	public void list(Model model, InventoryQuery query, Integer page, Integer size) throws Exception{		
		List<SupplierInfo> supplierList = inventoryProxy.initSupplierList();
		model.addAttribute("supplierList",supplierList);
		ResultInfo<PageInfo<InventoryDto>> pageResult = inventoryProxy.queryInventoryInfo(query, new PageInfo<>(page, size));
		if(!pageResult.isSuccess()){
			model.addAttribute("onData","没有查找到对应信息1。" );
		}else{
			List<InventoryDto> list = pageResult.getData().getRows();			
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
							inventoryDto.setSpName(resultDto.getSpName());	
						}
					}
				}
			}
			pageResult.getData().setRows(list);
			model.addAttribute("page", pageResult.getData());
			model.addAttribute("spId",	query.getSpId());
			model.addAttribute("sku", query.getSku());
		}	
	}
	
	@RequestMapping(value = "/queryRealInventoryList")
	public String queryRealInventoryList(Model model, String sku,Long warehouseId, Integer page, Integer size){
		if(StringUtils.isEmpty(sku)&&null==warehouseId){
			return "/storage/inventory/realInventory_list";
		}		
		InventoryLog ido = new InventoryLog();
		ido.setSku(sku);
		ido.setWarehouseId(warehouseId);
		if (null == inventoryLogProxy) {
			return "/storage/inventory/realInventory_list";
		}
		PageInfo<InventoryLog>  list = inventoryLogProxy.selectInventoryLog(ido, page, size);
		if(list != null){
			model.addAttribute("listLogs", list);
			if(list.getRows().size()>0){
				String warehouseName = list.getRows().get(0).getWareHouseName();
				model.addAttribute("warehouseName",warehouseName);  
			}
		}else{
			model.addAttribute("onData","没有查找到对应信息。" );
		}
		
		model.addAttribute("sku",sku);  
		model.addAttribute("warehouseId",warehouseId);  
		
		return "/storage/inventory/realInventory_list";
	}
	@RequestMapping(value = "/queryOccupyInventoryList")
	public String queryOccupyInventoryList(Model model, String sku,Integer page, Integer size){
		PageInfo<InventoryOccupy> pageInfo = inventoryProxy.queryOccupyInventoryList(sku, page, size);
		if(pageInfo != null){
			model.addAttribute("listLogs", pageInfo);
		}else{
			model.addAttribute("onData","没有查找到对应信息。" );
		}
		model.addAttribute("type",InputAndOutputType.PXS.getDesc());
		model.addAttribute("sku",sku);  
		return "/storage/inventory/occupy_list";
	}
}
