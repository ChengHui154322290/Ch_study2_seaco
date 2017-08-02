package com.tp.service.ord;

import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.dao.BaseDao;
import com.tp.common.util.OrderUtils;
import com.tp.common.util.mem.SmsException;
import com.tp.common.vo.OrderConstant;
import com.tp.common.vo.PaymentConstant;
import com.tp.dao.ord.OrderRedeemItemDao;
import com.tp.model.ord.OrderInfo;
import com.tp.model.ord.OrderItem;
import com.tp.model.ord.OrderRedeemItem;
import com.tp.model.ord.SubOrder;
import com.tp.model.prd.ItemAttribute;
import com.tp.model.prd.ItemDetail;
import com.tp.model.prd.ItemSku;
import com.tp.query.ord.RedeemItemQuery;
import com.tp.result.ord.OrderRedeemItemStatistics;
import com.tp.service.BaseService;
import com.tp.service.mem.ISendSmsService;
import com.tp.service.ord.IOrderInfoService;
import com.tp.service.ord.IOrderItemService;
import com.tp.service.ord.IOrderRedeemItemService;
import com.tp.service.ord.ISubOrderService;
import com.tp.service.prd.IItemAttributeService;
import com.tp.service.prd.IItemSkuService;
import com.tp.util.Base64Util;

/**
 * 
 * ClassName: OrderRedeemItemService <br/>
 * Function: 兑换码服务类<br/>
 * date: 2016年10月19日 上午9:41:13 <br/>
 * 
 * @author zhouguofeng
 * @version
 * @sinceJDK 1.8
 */
@Service
public class OrderRedeemItemService extends BaseService<OrderRedeemItem> implements IOrderRedeemItemService {

	private static final Logger LOGGER = LoggerFactory.getLogger(OrderRedeemItemService.class);

	@Autowired
	private OrderRedeemItemDao orderRedeemItemDao;

	@Autowired
	private IOrderItemService orderItemService;

	@Autowired
	private IItemSkuService itemSkuService;

	@Autowired
	private IItemAttributeService itemAttributeService;
	@Autowired
	IOrderInfoService orderInfoService;
	@Autowired
	ISubOrderService subOrderService;
	@Autowired
	private ISendSmsService sendSmsService;

	@Override
	public BaseDao<OrderRedeemItem> getDao() {
		return orderRedeemItemDao;
	}
	/**
	 * 
	 * cancleRedeemInfo:(根据id). <br/>  
	 *  
	 * @author zhouguofeng  
	 * @param orderId
	 * @return  
	 * @sinceJDK 1.8
	 */
	public void cancleRedeemInfo(long orderCode) {
		SubOrder subOrder=subOrderService.selectOneByCode(orderCode);//根据orderCode获取订单信息
		List<OrderRedeemItem> orderRedeemItemList = getOrderRedeemItemByObject(subOrder.getOrderCode());
		for(int i=0,len=orderRedeemItemList.size();i<len;i++){
			OrderRedeemItem orderRedeemItem=orderRedeemItemList.get(i);
			if(orderRedeemItem.getRedeemCodeState().equals(PaymentConstant.REDEEM_CODE_STATUS.NO_USE.code)){//更改未使用的状态
				orderRedeemItem.setRedeemCodeState(PaymentConstant.REDEEM_CODE_STATUS.BACK_PAY.code);//退款
				orderRedeemItemDao.updateById(orderRedeemItem);//更新兑换码状态
			}
		}
		
		
	}
	
