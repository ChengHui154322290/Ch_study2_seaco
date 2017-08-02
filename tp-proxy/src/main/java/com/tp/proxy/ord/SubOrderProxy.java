package com.tp.proxy.ord;

import static com.tp.util.BigDecimalUtil.divide;
import static com.tp.util.BigDecimalUtil.multiply;
import static com.tp.util.BigDecimalUtil.toPrice;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.util.ExceptionUtils;
import com.tp.common.util.ImageUtil;
import com.tp.common.util.OrderUtils;
import com.tp.common.vo.Constant;
import com.tp.common.vo.DssConstant;
import com.tp.common.vo.OrderConstant;
import com.tp.common.vo.PageInfo;
import com.tp.common.vo.Constant.AUTHOR_TYPE;
import com.tp.common.vo.DAOConstant.MYBATIS_SPECIAL_STRING;
import com.tp.common.vo.OrderConstant.ClearanceStatus;
import com.tp.common.vo.OrderConstant.DeclareCustoms;
import com.tp.common.vo.OrderConstant.PutCustomsType;
import com.tp.common.vo.bse.ClearanceChannelsEnum;
import com.tp.common.vo.ord.LogTypeConstant;
import com.tp.common.vo.ord.LogTypeConstant.LOG_LEVEL;
import com.tp.common.vo.ord.LogTypeConstant.LOG_TYPE;
import com.tp.dto.common.FailInfo;
import com.tp.dto.common.ResultInfo;
import com.tp.dto.ord.remote.OrderLine4ExcelDTO;
import com.tp.dto.ord.remote.SubOrder4BackendDTO;
import com.tp.enums.common.PlatformEnum;
import com.tp.exception.OrderServiceException;
import com.tp.model.dss.CommisionDetail;
import com.tp.model.dss.PromoterInfo;
import com.tp.model.mem.MemberInfo;
import com.tp.model.ord.CustomsClearanceLog;
import com.tp.model.ord.OrderDelivery;
import com.tp.model.ord.OrderItem;
import com.tp.model.ord.OrderStatusLog;
import com.tp.model.ord.RejectInfo;
import com.tp.model.ord.SubOrder;
import com.tp.model.wms.StockoutBack;
import com.tp.proxy.BaseProxy;
import com.tp.proxy.dss.GlobalCommisionProxy;
import com.tp.proxy.dss.PromoterInfoProxy;
import com.tp.proxy.prd.ExcelWriter;
import com.tp.query.ord.RejectQuery;
import com.tp.query.ord.SubOrderQO;
import com.tp.redis.util.JedisCacheUtil;
import com.tp.result.ord.SubOrderExpressInfoDTO;
import com.tp.service.IBaseService;
import com.tp.service.dss.ICommisionDetailService;
import com.tp.service.dss.IFastUserInfoService;
import com.tp.service.mem.IMemberInfoService;
import com.tp.service.mem.ISendSmsService;
import com.tp.service.ord.ICustomsClearanceLogService;
import com.tp.service.ord.IOrderStatusLogService;
import com.tp.service.ord.IRejectInfoService;
import com.tp.service.ord.ISubOrderService;
import com.tp.service.ord.remote.ISalesOrderRemoteService;
import com.tp.service.stg.IOutputOrderService;
/**
 * 子订单表代理层
 * @author szy
 *
 */
@Service
public class SubOrderProxy extends BaseProxy<SubOrder>{
	private static final String  PAY_SEPERATOR = " - ";
	/** 毫秒限制 */
	private static final Long MILLESECONDS_LIMIT = 31L * 24 * 60 * 60 * 1000;
	/** 分页大小 */
	private static final int PAGE_SIZE = 100;
	/** 导出条数限制 */
	private static final int EXPORT_LIMIT = 50000;
	private static final int DEFAULT_EXPORT_LIMIT = 5000;
	@Autowired
	private ISubOrderService subOrderService;
	@Autowired
	private IMemberInfoService memberInfoService;
	
	// by zhs 0223
	@Autowired
	private IRejectInfoService rejectInfoService;
	@Autowired
	ICommisionDetailService commisionDetailService;

