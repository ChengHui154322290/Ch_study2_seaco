package com.tp.service.bse;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.tp.common.dao.BaseDao;
import com.tp.dao.bse.AttributeDao;
import com.tp.dao.bse.CategoryAttributeLinkDao;
import com.tp.model.bse.Attribute;
import com.tp.model.bse.CategoryAttributeLink;
import com.tp.redis.util.JedisCacheUtil;
import com.tp.service.BaseService;
import com.tp.service.bse.IAttributeService;

@Service
public class AttributeService extends BaseService<Attribute> implements IAttributeService {

	@Autowired
	private AttributeDao attributeDao;
	@Autowired
	private CategoryAttributeLinkDao categoryAttributeLinkDao;
	@Autowired
	private JedisCacheUtil jedisCacheUtil;
	
	@Override
	public BaseDao<Attribute> getDao() {
		return attributeDao;
	}
	
	@Transactional(propagation = Propagation.REQUIRED)
	public void addAttributeAndCateLinked(Attribute insertAttribute, Long[] ids) {
		if(null==insertAttribute){
			return;
		}
		attributeDao.insert(insertAttribute);
		Long id = insertAttribute.getId();
		Attribute attribu =new Attribute();
		attribu.setId(id);
		attribu.setCode(id.toString());
		super.updateNotNullById(attribu);
		if (null==ids || ids.length==0) {
			return;
		}
		/**
		 * 暂时未写批量插入,暂使用for循环插入
		 */
		for(int i=0;i<ids.length;i++){
			CategoryAttributeLink categoryAttributeLink =new CategoryAttributeLink();
			categoryAttributeLink.setAttributeId(id);
			categoryAttributeLink.setCategoryId(ids[i]);
			categoryAttributeLink.setCreateTime(new Date());
			categoryAttributeLink.setModifyTime(new Date());
			categoryAttributeLinkDao.insert(categoryAttributeLink);
			@SuppressWarnings("unchecked")
			List<Long> attrList=(ArrayList<Long>)jedisCacheUtil.getCache("CATEGORY_ATTRIBUTE"+ids[i]);
			if(null==attrList){
				attrList=new ArrayList<Long>();
			}
			Boolean bool=false;
            for (Long long1 : attrList) {
				if(insertAttribute.getId().equals(long1)){
					bool=true;
					break;
				}
			}
            if(!bool){
            	attrList.add(insertAttribute.getId());
            }
            jedisCacheUtil.setCache("CATEGORY_ATTRIBUTE"+ids[i], attrList, Integer.MAX_VALUE);
		}
		
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void updateAttributeAndCateLinked(Attribute attribute,Long[] ids){
		super.updateNotNullById(attribute);
		CategoryAttributeLink attributeLink=new CategoryAttributeLink();
		attributeLink.setAttributeId(attribute.getId());
		List<CategoryAttributeLink> list=new ArrayList<CategoryAttributeLink>();
		list = categoryAttributeLinkDao.queryByObject(attributeLink);
		List<Long> categoryIdList =new ArrayList<Long>();
		for(CategoryAttributeLink linkDO:list){
			categoryAttributeLinkDao.deleteById(linkDO.getId());
			categoryIdList.add(linkDO.getCategoryId());
		}
		for (Long long1 : categoryIdList) {
		@SuppressWarnings("unchecked")
		List<Long> attrList=(ArrayList<Long>)jedisCacheUtil.getCache("CATEGORY_ATTRIBUTE"+long1);
		if(null==attrList ){
			continue;
		}
		  List<Long> insertList=new ArrayList<Long>();
		  for (Long long2 : attrList) {
			if(!attribute.getId().equals(long2)){
				insertList.add(long2);
			}
		}
		  jedisCacheUtil.setCache("CATEGORY_ATTRIBUTE"+long1, insertList, Integer.MAX_VALUE);
		 }
		if(null==ids || ids.length==0){
			return;
		}
		for(int i=0;i<ids.length;i++){
			CategoryAttributeLink categoryAttributeLink=new CategoryAttributeLink();
			categoryAttributeLink.setAttributeId(attribute.getId());
			categoryAttributeLink.setCategoryId(ids[i]);
			categoryAttributeLink.setCreateTime(new Date());
			categoryAttributeLink.setModifyTime(new Date());
			categoryAttributeLinkDao.insert(categoryAttributeLink);
			@SuppressWarnings("unchecked")
			List<Long> attrList=(ArrayList<Long>)jedisCacheUtil.getCache("CATEGORY_ATTRIBUTE"+ids[i]);
			if(null==attrList){
				attrList=new ArrayList<Long>();
			}
			Boolean bool=false;
            for (Long long1 : attrList) {
				if(attribute.getId().equals(long1)){
					bool=true;
					break;
				}
			}
            if(!bool){
            	attrList.add(attribute.getId());
            }
            jedisCacheUtil.setCache("CATEGORY_ATTRIBUTE"+ids[i], attrList, Integer.MAX_VALUE);
		}			
	}

}
