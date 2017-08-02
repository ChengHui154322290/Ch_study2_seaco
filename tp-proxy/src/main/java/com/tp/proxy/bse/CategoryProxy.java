package com.tp.proxy.bse;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.util.ExceptionUtils;
import com.tp.common.vo.Constant;
import com.tp.dto.common.FailInfo;
import com.tp.dto.common.ResultInfo;
import com.tp.model.bse.Category;
import com.tp.model.bse.CategorySpecGroupLink;
import com.tp.model.bse.DictionaryInfo;
import com.tp.proxy.BaseProxy;
import com.tp.result.bse.CategoryListResult;
import com.tp.result.bse.CategoryResult;
import com.tp.service.IBaseService;
import com.tp.service.bse.ICategoryService;
import com.tp.service.bse.ICategorySpecGroupLinkService;
/**
 * 商品类别代理层
 * @author szy
 *
 */
@Service
public class CategoryProxy extends BaseProxy<Category>{

	@Autowired
	private ICategoryService categoryService;
	@Autowired
	private ICategorySpecGroupLinkService categorySpecGroupLinkService;
	@Autowired
	private ForbiddenWordsProxy forbiddenWordsProxy;

	@Override
	public IBaseService<Category> getService() {
		return categoryService;
	}
	
	/**
	 * 
	 * <pre>
	 *  添加种类
	 * </pre>
	 *
	 * @param category
	 * @param parentCode
	 * @throws Exception
	 */
	public Category addCatgory(Category category,String parentCode) throws Exception {
		if(null==category.getParentId()){
			throw new Exception("数据异常");
		}
		String name = category.getName();
		if(StringUtils.isBlank(name)){
			throw new Exception("名称必填");
		} 
		forbiddenWordsProxy.checkForbiddenWordsField(name, "品类名称");
		forbiddenWordsProxy.checkForbiddenWordsField(category.getRemark(), "备注");	
		Category  searchCateGory=new Category();
		searchCateGory.setName(name.trim());
		searchCateGory.setParentId(category.getParentId());
		List<Category> list = categoryService.queryByObject(searchCateGory);
		if(CollectionUtils.isNotEmpty(list)){
			throw new Exception("存在相同的名称");
		}
		if(null==category.getLevel()||category.getLevel()>3 ||category.getLevel()<0 || category.getParentId()<0){
			throw new Exception("数据异常");
		}
		String code = categoryService.getCategoryCode(category);
		if (null==code) {
			if (null==parentCode ) {
				code = "01";
			} else {
				if (parentCode.length() == 1 || parentCode.length() == 3 || parentCode.length() == 5) {
					parentCode = "0" + parentCode;
				}
				code = parentCode + "01";
			}
		} else {
			if (code.length() == 3 || code.length() == 5 || code.length() == 7) {
				throw new Exception("已经达到分组的最大长度");
			}
		}
		Category insertCategory=new Category();
		insertCategory.setCode(code);
		insertCategory.setName(category.getName().trim());
		insertCategory.setLevel(category.getLevel());
		insertCategory.setCreateTime(new Date());
		insertCategory.setModifyTime(new Date());
		insertCategory.setParentId(category.getParentId());
		insertCategory.setStatus(category.getStatus());
		insertCategory.setRemark(category.getRemark().trim());
		categoryService.insert(insertCategory);
		return insertCategory;
	}
	
	public ResultInfo<List<Category>> queryCategoryByParams(List<Long> ids){
		try{
			return new ResultInfo<List<Category>>(categoryService.queryCategoryByParams(ids,null));
		}catch(Throwable exception){
			FailInfo failInfo = ExceptionUtils.print(new FailInfo(exception), logger,ids);
			return new ResultInfo<>(failInfo);
		}
	}

	public String getAncestorsNameStr(Long cateId) {
		return categoryService.getAncestorsNameStr(cateId);
	}
	
	/**
	 * 
	 * <pre>
	 * 返回所有的category
	 * </pre>
	 *
	 * @return
	 */
	
