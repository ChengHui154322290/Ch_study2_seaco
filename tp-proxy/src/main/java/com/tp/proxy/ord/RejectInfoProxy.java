package com.tp.proxy.ord;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.util.mem.SmsException;
import com.tp.common.vo.Constant;
import com.tp.common.vo.DAOConstant;
import com.tp.common.vo.PageInfo;
import com.tp.common.vo.DAOConstant.MYBATIS_SPECIAL_STRING;
import com.tp.common.vo.ord.RejectConstant;
import com.tp.dto.common.FailInfo;
import com.tp.dto.common.ResultInfo;
import com.tp.dto.ord.remote.RejectDetailDTO;
import com.tp.exception.OrderServiceException;
import com.tp.exception.ServiceException;
import com.tp.model.mem.MemberInfo;
import com.tp.model.ord.OrderItem;
import com.tp.model.ord.RejectInfo;
import com.tp.model.ord.RejectItem;
import com.tp.model.ord.RejectLog;
import com.tp.proxy.BaseProxy;
import com.tp.query.ord.RejectAudit;
import com.tp.query.ord.RejectQuery;
import com.tp.result.ord.RejectAuditDTO;
import com.tp.result.ord.SubOrderExpressInfoDTO;
import com.tp.service.IBaseService;
import com.tp.service.mem.ISendSmsService;
import com.tp.service.mmp.IPointDetailService;
import com.tp.service.ord.IOrderItemService;
import com.tp.service.ord.IRejectInfoService;
import com.tp.service.ord.IRejectItemService;
import com.tp.service.ord.IRejectLogService;
import com.tp.service.ord.ISubOrderService;
import com.tp.service.ord.remote.IOrderRejectRemoteService;
import com.tp.util.BeanUtil;
import com.tp.util.StringUtil;
/**
 * 退货单代理层
 * @author szy
 *
 */
@Service
public class RejectInfoProxy extends BaseProxy<RejectInfo>{

	@Autowired
	private IRejectInfoService rejectInfoService;
	@Autowired
	private IRejectItemService rejectItemService;
	@Autowired
	private IRejectLogService rejectLogService;
	@Autowired
	private IOrderItemService orderItemService;
	
	@Autowired
	private ISubOrderService subOrderService;
	@Autowired
	private ISendSmsService sendSmsService;

	// by zhs 0223
	@Autowired
	private IOrderRejectRemoteService orderRejectRemoteService;
	@Autowired
	private IPointDetailService pointDetailService;		
	@Override
	public IBaseService<RejectInfo> getService() {
		return rejectInfoService;
	}

	
	
	/**
	 * 提交售后  by zhs 0224
	 * @ user
	 * @ rejectInfo
	 * @ rejectItem
	 */
	
	public ResultInfo<Boolean>  applyReturnGoods(MemberInfo user, RejectInfo rejectInfo,	RejectItem rejectItem) throws Exception {		
		try{
			orderRejectRemoteService. applyReturnGoods(user, rejectInfo,	rejectItem);
			return new ResultInfo<>(Boolean.TRUE);
		}catch(OrderServiceException ord_ex){
			return new ResultInfo<>(new FailInfo(ord_ex.getMessage()));			
		}catch(Throwable exception){
			return new ResultInfo<>(new FailInfo("提交售后失败"));
		}
	}
	
	/**
	 * 提交运单号 by zhs 0224
	 * <pre>
	 * 
	 * </pre>
	 *
	 * @param info
	 * @param user
	 * @throws Exception
	 */
	public ResultInfo<Boolean> saveExpressNoForMemberId(RejectInfo rejectInfo,Long userId) {
		try{
			orderRejectRemoteService.saveExpressNoForMemberId(rejectInfo,userId);
			return new ResultInfo<>(Boolean.TRUE);
		}catch(OrderServiceException ord_ex){
			return new ResultInfo<>(new FailInfo(ord_ex.getMessage()));						
		}catch(Throwable exception){
			return new ResultInfo<>(new FailInfo("提交运单号 失败"));
		}		
	}
	
	
	/**
	 * 
	 * <pre>
	 * 查询售后历史列表by zhs 0224
	 * </pre>
	 *
	 * @param item
	 * @return
	 */
	public ResultInfo<RejectDetailDTO> showRejectHistoryForMemberId(RejectItem item,Long userId){
		try{
			return new ResultInfo<RejectDetailDTO>(orderRejectRemoteService.showRejectHistoryForMemberId(item,userId) );
		}catch(OrderServiceException ord_ex){
			return new ResultInfo<>(new FailInfo(ord_ex.getMessage()));						
		}catch(Throwable exception){
			return new ResultInfo<>(new FailInfo("查询售后历史列表 失败"));
		}		
	}

