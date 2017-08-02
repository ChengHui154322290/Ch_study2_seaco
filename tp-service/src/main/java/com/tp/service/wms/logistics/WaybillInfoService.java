package com.tp.service.wms.logistics;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tp.common.dao.BaseDao;
import com.tp.common.vo.wms.WmsWaybillConstant;
import com.tp.common.vo.wms.WmsWaybillConstant.PutStatus;
import com.tp.dao.wms.logistics.WaybillInfoDao;
import com.tp.dto.common.FailInfo;
import com.tp.dto.common.ResultInfo;
import com.tp.dto.wms.logistics.WaybillInfoDto;
import com.tp.dto.wms.logistics.WaybillItemInfoDto;
import com.tp.model.wms.WaybillInfo;
import com.tp.service.BaseService;
import com.tp.service.wms.logistics.IWaybillInfoService;
import com.tp.service.wms.thirdparty.IWaybillToTPService;
import com.tp.util.StringUtil;

@Service
public class WaybillInfoService extends BaseService<WaybillInfo> implements IWaybillInfoService {

	private static final Logger logger = LoggerFactory.getLogger(WaybillInfoService.class);
	
	@Autowired
	private List<IWaybillToTPService> waybillServiceList;
	
	@Autowired
	private WaybillInfoDao waybillInfoDao;
	
	@Override
	public BaseDao<WaybillInfo> getDao() {
		return waybillInfoDao;
	}

	@Override
	public ResultInfo<Boolean> deliverWaybillInfo(WaybillInfoDto dto) {
		ResultInfo<Boolean> message = validateWaybill(dto); 
		if (!message.isSuccess()) {
			logger.error("[PUT_WAYBILL_INFO][{}][expressNo={}]数据错误：{}", 
					dto.getOrderCode(), dto.getWaybillNo(), message.getMsg().getDetailMessage());
			return message;
		}		
		logger.info("[PUT_WAYBILL_INFO][{}]运单号{}推送到{}", dto.getOrderCode(), dto.getWaybillNo(), dto.getLogisticsName());
		return pushWaybillInfo(dto);
	}
	
	//推送数据至快递
	@Transactional
	private ResultInfo<Boolean> pushWaybillInfo(WaybillInfoDto dto){
		ResultInfo<Boolean> message = null;
		try {
			Map<String, Object> params = new HashMap<>();
			params.put("orderCode", dto.getOrderCode());
			List<WaybillInfo> waybillInfos = getDao().queryByParamNotEmpty(params);
			WaybillInfo waybillInfo = convertToWaybillInfo(dto);
			if (CollectionUtils.isNotEmpty(waybillInfos)) {
				waybillInfo.setId(waybillInfos.get(0).getId());
				getDao().updateNotNullById(waybillInfo);
				message = pushWaybillInfoToThirdparty(waybillInfo); 
			}else{
				getDao().insert(waybillInfo);
				message = pushWaybillInfoToThirdparty(waybillInfo);
			}
		} catch (Exception e) {
			logger.error("推送运单信息异常", e);
			message = new ResultInfo<>(new FailInfo("推送运单信息异常"));
		}
		return message;
	}
 	
	
	private ResultInfo<Boolean> pushWaybillInfoToThirdparty(WaybillInfo info){		
		if (WmsWaybillConstant.PutStatus.SUCCESS.code.equals(info.getStatus())) {
			logger.error("[PUT_WAYBILL_INFO][{}]运单号{}已推送成功，不需要重复推送", info.getOrderCode(), info.getWaybillNo());
			return new ResultInfo<>(Boolean.TRUE);
		}
		
		int failTimes = info.getFailTimes() == null ? 0 : info.getFailTimes();
		if (failTimes > WmsWaybillConstant.MAX_PUT_TIMES) {
			logger.error("[PUT_WAYBILL_INFO][{}]运单号{}已推送次数超过最大次数", info.getOrderCode(), info.getWaybillNo());
			return new ResultInfo<>(new FailInfo("订单" + info.getOrderCode() + "已推送" + failTimes + "次;超过最大推送次数" + WmsWaybillConstant.MAX_PUT_TIMES));
		}
		//推送
		for (IWaybillToTPService service : waybillServiceList) {
			if (service.checkPushWaybillInfo(info)) {
				return service.pushWaybillInfo(info);
			}
		}
		return new ResultInfo<>(new FailInfo("不支持快递公司"));
	}
	