	@Autowired
	private ISalesOrderRemoteService salesOrderRemoteService;
	@Autowired
	private ExcelWriter<OrderLine4ExcelDTO> orderExcelWriter;
	@Autowired
	private PromoterInfoProxy promoterInfoProxy;
	@Autowired
	private GlobalCommisionProxy globalCommisionProxy;
	@Autowired
	private ICustomsClearanceLogService customsClearanceLogService;
	@Autowired
	private IOrderStatusLogService orderStatusLogService;
	@Autowired
	private IOutputOrderService outputOrderService;
	@Autowired
	private JedisCacheUtil jedisCacheUtil;
	@Autowired
	private ISendSmsService sendSmsService;
	@Autowired
	private IFastUserInfoService fastUserInfoService;
	
	@Override
	public IBaseService<SubOrder> getService() {
		return subOrderService;
	}
	
	public Double GetShopTotalSales(Long promoterId){		
		return subOrderService.getShopTotalSales(promoterId);
	}

	public Double GetShopTotalSales_coupons_shop_scan(Long promoterId, Integer promoterType){

		Map<String, Object> params = new HashMap<String, Object>();
		CommisionDetail detail = new CommisionDetail();
		if( DssConstant.PROMOTER_TYPE.DISTRIBUTE.code.equals(promoterType) ){
			params.put("shopPromoterId", promoterId);			
			detail.setPromoterId( promoterId );
			detail.setPromoterType(DssConstant.PROMOTER_TYPE.DISTRIBUTE.code);
		}else if( DssConstant.PROMOTER_TYPE.COUPON.code.equals(promoterType) ){
			params.put("promoterId", promoterId);						
			detail.setPromoterId( promoterId );
			detail.setPromoterType(DssConstant.PROMOTER_TYPE.COUPON.code);
		}else if( DssConstant.PROMOTER_TYPE.SCANATTENTION.code.equals(promoterType) ){
			params.put("scanPromoterId", promoterId);									
			detail.setPromoterId( promoterId );
			detail.setPromoterType(DssConstant.PROMOTER_TYPE.SCANATTENTION.code);
		}
		 List<Long> ordercodeList = commisionDetailService.queryDistinctOrderCode(detail);
		 if(ordercodeList == null  ){
			 ordercodeList = new ArrayList<Long>();
			 ordercodeList.add(0L);
		 }else if( ordercodeList.isEmpty() ){
			 ordercodeList.add(0L);			 
		 }
		 params.put("orderCodeList", ordercodeList);		 
		return subOrderService.getShopTotalSales_coupons_shop_scan(params);
	}

	
	public SubOrder findSubOrderByCode(Long orderCode) {
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("orderCode", orderCode);
		return subOrderService.queryUniqueByParams(params);
	}
	
	
	public List<OrderLine4ExcelDTO> exportSubOrder(SubOrderQO qo) {
		restrictExcelQO(qo);	// 限制查询条件（31天范围内）
		List<SubOrder4BackendDTO> list = new ArrayList<SubOrder4BackendDTO>();
		int times = 0;
		if (null == qo.getStartTime() && null == qo.getEndTime()) {
			times = DEFAULT_EXPORT_LIMIT / qo.getPageSize() + 1; // 不限制时间时，倒出5千条数据，遍历次数
		} else {
			times = EXPORT_LIMIT / qo.getPageSize() + 1; // 5万条数据，遍历次数
		}
		for (int i = 1; i < times; i++) {	// 从第一页开始分页查询，直到最后一页
			qo.setStartPage(i);
			PageInfo<SubOrder4BackendDTO> page = findSubOrder4BackendPage(qo);
			List<SubOrder4BackendDTO> pList = page.getRows();
			list.addAll(pList);
			
			if (CollectionUtils.isEmpty(pList) || pList.size() < PAGE_SIZE) {
				break;
			}
		}
		List<OrderLine4ExcelDTO> dataList = extractSubOrder4ExcelList(list);
		return dataList;
	}

	public List<RejectInfo> findRejectInfoListBySubCode(Long code) {
		//return null;
		// by zhs 0223
		RejectQuery qo = new RejectQuery();
		qo.setOrderCode(code);
		PageInfo<RejectInfo> page = rejectInfoService.queryPageListByRejectQuery(qo);
		if (null != page && CollectionUtils.isNotEmpty(page.getRows())) {
			return page.getRows();
		} else {
			return null ;
		}
	}

