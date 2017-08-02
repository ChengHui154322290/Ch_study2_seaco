package com.tp.proxy.bse;

import java.util.Date;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.dto.common.FailInfo;
import com.tp.dto.common.ResultInfo;
import com.tp.model.bse.DictionaryCategory;
import com.tp.proxy.BaseProxy;
import com.tp.service.IBaseService;
import com.tp.service.bse.IDictionaryCategoryService;
/**
 * 数据字典：类别代理层
 * @author szy
 *
 */
@Service
public class DictionaryCategoryProxy extends BaseProxy<DictionaryCategory>{

	@Autowired
	private IDictionaryCategoryService dictionaryCategoryService;
	
	@Autowired
	private ForbiddenWordsProxy forbiddenWordsProxy;

	@Override
	public IBaseService<DictionaryCategory> getService() {
		return dictionaryCategoryService;
	}

	/**
	 * 
	 * <pre>
	 * 增加DictionaryCategory
	 * </pre>
	 *
	 * @param DictionaryCategory
	 */
	public ResultInfo<DictionaryCategory> addDictionaryCategory(DictionaryCategory dictionaryCategory) throws Exception {
		String code = dictionaryCategory.getCode();
		if(StringUtils.isBlank(code)){
			return new ResultInfo<>(new FailInfo("code码必填"));
		}
		DictionaryCategory category =new DictionaryCategory();
		category.setCode(code.trim());
		List<DictionaryCategory> listOne = dictionaryCategoryService.queryByObject(category);
	
		if (CollectionUtils.isNotEmpty(listOne)) {
			return new ResultInfo<>(new FailInfo("存在相同的种类code码"));
		}
		String name = dictionaryCategory.getName();
		if(StringUtils.isBlank(name)){
			return new ResultInfo<>(new FailInfo("种类名称必填"));
		}

		forbiddenWordsProxy.checkForbiddenWordsField(name, "种类名称");
		DictionaryCategory categorySecond=new DictionaryCategory();
		categorySecond.setName(name.trim());
		List<DictionaryCategory> listTwo = dictionaryCategoryService.queryByObject(categorySecond);
	
		if(CollectionUtils.isNotEmpty(listTwo)){
			return new ResultInfo<>(new FailInfo("存在相同的种类名称"));
		}
		
		DictionaryCategory insertCategory =new DictionaryCategory();
		insertCategory.setCode(dictionaryCategory.getCode().trim());
		insertCategory.setName(dictionaryCategory.getName().trim());
		insertCategory.setCreateTime(new Date());
		insertCategory.setModifyTime(new Date());
		dictionaryCategoryService.insert(insertCategory);
		return new ResultInfo<>(insertCategory);
	}

	/**
	 * 
	 * <pre>
	 * 更新DictionaryCategory
	 * </pre>
	 *
	 * @param DictionaryCategory
	 * @param isAllField
	 */
	public ResultInfo<DictionaryCategory> updateDictionaryCategory(DictionaryCategory dictionaryCategory, Boolean isAllField) throws Exception {
        String code = dictionaryCategory.getCode();
		if(StringUtils.isBlank(code)){
			return new ResultInfo<>(new FailInfo("code码必填"));
		}
		
		DictionaryCategory category =new DictionaryCategory();
		category.setCode(code.trim());
		List<DictionaryCategory> listOne = dictionaryCategoryService.queryByObject(category);
	     
	    for(DictionaryCategory category1:listOne){
	        Long id = category1.getId();
	        if(id != dictionaryCategory.getId()){
	        	return new ResultInfo<>(new FailInfo("存在相同的种类code码"));
	        }
	    }	
		
		String name = dictionaryCategory.getName();
	
		if(StringUtils.isBlank(name)){
			return new ResultInfo<>(new FailInfo("种类名称必填"));
		}
		forbiddenWordsProxy.checkForbiddenWordsField(name, "种类名称");
		DictionaryCategory categorySecond=new DictionaryCategory();
		categorySecond.setName(name.trim());
		List<DictionaryCategory> listTwo = dictionaryCategoryService.queryByObject(categorySecond);
	
		for(DictionaryCategory category1:listTwo){
	        Long id = category1.getId();
	        if(!id.equals(dictionaryCategory.getId())){
	        	return new ResultInfo<>(new FailInfo("存在相同的种类名称"));
	        }
	    }	
		
		DictionaryCategory insertCategory =new DictionaryCategory();
		insertCategory.setId(dictionaryCategory.getId());
		insertCategory.setCode(dictionaryCategory.getCode().trim());
		insertCategory.setName(dictionaryCategory.getName().trim());
		insertCategory.setModifyTime(new Date());
		if(isAllField){
			dictionaryCategoryService.updateById(insertCategory);
		}else{
			dictionaryCategoryService.updateNotNullById(insertCategory);
		}
		return new ResultInfo<>(insertCategory);
	}

}
