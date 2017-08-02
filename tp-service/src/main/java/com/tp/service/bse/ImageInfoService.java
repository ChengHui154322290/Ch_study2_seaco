package com.tp.service.bse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.dao.BaseDao;
import com.tp.dao.bse.ImageInfoDao;
import com.tp.model.bse.ImageInfo;
import com.tp.service.BaseService;
import com.tp.service.bse.IImageInfoService;

import java.util.List;

@Service
public class ImageInfoService extends BaseService<ImageInfo> implements IImageInfoService {

	@Autowired
	private ImageInfoDao imageInfoDao;
	
	@Override
	public BaseDao<ImageInfo> getDao() {
		return imageInfoDao;
	}

public 	List<ImageInfo> queryListWithId(Long id){
		return imageInfoDao.queryListWithId(id);
	}

}
