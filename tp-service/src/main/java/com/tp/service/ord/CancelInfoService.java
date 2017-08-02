package com.tp.service.ord;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.dao.BaseDao;
import com.tp.common.vo.Constant;
import com.tp.common.vo.OrderConstant;
import com.tp.common.vo.DAOConstant.MYBATIS_SPECIAL_STRING;
import com.tp.common.vo.ord.CancelConstant;
import com.tp.dao.ord.CancelInfoDao;
import com.tp.dao.ord.CancelItemDao;
import com.tp.dao.ord.CancelLogDao;
import com.tp.model.ord.CancelInfo;
import com.tp.model.ord.CancelItem;
import com.tp.model.ord.CancelLog;
import com.tp.model.ord.OrderItem;
import com.tp.model.ord.OrderRedeemItem;
import com.tp.model.ord.SubOrder;
import com.tp.service.BaseService;
import com.tp.service.ord.ICancelInfoService;
import com.tp.service.ord.IOrderRedeemItemService;
import com.tp.util.CodeCreateUtil;

@Service
public class CancelInfoService extends BaseService<CancelInfo> implements ICancelInfoService {

	@Autowired
	private CancelInfoDao cancelInfoDao;
	@Autowired
	private CancelItemDao cancelItemDao;
	@Autowired
	private CancelLogDao cancelLogDao;
	
	@Autowired
	private IOrderRedeemItemService orderRedeemItemService;
	@Override
	public BaseDao<CancelInfo> getDao() {
		return cancelInfoDao;
	}
	@Override
	public CancelInfo insert(CancelInfo cancelInfo){
		cancelInfoDao.insert(cancelInfo);
		Long key =  cancelInfo.getCancelId();
		List<CancelItem> cancelItemList = cancelInfo.getCancelItemList();
		for(CancelItem cancelItem:cancelItemList){
			cancelItem.setCancelId(key);
			cancelItemDao.insert(cancelItem);
		}
		CancelLog cancelLog = new CancelLog();
		cancelLog.setActionType(CancelConstant.LOG_ACTION_TYPE.APPLY.code);
		cancelLog.setCreateUser(cancelInfo.getCreateUser());
		cancelLog.setUpdateUser(cancelInfo.getCreateUser());
		cancelLog.setCancelCode(cancelInfo.getCancelCode());
		cancelLog.setCancelId(key);
		cancelLog.setOrderCode(cancelInfo.getOrderCode());
		cancelLog.setCurrentCancelStatus(cancelInfo.getStart());
		cancelLog.setLogContent("用户申请取消退款");
		cancelLog.setOperatorName(cancelInfo.getCreateUser());
		cancelLog.setOperatorType(Constant.LOG_AUTHOR_TYPE.MEMBER.code);
		cancelLog.setOperatorUserId(cancelInfo.getCreateUser());
		cancelLogDao.insert(cancelLog);
		return cancelInfo;
	}

	@Override
	public List<CancelInfo> queryCancelItemListByRefundNoList(List<Long> refundCodeList) {
		Map<String,Object> params = new HashMap<String,Object>();
		Map<String,Object> params1 = new HashMap<String,Object>();
		params1.put("refundNo", refundCodeList);
		params.put(MYBATIS_SPECIAL_STRING.INLIST.name(), params1);
		return cancelInfoDao.queryByParam(params);
	}
	
	@Override
	public CancelInfo addCancelInfo(SubOrder subOrder, List<OrderItem> orderItemList, Long refundNo) {
		CancelInfo cancelInfo = new CancelInfo();
		cancelInfo.setCancelCode(CodeCreateUtil.initCancelCode());
		cancelInfo.setCancelStatus(CancelConstant.CANCEL_STATUS.APPLY.code);
		cancelInfo.setCreateUser(subOrder.getMemberId().toString());
		cancelInfo.setCreateTime(new Date());
		cancelInfo.setUpdateTime(new Date());
		cancelInfo.setUpdateUser(subOrder.getMemberId().toString());
		cancelInfo.setOrderCode(subOrder.getOrderCode());
		cancelInfo.setRefundCode(refundNo);
		if(OrderConstant.OrderType.BUY_COUPONS.code.equals(subOrder.getType())){//退还未使用的
			cancelInfo.setRefundAmount(orderRedeemItemService.getUnUseAmountByOrderCode(subOrder.getOrderCode()));
		}else{
			cancelInfo.setRefundAmount(subOrder.getTotal());
		}
		
		List<CancelItem> cancelItemList = cancelInfo.getCancelItemList();
		for(OrderItem orderItem:orderItemList){
			CancelItem cancelItem = new CancelItem();
			cancelItem.setCreateUser(subOrder.getMemberId().toString());
			cancelItem.setItemName(orderItem.getSpuName());
			cancelItem.setCancelCode(cancelInfo.getCancelCode());
			cancelItem.setItemQuantity(orderItem.getQuantity());
			cancelItem.setItemUnitPrice(orderItem.getPrice());
			cancelItem.setItemRefundQuantity(orderItem.getQuantity());
			cancelItem.setItemRefundAmount(orderItem.getSubTotal());
			cancelItem.setItemSkuCode(orderItem.getSkuCode());
			cancelItem.setOrderItemId(orderItem.getId());
			cancelItem.setOrderCode(subOrder.getOrderCode());
			cancelItem.setCreateTime(new Date());
			cancelItem.setUpdateTime(cancelItem.getCreateTime());
			cancelItem.setUpdateUser(cancelItem.getCreateUser());
			cancelItemList.add(cancelItem);
		}
		insert(cancelInfo);
		return cancelInfo;
	}
}