	public List<SubOrderExpressInfoDTO> findExpressListBySubCode(Long subCode) {
		// TO Auto-generated method stub
		return salesOrderRemoteService.queryExpressLogInfo(subCode, null);
	}
	
	public PageInfo<SubOrder4BackendDTO> findSubOrder4BackendPage(SubOrderQO qo){
		process(qo);	// 加工QO
		PageInfo<SubOrder4BackendDTO> page = salesOrderRemoteService.findSubOrder4BackendPage(qo);
		setMemberInfo(page);
		setPromoterInfo(page);
		return page;
	}
	
	public Map<String,Number> queryOrderCountByPromoterId(Map<String,Object> params){
		return subOrderService.queryOrderCountByPromoterId(params);
	}
	
	//重置清关状态（重新推送海关）
	public ResultInfo<Boolean> resetPutCustomsStatus(String subCode, Integer type){
		if (StringUtils.isEmpty(subCode)) {
			return new ResultInfo<>(new FailInfo("订单号为空"));
		}
		PutCustomsType customsType = PutCustomsType.getPutCustomsTypeByIndex(type);
		if(customsType == null){
			return new ResultInfo<>(new FailInfo("重置通关类型不存在"));
		}
		try {
			Map<String, Object> params = new HashMap<>();
			params.put("orderCode", subCode);
			SubOrder subOrder = subOrderService.queryUniqueByParams(params);
			if (null == subOrder) {
				return new ResultInfo<>(new FailInfo("子订单不存在"));
			}
			//已清关通过的不允许重置
			if (ClearanceStatus.AUDIT_SUCCESS.code.equals(subOrder.getClearanceStatus())) {
				return new ResultInfo<>(new FailInfo("已清关的订单不允许重置"));
			}		
			//等待清关的订单不允许重置(已推送成功的)
			if (ClearanceStatus.PUT_SUCCESS.code.equals(subOrder.getClearanceStatus())) {
				return new ResultInfo<>(new FailInfo("等待清关的订单不允许重置"));
			}
			SubOrder so = new SubOrder();
			so.setId(subOrder.getId());
			so.resetPutCustomsStatus(customsType);
			subOrderService.updateNotNullById(so);
			CustomsClearanceLog log = new CustomsClearanceLog();
			log.setOrderCode(Long.valueOf(subCode));
			log.setCustomsCode(DeclareCustoms.JKF.code);
			log.setCreateTime(new Date());
			log.setType(customsType.getIndex());
			log.setResult(1);
			log.setResultDesc("重置");
			customsClearanceLogService.insert(log);
			return new ResultInfo<>(Boolean.TRUE);
		} catch (Exception e) {
			ExceptionUtils.print(new FailInfo(e), logger, subCode);
			return new ResultInfo<>(new FailInfo("操作异常"));
		}
	}
	
	//手动推送仓库
	public ResultInfo<Boolean> deliverSubOrderToWMS(Long subCode){
		if (null == subCode) {
			return new ResultInfo<>(new FailInfo("订单号为空"));
		}
		SubOrder subOrder = subOrderService.selectOneByCode(subCode);
		if (null == subOrder) {
			return new ResultInfo<>(new FailInfo("订单号不存在"));
		}
		if (!subOrder.getPutWaybill()) {
			return new ResultInfo<>(new FailInfo("订单不需要推送仓库"));
		}
		if (!ClearanceStatus.AUDIT_SUCCESS.code.equals(subOrder.getClearanceStatus())) {
			return new ResultInfo<>(new FailInfo("未清关通过的订单不需要推送"));
		}
		if (subOrder.getPutStatus() == 1) {
			return new ResultInfo<>(new FailInfo("订单已成功推送仓库，不需要重复推送"));
		}
		boolean bResult = salesOrderRemoteService.putWareHouseShippingBySeaSubOrder(subOrder);
		if (!bResult) {
			return new ResultInfo<>(new FailInfo("推送失败"));
		}
		return new ResultInfo<>(Boolean.TRUE);
	}
	
