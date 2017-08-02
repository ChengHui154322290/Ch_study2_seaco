package com.tp.common.vo.mmp;;

 /**
 * 日志记录 常量
 * @author szy
 */
public interface TopicAuditLogConstant{

	/** 状态 - 编辑中  */
	final String STATUS_EDITING = "编辑中";
	
	/** 状态 - 审批中  */
	final String STATUS_AUDITING = "审批中";
	
	/** 状态 - 已取消  */
	final String STATUS_CANCELED = "已取消";
	
	/** 状态 - 审核通过  */
	final String STATUS_AUDITED = "审核通过";
	
	/** 状态 - 已驳回  */
	final String STATUS_REFUSED = "已驳回";
	
	/** 状态 - 终止  */
	final String STATUS_TERMINATION = "终止";

	/** 状态 - 编辑中 */
	final String STATUS_EDITING_VALUE = "编辑";

	/** 状态 - 审批中 */
	final String STATUS_AUDITING_VALUE = "已提交";

	/** 状态 - 已取消 */
	final String STATUS_CANCELED_VALUE = "已取消";

	/** 状态 - 审核通过 */
	final String STATUS_AUDITED_VALUE = "已审批";

	/** 状态 - 已驳回 */
	final String STATUS_REFUSED_VALUE = "已驳回";

	/** 状态 - 终止 */
	final String STATUS_TERMINATION_VALUE = "已终止";

}
