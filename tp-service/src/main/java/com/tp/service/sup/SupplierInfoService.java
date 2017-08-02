package com.tp.service.sup;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.tp.common.dao.BaseDao;
import com.tp.common.vo.Constant;
import com.tp.common.vo.DAOConstant.MYBATIS_SPECIAL_STRING;
import com.tp.common.vo.supplier.SupplierConstant;
import com.tp.dao.sup.AuditRecordsDao;
import com.tp.dao.sup.SupplierAttachDao;
import com.tp.dao.sup.SupplierBankAccountDao;
import com.tp.dao.sup.SupplierBrandDao;
import com.tp.dao.sup.SupplierCategoryDao;
import com.tp.dao.sup.SupplierCustomsRecordationDao;
import com.tp.dao.sup.SupplierImageDao;
import com.tp.dao.sup.SupplierInfoDao;
import com.tp.dao.sup.SupplierInvoiceDao;
import com.tp.dao.sup.SupplierLinkDao;
import com.tp.dao.sup.SupplierUserDao;
import com.tp.dao.sup.SupplierXgLinkDao;
import com.tp.dto.common.FailInfo;
import com.tp.dto.common.ResultInfo;
import com.tp.model.sup.AuditRecords;
import com.tp.model.sup.SupplierAttach;
import com.tp.model.sup.SupplierBankAccount;
import com.tp.model.sup.SupplierBrand;
import com.tp.model.sup.SupplierCategory;
import com.tp.model.sup.SupplierCustomsRecordation;
import com.tp.model.sup.SupplierImage;
import com.tp.model.sup.SupplierInfo;
import com.tp.model.sup.SupplierInvoice;
import com.tp.model.sup.SupplierLink;
import com.tp.model.sup.SupplierUser;
import com.tp.model.sup.SupplierXgLink;
import com.tp.result.sup.SupplierDTO;
import com.tp.service.BaseService;
import com.tp.service.sup.ISupplierInfoService;

@Service
public class SupplierInfoService extends BaseService<SupplierInfo> implements ISupplierInfoService {

	@Autowired
	private SupplierInfoDao supplierInfoDao;
	
	@Override
	public BaseDao<SupplierInfo> getDao() {
		return supplierInfoDao;
	}
	@Autowired
    private SupplierAttachDao supplierAttachDao;
    @Autowired
    private SupplierImageDao supplierImageDao;
    @Autowired
    private AuditRecordsDao auditRecordsDao;
    @Autowired
    private SupplierUserDao supplierUserDao;
    @Autowired
    private SupplierLinkDao supplierLinkDao;
    @Autowired
    private SupplierBrandDao supplierBrandDao;
    @Autowired
    private SupplierCategoryDao supplierCategoryDao;
    @Autowired
    private SupplierXgLinkDao supplierXgLinkDao;
    @Autowired
    private SupplierBankAccountDao supplierBankAccountDao;
    @Autowired
    private SupplierInvoiceDao supplierInvoiceDao;
    @Autowired
    private SupplierCustomsRecordationDao supplierCustomsRecordationDao;
    
    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public ResultInfo<Boolean> auditSupplier(final SupplierInfo supplierInfo, final Integer auditStatus, final AuditRecords record){
        if (null != supplierInfo && null != supplierInfo.getId()) {
            final SupplierInfo oldSupplierInfo = this.queryById(supplierInfo.getId());
            if (null == oldSupplierInfo || !SupplierConstant.PREVIOUS_AUDIT_STATUS.get(auditStatus).contains(oldSupplierInfo.getAuditStatus())) {
            	return new ResultInfo<>(new FailInfo("供应商审核状态异常！ 审核失败。"));
            }

            final SupplierInfo updateInfo = new SupplierInfo();
            updateInfo.setAuditStatus(auditStatus);
            updateInfo.setUpdateTime(new Date());
            updateInfo.setId(supplierInfo.getId());
            final int num = supplierInfoDao.updateNotNullById(updateInfo);
            if (num < 1) {
            	return new ResultInfo<>(new FailInfo("审核异常。"));
            }
            auditRecordsDao.insert(record);
        } else {
        	return new ResultInfo<>(new FailInfo("审核异常。"));
        }
        return new ResultInfo<>(Boolean.TRUE);
    }

