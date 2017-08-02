package com.tp.service.dss.mq;

import static com.tp.util.BigDecimalUtil.add;
import static com.tp.util.BigDecimalUtil.divide;
import static com.tp.util.BigDecimalUtil.formatToPrice;
import static com.tp.util.BigDecimalUtil.multiply;
import static com.tp.util.BigDecimalUtil.toPrice;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.vo.DssConstant;
import com.tp.common.vo.Constant.AUTHOR_TYPE;
import com.tp.common.vo.DssConstant.ACCOUNT_TYPE;
import com.tp.common.vo.DssConstant.BUSSINESS_TYPE;
import com.tp.common.vo.OrderConstant.ORDER_STATUS;
import com.tp.model.dss.CommisionDetail;
import com.tp.model.dss.GlobalCommision;
import com.tp.model.dss.PromoterInfo;
import com.tp.model.ord.OrderItem;
import com.tp.model.ord.SubOrder;
import com.tp.mq.MqMessageCallBack;
import com.tp.service.dss.ICommisionDetailService;
import com.tp.service.dss.IGlobalCommisionService;
import com.tp.service.dss.IPromoterInfoService;

/**
 * 订单发货成功后增加佣金明细
 * @author szy
 *
 */
@Service
public class OrderCommisionConsumer implements MqMessageCallBack {
	
	private static final Logger logger = LoggerFactory.getLogger(ReferralFeesConsumer.class);
	
	@Autowired
	private ICommisionDetailService commisionDetailService;
	@Autowired
	private IPromoterInfoService promoterInfoService;
	@Autowired
	private IGlobalCommisionService globalCommisionService;
	
	@Override
	public boolean execute(Object o) {
		SubOrder subOrder = (SubOrder)o;
		if(subOrder.getDiscount()==null){
			subOrder.setDiscount(0d);
		}
		List<CommisionDetail> commisionDetailList = new ArrayList<CommisionDetail>();
//		if(subOrder.getPromoterId()!=null){
//			PromoterInfo promoterInfo = promoterInfoService.queryById(subOrder.getPromoterId());
//			if(promoterInfo!=null){
//				commisionDetailList.add(initCommisionDetail(subOrder,promoterInfo.getCommisionRate(),subOrder.getPromoterId(),promoterInfo.getPromoterType(),DssConstant.INDIRECT_TYPE.NO.code,subOrder.getPromoterId()));
//			}
//		}
		
		if(subOrder.getShopPromoterId()!=null && subOrder.getShopPromoterId()!=0){		// 店铺
			GlobalCommision globalCommision = globalCommisionService.queryLastGlobalCommision();
			if(globalCommision==null){
				logger.error("未设置全局提佣比率");
			}else{
				PromoterInfo promoterInfo = promoterInfoService.queryById(subOrder.getShopPromoterId());
				if(promoterInfo!=null && promoterInfo.getParentPromoterId()!=null && 0!=promoterInfo.getParentPromoterId()){
					PromoterInfo promoter = promoterInfoService.queryById(promoterInfo.getParentPromoterId());
					if(promoter!=null){
						promoterInfo.setTopPromoterId(promoter.getParentPromoterId());
					}
				}
				if(promoterInfo!=null){
					subOrder.getOrderItemList().forEach(new Consumer<OrderItem>(){
						public void accept(OrderItem t) {
							if(t.getCommisionRate()!=null && t.getCommisionRate()>0){
								commisionDetailList.add(initCommisionDetail(t,globalCommision.getCurrentCommisionRate(promoterInfo, t.getCommisionRate()),subOrder.getShopPromoterId(),DssConstant.PROMOTER_TYPE.DISTRIBUTE.code,DssConstant.INDIRECT_TYPE.NO.code,promoterInfo.getPromoterId()));
								if(promoterInfo.getParentPromoterId()!=null && 0!=promoterInfo.getParentPromoterId()){
									commisionDetailList.add(initCommisionDetail(t,globalCommision.getParentCommisionRate(promoterInfo, t.getCommisionRate()),promoterInfo.getParentPromoterId(),DssConstant.PROMOTER_TYPE.DISTRIBUTE.code,DssConstant.INDIRECT_TYPE.YES.code,promoterInfo.getPromoterId()));
									if(promoterInfo.getTopPromoterId()!=null && 0!= promoterInfo.getTopPromoterId() && globalCommision.getThreeCommisionRate()!=null){
										commisionDetailList.add(initCommisionDetail(t,globalCommision.getTopCommisionRate(promoterInfo, t.getCommisionRate()),promoterInfo.getTopPromoterId(),DssConstant.PROMOTER_TYPE.DISTRIBUTE.code,DssConstant.INDIRECT_TYPE.GRANDSON.code,promoterInfo.getPromoterId()));
									}
								}
							}else{
								logger.info("orderCode[{}]orderItem[{}]commisionRate is zero!",t.getOrderCode(),t.getId());
							}
						}
					});
				}
			}
		}else if(subOrder.getPromoterId()!=null && subOrder.getPromoterId()!=0){	// 卡券
			PromoterInfo promoterInfo = promoterInfoService.queryById(subOrder.getPromoterId());
			if(promoterInfo!=null){
				subOrder.getOrderItemList().forEach(new Consumer<OrderItem>(){
					public void accept(OrderItem t) {
						if(t.getCommisionRate()!=null && t.getCommisionRate()>0){
//							commisionDetailList.add(initCommisionDetail(subOrder, t.getCommisionRate().floatValue(), subOrder.getPromoterId(),promoterInfo.getPromoterType(),DssConstant.INDIRECT_TYPE.NO.code,subOrder.getPromoterId()));
							commisionDetailList.add(initCommisionDetail(t, t.getCommisionRate(), subOrder.getPromoterId(),promoterInfo.getPromoterType(),DssConstant.INDIRECT_TYPE.NO.code,promoterInfo.getPromoterId()));
						}
					}
				});
			}
		}else if(subOrder.getScanPromoterId()!=null && subOrder.getScanPromoterId()!=0){	// 扫码关注
			PromoterInfo promoterInfo = promoterInfoService.queryById(subOrder.getScanPromoterId());
			if(promoterInfo!=null){
				subOrder.getOrderItemList().forEach(new Consumer<OrderItem>(){
					public void accept(OrderItem t) {
						GlobalCommision globalCommision = globalCommisionService.queryLastGlobalCommision();
						if(t.getCommisionRate()!=null && t.getCommisionRate()>0
							&& globalCommision!=null && globalCommision.getScanCommisionRate()!=null && globalCommision.getScanCommisionRate()>0){
							Double finalScanRate = multiply(t.getCommisionRate(),divide( globalCommision.getScanCommisionRate(),100) ).doubleValue();							
							commisionDetailList.add(initCommisionDetail(t, finalScanRate, subOrder.getScanPromoterId(),promoterInfo.getPromoterType(),DssConstant.INDIRECT_TYPE.NO.code,promoterInfo.getPromoterId()));
						}
					}
				});
			}
		}

		
		
		
		if(CollectionUtils.isNotEmpty(commisionDetailList)){
			commisionDetailService.insertByOrder(commisionDetailList);
		}
		return true;
	}

