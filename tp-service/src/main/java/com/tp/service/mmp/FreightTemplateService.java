package com.tp.service.mmp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.dao.BaseDao;
import com.tp.common.vo.Constant;
import com.tp.common.vo.Constant.SPLIT_SIGN;
import com.tp.common.vo.Constant.TF;
import com.tp.common.vo.DAOConstant.MYBATIS_SPECIAL_STRING;
import com.tp.dao.mmp.FreightTemplateDao;
import com.tp.dto.mmp.enums.FreightCalculateType;
import com.tp.dto.mmp.enums.FreightType;
import com.tp.model.mmp.FreightTemplate;
import com.tp.service.BaseService;
import com.tp.service.mmp.IFreightTemplateService;
import com.tp.util.StringUtil;

@Service
public class FreightTemplateService extends BaseService<FreightTemplate> implements IFreightTemplateService {

    @Autowired
    private FreightTemplateDao freightTemplateDao;

    @Override
    public BaseDao<FreightTemplate> getDao() {
        return freightTemplateDao;
    }

    @Override
    public List<FreightTemplate> selectAll() {
        return null;
    }

    @Override
    public List<FreightTemplate> selectByCalculateMode(Integer type) {
        if (null == type) {
            type = FreightCalculateType.TYYZ.getValue();
        }
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("calculateMode", type);
        params.put("freightType", FreightType.INTERNAL.getValue());
        params.put("isDelete", Constant.DISABLED.NO);
        return freightTemplateDao.queryByParam(params);
    }

    @Override
    public List<FreightTemplate> queryItemFreightTemplate(Long templateId) {
        List<FreightTemplate> resList = new ArrayList<FreightTemplate>();
        FreightTemplate itemTemplate = queryById(templateId);
        if (itemTemplate != null)
            resList.add(itemTemplate);
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("calculateMode", FreightCalculateType.MBY.getValue());
        params.put("freightType", FreightType.INTERNAL.getValue());
        params.put("isDelete", Constant.DISABLED.NO);
        FreightTemplate itemTemplateOther = super.queryUniqueByParams(params);
        if(itemTemplateOther!=null){
        	resList.add(itemTemplateOther);
        }
        return resList;
    }

	@Override
	public List<FreightTemplate> queryItemFreightTemplateByTemplateIdList(List<Long> templateIdList) {
		Map<String,Object> params = new HashMap<String,Object>();
		params.put(MYBATIS_SPECIAL_STRING.COLUMNS.name(), "id in ("+StringUtil.join(templateIdList,SPLIT_SIGN.COMMA)+")");
		params.put("isDelete", Constant.DISABLED.NO);
		return super.queryByParam(params);
	}

	@Override
	public FreightTemplate queryGlobalTemplate() {
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("calculateMode", TF.NO);
		params.put("freightType", TF.NO);
		params.put("isDelete", TF.NO);
		return super.queryUniqueByParams(params);
	}
}
