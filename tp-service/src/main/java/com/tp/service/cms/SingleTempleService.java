package com.tp.service.cms;

import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.tp.dao.cms.ImportLogDao;
import com.tp.dao.cms.SingleTepactivityDao;
import com.tp.dao.cms.SingleTepnodeDao;
import com.tp.dao.cms.SingleproTempleDao;
import com.tp.dfsutils.service.DfsService;
import com.tp.dto.cms.CmsSingleTempleDTO;
import com.tp.dto.cms.CmsSingleTepactivityDTO;
import com.tp.model.cms.ImportLog;
import com.tp.model.cms.SingleTepactivity;
import com.tp.model.cms.SingleTepnode;
import com.tp.model.cms.SingleproTemple;
import com.tp.service.cms.ISingleTempleService;

/**
 * 模板组装数据service
 * @author szy
 */
@Service(value="singleTempleService")
public class SingleTempleService  implements ISingleTempleService{

	@Autowired
	SingleproTempleDao singleproTempleDao;
	
	@Autowired
	SingleTepnodeDao singleTepnodeDao;
	
	@Autowired
	SingleTepactivityDao singleTepactivityDao;
	
	@Autowired
	DfsService dfsService;
	@Autowired
	ImportLogDao improtLogDao;
	
	@Override
	public List<CmsSingleTempleDTO> querySingleproTemple(
			CmsSingleTempleDTO cmsSingleTempleDTO) throws Exception {
		int pageNo = cmsSingleTempleDTO.getStartPage();
		int pageSize = cmsSingleTempleDTO.getPageSize();
		
		cmsSingleTempleDTO.setStartPage((pageNo > 1 ? (pageNo - 1) * pageSize : 0));
		
		List<CmsSingleTempleDTO> mdList = new ArrayList<CmsSingleTempleDTO>();
		List<CmsSingleTempleDTO> list = singleproTempleDao.selectSingleTemples(cmsSingleTempleDTO);
		Long counts = singleproTempleDao.selectSingleTemplesCounts(cmsSingleTempleDTO);
		int countnum = (int)Math.ceil(counts/Double.parseDouble(pageSize+""));
		for(int i=0;i<list.size();i++){
			CmsSingleTempleDTO cmsDTO = list.get(i);
			cmsDTO.setStartPage(pageNo);
			cmsDTO.setPageSize(pageSize);
			cmsDTO.setTotalCount(countnum);
			cmsDTO.setTotalCountNum(counts.intValue());
			mdList.add(cmsDTO);
		}
		return mdList;
	}

	/**
	 * 单品团模板的保存
	 * @param params
	 * @param counts
	 * @param cmsSingleTempleDTO
	 * @return
	 * @throws Exception
	 */
	@Transactional
	@Override
	public int insertSingleTemple(String params, int counts,
			CmsSingleTempleDTO cmsSingleTempleDTO) throws Exception {
		SingleproTemple singleproTemple = new SingleproTemple();
		BeanUtils.copyProperties(cmsSingleTempleDTO, singleproTemple);
		if(cmsSingleTempleDTO.getId() == null || "".equals(cmsSingleTempleDTO.getId())){
			singleproTempleDao.insert(singleproTemple);
			cmsSingleTempleDTO.setSingleTempleId(singleproTemple.getId());
		}else{
			singleproTempleDao.updateNotNullById(singleproTemple);
			cmsSingleTempleDTO.setSingleTempleId(singleproTemple.getId());
		}
		
		/**
		 * 以下是保存数据
		 */
		Object obj2 = JSONValue.parse(params);
		JSONObject jsobjmp = (JSONObject)obj2;
		/*String info = jsobjmp.get("info").toString();
		String removeIds = jsobjmp.get("removeIds").toString();*/
		List<Long> removeIds = new ArrayList<Long>();
		JSONArray objarray1 = (JSONArray)jsobjmp.get("info");
		
		JSONArray objarray2 = (JSONArray)jsobjmp.get("removeIds");
		for(int i=0;i<objarray2.size();i++){
			removeIds.add((Long.parseLong(objarray2.get(i).toString())));
		}
		
		//清除被删除的数据
		if(removeIds.size()>0){
			counts = singleTepnodeDao.deleteByIds(removeIds);
		}
		
		//开始增加数据(需要改成手动提交事物，通过手动事物控制批量更新)
		for(int i=0;i<objarray1.size();i++){
			JSONObject jsobj = (JSONObject)objarray1.get(i);
			CmsSingleTempleDTO obj3 = new CmsSingleTempleDTO();
			obj3.setSingleTempleId(cmsSingleTempleDTO.getSingleTempleId());
			if(!StringUtils.isEmpty(jsobj.get("buriedCode"))){
				obj3.setBuriedCode(jsobj.get("buriedCode").toString());
			}
			if(!StringUtils.isEmpty(jsobj.get("positionName"))){
				obj3.setPositionName(jsobj.get("positionName").toString());
			}
			if(!StringUtils.isEmpty(jsobj.get("positionSize"))){
				obj3.setPositionSize(jsobj.get("positionSize").toString());
			}
			if(!StringUtils.isEmpty(jsobj.get("positionSort"))){
				obj3.setPositionSort(Integer.valueOf(jsobj.get("positionSort").toString()));
			}
			obj3.setDr(0);
			SingleTepnode singleTepnode = new SingleTepnode();
			BeanUtils.copyProperties(obj3, singleTepnode);
			if(!StringUtils.isEmpty(jsobj.get("id"))){
				singleTepnode.setId(Long.parseLong(jsobj.get("id").toString()));
				counts = singleTepnodeDao.updateById(singleTepnode);
			}else{
				singleTepnodeDao.insert(singleTepnode);
				if(singleTepnode.getId() != null)
					counts = 1;
				else
					counts = 0;
			}
			
	     }
		return counts;
	}
	
