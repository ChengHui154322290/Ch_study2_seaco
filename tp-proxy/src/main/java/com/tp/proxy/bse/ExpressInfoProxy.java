package com.tp.proxy.bse;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.dto.common.FailInfo;
import com.tp.dto.common.ResultInfo;
import com.tp.model.bse.ExpressInfo;
import com.tp.proxy.BaseProxy;
import com.tp.result.bse.ExpressCompanyDTO;
import com.tp.service.IBaseService;
import com.tp.service.bse.IExpressInfoService;
/**
 * 快递公司信息表代理层
 * @author szy
 *
 */
@Service
public class ExpressInfoProxy extends BaseProxy<ExpressInfo>{

	@Autowired
	private IExpressInfoService expressInfoService;

	@Override
	public IBaseService<ExpressInfo> getService() {
		return expressInfoService;
	}
	
	/**
	 * 数据有效性验证
	 * @param expressInfoDO
	 * @param operationType:操作类型-增加，更新
	 * @return
	 */
	public ResultInfo<Boolean> validateData(ExpressInfo expressInfoDO,Integer operationType){
		String name = expressInfoDO.getName();
		String code = expressInfoDO.getCode();
		Integer sortNo = expressInfoDO.getSortNo();
		if(StringUtils.isEmpty(name)){
			return new ResultInfo<Boolean>(new FailInfo("名称不能为空！"));
		}
		if(StringUtils.isEmpty(code)){
			return new ResultInfo<Boolean>(new FailInfo("编号不能为空！"));
		}
		if(null == sortNo){
			return new ResultInfo<Boolean>(new FailInfo("排序值不能为空！"));
		}
		if(operationType == 1){//是否增加操作
			ExpressInfo expressInfo = expressInfoService.selectByCode(code);
			if(null != expressInfo){
				return new ResultInfo<Boolean>(new FailInfo("code不能重复！"));
			}
			List<ExpressCompanyDTO> expressInfo1 = expressInfoService.selectByNameOrCode(name, null);
			if(CollectionUtils.isNotEmpty(expressInfo1)){
				return new ResultInfo<Boolean>(new FailInfo("名称不能重复"));
			}
		}else{
			ExpressInfo ex = expressInfoService.queryById(expressInfoDO.getId());
			if(name.equals(ex.getName()) && code.equals(ex.getCode()) && sortNo.intValue() == ex.getSortNo().intValue() ){
				return new ResultInfo<Boolean>(new FailInfo("没有数据需要更新！"));
			}
			
			if(!name.equals(ex.getName())){
				List<ExpressCompanyDTO> expressInfo1 = expressInfoService.selectByNameOrCode(name, null);
				if(CollectionUtils.isNotEmpty(expressInfo1)){
					return new ResultInfo<Boolean>(new FailInfo("名称不能重复"));
				}
			}
			if(!code.equals(ex.getCode())){
				ExpressInfo expressInfo = expressInfoService.selectByCode(code);
				if(null != expressInfo){
					return new ResultInfo<Boolean>(new FailInfo("code不能重复！"));
				}
			}
			
		}
		return new ResultInfo<Boolean>(Boolean.TRUE);
	}
}
