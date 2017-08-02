package com.tp.backend.controller.supplier.ao;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.vo.Constant;
import com.tp.common.vo.supplier.CommonUtil;
import com.tp.common.vo.supplier.SupplierConstant;
import com.tp.common.vo.supplier.entry.AuditStatus;
import com.tp.common.vo.supplier.entry.SupplierType;
import com.tp.model.bse.Brand;
import com.tp.model.bse.Category;
import com.tp.model.sup.SupplierAttach;
import com.tp.model.sup.SupplierBankAccount;
import com.tp.model.sup.SupplierCategory;
import com.tp.model.sup.SupplierCustomsRecordation;
import com.tp.model.sup.SupplierImage;
import com.tp.model.sup.SupplierInfo;
import com.tp.model.sup.SupplierInvoice;
import com.tp.model.sup.SupplierLink;
import com.tp.model.sup.SupplierUser;
import com.tp.model.sup.SupplierXgLink;
import com.tp.proxy.usr.UserHandler;
import com.tp.result.sup.SupplierDTO;
import com.tp.service.bse.IBrandService;
import com.tp.service.bse.ICategoryService;
import com.tp.service.stg.IWarehouseService;
import com.tp.service.sup.IAuditRecordsService;
import com.tp.service.sup.ISupplierAttachService;
import com.tp.service.sup.ISupplierBankAccountService;
import com.tp.service.sup.ISupplierBrandService;
import com.tp.service.sup.ISupplierCategoryService;
import com.tp.service.sup.ISupplierInfoService;
import com.tp.service.sup.ISupplierInvoiceService;
import com.tp.service.sup.ISupplierLinkService;
import com.tp.service.sup.ISupplierUserService;

/**
 * <pre>
 * 供应业务逻辑相关
 * </pre>
 *
 * @author Administrator
 * @version $Id: SupplierAO.java, v 0.1 2014年12月25日 上午11:55:11 Administrator Exp $
 */
@Service
public class SupplierAO extends SupplierBaseAO{

    @Autowired
    private ISupplierInfoService supplierInfoService;
    @Autowired
    private ISupplierBrandService supplierBrandService;

    @Autowired
    private IBrandService brandService;

    @Autowired
    private ICategoryService categoryService;

    @Autowired
    private ISupplierAttachService supplierAttachService;

    @Autowired
    private IAuditRecordsService auditRecordsService;

    @Autowired
    private IWarehouseService warehouseService;

    @Autowired
    private ISupplierUserService supplierUserService;

    @Autowired
    private ISupplierCategoryService supplierCategoryService;

    @Autowired
    private ISupplierLinkService supplierLinkService;

    @Autowired
    private ISupplierBankAccountService supplierBankAccountService;

    @Autowired
    private ISupplierInvoiceService supplierInvoiceService;
    @Autowired
    private SupplierItemAO supplierItemAO;
    @Autowired
    private SupplierUtilAO supplierUtilAO;

    /**
     * <pre>
     * 生成供应商银行信息
     * </pre>
     *
     * @param supplieDTO
     * @param resultMap
     * @param request
     * @param needCheck
     */
    private void generateSupplierBankInfo(final SupplierDTO supplieDTO, final Map<String, Object> resultMap, final HttpServletRequest request,
        final Boolean needCheck) {
        final List<SupplierBankAccount> bList = new ArrayList<SupplierBankAccount>();
        final String[] bankTypes = getStringValues(request, "bankType");
        final String[] bankNames = getStringValues(request, "bankName");
        final String[] bankAccounts = getStringValues(request, "bankAccount");
        final String[] bankAccNames = getStringValues(request, "bankAccName");
        final String[] bankCurrencys = getStringValues(request, "bankCurrency");
        if (null != bankTypes && bankTypes.length > 0) {
            for (int i = 0; i < bankTypes.length; i++) {
                final SupplierBankAccount supplierBankaccount = new SupplierBankAccount();
                supplierBankaccount.setAccountType(bankTypes[i]);
                supplierBankaccount.setBankName(bankNames[i]);
                supplierBankaccount.setBankAccount(bankAccounts[i]);
                supplierBankaccount.setBankAccName(bankAccNames[i]);
                supplierBankaccount.setBankCurrency(bankCurrencys[i]);
                supplierBankaccount.setStatus(Constant.ENABLED.YES);
                supplierBankaccount.setCreateUser(SupplierUtilAO.getCurrentUserName());
                supplierBankaccount.setUpdateUser(supplierBankaccount.getCreateUser());
                bList.add(supplierBankaccount);
            }
        }
        supplieDTO.setSupplierBankAccountList(bList);
    }

