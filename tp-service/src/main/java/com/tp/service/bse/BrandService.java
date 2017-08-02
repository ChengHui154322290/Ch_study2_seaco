package com.tp.service.bse;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.tp.common.dao.BaseDao;
import com.tp.common.vo.Constant;
import com.tp.dao.bse.BrandDao;
import com.tp.model.bse.Brand;
import com.tp.service.BaseService;
import com.tp.service.bse.IBrandService;

@Service
public class BrandService extends BaseService<Brand> implements IBrandService {

	@Autowired
	private BrandDao brandDao;
	
	@Override
	public BaseDao<Brand> getDao() {
		return brandDao;
	}
	@Override
	public List<Brand> selectListBrand(List<Long> ids, Integer status) {
		if(CollectionUtils.isEmpty(ids)){
			return null;
		}
		List<Brand> brandDOs = new ArrayList<Brand>();
		List<Brand> listBrand=new ArrayList<Brand>();
		for (int i = 0; i < ids.size(); i++) {
			Brand brandDO = this.queryById(ids.get(i));
			if(brandDO!=null){
				listBrand.add(brandDO);
			}	
		}		
		for (Brand brandDO : listBrand) {
			Integer bool =brandDO.getStatus();
			if (Constant.ENABLED.YES==bool) {
				switch (status) {
				case 0:
					break;
				case 1:
					brandDOs.add(brandDO);
					break;
				case 2:
					brandDOs.add(brandDO);
					break;
				default:
					break;
				}
			} else {
				switch (status) {
				case 0:
					brandDOs.add(brandDO);
					break;
				case 1:
					break;
				case 2:
					brandDOs.add(brandDO);
					break;
				default:
					break;
				}
			}
		}
		return brandDOs;
	}

	@Override
	public List<Brand> selectBrandListByLikeBrandDo(Brand brand) {
		return brandDao.selectBrandListByLikeBrandDo(brand);
	}
	/* (non-Javadoc)
	 * @see com.tp.service.bse.IBrandService#selectIdByName(java.lang.String, java.lang.Long)
	 */
	@Override
	public Long selectIdByName(String brandName, Long userId) {
		if(StringUtils.isEmpty(brandName)){ return null;}
		
		Date date= new Date();
		Brand brandDO = new Brand();
		brandDO.setName(brandName);
		brandDO.setStatus(Constant.ENABLED.YES);
		brandDO.setCreateTime(date);
		brandDO.setModifyTime(date);
		brandDO.setCountryId(196);
		
		List<Brand> list = this.queryByObject(brandDO);
		if(CollectionUtils.isEmpty(list)){
			return insert(brandDO).getId();
		}
		return list.get(0).getId();
	}
}
