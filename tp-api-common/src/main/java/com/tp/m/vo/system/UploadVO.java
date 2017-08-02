package com.tp.m.vo.system;

import com.tp.m.base.BaseVO;

/**
 * 上传出参
 * @author zhuss
 * @2016年1月13日 下午5:32:43
 */
public class UploadVO implements BaseVO{

	private static final long serialVersionUID = -7950819434823697163L;

	private String path;

	
	public UploadVO() {
		super();
	}

	public UploadVO(String path) {
		super();
		this.path = path;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}
}
