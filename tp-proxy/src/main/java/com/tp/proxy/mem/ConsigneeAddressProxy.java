package com.tp.proxy.mem;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.tp.common.vo.Constant;
import com.tp.common.vo.mem.ErrorCode;
import com.tp.common.vo.mem.MemberConstant;
import com.tp.dto.map.LngLatResult;
import com.tp.exception.UserServiceException;
import com.tp.model.mem.ConsigneeAddress;
import com.tp.proxy.BaseProxy;
import com.tp.proxy.map.MapAPIProxy;
import com.tp.service.IBaseService;
import com.tp.service.mem.IConsigneeAddressService;
import com.tp.util.StringUtil;
import com.tp.validate.util.IdcardValidator;

/**
 * 用户收货地址表代理层
 * 
 * @author szy
 *
 */
@Service
public class ConsigneeAddressProxy extends BaseProxy<ConsigneeAddress> {

	@Autowired
	private IConsigneeAddressService consigneeAddressService;
	@Autowired
	private MapAPIProxy mapAPIProxy;
	@Override
	public IBaseService<ConsigneeAddress> getService() {
		return consigneeAddressService;
	}

	@Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, readOnly = false)
	public void setDefaultAddress(Long userId, Long consigneeId) throws UserServiceException {
		try {
			ConsigneeAddress con = new ConsigneeAddress();
			con.setUserId(userId);
			con.setState(true);
			con.setIsDefault(false);
			logger.debug("[begin]batch update user(" + userId + ")consigneeAddress status to unDefault");
			this.consigneeAddressService.updateIsdefaultByUserId(con);
			logger.debug("[end]batch update user(" + userId + ")consigneeAddress status to unDefault");

			ConsigneeAddress defaultCon = new ConsigneeAddress();
			defaultCon.setId(consigneeId);
			defaultCon.setIsDefault(true);
			logger.debug("[begin]set user(" + userId + ")consigneeAddress(" + consigneeId + ")status to default");
			this.consigneeAddressService.updateNotNullById(defaultCon);
			logger.debug("[end]set user(" + userId + ")consigneeAddress(" + consigneeId + ")status to default");
		} catch (Exception e) {
			logger.error("[fail]set user(" + userId + ")consigneeAddress(" + consigneeId
					+ ")status to default.exception:" + e.getMessage());
			throw new UserServiceException(e.getMessage());
		}
	}
	
	public void cleanAddressDefault(Long userId) throws UserServiceException {
		try {
			ConsigneeAddress con = new ConsigneeAddress();
			con.setUserId(userId);
			con.setState(true);
			con.setIsDefault(false);
			logger.debug("[begin]batch update user(" + userId + ")consigneeAddress status to unDefault");
			this.consigneeAddressService.updateIsdefaultByUserId(con);
			logger.debug("[end]batch update user(" + userId + ")consigneeAddress status to unDefault");
		} catch (Exception e) {
			logger.error("[fail]set user(" + userId + ")status to default.exception:" + e.getMessage());
			throw new UserServiceException(e.getMessage());
		}
	}
	
	public ConsigneeAddress save(ConsigneeAddress con) throws UserServiceException{
		try {
			if(StringUtil.isNull(con.getUserId())) throw new UserServiceException(ErrorCode.CONSIGNEE_USERID_IS_NULL.code,ErrorCode.CONSIGNEE_USERID_IS_NULL.value);
			if(null == con.getIsDefault())con.setIsDefault(false);
			//如果要设置成默认地址 - 则把用户下所有地址设成false
			if(con.getIsDefault() != null && con.getIsDefault())cleanAddressDefault(con.getUserId());
			if(null == con.getId()){//新增
				if(StringUtil.isNullOrEmpty(con.getName())) throw new UserServiceException(ErrorCode.CONSIGNEE_USERNAME_IS_NULL.code,ErrorCode.CONSIGNEE_USERNAME_IS_NULL.value);
				if(con.getName().length() > 20) throw new UserServiceException(ErrorCode.CONSIGNEE_ADDRESS_NAME_LENGHT_ERROR.code,ErrorCode.CONSIGNEE_ADDRESS_NAME_LENGHT_ERROR.value);
				if(StringUtil.isNullOrEmpty(con.getMobile())) throw new UserServiceException(ErrorCode.CONSIGNEE_MOBILE_IS_NULL.code,ErrorCode.CONSIGNEE_MOBILE_IS_NULL.value);
				if(StringUtil.isNull(con.getProvinceId())) throw new UserServiceException(ErrorCode.CONSIGNEE_PROVINCE_IS_NULL.code,ErrorCode.CONSIGNEE_PROVINCE_IS_NULL.value);
				if(StringUtil.isNull(con.getCityId())) throw new UserServiceException(ErrorCode.CONSIGNEE_CITY_IS_NULL.code,ErrorCode.CONSIGNEE_CITY_IS_NULL.value);
				if(StringUtil.isNull(con.getCountyId())) throw new UserServiceException(ErrorCode.CONSIGNEE_COUNTY_IS_NULL.code,ErrorCode.CONSIGNEE_COUNTY_IS_NULL.value);
				if(StringUtil.isNullOrEmpty(con.getAddress())) throw new UserServiceException(ErrorCode.CONSIGNEE_ADDRESS_IS_NULL.code,ErrorCode.CONSIGNEE_ADDRESS_IS_NULL.value);
				//if(StringUtil.isNotBlank(con.getIdentityCard()) && new IdcardValidator().isValidatedAllIdcard(con.getIdentityCard())==false)throw new UserServiceException(ErrorCode.CONSIGNEE_ADDRESS_IDCARD_ERROR.code,ErrorCode.CONSIGNEE_ADDRESS_IDCARD_ERROR.value);
				//countCon.setUserId(con.getUserId());
				con.setState(true);
				ConsigneeAddress isOne = new ConsigneeAddress();
				isOne.setUserId(con.getUserId());
				isOne.setState(true);
				Integer count = this.consigneeAddressService.queryByObjectCount(isOne);
				if(MemberConstant.ConsigneeAddress.MaxCount == count) throw new UserServiceException(ErrorCode.CONSIGNEE_ADDRESS_MAX_FIVE.code,ErrorCode.CONSIGNEE_ADDRESS_MAX_FIVE.value);
				
				Map<String,Object> params = new HashMap<String,Object>();
				params.put("userId", con.getUserId());
				params.put("isDefault", Boolean.TRUE);
				Integer trueCount = consigneeAddressService.queryByParamCount(params);
				
				if(MemberConstant.ConsigneeAddress.NoneCoount == count || trueCount == 0) con.setIsDefault(true);//如果用户还未添加过收录货地址,那么第一个收货地址被设置为默认收货地址
				
				con.setCreateTime(new Date());
				con.setUpdateTime(new Date());
				con.setState(true);
				setLngLat(con);
				logger.debug("[begin]insert consigneeAddress"+con.toString());
				ConsigneeAddress result = this.consigneeAddressService.insert(con);
				return  result;
				
			}else{//修改
				if(con.getIdentityCard()!=null && con.getIdentityCard().contains("****")){
					con.setIdentityCard(null);
				}
				if(StringUtil.isNotBlank(con.getIdentityCard()) && new IdcardValidator().isValidatedAllIdcard(con.getIdentityCard())==false)throw new UserServiceException(ErrorCode.CONSIGNEE_ADDRESS_IDCARD_ERROR.code,ErrorCode.CONSIGNEE_ADDRESS_IDCARD_ERROR.value);
				con.setUpdateTime(new Date());
				setLngLat(con);
				Integer returnValue = this.consigneeAddressService.updateNotNullById(con);
				logger.debug("[begin]update consigneeAddress"+con.toString());
				if(returnValue > 0) {
					return con;
				}
				else throw new UserServiceException(ErrorCode.CONSIGNEE_UPDATE_FAIL.code,ErrorCode.CONSIGNEE_UPDATE_FAIL.value);
			}
			
		} catch (Exception e) {
			logger.error("[fail]update consigneeAddress"+con.toString()+".exception:"+e.getMessage());
			throw new UserServiceException(e.getMessage());
		} 
	}
	
	public List<ConsigneeAddress> findByUserId(Long userId, Integer resultCount) throws UserServiceException {
		try {
			return consigneeAddressService.findByUserIdOrderLimit(userId,resultCount);
		}catch(Exception e){
			logger.error(e.getMessage(), e);
            throw new UserServiceException(e.getMessage());
		}
	}

	public List<String> getAddressList(List<ConsigneeAddress> dtos) throws UserServiceException {
		List<String> addresses = new ArrayList<String>();
		if(dtos == null || dtos.size() == 0)
			return addresses;
		for(ConsigneeAddress dto : dtos) {
			StringBuilder sb = new StringBuilder();
			if(dto.getProvince() != null)
				sb.append(dto.getProvince()).append("&nbsp;");
			if(dto.getCity() != null)
				sb.append(dto.getCity()).append("&nbsp;");
			if(dto.getCounty() != null)
				sb.append(dto.getCounty()).append("&nbsp;");
			if(dto.getStreet() != null)
				sb.append(dto.getStreet()).append("&nbsp;");
			if(dto.getAddress() != null)
				sb.append(dto.getAddress()).append("&nbsp;");
			addresses.add(sb.toString());
		}
		return addresses;
	}


	public Integer removeConsigneeAddress(Long id, Long userId){
		try {		
			ConsigneeAddress con = new ConsigneeAddress();
			con.setId(id);
			con.setUserId(userId);
			con.setState(MemberConstant.Bool.FALSE);
			logger.debug("[begin]remove user consigneeAddress:"+con.toString());
			return consigneeAddressService.updateNotNullById(con);
		}catch(Exception e){
			logger.error("[fail]remove user consigneeAddress:"+id+".exception:"+e.getMessage());
	        throw new UserServiceException(e.getMessage());
		}
	}
	public ConsigneeAddress getDefaultAddress (Long userId){
		return consigneeAddressService.getDefaultAddress(userId);
	}
	
	public ConsigneeAddress setLngLat(ConsigneeAddress consigneeAddress){
		LngLatResult result = mapAPIProxy.geocode(consigneeAddress.getCounty()+consigneeAddress.getStreet()+consigneeAddress.getAddress(),consigneeAddress.getCity());
		if(result.getStatus()==1 && CollectionUtils.isNotEmpty(result.getGeocodes())){
			String destination = result.getGeocodes().get(0).getLocation();
			String[] deliveryLngLat = destination.split(Constant.SPLIT_SIGN.COMMA);
			consigneeAddress.setLongitude(deliveryLngLat[0]);
			consigneeAddress.setLatitude(deliveryLngLat[1]);
		}
		return consigneeAddress;
	}
}
