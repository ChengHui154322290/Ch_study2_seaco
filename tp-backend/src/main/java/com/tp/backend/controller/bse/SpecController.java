package com.tp.backend.controller.bse;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tp.backend.controller.AbstractBaseController;
import com.tp.common.vo.Constant;
import com.tp.common.vo.PageInfo;
import com.tp.dto.common.ResultInfo;
import com.tp.model.bse.Spec;
import com.tp.model.bse.SpecGroup;
import com.tp.proxy.bse.SpecGroupLinkProxy;
import com.tp.proxy.bse.SpecGroupProxy;
import com.tp.proxy.bse.SpecProxy;


@Controller
@RequestMapping("/basedata/specGroup/spec")
public class SpecController extends AbstractBaseController {

	 private static final Logger LOG = LoggerFactory.getLogger(SpecController.class);

	@Autowired
	private SpecProxy specProxy;
	@Autowired
	private SpecGroupProxy specGroupProxy;
    @Autowired
    private SpecGroupLinkProxy  specGroupLinkProxy;
	/**
	 * 
	 * <pre>
	 * 规格列表查询
	 * </pre>
	 *
	 * @param model
	 * @param spec
	 */
	@RequestMapping(value = "/list")
	public void list(Model model, Spec spec, Integer page,Integer size) {
		PageInfo<Spec> queryAllSpecByPage = specProxy.queryAllLikedofSpecByPage(spec, page, size);
		model.addAttribute("queryAllSpecByPage", queryAllSpecByPage);
		model.addAttribute("spec", spec);
	}

	/**
	 * 
	 * <pre>
	 * 规格编辑
	 * </pre>
	 *
	 * @param model
	 * @param spec
	 */
	@RequestMapping(value = "/edit")
	public void edit(@RequestParam(value = "id") Long id, Model model) {
		ResultInfo<Spec> specResultInfo = specProxy.queryById(id);
		List<SpecGroup> specGroupList = specProxy.getSpecGroupList(id);
		List<SpecGroup> invalidSpecGroupList = specProxy.getInvalidSpecGroupListById(id);
		SpecGroup specGroup = new SpecGroup();
		specGroup.setStatus(1);
		ResultInfo<List<SpecGroup>> allSpecGroupResultInfo = specGroupProxy.queryByObject(specGroup);
		model.addAttribute("spec", specResultInfo.getData());
		model.addAttribute("allSpecGroup", allSpecGroupResultInfo.getData());
		model.addAttribute("specGroupList", specGroupList);
		model.addAttribute("invalidSpecGroupList", invalidSpecGroupList);
	}



	@RequestMapping(value = "/add")
	public void addSpec(Model model) {
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("status", Constant.DEFAULTED.YES);
		ResultInfo<List<SpecGroup>> allSpecGroupResultInfo = specGroupProxy.queryByParam(params);
		model.addAttribute("allSpecGroup", allSpecGroupResultInfo.getData());
	}

	/**
	 * 规格增加
	 * @param
	 * @throws ExceptionResultInfo 
	 */
	@RequestMapping(value = "/addSpecSubmit", method = RequestMethod.POST)
	@ResponseBody
	public ResultInfo<?> addSpecSubmit(Spec spec, Long[] ids) throws Exception {
		if (null==spec ) {
			LOG.debug("数据不能为空");
			throw new Exception("数据异常");
		}
		return specProxy.addSpecAndSpecLink(spec, ids);
	
	
	}

	/**
	 * 
	 * <pre>
	 * 更新规格
	 * </pre>
	 *
	 * @param
	 */
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	@ResponseBody
	public ResultInfo<?> updateSpec(Spec spec,Long[] ids) throws Exception {
		if (null==spec) {
			LOG.info("数据不能为空");
			throw new Exception("数据异常");
		}
		return specProxy.updateSpecAndLinked(spec,ids);
	}
	
	
	
	@RequestMapping(value="/deleteSpecGroup",method=RequestMethod.GET)
	@ResponseBody
	public ResultInfo<?> deleteSpecGroup(@RequestParam(value = "specId") Long specId,@RequestParam(value = "specGroupId") Long specGroupId) throws Exception {
		if (null==specId || specId <=0 || null==specGroupId || specGroupId<=0) {
			LOG.info("数据不能为空");
			throw new Exception("数据异常");
		}
     	return specProxy.deleteSpecGroupLinked(specGroupId,specId);	
	}

	/**
	 * 
	 * <pre>
	 * 获取规格组
	 * </pre>
	 *
	 */
	@RequestMapping(value = "/getAllSpecGroupByName", method = RequestMethod.POST)
	@ResponseBody
	public List<SpecGroup> getAllSpecGroup(SpecGroup specGroup) {
		return specGroupProxy.queryByObject(specGroup).getData();
	}
	
	@RequestMapping(value="/addSpecGroup")
	public void addSpecGroup(){
		
	}
}
