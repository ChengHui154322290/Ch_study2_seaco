package com.tp.backend.controller.bse;

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
import com.tp.common.vo.PageInfo;
import com.tp.dto.common.ResultInfo;
import com.tp.model.bse.DictionaryCategory;
import com.tp.proxy.bse.DictionaryCategoryProxy;

/**
 * 
 * <pre>
 *    DictionaryCategory
 * </pre>
 *
 * @author szy
 * @version 0.0.1
 */
@Controller
@RequestMapping("/basedata/dictionary/category")
public class DictionaryCategoryController extends AbstractBaseController {
	
	 private static final Logger LOGGER = LoggerFactory.getLogger(DictionaryCategoryController.class);

	@Autowired
	private DictionaryCategoryProxy dictionaryCategoryProxy;

	/**
	 * 
	 * <pre>
	 * dictionaryCategory列表查询
	 * </pre>
	 *
	 * @param model
	 * @param DictionaryCategory
	 */
	@RequestMapping(value = "/list")
	public void list(Model model,DictionaryCategory dictionaryCategory,	Integer pageNo,Integer pageSize)
			throws Exception {
		PageInfo<DictionaryCategory> pageInfo = new PageInfo<DictionaryCategory>();
		if(pageNo!=null){
			pageInfo.setPage(pageNo);
		}
		if(pageSize!=null){
			pageInfo.setSize(pageSize);
		}
		ResultInfo<PageInfo<DictionaryCategory>> queryAllDictionaryCategoryByPageInfoResultInfo = dictionaryCategoryProxy.queryPageByObject(dictionaryCategory, pageInfo);
		
		model.addAttribute("queryAllDictionaryCategoryByPageInfo",queryAllDictionaryCategoryByPageInfoResultInfo.getData());
	}

	/**
	 * 
	 * <pre>
	 * dictionaryCategory编辑
	 * </pre>
	 *
	 * @param model
	 * @param DictionaryCategory
	 */
	@RequestMapping(value = "/edit")
	public void edit(@RequestParam(value = "id") Long id, Model model) {
		ResultInfo<DictionaryCategory> dictionaryCategoryResultInfo = dictionaryCategoryProxy.queryById(id);
		
		model.addAttribute("dictionaryCategory", dictionaryCategoryResultInfo.getData());
	}

	
	@RequestMapping(value = "/add")
	public void addBrand() {

	}

	/**
	 * 
	 * <pre>
	 * dictionaryCategory增加
	 * </pre>
	 *
	 * @param
	 */
	@RequestMapping(value = "/addDictionaryCategorySubmit")
	@ResponseBody
	public ResultInfo<?> addDictionaryCategorySubmit(DictionaryCategory dictionaryCategory) throws Exception{
		if (null==dictionaryCategory ) {
			LOGGER.info("数据不能为空");
			throw new Exception("数据异常");
		}
		return dictionaryCategoryProxy.addDictionaryCategory(dictionaryCategory);
	}

	/**
	 * 
	 * <pre>
	 * 修改dictionaryCategory
	 * </pre>
	 *
	 * @param
	 */
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	@ResponseBody
	public ResultInfo<?> updateBrand(DictionaryCategory dictionaryCategory) throws Exception {
		if (null==dictionaryCategory) {
			LOGGER.info("数据不能为空");
			throw new Exception("数据异常");
		}
		return dictionaryCategoryProxy.updateDictionaryCategory(dictionaryCategory, false);
	}

}
