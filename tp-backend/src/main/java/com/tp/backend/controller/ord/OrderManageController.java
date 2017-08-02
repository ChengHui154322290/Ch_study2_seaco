package com.tp.backend.controller.ord;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.rabbitmq.client.AMQP.Connection.Tune;
import com.tp.backend.controller.AbstractBaseController;
import com.tp.common.vo.Constant;
import com.tp.common.vo.DssConstant;
import com.tp.common.vo.FastConstant;
import com.tp.common.vo.OrderConstant;
import com.tp.common.vo.PageInfo;
import com.tp.common.vo.Constant.TF;
import com.tp.common.vo.DAOConstant.MYBATIS_SPECIAL_STRING;
import com.tp.common.vo.OrderConstant.ClearanceStatus;
import com.tp.common.vo.OrderConstant.ORDER_STATUS;
import com.tp.common.vo.OrderConstant.PutCustomsType;
import com.tp.common.vo.ord.LogTypeConstant.LOG_TYPE;
import com.tp.dto.common.ResultInfo;
import com.tp.dto.ord.customs.PutCustomsDto;
import com.tp.dto.ord.remote.OrderLine4ExcelDTO;
import com.tp.dto.ord.remote.SubOrder4BackendDTO;
import com.tp.enums.common.PlatformEnum;
import com.tp.model.bse.ClearanceChannels;
import com.tp.model.dss.FastUserInfo;
import com.tp.model.dss.PromoterInfo;
import com.tp.model.ord.CustomsClearanceLog;
import com.tp.model.ord.DerictOperatLog;
import com.tp.model.ord.OrderPromotion;
import com.tp.model.ord.OrderStatusLog;
import com.tp.model.ord.RejectInfo;
import com.tp.model.ord.SubOrder;
import com.tp.model.stg.Warehouse;
import com.tp.model.sup.SupplierInfo;
import com.tp.proxy.bse.ClearanceChannelsProxy;
import com.tp.proxy.dss.FastUserInfoProxy;
import com.tp.proxy.dss.PromoterInfoProxy;
import com.tp.proxy.ord.CustomsClearanceLogProxy;
import com.tp.proxy.ord.DerictOperatLogProxy;
import com.tp.proxy.ord.JKFServiceProxy;
import com.tp.proxy.ord.OrderInfoProxy;
import com.tp.proxy.ord.OrderPromotionProxy;
import com.tp.proxy.ord.OrderStatusLogProxy;
import com.tp.proxy.ord.SubOrderProxy;
import com.tp.proxy.stg.WarehouseProxy;
import com.tp.proxy.sup.SupplierInfoProxy;
import com.tp.query.ord.SubOrderQO;
import com.tp.result.ord.SubOrderExpressInfoDTO;
import com.tp.util.DateUtil;
import com.tp.util.StringUtil;

/**
 * 订单控制器
 * 
 * @author szy
 * @version 0.0.1
 */
@Controller
public class OrderManageController extends AbstractBaseController {

	private final static Log log = LogFactory.getLog(OrderManageController.class);
	
	private static final int CHANNEL_STATUS_VALID = 1;	// 渠道状态－有效的

	@Autowired
	private SubOrderProxy subOrderProxy;
	@Autowired
	private OrderInfoProxy orderInfoProxy;
	@Autowired
	private OrderStatusLogProxy orderStatusLogProxy;
	@Autowired
	private ClearanceChannelsProxy clearanceChannelsProxy;
	@Autowired
	private OrderPromotionProxy orderPromotionProxy;
	@Autowired
	private CustomsClearanceLogProxy clearanceLogProxy;
	@Autowired
	private JKFServiceProxy jkfServiceProxy;
	@Autowired
	private FastUserInfoProxy fastUserInfoProxy;
	@Autowired
	private PromoterInfoProxy promoterInfoProxy;
	@Autowired
	private SupplierInfoProxy supplierInfoProxy;
	@Autowired
	private WarehouseProxy warehouseProxy;
	
	@Autowired
	private DerictOperatLogProxy derictOperatLogProxy;

