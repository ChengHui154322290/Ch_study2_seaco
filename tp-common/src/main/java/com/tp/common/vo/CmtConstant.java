/**
 * 
 */
package com.tp.common.vo;

/**
 * @author szy
 *
 */
public class CmtConstant {
	public interface ItemReviewConstant {
		/** 是否审核通过 */
		public interface ISCHECK {
			Integer UNCHECK = 0; 	/** 未审核 */
			Integer CHECKING = 1;	/** 审核中 */
			Integer CHECKED = 2;	/** 已审核 */
			Integer REJECTED=3;		/** 已驳回 */
		}
		
		/** 是否删除 */
		public interface ISDELETE {
			Integer DELETED = 1;/** 删除 */
			Integer UNDELETE = 0;/** 未删除 */
		}
		
		/** 是否匿名 */
		public interface ISANONYMOUS {
			Integer ANONYMOUS = 1; /** 匿名 */
			Integer NOT_ANONYMOUS = 0;/** 非匿名 */
		}
		
		/** 状态 */
		public interface STATUS {
			Integer ZERO = 0;
			Integer ONE = 1;
		}
		
		/** 隐藏 */
		public interface HIDE {
			Integer SELF = 0;	/** 仅自己可见*/
			Integer HIDED = 1;  /** 隐藏 */
			Integer SHOW = 2;   /** 显示*/
		}
		public interface TOP{
			Integer BOTTOM = 0; 		/** 置底*/
			Integer TOP = 2;			/** 置顶*/
			Integer UNLIMITED = 1;		/** 不限*/
		}
	}
}
