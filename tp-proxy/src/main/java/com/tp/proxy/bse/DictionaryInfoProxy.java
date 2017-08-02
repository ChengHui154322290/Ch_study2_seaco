package com.tp.proxy.bse;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.vo.BseConstant;
import com.tp.common.vo.PageInfo;
import com.tp.common.vo.BseConstant.DictionaryCode;
import com.tp.common.vo.DAOConstant.MYBATIS_SPECIAL_STRING;
import com.tp.dto.common.FailInfo;
import com.tp.dto.common.ResultInfo;
import com.tp.model.bse.CustomsUnitInfo;
import com.tp.model.bse.CustomsUnitLink;
import com.tp.model.bse.DictionaryCategory;
import com.tp.model.bse.DictionaryInfo;
import com.tp.proxy.BaseProxy;
import com.tp.service.IBaseService;
import com.tp.service.bse.ICustomsUnitInfoService;
import com.tp.service.bse.ICustomsUnitLinkService;
import com.tp.service.bse.IDictionaryCategoryService;
import com.tp.service.bse.IDictionaryInfoService;
/**
 * 数据字典：信息代理层
 * @author szy
 *
 */
@Service
public class DictionaryInfoProxy extends BaseProxy<DictionaryInfo>{

	@Autowired
	private IDictionaryInfoService dictionaryInfoService;
	@Autowired
	private IDictionaryCategoryService dictionaryCategoryService;
	
	@Autowired
	private ICustomsUnitInfoService customsUnitInfoService;
	
	@Autowired
	private ICustomsUnitLinkService customsUnitLinkService;
	
	@Autowired
	private ForbiddenWordsProxy forbiddenWordsProxy;

	@Override
	public IBaseService<DictionaryInfo> getService() {
		return dictionaryInfoService;
	}

	/**
	 * 
	 * <pre>
	 * 增加DictionaryInfo
	 * </pre>
	 *
	 * @param DictionaryInfo
	 */
	public ResultInfo<DictionaryInfo> addDictionaryInfo(DictionaryInfo dictionaryInfo, Long customsUnitId)  throws Exception{
		String code = dictionaryInfo.getCode();
		if(StringUtils.isBlank(code)){
			return new ResultInfo<>(new FailInfo("必须选择一个种类"));
		}
		String name = dictionaryInfo.getName();
		if(StringUtils.isBlank(name)){
			return new ResultInfo<>(new FailInfo("请输入一个正确的字典名称"));
		}

		forbiddenWordsProxy.checkForbiddenWordsField(name, "字典名称");
		DictionaryInfo infoSearch=new DictionaryInfo();
		infoSearch.setName(name.trim());
		List<DictionaryInfo> list = dictionaryInfoService.queryByObject(infoSearch);
		if(CollectionUtils.isNotEmpty(list)){
			return new ResultInfo<>(new FailInfo("名字已经存在"));
		}
		//单位要验证海关计量单位
		if (DictionaryCode.c1001.getCode().equals(dictionaryInfo.getCode()) && customsUnitId != null) {
			CustomsUnitInfo customsUnitInfo = customsUnitInfoService.queryById(customsUnitId);
			if (null == customsUnitInfo) {
				return new ResultInfo<>(new FailInfo("海关计量单位不存在"));
			}
		}
		DictionaryCategory dictionaryCategory =new DictionaryCategory();
		dictionaryCategory.setCode(code.trim());
		Long categoryId = dictionaryCategoryService.queryUniqueByObject(dictionaryCategory).getId();
		DictionaryInfo dictionaryInsert=new DictionaryInfo();
		dictionaryInsert.setCatId(categoryId.toString());
		dictionaryInsert.setCode(code.trim());
		dictionaryInsert.setSortNo((int)(Math.random()*100));//tmp
		dictionaryInsert.setName(name.trim());
		dictionaryInsert.setCreateTime(new Date());
		dictionaryInsert.setModifyTime(new Date());
		dictionaryInsert= dictionaryInfoService.insert(dictionaryInsert);
		//计量单位
		if (DictionaryCode.c1001.getCode().equals(dictionaryInfo.getCode()) && customsUnitId != null) {			
			CustomsUnitLink link = new CustomsUnitLink();
			link.setCustomsUnitId(customsUnitId);
			link.setUnitId(dictionaryInsert.getId());
			link.setCreateTime(new Date());
			customsUnitLinkService.insert(link);
		}
		return new ResultInfo<>(dictionaryInsert);
	}
	