    /**
     * 保存供应商基本信息
     *
     * @param supplieDTO
     */
    @Override
    public ResultInfo<Long> saveSupplierBaseInfo(final SupplierDTO supplieDTO) {
        if (null == supplieDTO.getName() || supplieDTO.getName().length() > SupplierConstant.LENGTH_60) {
            return new ResultInfo<>(new FailInfo("供应商名称非空且不能超过60个字符"));
        }
        if (null == supplieDTO.getAlias() || supplieDTO.getAlias().length() > SupplierConstant.LENGTH_60) {
            return new ResultInfo<>(new FailInfo("供应商简称非空且不能超过60个字符"));
        }
        
        if (null == supplieDTO.getSupplierType()) {
            return new ResultInfo<>(new FailInfo("供应商类型不能为空。"));
        }
        if (null == supplieDTO.getLegalPerson()) {
            return new ResultInfo<>(new FailInfo("公司法人不能为空。"));
        }
        if (null == supplieDTO.getLinkName()) {
            return new ResultInfo<>(new FailInfo("供应商联系人不能为空。"));
        }
        final Map<String, Object> resultParams = new HashMap<String, Object>();
        final SupplierInfo supplierInfo = new SupplierInfo();
        try {
        	BeanUtils.copyProperties(supplieDTO, supplierInfo);
            supplierInfoDao.insert(supplierInfo);
            Long supplierId = supplierInfo.getId();
            saveSupplierLinks(supplieDTO.getSupplierLinkList(), supplierId);
            saveSupplierBrands(supplieDTO.getSupplierBrandList(), supplierId);
            saveSupplierCategorys(supplieDTO.getSupplierCategoryList(), supplierId);
            saveSupplierXgLinks(supplieDTO.getSupplierXgLinkList(), supplierId);
            saveSupplierBankAccount(supplieDTO.getSupplierBankAccountList(), supplierId);
            saveSupplierInvoice(supplieDTO.getSupplierInvoiceList(), supplierId);
            saveSupplierUserInfo(supplieDTO.getSupplierUser(), supplierId);
            saveSupplierRecordations(supplieDTO.getSupplierCustomsRecordationList(), supplierId);
            return new ResultInfo<>(supplierId);
        } catch (final Exception e) {
            logger.error(e.getMessage(), e);
            return new ResultInfo<>(new FailInfo("信息保存异常。"));
        }
        
    }
    public void saveSupplierBankAccount(final List<SupplierBankAccount> supplierBankaccountList, final Long supplierId) {
        if (CollectionUtils.isNotEmpty(supplierBankaccountList)) {
        	for (final SupplierBankAccount supplierBankaccount : supplierBankaccountList) {
                supplierBankaccount.setSupplierId(supplierId);
            }
            supplierBankAccountDao.batchInsert(supplierBankaccountList);
        }
    }
    public void saveSupplierRecordations(final List<SupplierCustomsRecordation> supplierCustomsRecordationList, final Long supplierId) {
    	if (CollectionUtils.isNotEmpty(supplierCustomsRecordationList)) {
	    	for (final SupplierCustomsRecordation supplierCustomsRecordation : supplierCustomsRecordationList) {
	    		supplierCustomsRecordation.setSupplierId(supplierId);
	        }
	    	supplierCustomsRecordationDao.batchInsert(supplierCustomsRecordationList);
    	}
    }
    public void saveSupplierBrands(final List<SupplierBrand> supplierBrandList, final Long supplierId) {
    	if (CollectionUtils.isNotEmpty(supplierBrandList)) {
	    	for (final SupplierBrand supplierBrand : supplierBrandList) {
	    		supplierBrand.setSupplierId(supplierId);
	        }
	    	supplierBrandDao.batchInsert(supplierBrandList);
    	}
    }

    public void saveSupplierCategorys(final List<SupplierCategory> supplierCategoryList, final Long supplierId) {
    	if (CollectionUtils.isNotEmpty(supplierCategoryList)) {
	    	for (final SupplierCategory supplierCategory : supplierCategoryList) {
	    		supplierCategory.setSupplierId(supplierId);
	        }
	    	supplierCategoryDao.batchInsert(supplierCategoryList);
    	}
    }

    public void saveSupplierImages(final List<SupplierImage> supplierImageList, final Long supplierId) {
    	if (CollectionUtils.isNotEmpty(supplierImageList)) {
	    	for (final SupplierImage supplierImage : supplierImageList) {
	    		supplierImage.setSupplierId(supplierId);
	        }
	    	supplierImageDao.batchInsert(supplierImageList);
    	}
    }

    public void saveSupplierInvoice(final List<SupplierInvoice> supplierInvoiceList, final Long supplierId) {
    	if (CollectionUtils.isNotEmpty(supplierInvoiceList)) {
	    	for (final SupplierInvoice supplierInvoice : supplierInvoiceList) {
	    		supplierInvoice.setSupplierId(supplierId);
	        }
	    	supplierInvoiceDao.batchInsert(supplierInvoiceList);
    	}
    }

