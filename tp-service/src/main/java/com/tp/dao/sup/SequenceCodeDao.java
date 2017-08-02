package com.tp.dao.sup;

import org.apache.ibatis.annotations.Param;

import com.tp.common.dao.BaseDao;
import com.tp.model.sup.SequenceCode;

public interface SequenceCodeDao extends BaseDao<SequenceCode> {

	Long getCurrentCode(@Param("templateType")String templateType);

	Integer codeAdd(@Param("templateType")String templateType);

}
