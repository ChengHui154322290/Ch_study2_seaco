package com.tp.dfsutils.file;

import com.tp.dfsutils.handle.TxtHandler;

/**
 * 
 * @describe
 * @author 叶礼锋
 * 
 *         2014-12-22 下午5:42:37
 */
@SuppressWarnings(value = { "unused", "deprecation", "serial", "unchecked", "static-access", "rawtypes" })
public class TxtFile extends BaseFile {

	public boolean encryptionFlag = false;
	public String secretKey = null;

	public boolean isEncryptionFlag() {
		return encryptionFlag;
	}

	public void setEncryptionFlag(boolean encryptionFlag) {
		this.encryptionFlag = encryptionFlag;
	}

	public String getSecretKey() {
		return secretKey;
	}

	public void setSecretKey(String secretKey) {
		this.secretKey = secretKey;
	}

	public TxtFile() {
		super.setHandler(new TxtHandler(this));
	}

}
