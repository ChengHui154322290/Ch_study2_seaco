package com.tp.dto.wx;

import java.io.Serializable;

/**
 * 二维码详细信息
 * @author zhuss
 *
 */
public class Scene implements Serializable{

	private static final long serialVersionUID = -1283233048312726539L;

	private int scene_id;//场景值ID，临时二维码时为32位非0整型，永久二维码时最大值为100000（目前参数只支持1--100000）
	private String scene_str; //场景值ID（字符串形式的ID），字符串类型，长度限制为1到64，仅永久二维码支持此字段
	
	
	public Scene() {
		super();
	}
	public Scene(String scene_str) {
		super();
		this.scene_str = scene_str;
	}
	public Scene(int scene_id, String scene_str) {
		super();
		this.scene_id = scene_id;
		this.scene_str = scene_str;
	}
	public int getScene_id() {
		return scene_id;
	}
	public void setScene_id(int scene_id) {
		this.scene_id = scene_id;
	}
	public String getScene_str() {
		return scene_str;
	}
	public void setScene_str(String scene_str) {
		this.scene_str = scene_str;
	}
}
