package com.tp.ptm.ao.storage;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.vo.ptm.ErrorCodes;
import com.tp.common.vo.ptm.ErrorCodes.SystemError;
import com.tp.dto.common.ResultInfo;
import com.tp.dto.prd.enums.ItemStatusEnum;
import com.tp.dto.ptm.ReturnData;
import com.tp.dto.ptm.remote.SupplierRelationDTO;
import com.tp.dto.ptm.storage.InventoryDto;
import com.tp.dto.ptm.storage.OutputOrderDto;
import com.tp.dto.stg.OrderDeliverDTO;
import com.tp.dto.stg.ResultOrderDeliverDTO;
import com.tp.model.bse.ExpressInfo;
import com.tp.model.mmp.TopicItem;
import com.tp.model.ord.OrderDelivery;
import com.tp.model.prd.ItemDetail;
import com.tp.model.prd.ItemPushLog;
import com.tp.model.prd.ItemSku;
import com.tp.result.mem.app.ResultMessage;
import com.tp.service.bse.IExpressInfoService;
import com.tp.service.mmp.ITopicItemService;
import com.tp.service.ord.remote.ISalesOrderRemoteService;
import com.tp.service.prd.IItemDetailService;
import com.tp.service.prd.IItemPushLogService;
import com.tp.service.prd.IItemSkuService;
import com.tp.service.stg.IOutputOrderService;

/**
 * 仓库模块服务
 * @author shaoqunxi
 *
 */
@Service
public class StorageAO {
	
	@Autowired
	private IOutputOrderService outputOrderService;
	
	@Autowired
	private IExpressInfoService expressInfoService;
	
	@Autowired
	private IItemSkuService itemSkuService;
	
	@Autowired
	private IItemDetailService itemDetailService;
	
	@Autowired
	private ITopicItemService topicItemService;
	
	@Autowired
	private IItemPushLogService itemPushLogService;
	
	@Autowired
	ISalesOrderRemoteService salesOrderRemoteService;
	//.findSubOrderDTOListBySubCodeList(List<String> subCodeList
	Logger logger = LoggerFactory.getLogger(this.getClass());
	//校验订单
	public ResultMessage vlidateRequestParms(OutputOrderDto outputOrderDto){
		ResultMessage r  = new ResultMessage();
		if(null == outputOrderDto){
			r.setErrorCode("入口参数不能为空");
			r.setResult(ResultMessage.FAIL);
			return r;
		}
		//快递公司编号
		String code = outputOrderDto.getCompanyCode();
		//订单号
		Long orderCode = outputOrderDto.getOrderCode();
		//运单号
		String packageNo = outputOrderDto.getPackageNo();
		if(StringUtils.isBlank(code)){
			r.setErrorCode("快递公司的编号不能为空");
			r.setResult(ResultMessage.FAIL);
		}
		if(orderCode == null){
			r.setErrorCode("订单号不能为空");
			r.setResult(ResultMessage.FAIL);
		}
		if(StringUtils.isBlank(packageNo)){
			r.setErrorCode("运单号不能为空");
			r.setResult(ResultMessage.FAIL);
		}
		List<Long> subCodeList = new ArrayList<Long>();
		subCodeList.add(outputOrderDto.getOrderCode());
		return r;
	}
	
	//校验订单
	public List<ResultMessage> vlidateRequestParms(List<OutputOrderDto> outputOrderDtos){
		List<ResultMessage> list = new ArrayList<ResultMessage>();
		for(OutputOrderDto OutputOrderDto : outputOrderDtos){
			ResultMessage r =vlidateRequestParms(OutputOrderDto);
			list.add(r);
		}
		return list;
	}
	
	/**
	 * 获得快递公司
	 * @return
	 */
	public Map<String,ExpressInfo> selectAllExpress(List<String> expressCodes){
		List<ExpressInfo> list = expressInfoService.selectAllExpress();
		Map<String,ExpressInfo> resMap = new HashMap<String,ExpressInfo>();
		for(ExpressInfo e : list){
			for(String code : expressCodes){
				if(code.equals(e.getCode())){
					resMap.put(code, e);
				}
			}
		}
		return resMap;
	}
	