	@Override
	public int delSingleproTempleByIds(List<Long> ids) throws Exception {
		int count = singleproTempleDao.forbiddenTempleByIds(ids);
		return count;
	}

	@Override
	public int addSingleproTemple(CmsSingleTempleDTO cmsSingleTempleDTO) throws Exception {
		SingleproTemple singleproTemple = new SingleproTemple();
		BeanUtils.copyProperties(cmsSingleTempleDTO, singleproTemple);
		singleproTempleDao.insert(singleproTemple);
		if(singleproTemple.getId() != null)
			return 1;
		else
			return 0;
	}

	@Override
	public int updateSingleproTemple(CmsSingleTempleDTO cmsSingleTempleDTO)
			throws Exception {
		SingleproTemple singleproTemple = new SingleproTemple();
		BeanUtils.copyProperties(cmsSingleTempleDTO, singleproTemple);
		int count = singleproTempleDao.updateNotNullById(singleproTemple);
		return count;
	}

	@Override
	public int updateSingleproTempleNode(CmsSingleTempleDTO cmsSingleTempleDTO)
			throws Exception {
		SingleTepnode singleTepnode = new SingleTepnode();
		BeanUtils.copyProperties(cmsSingleTempleDTO, singleTepnode);
		int count = singleTepnodeDao.updateNotNullById(singleTepnode);
		return count;
	}

	@Override
	public int addSingleproTempleNode(CmsSingleTempleDTO cmsSingleTempleDTO)
			throws Exception {
		SingleTepnode singleTepnode = new SingleTepnode();
		BeanUtils.copyProperties(cmsSingleTempleDTO, singleTepnode);
		singleTepnodeDao.insert(singleTepnode);
		if(singleTepnode.getId() != null)
			return 1;
		return 0;
	}

	@Override
	public List<CmsSingleTempleDTO> selectSingleActivity(
			CmsSingleTempleDTO cmsSingleTempleDTO) throws Exception {
		SingleTepnode singleTepnode = new SingleTepnode();
		BeanUtils.copyProperties(cmsSingleTempleDTO, singleTepnode);
		return singleTepnodeDao.selectSingleActivity(singleTepnode);
	}

	@Override
	public List<SingleTepnode> selectDynamic(
			CmsSingleTempleDTO cmsSingleTempleDTO) throws Exception {
		SingleTepnode singleTepnode = new SingleTepnode();
		BeanUtils.copyProperties(cmsSingleTempleDTO, singleTepnode);
		return singleTepnodeDao.queryByObject(singleTepnode);
	}

	@Override
	public List<SingleTepactivity> selectActityByNode(
			SingleTepactivity cmsSingleTepactivityDO) throws Exception {
		return singleTepactivityDao.queryByObject(cmsSingleTepactivityDO);
	}

	@Override
	public List<SingleTepactivity> selectActityByNodePageQuery(CmsSingleTepactivityDTO cmsSingleTepactivityDTO) throws Exception {
		SingleTepactivity singleTepactivity = new SingleTepactivity();
		BeanUtils.copyProperties(cmsSingleTepactivityDTO, singleTepactivity);
		return singleTepactivityDao.queryByObject(singleTepactivity);
	}

	@Override
	public List<SingleTepactivity> selectActityIDs(SingleTepactivity cmsSingleTepactivityDO) throws Exception {
		return singleTepactivityDao.queryByObject(cmsSingleTepactivityDO);
	}

	@Override
	public int delActivityByID(CmsSingleTepactivityDTO cmsSingleTepactivityDTO) throws Exception {
		return singleTepactivityDao.deleteByTempleNodeId(cmsSingleTepactivityDTO);
	}

	@Override
	public int insertActivityId(CmsSingleTepactivityDTO cmsSingleTepactivityDTO)
			throws Exception {
		Long ct = singleTepactivityDao.selectIsExistid(cmsSingleTepactivityDTO);
		if(ct > 0){
			return 0;
		}else{
			return singleTepactivityDao.insertActivityId(cmsSingleTepactivityDTO);
		}
		
	}

	@Override
	public int delSingleproTempleNodeByIds(List<Long> ids) throws Exception {
		return singleTepnodeDao.deleteByIds(ids);
	}
	@Override
	public Long saveImportTempleLog(ImportLog importTempleLogDO) throws Exception {
		improtLogDao.insert(importTempleLogDO);
		return importTempleLogDO.getId();
	}
}
