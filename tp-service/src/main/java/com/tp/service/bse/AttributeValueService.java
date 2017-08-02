package com.tp.service.bse;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.dao.BaseDao;
import com.tp.dao.bse.AttributeValueDao;
import com.tp.model.bse.AttributeValue;
import com.tp.service.BaseService;
import com.tp.service.bse.IAttributeValueService;

@Service
public class AttributeValueService extends BaseService<AttributeValue> implements IAttributeValueService {

	@Autowired
	private AttributeValueDao attributeValueDao;
	
	@Override
	public BaseDao<AttributeValue> getDao() {
		return attributeValueDao;
	}

	public List<AttributeValue> queryByIdsAndStatus(List<Long> attributeValueIdList, Integer status) {
		// TODO Auto-generated method stub
		return null;
	}

}
