package com.tp.m.ao.order;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.dto.common.ResultInfo;
import com.tp.m.base.MResultVO;
import com.tp.m.convert.OrderConvert;
import com.tp.m.enums.MResultInfo;
import com.tp.m.exception.MobileException;
import com.tp.m.query.order.QueryLogistic;
import com.tp.m.util.StringUtil;
import com.tp.m.vo.order.LogisticCompanyVO;
import com.tp.m.vo.order.LogisticVO;
import com.tp.model.bse.ExpressInfo;
import com.tp.proxy.bse.ExpressCodeInfoProxy;
import com.tp.proxy.ord.OrderInfoProxy;
import com.tp.result.ord.SubOrderExpressInfoDTO;

/**
 * 物流业务层
 * @author zhuss
 * @2016年1月7日 下午9:02:57
 */
@Service
public class LogisticAO {

	private static final Logger log = LoggerFactory.getLogger(LogisticAO.class);
	
	@Autowired
	private OrderInfoProxy orderInfoProxy;
	
	@Autowired
	private ExpressCodeInfoProxy expressCodeInfoProxy;
	
	/**
	 * 订单跟踪
	 * @param order
	 * @return
	 */
	public MResultVO<LogisticVO> getLogisticList(QueryLogistic logistic){
		try{
			ResultInfo<List<SubOrderExpressInfoDTO>> express = orderInfoProxy.queryExpressLogInfoByUser(logistic.getUserid(), StringUtil.getLongByStr(logistic.getCode()), logistic.getLogisticcode());
			if(express.isSuccess()){
				List<SubOrderExpressInfoDTO> expresslist = express.getData();
				if(CollectionUtils.isNotEmpty(expresslist)){
					return new MResultVO<>(MResultInfo.SUCCESS,OrderConvert.convertLogistic(expresslist.get(0)));
				}
				return new MResultVO<>(MResultInfo.SUCCESS);
			}
			log.error("[调用Service接口 - 订单跟踪(queryExpressLogInfoByUser) Failed] = {}",express.getMsg().toString());
			return new MResultVO<>(express.getMsg().getMessage());
		}catch (MobileException e) {
			log.error("[API接口 - 订单跟踪  MobileException]={}", e.getMessage());
			return new MResultVO<>(e);
		}catch(Exception ex){
			log.error("[API接口 - 订单跟踪 Exception] = {}",ex);
			return new MResultVO<>(MResultInfo.SYSTEM_ERROR);
		}
	}
	
	/**
	 * 获取物流公司列表
	 * @return
	 */
	public MResultVO<List<LogisticCompanyVO>> getLogisticCompany(){
		try{
			ResultInfo<List<ExpressInfo>> result = expressCodeInfoProxy.selectAllExpressCode();
			if(result.success){
				return new MResultVO<>(MResultInfo.SUCCESS,OrderConvert.convertLogisticCompany(result.getData()));
			}
			log.error("[调用Service接口 - 获取物流公司列表(selectAllExpressCode) Failed] = {}",result.getMsg().toString());
			return new MResultVO<>(result.getMsg().getMessage());
		}catch(Exception e){
			log.error("[API接口 - 获取物流公司列表 Exception] = {}",e.getMessage());
			return new MResultVO<>(MResultInfo.SYSTEM_ERROR);
		}
	}
}
