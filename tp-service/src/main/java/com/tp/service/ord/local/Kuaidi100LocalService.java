/**
 * NewHeight.com Inc.
 * Copyright (c) 2007-2014 All Rights Reserved.
 */
package com.tp.service.ord.local;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.alibaba.fastjson.JSONObject;
import com.tp.common.vo.Constant;
import com.tp.common.vo.ord.OrderDeliveryConstant;
import com.tp.common.vo.ord.OrderErrorCodes;
import com.tp.dto.ord.kuaidi100.ExpressInfo;
import com.tp.dto.ord.kuaidi100.ExpressResult;
import com.tp.dto.ord.kuaidi100.PushExpressInfoRequest;
import com.tp.dto.ord.kuaidi100.Result;
import com.tp.dto.ord.kuaidi100.ResultItem;
import com.tp.dto.ord.kuaidi100.SubscribeResult;
import com.tp.model.ord.Kuaidi100Express;
import com.tp.model.ord.Kuaidi100Subscribe;
import com.tp.model.ord.OrderDelivery;
import com.tp.model.ord.RejectInfo;
import com.tp.service.ord.IExpressForKuaidi100Service;
import com.tp.service.ord.IKuaidi100ExpressService;
import com.tp.service.ord.IKuaidi100SubscribeService;
import com.tp.service.ord.IOrderDeliveryService;
import com.tp.service.ord.IRejectInfoService;
import com.tp.service.ord.local.IKuaidi100LocalService;

/**
 * <pre>
 * 
 * </pre>
 * 
 * @author szy
 * @time 2015-2-2 下午6:02:28
 */
@Service
public class Kuaidi100LocalService implements IKuaidi100LocalService {
	private static final Logger logger = LoggerFactory.getLogger(Kuaidi100LocalService.class);

	private static Boolean IS_DEALING = false;
	/** 快递100的KEY */
	@Value("#{meta['kuaidi100.key']}")
	private String kuaidi100Key = "rddvqvGK9432";

	/** 默认推送次数 */
	private static final Integer DEFAULT_KUAIDI100_POST_TIMES = 5;
	/** 默认每次处理数量 */
	private static final Integer DEFAULT_DEAL_NUM_ONCE = 500;

	@Value("#{meta['kuaidi100.callback.domain']}")
	public String orderDomain;

	@Autowired
	private IOrderDeliveryService orderDeliveryService;
	@Autowired
	private IExpressForKuaidi100Service expressForKuaidi100Service;
	@Autowired
	private IKuaidi100SubscribeService kuaidi100SubscribeService;
	@Autowired
	private IKuaidi100ExpressService kuaidi100ExpressService;
	@Autowired
	private IRejectInfoService rejectInfoService;

	@Override
	public void pushExpressToKuaidi100() {
		if (!IS_DEALING) {
			// 处理订单的物流信息
			try {
				boolean b = false;
				do {
					IS_DEALING = true;
					b = pushOrderExpress();
				} while (b);
			} catch (Exception e) {
				logger.error("处理推送快递100平台订单数据异常", e);
			}

			// 处理退货单的物流信息
			try {
				boolean b1 = false;
				do {
					b1 = pushRejectExpress();
				} while (b1);
			} catch (Exception e) {
				logger.error("处理推送快递100平台订单数据异常", e);
			}
			IS_DEALING = false;
		}
	}