	@InitBinder
	public void init(HttpServletRequest request, ServletRequestDataBinder binder) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		dateFormat.setLenient(false);
		binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
	}

	/**
	 * 前往订单列表页
	 * 
	 * @return
	 */
	@RequestMapping(value = "/order/list")
	public String goOrderListPage(Model model,SubOrderQO qo,Integer page,Integer size,Integer decode) {
		if(TF.YES.equals(decode)){
			if(StringUtil.isNotBlank(qo.getPromoterName())){
				try {
					qo.setPromoterName(new String(qo.getPromoterName().getBytes("ISO8859-1"),"UTF-8"));
				} catch (UnsupportedEncodingException e) {
				}
			}
			if(StringUtil.isNotBlank(qo.getShopPromoterName())){
				try {
					qo.setShopPromoterName(new String(qo.getShopPromoterName().getBytes("ISO8859-1"),"UTF-8"));
				} catch (UnsupportedEncodingException e) {
				}
			}
			if(StringUtil.isNotBlank(qo.getScanPromoterName())){
				try {
					qo.setScanPromoterName(new String(qo.getScanPromoterName().getBytes("ISO8859-1"),"UTF-8"));
				} catch (UnsupportedEncodingException e) {
				}
			}
		}
		if(size!=null){
			qo.setPageSize(size);
			model.addAttribute("size", size);
		}
		if(StringUtil.isEmpty(qo.getChannelCode())){
			qo.setChannelCode(null);
		}
		PageInfo<SubOrder4BackendDTO> orderPageInfoResultInfo = subOrderProxy.findSubOrder4BackendPage(qo);
		model.addAttribute("page", orderPageInfoResultInfo);
		model.addAttribute("query", qo);
		
		/* 海关渠道 */
		List<ClearanceChannels> channelList = clearanceChannelsProxy.getAllClearanceChannelsByStatus(CHANNEL_STATUS_VALID);
		model.addAttribute("channelList", channelList);
		model.addAttribute("orderTypeList", OrderConstant.OrderType.values());
		model.addAttribute("resourceTypeList", PlatformEnum.values());
		
		/* 商城 */
		PromoterInfo queryPromoter = new PromoterInfo();
		queryPromoter.setPromoterType(DssConstant.PROMOTER_TYPE.COMPANY.code);
		queryPromoter.setPromoterStatus(1);
		ResultInfo<List<PromoterInfo>> channelInfoResult = promoterInfoProxy.queryByObject(queryPromoter);
		if(channelInfoResult.isSuccess() && CollectionUtils.isNotEmpty(channelInfoResult.getData())){
			model.addAttribute("channelInfoList", channelInfoResult.getData());
		}	
		
		//供应商
		ResultInfo<List<SupplierInfo>> supplierInfoList = supplierInfoProxy.queryByObject(new SupplierInfo());
		if (supplierInfoList.isSuccess()){
			model.addAttribute("supplierList", supplierInfoList.getData());
		}
		if (qo.getSupplierId() != null){
			Warehouse queryWarehouse = new Warehouse();
			queryWarehouse.setSpId(qo.getSupplierId());
			ResultInfo<List<Warehouse>> warehouseResult = warehouseProxy.queryByObject(queryWarehouse);
			if(warehouseResult.isSuccess()){
				model.addAttribute("warehouseList", warehouseResult.getData());
			}
		}
		return "salesorder/list";
	}

	/**
	 * 前往海淘订单列表页
	 * 
	 * @return
	 */
	@RequestMapping(value = "/order/sea-list")
	public String goSeaOrderListPage(@RequestParam(defaultValue = "1") Integer page, Integer size, SubOrder qo, Model model) {
		if(size!=null){
			qo.setPageSize(size);
			model.addAttribute("size", size);
		}
		qo.setStartPage(page);
		//qo.setPutStatus(PutStatus.VERIFY_FAIL.code);	// 审核失败
		qo.setOrderStatus(ORDER_STATUS.DELIVERY.code);	// 待发货
		ResultInfo<PageInfo<SubOrder>> orderPageInfoResultInfo = subOrderProxy.queryPageByObject(qo,new PageInfo<SubOrder>(page, size));
		model.addAttribute("page", orderPageInfoResultInfo.getData());
		model.addAttribute("query", qo);
		model.addAttribute("clearanceStatusList", ClearanceStatus.values());
		return "salesorder/sea-list";
	}

	/**
	 * 导出订单
	 * 
	 * @param qo
	 * @param response
	 */
	@RequestMapping(value = "/order/export")
	public void exportOrder(SubOrderQO qo, HttpServletResponse response,Integer decode) {
		response.setHeader("Content-disposition", "attachment; filename=order-list.xlsx");
        response.setContentType("application/x-download");
		try {
			if(StringUtil.isEmpty(qo.getChannelCode())) qo.setChannelCode(null);
			List<OrderLine4ExcelDTO> dataList = subOrderProxy.exportSubOrder(qo);

			String templatePath = "/WEB-INF/classes/template/order-list.xlsx";
			String fileName = "order_list_" + DateUtil.format(new Date(), "yyyyMMddHHmmss") + ".xlsx";
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("list",dataList);
			super.exportXLS(map, templatePath, fileName,response);
		} catch (Exception e) {
			log.error("订单导出异常", e);
		}
	}

	/**
	 * 查看订单
	 * 
	 * @param code
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/order/view")
	public String viewOrder(@RequestParam Long code, Model model) {
		SubOrder4BackendDTO order = orderInfoProxy.queryOrder(code);
		List<RejectInfo> rejectList = subOrderProxy.findRejectInfoListBySubCode(code);
		if(order.getSubOrder().getDirectOrderStatus() == 2){
			List<DerictOperatLog> logList = derictOperatLogProxy.queryMessageByOrderCode(code);
			if(null != logList && logList.size()>0){
				model.addAttribute("log", logList.get(0));
			}
		}
		model.addAttribute("sub", order);
		model.addAttribute("rejectList", rejectList);
		if(String.valueOf(code).startsWith(Constant.DOCUMENT_TYPE.SO_SUB_ORDER.code.toString())
		  && OrderConstant.FAST_ORDER_TYPE.equals(order.getSubOrder().getType())
		  && OrderConstant.ORDER_STATUS.DELIVERY.code.equals(order.getSubOrder().getOrderStatus())){
			Map<String,Object> params = new HashMap<String,Object>();
			params.put("enabled", Constant.ENABLED.YES);
			params.put("userType", FastConstant.USER_TYPE.COURIER.code);
			params.put("warehouseId", order.getSubOrder().getWarehouseId());
			List<FastUserInfo> fastUserInfoList = fastUserInfoProxy.queryByParam(params).getData();
			model.addAttribute("fastUserInfoList", fastUserInfoList);
		}
		return code.toString().startsWith(Constant.DOCUMENT_TYPE.SO_ORDER.code.toString()) ? "salesorder/view-order" : "salesorder/view";
	}

	/**
	 * 日志跟踪
	 * 
	 * @param subCode
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/order/log", method = RequestMethod.GET)
	public String log(@RequestParam String subCode, Model model) {
		List<OrderStatusLog> logList = orderStatusLogProxy.queryByOrderCode(subCode);
		model.addAttribute("logList", logList);
		return "salesorder/log";
	}

	/**
	 * 物流跟踪
	 * 
	 * @param subCode
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/order/track", method = RequestMethod.GET)
	public String track(@RequestParam Long subCode, Model model) {
		List<SubOrderExpressInfoDTO> expressList = subOrderProxy.findExpressListBySubCode(subCode);
		model.addAttribute("expressList", expressList);
		return "salesorder/track";
	}

	/**
	 * 海外直邮日志跟踪
	 * 
	 * @param subCode
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/order/directlog", method = RequestMethod.GET)
	public String directLog(@RequestParam String subCode, Model model) {
		List<DerictOperatLog> logList = derictOperatLogProxy.queryByOrderCode(subCode);
		model.addAttribute("logList", logList);
		return "salesorder/directlog";
	}
	/**
	 * 优惠券信息
	 * 
	 * @param subCode
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/order/coupon", method = RequestMethod.GET)
	public String coupon(@RequestParam String subCode, Model model) {
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("orderCode", subCode);
		ResultInfo<List<OrderPromotion>> couponListResultInfo = orderPromotionProxy.queryByParam(params);
		model.addAttribute("couponList", couponListResultInfo.getData());
		return "salesorder/coupon";
	}

	/**
	 * 取消订单
	 * 
	 * @param subCode
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/order/cancel", method = RequestMethod.POST)
	@ResponseBody
	public ResultInfo<Boolean> cancel(@RequestParam Long code, Model model,String cancelReason) {
		return orderInfoProxy.cancelOrder(code,super.getUserInfo(),cancelReason);
	}
	
	/**
	 * 清关状态
	 * 
	 * @param subCode
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/order/clearance", method = RequestMethod.GET)
	public String clearance(@RequestParam String subCode, Model model) {
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("orderCode", subCode);
		ResultInfo<SubOrder> queryResult = subOrderProxy.queryUniqueByParams(params);
		model.addAttribute("orderCode", subCode);
		if (null != queryResult.getData()) {
			SubOrder subOrder = queryResult.getData();
			List<PutCustomsDto> dtoList = new ArrayList<>();
			dtoList.add(new PutCustomsDto(PutCustomsType.ORDER_DECLARE, subOrder.getPutOrderStatus()));
			dtoList.add(new PutCustomsDto(PutCustomsType.PERSONALGOODS_DECLARE, subOrder.getPutPersonalgoodsStatus()));
			dtoList.add(new PutCustomsDto(PutCustomsType.WAYBILL_DECLARE, subOrder.getPutWaybillStatus()));
			dtoList.add(new PutCustomsDto(PutCustomsType.PAY_DECLARE, subOrder.getPutPayStatus()));
			dtoList.add(new PutCustomsDto(PutCustomsType.CLEARANCE, subOrder.getClearanceStatus()));
			model.addAttribute("statusList", dtoList);
			model.addAttribute("orderCode", subOrder.getOrderCode());
		}		
		return "salesorder/clearance";
	}

	/**
	 * 重新推送海关
	 * 
	 * @param subCode
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/order/reset_clearance", method = RequestMethod.GET)
	@ResponseBody
	public ResultInfo<Boolean> reDeclare(@RequestParam String subCode, Integer type, Model model) {		
		return subOrderProxy.resetPutCustomsStatus(subCode, type);
	}
	
	/**
	 * 清关日志
	 * 
	 * @param subCode
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/order/clearancelog", method = RequestMethod.GET)
	public String clearanceLog(@RequestParam Long orderCode, @RequestParam Integer type, Model model) {
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("orderCode", orderCode);
		params.put("type", type);
		params.put(MYBATIS_SPECIAL_STRING.ORDER_BY.name(), "create_time desc");
		ResultInfo<List<CustomsClearanceLog>> logResult = clearanceLogProxy.queryByParam(params);
		model.addAttribute("clearanceLogList", logResult.getData());
		return "salesorder/clearancelog";
	}
	
	/**
	 * 手动推送海关 
	 */
	@RequestMapping(value = "/order/manual_push", method = RequestMethod.GET)
	@ResponseBody
	public ResultInfo<Boolean> manualPush(@RequestParam Long subCode){
		return jkfServiceProxy.pushSingleSubOrder4Backend(subCode);
	}
	
	/**
	 * 手动推送海外自营
	 */
	@RequestMapping(value = "/order/direct_push", method = RequestMethod.GET)
	@ResponseBody
	public ResultInfo<Boolean> pushDirectOrder(@RequestParam Long subCode){
		return jkfServiceProxy.pushSingleDirectSubOrder4Backend(subCode);
	}
	
	/**
	 * 手动推送仓库 
	 */
	@RequestMapping(value = "/order/manual_saleout", method = RequestMethod.GET)
	@ResponseBody
	public ResultInfo<Boolean> manualSaleOut(@RequestParam Long subCode){
		return subOrderProxy.deliverSubOrderToWMS(subCode);
	}

	@RequestMapping(value = "/order/receivingorder")
	@ResponseBody
	public ResultInfo<Boolean> receivingOrder(Long orderCode){
		return subOrderProxy.receivingOrder(orderCode,getUserId(),getUserName(),LOG_TYPE.USER);
	}
	
	@RequestMapping(value = "/order/deliveryorder")
	@ResponseBody
	public ResultInfo<Boolean> deliveryOrder(Long orderCode,Long fastUserId,String content){
		return subOrderProxy.deliveryOrder(orderCode,getUserId(),getUserName(),LOG_TYPE.USER,fastUserId,content);
	}
}
