package com.tp.proxy.ord;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.vo.Constant;
import com.tp.common.vo.DAOConstant;
import com.tp.common.vo.PageInfo;
import com.tp.common.vo.DAOConstant.MYBATIS_SPECIAL_STRING;
import com.tp.common.vo.ord.OffsetConstant;
import com.tp.dto.common.FailInfo;
import com.tp.dto.common.ResultInfo;
import com.tp.model.ord.OffsetInfo;
import com.tp.model.ord.OffsetLog;
import com.tp.model.usr.UserInfo;
import com.tp.proxy.BaseProxy;
import com.tp.proxy.usr.UserHandler;
import com.tp.query.ord.OffsetQuery;
import com.tp.service.IBaseService;
import com.tp.service.ord.IOffsetInfoService;
import com.tp.service.ord.IOffsetLogService;
import com.tp.util.BeanUtil;
import com.tp.util.DateUtil;
/**
 * 退款单代理层
 * @author szy
 *
 */
@Service
public class OffsetInfoProxy extends BaseProxy<OffsetInfo>{

	@Autowired
	private IOffsetInfoService offsetInfoService;
	@Autowired
	private IOffsetLogService offsetLogService;

	@Override
	public IBaseService<OffsetInfo> getService() {
		return offsetInfoService;
	}

	public PageInfo<OffsetInfo> queryByOffsetQuery(OffsetQuery offsetQuery) {
		Map<String,Object> params = BeanUtil.beanMap(offsetQuery);
		List<DAOConstant.WHERE_ENTRY> whereList = new ArrayList<DAOConstant.WHERE_ENTRY>();
		if(null!=offsetQuery.getOffsetAmountStart()){
			whereList.add(new DAOConstant.WHERE_ENTRY("offset_amount", MYBATIS_SPECIAL_STRING.GT, offsetQuery.getOffsetAmountStart()));
		}
		params.remove("offsetAmountStart");
		if(null!=offsetQuery.getOffsetAmountEnd()){
			whereList.add(new DAOConstant.WHERE_ENTRY("offset_amount", MYBATIS_SPECIAL_STRING.LT, offsetQuery.getOffsetAmountEnd()));
		}
		params.remove("offsetAmountEnd");
		if(null!=offsetQuery.getCreateDateBegin()){
			whereList.add(new DAOConstant.WHERE_ENTRY("create_time", MYBATIS_SPECIAL_STRING.GT, offsetQuery.getCreateDateBegin()));
		}
		params.remove("createDateBegin");
		if(null!=offsetQuery.getCreateDateEnd()){
			whereList.add(new DAOConstant.WHERE_ENTRY("create_time", MYBATIS_SPECIAL_STRING.LT, offsetQuery.getCreateDateEnd()));
		}
		params.remove("createDateEnd");
		params.put(MYBATIS_SPECIAL_STRING.WHERE.name(), whereList);
		PageInfo<OffsetInfo> pageInfo = new PageInfo<OffsetInfo>(offsetQuery.getStartPage(),offsetQuery.getPageSize());
		pageInfo = offsetInfoService.queryPageByParam(params, pageInfo);
		return pageInfo;
	}

	public ResultInfo<OffsetInfo> insertOffsetInfo(OffsetInfo offsetInfo) {
		return insert(offsetInfo);
	}