	public JSONObject getAllData(){
		Map<Long, HashMap<Long,ArrayList<Long>>> map=new HashMap<Long, HashMap<Long,ArrayList<Long>>>();
		List<Category> list = categoryService.queryByParam(new HashMap<String,Object>());
		List<Category> listOfBig=new ArrayList<Category>();
		List<Category> listOfMidd=new ArrayList<Category>();
		List<Category> listOfSmall=new ArrayList<Category>();
		for (Category category : list) {
			Integer level = category.getLevel();
			switch (level) {
			case 1:
				listOfBig.add(category);
				break;
			case 2:
				listOfMidd.add(category);
				break;
			case 3 :
				listOfSmall.add(category);
				break;
			default:
				break;
			}
		}
		for (Category category : listOfBig) {
			Long id = category.getId();
			map.put(id, new HashMap<Long, ArrayList<Long>>());
			for (Category category2 : listOfMidd) {
				Long id2 = category2.getId();
				Long parentId = category2.getParentId();
				if(parentId.equals(id)){
					HashMap<Long, ArrayList<Long>> hashMap = map.get(id);
					if(null==hashMap){
						hashMap=new HashMap<Long, ArrayList<Long>>();
					}
					hashMap.put(id2, new ArrayList<Long>());
					for (Category category3 : listOfSmall) {
						Long id3 = category3.getId();
						Long parentId2 = category3.getParentId();
						if (parentId2.equals(id2)) {
							ArrayList<Long> hashSet = hashMap.get(id2);
							if(null==hashSet){
								hashSet=new ArrayList<Long>();
							}
							hashSet.add(id3);
							hashMap.put(id2, hashSet);
						}
					}
					map.put(id, hashMap);
				}
				
			}
		}
		JSONArray rows = new JSONArray();
		for (Category category : listOfBig) {
			JSONObject row = insertRowsData(category);
			Long id = category.getId();
			if (map.get(id).size() > 0) {
				row.put("isLeaf", false);
				row.put("expanded", false);//可以为true
				rows.add(row);
				HashMap<Long, ArrayList<Long>> hashMap = map.get(id);
				Set<Long> keySet = hashMap.keySet();
			    if(null !=keySet){
				Object[] iterator = hashMap.keySet().toArray();
				Arrays.sort(iterator);
				for (int i = 0; i < iterator.length; i++) {
					Long key = (Long) iterator[i];
					Category cate = this.getCateById(list, key);
					JSONObject row2 = insertRowsData(cate);
					ArrayList<Long> value = hashMap.get(key);
					if (CollectionUtils.isNotEmpty(value)) {
						row2.put("isLeaf", false);
						row2.put("expanded", false);//可以为true
						rows.add(row2);
						for (Long long1 : value) {
							Category smallCate = this.getCateById(list, long1);
							JSONObject row3 = insertRowsData(smallCate);
							row3.put("isLeaf", true);
							row3.put("expanded", false);
							rows.add(row3);
						}
					} else {
						row2.put("isLeaf", true);
						row2.put("expanded", false);
						rows.add(row2);
					}

				}
			}
			} else {
				row.put("isLeaf", true);
				row.put("expanded", false);
				rows.add(row);
			}
		}
		JSONObject res=new JSONObject();
		res.put("rows", rows);
		res.put("records", rows.size());
		res.put("page", 1);
		res.put("total", 1);
		return res; 
	}

	public List<DictionaryInfo> selectCategoryCertByCat(Long categoryId){
		
		return categoryService.selectCategoryCertsByCategoryId(categoryId);
	}
	
	/**
	 * 
	 * <pre>
	 * 更新种类
	 * </pre>
	 *
	 * @param category
	 * @throws Exception
	 */
	public void updateCategory( Category category) throws Exception {
		if(null==category.getParentId()){
			throw new Exception("数据异常");
		}
		String name = category.getName();
		if(StringUtils.isBlank(name)){
			throw new Exception("名称必填");
		} 

		forbiddenWordsProxy.checkForbiddenWordsField(name, "品类名称");
		forbiddenWordsProxy.checkForbiddenWordsField(category.getRemark(), "备注");
		Category  searchCateGory=new Category();
		searchCateGory.setName(name.trim());
		searchCateGory.setParentId(category.getParentId());
		List<Category> list = categoryService.queryByObject(searchCateGory);
		if(CollectionUtils.isNotEmpty(list)){
			for(Category cate:list){
		        Long id = cate.getId();
		        if(!id.equals(category.getId())){
		           throw new Exception("存在相同的名称,请跟换一个");
		        }
		    }	
		}
		Category insertCate=new Category();
		insertCate.setId(category.getId());
		insertCate.setName(name.trim());
		insertCate.setStatus(category.getStatus());
		insertCate.setRemark(category.getRemark().trim());
		categoryService.updateNotNullById(insertCate);
	}
	
