package com.tp.proxy.cms;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.vo.PageInfo;
import com.tp.model.cms.SeoElement;
import com.tp.proxy.BaseProxy;
import com.tp.service.IBaseService;
import com.tp.service.cms.ISeoElementService;
/**
 * SEO元素表代理层
 * @author szy
 *
 */
@Service
public class SeoElementProxy extends BaseProxy<SeoElement>{

	@Autowired
	private ISeoElementService seoElementService;

	@Override
	public IBaseService<SeoElement> getService() {
		return seoElementService;
	}

	public List<SeoElement> getDefinedElement(Long positionId) {
		return seoElementService.getDefinedElement(positionId);
	}
	public int deleteByIds(List<Long> ids) {
		return seoElementService.deleteByIds(ids);
	}

	public PageInfo<SeoElement> queryPageListByCmsSeoElement(SeoElement cmsSeoElement) {
		return seoElementService.queryPageListByCmsSeoElement(cmsSeoElement);
	}

	public PageInfo<SeoElement> queryPageListByCmsSeoElementAndStartPageSize(SeoElement cmsSeoElement, int startPage, int pageSize) {
		return seoElementService.queryPageListByCmsSeoElementAndStartPageSize(cmsSeoElement, startPage, pageSize);
	}
}
