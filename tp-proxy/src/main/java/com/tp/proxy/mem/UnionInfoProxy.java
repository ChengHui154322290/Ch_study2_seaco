package com.tp.proxy.mem;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.vo.mem.MemberUnionType;
import com.tp.model.mem.UnionInfo;
import com.tp.proxy.BaseProxy;
import com.tp.service.IBaseService;
import com.tp.service.mem.IUnionInfoService;
/**
 * 代理层
 * @author szy
 *
 */
@Service
public class UnionInfoProxy extends BaseProxy<UnionInfo>{

	@Autowired
	private IUnionInfoService unionInfoService;

	@Override
	public IBaseService<UnionInfo> getService() {
		return unionInfoService;
	}
	
	public UnionInfo getUnionInfo(Long memberId, MemberUnionType type){
		UnionInfo query = new UnionInfo();
		query.setMemId(memberId);
		query.setType(type.code);
		return unionInfoService.queryUniqueByObject(query);
	}
	
	/**
	 * 解绑联合登录
	 * @param unionVal
	 * @param unionType
	 * @return
	 */
	public boolean unbindUnionLogin(String unionVal, MemberUnionType unionType){
		UnionInfo query = new UnionInfo();
		query.setUnionVal(unionVal);
		query.setType(unionType.code);
		
		UnionInfo unionInfo = unionInfoService.queryUniqueByObject(query);
		if(unionInfo != null) {
			if(!unionInfo.getIsDeleted()) {
				unionInfo.setIsDeleted(true);
				try {
					unionInfoService.updateNotNullById(unionInfo);
				} catch (Exception e) {
					logger.error(e.getMessage(), e);
					return false;
				}
			}
		}
		else {
			return false;
		}
		
		return true;
	}
}
