package com.tp.proxy.cms;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.vo.PageInfo;
import com.tp.dto.cms.app.AppIndexAdvertReturnData;
import com.tp.dto.cms.app.AppSingleAllInfoDTO;
import com.tp.dto.cms.app.AppSingleCotAllInfoDTO;
import com.tp.dto.cms.app.AppSingleInfoDTO;
import com.tp.proxy.BaseProxy;
import com.tp.service.IBaseService;
import com.tp.service.cms.app.IFastAppService;
import com.tp.service.cms.app.IHaitaoAppService;
/**
 * 广告管理表代理层
 * @author szy
 *
 */
@Service
public class FastAppProxy extends BaseProxy{
	@Autowired
	IFastAppService fastAppService;

	@Override
	public IBaseService getService() {
		return null;
	}
	
	public IHaitaoAppService getFastAppService(){
		return fastAppService;
	}
	/**
	 * APP海淘首页-轮播图
	 */
	public AppIndexAdvertReturnData carouseAdvert() {
		return fastAppService.carouseAdvert();
	}

	/**
	 * APP海淘首页-功能标签
	 */
	public AppIndexAdvertReturnData funLab() {
		return fastAppService.funLab();
	}

	/**
	 * APP海淘首页-单品团
	 */
	public PageInfo<AppSingleCotAllInfoDTO> singleTemplet(int pagestart,int pagesize) {
		return fastAppService.singleTemplet(pagestart, pagesize);
	}

	/**
	 * APP海淘首页-专场列表
	 */
	public PageInfo<AppSingleInfoDTO> htTopicList(int pagestart,int pagesize) {
		return fastAppService.htTopicList(pagestart, pagesize);
	}
	/**
	 * APP海淘首页-专场列表
	 */
	public PageInfo<AppSingleAllInfoDTO> htTopicAllList( int pagestart,int pagesize) {
		return fastAppService.htTopicAllList(null, pagestart, pagesize,new String[0]);
	}
}