    /**
     * <pre>
     * 生成供应商基本信息
     * </pre>
     *
     * @param supplierAO
     * @param resultMap
     * @param request
     */
    private void generateSupplierBaseInfo(final SupplierDTO supplierDTO, final Map<String, Object> resultMap, final HttpServletRequest request,
        final Boolean needCheck) {
        /** 供应商编号 */
        final String supplierCode = "";
        /** 供应商或商家名称 */
        final String name = getStringValue(request, "name");
        /** 供应商简称*/
        final String simpleName = getStringValue(request, "simpleName");
        
        /** 供应商类型（自营商家/平台商家） */
        final String supplierType = getStringValue(request, "supplierType");
        /** 公司法人 */
        final String legalPerson = getStringValue(request, "legalPerson");
        /** 联系人 */
        final String linkName = getStringValue(request, "linkName");
        /**  */
        final String address = getStringValue(request, "address");
        /** 邮箱 */
        final String email = getStringValue(request, "email");
        /** 联系电话 */
        final String phone = getStringValue(request, "phone");
        /** 进项税率 */
        final Double incomeRate = getRateInfo(request, "incomeTaxRate");
        /** 传真 */
        final String fax = getStringValue(request, "fax");
        /** 父供应商id */
        Long parentSupplierId = getLongValue(request, "parentId");
        /** 供应商状态 */
        final Integer status = Constant.ENABLED.YES;
        /** 审核状态 */
        final Integer examineStatus = AuditStatus.WAIT_UPLOAD_FILE.getStatus();
        /** 父供应商的名称 */
        String parentSupplierName = "";
        /** 获取品牌Id */
        final Long[] brandIds = getLongValues(request, "supplierBrand");
        if (null != brandIds && brandIds.length > 0) {
            final StringBuffer brandNameStr = new StringBuffer();
            final List<Long> bids = Arrays.asList(brandIds);
            final Map<Long, Brand> brandMap = supplierItemAO.getBrandMap(bids);
            for (int i = 0; i < bids.size(); i++) {
                if (null != brandIds[i] && null != brandMap.get(brandIds[i])) {
                    final Brand brand = brandMap.get(brandIds[i]);
                    brandNameStr.append(brand.getName()).append(",");
                }
            }
            if (brandNameStr.length() > 0) {
                brandNameStr.deleteCharAt(brandNameStr.length() - 1);
            }
            // 将所有品牌名称保存到key1中
            supplierDTO.setKey1(brandNameStr.toString());
        }

        /** 是否是海淘供应商 */
        Long expressTemplateId = getLongValue(request, "expressTemplateId");
        String expressTemplateName = getStringValue(request, "expressTemplateName");
        final Integer isHaitao = getIntValue(request, "isHtSupplier");
        if (new Integer(1).equals(isHaitao)) {
            if (null == expressTemplateId) {
                resultMap.put(SupplierConstant.SUCCESS_KEY, false);
                resultMap.put(SupplierConstant.MESSAGE_KEY, "海淘供应商运费模板非空。");
                return;
            }
        } else {
            expressTemplateId = null;
            expressTemplateName = null;
        }
        final String expressTemplateRemark = getStringValue(request, "expressTemplateRemark");
        // 如果自己不是主供应商 可以设置主供应商
        if (!SupplierType.MAIN.getValue().equals(supplierType)) {
            if (null != parentSupplierId) {
                final SupplierInfo supplieDO = supplierInfoService.queryById(parentSupplierId);
                if (null == supplieDO) {
                    parentSupplierId = null;
                } else {
                    parentSupplierName = supplieDO.getName();
                }
            }
        }

        if (null == name && needCheck) {
            resultMap.put(SupplierConstant.SUCCESS_KEY, false);
            resultMap.put(SupplierConstant.MESSAGE_KEY, "供应商名称非空。");
            return;
        }
        if (null == supplierType && needCheck) {
            resultMap.put(SupplierConstant.SUCCESS_KEY, false);
            resultMap.put(SupplierConstant.MESSAGE_KEY, "供应商类型非空。");
            return;
        }
        if (null == legalPerson && needCheck) {
            resultMap.put(SupplierConstant.SUCCESS_KEY, false);
            resultMap.put(SupplierConstant.MESSAGE_KEY, "公司法人非空。");
            return;
        }
        if (null == linkName && needCheck) {
            resultMap.put(SupplierConstant.SUCCESS_KEY, false);
            resultMap.put(SupplierConstant.MESSAGE_KEY, "供应商联系人非空。");
            return;
        }
        supplierDTO.setName(name);
        supplierDTO.setAlias(simpleName);
        supplierDTO.setSupplierType(supplierType);
        supplierDTO.setLegalPerson(legalPerson);
        supplierDTO.setAddress(address);
        supplierDTO.setEmail(email);
        supplierDTO.setPhone(phone);
        supplierDTO.setFax(fax ==null ? "" : fax);
        supplierDTO.setStatus(status);
        supplierDTO.setAuditStatus(examineStatus);
        supplierDTO.setSupplierCode(supplierCode);
        supplierDTO.setParentSupplierId(parentSupplierId);
        supplierDTO.setParentSupplierName(parentSupplierName);
        supplierDTO.setLinkName(linkName);
        supplierDTO.setCreateTime((Date) resultMap.get("createTime"));
        supplierDTO.setUpdateTime((Date) resultMap.get("createTime"));
        supplierDTO.setCreateUser(SupplierUtilAO.getCurrentUserName());
        supplierDTO.setUpdateUser(supplierDTO.getCreateUser());
        supplierDTO.setIsSea(isHaitao);
        supplierDTO.setFreightTemplateId(expressTemplateId);
        supplierDTO.setFreightTemplateName(expressTemplateName);        
        supplierDTO.setSupplierDesc(expressTemplateRemark ==null ? "" : expressTemplateRemark );
        supplierDTO.setIncomeTaxRate(incomeRate.doubleValue());
        resultMap.put(SupplierConstant.SUCCESS_KEY, true);
    }