	/**
	 * 
	 * <pre>
	 *  用户-售后-列表以及详情  by zhs 0224
	 * </pre>
	 *
	 * @param userId
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	public ResultInfo<PageInfo<RejectInfo>> queryMobilePageListByRejectQuery(Long userId,int pageNo,int pageSize,Integer orderType,String channelCode){
		RejectQuery query = new RejectQuery();
		query.setUserId(userId);
		query.setStartPage(pageNo);
		query.setPageSize(pageSize);
		if(orderType!=null){
			List<Integer> orderTypeList = new ArrayList<Integer>();
			orderTypeList.add(orderType);
			query.setOrderTypeList(orderTypeList);
		}
		query.setChannelCode(channelCode);
		try{
			return new ResultInfo<PageInfo<RejectInfo>>(orderRejectRemoteService.queryMobilePageListByRejectQuery(query) );			
		}catch(OrderServiceException ord_ex){
			return new ResultInfo<>(new FailInfo(ord_ex.getMessage()));						
		}catch(Throwable exception){
			return new ResultInfo<>(new FailInfo("用户-售后-列表以及详情 失败"));
		}		
	}
	
	
	/**
	 * 获取退货信息
	 * @param rejectQuery
	 * @return
	 */
	public PageInfo<RejectInfo> getRejectInfoList(RejectQuery rejectQuery) {
		Map<String,Object> params = BeanUtil.beanMap(rejectQuery);
		params.remove("orderByParams");
		params.remove("orderBy");
		params.put(MYBATIS_SPECIAL_STRING.ORDER_BY.name(), rejectQuery.getOrderByParams()+" "+rejectQuery.getOrderBy());
		List<DAOConstant.WHERE_ENTRY> wehreList = new ArrayList<DAOConstant.WHERE_ENTRY>();
		if(CollectionUtils.isNotEmpty(rejectQuery.getOffsetCodeList())){
			wehreList.add(new DAOConstant.WHERE_ENTRY("offset_code", MYBATIS_SPECIAL_STRING.INLIST, rejectQuery.getOffsetCodeList()));
		}
		params.remove("offsetCodeList");
		if(CollectionUtils.isNotEmpty(rejectQuery.getRefundCodeList())){
			wehreList.add(new DAOConstant.WHERE_ENTRY("refund_code", MYBATIS_SPECIAL_STRING.INLIST, rejectQuery.getRefundCodeList()));
		}
		params.remove("refundCodeList");
		if(CollectionUtils.isNotEmpty(rejectQuery.getRejectCodeList())){
			wehreList.add(new DAOConstant.WHERE_ENTRY("reject_code", MYBATIS_SPECIAL_STRING.INLIST, rejectQuery.getRejectCodeList()));
		}
		params.remove("rejectCodeList");
		params.put(MYBATIS_SPECIAL_STRING.WHERE.name(), wehreList);
		params.remove("startPage");
		params.remove("pageSize");
		PageInfo<RejectInfo> pageInfo = new PageInfo<RejectInfo>(rejectQuery.getStartPage(),rejectQuery.getPageSize());
		pageInfo = rejectInfoService.queryPageByParam(params, pageInfo);
		if(pageInfo!=null && CollectionUtils.isNotEmpty(pageInfo.getRows())){
			List<Long> rejectCodeList = new ArrayList<Long>();
			for(RejectInfo reject:pageInfo.getRows()){
				rejectCodeList.add(reject.getRejectCode());
			}
			params.clear();
			params.put(MYBATIS_SPECIAL_STRING.COLUMNS.name(), " reject_code in ("+StringUtil.join(rejectCodeList, Constant.SPLIT_SIGN.COMMA)+")");
			List<RejectItem> rejectItemList = rejectItemService.queryByParam(params);
			for(RejectInfo reject:pageInfo.getRows()){
				for(RejectItem rejectItem:rejectItemList){
					if(reject.getRejectCode().equals(rejectItem.getRejectCode())){
						reject.getRejectItemList().add(rejectItem);
					}
				}
			}
			
		}
		return pageInfo;
	}

