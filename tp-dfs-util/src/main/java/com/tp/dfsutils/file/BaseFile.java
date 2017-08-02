package com.tp.dfsutils.file;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.csource.common.NameValuePair;

import com.tp.dfsutils.constants.MetaDataKey;
import com.tp.dfsutils.handle.FileHandle;

/**
 * 
 * @describe
 * @author 叶礼锋
 * 
 *         2014-12-22 下午2:13:12
 */
@SuppressWarnings(value = { "unused", "deprecation", "serial", "unchecked", "static-access", "rawtypes" })
public abstract class BaseFile {

	private File file = null;

	private File originalFile = null;

	public FileHandle handler = null;

	private Map<MetaDataKey, String> metaData = null;

	public File getOriginalFile() {
		return originalFile;
	}

	public void setOriginalFile(File originalFile) {
		this.originalFile = originalFile;
	}

	public File getFile() {
		return file;
	}

	public void setFile(File file) {
		this.file = file;
	}

	public Map<MetaDataKey, String> getMetaData() {
		return metaData;
	}

	public void setMetaData(Map<MetaDataKey, String> metaData) {
		this.metaData = metaData;
	}

	public void setHandler(FileHandle handler) {
		this.handler = handler;
	}

	/**
	 * 将Map元数据转为线性结构
	 * 
	 * @return
	 */
	public List<NameValuePair> getNameValuePair() {
		List<NameValuePair> metaList = new ArrayList();
		if (metaData != null) {
			for (MetaDataKey key : metaData.keySet()) {
				metaList.add(new NameValuePair(key.name(), metaData.get(key)));
			}
		}
		return metaList;
	}

	/**
	 * 获得文件扩展名
	 * 
	 * @return
	 */
	public String getFileExtName() {
		String fileExtName = null;
		if (file != null) {
			String fileName = file.getName();
			if (fileName.indexOf(".") > 0) {
				fileExtName = fileName.substring(fileName.lastIndexOf(".") + 1);
			}
		}
		return fileExtName;
	}

	/**
	 * 文件处理
	 * 
	 * @throws Exception
	 */
	public void handle() throws Exception {
		if (handler != null) {
			handler.handle();
		}
	}

	/**
	 * 清理文件
	 * 
	 * @throws Exception
	 */
	public void clear() throws Exception {
		if (file != null && file.exists()) {
			file.delete();
		}
		if (originalFile != null && originalFile.exists()) {
			originalFile.delete();
		}
	}
}
