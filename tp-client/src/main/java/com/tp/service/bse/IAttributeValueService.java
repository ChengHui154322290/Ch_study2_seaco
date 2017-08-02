package com.tp.service.bse;

import java.util.List;

import com.tp.model.bse.AttributeValue;
import com.tp.service.IBaseService;
/**
  * @author szy
  * 属性值 表接口
  */
public interface IAttributeValueService extends IBaseService<AttributeValue>{
	List<AttributeValue> queryByIdsAndStatus(List<Long> attributeValueIdList, Integer status);
}
