package com.tp.dao.cms;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.tp.common.dao.BaseDao;
import com.tp.model.cms.ActivityElement;
import com.tp.model.cms.DefinedElement;
import com.tp.model.cms.PictureElement;
import com.tp.model.cms.Position;
import com.tp.model.cms.Temple;
import com.tp.model.cms.WrittenElement;

public interface PositionDao extends BaseDao<Position> {

	long selectIsExists(List<Long> ids, boolean b);

	List<Temple> selectTempletByCounts(Temple cmsTempleDO);

	int deleteByIds(List<Long> ids);

	List<ActivityElement> selectActivityByTempletId(Temple cmsTempleDO) throws Exception;
	
	List<ActivityElement> selectActivityByTempletPage(Temple cmsTempleDO) throws Exception;
	
	List<WrittenElement> selectWrittenByTempletId(Temple cmsTempleDO) throws Exception;
	
	List<PictureElement> selectPicByTempletId(Temple cmsTempleDO) throws Exception;
	
	List<DefinedElement> selectDefinedByTempletId(Temple cmsTempleDO) throws Exception;

	List<Temple> selectTempletByTempletCode(Temple cmsTempleDO);

	List<ActivityElement> selectActivityByTempletsPage(Map<String, Object> params);
	
	List<ActivityElement> selectActivityByTempletsPageForDSS(Map<String, Object> params);

}
