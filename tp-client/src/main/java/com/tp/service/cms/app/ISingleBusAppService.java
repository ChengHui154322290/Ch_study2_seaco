package com.tp.service.cms.app;

import com.tp.common.vo.PageInfo;
import com.tp.dto.cms.app.AppSingleInfoDTO;
import com.tp.enums.common.PlatformEnum;


/**
* APP的模板业务管理 Service
*/
public interface ISingleBusAppService {
	
	/**
	 * 单品开团提醒
	 * @param userId(用户id) specialid(专线id) productid(商品id)
	 * @return
	 * @throws Exception
	 */
	/*void remindOpenGroup(Long userId) throws Exception;*/
	
	/**
	 * 首页-今日上新
	 * @param pageStart 起始页
	 * @param pageSize  页大小
	 * @param platformType 平台标识
	 * @return
	 * @throws Exception
	 */
	PageInfo<AppSingleInfoDTO> queryIndexNowSingleInfo(int pageStart,int pageSize,PlatformEnum platformType) throws Exception;

	
}
