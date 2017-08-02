package com.tp.service.ord;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tp.common.dao.BaseDao;
import com.tp.common.vo.Constant;
import com.tp.common.vo.OrderConstant;
import com.tp.common.vo.DAOConstant.MYBATIS_SPECIAL_STRING;
import com.tp.common.vo.ord.OrderStatusLogConstant;
import com.tp.dao.ord.OrderDeliveryDao;
import com.tp.dto.mmp.ReturnData;
import com.tp.dto.ord.remote.ExpressModifyDTO;
import com.tp.exception.ErrorCodes.ExpressError;
import com.tp.model.ord.OrderDelivery;
import com.tp.model.ord.OrderStatusLog;
import com.tp.service.BaseService;
import com.tp.service.ord.IKuaidi100ExpressService;
import com.tp.service.ord.IOrderDeliveryService;
import com.tp.service.ord.IOrderStatusLogService;
import com.tp.util.StringUtil;

@Service
public class OrderDeliveryService extends BaseService<OrderDelivery> implements IOrderDeliveryService {

	@Autowired
	private OrderDeliveryDao orderDeliveryDao;
	@Autowired
	IKuaidi100ExpressService kuaidi100ExpressService;
	@Autowired
	private IOrderStatusLogService orderStatusLogService;
	
	@Override
	public BaseDao<OrderDelivery> getDao() {
		return orderDeliveryDao;
	}
	@Override
	public OrderDelivery selectOneBySubOrderCode(Long subCode) {
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("orderCode", subCode);
		return super.queryUniqueByParams(params);
	}

	@Override
	public List<OrderDelivery> selectListBySubCodeList(List<Long> subCodeList) {
		if (CollectionUtils.isNotEmpty(subCodeList)) {
			Map<String,Object> params = new HashMap<String,Object>();
			params.put(MYBATIS_SPECIAL_STRING.COLUMNS.name(),"order_code in ("+StringUtil.join(subCodeList, Constant.SPLIT_SIGN.COMMA)+")");
			return orderDeliveryDao.queryByParam(params);
		} else {
			return new ArrayList<OrderDelivery>(0);
		}
	}

	@Override
	public Integer batchInsert(List<OrderDelivery> orderDeliveryDOList) {
		return orderDeliveryDao.batchInsert(orderDeliveryDOList);
	}

	@Override
	public List<OrderDelivery> queryNotSuccessPostKuaidi100List(OrderDelivery orderDelivery) {
		return orderDeliveryDao.selectNotSuccessPostKuaidi100List(orderDelivery);
	}

	@Override
	public Integer batchUpdatePostKuaidi100(List<OrderDelivery> orderDeliveryDOList) {
		return orderDeliveryDao.batchUpdatePostKuaidi100(orderDeliveryDOList);
	}

	@Override
	public Integer updatePostKuaidi100(OrderDelivery orderDelivery) {
		return orderDeliveryDao.updatePostKuaidi100(orderDelivery);
	}

