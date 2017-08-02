package com.tp.service.cms;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.dao.BaseDao;
import com.tp.common.vo.PageInfo;
import com.tp.dao.cms.PictureElementDao;
import com.tp.dto.cms.CmsPictureElementDTO;
import com.tp.model.cms.PictureElement;
import com.tp.service.BaseService;
import com.tp.service.cms.IPictureElementService;

@Service(value="pictureElementService")
public class PictureElementService extends BaseService<PictureElement> implements IPictureElementService{

	private Log logger = LogFactory.getLog(this.getClass());

	@Autowired
	private PictureElementDao pictureElementDao;
	
	/*@Autowired
	DfsDomainUtil dfsDomainUtil;*/
	
	@Autowired
	SwitchBussiesConfigDao switchBussiesConfigDao;



	public PageInfo<CmsPictureElementDTO> queryPageListByPictureElement(PictureElement cmsPictureElementDO) {
		if (cmsPictureElementDO != null) {
			Integer totalCount = queryByObjectCount(cmsPictureElementDO);
			
			PageInfo<PictureElement> pageInfo = new PageInfo<PictureElement>(cmsPictureElementDO.getStartPage(),cmsPictureElementDO.getPageSize());
			pageInfo = queryPageByObject(cmsPictureElementDO, pageInfo);
			List<CmsPictureElementDTO> lst = new ArrayList<CmsPictureElementDTO>();
			List<PictureElement> resultList = pageInfo.getRows();
			
			for(int i=0,j=resultList.size();i<j;i++){
				CmsPictureElementDTO CmsPictureElementDTO = new CmsPictureElementDTO();
				cmsPictureElementDO = resultList.get(i);
				
				try {
					BeanUtils.copyProperties(CmsPictureElementDTO, cmsPictureElementDO);
					if(cmsPictureElementDO.getActivityid() == null){
						CmsPictureElementDTO.setActivityid(null);
					}
				} catch (IllegalAccessException e) {
					logger.error("图片元素查询，bean复制报错", e);
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					logger.error("图片元素查询，bean复制报错", e);
					e.printStackTrace();
				}
				
				if(cmsPictureElementDO.getPicSrc() != null){
					/*CmsPictureElementDTO.setPicSrcStr(
							dfsDomainUtil.getFileFullUrl(cmsPictureElementDO.getPicSrc()));*/
					CmsPictureElementDTO.setPicSrcStr(
							switchBussiesConfigDao.getFullPictureSrc_PC(cmsPictureElementDO.getPicSrc()));
					
				}
				if(cmsPictureElementDO.getRollpicsrc() != null){
					/*CmsPictureElementDTO.setRollPicSrcStr(
							dfsDomainUtil.getFileFullUrl(cmsPictureElementDO.getRollPicSrc()));*/
					CmsPictureElementDTO.setRollPicSrcStr(
							switchBussiesConfigDao.getFullPictureSrc_PC(cmsPictureElementDO.getRollpicsrc()));
				}
				lst.add(CmsPictureElementDTO);
			}

			PageInfo<CmsPictureElementDTO> page = new PageInfo<CmsPictureElementDTO>();
			page.setPage(cmsPictureElementDO.getStartPage());
			page.setSize(cmsPictureElementDO.getPageSize());
			page.setRecords(totalCount.intValue());
			//page.setList(resultList);
			page.setRows(lst);
			return page;
		}
		return new PageInfo<CmsPictureElementDTO>();
	}

	public PageInfo<CmsPictureElementDTO> queryPageListByPictureElementAndStartPageSize(PictureElement cmsPictureElementDO,int startPage,int pageSize){
		if (cmsPictureElementDO != null && startPage>0 && pageSize>0) {
			cmsPictureElementDO.setStartPage(startPage);
			cmsPictureElementDO.setPageSize(pageSize);
			return this.queryPageListByPictureElement(cmsPictureElementDO);
		}
		return new PageInfo<CmsPictureElementDTO>();
	}

	@Override
	public int deleteByIds(List<Long> ids) throws Exception {
		int count = 0;
		try {
			count = pictureElementDao.deleteByIds(ids);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("批量删除报错", e);
		}
		return count;
	}

	@Override
	public BaseDao<PictureElement> getDao() {
		return pictureElementDao;
	}
	
}
