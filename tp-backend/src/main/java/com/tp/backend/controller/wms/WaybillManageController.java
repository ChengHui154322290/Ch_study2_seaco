/**
 * 
 */
package com.tp.backend.controller.wms;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.annotation.JsonCreator.Mode;
import com.tp.backend.controller.AbstractBaseController;
import com.tp.common.vo.PageInfo;
import com.tp.common.vo.DAOConstant.MYBATIS_SPECIAL_STRING;
import com.tp.common.vo.wms.WmsConstant;
import com.tp.common.vo.wms.WmsWaybillConstant;
import com.tp.common.vo.wms.WmsWaybillConstant.WaybillStatus;
import com.tp.dto.common.FailInfo;
import com.tp.dto.common.ResultInfo;
import com.tp.model.bse.ExpressInfo;
import com.tp.model.wms.WaybillApplication;
import com.tp.model.wms.WaybillDetail;
import com.tp.model.wms.WaybillDetailLog;
import com.tp.model.wms.WaybillInfo;
import com.tp.proxy.bse.ExpressInfoProxy;
import com.tp.proxy.wms.logistics.WaybillApplicationProxy;
import com.tp.proxy.wms.logistics.WaybillDetailLogProxy;
import com.tp.proxy.wms.logistics.WaybillDetailProxy;
import com.tp.proxy.wms.logistics.WaybillInfoProxy;
import com.tp.util.StringUtil;

/**
 * @author Administrator
 *
 */
@Controller
@RequestMapping("/wms/waybill")
public class WaybillManageController extends AbstractBaseController{

	@Autowired
	private WaybillApplicationProxy waybillApplicationProxy;
	
	@Autowired
	private WaybillDetailProxy waybillDetailProxy;
	
	@Autowired
	private WaybillDetailLogProxy waybillDetailLogProxy;
	
	@Autowired
	private ExpressInfoProxy expressInfoProxy;
	
	@Autowired
	private WaybillInfoProxy waybillInfoProxy;
	
	/*--------------------------------------运单申请----------------------------------------*/
	@RequestMapping("/apply_list")
	public String applyList(Model model, WaybillApplication application, Integer page, Integer size){
		ResultInfo<PageInfo<WaybillApplication>> appResultInfo = waybillApplicationProxy.queryPageByObject(application, new PageInfo<>(page, size));
		ResultInfo<List<ExpressInfo>> expressList = expressInfoProxy.queryByObject(new ExpressInfo());
		model.addAttribute("page", appResultInfo.getData());
		model.addAttribute("application", application);
		model.addAttribute("logisticsList", expressList.getData());
		return "/wms/waybill/applyList";
	}
	
	@RequestMapping("/toapply")
	public String toApply(Model model){
		ResultInfo<List<ExpressInfo>> expressList = expressInfoProxy.queryByObject(new ExpressInfo());
		model.addAttribute("logisticsList", expressList.getData());
		return "/wms/waybill/applyWaybill";
	}
	
	@RequestMapping("/applyWaybill")
	@ResponseBody
	public ResultInfo<Boolean> applyWaybill(Model model, WaybillApplication application){
		return waybillApplicationProxy.apply(application);
	}
	
	/*--------------------------------------单号管理----------------------------------------*/
	@RequestMapping("/waybillno_list")
	public String waybillNoList(Model model, WaybillDetail detail, Integer page, Integer size){
		ResultInfo<PageInfo<WaybillDetail>> detailResultInfo = waybillDetailProxy.queryPageByObject(detail, new PageInfo<>(page, size));
		ResultInfo<List<ExpressInfo>> expressList = expressInfoProxy.queryByObject(new ExpressInfo());
		model.addAttribute("page", detailResultInfo.getData());
		model.addAttribute("logisticsList", expressList.getData());
		model.addAttribute("waybillStatusList", WaybillStatus.values());
		model.addAttribute("detail", detail);
		return "/wms/waybill/waybillNoList";
	}
	
	@RequestMapping("/waybillNoOperation")
	public String waybillNoOperation(Model model, String waybillNo, String orderCode){
		model.addAttribute("waybillNo", waybillNo);
		model.addAttribute("orderCode", orderCode);
		return "/wms/waybill/waybillOperation";
	}
	@RequestMapping("/waybillNoLog")
	public String waybillNoLog(Model model, String waybillNo){
		if (!StringUtil.isEmpty(waybillNo)) {
			Map<String, Object> params = new HashMap<>();
			params.put("waybillNo", waybillNo);
			params.put(MYBATIS_SPECIAL_STRING.ORDER_BY.name(), "create_time desc");
			ResultInfo<List<WaybillDetailLog>> logResultInfo = waybillDetailLogProxy.queryByParamNotEmpty(params);
			model.addAttribute("logList", logResultInfo.getData());
		}
		return "/wms/waybill/waybillNoLog";
	}
	
	//类型(type:1是绑定2是解绑3是作废)
	@RequestMapping("/waybillOper")
	@ResponseBody
	public ResultInfo<Boolean> waybillOper(Model model, String waybillNo, String orderCode, Integer type){
		if (type == 1) {
			return waybillDetailProxy.bindWaybillNo(waybillNo, orderCode, getUserName());
		}else if (type == 2) {
			return waybillDetailProxy.unBindWaybillNo(waybillNo, orderCode, getUserName());
		}else if (type == 3) {
			return waybillDetailProxy.abandon(waybillNo, getUserName());
		}
		return new ResultInfo<>(new FailInfo("操作类型错误"));
	}
	/*--------------------------------------运单申报----------------------------------------*/
	@RequestMapping(value = "waybillinfo_list")
	public String waybillInfoList(Model model, WaybillInfo waybillInfo, Integer page, Integer size){
		ResultInfo<PageInfo<WaybillInfo>> resultInfo = waybillInfoProxy.queryPageByObject(waybillInfo, new PageInfo<>(page, size));
		ResultInfo<List<ExpressInfo>> expressList = expressInfoProxy.queryByObject(new ExpressInfo());
		model.addAttribute("page", resultInfo.getData());
		model.addAttribute("logisticsList", expressList.getData());
		model.addAttribute("waybillInfoTypeList", WmsWaybillConstant.WaybillType.values());
		model.addAttribute("info", waybillInfo);
		return "/wms/waybill/waybillInfoList";
	}
	
	@RequestMapping(value="waybillinfo")
	public String waybillInfo(Model model, Long id){
		if (id != null) {
			model.addAttribute("waybill", waybillInfoProxy.queryById(id).getData());	
		}
		return "/wms/waybill/viewWaybillInfo";
	}
}