	/**
	 * 参数转换 
	 */
	private WaybillInfo convertToWaybillInfo(WaybillInfoDto dto){
		WaybillInfo waybillInfo = new WaybillInfo();
		//基本信息
		waybillInfo.setOrderCode(dto.getOrderCode());
		waybillInfo.setType(dto.getWaybillType().code);
		waybillInfo.setGrossWeight(dto.getGrossWeight());
		waybillInfo.setNetWeight(dto.getNetWeight());
		waybillInfo.setLogisticsCode(dto.getLogisticsCode());
		waybillInfo.setLogisticsName(dto.getLogisticsName());
		waybillInfo.setWaybillNo(dto.getWaybillNo());
		waybillInfo.setIsDeliveryPay(1);
		waybillInfo.setIsPostagePay(1);
		
		//收件人信息
		waybillInfo.setConsignee(dto.getConsignee());
		waybillInfo.setProvince(dto.getProvince());
		waybillInfo.setCity(dto.getCity());
		waybillInfo.setArea(dto.getArea());
		waybillInfo.setAddress(dto.getAddress());
		waybillInfo.setMobile(dto.getMobile());
		
		//发件人信息
		waybillInfo.setSendProvince(dto.getSendProvince());
		waybillInfo.setSendCity(dto.getSendCity());
		waybillInfo.setSendArea(dto.getSendArea());
		waybillInfo.setSendAddress(dto.getSendAddress());
		waybillInfo.setSendRoughArea(dto.getSendRoughArea());
		waybillInfo.setSendName(dto.getSenderName());
		waybillInfo.setSendCompany(dto.getSenderCompany());
		waybillInfo.setSendTel(dto.getSenderTel());
		
		//商品信息
		Integer totalQuantity = 0;
		Double worth = 0.0;
		for (WaybillItemInfoDto itemInfo : dto.getDetails()) {
			totalQuantity += itemInfo.getQuantity();
			worth += BigDecimal.valueOf(itemInfo.getSalesprice()).multiply(BigDecimal.valueOf(itemInfo.getQuantity())).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
		}
		waybillInfo.setWorth(worth.toString());
		waybillInfo.setPackAmount(totalQuantity.toString());
		waybillInfo.setMainGoodsName(dto.getDetails().get(0).getItemName());
		
		//其他
		waybillInfo.setStatus(PutStatus.FAIL.code);
		waybillInfo.setFailTimes(0);
		waybillInfo.setErrorMsg("");
		
		//直邮信息
		waybillInfo.setImportType(dto.getImportType());
		waybillInfo.setVoyageNo(dto.getVoyageNo());
		waybillInfo.setDeliveryNo(dto.getDeliveryNo());
		
		return waybillInfo;
	}
	
	/**
	 * 验证参数 
	 */
	private ResultInfo<Boolean> validateWaybill(WaybillInfoDto dto){
		//运单基本信息
		if (StringUtil.isEmpty(dto.getOrderCode())) {
			return new ResultInfo<>(new FailInfo("订单编号为空"));
		}
		if (StringUtil.isEmpty(dto.getWaybillNo())) {
			return new ResultInfo<>(new FailInfo("订单编号为空"));
		}
		if (null == dto.getOrderType()) {
			return new ResultInfo<>(new FailInfo("订单类型为空"));
		}
		if (null == dto.getWaybillType()) {
			return new ResultInfo<>(new FailInfo("运单类型为空"));
		}
		if (StringUtil.isEmpty(dto.getGrossWeight())) {
			return new ResultInfo<>(new FailInfo("包裹毛重为空"));
		}
		if (StringUtil.isEmpty(dto.getNetWeight())) {
			return new ResultInfo<>(new FailInfo("包裹净重为空"));
		}
		if (CollectionUtils.isEmpty(dto.getDetails())) {
			return new ResultInfo<>(new FailInfo("订单商品详情为空"));
		}
		if (StringUtil.isEmpty(dto.getSendRoughArea())) {
			return new ResultInfo<>(new FailInfo("模糊发货地址为空"));
		}
		if (StringUtil.isEmpty(dto.getLogisticsCode())) {
			return new ResultInfo<>(new FailInfo("物流企业编号为空"));
		}
		if (StringUtil.isEmpty(dto.getLogisticsName())) {
			return new ResultInfo<>(new FailInfo("物流企业名称为空"));
		}
		//收件人信息校验
		if (StringUtil.isEmpty(dto.getConsignee())) {
			return new ResultInfo<>(new FailInfo("收件人姓名为空"));
		}
		if (StringUtil.isEmpty(dto.getAddress())) {
			return new ResultInfo<>(new FailInfo("收件人地址为空"));
		}
		if (StringUtil.isEmpty(dto.getProvince())) {
			return new ResultInfo<>(new FailInfo("收件人省份为空"));
		}
		if (StringUtil.isEmpty(dto.getCity())) {
			return new ResultInfo<>(new FailInfo("收件人城市为空"));
		}
		if (StringUtil.isEmpty(dto.getArea())) {
			return new ResultInfo<>(new FailInfo("收件人区域为空"));
		}
		//商品信息
		for (WaybillItemInfoDto itemInfo : dto.getDetails()) {
			if (StringUtil.isEmpty(itemInfo.getItemName())) {
				return new ResultInfo<>(new FailInfo("商品名称为空"));
			}
			if(null == itemInfo.getSalesprice()){
				return new ResultInfo<>(new FailInfo("商品售价为空"));
			}
			if (null == itemInfo.getQuantity()) {
				return new ResultInfo<>(new FailInfo("商品数量为空"));
			}
		}
		//直邮信息校验
		if (dto.getImportType() != null && dto.getImportType() == 0){
			if (StringUtil.isEmpty(dto.getDeliveryNo())){
				return new ResultInfo<>(new FailInfo("总提运单号为空"));
			}
		}
		return new ResultInfo<>(Boolean.TRUE);
	}
}
