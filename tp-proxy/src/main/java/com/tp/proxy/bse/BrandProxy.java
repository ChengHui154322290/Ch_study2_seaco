package com.tp.proxy.bse;

import java.util.*;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.dto.common.FailInfo;
import com.tp.dto.common.ResultInfo;
import com.tp.model.bse.Brand;
import com.tp.proxy.BaseProxy;
import com.tp.result.bse.AutoCompleteResult;
import com.tp.service.IBaseService;
import com.tp.service.bse.IBrandService;
/**
 * 商品品牌代理层
 * @author szy
 *
 */
@Service
public class BrandProxy extends BaseProxy<Brand>{

	@Autowired
	private IBrandService brandService;
	@Autowired
	private ForbiddenWordsProxy forbiddenWordsProxy;
	@Override
	public IBaseService<Brand> getService() {
		return brandService;
	}
	
	
	/**
	 * 
	 * <pre>
	 * 增加品牌(设置sortNo和主键一致自增长)
	 * </pre>
	 *
	 * @param brand
	 */
	public ResultInfo<?> addBrand(Brand brand) throws Exception{
		Integer countryId = brand.getCountryId();
		if (null==countryId) {
			return new ResultInfo<>(new FailInfo("必须选择一个地区"));
		}
	    if(StringUtils.isBlank(brand.getLogo())){
	    	return new ResultInfo<>(new FailInfo("必须上传品牌logo图片"));
	    }
		String name = brand.getName();
		if(StringUtils.isBlank(name)){
			return new ResultInfo<>(new FailInfo("中文名称必填"));
		}
		forbiddenWordsProxy.checkForbiddenWordsField(name, "品牌名称");
		forbiddenWordsProxy.checkForbiddenWordsField(brand.getNameEn(), "品牌英文名称");
		forbiddenWordsProxy.checkForbiddenWordsField(brand.getRemark(), "备注");

		Map<String,Object> params = new HashMap<String,Object>();
		params.put("name", name.trim());
	    List<Brand> list = brandService.queryByParam(params);
	    if(CollectionUtils.isNotEmpty(list)){
	    	return new ResultInfo<>(new FailInfo("存在相同的中文名称"));
	    }  	
	    //设置插入对象的属性值
	    Brand insertBrand =new Brand();
	    if(null !=brand.getLogo()){
	    	 insertBrand.setLogo(brand.getLogo());
	    }
		insertBrand.setCountryId(countryId);
		insertBrand.setName(name.trim());
		insertBrand.setNameEn(brand.getNameEn().trim());
		insertBrand.setStatus(brand.getStatus());
		insertBrand.setCreateTime(new Date());
		insertBrand.setModifyTime(new Date());
		insertBrand.setRemark(brand.getRemark().trim());
		brandService.insert(insertBrand);
		return new ResultInfo<>(insertBrand);
	}

	public List<AutoCompleteResult> selectAtuoCompleteBrand(String brandName){
		Brand search = new Brand();
		search.setName(brandName);
		List<Brand> searchList = brandService.selectBrandListByLikeBrandDo(search);
		List<AutoCompleteResult> returnList = new ArrayList<>();
		if(CollectionUtils.isNotEmpty(searchList)){
			for(Brand bdo:searchList){
				AutoCompleteResult add = new AutoCompleteResult();
				add.setId(bdo.getId().intValue());
				add.setLabel(bdo.getName());
				add.setValue(bdo.getName());
				returnList.add(add);
			}
		}
		return returnList;
	}
}
