package com.tp.shop.ao.dss;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.vo.DAOConstant;
import com.tp.common.vo.PageInfo;
import com.tp.common.vo.DAOConstant.MYBATIS_SPECIAL_STRING;
import com.tp.dto.common.ResultInfo;
import com.tp.m.base.MResultVO;
import com.tp.m.enums.MResultInfo;
import com.tp.model.dss.WithdrawDetail;
import com.tp.model.dss.WithdrawDetailResponse;
import com.tp.model.dss.WithdrawLog;
import com.tp.proxy.dss.WithdrawDetailProxy;
import com.tp.proxy.dss.WithdrawLogProxy;
import com.tp.util.DateUtil;

@Service
public class DSSWithdrawAO {

	@Autowired
    private WithdrawDetailProxy withdrawDetailProxy;
	
	@Autowired
    private WithdrawLogProxy withdrawDetailLogProxy;
	
	public MResultVO<PageInfo<WithdrawDetailResponse>> queryWithdrawDetail(Long userId,Integer curPage){
		List<WithdrawDetailResponse> responseList = new ArrayList<WithdrawDetailResponse>();
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("withdrawor", userId);
		params.put(MYBATIS_SPECIAL_STRING.ORDER_BY.name(), " withdraw_time desc");
		ResultInfo<PageInfo<WithdrawDetail>> result = withdrawDetailProxy.queryPageByParam(params, new PageInfo<WithdrawDetail>(curPage,DAOConstant.DEFUALT_SIZE));
		if(result.success){
			for(WithdrawDetail withdrawDetail : result.data.getRows() ){
				WithdrawDetailResponse withdrawDetailResponse = new WithdrawDetailResponse();
				if(withdrawDetail.getWithdrawTime()!=null){
					withdrawDetailResponse.setWithdrawTime(DateUtil.formatDate(withdrawDetail.getWithdrawTime(),DateUtil.NEW_FORMAT));
				}
//				withdrawDetailResponse.setWithdrawStatus(withdrawDetail.getWithdrawStatus());
				withdrawDetailResponse.setWithdrawAmount(String.valueOf(withdrawDetail.getWithdrawAmount()));
				withdrawDetailResponse.setRemark(withdrawDetail.getRemark());
				ResultInfo<List<WithdrawLog>> withDrawLogs = new ResultInfo<List<WithdrawLog>>();
				switch (withdrawDetail.getWithdrawStatus()) {
				case 1:		//1:申请
					withdrawDetailResponse.setWithdrawStatus("申请中");
					break;
				case 2:		//2：审核中
					withdrawDetailResponse.setWithdrawStatus("审核中");
					break;
				case 3:		//3：审核通过
					withdrawDetailResponse.setWithdrawStatus("审核通过");
					break;
				case 4:		//4：审核未通过
					withdrawDetailResponse.setWithdrawStatus("审核未通过");
					params.clear();
					params.put("withdraw_detail_code", withdrawDetail.getWithdrawDetailCode());
					params.put(MYBATIS_SPECIAL_STRING.ORDER_BY.name(), " create_time desc");
					withDrawLogs = withdrawDetailLogProxy.queryByParam(params);
					withdrawDetailResponse.setRemark(withDrawLogs.getData().get(0).getRemark());
					break;
				case 5:		//5：财务打款成功
					withdrawDetailResponse.setWithdrawStatus("财务打款成功");
					break;
				case 6:		//6：财务打款失败
					withdrawDetailResponse.setWithdrawStatus("财务打款失败");
					params.clear();
					params.put("withdraw_detail_code", withdrawDetail.getWithdrawDetailCode());
					params.put(MYBATIS_SPECIAL_STRING.ORDER_BY.name(), " create_time desc");
					withDrawLogs = withdrawDetailLogProxy.queryByParam(params);
					withdrawDetailResponse.setRemark(withDrawLogs.getData().get(0).getRemark());
					break;
				}
				responseList.add(withdrawDetailResponse);
			}
			WithdrawDetailResponse r = new WithdrawDetailResponse();
			r.setWithdrawAmount("2000");
			r.setWithdrawStatus("通過");
			r.setWithdrawTime("22");
				responseList.add(r);
			PageInfo<WithdrawDetailResponse> pageResult = new PageInfo<WithdrawDetailResponse>();
			pageResult.setPage(result.data.getPage());
			pageResult.setRecords(result.data.getRecords());
			pageResult.setRows(responseList);
			pageResult.setSize(result.data.getSize());
//			pageResult.setStep(result.data.getSteps());
			pageResult.setTotal(result.data.getTotal());
			return new MResultVO<PageInfo<WithdrawDetailResponse>>(MResultInfo.SUCCESS,pageResult);
		}
		return new MResultVO(MResultInfo.FAILED,result.msg.getMessage());
	}
}
