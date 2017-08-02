package com.tp.service.ord.local;

import java.util.LinkedList;

import com.tp.model.mem.ConsigneeAddress;

/**
 * 
 * <pre>
 * 收货人业务接口
 * </pre>
 * 
 * @author szy
 * @version 0.0.1
 *          Exp $
 */
public interface IConsigneeLocalService {

	/**
	 * 
	 * <pre>
	 * 获取收货人信息列表
	 * </pre>
	 * 
	 * @param memberId
	 * @return 收货人列表
	 */
	LinkedList<ConsigneeAddress> getConsigneeInfoList(Long memberId);

	/**
	 * 
	 * <pre>
	 * 保存用户联系人信息
	 * </pre>
	 * 
	 * @param consigneeDTO
	 * @return 收货人ID
	 */
	Long saveConsigneeInfo(ConsigneeAddress consigneeDTO);

	/**
	 * 删除收货人
	 * 
	 * @param memberId
	 *            用户会员ID
	 * @param consigneeId
	 *            收货人ID
	 * @return 是否删除成功
	 */
	boolean deleteConsigneeInfo(Long memberId, Long consigneeId);

	/**
	 * 
	 * <pre>
	 * 设置默认收货人
	 * </pre>
	 * 
	 * @param memberId
	 *            用户会员ID
	 * @param consigneeId
	 *            收货人ID
	 * @return 是否设置成功
	 */
	boolean setDefaultConsignee(Long memberId, Long consigneeId);
}
