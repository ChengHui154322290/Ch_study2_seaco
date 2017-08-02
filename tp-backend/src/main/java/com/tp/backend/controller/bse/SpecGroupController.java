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
@RequestMapping("/basedata/specGroup/group")
public class SpecGroupController extends AbstractBaseController {

	 private static final Logger LOG = LoggerFactory.getLogger(SpecGroupController.class);


	@Autowired
	private SpecProxy specProxy;
	@Autowired
	private SpecGroupProxy specGroupProxy;
    @Autowired
    private SpecGroupLinkProxy  specGroupLinkProxy;
    
	/**
	 * 获取规格组
	 * @param model
	 * @param 
	 */
	@RequestMapping(value = "/list")
	public void list(Model model, SpecGroup specGroup, Integer page, Integer size) {
		PageInfo<SpecGroup> queryAllSpecGroupByPage = specGroupProxy.queryAllLikedofSpecGroupByPage(specGroup, page, size);
		model.addAttribute("queryAllSpecGroupByPage", queryAllSpecGroupByPage);
		model.addAttribute("specGroup", specGroup);
	}

	/**
	 * 规格编辑
	 * @param model
	 * @param spec
	 */	
	@RequestMapping(value = "/edit")
	public void edit(@RequestParam(value = "id") Long id, Model model) {
		ResultInfo<SpecGroup> specGroupResultInfo = specGroupProxy.queryById(id);
		List<Spec> invalidSpecList=specGroupProxy.getInvalidSpecListById(id);
	    List<Spec> specSelectedList = specGroupProxy.getSpecList(id);
	    Map<String,Object> params = new HashMap<String,Object>();
	    params.put("status", Constant.DEFAULTED.YES);
	    ResultInfo<List<Spec>> allSpecListResultInfo = specProxy.queryByParam(params);
		model.addAttribute("specGroup", specGroupResultInfo.getData());
		model.addAttribute("allSpecList", allSpecListResultInfo.getData());
		model.addAttribute("specSelectedList", specSelectedList);
		model.addAttribute("invalidSpecList", invalidSpecList);
	}
	
	@RequestMapping(value = "/view")
	public void view(@RequestParam(value = "id") Long id, Model model) {
		ResultInfo<SpecGroup> specGroupResultInfo = specGroupProxy.queryById(id);
		List<Spec> specSelectedList = specGroupProxy.getSpecList(id);
		model.addAttribute("specGroup", specGroupResultInfo.getData());
		model.addAttribute("specSelectedList", specSelectedList);
	}



	@RequestMapping(value = "/add")
	public void addSpecGroup( Model model) {
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("status", Constant.DEFAULTED.YES);
	    ResultInfo<List<Spec>> allSpecListResultInfo = specProxy.queryByParam(params);
	    model.addAttribute("allSpecList", allSpecListResultInfo.getData());
	}

	/**
	 * 规格增加
	 * @param
	 */
	
	@RequestMapping(value = "/addSpecGroupSubmit", method = RequestMethod.POST)
	@ResponseBody
	public ResultInfo<?> addSpecSubmit(SpecGroup specGroup, Long[] ids) throws Exception {
		if (null==specGroup) {
			LOG.info("数据不能为空");
			throw new Exception("数据异常");
		}
		return  specGroupProxy.addSpecGroupAndSpecLink(specGroup, ids);
	}

	/**
	 * 更新尺
	 * @param
	 */
	
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	@ResponseBody
	public ResultInfo<?> updateSpecGroup(SpecGroup specGroup,Long[] ids) throws Exception {
		if (null==specGroup) {
			LOG.info("数据不能为空");
			throw new Exception("数据异常");
		}
		return  specGroupProxy.updateSpecGroupAndLinked(specGroup,ids);
	}  

	/**
	 * 获取规格组
	 */
	@RequestMapping(value = "/getAllSpec", method = RequestMethod.POST)
	@ResponseBody
	public List<Spec> getAllSpec(Spec spec) {
		return specProxy.queryByObject(spec).getData();
	}
	
	@RequestMapping(value="/addSpec")
	public String addSpec(){
		return "/basedata/specGroup/group/addSpec";
	}
}
