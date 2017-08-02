package com.tp.service.cms.app;

import com.tp.dto.cms.app.AppAdvertiseInfoDTO;
import com.tp.dto.cms.app.AppIndexAdvertReturnData;

/**
* APP的图片管理 Service
*/
public interface IAdvertiseAppService {

	/**
	 * 首页-广告位：给出图片地址，图片链接，以及图片所关联的活动ID
	 * @return
	 * @throws Exception
	 */
	AppIndexAdvertReturnData queryIndexAdvertiseInfo() throws Exception;
	
	/**
	 * 秒杀-广告位信息：图片src ，link ，图片所关联的商品id。
	 * @return
	 * @throws Exception
	 */
	AppAdvertiseInfoDTO<Object> querySecKillAdvertiseInfo() throws Exception;
	
	/**
	 * 首页-功能标签信息：给出图片地址，图片链接
	 * @return
	 * @throws Exception
	 */
	AppIndexAdvertReturnData queryIndexFunlabInfo() throws Exception;
	
	/**
	 * 海淘-广告位信息：图片src ，link ，图片所关联的商品id。
	 * @return
	 * @throws Exception
	 */
	AppAdvertiseInfoDTO<Object> queryGrainAdvertiseInfo() throws Exception;
	
	/**
	 * 广告-启动页面：图片名称 url  link  商品id  启动时间。
	 * @return
	 * @throws Exception
	 */
	AppAdvertiseInfoDTO<Object> queryAdvertPulseInfo() throws Exception;
	
	/**
	 * 广告-支付成功：图片url  link  商品id
	 * @return
	 * @throws Exception
	 */
	AppAdvertiseInfoDTO<Object> queryAdvertPayInfo() throws Exception;
	
	/**
	 * wap-首页弹框：图片url 名称 类型 管理信息
	 * @return
	 * @throws Exception
	 */
	AppAdvertiseInfoDTO<Object> queryWapAdvertInfo() throws Exception;
	
	/**
	 * wap-今日精选：图片url 名称 
	 * @return
	 * @throws Exception
	 */
	AppAdvertiseInfoDTO<Object> queryWapChosenHeadPic() throws Exception;
	
	
}
