package com.tp.proxy.cms;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.vo.PageInfo;
import com.tp.dto.cms.CmsPictureElementDTO;
import com.tp.model.cms.PictureElement;
import com.tp.proxy.BaseProxy;
import com.tp.service.IBaseService;
import com.tp.service.cms.IPictureElementService;
/**
 * 图片元素表代理层
 * @author szy
 *
 */
@Service
public class PictureElementProxy extends BaseProxy<PictureElement>{

	@Autowired
	private IPictureElementService pictureElementService;

	@Override
	public IBaseService<PictureElement> getService() {
		return pictureElementService;
	}
	
	/**
	 * 单击位置编辑，跳转到图片元素列表中
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public List<CmsPictureElementDTO> getPictureElement(Long id) throws Exception {
		PictureElement cmsPictureElementDO = new PictureElement();
		cmsPictureElementDO.setPositionId(id);
		PageInfo<CmsPictureElementDTO> pageList = pictureElementService.queryPageListByPictureElementAndStartPageSize(cmsPictureElementDO, 1, 99999);
		
		List<CmsPictureElementDTO> list = pageList.getRows();
		
		return list;
	}
	
	public Long addPicEmelent(PictureElement cmsPictureElement) throws Exception {
		cmsPictureElement = pictureElementService.insert(cmsPictureElement);
		return cmsPictureElement.getId();
		
	}
	
	public Integer updatePicEmelent(PictureElement cmsPictureElementDO) throws Exception {
		return pictureElementService.updateById(cmsPictureElementDO);
	}
	
	public Integer delById(Long id) throws Exception {
		return pictureElementService.deleteById(id);
	}
}
