package com.tp.service.bse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.dao.BaseDao;
import com.tp.dao.bse.TagsDao;
import com.tp.model.bse.Tags;
import com.tp.service.BaseService;
import com.tp.service.bse.ITagsService;

@Service
public class TagsService extends BaseService<Tags> implements ITagsService {

	@Autowired
	private TagsDao tagsDao;
	
	@Override
	public BaseDao<Tags> getDao() {
		return tagsDao;
	}

}
