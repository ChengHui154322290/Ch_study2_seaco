package com.tp.service.app;


import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.dao.BaseDao;
import com.tp.dao.app.SkinInfoDao;
import com.tp.model.app.SkinInfo;
import com.tp.service.BaseService;
import com.tp.service.app.ISkinInfoService;

@Service
public class SkinInfoService extends BaseService<SkinInfo> implements ISkinInfoService {

	@Autowired
	private SkinInfoDao skinInfoDao;
	
	@Override
	public BaseDao<SkinInfo> getDao() {
		return skinInfoDao;
	}
	//手机皮肤接口
	@Override
	public SkinInfo searchSkin(Long skinid,Date nowTime){
		int status=1;
		List<SkinInfo> skin=skinInfoDao.selectSkinNew(skinid, nowTime,status);
		if(skin.size()!=0){
			return  skin.get(0);
		}else{
			return null;
		}
		

	}


}