	/**
	 * 
	 * generateRedeemInfo:(根据订单ID，生成兑换码). <br/>
	 * 
	 * @author zhouguofeng
	 * @param orderId
	 * @sinceJDK 1.8
	 */
	public String generateAndSaveRedeemInfo(long orderCode) {
		String redeemCodes = "";
		Long orderId=0l;
		Long subOrderId=0l;
		OrderItem orderItemParam=new OrderItem();
		if(OrderUtils.isOrderCode(orderCode)==true){//如果是父编码
			orderItemParam.setParentOrderCode(orderCode);
		}else{//如果是子编码
			orderItemParam.setOrderCode(orderCode);
		}
		List<OrderItem> orderItemList = orderItemService.queryByObject(orderItemParam);
		if(CollectionUtils.isEmpty(orderItemList)){
			LOGGER.error("订单数据异常！，订单编号为:"+orderCode);
		}else{
			orderId=orderItemList.get(0).getParentOrderId();
			subOrderId=orderItemList.get(0).getOrderId();
		}
	    OrderInfo orderInfo = orderInfoService.queryById(orderId);
		SimpleDateFormat simFomat = new SimpleDateFormat("yyyy-MM-dd");
		for (OrderItem orderItem : orderItemList) {
			Long supplierId = orderItem.getSupplierId();// 供应商ID
			Integer quantity = orderItem.getQuantity();// 数量
			for (int i = 0; i < quantity; i++) {
				String redeemCode = generateRedeemCode(supplierId);// 生成兑换码
				Map<String, String> effectiveTimeMap = getEffectiveTime(orderItem.getSkuCode());
				String  base64redeemCode = Base64Util.encrypt(redeemCode.getBytes()).replaceAll("\r|\n", "");// 转成base64保存
				OrderRedeemItem orderRedeemItem = new OrderRedeemItem();
				orderRedeemItem.setOrderCode(orderItem.getOrderCode());// 订单编号
				orderRedeemItem.setParentOrderId(orderItem.getParentOrderId());
				orderRedeemItem.setParentOrderCode(orderItem.getParentOrderCode());//父订单ID
				orderRedeemItem.setOrderId(orderId);// 订单ID
				orderRedeemItem.setSkuCode(orderItem.getSkuCode());// SKU编码
				orderRedeemItem.setSalesPrice(orderItem.getSalesPrice());//金额
				orderRedeemItem.setSpuName(orderItem.getSpuName());
				orderRedeemItem.setRedeemCode(base64redeemCode);// 兑换码
				orderRedeemItem.setWarehouseId(orderItem.getWarehouseId());//仓库ID
				if(i<10){
					orderRedeemItem.setRedeemName("券码0" + (i + 1));// 兑换码名称
				}else{
					orderRedeemItem.setRedeemName("券码" + (i + 1));// 兑换码名称
				}
				
				if (MapUtils.getString(effectiveTimeMap, "effectTimeStart") != null) {
					try {
						orderRedeemItem.setEffectTimeStart(
								simFomat.parse(MapUtils.getString(effectiveTimeMap, "effectTimeStart")));
					} catch (ParseException e1) {
						e1.printStackTrace();

					}
				}
				if (MapUtils.getString(effectiveTimeMap, "effectTimeEnd") != null) {
					try {
						orderRedeemItem.setEffectTimeEnd(
								simFomat.parse(MapUtils.getString(effectiveTimeMap, "effectTimeEnd")));
					} catch (ParseException e) {
						e.printStackTrace();

					}
				}

				orderRedeemItem.setSupplierId(supplierId);// 供应商
				orderRedeemItem.setShopName(orderItem.getSupplierName());// 供应商名称（店铺名称）
				orderRedeemItem.setRedeemCodeState(PaymentConstant.REDEEM_CODE_STATUS.NO_USE.code);// 未使用状态
				orderRedeemItemDao.insert(orderRedeemItem);// 保存兑换码
				
				if(i==0){
					redeemCodes = redeemCode;
				}else{
					redeemCodes = redeemCodes + "," + redeemCode;
				}
				
			}
			
		}
		if(StringUtils.isNoneBlank(redeemCodes) && StringUtils.isNoneBlank(orderInfo.getReceiveTel())){//发送短信
			sendRedeemCodesToUser(redeemCodes,orderInfo.getReceiveTel(), orderInfo.getParentOrderCode());
		}
		SubOrder  subOrderUpdate=new  SubOrder();
		subOrderUpdate.setOrderCode(orderCode);
		subOrderUpdate=subOrderService.queryUniqueByObject(subOrderUpdate);
		subOrderUpdate.setOrderStatus(OrderConstant.ORDER_STATUS.RECEIPT.code);//待使用
		subOrderService.updateById(subOrderUpdate);
		LOGGER.info("订单编号："+orderInfo.getParentOrderCode()+"生成的兑换码为："+redeemCodes);
		return redeemCodes;
	}

