package com.tp.service.bse;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.tp.common.dao.BaseDao;
import com.tp.common.vo.Constant;
import com.tp.common.vo.DAOConstant.MYBATIS_SPECIAL_STRING;
import com.tp.dao.bse.CategoryAttributeLinkDao;
import com.tp.dao.bse.CategoryCertLinkDao;
import com.tp.dao.bse.CategoryDao;
import com.tp.dao.bse.CategorySpecGroupLinkDao;
import com.tp.dao.bse.DictionaryInfoDao;
import com.tp.model.bse.Attribute;
import com.tp.model.bse.AttributeValue;
import com.tp.model.bse.Category;
import com.tp.model.bse.CategoryAttributeLink;
import com.tp.model.bse.CategoryCertLink;
import com.tp.model.bse.CategorySpecGroupLink;
import com.tp.model.bse.DictionaryInfo;
import com.tp.redis.util.JedisCacheUtil;
import com.tp.result.bse.AttributeResult;
import com.tp.result.bse.CategoryListResult;
import com.tp.result.bse.CategoryResult;
import com.tp.service.BaseService;
import com.tp.service.bse.IAttributeService;
import com.tp.service.bse.IAttributeValueService;
import com.tp.service.bse.ICategoryService;
import com.tp.util.StringUtil;

@Service
public class CategoryService extends BaseService<Category> implements ICategoryService {

	@Autowired
	private CategoryDao categoryDao;
	@Autowired
	private CategoryCertLinkDao categoryCertLinkDao;
	@Autowired
	private CategoryAttributeLinkDao categoryAttributeLinkDao;
	@Autowired
	private CategorySpecGroupLinkDao categorySpecGroupLinkDao;
	
	@Autowired
	private DictionaryInfoDao dictionaryInfoDao;
	
	@Autowired
	private IAttributeService attributeService;
	@Autowired
	private IAttributeValueService attributeValueService;
	@Autowired
	private JedisCacheUtil jedisCacheUtil;
	
	@Override
	public BaseDao<Category> getDao() {
		return categoryDao;
	}

	/**
	 * 根据 id 集合和状态 返回  所有匹配Category;
	 * @param ids
	 * @param status 0:仅无效  , 1: 仅有效, 2:查全部 . 如果状态 为其它值, 返回 null;
	 * @return
	 * @throws BaseServiceException
	 */
	public List<Category> queryCategoryByParams(List<Long> ids, Integer status){
		if(CollectionUtils.isEmpty(ids)){
			return new ArrayList<Category>();
		}
		for(int i=0;i<ids.size();i++){
			if(null==ids.get(i)){
				ids.remove(i);
				i--;
			}
		}
		if(CollectionUtils.isEmpty(ids)){
			return new ArrayList<Category>();
		}
		Map<String,Object> params = new HashMap<String,Object>();
		params.put(MYBATIS_SPECIAL_STRING.COLUMNS.name(), " id in ("+StringUtil.join(ids, Constant.SPLIT_SIGN.COMMA)+")");
		params.put("status", status);
		return categoryDao.queryByParamNotEmpty(params);
	}
	
	public String getCategoryCode(Category category){
		String code = categoryDao.getAutoCode(category);
		if (code != null) {
			Integer level = category.getLevel();
			if (level == 1) {
				int i = Integer.parseInt(code);
				i = i + 1;
				if (i < 10) {
					return "0" + i;
				} else {
					return i + "";
				}
			} else {
				StringBuffer buffer = new StringBuffer(code.substring(0,code.length() - 2));
				int i = Integer.parseInt(code.substring(code.length() - 2));
				i = i + 1;
				if (i < 10) {
					return buffer.append("0" + i).toString();
				} else {
					return buffer.append(i).toString();
				}
			}
		} else {
			return null;
		}
	}
	
	public String getAncestorsNameStr(Long categoryId) {
		if (null==categoryId || categoryId < 0) {
			return null;
		}
		StringBuilder sb = new StringBuilder();
		LinkedList<String> list = new LinkedList<String>();

		for (;;) {
			Category me = queryById(categoryId);

			String categoryNames = me.getFullName();
			list.push(categoryNames);
			list.push(">>");

			if (me.isTopLevel()) {// 如果有 父级类别 , 继续循环,直到大类.
				break;
			} else {
				categoryId = me.getParentId();
			}
		}

		int n = list.size();
		for (int i = 0; i < n; i++) {
			sb.append(list.removeFirst());
		}
		return sb.substring(2, sb.length());
	}
	