    /**
     * <pre>
     * 生成证件信息
     * </pre>
     *
     * @param supplierAttachDTO
     * @param resultMap
     * @param request
     */
    private void generateSupplierBaseLicenInfo(final SupplierAttach supplierAttachDTO, final Map<String, Object> resultMap,
        final HttpServletRequest request, final Boolean needCheck) {
        /** 状体（1：启用 0：禁用） */
        final Integer status = Constant.ENABLED.YES;
        /** 营业执照 */
        final String businessLicense = getStringValue(request, "businessLicense");
        /** 税务登记证 */
        final String taxregist = getStringValue(request, "taxregist");
        /** 组织机构代码证 */
        final String organize = getStringValue(request, "organize");
        /** 商标注册证 */
        final String brandRetist = getStringValue(request, "brandRetist");
        /** 一般纳税人资格证 */
        final String taxpayer = getStringValue(request, "taxpayer");
        /** 银行开户许可证 */
        final String depositBank = getStringValue(request, "depositBank");
        /** 法人/授权人身份证明 */
        final String agentLiscenceCredit = getStringValue(request, "agentLiscenceCredit");
        /** 法定代表人授权委托书 */
        final String agentLiscence = getStringValue(request, "agentLiscence");
        
        // by zhs 0126
        /** brandLiscence*/
        final String brandLiscence = getStringValue(request, "brandLiscence");
        final String specialPapers = getStringValue(request, "specialPapers");
        final String qualityLiscence = getStringValue(request, "qualityLiscence");
        /** 用户ip */
        supplierAttachDTO.setStatus(status);
        supplierAttachDTO.setBusinessLicense(businessLicense);
        supplierAttachDTO.setTaxpayer(taxpayer);
        supplierAttachDTO.setTaxregist(taxregist);
        supplierAttachDTO.setOrganize(organize);
        supplierAttachDTO.setDepositBank(depositBank);
        supplierAttachDTO.setBrandRetist(brandRetist);
        supplierAttachDTO.setAgentLiscenceCredit(agentLiscenceCredit);
        supplierAttachDTO.setAgentLiscence(agentLiscence);
        supplierAttachDTO.setCreateUser(SupplierUtilAO.getCurrentUserName());
        supplierAttachDTO.setUpdateUser(supplierAttachDTO.getCreateUser());
        // by zhs 0126
        supplierAttachDTO.setBrandLiscence(brandLiscence);
        supplierAttachDTO.setSpecialPapers(specialPapers);
        if (qualityLiscence == null) {
            supplierAttachDTO.setQualityLiscence("");			
		}else {
            supplierAttachDTO.setQualityLiscence(qualityLiscence);			
		}
        resultMap.put(SupplierConstant.SUCCESS_KEY, true);
    }


    /**
     * <pre>
     * 生成供应商分类信息
     * </pre>
     *
     * @param supplierDTO
     * @param resultMap
     * @param request
     */
    private void generateSupplierCategoryInfo(final SupplierDTO supplierDTO, final Map<String, Object> resultMap, final HttpServletRequest request,
        final Boolean needCheck) {

        final List<SupplierCategory> bList = new ArrayList<SupplierCategory>();
        // 有效的品牌的id
        final Long[] selBrandIds = getLongValues(request, "supplierABrandSel");
        // 所有选择的品牌id
        final Long[] brandIds = getLongValues(request, "supplierBrand");

        if (null == brandIds || brandIds.length == 0) {
            supplierDTO.setSupplierCategoryList(bList);
            return;
        }
        Map<Long, Brand> brandMap = null;
        if (null != selBrandIds && selBrandIds.length > 0) {
            // 此方法不会返回null
            brandMap = supplierItemAO.getBrandMap(Arrays.asList(brandIds));
        } else {
            supplierDTO.setSupplierCategoryList(bList);
            return;
        }
        if (null == brandMap) {
            supplierDTO.setSupplierCategoryList(bList);
            return;
        }
        // 设置分类信息
        setCategoryInfo(resultMap, request, bList, brandIds, brandMap);

        supplierDTO.setSupplierCategoryList(bList);
    }

