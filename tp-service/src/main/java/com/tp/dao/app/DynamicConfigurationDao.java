package com.tp.dao.app;

import com.tp.common.dao.BaseDao;
import com.tp.model.app.DynamicConfiguration;

public interface DynamicConfigurationDao extends BaseDao<DynamicConfiguration> {

    DynamicConfiguration queryByVersion(DynamicConfiguration query);

}