	public List<DictionaryInfo> selectCategoryCertsByCategoryId(Long categoryId) {
	    if(null==categoryId){
		   return null;
	    }
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("categoryId", categoryId);
		List<CategoryCertLink> baseCategoryCertLinks = categoryCertLinkDao.queryByParam(params);
		if(CollectionUtils.isEmpty(baseCategoryCertLinks)){
			return null;
		}
		List<Long> dictIds = new ArrayList<Long>();
		for (CategoryCertLink certLinkDO : baseCategoryCertLinks) {
			dictIds.add(certLinkDO.getDictionaryInfoId());
		}
		params.clear();
		params.put(MYBATIS_SPECIAL_STRING.COLUMNS.name(), "id in ("+StringUtil.join(dictIds, Constant.SPLIT_SIGN.COMMA)+")");
		List<DictionaryInfo> dictionaryInfoDOs = dictionaryInfoDao.queryByParam(params);
		
		return dictionaryInfoDOs;
	}
	
	/**
	 * 返回 Category 和 它的属性 和 属性值
	 * 
	 * @param categoryId
	 * @param status
	 *            0:仅查无效, 1: 仅查有效, 2:查全部 . 如果状态 为其它值, 返回 null;
	 * @return
	 */
	@Override
	public CategoryResult getAttributeAndValues(Long categoryId, int status) {
		if (null == categoryId) {
			return null;
		}

		Integer state = 1;
		if (status == 0) {
			state = 0;
		} else if (status == 2) {
			state = null;
		}

		CategoryResult categoryResult = new CategoryResult();
		List<AttributeResult> attributeResultList = new ArrayList<AttributeResult>();
		Category category = queryById(categoryId);
		if (null == category) {
			return null;
		}
		categoryResult.setCategory(category);
		@SuppressWarnings("unchecked")
		ArrayList<Long> alerdayRelatedAttrId = (ArrayList<Long>) jedisCacheUtil.getCache("CATEGORY_ATTRIBUTE" + categoryId);
		if (alerdayRelatedAttrId != null) {
			for (Long attrId : alerdayRelatedAttrId) {
				AttributeResult attrResult = new AttributeResult();
				Attribute attribute = attributeService.queryById(attrId);
				if (!(null != attribute && (null == state || (state.equals(attribute.getStatus()))))) {
					continue;
				}
				attrResult.setAttribute(attribute);
				@SuppressWarnings("unchecked")
				ArrayList<Long> lists = (ArrayList<Long>) jedisCacheUtil.getCache("ATTRIBUTE_LINKED"+ attrId);
				List<AttributeValue> dos = new ArrayList<AttributeValue>();
				if (CollectionUtils.isNotEmpty(lists)) {
					for (Long attrValueId : lists) {
						AttributeValue attributeValue = attributeValueService.queryById(attrValueId);
						if (attributeValue != null) {
							Integer bool = attributeValue.getStatus();
							selectResultMethod(status, dos, attributeValue,bool);
						}
					}
				} else {
					AttributeValue attrVaDo = new AttributeValue();
					attrVaDo.setAttributeId(attrId);
					List<AttributeValue> attributeValueList = attributeValueService.queryByObject(attrVaDo);
					List<Long> attrVa = new ArrayList<Long>();
					for (AttributeValue attributeValue : attributeValueList) {
						Integer bool = attributeValue.getStatus();
						attrVa.add(attributeValue.getId());
						selectResultMethod(status, dos, attributeValue, bool);
						jedisCacheUtil.setCache("ATTRIBUTE_LINKED"+ attrId, attrVa, Integer.MAX_VALUE);
					}
				}
				attrResult.setAttributeValues(dos);
				attributeResultList.add(attrResult);
			}
			categoryResult.setAttributesAndValues(attributeResultList);
			return categoryResult;
		}
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("categoryId", categoryId);
		List<CategoryAttributeLink> categoryAttributeLinkList = categoryAttributeLinkDao.queryByParam(params);
		List<Long> insertList = new ArrayList<Long>();
		for (CategoryAttributeLink catAttLinkDO : categoryAttributeLinkList) {
			AttributeResult attributeResult = new AttributeResult();
			Long attributeId = catAttLinkDO.getAttributeId();
			Attribute attribute = attributeService.queryById(attributeId);
			if (!(null != attribute && (null == state || state.equals(attribute.getStatus())))) {
				continue;
			}
			attributeResult.setAttribute(attribute);
			insertList.add(attributeId);
			@SuppressWarnings("unchecked")
			List<Long> releted = (ArrayList<Long>) jedisCacheUtil.getCache("ATTRIBUTE_LINKED" + attributeId);
			List<AttributeValue> attributeValueList = new ArrayList<AttributeValue>();
			if (CollectionUtils.isNotEmpty(releted)) {
				for (Long long1 : releted) {
					AttributeValue attributeValue = attributeValueService.queryById(long1);
					if (attributeValue != null) {
						Integer bool = attributeValue.getStatus();
						selectResultMethod(status, attributeValueList,attributeValue, bool);
					}
				}
			} else {
				AttributeValue valueDO = new AttributeValue();
				valueDO.setAttributeId(attributeId);
				List<AttributeValue> list2 = attributeValueService.queryByObject(valueDO);
				List<Long> attrVa = new ArrayList<Long>();
				for (AttributeValue attributeValue2 : list2) {
					Integer bool = attributeValue2.getStatus();
					attrVa.add(attributeValue2.getId());
					selectResultMethod(status, attributeValueList,attributeValue2, bool);
					jedisCacheUtil.setCache("ATTRIBUTE_LINKED"+ attributeId, attrVa, Integer.MAX_VALUE);
				}
			}
			attributeResult.setAttributeValues(attributeValueList);
			attributeResultList.add(attributeResult);
		}
		categoryResult.setAttributesAndValues(attributeResultList);
		jedisCacheUtil.setCache("CATEGORY_ATTRIBUTE" + categoryId,insertList, Integer.MAX_VALUE);
		return categoryResult;
	}