	/*---------------------------------------------------------------------------*/
	/*---------------------------------------------------------------------------*/
	private void process(SubOrderQO qo) {
		trim(qo);	// string类型参数trim
		
		/* 登录名 */
		String loginName = qo.getLoginName();
		if (StringUtils.isNotBlank(loginName)) {
			Map<String,Object> params = new HashMap<String,Object>();
			params.put(MYBATIS_SPECIAL_STRING.COLUMNS.name()," email='"+loginName +"' or mobile='"+loginName+"'");
			MemberInfo memberInfo = memberInfoService.queryUniqueByParams(params);
			if (null != memberInfo) {
				qo.setMemberId(memberInfo.getId());
			}
		}
		
		/* 订单号为父订单号 */
		Long code = qo.getCode();
		if (null!=code) {
			if (String.valueOf(code).startsWith(Constant.DOCUMENT_TYPE.SO_ORDER.code.toString())) {
				qo.setCode(null);
				qo.setParentOrderCode(code);
			}else{
				qo.setCode(null);
				qo.setOrderCode(code);
			}
		}
	}
	
	private void trim(SubOrderQO qo) {
		if (StringUtils.isNotBlank(qo.getLoginName())) {
			qo.setLoginName(qo.getLoginName().trim());
		}
		if (StringUtils.isNotBlank(qo.getConsigneeName())) {
			qo.setConsigneeName(qo.getConsigneeName().trim());
		}
		if (StringUtils.isNotBlank(qo.getSupplierName())) {
			qo.setSupplierName(qo.getSupplierName().trim());
		}
	}

	// 设置会员信息
	private void setMemberInfo(PageInfo<SubOrder4BackendDTO> page) {
		/* 获取会员名 */
		List<SubOrder4BackendDTO> list = page.getRows();
		if (CollectionUtils.isNotEmpty(list)) {
			List<Long> memberIdList = extractMemberId(list);
			List<MemberInfo> userList = memberInfoService.selectByIds(memberIdList);
			Map<Long, MemberInfo> userMap = toUserMap_id(userList);
			for (SubOrder4BackendDTO dto : list) {
				Long memberId = dto.getOrder().getMemberId();
				MemberInfo member = userMap.get(memberId);
				if (null != member) {
					dto.setLoginName(member.getMobile());
				}
			}
		}
	}
	
	// 设置会员信息
	private void setPromoterInfo(PageInfo<SubOrder4BackendDTO> page) {
		/* 获取会员名 */
		List<SubOrder4BackendDTO> list = page.getRows();
		if (CollectionUtils.isNotEmpty(list)) {
			List<Long> promoterIdList = extractPromoterId(list);
			List<PromoterInfo> promoterList = promoterInfoProxy.queryPromoterInfoByIdList(promoterIdList).getData();
			Map<Long, String> promoterMap = transMap(promoterList);
			for (SubOrder4BackendDTO dto : list) {
				dto.setPromoterName(promoterMap.get(dto.getSubOrder().getPromoterId()));
				dto.setShopPromoterName(promoterMap.get(dto.getSubOrder().getShopPromoterId()));
				dto.setScanPromoterName(promoterMap.get(dto.getSubOrder().getScanPromoterId()));
			}
		}
	}
	// 提取会员id
	private List<Long> extractMemberId(List<SubOrder4BackendDTO> list) {
		if (CollectionUtils.isNotEmpty(list)) {
			Set<Long> memberIdList = new HashSet<Long>(list.size());	// 去重
			for (SubOrder4BackendDTO dto : list) {
				memberIdList.add(dto.getOrder().getMemberId());
			}
			return new ArrayList<Long>(memberIdList);
		}
		return new ArrayList<Long>(0);
	}
	
	// 提取会员id
	private List<Long> extractPromoterId(List<SubOrder4BackendDTO> list) {
		if (CollectionUtils.isNotEmpty(list)) {
			Set<Long> promoterIdSet = new HashSet<Long>(list.size());	// 去重
			for (SubOrder4BackendDTO dto : list) {
				if(dto.getOrder().getPromoterId()!=null){
					promoterIdSet.add(dto.getOrder().getPromoterId());
				}
				if(dto.getOrder().getShopPromoterId()!=null){
					promoterIdSet.add(dto.getOrder().getShopPromoterId());
				}
				if(dto.getOrder().getScanPromoterId()!=null){
					promoterIdSet.add(dto.getOrder().getScanPromoterId());					
				}				
			}
			return new ArrayList<Long>(promoterIdSet);
		}
		return new ArrayList<Long>(0);
	}
		
