package com.tp.proxy.bse;

import java.util.Date;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.util.ExceptionUtils;
import com.tp.dto.common.FailInfo;
import com.tp.dto.common.ResultInfo;
import com.tp.model.bse.AttributeValue;
import com.tp.proxy.BaseProxy;
import com.tp.service.IBaseService;
import com.tp.service.bse.IAttributeValueService;
import com.tp.util.CodeCreateUtil;
/**
 * 属性值 表代理层
 * @author szy
 *
 */
@Service
public class AttributeValueProxy extends BaseProxy<AttributeValue>{

	@Autowired
	private IAttributeValueService attributeValueService;
	@Autowired
	private ForbiddenWordsProxy forbiddenWordsProxy;
	
	@Override
	public IBaseService<AttributeValue> getService() {
		return attributeValueService;
	}
	
	public ResultInfo<?> saveAttrValue(AttributeValue attributeValue){
		String name = attributeValue.getName();
		try{
			if(StringUtils.isBlank(name)){
				throw new Exception("属性值名称必填");
			}
			forbiddenWordsProxy.checkForbiddenWordsField(name, "属性值名称");
	
			AttributeValue searchValue =new AttributeValue();
			searchValue.setName(name.trim());
			searchValue.setAttributeId(attributeValue.getAttributeId());
			List<AttributeValue> list = attributeValueService.queryByObject(searchValue);
			if(CollectionUtils.isNotEmpty(list)){
				throw new Exception("此属性组下存在相同的属性值名称");
			}
			AttributeValue value=new AttributeValue();
			value.setAttributeId(attributeValue.getAttributeId());
			value.setCreateTime(new Date());
			value.setModifyTime(new Date());
			value.setStatus(attributeValue.getStatus());
			value.setName(name.trim());
			value.setCode(CodeCreateUtil.initAttributeValue());
			attributeValueService.insert(value);
		}catch(Exception exception){
			FailInfo failInfo = ExceptionUtils.print(new FailInfo(exception), logger,attributeValue);
			return new ResultInfo<>(failInfo);
		}
		return new ResultInfo<>(Boolean.TRUE);
	}
	
	
	public List<AttributeValue>  selectDynamicByAttributeValue(AttributeValue attributeValue){
		 return  attributeValueService.queryByObject(attributeValue);
	}

	public void atttrValueEditSubmit(Long[] ids, Integer[] allStatus) {
		AttributeValue attributeValue = new AttributeValue();
		for(int i=0;i<ids.length;i++){
			attributeValue.setId(ids[i]);
			attributeValue.setStatus(allStatus[i]);
			attributeValueService.updateNotNullById(attributeValue);
		}
	}
	
}
