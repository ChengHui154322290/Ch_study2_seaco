package com.tp.proxy.ord;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.util.ExceptionUtils;
import com.tp.common.vo.Constant.DOCUMENT_TYPE;
import com.tp.dto.common.FailInfo;
import com.tp.dto.common.ResultInfo;
import com.tp.dto.ord.customs.DirectmailOrderClearaceExcelDTO;
import com.tp.model.ord.PersonalgoodsDeclareInfo;
import com.tp.proxy.BaseProxy;
import com.tp.service.IBaseService;
import com.tp.service.ord.IPersonalgoodsDeclareInfoService;
import com.tp.util.StringUtil;
/**
 * 个人物品申报数据表代理层
 * @author szy
 *
 */
@Service
public class PersonalgoodsDeclareInfoProxy extends BaseProxy<PersonalgoodsDeclareInfo>{

	private static final Logger logger = LoggerFactory.getLogger(PersonalgoodsDeclareInfoProxy.class);
	
	@Autowired
	private IPersonalgoodsDeclareInfoService personalgoodsDeclareInfoService;

	@Override
	public IBaseService<PersonalgoodsDeclareInfo> getService() {
		return personalgoodsDeclareInfoService;
	}
	
	//手动更新
	public ResultInfo<Boolean> updateDirectmailClearanceByPersonalgoosInfo(PersonalgoodsDeclareInfo info, List<String> orderCodes){
		if (CollectionUtils.isEmpty(orderCodes)){
			return new ResultInfo<>(new FailInfo("待更新清单为空"));
		}	
		FailInfo failInfo = validateDirectmailClearanceData(info);
		if (failInfo != null){
			return new ResultInfo<>(failInfo);
		}
		List<PersonalgoodsDeclareInfo> infos = new ArrayList<>();
		for (String orderCode : orderCodes){
			PersonalgoodsDeclareInfo pgInfo = new PersonalgoodsDeclareInfo();
			pgInfo.setOrderCode(Long.valueOf(orderCode));
			pgInfo.setBillNo(info.getBillNo());
			pgInfo.setVoyageNo(info.getVoyageNo());
			pgInfo.setTrafNo(StringUtil.isEmpty(info.getTrafNo()) ? info.getBillNo() : info.getTrafNo());
			infos.add(pgInfo);
		}
		try{			
			return personalgoodsDeclareInfoService.updateDirectmailPersonalgoodsDeclareInfos(infos);
		}catch(Exception e){
			ExceptionUtils.print(new FailInfo(e), logger, orderCodes);
			return new ResultInfo<>(new FailInfo("更新异常"));
		}
	}
	
	/**
	 * 导表更新 
	 */
	public ResultInfo<Boolean> updateDirectmailClearanceByExcel(List<DirectmailOrderClearaceExcelDTO> dmOrders){
		FailInfo failInfo = validateDirectmailClearanceExcelData(dmOrders);
		if (failInfo != null){
			return new ResultInfo<>(failInfo);
		}
		List<PersonalgoodsDeclareInfo> infos = new ArrayList<>();
		for (DirectmailOrderClearaceExcelDTO dto : dmOrders){
			PersonalgoodsDeclareInfo info = new PersonalgoodsDeclareInfo();
			info.setOrderCode(Long.valueOf(dto.getOrderCode()));
			info.setBillNo(dto.getBillNo());
			info.setVoyageNo(dto.getVoyageNo());
			info.setTrafNo(StringUtil.isEmpty(dto.getTrafNo()) ? dto.getVoyageNo() : dto.getTrafNo());
			infos.add(info);
		}
		try{
			return personalgoodsDeclareInfoService.updateDirectmailPersonalgoodsDeclareInfos(infos);
		}catch(Exception e){
			ExceptionUtils.print(new FailInfo(e), logger, dmOrders);
			return new ResultInfo<>(new FailInfo("更新异常"));
		}
	}
	
	private FailInfo validateDirectmailClearanceData(PersonalgoodsDeclareInfo info){
		if (StringUtil.isEmpty(info.getBillNo())){
			return new FailInfo("提运单号不能为空");
		}
		if(StringUtil.isEmpty(info.getVoyageNo())){
			return new FailInfo("航班号不能为空");
		}
		return null;
	}
	
	private FailInfo validateDirectmailClearanceExcelData(List<DirectmailOrderClearaceExcelDTO> dmOrders){
		if (CollectionUtils.isEmpty(dmOrders)){
			return new FailInfo("导入数据为空");
		}
		for(DirectmailOrderClearaceExcelDTO dto : dmOrders){
			if (StringUtil.isEmpty(dto.getOrderCode())){
				return new FailInfo("订单号不能为空");
			}
			if (!StringUtil.startsWith(dto.getOrderCode(), DOCUMENT_TYPE.SO_SUB_ORDER.code.toString())){
				return new FailInfo("订单号错误");
			}
			if (StringUtil.isEmpty(dto.getBillNo())){
				return new FailInfo("总提运单号不能为空");
			}
			if(StringUtil.isEmpty(dto.getVoyageNo())){
				return new FailInfo("航班号不能为空");
			}
		}
		return null;
	}
}
