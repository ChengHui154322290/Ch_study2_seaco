package com.tp.service.app;

import com.tp.model.app.DynamicConfiguration;
import com.tp.service.IBaseService;

/**
 * @author szy
 *         接口
 */
public interface IDynamicConfigurationService extends IBaseService<DynamicConfiguration> {

    DynamicConfiguration queryByVersion(DynamicConfiguration query);

}
