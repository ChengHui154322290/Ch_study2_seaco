package com.tp.common.vo.mem;
import com.tp.util.StringUtil;

/**
 * 
 * <pre>
 *  积分类型场景枚举
 * </pre>
 *
 * @author szy
 * @version 0.0.1
 */
public enum SceneCode {
	
	/** 登录积分*/
	LOGIN_SCENE("0", "登录积分"),
	
	/** 注册积分*/
	REGISTER_SCENE("1", "注册积分"),
	
	/** 下单积分*/
	ORDER_SCENE("2", "下单积分"),
	
	/** 活动积分*/
	ACTIVITY_SCENE("3", "活动积分"),
	
	/** 签到积分*/
	SIGN_SCENE("4", "签到积分"),
	
	/** 完善个人信息*/
	MY_INFO_SCENE("5", "完善个人信息"),
	
	/** 新增宝宝信息*/
	MY_BABY_SCENE("6", "新增宝宝信息");
	
	/** 通用错误码 **/
	public String code;
	
	/** 错误描述 */
    public String desc;
    

	private SceneCode(String code, String desc) {
		this.code = code;
		this.desc = desc;
	}

	public static String getDesc(String code){
		if(StringUtil.isNullOrEmpty(code)) return null;
		for (SceneCode c : SceneCode.values()) {
            if (c.code.equals(code)) {
                return c.desc;
            }
        }
		return null;
	}
	
}
