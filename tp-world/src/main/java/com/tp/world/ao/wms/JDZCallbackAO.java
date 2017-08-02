package com.tp.world.ao.wms;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.tp.common.vo.wms.JDZWmsConstant;
import com.tp.dto.common.ResultInfo;
import com.tp.dto.wms.jdz.JDZStockoutBackDto;
import com.tp.dto.wms.jdz.JDZStockoutBackItemDto;
import com.tp.m.util.DateUtil;
import com.tp.model.wms.StockoutBack;
import com.tp.model.wms.StockoutBackDetail;
import com.tp.proxy.wms.StockoutBackProxy;
import com.tp.result.wms.ResultMessage;
import com.tp.util.StringUtil;

@Service
public class JDZCallbackAO {

	private static final Logger logger = LoggerFactory.getLogger(JDZCallbackAO.class);
	
	@Value("#{meta['JDZ.appKey']}")
	private String appKey;
	
	@Value("#{meta['JDZ.password']}")
	private String password;
	
	@Value("#{meta['JDZ.isEncrypt']}")
	private boolean isEncrypt;
	
	@Value("#{meta['JDZ.providerCode']}")
	private String providerCode;
	
	@Autowired
	private StockoutBackProxy stockoutBackProxy;
	
	/**
	 * 出库单回执 
	 * @param dto
	 * @return
	 */
	public ResultMessage stockoutCallback(JDZStockoutBackDto dto){
		if (null == dto) return new ResultMessage(false, "系统异常");
		if(!validate(dto)) return new ResultMessage(false, "系统异常");
		try {
			StockoutBack stockoutBack = convertToStockoutBack(dto);
			ResultInfo<StockoutBack> queryResult = stockoutBackProxy.queryStockoutBackByOrderCode(dto.getOrderCode());
			if (!queryResult.isSuccess()) {
				logger.error("查询出库单回执失败:{}", queryResult.getMsg().getDetailMessage());
				return new ResultMessage(false, "系统异常");
			}
			if (null != queryResult.getData()) {
				logger.error("出库单回执已存在,重复回调");
				return new ResultMessage(true);
			}
			ResultInfo<Boolean> result = stockoutBackProxy.processStockoutBack(stockoutBack);
			if (!result.isSuccess()) {
				logger.error("处理出库单回执异常:{}", result.getMsg().getDetailMessage());
				return new ResultMessage(false, "服务器异常");	
			}
			return new ResultMessage(true);
		} catch (Exception e) {
			logger.error("处理出库单回执异常", e);
			return new ResultMessage(false, "系统异常");
		}
	}
	
	/**
	 * 入库单回执 
	 */
	public ResultMessage stockasnCallback(){
		return null;
	}
	
	public String getAppKey() {
		return appKey;
	}

	public String getPassword() {
		return password;
	}
	
	/** 数据校验 */
	private boolean validate(JDZStockoutBackDto dto){
		if (StringUtil.isEmpty(dto.getProviderCode())) {
			logger.error("公共仓出库单反馈 - 厂商编码为空");
			return false;
		}else{
			if (!providerCode.equals(dto.getProviderCode())) {
				logger.error("公共仓出库单反馈 - 厂商编码不相同");
				return false;
			}
		}
		
		if (StringUtil.isEmpty(dto.getOrderCode())) {
			logger.error("公共仓出库单反馈 - 订单号为空");
			return false;
		}
		if (StringUtil.isEmpty(dto.getWarehouseCode())) {
			logger.error("公共仓出库单反馈 - 仓库编号为空");
			return false;
		}		
		if (StringUtil.isEmpty(dto.getLogisticsCompanyCode())) {
			logger.error("公共仓出库单反馈1 - 物流公司编号为空");
			return false;
		}
		if (null == JDZWmsConstant.ExpressCompany.getExpressCompanyByJDZCode(dto.getLogisticsCompanyCode())) {
			logger.error("公共仓出库单反馈1 - 物流公司编号错误:" + dto.getLogisticsCompanyCode());
			return false;
		}
		if (CollectionUtils.isEmpty(dto.getOrderOutDetails())) {
			logger.error("公共仓出库单反馈1 - 商品详情为空");
			return false;
		}
		for (JDZStockoutBackItemDto item : dto.getOrderOutDetails()) {
			if (StringUtil.isEmpty(item.getSku())) {
				logger.error("公共仓出库单反馈 - 商品详情数据中SKU为空");
				return false;
			}
			if (null == item.getQty()) {
				logger.error("公共仓出库单反馈 - 商品详情数据中数量不存在");
				return false;
			}
		}
		return true;
	}
	
	
	private StockoutBack convertToStockoutBack(JDZStockoutBackDto dto){
		StockoutBack back = new StockoutBack();
		back.setOrderCode(dto.getOrderCode());
		back.setExpressNo(dto.getExpressNo());
		back.setWmsCode(dto.getWarehouseCode());	//wmsCode
		//转成系统内的快递公司
		JDZWmsConstant.ExpressCompany ec = JDZWmsConstant.ExpressCompany.getExpressCompanyByJDZCode(dto.getLogisticsCompanyCode());
		back.setLogisticsCompanyCode(ec.getCommonCode());
		back.setLogisticsCompanyName(ec.getName());
		if(StringUtil.isNotEmpty(dto.getWeight())){
			back.setWeight(Double.valueOf(dto.getWeight()));
		}
		if (StringUtil.isNotEmpty(dto.getAuditor())) {
			back.setAuditor(dto.getAuditor());
		}
		if (StringUtil.isNotEmpty(dto.getAuditTime())) {
			back.setAuditTime(DateUtil.getDate(dto.getAuditTime(), "yyyy-mm-dd HH:mm:ss"));
		}
		
		List<StockoutBackDetail> details = new ArrayList<>();
		for (JDZStockoutBackItemDto item : dto.getOrderOutDetails()) {
			StockoutBackDetail detail =  new StockoutBackDetail();
			detail.setStockSku(item.getSku());
			detail.setQuantity(item.getQty());
			detail.setCreateTime(new Date());
			details.add(detail);
		}
		back.setDetails(details);
		back.setCreateTime(new Date());
		return back;
	}

	public boolean isEncrypt() {
		return isEncrypt;
	}
}
