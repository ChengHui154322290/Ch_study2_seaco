package com.tp.proxy.ord;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.util.ExceptionUtils;
import com.tp.common.vo.Constant;
import com.tp.common.vo.OrderConstant;
import com.tp.common.vo.PaymentConstant;
import com.tp.common.vo.Constant.LOG_AUTHOR_TYPE;
import com.tp.common.vo.Constant.SPLIT_SIGN;
import com.tp.common.vo.DAOConstant.MYBATIS_SPECIAL_STRING;
import com.tp.common.vo.FastConstant.USER_TYPE;
import com.tp.common.vo.PaymentConstant.REDEEM_CODE_STATUS;
import com.tp.common.vo.ord.OrderStatusLogConstant.LOG_TYPE;
import com.tp.dto.common.FailInfo;
import com.tp.dto.common.ResultInfo;
import com.tp.dto.ord.GroupCouponExchangeDto;
import com.tp.dto.ord.OrderReceiveGoodsDTO;
import com.tp.model.dss.FastUserInfo;
import com.tp.model.ord.OrderRedeemItem;
import com.tp.model.ord.OrderStatusLog;
import com.tp.model.ord.SubOrder;
import com.tp.proxy.BaseProxy;
import com.tp.query.ord.RedeemItemQuery;
import com.tp.result.ord.OrderRedeemItemStatistics;
import com.tp.service.IBaseService;
import com.tp.service.dss.IFastUserInfoService;
import com.tp.service.ord.IOrderRedeemItemService;
import com.tp.service.ord.IOrderStatusLogService;
import com.tp.service.ord.ISubOrderService;
import com.tp.service.ord.remote.ISalesOrderRemoteService;
import com.tp.util.Base64;
import com.tp.util.Base64Util;
import com.tp.util.ErWeiMaUtil;
/**
 * 商家线下购买兑换码信息代理层
 * 
 * @author szy
 *
 */
@Service
public class OrderRedeemItemProxy extends BaseProxy<OrderRedeemItem> {
	
	private final static Logger logger = LoggerFactory.getLogger(OrderRedeemItemProxy.class);
	
	@Autowired
	private IOrderRedeemItemService orderRedeemItemService;
	@Autowired
	private IOrderStatusLogService orderStatusLogService;
	@Autowired
	private IFastUserInfoService fastUserInfoService;
	@Autowired
	private ISubOrderService subOrderService;
	@Autowired
	private ISalesOrderRemoteService salesOrderRemoteService;
	
	@Override
	public IBaseService<OrderRedeemItem> getService() {
		return orderRedeemItemService;
	}

