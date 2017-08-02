package com.tp.proxy.wms.logistics;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.vo.wms.WmsWaybillConstant.WaybillStatus;
import com.tp.dto.common.FailInfo;
import com.tp.dto.common.ResultInfo;
import com.tp.model.wms.WaybillDetail;
import com.tp.model.wms.WaybillDetailLog;
import com.tp.proxy.BaseProxy;
import com.tp.service.IBaseService;
import com.tp.service.wms.logistics.IWaybillDetailLogService;
import com.tp.service.wms.logistics.IWaybillDetailService;
import com.tp.util.StringUtil;
/**
 * 运单号使用详情表代理层
 * @author szy
 *
 */
@Service
public class WaybillDetailProxy extends BaseProxy<WaybillDetail>{

	@Autowired
	private IWaybillDetailService waybillDetailService;
	
	@Autowired
	private IWaybillDetailLogService waybillDetailLogService;

	@Override
	public IBaseService<WaybillDetail> getService() {
		return waybillDetailService;
	}
	
	/** 绑定运单号和订单号 */
	public ResultInfo<Boolean> bindWaybillNo(String waybillNo, String orderCode, String userName){
		if (StringUtil.isEmpty(waybillNo)) {
			return new ResultInfo<>(new FailInfo("运单号为空"));
		}
		if (StringUtil.isEmpty(orderCode)) {
			return new ResultInfo<>(new FailInfo("订单号为空"));
		}
		Map<String, Object> params = new HashMap<>();
		params.put("waybillNo", waybillNo);
		ResultInfo<WaybillDetail> waybillResult = queryUniqueByParams(params);
		if (!waybillResult.isSuccess()) {
			return new ResultInfo<>(new FailInfo("绑定失败"));
		}
		WaybillDetail waybillDetail = waybillResult.getData();
		if (null == waybillDetail) {
			return new ResultInfo<>(new FailInfo("运单号信息不存在"));
		}
		if (!WaybillStatus.UNUSE.code.equals(waybillDetail.getStatus())) {
			return new ResultInfo<>(new FailInfo("运单号已被使用或者被废弃"));
		}
		waybillDetail.setOrderCode(orderCode);
		waybillDetail.setStatus(WaybillStatus.USED.code);
		waybillDetail.setUpdateTime(new Date());
		waybillDetail.setRemark("手动绑定");
		updateById(waybillDetail);
		
		//日志
		WaybillDetailLog log = new WaybillDetailLog();
		log.setWaybillNo(waybillDetail.getWaybillNo());
		log.setOrderCode(waybillDetail.getOrderCode());
		log.setLogisticsCode(waybillDetail.getLogisticsCode());
		log.setPreStatus(WaybillStatus.UNUSE.code);
		log.setCurStatus(WaybillStatus.USED.code);
		log.setContent("订单" + waybillDetail.getOrderCode() + "绑定运单号" + waybillNo + ";操作者：" + userName);
		log.setCreateTime(new Date());
		log.setUpdateTime(new Date());
		waybillDetailLogService.insert(log);
		return new ResultInfo<>(Boolean.TRUE);
	}
	
	/** 解除绑定（回收） */
	public ResultInfo<Boolean> unBindWaybillNo(String waybillNo, String orderCode, String userName){
		if (StringUtil.isEmpty(waybillNo)) {
			return new ResultInfo<>(new FailInfo("运单号为空"));
		}
		if (StringUtil.isEmpty(orderCode)) {
			return new ResultInfo<>(new FailInfo("订单号为空"));
		}
		WaybillDetail detail = new WaybillDetail();
		detail.setWaybillNo(Long.valueOf(waybillNo));
		detail.setOrderCode(orderCode);
		ResultInfo<WaybillDetail> waybillResult = queryUniqueByObject(detail);
		WaybillDetail waybillDetail = waybillResult.getData();
		if (waybillDetail == null) {
			return new ResultInfo<>(new FailInfo("不存在运单信息"));
		}
		if (!WaybillStatus.USED.code.equals(waybillDetail.getStatus())) {
			return new ResultInfo<>(new FailInfo("运单号被禁用或者数据错误"));
		}
		waybillDetail.setOrderCode("");
		waybillDetail.setStatus(WaybillStatus.UNUSE.code);
		waybillDetail.setUpdateTime(new Date());
		waybillDetail.setRemark("手动解除绑定");
		updateById(waybillDetail);
		
		//日志
		WaybillDetailLog log = new WaybillDetailLog();
		log.setWaybillNo(waybillDetail.getWaybillNo());
		log.setOrderCode(waybillDetail.getOrderCode());
		log.setLogisticsCode(waybillDetail.getLogisticsCode());
		log.setPreStatus(WaybillStatus.USED.code);
		log.setCurStatus(WaybillStatus.UNUSE.code);
		log.setContent("订单" + waybillDetail.getOrderCode() + "解除绑定运单号" + waybillNo + ";操作者:" + userName);
		log.setCreateTime(new Date());
		log.setUpdateTime(new Date());
		waybillDetailLogService.insert(log);
		return new ResultInfo<>(Boolean.TRUE);
	}
	
	
	/** 作废 */	
	public ResultInfo<Boolean> abandon(String waybillNo, String userName){
		Map<String, Object> params = new HashMap<>();
		params.put("waybillNo", waybillNo);
		WaybillDetail waybillDetail = waybillDetailService.queryUniqueByParams(params);
		if (null == waybillDetail) {
			return new ResultInfo<>(new FailInfo("运单号信息不存在"));
		}
		if (!WaybillStatus.UNUSE.code.equals(waybillDetail.getStatus())) {
			return new ResultInfo<>(new FailInfo("运单号已被绑定或者废弃"));
		}
		WaybillDetail wd = new WaybillDetail();
		wd.setId(waybillDetail.getId());
		wd.setStatus(WaybillStatus.ABANDONED.code);
		waybillDetailService.updateNotNullById(wd);
		
		//日志
		WaybillDetailLog log = new WaybillDetailLog();
		log.setWaybillNo(waybillDetail.getWaybillNo());
		log.setOrderCode(waybillDetail.getOrderCode());
		log.setLogisticsCode(waybillDetail.getLogisticsCode());
		log.setPreStatus(wd.getStatus());
		log.setCurStatus(WaybillStatus.ABANDONED.code);
		log.setContent("废弃运单号" + waybillNo + ";操作者:" + userName);
		log.setCreateTime(new Date());
		log.setUpdateTime(new Date());
		waybillDetailLogService.insert(log);
		return new ResultInfo<>(Boolean.TRUE);
	}
}
