package com.tp.service.cms;

import com.tp.dfsutils.file.BaseFile;

/**
* 公共管理 Service
* @author szy
*/
public interface ICommUtilService {

	/**
	 * 图片上传接口
	 * @param baseFile
	 * @return
	 */
	public String uploadFile(BaseFile baseFile) ;
	
	
}