	private Map<Long, MemberInfo> toUserMap_id(List<MemberInfo> userList) {
		if (CollectionUtils.isNotEmpty(userList)) {
			Map<Long, MemberInfo> map = new HashMap<Long, MemberInfo>();
			for (MemberInfo user : userList) {
				map.put(user.getId(), user);
			}
			return map;
		}
		return new HashMap<Long, MemberInfo>(0);
	}
	
	private Map<Long, String> transMap(List<PromoterInfo> promoterInfoList) {
		if (CollectionUtils.isNotEmpty(promoterInfoList)) {
			Map<Long, String> map = new HashMap<Long, String>();
			for (PromoterInfo promoterInfo : promoterInfoList) {
				map.put(promoterInfo.getPromoterId(), promoterInfo.getPromoterName());
			}
			return map;
		}
		return new HashMap<Long, String>(0);
	}
	
	// 导出条件限制
	private void restrictExcelQO(SubOrderQO qo) {
		qo.setPageSize(PAGE_SIZE);	// 100条纪录一批
		
		/* 最多导出31天的订单 */
		Date startTime = qo.getStartTime();
		Date endTime = qo.getEndTime();
		if (null == startTime && null == endTime) {	// 默认导出当月订单
				Calendar beginOfDay = Calendar.getInstance();
				beginOfDay.set(Calendar.DAY_OF_MONTH, 1);
				beginOfDay.set(Calendar.HOUR_OF_DAY, 0);
				beginOfDay.set(Calendar.MINUTE, 0);
				beginOfDay.set(Calendar.SECOND, 0);
				beginOfDay.set(Calendar.MILLISECOND, 0);
				Calendar endOfDay = Calendar.getInstance();
				qo.setStartTime(beginOfDay.getTime());
				qo.setEndTime(endOfDay.getTime());
		} else if (null == startTime) {	// 开始时间缺省值 － 结束时间前一个月
			Calendar cal = Calendar.getInstance();
			cal.setTime(endTime);
			cal.add(Calendar.MONTH, -1);
			qo.setStartTime(cal.getTime());
		} else if (null == endTime) {	// 结束时间缺省值 － 开始时间后一个月
			Calendar cal = Calendar.getInstance();
			cal.setTime(startTime);
			cal.add(Calendar.MONTH, 1);
			qo.setEndTime(cal.getTime());
		} else {
			if (endTime.getTime() - startTime.getTime() > MILLESECONDS_LIMIT) {
				throw new OrderServiceException("下单时间范围不能超过31天");
			}
		}
	}

