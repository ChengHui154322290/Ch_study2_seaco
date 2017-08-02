package com.tp.dao.cms;

import java.util.List;

import com.tp.common.dao.BaseDao;
import com.tp.model.cms.PictureElement;

public interface PictureElementDao extends BaseDao<PictureElement> {

	int deleteByIds(List<Long> ids);

}
