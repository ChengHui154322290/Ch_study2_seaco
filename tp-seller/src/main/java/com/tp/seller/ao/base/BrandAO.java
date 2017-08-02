package com.tp.seller.ao.base;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.model.bse.Brand;
import com.tp.service.bse.IBrandService;

/**
 * 
 * <pre>
 * 
 * </pre>
 *
 * @author liuchunhua
 * @version $Id: BrandServiceAO.java, v 0.1 2014年12月17日 上午11:24:02 Administrator
 *          Exp $
 */
@Service
public class BrandAO {
	@Autowired
	private IBrandService brandService;
	
	public List<Brand> queryByObject(Brand brandDO) throws Exception {
		return brandService.queryByObject(brandDO);
	}

}