	// 获取excel model列表
	private List<OrderLine4ExcelDTO> extractSubOrder4ExcelList(List<SubOrder4BackendDTO> list) {
		if (CollectionUtils.isEmpty(list)) 
			return new ArrayList<OrderLine4ExcelDTO>(0);
		
		/* 获取会员名 */
		List<Long> memberIdList = extractMemberId(list);
		List<MemberInfo> userList = memberInfoService.selectByIds(memberIdList);
		Map<Long, MemberInfo> userMap = toUserMap_id(userList);
		
		List<Long> promoterIdList = extractPromoterId(list);
		List<PromoterInfo> promoterInfoList = promoterInfoProxy.queryPromoterInfoByIdList(promoterIdList).getData();
		Map<Long,String> promoterNameMap = transMap(promoterInfoList);
		
		List<OrderLine4ExcelDTO> dataList = new ArrayList<OrderLine4ExcelDTO>(list.size());
		for (SubOrder4BackendDTO backDTO : list) {
			for (OrderItem line : backDTO.getOrderItemList()) {
				OrderLine4ExcelDTO exline = new OrderLine4ExcelDTO();
				
				exline.setCode(""+backDTO.getSubOrder().getOrderCode());
				exline.setOrderCode(""+backDTO.getSubOrder().getParentOrderCode());
				exline.setStatusStr(backDTO.getSubOrder().getStatusStr());
				exline.setTypeStr(backDTO.getSubOrder().getTypeStr());
				exline.setSupplierName(backDTO.getSubOrder().getSupplierName());
				MemberInfo user = userMap.get(backDTO.getOrder().getMemberId());
				if (null != user) {
					exline.setLoginName(user.getMobile());
				}
				exline.setPayTotal(backDTO.getSubOrder().getPayTotal());
				if(backDTO.getOrderConsignee()!=null){
					exline.setProvince(backDTO.getOrderConsignee().getProvinceName());
					exline.setCity(backDTO.getOrderConsignee().getCityName());
					exline.setCounty(backDTO.getOrderConsignee().getCountyName());
					exline.setTown(backDTO.getOrderConsignee().getTownName());
					exline.setConsigneeAddress(backDTO.getOrderConsignee().getAddress());
					exline.setConsigneeName(backDTO.getOrderConsignee().getName());
					exline.setConsigneeMobile(backDTO.getOrderConsignee().getMobile());
				}else{
					exline.setProvince("");
					exline.setCity("");
					exline.setCounty("");
					exline.setTown("");
					exline.setConsigneeAddress("");
					exline.setConsigneeName("");
					exline.setConsigneeMobile("");
				}
				exline.setCreateTime(backDTO.getSubOrder().getCreateTime());
				exline.setTaxRate(line.getTaxRate());
				exline.setTaxFee(line.getTaxFee());
				exline.setSubAmount(line.getOriginalSubTotal());
				if (null != backDTO.getOrderDelivery()) {
					exline.setDeliveryTime(backDTO.getOrderDelivery().getDeliveryTime());
					exline.setExpressNo(backDTO.getOrderDelivery().getPackageNo());
					exline.setExpressName(backDTO.getOrderDelivery().getCompanyName());
				}
				
				exline.setSeaChannel(backDTO.getSubOrder().getSeaChannelStr());
				if (OrderUtils.isSeaOrder(backDTO.getSubOrder().getType())) {	// 海淘
					exline.setPayCode(backDTO.getSubOrder().getPayCode());
					if(backDTO.getMemRealinfo()!=null){
						exline.setIdCode(backDTO.getMemRealinfo().getIdentityCode());
						exline.setRealName(backDTO.getMemRealinfo().getRealName());
						exline.setFrontImg(ImageUtil.getCMSImgFullUrl(backDTO.getMemRealinfo().getIdentityFrontImg()));
						exline.setBackImg(ImageUtil.getCMSImgFullUrl(backDTO.getMemRealinfo().getIdentityBackImg()));
					}
					exline.setPayWayStr(backDTO.getSubOrder().getPayTypeStr() + PAY_SEPERATOR + backDTO.getSubOrder().getPayWayStr());
					exline.setPayTime(backDTO.getSubOrder().getPayTime());
				} else {
					exline.setPayWayStr(backDTO.getOrder().getPayTypeStr() + PAY_SEPERATOR + backDTO.getOrder().getPayWayStr());
					exline.setPayCode(backDTO.getOrder().getPayCode());
					exline.setPayTime(backDTO.getOrder().getPayTime());
				}
				exline.setDiscount(backDTO.getSubOrder().getDiscount());
				
				/* 行数据 */
				exline.setBarCode(line.getBarCode());
				exline.setQuantity(line.getQuantity());
				exline.setSkuCode(line.getSkuCode());
				exline.setSpuName(line.getSpuName());
				exline.setProductCode(line.getProductCode());
				exline.setTopicId(line.getTopicId().toString());
				exline.setUnit(line.getUnit());
				exline.setPrice(line.getPrice());
				exline.setRealPrice(line.getItemAmount());
				exline.setSubRealAmount(line.getSubTotal());
				exline.setSourceName(PlatformEnum.getDescByCode(backDTO.getSubOrder().getSource()));
				exline.setPromoterName(promoterNameMap.get(backDTO.getSubOrder().getPromoterId()));
				exline.setShopPromoterName(promoterNameMap.get(backDTO.getSubOrder().getShopPromoterId()));
				exline.setScanPromoterName(promoterNameMap.get(backDTO.getSubOrder().getScanPromoterId()));
				if (OrderUtils.isSeaOrder(backDTO.getSubOrder().getType())) {
					Double taxFee = toPrice(divide(multiply(multiply(line.getSalesPrice(),line.getQuantity()),line.getTaxRate()),100,6));
					exline.setDuties(taxFee);
					if (OrderConstant.OrderType.DOMESTIC.getCode().equals(line.getStorageType())
					   || ClearanceChannelsEnum.HWZY.id.equals(line.getSeaChannel())){
						exline.setDuties(Constant.ZERO);
					}
				}
				exline.setSalesPrice(line.getSalesPrice());
				exline.setCouponAmount(line.getCouponAmount());
				exline.setOrderCouponAmount(line.getOrderCouponAmount());
				if(line.getPoints()==null){
					line.setPoints(0);
				}
				if("hhb".equalsIgnoreCase(backDTO.getSubOrder().getChannelCode())){
					exline.setPointTypeZh("慧币");
				}else{
					exline.setPointTypeZh("西客币");
				}
				exline.setPoints(toPrice(divide(line.getPoints(),100,2)));
				dataList.add(exline);
			}
		}
		return dataList;
	}

