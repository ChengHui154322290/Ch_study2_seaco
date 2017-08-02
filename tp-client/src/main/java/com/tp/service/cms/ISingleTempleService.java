package com.tp.service.cms;

import java.util.List;

import com.tp.dto.cms.CmsSingleTempleDTO;
import com.tp.dto.cms.CmsSingleTepactivityDTO;
import com.tp.exception.CmsServiceException;
import com.tp.model.cms.ImportLog;
import com.tp.model.cms.SingleTepactivity;
import com.tp.model.cms.SingleTepnode;

/**
* 单品团模板管理 Service
* @author szy
*/
public interface ISingleTempleService {

	
	/**
	 * 根据模板类型或主键，查询模板信息，单表查询
	 */
	List<SingleTepnode>  selectDynamic (CmsSingleTempleDTO cmsSingleTempleDTO) throws Exception;
	
	/**
	 * querySingleproTemple方法是通过模板id去找位置表id以及位置顺序
	 * @param json的string
	 * @return json的string
	 * @throws CmsServiceException
	 * @author szy
	 * @throws Exception 
	 */
	List<CmsSingleTempleDTO> querySingleproTemple(CmsSingleTempleDTO cmsSingleTempleDTO) throws Exception;
	
	/**
	 * 根据模板节点表主键，查询模板活动信息
	 */
	List<SingleTepactivity>  selectActityByNode (SingleTepactivity cmsSingleTepactivityDO) throws Exception;
	
	/**
	 * 今日特卖，按每次3条查询，分页查询
	 */
	List<SingleTepactivity>  selectActityByNodePageQuery (CmsSingleTepactivityDTO cmsSingleTepactivityDTO) throws Exception;
	
	/**
	 * 单品团模板的保存
	 * @param params
	 * @param counts
	 * @param cmsSingleTempleDTO
	 * @return
	 * @throws Exception
	 */
	public int insertSingleTemple(String params, int counts,
			CmsSingleTempleDTO cmsSingleTempleDTO) throws Exception ;
	
	int delSingleproTempleByIds(List<Long> ids) throws Exception;
	
	int delSingleproTempleNodeByIds(List<Long> ids) throws Exception;
	
	int addSingleproTemple(CmsSingleTempleDTO cmsSingleTempleDTO) throws Exception;
	
	int addSingleproTempleNode(CmsSingleTempleDTO cmsSingleTempleDTO) throws Exception;
	
	int updateSingleproTemple(CmsSingleTempleDTO cmsSingleTempleDTO) throws Exception;
	
	int updateSingleproTempleNode(CmsSingleTempleDTO cmsSingleTempleDTO) throws Exception;
	
	List<CmsSingleTempleDTO> selectSingleActivity(CmsSingleTempleDTO cmsSingleTempleDTO) throws Exception;
	
	List<SingleTepactivity> selectActityIDs(SingleTepactivity cmsSingleTepactivityDO) throws Exception;
	
	int delActivityByID(CmsSingleTepactivityDTO cmsSingleTepactivityDTO) throws Exception;
	
	int insertActivityId(CmsSingleTepactivityDTO cmsSingleTepactivityDTO) throws Exception;

	Long saveImportTempleLog(ImportLog importTempleLogDO) throws Exception;
}
