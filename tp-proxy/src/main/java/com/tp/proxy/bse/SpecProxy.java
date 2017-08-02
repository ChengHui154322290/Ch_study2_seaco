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

import com.tp.common.util.ExceptionUtils;
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
import com.tp.util.StringUtil;
/**
 * 规格表代理层
 * @author szy
 *
 */
@Service
public class SpecProxy extends BaseProxy<Spec>{

	@Autowired
	private ISpecService specService;
	@Autowired
	private ISpecGroupLinkService specGroupLinkService;
	@Autowired
	private ISpecGroupService specGroupService;

	@Autowired
	private ForbiddenWordsProxy forbiddenWordsProxy;
	
	@Override
	public IBaseService<Spec> getService() {
		return specService;
	}

	/**
	 * 
	 * <pre>
	 * 插入规格及其相关连的规格组
	 * </pre>
	 *
	 * @param spec
	 * @param ids
	 *          
	 */
	public ResultInfo<Spec> addSpecAndSpecLink(Spec spec, Long[] ids) throws Exception {
		String specStr = spec.getSpec();
		if(StringUtils.isBlank(specStr)){
			return new ResultInfo<>(new FailInfo("规格必填"));
		}
		forbiddenWordsProxy.checkForbiddenWordsField(specStr, "规格名称");
		forbiddenWordsProxy.checkForbiddenWordsField(spec.getRemark(), "备注");
		Spec searchSpec =new Spec();
		searchSpec.setSpec(specStr.trim());
		List<Spec> list = specService.queryByObject(searchSpec);
		if(CollectionUtils.isNotEmpty(list)){
			return new ResultInfo<>(new FailInfo("存在相同规格名称"));
		}
		Spec insertSpec =new Spec();
		insertSpec.setSpec(specStr.trim());
		insertSpec.setStatus(spec.getStatus());
		insertSpec.setRemark(spec.getRemark().trim());
		insertSpec.setCreateTime(new Date());
		insertSpec.setModifyTime(new Date());		
		specService.addSpecAndSpecLinkMethod(insertSpec, ids);
		return new ResultInfo<>(insertSpec);
	}
	
	/**
	 * 
	 * <pre>
	 * 更新规格及其关联的规格组表
	 * </pre>
	 *
	 * @param spec
	 * @param ids
	 */
	
	public ResultInfo<Spec> updateSpecAndLinked(Spec spec, Long[] ids) throws Exception{
		String specStr = spec.getSpec();
		if (StringUtils.isBlank(specStr)) {
			return new ResultInfo<>(new FailInfo("规格必填"));
		}
		forbiddenWordsProxy.checkForbiddenWordsField(specStr, "规格名称");
		forbiddenWordsProxy.checkForbiddenWordsField(spec.getRemark(), "备注");
		Spec searchSpec = new Spec();
		searchSpec.setSpec(specStr.trim());
		List<Spec> list = specService.queryByObject(searchSpec);
		for (Spec sp : list) {
			Long id = sp.getId();
			if (!id.equals(spec.getId())) {
				return new ResultInfo<>(new FailInfo("存在相同组规格,请更换一个"));
			}
		}
		Spec insertSpec = new Spec();
		insertSpec.setId(spec.getId());
		insertSpec.setSpec(specStr.trim());
		insertSpec.setStatus(spec.getStatus());
		insertSpec.setRemark(spec.getRemark().trim());
		insertSpec.setModifyTime(new Date());
		specService.updateSpecAndLinked(insertSpec, ids);
		return new ResultInfo<>(insertSpec);
	}
	
	public PageInfo<Spec> queryAllLikedofSpecByPage(Spec spec, Integer pageNo,
			Integer pageSize) {
		Map<String,Object> params = new HashMap<String,Object>();
		if(StringUtil.isNoneBlank(spec.getSpec())){
			params.put(MYBATIS_SPECIAL_STRING.LIKE.name(), " spec like concat('%','"+spec.getSpec()+"','%')");
		}
		params.put("status", spec.getStatus());
		params.put("sort", spec.getSort());
		PageInfo<Spec> pageInfo = new PageInfo<Spec>(pageNo, pageSize);
		return specService.queryPageByParamNotEmpty(params, pageInfo);
	}
	
	/**
	 * 
	 * <pre>
	 * 根据规格id返此id所在的规格组
	 * </pre>
	 *
	 * @param id
	 *            尺码id
	 * @return
	 */
	public List<SpecGroup> getSpecGroupList(Long id) {
		SpecGroupLink specGroupLink = new SpecGroupLink();
		specGroupLink.setSpecId(id);
		List<SpecGroup> specGroupList = new ArrayList<SpecGroup>();
		List<SpecGroupLink> listOfSpecGroupLink = specGroupLinkService.queryByObject(specGroupLink);
		if(CollectionUtils.isNotEmpty(listOfSpecGroupLink)){
			List<Long> idsList = new ArrayList<Long>();
			for (SpecGroupLink spGro : listOfSpecGroupLink) {
				idsList.add(spGro.getGroupId());
			}
			specGroupList=specGroupService.selectListSpecGroup(idsList, 2);
		}
		return specGroupList;
	}
	
	/**
	 * 
	 * <pre>
	 * 
	 * </pre>
	 *
	 * @param id
	 * @return
	 */
	
	public List<SpecGroup> getInvalidSpecGroupListById(Long id) {
		SpecGroupLink specGroupLink = new SpecGroupLink();
		specGroupLink.setSpecId(id);
		List<SpecGroup> specGroupList = new ArrayList<SpecGroup>();
		List<SpecGroupLink> listOfSpecGroupLink = specGroupLinkService.queryByObject(specGroupLink);
		if(CollectionUtils.isNotEmpty(listOfSpecGroupLink)){
			List<Long> idsList = new ArrayList<Long>();
			for (SpecGroupLink spGro : listOfSpecGroupLink) {
				idsList.add(spGro.getGroupId());
			}
			specGroupList=specGroupService.selectListSpecGroup(idsList,0);
		}
		return specGroupList;
	}

	public ResultInfo<?> deleteSpecGroupLinked(Long specGroupId, Long specId) {
		try{
			specService.deleteSpecGroupLinked(specGroupId, specId);
			return new ResultInfo<>(Boolean.TRUE);
		}catch(Exception exception){
			FailInfo failInfo = ExceptionUtils.print(new FailInfo(exception), logger,specGroupId,specId);
			return new ResultInfo<>(failInfo);
		}
	}
}
