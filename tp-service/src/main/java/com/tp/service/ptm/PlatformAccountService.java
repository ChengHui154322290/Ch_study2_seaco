package com.tp.service.ptm;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tp.common.dao.BaseDao;
import com.tp.common.vo.PageInfo;
import com.tp.common.vo.ord.ParamValidator;
import com.tp.common.vo.ptm.AccountConstant;
import com.tp.common.vo.ptm.SalesOrderConstants;
import com.tp.common.vo.supplier.entry.AuditStatus;
import com.tp.dao.ptm.PlatformAccountDao;
import com.tp.dto.ptm.remote.Account4DetailDTO;
import com.tp.dto.ptm.remote.Account4ListDTO;
import com.tp.dto.ptm.remote.AccountDTO;
import com.tp.dto.ptm.remote.SupplierRelationDTO;
import com.tp.dto.ptm.salesorder.OrderMiniDTO;
import com.tp.exception.ErrorCodes;
import com.tp.exception.InvalidAppkeyException;
import com.tp.exception.PlatformServiceException;
import com.tp.model.bse.Brand;
import com.tp.model.ord.SubOrder;
import com.tp.model.ptm.PlatformAccount;
import com.tp.model.ptm.PlatformSupplierRelation;
import com.tp.model.sup.SupplierInfo;
import com.tp.result.sup.SupplierResult;
import com.tp.service.BaseService;
import com.tp.service.ord.remote.ISalesOrderRemoteService;
import com.tp.service.ptm.IPlatformAccountService;
import com.tp.service.ptm.IPlatformSupplierRelationService;
import com.tp.service.sup.IPurchasingManagementService;

@Service
public class PlatformAccountService extends BaseService<PlatformAccount> implements IPlatformAccountService {

	@Autowired
	private PlatformAccountDao platformAccountDao;
	@Autowired
	private ISalesOrderRemoteService salesOrderRemoteService;
	@Autowired
	private IPlatformSupplierRelationService platformSupplierRelationService;
	
	@Override
	public BaseDao<PlatformAccount> getDao() {
		return platformAccountDao;
	}

	/* (non-Javadoc)
	 * @see com.tp.service.ptm.IPlatformAccountService#selectTokenByAppkey(java.lang.String)
	 */
	@Override
	public String selectTokenByAppkey(String appKey) {
		String token = null;
		try {
			PlatformAccount account = new PlatformAccount();
			account.setAppkey(appKey);
			List<PlatformAccount> accountList = platformAccountDao.queryByObject(account);
			PlatformAccount accountDO = (CollectionUtils.isEmpty(accountList) ? null : accountList.get(0));
			if (accountDO != null && AccountConstant.AccountStatus.ENABLED.code.equals(accountDO.getStatus())) {
				token = accountDO.getToken();
			}
		} catch (Exception e) {
			logger.error("根据appkey查询token帐号异常", e);
		}
		return token;
	}

	/* (non-Javadoc)
	 * @see com.tp.service.ptm.IPlatformAccountService#verifySalesOrderAuthOfAccount(java.lang.Long, java.util.List)
	 */
	@Override
	public Map<KeyType, List<OrderMiniDTO>> verifySalesOrderAuthOfAccount(String appkey, List<Long> orderCodeList) {
		 if (CollectionUtils.isEmpty(orderCodeList)) {
	            return Collections.emptyMap();
	        }

	        if (orderCodeList.size() > SalesOrderConstants.QUERY_ORDER_LIMIT) {
	            logger.error("超出查询记录数限制，最多只能查询100个订单，但实际为{}个", orderCodeList.size());
	            throw new PlatformServiceException(ErrorCodes.OrderQueryError.COUNT_LIMIT.code, ErrorCodes.OrderQueryError.COUNT_LIMIT.cnName);
	        }

	        Map<KeyType, List<OrderMiniDTO>> resultMap = new HashMap<PlatformAccountService.KeyType, List<OrderMiniDTO>>();
	        List<OrderMiniDTO> successList = new ArrayList<OrderMiniDTO>();
	        List<OrderMiniDTO> failureList = new ArrayList<OrderMiniDTO>();
	        List<OrderMiniDTO> notExistList = new ArrayList<OrderMiniDTO>();
	        resultMap.put(KeyType.SUCCESS, successList);
	        resultMap.put(KeyType.FAILURE, failureList);
	        resultMap.put(KeyType.NOT_EXIST, notExistList);

	        List<SubOrder> subList = salesOrderRemoteService.findSubOrderDTOListBySubCodeList(orderCodeList);
	        Map<Long, SubOrder> subMap = toSubMap(subList);
	        List<PlatformSupplierRelation> supplierList = platformSupplierRelationService.selectListByAppkey(appkey);
	        // 有权限的供应商ID
	        Set<Long> supplierIdSet = extractSupplierIdListFromSupplierRelationDO(supplierList);

	        for (Long code : orderCodeList) {
	            OrderMiniDTO orderMini = new OrderMiniDTO();
	            orderMini.setCode(code);

	            SubOrder sub = subMap.get(code);
	            if (null != sub && supplierIdSet.contains(sub.getSupplierId())) {
	                successList.add(orderMini);
	            } else {
	                failureList.add(orderMini);

	                if (sub == null) {
	                    notExistList.add(orderMini);
	                }
	            }
	        }

	        return resultMap;
	}
	// 转MAP，key：子单号
    private Map<Long, SubOrder> toSubMap(final List<SubOrder> subList) {
        if (CollectionUtils.isNotEmpty(subList)) {
            Map<Long, SubOrder> map = new HashMap<Long, SubOrder>();
            for (SubOrder sub : subList) {
                map.put(sub.getOrderCode(), sub);
            }
            return map;
        }
        return Collections.emptyMap();
    }
 // 提取供应商ID
    private Set<Long> extractSupplierIdListFromSupplierRelationDO(final List<PlatformSupplierRelation> supplierList) {
        if (CollectionUtils.isNotEmpty(supplierList)) {
            Set<Long> set = new HashSet<Long>(supplierList.size());
            for (PlatformSupplierRelation sr : supplierList) {
                set.add(sr.getSupplierId());
            }
            return set;
        }

        return Collections.emptySet();
    }

