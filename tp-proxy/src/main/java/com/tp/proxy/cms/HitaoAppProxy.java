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
import com.tp.service.cms.app.IHaitaoAppService;
/**
 * 广告管理表代理层
 * @author szy
 *
 */
@Service
public class HitaoAppProxy extends BaseProxy{
	@Autowired
	IHaitaoAppService haitaoAppService;

	@Override
	public IBaseService getService() {
		return null;
	}
	
	public IHaitaoAppService getHaitaoAppService(){
		return haitaoAppService;
	}
	/**
	 * APP海淘首页-轮播图
	 */
	public AppIndexAdvertReturnData carouseAdvert() {
		return haitaoAppService.carouseAdvert();
	}

	/**
	 * APP海淘首页-功能标签
	 */
	public AppIndexAdvertReturnData funLab() {
		return haitaoAppService.funLab();
	}

	/**
	 * world首页功能标签
	 */
	public AppIndexAdvertReturnData funLabForWorld() {
		return haitaoAppService.funLabForWorld();
	}

	public AppIndexAdvertReturnData dssFunLab(String channelCode) {
		return haitaoAppService.dssFunLab(channelCode);
	}
	
	/**
	 * APP海淘首页-单品团
	 */
	public PageInfo<AppSingleCotAllInfoDTO> singleTemplet(int pagestart,int pagesize) {
		return haitaoAppService.singleTemplet(pagestart, pagesize);
	}

	/**
	 * APP海淘首页-专场列表
	 */
	public PageInfo<AppSingleInfoDTO> htTopicList(int pagestart,int pagesize) {
		return haitaoAppService.htTopicList(pagestart, pagesize);
	}
	/**
	 * APP海淘首页-专场列表
	 */
	public PageInfo<AppSingleAllInfoDTO> htTopicAllList( int pagestart,int pagesize) {
		return haitaoAppService.htTopicAllList(null, pagestart, pagesize,new String[0]);
	}
}
