package com.tp.proxy.bse;

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
import com.tp.model.bse.ForbiddenWords;
import com.tp.proxy.BaseProxy;
import com.tp.service.IBaseService;
import com.tp.service.bse.IForbiddenWordsService;
import com.tp.util.CodeCreateUtil;
/**
 * 违禁词代理层
 * @author szy
 *
 */
@Service
public class ForbiddenWordsProxy extends BaseProxy<ForbiddenWords>{

	@Autowired
	private IForbiddenWordsService forbiddenWordsService;

	@Override
	public IBaseService<ForbiddenWords> getService() {
		return forbiddenWordsService;
	}
	
	/**
	 * 
	 * <pre>
	 * 增加违禁词
	 * </pre>
	 *
	 * @param forbiddenWords
	 */
	public void addForbiddenWords(ForbiddenWords forbiddenWords) throws Exception {
	    String words = forbiddenWords.getWords();
		if(StringUtils.isBlank(words)){
			throw new Exception("违禁词必填");
		}
		ForbiddenWords wordsSerarch =new ForbiddenWords();
		wordsSerarch.setWords(forbiddenWords.getWords().trim());
		List<ForbiddenWords> list = forbiddenWordsService.queryByObject(wordsSerarch);
		if(CollectionUtils.isNotEmpty(list)){
			throw new Exception("存在违禁词名称");
		}
		ForbiddenWords insertWords =new ForbiddenWords();
		insertWords.setCode(CodeCreateUtil.initForbiddenWordsCode());
		insertWords.setWords(forbiddenWords.getWords().trim());
		insertWords.setStatus(forbiddenWords.getStatus());
		insertWords.setType(1);
		insertWords.setRemark(forbiddenWords.getRemark().trim());
		insertWords.setCreatedTime(new Date());
		insertWords.setModifyTime(new Date());
		forbiddenWordsService.insert(insertWords);
	}

	/**
	 * 
	 * <pre>
	 * 根据id删除违禁词
	 * </pre>
	 *
	 * @param id
	 */
	public void deleteForbiddenWords(Long id) {
		forbiddenWordsService.deleteById(id);
	}

	/**
	 * 
	 * <pre>
	 * 更新违禁词
	 * </pre>
	 *
	 * @param forbiddenWords
	 * @param isAllField
	 */
	public void updateForbiddenWords(ForbiddenWords forbiddenWords,
			Boolean isAllField) throws Exception {
		String words = forbiddenWords.getWords();
		if (StringUtils.isBlank(words)) {
			throw new Exception("违禁词必填");
		}
		ForbiddenWords wordsSerarch = new ForbiddenWords();
		wordsSerarch.setWords(forbiddenWords.getWords().trim());
		List<ForbiddenWords> list = forbiddenWordsService
				.queryByObject(wordsSerarch);
		if (CollectionUtils.isNotEmpty(list)) {
			for (ForbiddenWords wordsDO : list) {
				Long id = wordsDO.getId();
				if (!id.equals(forbiddenWords.getId())) {
					throw new Exception("存在相相同的违禁词");
				}
			}
		}
		ForbiddenWords insertWords = new ForbiddenWords();
		insertWords.setId(forbiddenWords.getId());
		insertWords.setWords(words.trim());
		insertWords.setCode(forbiddenWords.getCode());
		insertWords.setRemark(forbiddenWords.getRemark().trim());
		insertWords.setType(1);
		insertWords.setStatus(forbiddenWords.getStatus());
		insertWords.setModifyTime(new Date());
		if (isAllField) {
			forbiddenWordsService.updateById(insertWords);
		} else {
			forbiddenWordsService.updateNotNullById(insertWords);
		}

	}

	/**
	 * 
	 * <pre>
	 * 分页查询
	 * </pre>
	 *
	 * @return
	 */
	public PageInfo<ForbiddenWords> queryAllForbiddenWordsByPageInfo(ForbiddenWords forbiddenWords) {
		PageInfo<ForbiddenWords> pageInfo = new PageInfo<ForbiddenWords>();
		pageInfo.setSize(50000);
		return forbiddenWordsService.queryPageByObject(forbiddenWords,pageInfo);
	}

	/**
	 * 
	 * <pre>
	 * 分页重载
	 * </pre>
	 *
	 * @param forbiddenWords
	 * @param startPageInfo
	 * @param pageSize
	 * @return
	 */
	public PageInfo<ForbiddenWords> queryAllForbiddenWordsByPageInfo(ForbiddenWords forbiddenWords, Integer startPageInfo, Integer pageSize) {
		PageInfo<ForbiddenWords> pageInfo = new PageInfo<ForbiddenWords>();
		if(startPageInfo!=null){
			pageInfo.setPage(startPageInfo);
		}
		if(pageSize!=null){
			pageInfo.setSize(pageSize);
		}
		return forbiddenWordsService.queryPageByObject(forbiddenWords, pageInfo);
	}

	/**
	 * 
	 * <pre>
	 * 获取一个forbiddenWords
	 * </pre>
	 *
	 * @param id
	 * @return
	 */
	public ForbiddenWords getBrandById(Long id) {
		return forbiddenWordsService.queryById(id);
	}

	/**
	 * 
	 * <pre>
	 * 根据条件动态获取List<ForbiddenWords>
	 * </pre>
	 *
	 * @param forbiddenWords
	 * @return
	 */
	public List<ForbiddenWords> selectDynamic(ForbiddenWords forbiddenWords){
		return forbiddenWordsService.queryByObject(forbiddenWords);
	}

	public PageInfo<ForbiddenWords> queryAllLikeForbiddenWordsByPageInfo(ForbiddenWords forbiddenWords, Integer page, Integer size) {
		if(null==forbiddenWords){
			forbiddenWords=new ForbiddenWords();
		}
		PageInfo<ForbiddenWords> pageInfo = new PageInfo<ForbiddenWords>(page,size);
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("code", forbiddenWords.getCode());
		params.put("type", forbiddenWords.getType());
		if(StringUtils.isNotBlank(forbiddenWords.getWords())){
			params.put(MYBATIS_SPECIAL_STRING.COLUMNS.name(), "words like concat('%','"+forbiddenWords.getWords()+"','%')");
		}
		if(StringUtils.isNotBlank(forbiddenWords.getRemark())){
			params.put(MYBATIS_SPECIAL_STRING.COLUMNS.name(),"remark like concat('%','"+forbiddenWords.getRemark()+"','%')");
		}
		params.put("status", forbiddenWords.getStatus());
		return forbiddenWordsService.queryPageByParamNotEmpty(params, pageInfo);
	}
	
	/**
	 * 
	 * <pre>
	 * 检查是否存在违禁词 ,存在则抛出违禁词异常信息
	 * </pre>
	 *
	 * @param sourceField
	 * @param type
	 * @throws Exception
	 */
	public 	void  checkForbiddenWordsField(String sourceField,String type) throws Exception {
		if(StringUtils.isNotBlank(sourceField)){
			String field = forbiddenWordsService.checkForbiddenWordsField(sourceField.trim());
			if(StringUtils.isNotBlank(field)){
				if(null==type){
					type="输入中";
				}
				throw new Exception(type+field);
			}	
		}	
	}
}
