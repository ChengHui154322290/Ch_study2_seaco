package com.tp.service.bse;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.tp.common.dao.BaseDao;
import com.tp.common.vo.Constant;
import com.tp.common.vo.DAOConstant.MYBATIS_SPECIAL_STRING;
import com.tp.dao.bse.CategorySpecGroupLinkDao;
import com.tp.dao.bse.SpecDao;
import com.tp.dao.bse.SpecGroupDao;
import com.tp.dao.bse.SpecGroupLinkDao;
import com.tp.model.bse.CategorySpecGroupLink;
import com.tp.model.bse.Spec;
import com.tp.model.bse.SpecGroup;
import com.tp.model.bse.SpecGroupLink;
import com.tp.redis.util.JedisCacheUtil;
import com.tp.result.bse.SpecGroupResult;
import com.tp.service.BaseService;
import com.tp.service.bse.ISpecGroupService;
import com.tp.util.StringUtil;

@Service
public class SpecGroupService extends BaseService<SpecGroup> implements ISpecGroupService {

	@Autowired
	private SpecGroupDao specGroupDao;
	@Autowired
	private SpecGroupLinkDao specGroupLinkDao;
	@Autowired
	private CategorySpecGroupLinkDao categorySpecGroupLinkDao;
	@Autowired
	private SpecDao specDao;
	
	@Autowired
	private JedisCacheUtil jedisCacheUtil;
	
	@Override
	public BaseDao<SpecGroup> getDao() {
		return specGroupDao;
	}