	/**
	 * 
	 * <pre>
	 * 更新DictionaryInfo
	 * </pre>
	 *
	 * @param DictionaryInfo
	 * @param isAllField
	 */
	public ResultInfo<DictionaryInfo> updateDictionaryInfo(DictionaryInfo dictionaryInfo, Long customsUnitId, Boolean isAllField)  throws Exception{
		String code = dictionaryInfo.getCode();
		if(StringUtils.isBlank(code)){
			return new ResultInfo<>(new FailInfo("必须选择一个种类"));
		}
		String name = dictionaryInfo.getName();
		if(StringUtils.isBlank(name)){
			return new ResultInfo<>(new FailInfo("请输入一个正确的字典名称"));
		}

		forbiddenWordsProxy.checkForbiddenWordsField(name, "字典名称");
		DictionaryInfo infoSearch=new DictionaryInfo();
		infoSearch.setName(name.trim());
		List<DictionaryInfo> list = dictionaryInfoService.queryByObject(infoSearch);
		if(CollectionUtils.isNotEmpty(list)){
			for(DictionaryInfo dictionaryInfoDO : list){
				Long id = dictionaryInfoDO.getId();
				if(!id.equals(dictionaryInfo.getId())){
					return new ResultInfo<>(new FailInfo("名字已经存在"));
				}
			}
		}
		//校验海关计量单位
		if (DictionaryCode.c1001.getCode().equals(dictionaryInfo.getCode()) && customsUnitId != null) {
			CustomsUnitInfo customsUnitInfo = customsUnitInfoService.queryById(customsUnitId);
			if (null == customsUnitInfo) {
				return new ResultInfo<>(new FailInfo("海关计量单位不存在"));
			}
		}
		
		DictionaryCategory searchId =new DictionaryCategory();
		searchId.setCode(code.trim());
		Long categoryId = dictionaryCategoryService.queryUniqueByObject(searchId).getId();
		
		DictionaryInfo updateInfo=new DictionaryInfo();
		updateInfo.setId(dictionaryInfo.getId());
		updateInfo.setCode(code.trim());
		updateInfo.setCatId(categoryId.toString());
		updateInfo.setModifyTime(new Date());
		updateInfo.setName(name.trim());
		if(isAllField){
			dictionaryInfoService.updateById(updateInfo);
		}else{
			dictionaryInfoService.updateNotNullById(updateInfo);
		}
		if (DictionaryCode.c1001.getCode().equals(dictionaryInfo.getCode()) && customsUnitId != null) {
			CustomsUnitLink link = new CustomsUnitLink();
			link.setUnitId(dictionaryInfo.getId());
			CustomsUnitLink linkObj = customsUnitLinkService.queryUniqueByObject(link);
			if (null != linkObj) {				
				linkObj.setCustomsUnitId(customsUnitId);
				customsUnitLinkService.updateById(linkObj);
			}else{
				CustomsUnitLink l = new CustomsUnitLink();
				l.setCustomsUnitId(customsUnitId);
				l.setUnitId(dictionaryInfo.getId());
				l.setCreateTime(new Date());
				customsUnitLinkService.insert(l);
			}
		}	
		return new ResultInfo<>(updateInfo);
	}
	
	public PageInfo<DictionaryInfo> queryAllDictionaryInfoLikesByPage(DictionaryInfo dictionaryInfo, Integer pageNo, Integer pageSize) {
		if(null==dictionaryInfo){
			dictionaryInfo=new DictionaryInfo();
		}
		PageInfo<DictionaryInfo> pageInfo = new PageInfo<DictionaryInfo>(pageNo,pageSize);
		Map<String,Object> params = new HashMap<String,Object>();
		if(StringUtils.isNoneBlank(dictionaryInfo.getName())){
			params.put(MYBATIS_SPECIAL_STRING.COLUMNS.name(), " name like concat('%','"+dictionaryInfo.getName()+"','%')");
		}
		if(StringUtils.isNoneBlank(dictionaryInfo.getCode())){
			params.put(MYBATIS_SPECIAL_STRING.COLUMNS.name(), " code like concat('%','"+dictionaryInfo.getCode()+"','%')");
		}
		params.put("catId", dictionaryInfo.getCatId());
		params.put("sortNo", dictionaryInfo.getSortNo());
		return dictionaryInfoService.queryPageByParamNotEmpty(params, pageInfo);
	}
	
	public Map<String, String> initCataGoryInfo() {
		// 税率类型类型
		Map<String, String> cataGoryTypes = new HashMap<String, String>();
		BseConstant.DictionaryCode[] values = BseConstant.DictionaryCode.values();
		for (BseConstant.DictionaryCode sTax : values) {
			cataGoryTypes.put(sTax.getCode(), sTax.getName());
		}
		return cataGoryTypes;
	}
}
