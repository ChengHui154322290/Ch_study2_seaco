package com.tp.proxy.dss;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.util.ExceptionUtils;
import com.tp.common.vo.PageInfo;
import com.tp.common.vo.Constant.SPLIT_SIGN;
import com.tp.common.vo.DAOConstant.MYBATIS_SPECIAL_STRING;
import com.tp.dto.common.FailInfo;
import com.tp.dto.common.ResultInfo;
import com.tp.model.dss.PromoterInfo;
import com.tp.model.dss.RefereeDetail;
import com.tp.proxy.BaseProxy;
import com.tp.service.IBaseService;
import com.tp.service.dss.IPromoterInfoService;
import com.tp.service.dss.IRefereeDetailService;
import com.tp.util.StringUtil;
/**
 * 推荐新人佣金明细表代理层
 * @author szy
 *
 */
@Service
public class RefereeDetailProxy extends BaseProxy<RefereeDetail>{

	@Autowired
	private IRefereeDetailService refereeDetailService;
	@Autowired
	private IPromoterInfoService promoterInfoService;

	@Override
	public IBaseService<RefereeDetail> getService() {
		return refereeDetailService;
	}
	
	@Override
	public ResultInfo<PageInfo<RefereeDetail>> queryPageByParamNotEmpty(Map<String, Object> params,
			PageInfo<RefereeDetail> info) {
		try{
			PageInfo<RefereeDetail> pageInfoResult = refereeDetailService.queryPageByParamNotEmpty(params,info);
			initName(pageInfoResult.getRows());
			return new ResultInfo<>(pageInfoResult);
		}catch(Throwable exception){
			FailInfo failInfo = ExceptionUtils.print(new FailInfo(exception), logger,params,info);
			return new ResultInfo<>(failInfo);
		}
	}
	
	private List<RefereeDetail> initName(List<RefereeDetail> refereeDetailList){
		if(CollectionUtils.isNotEmpty(refereeDetailList)){
			List<Long> promoterIdList = new ArrayList<Long>();
			refereeDetailList.forEach(new Consumer<RefereeDetail>(){
				@Override
				public void accept(RefereeDetail refereeDetail) {
					promoterIdList.add(refereeDetail.getPromoterId());
					promoterIdList.add(refereeDetail.getMemberId());
				}
			});
			Map<String,Object> params = new HashMap<String,Object>();
			params.put(MYBATIS_SPECIAL_STRING.COLUMNS.name(), " promoter_id in ("+StringUtils.join(promoterIdList, SPLIT_SIGN.COMMA)+")");
			List<PromoterInfo> promoterInfoList = promoterInfoService.queryByParam(params);
			refereeDetailList.forEach(new Consumer<RefereeDetail>(){
				public void accept(RefereeDetail refereeDetail) {
					promoterInfoList.forEach(new Consumer<PromoterInfo>(){
						public void accept(PromoterInfo promoterInfo) {
							if(promoterInfo.getPromoterId().equals(refereeDetail.getPromoterId())){
								if( StringUtil.isBlank( promoterInfo.getPromoterName() )  ){
									refereeDetail.setPromoterName(promoterInfo.getNickName());
								}else{
									refereeDetail.setPromoterName(promoterInfo.getPromoterName());									
								}
							}
							if(promoterInfo.getPromoterId().equals(refereeDetail.getMemberId())){
								if( StringUtil.isBlank( promoterInfo.getPromoterName())){
									refereeDetail.setMemberAccount(promoterInfo.getNickName());									
								}else{									
									refereeDetail.setMemberAccount(promoterInfo.getPromoterName());
								}
							}
						}
					});
				}
			});
		}
		return refereeDetailList;
	}
	
	
	public List<RefereeDetail> queryRefereeByDetailCode( List<Long> detaiCodeList){
		return refereeDetailService.queryRefereeByDetailCode(detaiCodeList);
	}

}
