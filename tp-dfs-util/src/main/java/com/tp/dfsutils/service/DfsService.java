package com.tp.dfsutils.service;

import java.util.Map;

import com.tp.dfsutils.constants.MetaDataKey;
import com.tp.dfsutils.file.BaseFile;

/**
 * 
 * @describe 图片访问服务接口
 * @author 叶礼锋
 * 
 *         2014-12-16 上午9:56:07
 */
@SuppressWarnings(value = { "unused", "deprecation", "serial", "unchecked", "static-access", "rawtypes" })
public interface DfsService {

	/**
	 * 添加一张图片
	 * 
	 * @param baseFile
	 *            图片实体
	 * @return
	 */
	public String uploadFile(BaseFile baseFile);
	
	/**
	 * 添加一张图片
	 * 
	 * @param baseFile
	 *            图片实体
	 * @param groupName           
	 * @return
	 */
	public String uploadFile(BaseFile baseFile,String groupName);

	/**
	 * 批量添加图片
	 * 
	 * @param baseFile实体
	 * @return
	 */
	public String[] batchUploadFile(BaseFile... baseFiles);

	/**
	 * 根据图片ID删除图片
	 * 
	 * @param fileid
	 *            文件ID
	 * @return
	 */
	public int deleteFile(String fileid);

	/**
	 * 批量删除图片
	 * 
	 * @param fileids
	 *            文件IDS
	 * @return
	 */
	public int[] batchDeleteFile(String... fileids);

	/**
	 * 根据图片ID获得文件流，默认裸输出
	 * 
	 * @param fileid
	 *            文件ID
	 * @return
	 */
	public byte[] getFileBytes(String fileid);

	/**
	 * 根据图片ID获得文件元数据信息
	 * 
	 * @param fileid
	 * @return
	 */
	public Map<MetaDataKey, String> getFileMetaData(String fileid);

	/**
	 * 修改文件元数据信息
	 * 
	 * @param fileid
	 * @param metaData
	 * @return
	 */
	public boolean setFileMetaData(String fileid, Map<MetaDataKey, String> metaData);

}
