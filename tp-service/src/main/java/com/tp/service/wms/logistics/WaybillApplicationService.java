package com.tp.service.wms.logistics;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.tp.common.dao.BaseDao;
import com.tp.common.vo.DAOConstant.MYBATIS_SPECIAL_STRING;
import com.tp.common.vo.wms.WmsWaybillConstant;
import com.tp.common.vo.wms.WmsConstant.ExpressCompany;
import com.tp.common.vo.wms.WmsWaybillConstant.WaybillStatus;
import com.tp.dao.wms.logistics.WaybillApplicationDao;
import com.tp.dao.wms.logistics.WaybillDetailDao;
import com.tp.dao.wms.logistics.WaybillDetailLogDao;
import com.tp.dto.common.FailInfo;
import com.tp.dto.common.ResultInfo;
import com.tp.model.wms.WaybillApplication;
import com.tp.model.wms.WaybillDetail;
import com.tp.model.wms.WaybillDetailLog;
import com.tp.redis.util.JedisCacheUtil;
import com.tp.service.BaseService;
import com.tp.service.wms.logistics.IWaybillApplicationService;
import com.tp.service.wms.thirdparty.IWaybillToTPService;
import com.tp.util.StringUtil;

@Service
public class WaybillApplicationService extends BaseService<WaybillApplication> implements IWaybillApplicationService {

	private static final Logger logger = LoggerFactory.getLogger(WaybillApplicationService.class);
	
	private static final String LOCK_KEY = "applyWaybillNoForOrder";
	
	@Autowired
	private WaybillApplicationDao waybillApplicationDao;
	
	@Autowired
	private WaybillDetailDao waybillDetailDao; 
	
	@Autowired
	private List<IWaybillToTPService> waybillServiceList;
	
	@Autowired
	private WaybillDetailLogDao waybillDetailLogDao;
	
	@Autowired
	private JedisCacheUtil jedisCacheUtil;
	
	@Override
	public BaseDao<WaybillApplication> getDao() {
		return waybillApplicationDao;
	}
	
	/**
	 * 向第三方快递公司批量申请运单号 
	 */
	@Transactional
	@Override
	public ResultInfo<Boolean> applyWaybills(ExpressCompany expressCompany, int count) {
		if (null == expressCompany) {
			return new ResultInfo<>(new FailInfo("物流公司为空"));
		}
		logger.info("[APPLY_WAYBILLS]向第三方物流公司批量申请运单号...[company={}]，[count={}]",
				expressCompany.getDesc(), count);
		if (count < 0 || count > WmsWaybillConstant.MAX_APPLY_WAYBILL_COUNT) {
			logger.error("[APPLY_WAYBILLS]申请运单数不合理:{}", count);
			return new ResultInfo<>(new FailInfo("申请运单数非法"));
		}
		WaybillApplication application = null;
		for (IWaybillToTPService service : waybillServiceList) {
			if (service.checkApplyWaybill(expressCompany, count)) {
				application = service.applyWaybill(expressCompany, count);
			}
		}		
		if (null == application || CollectionUtils.isEmpty(application.getDetails())) {
			logger.error("[APPLY_WAYBILLS]批量申请运单失败");
			return new ResultInfo<>(new FailInfo("申请运单失败"));
		}
		try {
			getDao().insert(application);
			for (WaybillDetail detail : application.getDetails()) {
				detail.setApplicationId(application.getId());
			}
			waybillDetailDao.insertDetails(application.getDetails());
		} catch (Exception e) {
			logger.error("[APPLY_WAYBILLS]申请运单号成功 - 保存异常");
			return new ResultInfo<>(new FailInfo("申请运单失败"));
		}
		return new ResultInfo<>(Boolean.TRUE);
	}

