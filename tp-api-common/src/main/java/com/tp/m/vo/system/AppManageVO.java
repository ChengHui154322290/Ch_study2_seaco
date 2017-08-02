package com.tp.m.vo.system;

import com.tp.m.base.BaseVO;

/**
 * APP管理出参
 * @author zhuss
 * @2016年4月8日 下午6:34:23
 */
public class AppManageVO implements BaseVO{

	private static final long serialVersionUID = -7433139541266962701L;

	private String isnew;//是否最新 0否 1是
	private String downurl;//下载地址
	private String content;//更新内容
	private String v;//版本号

	public String getV() {
		return v;
	}

	public void setV(String v) {
		this.v = v;
	}

	public AppManageVO() {
		super();
	}

	public String getIsnew() {
		return isnew;
	}

	public void setIsnew(String isnew) {
		this.isnew = isnew;
	}

	public String getDownurl() {
		return downurl;
	}

	public void setDownurl(String downurl) {
		this.downurl = downurl;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
}
