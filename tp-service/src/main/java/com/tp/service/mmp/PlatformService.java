package com.tp.service.mmp;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.dao.BaseDao;
import com.tp.common.vo.PageInfo;
import com.tp.dao.mmp.PlatformDao;
import com.tp.model.mmp.Platform;
import com.tp.service.BaseService;
import com.tp.service.mmp.IPlatformService;

@Service
public class PlatformService extends BaseService<Platform> implements IPlatformService {

	@Autowired
	private PlatformDao platformDao;
	
	@Override
	public BaseDao<Platform> getDao() {
		return platformDao;
	}

	@Override
	public int update(Platform platformDO, boolean isAllField) {
		return 0;
	}

	@Override
	public int deleteById(Long id) {
		return 0;
	}

	@Override
	public Platform selectById(Long id) {
		return null;
	}

	@Override
	public Long selectCountDynamic(Platform platform) {
		return null;
	}

	@Override
	public List<Platform> selectDynamic(Platform platform) {
		return null;
	}

	@Override
	public PageInfo<Platform> queryPageInfoListByPlatform(Platform platform) {
		return null;
	}

	@Override
	public PageInfo<Platform> queryPageInfoListByPlatformAndStartPageInfoSize(Platform platformDO, int startPageInfo, int pageSize) {
		return null;
	}
}