	private void selectResultMethod(int status,List<AttributeValue> attributeValueList,AttributeValue attributeValue2, Integer bool) {
		if (1==bool) {
			switch (status) {
			case 0:
				break;
			case 1:
				attributeValueList.add(attributeValue2);
				break;
			case 2:
				attributeValueList.add(attributeValue2);
				break;
			default:
				break;
			}
		} else {
			switch (status) {
			case 0:
				attributeValueList.add(attributeValue2);
				break;
			case 1:
				break;
			case 2:
				attributeValueList.add(attributeValue2);
				break;
			default:
				break;
			}
		}
	}
	
	@Override
	public void deleteCateAttrLinked(Long cateId, Long attrId) {
		if(null==cateId || null==attrId){
			return;
		}
		CategoryAttributeLink categoryAttributeLink = new CategoryAttributeLink();
		categoryAttributeLink.setCategoryId(cateId);
		categoryAttributeLink.setAttributeId(attrId);
		List<CategoryAttributeLink> list = categoryAttributeLinkDao.queryByObject(categoryAttributeLink);
		if (CollectionUtils.isEmpty(list)) {
			logger.info("数据库不存在小类和属性的关系异常");
			return;
		}
		categoryAttributeLinkDao.deleteById(list.get(0).getId());
		@SuppressWarnings("unchecked")
		ArrayList<Long> existList = (ArrayList<Long>) jedisCacheUtil.getCache("CATEGORY_ATTRIBUTE"+ cateId);
		if (CollectionUtils.isEmpty(existList)) {
			return;
		}
		List<Long> array = new ArrayList<Long>();
		for (Long long1 : existList) {
			if (!attrId.equals(long1)) {
				array.add(long1);
			}
		}
		jedisCacheUtil.setCache("CATEGORY_ATTRIBUTE" + cateId, array,Integer.MAX_VALUE);
	}
	
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void updateCateAttrLinked(Long cateId, Long[] ids) {
		if(null==cateId){
			return;
		}
		CategoryAttributeLink categoryAttributeLink=new CategoryAttributeLink();
		categoryAttributeLink.setCategoryId(cateId);	
		List<CategoryAttributeLink> list = categoryAttributeLinkDao.queryByObject(categoryAttributeLink);
		/**
		 * 暂时未写批量删除,先使用for循环
		 */
		for(CategoryAttributeLink attributeLink :list){
			Long id = attributeLink.getId();
			categoryAttributeLinkDao.deleteById(id);
		}
		jedisCacheUtil.setCache("CATEGORY_ATTRIBUTE"+cateId, new ArrayList<Long>(), Integer.MAX_VALUE);
		
		/**
		 * 暂时未写批量插入,先使用for循环
		 */
		if(null==ids || ids.length==0){
			return;
		}
		ArrayList<Long> array =new ArrayList<Long>();
		for(int i=0; i<ids.length;i++){
			array.add(ids[i]);
			CategoryAttributeLink attributeLink=new CategoryAttributeLink();
			attributeLink.setCategoryId(cateId);
			attributeLink.setAttributeId(ids[i]);
			attributeLink.setCreateTime(new Date());
			attributeLink.setModifyTime(new Date());
			categoryAttributeLinkDao.insert(attributeLink);
		}
		jedisCacheUtil.setCache("CATEGORY_ATTRIBUTE"+cateId, array, Integer.MAX_VALUE);

	}
	
