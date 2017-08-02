package com.tp.proxy.bse;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.vo.BseConstant;
import com.tp.common.vo.Constant;
import com.tp.common.vo.BseConstant.TaxRateEnum;
import com.tp.dto.common.FailInfo;
import com.tp.dto.common.ResultInfo;
import com.tp.model.bse.TaxRate;
import com.tp.proxy.BaseProxy;
import com.tp.service.IBaseService;
import com.tp.service.bse.ITaxRateService;
import com.tp.util.BeanUtil;
/**
 * 税率表代理层
 * @author szy
 *
 */
@Service
public class TaxRateProxy extends BaseProxy<TaxRate>{

	@Autowired
	private ITaxRateService taxRateService;
	
	@Autowired
	private ForbiddenWordsProxy forbiddenWordsProxy;

	@Override
	public IBaseService<TaxRate> getService() {
		return taxRateService;
	}
	
	/**
	 * 
	 * <pre>
	 * 增加税率
	 * </pre>
	 *
	 * @param taxRate
	 */
	public ResultInfo<TaxRate> addTaxRate(TaxRate taxRate) throws Exception {
		String type = taxRate.getType();
		if(StringUtils.isBlank(type)){
			return new ResultInfo<>(new FailInfo("请选择一个分类"));
		}
		Double rate = taxRate.getRate();
		if(null==rate || rate<0 || rate>100){
			return new ResultInfo<>(new FailInfo("请输入一个正确的1-100之间的税率"));
		}
		if(StringUtils.isNotBlank(taxRate.getCode())){
			Map<String,Object> params = new HashMap<String,Object>();
			params.put("code", taxRate.getCode().trim());
			List<TaxRate> list = taxRateService.queryByParam(params);
			if(CollectionUtils.isNotEmpty(list)){
				return new ResultInfo<>(new FailInfo("存在相同的税号,请更换一个"));
			}
		}
		if(taxRate.getType().equals(TaxRateEnum.TARRIFRATE.getType())){
			if(null==taxRate.getDutiableValue()){
				return new ResultInfo<>(new FailInfo("完税价格必填"));
			}
			if(StringUtils.isBlank(taxRate.getCode())){
				return new ResultInfo<>(new FailInfo("税号必填"));
			}
		}
		forbiddenWordsProxy.checkForbiddenWordsField(taxRate.getRemark(),"备注");
		TaxRate insertTaxRate=new TaxRate();
		insertTaxRate.setRemark(taxRate.getRemark().trim());
		insertTaxRate.setStatus(taxRate.getStatus());
		insertTaxRate.setRate(taxRate.getRate());
		insertTaxRate.setDutiableValue(taxRate.getDutiableValue());
		if(StringUtils.isNotBlank(taxRate.getCode())){
			insertTaxRate.setCode(taxRate.getCode().trim());
		}
		insertTaxRate.setCreateTime(new Date());
		insertTaxRate.setModifyTime(new Date());
		insertTaxRate.setType(type.trim());
		taxRateService.insert(insertTaxRate);
		return new ResultInfo<>(insertTaxRate);
	}
	
	/**
	 * 
	 * <pre>
	 * 更新税率
	 * </pre>
	 *
	 * @param taxRate
	 * @param isAllField
	 */
	public ResultInfo<TaxRate> updateTaxRate(TaxRate taxRate, Boolean isAllField) throws Exception {
		String type = taxRate.getType();
		if(StringUtils.isBlank(type)){
			return new ResultInfo<>(new FailInfo("请选择一个分类"));
		}
		Double rate = taxRate.getRate();
		if(null==rate || rate<0 || rate>100){
			return new ResultInfo<>(new FailInfo("请输入一个正确的1-100之间的税率"));
		}
		if(StringUtils.isNotBlank(taxRate.getCode())){
			Map<String,Object> params = new HashMap<String,Object>();
			params.put("code", taxRate.getCode().trim());
			List<TaxRate> list = taxRateService.queryByParam(params);
			  for(TaxRate tax:list){
			        Long id = tax.getId();
			        if(!id.equals(taxRate.getId())){
			        	return new ResultInfo<>(new FailInfo("存在相同税号,请跟换一个"));
			        }
			    }  
		}
		if(taxRate.getType().equals(TaxRateEnum.TARRIFRATE.getType())){
			if(null==taxRate.getDutiableValue()){
				return new ResultInfo<>(new FailInfo("完税价格必填"));
			}
			if(StringUtils.isBlank(taxRate.getCode())){
				return new ResultInfo<>(new FailInfo("税号必填"));
			}
		}
		forbiddenWordsProxy.checkForbiddenWordsField(taxRate.getRemark(),"备注");
		TaxRate insertTaxRate=new TaxRate();
		insertTaxRate.setId(taxRate.getId());
		insertTaxRate.setRemark(taxRate.getRemark().trim());
		insertTaxRate.setStatus(taxRate.getStatus());
		insertTaxRate.setRate(taxRate.getRate());
		insertTaxRate.setDutiableValue(taxRate.getDutiableValue());
		if(StringUtils.isNotBlank(taxRate.getCode())){
			insertTaxRate.setCode(taxRate.getCode().trim());
		}
		insertTaxRate.setModifyTime(new Date());
		insertTaxRate.setType(type.trim());
		if(isAllField){
			taxRateService.updateById(insertTaxRate);
		}else{
			taxRateService.updateNotNullById(insertTaxRate);
		}
		return new ResultInfo<>(insertTaxRate);
	}
	
	public Map<String, String> initTaxRateType() {
		// 税率类型类型
		Map<String, String> taxRateTypes = new HashMap<String, String>();
		BseConstant.TaxRateEnum[] values = BseConstant.TaxRateEnum.values();
		for (TaxRateEnum sTax : values) {
			taxRateTypes.put(sTax.getType(), sTax.getName());
		}
		return taxRateTypes;
	}

	public List<TaxRate> getRatesByType(TaxRateEnum primerate) {
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("type", primerate.getType() );
    	params.put("status", Constant.ENABLED.YES);
		return taxRateService.queryByParam(params);
	}
}