    @Override
    public PageInfo<Account4ListDTO> findAccount4ListDTOByQOWithPage(PlatformAccount qo) {
    	PageInfo<PlatformAccount> page = new PageInfo<PlatformAccount>(qo.getStartPage(),qo.getPageSize());
        page = queryPageByObject(qo, page); // 分页查账户
        List<Account4ListDTO> resultList = toAccount4ListDTOList(page.getRows()); // 转Account4ListDTO

        PageInfo<Account4ListDTO> resultPage = new PageInfo<Account4ListDTO>();
        resultPage.setRows(resultList);
        resultPage.setPage(page.getPage());
        resultPage.setSize(page.getSize());
        resultPage.setTotal(page.getTotal());
        return resultPage;
    }
 // 转Account4ListDTO
    private List<Account4ListDTO> toAccount4ListDTOList(final List<PlatformAccount> list) {
        if (CollectionUtils.isNotEmpty(list)) {
            List<Account4ListDTO> dtoList = new ArrayList<Account4ListDTO>(list.size());
            for (PlatformAccount account : list) {
                dtoList.add(new Account4ListDTO(AccountDTO.toDTO(account)));
            }
            return dtoList;
        }

        return Collections.emptyList();
    }

    @Override
    @Transactional
    public PlatformAccount createAccount(PlatformAccount account, final List<SupplierInfo> supplierList) {
        validateParam(account, supplierList, true); // 入参校验

        
        PlatformAccount model = new PlatformAccount();
		model.setCreateTime(new Date());
		model.setModifyTime(new Date());
		model.setName(account.getName());
		model.setStatus(1);
		model.setAppkey(account.getAppkey());
		model.setToken(UUID.randomUUID().toString().replaceAll("-", StringUtils.EMPTY));
		model = insert(model); // 创建账号
        createSupplierRelation(model, supplierList); // 创建供应商关系

        return model;
    }
    private static final String BUSINESS_CREATE = "创建账户";
    /**
     * 入参校验
     * 
     * @param account
     * @param supplierList
     * @param isCreated 是否是创建操作
     */
    private void validateParam(final PlatformAccount account, final List<SupplierInfo> supplierList, final boolean isCreated) {
        ParamValidator pv = new ParamValidator(BUSINESS_CREATE);
        pv.notNull(account, "账户");
        pv.notEmpty(supplierList, "供应商关系");
        if (!isCreated) {
            pv.notNull(account.getId(), "appkey");
        }
        pv.notBlank(account.getName(), "账户名称");
        for (SupplierInfo supplier : supplierList) {
            pv.notNull(supplier.getId(), "供应商ID");
        }
    }
    
 // 创建供应商关系
    private void createSupplierRelation(final PlatformAccount account, final List<SupplierInfo> supplierList) {
        /**
         * 获取供应商 编号集合
         */
        List<Long> idList = new ArrayList<>();
        for(SupplierInfo info : supplierList) {
        	idList.add(info.getId());
        }

        /**
         * 获取有效的供应商集合
         */
        List<SupplierInfo> supList = findSupplierList(idList);

        /**
         * 把供应商集合转换为 对接方与供应商关系集合
         */
        List<PlatformSupplierRelation> relationList = new ArrayList<>();
        for(SupplierInfo info : supList) {
        	PlatformSupplierRelation relation = new PlatformSupplierRelation();
            relation.setAccountId(account.getId());
            relation.setCreateTime(new Date());
            relation.setSupplierId(info.getId());
            relationList.add(relation);
        }
        for(PlatformSupplierRelation info : relationList){
        	platformSupplierRelationService.insert(info);
        }

        // 释放 相关集合数据，等待 GC 回收
        idList.clear();
        supList.clear();
        relationList.clear();
        idList = null;
        supList = null;
        relationList = null;
    }
    @Autowired
    private IPurchasingManagementService purchasingManagementService;
 // 查询供应商列表
    private List<SupplierInfo> findSupplierList(final List<Long> supplierIdList) {
        try {
            SupplierResult result = purchasingManagementService.getUsedSuppliersByIds(supplierIdList, AuditStatus.THROUGH, 1); // 批量查询供应商
            return result.getSupplierInfoList();
        } catch (Exception e) {
            logger.error("调用供应商批量查询接口异常", e);
        }
        return Collections.emptyList();
    }

