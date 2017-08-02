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

import com.tp.common.vo.PageInfo;
import com.tp.common.vo.DAOConstant.MYBATIS_SPECIAL_STRING;
import com.tp.dto.common.FailInfo;
import com.tp.dto.common.ResultInfo;
import com.tp.model.bse.Spec;
import com.tp.model.bse.SpecGroup;
import com.tp.model.bse.SpecGroupLink;
import com.tp.proxy.BaseProxy;
import com.tp.service.IBaseService;
import com.tp.service.bse.ISpecGroupLinkService;
import com.tp.service.bse.ISpecGroupService;
import com.tp.service.bse.ISpecService;
import com.tp.util.BeanUtil;
import com.tp.util.StringUtil;
/**
 * 规格组表代理层
 * @author szy
 *
 */
@Service
public class SpecGroupProxy extends BaseProxy<SpecGroup>{

	@Autowired
	private ISpecGroupService specGroupService;
	@Autowired
	private ISpecGroupLinkService specGroupLinkService;
	@Autowired
	private ISpecService specService;
	
	@Autowired
	private ForbiddenWordsProxy forbiddenWordsProxy;
	
	@Override
	public IBaseService<SpecGroup> getService() {
		return specGroupService;
	}

	public PageInfo<SpecGroup> queryAllLikedofSpecGroupByPage(
			SpecGroup specGroup, Integer pageNo, Integer pageSize) {
		Map<String,Object> params = BeanUtil.beanMap(specGroup);
		if(StringUtil.isNotBlank(specGroup.getName())){
			params.remove("name");
			params.put(MYBATIS_SPECIAL_STRING.COLUMNS.name(), " name like concat('%','"+specGroup.getName()+"','%')");
		}
		PageInfo<SpecGroup> pageInfo = new PageInfo<SpecGroup>(pageNo,pageSize);
		return specGroupService.queryPageByParam(params, pageInfo);
	}

	public List<Spec> getInvalidSpecListById(Long id) {
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("groupId", id);
		List<Spec> specList = new ArrayList<Spec>();
		List<SpecGroupLink> listOfSpecGroupLink = specGroupLinkService.queryByParam(params);
		if(CollectionUtils.isNotEmpty(listOfSpecGroupLink)){
			List<Long> idsList=new ArrayList<Long>();
			for (SpecGroupLink specGroupLink : listOfSpecGroupLink) {
				Long specId = specGroupLink.getSpecId();
				idsList.add(specId);
			}
			specList = specService.selectListSpec(idsList,0);
			
		}
		return specList;
	}

	public List<Spec> getSpecList(Long id) {
		Map<String,Object> parms = new HashMap<String,Object>();
		parms.put("groupId", id);
		List<Spec> specList = new ArrayList<Spec>();
		List<SpecGroupLink> listOfSpecGroupLink = specGroupLinkService.queryByParam(parms);
		if(CollectionUtils.isNotEmpty(listOfSpecGroupLink)){
			List<Long> idsList=new ArrayList<Long>();
			for (SpecGroupLink specGroupLink : listOfSpecGroupLink) {
				Long specId = specGroupLink.getSpecId();
				idsList.add(specId);
			}
			specList=specService.selectListSpec(idsList, 2);
		}
		return specList;
	}

	public ResultInfo<?> addSpecGroupAndSpecLink(SpecGroup specGroup, Long[] ids) throws Exception {
		String name = specGroup.getName();
		if(StringUtils.isBlank(name)){
			return new ResultInfo<>(new FailInfo("规格组名称必填"));
		}

		forbiddenWordsProxy.checkForbiddenWordsField(name, "规格组名称");
		forbiddenWordsProxy.checkForbiddenWordsField(specGroup.getRemark(), "备注");
		SpecGroup groupSearch =new SpecGroup();
		groupSearch.setName(name.trim());
		List<SpecGroup> list = specGroupService.queryByObject(groupSearch);
		if(CollectionUtils.isNotEmpty(list)){
			return new ResultInfo<>(new FailInfo("存在相同规格组名称"));
		}
		SpecGroup group=new SpecGroup();
		group.setName(name.trim());
		group.setRemark(specGroup.getRemark().trim());
		group.setStatus(specGroup.getStatus());
		group.setCreateTime(new Date());
		group.setModifyTime(new Date());
		specGroupService.addSpecAndSpecGroupLinkMethod(group, ids);
		return new ResultInfo<>(Boolean.TRUE);
	}

	public ResultInfo<?> updateSpecGroupAndLinked(SpecGroup specGroup,
			Long[] ids) throws Exception {
		String name = specGroup.getName();
		if(StringUtils.isBlank(name)){
			return new ResultInfo<>(new FailInfo("规格组名称必填"));
		}

		forbiddenWordsProxy.checkForbiddenWordsField(name, "规格组名称");
		forbiddenWordsProxy.checkForbiddenWordsField(specGroup.getRemark(), "备注");
		SpecGroup groupSearch =new SpecGroup();
		groupSearch.setName(name.trim());
		List<SpecGroup> list = specGroupService.queryByObject(groupSearch);
		if (CollectionUtils.isNotEmpty(list)) {
			for (SpecGroup sp : list) {
				Long id = sp.getId();
				if (!id.equals(specGroup.getId())) {
					return new ResultInfo<>(new FailInfo("存在相同组的名称,请更换一个"));
				}
			}
		}
	    SpecGroup group=new SpecGroup();
	    group.setId(specGroup.getId());
		group.setName(name.trim());
		group.setRemark(specGroup.getRemark().trim());
		group.setStatus(specGroup.getStatus());
		group.setModifyTime(new Date());
		specGroupService.updateSpecAndSpecGroupLinkMethod(group,ids);
		return new ResultInfo<>(Boolean.TRUE);
	}
}
