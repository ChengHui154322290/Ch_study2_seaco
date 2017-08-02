package com.tp.service.bse;

import java.util.List;

import com.tp.model.bse.Category;
import com.tp.model.bse.DictionaryInfo;
import com.tp.result.bse.CategoryListResult;
import com.tp.result.bse.CategoryResult;
import com.tp.service.IBaseService;
/**
  * @author szy
  * 商品类别接口
  */
public interface ICategoryService extends IBaseService<Category>{

	/**
	 * 根据 id 集合和状态 返回  所有匹配CategoryDO;
	 * @param ids
	 * @param status 0:仅无效  , 1: 仅有效, 2:查全部 . 如果状态 为其它值, 返回 null;
	 * @return
	 * @throws BaseServiceException
	 */
	List<Category> queryCategoryByParams(List<Long> ids, Integer status);

	/**
     * 
     * <pre>
     *  子类根据主键id获取上级(如果上级的上级存在,也返回)
     * </pre>
     *
     * @param categoryId
     * @return
     */
	public String getAncestorsNameStr(Long categoryId);

	List<DictionaryInfo> selectCategoryCertsByCategoryId(Long categoryId);

	/**
	 * 返回 Category 和 它的属性 和 属性值
	 * @param categoryId
	 * @param status 0:仅查无效, 1: 仅查有效, 2:查全部 
	 * @return
	 */
	CategoryResult getAttributeAndValues(Long categoryId, int status);
	
	String getCategoryCode(Category category);

	void updateCateAttrLinked(Long cateId, Long[] ids);

	void deleteCateAttrLinked(Long cateId, Long attrId);

	void updateCateCertLinked(Long cateId, Long[] ids);

	void updateCataSpecGroupLinked(Long cateId, Long[] ids);

	List<CategoryListResult> getdescendantCategoryListByIds(List<Long> categoryIds);
	
	/**
	 * 根据 id 集合和状态 返回  所有匹配CategoryDO;
	 * @param ids
	 * @param status 0:仅无效  , 1: 仅有效, 2:查全部 . 如果状态 为其它值, 返回 null;
	 * @return
	 */
	public List<Category> selectByIdsAndStatus(List<Long> ids,  Integer status);

	List<Category> getFirstCategoryList();

	List<Long> findSmallCateIdListById(Long id);

	/**
	 * @param id
	 * @return
	 */
	List<Category> getParentCategoryList(Long id);

}