	public List<OrderRedeemItem> getOrderRedeemItemByOrderCode(long orderCode) {
		SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");// 设置日期格式
		OrderRedeemItem orderRedeemItem = new OrderRedeemItem();
		orderRedeemItem.setOrderCode(orderCode);
		List<OrderRedeemItem> orderRedeemItemList = orderRedeemItemService.queryByObject(orderRedeemItem);
		for (OrderRedeemItem orderRedeemItemInfo : orderRedeemItemList) {
			byte[] redeemCodeBase64 = Base64Util.decrypt(orderRedeemItemInfo.getRedeemCode());// 将base64转化为明码
			try {
				String redeemCode = new String(redeemCodeBase64, "UTF-8");
				String qrCodeContent = redeemCode + "||" + df.format(new Date());
				qrCodeContent=Base64Util.encrypt(qrCodeContent.getBytes()).replaceAll("\r|\n", "");//转化为base64
				ByteArrayOutputStream output = new ByteArrayOutputStream();
				ErWeiMaUtil.encoderQRCode(qrCodeContent,output,"png", 7);
				String imgData = Base64.encode(output.toByteArray());
				orderRedeemItemInfo.setQrCode(imgData);// 设置二维码
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
		return orderRedeemItemList;
	}

	
	
	public ResultInfo<OrderRedeemItem> exchangeCode(GroupCouponExchangeDto groupCouponExchange){
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("mobile", groupCouponExchange.getUserMobile());
		params.put("enabled", Constant.ENABLED.YES);
		params.put(MYBATIS_SPECIAL_STRING.COLUMNS.name(), "user_type in ("+USER_TYPE.MANAGER.code+SPLIT_SIGN.COMMA+USER_TYPE.EMPLOYEE.code+")");
		FastUserInfo fastUserInfo = fastUserInfoService.queryUniqueByParams(params);
		if(fastUserInfo==null){
			return new ResultInfo<>(new FailInfo("您无权核销兑换券",103020));
		}
		params.clear();
		String origCode = groupCouponExchange.getExchangeCode();
		try {
			String redeemCodeBase64=Base64Util.encrypt(origCode.getBytes("UTF-8")).replaceAll("\r|\n", "");//将base64转化为明码
			params.put("redeemCode", redeemCodeBase64);
			logger.info("二维码转码 原{}，base64:{}",origCode,redeemCodeBase64);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();  
		}
		OrderRedeemItem orderRedeemItem = this.queryUniqueByParams(params).getData();
		if(orderRedeemItem==null){
			return new ResultInfo<>(new FailInfo("兑换券不存在",103021));
		}
		SubOrder subOrder = subOrderService.selectOneByCode(orderRedeemItem.getOrderCode());
		if(!subOrder.getWarehouseId().equals(fastUserInfo.getWarehouseId())){
			return new ResultInfo<>(new FailInfo("不能核销其它店铺的兑换券",103022));
		}
		if(PaymentConstant.REDEEM_CODE_STATUS.USED.code.equals(orderRedeemItem.getRedeemCodeState())){
			return new ResultInfo<>(new FailInfo("兑换券已使用",103024));
		}else if(PaymentConstant.REDEEM_CODE_STATUS.OUT_TIME.code.equals(orderRedeemItem.getRedeemCodeState())){
			return new ResultInfo<>(new FailInfo("兑换券已过期",103025));
		}else if(!PaymentConstant.REDEEM_CODE_STATUS.NO_USE.code.equals(orderRedeemItem.getRedeemCodeState())){
			return new ResultInfo<>(new FailInfo("兑换券已作废",103026));
		}
		orderRedeemItem.setRedeemCodeState(REDEEM_CODE_STATUS.USED.code);
		orderRedeemItem.setUpdateTime(new Date());
		orderRedeemItem.setUpdateUser(fastUserInfo.getUserName());
		OrderReceiveGoodsDTO orderReceiveGoodsDTO = new OrderReceiveGoodsDTO();
		orderReceiveGoodsDTO.setReceiptDate(new Date());
		orderReceiveGoodsDTO.setSubOrderCode(orderRedeemItem.getOrderCode());
		try{
			orderRedeemItemService.updateNotNullById(orderRedeemItem);
			orderStatusLogService.insert(initExchageLog(orderRedeemItem,groupCouponExchange));
			params.clear();
			params.put("orderCode", orderRedeemItem.getOrderCode());
			params.put(MYBATIS_SPECIAL_STRING.COLUMNS.name()," redeem_code_state!="+PaymentConstant.REDEEM_CODE_STATUS.USED.code);
			Integer count = orderRedeemItemService.queryByParamCount(params);
			if(count==null || count==0){
				salesOrderRemoteService.operateOrderForReceiveGoods(orderReceiveGoodsDTO);
			}
			orderRedeemItem.setRedeemCode(origCode);
			return new ResultInfo<>(orderRedeemItem);
		}catch(Throwable exception){
			return new ResultInfo<>(ExceptionUtils.println(new FailInfo(1,exception), logger, groupCouponExchange));
		}
	}
	
	public ResultInfo<OrderRedeemItemStatistics> queryStatisticsByQuery(RedeemItemQuery redeemItemQuery){
		try{
			OrderRedeemItemStatistics result= orderRedeemItemService.queryStatisticsByQuery(redeemItemQuery);
			if(result==null){
				result=new OrderRedeemItemStatistics();
			}
			return new ResultInfo<>(result);
		}catch(Throwable exception){
			FailInfo failInfo = ExceptionUtils.print(new FailInfo(exception), logger,redeemItemQuery);
			return new ResultInfo<>(failInfo);
		}
	}
	
	private OrderStatusLog initExchageLog(OrderRedeemItem orderRedeemItem,GroupCouponExchangeDto dto){
		OrderStatusLog log = new OrderStatusLog();
		log.setCreateTime(new Date());
		log.setCreateUserId(dto.getFastUserId());
		log.setCreateUserName(dto.getUserName());
		log.setCreateUserType(LOG_AUTHOR_TYPE.SELLER.code);
		log.setCurrStatus(OrderConstant.ORDER_STATUS.UNUSE.code);
		log.setName("使用兑换券");
		log.setOrderCode(orderRedeemItem.getOrderCode());
		log.setParentOrderCode(orderRedeemItem.getParentOrderCode());
		log.setPreStatus(OrderConstant.ORDER_STATUS.UNUSE.code);
		log.setType(LOG_TYPE.MONITOR.code);
		log.setContent(String.format("核销券 [%s]店家员工[%s]核销团购券，券号[%s]",dto.getSupplierName(),dto.getUserName(),orderRedeemItem.getRedeemCode()));
		return log;
	}
	
	public static void main(String args[]) {
		SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");// 设置日期格式
		String redeemCode = "MzU1NTgzNTIwMDQ5";
		String redeemCodeshow = "";
		byte[] redeemCodeBase64 = Base64Util.decrypt(redeemCode);// 将base64转化为明码
		try {
			redeemCodeshow = new String(redeemCodeBase64, "UTF-8");
		} catch (UnsupportedEncodingException e) {

			// TODO Auto-generated catch block
			e.printStackTrace();

		}
		String qrCode = redeemCodeshow + "||" + df.format(new Date());
		System.out.println(qrCode);
	}
}
