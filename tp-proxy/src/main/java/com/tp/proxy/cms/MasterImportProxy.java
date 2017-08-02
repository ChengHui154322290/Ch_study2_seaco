package com.tp.proxy.cms;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.model.cms.ActivityElement;
import com.tp.proxy.BaseProxy;
import com.tp.service.IBaseService;
import com.tp.service.cms.IActivityElementService;
/**
 * 活动元素表代理层
 * @author szy
 *
 */
@Service
public class MasterImportProxy extends BaseProxy{

	@Override
	public IBaseService getService() {
		return null;
	}
}
