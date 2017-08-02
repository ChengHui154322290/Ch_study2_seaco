package com.tp.service.dss.mq;

import static com.tp.util.BigDecimalUtil.divide;
import static com.tp.util.BigDecimalUtil.formatToPrice;
import static com.tp.util.BigDecimalUtil.multiply;
import static com.tp.util.BigDecimalUtil.subtract;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.vo.DssConstant;
import com.tp.common.vo.Constant.SPLIT_SIGN;
import com.tp.common.vo.DAOConstant.MYBATIS_SPECIAL_STRING;
import com.tp.model.dss.CommisionDetail;
import com.tp.model.ord.RefundInfo;
import com.tp.model.ord.RejectInfo;
import com.tp.model.ord.RejectItem;
import com.tp.mq.MqMessageCallBack;
import com.tp.service.dss.ICommisionDetailService;
import com.tp.service.ord.IRejectInfoService;
import com.tp.service.ord.IRejectItemService;

/**
 * 退款（退货)成功后冲佣金
 * @author szy
 *
 */
@Service
public class RefundCommisionConsumer implements MqMessageCallBack {
	
	private static final Logger logger = LoggerFactory.getLogger(ReferralFeesConsumer.class);
	
	@Autowired
	private ICommisionDetailService commisionDetailService;
	@Autowired
	private IRejectInfoService rejectInfoService;
	@Autowired
	private IRejectItemService rejectItemService;
	
	@Override
	public boolean execute(Object o) {
		RefundInfo refundInfo = (RefundInfo)o;
		Long orderCode = refundInfo.getOrderCode();
		List<Long> orderItemIdList = getCommsionRate(refundInfo);
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("orderCode", orderCode);
		params.put("bizType", DssConstant.BUSSINESS_TYPE.ORDER.code);
		params.put(MYBATIS_SPECIAL_STRING.COLUMNS.name(), " (promoter_type="+DssConstant.PROMOTER_TYPE.COUPON.code+(CollectionUtils.isNotEmpty(orderItemIdList)?(" or biz_code in ("+StringUtils.join(orderItemIdList, SPLIT_SIGN.COMMA)+")"):"")+")");
		List<CommisionDetail> commisionDetailList = commisionDetailService.queryByParam(params);
		if(CollectionUtils.isNotEmpty(commisionDetailList)){
			commisionDetailList.forEach(new Consumer<CommisionDetail>(){
				@Override
				public void accept(CommisionDetail commisionDetail) {
					commisionDetail.setBizAmount(refundInfo.getRefundAmount());
					commisionDetail.setBizType(DssConstant.BUSSINESS_TYPE.REFUND.code);
					commisionDetail.setCollectStatus(DssConstant.COLLECT_STATUS.NO.code);
					commisionDetail.setCommision(formatToPrice(subtract(BigDecimal.ZERO,divide(multiply(commisionDetail.getBizAmount(),commisionDetail.getCommisionRate()),100))).doubleValue());
					commisionDetail.setOperateType(DssConstant.ACCOUNT_TYPE.OUTCOMING.code);
				}
				//1.查找拒收
				//根据拒收查找退款比率
			});
			commisionDetailService.insertByRefund(commisionDetailList);
		}
		return Boolean.TRUE;
	}

	public List<Long> getCommsionRate(RefundInfo refundInfo){
		List<Long> orderItemIdList = new ArrayList<Long>();
		RejectInfo rejectInfo = rejectInfoService.queryRejectByRefundNo(refundInfo.getRefundCode());
		if(rejectInfo!=null){
			Map<String,Object> params = new HashMap<String,Object>();
			params.put("rejectCode", rejectInfo.getRejectCode());
			List<RejectItem> rejectItemList = rejectItemService.queryByParam(params);
			for(RejectItem rejectItem:rejectItemList){
				orderItemIdList.add(rejectItem.getOrderItemId());
			}
		}
		return orderItemIdList;
	}
}
