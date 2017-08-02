package com.tp.proxy.bse;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.dto.common.FailInfo;
import com.tp.dto.common.ResultInfo;
import com.tp.exception.OrderServiceException;
import com.tp.model.bse.ExpressCodeInfo;
import com.tp.model.bse.ExpressInfo;
import com.tp.proxy.BaseProxy;
import com.tp.service.IBaseService;
import com.tp.service.bse.IExpressCodeInfoService;
/**
 * 快递100物流公司编号与仓库系统的物流编号对照表代理层
 * @author szy
 *
 */
@Service
public class ExpressCodeInfoProxy extends BaseProxy<ExpressCodeInfo>{

	@Autowired
	private IExpressCodeInfoService expressCodeInfoService;
	
	@Override
	public IBaseService<ExpressCodeInfo> getService() {
		return expressCodeInfoService;
	}
	
	/**
	 * 
	 * <pre>
	 *  获取物流公司列表 by zhs 0224
	 * </pre>
	 * @return
	 */
	public ResultInfo< List<ExpressInfo>> selectAllExpressCode(){
		try{
			return new ResultInfo< List<ExpressInfo>>(expressCodeInfoService.selectAllExpressCode() );			
		}catch(OrderServiceException ord_ex){
			return new ResultInfo<>(new FailInfo(ord_ex.getMessage()));						
		}catch(Throwable exception){
			return new ResultInfo<>(new FailInfo("获取物流公司列表 失败"));
		}		
	}	
}
