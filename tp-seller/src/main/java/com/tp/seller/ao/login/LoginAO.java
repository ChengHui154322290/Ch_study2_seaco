package com.tp.seller.ao.login;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.vo.Constant;
import com.tp.common.vo.supplier.entry.AuditStatus;
import com.tp.model.sup.SupplierInfo;
import com.tp.model.sup.SupplierUser;
import com.tp.seller.constant.CommonUtils;
import com.tp.service.sup.ISupplierInfoService;
import com.tp.service.sup.ISupplierUserService;

/**
 * @author yfxie
 */
@Service
public class LoginAO {

    private static final Logger LOGGER = LoggerFactory.getLogger(LoginAO.class);

    @Autowired
    private ISupplierUserService supplierUserService;

    @Autowired
    private ISupplierInfoService supplierInfoService;

    /** 供应商信息有效 */
    public static final Integer IS_TRUE = 0;
    /** 账户被禁用 */
    public static final Integer IS_DISABLED = 4;
    /** 无供应商信息 */
    public static final Integer NO_SUPPLIER = 1;
    /** 查出多个供应商 */
    public static final Integer TO_MANY_RESULTS = 2;
    /** 供应商状态不对 */
    public static final Integer SUPPLIER_STATUS = 3;

    /**
     * 校验用户信息
     *
     * @param uname
     * @param pwd
     * @return
     */
    public int checkUser(final String uname, final String pwd) {
        final String pwdEn = CommonUtils.toMD5(pwd);
        final SupplierUser supplierUserDO = new SupplierUser();
        // supplierUserDO.setUserName(uname);
        supplierUserDO.setLoginName(uname);
        supplierUserDO.setPassword(pwdEn);
        // supplierUserDO.setStatus(1);
        final List<SupplierUser> userDO = supplierUserService.queryByObject(supplierUserDO);
        final int len = userDO.size();
        if (len == 1) {
            if (!new Integer(1).equals(userDO.get(0).getStatus())) {
                return IS_DISABLED;
            }
            final Long supplierId = userDO.get(0).getSupplierId();
            final SupplierInfo supplierDO = supplierInfoService.queryById(supplierId);
            if (null == supplierDO) {
                return NO_SUPPLIER;
            }
            // 审核状态
            if (!AuditStatus.THROUGH.getStatus().equals(supplierDO.getAuditStatus())) {
                return SUPPLIER_STATUS;
            }
            return IS_TRUE;
        } else if (len > 1) {
            LOGGER.error("用户名{}查询出多个用户信息", uname);
            return TO_MANY_RESULTS;
        } else {
            return NO_SUPPLIER;
        }
    }

    /**
     * 获取用户id信息
     *
     * @return
     */
    public SupplierUser getUserInfo(final String uname) {
        final SupplierUser supplierUserDO = new SupplierUser();
        supplierUserDO.setLoginName(uname);
        supplierUserDO.setStatus(1);
        final List<SupplierUser> userDO = supplierUserService.queryByObject(supplierUserDO);
        if (userDO.size() == 1) {
            return userDO.get(0);
        }
        return null;
    }

    /**
     * 修改用户密码
     */
    public boolean updatePassword(final String userName, final String pwd) {
        final SupplierUser supplierUserDO = getUserInfo(userName);
        boolean result = false;
        if (supplierUserDO != null) {
            final Long id = supplierUserDO.getId();
            if (id != null) {
                final SupplierUser supplierUserDO2 = new SupplierUser();
                final String pwdEn = CommonUtils.toMD5(pwd);
                supplierUserDO2.setId(id);
                supplierUserDO2.setPassword(pwdEn);
                final int result1 = supplierUserService.updateNotNullById(supplierUserDO2);
                if (result1 > 0) {
                    result = true;
                }
            }
        }
        return result;
    }

    /**
     * 判断是否是海淘供应商
     *
     * @param supplierId
     * @return
     */
    public Boolean isHaitaoSupplier(final Long supplierId) {
        if (null == supplierId) {
            return false;
        }
        final SupplierInfo supplierDO = supplierInfoService.queryById(supplierId);
        if (null == supplierDO) {
            return false;
        }
        if (null != supplierDO.getIsSea() && Constant.ENABLED.YES.equals(supplierDO.getIsSea())) {
            return true;
        } else {
            return false;
        }
    }
}
