package com.tp.proxy.ptm;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.vo.PageInfo;
import com.tp.dto.ptm.remote.Account4DetailDTO;
import com.tp.dto.ptm.remote.Account4ListDTO;
import com.tp.dto.ptm.remote.AccountQO;
import com.tp.model.ptm.PlatformAccount;
import com.tp.model.sup.SupplierInfo;
import com.tp.proxy.BaseProxy;
import com.tp.result.sup.SupplierResult;
import com.tp.service.IBaseService;
import com.tp.service.ptm.IPlatformAccountService;
import com.tp.service.sup.IPurchasingManagementService;
/**
 * 开放平台账户表代理层
 * @author szy
 *
 */
@Service
public class PlatformAccountProxy extends BaseProxy<PlatformAccount>{

	@Autowired
	private IPlatformAccountService platformAccountService;

	@Override
	public IBaseService<PlatformAccount> getService() {
		return platformAccountService;
	}
	


    private static final Logger log = LoggerFactory.getLogger(PlatformAccountProxy.class);
    // 验证 供应商集合 正则表达式
    private static final String REG_VALUE = "^(\\d+,)*(\\d+\\s*)$";

    /**
     * 开放平台账户管理 Service
     */

    @Autowired
    private IPurchasingManagementService purchasingManagementService;

    public PageInfo<Account4ListDTO> findAccountBackendPage(final PlatformAccount qo) {

        if (null == qo) {
            return null;
        }

        return platformAccountService.findAccount4ListDTOByQOWithPage(qo);
    }

    /**
     * 创建开放平台账户
     *
     * @param account
     * @param supplier
     * @return PlatformAccount
     */
    public PlatformAccount createAccount(final PlatformAccount account, final String supplier) {
        if (null == account) {
            throw new RuntimeException("account is null...");
        }
        /**
         * 验证格式 和 数据转换
         */
        List<SupplierInfo> supplierList = this.verifyAndConvert(supplier);

        return platformAccountService.createAccount(account, supplierList);
    }

    private interface Callable<V> {
        V call(Long val);
    }

    /**
     * 验证 和 数据转换
     *
     * @param account
     * @param supplier
     * @return
     */
    private <T> List<T> verifyAndConvert(final String supplier, final Callable<T> callable) {
        if (StringUtils.isEmpty(supplier)) {
            throw new RuntimeException("supplier is null...");
        }
        /**
         * 验证格式是否正确
         */
        Matcher mc = Pattern.compile(REG_VALUE).matcher(supplier);
        if (mc.matches() == false) {
            throw new RuntimeException("supplier format is wrong...");
        }
        List<T> list = new ArrayList<T>();
        for (String val : supplier.split(",")) {
            list.add(callable.call(Long.valueOf(val.replaceAll("\\s", ""))));
        }
        return list;
    }

    /**
     * 验证 和 数据转换
     *
     * @param account
     * @param supplier
     * @return
     */
    private List<SupplierInfo> verifyAndConvert(final String supplier) {
        return verifyAndConvert(supplier, new Callable<SupplierInfo>() {
            @Override
            public SupplierInfo call(final Long val) {
                SupplierInfo sup = new SupplierInfo();
                sup.setId(val);
                return sup;
            }
        });
    }

    /**
     * 更新开放平台账户， 只允许修改对接方名称和供应商关联
     *
     * @param account
     * @param supplier
     */
    public void updateAccount(final PlatformAccount account, final String supplier) {
        if (null == account) {
            throw new RuntimeException("account is null...");
        }
        /**
         * 验证格式 和 数据转换
         */
        List<SupplierInfo> supplierList = this.verifyAndConvert(supplier);

        platformAccountService.updateAccount(account, supplierList);
    }

    /**
     * 根据appkey查询账户详情
     *
     * @param appkey
     * @return
     */
    public Account4DetailDTO findAccount4DetailDTOByAppkey(final String appkey) {
        if (null == appkey) {
            return null;
        }
        return platformAccountService.findAccount4DetailDTOByAppkey(appkey);
    }

    /**
     * 根据appkey逻辑删除开放平台账户
     *
     * @param appkey
     */
    public void deteleAccount(final String appkey) {
        if (null == appkey) {
            return;
        }
        platformAccountService.deteleAccount(appkey);
    }

    /**
     * 根据编号集合查询编号信息
     *
     * @param supplier
     * @return List<SupplierInfo>
     */
    public List<SupplierInfo> findSupplierList(final String supplier) {
        List<Long> list = this.verifyAndConvert(supplier, new Callable<Long>() {
            @Override
            public Long call(final Long val) {
                return val;
            }
        });

        try {
            SupplierResult result = purchasingManagementService.getSuppliersByIds(list);
            return result.getSupplierInfoList();
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }


}
