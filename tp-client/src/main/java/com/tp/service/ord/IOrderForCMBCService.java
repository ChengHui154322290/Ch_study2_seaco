package com.tp.service.ord;

import java.util.List;
import java.util.Map;

import com.tp.dto.cmbc.MemberCMBCDto;
import com.tp.model.ord.SubOrder;

public interface IOrderForCMBCService {
	
	
	List<SubOrder> getSubOrderByTime( Map<String, Object> params);
	List<SubOrder> pushSubOrderToCMBC(  List<SubOrder> suborderList);	
	boolean pushNewMemberToCMBC(  MemberCMBCDto member  );
}