	/**
	 * 速购接单
	 * @param orderCode
	 * @return
	 */
	public ResultInfo<Boolean> receivingOrder(Long orderCode,Long updateUserId,String updateUserName,LogTypeConstant.LOG_TYPE logType) {
		SubOrder subOrder = findSubOrderByCode(orderCode);
		if(!OrderConstant.FAST_ORDER_TYPE.equals(subOrder.getType())){
			return new ResultInfo<Boolean>(new FailInfo("只有速购类型的订单才能接单"));
		}
		if(!OrderConstant.ORDER_STATUS.TRANSFER.code.equals(subOrder.getOrderStatus())){
			return new ResultInfo<Boolean>(new FailInfo("订单状态是"+OrderConstant.ORDER_STATUS.getAlias(subOrder.getOrderStatus())+",不能接单"));
		}
		SubOrder order = new SubOrder();
		order.setId(subOrder.getId());
		order.setOrderStatus(OrderConstant.ORDER_STATUS.DELIVERY.code);
		order.setUpdateTime(new Date());
		OrderStatusLog orderStatusLog = new OrderStatusLog();
		orderStatusLog.setContent("["+logType.cnName+"]"+updateUserName+"已接单，开始进行备货");
		orderStatusLog.setCreateTime(new Date());
		orderStatusLog.setCreateUserId(updateUserId);
		orderStatusLog.setCreateUserName(updateUserName);
		orderStatusLog.setCreateUserType(logType.code);
		orderStatusLog.setCurrStatus(OrderConstant.ORDER_STATUS.DELIVERY.code);
		orderStatusLog.setName("接单");
		orderStatusLog.setOrderCode(orderCode);
		orderStatusLog.setParentOrderCode(subOrder.getParentOrderCode());
		orderStatusLog.setPreStatus(subOrder.getOrderStatus());
		orderStatusLog.setType(LOG_LEVEL.INFO.code);
		try{
			subOrderService.updateNotNullById(order);
			orderStatusLogService.insert(orderStatusLog);
		}catch(Throwable throwable){
			return new ResultInfo<Boolean>(ExceptionUtils.println(new FailInfo(throwable), logger, order,orderStatusLog));
		}
		return new ResultInfo<Boolean>(Boolean.TRUE);
	}

