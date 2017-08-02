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
import com.tp.common.vo.StorageConstant.InputAndOutputType;
import com.tp.model.stg.InventoryLog;
import com.tp.proxy.stg.InventoryLogProxy;


/**
 * 出入库流水查询（自营仓库及商家仓库）
 * @author 付磊
 * 2015年1月8日 下午8:36:35
 *
 */
@Controller
@RequestMapping("storage/out-in/")
public class OutAndInLogController extends AbstractBaseController{
	
	private Logger logger =LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private InventoryLogProxy inventoryLogProxy;
	
	@RequestMapping(value = "/list")
	public void list(Model model, InventoryLog ido, Integer page, Integer size){			
		PageInfo<InventoryLog>  list = inventoryLogProxy.selectInventoryLog(ido, page, size);
		if(list != null){
			model.addAttribute("listLogs", list);
		}else{
			model.addAttribute("onData","没有查找到对应信息。" );
		}
		model.addAttribute("enum",InputAndOutputType.values());  
		model.addAttribute("ado", ido);
	}
	
	
	
	
	@RequestMapping("/export")
	public void exportExcel(
			InventoryLog ado,
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
		inventoryLogProxy.export(request,response,ado);
	}
	
}
