package com.tp.dfsutils.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.csource.common.NameValuePair;
import org.csource.fastdfs.StorageClient1;
import org.csource.fastdfs.TrackerServer;

import com.tp.dfsutils.constants.MetaDataKey;
import com.tp.dfsutils.file.BaseFile;
import com.tp.dfsutils.service.DfsService;
import com.tp.dfsutils.util.TrackerServerPool;

@SuppressWarnings(value = { "unused", "deprecation", "serial", "unchecked", "static-access", "rawtypes" })
public class DfsServiceImpl implements DfsService {

	private static final Log logger = LogFactory.getLog(DfsServiceImpl.class);

	private TrackerServerPool trackerServerPool;

	public DfsServiceImpl(TrackerServerPool storageClient1Pool) {
		this.trackerServerPool = storageClient1Pool;
	}

	private String uploadFile(BaseFile baseFile, String fileExtName, StorageClient1 client) throws Exception {
		baseFile.handle();
		List<NameValuePair> metaList = baseFile.getNameValuePair();
		return client.upload_file1(baseFile.getFile().getAbsolutePath(), fileExtName, metaList.toArray(new NameValuePair[0]));
	}

	public String uploadFile(BaseFile baseFile) {
		if (null == baseFile) {
			return null;
		}
		String fileExtName = baseFile.getFileExtName();
		if (fileExtName == null || "".equals(fileExtName.trim())) {
			return null;
		}
		String fileId = null;
		TrackerServer trackerServer = null;
		try {
			trackerServer = trackerServerPool.borrowObject();
			StorageClient1 client = new StorageClient1(trackerServer, null);
			fileId = uploadFile(baseFile, fileExtName, client);
		} catch (Exception e) {
			logError(e);
		} finally {
			trackerServerPool.returnObject(trackerServer);
		}
		return fileId;
	}
	
	public String uploadFile(BaseFile baseFile,String groupName) {
		if (null == baseFile) {
			return null;
		}
		String fileExtName = baseFile.getFileExtName();
		if (fileExtName == null || "".equals(fileExtName.trim())) {
			return null;
		}
		String fileId = null;
		TrackerServer trackerServer = null;
		try {
			trackerServer = trackerServerPool.borrowObject();
			StorageClient1 client = new StorageClient1(trackerServer, null);
			return uploadFile(baseFile, groupName, fileExtName, client);
		} catch (Exception e) {
			logError(e);
		} finally {
			trackerServerPool.returnObject(trackerServer);
		}
		return fileId;
	}

	private String uploadFile(BaseFile baseFile, String groupName, String fileExtName, StorageClient1 client) throws Exception {
		baseFile.handle();// 文件处理
		List<NameValuePair> metaList = baseFile.getNameValuePair();// 封装元数据
		return client.upload_file1(groupName, baseFile.getFile().getAbsolutePath(), fileExtName, metaList.toArray(new NameValuePair[0]));
	}

	static void logError(Exception e) {
		if (logger.isErrorEnabled()) {
			logger.error(e);
		}
	}

	public String[] batchUploadFile(BaseFile... baseFiles) {
		TrackerServer trackerServer = null;
		String fileIds[] = new String[baseFiles.length];
		try {
			trackerServer = trackerServerPool.borrowObject();
			StorageClient1 client = new StorageClient1(trackerServer, null);
			int i = 0;
			for (BaseFile baseFile : baseFiles) {
				String fileExtName = baseFile.getFileExtName();
				if (fileExtName == null) {
					fileIds[i] = null;
					continue;
				} else {
					fileIds[i] = uploadFile(baseFile, fileExtName, client);
				}
				i++;
			}
		} catch (Exception e) {
			logError(e);
		} finally {
			trackerServerPool.returnObject(trackerServer);
		}
		return fileIds;
	}

	public int deleteFile(String fileid) {
		int result = 1;
		TrackerServer trackerServer = null;
		try {
			trackerServer = trackerServerPool.borrowObject();
			StorageClient1 client = new StorageClient1(trackerServer, null);
			result = client.delete_file1(fileid);
		} catch (Exception e) {
			logError(e);
		} finally {
			trackerServerPool.returnObject(trackerServer);
		}
		return result;
	}

	public int[] batchDeleteFile(String... fileids) {
		int result[] = new int[fileids.length];
		TrackerServer trackerServer = null;
		try {
			trackerServer = trackerServerPool.borrowObject();
			StorageClient1 client = new StorageClient1(trackerServer, null);
			int i = 0;
			for (String fileid : fileids) {
				result[i++] = client.delete_file1(fileid);
			}
		} catch (Exception e) {
			logError(e);
		} finally {
			trackerServerPool.returnObject(trackerServer);
		}
		return result;
	}

	public byte[] getFileBytes(String fileid) {
		byte[] bytes = null;
		TrackerServer trackerServer = null;
		try {
			trackerServer = trackerServerPool.borrowObject();
			StorageClient1 client = new StorageClient1(trackerServer, null);
			bytes = client.download_file1(fileid);
		} catch (Exception e) {
			logError(e);
		} finally {
			trackerServerPool.returnObject(trackerServer);
		}
		return bytes;
	}

	public Map<MetaDataKey, String> getFileMetaData(String fileid) {
		Map<MetaDataKey, String> metaData = null;
		TrackerServer trackerServer = null;
		try {
			trackerServer = trackerServerPool.borrowObject();
			StorageClient1 client = new StorageClient1(trackerServer, null);
			NameValuePair nvps[] = client.get_metadata1(fileid);
			metaData = new HashMap(nvps.length);
			for (NameValuePair nvp : nvps) {
				metaData.put(MetaDataKey.valueOf(nvp.getName()), nvp.getValue());
			}
		} catch (Exception e) {
			logError(e);
		} finally {
			trackerServerPool.returnObject(trackerServer);
		}
		return metaData;
	}

	public boolean setFileMetaData(String fileid, Map<MetaDataKey, String> metaData) {
		int result = 0;
		TrackerServer trackerServer = null;
		try {
			trackerServer = trackerServerPool.borrowObject();
			StorageClient1 client = new StorageClient1(trackerServer, null);
			NameValuePair metaArray[] = new NameValuePair[metaData.size()];
			int i = 0;
			if (metaData != null) {
				for (MetaDataKey key : metaData.keySet()) {
					metaArray[i++] = new NameValuePair(key.name(), metaData.get(key));
				}
			}
			result = client.set_metadata1(fileid, metaArray, (byte) 77);
		} catch (Exception e) {
			logError(e);
		} finally {
			trackerServerPool.returnObject(trackerServer);
		}
		return result == 0;
	}

	@Deprecated
	private String modifyFile(String fileid, BaseFile baseFile) {
		String fileExtName = baseFile.getFileExtName();
		if (fileExtName == null) {
			return null;
		}
		String newFileId = null;
		NameValuePair nvps[] = null;
		Map<String, String> metaData = null;
		TrackerServer trackerServer = null;
		try {
			trackerServer = trackerServerPool.borrowObject();
			StorageClient1 client = new StorageClient1(trackerServer, null);
			client.delete_file1(fileid);
			newFileId = uploadFile(baseFile, fileExtName, client);
		} catch (Exception e) {
			logError(e);
		} finally {
			trackerServerPool.returnObject(trackerServer);
		}
		return newFileId;
	}

}