	/**
	 * 速购配送
	 * @param orderCode
	 * @return
	 */
	public ResultInfo<Boolean> deliveryOrder(Long orderCode,Long updateUserId,String updateUserName,
			LogTypeConstant.LOG_TYPE logType,Long fastUserId,String content) {
		
		
		SubOrder subOrder = findSubOrderByCode(orderCode);
		if(!OrderConstant.FAST_ORDER_TYPE.equals(subOrder.getType())){
			return new ResultInfo<Boolean>(new FailInfo("只有速购类型的订单才能配送"));
		}
		if(!OrderConstant.ORDER_STATUS.DELIVERY.code.equals(subOrder.getOrderStatus())){
			return new ResultInfo<Boolean>(new FailInfo("订单状态是"+OrderConstant.ORDER_STATUS.getAlias(subOrder.getOrderStatus())+",不能配送"));
		}

		List<SubOrder> list = new ArrayList<SubOrder>();
		subOrder.setPromoterId(null);
		subOrder.setShopPromoterId(null);
		list.add(subOrder);
		OrderDelivery orderDelivery = new OrderDelivery();
		orderDelivery.setCompanyId("seagoorFast");
		orderDelivery.setCompanyName("西客速购");
		orderDelivery.setOrderCode(orderCode);
		orderDelivery.setPackageNo(orderCode.toString());
		orderDelivery.setCreateUser(AUTHOR_TYPE.SYSTEM);	// 设置处理人
		orderDelivery.setDeliveryTime(new Date());			// 设置发货时间
		SubOrder order = new SubOrder();
		order.setId(subOrder.getId());
		order.setOrderStatus(OrderConstant.ORDER_STATUS.RECEIPT.code);
		order.setDeliveredTime(new Date());
		order.setUpdateTime(new Date());
		order.setFastUserId(fastUserId);
		OrderStatusLog orderStatusLog = new OrderStatusLog();
		orderStatusLog.setContent("订单配送中。"+content);
		orderStatusLog.setCreateTime(new Date());
		orderStatusLog.setCreateUserId(updateUserId);
		orderStatusLog.setCreateUserName(updateUserName);
		orderStatusLog.setCreateUserType(logType.code);
		orderStatusLog.setCurrStatus(order.getOrderStatus());
		orderStatusLog.setName("配送");
		orderStatusLog.setOrderCode(orderCode);
		orderStatusLog.setParentOrderCode(subOrder.getParentOrderCode());
		orderStatusLog.setPreStatus(subOrder.getOrderStatus());
		orderStatusLog.setType(LOG_LEVEL.INFO.code);
		try{
			subOrderService.updateNotNullById(order);
			orderStatusLogService.insert(orderStatusLog);
			outputOrderService.exWarehouseService(orderDelivery);
			salesOrderRemoteService.sendMqMessageByDelivery(list);
		}catch(Throwable throwable){
			return new ResultInfo<Boolean>(ExceptionUtils.println(new FailInfo(throwable), logger, order,orderStatusLog));
		}
		return new ResultInfo<Boolean>(Boolean.TRUE);
	}

	/**
	 * 催单
	 * @param orderCode
	 * @return
	 */
	public ResultInfo<Boolean> urgeOrder(Long orderCode,Long memberId){
		SubOrder subOrder = findSubOrderByCode(orderCode);
		if(subOrder==null){
			return new ResultInfo<Boolean>(new FailInfo("没有查询到订单"));
		}
		if(!subOrder.getMemberId().equals(memberId)){
			return new ResultInfo<Boolean>(new FailInfo("你无权操作订单"));
		}
		if(!jedisCacheUtil.lock("order:urge:"+orderCode)){
			return new ResultInfo<Boolean>(new FailInfo("已催单，请休息会儿再试"));
		}
		OrderStatusLog orderStatusLog = new OrderStatusLog();
		orderStatusLog.setContent("已成功催促，店铺知晓，请稍等");
		orderStatusLog.setCreateTime(new Date());
		orderStatusLog.setCreateUserId(subOrder.getMemberId());
		orderStatusLog.setCreateUserName(subOrder.getAccountName());
		orderStatusLog.setCreateUserType(LOG_TYPE.MEMBER.code);
		orderStatusLog.setCurrStatus(subOrder.getOrderStatus());
		orderStatusLog.setName("客户催单");
		orderStatusLog.setOrderCode(orderCode);
		orderStatusLog.setParentOrderCode(subOrder.getParentOrderCode());
		orderStatusLog.setPreStatus(subOrder.getOrderStatus());
		orderStatusLog.setType(LOG_LEVEL.INFO.code);
		try{
			orderStatusLogService.insert(orderStatusLog);
			fastUserInfoService.sendUrgeOrderSms(subOrder.getWarehouseId());
		}catch(Throwable throwable){
			return new ResultInfo<Boolean>(ExceptionUtils.println(new FailInfo(throwable), logger, orderStatusLog));
		}
		return new ResultInfo<Boolean>(Boolean.TRUE);
	}
}