    @Override
    public ResultInfo<Boolean> saveSupplierLicenInfo(final SupplierAttach supplierAttach, final Long supplierId) {
    	if(null!=supplierAttach){
    		supplierAttach.setUpdateUser(supplierAttach.getCreateUser());
	    	supplierAttachDao.insert(supplierAttach);
	    	if(CollectionUtils.isNotEmpty(supplierAttach.getSupplierImageList())){
	    		for(SupplierImage supplierImage:supplierAttach.getSupplierImageList()){
	    			supplierImage.setUpdateUser(supplierAttach.getCreateUser());
	    			supplierImage.setUpdateUser(supplierAttach.getCreateUser());
	    		}
	    	}
	    	saveSupplierImages(supplierAttach.getSupplierImageList(),supplierId);
    	}
        return new ResultInfo<>(Boolean.TRUE);
    }

    public void saveSupplierLinks(final List<SupplierLink> supplierLinkList, final Long supplierId) {
        if (CollectionUtils.isNotEmpty(supplierLinkList)) {
        	for(SupplierLink supplierLink:supplierLinkList){
        		supplierLink.setSupplierId(supplierId);
        	}
        	supplierLinkDao.batchInsert(supplierLinkList);
        }
    }

    public void saveSupplierXgLinks(final List<SupplierXgLink> supplierXgLinkList, final Long supplierId) {
    	if (CollectionUtils.isNotEmpty(supplierXgLinkList)) {
        	for(SupplierXgLink supplierXgLink:supplierXgLinkList){
        		supplierXgLink.setStatus(Constant.ENABLED.YES);
        		supplierXgLink.setSupplierId(supplierId);
        	}
        	supplierXgLinkDao.batchInsert(supplierXgLinkList);
        }
    }

    public void saveSupplierUserInfo(final SupplierUser SupplierUser, final Long supplierId) {
        if (null != SupplierUser) {
        	SupplierUser.setSupplierId(supplierId);
        	SupplierUser.setLoginName("V" + supplierId);
            supplierUserDao.insert(SupplierUser);
        }
    }

    @Override
    public SupplierDTO queryAllInfoBySupplierId(final Long supplierId) {
        final SupplierDTO dto = new SupplierDTO();
        final SupplierInfo supplierInfo = supplierInfoDao.queryById(supplierId);
        if (null == supplierInfo) {
            return null;
        }
        BeanUtils.copyProperties(supplierInfo, dto);
        Map<String,Object> params = new HashMap<String,Object>();
        params.put("supplierId", supplierId);
        params.put("status", Constant.DEFAULTED.YES);
        List<SupplierLink> supplierLinkList = supplierLinkDao.queryByParam(params);
        List<SupplierBrand> supplierBrandList = supplierBrandDao.queryByParam(params);
        List<SupplierCategory> supplierCategoryList = supplierCategoryDao.queryByParam(params);
        List<SupplierXgLink> supplierXgLinkList = supplierXgLinkDao.queryByParam(params);
        List<SupplierBankAccount> supplierBankAccountList = supplierBankAccountDao.queryByParam(params);
        List<SupplierInvoice> supplierInvoiceList = supplierInvoiceDao.queryByParam(params);
        List<SupplierCustomsRecordation> supplierCustomsRecordationList = supplierCustomsRecordationDao.queryByParam(params);
        dto.setSupplierLinkList(supplierLinkList);
        dto.setSupplierBrandList(supplierBrandList);
        dto.setSupplierCategoryList(supplierCategoryList);
        dto.setSupplierXgLinkList(supplierXgLinkList);
        dto.setSupplierBankAccountList(supplierBankAccountList);
        dto.setSupplierInvoiceList(supplierInvoiceList);
        dto.setSupplierCustomsRecordationList(supplierCustomsRecordationList);
        return dto;
    }

