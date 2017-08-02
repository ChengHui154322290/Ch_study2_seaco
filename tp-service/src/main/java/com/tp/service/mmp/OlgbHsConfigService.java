package com.tp.service.mmp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.dao.BaseDao;
import com.tp.dao.mmp.OlgbHsConfigDao;
import com.tp.model.mmp.OlgbHsConfig;
import com.tp.service.BaseService;
import com.tp.service.mmp.IOlgbHsConfigService;

import org.springframework.util.CollectionUtils;

import java.util.List;

@Service
public class OlgbHsConfigService extends BaseService<OlgbHsConfig> implements IOlgbHsConfigService {

    @Autowired
    private OlgbHsConfigDao olgbHsConfigDao;

    @Override
    public BaseDao<OlgbHsConfig> getDao() {
        return olgbHsConfigDao;
    }


    @Override
    public int updateStatusByIds(List<Long> ids) {
        if(CollectionUtils.isEmpty(ids)) return 0;
        return olgbHsConfigDao.updateStatusByIds(ids);
    }
}


