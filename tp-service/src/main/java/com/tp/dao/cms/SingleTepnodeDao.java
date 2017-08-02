package com.tp.dao.cms;

import java.util.List;

import com.tp.common.dao.BaseDao;
import com.tp.dto.cms.CmsSingleTempleDTO;
import com.tp.model.cms.SingleTepnode;

public interface SingleTepnodeDao extends BaseDao<SingleTepnode> {

	List<CmsSingleTempleDTO> selectSingleActivity(SingleTepnode singleTepnode);

	int deleteByIds(List<Long> ids);

	List<CmsSingleTempleDTO> selectDynamic(CmsSingleTempleDTO cmsSingleTempleDTO);

}
