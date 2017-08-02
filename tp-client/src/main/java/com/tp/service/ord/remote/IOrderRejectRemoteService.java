package com.tp.service.ord.remote;

import com.tp.common.vo.PageInfo;
import com.tp.dto.ord.remote.RejectDetailDTO;
import com.tp.model.mem.MemberInfo;
import com.tp.model.ord.RejectInfo;
import com.tp.model.ord.RejectItem;
import com.tp.query.ord.RejectQuery;


/**
 * 退货远程服务
 * 
 * @author szy
 * @version 0.0.1
 */
public interface IOrderRejectRemoteService {
	
	/**
	 * @return 保存退货单及退货商品信息
	 */
	void applyReturnGoods(MemberInfo user, RejectInfo rejectInfo,RejectItem rejectItem);

	/**
	 * 查看退货历史记录
	 */
	RejectDetailDTO showRejectHistory(RejectItem rejectItem);
	
	/**
	 * 取消退货
	 * @return 
	 */
	void cancelRejectInfo(Long rejectId, MemberInfo user );
	
	/**
	 * 更新问题表述
	 */
	int updateBuyerRemarks(RejectInfo rejectInfoDO,MemberInfo user);
	
	/**
	 * 修改售后信息
	 * @param user
	 * @param rejectInfo
	 * @param rejectItem
	 */	
	void updateRejctData(MemberInfo user, RejectInfo rejectInfo,RejectItem rejectItem);

	/**
	 * 动态查询列表详情
	 */
	PageInfo<RejectInfo> queryMobilePageListByRejectQuery(RejectQuery rejectQuery);
	
	/**
	 * 保存快递单号
	 */
	int saveExpressNo(RejectInfo rejectInfo);
	
	/**
	 * 根据退货编号查询退货信息
	 * 
	 * @param rejectNo
	 * @return
	 */
	RejectInfo queryRejectDetailByNo(Long rejectNo);
	
	/**
	 * 动态查询退货列表详情，类似订单列表格式，暂时还没用到
	 */
	
	PageInfo<RejectInfo> queryPageList(RejectQuery rejectQuery);
	
	RejectDetailDTO showRejectHistoryForMemberId(RejectItem rejectItem,Long memberId);
	
	void saveExpressNoForMemberId(RejectInfo rejectInfoDTO2,Long memberId);
	
}
