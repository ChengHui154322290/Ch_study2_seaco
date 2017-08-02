package com.tp.proxy.dss;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.util.ExceptionUtils;
import com.tp.common.vo.PageInfo;
import com.tp.common.vo.Constant.SPLIT_SIGN;
import com.tp.common.vo.DAOConstant.MYBATIS_SPECIAL_STRING;
import com.tp.dto.common.FailInfo;
import com.tp.dto.common.ResultInfo;
import com.tp.model.dss.CommisionDetail;
import com.tp.model.mem.MemberInfo;
import com.tp.proxy.BaseProxy;
import com.tp.service.IBaseService;
import com.tp.service.dss.ICommisionDetailService;
import com.tp.service.mem.IMemberInfoService;
import com.tp.util.StringUtil;
/**
 * 佣金明细代理层
 * @author szy
 *
 */
@Service
public class CommisionDetailProxy extends BaseProxy<CommisionDetail>{

	@Autowired
	private ICommisionDetailService commisionDetailService;
	@Autowired
	private IMemberInfoService memberInfoService;

	@Override
	public IBaseService<CommisionDetail> getService() {
		return commisionDetailService;
	}
	
	/**
	 * 把到期的未汇总的佣金进行汇总
	 * @param commisionDetailList
	 * @return
	 */
	public ResultInfo<Boolean> updateByCollectCommision(List<CommisionDetail> commisionDetailList){
		try{
			commisionDetailService.updateByCollectCommision(commisionDetailList);
			return new ResultInfo<>(Boolean.TRUE);
		}catch(Throwable exception){
			FailInfo failInfo = ExceptionUtils.print(new FailInfo(exception), logger,commisionDetailList);
			return new ResultInfo<>(failInfo);
		}
	}
	
	@Override
	public ResultInfo<PageInfo<CommisionDetail>> queryPageByParamNotEmpty(Map<String, Object> params,
			PageInfo<CommisionDetail> info) {
		try{
			PageInfo<CommisionDetail> pageInfo = commisionDetailService.queryPageByParamNotEmpty(params,info);
			if(CollectionUtils.isNotEmpty(pageInfo.getRows())){
				List<Long> memberIdList = new ArrayList<Long>();
				for(CommisionDetail detail:pageInfo.getRows()){
					memberIdList.add(detail.getMemberId());
				}
				params.clear();
				params.put(MYBATIS_SPECIAL_STRING.COLUMNS.name(), " id in ("+StringUtil.join(memberIdList,SPLIT_SIGN.COMMA)+")");	
				List<MemberInfo> memberInfoList = memberInfoService.queryByParam(params);
				for(CommisionDetail detail:pageInfo.getRows()){
					for(MemberInfo memberInfo:memberInfoList){
						if(detail.getMemberId().equals(memberInfo.getId())){
							detail.setMemberAccount(memberInfo.getNickName());
						}
					}
				}
			}
			return new ResultInfo<>(pageInfo);
		}catch(Throwable exception){
			FailInfo failInfo = ExceptionUtils.print(new FailInfo(exception), logger,params,info);
			return new ResultInfo<>(failInfo);
		}
	}
}