	/**
	 * 此方法获取所有的规格组信息,0获取无效数据,1为有效,2为全部数据
	 * @param ids
	 * @param status
	 * @return
	 */
	@Override
	public List<SpecGroup> selectListSpecGroup(List<Long> ids, Integer status) {
		if(CollectionUtils.isEmpty(ids)){
			return null;
		}
		List<SpecGroup> specGroups = new ArrayList<SpecGroup>();
		
		List<SpecGroup> listSpecGroup=new ArrayList<SpecGroup>();
		for (int i = 0; i < ids.size(); i++) {
				SpecGroup group = this.queryById(ids.get(i));
				if(group!=null){
					listSpecGroup.add(group);
				}	
		}
		 //当bool为true是使用switch语句根据status的值返回数据(此方法是获取的全部数据删选后返回的)0获取无效数据,1为有效,2为全部数据
		for (SpecGroup specGroup : listSpecGroup) {
			Integer bool = specGroup.getStatus();
			if (bool==Constant.DEFAULTED.YES) {
				switch (status) {
				case 0:
					break;
				case 1:
					specGroups.add(specGroup);
					break;
				case 2:
					specGroups.add(specGroup);
					break;
				default:
					break;
				}
			} else {
				switch (status) {
				case 0:
					specGroups.add(specGroup);
					break;
				case 1:
					break;
				case 2:
					specGroups.add(specGroup);
					break;
				default:
					break;
				}
			}
		}
		return specGroups;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void addSpecAndSpecGroupLinkMethod(SpecGroup specGroup,Long[] ids){
		specGroup.setCode(jedisCacheUtil.incr("specGroupCode").intValue());
		insert(specGroup);
		Long id = specGroup.getId();
		if (null==ids || ids.length==0) {
			return;
		}
		List<SpecGroupLink> specGroupLinks = new ArrayList<SpecGroupLink>();
		for (int i = 0; i < ids.length; i++) {
			Long specId = ids[i];
			SpecGroupLink specGroupLink = new SpecGroupLink();
			specGroupLink.setSpecId(specId);
			specGroupLink.setGroupId(id);
			specGroupLink.setCreateTime(new Date());
			specGroupLink.setModifyTime(new Date());
			specGroupLinks.add(specGroupLink);
		}
		specGroupLinkDao.insertByList(specGroupLinks);
		List<Long> insertList = new ArrayList<Long>();
		for (int i = 0; i < ids.length; i++) {
			insertList.add(ids[i]);
		}
		jedisCacheUtil.setCache("SPEC_SPECGROUP_1_" + id, insertList,Integer.MAX_VALUE);
		
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void updateSpecAndSpecGroupLinkMethod(SpecGroup group, Long[] ids){
		updateNotNullById(group);
		if (ids != null) {
			Map<String,Object> params = new HashMap<String,Object>();
			params.put("groupId", group.getId());
			List<Long> resultIds = new ArrayList<Long>();
			List<SpecGroupLink> listOfSpecGroupLink = specGroupLinkDao.queryByParam(params);
			for (SpecGroupLink groupLinkDO : listOfSpecGroupLink) {
				Long id = groupLinkDO.getId();
				resultIds.add(id);
			}
			if (CollectionUtils.isNotEmpty(resultIds)) {
				deleteSpecGroupLinkByList(resultIds);
			}
			List<SpecGroupLink> specGroupLinks = new ArrayList<SpecGroupLink>();
			for (int i = 0; i < ids.length; i++) {
				SpecGroupLink insertSpecLink = new SpecGroupLink();
				insertSpecLink.setSpecId(ids[i]);
				insertSpecLink.setGroupId(group.getId());
				insertSpecLink.setCreateTime(new Date());
				insertSpecLink.setModifyTime(new Date());
				specGroupLinks.add(insertSpecLink);
			}
			specGroupLinkDao.insertByList(specGroupLinks);
			List<Long> insertList = new ArrayList<Long>();
			for (int i = 0; i < ids.length; i++) {
				insertList.add(ids[i]);
			}
			jedisCacheUtil.setCache("SPEC_SPECGROUP_1_" + group.getId(), insertList,Integer.MAX_VALUE);

		} else {
			Map<String,Object> params = new HashMap<String,Object>();
			params.put("groupId", group.getId());
			List<Long> resultIds = new ArrayList<Long>();
			List<SpecGroupLink> listOfSpecGroupLink = specGroupLinkDao.queryByParam(params);
			for (SpecGroupLink groupLinkDO : listOfSpecGroupLink) {
				Long id = groupLinkDO.getId();
				resultIds.add(id);
			}
			deleteSpecGroupLinkByList(resultIds);
			jedisCacheUtil.setCache("SPEC_SPECGROUP_1_" + group.getId(),new ArrayList<Long>(), Integer.MAX_VALUE);
		}
		
	}
	
	public void deleteSpecGroupLinkByList(List<Long> ids){
		if(CollectionUtils.isEmpty(ids)){
			return;
		}
		Map<String,Object> params = new HashMap<String,Object>();
		params.put(MYBATIS_SPECIAL_STRING.WHERE.name(), " id in ("+StringUtil.join(ids, Constant.SPLIT_SIGN.COMMA)+")");
		specGroupLinkDao.deleteByParam(params);
	}

	@Override
	public List<SpecGroupResult> selectListSpecGroupResult(List<Long> ids,	Integer status) {
		if(CollectionUtils.isEmpty(ids)){
			return null;
		}
		List<SpecGroupResult> specGroupResults = new ArrayList<SpecGroupResult>();
       //当bool为true是使用switch语句根据status的值返回数据(此方法是获取的全部数据删选后返回的)0获取无效数据,1为有效,2为全部数据
		for (int i = 0; i < ids.size(); i++) {
			SpecGroupResult specGroupResultById = selectSpecGroupResultById(ids.get(i), status);
			if(null==specGroupResultById){
				continue;
			}
			Integer bool = specGroupResultById.getSpecGroup().getStatus();
			if (Constant.DEFAULTED.YES==bool) {
				switch (status) {
				case 0:
					break;
				case 1:
					specGroupResults.add(specGroupResultById);
					break;
				case 2:
					specGroupResults.add(specGroupResultById);
					break;
				default:
					break;
				}
			} else {
				switch (status) {
				case 0:
					specGroupResults.add(specGroupResultById);
					break;
				case 1:
					break;
				case 2:
					specGroupResults.add(specGroupResultById);
					break;
				default:
					break;
				}
			}
		}
		return specGroupResults;
	}
	
	@Override
	public SpecGroupResult selectSpecGroupResultById(Long id, Integer status) {
		if (null == id) {
			return null;
		}
		SpecGroup group = this.queryById(id);
		if (null == group) {
			return null;
		}
		ArrayList<Long> ids = new ArrayList<Long>();
		List<Spec> resultSpec = new ArrayList<Spec>();
		@SuppressWarnings("unchecked")
		ArrayList<Long> cache = (ArrayList<Long>) jedisCacheUtil.getCache("SPEC_SPECGROUP_1_" + id);
		Map<String,Object> params = new HashMap<String,Object>();
		if (CollectionUtils.isNotEmpty(cache)) {
			ids = cache;
		} else {
			params.put("groupId", id);
			List<SpecGroupLink> dynamicSpecGroupLink = specGroupLinkDao.queryByParam(params);
			for (SpecGroupLink specGroupLink : dynamicSpecGroupLink) {
				Long specId = specGroupLink.getSpecId();
				ids.add(specId);
			}
		}
		if (CollectionUtils.isNotEmpty(ids)) {
			jedisCacheUtil.setCache("SPEC_SPECGROUP_1_" + id, ids,Integer.MAX_VALUE);
			params.clear();
			params.put(MYBATIS_SPECIAL_STRING.COLUMNS.name()," id in ("+StringUtil.join(ids, Constant.SPLIT_SIGN.COMMA)+")");
			List<Spec> listOfSpec= specDao.queryByParam(params);
			/**
			 * 当bool为true是使用switch语句根据status的值返回数据(此方法是获取的全部数据删选后返回的)0获取无效数据,1为有效,2
			 * 为全部数据
			 */
			for (Spec spec : listOfSpec) {
				Integer bool = spec.getStatus();
				if (Constant.DEFAULTED.YES==bool) {
					switch (status) {
					case 0:
						break;
					case 1:
						resultSpec.add(spec);
						break;
					case 2:
						resultSpec.add(spec);
						break;
					default:
						break;
					}
				} else {
					switch (status) {
					case 0:
						resultSpec.add(spec);
						break;
					case 1:
						break;
					case 2:
						resultSpec.add(spec);
						break;
					default:
						break;
					}
				}
			}
		}
		SpecGroupResult specGroupResult = new SpecGroupResult();
		specGroupResult.setSpecDoList(resultSpec);
		specGroupResult.setSpecGroup(group);
		return specGroupResult;

	}

	@Override
	public List<SpecGroupResult> getSpecGroupResultByCategoryId(Long categoryId) {
		if (null == categoryId) {
			return null;
		}
		List<Long> list = (ArrayList<Long>) jedisCacheUtil.getCache("CATEGORY_SPECGROUP" + categoryId);
		if (CollectionUtils.isNotEmpty(list)) {
			return this.selectListSpecGroupResult(list, 1);// 默认返回有效值
		}
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("categoryId", categoryId);
		List<CategorySpecGroupLink> list2 = categorySpecGroupLinkDao.queryByParam(params);
		List<Long> ids=new ArrayList<Long>();
		for (CategorySpecGroupLink categorySpecGroupLinkDO2 : list2) {
			ids.add(categorySpecGroupLinkDO2.getSpecGroupId());
		}
		jedisCacheUtil.setCache("CATEGORY_SPECGROUP"+categoryId, ids, Integer.MAX_VALUE);
		return this.selectListSpecGroupResult(ids, 1);
	}

	@Override
	public List<SpecGroupResult> getAllSpecGroupResult() {	
		List<SpecGroup> allSpecGroup = specGroupDao.queryByParam(new HashMap<String,Object>());
		List<SpecGroupResult> specGroupResults = new ArrayList<SpecGroupResult>();
		for (SpecGroup specGroupDO : allSpecGroup) {
			Integer status = specGroupDO.getStatus();
               if(Constant.ENABLED.YES.equals(status)){  
            	SpecGroupResult groupResult = this.selectSpecGroupResultById( specGroupDO.getId(), 1);
            	if(groupResult !=null){
            		specGroupResults.add(groupResult);
            	  }        
               }
		}
		return specGroupResults;
	}
}