	@Override
	public void updateCateCertLinked(Long cateId, Long[] ids) {
		if(null==cateId){
			return;
		}
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("categoryId", cateId);
		categoryCertLinkDao.deleteByParam(params);
		if(null==ids||ids.length==0){
			return;
		}
		Date now = new Date();
		List<CategoryCertLink> baseCategoryCertLinks = new ArrayList<CategoryCertLink>();
		for (Long id : ids) {
			CategoryCertLink categoryCertLink = new CategoryCertLink();
			categoryCertLink.setCategoryId(cateId);
			categoryCertLink.setCreateTime(now);
			categoryCertLink.setDictionaryInfoId(id);
			baseCategoryCertLinks.add(categoryCertLink);
		}
		categoryCertLinkDao.batchInsert(baseCategoryCertLinks);
	}
	
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void updateCataSpecGroupLinked(Long cateId, Long[] ids){
		if(null==cateId){
			return ;
		}
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("categoryId", cateId);
		List<CategorySpecGroupLink> list = categorySpecGroupLinkDao.queryByParam(params);
		/**
		 * 暂时批量删除
		 */
		for(CategorySpecGroupLink cateSpec : list){
			Long id = cateSpec.getId();
			categorySpecGroupLinkDao.deleteById(id);
		}
		
		jedisCacheUtil.setCache("CATEGORY_SPECGROUP"+cateId, new ArrayList<Long>(), Integer.MAX_VALUE);
		if(null==ids || ids.length==0){
			return;
		}
		List<Long>  insertIds=new ArrayList<Long>();
		for(int i=0;i<ids.length;i++){
			insertIds.add(ids[i]);
			CategorySpecGroupLink groupLink =new CategorySpecGroupLink();
			groupLink.setCategoryId(cateId);
			groupLink.setSpecGroupId(ids[i]);
			groupLink.setCreateTime(new Date());
			groupLink.setModifyTime(new Date());
			categorySpecGroupLinkDao.insert(groupLink);
		}
		jedisCacheUtil.setCache("CATEGORY_SPECGROUP"+cateId, insertIds, Integer.MAX_VALUE);
	}

	@Override
	public List<CategoryListResult> getdescendantCategoryListByIds(List<Long> categoryIds) {
		if(CollectionUtils.isEmpty(categoryIds)){
			return null;
		}
		Set<Long> set =new HashSet<Long>(categoryIds);
		Category category =new Category();
		category.setStatus(Constant.ENABLED.YES);
		List<Category> listOfCategory = this.queryByObject(category);
		List<CategoryListResult> resultList =new ArrayList<CategoryListResult>();
		for (Long long1 : set) {
			if(null==long1){
				continue;
			}
			List<Category> result =new ArrayList<Category>();
			CategoryListResult categoryListResult =new CategoryListResult();
			for (Category cate : listOfCategory) {
				if(long1.equals(cate.getParentId())){
					result.add(cate);
				}
			}
			categoryListResult.setCategoryId(long1);
			categoryListResult.setDescendantCategory(result);
			resultList.add(categoryListResult);
		}
		return resultList;
	}
	/**
	 * 根据 id 集合和状态 返回  所有匹配CategoryDO;
	 * @param ids
	 * @param status 0:仅无效  , 1: 仅有效, 2:查全部 . 如果状态 为其它值, 返回 null;
	 * @return
	 */
	@Override
	public List<Category> selectByIdsAndStatus(List<Long> ids,  Integer status){
		if(CollectionUtils.isEmpty(ids)){
			return new ArrayList<Category>();
		}
		Map<String,Object> params = new HashMap<String,Object>();
		params.put(MYBATIS_SPECIAL_STRING.COLUMNS.name(), " id in ("+StringUtil.join(ids, Constant.SPLIT_SIGN.COMMA)+")");
		List<Category> catList=super.queryByParam(params);	
		if(status==2){
			return catList;
		}
		List<Category> des=new ArrayList<Category>();
		for(Category cat: catList){
			if(cat.getStatus().equals(status)){
				des.add(cat);
			}
		}
		
		return des;
	}

	@Override
	public List<Category> getFirstCategoryList() {
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("parentId", 0);
		params.put("status", Constant.ENABLED.YES);
		return super.queryByParam(params);
	}

	@Override
	public List<Long> findSmallCateIdListById(Long id) {
		return null;
	}
	@Override
	public List<Category> getParentCategoryList(Long id) {
		
		List<Category> list = new ArrayList<Category>();
		if (null == id) {
			return list;
		}
		Category cate = this.queryById(id);

		if (null == cate) {
			return list;
		}
		Integer level = cate.getLevel();
		if (level.intValue() == 1) {
			list.add(cate);
			return list;
		} else if (level.intValue() == 2) {
			Category largeCate = this.queryById(cate.getParentId());
			if (null == largeCate) {
				return list;
			}
			list.add(largeCate);
			list.add(cate);
			return list;
		} else if (level.intValue() == 3) {
			Category middleCate = this.queryById(cate.getParentId());
			if (null == middleCate) {
				return list;
			}
			Category largeCate = this.queryById(middleCate.getParentId());
			if (null == largeCate) {
				return list;
			}
			list.add(largeCate);
			list.add(middleCate);
			list.add(cate);
			return list;
		} else {
			return list;
		}
	}
}