	/**
	 * 审核退货信息
	 * @param rejectQuery
	 * @return
	 */
	public ResultInfo<?> auditReject(RejectAudit rejectAudit) {
//		RejectInfo reject = new RejectInfo();
		Long rejectId = rejectAudit.getRejectId();
		if(null == rejectId) return new ResultInfo<>(new FailInfo("没有要审核的退货信息"));
		
		RejectInfo rejectInfo = rejectInfoService.queryById(rejectId);
		if(null == rejectInfo) return new ResultInfo<>(new FailInfo("系统没有要审核的退货信息"));
		
		Integer auditStatus = RejectConstant.REJECT_AUDIT_STATUS.XG_FIAL.code; //默认审核不通过		
		
		Boolean success = rejectAudit.getSuccess();
		
//		reject.setOrderCode(rejectInfo.getOrderCode());
//		reject.setRejectCode(rejectAudit.getRejectCode());
		rejectInfo.setRejectCode(rejectAudit.getRejectCode());
//		reject.setAuditStatus(auditStatus);
		rejectInfo.setAuditStatus(auditStatus);
//		reject.setRemarks(rejectAudit.getRemark());
		rejectInfo.setRemarks(rejectAudit.getRemark());		
//		reject.setRejectId(rejectInfo.getRejectId());
//		reject.setLinkMobile(rejectInfo.getLinkMobile());
		rejectInfo.setReturnAddress( rejectAudit.getReturnAddress() );
		rejectInfo.setReturnContact(rejectAudit.getReturnContact());
		rejectInfo.setReturnMobile( rejectAudit.getReturnMobile());
		if(null == rejectAudit.getAmount()){
			return new ResultInfo<>(new FailInfo("请输入退货金额"));
		}
//		reject.setRefundAmount(rejectAudit.getAmount());
		rejectInfo.setRefundAmount(rejectAudit.getAmount());
		rejectInfo.setPoints(rejectAudit.getPoints());
		rejectInfo.setCreateUser(rejectAudit.getCreateUser());
		rejectInfo.setOperatorType(Constant.LOG_AUTHOR_TYPE.USER_CALL.code);
		rejectInfo.setSuccess(success);
		rejectInfo.setRejectItemId(rejectAudit.getRejectItemId());
		rejectInfoService.updateForAudit(rejectInfo);	
		return new ResultInfo<>(Boolean.TRUE);
	}

	public RejectInfo queryRejectItem(Long rejectId) {
		RejectInfo rejectInfo = rejectInfoService.queryById(rejectId);
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("rejectCode", rejectInfo.getRejectCode());
		List<RejectItem> rejectItemList = rejectItemService.queryByParam(params);
		rejectInfo.setRejectItemList(rejectItemList);
		return rejectInfo;
	}

	public RejectInfo queryByOrderNo(Long orderNo) {
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("orderCode", orderNo);
		return rejectInfoService.queryUniqueByParams(params);
	}

	public List<RejectItem> queryRejectItemListByRejectNo(Long rejectNo) {
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("rejectCode", rejectNo);
		return rejectItemService.queryByParam(params);
	}

	public RejectInfo queryRejectByRefundNo(Long refundNo) {
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("refundCode", refundNo);
		RejectInfo rejectInfo = rejectInfoService.queryUniqueByParams(params);
		if(null!=rejectInfo){
			rejectInfo.setRejectItemList(queryRejectItemListByRejectNo(rejectInfo.getRejectCode()));
		}
		return rejectInfo;
	}

	public List<RejectLog> queryRejectLog(Long rejectNo) {
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("rejectCode", rejectNo);
		return rejectLogService.queryByParam(params);
	}

	public List<SubOrderExpressInfoDTO> queryExpressInfo(String rejectNo,
			String expressNo) {
		List<SubOrderExpressInfoDTO> queryExpressLogInfo = subOrderService.queryExpressLogInfo(rejectNo, expressNo);
		return queryExpressLogInfo;	
	}

	public ResultInfo<?> forceAudit(RejectAuditDTO rejectAudit) {
		RejectInfo rejectInfo = rejectInfoService.queryById(rejectAudit.getRejectId());
		if(null==rejectInfo){
			return new ResultInfo<>(new FailInfo("根据参数查询不到信息"));
		}
		rejectInfo.setAuditStatus(RejectConstant.REJECT_AUDIT_STATUS.FORCE_AUDITED.code); 
		rejectInfo.setRejectStatus(RejectConstant.REJECT_STATUS.REFUNDING.code);
		rejectInfo.setUpdateUser(rejectAudit.getCreateUser());
		rejectInfo.setRemarks(rejectAudit.getRemark());
		if(null == rejectAudit.getAmount()){
			return new ResultInfo<>(new FailInfo("请输入退货金额"));
		}
		rejectInfo.setRefundAmount(rejectAudit.getAmount());
		rejectInfo.setPoints(rejectAudit.getPoints());
		rejectInfoService.updateForForceAudit(rejectInfo);
		return new ResultInfo<>(Boolean.TRUE);
		
	}