    /**
     * 更新供应商基本信息
     *
     * @param supplierDTO
     */
    @Override
    public ResultInfo<Boolean> updateSupplierInfo(final SupplierDTO supplierDTO) {
        if (null == supplierDTO || supplierDTO.getId() == null) {
            return new ResultInfo<>(new FailInfo("供应商参数异常。"));
        }
        
        ResultInfo<Boolean> msg=checkSupplierNameExists(supplierDTO);
        if(!msg.success){
        	return msg;
        }
        msg=checkSuppilerAliasExixts(supplierDTO);
        if(!msg.success){
        	return msg;
        }
        Long supplierId = supplierDTO.getId();
        updateSupplierLinks(supplierDTO.getSupplierLinkList(), supplierId);
        updateSupplierBrands(supplierDTO.getSupplierBrandList(), supplierId);
        updateSupplierCategorys(supplierDTO.getSupplierCategoryList(), supplierId);
        updateSupplierXgLinks(supplierDTO.getSupplierXgLinkList(), supplierId);
        updateSupplierBankAccount(supplierDTO.getSupplierBankAccountList(), supplierId);
        updateSupplierInvoice(supplierDTO.getSupplierInvoiceList(), supplierId);
        updateSupplierUserInfo(supplierDTO.getSupplierUser(), supplierId);
        updateSupplierRecordations(supplierDTO.getSupplierCustomsRecordationList(), supplierId);
        final Map<String, Object> resultParams = new HashMap<String, Object>();
        Boolean hasFail = true;
        Integer rownum = 0;
        supplierDTO.setUpdateUser(null);
    	supplierDTO.setCreateTime(null);
    	SupplierInfo supplierInfo = new SupplierInfo();
    	BeanUtils.copyProperties(supplierDTO, supplierInfo);
        rownum = supplierInfoDao.updateNotNullById(supplierInfo);
        SupplierInfo newSupplier = supplierInfoDao.queryById(supplierDTO.getId());
        if(!Constant.TF.YES.equals(newSupplier.getIsSea())){
        	//如果不是海淘的供应商  清空一些信息
        	newSupplier.setFreightTemplateId(null);
        	newSupplier.setFreightTemplateName(null);
        	supplierInfoDao.updateById(newSupplier);
        	supplierCustomsRecordationDao.updateStatusBySupplierId(supplierId, Constant.DEFAULTED.NO,supplierDTO.getUpdateUser());
        }
        
        return new ResultInfo<>(Boolean.TRUE);
    }

    /**
     * 校验供应商简称是否已经存在
     * 
     * @param supplierDTO
     * @param messageDto
     */
	private ResultInfo<Boolean> checkSuppilerAliasExixts(final SupplierInfo supplierInfo) {
		if(StringUtils.isNotBlank(supplierInfo.getName())){
    		Map<String,Object> params = new HashMap<String,Object>();
        	params.put("alias",supplierInfo.getAlias());
        	params.put("status",Constant.DEFAULTED.YES);
        	List<SupplierInfo> existsSuppliers = supplierInfoDao.queryByParam(params);
        	if(!CollectionUtils.isEmpty(existsSuppliers)){
        		for(SupplierInfo info : existsSuppliers){
        			if(!info.getId().equals(supplierInfo.getId())){
        				return new ResultInfo<>(new FailInfo("供应商简称已经存在。"));
        			}
        		}
        	}
        }
		return new ResultInfo<>(Boolean.TRUE);
	}

    /**
     * 校验供应商是否存在
     * 
     * @param supplierDTO
     * @param messageDto
     */
	private ResultInfo<Boolean> checkSupplierNameExists(final SupplierInfo supplierInfo) {
		if(StringUtils.isNotBlank(supplierInfo.getName())){
    		Map<String,Object> params = new HashMap<String,Object>();
        	params.put("name",supplierInfo.getName());
        	params.put("status",Constant.DEFAULTED.YES);
        	List<SupplierInfo> existsSuppliers = supplierInfoDao.queryByParam(params);
        	if(!CollectionUtils.isEmpty(existsSuppliers)){
        		for(SupplierInfo info : existsSuppliers){
        			if(!info.getId().equals(supplierInfo.getId())){
        				return new ResultInfo<>(new FailInfo("供应商名称已经存在。"));
        			}
        		}
        	}
        }
		return new ResultInfo<>(Boolean.TRUE);
	}

