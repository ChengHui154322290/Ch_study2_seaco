package com.tp.m.vo.home;

import java.util.List;

import com.tp.m.base.BaseVO;

/**
 * 首页模块
 * @author zhuss
 *
 */
public class IndexModuleVO implements BaseVO{

	private static final long serialVersionUID = 8134863468321247826L;

	private List<BannerVO> banners;//广告位
	private List<LabVO> labs; //功能标签
	public List<BannerVO> getBanners() {
		return banners;
	}
	public void setBanners(List<BannerVO> banners) {
		this.banners = banners;
	}
	public List<LabVO> getLabs() {
		return labs;
	}
	public void setLabs(List<LabVO> labs) {
		this.labs = labs;
	}
}
