package com.tp.proxy.bse;

import java.util.Date;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.math.RandomUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.vo.BseConstant.DISTRICT_LEVEL;
import com.tp.dto.common.FailInfo;
import com.tp.dto.common.ResultInfo;
import com.tp.model.bse.DistrictInfo;
import com.tp.model.bse.NationalIcon;
import com.tp.proxy.BaseProxy;
import com.tp.service.IBaseService;
import com.tp.service.bse.IDistrictInfoService;
import com.tp.service.bse.INationalIconService;
/**
 * 国家图标表代理层
 * @author szy
 *
 */
@Service
public class NationalIconProxy extends BaseProxy<NationalIcon>{

	@Autowired
	private INationalIconService nationalIconService;
	@Autowired
	private IDistrictInfoService districtInfoService;
	
	@Autowired
	private ForbiddenWordsProxy forbiddenWordsProxy;

	@Override
	public IBaseService<NationalIcon> getService() {
		return nationalIconService;
	}
	
	/**
	 * 
	 * <pre>
	 * 增加国家图标
	 * </pre>
	 *
	 * @param NationalIcon
	 */
	public ResultInfo<NationalIcon> addNationalIcon(NationalIcon nationalIcon) throws Exception {
		Integer countryId = nationalIcon.getCountryId();
		if (null==countryId) {
			return new ResultInfo<>(new FailInfo("必须选择一个地区"));
		}
		NationalIcon icon =new NationalIcon();
		icon.setCountryId(countryId);
		List<NationalIcon> result = nationalIconService.queryByObject(icon);
		if(CollectionUtils.isNotEmpty(result)){
			return new ResultInfo<>(new FailInfo("您要新增产地信息已存在"));
		}
	    if(StringUtils.isBlank(nationalIcon.getPicPath())){
	    	return new ResultInfo<>(new FailInfo("必须上传国旗或区旗图片"));
	    }
	    forbiddenWordsProxy.checkForbiddenWordsField(nationalIcon.getRemark(), "备注");
		DistrictInfo serarchDistrict = districtInfoService.queryById(countryId.longValue());
		Integer inseartContinentId=null;
		Integer type = serarchDistrict.getType();
		if(type.intValue()==DISTRICT_LEVEL.CONTRY.getCode()){
			inseartContinentId=serarchDistrict.getParentId().intValue();
		}else{
			DistrictInfo fatherDistrict = districtInfoService.queryById(serarchDistrict.getParentId());
			DistrictInfo grandFaterDistrict = districtInfoService.queryById(fatherDistrict.getParentId());
			inseartContinentId=grandFaterDistrict.getParentId().intValue();
		}
	    NationalIcon insertNationalIcon =new NationalIcon();
	    insertNationalIcon.setCountryId(countryId);
	    insertNationalIcon.setNameEn(nationalIcon.getNameEn().trim());
	    insertNationalIcon.setStatus(nationalIcon.getStatus());
	    insertNationalIcon.setSortNo(RandomUtils.nextInt());
		insertNationalIcon.setPicPath(nationalIcon.getPicPath());
		insertNationalIcon.setCreateTime(new Date());
		insertNationalIcon.setModifyTime(new Date());
		insertNationalIcon.setContinentId(inseartContinentId);
		insertNationalIcon.setRemark(nationalIcon.getRemark().trim());
		nationalIconService.insert(insertNationalIcon);
		return new ResultInfo<>(insertNationalIcon);
	}
	
	/**
	 * 
	 * <pre>
	 * 更新品牌
	 * </pre>
	 *
	 * @param NationalIcon
	 * @param isAllField
	 */
	public ResultInfo<NationalIcon>  updateNationalIcon(NationalIcon nationalIcon, Boolean isAllField)throws Exception {
		Integer countryId = nationalIcon.getCountryId();
		if (null==countryId  ) {
			return new ResultInfo<>(new FailInfo("必须选择一个地区"));
		}
	    NationalIcon iconDO =new NationalIcon();
		iconDO.setCountryId(countryId);
		List<NationalIcon> result = nationalIconService.queryByObject(iconDO);
		
		if(CollectionUtils.isNotEmpty(result)){
			for(NationalIcon naco:result){
		        Long id = naco.getId();
		        if(!id.equals(nationalIcon.getId())){
		        	return new ResultInfo<>(new FailInfo("您要新增产地信息已存在"));
		        }
		    } 
		}
	    if(StringUtils.isBlank(nationalIcon.getPicPath())){
	    	return new ResultInfo<>(new FailInfo("必须上传国旗或者区旗"));
	    }
		forbiddenWordsProxy.checkForbiddenWordsField(nationalIcon.getRemark(), "备注");
		DistrictInfo serarchDistrict = districtInfoService.queryById(countryId.longValue());
		Integer inseartContinentId=null;
		Integer type = serarchDistrict.getType();
		if(type.intValue()==DISTRICT_LEVEL.CONTRY.getCode()){
			inseartContinentId=serarchDistrict.getParentId().intValue();
		}else{
			DistrictInfo fatherDistrict = districtInfoService.queryById(serarchDistrict.getParentId());
			DistrictInfo grandFaterDistrict = districtInfoService.queryById(fatherDistrict.getParentId());
			inseartContinentId=grandFaterDistrict.getParentId().intValue();
		}
	    NationalIcon insertNationalIcon =new NationalIcon();
	    insertNationalIcon.setId(nationalIcon.getId());
	    insertNationalIcon.setCountryId(countryId);
	    insertNationalIcon.setStatus(nationalIcon.getStatus());
	    insertNationalIcon.setNameEn(nationalIcon.getNameEn().trim());
	    insertNationalIcon.setSortNo(nationalIcon.getSortNo());
		insertNationalIcon.setPicPath(nationalIcon.getPicPath());
		insertNationalIcon.setContinentId(inseartContinentId);
		insertNationalIcon.setModifyTime(new Date());
		insertNationalIcon.setRemark(nationalIcon.getRemark().trim());
		if(isAllField){
			nationalIconService.updateById(insertNationalIcon);
		}else{
			nationalIconService.updateNotNullById(insertNationalIcon);
		}
		return new ResultInfo<>(insertNationalIcon);
	}
}
