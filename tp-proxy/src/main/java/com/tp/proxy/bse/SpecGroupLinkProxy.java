package com.tp.proxy.bse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.model.bse.SpecGroupLink;
import com.tp.proxy.BaseProxy;
import com.tp.service.IBaseService;
import com.tp.service.bse.ISpecGroupLinkService;
/**
 * 规格与规格组关系表代理层
 * @author szy
 *
 */
@Service
public class SpecGroupLinkProxy extends BaseProxy<SpecGroupLink>{

	@Autowired
	private ISpecGroupLinkService specGroupLinkService;

	@Override
	public IBaseService<SpecGroupLink> getService() {
		return specGroupLinkService;
	}
}
