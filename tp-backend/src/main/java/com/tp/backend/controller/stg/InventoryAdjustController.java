package com.tp.backend.controller.stg;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.tp.backend.controller.AbstractBaseController;
import com.tp.common.vo.PageInfo;
import com.tp.dto.common.FailInfo;
import com.tp.dto.common.ResultInfo;
import com.tp.dto.stg.InventoryDto;
import com.tp.model.stg.InventoryAdjustLog;
import com.tp.model.stg.Warehouse;
import com.tp.model.stg.vo.InventoryAdjustLogVO;
import com.tp.proxy.stg.InventoryAdjustLogProxy;

/**
 * 库存盘点查询
 * 
 * @author beck
 *
 */
@Controller
@RequestMapping("storage/inventoryAdjust")
public class InventoryAdjustController extends AbstractBaseController {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private InventoryAdjustLogProxy inventoryAdjustLogProxy;

	@RequestMapping(value = "/list")
	public void list(Model model, InventoryAdjustLog ado, Integer page, Integer size) throws Exception {
		PageInfo<InventoryAdjustLog> pi = new PageInfo<InventoryAdjustLog>(page, size);
		ResultInfo<PageInfo<InventoryAdjustLog>> pageResult = inventoryAdjustLogProxy.queryPageByObject(ado, pi);
		if (pageResult.isSuccess() && CollectionUtils.isNotEmpty(pageResult.getData().getRows())) {
			PageInfo<InventoryAdjustLog> pageInfo = pageResult.getData();
			model.addAttribute("listLogs", pageInfo);
			List<InventoryAdjustLog> logDOs = pageInfo.getRows();
			List<InventoryAdjustLogVO> logVos = new ArrayList<InventoryAdjustLogVO>();
			InventoryAdjustLogVO adjustLogVo = null;
			List<Long> warehouseIds = new ArrayList<Long>();
			for (InventoryAdjustLog adjustLogDO : logDOs) {
				adjustLogVo = new InventoryAdjustLogVO();
				PropertyUtils.copyProperties(adjustLogVo, adjustLogDO);
				logVos.add(adjustLogVo);
				warehouseIds.add(adjustLogDO.getWarehouseId());
			}

			List<Warehouse> warehouseDOs = inventoryAdjustLogProxy.queryWarehouseByIds(warehouseIds);
			for (InventoryAdjustLogVO logVo : logVos) {
				for (Warehouse warehouseDO : warehouseDOs) {
					if (null != logVo.getWarehouseId()
							&& logVo.getWarehouseId().longValue() == warehouseDO.getId().longValue()) {
						logVo.setWarehouseName(warehouseDO.getName());
						break;
					}
				}
			}
			model.addAttribute("listVoLogs", logVos);
		} else {
			model.addAttribute("onData", "没有查找到对应信息。");
		}
		model.addAttribute("ado", ado);
	}

	/**
	 * 
	 * <pre>
	 * 转到新增盘点页面
	 * </pre>
	 *
	 */
	@RequestMapping(value = "/add")
	public ModelAndView addLog() {
		ModelAndView mv = new ModelAndView();
		mv.setViewName("/storage/inventoryAdjust/add");
		return mv;
	}

	@RequestMapping(value = "queryWarehouseBySkuCode")
	@ResponseBody
	public List<InventoryDto> queryWarehouseBySkuCode(String sku) {
		List<InventoryDto> inventoryDtos = inventoryAdjustLogProxy.queryWarehouseBySkuCode(sku);
		return inventoryDtos;
	}

	/**
	 * 
	 * <pre>
	 * 新增库存盘点记录
	 * </pre>
	 *
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value = "/adjustLog")
	@ResponseBody
	public ResultInfo<String> addBrandSubmit(InventoryAdjustLog ado) throws Exception {
		if (ado == null) {
			logger.debug("数据不能为空");
			return new ResultInfo<String>(new FailInfo("盘点数据为空。", 1010));
		}
		if (StringUtils.isBlank(ado.getSku())) {
			return new ResultInfo<String>(new FailInfo("请输入sku。", 1010));
		}
		if (null == ado.getWarehouseId()) {
			return new ResultInfo<String>(new FailInfo("请选择仓库。", 1010));
		}

		if (!NumberUtils.isNumber(ado.getQuantity().toString()) || ado.getQuantity() == null) {
			logger.debug("调整数量必须是数字".concat(ado.getQuantity().toString()));
			return new ResultInfo<String>(new FailInfo("调整数量必须为数字。", 1010));
		}
		if (ado.getQuantity() > Integer.MAX_VALUE) {
			logger.debug("调整数字超过最大额度。".concat(ado.getQuantity().toString()));
			return new ResultInfo<String>(new FailInfo("调整数字超过最大额度。", 1010));
		}
		return inventoryAdjustLogProxy.adjustLog(ado, super.getUserInfo());
	}

	/***
	 * 到处Excel
	 * 
	 * @param request
	 * @param response
	 * @param model
	 * @throws InvalidFormatException
	 * @throws IOException
	 */
	@RequestMapping("/export")
	public void exportExcel(InventoryAdjustLog ado, HttpServletRequest request, HttpServletResponse response,
			Model model) throws InvalidFormatException, IOException {
		if (ado == null) {
			logger.error("库存盘点导出参数:" + " 为空");
			response.setStatus(HttpServletResponse.SC_OK);
			response.flushBuffer();
			return;
		}
		request.setCharacterEncoding("UTF-8");
		inventoryAdjustLogProxy.export(request, response, ado);
	}
}
