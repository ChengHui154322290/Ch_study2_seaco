package com.tp.service;

import java.io.File;
import java.util.List;


public interface IUploadService {
	
	/**
	 * 上传单个文件
	 * @param file
	 * @return
	 */
	public String upload(File file);
	/**
	 * 上传多个文件
	 * @param fileList
	 * @return
	 */
	public List<String> upload(List<File> fileList);
	/**
	 * 下载文件
	 * @param fileId
	 * @return
	 */
	public File downFile(String fileId);
}
