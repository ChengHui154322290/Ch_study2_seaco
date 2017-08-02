package com.tp.proxy.cms;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.vo.PageInfo;
import com.tp.model.cms.ActivityElement;
import com.tp.model.cms.WrittenElement;
import com.tp.proxy.BaseProxy;
import com.tp.service.IBaseService;
import com.tp.service.cms.IWrittenElementService;
/**
 * 活动元素表代理层
 * @author szy
 *
 */
@Service
public class WrittenElementProxy extends BaseProxy<WrittenElement>{

	@Autowired
	IWrittenElementService writtenElementService;
	
	@Override
	public IBaseService<WrittenElement> getService() {
		return writtenElementService;
	}

	/**
	 * 单击位置编辑，跳转到文字元素列表中
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public List<WrittenElement> getWrittenElement(Long id) throws Exception {
		WrittenElement cmsWrittenElementDO = new WrittenElement();
		cmsWrittenElementDO.setPositionId(id);
		PageInfo<WrittenElement> pageList = writtenElementService.queryPageListByWrittenElementAndStartPageSize(cmsWrittenElementDO, 1, 99999);
		
		List<WrittenElement> list = pageList.getRows();
		
		return list;
	}
	
	public Long addSubmit(WrittenElement writtenElement){
		writtenElement = writtenElementService.insert(writtenElement);
		return writtenElement.getId();
	}
	
	public Integer updateSubmit(WrittenElement writtenElement){
		return writtenElementService.updateById(writtenElement);
	}
}
