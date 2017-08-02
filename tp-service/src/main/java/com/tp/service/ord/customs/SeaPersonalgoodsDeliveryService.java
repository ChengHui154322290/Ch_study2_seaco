/**
 * 
 */
package com.tp.service.ord.customs;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.vo.OrderConstant.DeclareCustoms;
import com.tp.common.vo.OrderConstant.PutCustomsStatus;
import com.tp.common.vo.OrderConstant.PutCustomsType;
import com.tp.common.vo.bse.ClearanceChannelsEnum;
import com.tp.common.vo.wms.WmsConstant.ExpressCompany;
import com.tp.dto.common.FailInfo;
import com.tp.dto.common.ResultInfo;
import com.tp.model.ord.CustomsClearanceLog;
import com.tp.model.ord.PersonalgoodsDeclareInfo;
import com.tp.model.ord.SubOrder;
import com.tp.model.stg.Warehouse;
import com.tp.model.wms.WaybillDetail;
import com.tp.redis.util.JedisDBUtil;
import com.tp.service.ord.ICustomsClearanceLogService;
import com.tp.service.ord.IPersonalgoodsDeclareInfoService;
import com.tp.service.ord.ISubOrderService;
import com.tp.service.ord.customs.ISeaCustomsInfoDeliveryService;
import com.tp.service.ord.customs.JKF.IJKFDeclarePersonalGoodsLocalService;
import com.tp.service.stg.IWarehouseService;
import com.tp.service.wms.logistics.IWaybillApplicationService;

/**
 * @author Administrator
 * 清关单申报
 */
@Service
public class SeaPersonalgoodsDeliveryService implements ISeaCustomsInfoDeliveryService{

	private static final Logger logger = LoggerFactory.getLogger(SeaPersonalgoodsDeliveryService.class);
	private static final String DATE_PATTERN = "yyMMddHH";
	/** 索引字符串长度 */
	private static final Integer INDEX_STRING_LENGTH = 8;
	/** 编号长度 */
	private static final Integer CODE_LENGTH = 16;
	/** index **/
	public static final String INDEX_KEY = "pgd_index_key";
	
	@Autowired
	private ISubOrderService subOrderService;
	
	@Autowired
	private IPersonalgoodsDeclareInfoService pgDeclareInfoService;
	
	@Autowired
	private IJKFDeclarePersonalGoodsLocalService jkfDeclarePersonalGoodsLocalService;
	
	@Autowired
	private ICustomsClearanceLogService customsClearanceLogService;
	
	@Autowired
	private IWarehouseService warehouseService;
	
	@Autowired
	private IWaybillApplicationService waybillApplicationService;
	
	@Autowired
	private JedisDBUtil jedisDBUtil;
	
	@Override
	public ResultInfo<Boolean> delivery(SubOrder subOrder) {
		ResultInfo<Boolean> pushResult = null;	
		Long orderCode = subOrder.getOrderCode();
		if (ClearanceChannelsEnum.HANGZHOU.id.equals(subOrder.getSeaChannel())) { //杭州保税区
			PersonalgoodsDeclareInfo personalgoodsDeclareInfo = 
					pgDeclareInfoService.queryUniquePersonalGoodsDeclByOrderCode(subOrder.getOrderCode().toString());
			pushResult = jkfDeclarePersonalGoodsLocalService.pushPersonalGoodsInfoToJKF(personalgoodsDeclareInfo, subOrder);
		}else{
			logger.error("[PUSH_PERSONALGOODS_INFO][{}]不支持{}申报", orderCode, subOrder.getSeaChannelName());
			return new ResultInfo<>(new FailInfo("订单所在保税区不支持申报"));
		}
		//日志
		
		PutCustomsStatus putStatus = pushResult.isSuccess() ? PutCustomsStatus.SUCCESS : PutCustomsStatus.FAIL;
		logger.error("[PUSH_PERSONALGOODS_INFO][{}]推清关单结果：{}", orderCode, putStatus.getDesc());
		
		updateSubOrderWithDeliveryStatus(subOrder, putStatus);
		customsClearanceLogService.insert(createCustomsClearanceLog(pushResult, subOrder));
		logger.info("[PUSH_PERSONALGOODS_INFO][{}]清关单推送成功", orderCode);
		return pushResult;
	}