	public List<RejectItem> queryRejectItemListByRefundNoList(List<Long> refundNos){
		Map<String,Object> params = new HashMap<String,Object>();
		params.put(MYBATIS_SPECIAL_STRING.COLUMNS.name(), " refund_code in ("+StringUtil.join(refundNos, Constant.SPLIT_SIGN.COMMA)+")");
		List<RejectInfo> rejectInfoList = rejectInfoService.queryByParam(params);
		if(CollectionUtils.isNotEmpty(rejectInfoList)){
			List<Long> rejectNoList = new ArrayList<Long>();
			for(RejectInfo rejectInfo:rejectInfoList){
				rejectNoList.add(rejectInfo.getRejectCode());
			}
			params.put(MYBATIS_SPECIAL_STRING.COLUMNS.name(), " reject_code in ("+StringUtil.join(rejectNoList, Constant.SPLIT_SIGN.COMMA)+")");
			return rejectItemService.queryByParam(params);
		}
		return null;
	}
	
    public void refundResult(final Long refundCode, final Boolean isSuccess) {
        RejectInfo rejectInfoDO = queryRejectByRefundNo(refundCode);
        if (rejectInfoDO == null) {
            logger.error("数据错误,根据退款单{}没有找到对应的退货信息", refundCode);
            return;
        }
        if (isSuccess) {

            RejectLog log = new RejectLog();
            log.setRejectCode(rejectInfoDO.getRejectCode());
            log.setOrderCode(rejectInfoDO.getOrderCode());
            log.setActionType(RejectConstant.REJECT_LOG_ACTIVE_TYPE.REFUND.code);
            log.setOldRejectStatus(RejectConstant.REJECT_STATUS.REJECTING.code);
            log.setCurrentRejectStatus(RejectConstant.REJECT_STATUS.REJECTED.code);
            log.setAuditStatus(rejectInfoDO.getAuditStatus());
            log.setLogContent(RejectConstant.REJECT_STATUS.REJECTED.cnName);
            log.setOperatorType(4);
            log.setOperatorName("system");
            rejectLogService.insert(log);

            RejectInfo toBeUpdated = new RejectInfo();
            toBeUpdated.setRejectId(rejectInfoDO.getRejectId());
            toBeUpdated.setRejectStatus(RejectConstant.REJECT_STATUS.REJECTED.code);
            try {
                rejectInfoService.updateNotNullById(toBeUpdated);

                RejectItem query = new RejectItem();
                query.setRejectCode(rejectInfoDO.getRejectCode());
                List<RejectItem> rejectItemDOList = rejectItemService.queryByObject(query);
                if (!CollectionUtils.isEmpty(rejectItemDOList)) {
                    OrderItem toBeUpdatedOrderLineDO = new OrderItem();
                    toBeUpdatedOrderLineDO.setId(rejectItemDOList.get(0).getOrderItemId());
                    toBeUpdatedOrderLineDO.setRefundStatus(RejectConstant.REJECT_STATUS.REJECTED.code);
                    orderItemService.updateNotNullById(toBeUpdatedOrderLineDO);
                } else {
                    logger.warn("退货单号" + rejectInfoDO.getRejectCode() + "没找到对应的item");
                }
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
                throw new ServiceException(e);
            }
            try {
                sendSmsService.sendSms(rejectInfoDO.getLinkMobile(), "亲爱的：您的订单#" + rejectInfoDO.getOrderCode() + "#已退款，退款预计在3-15个工作日内到账!",null);
            } catch (SmsException e) {
                logger.error(e.getMessage(), e);
            }
        }
    }
    
    
    
	public ResultInfo<Boolean> cancelReject(Long rejectId, MemberInfo usr) {
		try{
			orderRejectRemoteService.cancelRejectInfo(rejectId, usr);
			return new ResultInfo<>(Boolean.TRUE);
		} catch(OrderServiceException ex){
			return new ResultInfo<>(new FailInfo(ex.getMessage()));
		}catch(Throwable exception){
//			ExceptionUtils.print(new FailInfo(exception), logger,orderCode,userInfo);
			return new ResultInfo<>(new FailInfo("退货单取消失败"));
		}		
	}
	
	public ResultInfo<Boolean> updateRejectData(MemberInfo user, RejectInfo rejectInfo,	RejectItem rejectItem ) {
		try{
			orderRejectRemoteService.updateRejctData(user, rejectInfo, rejectItem);
			return new ResultInfo<>(Boolean.TRUE);			
		} catch(OrderServiceException ex){
			return new ResultInfo<>(new FailInfo(ex.getMessage()));
		}catch(Throwable exception){
			return new ResultInfo<>(new FailInfo("退货单修改失败"));
		}		
	}
	
}
