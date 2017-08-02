package com.tp.proxy.bse;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.vo.Constant;
import com.tp.common.vo.PageInfo;
import com.tp.common.vo.DAOConstant.MYBATIS_SPECIAL_STRING;
import com.tp.model.bse.Attribute;
import com.tp.model.bse.CategoryAttributeLink;
import com.tp.proxy.BaseProxy;
import com.tp.service.IBaseService;
import com.tp.service.bse.IAttributeService;
import com.tp.service.bse.ICategoryAttributeLinkService;
import com.tp.util.StringUtil;
/**
 * 属性值表代理层
 * @author szy
 *
 */
@Service
public class AttributeProxy extends BaseProxy<Attribute>{

	@Autowired
	private IAttributeService attributeService;
	
	@Autowired
	private  ICategoryAttributeLinkService categoryAttributeLinkService;

	@Autowired
	private ForbiddenWordsProxy forbiddenWordsProxy;
	
	@Override
	public IBaseService<Attribute> getService() {
		return attributeService;
	}
	
	public List<Attribute> selectDynamicPageQueryInIds(List<Long> attributeIds){
		if(null==attributeIds){
			return new ArrayList<Attribute>();
		}
		Map<String,Object> params = new HashMap<String,Object>();
		params.put(MYBATIS_SPECIAL_STRING.COLUMNS.name(), " id in ("+StringUtil.join(attributeIds, Constant.SPLIT_SIGN.COMMA)+")");
		return attributeService.queryByParam(params);
	}
	
	/**
	 * 
	 * @param attribute
	 * @param startPage
	 * @param pageSize
	 * @return
	 */
	public PageInfo<Attribute> queryPageListByAttributeAndStartPageSize(Attribute attribute, int startPage, int pageSize){
		if(null==attribute){
			 return null;
		}
		PageInfo<Attribute> pageInfo = new PageInfo<Attribute>();
		pageInfo.setPage(startPage);
		pageInfo.setSize(pageSize);
		return attributeService.queryPageByObject(attribute,pageInfo);
		
	}
	
	/**
	 * 根据 id 返回 Attribute
	 * @param id
	 * @return
	 */
	public Attribute selectById(long id){
		if(id<=0){
		return null;
		}
		return attributeService.queryById(id);
	}
	
	public void addAttributeAndLinked(Attribute attribute, Long[] ids) throws Exception {
			String name = attribute.getName();
			if(StringUtils.isBlank(name)){
				throw new Exception("属性名称必填");
			}
			forbiddenWordsProxy.checkForbiddenWordsField(name, "属性名称");
			forbiddenWordsProxy.checkForbiddenWordsField(attribute.getRemark(), "备注");	
			Attribute searchAttr =new Attribute();
			searchAttr.setName(name.trim());
			List<Attribute> listOne = attributeService.queryByObject(searchAttr);	
			if(CollectionUtils.isNotEmpty(listOne)){
				throw new Exception("存在相同的属性名称");
			}		
			Attribute insertAttribute =new Attribute();
			insertAttribute.setRemark(attribute.getRemark().trim());
			insertAttribute.setName(attribute.getName().trim());
			insertAttribute.setCreateTime(new Date());
			insertAttribute.setModifyTime(new Date());
			insertAttribute.setStatus(attribute.getStatus());
			insertAttribute.setIsRequired(attribute.getIsRequired());
			insertAttribute.setAllowMultiSelect(attribute.getAllowMultiSelect());
			attributeService.addAttributeAndCateLinked(insertAttribute,ids);
	}
	
	/**
	 * 
	 * <pre>
	 * 插入Attribute及其相关联的种类
	 * </pre>
	 *
	 * @param Attribute
	 * @param ids
	 * @throws Exception
	 */

	public void updateAttribute(Attribute attribute, Long[] ids)  throws Exception{
		String name = attribute.getName();
		if(StringUtils.isBlank(name)){
			//"属性名称必填";
			return;
		}
		forbiddenWordsProxy.checkForbiddenWordsField(name, "属性名称");
		forbiddenWordsProxy.checkForbiddenWordsField(attribute.getRemark(), "备注");	
		Attribute searchAttr =new Attribute();
		searchAttr.setName(name.trim());
		List<Attribute> list = attributeService.queryByObject(searchAttr);
	
		if(CollectionUtils.isNotEmpty(list)){
			for(Attribute  attr:list){
		        Long id = attr.getId();
		        if(!id.equals(attribute.getId())){
		        	throw new Exception("存在相同属性名称,请跟换一个");
		        }
		    }
		}
		Attribute insertAttribute =new Attribute();
		insertAttribute.setRemark(attribute.getRemark().trim());
		insertAttribute.setName(attribute.getName().trim());
		insertAttribute.setModifyTime(new Date());
		insertAttribute.setStatus(attribute.getStatus());
		insertAttribute.setIsRequired(attribute.getIsRequired());
		insertAttribute.setAllowMultiSelect(attribute.getAllowMultiSelect());
		insertAttribute.setId(attribute.getId());
		attributeService.updateAttributeAndCateLinked(insertAttribute,ids); 
	}
	

	
	public List<CategoryAttributeLink> slectLinkInfo(Long catId) throws Exception {
		CategoryAttributeLink CategoryAttributeLink =new CategoryAttributeLink();
		CategoryAttributeLink.setCategoryId(catId);
		List<CategoryAttributeLink> list = categoryAttributeLinkService.queryByObject(CategoryAttributeLink);
		return list;
	}
	
	
	
	public  List<Attribute> selectDyamicCategory(Attribute attribute) {
		return attributeService.queryByObject(attribute);
		
	}

	public PageInfo<Attribute> queryPageListByLikeAttribute(Attribute attribute, Integer pageNo, Integer pageSize) {
		if(null==attribute){
			attribute=new Attribute();
		}
		PageInfo<Attribute> pageInfo = new PageInfo<Attribute>();
		return attributeService.queryPageByObject(attribute, pageInfo);
	}

}
