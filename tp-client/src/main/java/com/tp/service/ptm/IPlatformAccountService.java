package com.tp.service.ptm;

import java.util.List;
import java.util.Map;

import com.tp.common.vo.PageInfo;
import com.tp.dto.ptm.remote.Account4DetailDTO;
import com.tp.dto.ptm.remote.Account4ListDTO;
import com.tp.dto.ptm.salesorder.OrderMiniDTO;
import com.tp.model.ptm.PlatformAccount;
import com.tp.model.sup.SupplierInfo;
import com.tp.service.IBaseService;
/**
  * @author szy
  * 开放平台账户表接口
  */
public interface IPlatformAccountService extends IBaseService<PlatformAccount>{

	/**
	 * @param appKey
	 * @return
	 */
	String selectTokenByAppkey(String appKey);
	public enum KeyType {

        /** 成功 */
        SUCCESS,

        /** 失败 */
        FAILURE,

        /**
         * <code>NOT_EXIST</code> - {订单不存在}.
         */
        NOT_EXIST;
    }
	/**
	 * @param appkey
	 * @param orderNoList
	 * @return
	 */
	Map<KeyType, List<OrderMiniDTO>> verifySalesOrderAuthOfAccount(String appkey, List<Long> orderNoList);
	/**
	 * @param qo
	 * @return
	 */
	PageInfo<Account4ListDTO> findAccount4ListDTOByQOWithPage(PlatformAccount qo);
	/**
	 * @param account
	 * @param supplierList
	 * @return
	 */
	PlatformAccount createAccount(PlatformAccount account, List<SupplierInfo> supplierList);
	/**
	 * @param account
	 * @param supplierList
	 */
	void updateAccount(PlatformAccount account, List<SupplierInfo> supplierList);
	/**
	 * @param appkey
	 * @return
	 */
	Account4DetailDTO findAccount4DetailDTOByAppkey(String appkey);
	/**
	 * @param appkey
	 */
	void deteleAccount(String appkey);
}
