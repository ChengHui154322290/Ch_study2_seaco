/**
 * 
 */
package com.tp.m.vo.promoter;

import com.tp.m.base.BaseVO;

/**
 * @author Administrator
 *
 */
public class ChannelInfoVO implements BaseVO{
	
	private static final long serialVersionUID = 6830401538521954747L;
	/*------------渠道基本信息-----------------*/
	private Integer channelid;		//id
	private String channelcode;		//编码
	private String channelname;		//渠道名称
	private String token;			//渠道token
	private Integer disabled;		//是否已禁用0:否，1是，默认0
	private String createtime;
	
	/*--------------渠道对应商铺信息------------*/
	private String eshopname; 		// 渠道商城名称
	private String sharetitle;		// 渠道分享标题
	private String sharecontent;	// 渠道分享内容
	private Integer dsstype;		// 渠道是否开通分销：0不开通1开通
	private String isUsedPoint;//是否使用第三方商城的积分 1是，默认0  不使用
	
	/*--------------对应推广员信息--------------*/
	private PromoterInfoVO promoterinfo;	//对应推广信息

	public Integer getChannelid() {
		return channelid;
	}

	public void setChannelid(Integer channelid) {
		this.channelid = channelid;
	}

	public String getChannelcode() {
		return channelcode;
	}

	public void setChannelcode(String channelcode) {
		this.channelcode = channelcode;
	}

	public String getChannelname() {
		return channelname;
	}

	public void setChannelname(String channelname) {
		this.channelname = channelname;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public Integer getDisabled() {
		return disabled;
	}

	public void setDisabled(Integer disabled) {
		this.disabled = disabled;
	}

	public String getCreatetime() {
		return createtime;
	}

	public void setCreatetime(String createtime) {
		this.createtime = createtime;
	}

	public String getEshopname() {
		return eshopname;
	}

	public void setEshopname(String eshopname) {
		this.eshopname = eshopname;
	}

	public String getSharetitle() {
		return sharetitle;
	}

	public void setSharetitle(String sharetitle) {
		this.sharetitle = sharetitle;
	}

	public String getSharecontent() {
		return sharecontent;
	}

	public void setSharecontent(String sharecontent) {
		this.sharecontent = sharecontent;
	}

	public PromoterInfoVO getPromoterinfo() {
		return promoterinfo;
	}

	public void setPromoterinfo(PromoterInfoVO promoterinfo) {
		this.promoterinfo = promoterinfo;
	}

	public Integer getDsstype() {
		return dsstype;
	}

	public void setDsstype(Integer dsstype) {
		this.dsstype = dsstype;
	}

	public String getIsUsedPoint() {
		return isUsedPoint;
	}

	public void setIsUsedPoint(String isUsedPoint) {
		this.isUsedPoint = isUsedPoint;
	}
	
}
