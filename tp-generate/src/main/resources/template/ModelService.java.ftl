package com.tp.service.${modelFixed};

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.dao.BaseDao;
import com.tp.dao.${modelFixed}.${modelName}Dao;
import com.tp.model.${modelFixed}.${modelName};
import com.tp.service.BaseService;

@Service
public class ${modelName}Service extends BaseService<${modelName}> implements I${modelName}Service {

	@Autowired
	private ${modelName}Dao ${modelNameMin}Dao;
	
	@Override
	public BaseDao<${modelName}> getDao() {
		return ${modelNameMin}Dao;
	}

}
