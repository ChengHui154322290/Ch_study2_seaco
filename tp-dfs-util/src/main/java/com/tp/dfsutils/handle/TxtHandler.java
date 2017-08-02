package com.tp.dfsutils.handle;

import com.tp.dfsutils.file.TxtFile;
import com.tp.dfsutils.filter.FileFilter;
import com.tp.dfsutils.filter.TxtEncryptionFilter;

/**
 * 
 * @describe
 * @author 叶礼锋
 * 
 *         2014-12-22 下午5:43:18
 */
@SuppressWarnings(value = { "unused", "deprecation", "serial", "unchecked", "static-access", "rawtypes" })
public class TxtHandler implements FileHandle {

	private TxtFile txtFile = null;

	public TxtHandler(TxtFile txtFile) {
		this.txtFile = txtFile;
	}

	public void handle() throws Exception {
		FileFilter encryptionFilter = new TxtEncryptionFilter();
		encryptionFilter.doFilter(txtFile, null);
	}

}
