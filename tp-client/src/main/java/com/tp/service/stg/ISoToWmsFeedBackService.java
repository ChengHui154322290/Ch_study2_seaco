package com.tp.service.stg;

import com.tp.common.vo.StorageConstant.OutputOrderType;
import com.tp.dto.common.ResultInfo;
import com.tp.model.stg.OutputBack;
import com.tp.model.stg.vo.feedback.OutputBacksVO;

/**
 * 出库单反馈[仓库推送入库反馈信息至客户]
 */
public interface ISoToWmsFeedBackService {
	/**
	 * 保存反馈信息
	 * @param attributeDO
	 * @return
	 * @throws Exception 
	 * @throws StorageServiceException
	 */
	ResultInfo<String> saveBackInfo(OutputBacksVO attributeDO);

	OutputBack selectByOrderCode(String orderCode);
	/**
	 * 根据orderNo获得出库类型
	 * 
	 * @param orderNo
	 * @return
	 */
	OutputOrderType selectOutputTypeByOrderCode(String orderNo);
}
