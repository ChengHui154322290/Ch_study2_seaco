package com.tp.service.mmp;

import java.util.List;

import com.tp.model.mmp.FreightTemplate;
import com.tp.service.IBaseService;
 
public interface IFreightTemplateService  extends IBaseService<FreightTemplate>{
	List<FreightTemplate> selectAll();
	
	List<FreightTemplate> selectByCalculateMode(Integer type) ;	
	/**
	 * 商品获得的运费模板 + 全场运费满包邮模板
	 * 
	 */
	List<FreightTemplate> queryItemFreightTemplate(Long  templateId);
	
	/**
	 * 根据运费模板ID列表查询信息
	 * @param templateIdList
	 * @return
	 */
	List<FreightTemplate> queryItemFreightTemplateByTemplateIdList(List<Long> templateIdList);
	
	/**
	 * 获取全场包邮运费模板
	 * @return
	 */
	FreightTemplate queryGlobalTemplate();
}