    /**
     * 设置分类信息
     *
     * @param resultMap
     * @param request
     * @param bList
     * @param brandIds
     * @param brandMap
     */
    private void setCategoryInfo(final Map<String, Object> resultMap, final HttpServletRequest request, final List<SupplierCategory> bList,
        final Long[] brandIds, final Map<Long, Brand> brandMap) {
        final Long[] categoryDaleis = getLongValues(request, "categoryDalei");
        final Long[] supplierCategoryMids = getLongValues(request, "supplierCategoryMid");
        final Long[] supplierCategorySmall = getLongValues(request, "supplierCategorySmall");
        Map<Long, Category> smallCategoryMap = new HashMap<Long, Category>();
        Map<Long, Category> categoryBigMap = new HashMap<Long, Category>();
        Map<Long, Category> categoryMidMap = new HashMap<Long, Category>();

        if (null != supplierCategorySmall && supplierCategorySmall.length > 0) {
            smallCategoryMap = supplierItemAO.getCategoryMap(Arrays.asList(supplierCategorySmall));
        }
        if (null != categoryDaleis && categoryDaleis.length >= 0) {
            categoryBigMap = supplierItemAO.getCategoryMap(Arrays.asList(categoryDaleis));
        }
        if (null != supplierCategoryMids && supplierCategoryMids.length > 0) {
            categoryMidMap = supplierItemAO.getCategoryMap(Arrays.asList(supplierCategoryMids));
        }
        for (int i = 0; i < brandIds.length; i++) {
            final Long brandId = brandIds[i];
            if (null != brandId && null != brandMap.get(brandId)) {
                final Brand brandDO = brandMap.get(brandId);
                final SupplierCategory categoryDTO = new SupplierCategory();
                categoryDTO.setBrandId(brandDO.getId());
                categoryDTO.setBrandName(brandDO.getName());
                if (null != categoryDaleis && categoryDaleis.length > i && null != categoryBigMap.get(categoryDaleis[i])) {
                    categoryDTO.setCategoryBigId(categoryDaleis[i]);
                    categoryDTO.setCategoryBigName(categoryBigMap.get(categoryDaleis[i]).getName());
                }
                if (null != supplierCategoryMids && supplierCategoryMids.length > i && null != categoryMidMap.get(supplierCategoryMids[i])) {
                    categoryDTO.setCategoryMidId(supplierCategoryMids[i]);
                    categoryDTO.setCategoryMidName(categoryMidMap.get(supplierCategoryMids[i]).getName());
                }
                if (null != supplierCategorySmall && supplierCategorySmall.length > i && null != smallCategoryMap.get(supplierCategorySmall[i])) {
                    categoryDTO.setCategorySmallId(supplierCategorySmall[i]);
                    categoryDTO.setCategorySmallName(smallCategoryMap.get(supplierCategorySmall[i]).getName());
                }
                categoryDTO.setStatus(Constant.ENABLED.YES);
                categoryDTO.setCreateTime(new Date());
                categoryDTO.setUpdateTime(new Date());
                categoryDTO.setCreateUser(SupplierUtilAO.getCurrentUserName());
                categoryDTO.setUpdateUser(categoryDTO.getCreateUser());
                bList.add(categoryDTO);
            }
        }
    }