	/**
	 * 为订单申请运单号
	 * @param orderCode
	 * @return
	 */
	@Transactional
	@Override
	public ResultInfo<WaybillDetail> applyWaybillNoForOrder(String orderCode, ExpressCompany company) {
		if (StringUtil.isEmpty(orderCode)) {
			return new ResultInfo<>(new FailInfo("订单号为空"));
		}
		if (null == company) {
			return new ResultInfo<>(new FailInfo("快递公司为空"));
		}
		logger.info("[APPLY_WAYBILL_FOR_ORDER]申请运单号：[orderCode={}][company={}]", orderCode, company.getDesc());
		if (!jedisCacheUtil.lock(LOCK_KEY)) { //加锁
			logger.error("[APPLY_WAYBILL_FOR_ORDER]请求加锁失败：[lock_key={}]", LOCK_KEY);
			return new ResultInfo<WaybillDetail>(new FailInfo("请求失败"));
		}
		try {
			//检查订单号是否存在
			Map<String, Object> params = new HashMap<>();
			params.put("orderCode", orderCode);
			params.put("logisticsCode", company.getCommonCode());
			params.put(MYBATIS_SPECIAL_STRING.COLUMNS.name(), "status =" + WaybillStatus.USED.code);
			List<WaybillDetail> waybillDetailObjs = waybillDetailDao.queryByParamNotEmpty(params);
			if (CollectionUtils.isNotEmpty(waybillDetailObjs)) {
				WaybillDetail waybillDetail = waybillDetailObjs.get(0);
				logger.info("[APPLY_WAYBILL_FOR_ORDER][{}]订单已申请运单号：{}", 
						orderCode, JSONObject.toJSONString(waybillDetail));
				return new ResultInfo<>(waybillDetail);
			}
			params.clear();
			params.put("status", WaybillStatus.UNUSE.code);
			params.put("logisticsCode", company.getCommonCode());
			params.put(MYBATIS_SPECIAL_STRING.LIMIT.name(), 2);
			List<WaybillDetail> unusedDetails = waybillDetailDao.queryByParamNotEmpty(params);
			if (CollectionUtils.isEmpty(unusedDetails)) {  //此处应发送MQ消息批量申请运单号
				logger.error("[APPLY_WAYBILL_FOR_ORDER]可用运单号已用光，请批量申请运单号"); 
				return new ResultInfo<>(new FailInfo("可用运单号已使用完毕！"));
			}
			WaybillDetail selectWaybill = unusedDetails.get(0);
			selectWaybill.setOrderCode(orderCode);
			selectWaybill.setUpdateTime(new Date());
			selectWaybill.setStatus(WaybillStatus.USED.code);
			//日志
			WaybillDetailLog log = new WaybillDetailLog();
			log.setWaybillNo(selectWaybill.getWaybillNo());
			log.setOrderCode(selectWaybill.getOrderCode());
			log.setLogisticsCode(selectWaybill.getLogisticsCode());
			log.setPreStatus(WaybillStatus.UNUSE.code);
			log.setCurStatus(WaybillStatus.USED.code);
			log.setContent("订单" + selectWaybill.getOrderCode() + "申请使用该运单号");
			log.setCreateTime(new Date());
			log.setUpdateTime(new Date());
			waybillDetailDao.updateById(selectWaybill);
			waybillDetailLogDao.insert(log);
			return new ResultInfo<>(selectWaybill);
		} catch (Exception e) {
			logger.error("[APPLY_WAYBILL_FOR_ORDER][{}]为订单号分配运单号异常", orderCode, e);
			return new ResultInfo<>(new FailInfo("申请运单号异常"));
		} finally {
			jedisCacheUtil.unLock(LOCK_KEY); //解锁
			logger.info("[APPLY_WAYBILL_FOR_ORDER]解锁成功");
		}
	}	
	
	/**
	 * 查询物流公司未使用运单号
	 */
	@Override
	public ResultInfo<Integer> queryUnUsedWaybillNoCount(ExpressCompany company){
		if (null == company) {
			return new ResultInfo<>(new FailInfo("物流公司为空"));
		}
		try {
			logger.info("[QUERY_UNUSED_WAYBILL][{}]查询快递公司剩余未使用运单号", company.getDesc());
			Map<String, Object> params = new HashMap<>();
			params.put("logisticsCode", company.getCommonCode());
			params.put("status", WaybillStatus.UNUSE);
			return new ResultInfo<>(waybillDetailDao.queryByParamNotEmptyCount(params));
		} catch (Exception e) {
			logger.error("[QUERY_UNUSED_WAYBILL][{}]查询快递公司剩余未使用运单号异常", company.getDesc(), e);
			return new ResultInfo<>(new FailInfo("查看物流公司未使用运单号异常"));
		}
	}

	/** 归还运单号 */
	@Transactional
	@Override
	public ResultInfo<Boolean> waybillNoBack(String waybillNo) {
		if (null == waybillNo) {
			return new ResultInfo<>(new FailInfo("运单号为空"));
		}
		logger.info("[WAYBILL_BACK][{}]归还运单号", waybillNo);
		try {
			Map<String, Object> params = new HashMap<>();
			params.put("waybillNo", waybillNo);
			List<WaybillDetail> details = waybillDetailDao.queryByParamNotEmpty(params);
			if (CollectionUtils.isEmpty(details)) {
				logger.error("[WAYBILL_BACK][{}]运单对应的运单使用信息不存在", waybillNo);
				return new ResultInfo<>(new FailInfo("运单信息不存在"));
			}
			WaybillDetail detail = details.get(0);
			if (!WaybillStatus.USED.code.equals(detail.getStatus())) {
				logger.error("[WAYBILL_BACK][{}]运单未被使用或者已被禁用", waybillNo);
				return new ResultInfo<>(new FailInfo("运单未被使用或者已被禁用"));
			}
			detail.setStatus(WaybillStatus.UNUSE.code);
			detail.setOrderCode("");
			//日志
			WaybillDetailLog log = new WaybillDetailLog();
			log.setWaybillNo(detail.getWaybillNo());
			log.setOrderCode(detail.getOrderCode());
			log.setLogisticsCode(detail.getLogisticsCode());
			log.setPreStatus(WaybillStatus.USED.code);
			log.setCurStatus(WaybillStatus.UNUSE.code);
			log.setContent("订单" + detail.getOrderCode() + "归还该运单号");
			log.setCreateTime(new Date());
			log.setUpdateTime(new Date());
			waybillDetailDao.updateById(detail);
			waybillDetailLogDao.insert(log);
			logger.info("[WAYBILL_BACK][{}]归还运单号成功", waybillNo);
			return new ResultInfo<>(Boolean.TRUE);
		} catch (Exception e) {
			logger.error("[WAYBILL_BACK][{}]归还运单号异常", waybillNo, e);
			return new ResultInfo<>(new FailInfo("归还运单号异常"));
		}
	}
}
