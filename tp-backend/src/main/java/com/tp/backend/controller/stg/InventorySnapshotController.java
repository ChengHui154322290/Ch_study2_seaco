package com.tp.backend.controller.stg;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.tp.backend.controller.AbstractBaseController;
import com.tp.common.vo.PageInfo;
import com.tp.model.stg.InventorySnapshot;
import com.tp.proxy.stg.InventorySnapshotProxy;

/**
 * 库存快照
 * @author 付磊
 * 2015年1月8日 下午8:36:35
 *
 */
@Controller
@RequestMapping("storage/inventory-snapshot/")
public class InventorySnapshotController extends AbstractBaseController{
	
	private Logger logger =LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private InventorySnapshotProxy inventorySnapShotProxy;
	
	@RequestMapping(value = "/list")
	public String list(Model model, InventorySnapshot isdo, Integer page, Integer size){
		
		PageInfo<InventorySnapshot>  list = inventorySnapShotProxy.selectSnapshot(isdo,page,size);
		if(list != null){
			model.addAttribute("listSnap", list);
		}else{
			model.addAttribute("onData","没有查找到对应信息。" );
		}
		model.addAttribute("ado", isdo);
	    return "/storage/inventorySnapshot/list";
	}
	
	

	@RequestMapping("/export")
	public void exportExcel(
			InventorySnapshot ado,
			HttpServletRequest request,
			HttpServletResponse response, Model model)
			throws InvalidFormatException, IOException {
		if(ado == null){
			logger.error("库存盘点导出参数:" + " 为空");
			response.setStatus(HttpServletResponse.SC_OK);
			response.flushBuffer();
			return ;
		}
		request.setCharacterEncoding("UTF-8");
		inventorySnapShotProxy.export(request,response,ado);
	}
	
}
