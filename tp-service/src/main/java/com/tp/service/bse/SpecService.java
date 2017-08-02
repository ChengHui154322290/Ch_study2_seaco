package com.tp.service.bse;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.tp.common.dao.BaseDao;
import com.tp.common.vo.Constant;
import com.tp.common.vo.DAOConstant;
import com.tp.common.vo.DAOConstant.MYBATIS_SPECIAL_STRING;
import com.tp.dao.bse.SpecDao;
import com.tp.dao.bse.SpecGroupLinkDao;
import com.tp.model.bse.Spec;
import com.tp.model.bse.SpecGroupLink;
import com.tp.redis.util.JedisCacheUtil;
import com.tp.service.BaseService;
import com.tp.service.bse.ISpecService;
import com.tp.util.StringUtil;

@Service
public class SpecService extends BaseService<Spec> implements ISpecService {

	@Autowired
	private SpecDao specDao;
	@Autowired
	private SpecGroupLinkDao specGroupLinkDao;
	
	@Autowired
	private JedisCacheUtil jedisCacheUtil;
	
	@Override
	public BaseDao<Spec> getDao() {
		return specDao;
	}
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void addSpecAndSpecLinkMethod(Spec spec, Long[] ids){
		insert(spec);
		Long id = spec.getId();
		Spec  doSpec =new Spec();
		doSpec.setId(id);
		doSpec.setCode(id.toString());
		this.updateNotNullById(doSpec);
		if (null !=ids && ids.length !=0) {
			List<SpecGroupLink> specGroupLinks = new ArrayList<SpecGroupLink>();
			for (int i = 0; i < ids.length; i++) {
				Long gtoupId = ids[i];
				SpecGroupLink specGroupLink = new SpecGroupLink();
				specGroupLink.setSpecId(id);
				specGroupLink.setGroupId(gtoupId);
				specGroupLink.setCreateTime(new Date());
				specGroupLink.setModifyTime(new Date());
				specGroupLinkDao.insert(specGroupLink);
				specGroupLinks.add(specGroupLink);
			}
			 for (int i = 0; i < ids.length; i++) {
				 @SuppressWarnings("unchecked")
				ArrayList<Long> list=(ArrayList<Long>)jedisCacheUtil.getCache("SPEC_SPECGROUP_1_"+ids[i]);
				 if(null==list){
					 list=new ArrayList<Long>();
				 }
				 Boolean bool = false;
				 for(int j=0;j<list.size();j++){
					 if(id.equals(list.get(j))){
						 bool=true;
						 break;
					 }
				 }
				 if(!bool){
					 list.add(id);
				 }
				 jedisCacheUtil.setCache("SPEC_SPECGROUP_1_"+ids[i],list,Integer.MAX_VALUE);
			 }
		}
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void updateSpecAndLinked(Spec insertSpec, Long[] ids) {
		this.updateNotNullById(insertSpec);
		if (ids != null) {
			SpecGroupLink specGroupLink = new SpecGroupLink();
			specGroupLink.setSpecId(insertSpec.getId());
			List<Long> resultIds = new ArrayList<Long>();
			List<Long> specGroupHasSelectedList = new ArrayList<Long>();
			List<SpecGroupLink> listOfSpecGroupLink = new ArrayList<SpecGroupLink>();
			listOfSpecGroupLink = specGroupLinkDao.queryByObject(specGroupLink);
			for (SpecGroupLink groupLinkDO : listOfSpecGroupLink) {
				Long id = groupLinkDO.getId();
				specGroupHasSelectedList.add(groupLinkDO.getGroupId());
				resultIds.add(id);
			}
			if (CollectionUtils.isNotEmpty(resultIds)) {
				Map<String,Object> params = new HashMap<String,Object>();
				params.put(MYBATIS_SPECIAL_STRING.COLUMNS.name(), " id in ("+StringUtil.join(resultIds, Constant.SPLIT_SIGN.COMMA)+")");
				specGroupLinkDao.deleteByParam(params);
			}
			for (Long specGroId : specGroupHasSelectedList) {
				@SuppressWarnings("unchecked")
				ArrayList<Long> list1 = (ArrayList<Long>) jedisCacheUtil.getCache("SPEC_SPECGROUP_1_"+ specGroId);
				if (list1 != null) {
					ArrayList<Long> list2 = new ArrayList<Long>();
					for (int i = 0; i < list1.size(); i++) {
						if (!list1.get(i).equals(insertSpec.getId())) {
							list2.add(list1.get(i));
						}
					}
					jedisCacheUtil.setCache("SPEC_SPECGROUP_1_"+ specGroId, list2, Integer.MAX_VALUE);
				}
			}

			for (int i = 0; i < ids.length; i++) {
				Long groupId = ids[i];
				SpecGroupLink insertSpecLink = new SpecGroupLink();
				insertSpecLink.setSpecId(insertSpec.getId());
				insertSpecLink.setGroupId(groupId);
				insertSpecLink.setCreateTime(new Date());
				insertSpecLink.setModifyTime(new Date());
				specGroupLinkDao.insert(insertSpecLink);
			}
			
			for (int i = 0; i < ids.length; i++) {
				Long groupId = ids[i];
				@SuppressWarnings("unchecked")
				ArrayList<Long> list3 = (ArrayList<Long>) jedisCacheUtil.getCache("SPEC_SPECGROUP_1_"+ groupId);
				if (null==list3 ) {
					list3 = new ArrayList<Long>();
				}
				Boolean bool = false;
				for (int j = 0; j < list3.size(); j++) {
					if (list3.get(j).equals(insertSpec.getId())) {
						bool = true;
						break;
					}
				}
				if (!bool) {
					list3.add(insertSpec.getId());
				}
				jedisCacheUtil.setCache("SPEC_SPECGROUP_1_" + ids[i],list3, Integer.MAX_VALUE);
			}

		} else {
			SpecGroupLink specGroupLink = new SpecGroupLink();
			specGroupLink.setSpecId(insertSpec.getId());
			List<Long> resultIds = new ArrayList<Long>();

			List<Long> specGroupHasSelectedList = new ArrayList<Long>();

			List<SpecGroupLink> listOfSpecGroupLink = new ArrayList<SpecGroupLink>();
			listOfSpecGroupLink = specGroupLinkDao.queryByObject(specGroupLink);
			for (SpecGroupLink groupLinkDO : listOfSpecGroupLink) {
				Long id = groupLinkDO.getId();
				specGroupHasSelectedList.add(groupLinkDO.getGroupId());
				resultIds.add(id);
			}
			if (CollectionUtils.isNotEmpty(resultIds)) {
				Map<String,Object> params = new HashMap<String,Object>();
				params.put(MYBATIS_SPECIAL_STRING.WHERE.name(), " id in ("+StringUtil.join(resultIds, Constant.SPLIT_SIGN.COMMA)+")");
				specGroupLinkDao.deleteByParam(params);
			}

			for (Long specGroId : specGroupHasSelectedList) {
				@SuppressWarnings("unchecked")
				ArrayList<Long> list1 = (ArrayList<Long>) jedisCacheUtil
						.getCache("SPEC_SPECGROUP_1_"+ specGroId);
				if (list1 != null) {
					ArrayList<Long> list2 = new ArrayList<Long>();
					for (int i = 0; i < list1.size(); i++) {
						if (!list1.get(i).equals(insertSpec.getId())) {
							list2.add(list1.get(i));
						}
					}
					jedisCacheUtil.setCache("SPEC_SPECGROUP_1_"+ specGroId, list2, Integer.MAX_VALUE);
				}
			}

		}
	}
	
	@Override
	public void deleteSpecGroupLinked(Long specGroupId, Long specId) {
		if (null == specGroupId || null == specId) {
			return;
		}
		SpecGroupLink specGroupLink = new SpecGroupLink();
		specGroupLink.setSpecId(specId);
		specGroupLink.setGroupId(specGroupId);

		List<SpecGroupLink> listOfResult = new ArrayList<SpecGroupLink>();
		listOfResult = specGroupLinkDao.queryByObject(specGroupLink);
		
		if (listOfResult.size() != 1) {
			return;
		}
		
		specGroupLinkDao.deleteById(listOfResult.get(0).getId());
		@SuppressWarnings("unchecked")
		ArrayList<Long> list = (ArrayList<Long>) jedisCacheUtil.getCache("SPEC_SPECGROUP_1_"+ specGroupId);
		if (null == list) {
			return;
		}
		List<Long> insertList = new ArrayList<Long>();
		for (Long long1 : list) {
			if (!specId.equals(long1)) {
				insertList.add(long1);
			}
		}
		jedisCacheUtil.setCache("SPEC_SPECGROUP_1_" + specGroupId,insertList, Integer.MAX_VALUE);
	}
	
	@Override
	public List<Spec> selectListSpec(List<Long> ids, Integer status) {
		if(CollectionUtils.isEmpty(ids)){
			return null;
		}
		List<Spec> specs = new ArrayList<Spec>();
		List<Spec> listSpec =new ArrayList<Spec>();
		Map<String,Object> params = new HashMap<String,Object>();
		params.put(DAOConstant.MYBATIS_SPECIAL_STRING.COLUMNS.name(), " id in ("+StringUtils.join(ids, Constant.SPLIT_SIGN.COMMA)+")");
		listSpec=specDao.queryByParam(params);
		
		for (Spec spec : listSpec) {
			Integer bool = spec.getStatus();
			if (bool==Constant.DEFAULTED.YES) {
				switch (status) {
				case 0:
					break;
				case 1:
					specs.add(spec);
					break;
				case 2:
					specs.add(spec);
					break;
				default:
					break;
				}
			} else {
				switch (status) {
				case 0:
					specs.add(spec);
					break;
				case 1:
					break;
				case 2:
					specs.add(spec);
					break;
				default:
					break;
				}
			}
		}
		return specs;
	}
}
