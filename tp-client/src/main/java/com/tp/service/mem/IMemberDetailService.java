package com.tp.service.mem;

import com.tp.model.mem.MemberDetail;
import com.tp.service.IBaseService;
/**
  * @author szy
  * 接口
  */
public interface IMemberDetailService extends IBaseService<MemberDetail>{

	MemberDetail selectByUid(Long memberId);

	MemberDetail doAuthencation(MemberDetail userDetail, String[] imageUrls);

	MemberDetail doRefreshAuthencation(MemberDetail userDetail, String picA,
			String picB);

}