	/**
	 * 
	 * sendRedeemCodesToUser:(发送兑换码信息). <br/>
	 * 
	 * @author zhouguofeng
	 * @param codes
	 * @param receiverTel
	 * @sinceJDK 1.8
	 */
	private void sendRedeemCodesToUser(String codes, String receiverTel, Long orderCode) {
		String content = "您的订单编号：" + orderCode + ",对应的兑换码为：" + codes;
		try {
			sendSmsService.sendSms(receiverTel, content, "");
			LOGGER.info("兑换码手机接收信息：接收号码："+receiverTel+"接收内容"+content);
		} catch (SmsException e) {

			// TODO Auto-generated catch block
			e.printStackTrace();

		}
	}

	/**
	 * 
	 * getTime:(获取兑换券的有效时间). <br/>
	 * 
	 * @author zhouguofeng
	 * @param itemDetailId
	 * @return
	 * @sinceJDK 1.8
	 */
	public  Map<String, String> getEffectiveTime(String skucode) {
		HashMap<String, String> attributeResult = new HashMap<String, String>();
		ItemSku itemSku = new ItemSku();
		itemSku.setSku(skucode);
		List<ItemSku> itemDetailList = itemSkuService.queryByObject(itemSku);
		if (itemDetailList.size() > 0) {
			ItemSku itemSkuInfo = itemDetailList.get(0);
			ItemAttribute itemAttributePram=new ItemAttribute();
			itemAttributePram.setItemId(itemSkuInfo.getItemId());
			List<ItemAttribute> itemAttributes = itemAttributeService.queryByObject(itemAttributePram);
			for (ItemAttribute itemAttribute : itemAttributes) {
				if ("effectTimeStart".equals(itemAttribute.getAttrKey()) || "effectTimeEnd".equals(itemAttribute.getAttrKey())) {// 开始时间
					attributeResult.put(itemAttribute.getAttrKey(), itemAttribute.getAttrValue());
				}

			}
		}
		return attributeResult;

	}

	/**
	 * 
	 * getOrderRedeemItemByObject:(根据订单ID 查询兑换码信息). <br/>
	 * 
	 * @author zhouguofeng
	 * @param orderID
	 * @return
	 * @sinceJDK 1.8
	 */
	@Override
	public List<OrderRedeemItem> getOrderRedeemItemByObject(Long orderCode) {
		OrderRedeemItem orderRedeemItem = new OrderRedeemItem();
		orderRedeemItem.setOrderCode(orderCode);
		List<OrderRedeemItem> orderRedeemItemList = orderRedeemItemDao.queryByObject(orderRedeemItem);
		return orderRedeemItemList;
	}
	/**
	 * 
	 * getOverdueOrderRedeemItem:(获取过期的兑换码). <br/>  
	 *  
	 * @author zhouguofeng  
	 * @return  
	 * @sinceJDK 1.8
	 */
	public List<OrderRedeemItem> getOverdueOrderRedeemItem(){
		 return  orderRedeemItemDao.queryOverdueOrderRedeemItem();
	}

	/**
	 * 
	 * generateCode:(生成唯一的兑换码). <br/>
	 * 
	 * @author zhouguofeng
	 * @param supId
	 *            供应商ID
	 * @return
	 * @sinceJDK 1.8
	 */
	private String generateCode(Long supplierId) {
		String supId = supplierId.toString();
		String redeemCode;
		String supIdTemp;
		String radom =String.valueOf(System.nanoTime());// 随机数字8位
		System.out.println(radom);
		radom=radom.substring(radom.length()-8, radom.length());
		if (supId.length() > 4) {
			supIdTemp = supId.substring(0, 5);
		} else {// 小于四位 补足0
			NumberFormat formatter = NumberFormat.getNumberInstance();
			formatter.setMinimumIntegerDigits(4);
			formatter.setGroupingUsed(false);
			supIdTemp = formatter.format(Integer.valueOf(supId));// 补足0
		}
		redeemCode = radom + supIdTemp;
		return redeemCode;

	}