	/**
	 * 根据level 获得 其子类的 类名.
	 * @param level
	 * @return 如果level<1 或者 level>3 返回""
	 */
	public String getChildLevelName(int level){
		level++;

		if(level==1){
			return "大类";
		}else if(level==2){
			return "中类";
		}else if(level==3){
			return "小类";
		}else{
			return "";
		}
		
	}
	
	private Category getCateById(List<Category> list,Long id){
		for (Category category : list) {
			if(category.getId().equals(id)){
				return category;
			}
		}
		return null;	
	}
	
	private JSONObject insertRowsData(Category categoryLevelTwo) {
		JSONObject rowll = new JSONObject();
		rowll.put("id",categoryLevelTwo.getId());
		rowll.put("myLevelName", categoryLevelTwo.getMyLevelName());
		rowll.put("name", categoryLevelTwo.getName());
		Integer levell=categoryLevelTwo.getLevel()-1;
		rowll.put("level", levell);
		rowll.put("parentId",categoryLevelTwo.getParentId());
		rowll.put("code", categoryLevelTwo.getCode());
		rowll.put("remark", categoryLevelTwo.getRemark());
		rowll.put("status", categoryLevelTwo.getStatus());
		rowll.put("id2", categoryLevelTwo.getId());
		return rowll;
	}

	/**
	 * 根据categoryId 获得 所有attribute 和attribute value
	 * @param catId
	 * @return
	 */
	public CategoryResult selectAttrsAndValuesByCatId(Long catId){
		return categoryService.getAttributeAndValues(catId, 2);
	}

	public void updateCateAttrLinked(Long cateId, Long[] ids) {
		categoryService.updateCateAttrLinked(cateId,ids);
	}

	/**
	 * 
	 * <pre>
	 * 删除小类相关的属性组
	 * </pre>
	 *
	 * @param cateId
	 * @param ids
	 * @throws Exception
	 */
	public void deleteCateAttrLinked(Long cateId, Long attrId) {
		categoryService.deleteCateAttrLinked(cateId, attrId);	
	}

	public void updateCateCertLinked(Long cateId, Long[] ids) {
		categoryService.updateCateCertLinked(cateId,ids);
	}

	public void updateCataSpecGroupLinked(Long cateId, Long[] ids) {
		categoryService.updateCataSpecGroupLinked(cateId,ids);
	}

	public List<CategorySpecGroupLink> queryCurrentCataSpecGroupLinked(
			Long cateId) {
		CategorySpecGroupLink categorySpecGroupLink=new CategorySpecGroupLink();
		categorySpecGroupLink.setCategoryId(cateId);
		List<CategorySpecGroupLink> list = categorySpecGroupLinkService.queryByObject(categorySpecGroupLink);
		return list;
	}

	public Map<String, List<Category>> getCategorySiblings(List<Long> categoryIds) {
        final Map<String, List<Category>> cateogoryMap = new HashMap<String, List<Category>>();
        List<CategoryListResult> categoryListResult = null;
        try {
            categoryListResult = categoryService.getdescendantCategoryListByIds(categoryIds);
        } catch (final Exception e) {
            logger.error("商品信息异常：{}", categoryIds);
            logger.error(e.getMessage(), e);
        }
        if (null != categoryListResult && categoryListResult.size() > 0) {
            for (final CategoryListResult categoryResult : categoryListResult) {
                cateogoryMap.put(categoryResult.getCategoryId() + "", categoryResult.getDescendantCategory());
            }
        }
        return cateogoryMap;
    }

	public List<Category> getFirstCategoryList() {
        Map<String,Object> params = new HashMap<String,Object>();
        params.put("status", Constant.ENABLED.YES);
        params.put("parentId", 0);
        return categoryService.queryByParam(params);
    }
	public List<Category> selectByIdsAndStatus(List<Long> categoryIds,Integer status) {
		return categoryService.selectByIdsAndStatus(categoryIds,status);
	}

	public List<Category> selectCldListById(Long catId) {
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("status", Constant.ENABLED.YES);
		params.put("parentId", catId);
		return categoryService.queryByParam(params);
	}
}
