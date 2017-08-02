package com.tp.backend.controller.bse;

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
import com.tp.common.vo.PageInfo;
import com.tp.common.vo.BseConstant.DictionaryCode;
import com.tp.dto.common.ResultInfo;
import com.tp.model.bse.CustomsUnitInfo;
import com.tp.model.bse.CustomsUnitLink;
import com.tp.model.bse.DictionaryCategory;
import com.tp.model.bse.DictionaryInfo;
import com.tp.proxy.bse.CustomsUnitInfoProxy;
import com.tp.proxy.bse.CustomsUnitLinkProxy;
import com.tp.proxy.bse.DictionaryCategoryProxy;
import com.tp.proxy.bse.DictionaryInfoProxy;

/**
 * 
 * <pre>
 *    DictionaryInfo
 * </pre>
 *
 * @author szy
 * @version 0.0.1
 */
@Controller
@RequestMapping("/basedata/dictionary/info")
public class DictionaryInfoController extends AbstractBaseController {
	  private static final Logger LOGGER = LoggerFactory.getLogger(DictionaryInfoController.class);

	@Autowired
	private DictionaryInfoProxy dictionaryInfoProxy;
	
	@Autowired
	private DictionaryCategoryProxy dictionaryCategoryProxy;
	
	@Autowired
	private CustomsUnitInfoProxy customsUnitInfoProxy;
	
	@Autowired
	private CustomsUnitLinkProxy customsUnitLinkProxy;

	/**
	 * 
	 * <pre>
	 * dictionaryInfo列表查询
	 * </pre>
	 *
	 * @param model
	 * @param DictionaryInfo
	 */
	@RequestMapping(value = "/list")
	public void list(Model model,DictionaryInfo dictionaryInfo,Integer sortNo,Integer page,Integer size) throws Exception {
		dictionaryInfo.setSortNo(sortNo);
	    PageInfo<DictionaryInfo> queryAllDictionaryInfoByPage = dictionaryInfoProxy.queryAllDictionaryInfoLikesByPage(dictionaryInfo, page, size);
	    Map<String, String> cataGoryInfoMap = dictionaryInfoProxy.initCataGoryInfo();
	    model.addAttribute("queryAllDictionaryInfoByPage",queryAllDictionaryInfoByPage);
	    model.addAttribute("cataGoryInfoMap", cataGoryInfoMap);
	}

	/**
	 * 
	 * <pre>
	 * dictionaryInfo编辑
	 * </pre>
	 *
	 * @param model
	 * @param DictionaryInfo
	 */
	@RequestMapping(value = "/edit")
	public void edit(@RequestParam(value = "id") Long id, Model model) {
		List<DictionaryCategory> listOfDictionaryCategory = dictionaryCategoryProxy.queryByObject(new DictionaryCategory()).getData();
		ResultInfo<DictionaryInfo> dictionaryInfoResultInfo = dictionaryInfoProxy.queryById(id);
	    Map<String, String> cataGoryInfoMap = dictionaryInfoProxy.initCataGoryInfo();
		model.addAttribute("dictionaryInfo", dictionaryInfoResultInfo.getData());
		model.addAttribute("listOfDictionaryCategory",listOfDictionaryCategory);
	    model.addAttribute("cataGoryInfoMap", cataGoryInfoMap);
	    if (dictionaryInfoResultInfo.getData() != null && DictionaryCode.c1001.getCode().equals(dictionaryInfoResultInfo.getData().getCode())) {
			ResultInfo<List<CustomsUnitInfo>> unitInfos = customsUnitInfoProxy.queryByObject(new CustomsUnitInfo());
			model.addAttribute("customsUnitList", unitInfos.getData());
			CustomsUnitLink link = new CustomsUnitLink();
			link.setUnitId(dictionaryInfoResultInfo.getData().getId());
			ResultInfo<CustomsUnitLink> linkResult = customsUnitLinkProxy.queryUniqueByObject(link);
			if (linkResult.getData() != null) {
				model.addAttribute("cuId", linkResult.getData().getCustomsUnitId());
			}
		}
	}

	
	@RequestMapping(value = "/add")
	public void addBrand(Model model) {
	    Map<String, String> cataGoryInfoMap = dictionaryInfoProxy.initCataGoryInfo();
	    model.addAttribute("cataGoryInfoMap", cataGoryInfoMap);
	}

	/**
	 * 
	 * <pre>
	 * dictionaryInfo增加
	 * </pre>
	 *
	 * @param
	 * @throws Exception 
	 */
	@RequestMapping(value = "/addDictionaryInfoSubmit")
	@ResponseBody
	public ResultInfo<?> addDictionaryInfoSubmit(
			DictionaryInfo dictionaryInfo, Long customsUnitId) throws Exception {
		if (null==dictionaryInfo) {
			 LOGGER.error("数据异常");
			throw new Exception("数据异常");
		}
		return dictionaryInfoProxy.addDictionaryInfo(dictionaryInfo, customsUnitId);
	}

	/**
	 * 
	 * <pre>
	 * 跟新dictionaryInfo
	 * </pre>
	 *
	 * @param
	 */
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	@ResponseBody
	public ResultInfo<?> updateBrand(DictionaryInfo dictionaryInfo, Long customsUnitId)  throws Exception{
		if (null==dictionaryInfo) {
			 LOGGER.error("数据异常");
			 throw new Exception("数据异常");
		}
		return dictionaryInfoProxy.updateDictionaryInfo(dictionaryInfo, customsUnitId, false);
	}
	
	
	@RequestMapping(value = "/listOfDictionaryCategory")
	public @ResponseBody List<DictionaryCategory> listOfDictionaryCategory() {
		return dictionaryCategoryProxy.queryByObject(new DictionaryCategory()).getData();
	
	}

	@RequestMapping("/customsUnit")
	@ResponseBody
	public ResultInfo<List<CustomsUnitInfo>> queryCustomsUnit(){
		return customsUnitInfoProxy.queryByObject(new CustomsUnitInfo());
	}
}
