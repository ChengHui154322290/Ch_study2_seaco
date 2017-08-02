package com.tp.seller.ao.base;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.vo.Constant;
import com.tp.common.vo.PageInfo;
import com.tp.model.bse.Category;
import com.tp.model.bse.DictionaryInfo;
import com.tp.result.bse.CategoryResult;
import com.tp.service.bse.ICategoryService;

@Service
public class CategoryAO {
	
	
	@Autowired
	private ICategoryService categoryService;
	
	public List<DictionaryInfo> selectCategoryCertByCat(Long categoryId){
		
		return categoryService.selectCategoryCertsByCategoryId(categoryId);
	}
	
	public PageInfo<Category> getAllType(){
		Category categoryDO = new Category();
		categoryDO.setParentId(0l); //通过父节点查询下面的商品分类
		return categoryService.queryPageByObject(categoryDO,new PageInfo<Category>());
	}
	
	public Category queryById( Long id){
		return categoryService.queryById(id);
	}
	
	/**
	 * 查询大类
	 * @return
	 */
	public List<Category> getFirstCategoryList() {
		Category categoryDO = new Category();
	//	categoryDO.setStatus(true);
		categoryDO.setParentId(0L);
		categoryDO.setStatus(Constant.ENABLED.YES);
		return categoryService.queryByObject(categoryDO);
	}

 	
	/**
	 * 根据categoryId 获得 所有attribute 和attribute value
	 * @param catId
	 * @return
	 */
	public CategoryResult selectAttrsAndValuesByCatId(Long catId){
		return categoryService.getAttributeAndValues(catId, 2);
	}
	
	
	/**
	 * 根据上级分类查询子类
	 * @param catId
	 * @return
	 */
	public List<Category> selectCldListById(Long catId) {
		Category categoryDO = new Category();
		categoryDO.setStatus(Constant.ENABLED.YES);
		categoryDO.setParentId(catId);
		return categoryService.queryByObject(categoryDO);
	}

}
