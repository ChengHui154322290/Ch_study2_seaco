package com.tp.dao.cms;

import java.util.List;

import com.tp.common.dao.BaseDao;
import com.tp.dto.cms.CmsSingleTempleDTO;
import com.tp.model.cms.SingleproTemple;

public interface SingleproTempleDao extends BaseDao<SingleproTemple> {

	List<CmsSingleTempleDTO> selectSingleTemples(CmsSingleTempleDTO cmsSingleTempleDTO);

	Long selectSingleTemplesCounts(CmsSingleTempleDTO cmsSingleTempleDTO);

	int forbiddenTempleByIds(List<Long> ids);

	List<CmsSingleTempleDTO> selectDateCountQuery(CmsSingleTempleDTO cmsSingleTempleDTO);

	List<CmsSingleTempleDTO> selectDateActivityIdQuery(CmsSingleTempleDTO mdd);

}
