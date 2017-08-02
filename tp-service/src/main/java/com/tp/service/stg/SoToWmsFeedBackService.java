/**
 * 
 */
package com.tp.service.stg;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.tp.common.vo.StorageConstant.OutputOrderType;
import com.tp.dto.common.FailInfo;
import com.tp.dto.common.ResultInfo;
import com.tp.model.stg.OutputBack;
import com.tp.model.stg.OutputBackSku;
import com.tp.model.stg.OutputOrder;
import com.tp.model.stg.Warehouse;
import com.tp.model.stg.vo.feedback.OutputBackVO;
import com.tp.model.stg.vo.feedback.OutputBacksVO;
import com.tp.model.stg.vo.feedback.SkuVO;
import com.tp.service.stg.IOutputBackService;
import com.tp.service.stg.IOutputBackSkuService;
import com.tp.service.stg.IOutputOrderService;
import com.tp.service.stg.ISoToWmsFeedBackService;
import com.tp.service.stg.IWarehouseService;
import com.tp.util.DateUtil;

/**
 * @author szy
 *
 */
@Service
public class SoToWmsFeedBackService implements ISoToWmsFeedBackService {
	private Logger logger = LoggerFactory.getLogger(SoToWmsFeedBackService.class);

	@Autowired
	private IOutputBackService outputBackService;

	@Autowired
	private IOutputBackSkuService outputBackSkuService;

	@Autowired
	private IWarehouseService warehouseService;

	@Autowired
	private IOutputOrderService outputOrderService;

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public ResultInfo<String> saveBackInfo(OutputBacksVO outputBacksVO) {
		Map<String, Object> params = new HashMap<>();
		// 保存反馈信息到数据库 pre data
		OutputBackVO outputBackVO = outputBacksVO.getOutputBack();
		OutputBack outputBackObj = new OutputBack();
		outputBackObj.setOrderNo(outputBackVO.getOrderNo()); // 也对应出库订单的system_id
		outputBackObj.setShipNo(outputBackVO.getShipNo());
		outputBackObj.setShipTime(outputBackVO.getShipTime());
		outputBackObj.setCarrierId(outputBackVO.getCarrierID());
		outputBackObj.setCarrierName(outputBackVO.getCarrierName());
		outputBackObj.setCustomerId(outputBackVO.getCustomerId());
		outputBackObj.setBgNo(outputBackVO.getBgNo());

		params.clear();
		params.put("code", outputBackVO.getBgNo());
		Warehouse warehouseObj = warehouseService.queryUniqueByParams(params);
		if (null == warehouseObj) {
			return new ResultInfo<String>(new FailInfo("对应仓库的id号不存在"));
		}
		long warehouseId = warehouseObj.getId();

		outputBackObj.setVarehouseId(warehouseId);
		outputBackObj.setWeight(new Double(outputBackVO.getWeight()));
		outputBackObj.setCreateTime(DateUtil.getDateAfterDays(0));
		// 保存出库反馈记录
		outputBackService.insert(outputBackObj);
		if (0L == outputBackObj.getId()) {
			return new ResultInfo<String>(new FailInfo("保存出库反馈异常"));
		}
		logger.debug("outputBackId = " + outputBackObj.getId());
		List<SkuVO> skuList = outputBackVO.getSend();
		List<OutputBackSku> outputBackSkuList = new ArrayList<OutputBackSku>();
		for (SkuVO sku : skuList) {
			logger.debug("正在保存出库反馈记录 SKU:" + sku.getSkuCode());
			OutputBackSku outputBackSkuObj = new OutputBackSku();
			outputBackSkuObj.setOutputBackId(outputBackObj.getId());
			outputBackSkuObj.setSku(sku.getSkuCode());
			outputBackSkuObj.setBarcode(sku.getBarcode());
			outputBackSkuObj.setNum(sku.getSkuNum());
			outputBackSkuObj.setVendor(sku.getVendor());
			outputBackSkuObj.setLotatt01("");
			outputBackSkuObj.setLotatt02("");
			outputBackSkuObj.setLotatt03("");
			outputBackSkuObj.setCreateTime(DateUtil.getDateAfterDays(0));
			outputBackSkuList.add(outputBackSkuObj);
		}
		// 保存出库反馈SKU记录
		long count = outputBackSkuService.insertBatch(outputBackSkuList);
		if (count != outputBackSkuList.size()) {
			return new ResultInfo<String>(new FailInfo("保存出库反馈SKU异常"));
		}

		return new ResultInfo<String>("处理出库反馈成功");
	}

	@Override
	public OutputBack selectByOrderCode(String orderCode) {
		if (StringUtils.isBlank(orderCode)) {
			return null;
		}
		Map<String, Object> params = new HashMap<>();
		params.put("orderNo", orderCode);
		List<OutputBack> backObjs = outputBackService.queryByParamNotEmpty(params);
		return backObjs.get(0);
	}

	@Override
	public OutputOrderType selectOutputTypeByOrderCode(String orderNo) {
		Map<String, Object> params = new HashMap<>();
		params.put("orderCode", orderNo);
		List<OutputOrder> outputOrderDOList = outputOrderService.queryByParamNotEmpty(params);
		return OutputOrderType.valueOf(outputOrderDOList.get(0).getOrderType());
	}

}
