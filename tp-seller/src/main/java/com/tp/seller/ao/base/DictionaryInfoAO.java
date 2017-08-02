package com.tp.seller.ao.base;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.vo.PageInfo;
import com.tp.common.vo.BseConstant.DictionaryCode;
import com.tp.dto.common.FailInfo;
import com.tp.dto.common.ResultInfo;
import com.tp.model.bse.DictionaryCategory;
import com.tp.model.bse.DictionaryInfo;
import com.tp.service.bse.IDictionaryCategoryService;
import com.tp.service.bse.IDictionaryInfoService;

/**
 * 
 * <pre>
 *        DictionaryInfoServiceAO服务类
 * </pre>
 *
 * @author liuchunhua
 * @version $Id: DictionaryInfoServiceAO.java, v 0.1 2014年12月21日 下午1:45:01 Administrator Exp $
 */
@Service
public class DictionaryInfoAO {
 
	@Autowired
	private IDictionaryInfoService dictionaryInfoService;
	
	@Autowired
	private IDictionaryCategoryService dictionaryCategoryService;
	
	@Autowired
	private ForbiddenWordsAO forbiddenWordsAO;
	

	
	/**
	 * 
	 * <pre>
	 * 增加DictionaryInfo
	 * </pre>
	 *
	 * @param DictionaryInfo
	 */
	public ResultInfo<DictionaryInfo> addDictionaryInfo(DictionaryInfo dictionaryInfo){
		String code = dictionaryInfo.getCode();
		if(StringUtils.isBlank(code)){
			new ResultInfo<DictionaryInfo>(new FailInfo("必须选择一个种类",910));
		}
		String name = dictionaryInfo.getName();
		if(StringUtils.isBlank(name)){
			new ResultInfo<DictionaryInfo>(new FailInfo("请输入一个正确的字典名称",910));
		}

		ResultInfo<Boolean> forbiddenResultInfo = forbiddenWordsAO.checkForbiddenWordsField(name, "字典名称");
		if(!forbiddenResultInfo.success){
			return new ResultInfo<DictionaryInfo>(forbiddenResultInfo.msg);
		}
		DictionaryInfo infoSearch=new DictionaryInfo();
		infoSearch.setName(name.trim());
		List<DictionaryInfo> list = dictionaryInfoService.queryByObject(infoSearch);
		if(CollectionUtils.isNotEmpty(list)){
			new ResultInfo<Boolean>(new FailInfo("名字已经存在",910));
		}
		DictionaryCategory dictionaryCategory =new DictionaryCategory();
		dictionaryCategory.setCode(code.trim());
		Long categoryId = dictionaryCategoryService.queryByObject(dictionaryCategory).get(0).getId();
		DictionaryInfo dictionaryInsert=new DictionaryInfo();
		dictionaryInsert.setCatId(categoryId.toString());
		dictionaryInsert.setCode(code.trim());
		dictionaryInsert.setName(name.trim());
		dictionaryInsert.setCreateTime(new Date());
		dictionaryInsert.setModifyTime(new Date());
		if(dictionaryInsert.getId()!=null){
			dictionaryInfoService.updateNotNullById(dictionaryInsert);
		}else{
			dictionaryInfoService.insert(dictionaryInsert);
		}
		return new ResultInfo<DictionaryInfo>(dictionaryInsert);
	}

