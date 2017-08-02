package com.tp.common.vo.app;

/**
 * @author theodore
 *
 */
public class GetuiConstant {
	
	/**
	 * 目标用户
	 */
	public static class TargetUser{
		
		public static final int SINGLE		= 11;		//单个用户
		
		public static final int ALL 		= 0;		//全部
		
		public static final int BIAOQIAN 	= 1;		//标签用户
		
		public static final int SPECIAL 	= 2;		//特定用户
		
		public static final int GROUP 		= 3;		//用户分组
	}
	
	/**
	 * 后续动作
	 */
	public static class Successor{
		
		public static final int TRANS 		= 0;		//透传
		
		public static final int START 		= 2;		//启动应用
		
		public static final int OPEN 		= 1;		//打开链接
		
		public static final int DOWNLOAN 	= 6;		//下载应用
	}

	/**
	 * 透传类型-收到消息是否立即启动
	 */
	public static class TransmissionType{
		
		public static final int IMMEDIATELY = 1;		//立即启动
		
		public static final int WAIT		= 2;		//等待客户端启动
	}
	
	/**
	 * ios参数类型
	 */
	public static class IOSParamType{
		
		public static final String STRING 		= "string";
		
		public static final String DICTIONARY	= "dictionary";
		
		public static final String REMOTE 		= "remote";
	}
	
	/**
	 * 消息模版类型
	 *
	 */
	public static class TemplateType{
		
		public static final String TRANS = "trans";
		
		public static final String NOTE = "note";
		
		public static final String LINK = "link";
		
		public static final String NOTYPOPLOAD = "notyPopLoad";
	}
}
