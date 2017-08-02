package com.tp.proxy.ord;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.vo.Constant;
import com.tp.common.vo.DAOConstant.MYBATIS_SPECIAL_STRING;
import com.tp.model.ord.CancelInfo;
import com.tp.model.ord.CancelItem;
import com.tp.model.ord.RejectInfo;
import com.tp.proxy.BaseProxy;
import com.tp.service.IBaseService;
import com.tp.service.ord.ICancelInfoService;
import com.tp.service.ord.ICancelItemService;
import com.tp.util.StringUtil;
/**
 * 取消单代理层
 * @author szy
 *
 */
@Service
public class CancelInfoProxy extends BaseProxy<CancelInfo>{

	@Autowired
	private ICancelInfoService cancelInfoService;
	@Autowired
	private ICancelItemService cancelItemService;

	@Override
	public IBaseService<CancelInfo> getService() {
		return cancelInfoService;
	}

	public List<CancelItem> queryCancelItemList(Long orderNo) {
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("orderCode", orderNo);
		List<CancelInfo> cancelInfoList = cancelInfoService.queryByParam(params);
		if(CollectionUtils.isNotEmpty(cancelInfoList)){
			List<Long> cancelNoList = new ArrayList<Long>();
			for(CancelInfo cancelInfo:cancelInfoList){
				cancelNoList.add(cancelInfo.getCancelCode());
			}
			params.put(MYBATIS_SPECIAL_STRING.COLUMNS.name(), " cancel_code in ("+StringUtil.join(cancelNoList, Constant.SPLIT_SIGN.COMMA)+")");
			return cancelItemService.queryByParam(params);
		}
		return null;
	}

	public List<CancelItem> queryRejectItemListByRefundNoList(
			List<Long> cancelRefundNoList) {
		Map<String,Object> params = new HashMap<String,Object>();
		params.put(MYBATIS_SPECIAL_STRING.COLUMNS.name(), " refund_code in ("+StringUtil.join(cancelRefundNoList, Constant.SPLIT_SIGN.COMMA)+")");
		List<CancelInfo> cancelInfoList = cancelInfoService.queryByParam(params);
		if(CollectionUtils.isNotEmpty(cancelInfoList)){
			List<Long> cancelNoList = new ArrayList<Long>();
			for(CancelInfo cancelInfo:cancelInfoList){
				cancelNoList.add(cancelInfo.getCancelCode());
			}
			params.put(MYBATIS_SPECIAL_STRING.COLUMNS.name(), " cancel_code in ("+StringUtil.join(cancelNoList, Constant.SPLIT_SIGN.COMMA)+")");
			return cancelItemService.queryByParam(params);
		}
		return null;
	}
}
