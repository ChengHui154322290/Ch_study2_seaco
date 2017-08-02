package com.tp.proxy.bse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.model.bse.Tags;
import com.tp.proxy.BaseProxy;
import com.tp.service.IBaseService;
import com.tp.service.bse.ITagsService;
/**
 * 标签信息表代理层
 * @author szy
 *
 */
@Service
public class TagsProxy extends BaseProxy<Tags>{

	@Autowired
	private ITagsService tagsService;

	@Override
	public IBaseService<Tags> getService() {
		return tagsService;
	}
}