    /***
     * <pre>
     * 生成供应商开户行信息
     * </pre>
     *
     * @param supplieDTO
     * @param resultMap
     * @param request
     * @param needCheck
     */
    private void generateSupplierKpBankInfo(final SupplierDTO supplieDTO, final Map<String, Object> resultMap, final HttpServletRequest request,
        final Boolean needCheck) {
        /** 创建供应商信息的用户userId */
        final List<SupplierInvoice> bList = new ArrayList<SupplierInvoice>();
        final String[] kpNames = getStringValues(request, "kpName");
        final String[] kpBanks = getStringValues(request, "kpBank");
        final String[] kpAccounts = getStringValues(request, "kpAccount");
        final String[] kpAddresss = getStringValues(request, "kpAddress");
        final String[] taxpayerCodes = getStringValues(request, "taxpayerCode");
        final String[] kpAccountNames = getStringValues(request, "kpAccountName");
        final String[] kpTels = getStringValues(request, "kpTel");
        if (null != kpNames && kpNames.length > 0) {
            for (int i = 0; i < kpNames.length; i++) {
                final SupplierInvoice supplierInvoice = new SupplierInvoice();
                supplierInvoice.setName(kpNames[i]);
                supplierInvoice.setBankName(kpBanks[i]);
                supplierInvoice.setBankAccount(kpAccounts[i]);
                supplierInvoice.setBankAccName(kpAccountNames[i]);
                supplierInvoice.setLinkAddr(kpAddresss[i]);
                supplierInvoice.setTaxpayerCode(taxpayerCodes[i]);
                supplierInvoice.setLinkPhone(kpTels[i]);
                supplierInvoice.setStatus(Constant.ENABLED.YES);
                supplierInvoice.setCreateUser(SupplierUtilAO.getCurrentUserName());
                supplierInvoice.setUpdateUser(supplierInvoice.getCreateUser());
                bList.add(supplierInvoice);
            }
        }
        supplieDTO.setSupplierInvoiceList(bList);
    }

    /***
     * <pre>
     * 生成海关备案号信息
     * </pre>
     *
     * @param supplieDTO
     * @param resultMap
     * @param request
     * @param needCheck
     */
    private void generateSupplierRecordation(final SupplierDTO supplieDTO, final Map<String, Object> resultMap, final HttpServletRequest request,
        final Boolean needCheck) {
        final List<SupplierCustomsRecordation> bList = new ArrayList<SupplierCustomsRecordation>();
        final Long[] customsChannelIds = getLongValues(request, "customsChannel");
        final String[] customsChannelNames = getStringValues(request, "customsChannelName");
        final String[] recordationNames = getStringValues(request, "recordationName");
        final String[] recordationNums = getStringValues(request, "recordationNum");
        if (null != customsChannelIds && customsChannelIds.length > 0) {
            for (int i = 0; i < customsChannelIds.length; i++) {
                final SupplierCustomsRecordation supplierCustomsRecordation = new SupplierCustomsRecordation();
                supplierCustomsRecordation.setCustomsChannelId(customsChannelIds[i]);
                supplierCustomsRecordation.setCustomsChannelName(customsChannelNames[i]);
                supplierCustomsRecordation.setRecordationName(recordationNames[i]);
                supplierCustomsRecordation.setRecordationNum(recordationNums[i]);
                supplierCustomsRecordation.setStatus(Constant.ENABLED.YES);
                supplierCustomsRecordation.setCreateUser(SupplierUtilAO.getCurrentUserName());
                supplierCustomsRecordation.setUpdateUser(supplierCustomsRecordation.getCreateUser());
                bList.add(supplierCustomsRecordation);
            }
        }
        supplieDTO.setSupplierCustomsRecordationList(bList);
    }

    /**
     * <pre>
     * 生成供应商联系人基本信息
     * </pre>
     *
     * @param supplierAO
     * @param resultMap
     * @param request
     */
    private void generateSupplierLinkInfo(final SupplierDTO supplierAO, final Map<String, Object> resultMap, final HttpServletRequest request,
        final Boolean needCheck) {
        // 分类列表
        final List<SupplierLink> supplierLinks = new ArrayList<SupplierLink>();
        final String[] suppLinkTypes = getStringValues(request, "suppLinkType");
        final String[] supplierLinkMobiles = getStringValues(request, "supplierLinkMobile");
        final String[] supplierLinkTels = getStringValues(request, "supplierLinkTel");
        final String[] supplierLinkAddrs = getStringValues(request, "supplierLinkAddr");
        final String[] supplierLinkNames = getStringValues(request, "supplierLinkName");
        final String[] supplierLinkEmails = getStringValues(request, "supplierLinkEmail");
        final String[] supplierLinkFaqs = getStringValues(request, "supplierLinkFaq");
        final String[] supplierLinkQQs = getStringValues(request, "supplierLinkQQ");
        if (null != suppLinkTypes && suppLinkTypes.length > 0) {
            for (int i = 0; i < suppLinkTypes.length; i++) {
                final SupplierLink supplierLink = new SupplierLink();
                supplierLink.setLinkName(supplierLinkNames[i]);
                supplierLink.setLinkType(suppLinkTypes[i]);
                supplierLink.setMobilePhone(supplierLinkMobiles[i]);
                supplierLink.setTelephone(supplierLinkTels[i]);
                supplierLink.setLinkAddress(supplierLinkAddrs[i]);
                supplierLink.setEmail(supplierLinkEmails[i]);
                supplierLink.setQq(supplierLinkQQs[i]);
                supplierLink.setFax(supplierLinkFaqs[i]);
                supplierLink.setStatus(Constant.ENABLED.YES);
                supplierLink.setCreateUser(SupplierUtilAO.getCurrentUserName());
                supplierLink.setUpdateUser(supplierLink.getCreateUser());
                supplierLinks.add(supplierLink);
            }
        }
        supplierAO.setSupplierLinkList(supplierLinks);
    }

