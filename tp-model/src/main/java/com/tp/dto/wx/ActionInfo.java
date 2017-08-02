package com.tp.dto.wx;

import java.io.Serializable;

public class ActionInfo implements Serializable{

	private static final long serialVersionUID = 840856721215394497L;

	private Scene scene;

	public Scene getScene() {
		return scene;
	}

	public void setScene(Scene scene) {
		this.scene = scene;
	}
}
