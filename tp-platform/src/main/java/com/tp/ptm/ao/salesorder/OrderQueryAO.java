package com.tp.ptm.ao.salesorder;


import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.vo.PageInfo;
import com.tp.common.vo.ptm.ErrorCodes.AuthError;
import com.tp.dto.ord.remote.SubOrder4BackendDTO;
import com.tp.dto.ptm.salesorder.Order4PlatformDTO;
import com.tp.dto.ptm.salesorder.OrderLine4PlatformDTO;
import com.tp.dto.ptm.salesorder.OrderMiniDTO;
import com.tp.dto.ptm.salesorder.SubOrder4PlatformQO;
import com.tp.exception.PlatformServiceException;
import com.tp.model.ord.OrderItem;
import com.tp.model.pay.PaymentGateway;
import com.tp.model.ptm.PlatformSupplierRelation;
import com.tp.query.ord.SubOrderQO;
import com.tp.service.ord.remote.ISalesOrderRemoteService;
import com.tp.service.pay.IPaymentGatewayService;
import com.tp.service.ptm.IPlatformAccountService;
import com.tp.service.ptm.IPlatformSupplierRelationService;
import com.tp.service.ptm.IPlatformAccountService.KeyType;

/**
 * 订单查询AO
 * 
 * @author 项硕
 * @version 2015年5月13日 下午4:03:31
 */
@Service
public class OrderQueryAO {

	private static final Logger log = LoggerFactory.getLogger(OrderQueryAO.class);

	@Autowired
	private IPlatformAccountService platformAccountService;
	@Autowired
	private IPlatformSupplierRelationService platformSupplierRelationService;
	@Autowired
	private ISalesOrderRemoteService salesOrderRemoteService;
	@Autowired
	private IPaymentGatewayService paymentGatewayService;

	/**
	 * 分页查询订单
	 * 
	 * @param appkey
	 * @param qo
	 * @return
	 */
	public PageInfo<Order4PlatformDTO> findOrderInPage(String appkey, SubOrder4PlatformQO qo) {
		List<PlatformSupplierRelation> relationList = platformSupplierRelationService.selectListByAppkey(appkey);
        verifyQO(appkey, qo, relationList);
		
		SubOrderQO subQO = packSubOrderQO(qo, relationList);
		PageInfo<SubOrder4BackendDTO> subPage = salesOrderRemoteService.findSubOrder4BackendPage(subQO);
		return packOrder4PlatformDTO(subPage);
	}

	// 封装Page<Order4PlatformDTO>
	private PageInfo<Order4PlatformDTO> packOrder4PlatformDTO(PageInfo<SubOrder4BackendDTO> subPage) {
		PageInfo<Order4PlatformDTO> page = new PageInfo<Order4PlatformDTO>();
		List<Order4PlatformDTO> list = new ArrayList<Order4PlatformDTO>(subPage.getRows().size());

		Map<Long, String> payMap = getPayWayMap();	// 支付方式map
		
		List<SubOrder4BackendDTO> subList = subPage.getRows();
		for (SubOrder4BackendDTO sobDTO : subList) {
			if (null != sobDTO.getSubOrder().getPayWay()) {
				sobDTO.getSubOrder().setPayWayStr(payMap.get(sobDTO.getSubOrder().getPayWay().longValue()));	// 设置支付方式名称
			}
			Order4PlatformDTO opDTO = packOrder4PlatformDTO(sobDTO);
			list.add(opDTO);
		}

		page.setPage(subPage.getPage());
		page.setSize(subPage.getSize());
		page.setTotal(subPage.getTotal());
		page.setRows(list);
		return page;
	}