    /**
     * <pre>
     * 生成西客联系人基本信息
     * </pre>
     *
     * @param supplierAO
     * @param resultMap
     * @param request
     */
    private void generateSupplierXgLinkInfo(final SupplierDTO supplierAO, final Map<String, Object> resultMap, final HttpServletRequest request,
        final Boolean needCheck) {
        // 分类列表
        final List<SupplierXgLink> supplierMtLinks = new ArrayList<SupplierXgLink>();
        final String[] xgLinkTypes = getStringValues(request, "xgLinkType");
        final String[] xgLinkerDeptIds = getStringValues(request, "xgLinkerDeptId");
        final String[] xgLinkerUserIds = getStringValues(request, "xgLinkerUserId");
        final String[] xgLinkerUserNames = getStringValues(request, "xgLinkerDeptName");
        final String[] xgLinkerDeptNames = getStringValues(request, "xgLinkerUserName");
        if (null != xgLinkTypes && xgLinkTypes.length > 0 && null != xgLinkerUserIds && xgLinkerUserIds.length > 0) {
            for (int i = 0; i < xgLinkTypes.length; i++) {
                if (null != xgLinkerUserIds && null != xgLinkerUserIds[i]) {
                    final SupplierXgLink xgLinker = new SupplierXgLink();
                    xgLinker.setDeptId(xgLinkerDeptIds[i]);
                    xgLinker.setUserId(xgLinkerUserIds[i]);
                    xgLinker.setLinkType(xgLinkTypes[i]);
                    xgLinker.setDeptName(xgLinkerUserNames[i]);
                    xgLinker.setUserName(xgLinkerDeptNames[i]);
                    xgLinker.setStatus(Constant.ENABLED.YES);
                    xgLinker.setCreateUser(SupplierUtilAO.getCurrentUserName());
                    xgLinker.setUpdateUser(xgLinker.getCreateUser());
                    supplierMtLinks.add(xgLinker);
                }
            }
        }
        supplierAO.setSupplierXgLinkList(supplierMtLinks);
    }

    /**
     * <pre>
     * 生成多图片的文件信息
     * </pre>
     *
     * @param supplierAttachDTO
     * @param resultMap
     * @param request
     */
    private void generateSupplierMulImgInfo(final SupplierAttach supplierAttachDTO, final Map<String, Object> resultMap, final HttpServletRequest request,
        final Boolean needCheck) {
        /** 特殊资质文件 */
        final String[] specialPapers = request.getParameterValues("specialPapers");
        final String[] specialPapersDesc = request.getParameterValues("specialPapersDesc");
        /** 产品质检报告 */
//        final String qualityLiscence = getStringValue(request, "qualityLiscence");
        /** 品牌授权证明 */
        final String[] brandLiscence = request.getParameterValues("brandLiscence");
        final String[] brandName = request.getParameterValues("brandName");
        final String[] brandDesc = request.getParameterValues("imageDesc");

//        supplierAttachDTO.setQualityLiscence(qualityLiscence);
        final List<SupplierImage> supplierImageList = new ArrayList<SupplierImage>();

        // 设置品牌
        if (null != brandLiscence && brandLiscence.length > 0) {
            for (int i = 0; i < brandLiscence.length; i++) {
                final SupplierImage spImage = new SupplierImage();
                spImage.setSupplierId(supplierAttachDTO.getSupplierId());
                spImage.setImageUrl(setBlankNull(brandLiscence[i]));
                spImage.setImageType(SupplierConstant.IMG_T_BRAND_LICEN);
                spImage.setBrandName(setBlankNull(brandName[i]));
                spImage.setDescription(setBlankNull(brandDesc[i]));
                spImage.setStatus(Constant.ENABLED.YES);
                spImage.setCreateUser(SupplierUtilAO.getCurrentUserName());
                spImage.setUpdateUser(spImage.getCreateUser());
                if( spImage.getName() == null ){
                	spImage.setName("");
                }
                supplierImageList.add(spImage);
            }

        }

        // 设置特殊资质
        if (null != specialPapers && specialPapers.length > 0) {
            for (int i = 0; i < specialPapers.length; i++) {
                final SupplierImage spImage = new SupplierImage();
                spImage.setSupplierId(supplierAttachDTO.getSupplierId());
                spImage.setImageUrl(setBlankNull(specialPapers[i]));
                spImage.setImageType(SupplierConstant.IMG_T_SPECIAL_PAPER);
                spImage.setDescription(setBlankNull(specialPapersDesc[i]));
                spImage.setStatus(Constant.ENABLED.YES);
                spImage.setCreateUser(SupplierUtilAO.getCurrentUserName());
                spImage.setUpdateUser(spImage.getCreateUser());
                if( spImage.getName() == null ){
                	spImage.setName("");
                }
                supplierImageList.add(spImage);
            }
        }
        supplierAttachDTO.setSupplierImageList(supplierImageList);
        resultMap.put(SupplierConstant.SUCCESS_KEY, true);
    }

