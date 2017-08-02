package com.tp.proxy.dss;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.util.ExceptionUtils;
import com.tp.common.vo.DssConstant;
import com.tp.common.vo.Constant.SPLIT_SIGN;
import com.tp.common.vo.DAOConstant.MYBATIS_SPECIAL_STRING;
import com.tp.common.vo.DssConstant.WITHDRAW_STATUS;
import com.tp.dto.common.FailInfo;
import com.tp.dto.common.ResultInfo;
import com.tp.model.dss.PromoterInfo;
import com.tp.model.dss.WithdrawDetail;
import com.tp.proxy.BaseProxy;
import com.tp.service.IBaseService;
import com.tp.service.dss.IPromoterInfoService;
import com.tp.service.dss.IWithdrawDetailService;
import com.tp.util.StringUtil;
/**
 * 提现明细表代理层
 * @author szy
 *
 */
@Service
public class WithdrawDetailProxy extends BaseProxy<WithdrawDetail>{

	@Autowired
	private IWithdrawDetailService withdrawDetailService;
	@Autowired
	private IPromoterInfoService promoterInfoService;

	@Override
	public IBaseService<WithdrawDetail> getService() {
		return withdrawDetailService;
	}
	
	/**
	 * 申请提现
	 * @param withdrawor
	 * @param withdrawAmount
	 * @return
	 */
	public ResultInfo<WithdrawDetail> applyByPromoter(Long promoterId,Double withdrawAmount, Integer paymode){
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("promoterId", promoterId);
		PromoterInfo promoterInfo = promoterInfoService.queryById(promoterId);
		if(promoterInfo==null){
			return new ResultInfo<>(new FailInfo("您的信息不存在"));
		}
		if(!DssConstant.PROMOTER_STATUS.EN_PASS.code.equals(promoterInfo.getPromoterStatus())){
			return new ResultInfo<>(new FailInfo("您未开通提现功能"));
		}
		if(promoterInfo.getSurplusAmount()==null || promoterInfo.getSurplusAmount()<withdrawAmount){
			return new ResultInfo<>(new FailInfo("您提现金额大于账户余额，请修改后再操作"));
		}
				
		if( DssConstant.PAYMENT_MODE.POINT.code != paymode && StringUtil.isBlank( promoterInfo.getAlipay()) && 
				(StringUtil.isBlank( promoterInfo.getBankName() ) || StringUtil.isBlank( promoterInfo.getBankAccount() ) ) ){
			return new ResultInfo<>(new FailInfo("请输入支付宝信息"));			
		}
		
		if( DssConstant.PAYMENT_MODE.ALIPAY.code.equals(paymode) ){
			if( StringUtil.isBlank( promoterInfo.getAlipay()) ){
				paymode = DssConstant.PAYMENT_MODE.UNPAY.code;
			}
		}else if( DssConstant.PAYMENT_MODE.UNPAY.code.equals(paymode)){
			if(StringUtil.isBlank( promoterInfo.getBankName() ) || StringUtil.isBlank( promoterInfo.getBankAccount() ) ) {
				paymode = DssConstant.PAYMENT_MODE.ALIPAY.code;				
			}			
		}
			
		params.clear();
		params.put("withdrawor", promoterId);
		params.put(MYBATIS_SPECIAL_STRING.COLUMNS.name()," withdraw_status not in ("+StringUtil.join(WITHDRAW_STATUS.canApplyStatus(),SPLIT_SIGN.COMMA)+")");
		Integer count = withdrawDetailService.queryByParamCount(params);
		if(count>0){
			return new ResultInfo<>(new FailInfo("您有一笔提现正在处理中，请等待处理完成后再提现"));
		}
		return apply(promoterId,withdrawAmount, paymode);
	}
	
	
	public ResultInfo<WithdrawDetail> getDetailForWithdrawing(Long promoterId){		
		Map<String,Object> params = new HashMap<String,Object>();
		params.clear();
		params.put("withdrawor", promoterId);
		params.put(MYBATIS_SPECIAL_STRING.COLUMNS.name()," withdraw_status not in ("+StringUtil.join(WITHDRAW_STATUS.canApplyStatus(),SPLIT_SIGN.COMMA)+")");
		params.put(MYBATIS_SPECIAL_STRING.ORDER_BY.name()," withdraw_time desc");
		try {
			ResultInfo<List<WithdrawDetail>> reltList= queryByParamNotEmpty(params);
			if( !reltList.isSuccess()  || reltList.getData()==null || reltList.getData().isEmpty() ){
				return new ResultInfo<>(new FailInfo("提现详细不存在 推广员ID["+promoterId+"]"));				
			}
			return 	new ResultInfo<>( reltList.getData().get(0) );
		} catch (Exception exception) {
			FailInfo failInfo = ExceptionUtils.print(new FailInfo(exception), logger,promoterId);
			return new ResultInfo<>(failInfo);
		}
		
	}
	/**
	 * 申请提现
	 * @param withdrawor
	 * @param withdrawAmount
	 * @return
	 */
	public ResultInfo<WithdrawDetail> apply(Long withdrawor,Double withdrawAmount, Integer paymode){
		WithdrawDetail withdrawDetail = new WithdrawDetail();
		withdrawDetail.setWithdrawor(withdrawor);
		withdrawDetail.setWithdrawAmount(withdrawAmount);
		try{
			return new ResultInfo<>(withdrawDetailService.insert(withdrawDetail, paymode));
		}catch(Throwable exception){
			FailInfo failInfo = ExceptionUtils.print(new FailInfo(exception), logger,withdrawDetail);
			return new ResultInfo<>(failInfo);
		}
	}
	