	// 封装Order4PlatformDTO
	private Order4PlatformDTO packOrder4PlatformDTO(SubOrder4BackendDTO sobDTO) {
		Order4PlatformDTO opDTO = new Order4PlatformDTO(sobDTO);
		
		List<OrderItem> lineList = sobDTO.getOrderItemList();
		List<OrderLine4PlatformDTO> itemList = new ArrayList<OrderLine4PlatformDTO>(lineList.size());
		for (OrderItem line : lineList) {
			OrderLine4PlatformDTO item = new OrderLine4PlatformDTO(line);
			itemList.add(item);
		}
		
		opDTO.setItemList(itemList);
		return opDTO;
	}

	// 校验QO
	private void verifyQO(String appkey, SubOrder4PlatformQO qo, List<PlatformSupplierRelation> relationList) {
		if (null != qo.getSupplierId()) { // 根据供应商查询
			if (!verifySupplierId(qo.getSupplierId(), relationList)) {
				log.error("appkey[{}]的用户越权访问供应商ID[{}]的数据", appkey, qo.getSupplierId());
				throw new PlatformServiceException(AuthError.ACCESS_ILLEGAL_DATA.code, AuthError.ACCESS_ILLEGAL_DATA.cnName);
			}
		}

		if (null != qo.getCode()) { // 根据订单号查询
			List<Long> orderCodeList = new ArrayList<Long>(1);
			orderCodeList.add(qo.getCode());
			Map<KeyType, List<OrderMiniDTO>> map = platformAccountService.verifySalesOrderAuthOfAccount(appkey, orderCodeList);
			if (null != map && CollectionUtils.isNotEmpty(map.get(KeyType.FAILURE))) {
				log.error("appkey[{}]的用户越权访问订单[{}]", appkey, qo.getCode());
				throw new PlatformServiceException(AuthError.ACCESS_ILLEGAL_DATA.code, AuthError.ACCESS_ILLEGAL_DATA.cnName);
			}
		}
	}

	// 校验供应商ID
	private boolean verifySupplierId(Long supplierId, List<PlatformSupplierRelation> relationList) {
		for (PlatformSupplierRelation relation : relationList) {
			if (supplierId.equals(relation.getSupplierId())) {
				return true;
			}
		}
		return false;
	}

	// 封装SubOrderQO
	private SubOrderQO packSubOrderQO(SubOrder4PlatformQO qo, List<PlatformSupplierRelation> relationList) {
		SubOrderQO subQO = new SubOrderQO();
		subQO.setPageSize(qo.getPageSize());
		subQO.setStartPage(qo.getPageNo());
		subQO.setStartTime(qo.getStartTime());
		subQO.setEndTime(qo.getEndTime());
		subQO.setOrderStatus(qo.getStatus());
		if (null == qo.getSupplierId()) {
			subQO.setSupplierId(relationList.get(0).getSupplierId());
		} else {
			subQO.setSupplierId(qo.getSupplierId());
		}
		subQO.setCode(qo.getCode());
		subQO.setOrderCode(qo.getCode());
		subQO.setHasPayCode(SubOrderQO.HAS_PAY_CODE_YES); // 必须支付
		return subQO;
	}

	// 提取供应商ID列表
	private List<Long> extractSupplierIdList(List<PlatformSupplierRelation> relationList) {
		if (CollectionUtils.isNotEmpty(relationList)) {
			List<Long> supplierIdList = new ArrayList<>(relationList.size());
			for (PlatformSupplierRelation relation : relationList) {
				supplierIdList.add(relation.getSupplierId());
			}
			return supplierIdList;
		}
		
		return Collections.emptyList();
	}

	// 获取支付途径map
	private Map<Long, String> getPayWayMap() {
		List<PaymentGateway> payList = paymentGatewayService.selectAllOrderbyParentId();
		if (CollectionUtils.isNotEmpty(payList)) {
			Map<Long, String> map = new HashMap<Long, String>();
			for (PaymentGateway pay : payList) {
				if (null != pay && null != pay.getGatewayId() && null != pay.getGatewayName()) {
					map.put(pay.getGatewayId(), pay.getGatewayName());
				}
			}
			return map;
		} else {
			log.error("获取支付途径列表失败");
		}
		return Collections.emptyMap();
	}
}
