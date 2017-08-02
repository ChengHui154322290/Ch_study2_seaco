package com.tp.dfsutils.file;

import com.tp.dfsutils.handle.GeneralHandler;

public class GeneralFile extends BaseFile {

	public GeneralFile() {
		super.setHandler(new GeneralHandler(this));
	}

}
