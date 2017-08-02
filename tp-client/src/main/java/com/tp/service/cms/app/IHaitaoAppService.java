package com.tp.service.cms.app;

import com.tp.common.vo.PageInfo;
import com.tp.dto.cms.app.AppIndexAdvertReturnData;
import com.tp.dto.cms.app.AppSingleAllInfoDTO;
import com.tp.dto.cms.app.AppSingleCotAllInfoDTO;
import com.tp.dto.cms.app.AppSingleInfoDTO;
import com.tp.model.cms.ActivityElement;

import java.util.List;

/**
* 新板APP海淘管理 Service
*/
public interface IHaitaoAppService {

	/**
	 * APP海淘首页-轮播图
	 */
	public AppIndexAdvertReturnData carouseAdvert();

	//店铺的banner
	AppIndexAdvertReturnData carouseDssAdvert();
	
	@SuppressWarnings({"unchecked" })
	 List<?> queryPageTempletElementInfo(String pageCode, String templetCode) throws Exception;

	List<ActivityElement> queryActivityElementsByTempletCodes(Long promoterId, String pageCode, List<String> templetCode, int pagestart, int pagesize) throws Exception;
	
	/**
	 * APP海淘首页-功能标签
	 */
	public AppIndexAdvertReturnData funLab();
	/**
	 * world首页功能标签
	 */
	public AppIndexAdvertReturnData funLabForWorld();

	AppIndexAdvertReturnData dssFunLab(String channelCode);
	/**
	 * APP海淘首页-单品团
	 */
	public PageInfo<AppSingleCotAllInfoDTO> singleTemplet(int pagestart,int pagesize);
	
	/**
	 * APP海淘首页-专场列表
	 */
	public PageInfo<AppSingleInfoDTO> htTopicList(int pagestart,int pagesize);

	/**
	 * APP海淘首页-专场、单品团
	 */
	PageInfo<AppSingleAllInfoDTO> htTopicAllList(Long promoterid, int pagestart, int pagesize,String ...templates);

	public List<AppSingleAllInfoDTO> getAppSingleAllInfoDTOs(Long promoterid, List<ActivityElement> lst,String ...templates);


	/**
	 * 店铺首页 
	 */
	PageInfo<AppSingleAllInfoDTO> htTopicAllListForDss(Long promoterid, int pagestart, int pagesize);

	/**
	 * 分销店铺2
	 */
	PageInfo<AppSingleAllInfoDTO> htTopicAllListForDss2(Long promoterid,String channelCode, int pagestart, int pagesize);

	PageInfo<AppSingleAllInfoDTO> htTopicAllListForDssWithMS(Long promoterid,String channelCode, int pagestart,int pagesize);

}
