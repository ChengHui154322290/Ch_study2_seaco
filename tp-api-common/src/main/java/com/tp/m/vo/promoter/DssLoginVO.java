package com.tp.m.vo.promoter;

import com.tp.m.base.BaseVO;

public class DssLoginVO implements BaseVO{
	private static final long serialVersionUID = -7442773149373729771L;
	/**是否卡券推广员：0不是1是**/
	private Integer iscoupondss;
	/**是否店铺推广员：0不是1是**/
	private Integer isshopdss;
	/**是否扫码推广员：0不是1是**/
	private Integer isscandss;
	/**是否一级扫码推广员 0不是1是*/
	private String isTopScandss;

	private Long promoterid;
	
	private Integer isshopadmin; //是否是商城管理员

	public Long getPromoterid() {
		return promoterid;
	}

	public void setPromoterid(Long promoterid) {
		this.promoterid = promoterid;
	}

	public Integer getIscoupondss() {
		return iscoupondss;
	}
	public void setIscoupondss(Integer iscoupondss) {
		this.iscoupondss = iscoupondss;
	}
	public Integer getIsshopdss() {
		return isshopdss;
	}
	public void setIsshopdss(Integer isshopdss) {
		this.isshopdss = isshopdss;
	}
	public Integer getIsscandss() {
		return isscandss;
	}
	public void setIsscandss(Integer isscandss) {
		this.isscandss = isscandss;
	}

	public Integer getIsshopadmin() {
		return isshopadmin;
	}

	public void setIsshopadmin(Integer isshopadmin) {
		this.isshopadmin = isshopadmin;
	}

	public String getIsTopScandss() {
		return isTopScandss;
	}

	public void setIsTopScandss(String isTopScandss) {
		this.isTopScandss = isTopScandss;
	}
	
}