	@Override
	public boolean checkDelivery(SubOrder subOrder) {
		Integer putPgStatus = subOrder.getPutPersonalgoodsStatus();
		if (!subOrder.getPutCleanOrder()){
			logger.error("[PUSH_PERSONALGOODS_INFO][{}]清单不需要推送", subOrder.getOrderCode());
			return false;
		}
		if (PutCustomsStatus.isSuccess(putPgStatus)) {
			logger.info("[PUSH_PERSONALGOODS_INFO][{}]清关单不需要重复推送", subOrder.getOrderCode());
			return false;
		}
		return true;
	}

	@Override
	public ResultInfo<Boolean> prepareDelivery(SubOrder subOrder) {
		Long orderCode = subOrder.getOrderCode();
//		if (false == PutCustomsStatus.isInit(subOrder.getPutPersonalgoodsStatus())){
//			logger.info("[PUSH_PERSONALGOODS_INFO][]清关单状态未初始化", orderCode);
//			return new ResultInfo<>(new FailInfo("清关单推送状态未初始化"));
//		}
		//杭州保税区
		Long seaChannelId = subOrder.getSeaChannel();
		if (!ClearanceChannelsEnum.HANGZHOU.id.equals(seaChannelId)) {
			return new ResultInfo<>(new FailInfo("非杭州保税区订单不推送"));
		}
		
		//检查清关单
		PersonalgoodsDeclareInfo pgDeclareInfo = getPersonalgoodsDeclareInfo(subOrder);		
		if (pgDeclareInfo == null){
			return new ResultInfo<>(new FailInfo("清单数据不存在"));
		}
		//校验直邮逻辑
		if (pgDeclareInfo.getImportType() != null && pgDeclareInfo.getImportType() == 0){//直邮
			boolean canDelivery = StringUtils.isNotEmpty(pgDeclareInfo.getBillNo()) 
					&& StringUtils.isNotEmpty(pgDeclareInfo.getVoyageNo())
					&& StringUtils.isNotEmpty(pgDeclareInfo.getTrafNo());
			if (canDelivery == false){
				logger.error("直邮订单未确定航班号等信息，暂不能推送");
				return new ResultInfo<>(new FailInfo("直邮订单未确定航班号等信息，暂不能推送"));
			}
		}		
		return new ResultInfo<>(Boolean.TRUE);
	}
	
	private void updateSubOrderWithDeliveryStatus(SubOrder subOrder, PutCustomsStatus status){
		SubOrder so = new SubOrder();
		so.setPutPersonalgoodsStatus(status.code);
		so.setId(subOrder.getId());
		subOrderService.updateNotNullById(so);
	}
	
	private CustomsClearanceLog createCustomsClearanceLog(ResultInfo<Boolean> pushResult, SubOrder subOrder){
		CustomsClearanceLog log = new CustomsClearanceLog();
		log.setCustomsCode("");
		log.setOrderCode(subOrder.getOrderCode());
		log.setType(PutCustomsType.PERSONALGOODS_DECLARE.getIndex());
		log.setResult(pushResult.isSuccess() == true ? 1 : 0);
		log.setResultDesc(pushResult.isSuccess() == true ? "推送成功":pushResult.getMsg().getMessage());
		log.setCreateTime(new Date());
		return log;
	}
	
	private PersonalgoodsDeclareInfo getPersonalgoodsDeclareInfo(SubOrder subOrder){
		PersonalgoodsDeclareInfo pgDeclareInfo = 
				pgDeclareInfoService.queryUniquePersonalGoodsDeclByOrderCode(subOrder.getOrderCode().toString());
		if (pgDeclareInfo == null){
			pgDeclareInfo = createPersonalgoodsDeclareInfo(subOrder);
		}
		return pgDeclareInfo;
	}
	
