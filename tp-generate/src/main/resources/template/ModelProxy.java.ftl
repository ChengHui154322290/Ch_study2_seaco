package com.tp.proxy.${modelFixed};

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.model.${modelFixed}.${modelName};
import com.tp.proxy.BaseProxy;
import com.tp.service.IBaseService;
import com.tp.service.${modelFixed}.I${modelName}Service;
/**
 * ${modelMark}代理层
 * @author szy
 *
 */
@Service
public class ${modelName}Proxy extends BaseProxy<${modelName}>{

	@Autowired
	private I${modelName}Service ${modelNameMin}Service;

	@Override
	public IBaseService<${modelName}> getService() {
		return ${modelNameMin}Service;
	}
}
