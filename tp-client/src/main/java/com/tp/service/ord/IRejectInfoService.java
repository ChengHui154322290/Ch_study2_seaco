package com.tp.service.ord;

import java.util.List;

import com.tp.common.vo.PageInfo;
import com.tp.dto.common.ResultInfo;
import com.tp.model.ord.RejectInfo;
import com.tp.query.ord.RejectQuery;
import com.tp.service.IBaseService;
/**
  * @author szy
  * 退货单接口
  */
public interface IRejectInfoService extends IBaseService<RejectInfo>{

	public RejectInfo queryRejectByRefundNo(Long refundNo);
	void refundResult(Long refundNo, Boolean isSuccess);
	public void updateForAudit(RejectInfo rejectInfo);
	
	public void updateForForceAudit(RejectInfo rejectInfo);
	List<RejectInfo> queryNotSuccessPostKuaidi100List(RejectInfo rejectInfo);
	Integer batchUpdatePostKuaidi100(List<RejectInfo> rejectInfoDOList);
	Integer updatePostKuaidi100(RejectInfo rejectInfo);
	List<RejectInfo> selectListByRejectNoAndPackageNo(Long rejectNo,String packageNo);
	PageInfo<RejectInfo> queryPageListByRejectQuery(RejectQuery rejectQuery);
	void updateForSellerAudit(RejectInfo rejectInfo, Integer operatorType,String createUser);
	RejectInfo queryRejectInfoByNo(Long rejectNo);
	RejectInfo queryRejectItemByRejectId(Long rejectId);
	List<RejectInfo> queryRejectInfoListByRefundNoList(List<Long> refundNos);
	public ResultInfo<Boolean> validRejectInfo(RejectInfo rejectInfo);

}
