package com.tp.backend.controller.bse;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tp.backend.controller.AbstractBaseController;
import com.tp.common.vo.Constant;
import com.tp.common.vo.BseConstant.DictionaryCode;
import com.tp.dto.common.ResultInfo;
import com.tp.model.bse.Category;
import com.tp.model.bse.CategorySpecGroupLink;
import com.tp.model.bse.DictionaryInfo;
import com.tp.model.bse.SpecGroup;
import com.tp.proxy.bse.CategoryProxy;
import com.tp.proxy.bse.DictionaryInfoProxy;
import com.tp.proxy.bse.SpecGroupProxy;

@Controller
@Scope(BeanDefinition.SCOPE_SINGLETON)
@RequestMapping("/basedata/category/")
public class BaseCategoryController extends AbstractBaseController{
	
	 private static final Logger LOGGER = LoggerFactory.getLogger(BaseCategoryController.class);

	
	@Autowired
	private CategoryProxy categoryProxy;
	
	@Autowired
	private SpecGroupProxy specGroupProxy;
	
	@Autowired
	private DictionaryInfoProxy dictionaryInfoProxy;
	
	/**
	 * 返回 category 列表
	 * @param attribute
	 * @param model
	 * @param request
	*/
	@RequestMapping(value="/listCatJSON2", method=RequestMethod.GET, produces=MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public  JSONObject  listCatJSON2() {
	   return 	categoryProxy.getAllData();
	}

	/**
	 * 转到category list页面.
	 */
	@RequestMapping(value="/list")
	public  void  list() {
		
	}

	/**
	 * 转转到增加 页面
	 * @param attribute
	 * @param model
	 * @param request
	 */
	@RequestMapping(value="/add")
	public String add( @RequestParam(value="catId",defaultValue="0") Long catId, Integer level,String code ,Model model) {
        if(catId!=0 || level !=0){
		String ansNameStr=categoryProxy.getAncestorsNameStr(catId);
		String levelName=categoryProxy.getChildLevelName(level);
		//当前类的level
		model.addAttribute("level", level+1);
		model.addAttribute("ansNameStr",ansNameStr);
		model.addAttribute("levelName",levelName);
		model.addAttribute("parentCode", code);
		model.addAttribute("parentId", catId);
        return "/basedata/category/add";}
        else{
        	model.addAttribute("level", 1);	
        return "/basedata/category/addBig";
        }
	}
	/**
	 *
	 * @param id
	 * @param model
	 * @return
	 */
	
	@RequestMapping("/edit")
	public String edit(@RequestParam(value = "id") Long id, Model model) {
		LOGGER.info("要编辑的id为"+id);
		Category category = categoryProxy.queryById(id).getData();
		model.addAttribute("category", category);
		Integer level = category.getLevel();
		if (level == 1) {
			return "/basedata/category/editBig";
		} else if (level == 3) {
			String ansNameStr = categoryProxy.getAncestorsNameStr(id);
			String levelName = categoryProxy.getChildLevelName(category.getLevel() - 1);
			model.addAttribute("ansNameStr", ansNameStr);
			model.addAttribute("levelName", levelName);
			List<DictionaryInfo> certDictionaryInfos = categoryProxy.selectCategoryCertByCat(id);
			model.addAttribute("certDictionaryInfos", certDictionaryInfos);
			return "/basedata/category/editSmall";
		} else {
			String ansNameStr = categoryProxy.getAncestorsNameStr(id);
			String levelName = categoryProxy.getChildLevelName(category.getLevel() - 1);
			model.addAttribute("ansNameStr", ansNameStr);
			model.addAttribute("levelName", levelName);
			return "/basedata/category/edit";
		}

	}
	
	@RequestMapping(value="/update")
     public @ResponseBody ResultInfo<?> updateCate(Category category) throws Exception{
		if(null==category){
			LOGGER.info("数据不能为空");
			throw new Exception("数据不能为空");
		}
		 categoryProxy.updateCategory(category);
		 return new ResultInfo<Boolean>(Boolean.TRUE);	
	}
	
	/**
	 * 保存 增加 的记录
	 * @param attribute
	 * @param model
	 * @param request
	 */
	@RequestMapping(value="/saveAdd", method=RequestMethod.POST)
	@ResponseBody
	public ResultInfo<?> saveAdd(Category category,String parentCode) throws Exception {
		if(null==category){
			LOGGER.info("数据不能为空");
			throw new Exception("数据异常");
		}
		 categoryProxy.addCatgory(category,parentCode);
		 return new ResultInfo<>(Boolean.TRUE);
	}
	
	
	/**
	 * 更新cate和attr的关系
	 * @param cateId
	 * @param ids
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/updateCateAttrLinked", method=RequestMethod.POST)
	@ResponseBody
	public ResultInfo<?> updateCateAttrLinked(Long cateId,Long[] ids) throws Exception{
		if(null==cateId){
			LOGGER.info("数据不能为空");
			throw new Exception("数据异常");
		}
		 categoryProxy.updateCateAttrLinked(cateId,ids);
		 return new ResultInfo<>(Boolean.TRUE);	
	}
	
	/**
	 *  删除cate和attr的关系
	 * @param cateId
	 * @param attrId
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/deleteCateAttrLinked", method=RequestMethod.GET)
	@ResponseBody
	public ResultInfo<?> deleteCateAttrLinked(Long cateId,Long  attrId) throws Exception{
		if(null==cateId ||null==attrId){
			LOGGER.info("数据不能为空");
			throw new Exception("数据异常");
		}
		categoryProxy.deleteCateAttrLinked(cateId,attrId);
		return new ResultInfo<>(Boolean.TRUE);
	}
	
	@RequestMapping("/addCataSpecGroupLinked")
	public void addCataSpecGroupLinked(@RequestParam(value="cateId")Long cateId,Model model){
	    List<CategorySpecGroupLink>  list =categoryProxy.queryCurrentCataSpecGroupLinked(cateId);
	    List<SpecGroup> specGroupList = specGroupProxy.queryByObject(new SpecGroup()).getData();
		model.addAttribute("cateId", cateId);	
		model.addAttribute("specGroupList", specGroupList);
		model.addAttribute("list", list);
	}
	
	@RequestMapping("/addCertLinked")
	public void addCertLinked(@RequestParam(value="cateId")Long cateId,Model model){
		DictionaryInfo dictionaryInfo = new DictionaryInfo();
		dictionaryInfo.setCode(DictionaryCode.c1002.getCode());
		List<DictionaryInfo> dictionaryInfos = dictionaryInfoProxy.queryByObject(dictionaryInfo).getData();
		List<DictionaryInfo> hasSelDictionaryInfos = categoryProxy.selectCategoryCertByCat(cateId);
		model.addAttribute("hasSelDictionaryInfos", hasSelDictionaryInfos);
		model.addAttribute("certDicts", dictionaryInfos);
		model.addAttribute("cateId", cateId);	
	}
	
	@RequestMapping("/updateCateCertLinked")
	@ResponseBody
	public ResultInfo<?> updateCateCertLinked(Long cateId, Long[] ids) throws Exception{
		if(null==cateId){
			LOGGER.info("数据不能为空");
			throw new Exception("数据异常");
		}
		categoryProxy.updateCateCertLinked(cateId,ids);
		return new ResultInfo<>(Boolean.TRUE);	
	}
	
	
	@RequestMapping("/updateCataSpecGroupLinked")
	@ResponseBody
	public ResultInfo<?> updateCataSpecGroupLinked(Long cateId, Long[] ids) throws Exception{
		if(null==cateId){
			LOGGER.info("数据不能为空");
			throw new Exception("数据异常");
		}
		categoryProxy.updateCataSpecGroupLinked(cateId,ids);
		return new ResultInfo<>(Boolean.TRUE);
	}
	
	@RequestMapping("/queryByCategoryAllByParentId")
	@ResponseBody
	public ResultInfo<List<Category>> queryByCategoryAllByParentId(Long parentId){
		if(parentId==null){
			parentId = 0L;
		}
		Map<String,Object> params = new HashMap<String,Object>();
        params.put("status", Constant.ENABLED.YES);
        params.put("parentId", parentId);
        return categoryProxy.queryByParam(params);
	}
	
}
