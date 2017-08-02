/**
 * 
 */
package com.tp.service.wms.thirdparty;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.vo.Constant.SPLIT_SIGN;
import com.tp.common.vo.DAOConstant.MYBATIS_SPECIAL_STRING;
import com.tp.common.vo.bse.ClearanceChannelsEnum;
import com.tp.common.vo.stg.WarehouseConstant.WMSCode;
import com.tp.dto.common.FailInfo;
import com.tp.dto.common.ResultInfo;
import com.tp.model.prd.ItemSkuArt;
import com.tp.model.wms.Stockout;
import com.tp.model.wms.StockoutBack;
import com.tp.model.wms.StockoutBackDetail;
import com.tp.model.wms.StockoutDetail;
import com.tp.service.prd.IItemSkuArtService;
import com.tp.service.wms.thirdparty.IStockoutBackTPService;
import com.tp.util.StringUtil;

/**
 * @author Administrator
 *
 */
@Service("stockoutBackFromJDZService")
public class StockoutBackFromJDZService implements IStockoutBackTPService{

	private static final Logger logger = LoggerFactory.getLogger(StockoutBackFromJDZService.class);
	
	@Autowired
	private IItemSkuArtService itemSkuArtService;
	
	@Override
	public boolean check(StockoutBack stockoutBack, Stockout stockout) {
		// TODO Auto-generated method stub
		return false;
	}
	
	@Override
	public ResultInfo<StockoutBack> processStockoutBack(StockoutBack stockoutBack, Stockout stockout) {
		//SKU转换
		List<String> stockSkuList = new ArrayList<>();
		for (StockoutBackDetail stockDetail : stockoutBack.getDetails()) {
			stockSkuList.add(stockDetail.getStockSku());
		}
		//查询海关备案信息
		Map<String, Object> params = new HashMap<>();
		params.put(MYBATIS_SPECIAL_STRING.COLUMNS.name(),
				"bonded_area = " + ClearanceChannelsEnum.HANGZHOU.id + " and article_number in(" + StringUtils.join(stockSkuList, SPLIT_SIGN.COMMA) + ")");
		List<ItemSkuArt> arts = itemSkuArtService.queryByParamNotEmpty(params);
		if (CollectionUtils.isEmpty(arts)) {
			logger.error("[STOCKOUT_BACK][{}]杭州口岸备案信息为空;{}", stockout.getOrderCode(), StringUtils.join(stockSkuList, SPLIT_SIGN.COMMA));
			return new ResultInfo<>(new FailInfo("海关备案信息不存在"));
		}
		Map<String, ItemSkuArt> stockSku2ArtMap = new HashMap<>();
		for (ItemSkuArt itemSkuArt : arts) {
			stockSku2ArtMap.put(itemSkuArt.getArticleNumber(), itemSkuArt);
		}
		return validateStockoutBack(stockout, stockoutBack, stockSku2ArtMap);
	}
	
	/**
	 * 出库单与回执校验 
	 * @param stockout
	 * @param stockoutBack
	 * @return
	 */
	private ResultInfo<StockoutBack> validateStockoutBack(Stockout stockout, StockoutBack stockoutBack, Map<String, ItemSkuArt> stockSku2ArtMap){
		if (!WMSCode.JDZ_HZ.code.equals(stockoutBack.getWmsCode()) || !WMSCode.JDZ_HZ.code.equals(stockout.getWmsCode())) {
			return new ResultInfo<>(new FailInfo("WMS编号不匹配"));
		}
		if (StringUtil.isNotEmpty(stockoutBack.getWarehouseCode()) && 
				!stockout.getWarehouseCode().equalsIgnoreCase(stockoutBack.getWarehouseCode())) {
			return new ResultInfo<>(new FailInfo("仓库编号不匹配"));
		}
		if (StringUtil.isNotEmpty(stockoutBack.getLogisticsCompanyCode()) &&
				!stockout.getLogisticsCompanyCode().equalsIgnoreCase(stockoutBack.getLogisticsCompanyCode())) {
			return new ResultInfo<>(new FailInfo("物流公司编号不匹配"));
		}
		if (StringUtil.isNotEmpty(stockoutBack.getExpressNo()) && 
				!stockout.getExpressNo().equalsIgnoreCase(stockoutBack.getExpressNo())){
			return new ResultInfo<>(new FailInfo("快递单号不匹配"));
		}
		//仓库方SKU是货号，通过货号查询平台方SKU
		Map<String, StockoutBackDetail> backDetailMap = new HashMap<>();
		for (StockoutBackDetail backDetail : stockoutBack.getDetails()) {
			ItemSkuArt art = stockSku2ArtMap.get(backDetail.getStockSku());
			if (null == art || StringUtils.isEmpty(art.getSku())) {
				return new ResultInfo<>(new FailInfo("商品" + backDetail.getItemSku() + "备案信息不存在"));
			}
			backDetail.setItemSku(art.getSku()); //设置平台方SKU
			backDetailMap.put(backDetail.getItemSku(), backDetail);
		}
		for (StockoutDetail detail : stockout.getStockoutDetails()) {	
			StockoutBackDetail bd = backDetailMap.get(detail.getItemSku());
			if (null == bd) {
				logger.error("[STOCKOUT_BACK][{}]出库单中商品{}在实际出库回执中不存在", 
						stockout.getOrderCode(), detail.getItemSku());
				return new ResultInfo<>(new FailInfo("出库单回执商品详情与出库单不匹配"));
			}
			if (!bd.getQuantity().equals(detail.getQuantity())) {
				logger.error("[STOCKOUT_BACK][{}]出库单中商品{}数量{}与实际出库回执中数量{}不匹配", 
						stockout.getOrderCode(), detail.getItemSku(), detail.getQuantity(), bd.getQuantity());
				return new ResultInfo<>(new FailInfo("出库单回执商品详情实际数量与出库单中数量不匹配"));
			}
		}
		//成功
		stockoutBack.setStatus(1);
		stockoutBack.setWarehouseCode(stockout.getWarehouseCode());
		return new ResultInfo<>(stockoutBack);
	}
}
