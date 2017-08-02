package com.tp.result.wx;

import java.io.Serializable;
import java.util.List;

import com.tp.dto.wx.MenuButtonDto;

public class MenuResult implements Serializable{

	private static final long serialVersionUID = 6551271471352924620L;

	private List<MenuButtonDto> button;

	public List<MenuButtonDto> getButton() {
		return button;
	}

	public void setButton(List<MenuButtonDto> button) {
		this.button = button;
	}
}
