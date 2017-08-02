package com.tp.ptm.controller.storage;


import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.tp.common.vo.OrderConstant;
import com.tp.common.vo.ptm.ErrorCodes;
import com.tp.common.vo.ptm.ErrorCodes.SystemError;
import com.tp.dto.ptm.ReturnData;
import com.tp.dto.ptm.remote.Account4DetailDTO;
import com.tp.dto.ptm.remote.SupplierRelationDTO;
import com.tp.dto.ptm.salesorder.OrderMiniDTO;
import com.tp.dto.ptm.storage.DealOutputOrderDto;
import com.tp.dto.ptm.storage.InventoryDto;
import com.tp.dto.ptm.storage.OutputOrderDto;
import com.tp.dto.stg.OrderDeliverDTO;
import com.tp.dto.stg.ResultOrderDeliverDTO;
import com.tp.model.ord.OrderDelivery;
import com.tp.model.ord.SubOrder;
import com.tp.ptm.annotation.Authority;
import com.tp.ptm.ao.item.PushItemInfoServiceAO;
import com.tp.ptm.ao.storage.StorageAO;
import com.tp.ptm.controller.BaseController;
import com.tp.result.mem.app.ResultMessage;
import com.tp.service.ord.remote.ISalesOrderRemoteService;
import com.tp.service.ptm.IPlatformAccountService;
import com.tp.service.ptm.IPlatformAccountService.KeyType;

/***
 * 仓库控制器
 * @author shaoqunxi
 *
 */
@Controller
@RequestMapping("/storage")
public class StorageController extends BaseController {
	
	private static final Logger logger = LoggerFactory.getLogger(StorageController.class);
	//默认最多一批处理100个订单
	private static final int DEFULT_DEAL_ORDER_SIZE = 100;
	@Autowired
	private StorageAO storageAO;
	
	@Autowired
	private IPlatformAccountService platformAccountService;
	
	@Autowired
	private ISalesOrderRemoteService salesOrderRemoteService;
	
	@Autowired
	private PushItemInfoServiceAO pushItemInfoServiceAO;
	
	@RequestMapping(value = "/outPutOrder", method = RequestMethod.POST)
	@Authority
	public void outPutOrder(@RequestParam String appkey, HttpServletRequest request, HttpServletResponse response, @RequestBody OutputOrderDto outputOrderDto) {
		response.setCharacterEncoding("UTF-8");
		ReturnData rtData = new ReturnData(Boolean.TRUE);
		OrderDelivery deliverDTO = new OrderDelivery();
		if (outputOrderDto != null) {
			Map<KeyType, List<OrderMiniDTO>> resultMap = null;
			try {
				List<Long> orderNoList = new ArrayList<Long>();
				if(outputOrderDto.getOrderCode() != null)
					orderNoList.add(outputOrderDto.getOrderCode());
				resultMap = platformAccountService.verifySalesOrderAuthOfAccount(appkey, orderNoList);
				if(CollectionUtils.isEmpty(resultMap.get(KeyType.SUCCESS))){
					rtData = new ReturnData(Boolean.FALSE, ErrorCodes.AuthError.ACCESS_ILLEGAL_DATA.code,ErrorCodes.AuthError.ACCESS_ILLEGAL_DATA.cnName);
					response.getWriter().print(JSONObject.toJSONString(rtData));
					return;
				}
			} catch (Exception e) {
				logger.error("验证供应商权限异常", e);
				rtData = new ReturnData(Boolean.FALSE, ErrorCodes.SystemError.SYSTEM_ERROR.code, ErrorCodes.SystemError.SYSTEM_ERROR.cnName);
				try {
					response.getWriter().print(JSONObject.toJSONString(rtData));
				} catch (IOException e1) {
					logger.error(e.getMessage(), e);
				}
				return;
			}
			
			ResultMessage r = storageAO.vlidateRequestParms(outputOrderDto);
			if(r.getResult() == ResultMessage.FAIL){
				rtData = new ReturnData(Boolean.FALSE, SystemError.PARAM_ERROR.code, SystemError.PARAM_ERROR.cnName);
				try {
					response.getWriter().print(JSONObject.toJSONString(rtData));
				} catch (IOException e) {
					logger.error("返回响应结果时IO异常", e);
				}
				return;
			}
			Long currentUserId= pushItemInfoServiceAO.getCurrentUserIdByAppKey(appkey);
			deliverDTO.setWeight(1.0);//重量
			deliverDTO.setDeliveryTime(new Date());
			deliverDTO.setOrderCode(outputOrderDto.getOrderCode());
			deliverDTO.setPackageNo(outputOrderDto.getPackageNo());
			deliverDTO.setCompanyName(outputOrderDto.getCompanyName());
			deliverDTO.setCompanyId(outputOrderDto.getCompanyCode());
			deliverDTO.setCreateTime(new Date());
			deliverDTO.setCreateUser(currentUserId.toString());
			deliverDTO.setUpdateTime(new Date());
			deliverDTO.setUpdateUser(currentUserId.toString());
			ResultMessage rmsg = storageAO.exWarehouseService(deliverDTO);
			if(rmsg.getResult()==ResultMessage.FAIL){
				rtData = new ReturnData(Boolean.FALSE, null, rmsg.getMessage());
			}else{
				rtData = new ReturnData(Boolean.TRUE, null, rmsg);
			}
			
		}
		try {
			response.getWriter().print(JSONObject.toJSONString(rtData));
		} catch (IOException e) {
			logger.error("返回响应结果时IO异常", e);
		}
	}
	