    /**
     * 生成商家平台信息
     *
     * @param supplierDTO
     * @param resultMap
     * @param request
     * @param needCheck
     */
    public void generateSupplierUserInfo(final SupplierDTO supplierDTO, final Map<String, Object> resultMap, final HttpServletRequest request,
        final Boolean needCheck) {

        String userpassword = getStringValue(request, "sp_user_password");
        if ("商家平台密码".equals(userpassword)) {
            userpassword = null;
        }
        Integer userstatus = getIntValue(request, "status");
        if (null == userstatus) {
            userstatus = 1;
        }
        final SupplierUser supplierUserDTO = new SupplierUser();
        supplierUserDTO.setPassword(CommonUtil.toMD5(userpassword));
        supplierUserDTO.setStatus(userstatus);
        supplierUserDTO.setCreateTime(new Date());
        supplierUserDTO.setUpdateTime(new Date());
        supplierUserDTO.setCreateUser(SupplierUtilAO.getCurrentUserName());
        supplierUserDTO.setUpdateUser(supplierUserDTO.getCreateUser());
        supplierDTO.setSupplierUser(supplierUserDTO);
    }

    /**
     * <pre>
     * 从页面获取供应商的基本信息
     * </pre>
     *
     * @param request
     * @return
     */
    public Map<String, Object> genSupplierBaseInfo(final HttpServletRequest request, final Boolean needCheck) {
        final Map<String, Object> retMap = new HashMap<String, Object>();
        final SupplierDTO supplieDTO = new SupplierDTO();
        final Map<String, Object> resultMap = new HashMap<String, Object>();
        resultMap.put("createTime", new Date());
        resultMap.put("createUser", UserHandler.getUser().getLoginName());
        resultMap.put("userIp", "本地");
        resultMap.put("serverIp", CommonUtil.getIpAddress());
        /**
         * 生成供应商基本信息
         */
        // do
        generateSupplierBaseInfo(supplieDTO, resultMap, request, needCheck);
        if (!checkResult(resultMap)) {
            return setResult(resultMap);
        }
        // do
        generateSupplierBankInfo(supplieDTO, resultMap, request, needCheck);
        if (!checkResult(resultMap)) {
            return setResult(resultMap);
        }
        // do
        generateSupplierLinkInfo(supplieDTO, resultMap, request, needCheck);
        if (!checkResult(resultMap)) {
            return setResult(resultMap);
        }
        generateSupplierCategoryInfo(supplieDTO, resultMap, request, needCheck);
        if (!checkResult(resultMap)) {
            return setResult(resultMap);
        }
        // do
        generateSupplierXgLinkInfo(supplieDTO, resultMap, request, needCheck);
        if (!checkResult(resultMap)) {
            return setResult(resultMap);
        }

        generateSupplierKpBankInfo(supplieDTO, resultMap, request, needCheck);
        if (!checkResult(resultMap)) {
            return setResult(resultMap);
        }

        generateSupplierRecordation(supplieDTO, resultMap, request, needCheck);
        if (!checkResult(resultMap)) {
            return setResult(resultMap);
        }

        generateSupplierUserInfo(supplieDTO, resultMap, request, needCheck);
        if (!checkResult(resultMap)) {
            return setResult(resultMap);
        }
        retMap.put(SupplierConstant.DATA_KEY, supplieDTO);
        retMap.put(SupplierConstant.SUCCESS_KEY, true);
        return retMap;
    }

    /**
     * <pre>
     * 生成供应商的附件信息
     * </pre>
     *
     * @param request
     * @param spId
     * @return
     */
    public Map<String, Object> genSupplierLicenInfo(final HttpServletRequest request, final Long spId, final Boolean needCheck) {
        final SupplierAttach supplierAttachDTO = new SupplierAttach();
        final Map<String, Object> resultMap = new HashMap<String, Object>();
        supplierAttachDTO.setSupplierId(spId);
        /**
         * 生成供应商基本信息
         */
        generateSupplierBaseLicenInfo(supplierAttachDTO, resultMap, request, needCheck);
        if (!checkResult(resultMap)) {
            return setResult(resultMap);
        }
        /**
         * 设置多图片的特殊字段
         */
        generateSupplierMulImgInfo(supplierAttachDTO, resultMap, request, needCheck);
        if (!checkResult(resultMap)) {
            return setResult(resultMap);
        }
        resultMap.put(SupplierConstant.DATA_KEY, supplierAttachDTO);
        resultMap.put(SupplierConstant.SUCCESS_KEY, true);
        return resultMap;
    }