	private CommisionDetail initCommisionDetail(SubOrder subOrder,Float commisionRate,Long promoterId,Integer promoterType,Integer indirect,Long giver){
		return initCommisionDetail(subOrder,commisionRate,formatToPrice(add(subOrder.getDiscount(),subOrder.getPayTotal())).doubleValue(),promoterId,promoterType,indirect,giver);
	}
	
	private CommisionDetail initCommisionDetail(SubOrder subOrder,Float commisionRate,Double bizAmount,Long promoterId,Integer promoterType,Integer indirect,Long giver){
		CommisionDetail commisionDetail = new CommisionDetail();
		commisionDetail.setBizAmount(bizAmount);
		commisionDetail.setBizCode(subOrder.getOrderCode());
		commisionDetail.setBizType(BUSSINESS_TYPE.ORDER.code);
		commisionDetail.setCommisionRate(commisionRate);
		commisionDetail.setCommision(formatToPrice(divide(multiply(commisionDetail.getBizAmount(),commisionDetail.getCommisionRate()),100)).doubleValue());
		commisionDetail.setCouponAmount(subOrder.getDiscount());
		commisionDetail.setCreateUser(AUTHOR_TYPE.SYSTEM);
		commisionDetail.setMemberId(subOrder.getMemberId());
		commisionDetail.setOperateType(ACCOUNT_TYPE.INCOMING.code);
		commisionDetail.setOrderAmount(subOrder.getPayTotal());
		commisionDetail.setOrderCode(subOrder.getOrderCode());
		commisionDetail.setOrderStatus(ORDER_STATUS.RECEIPT.code);
		commisionDetail.setCollectStatus(DssConstant.COLLECT_STATUS.NO.code);
		commisionDetail.setPromoterId(promoterId);
		commisionDetail.setPromoterType(promoterType);
		commisionDetail.setUpdateUser(commisionDetail.getCreateUser());
		commisionDetail.setIndirect(indirect);
		commisionDetail.setGiver(giver);
		return commisionDetail;
	}
	
	private CommisionDetail initCommisionDetail(OrderItem orderItem,Double commisionRate,Long promoterId,Integer promoterType,Integer indirect,Long giver){
		CommisionDetail commisionDetail = new CommisionDetail();
		commisionDetail.setBizAmount(toPrice(add(orderItem.getSubTotal(),orderItem.getCouponAmount())));
		/**if(orderItem.getPoints()!=null && orderItem.getPoints()>0){
			commisionDetail.setBizAmount(toPrice(add(commisionDetail.getBizAmount(),divide(orderItem.getPoints(),100))));
		}*/
		commisionDetail.setBizCode(orderItem.getId());
		commisionDetail.setBizType(BUSSINESS_TYPE.ORDER.code);
		commisionDetail.setCommisionRate(multiply(commisionRate,100).floatValue());
		commisionDetail.setCommision(toPrice(multiply(commisionDetail.getBizAmount(),commisionRate)).doubleValue());
		commisionDetail.setCouponAmount(orderItem.getCouponAmount());
		/**if(orderItem.getPoints()!=null && orderItem.getPoints()>0){
			commisionDetail.setCouponAmount(toPrice(add(commisionDetail.getCouponAmount(),divide(orderItem.getPoints(),100))));
		}*/
		commisionDetail.setCreateUser(AUTHOR_TYPE.SYSTEM);
		commisionDetail.setMemberId(orderItem.getMemberId());
		commisionDetail.setOperateType(ACCOUNT_TYPE.INCOMING.code);
		commisionDetail.setOrderAmount(orderItem.getSubTotal());
		commisionDetail.setOrderCode(orderItem.getOrderCode());
		commisionDetail.setOrderStatus(ORDER_STATUS.RECEIPT.code);
		commisionDetail.setCollectStatus(DssConstant.COLLECT_STATUS.NO.code);
		commisionDetail.setPromoterId(promoterId);
		commisionDetail.setPromoterType(promoterType);
		commisionDetail.setUpdateUser(commisionDetail.getCreateUser());
		commisionDetail.setIndirect(indirect);
		commisionDetail.setGiver(giver);
		return commisionDetail;
	}
}
