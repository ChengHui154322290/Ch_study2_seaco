package com.tp.service.mem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.tp.common.dao.BaseDao;
import com.tp.dao.mem.ConsigneeAddressDao;
import com.tp.exception.UserServiceException;
import com.tp.model.mem.ConsigneeAddress;
import com.tp.service.BaseService;
import com.tp.service.mem.IConsigneeAddressService;
import com.tp.util.Base64;
import com.tp.util.StringUtil;

@Service
public class ConsigneeAddressService extends BaseService<ConsigneeAddress> implements IConsigneeAddressService {

	@Autowired
	private ConsigneeAddressDao consigneeAddressDao;
	
	@Override
	public BaseDao<ConsigneeAddress> getDao() {
		return consigneeAddressDao;
	}
	
	@Override
	public ConsigneeAddress insert(ConsigneeAddress consigneeAddress){
		return super.insert(encode(consigneeAddress));
	}
	
	@Override
	public int updateNotNullById(ConsigneeAddress consigneeAddress){
		return super.updateNotNullById(encode(consigneeAddress));
	}
	@Override
	public Long updateIsdefaultByUserId(ConsigneeAddress consigneeAddress){
		return consigneeAddressDao.updateIsdefaultByUserId(consigneeAddress);
	}
	@Override
	public List<ConsigneeAddress> findByUserIdOrderLimit(Long userId, Integer resultCount) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("userId", userId);
		params.put("resultCount", resultCount);
		return decode(consigneeAddressDao.findByUserIdOrderLimit(params));
	}
	@Transactional(propagation=Propagation.REQUIRED,isolation=Isolation.DEFAULT,readOnly=false)
	@Override
	public void setDefaultAddress(Long userId,Long consigneeId) throws UserServiceException{
		try {
			ConsigneeAddress con = new ConsigneeAddress();
			con.setUserId(userId);
			con.setState(Boolean.TRUE);
			con.setIsDefault(Boolean.FALSE);
			logger.debug("[begin]batch update user("+userId+")consigneeAddress status to unDefault");
			this.consigneeAddressDao.updateIsdefaultByUserId(con);
			logger.debug("[end]batch update user("+userId+")consigneeAddress status to unDefault");
			
			ConsigneeAddress defaultCon = new ConsigneeAddress();
			defaultCon.setId(consigneeId);
			defaultCon.setIsDefault(Boolean.TRUE);
			logger.debug("[begin]set user("+userId+")consigneeAddress("+consigneeId+")status to default");
			this.consigneeAddressDao.updateNotNullById(defaultCon);
			logger.debug("[end]set user("+userId+")consigneeAddress("+consigneeId+")status to default");
			
		} catch (Exception e) {
			logger.error("[fail]set user("+userId+")consigneeAddress("+consigneeId+")status to default.exception:"+e.getMessage());
			throw new UserServiceException(e.getMessage());
		}
	}
	@Override
	public List<ConsigneeAddress> findByUserId(Long id, Integer resultCount) {
		try {
			return findByUserIdOrderLimit(id,resultCount);
		}catch(Exception e){
			logger.error(e.getMessage(), e);
            throw new UserServiceException(e.getMessage());
		}
	}

	@Override
	public List<String> getAddressList(List<ConsigneeAddress> dtos) {
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
	@Override
	public ConsigneeAddress getDefaultAddress(Long userId) {
		ConsigneeAddress query = new ConsigneeAddress();
		query.setIsDefault(true);
		query.setUserId(userId);
		query.setState(true);
		query = super.queryUniqueByObject(query);
		return decode(query);
	}
	
	@Override
	public ConsigneeAddress queryUniqueByParams(Map<String,Object> params){
		return decode(super.queryUniqueByParams(params));
	}
	private ConsigneeAddress encode(ConsigneeAddress consigneeAddress){
		if(StringUtil.isNotBlank(consigneeAddress.getIdentityCard())){
			consigneeAddress.setIdentityCard(Base64.encode(consigneeAddress.getIdentityCard().getBytes()));
		}
		return consigneeAddress;
	}
	
	
	private Map<String,Object> encode(Map<String,Object> param){
		if(StringUtil.isNotBlank(param.get("identityCard"))){
			param.put("identityCard", Base64.encode(param.get("identityCard").toString().getBytes()));
		}
		return param;
	}
	
	private List<ConsigneeAddress> decode(List<ConsigneeAddress> consigneeAddressList){
		if(CollectionUtils.isNotEmpty(consigneeAddressList)){
			consigneeAddressList.forEach(new Consumer<ConsigneeAddress>(){
				@Override
				public void accept(ConsigneeAddress consigneeAddress) {
					decode(consigneeAddress);
				}
			});
		}
		return consigneeAddressList;
	}
	private ConsigneeAddress decode(ConsigneeAddress consigneeAddress){
		if(consigneeAddress == null || StringUtil.isBlank(consigneeAddress.getIdentityCard())) return consigneeAddress;
		consigneeAddress.setIdentityCard(new String(Base64.decode(consigneeAddress.getIdentityCard())));
		return consigneeAddress;
	}
}