    // 转供应商map，key＝供应商ID
    private Map<Long, SupplierInfo> toSupplierMap_Id(final List<SupplierInfo> supplierList) {
        if (CollectionUtils.isNotEmpty(supplierList)) {
            Map<Long, SupplierInfo> map = new HashMap<Long, SupplierInfo>();

            for (SupplierInfo supplier : supplierList) {
                if (null != supplier && null != supplier.getId()) {
                    map.put(supplier.getId(), supplier);
                }
            }

            return map;
        }

        return Collections.emptyMap();
    }

    @Override
    @Transactional
    public void updateAccount(final PlatformAccount account, final List<SupplierInfo> supplierList) {
        validateParam(account, supplierList, false); // 入参校验
        PlatformAccount query = new PlatformAccount();
        query.setId(account.getId());
        List<PlatformAccount> list = queryByObject(query);
        for(PlatformAccount act : list) {
        	act.setName(account.getName());
        	act.setAppkey(account.getAppkey());
        	act.setModifyTime(new Date());
        	updateById(act);// 更新账户名称
        	updateSupplierRelation(act, supplierList); // 更新供应商关系
        }
       
    }
 // 更新供应商关系
    private void updateSupplierRelation(final PlatformAccount account, final List<SupplierInfo> supplierList) {
    	PlatformSupplierRelation query = new PlatformSupplierRelation();
    	query.setAccountId(account.getId());
    	List<PlatformSupplierRelation> list = platformSupplierRelationService.queryByObject(query);
    	if(CollectionUtils.isNotEmpty(list)) {
    		for(PlatformSupplierRelation sr : list) {
    			platformSupplierRelationService.deleteById(sr.getId()); // 删除该账号所有供应商关联
    		}
    	}
        createSupplierRelation(account, supplierList); // 创建供应商关系
    }

    @Override
    public Account4DetailDTO findAccount4DetailDTOByAppkey(final String appkey) {
    	PlatformAccount query = new PlatformAccount();
    	query.setAppkey(appkey);
    	PlatformAccount account = queryUniqueByObject(query);
        if (null == account) { // 无效appkey
            throw new InvalidAppkeyException(appkey);
        }

        List<PlatformSupplierRelation> relationList = platformSupplierRelationService.selectListByAppkey(appkey);
        List<SupplierRelationDTO> supplierList = toAccount4DetailDTO(relationList);

        return new Account4DetailDTO(AccountDTO.toDTO(account), supplierList);
    }
    
 // 转SupplierRelationDTO，增加供应商名称
    private List<SupplierRelationDTO> toAccount4DetailDTO(final List<PlatformSupplierRelation> relationList) {
        List<Long> supplierIdList = extractSupplierIdList(relationList);
        List<SupplierInfo> supplierList = findSupplierList(supplierIdList); // 批量查询供应商
        Map<Long, SupplierInfo> supplierMap = toSupplierMap_Id(supplierList);

        List<SupplierRelationDTO> dtoList = new ArrayList<SupplierRelationDTO>(relationList.size());
        for (PlatformSupplierRelation relation : relationList) {

        	SupplierInfo temp = supplierMap.get(relation.getSupplierId());
            /**
             * 若 SupplierDO is null 说明关系存在，但供应商不存在
             */
            if (temp == null) {
                continue;
            }
            SupplierRelationDTO dto = SupplierRelationDTO.toDTO(relation);
            dto.setSupplierName(temp.getName()); // 供应商名称
            dtoList.add(dto);
        }

        return dtoList;
    }
    
 // 提取所有供应商ID
    private List<Long> extractSupplierIdList(final List<PlatformSupplierRelation> relationList) {
        Set<Long> supplierIdSet = new HashSet<Long>(relationList.size());
        for (PlatformSupplierRelation relation : relationList) {
            if (null != relation) {
                supplierIdSet.add(relation.getSupplierId());
            }
        }
        return new ArrayList<Long>(supplierIdSet);
    }

    @Override
    public void deteleAccount(String appkey) {
    	PlatformAccount query = new PlatformAccount();
    	PlatformAccount account = queryUniqueByObject(query);
    	if(account != null) {
    		account.setStatus(-1);
    		updateById(account);
    	}
    }
}
