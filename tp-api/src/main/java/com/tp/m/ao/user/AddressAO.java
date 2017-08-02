package com.tp.m.ao.user;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.dto.common.ResultInfo;
import com.tp.exception.UserServiceException;
import com.tp.m.base.MResultVO;
import com.tp.m.constant.UserConstant;
import com.tp.m.convert.AddressConvert;
import com.tp.m.enums.MResultInfo;
import com.tp.m.exception.MobileException;
import com.tp.m.query.user.QueryAddress;
import com.tp.m.vo.user.AddressDetailVO;
import com.tp.model.mem.ConsigneeAddress;
import com.tp.proxy.mem.ConsigneeAddressProxy;
import com.tp.util.Base64;

/**
 * 用户 - 收货地址业务层
 *
 * @author zhuss
 * @2016年1月3日 下午4:58:54
 */
@Service
public class AddressAO {

    private static final Logger log = LoggerFactory.getLogger(AddressAO.class);

    @Autowired
    private ConsigneeAddressProxy consigneeAddressProxy;

    /**
     * 用户 -收货地址列表
     *
     * @param userId
     * @return
     */
    public MResultVO<List<AddressDetailVO>> getAddressList(Long userId) {
        try {
            List<ConsigneeAddress> addresslist = consigneeAddressProxy.findByUserId(userId, UserConstant.DEFAULT_ADDRESS_SIZE);
            if (null != addresslist)
                return new MResultVO<>(MResultInfo.SUCCESS, AddressConvert.convertAddresslist(addresslist));
            return new MResultVO<>(MResultInfo.SUCCESS);
        } catch (MobileException e) {
            log.error("[API接口 - 收货地址列表 MobileException]={}", e.getMessage());
            return new MResultVO<>(e);
        } catch (Exception ex) {
            log.error("[API接口 - 收货地址列表 Exception] = {}", ex);
            return new MResultVO<>(MResultInfo.SYSTEM_ERROR);
        }
    }

    /**
     * 用户 - 默认收货地址
     *
     * @param userId
     * @return
     */
    public MResultVO<AddressDetailVO> defaultAddress(Long userId) {
        try {
            ConsigneeAddress address = consigneeAddressProxy.getDefaultAddress(userId);
            if (null != address)
                return new MResultVO<>(MResultInfo.SUCCESS, AddressConvert.convertAddress(address, true));
            return new MResultVO<>(MResultInfo.SUCCESS);
        } catch (MobileException e) {
            log.error("[API接口 - 获取用户默认收货地址 MobileException]={}", e.getMessage());
            return new MResultVO<>(e);
        } catch (Exception ex) {
            log.error("[API接口 - 获取用户默认收货地址 Exception] = {}", ex);
            return new MResultVO<>(MResultInfo.SYSTEM_ERROR);
        }
    }

    /**
     * 用户 -收货地址 - 更新(添加+编辑)
     *
     * @param address
     * @return
     */
    public MResultVO<AddressDetailVO> modifyAddress(QueryAddress address) {
        try {
        	//修改并且包含*号时，拉取DB数据对比
            if (NumberUtils.isNumber(address.getAid()) && StringUtils.isNotBlank(address.getIdentityCard()) && address.getIdentityCard().contains("*")) {
                if (address.getIdentityCard().trim().length() < 15)
                    throw new MobileException(MResultInfo.ID_NO_VALID);
                ResultInfo<ConsigneeAddress> resultInfo = consigneeAddressProxy.queryById(Long.parseLong(address.getAid()));
                if (!resultInfo.isSuccess() || resultInfo.getData() == null)
                    throw new MobileException(MResultInfo.MODIFY_FAILED);
                if (StringUtils.isBlank(resultInfo.getData().getIdentityCard()))
                    throw new MobileException(MResultInfo.ID_NO_VALID);
                String idCardFromDB = new String(Base64.decode(resultInfo.getData().getIdentityCard()));
                if (address.getIdentityCard().length() != idCardFromDB.length() ||
                        ! idCardFromDB.replaceAll("^(\\d{4})(.+)(\\d{4}.)$", "$1*********$3").equals(address.getIdentityCard())) {
                    throw new MobileException(MResultInfo.ID_NO_VALID);
                } else {
                    address.setIdentityCard(idCardFromDB);
                }
            }
            ConsigneeAddress ca = consigneeAddressProxy.save(AddressConvert.convertModifyAddress(address));
            return new MResultVO<>(MResultInfo.OPERATION_SUCCESS, AddressConvert.convertAddress(ca,true));
        } catch (MobileException e) {
            log.error("[API接口 - 操作用户收货地址 MobileException]={}", e.getMessage());
            return new MResultVO<>(e);
        } catch (UserServiceException ue) {
            log.error("[API接口 - 操作用户收货地址 UserServiceException]={}", ue.getMessage());
            return new MResultVO<>(ue.getMessage());
        } catch (Exception ex) {
            log.error("[API接口 - 操作用户收货地址 Exception] = {}", ex);
            return new MResultVO<>(MResultInfo.MODIFY_FAILED);
        }
    }

    /**
     * 用户 -收货地址 - 删除
     *
     * @return
     */
    public MResultVO<MResultInfo> delAddress(Long aid, Long uid) {
        try {
            int rel = consigneeAddressProxy.removeConsigneeAddress(aid, uid);
            if (rel > 0) return new MResultVO<>(MResultInfo.DEL_SUCCESS);
            return new MResultVO<>(MResultInfo.DEL_FAILED);
        } catch (MobileException e) {
            log.error("[API接口 - 删除用户收货地址 MobileException]={}", e.getMessage());
            return new MResultVO<>(e);
        } catch (Exception ex) {
            log.error("[API接口 - 删除用户收货地址 Exception] = {}", ex);
            return new MResultVO<>(MResultInfo.DEL_FAILED);
        }
    }


}