	public ResultInfo<?> audit(OffsetInfo offsetInfo, Boolean success,
			String auditType) {
		OffsetInfo oldOffsetInfo = offsetInfoService.queryById(offsetInfo.getOffsetId());
		oldOffsetInfo.setSerialNo(offsetInfo.getSerialNo());
		ResultInfo<?> rd = auditValidation(oldOffsetInfo, auditType, success);
		//if(rd != null)
		if(rd == null)
			return rd;
		offsetInfo.setOffsetId(oldOffsetInfo.getOffsetId());
		offsetInfo.setUpdateUser(oldOffsetInfo.getUpdateUser());
		offsetInfo.setUpdateTime(new Date());
		offsetInfo.setRemarks(oldOffsetInfo.getRemarks());
		offsetInfo.setSerialNo(oldOffsetInfo.getSerialNo());
		offsetInfo.setCouponCode(oldOffsetInfo.getCouponCode());
		if(success){
			if("audit".equals(auditType))
				offsetInfo.setOffsetStatus(OffsetConstant.OFFSET_STATUS.AUDIT.code);
			else
				offsetInfo.setOffsetStatus(OffsetConstant.OFFSET_STATUS.AUDITED.code);
		}
		else{
			if("audit".equals(auditType))
				offsetInfo.setOffsetStatus(OffsetConstant.OFFSET_STATUS.FAIL.code);
			else
				offsetInfo.setOffsetStatus(OffsetConstant.OFFSET_STATUS.FINAL_FAIL.code);
		}
		offsetInfoService.updateNotNullById(offsetInfo);
		
		OffsetLog log = new OffsetLog();
		log.setActionType(OffsetConstant.OFFSET_ACTION_TYPE.AUDIT.code);
		log.setOldOffsetStatus(oldOffsetInfo.getOffsetStatus());
		if(success){
			if("audit".equals(auditType))
				log.setCurrentOffsetStatus(OffsetConstant.OFFSET_STATUS.AUDIT.code);				
			else	
				log.setCurrentOffsetStatus(OffsetConstant.OFFSET_STATUS.AUDITED.code);			
		}			
		else{
			if("audit".equals(auditType))
				log.setCurrentOffsetStatus(OffsetConstant.OFFSET_STATUS.FAIL.code);			
			else 
				log.setCurrentOffsetStatus(OffsetConstant.OFFSET_STATUS.FINAL_FAIL.code);			
		}
			
		log.setOffsetId(offsetInfo.getOffsetId());
		log.setOffsetCode(oldOffsetInfo.getOffsetCode());	// by zhs 0225 增加offsetcode
		log.setOperatorName(oldOffsetInfo.getUpdateUser());
		log.setOperatorType(Constant.LOG_AUTHOR_TYPE.USER_CALL.code);
		log.setOrderCode(oldOffsetInfo.getOrderCode());
		log.setLogContent(oldOffsetInfo.getRemarks() == null?"": oldOffsetInfo.getRemarks());
		log.setCreateTime(new Date());		
		UserInfo user = UserHandler.getUser();
		log.setCreateUser( user!=null ? user.getUserName() : null );
		offsetLogService.insert(log);
		
		return new ResultInfo<>(true);
	}

	public List<OffsetLog> queryOffsetLogListById(Long offsetId) {
		Map<String,Object> params =  new HashMap<String,Object>();
		params.put("offsetId", offsetId);
		return offsetLogService.queryByParam(params);
	}

	public void importOffsetApply(FileInputStream fileInputStream) {
		// TODO Auto-generated method stub
		
	}

	public OffsetInfo queryByCode(Long offsetNo) {
		Map<String,Object> params =  new HashMap<String,Object>();
		params.put("offsetCode", offsetNo);
		return offsetInfoService.queryUniqueByParams(params);
	}
	private ResultInfo<?> auditValidation(OffsetInfo offsetInfo, String auditType, Boolean success){
		if(OffsetConstant.OFFSET_STATUS.AUDITED.code.intValue() == offsetInfo.getOffsetStatus().intValue()
				|| OffsetConstant.OFFSET_STATUS.FINAL_FAIL.code.intValue() == offsetInfo.getOffsetStatus().intValue()){
			return new ResultInfo<>(new FailInfo("不能操作,补偿单已" + OffsetConstant.OFFSET_STATUS.getCnName(offsetInfo.getOffsetStatus())));
		}
		if("audit".equals(auditType)){
			if(OffsetConstant.OFFSET_STATUS.AUDIT.code.intValue() == offsetInfo.getOffsetStatus().intValue()
				|| OffsetConstant.OFFSET_STATUS.FAIL.code.intValue() == offsetInfo.getOffsetStatus().intValue()){
				return new ResultInfo<>(new FailInfo("不能操作,补偿单已" + OffsetConstant.OFFSET_STATUS.getCnName(offsetInfo.getOffsetStatus())));
			}
		}
		else{
			if(OffsetConstant.OFFSET_STATUS.APPLY.code.intValue() == offsetInfo.getOffsetStatus().intValue()){
				return new ResultInfo<>(new FailInfo("请等待客服领导审核"));
			}
			else if(OffsetConstant.OFFSET_STATUS.FAIL.code.intValue() == offsetInfo.getOffsetStatus().intValue()){
				return new ResultInfo<>(new FailInfo("客服审核未通过不能继续审核"));
			}
			else if(success && StringUtils.isEmpty(offsetInfo.getSerialNo())){
				return new ResultInfo<>(new FailInfo("交易流水号不能为空"));
			}
		}
		return new ResultInfo<Boolean>(Boolean.TRUE);
	}
}
