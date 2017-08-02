/**
 * 
 */
package com.tp.service.stg;

import com.tp.model.stg.InputBack;
import com.tp.model.stg.vo.feedback.ASNDetailsVO;

/**
 * @author szy
 * 入库单反馈[仓库推送入库反馈信息至客户]
 */
public interface IAnsToWmsFeedBackService {
	/**
	 * 入库单反馈
	 * 
	 * @param attributeDO
	 * @return
	 * @throws Exception 
	 * @throws StorageServiceException
	 */
	Long insert(ASNDetailsVO attribute);

	InputBack selectByOrderCode(String orderCode);
}