	/** 生成清单 */
	private PersonalgoodsDeclareInfo createPersonalgoodsDeclareInfo(SubOrder subOrder) {
		Long orderCode = subOrder.getOrderCode();
		Long warehouseId = subOrder.getWarehouseId();	
		Warehouse warehouse = warehouseService.queryById(warehouseId);
		if (warehouse == null) {
			logger.error("未查询到仓库数据：{}", warehouseId);
			return null;
		}
		ExpressCompany expressCompany = getLogisticsInfo(warehouse);
		if (expressCompany == null) {
			logger.error("[orderCode={}]获取物流公司失败", orderCode);
			return null;
		}
		
		String waybillNo = applyWaybillNo(expressCompany, orderCode);
		if (StringUtils.isEmpty(waybillNo)) {
			logger.error("[orderCode={}]申请运单号失败", orderCode);
			return null;
		}
		
		String serialNo = generateSerialNo(subOrder);
		if (StringUtils.isEmpty(serialNo)){
			logger.error("[orderCode={}]生成清关单流水号失败", orderCode);
			return null;
		}
		
		PersonalgoodsDeclareInfo pgInfo = initPersonalgoodsDeclareInfo(subOrder, expressCompany, waybillNo, serialNo, warehouse);
		if (pgInfo == null){
			logger.error("[orderCode={}]初始化清关单失败", orderCode);
			return null;
		}
		//插入数据
		pgDeclareInfoService.insert(pgInfo);
		return pgInfo;
	}
	
	/** 获取物流信息 */
	private ExpressCompany getLogisticsInfo(Warehouse warehouse){
		if (StringUtils.isEmpty(warehouse.getLogistics())) {
			return null;
		}
		return ExpressCompany.getCompanyByCommonCode(warehouse.getLogistics());
	}
	
	/** 申请运单号 */
	private String applyWaybillNo(ExpressCompany company, Long orderCode){
		ResultInfo<WaybillDetail> resultWaybill = 
				waybillApplicationService.applyWaybillNoForOrder(orderCode.toString(), company);
		if (Boolean.TRUE != resultWaybill.isSuccess() || null == resultWaybill.getData()) {
			logger.error("订单{}申请运单号失败", orderCode);
			return null;
		}
		return resultWaybill.getData().getWaybillNo().toString();
	}
	
	/** 创建清关单数据 */
	private PersonalgoodsDeclareInfo initPersonalgoodsDeclareInfo(SubOrder subOrder,
			ExpressCompany expressCompany, String waybillNo, String serialNo, Warehouse warehouse) {
		Long orderCode = subOrder.getOrderCode();
		Long channelId = subOrder.getSeaChannel();
		PersonalgoodsDeclareInfo declareInfo = new PersonalgoodsDeclareInfo();
		declareInfo.setOrderCode(subOrder.getOrderCode());
		declareInfo.setDeclareCustoms(DeclareCustoms.getCodeByChannelId(channelId));
		declareInfo.setDeclareNo(orderCode.toString());
		declareInfo.setPreEntryNo(serialNo);
		declareInfo.setCompanyNo(expressCompany.commonCode);
		declareInfo.setCompanyName(expressCompany.desc);
		declareInfo.setExpressNo(waybillNo);
		//直邮参数
		declareInfo.setImportType(warehouse.getImportType());
		
		declareInfo.setCreateTime(new Date());
		declareInfo.setUpdateTime(new Date());
		return declareInfo;
	}
	
	/** 生成清单流水号 */
	private String generateSerialNo(SubOrder subOrder){
		Long channelId = subOrder.getSeaChannel();
		if (ClearanceChannelsEnum.HANGZHOU.id.equals(channelId)){
			return jkfDeclarePersonalGoodsLocalService.getPreEntryNo();
		}else{
	    	String dateStr = dateString();
	    	String indexStr = indexString();
	    	StringBuilder sb = new StringBuilder(CODE_LENGTH);
	    	return sb.append(dateStr).append(indexStr).toString();
		}
	}
	
	/**
	 * 日期字符串
	 */
	private String dateString() {
		Calendar currentTime = Calendar.getInstance();
		return new SimpleDateFormat(DATE_PATTERN).format(currentTime.getTime());
	}
	/**
	 * 自增码
	 */
	private String indexString() {
		Long index = jedisDBUtil.incr(INDEX_KEY);
		if (null == index) {
			logger.error("生成个人物品申报序列编号异常：redis服务器获取自增值为空");
			index = System.currentTimeMillis();
		}
		String idxStr = index.toString();
		int len = idxStr.length();
		StringBuilder sb = new StringBuilder(idxStr);
		if (len < INDEX_STRING_LENGTH) {
			return String.format("%08d", index);
		} else {
			return sb.delete(0,sb.length()-INDEX_STRING_LENGTH).toString();
		}
	}

	@Override
	public boolean checkPutCustomsType(PutCustomsType type) {
		if (PutCustomsType.PERSONALGOODS_DECLARE == type){
			return true;
		}
		return false;
	}
}