	@RequestMapping(value = "/outPutOrders", method = RequestMethod.POST)
	@Authority
	public void outPutOrders(@RequestParam String appkey, HttpServletRequest request, HttpServletResponse response) {
		response.setCharacterEncoding("UTF-8");
		ReturnData rtData = new ReturnData(Boolean.TRUE);
		String reqDataJson = getRequestContent(request);
		List<OrderDelivery> deliverDTOs = new ArrayList<OrderDelivery>();
		List<OutputOrderDto> outputOrderDtos = new ArrayList<OutputOrderDto>();
		if (StringUtils.isNotBlank(reqDataJson)) {
			outputOrderDtos = JSONArray.parseArray(reqDataJson, OutputOrderDto.class);
			if(CollectionUtils.isEmpty(outputOrderDtos) || outputOrderDtos.size() > DEFULT_DEAL_ORDER_SIZE) {
				logger.info("批量发货参数为空或者超过限制");
				rtData = new ReturnData(Boolean.FALSE, SystemError.PROCESS_SIZE_UNMATCH_LIMIT.code, SystemError.PROCESS_SIZE_UNMATCH_LIMIT.cnName);
			}else{
				List<Long> subCodeList = new ArrayList<Long>();
				for(OutputOrderDto outputOrderDto : outputOrderDtos){
					ResultMessage r = storageAO.vlidateRequestParms(outputOrderDto);
					OrderDelivery deliverDTO = new OrderDelivery(); 
					if(r.getResult() == ResultMessage.FAIL){
						outputOrderDto.setValidateFlag(2);
						outputOrderDto.setValidateMsg(r.getMessage());
					}else{
						deliverDTO.setWeight(1.0);//重量
						deliverDTO.setDeliveryTime(new Date());
						deliverDTO.setOrderCode(outputOrderDto.getOrderCode());
						deliverDTO.setPackageNo(outputOrderDto.getPackageNo());
						deliverDTO.setCompanyName(outputOrderDto.getCompanyName());
						deliverDTO.setCompanyId(outputOrderDto.getCompanyCode());
						deliverDTOs.add(deliverDTO);
						if(!subCodeList.contains(outputOrderDto.getOrderCode())){
							subCodeList.add(outputOrderDto.getOrderCode());
						}
					}
				}
				//校验订单
				Map<KeyType, List<OrderMiniDTO>> resultMap = null;
				List<OrderMiniDTO> miniSuccessList  = new ArrayList<OrderMiniDTO>();
				try {
					resultMap = platformAccountService.verifySalesOrderAuthOfAccount(appkey, subCodeList);
					if(resultMap.containsKey(KeyType.SUCCESS)){
						miniSuccessList = resultMap.get(KeyType.SUCCESS);
					}
					
				} catch (Exception e) {
					logger.error("验证供应商权限异常", e);
					rtData = new ReturnData(Boolean.FALSE, ErrorCodes.SystemError.SYSTEM_ERROR.code, ErrorCodes.SystemError.SYSTEM_ERROR.cnName);
					try {
						response.getWriter().print(JSONObject.toJSONString(rtData));
					} catch (IOException e1) {
						logger.error(e.getMessage(), e);
					}
					return;
				}
				//校验状态
				List<Long> cancleOrderList =new ArrayList<Long>();
				List<Long> receiptOrderList =new ArrayList<Long>();
				Set<Long> orderSet = new HashSet<Long>();
				
				subCodeList = new ArrayList<Long>();
				for(OrderMiniDTO o : miniSuccessList){
					subCodeList.add(o.getCode());
				}
				List<SubOrder> subOrderList = salesOrderRemoteService.findSubOrderDTOListBySubCodeList(subCodeList);
				if(CollectionUtils.isNotEmpty(subOrderList)){
					for(SubOrder order : subOrderList){
						//符合条件的订单
						if(OrderConstant.ORDER_STATUS.DELIVERY.equals(order.getOrderStatus())){
							orderSet.add(order.getOrderCode());
						}
						else if(order.getOrderStatus().equals(OrderConstant.ORDER_STATUS.CANCEL)){
							cancleOrderList.add(order.getOrderCode());
						}
						else if(order.getOrderStatus().equals(OrderConstant.ORDER_STATUS.RECEIPT)){
							receiptOrderList.add(order.getOrderCode());
						}else{
							
						}
					}
				}
				
				List<OrderDelivery> batchDeliverDTOs = new ArrayList<OrderDelivery>();
				if(CollectionUtils.isNotEmpty(deliverDTOs)){
					for(OrderDelivery deliverDTO : deliverDTOs){
						if(orderSet.contains(deliverDTO.getOrderCode()) ){
							batchDeliverDTOs.add(deliverDTO);
						}
					}
				}
				ResultOrderDeliverDTO r = storageAO.batchExWarehouseService(batchDeliverDTOs);
				//处理信息
				DealOutputOrderDto res = new DealOutputOrderDto();
				Integer failNums=r.getErrorSize();//失败条数
				if(null != failNums && failNums>0){
					res.setCancleOrders(cancleOrderList.toString());
					res.setReceiptOrders(receiptOrderList.toString());
					res.setSuccessNums(batchDeliverDTOs.size()-failNums);
					res.setFailNums(failNums);
					rtData = new ReturnData(Boolean.FALSE, ErrorCodes.SystemError.BUSINESS_PROCESS_ERROR.code, res);
				}else{
					rtData = new ReturnData(Boolean.TRUE);
				}
			} 
		}
//		else{
//			rtData = new ReturnData(Boolean.FALSE, ErrorCodes.SystemError.BUSINESS_PROCESS_ERROR.code,new DealOutputOrderDto());
//		}
		try {
			response.getWriter().print(JSONObject.toJSONString(rtData));
		} catch (IOException e) {
			logger.error("返回响应结果时IO异常", e);
		}
		
	}

	
	@RequestMapping(value = "/setInventory", method = RequestMethod.POST)
	@Authority
	public void setInventory(@RequestParam String appkey, HttpServletRequest request, HttpServletResponse response, @RequestBody InventoryDto inventory) {
		response.setCharacterEncoding("UTF-8");
		ReturnData rtData = new ReturnData(Boolean.TRUE);
		if(inventory == null || StringUtils.isEmpty(inventory.getSku()) || inventory.getCount() == null)
			rtData = new ReturnData(Boolean.FALSE, ErrorCodes.SystemError.PARAM_ERROR.code, ErrorCodes.SystemError.PARAM_ERROR.cnName); 
		else{
			Account4DetailDTO dto = platformAccountService.findAccount4DetailDTOByAppkey(appkey);
			List<SupplierRelationDTO> supplierList = dto.getSupplierList();
			rtData = storageAO.setInventory(inventory, supplierList);
		}
		
		try {
			response.getWriter().print(JSONObject.toJSONString(rtData));
		} catch (IOException e) {
			logger.error("返回响应结果时IO异常", e);
		}
	}
	
	@RequestMapping(value = "/pushInventory", method = RequestMethod.POST)
	@ResponseBody
	@Authority
	public ReturnData pushInventoryApi(@RequestParam String appkey, HttpServletRequest request, HttpServletResponse response, @RequestBody InventoryDto inventory) {
		response.setCharacterEncoding("UTF-8");
		ReturnData rtData = new ReturnData(Boolean.TRUE);
		if(inventory == null || StringUtils.isEmpty(inventory.getSku()) || inventory.getCount() == null)
			rtData = new ReturnData(Boolean.FALSE, ErrorCodes.SystemError.PARAM_ERROR.code, ErrorCodes.SystemError.PARAM_ERROR.cnName); 
		else{
			Account4DetailDTO dto = platformAccountService.findAccount4DetailDTOByAppkey(appkey);
			List<SupplierRelationDTO> supplierList = dto.getSupplierList();
			rtData = storageAO.checkInventory(inventory, supplierList);
		}
		
//		response.getWriter().print(JSONObject.toJSONString(rtData));
		return rtData;
	}
}
