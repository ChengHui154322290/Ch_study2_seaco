package com.tp.dfsutils.filter;

import com.tp.dfsutils.file.BaseFile;

/**
 * 
 * @describe
 * @author 叶礼锋
 * 
 *         2014-12-18 下午1:48:11
 */
@SuppressWarnings(value = { "unused", "deprecation", "serial", "unchecked", "static-access", "rawtypes" })
public abstract class FileFilter<T> {

	protected FileFilter<T> successor;

	public FileFilter setSuccessor(FileFilter<T> successor) {
		return this.successor = successor;
	}

	public abstract void doFilter(BaseFile baseFile, T adjunct) throws Exception;

}