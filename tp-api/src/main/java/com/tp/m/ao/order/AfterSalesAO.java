package com.tp.m.ao.order;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.vo.PageInfo;
import com.tp.dto.common.ResultInfo;
import com.tp.m.base.MResultVO;
import com.tp.m.base.Page;
import com.tp.m.constant.PageConstant;
import com.tp.m.convert.AfterSalesConvert;
import com.tp.m.enums.MResultInfo;
import com.tp.m.exception.MobileException;
import com.tp.m.query.order.QueryAfterSales;
import com.tp.m.util.StringUtil;
import com.tp.m.vo.order.AfterSalesVO;
import com.tp.model.mem.MemberInfo;
import com.tp.model.ord.RejectInfo;
import com.tp.model.ord.RejectItem;
import com.tp.proxy.ord.RejectInfoProxy;

/**
 * 售后业务层
 * @author zhuss
 * @2016年2月26日 下午2:13:30
 */
@Service
public class AfterSalesAO {
	private static Logger log = LoggerFactory.getLogger(AfterSalesAO.class);
	
	@Autowired
	private RejectInfoProxy rejectInfoProxy;
	
	/**
	 * 申请售后
	 * @return
	 */
	public MResultVO<MResultInfo> apply(QueryAfterSales afterSales) {
		try {
			MemberInfo user = new MemberInfo();
			RejectInfo rejectInfo = new RejectInfo();// 退货单
			RejectItem rejectItem = new RejectItem();// 退货商品
			AfterSalesConvert.convertApple(user, rejectInfo, rejectItem, afterSales);
			ResultInfo<Boolean> result = rejectInfoProxy.applyReturnGoods(user, rejectInfo, rejectItem);
			if(result.isSuccess())return new MResultVO<>(MResultInfo.SUCCESS);
			log.error("[调用Service接口 - 申请售后(applyReturnGoods) Failed] = {}",result.getMsg().toString());
			return new MResultVO<>(result.getMsg().getMessage());
		}catch(MobileException ex){
			log.error("[API接口 - 申请售后 MobileException] = {}",ex.getMessage());
			return new MResultVO<>(ex);
		}catch(Exception ex){
			log.error("[API接口 - 申请售后 Exception] = {}",ex);
			return new MResultVO<>(MResultInfo.SUBMIT_FAILED);
		}
	}
	
	/**
	 * 售后列表及详情
	 * @return
	 */
	public MResultVO<Page<AfterSalesVO>> list2Detail(QueryAfterSales afterSales) {
		try {
			ResultInfo<PageInfo<RejectInfo>> result = rejectInfoProxy.queryMobilePageListByRejectQuery(afterSales.getUserid(), StringUtil.getCurpageDefault(afterSales.getCurpage()), PageConstant.DEFAULT_PAGESIZE,null,null);
			if(result.isSuccess())return new MResultVO<>(MResultInfo.SUCCESS,AfterSalesConvert.convertList2Detail(result.getData()));
			log.error("[调用Service接口 - 售后列表及详情(queryMobilePageListByRejectQuery) Failed] = {}",result.getMsg().toString());
			return new MResultVO<>(result.getMsg().getMessage());
		}catch(MobileException ex){
			log.error("[API接口 - 售后列表及详情 MobileException] = {}",ex.getMessage());
			return new MResultVO<>(ex);
		}catch(Exception ex){
			log.error("[API接口 - 售后列表及详情 Exception] = {}",ex);
			return new MResultVO<>(MResultInfo.SUBMIT_FAILED);
		}
	}
	
	/**
	 * 修改售后单信息
	 * @return
	 */
	public MResultVO<MResultInfo> update(QueryAfterSales afterSales) {
		try {
			MemberInfo user = new MemberInfo();
			RejectInfo rejectInfo = new RejectInfo();// 退货单
			RejectItem rejectItem = new RejectItem();// 退货商品
			AfterSalesConvert.convertApple(user, rejectInfo, rejectItem, afterSales);
			ResultInfo<Boolean> result = rejectInfoProxy.updateRejectData(user, rejectInfo, rejectItem);
			if(result.isSuccess())return new MResultVO<>(MResultInfo.SUCCESS);
			log.error("[调用Service接口 - 修改售后单(applyReturnGoods) Failed] = {}",result.getMsg().toString());
			return new MResultVO<>(result.getMsg().getMessage());
		}catch(MobileException ex){
			log.error("[API接口 - 修改售后单 MobileException] = {}",ex.getMessage());
			return new MResultVO<>(ex);
		}catch(Exception ex){
			log.error("[API接口 - 修改售后单 Exception] = {}",ex);
			return new MResultVO<>(MResultInfo.SUBMIT_FAILED);
		}
	}
	
	/**
	 * 提交物流单号
	 * @return
	 */
	public MResultVO<MResultInfo> submitLogistic(QueryAfterSales afterSales) {
		try {
			RejectInfo rejectInfo = new RejectInfo();
			rejectInfo.setRejectId(Long.valueOf(afterSales.getAsid()));
			rejectInfo.setPackageNo(afterSales.getLogisticcode());
			rejectInfo.setCompanyName(afterSales.getCompany());
			rejectInfo.setCompanyCode(afterSales.getCompanycode());
			ResultInfo<Boolean> result = rejectInfoProxy.saveExpressNoForMemberId(rejectInfo, afterSales.getUserid());
			if(result.isSuccess()){
				if(result.getData())return new MResultVO<>(MResultInfo.SUCCESS);
			}
			log.error("[调用Service接口 - 提交物流单号(saveExpressNoForMemberId) Failed] = {}",result.getMsg().toString());
			return new MResultVO<>(result.getMsg().getMessage());
		}catch(MobileException ex){
			log.error("[API接口 - 提交物流单号 MobileException] = {}",ex.getMessage());
			return new MResultVO<>(ex);
		}catch(Exception ex){
			log.error("[API接口 - 提交物流单号 Exception] = {}",ex);
			return new MResultVO<>(MResultInfo.SUBMIT_FAILED);
		}
	}
	
	/**
	 * 取消售后
	 * @return
	 */
	public MResultVO<MResultInfo> cancel(QueryAfterSales afterSales) {
		try {
			 MemberInfo usr = new  MemberInfo();
			 usr.setId(afterSales.getUserid());
			ResultInfo<Boolean> result = rejectInfoProxy.cancelReject(Long.parseLong(afterSales.getAsid()), usr);
			if(result.isSuccess()){
				if(result.getData())return new MResultVO<>(MResultInfo.SUCCESS);
			}
			log.error("[调用Service接口 - 取消售后(cancelReject) Failed] = {}",result.getMsg().toString());
			return new MResultVO<>(result.getMsg().getMessage());
		}catch(MobileException ex){
			log.error("[API接口 - 取消售后 MobileException] = {}",ex.getMessage());
			return new MResultVO<>(ex);
		}catch(Exception ex){
			log.error("[API接口 - 取消售后 Exception] = {}",ex);
			return new MResultVO<>(MResultInfo.SUBMIT_FAILED);
		}
	}
}
