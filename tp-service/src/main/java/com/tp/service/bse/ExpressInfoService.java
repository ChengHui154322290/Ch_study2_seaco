package com.tp.service.bse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.dao.BaseDao;
import com.tp.common.vo.Constant;
import com.tp.common.vo.OrderConstant;
import com.tp.dao.bse.ExpressInfoDao;
import com.tp.dto.ord.remote.ExpressInfoDTO;
import com.tp.exception.ServiceException;
import com.tp.model.bse.ExpressInfo;
import com.tp.model.ord.Kuaidi100Express;
import com.tp.model.ord.OrderDelivery;
import com.tp.model.ord.RejectInfo;
import com.tp.result.bse.ExpressCompanyDTO;
import com.tp.result.ord.ExpressLogInfoDTO;
import com.tp.service.BaseService;
import com.tp.service.bse.IExpressInfoService;
import com.tp.service.ord.IKuaidi100ExpressService;
import com.tp.service.ord.IOrderDeliveryService;
import com.tp.service.ord.IRejectInfoService;

@Service
public class ExpressInfoService extends BaseService<ExpressInfo> implements IExpressInfoService {

	@Autowired
	private ExpressInfoDao expressInfoDao;
	@Autowired
	private IOrderDeliveryService orderDeliveryService;
	@Autowired
	private IKuaidi100ExpressService kuaidi100ExpressService;
	@Autowired
	private IRejectInfoService rejectInfoService;
	
	@Override
	public BaseDao<ExpressInfo> getDao() {
		return expressInfoDao;
	}

	@Override
	public ExpressInfo selectByCode(String code) {
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("code", code);
		return super.queryUniqueByParams(params);
	}

	@Override
	public List<ExpressCompanyDTO> selectByNameOrCode(String name, String code) {
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("code", code);
		params.put("name", name);
		List<ExpressInfo> expressInfos = expressInfoDao.queryByParamNotEmpty(params);
		if(CollectionUtils.isNotEmpty(expressInfos)){
			List<ExpressCompanyDTO> list = new ArrayList<ExpressCompanyDTO>();
			for(ExpressInfo expressInfo : expressInfos){
				ExpressCompanyDTO dto = new ExpressCompanyDTO();
				dto.setName(expressInfo.getName());
				dto.setCode(expressInfo.getCode());
				list.add(dto);
			}
			return list;
		}
		return null;
	}

	
	/**
	 * 获得所有快递公司信息
	 * @return
	 */
	@Override
	public List<ExpressInfo> selectAllExpress(){
		ExpressInfo expressInfoDO = new ExpressInfo();
		List<ExpressInfo> expressInfoDOs = null;
		try {
//			expressInfoDOs = expressInfoDao.selectDynamic(expressInfoDO,DatasourceKey.slave_base_dataSource);
			expressInfoDOs = expressInfoDao.queryByObject(expressInfoDO);
		} catch (Exception e) {
			logger.error("获得快递公司信息异常：{}",e.getMessage());
		}
		return expressInfoDOs;
	}