	/**
	 * 
	 * <pre>
	 * 推送退货单的运单信息
	 * </pre>
	 * 
	 * @return
	 */
	private boolean pushRejectExpress() {
		RejectInfo rejectInfo = new RejectInfo();
		rejectInfo.setPageSize(DEFAULT_DEAL_NUM_ONCE);
		rejectInfo.setPostKuaidi100(OrderDeliveryConstant.postKuaidi100Status.POST_FALSE.code);
		rejectInfo.setPostKuaidi100Times(DEFAULT_KUAIDI100_POST_TIMES);
		List<RejectInfo> rejectInfoDOs = rejectInfoService.queryNotSuccessPostKuaidi100List(rejectInfo);
		if (CollectionUtils.isNotEmpty(rejectInfoDOs)) {
			String callbackurlString = orderDomain + "/kuaidi100/gainExpress/";
			List<Kuaidi100Subscribe> kuaidi100SubscribeDOList = new ArrayList<Kuaidi100Subscribe>();
			List<RejectInfo> dealRejectInfoList = new ArrayList<RejectInfo>();
			for (RejectInfo tmpRejectInfo : rejectInfoDOs) {
				// 推送给快递100平台
				PushExpressInfoRequest pushReq = new PushExpressInfoRequest();
				pushReq.setCompany(tmpRejectInfo.getCompanyCode());
				pushReq.setNumber(tmpRejectInfo.getExpressNo());
				pushReq.setFrom(null);
				pushReq.setTo(tmpRejectInfo.getReturnAddress());
				pushReq.setKey(kuaidi100Key);
				pushReq.getParameters().put("callbackurl", callbackurlString + tmpRejectInfo.getRejectCode() + ".htm");
				SubscribeResult result = expressForKuaidi100Service.pushExpressInfoToKuaidi100(pushReq);

				// 构建推送快递100记录对象
				Kuaidi100Subscribe ks = new Kuaidi100Subscribe();
				ks.setOrderCode(tmpRejectInfo.getOrderCode());
				ks.setRejectCode(tmpRejectInfo.getRejectCode());
				ks.setDeliveryOrderType(OrderDeliveryConstant.deliveryOrderType.REJECT.code);
				ks.setPackageNo(tmpRejectInfo.getExpressNo());
				ks.setCompanyId(tmpRejectInfo.getCompanyCode());
				ks.setStartCity(null);
				ks.setEndCity(tmpRejectInfo.getReturnAddress());
				ks.setReqData(JSONObject.toJSONString(pushReq));
				ks.setResResult(String.valueOf(result.getResult()));
				ks.setResReturnCode(result.getReturnCode());
				ks.setResReturnMessage(result.getMessage());
				kuaidi100SubscribeDOList.add(ks);

				// 构建推送结果对象
				RejectInfo riDo = new RejectInfo();
				riDo.setRejectId(tmpRejectInfo.getRejectId());
				if (String.valueOf(OrderErrorCodes.SYSTEM_ERROR).equals(result.getReturnCode())) {
					riDo.setPostKuaidi100(OrderDeliveryConstant.postKuaidi100Status.POST_FALSE.code);
				} else {
					riDo.setPostKuaidi100(OrderDeliveryConstant.postKuaidi100Status.POST_TRUE.code);
				}
				dealRejectInfoList.add(riDo);
			}
			// 记录推送日志
			if (CollectionUtils.isNotEmpty(kuaidi100SubscribeDOList)) {
				kuaidi100SubscribeService.batchInsert(kuaidi100SubscribeDOList);
			}
			// 更新订单物流表中推送记录
			if (CollectionUtils.isNotEmpty(dealRejectInfoList)) {
				rejectInfoService.batchUpdatePostKuaidi100(dealRejectInfoList);
			}
			return true;
		}
		return false;
	}