	@Override
	public List<OrderDelivery> selectListBySubCodeAndPackageNo(Long subOrderCode, String packageNo) {
		return orderDeliveryDao.selectListBySubCodeAndPackageNo(subOrderCode, packageNo);
	}
	@Override
    @Transactional
    public ReturnData modifyExpressNo(final ExpressModifyDTO expressModify) {
        ReturnData rData = verifyModifyParam(expressModify);
        if (rData.isSuccess()) {// 参数校验通过
            try {
                // 更新快递信息
                Integer i = updateExpressNoAndCompany(expressModify);
                if (i.intValue() > 0) {
                    // 删除原有的快递日志
                	kuaidi100ExpressService.deleteOldExpressInfo(expressModify.getOrderNo(), expressModify.getOriginalExpressNo());
                    OrderStatusLog orderStatusLogDO = new OrderStatusLog();
                    orderStatusLogDO.setName("变更运单信息");
                    orderStatusLogDO.setPreStatus(OrderConstant.ORDER_STATUS.RECEIPT.code);
                    orderStatusLogDO.setCurrStatus(OrderConstant.ORDER_STATUS.RECEIPT.code);
                    orderStatusLogDO.setOrderCode(expressModify.getOrderNo());
                    orderStatusLogDO.setParentOrderCode(expressModify.getOrderNo());
                    orderStatusLogDO.setType(OrderStatusLogConstant.LOG_TYPE.MONITOR.code);
                    orderStatusLogDO.setContent("把原运单号[" + expressModify.getOriginalExpressNo() + "]变更为" + expressModify.getCompanyName() + ":"
                        + expressModify.getNewExpressNo());
                    orderStatusLogDO.setCreateTime(new Date());
                    orderStatusLogDO.setCreateUserName(expressModify.getModifyAccount());
                    orderStatusLogDO.setCreateUserType(expressModify.getUserType());
                    orderStatusLogService.insert(orderStatusLogDO);
                } else {
                    rData = new ReturnData(Boolean.FALSE, ExpressError.ORIGINAL_EXPRESSNO_NOT_EXIT.code, expressModify.getOrderNo() + "-"
                        + expressModify.getOriginalExpressNo() + ":" + ExpressError.ORIGINAL_EXPRESSNO_NOT_EXIT.cnName);
                }

            } catch (Exception e) {
                logger.error("更新快递信息SQL执行异常", e);
                rData = new ReturnData(Boolean.FALSE, ExpressError.UPDATE_EXPRESSNO_EXCEPTION.code, expressModify.getOrderNo() + "-"
                    + expressModify.getOriginalExpressNo() + ":" + ExpressError.UPDATE_EXPRESSNO_EXCEPTION.cnName);
            }
        }
        return rData;
    }
	/**
	 * @param expressModify
	 * @return
	 */
	private Integer updateExpressNoAndCompany(ExpressModifyDTO expressModify) {
		OrderDelivery query = new OrderDelivery();
		query.setOrderCode(expressModify.getOrderNo());
		query.setPackageNo(expressModify.getOriginalExpressNo());
		List<OrderDelivery> list = orderDeliveryDao.queryByObject(query);
		int i=0;
		if(CollectionUtils.isNotEmpty(list)) {
			for(OrderDelivery orderDelivery : list) {
				orderDelivery.setCompanyId(expressModify.getCompanyNo());
				orderDelivery.setCompanyName(expressModify.getCompanyName());
				orderDelivery.setPackageNo(expressModify.getNewExpressNo());
				orderDelivery.setPostKuaidi100(0);
				orderDelivery.setPostKuaidi100Times(0);
				orderDelivery.setUpdateTime(new Date());
				i+=orderDeliveryDao.updateById(orderDelivery);
			}
		}
		return i;
	}

	/**
     * <pre>
     * 验证请求参数
     * </pre>
     * 
     * @param expressModifyDTO
     * @return
     */
    private ReturnData verifyModifyParam(final ExpressModifyDTO expressModifyDTO) {
        ReturnData rData = new ReturnData(Boolean.TRUE);
        StringBuffer errorMsg = new StringBuffer();
        if (expressModifyDTO.getOrderNo() == null) {
            errorMsg.append("订单编号不可为空，");
        }
        if (StringUtils.isBlank(expressModifyDTO.getCompanyNo())) {
            errorMsg.append("物流公司编号不可为空，");
        }
        if (StringUtils.isBlank(expressModifyDTO.getCompanyName())) {
            errorMsg.append("物流公司名称不可为空，");
        }
        if (StringUtils.isBlank(expressModifyDTO.getNewExpressNo()) || StringUtils.isBlank(expressModifyDTO.getOriginalExpressNo())) {
            errorMsg.append("运单编号不可为空，");
        }

        if (StringUtils.isNotBlank(errorMsg.toString())) {
            String msg = expressModifyDTO.getOrderNo() + "-" + expressModifyDTO.getOriginalExpressNo() + ":"
                + errorMsg.toString().substring(0, errorMsg.toString().length() - 2);
            rData = new ReturnData(Boolean.FALSE, msg);
        }
        return rData;
    }
	@Override
	public List<ReturnData> batchModifyExpressNo(List<ExpressModifyDTO> passExpressModifyList) {
		 List<ReturnData> rDataList = new ArrayList<ReturnData>();
	        if (CollectionUtils.isNotEmpty(passExpressModifyList)) {
	            for (ExpressModifyDTO expressModify : passExpressModifyList) {
	                ReturnData rData = this.modifyExpressNo(expressModify);
	                rDataList.add(rData);
	            }
	        }
	        return rDataList;
	}
}
