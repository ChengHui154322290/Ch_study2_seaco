package com.tp.seller.ao.base;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.vo.PageInfo;
import com.tp.dto.common.FailInfo;
import com.tp.dto.common.ResultInfo;
import com.tp.model.bse.ForbiddenWords;
import com.tp.service.bse.IForbiddenWordsService;

/**
 * 
 * <pre>
 *     违禁词ao服务类
 * </pre>
 *
 * @author liuchunhua
 * @version $Id: ForbiddenWordsServiceAO.java, v 0.1 2014年12月21日 上午10:41:20 Administrator Exp $
 */
@Service
public class ForbiddenWordsAO {

	@Autowired
	private IForbiddenWordsService forbiddenWordsService;


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
	 * 分页查询
	 * </pre>
	 *
	 * @return
	 */
	public PageInfo<ForbiddenWords> queryAllForbiddenWordsByPageInfo(ForbiddenWords forbiddenWordsDO) {
		return forbiddenWordsService.queryPageByObject(forbiddenWordsDO,new PageInfo<ForbiddenWords>(forbiddenWordsDO.getStartPage(),forbiddenWordsDO.getPageSize()));
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
	public PageInfo<ForbiddenWords> queryAllForbiddenWordsByPageInfo(ForbiddenWords forbiddenWordsDO, int startPageInfo, int pageSize) {
		return forbiddenWordsService.queryPageByObject(forbiddenWordsDO,new PageInfo<ForbiddenWords>(startPageInfo,pageSize));
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
	 * @param forbiddenWordsDO
	 * @return
	 */
	public List<ForbiddenWords> queryByObject(ForbiddenWords forbiddenWordsDO){
		return forbiddenWordsService.queryByObject(forbiddenWordsDO);
	}

	public PageInfo<ForbiddenWords> queryAllLikeForbiddenWordsByPageInfo(ForbiddenWords forbiddenWordsDO, Integer pageNo, Integer pageSize) {
		if(null==forbiddenWordsDO){
			forbiddenWordsDO=new ForbiddenWords();
		}
		return forbiddenWordsService.queryPageByObject(forbiddenWordsDO,new PageInfo<ForbiddenWords>(pageNo,pageSize));
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
	public 	ResultInfo<Boolean>  checkForbiddenWordsField(String sourceField,String type){
		if(StringUtils.isNotBlank(sourceField)){
			String field = forbiddenWordsService.checkForbiddenWordsField(sourceField.trim());
			if(StringUtils.isNotBlank(field)){
				if(null==type){
					type="输入中";
				}
				new ResultInfo<Boolean>(new FailInfo(type+field,910));
			}	
		}	
		return new ResultInfo<Boolean>(Boolean.TRUE);
	}
}
