package com.tp.common.vo.wx;


/**
 * 微信消息常量类
 * @author zhuss
 */
public interface MessageConstant {
	
	//场景
	public enum SCENE{
		SUBSCRIBE("subscribe", "关注事件"),
		//UNSUBSCRIBE("unsubscribe", "取消关注"), 
		SCAN("scan", "用户已关注扫码"), 
		CLICK("click", "点击菜单拉取消息"),
		OFFLINE("offline", "线下活动兑换码"),
		KEYWORD("keyword", "关键字回复");
		//VIEW("viem", "点击菜单跳转链接"), 
		//LOCATION("location", "ANDROID");
		private String code;
		private String desc;

		SCENE(String code, String desc) {
			this.code = code;
			this.desc = desc;
		}
		
		public static String getDesc(String code){
			for(SCENE entry:SCENE.values()){
				if(entry.code.equalsIgnoreCase(code)){
					return entry.desc;
				}
			}
			return null;
		}
		
		public String getCode() {
			return code;
		}

		public String getDesc() {
			return desc;
		}
	}
	
	// 状态
	public enum STATUS {
		NO(0, "冻结"), YES(1, "发布");
		private int code;
		private String desc;

		STATUS(int code, String desc) {
			this.code = code;
			this.desc = desc;
		}

		public static String getDesc(Integer code) {
			for (STATUS entry : STATUS.values()) {
				if (entry.code == code) {
					return entry.desc;
				}
			}
			return null;
		}

		public int getCode() {
			return code;
		}

		public String getDesc() {
			return desc;
		}
	}
	
	// 类型
	public enum TYPE {
		TEXT("text", "文本消息");
		/*IMAGE("image", "图片消息"),
		VOICE("voice", "音频消息"),
		VIDEO("video", "视频消息"),
		LOCATION("location", "地理位置"),
		MUSIC("music", "音乐消息"),
		NEWS("news", "图文消息");*/
		private String code;
		private String desc;

		TYPE(String code, String desc) {
			this.code = code;
			this.desc = desc;
		}
		
		public static String getDesc(String code) {
			for (TYPE entry : TYPE.values()) {
				if (entry.code.equalsIgnoreCase(code) ) {
					return entry.desc;
				}
			}
			return null;
		}

		public String getCode() {
			return code;
		}

		public String getDesc() {
			return desc;
		}
	}
}