	/**
	 * 
	 * generateRedeemCode:(根据供应商ID 生成唯一 兑换码). <br/>
	 * TODO(这里描述这个方法适用条件 – 可选).<br/>
	 * 
	 * @author zhouguofeng
	 * @param supId
	 *            供应商Id
	 * @return
	 * @sinceJDK 1.8
	 */
	private String generateRedeemCode(Long supId) {
		String redeemCode = "";
		while (true) {
			redeemCode = generateCode(supId); 
			OrderRedeemItem orderRedeemItem = new OrderRedeemItem();
			orderRedeemItem.setRedeemCode(redeemCode);
			List<OrderRedeemItem> orderRedeemItemList = orderRedeemItemDao.queryByObject(orderRedeemItem);
			if (CollectionUtils.isEmpty(orderRedeemItemList)) {// 判断是否存在 如果存在 则返回空
				break;
			}

		}
		return redeemCode;

	}
	@Override
	public Double getUnUseAmountByOrderCode(long orderCode) {
		 Double   leftAmount=0D;
		SubOrder subOrder=subOrderService.selectOneByCode(orderCode);//根据orderCode获取订单信息
		List<OrderRedeemItem> orderRedeemItemList = getOrderRedeemItemByObject(subOrder.getOrderCode());
		for(int i=0,len=orderRedeemItemList.size();i<len;i++){
			OrderRedeemItem orderRedeemItem=orderRedeemItemList.get(i);
			if(orderRedeemItem.getRedeemCodeState().equals(PaymentConstant.REDEEM_CODE_STATUS.NO_USE.code)){//更改未使用的状态
				leftAmount=leftAmount+orderRedeemItem.getSalesPrice();
			}
		}
		return leftAmount;
	}
	
	@Override
	public Double getUsedAmountByOrderCode(long orderCode) {
		 Double   leftAmount=0D;
		SubOrder subOrder=subOrderService.selectOneByCode(orderCode);//根据orderCode获取订单信息
		List<OrderRedeemItem> orderRedeemItemList = getOrderRedeemItemByObject(subOrder.getOrderCode());
		for(int i=0,len=orderRedeemItemList.size();i<len;i++){
			OrderRedeemItem orderRedeemItem=orderRedeemItemList.get(i);
			if(orderRedeemItem.getRedeemCodeState().equals(PaymentConstant.REDEEM_CODE_STATUS.USED.code)){//已使用
				leftAmount=leftAmount+orderRedeemItem.getSalesPrice();
			}
		}
		return leftAmount;
	}

	@Override
	public OrderRedeemItemStatistics queryStatisticsByQuery(RedeemItemQuery redeemItemQuery) {
		return orderRedeemItemDao.queryStatisticsByQuery(redeemItemQuery);
	}
	/**
	 * 
	 * 过期取消（可选）.  
	 * @see com.tp.service.ord.IOrderRedeemItemService#cancleOverDueRedeemInfo(long)
	 */
	@Override
	public void cancleOverDueRedeemInfo(long orderCode) {
		SubOrder subOrder=subOrderService.selectOneByCode(orderCode);//根据orderCode获取订单信息
		List<OrderRedeemItem> orderRedeemItemList = getOrderRedeemItemByObject(subOrder.getOrderCode());
		for(int i=0,len=orderRedeemItemList.size();i<len;i++){
			OrderRedeemItem orderRedeemItem=orderRedeemItemList.get(i);
			if(orderRedeemItem.getRedeemCodeState().equals(PaymentConstant.REDEEM_CODE_STATUS.NO_USE.code)){//更改未使用的状态
				orderRedeemItem.setRedeemCodeState(PaymentConstant.REDEEM_CODE_STATUS.OUT_TIME.code);//退款
				orderRedeemItemDao.updateById(orderRedeemItem);//更新兑换码状态
			}
		}
	}
	
	/**
	 * 
	 * getUnusedRedeemCodeRateByOrderCode:(获取未使用的比例). <br/>  
	 *  
	 * @author zhouguofeng  
	 * @param orderCode
	 * @return  
	 * @sinceJDK 1.8
	 */
	public  Double  getUnusedRateByOrderCode(long orderCode){
		List<OrderRedeemItem>  allRedeemCodes=getOrderRedeemItemByObject(orderCode);
		int unUsedCount=0;
		int total=allRedeemCodes.size();
		for(int i=0;i<total;i++){
			OrderRedeemItem orderRedeemItem=allRedeemCodes.get(i);
			if(orderRedeemItem.getRedeemCodeState()==1){
				unUsedCount++;
			}
		}
		
		return Double.valueOf(unUsedCount)/ total;
		
	}
}