	public ResultInfo<Integer> audit(WithdrawDetail withdrawDetail){
		try{
			return new ResultInfo<>(withdrawDetailService.updateByAudit(withdrawDetail));
		}catch(Throwable exception){
			FailInfo failInfo = ExceptionUtils.print(new FailInfo(exception), logger,withdrawDetail);
			return new ResultInfo<>(failInfo);
		}
	}
	
	public ResultInfo<Integer> batchAudit(WithdrawDetail withdrawDetail){
		List<Long> withdrawDetailIdList = withdrawDetail.getWithdrawDetailIdList();
		Integer withdrawStatus = withdrawDetail.getWithdrawStatus();
		if(CollectionUtils.isEmpty(withdrawDetailIdList)){
			return new ResultInfo<>(new FailInfo("没有选择提现信息"));
		}
		if(null==withdrawDetail.getWithdrawStatus()){
			return new ResultInfo<Integer>(new FailInfo("请选择审核状态"));
		}
		Map<String,Object> params = new HashMap<String,Object>();
		params.put(MYBATIS_SPECIAL_STRING.COLUMNS.name(), " withdraw_detail_id in ("+StringUtil.join(withdrawDetailIdList, SPLIT_SIGN.COMMA)+")");
		List<WithdrawDetail> withdrawDetailList = withdrawDetailService.queryByParam(params);
		if(CollectionUtils.isEmpty(withdrawDetailList)){
			return new ResultInfo<>(new FailInfo("选择的提现信息不存在"));
		}
		Integer passCount=0,payedCount=0,size=withdrawDetailList.size();
		for(WithdrawDetail withdraw:withdrawDetailList){
			if(DssConstant.WITHDRAW_STATUS.APPLY.code.equals(withdraw.getWithdrawStatus()) ||
			   DssConstant.WITHDRAW_STATUS.AUDITING.code.equals(withdraw.getWithdrawStatus())){
				passCount++;
			}
			if(DssConstant.WITHDRAW_STATUS.PASS.code.equals(withdraw.getWithdrawStatus())){
				payedCount++;
			}
		}
		if((DssConstant.WITHDRAW_STATUS.PASS.code.equals(withdrawStatus)
		  ||DssConstant.WITHDRAW_STATUS.UNPASS.code.equals(withdrawStatus))
		  && !passCount.equals(size)){
			return new ResultInfo<>(new FailInfo("有提现数据状态有变，请刷新列表后再选择审核数据"));
		}
		if((DssConstant.WITHDRAW_STATUS.PAYED.code.equals(withdrawStatus)
		  ||DssConstant.WITHDRAW_STATUS.UNPAY.code.equals(withdrawStatus))
		  && !payedCount.equals(size)){
			return new ResultInfo<>(new FailInfo("有提现数据状态有变，请刷新列表后再选择打款数据"));
		}
		
		if(DssConstant.WITHDRAW_STATUS.PAYED.code.equals(withdrawStatus)){
			if(StringUtils.isBlank(withdrawDetail.getPaymentor())){
				return new ResultInfo<Integer>(new FailInfo("请填写打款人"));
			}
			if(null==withdrawDetail.getPayedTime()){
				return new ResultInfo<Integer>(new FailInfo("请填写打款时间"));
			}
		}
		
		for(WithdrawDetail withdraw:withdrawDetailList){
			withdraw.setOldWithdrawStatus(withdraw.getWithdrawStatus());
			withdraw.setWithdrawStatus(withdrawStatus);
			withdraw.setPaymentor(withdrawDetail.getPaymentor());
			withdraw.setPayedTime(withdrawDetail.getPayedTime());
			withdraw.setRemark(withdrawDetail.getRemark());
			withdraw.setUpdateUser(withdrawDetail.getUpdateUser());
		}
		try{
			return new ResultInfo<>(withdrawDetailService.updateByBatchAudit(withdrawDetailList));
		}catch(Throwable exception){
			FailInfo failInfo = ExceptionUtils.print(new FailInfo(exception), logger,withdrawDetail);
			return new ResultInfo<>(failInfo);
		}
	}
	
	/**
	 * 查询是否提现中
	 */
	public ResultInfo<List<WithdrawDetail>> queryWithdrawStatus(Long promoterId){
		if (null == promoterId) {
			return new ResultInfo<>(new FailInfo("参数非法"));
		}
		Map<String, Object> params = new HashMap<>();
		params.put("withdrawor", promoterId);
		params.put(MYBATIS_SPECIAL_STRING.COLUMNS.name()," withdraw_status not in ("+StringUtil.join(WITHDRAW_STATUS.canApplyStatus(),SPLIT_SIGN.COMMA)+")");
		try {
			return queryByParam(params);
		} catch (Exception exception) {
			FailInfo failInfo = ExceptionUtils.print(new FailInfo(exception), logger,promoterId);
			return new ResultInfo<>(failInfo);
		}
	}
}