    /**
     * <pre>
     * 更新供应商基本信息和附件信息
     * </pre>
     *
     * @param supplierDTO
     * @param supplierAttachDTO
     * @return
     */
    public Map<String, Object> updateSupplierInfo(final SupplierDTO supplierDTO, final SupplierAttach supplierAttachDTO) {
        final Map<String, Object> retMap = new HashMap<String, Object>();
        final SupplierAttach spDO = new SupplierAttach();
        spDO.setSupplierId(supplierDTO.getId());
        spDO.setStatus(Constant.ENABLED.YES);
        try {
            /*
             * BeanUtils.copyProperties(supplierDO, supplierDTO); BeanUtils.copyProperties(attachDO,
             * supplierAttachDTO);
             */
            supplierInfoService.updateSupplierInfo(supplierDTO);
            supplierAttachService.updateAttachInfoBySupplierId(supplierAttachDTO);
            retMap.put(SupplierConstant.SUCCESS_KEY, true);
        } catch (final Exception e) {
            LOGGER.error(e.getMessage(), e);
            retMap.put(SupplierConstant.SUCCESS_KEY, false);
            retMap.put(SupplierConstant.MESSAGE_KEY, "保存信息出错，可能部分信息没有更新成功。");
        }
        return retMap;
    }

    /**
     * 获取供应商分类信息
     *
     * @param spId
     * @param cid
     * @param cType
     * @return
     */
    public List<Category> getSupplierCategory(final Long spId, final Long brandId, final Long cid, final String cType) {
        final SupplierCategory supplierCategory = new SupplierCategory();
        final List<Category> retList = new ArrayList<Category>();
        supplierCategory.setSupplierId(spId);
        supplierCategory.setStatus(Constant.ENABLED.YES);
        if ("0".equals(cType)) {
            supplierCategory.setBrandId(cid);
        } else if ("1".equals(cType)) {
            supplierCategory.setBrandId(brandId);
            supplierCategory.setCategoryBigId(cid);
        } else {
            supplierCategory.setBrandId(brandId);
            supplierCategory.setCategoryMidId(cid);
        }
        final List<SupplierCategory> supplierCategoryList = supplierCategoryService.queryByObject(supplierCategory);
        if (null != supplierCategoryList && supplierCategoryList.size() > 0) {
            if ("0".equals(cType)) {
                final Set<Long> cSet = new HashSet<Long>();
                for (final SupplierCategory spCategory : supplierCategoryList) {
                    if (null != spCategory.getCategoryBigId() && !cSet.contains(spCategory.getCategoryBigId())) {
                        final Category categoryDO = new Category();
                        categoryDO.setId(spCategory.getCategoryBigId());
                        categoryDO.setName(spCategory.getCategoryBigName());
                        
                        retList.add(categoryDO);
                    }
                    cSet.add(spCategory.getCategoryBigId());
                }
            } else if ("1".equals(cType)) {
                final Set<Long> cSet = new HashSet<Long>();
                for (final SupplierCategory spCategory : supplierCategoryList) {
                    if (null != spCategory.getCategoryMidId() && !cSet.contains(spCategory.getCategoryMidId())) {
                        final Category categoryDO = new Category();
                        categoryDO.setId(spCategory.getCategoryMidId());
                        categoryDO.setName(spCategory.getCategoryMidName());
                        retList.add(categoryDO);
                    }
                    cSet.add(spCategory.getCategoryMidId());
                }
            } else if ("2".equals(cType)) {
                final Set<Long> cSet = new HashSet<Long>();
                for (final SupplierCategory spCategory : supplierCategoryList) {
                    if (null != spCategory.getCategorySmallId() && !cSet.contains(spCategory.getCategorySmallId())) {
                        final Category categoryDO = new Category();
                        categoryDO.setId(spCategory.getCategorySmallId());
                        categoryDO.setName(spCategory.getCategorySmallName());
                        retList.add(categoryDO);
                    }
                    cSet.add(spCategory.getCategorySmallId());
                }
            }

        }
        return retList;
    }

    public boolean checkResult(Map<String,Object> resultMap){
        try {
            return null != resultMap && null != resultMap.get(SupplierConstant.SUCCESS_KEY)
                    && (Boolean)resultMap.get(SupplierConstant.SUCCESS_KEY);
        } catch(Exception e){
            return false;
        }
    }  

}