	/**
	 * 
	 * <pre>
	 * 根据id删除DictionaryInfo
	 * </pre>
	 *
	 * @param id
	 */
	public void deleteDictionaryInfo(Long id) {
		dictionaryInfoService.deleteById(id);
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
	public ResultInfo<Integer> updateDictionaryInfo(DictionaryInfo dictionaryInfo, Boolean isAllField)  throws Exception{
		
		String code = dictionaryInfo.getCode();
		if(StringUtils.isBlank(code)){
			return new ResultInfo<Integer>(new FailInfo("必须选择一个种类",910));
		}
		String name = dictionaryInfo.getName();
		if(StringUtils.isBlank(name)){
			return new ResultInfo<Integer>(new FailInfo("请输入一个正字典名称",910));
		}

		forbiddenWordsAO.checkForbiddenWordsField(name, "字典名称");
		DictionaryInfo infoSearch=new DictionaryInfo();
		infoSearch.setName(name.trim());
		List<DictionaryInfo> list = dictionaryInfoService.queryByObject(infoSearch);
		for(DictionaryInfo dictionaryItemInfo : list){
			Long id = dictionaryItemInfo.getId();
			if(!id.equals(dictionaryInfo.getId())){
				return new ResultInfo<Integer>(new FailInfo("名字已经存在",910));
			}
		}
		
		DictionaryCategory searchId =new DictionaryCategory();
		searchId.setCode(code.trim());
		Long categoryId = dictionaryCategoryService.queryByObject(searchId).get(0).getId();
		
		DictionaryInfo updateItemInfo=new DictionaryInfo();
		updateItemInfo.setId(dictionaryInfo.getId());
		updateItemInfo.setCode(code.trim());
		updateItemInfo.setCatId(categoryId.toString());
		updateItemInfo.setModifyTime(new Date());
		updateItemInfo.setName(name.trim());
		return new ResultInfo<Integer>(dictionaryInfoService.updateNotNullById(updateItemInfo));
	}

	/**
	 * 
	 * <pre>
	 * 分页查询
	 * </pre>
	 *
	 * @param DictionaryInfo
	 * @return
	 */
	public PageInfo<DictionaryInfo> queryAllDictionaryInfoByPageInfo(DictionaryInfo dictionaryCategory) {
		return dictionaryInfoService.queryPageByObject(dictionaryCategory,new PageInfo<DictionaryInfo>(dictionaryCategory.getStartPage(),dictionaryCategory.getPageSize()));
	}

	/**
	 * 
	 * <pre>
	 * 分页重载
	 * </pre>
	 *
	 * @param DictionaryInfo
	 * @param startPageInfo
	 * @param pageSize
	 * @return
	 */
	public PageInfo<DictionaryInfo> queryAllDictionaryInfoByPageInfo(DictionaryInfo dictionaryCategory, int startPageInfo,int pageSize) {
		return dictionaryInfoService.queryPageByObject(dictionaryCategory, new PageInfo<DictionaryInfo>(startPageInfo,pageSize));
	}
	
	
	/**
	 * 
	 * <pre>
	 *  获取一个DictionaryInfoResult
	 * </pre>
	 *
	 * @param id
	 * @return
	 */
	public DictionaryInfo getDictionaryInfoById(Long id){
		return dictionaryInfoService.queryById(id);
	}
	
	
	/**
	 * 
	 * <pre>
	 * 		根据dictCode查询数据字典里面的值
	 * </pre>
	 *
	 * @param dictCode
	 * @return
	 */
	public List<DictionaryInfo> getByDictionaryCode(String dictCode){
		DictionaryInfo dictionaryItemInfo = new DictionaryInfo();
		dictionaryItemInfo.setCode(dictCode);
		return dictionaryInfoService.queryByObject(dictionaryItemInfo);
	}
	
	/**
	 * 
	 * <pre>
	 * 根据条件动态获取List<DictionaryInfo>
	 * </pre>
	 *
	 * @param dictionaryItemInfo
	 * @return
	 */
	public List<DictionaryInfo> queryByObject(DictionaryInfo dictionaryItemInfo){
		return dictionaryInfoService.queryByObject(dictionaryItemInfo);
	}
	
	
	public Map<String, String> initCataGoryInfo() {
		// 税率类型类型
		Map<String, String> cataGoryTypes = new HashMap<String, String>();
		DictionaryCode[]values = DictionaryCode.values();
		for ( DictionaryCode sTax : values) {
			cataGoryTypes.put(sTax.getCode(), sTax.getName());
		}
		return cataGoryTypes;
	}

	public PageInfo<DictionaryInfo> queryAllDictionaryInfoLikesByPageInfo(DictionaryInfo dictionaryItemInfo, Integer pageNo, Integer pageSize) {
		if(null==dictionaryItemInfo){
			dictionaryItemInfo=new DictionaryInfo();
		}
		return dictionaryInfoService.queryPageByObject(dictionaryItemInfo, new PageInfo<DictionaryInfo>(pageNo,pageSize));
	}
	
}