	@Override
    public List<ExpressInfoDTO> queryExpressLogInfo(final Long code, final String packageNo) {
        if (code == null) {
            throw new ServiceException("子订单编号或退货单号不可为空");
        }
        List<Kuaidi100Express> expressLogInfoList = null;
        Map<String, OrderDelivery> orderDeliveryMap = new HashMap<String, OrderDelivery>();
        Map<String, RejectInfo> rejectInfoMap = new HashMap<String, RejectInfo>();
        List<ExpressInfoDTO> list = new ArrayList<ExpressInfoDTO>();
        if (code.toString().startsWith(Constant.DOCUMENT_TYPE.SO_SUB_ORDER.code.toString())) {// 子订单
        	OrderDelivery query = new OrderDelivery();
        	query.setOrderCode(code);
        	if(StringUtils.isNotEmpty(packageNo)) {
        		query.setPackageNo(packageNo);
        	}
            List<OrderDelivery> orderDeliveryDOList = orderDeliveryService.queryByObject(query);
            if (CollectionUtils.isNotEmpty(orderDeliveryDOList)) {
                List<Kuaidi100Express> kuaidi100ExpressDOList = new ArrayList<Kuaidi100Express>();
                Kuaidi100Express kuaidi100ExpressDO = null;
                for (OrderDelivery orderDeliveryDO : orderDeliveryDOList) {
                    orderDeliveryMap.put(orderDeliveryDO.getPackageNo(), orderDeliveryDO);// 构建订单物流信息map
                    kuaidi100ExpressDO = new Kuaidi100Express();
                    kuaidi100ExpressDO.setOrderCode(orderDeliveryDO.getOrderCode());
                    kuaidi100ExpressDO.setPackageNo(orderDeliveryDO.getPackageNo());
                    kuaidi100ExpressDOList.add(kuaidi100ExpressDO);// 组织物流日志记录查询参数列表O
                }
                expressLogInfoList = kuaidi100ExpressService.selectListBySubOrderCodeAndPackageNo(kuaidi100ExpressDOList);
            }
        } else if (code.toString().startsWith(Constant.DOCUMENT_TYPE.RETURNED.code.toString())) {// 退货
            List<RejectInfo> rejectInfoDOList = rejectInfoService.selectListByRejectNoAndPackageNo(code, packageNo);
            if (CollectionUtils.isNotEmpty(rejectInfoDOList)) {
                List<Kuaidi100Express> kuaidi100ExpressDOList = new ArrayList<Kuaidi100Express>();
                Kuaidi100Express kuaidi100ExpressDO = null;
                for (RejectInfo rejectInfoDO : rejectInfoDOList) {
                    rejectInfoMap.put(rejectInfoDO.getExpressNo(), rejectInfoDO);// 构建订单物流信息map
                    kuaidi100ExpressDO = new Kuaidi100Express();
                    kuaidi100ExpressDO.setRejectCode(rejectInfoDO.getRejectCode());// 退货单号
                    kuaidi100ExpressDO.setPackageNo(rejectInfoDO.getExpressNo());
                    kuaidi100ExpressDOList.add(kuaidi100ExpressDO);// 组织物流日志记录查询参数列表O
                }
                expressLogInfoList = kuaidi100ExpressService.selectListByRejectNoAndPackageNo(kuaidi100ExpressDOList);
            }
        } 
        if (CollectionUtils.isNotEmpty(expressLogInfoList)) {
            // 组织具体的日志信息
            Map<String, ArrayList<ExpressLogInfoDTO>> expressLogInfoMap = new HashMap<String, ArrayList<ExpressLogInfoDTO>>();
            for (Kuaidi100Express tmpKuaidi100ExpressDO : expressLogInfoList) {
                ArrayList<ExpressLogInfoDTO> list2 = expressLogInfoMap.get(tmpKuaidi100ExpressDO.getPackageNo());
                if (CollectionUtils.isEmpty(list2)) {
                    list2 = new ArrayList<ExpressLogInfoDTO>();
                }
                ExpressLogInfoDTO eliDTO = new ExpressLogInfoDTO();
                String dataTime = StringUtils.isNotBlank(tmpKuaidi100ExpressDO.getDataFtime()) ? tmpKuaidi100ExpressDO.getDataFtime() : tmpKuaidi100ExpressDO
                    .getDataTime();
                eliDTO.setDataTime(dataTime);
                eliDTO.setContext(tmpKuaidi100ExpressDO.getDataContext());
                list2.add(eliDTO);
                expressLogInfoMap.put(tmpKuaidi100ExpressDO.getPackageNo(), list2);
            }

            if (MapUtils.isNotEmpty(orderDeliveryMap)) {
                ExpressInfoDTO expressInfoDTO = null;
                for (Map.Entry<String, OrderDelivery> entry : orderDeliveryMap.entrySet()) {
                    expressInfoDTO = new ExpressInfoDTO();
                    OrderDelivery orderDeliveryDO = entry.getValue();
                    expressInfoDTO.setExpressLogInfoList(expressLogInfoMap.get(entry.getKey()));
                    expressInfoDTO.setCompanyId(orderDeliveryDO.getCompanyId());
                    expressInfoDTO.setCompanyName(orderDeliveryDO.getCompanyName());
                    expressInfoDTO.setPackageNo(entry.getKey());
                    expressInfoDTO.setSubOrderCode(orderDeliveryDO.getOrderCode());
                    list.add(expressInfoDTO);
                }
            } else if (MapUtils.isNotEmpty(rejectInfoMap)) {
                ExpressInfoDTO expressInfoDTO = null;
                for (Map.Entry<String, RejectInfo> entry : rejectInfoMap.entrySet()) {
                    expressInfoDTO = new ExpressInfoDTO();
                    RejectInfo rejectInfoDO = entry.getValue();
                    expressInfoDTO.setExpressLogInfoList(expressLogInfoMap.get(entry.getKey()));
                    expressInfoDTO.setCompanyId(rejectInfoDO.getCompanyCode());
                    expressInfoDTO.setCompanyName(rejectInfoDO.getCompanyName());
                    expressInfoDTO.setPackageNo(entry.getKey());
                    expressInfoDTO.setSubOrderCode(rejectInfoDO.getOrderCode());
                    expressInfoDTO.setRejectNo(rejectInfoDO.getRejectCode());
                    list.add(expressInfoDTO);
                }
            }

        }
        return list;
    }

}
