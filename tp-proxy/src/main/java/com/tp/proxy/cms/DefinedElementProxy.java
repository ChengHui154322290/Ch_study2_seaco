package com.tp.proxy.cms;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.vo.PageInfo;
import com.tp.model.cms.DefinedElement;
import com.tp.proxy.BaseProxy;
import com.tp.service.IBaseService;
import com.tp.service.cms.IDefinedElementService;
/**
 * 自定义元素表代理层
 * @author szy
 *
 */
@Service
public class DefinedElementProxy extends BaseProxy<DefinedElement>{

	@Autowired
	private IDefinedElementService definedElementService;

	@Override
	public IBaseService<DefinedElement> getService() {
		return definedElementService;
	}
	
	/**
	 * 单击位置编辑，跳转到文字元素列表中
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public List<DefinedElement> getDefinedElement(Long id) throws Exception {
		DefinedElement cmsDefinedElementDO = new DefinedElement();
		cmsDefinedElementDO.setPositionId(id);
		PageInfo<DefinedElement> pageList = definedElementService.queryPageListByDefinedElementAndStartPageSize(cmsDefinedElementDO, 1, 99999);
		
		List<DefinedElement> list = pageList.getRows();
		
		return list;
	}
	
	public Long addSubmit(DefinedElement cmsDefinedElementDO){
		cmsDefinedElementDO = definedElementService.insert(cmsDefinedElementDO);
		return cmsDefinedElementDO.getId();
	}
	
	public Integer updateSubmit(DefinedElement cmsDefinedElementDO){
		return definedElementService.updateById(cmsDefinedElementDO);
	}
	
	public Integer delById(Long id){
		return definedElementService.deleteById(id);
	}
	
	public DefinedElement selectById(Long id){
		return definedElementService.queryById(id);
	}
}