	/**
	 * 根据编号获得快递公司信息
	 * @param code
	 * @return
	 */
	public ExpressInfo selectByCode(String code){
		return expressInfoService.selectByCode(code);
	}
	/**
	 * 根据订单编号进行商品出库操作
	 * @param deliverDTO
	 * @return
	 */
	public ResultMessage exWarehouseService(OrderDelivery deliverDTO){
		try {
			ResultInfo<Boolean> result = outputOrderService.exWarehouseService(deliverDTO);
			return new ResultMessage(result.isSuccess()?1:0, result.getMsg() == null?null:result.getMsg().getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			return new ResultMessage(ResultMessage.FAIL, e.getMessage());
		}
	}
	
	/**
	 * 根据订单编号批量进行商品出库操作
	 * @param deliverDTOs
	 * @return
	 */
	public ResultOrderDeliverDTO batchExWarehouseService(List<OrderDelivery> deliverDTOs){
		return outputOrderService.batchExWarehouseService(deliverDTOs);
	}

	/**
	 * @param inventory
	 * @param supplierList 
	 * @return
	 */
	public ReturnData setInventory(InventoryDto inventory, List<SupplierRelationDTO> supplierList) {
		ItemSku skuQuery = new ItemSku();
		skuQuery.setSku(inventory.getSku());
		ItemSku sku = itemSkuService.queryUniqueByObject(skuQuery);
		if(sku == null) {
			return new ReturnData(Boolean.FALSE, 301301, "sku不存在");
		}
		for(SupplierRelationDTO dto : supplierList){
			if(sku.getSpId().equals(dto.getSupplierId())){
				sku.setSupplierStock(inventory.getCount());
				sku.setUpdateTime(new Date());
				itemSkuService.updateById(sku);
				return new ReturnData(Boolean.TRUE);
			}
		}
		
		return new ReturnData(Boolean.FALSE, ErrorCodes.SystemError.PARAM_ERROR.code, "越权访问数据");
	}
	
	/**
	 * 根据第三方推送信息：库存不足，在专题中下架；库存足够，上架
	 * @param inventory
	 * @return
	 */
	public ReturnData checkInventory(InventoryDto inventory, List<SupplierRelationDTO> supplierList) {
		
		ItemSku skuQuery = new ItemSku();
		skuQuery.setSku(inventory.getSku());
		ItemSku sku = itemSkuService.queryUniqueByObject(skuQuery);
		if(sku == null) {
			return new ReturnData(Boolean.FALSE, 301301, "sku不存在");
		}
		for(SupplierRelationDTO dto : supplierList){
			if(sku.getSpId().equals(dto.getSupplierId())){
				
				Map<String, Object> params = new HashMap<String,Object>();
				params.put("sku", inventory.getSku());
				params.put("goods_code", inventory.getGoodsCode());
				ItemSku itemSku = itemSkuService.queryUniqueByParams(params);
				if(null == itemSku){
					return new ReturnData(Boolean.FALSE, SystemError.PARAM_ERROR.code, "sku与goodsCode匹配不到");
				}
				params.clear();
				params.put("prdid", itemSku.getPrdid());
				params.put("item_id", itemSku.getItemId());
				ItemDetail detail = itemDetailService.queryUniqueByParams(params);
				params.clear();
				params.put("sku", inventory.getSku());
				params.put("supplier_id", dto.getSupplierId());
				params.put("item_id", itemSku.getItemId());
				List<TopicItem> topicItemList = topicItemService.queryByParam(params);
				
				adjustStatus(inventory, itemSku, detail, topicItemList,dto.getSupplierId()); 
				
				return new ReturnData(Boolean.TRUE);
			}
		}
		return new ReturnData(Boolean.FALSE, ErrorCodes.SystemError.PARAM_ERROR.code, "越权访问数据");
	}
	
	private void adjustStatus(InventoryDto inventory,ItemSku itemSku,ItemDetail detail,List<TopicItem> topicItemList,Long supplierId){
		Date current = new Date();
		if(inventory.getCount()<=0){
			detail.setStatus(ItemStatusEnum.OFFLINE.getValue());
            detail.setUpdateTime(current);
            detail.setUpdateUser("system_auto");
            itemSku.setStatus(ItemStatusEnum.OFFLINE.getValue());
            itemSku.setUpdateTime(current);
            itemSku.setUpdateUser("system_auto");
            itemDetailService.updateNotNullById(detail);
            itemSkuService.updateNotNullById(itemSku);
            if (!CollectionUtils.isEmpty(topicItemList)) {
                for (TopicItem topicItem : topicItemList) {
                    topicItem.setItemStatus(ItemStatusEnum.OFFLINE.getValue());
                    topicItem.setUpdateTime(current);
                    topicItem.setUpdateUser("system_auto");
                    topicItemService.updateNotNullById(topicItem);
                }
//                addLog();
            }
            logger.info("sku为:" + itemSku.getSku() + " 的商品因库存不足下架");
		}else{
			detail.setStatus(ItemStatusEnum.ONLINE.getValue());
            detail.setUpdateTime(current);
            detail.setUpdateUser("system_auto");
            itemSku.setStatus(ItemStatusEnum.ONLINE.getValue());
            itemSku.setUpdateTime(current);
            itemSku.setUpdateUser("system_auto");
            itemDetailService.updateNotNullById(detail);
            itemSkuService.updateNotNullById(itemSku);
            //不在黑名单中，原因 为库存充足时, 进行上架操作

            if (!CollectionUtils.isEmpty(topicItemList)) {
                for (TopicItem topicItem : topicItemList) {
                    topicItem.setItemStatus(ItemStatusEnum.ONLINE.getValue());
                    topicItem.setUpdateTime(current);
                    topicItem.setUpdateUser("system_auto");
                    topicItemService.updateNotNullById(topicItem);
                }
//                addLog();
            }
            logger.info("sku为:" + itemSku.getSku() + " 的商品因库存充足上架");
		}
		ItemPushLog itemPushLog = new ItemPushLog();
		itemPushLog.setSku(inventory.getSku());
		itemPushLog.setGoodsCode(inventory.getGoodsCode());
		itemPushLog.setType(1);//type 1-设置库存接口 ， 2-设置成本价接口'
		itemPushLog.setInventory(inventory.getCount());//第三方推送库存
		itemPushLog.setCreateUser(String.valueOf(supplierId));
		itemPushLog.setCreateTime(new Date());
		itemPushLogService.insert(itemPushLog);
	}
}