    /**
     * 更新供应商商家平台账户信息
     * 
     * @param supplierUserDTO
     * @param supplierId
     * @throws DaoException 
     */
    private void updateSupplierUserInfo(final SupplierUser supplierUser,final long supplierId) {
		String password = supplierUser.getPassword();
		SupplierUser updateSupplierUser = new SupplierUser();
		updateSupplierUser.setSupplierId(supplierId);
		updateSupplierUser.setUpdateUser(supplierUser.getUpdateUser());
		if(null != supplierUser && !StringUtils.isBlank(password)) {
			//更新密码
			updateSupplierUser.setPassword(password);
			supplierUserDao.updateUserBySupplierId(updateSupplierUser);
		}
		if(null != supplierUser && null != supplierUser.getStatus()){
			updateSupplierUser.setStatus( supplierUser.getStatus());
			supplierUserDao.updateUserBySupplierId(updateSupplierUser);
		}
	}
    /**
     * 更新海关备案号信息
     * 
     * @param updateSupplierRecordations
     * @param supplierId
     * @throws DaoException 
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void updateSupplierRecordations(final List<SupplierCustomsRecordation> supplierRecordationList, final long supplierId){
        if (CollectionUtils.isNotEmpty(supplierRecordationList)) {
        	supplierCustomsRecordationDao.updateStatusBySupplierId(supplierId, Constant.DEFAULTED.NO, supplierRecordationList.get(0).getUpdateUser());
            for (final SupplierCustomsRecordation supplierCustomsRecordation : supplierRecordationList) {
            	supplierCustomsRecordation.setSupplierId(supplierId);
            }
            supplierCustomsRecordationDao.batchInsert(supplierRecordationList);
        }
    }
	@Transactional(propagation = Propagation.REQUIRED)
    public void updateSupplierInvoice(final List<SupplierInvoice> supplierInvoiceList, final long supplierId){
        if (CollectionUtils.isNotEmpty(supplierInvoiceList)) {
        	supplierInvoiceDao.updateStatusBySupplierId(supplierId, Constant.DEFAULTED.NO, supplierInvoiceList.get(0).getUpdateUser());
            for (final SupplierInvoice supplierInvoice : supplierInvoiceList) {
            	supplierInvoice.setSupplierId(supplierId);
            }
            supplierInvoiceDao.batchInsert(supplierInvoiceList);
        }
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void updateSupplierLinks(final List<SupplierLink> supplierLinkList, final Long supplierId) {
        if (CollectionUtils.isNotEmpty(supplierLinkList)) {
        	supplierLinkDao.updateStatusBySupplierId(supplierId, Constant.DEFAULTED.NO, supplierLinkList.get(0).getUpdateUser());
            for (final SupplierLink supplierLink : supplierLinkList) {
            	supplierLink.setSupplierId(supplierId);
            }
            supplierLinkDao.batchInsert(supplierLinkList);
        }
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void updateSupplierXgLinks(final List<SupplierXgLink> supplierXgLinkList, final long supplierId){
        if (CollectionUtils.isNotEmpty(supplierXgLinkList)) {
        	supplierXgLinkDao.updateStatusBySupplierId(supplierId, Constant.DEFAULTED.NO, supplierXgLinkList.get(0).getUpdateUser());
            for (final SupplierXgLink supplierXgLink : supplierXgLinkList) {
            	supplierXgLink.setStatus(Constant.ENABLED.YES);
            	supplierXgLink.setSupplierId(supplierId);
            }
            supplierXgLinkDao.batchInsert(supplierXgLinkList);
        }
    }
    

    @Transactional(propagation = Propagation.REQUIRED)
    public void updateSupplierBankAccount(final List<SupplierBankAccount> supplierBankAccountList, final long supplierId){
        if (CollectionUtils.isNotEmpty(supplierBankAccountList)) {
        	supplierBankAccountDao.updateStatusBySupplierId(supplierId, Constant.DEFAULTED.NO, supplierBankAccountList.get(0).getUpdateUser());
            for (final SupplierBankAccount supplierBankAccount : supplierBankAccountList) {
            	supplierBankAccount.setSupplierId(supplierId);
            }
            supplierBankAccountDao.batchInsert(supplierBankAccountList);
        }
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void updateSupplierBrands(final List<SupplierBrand> supplierBrandList, final Long supplierId){
        if (CollectionUtils.isNotEmpty(supplierBrandList)) {
        	supplierBrandDao.updateStatusBySupplierId(supplierId, Constant.DEFAULTED.NO, supplierBrandList.get(0).getUpdateUser());
            for (final SupplierBrand supplierBrand : supplierBrandList) {
            	supplierBrand.setSupplierId(supplierId);
            }
            supplierBrandDao.batchInsert(supplierBrandList);
        }
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void updateSupplierCategorys(final List<SupplierCategory> supplierCategoryList, final Long supplierId){
        if (CollectionUtils.isNotEmpty(supplierCategoryList)) {
        	supplierCategoryDao.updateStatusBySupplierId(supplierId, Constant.DEFAULTED.NO, supplierCategoryList.get(0).getUpdateUser());
            for (final SupplierCategory supplierCategory : supplierCategoryList) {
            	supplierCategory.setSupplierId(supplierId);
            }
            supplierCategoryDao.batchInsert(supplierCategoryList);
        }
    }
    
    public List<SupplierInfo> querySupplierInfoByIds(List<Long> ids){
    	Map<String,Object> params = new HashMap<String,Object>();
    	params.put(MYBATIS_SPECIAL_STRING.COLUMNS.name(), " id in ("+StringUtils.join(ids, Constant.SPLIT_SIGN.COMMA)+")");
        return queryByParam(params);
    }
}