	/**
	 * 
	 * <pre>
	 * 推送订单的运单信息
	 * </pre>
	 * 
	 * @return
	 */
	@Transactional(propagation = Propagation.REQUIRED)
	private boolean pushOrderExpress() {
		OrderDelivery query = new OrderDelivery();
		query.setPageSize(DEFAULT_DEAL_NUM_ONCE);
		query.setPostKuaidi100(OrderDeliveryConstant.postKuaidi100Status.POST_FALSE.code);
		query.setPostKuaidi100Times(DEFAULT_KUAIDI100_POST_TIMES);
		List<OrderDelivery> orderDeliveryDOs = orderDeliveryService.queryNotSuccessPostKuaidi100List(query);
		if (CollectionUtils.isNotEmpty(orderDeliveryDOs)) {

			String callbackurlString = orderDomain + "/kuaidi100/gainExpress/";
			List<Kuaidi100Subscribe> kuaidi100SubscribeDOList = new ArrayList<Kuaidi100Subscribe>();
			List<OrderDelivery> dealOrderDeliveryList = new ArrayList<OrderDelivery>();
			for (OrderDelivery orderDelivery : orderDeliveryDOs) {
				PushExpressInfoRequest pushReq = new PushExpressInfoRequest();
				pushReq.setCompany(orderDelivery.getCompanyId());
				pushReq.setNumber(orderDelivery.getPackageNo());
				pushReq.setFrom(orderDelivery.getStartCity());
				pushReq.setTo(orderDelivery.getEndCity());

				pushReq.setKey(kuaidi100Key);
				pushReq.getParameters().put("callbackurl", callbackurlString + orderDelivery.getOrderCode() + ".htm");
				SubscribeResult result = expressForKuaidi100Service.pushExpressInfoToKuaidi100(pushReq);
				// 构建推送快递100记录对象
				Kuaidi100Subscribe ks = new Kuaidi100Subscribe();
				ks.setOrderCode(orderDelivery.getOrderCode());
				ks.setRejectCode(null);
				ks.setDeliveryOrderType(OrderDeliveryConstant.deliveryOrderType.ORDER.code);
				ks.setPackageNo(orderDelivery.getPackageNo());
				ks.setCompanyId(orderDelivery.getCompanyId());
				ks.setStartCity(orderDelivery.getStartCity());
				ks.setEndCity(orderDelivery.getEndCity());
				ks.setReqData(JSONObject.toJSONString(pushReq));
				ks.setResResult(String.valueOf(result.getResult()));
				ks.setResReturnCode(result.getReturnCode());
				ks.setResReturnMessage(result.getMessage());
				kuaidi100SubscribeDOList.add(ks);

				// 构建订单物流推送结果对象
				OrderDelivery odDo = new OrderDelivery();
				odDo.setId(orderDelivery.getId());
				if (String.valueOf(OrderErrorCodes.SYSTEM_ERROR).equals(result.getReturnCode())) {
					odDo.setPostKuaidi100(OrderDeliveryConstant.postKuaidi100Status.POST_FALSE.code);
				} else {
					odDo.setPostKuaidi100(OrderDeliveryConstant.postKuaidi100Status.POST_TRUE.code);
				}
				dealOrderDeliveryList.add(odDo);
			}
			// 记录推送日志
			if (CollectionUtils.isNotEmpty(kuaidi100SubscribeDOList)) {
				kuaidi100SubscribeService.batchInsert(kuaidi100SubscribeDOList);
			}
			// 更新订单物流表中推送记录
			if (CollectionUtils.isNotEmpty(dealOrderDeliveryList)) {
				orderDeliveryService.batchUpdatePostKuaidi100(dealOrderDeliveryList);
			}
			return true;
		}
		return false;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public ExpressResult saveExpressInfo(Long code, ExpressInfo expressInfo) {
		Assert.notNull(expressInfo, "接口请求参数不可为空");
		Result lastResult = expressInfo.getLastResult();
		String postData = JSONObject.toJSONString(expressInfo);
		List<Kuaidi100Express> kuaidi100ExpressDOList = new ArrayList<Kuaidi100Express>();
		String packageNo = null;
		if (lastResult != null) {
			packageNo = lastResult.getNu();
			Kuaidi100Express kuaidi100Express = null;
			ArrayList<ResultItem> data = lastResult.getData();
			if (CollectionUtils.isNotEmpty(data)) {
				for (ResultItem resultItem : data) {
					kuaidi100Express = new Kuaidi100Express();
					if (String.valueOf(code).startsWith(Constant.DOCUMENT_TYPE.RETURNED.code.toString())) {// 退货
						kuaidi100Express.setRejectCode(code);
						kuaidi100Express.setDeliveryOrderType(OrderDeliveryConstant.deliveryOrderType.REJECT.code);
					} else {
						kuaidi100Express.setOrderCode(code);
						kuaidi100Express.setDeliveryOrderType(OrderDeliveryConstant.deliveryOrderType.ORDER.code);
					}
					kuaidi100Express.setMonitorStatus(expressInfo.getStatus());
					kuaidi100Express.setMonitorMessage(expressInfo.getMessage());
					kuaidi100Express.setStatus(lastResult.getState());
					kuaidi100Express.setCompanyId(lastResult.getCom());
					kuaidi100Express.setPackageNo(lastResult.getNu());
					kuaidi100Express.setIsCheck(lastResult.getIscheck());
					kuaidi100Express.setPostData(postData);

					kuaidi100Express.setDataContext(resultItem.getContext());
					kuaidi100Express.setDataTime(resultItem.getTime());
					kuaidi100Express.setDataFtime(resultItem.getFtime());

					kuaidi100ExpressDOList.add(kuaidi100Express);
				}

			} else {
				kuaidi100Express = new Kuaidi100Express();
				if (String.valueOf(code).startsWith(Constant.DOCUMENT_TYPE.RETURNED.code.toString())) {// 退货
					kuaidi100Express.setRejectCode(code);
					kuaidi100Express.setDeliveryOrderType(OrderDeliveryConstant.deliveryOrderType.REJECT.code);
				} else {
					kuaidi100Express.setOrderCode(code);
					kuaidi100Express.setDeliveryOrderType(OrderDeliveryConstant.deliveryOrderType.ORDER.code);
				}
				kuaidi100Express.setMonitorStatus(expressInfo.getStatus());
				kuaidi100Express.setMonitorMessage(expressInfo.getMessage());
				kuaidi100Express.setStatus(lastResult.getState());
				kuaidi100Express.setCompanyId(lastResult.getCom());
				kuaidi100Express.setPackageNo(lastResult.getNu());
				kuaidi100Express.setIsCheck(lastResult.getIscheck());
				kuaidi100Express.setPostData(postData);
				kuaidi100ExpressDOList.add(kuaidi100Express);
			}
		}
		if (CollectionUtils.isNotEmpty(kuaidi100ExpressDOList) && null!=code && StringUtils.isNotBlank(packageNo)) {
			// 因为快递100平台每次推送的数据都是全量信息，所以记录数据前需要先把旧的数据删除
			kuaidi100ExpressService.deleteOldExpressInfo(code, packageNo);
			// 批量记录数据
			kuaidi100ExpressService.batchInsert(kuaidi100ExpressDOList);
		}

		if ("abort".equals(expressInfo.getStatus())) {// abort“3天查询无记录”或“60天无变化时”
			// 将推送状态改为失败，由定时器重新推送
			if(String.valueOf(code).startsWith(Constant.DOCUMENT_TYPE.RETURNED.code.toString())){
				RejectInfo rejectInfo=new RejectInfo();
				rejectInfo.setPostKuaidi100(OrderDeliveryConstant.postKuaidi100Status.POST_FALSE.code);
				rejectInfo.setExpressNo(lastResult.getNu());
				rejectInfo.setRejectCode(code);
				rejectInfoService.updatePostKuaidi100(rejectInfo);
			}else {
				OrderDelivery orderDelivery = new OrderDelivery();
				orderDelivery.setPostKuaidi100(OrderDeliveryConstant.postKuaidi100Status.POST_FALSE.code);
				orderDelivery.setPackageNo(lastResult.getNu());
				orderDelivery.setOrderCode(code);
				orderDeliveryService.updatePostKuaidi100(orderDelivery);
			}
		}
		return new ExpressResult(true, "200", "处理成功");
	}

}
